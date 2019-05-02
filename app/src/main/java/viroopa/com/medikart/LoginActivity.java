/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package viroopa.com.medikart;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.MedicineReminder.services.MR_GetMedfriedDataService;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.downloadservice.DownloadBPDmWmOnLoginService;
import viroopa.com.medikart.downloadservice.DownloadDoctorService;
import viroopa.com.medikart.downloadservice.DownloadMemberPhotoService;
import viroopa.com.medikart.downloadservice.DownloadMemberService;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SessionManager;


public class LoginActivity extends Activity {

    private SQLiteHandler db;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,100}" + "@"
            + "[a-zA-Z0-9][a-zA-Z0-9-]{0,10}" + "(" + "."
            + "[a-zA-Z0-9][a-zA-Z0-9-]{0,20}" +
            ")+");

    //private static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9]{1,250}");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9+_.]{4,16}");

    // LogCat tag
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private TextView txtRegister,forgetpassword;

    private String suseremailid ;
    private String spassword;
    private String sphoneno;
    private String sfname;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        TextView txtRegister = (TextView) findViewById(R.id.txtRegister);
        TextView forgetpassword = (TextView) findViewById(R.id.txtforgetpassword);

        txtRegister.setText(Html.fromHtml("Don't have an Account yet? <font color='#df6301'>Sign up</font> here"));

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);

        getregistration();

        // Session manager
        session = new SessionManager(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            showmainactivity();
        }

        inputEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //Validation.isEmailAddress(inputEmail, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });


        inputPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
                if(inputEmail.getText().toString().isEmpty())
                {
                    inputEmail.setError("please enter username.");
                    inputEmail.isFocused();

                }
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                // Check for empty data in the form
                if (email.trim().length() > 0 && password.trim().length() > 0) {

                    checkLogin(email, password);
                } else {
                    //Toast.makeText(getApplicationContext(),"Please enter the credentials!", Toast.LENGTH_LONG).show();
                    f_alert_ok("Unable to Log-In", "Please Enter the Credentials.");
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showregistrationactivity();
            }
        });

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showforgetpassword();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    private void showmainactivity(){
        // User is already logged in. Take him to main activity
        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showregistrationactivity(){
        Intent Intent_reg = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(Intent_reg);

    }

    public String getMobileNumber()
    {
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String strMobileNumber = telephonyManager.getLine1Number();

        //String number = getMobileNumber();
        Toast.makeText(getApplicationContext(), strMobileNumber, Toast.LENGTH_SHORT).show();

        return strMobileNumber;
    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {

        showPdialog("Loading. . .");
        String url = String.format(AppConfig.URL_GET_LOGIN,email,password);

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        String tag_string_req = "req_login";

        JsonObjectRequest get_login_req = new JsonObjectRequest(Method.GET,url,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hidePDialog();

               // Log.d(TAG, "Register Response: " + response.toString());

                if (response.optBoolean("IsValid") == false)
                {
                    f_alert_ok("Unable to Log-In",response.optString("Message"));
                    hidePDialog();
                }
                else
                {
                    session.setLogin(true);

                    if (db.getRowCount()== 0)
                    {
                         db.addUser(response.optString("UserName"), "",
                                   response.optString("PinCode"), response.optString("MobileNo"),
                                   response.optString("EmailID"),response.optString("gender"),
                                   response.optInt("CityID"), "", "",
                                   response.optString("UserID"),


                                 response.optString("ImageType"),
                                 response.optString("ImageName"),
                                 response.optString("Address"));
                    }


                     String sImageName= response.optString("ImageName");
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("memberid", response.optString("UserID"));
                    editor.putString("UserName", response.optString("UserName"));
                    editor.putString("User_Name", response.optString("UserName"));
                    editor.putString("ImageName", response.optString("ImageName"));
                    editor.putString("FolderPath", response.optString("FolderPath"));
                    editor.putString("imagepath", response.optString("ImageUrl"));
                    editor.putString("imagename", response.optString("ImageName"));
                    editor.putString("ProfilePicName", response.optString("ImageName"));
                    editor.putString("imageStreamName", response.optString("ImageStream"));
                    editor.commit();


                    get_cart_count(response.optString("UserID"));

                    downloadMemberPhoto();
                    downloaddatafromserver();
                    downloadmemberdatafromserver();
                    downloadBPWMDMservices( response.optString("UserID"));


                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                Log.e("HttpClient", "error: " + error.toString());
                //Toast.makeText(getApplicationContext(),"Server error", Toast.LENGTH_LONG).show();
                f_alert_ok("Unable to Log-In", "Server error");
            }
        });

        get_login_req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        queue.add(get_login_req);
    }


    private boolean CheckEmail_pattern(String emailid) {
        return EMAIL_PATTERN.matcher(emailid).matches();
    }

    private boolean CheckPassword_pattern(String password) {

        return PASSWORD_PATTERN.matcher(password).matches();
    }



    private void getregistration() {
        Intent intent_login = getIntent();
        suseremailid = intent_login.getStringExtra("useremailid");
        spassword = intent_login.getStringExtra("password");
        sphoneno = intent_login.getStringExtra("phone");
        sfname = intent_login.getStringExtra("fname");
    }



    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            //pDialog = null;
        }
    }

    private void showPdialog(String sMessage) {
        if (pDialog != null) {
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }





    private void showforgetpassword(){

        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.forget_password, null);
        builder.setView(dialogview);
        final Dialog dlg=builder.create();
        final EditText username=(EditText)dialogview.findViewById(R.id.txtoldpass);
        final Button send=(Button)dialogview.findViewById(R.id.btninvite);

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (username.getText().toString().isEmpty()) {
                    f_alert_ok("Unable to Log-In", "Please enter your username or email id");
                } else {
                    forgetpassword(username.getText().toString());
                    dlg.cancel();
                }
            }
        });
        dlg.show();
    }

    public void forgetpassword(String username)
    {
        showPdialog("Loading. . .");
        String url = String.format(AppConfig.URL_GET_FORGOT_PASSWORD, username);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest cityrequest = new JsonObjectRequest(Method.GET,url,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    hidePDialog();
                    onPasswordChangeSuccess(response);
                }
                catch (Exception e) {
                    hidePDialog();
                    e.printStackTrace();

                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                error.toString();
            }
        });

        cityrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(cityrequest);
    }

    private void onPasswordChangeSuccess(JSONObject response) throws JSONException {


        if (response.getString("bReturnFlag").equals("true")) {
           // f_alert_ok("Registration", response.getString("sReturnMsg"));
            f_alert_ok("Password reset", response.getString("sReturnMsg"));
        }
        else
        {
            f_alert_ok("Information",response.getString("sReturnMsg"));
        }
    }

    private void downloaddatafromserver(){

        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadDoctorService.class);
        startService(download_intent);

        /* End Download Service */
    }
    private void downloadMemberPhoto(){

        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadMemberPhotoService.class);
        startService(download_intent);

    }
    private void downloadmemberdatafromserver(){

        /* Starting Download Service */
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadMemberService.class);
        startService(download_intent);

        /* End Download Service */
    }

    private void downloadBPWMDMservices(String sMemberId){

        /* Starting Download Service */
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadBPDmWmOnLoginService.class);
       startService(download_intent);

        Intent download_intent_mr = new Intent(Intent.ACTION_SYNC, null, this, MR_GetMedfriedDataService.class);
        download_intent_mr.putExtra("member_id",sMemberId);
         startService(download_intent_mr);

        /* End Download Service */
    }


    private  void get_cart_count(String memberId)
    {
        final Integer results =  0;
        String tag_string_req = "req_cartCount";
        String url = String.format(AppConfig.URL_GET_CARTCOUNT_ONLOGIN, memberId);

        JsonObjectRequest FamilyMemberrequest = new JsonObjectRequest(Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Success_cartcount(response);
                         hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d(TAG, "Register Response: " + error.getMessage());
                        hidePDialog();
                        showmainactivity();
                        //Error_FamilyMember(error);
                    }
                });

        FamilyMemberrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(FamilyMemberrequest, tag_string_req);

    }

    private void Success_cartcount(JSONObject response) {

        for (int i = 0; i < response.length(); i++) {
            try {
                String nCartjson = response.optString("cartTotalModel");
                JSONObject json= new JSONObject(nCartjson);
                String cartCount=json.optString("CartTotal");
                String ScartId=json.optString("CartID");
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = pref.edit();
                prefsEditor.putString("addtocart_count", cartCount);
                prefsEditor.putString("cartid", ScartId);
                prefsEditor.commit();




            } catch (Exception e) {

            }
        }
        showmainactivity();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package viroopa.com.medikart;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.messagebox;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SessionManager;
import viroopa.com.medikart.model.citylist;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private  String IMEINUmber="99";

    private EditText et_inputFullName;
    private EditText et_inputLastName;
    private EditText et_inputEmail;
    private EditText et_inputPassword,et_cnfrmpassword;
    private EditText et_inputphoneno;
    private EditText et_inputPincode;
    ArrayAdapter<String> adapter;
    private Spinner myspinner;
    private TextView sp_city;
    private JSONArray ja_city;
    private Integer Selected_Position;

    int selected_cityid = -99;
    int selected_stateid = -99;


    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    JSONObject jsonobject;
    JSONArray jsonarray;
    //ArrayList<String> citylist;
    ArrayList<citylist > currentcity;

    private String fname ;
    private String lname ;
    private String email ;
    private String phone ;
    private String pincode;
    private String password;
    private  String confirmpassword;

    private String sMemberId;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayList<String> Cityid = new ArrayList<String>();



    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    Boolean lCheckEmailid = false ;
    Boolean lCheckmobile = false ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        GetIMEI();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        et_inputFullName = (EditText) findViewById(R.id.input_name);
        et_inputLastName = (EditText) findViewById(R.id.lastname);
        et_inputEmail = (EditText) findViewById(R.id.email);
        et_inputPassword = (EditText) findViewById(R.id.password);
        et_inputphoneno = (EditText) findViewById(R.id.phoneno);
        et_inputphoneno.setRawInputType(Configuration.KEYBOARD_12KEY);
        et_cnfrmpassword= (EditText) findViewById(R.id.confpwd);

        sp_city = (TextView) findViewById(R.id.txt_city);
        myspinner = (Spinner) findViewById(R.id.spinner);

        et_inputPincode = (EditText) findViewById(R.id.pincode);
        et_inputPincode.setRawInputType(Configuration.KEYBOARD_12KEY);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        //citylist = new ArrayList<String>();
        currentcity = new ArrayList<citylist>();

        getIntenet();

        //getcitylist();
        //load_city("");

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//            Intent intent = new Intent(RegisterActivity.this, BuyMainActivity.class);
            startActivity(intent);
            finish();
        }
        et_inputPincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(et_inputPincode.length()!=6) {
                        f_alert_ok("Pincode", "please enter proper pincode");
                    }

                }
            }
        });



        et_inputFullName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });


        et_inputEmail.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                //Validation.isEmailAddress(et_inputEmail, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        et_inputphoneno.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Validation.isPhoneNumber(et_inputphoneno, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        et_cnfrmpassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


//                if (et_inputPassword.getText().toString().equals(et_cnfrmpassword.getText().toString())) {
//
//
//                } else {
//                    et_cnfrmpassword.setError("password does not match");
//                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                String pwd = et_inputPassword.getText().toString();
//                if (pwd.isEmpty()) {
//                    et_inputPassword.setError("please enter password");
//                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        et_inputPincode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


                if (et_inputPincode.length() == 6) {
                    getcitylistWisePincode(et_inputPincode.getText().toString());
                } else {
                    //f_alert_ok("Pincode","please enter proper pincode");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                fname = et_inputFullName.getText().toString();
                lname = et_inputLastName.getText().toString();
                password = et_inputPassword.getText().toString();
                phone = et_inputphoneno.getText().toString();
                email = et_inputEmail.getText().toString();
                pincode = et_inputPincode.getText().toString();

                String ConfirmPWD=et_cnfrmpassword.getText().toString();

                String  Confirmpassword = et_cnfrmpassword .getText().toString();


                if (CheckEmail_pattern(et_inputEmail.getText().toString())) {
                    if (!fname.isEmpty()
                            && !email.isEmpty()
                            //  && Validation.isEmailAddress(et_inputEmail, true)
                            && !password.isEmpty()
                            && !phone.isEmpty()
                            // && Validation.isPhoneNumber(et_inputphoneno, true)
                            && !pincode.isEmpty()
                            // && et_inputphoneno.length()==10
                            && pincode.length() == 6
                            && selected_cityid > 0) {
                        if(password.equals(Confirmpassword)) {
                            if(et_inputphoneno.length()==10) {


                                if (password.trim().equals(ConfirmPWD.trim())) {


                                    if (password.trim().length()<6) {
                                        final messagebox mb = new messagebox();
                                        mb.showAlertDialog(RegisterActivity.this,"", " password should be minimum 6 characters..!", true);
                                    }
                                    else {
                                        CheckEmailid();
                                    }
                                } else {
                                    final messagebox mb = new messagebox();
                                    mb.showAlertDialog(RegisterActivity.this, "", "please enter confirm password same as  password..!", true);
                                }



                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Please enter valid phone number", Toast.LENGTH_LONG)
                                        .show();
                            }
//                            if ( lCheckEmailid.equals(true) )
//                            {
//                                CheckMobileNo();
//                                if ( lCheckmobile.equals(true) )
//                                {
//                                    registerUser();
//                                }
//                            }
                            //registerUser();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Password and confirm password must be same!", Toast.LENGTH_LONG)
                                    .show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG)
                                .show();
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email id!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        sp_city.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(sp_city.getText().toString().isEmpty() && et_inputPincode.getText().toString().isEmpty())
                {
                    f_alert_ok("City","Please enter pincode");
                }
                else if (et_inputPincode.length()>0 && sp_city.getText().toString().isEmpty())
                {
                    f_alert_ok("City","Please enter proper pincode");
                }
            }
        });

    }


    /**
     * Method to make json object request where json response starts wtih {
     */

    @Override
    public void onBackPressed() {

            exit_notification();
    }

    private void exit_notification()
    {
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure want to Exit")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void getcitylist() {
        showPdialog("get city ...");

        String tag_string_req = "req_city";

        final messagebox mb = new messagebox();
        String tag_city_list = "json_city";


        String uri = String.format( AppConfig.URL_GET_CITY, "", "0");

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        JsonObjectRequest cityrequest = new JsonObjectRequest(Method.GET,uri,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Register Response: " + response.toString());
                        hidePDialog();

                        Success_city(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Register Response: " + error.getMessage());
                        hidePDialog();

                        Error_city(error);
                    }
                });

        cityrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(cityrequest, tag_string_req);

        queue.add(cityrequest);

    }

    public String getMobileNumber()
    {
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String strMobileNumber = telephonyManager.getLine1Number();

        return strMobileNumber;
    }



    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * registerUser(name, lname,email, password,phone,pincode,city);
     */

    private void registerUser(final String name, final String lastname, final String email,
                              final String password, final String phone, final String pincode,
                              final int selected_cityid) {

        showPdialog("Register ...");

        //RequestQueue queue = Volley.newRequestQueue(this);
        String Post_registration = "Post_registration";

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("EMail", email);
        jsonParams.put("MemberFName", name);
        jsonParams.put("MemberLName", lastname);
        jsonParams.put("PinCode", pincode);
        jsonParams.put("MobileNo", phone);
        jsonParams.put("DistrictId", String.valueOf(selected_cityid));//String.valueOf(selected_cityid));
        jsonParams.put("Password", password);

        JSONObject jparams = new JSONObject(jsonParams);

        //String xyz = "{"EMail" :'prakashb37@gmail.com' , 'MobileNo':'987112345', 'Password' : 'a', 'MemberFName':'d', 'MemberLName':'b','PinCode':'456789', 'DistrictId':'6'}";
        //new JSONObject(jsonParams)
        //RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        JsonObjectRequest jsObjRequest_post = new JsonObjectRequest(
                //Method.POST,
                AppConfig.URL_POST_REGISTER,
                jparams,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidePDialog();
                        verificationSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        verificationFailed(error);
                    }
                });

        //AppController.getInstance().addToRequestQueue(jsObjRequest_post, Post_registration);

    }
    private void CheckMobileNo() {

        showPdialog("Loading. . .");
        String url = String.format(AppConfig.URL_GET_CHECKDUPLICATEMOBILENO,0,phone);

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        String tag_string_req = "req_login";

        JsonObjectRequest get_mobile = new JsonObjectRequest(Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidePDialog();

                        Log.d(TAG, "Register Response: " + response.toString());

                        if (response.optBoolean("bReturnFlag") == false)
                        {
                            f_alert_ok("Error",response.optString("Message"));
                            lCheckmobile = false ;
                        }
                        else
                        {
                            lCheckmobile = true ;
                            //registerUser();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lCheckmobile = false ;
                        hidePDialog();
                        Log.e("HttpClient", "error: " + error.toString());
                        //Toast.makeText(getApplicationContext(),"Server error", Toast.LENGTH_LONG).show();
                        f_alert_ok("Error", "Server error"+error.toString());
                    }
                });


        get_mobile.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        queue.add(get_mobile);

    }

    private void CheckEmailid() {

        showPdialog("Loading. . .");
//        String url = String.format(AppConfig.URL_GET_CHECKDUPLICATEEMAILID,0,email);
        String url = String.format(AppConfig.URL_GET_CHECKDUPLICATEEMAILIDMOBILENO,0,email,phone);

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        String tag_string_req = "req_login";

        RequestFuture<JSONObject> future=RequestFuture.newFuture();

        JsonObjectRequest get_login_req = new JsonObjectRequest(Method.GET,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hidePDialog();

                Log.d(TAG, "Register Response: " + response.toString());

                if (response.optBoolean("bReturnFlag") == false)
                {
                    f_alert_ok("Error",response.optString("sReturnMsg"));
                    // lCheckEmailid = false;
                }
                else
                {
                    registerUser();
                    // CheckMobileNo();
                    //lCheckEmailid = true;
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lCheckEmailid = false;
                        hidePDialog();
                        Log.e("HttpClient", "error: " + error.toString());
                        //Toast.makeText(getApplicationContext(),"Server error", Toast.LENGTH_LONG).show();
                        f_alert_ok("Error", "Server error");
                    }
                });


        get_login_req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        queue.add(get_login_req);

    }
    private void registerUser() {

        showPdialog("Register ...");

        //RequestQueue queue = Volley.newRequestQueue(this);
        String Post_registration = "Post_registration";

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("EMail", email);
        jsonParams.put("DistrictId", String.valueOf(selected_cityid));//String.valueOf(selected_cityid));
        jsonParams.put("MemberFName", fname);
        jsonParams.put("MemberLName", "");
        jsonParams.put("MobileNo", phone);
        jsonParams.put("Password", password);
        jsonParams.put("PinCode", pincode);
        jsonParams.put("RegisterFrom", "A");
        jsonParams.put("UUID", IMEINUmber);

        JSONObject jparams = new JSONObject(jsonParams);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                //Method.POST,
                AppConfig.URL_POST_REGISTER,
                jparams,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        hidePDialog();

                        //String a = response.getString("iMemberId");
                        verificationSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        verificationFailed(error);
                    }
                });

        //AppController.getInstance().addToRequestQueue(jsObjRequest_post, Post_registration);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);

    }

    private void verificationSuccess(JSONObject response) {
        try
        {
            if (response.getString("bReturnFlag").equals("true")) {

                sMemberId = response.getString("iMemberId");

                SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("memberid", sMemberId);
                editor.commit();

                db.addUser(fname, lname, pincode, phone, email, "", selected_cityid, "", "", sMemberId, "A", "avtar1","");

                hidePDialog();
                //final messagebox mb = new messagebox();
                //mb.showAlertDialog(RegisterActivity.this, "Registration", response.getString("sReturnMsg"), true);
                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("Success")
                        .setMessage(response.getString("sReturnMsg"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                session.setLogin(false);
                                showmainactivity();
                                // finish();

                            }
                        }).show();

                //f_alert_ok("Registration", response.getString("sReturnMsg"));
                //hidePDialog();

            }
            else
            {
                f_alert_ok("Information",response.getString("sReturnMsg"));
                hidePDialog();
            }
        }
        catch(JSONException e){
            Log.e("Error", e.getMessage());
            f_alert_ok("Error ", e.getMessage());
        }
    }

    private void showmainactivity(){
        // prakash k bhandary

        //Intent Intenet_main = new Intent(this, MainActivity.class);
        Intent Intenet_main = new Intent(this, MainActivity.class);
        session.setLogin(true);
        Intenet_main.putExtra("useremailid", email);
        Intenet_main.putExtra("password", password);
        Intenet_main.putExtra("mobileno", phone);
        Intenet_main.putExtra("firstname", fname);
        Intenet_main.putExtra("memberid", sMemberId);

        this.startActivity(Intenet_main);
        finish();
    }

    private void verificationFailed(VolleyError error) {
        String json = null;

        hidePDialog();

        NetworkResponse response = error.networkResponse;
        if (response != null && response.data != null) {
            switch (response.statusCode) {
                case 400:
                    json = new String(response.data);
                    json = trimMessage(json, "message");
                    if (json != null) displayMessage(json);
                    break;

            }
            //Additional cases
        }

        final messagebox mb = new messagebox();
        mb.showAlertDialog(RegisterActivity.this, "Registration",
                "Verification failed " + error.getMessage(), true);

    }

    public String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    //Somewhere that has access to a context
    public void displayMessage(String toastString) {
        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_LONG).show();
    }

    private void Success_city(JSONObject response) {
        final messagebox mb = new messagebox();
        try {
            //citylist.clear();

            jsonarray = response.getJSONArray("cityModel");
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                citylist citydetails = new citylist();

                citydetails.setCityId(jsonobject.optInt("CityId"));
                citydetails.setStateId(jsonobject.optInt("StateId"));
                citydetails.setCityDesc(jsonobject.optString("population"));
                citydetails.setCityName(jsonobject.optString("CityName"));
                citydetails.setCityName(jsonobject.optString("StateName"));

                currentcity.add(citydetails);

                String vcityname = jsonobject.getString("CityName");
                //citylist.add(vcityname);

            }

            ArrayAdapter<citylist> dataAdapter = new ArrayAdapter<citylist>(RegisterActivity.this,
                    android.R.layout.simple_spinner_item, currentcity);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //sp_city.setAdapter(dataAdapter);
            sp_city.setFocusableInTouchMode(true);

            myspinner.setAdapter(dataAdapter);

            hidePDialog();
            //mb.showAlertDialog(RegisterActivity.this, "done", "done", true);


        } catch (Exception e) {
            hidePDialog();
            Log.e("Error", e.getMessage());
            mb.showAlertDialog(RegisterActivity.this, "Error", e.getMessage(), true);
            e.printStackTrace();
        }

    }

    private void Error_city(VolleyError error) {

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(this,"TimeoutError", Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            //TODO
            Toast.makeText(this,"AuthFailureError", Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            //TODO
            Toast.makeText(this,"ServerError", Toast.LENGTH_LONG).show();
        } else if (error instanceof NetworkError) {
            //TODO
            Toast.makeText(this, "NetworkError", Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            //TODO
            Toast.makeText(this,"ParseError", Toast.LENGTH_LONG).show();
        }

        hidePDialog();
        final messagebox mb = new messagebox();
        mb.showAlertDialog(RegisterActivity.this, "error", "ErrorListener", true);

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
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void getIntenet()
    {
        try
        {
            //Intent intent_buymainactivity = getIntent();

            SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
            ja_city = new JSONArray(pref.getString("citylist", ""));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getcitylistWisePincode(String pincode) {
        showPdialog("please wait ...");

        String tag_string_req = "req_city";

        // final messagebox mb = new messagebox();
        //String tag_city_list = "json_city";


        String uri = String.format(AppConfig.URL_GET_CITY_ON_PINCODE, pincode);

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        JsonObjectRequest cityrequest = new JsonObjectRequest(Method.GET,uri,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Register Response: " + response.toString());


                        Success_CityOn_pincode(response);
                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Register Response: " + error.getMessage());
                        hidePDialog();

                        f_alert_ok("Error",error.toString());
                    }
                });

        cityrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(cityrequest, tag_string_req);

        queue.add(cityrequest);

    }









    private void Success_CityOn_pincode(JSONObject response) {
        final messagebox mb = new messagebox();
        try {
            //citylist.clear();

            String city_id = response.getString("CityId");
            String city_name = response.getString("CityName");
            if (Integer.parseInt(city_id)==0)
            {
                sp_city.setText("");
                f_alert_ok("pincode","pincode is not proper");
            }
            else {
                selected_cityid=Integer.parseInt(city_id);

                sp_city.setText(city_name);
            }



        } catch (Exception e) {
            hidePDialog();
            Log.e("Error", e.getMessage());
            mb.showAlertDialog(RegisterActivity.this, "Error", e.getMessage(), true);
            e.printStackTrace();
        }

    }
    private boolean CheckEmail_pattern(String emailid) {
        return EMAIL_PATTERN.matcher(emailid).matches();
    }

    private void GetIMEI()
    {
        TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEINUmber=mngr.getDeviceId();
    }
}


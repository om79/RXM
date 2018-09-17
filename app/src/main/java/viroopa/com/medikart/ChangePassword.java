package viroopa.com.medikart;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.helper.SessionManager;


public class ChangePassword extends AppCompatActivity {

    // private ImageButton btnbp;
    private EditText oldpassword, newpassword, confirmpassword;
    private Button confirmbtn, clearbtn;
    private String sMemberId;
    private SessionManager session;
    private RelativeLayout Main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        Main_layout=(RelativeLayout) findViewById(R.id.Main_layout);
        setupUI(Main_layout);
        session = new SessionManager(getApplicationContext());
        getIntenet();

        try {
            oldpassword = (EditText) findViewById(R.id.txtoldpass);
            newpassword = (EditText) findViewById(R.id.txtpass);
            confirmpassword = (EditText) findViewById(R.id.txtcompass);
            confirmbtn = (Button) findViewById(R.id.btnconfirm);
            clearbtn = (Button) findViewById(R.id.btnnocode);

            confirmbtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if(!newpassword.getText().toString().trim().isEmpty() && !confirmpassword.getText().toString().trim().isEmpty()&& !oldpassword.getText().toString().trim().isEmpty())
                    {
                        if(!oldpassword.getText().toString().trim().equals(newpassword.getText().toString().trim())) {


                            if(confirmpassword.getText().toString().trim().length()<6 && newpassword.getText().toString().trim().length()<6) {
                                Toast.makeText(getApplicationContext(),
                                        " password should be minimum 6 characters..!", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        else {
                                if (newpassword.getText().toString().trim().equals(confirmpassword.getText().toString().trim())) {
                                    change_password();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "please enter confirm password same as new password..!", Toast.LENGTH_SHORT)
                                            .show();
                                }

                            }

                        }else {
                            Toast.makeText(getApplicationContext(),
                                    "old password is same as new password..!", Toast.LENGTH_SHORT)
                                    .show();

                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "please enter password..!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
            clearbtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    oldpassword.setText("");
                    newpassword.setText("");
                    confirmpassword.setText("");

                }
            });
        }catch(Exception e)
        {e.toString();}


    }

    public void change_password() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("MemberId", sMemberId);
        params.put("OldPassword", oldpassword.getText().toString().trim());
        params.put("NewPassword", newpassword.getText().toString().trim());
       String password =newpassword.getText().toString().trim();
        String Confirmpassword =confirmpassword.getText().toString().trim();
        if(password.equals(Confirmpassword)) {


            RequestQueue queue = Volley.newRequestQueue(ChangePassword.this);

            JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConfig.URL_POST_CHANGE_PASSWORD,
                    new JSONObject(params),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            sucess_profile(response);
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error_profile(error);

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("charset", "utf-8");
                    headers.put("User-agent", "Buying");
                    return headers;
                }
            };

            jor_inhurry_post.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //AppController.getInstance().addToRequestQueue(jor_inhurry_post, Post_registration);

            queue.add(jor_inhurry_post);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Password and confirm  password  must be same!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void sucess_profile(JSONObject response) {


        try
        {
            if (response.getString("bReturnFlag").equals("true")) {
               /* new AlertDialogWrapper.Builder(ChangePassword.this)
                        .setTitle("Success")
                        .setMessage(response.getString("sReturnMsg"))
                        .show();*/
                Toast.makeText(getApplicationContext(),response.getString("sReturnMsg"), Toast.LENGTH_LONG).show();

                session.setLogin(false);
                showlogin();
                //f_alert_ok("Success", response.getString("sReturnMsg"));
            }
            else
            {
                f_alert_ok("Information",response.getString("sReturnMsg"));
            }
        }
        catch(JSONException e){

            Log.e("Error", e.getMessage());
            f_alert_ok("Error ", e.getMessage());
        }
       // final messagebox mb = new messagebox();
        //f_alert_ok("Password", "Password changed Successfully");

    }

    private void error_profile(VolleyError error) {
    }
    private void getIntenet()
    {
        Intent intent_buymainactivity = getIntent();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
    }

    private  void  showlogin()
    {
        Intent Intenet_Login = new Intent(ChangePassword.this, LoginActivity.class);
        Intenet_Login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(Intenet_Login);
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(ChangePassword.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //show_main();
                    }
                }).show();
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ChangePassword.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}







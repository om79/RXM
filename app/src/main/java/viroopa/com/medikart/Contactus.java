package viroopa.com.medikart;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.adapter.AD_adapterCombo;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.helper.SQLiteHandler;

public class Contactus extends AppCompatActivity
{
    private ProgressDialog pDialog;
    private JSONArray ja_state,ja_city;
    private String sMemberId;
    private Button btnsubmit;
    private LinearLayout main_layout;
    private TextView txtenquiry,txtMessage;
    private String District_id,State_id,User_name,user_email,ContactMobileNo,Reason_id;
    private SQLiteHandler db;
    HashMap<String, String> Udetails;
    AD_adapterCombo enquiry_options;
    ObjectItem[] ObjectItemEnquiry=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus);
        main_layout = (LinearLayout)findViewById(R.id.main_layout);
        setupUI(main_layout);

        db = new SQLiteHandler(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        btnsubmit=(Button)findViewById(R.id.btnsubmit);
        txtenquiry=(TextView)findViewById(R.id.txtenquiry);
        txtMessage=(TextView)findViewById(R.id.txtMessage);
        getIntenet();


        txtenquiry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                enquiry_reasons();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
              public void onClick(View view) {
                  String test = txtMessage.getText().toString();

                  if(Reason_id!=null)
                  {
                      if(!txtMessage.getText().toString().equals(""))
                      {
                          SendEnquiry();
                      }else{
                          Toast.makeText(getApplicationContext(), "please enter your message", Toast.LENGTH_LONG).show();
                      }
                  }else{
                      Toast.makeText(getApplicationContext(), "please select a enquiry reason", Toast.LENGTH_LONG).show();
                  }
              }
          });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(Contactus.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void getIntenet() {
        try
        {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
            sMemberId = pref.getString("memberid", "");
            get_user_details();

            ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
            test.add(db.getUserDetails());

            Udetails = test.get(0);

            if (!Udetails.isEmpty()) {
                user_email=Udetails.get("email");
            }
            if(user_email==null)
            {
                user_email="";
            }

              }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SendEnquiry() {



        Map<String, String> params = new HashMap<String, String>();
        params.put("ContactDistrict_Id", District_id);
        params.put("ContactStateId", State_id);
        params.put("ContactName", User_name);
        params.put("ContactEmailId", user_email);
        params.put("Remark", txtMessage.getText().toString());
        params.put("ContactMobileNo", ContactMobileNo);
        params.put("ContactReasonId", Reason_id);

        JSONObject jparams = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(Contactus.this);




        JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_SEND_INVITE,
                jparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        sucess_query(response);
                        hidePDialog();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error_enquiry(error);
                        hidePDialog();
                    }
                })
        {
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

    private void sucess_query(JSONObject response) {
        if (response.optString("bReturnFlag") == "true")
        {

            new AlertDialog.Builder(Contactus.this)
                    .setTitle("Success")
                    .setMessage("Your query was submitted successfully")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();

            // lCheckEmailid = false;
        }
        else {
        }

        }


    private void error_enquiry(VolleyError error) {

        f_alert_ok("error",  "error" + error.getMessage());

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
    private void get_user_details() {
        showPdialog("loading....");
        String url = String.format(AppConfig.URL_GET_USER_DATA,sMemberId);
        new contatusAsyncHttpTask().execute(url);
     /*   RequestQueue queue = Volley.newRequestQueue(Contactus.this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Success_user_details(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Error_user_details(error);
                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);*/
    }

    private void Success_user_details(JSONObject response) {
        try
        {
            hidePDialog();
            if(response!=null) {
                User_name = response.opt("MemberName").toString();
                ContactMobileNo = response.opt("MobileNo").toString();
                State_id = response.opt("StateId").toString();
                District_id = response.opt("DistricId").toString();
            }

        } catch (Exception e) {
            hidePDialog();
            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Error_user_details(VolleyError error) {
        try {
            hidePDialog();
        } catch (Exception e) {

            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void enquiry_reasons()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);
        final EditText inputSearch = (EditText) dialogview.findViewById(R.id.inputSearch);
        builder.setView(dialogview);
        // Adding items to listview

        objectItemPrescriptionForLoad();
        enquiry_options = new AD_adapterCombo(this, R.layout.list_item,ObjectItemEnquiry);
        lv.setAdapter(enquiry_options);

        final TextView Titile =(TextView)dialogview.findViewById(R.id.textView);
        Titile.setText("Select");
        final TextView btncancel=(TextView)dialogview.findViewById(R.id.btncancel);
        final Dialog dlg= builder.create();
        lv.setOnItemClickListener(new OnItemClickListenerListViewItempatientfor(dlg));

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String text = cs.toString().toLowerCase(Locale.getDefault());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });




        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dlg.cancel();
            }
        });

        dlg.show();
    }


    public class OnItemClickListenerListViewItempatientfor implements AdapterView.OnItemClickListener {
        Dialog d=null;
        OnItemClickListenerListViewItempatientfor(Dialog dlg)
        {
            d=dlg;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Context context = view.getContext();


            TextView textViewItem = ((TextView) view.findViewById(R.id.product_name));
            String listItemText = textViewItem.getText().toString();
            String listItemId = textViewItem.getTag().toString();
            Reason_id=listItemId;
            txtenquiry.setText(listItemText);

            d.cancel();

        }
    }
    private void objectItemPrescriptionForLoad()
    {

        ObjectItemEnquiry = new ObjectItem[4];

        ObjectItemEnquiry[0] = new ObjectItem(1,"Disease");
        ObjectItemEnquiry[1] = new ObjectItem(2,"General Store");
        ObjectItemEnquiry[2] = new ObjectItem(3,"Other");
        ObjectItemEnquiry[3] = new ObjectItem(4,"Product not Availabe");

    }
    public class contatusAsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

        }



        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                String Myresponse = urlConnection.getResponseMessage();

                /* 200 represents HTTP OK */
                if (statusCode == 200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject x = new JSONObject(response.toString());


                    Success_user_details(x);

                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {

            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {


            hidePDialog();

            if (result == 1) {


            } else {


            }
        }
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
                    hideSoftKeyboard(Contactus.this);
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

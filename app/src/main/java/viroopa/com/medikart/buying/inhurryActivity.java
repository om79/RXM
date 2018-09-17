package viroopa.com.medikart.buying;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.ObjectItem;
import viroopa.com.medikart.R;
import viroopa.com.medikart.adapter.AD_adapterCombo;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.common.BadgeDrawable;
import viroopa.com.medikart.helper.SQLiteHandler;

public class inhurryActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private String sMemberId;

    private EditText input_name;
    private EditText input_mobile;
    private EditText input_email;
    private EditText input_message;
    private Button input_besttime;
    AppController globalVariable;
    private Button btnSubmit;
    private ImageView date_button, time_button;
    private CheckBox input_emergency;
    private String emergencydate, emergencytime;
    AD_adapterCombo besttimeadapter;

    private SQLiteHandler db;
    private LinearLayout main_layout;

    private ProgressDialog pDialog;

    private Menu mToolbarMenu;
    Integer nAddtocart_count = 0;
    ObjectItem[] ObjectItemBestTime=null;
    private Integer timeslot=17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.inhurry);
        main_layout=(LinearLayout) findViewById(R.id.main_layout);
        setupUI(main_layout);
        globalVariable = (AppController) getApplicationContext();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        pDialog = new ProgressDialog(this);

        getIntenet();

        input_name = (EditText) findViewById(R.id.input_name);
        input_email = (EditText) findViewById(R.id.email);
        input_mobile = (EditText) findViewById(R.id.mobile);
        input_message = (EditText) findViewById(R.id.message);
        input_besttime = (Button) findViewById(R.id.bestime);

        input_emergency = (CheckBox) findViewById(R.id.chk_emergency);
        btnSubmit = (Button) findViewById(R.id.btnsubmit);
        time_button = (ImageView) findViewById(R.id.time_button);
        date_button = (ImageView) findViewById(R.id.date_button);

        db = new SQLiteHandler(getApplicationContext());
        objectItemBestTimeLoad();

        ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
        test.add(db.getUserDetails());

        HashMap<String, String> m = test.get(0);

        input_name.setText(m.get("name"));
        input_email.setText(m.get("email"));
        input_mobile.setText(m.get("phoneno"));

/*        input_name.setEnabled(false);
        input_email.setEnabled(false);
        input_mobile.setEnabled(false);*/

        input_name.setFocusable(false);
        input_name.setClickable(true);
        input_email.setFocusable(false);
        input_email.setClickable(true);
/*        input_mobile.setFocusable(false);
        input_mobile.setClickable(true);*/

       /* String[] ITEMS = {"As soon as possible","8am - 12 noon", "12pm - 3pm", "3pm - 6pm", "6pm - 8pm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_besttime.setAdapter(adapter);*/

        Date current_date = Calendar.getInstance().getTime();
        //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        emergencydate = formatter.format(current_date);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                input_mobile = (EditText) findViewById(R.id.mobile);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    if (!input_mobile.getText().toString().isEmpty()) {
                        if (input_mobile.getText().toString().length() == 10) {
                            Postinhurrydata("", "");

                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter valid phone number", Toast.LENGTH_LONG)
                                    .show();

                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please enter phone number", Toast.LENGTH_LONG)
                                .show();
                    }


            }
        });

        input_emergency.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (input_emergency.isChecked()) {
                } else {
                    emergencydate = "";
                    emergencytime = "";
                }
            }
        });

        input_besttime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
               best_time_for_call();
            }
        });

        // Show a timepicker when the timeButton is clicked
        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        inhurryActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );

                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        // Show a datepicker when the dateButton is clicked
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        inhurryActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }


    private void Postinhurrydata(String email, final String password) {


                    showPdialog("Loading. . .");

                    input_name = (EditText) findViewById(R.id.input_name);
                    input_mobile = (EditText) findViewById(R.id.mobile);
                    input_email = (EditText) findViewById(R.id.email);
                    input_message = (EditText) findViewById(R.id.message);
                   // input_besttime = (Spinner) findViewById(R.id.bestime);
                    input_emergency = (CheckBox) findViewById(R.id.chk_emergency);

                    btnSubmit = (Button) findViewById(R.id.btnsubmit);

                    time_button = (ImageView) findViewById(R.id.time_button);
                    date_button = (ImageView) findViewById(R.id.date_button);


                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("FullName", input_name.getText().toString());
                    params.put("EmailAddress", input_email.getText().toString());
                    params.put("PhoneNo", input_mobile.getText().toString());
                    params.put("AlternatePhoneNo", input_mobile.getText().toString());
                    params.put("OTP", "123456");
                    params.put("other", "other");

                    params.put("IsEmergencyDelivery", "true");
                    params.put("ExpectedDeliveryDate", emergencydate);
                    params.put("IsViaWhatsApp", "false");
                    params.put("Comment", input_message.getText().toString());
                    params.put("BestTimeForCall", timeslot);

                    JSONObject jparams = new JSONObject(params);

                    RequestQueue queue = Volley.newRequestQueue(inhurryActivity.this);

                    JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                            Request.Method.POST,
                            AppConfig.URL_POST_SUBMITDATA,
                            //new JSONObject(params),
                            jparams,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    sucess_inhurry(response);
                                    hidePDialog();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    hidePDialog();
                                    error_inhurry(error);
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

    private void sucess_inhurry(JSONObject response) {
        try
        {
            if (response.getString("bReturnFlag").equals("true")) {
                new AlertDialog.Builder(inhurryActivity.this)
                        .setTitle("Success")
                        .setMessage(response.getString("sReturnMsg"))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
                //f_alert_ok("Success", response.getString("sReturnMsg"));
            }
            else
            {
                f_alert_ok("Information",response.getString("sReturnMsg"));
            }
        }
        catch(JSONException e){
            hidePDialog();
            Log.e("Error", e.getMessage());
            f_alert_ok("Error ", e.getMessage());
        }
    }

    private void error_inhurry(VolleyError error) {

        /*
        NetworkResponse response = error.networkResponse;
        if (response != null && response.data != null) {
            switch (response.statusCode) {
                case 400:
                    json = new String(response.data);

                    final messagebox mb = new messagebox();
                    mb.showAlertDialog(this, "Registration", "verification Success", true);

            }
            //Additional cases
        }
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));

        createCartBadge(nAddtocart_count);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shoppingcart, menu);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cart) {
            //get_cartlist();
            if (nAddtocart_count > 0)
            {
                get_checkout();
            }
            else
            {
                f_alert_ok("Information","Your cart is empty");
            }
            return true;
        }if (id == R.id.order_transaction) {
            Show_Order_Transaction();

        }else  if (id == R.id.return_cancel_policies) {
            Show_Return_cancel_policies();

        }else  if (id == R.id.termscondition) {
            Show_termscondition();
        }

        return super.onOptionsItemSelected(item);
    }

    public void Show_Order_Transaction() {
       // Intent Intenet_change = new Intent(inhurryActivity.this, Order_Transaction.class);
       // startActivity(Intenet_change);
    }
    public void Show_Return_cancel_policies() {
      //  Intent Intenet_change = new Intent(inhurryActivity.this, TermsCondition.class);
      //  startActivity(Intenet_change);
    }
    public void Show_termscondition() {
       // Intent Intenet_change = new Intent(inhurryActivity.this, TermsCondition.class);
       // startActivity(Intenet_change);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        emergencytime = hourString + ":" + minuteString;
        //input_besttime.setText(emergencytime);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        emergencydate =  dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        //emergencydate = "You picked the following date: " + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        //dateTextView.setText(date);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            //pDialog = null;
        }
    }

    private void showPdialog(String sMessage) {
        if (pDialog != null) {
            // Showing progress dialog before making http request
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }


    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(inhurryActivity.this)
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
        Intent intent_buymainactivity = getIntent();
        // sMemberId = intent_buymainactivity.getStringExtra("memberid");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
    }

    private void get_checkout() {

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_CHECKOUT, sMemberId);
        new  CheckoutAsyncHttpTask().execute(url);

       /* RequestQueue queue = Volley.newRequestQueue(inhurryActivity.this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Success_checkout(response);
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Error_checkout(error);
                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);*/
    }

    private void Success_checkout(JSONObject response) {
        try
        {
            Intent  buycartcheckout_intenet= new Intent(inhurryActivity.this, NewCartSummary.class);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cart_productdetail", response.getJSONArray("ProductDetail").toString());
            editor.putString("cart_pricesummarylist", response.getJSONArray("PriceSummaryList").toString());

            globalVariable.setScartjson(response.getJSONArray("ProductDetail").toString());
            globalVariable.setSsummaryjson(response.getJSONArray("PriceSummaryList").toString());

            if (response.getString("PromoCode").toString().equals("null"))
            {
                editor.putString("cart_promocode", "");
                globalVariable.setsPromoCode("");
            }
            else
            {
                editor.putString("cart_promocode", response.getString("PromoCode").toString());
                globalVariable.setsPromoCode(response.getString("PromoCode").toString());
            }
           // editor.putString("cart_checkout_ParentClass", "inhurryActivity");
            globalVariable.setParentClass("inhurryActivity");
            editor.commit();

            startActivity(buycartcheckout_intenet);
        } catch (JSONException e) {
            e.printStackTrace();
            f_alert_ok("Error ",e.getMessage());
        }
    }


    private void Error_checkout(VolleyError error) {
        f_alert_ok("Error ", error.getMessage());
    }

    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        mToolbarMenu = paramMenu;

        createCartBadge(nAddtocart_count);
        return super.onPrepareOptionsMenu(paramMenu);
    }

    private void createCartBadge(int paramInt) {
        if (Build.VERSION.SDK_INT <= 15) {
            return;
        }
        if (mToolbarMenu != null) {
            MenuItem cartItem = this.mToolbarMenu.findItem(R.id.action_cart);
            LayerDrawable localLayerDrawable = (LayerDrawable) cartItem.getIcon();
            Drawable cartBadgeDrawable = localLayerDrawable
                    .findDrawableByLayerId(R.id.ic_badge);
            BadgeDrawable badgeDrawable;
            if ((cartBadgeDrawable != null)
                    && ((cartBadgeDrawable instanceof BadgeDrawable))
                    && (paramInt < 10)) {
                badgeDrawable = (BadgeDrawable) cartBadgeDrawable;
            } else {
                badgeDrawable = new BadgeDrawable(this);
            }
            badgeDrawable.setCount(paramInt);
            localLayerDrawable.mutate();
            localLayerDrawable.setDrawableByLayerId(R.id.ic_badge, badgeDrawable);
            cartItem.setIcon(localLayerDrawable);
        }
    }

    private void best_time_for_call()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);
        final EditText inputSearch = (EditText) dialogview.findViewById(R.id.inputSearch);
        builder.setView(dialogview);
        // Adding items to listview


        besttimeadapter = new AD_adapterCombo(this, R.layout.list_item,ObjectItemBestTime);
        lv.setAdapter(besttimeadapter);

        final TextView Titile =(TextView)dialogview.findViewById(R.id.textView);
        Titile.setText("Select Best Time to Call");
        inputSearch.setHint("Enter Relation");
        final TextView btncancel=(TextView)dialogview.findViewById(R.id.btncancel);
        final Dialog dlg= builder.create();
        lv.setOnItemClickListener(new OnItemClickListenerListViewItemBestTime(dlg));

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
    public class OnItemClickListenerListViewItemBestTime implements AdapterView.OnItemClickListener {
        Dialog d=null;
        OnItemClickListenerListViewItemBestTime(Dialog dlg)
        {
            d=dlg;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Context context = view.getContext();


            TextView textViewItem = ((TextView) view.findViewById(R.id.product_name));
            String listItemText = textViewItem.getText().toString();
            String listItemId = textViewItem.getTag().toString();
            timeslot = Integer.parseInt(listItemId);
            input_besttime.setText(listItemText);


            d.cancel();

        }
    }

    private void objectItemBestTimeLoad()
    {

        ObjectItemBestTime = new ObjectItem[5];

        ObjectItemBestTime[0] = new ObjectItem(12,"08 AM-12 NOON");
        ObjectItemBestTime[1] = new ObjectItem(13,"12 PM-03 PM");
        ObjectItemBestTime[2] = new ObjectItem(14,"03 PM-06 PM");
        ObjectItemBestTime[3] = new ObjectItem(15,"06 PM-08 PM");
        ObjectItemBestTime[4] = new ObjectItem(17,"ASAP");
    }
    public class CheckoutAsyncHttpTask extends AsyncTask<String, Void, Integer> {

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


                    Success_checkout(x);

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
                    hideSoftKeyboard(inhurryActivity.this);
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

package viroopa.com.medikart.buying;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.buying.adapter.ExpandableAdapter;
import viroopa.com.medikart.buying.adapter.adapterordertransaction;
import viroopa.com.medikart.buying.model.Category;
import viroopa.com.medikart.buying.model.ItemDetail;
import viroopa.com.medikart.buying.model.M_orderlist;

public class Order_Transaction extends AppCompatActivity {
    private ListView mainList;
    private String sMemberId;
    adapterordertransaction adapterorder;
    private ProgressDialog pDialog;
    private LinearLayout noData;
    private ArrayList<String> HeaderSectionArray = new ArrayList<String>();
    private Button btnorder;
    private LinearLayout mLinearListView;
    boolean isFirstViewClick = false;
    boolean isSecondViewClick = false;
    private ExpandableListView exList;
    private List<Category> catList;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat dateFormat_display = new SimpleDateFormat("dd");
    DateFormat dateFormat_letterIn_day_display = new SimpleDateFormat("EEE", Locale.getDefault());
    private ArrayList<M_orderlist> ordertransactionlist = new ArrayList<M_orderlist>();
    private static final String TAG = Order_Transaction.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_transactin);
        mLinearListView = (LinearLayout) findViewById(R.id.linear_listview);
        mainList = (ListView) findViewById(R.id.list_orderlist);
        noData = (LinearLayout) findViewById(R.id.rl_no_size_available);
        btnorder = (Button) findViewById(R.id.btnorder);
        exList = (ExpandableListView) findViewById(R.id.expandableListView1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        pDialog = new ProgressDialog(this);


        getIntenet();
        get_orderlist();
        exList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });

        int firstPosition = mainList.getFirstVisiblePosition();
        adapterorder = new adapterordertransaction(this, ordertransactionlist, HeaderSectionArray, ordertransactionlist.size() + HeaderSectionArray.size());
        mainList.setAdapter(adapterorder);
        adapterorder.notifyDataSetChanged();
        mainList.setSelection(firstPosition);

        btnorder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent Intenet_buy = new Intent(Order_Transaction.this, BuySearchActivity.class);
                startActivity(Intenet_buy);
                finish();

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
        new AlertDialog.Builder(Order_Transaction.this)
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
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
    }

    private void get_orderlist() {

        showPdialog("Loading. . .");

        //?iDeliveryAgentId=4&sDeviceId=355666055661382"

        //String url = AppConfig.URL_GET_ORDERLIST+"iDeliveryAgentId="+DeliveryAgentID+"&sdeviceid="+IMEINumber;

        String url = String.format(AppConfig.URL_GET_REFILLBASKET, sMemberId);
        new getOrderListtAsyncHttpTask().execute(url);


      /*  RequestQueue queue = Volley.newRequestQueue(Order_Transaction.this);

        String tag_string_req = "req_refill";

        JsonArrayRequest refillrequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "Register Response: " + response.toString());
                Success_refill(response);
                fillquestionAnswer(response);

                //bindvalue();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Register Response: " + error.getMessage());
                        hidePDialog();
                        Error_refill(error);
                    }
                });

        refillrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(refillrequest, tag_string_req);

        queue.add(refillrequest);*/

    }

    private void Success_refill(JSONArray response) {

        if (response.length() == 0) {

        }

        String Mymonth = "";
        for (int i = 0; i < response.length(); i++) {

            try {
                JSONObject obj_json = response.getJSONObject(i);

                if (!obj_json.equals(null)) {
                    M_orderlist O_orderlist = new M_orderlist();

                            /*
                            "MemberId": 184,
                            "OrderDate": "2015-08-04T00:37:19",
                            "OrderId": 136,
                            "OrderNo": "O1508040001",
                            "status": "Pending",
                            "DeliveryAgentId": 0,
                            "ExpectedDeliveryDate": "0001-01-01T00:00:00"
                             */
                    if (!Mymonth.equals(obj_json.getString("OrderMonth"))) {
                        HeaderSectionArray.add(obj_json.getString("OrderMonth") + "," + obj_json.getString("OrderYear"));
                        // HeaderSectionArray.add(obj_json.getString("OrderMonth"));
                        Mymonth = obj_json.getString("OrderMonth");

                    } else {

                    }
                    O_orderlist.setMemberId(obj_json.getString("MemberId"));
                    O_orderlist.setOrderDate(obj_json.getString("OrderDate"));
                    O_orderlist.setOrderId(obj_json.getString("OrderId"));
                    O_orderlist.setOrderNo(obj_json.getString("OrderNo"));
                    O_orderlist.setStatus(obj_json.getString("status"));
                    O_orderlist.setDeliveryAgentId(obj_json.getString("DeliveryAgentId"));
                    O_orderlist.setExpectedDeliveryDate(obj_json.getString("ExpectedDeliveryDate"));
                    O_orderlist.setNetAmount(obj_json.getString("NetAmount"));
                    O_orderlist.setOrderMonth(obj_json.getString("OrderMonth"));
                    O_orderlist.setOrderYear(obj_json.getString("OrderYear"));

                    // adding Invoice to Invoices array
                    ordertransactionlist.add(O_orderlist);
                } else {

                }
            } catch (JSONException e) {
                hidePDialog();
                Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

    }

    private void Error_refill(VolleyError error) {
        hidePDialog();

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

    private void fillquestionAnswer(JSONArray response) {
        catList = new ArrayList<Category>();
        List<ItemDetail> result=null;

        ItemDetail item=null;
        Category cat1=null;
        String smymonthyear = "-99";


        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj_json = response.getJSONObject(i);
                if (!obj_json.equals(null))
                {
                    String runingmonthyear = obj_json.getString("OrderMonth").trim()+" - "+obj_json.getString("OrderYear").trim();

                    if (!runingmonthyear.equals(smymonthyear)) {
                        if (cat1!=null)
                        {
                            catList.add(cat1);
                        }


                        result = new ArrayList<ItemDetail>();
                        smymonthyear = runingmonthyear;


                        cat1 = new Category(i,obj_json.getString("OrderMonth").trim(),obj_json.getString("OrderYear").trim());
                    }
                    item = new ItemDetail(i,obj_json.getString("OrderDate"),obj_json.getString("OrderNo"),obj_json.getString("NetAmount"),obj_json.getString("OrderId"));

                    result.add(item);
                    cat1.setItemList(result);

                }

            } catch (Exception e) {
                e.toString();

            }
        }

        if (cat1!=null)
        {
            catList.add(cat1);
            //exList.expandGroup(nGroup);
        }



    }

    public class getOrderListtAsyncHttpTask extends AsyncTask<String, Void, Integer> {

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

                    JSONArray x = new JSONArray(response.toString());


                    Success_refill(x);
                    fillquestionAnswer(x);
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

                if(catList.size()==0)
                {
                    noData.setVisibility(View.VISIBLE);
                }
                adapterorder.notifyDataSetChanged();
                ExpandableAdapter exAdpt = new ExpandableAdapter(catList, Order_Transaction.this);

                exList.setAdapter(exAdpt);
                int count =  catList.size();
                for (int i = 0; i <count ; i++)
                    exList.expandGroup(i);


            } else {


            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}



package viroopa.com.medikart.buying;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.TermsCondition;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.adapterorderlist;
import viroopa.com.medikart.buying.common.BadgeDrawable;
import viroopa.com.medikart.buying.model.M_orderlist;


/**
 * Created by prakash on 17/08/15.
 */
public class refillorder extends AppCompatActivity {
    // Log tag
    private String sMemberId;

    AppController globalVariable;
    private ProgressDialog pDialog;
    private Button btnorder;
    private String obj_saved,reasons;
    private LinearLayout heading_tab;

    private String DeliveryAgentID;
    private String IMEINumber ;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private ArrayList<M_orderlist> orderlist = new ArrayList<M_orderlist>();
    private ListView listView;
    private adapterorderlist adapter_order;

    private Menu mToolbarMenu;
    Integer nAddtocart_count = 0;

    private LinearLayout rl_no_size_available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refill_order);
        globalVariable = (AppController) getApplicationContext();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        btnorder=(Button)findViewById(R.id.btnorder);

        getIntenet();

        rl_no_size_available  = (LinearLayout) findViewById(R.id.rl_no_size_available);

        pDialog = new ProgressDialog(this);

        listView = (ListView) findViewById(R.id.list_orderlist);
        heading_tab=(LinearLayout)findViewById(R.id.linearLayout2);

        adapter_order = new adapterorderlist(this, orderlist);
        listView.setAdapter(adapter_order);
        btnorder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent Intenet_buy = new Intent(refillorder.this, BuySearchActivity.class);
                startActivity(Intenet_buy);
                finish();

            }
        });
        get_orderlist();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        //inflater.inflate(R.menu.menu_main, menu);
        inflater.inflate(R.menu.menu_shoppingcart, menu);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));

        /*MenuItem item_cart = menu.findItem(R.id.cart_count);
        item_cart.setTitle(sAddtocart_count);

        MenuItem item_search = menu.findItem(R.id.action_search);
        item_search.setVisible(false);

        MenuItem item_badge = menu.findItem(R.id.badge);
        item_badge.setVisible(false);

        MenuItem item_settings = menu.findItem(R.id.action_settings);
        item_settings.setVisible(false);*/

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

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
        }else  if (id == R.id.order_transaction) {
            Show_Order_Transaction();

        }else  if (id == R.id.return_cancel_policies) {
            Show_Return_cancel_policies();

        }else  if (id == R.id.termscondition) {
            Show_termscondition();
        }
        return super.onOptionsItemSelected(item);
    }

    public void Show_Order_Transaction() {
        Intent Intenet_change = new Intent(refillorder.this, Order_Transaction.class);
        startActivity(Intenet_change);
    }
    public void Show_Return_cancel_policies() {
        Intent Intenet_change = new Intent(refillorder.this, TermsCondition.class);
        startActivity(Intenet_change);
    }
    public void Show_termscondition() {
        Intent Intenet_change = new Intent(refillorder.this, TermsCondition.class);
        startActivity(Intenet_change);
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
    private void get_orderlist() {

        showPdialog("Loading. . .");

        //?iDeliveryAgentId=4&sDeviceId=355666055661382"

        //String url = AppConfig.URL_GET_ORDERLIST+"iDeliveryAgentId="+DeliveryAgentID+"&sdeviceid="+IMEINumber;

        String url = String.format(AppConfig.URL_GET_DELIVERORDERLIST, sMemberId,0,"DEL");
        new getOrderListtAsyncHttpTask().execute(url);

      /*  RequestQueue queue = Volley.newRequestQueue(refillorder.this);

        String tag_string_req = "req_refill";

        JsonArrayRequest refillrequest = new JsonArrayRequest(url,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "Register Response: " + response.toString());
                Success_refill(response);
                hidePDialog();
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

        if (response.length() == 0)
        {

        }

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

                    //O_orderlist.setMemberId(obj_json.getString("MemberId"));
                    O_orderlist.setOrderDate(obj_json.getString("OrderDate"));
                    O_orderlist.setOrderId(obj_json.getString("OrderId"));
                    O_orderlist.setOrderNo(obj_json.getString("OrderNo"));
                    O_orderlist.setStatus(obj_json.getString("StatusName"));
                    //O_orderlist.setDeliveryAgentId(obj_json.getString("DeliveryAgentId"));
                    //O_orderlist.setExpectedDeliveryDate(obj_json.getString("ExpectedDeliveryDate"));
                    O_orderlist.setNetAmount(obj_json.getString("NetAmount"));

                    // adding Invoice to Invoices array
                    orderlist.add(O_orderlist);
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

    private void getIntenet() {
        Intent intent_buymainactivity = getIntent();
       // sMemberId = intent_buymainactivity.getStringExtra("memberid");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(refillorder.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

   /* private void get_cartlist() {

        showPdialog("Loading. . .");
        String url = String.format(AppConfig.URL_GET_CHECKOUT,sMemberId);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Success_cartlist(response);
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Error_cartlist(error);
                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq_single);
    }

    private void Success_cartlist(JSONObject response) {
        try
        {
            Intent intent_cartlist= new Intent(refillorder.this, cart_list.class);
            intent_cartlist.putExtra("ProductDetail", response.getJSONArray("ProductDetail").toString());
            intent_cartlist.putExtra("PriceSummaryList", response.getJSONArray("PriceSummaryList").toString());
            intent_cartlist.putExtra("ParentClassName", "refillorder");

            startActivity(intent_cartlist);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            f_alert_ok("Error",e.getMessage());
        }
    }

    private void Error_cartlist(VolleyError error) {

        f_alert_ok("Error ", error.getMessage());
    }*/

    private void get_checkout() {

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_CHECKOUT,sMemberId);
        new  CheckoutAsyncHttpTask().execute(url);
       /* RequestQueue queue = Volley.newRequestQueue(refillorder.this);

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
            Intent  buycartcheckout_intenet= new Intent(refillorder.this, NewCartSummary.class);
            //buycartcheckout_intenet.putExtra("ParentClassName", "BuySearchActivity");

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
            //editor.putString("cart_checkout_ParentClass", "refillorder");
            globalVariable.setParentClass("refillorder");
            editor.commit();

            startActivity(buycartcheckout_intenet);
        } catch (JSONException e) {
            e.printStackTrace();
            f_alert_ok("Error ",e.getMessage());
        }
    }

    private void Error_checkout(VolleyError error) {
        f_alert_ok("Error ",error.getMessage());
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
                if(orderlist.size()==0)
                {
                    rl_no_size_available.setVisibility(View.VISIBLE);
                    heading_tab.setVisibility(View.GONE);
                }
                adapter_order.notifyDataSetChanged();
            } else {


            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}


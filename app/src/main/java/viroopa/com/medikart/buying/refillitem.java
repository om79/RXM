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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.adaptersingleorderlist;
import viroopa.com.medikart.buying.common.BadgeDrawable;
import viroopa.com.medikart.buying.model.M_singleorder_detail;

/**
 * Created by prakash on 17/08/15.
 */
public class refillitem extends AppCompatActivity {
    // Log tag

    private ProgressDialog pDialog;

    private String obj_saved,reasons,sOrderId,sMemberId;

    private String DeliveryAgentID;
    private String IMEINumber ;
    private int nTotalCartCount,nCartId;
    static Button notifCount;
    static int mNotifCount = 0;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private ArrayList<M_singleorder_detail> singleorderlist = new ArrayList<M_singleorder_detail>();
    private ListView listView;
    private adaptersingleorderlist adapter_singleorder;
    AppController globalVariable;
    private Button add_m_cart_btn;
    private  String Product_idS="";
    private Menu mToolbarMenu;
    List<String> productIds = new ArrayList<String>();
    Integer nAddtocart_count = 0;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateFormat_display = new SimpleDateFormat("dd LLLL yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.refill_detailed);
        globalVariable = (AppController) getApplicationContext();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        getIntenet();

        pDialog = new ProgressDialog(this);
        add_m_cart_btn= (Button) findViewById(R.id.add_m_cart_btn);
        listView = (ListView) findViewById(R.id.list_Singleorderlist);

        adapter_singleorder = new adaptersingleorderlist(this, singleorderlist, sMemberId, sOrderId, 1);
        listView.setAdapter(adapter_singleorder);

        get_singleorderlist();

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


      /*  checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                // listView.setItemChecked(position,true);

                CheckBox chk = (CheckBox) v.findViewById(R.id.btnaddcart);

             /*   chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {



                   }
               }
                );*/

              //  String gt_Product_qty = (String) v.getTag(R.id.key_product_qty);

                if (!chk.isChecked()) {
                    chk.setChecked(true);
                    String gt_Product_id = (String) v.getTag(R.id.key_product_id);


                    productIds.add(gt_Product_id);
                } else {
                    chk.setChecked(false);
                    String gt_Product_id = (String) v.getTag(R.id.key_product_id);
                    productIds.remove(gt_Product_id);
                }
            }
        });

        add_m_cart_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(productIds.size()>0) {

                    String allIds = TextUtils.join(",", productIds);
                    get_add_to_cart(sOrderId, allIds);
                }else {
                    Toast.makeText(refillitem.this, "please select a item to reorder", Toast.LENGTH_LONG).show();
                }

            }
        });
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);

        return true;

    }*/

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
        }
        return super.onOptionsItemSelected(item);

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
    private void get_singleorderlist() {

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_VIEWORDERDETAIL, sOrderId, sMemberId);
        //new getOrderListtAsyncHttpTask().execute(url);

        RequestQueue queue = Volley.newRequestQueue(refillitem.this);

        JsonObjectRequest refillsinglerequest = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                Success_refillsingle(response);
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d(TAG, "Register Response: " + error.getMessage());
                        hidePDialog();
                        Error_refill(error);
                    }
                });

        refillsinglerequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(refillsinglerequest, tag_string_req);

        queue.add(refillsinglerequest);

    }

    private void Success_refillsingle(JSONObject response_data) {

        try {

            TextView refs_orderno = (TextView) findViewById(R.id.refs_orderno);
            TextView refs_orderdt = (TextView) findViewById(R.id.refs_orderdt);
            TextView refs_name = (TextView) findViewById(R.id.refs_name);
            TextView refs_add = (TextView) findViewById(R.id.refs_add);

            refs_orderno.setText(response_data.getString("OrderNo"));

            try {
                Date current_date = Calendar.getInstance().getTime();
                current_date = dateFormat.parse(response_data.getString("OrderDate"));
                String dayLetter=(dateFormat_display.format(current_date));

                refs_orderdt.setText(dayLetter);

            }catch(Exception e)
            {

            }
            //  refs_orderdt.setText(response_data.getString("OrderDate").substring(0, 10));
            refs_name.setText(response_data.getString("CustomerName"));
            refs_add.setText(response_data.getString("CustomerAddress"));

/*            ref_OrderNo.setText("Order No : " + response.getOrderNo());
            ref_OrderDate.setText("Date : " + O_singleorder_detail.getOrderDate());
            ref_status.setText("Status : " + O_singleorder_detail.getStatus());
            ref_ExpectedDeliveryDate.setText("Expected date :"+O_singleorder_detail.getExpectedDeliveryDate());*/

            // Parsing json
            JSONArray response =  new JSONArray();
            response = response_data.getJSONArray("odm");

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj_json = response.getJSONObject(i);

                    if (!obj_json.equals(null)) {

                        //OrderId   ,  ProductId   ,  Product,  Price ,  Discount   ,    Quantity;
                        M_singleorder_detail O_singleorder_detail = new M_singleorder_detail();

                        O_singleorder_detail.setOrderId(obj_json.getString("OrderId"));
                        O_singleorder_detail.setProductId(obj_json.getString("ProductId"));
                        O_singleorder_detail.setProduct(obj_json.getString("Product"));
                        O_singleorder_detail.setPrice(obj_json.getString("Price"));
                        O_singleorder_detail.setDiscount(obj_json.getString("Discount"));
                        O_singleorder_detail.setQuantity(obj_json.getString("Quantity"));
                        O_singleorder_detail.setPacksize(obj_json.getString("Packsize"));

                        // adding Invoice to Invoices array
                        singleorderlist.add(O_singleorder_detail);

                    }

                } catch (JSONException e) {
                    hidePDialog();
                    Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
            adapter_singleorder.notifyDataSetChanged();
        } catch (JSONException e) {
            hidePDialog();
            e.printStackTrace();
            // Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Error_refill(VolleyError error) {
        hidePDialog();

    }

    private void getIntenet(){
        Intent intent_refilorder = getIntent();
        sOrderId = intent_refilorder.getStringExtra("orderid");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
    }

    /*private void get_cartlist() {

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
            Intent intent_cartlist= new Intent(refillitem.this, cart_list.class);
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

        f_alert_ok("Error ",error.getMessage());
    }*/

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(refillitem.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void get_checkout() {

        showPdialog("Loading. . .");

        //String url = "http://198.50.198.184:86/Api/Checkout/Checkout?iProductId=10152&iMemberId=184";

        String url = String.format(AppConfig.URL_GET_CHECKOUT,sMemberId);
        // new  CheckoutAsyncHttpTask().execute(url);

        RequestQueue queue = Volley.newRequestQueue(refillitem.this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url,  new Response.Listener<JSONObject>() {
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

        queue.add(jsonObjReq_single);
    }

    private void Success_checkout(JSONObject response) {
        try
        {
            Intent  buycartcheckout_intenet= new Intent(refillitem.this, NewCartSummary.class);
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
            editor.putString("cart_checkout_ParentClass", "refillorder");
            globalVariable.setParentClass("refillorder");

            editor.commit();

            startActivity(buycartcheckout_intenet);
        } catch (JSONException e) {
            hidePDialog();
            e.printStackTrace();
            f_alert_ok("Error ",e.getMessage());
        }
    }

    private void Error_checkout(VolleyError error) {
        f_alert_ok("Error ", error.getMessage());
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

                    JSONObject x = new JSONObject(response.toString());


                    Success_refillsingle(x);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                e.toString();
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {


            hidePDialog();

            if (result == 1) {
                adapter_singleorder.notifyDataSetChanged();
            } else {


            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void get_add_to_cart(String order_id,String p_productid) {

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_ADDORDERITEMTOCART,order_id,sMemberId,p_productid,"M");

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Success_addtocart(response);
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Error_addtocart(error);
                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);
    }

    private void Success_addtocart(JSONObject response) {
        try {
            nTotalCartCount = Integer.parseInt(response.getString("TotalCartCount"));
            nCartId = Integer.parseInt(response.getString("iCartId"));

            SharedPreferences pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cartid", response.getString("iCartId"));
            editor.putString("addtocart_count",response.getString("TotalCartCount"));
            editor.commit();

            setNotifCount(nTotalCartCount);

            f_alert_ok("Information", "Added to Cart Successfully");
        }
        catch (Exception e) {
            hidePDialog();
            e.printStackTrace();
        }
    }

    private void Error_addtocart(VolleyError error) {
        f_alert_ok("Error : ", error.getMessage());
    }

    private void setNotifCount(int count){
        mNotifCount = count;

        this.invalidateOptionsMenu();
    }

    public void  fill_checked_data(final CheckBox ch, String gt_Product_id)
    {

                if (ch.isChecked()) {

                    productIds.add(gt_Product_id);
                } else {

                    productIds.remove(gt_Product_id);
                }

    }

}


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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
import viroopa.com.medikart.MainActivity;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.adapterprice;
import viroopa.com.medikart.buying.adapter.adaptersingleorderlist;
import viroopa.com.medikart.buying.model.M_pricing;
import viroopa.com.medikart.buying.model.M_singleorder_detail;

/**
 * Created by prakash on 17/08/15.
 */
public class orderPlacedData extends AppCompatActivity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;

    private String obj_saved,reasons,sOrderId,sMemberId;

    private String DeliveryAgentID;
    private String IMEINumber ;
    private  ImageView okbtn;
    private String Total_price="0.0";

    static Button notifCount;
    static int mNotifCount = 0;
    adapterprice secadapter;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    AppController globalVariable;
    Date current_date = Calendar.getInstance().getTime();
  //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.ms");


    DateFormat dateFormat_display = new SimpleDateFormat("dd LLL, yyyy");

    private ArrayList<M_singleorder_detail> singleorderlist = new ArrayList<M_singleorder_detail>();
    List<M_pricing> pricelist = new ArrayList<M_pricing>();
    private ListView listView,UI_lvSecond;
    private adaptersingleorderlist adapter_singleorder;
    private TextView TotalPrice,order_num;
    private TextView order_date,txt_payment_id;

    private Menu mToolbarMenu;
    Integer nAddtocart_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_placed_data);

        globalVariable = (AppController) getApplicationContext();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        // orderno=(TextView)findViewById(R.id.orderNoplaced);
        okbtn=(ImageView)findViewById(R.id.btnok);
        order_date= (TextView)findViewById(R.id.order_date);
        txt_payment_id= (TextView)findViewById(R.id.txt_payment_id);
        getIntenet();

        pDialog = new ProgressDialog(this);


        listView = (ListView) findViewById(R.id.list_Singleorderlist);
        UI_lvSecond = (ListView)findViewById(R.id.list_pricelist);
        TotalPrice= (TextView)findViewById(R.id.TotalPrice);
        order_num= (TextView)findViewById(R.id.order_num);
        adapter_singleorder = new adaptersingleorderlist(this, singleorderlist,sMemberId,sOrderId,0);
        listView.setAdapter(adapter_singleorder);
        secadapter = new adapterprice(this,pricelist,sMemberId);
        UI_lvSecond.setAdapter(secadapter);
        get_singleorderlist();

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
              Intent intent_main=new Intent(orderPlacedData.this,BuyMainActivity.class);
                startActivity(intent_main);
               finish();

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

    @Override
    public void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);
        //inflater.inflate(R.menu.menu_shoppingcart, menu);

       // SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
      //  nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));


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

  /*  public boolean onPrepareOptionsMenu(Menu paramMenu) {
        mToolbarMenu = paramMenu;
        createCartBadge(nAddtocart_count);
        return super.onPrepareOptionsMenu(paramMenu);
    }*/

  /*  private void createCartBadge(int paramInt) {
        if (Build.VERSION.SDK_INT <= 15) {
            return;
        }
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
    }*/

    private void get_singleorderlist() {

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_PRICINGORDERDETAIL, sOrderId);

        RequestQueue queue = Volley.newRequestQueue(orderPlacedData.this);



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
                    //Log.d(TAG, "Register Response: " + error.getMessage());
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



            // Parsing json
            JSONArray response =  new JSONArray();
            JSONArray priceresponse =  new JSONArray();

            order_num.setText(response_data.getString("OrderNo"));


            try {

               // current_date = dateFormat.parse(response_data.getString("OrderDate"));
                String day_formatted = (dateFormat_display.format(current_date));
                order_date.setText(day_formatted);

            } catch (Exception e) {

            }



            response = response_data.getJSONArray("odm");
            priceresponse = response_data.getJSONArray("PricisingList");

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
                        O_singleorder_detail.setImageUrl(obj_json.getString("ImagePath"));

                        // adding Invoice to Invoices array
                        singleorderlist.add(O_singleorder_detail);



                    }

                } catch (JSONException e) {
                    hidePDialog();
                    Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


                adapter_singleorder.notifyDataSetChanged();
            }

            for (int i = 0; i < priceresponse.length(); i++) {
                try {

                    JSONObject priceobj_json = priceresponse.getJSONObject(i);

                    if (!priceobj_json.equals(null)) {
                        M_pricing O_pricing = new M_pricing();
                        O_pricing.setDiscription(priceobj_json.getString("Description"));
                        O_pricing.setAmount(priceobj_json.getString("Amount"));

                        if(priceobj_json.getString("Description").equals("Grand Total"))
                        {
                            if(!priceobj_json.getString("Amount").equals("")  || priceobj_json.getString("Amount")!=null)
                            {
                                Total_price=priceobj_json.getString("Amount");

                                Double double_price=0.00;
                                if(Total_price!=null && Total_price!="") {
                                    double_price = Double.parseDouble(Total_price);
                                }

                                TotalPrice.setText("Rs." + " " + String.format("%.2f",double_price));
                            }
                        }
                        pricelist.add(O_pricing);




                    }
                }catch (Exception e){
                    e.toString();
                }

            }
            secadapter.notifyDataSetChanged();
            ListUtils.setDynamicHeight(listView);
            ListUtils.setDynamicHeight(UI_lvSecond);

        } catch (JSONException e) {
            hidePDialog();
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Error_refill(VolleyError error) {
        hidePDialog();

    }

    private void getIntenet(){
        Intent intent_refilorder = getIntent();
        sOrderId = intent_refilorder.getStringExtra("orderid");

        if(intent_refilorder.getStringExtra("payment_id")!=null)
        {
            if(intent_refilorder.getStringExtra("payment_id").equals("false"))
            {
                txt_payment_id.setVisibility(View.GONE);
            }else {
                txt_payment_id.setVisibility(View.VISIBLE);
                txt_payment_id.setText("Your payment id is : "+intent_refilorder.getStringExtra("payment_id"));
            }
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        String orderNO=intent_refilorder.getStringExtra("orderNO");
       // orderno.setText("Your order is Successfully placed . your order number is  : "+orderNO);
    }


    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(orderPlacedData.this)
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
        new  CheckoutAsyncHttpTask().execute(url);

      /*  RequestQueue queue = Volley.newRequestQueue(orderPlacedData.this);

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
            Intent  buycartcheckout_intenet= new Intent(orderPlacedData.this, NewCartSummary.class);
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
            globalVariable.setParentClass("orderPlacedData");

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


    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }


            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public Intent getSupportParentActivityIntent() {
        Intent newIntent = null;
        try {
            //you need to define the class with package name
            newIntent = new Intent(this, BuyMainActivity.class);
            startActivity(newIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            //  f_alert_ok("Error ", e.getMessage());
        }
        return newIntent;
    }

    @Override
    public void onBackPressed() {


        Intent newIntent = null;
        try {
            //you need to define the class with package name
            newIntent = new Intent(this, BuyMainActivity.class);
            startActivity(newIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
          //  f_alert_ok("Error ", e.getMessage());
        }
        // super.onBackPressed();
        // finish();
    }

}

/*
   OrderId   , MemberId   ,  status,OrderNo ,TotalAmount ,TotalDiscount ,NetAmount , OrderDate ,   CustomerName  , CustomerAddress ,
   MobileNo ,ProductID ,
   odm  OrderId   ,  ProductId   ,  Product,  Price ,  Discount   ,    Quantity
 */
package viroopa.com.medikart.buying;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.ViewPagerAdapter;
import viroopa.com.medikart.buying.common.BadgeDrawable;


public class BuyTabs extends AppCompatActivity {

    private String sMemberId;


    static Button notifCount;
    static int mNotifCount = 0;
    AppController globalVariable;
    private Button btncheckout;
    ViewPager pager;
    //private String titles[] = new String[]{"Details", "Molecule1", "Info","Substitute","Molecule"};
    private String titles[] = new String[]{"Details", "Molecule","Substitute"};

    private ProgressDialog pDialog;


    private String iProductId, productname;

    private Menu mToolbarMenu;
    Integer nAddtocart_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_search_details);
        globalVariable = (AppController) getApplicationContext();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);




        getIntenet();

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(productname);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pager = (ViewPager) findViewById(R.id.viewpager);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Molecule"));
        tabLayout.addTab(tabLayout.newTab().setText("Substitute"));



        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pager = (ViewPager) findViewById(R.id.viewpager);

        final PagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),titles,iProductId,sMemberId);

        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#f38630"));
       // mTabStrip  = (SlidingTabStrip)tabLayout.get

       // pagerTitleStrip = (PagerTitleStrip)tabLayout.findViewById(R.id.pager_title_strip);
       tabLayout.setTabTextColors(Color.parseColor("#428bca"), Color.parseColor("#dfe8f9"));



        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                //tabLayout.setBackgroundColor(Color.parseColor("#f38630"));
               // pager.setBackgroundColor(Color.parseColor("#f38630"));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });



        /*slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),titles,iProductId,sMemberId));

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.parseColor("#f38630");
            }
        });*/

        //slidingTabLayout.setBackgroundColor(Color.parseColor("#f38630"));

        btncheckout = (Button) findViewById(R.id.btncheckout);

        btncheckout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (nAddtocart_count > 0)
                {
                    get_checkout();
                }
                else
                {
                    f_alert_ok("Information","Your cart is empty");
                }
            }
        });
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


        return true;
    }


    private void setNotifCount(int count){
        mNotifCount = count;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_cart) {
            if (nAddtocart_count > 0)
            {
                get_checkout();
            }
            else
            {
                f_alert_ok("Information","Your cart is empty");
            }


        }
        if (id == R.id.order_transaction) {
            Show_Order_Transaction();
            return true;
        }
        if (id == R.id.return_cancel_policies) {
            Show_Return_cancel_policies();
            return true;
        }
        if (id == R.id.termscondition) {
            Show_termscondition();
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
    private void getIntenet() {
        Intent intent_productdetails = getIntent();
        iProductId = intent_productdetails.getStringExtra("productid");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        productname = pref.getString("product_name", "");
    }

    private void get_checkout() {

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_CHECKOUT,sMemberId);
        new  CheckoutAsyncHttpTask().execute(url);

      /*  RequestQueue queue = Volley.newRequestQueue(BuyTabs.this);

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
            Intent  buycartcheckout_intenet= new Intent(BuyTabs.this, NewCartSummary.class);

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
            globalVariable.setParentClass("BuySearchActivity");

            editor.commit();

            startActivity(buycartcheckout_intenet);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            f_alert_ok("Error ",e.getMessage());
        }
    }

    private void Error_checkout(VolleyError error) {

        f_alert_ok("Error ",error.getMessage());
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    private void showPdialog(String sMessage) {
        if (pDialog != null) {
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(BuyTabs.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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
    public void Show_Order_Transaction() {
       // Intent Intenet_change = new Intent(this, Order_Transaction.class);
       // startActivity(Intenet_change);
    }
    public void Show_Return_cancel_policies() {
       // Intent Intenet_change = new Intent(this, TermsCondition.class);
       // startActivity(Intenet_change);
    }
    public void Show_termscondition() {
      //  Intent Intenet_change = new Intent(this, TermsCondition.class);
      //  startActivity(Intenet_change);
    }

}



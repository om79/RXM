package viroopa.com.medikart.buying;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.Aboutus;
import viroopa.com.medikart.ChangePassword;
import viroopa.com.medikart.Contactus;
import viroopa.com.medikart.EditAndAddMember;
import viroopa.com.medikart.EditProfile;
import viroopa.com.medikart.FAQs;
import viroopa.com.medikart.LoginActivity;
import viroopa.com.medikart.R;
import viroopa.com.medikart.TermsCondition;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.common.BadgeDrawable;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SessionManager;


/**
 * Created by admin on 03/08/2015.
 */
public class BuyMainActivity
        extends AppCompatActivity
        implements SearchView.OnQueryTextListener
       {

    private final static String TAG = "BuyMainActivity";

    private String sMemberId;
    private ProgressDialog pDialog;
    private Boolean isOverlayScreensShown=false;

    private ImageView btnbuy, btnupload, btnhurry, btnrefill, btnsuper;
    ListView searchResults;
    AppController globalVariable;
    MenuItem item_search;
    SearchView mysearchView;
    private   Handler handler;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    private static final String NAV_ITEM_ID = "navItemId";

    private SQLiteHandler db;
    private SessionManager session;
    private Menu mToolbarMenu;

           private TextView btnwebsite;
           private LinearLayout laymain;


    DisplayImageOptions options;


    private Integer nAddtocart_count = 0;

    HashMap<String, String> m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.buymainscreen);
            options = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(1000)) //rounded corner bitmap
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .build();





           handler = new Handler();

            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                            .setDefaultFontPath("Lato-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
            );

            laymain= (LinearLayout) findViewById(R.id.laymain);
            btnupload = (ImageView) findViewById(R.id.btnupload);
            btnrefill = (ImageView) findViewById(R.id.btnrefill);
            btnhurry = (ImageView) findViewById(R.id.btnhurry);
            btnsuper = (ImageView) findViewById(R.id.btnsuper);

            btnwebsite = (TextView) findViewById(R.id.btnwebsite);
            //setContentView(R.layout.buymainscreen);
            initImageLoader();

            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            globalVariable = (AppController) getApplicationContext();
            //network_check();
            internet_check_alert();


            getSupportActionBar().setDisplayShowHomeEnabled(true);


            db = new SQLiteHandler(getApplicationContext());

            ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
            test.add(db.getUserDetails());

            m = test.get(0);


            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.rxlogo);

            pDialog = new ProgressDialog(this);
            //db = new SQLiteHandler(getApplicationContext());
            session = new SessionManager(getApplicationContext());

            getIntenet();

            get_cart_count(sMemberId);
            //searchResults = (ListView) buymain.findViewById(R.id.listview_search);


            // link button Click Event
            btnwebsite.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {


                    Snackbar snackbar = Snackbar
                            .make(laymain, "want to visit rxmedikart.com ?", Snackbar.LENGTH_LONG)
                            .setAction("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                   /* Snackbar snackbar1 = Snackbar.make(laymain, "Message is restored!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();*/
                                    Uri uri = Uri.parse("http://www.rxmedikart.com");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });

                    snackbar.show();


                }
            });

            btnupload.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            f_Touch_Down(v);
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            showuploadprescription();
                        }
                        case MotionEvent.ACTION_CANCEL: {
                            f_Touch_Cancel(v);
                            break;
                        }
                    }
                    return true;
                }
            });

            btnrefill.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            f_Touch_Down(v);
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            showrefill();
                        }
                        case MotionEvent.ACTION_CANCEL: {
                            f_Touch_Cancel(v);
                            break;
                        }
                    }
                    return true;
                }
            });

            btnhurry.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            f_Touch_Down(v);
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            showinhurry();
                        }
                        case MotionEvent.ACTION_CANCEL: {
                            f_Touch_Cancel(v);
                            break;
                        }
                    }
                    return true;
                }
            });

            btnsuper.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            f_Touch_Down(v);
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            showsuperuser();
                        }
                        case MotionEvent.ACTION_CANCEL: {
                            f_Touch_Cancel(v);
                            break;
                        }
                    }
                    return true;
                }
            });


            getSupportActionBar().getCustomView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showsearchmedicine("");
                }
            });

            btnupload.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    showuploadprescription();
                }
            });

            btnrefill.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    showrefill();
                }
            });

            btnhurry.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    showinhurry();
                }
            });
            btnsuper.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    showsuperuser();
                }
            });
        } catch (Exception e) {
            e.toString();
        }
    }




    @Override
    public void onStart() {


        super.onStart();
        Log.d(TAG, "onStart() called");
    }



    private void showsearchmedicine(String p_search_product) {
       Intent intent_buysearch = new Intent(BuyMainActivity.this, BuySearchActivity.class);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("search_product", p_search_product);
        editor.commit();

        startActivity(intent_buysearch);
    }

    private void showuploadprescription() {
        Intent intent_upload = new Intent(BuyMainActivity.this, uploadprescriptionActivity.class);

        startActivity(intent_upload);
    }

    private void showrefill() {
        Intent intent_refill = new Intent(BuyMainActivity.this, refillorder.class);
       startActivity(intent_refill);
    }

    private void showinhurry() {
        Intent intent_hurry = new Intent(BuyMainActivity.this, inhurryActivity.class);
       startActivity(intent_hurry);
    }

    private void showsuperuser() {

        showsearchmedicine("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buy, menu);


        //MenuItem item_exportdb = menu.findItem(R.id.exportdb);
        //item_exportdb.setVisible(false);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        int id = item.getItemId();
        if (id == R.id.action_searchmedicine) {
            showsearchmedicine("");
            return true;
        }

        return super.onOptionsItemSelected(item);*/

        switch (item.getItemId()) {
            case R.id.action_searchmedicine:
                showsearchmedicine("");
                return true;
            case R.id.edit_profile:
                Show_Edit_Profile();
                return true;
            case R.id.change_password:
                Show_Change_Password();
                return true;
            case R.id.add_member:
                Show_Add_Member();
                return true;
            case R.id.logout:
                Show_Logout();
                return true;
            case R.id.exportdb:
                exportDB();
                return true;
            case R.id.faqs:
                Show_faqs();
                return true;
            case R.id.termscondition:
                Show_termscondition();
                return true;
            case R.id.return_cancel_policies:
                Show_Return_cancel_policies();
                return true;
            case R.id.aboutus:
                Show_Aboutus();
                return true;
            case R.id.contactus:
                Show_Contactus();
                return true;
            case R.id.order_transaction:
                Show_Order_Transaction();
                return true;
            case R.id.action_cart:
                Show_cart();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 3) {
            //searchResults.setVisibility(myFragmentView.VISIBLE);
            //myAsyncTask m= (myAsyncTask) new myAsyncTask().execute(newText);
            get_productsearch(newText);
        } else {
            //searchResults.setVisibility(myFragmentView.INVISIBLE);
        }

        return false;
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
        if(mToolbarMenu!=null) {
            MenuItem cartItem = this.mToolbarMenu.findItem(R.id.action_cart);

            if (cartItem != null) {
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
    }

    public void Show_Aboutus() {
        Intent Intenet_change = new Intent(BuyMainActivity.this, Aboutus.class);
        startActivity(Intenet_change);
    }

    public void Show_Contactus() {
        Intent Intenet_change = new Intent(BuyMainActivity.this, Contactus.class);
          startActivity(Intenet_change);
    }

    public void Show_Order_Transaction() {
        Intent Intenet_change = new Intent(BuyMainActivity.this, Order_Transaction.class);
       startActivity(Intenet_change);
    }
    public void Show_Prescription_list() {
        //Intent Intenet_change = new Intent(BuyMainActivity.this, Upload_prescription_list.class);
       // startActivity(Intenet_change);
    }

    public void Show_cart() {
        if (nAddtocart_count > 0) {
            get_checkout();
        } else {
            f_alert_ok("Information", "Your cart is empty");
        }
    }

    public void Show_faqs() {
        Intent Intenet_change = new Intent(BuyMainActivity.this, FAQs.class);
        startActivity(Intenet_change);
    }

    public void Show_termscondition() {
        Intent Intenet_change = new Intent(BuyMainActivity.this, TermsCondition.class);
       startActivity(Intenet_change);
    }

    public void Show_Return_cancel_policies() {
        Intent Intenet_change = new Intent(BuyMainActivity.this, TermsCondition.class);
        startActivity(Intenet_change);
    }

    public void Show_Change_Password() {
        Intent Intenet_change = new Intent(BuyMainActivity.this, ChangePassword.class);
        startActivity(Intenet_change);
    }

    public void Show_Add_Member() {
        Intent Intenet_member = new Intent(BuyMainActivity.this, EditAndAddMember.class);
        startActivity(Intenet_member);
    }

    private void Show_Edit_Profile() {
        Intent Intenet_edit_profile = new Intent(BuyMainActivity.this, EditProfile.class);
        startActivity(Intenet_edit_profile);
    }

    private void Show_Logout() {
        showPdialog("loading...");
        db.logout_delete_all();
        session.setLogin(false);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.clear();
        delete_folder();
        prefsEditor.commit();

        hidePDialog();

        Intent Intenet_edit_profile = new Intent(BuyMainActivity.this, LoginActivity.class);
        startActivity(Intenet_edit_profile);
        finish();
    }

    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String backupDBPath = "Buying";
        String currentDBPath = "/data/" + "com.viroopa.Buying" + "/databases/" + backupDBPath;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void get_productsearch(String sSearchText) {

        showPdialog("Loading. . .");


        String url = String.format(AppConfig.URL_GET_SEARCHPRODUCT, sSearchText, "0", "s");

        RequestQueue queue = Volley.newRequestQueue(BuyMainActivity.this);

        JsonObjectRequest jar_getsearchproduct = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hidePDialog();

                try {
                    JSONArray jsonArray_search_pro = response.getJSONArray("searchModel");
                    if (jsonArray_search_pro.length() <= 0) {

                        new AlertDialog.Builder(BuyMainActivity.this)
                                .setTitle("Reason Data")
                                .setMessage("Order Data not found on sever.")
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                    if (jsonArray_search_pro.length() > 0) {
                        showsearchmedicine(response.getJSONArray("searchModel").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(getApplicationContext(), "main Server Error :" + error.getMessage(), Toast.LENGTH_LONG).show();

                hidePDialog();

            }
        });

        /*jar_getsearchproduct.setRetryPolicy(new DefaultRetryPolicy(
                AppConfig.SERVERTIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        //AppController.getInstance().addToRequestQueue(jar_getsearchproduct);

        queue.add(jar_getsearchproduct);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
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

    private void getIntenet() {
        Intent intent_mainactivity = getIntent();
        //sMemberId = intent_mainactivity.getStringExtra("memberid");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(BuyMainActivity.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void f_Touch_Down(View v) {
        ImageView view = (ImageView) v;
        //overlay is black with transparency of 0x77 (119)
        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
        view.invalidate();
    }

    private void f_Touch_Cancel(View v) {
        ImageView view = (ImageView) v;
        //clear the overlay
        view.getDrawable().clearColorFilter();
        view.invalidate();
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));

            createCartBadge(nAddtocart_count);


    }



    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


/*
    @Override
    public void onBackPressed() {
           // exit_notification();


    }*/

    @SuppressWarnings("StatementWithEmptyBody")




    private void exit_notification() {
        new AlertDialog.Builder(BuyMainActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure want to Exit")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();
                    }
                })
                .show();
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    private void network_check() {
        if (haveNetworkConnection() == true) {

        } else {
          //  Intent intent_network = new Intent(BuyMainActivity.this, networkerror.class);
           // startActivity(intent_network);
            finish();
        }
    }

    private void get_checkout() {

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_CHECKOUT, sMemberId);
        // new  CheckoutAsyncHttpTask().execute(url);

        RequestQueue queue = Volley.newRequestQueue(BuyMainActivity.this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Success_checkout(response);

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

        queue.add(jsonObjReq_single);
    }

    private void Success_checkout(JSONObject response) {
        try {
            Intent buycartcheckout_intenet = new Intent(BuyMainActivity.this, NewCartSummary.class);
           buycartcheckout_intenet.putExtra("GotoMain", "true");
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cart_productdetail", response.getJSONArray("ProductDetail").toString());
            editor.putString("cart_pricesummarylist", response.getJSONArray("PriceSummaryList").toString());

            globalVariable.setScartjson(response.getJSONArray("ProductDetail").toString());
            globalVariable.setSsummaryjson(response.getJSONArray("PriceSummaryList").toString());


            if (response.getString("PromoCode").toString().equals("null")) {
                //editor.putString("cart_promocode", "");
                globalVariable.setsPromoCode("");

            } else {
                //editor.putString("cart_promocode", response.getString("PromoCode").toString());
                globalVariable.setsPromoCode(response.getString("PromoCode").toString());

            }
            // editor.putString("cart_checkout_ParentClass", "BuyMainActivity");
            globalVariable.setParentClass("BuyMainActivity");
            editor.commit();
            startActivity(buycartcheckout_intenet);
            hidePDialog();
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            f_alert_ok("Error ", e.getMessage());
        }
    }

    private void Error_checkout(VolleyError error) {
        f_alert_ok("Error ", error.getMessage());
    }

    private void delete_folder() {
        //File dir = new File(Environment.getExternalStorageDirectory()+"Dir_name_here");
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
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

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(font, 0, mNewTitle.length(), 0);

        mi.setTitle(mNewTitle);
    }

    private void internet_check_alert() {

        handler.removeCallbacks(checkInternetConnection);
        handler.postDelayed(checkInternetConnection, 1000);


}
    public Runnable checkInternetConnection = new Runnable() {

        public void run() {

            handler.postDelayed(checkInternetConnection, 1000);
            get_cart_count(sMemberId);

            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                alertOff();

            } else {
                if(globalVariable.getIsNetwrkpageVisible()==false) {
                    alertOn();
                }
            }

        }
    };

    private void alertOff()
    {
       // Toast.makeText(getApplicationContext(), "Internet connection is on", Toast.LENGTH_LONG)
               // .show();
    }

    private void alertOn()
    {
       // Intent intent_network = new Intent(BuyMainActivity.this, networkerror.class);
       // startActivity(intent_network);
        globalVariable.setIsNetwrkpageVisible(true);

    }

    private  String get_current_activity_info()
    {
        ActivityManager am = (ActivityManager) this .getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        String name =
        taskInfo.get(0).topActivity.getClassName()+"   Package Name :  "+componentInfo.getPackageName();

        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG)
         .show();
        return name;
    }

    private  void get_cart_count(String memberId)
    {
        RequestQueue queue =globalVariable.getRequestQueue(); //Volley.newRequestQueue(BuyMainActivity.this);
        String url = String.format(AppConfig.URL_GET_CARTCOUNT_ONLOGIN, memberId);

        JsonObjectRequest FamilyMemberrequest = new JsonObjectRequest(Request.Method.GET,url,
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
                       // hidePDialog();
                       // showmainactivity();
                        //Error_FamilyMember(error);
                    }
                });

        FamilyMemberrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(FamilyMemberrequest);

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

    }
  /*  private void show_overlay_screens()
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putBoolean("isOverlayScreensShown", true);
        prefsEditor.commit();

        if(overlayDialog_count==0) {
            showOverLay(R.layout.overlay_view);
        }else if(overlayDialog_count==1) {
            showOverLay(R.layout.overlay_view_second);
        }else if(overlayDialog_count==2) {
            showOverLay(R.layout.overlay_view_third);
        }else if(overlayDialog_count==3) {
            showOverLay(R.layout.overlay_view_forth);
        }
        else if(overlayDialog_count==4) {
            showOverLay(R.layout.overlay_view_fifth);
        }
        else if(overlayDialog_count==5) {
            showOverLay(R.layout.overlay_view_sixth);
        }
        else if(overlayDialog_count==6) {
            showOverLay(R.layout.overlay_view_seven);
        }
        else if(overlayDialog_count==7) {
            showOverLay(R.layout.overlay_view_eight);
        }

    }

    private void showOverLay(int r_id){

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

        dialog.setContentView(r_id);

        FrameLayout layout = (FrameLayout) dialog.findViewById(R.id.overlayLayout);
        Button skip = (Button) dialog.findViewById(R.id.skip);

        layout.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {

                dialog.dismiss();

                overlayDialog_count++;
                show_overlay_screens();
            }

        });

        skip.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {

                dialog.dismiss();

            }

        });
        dialog.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dialog.show();

    }*/




}



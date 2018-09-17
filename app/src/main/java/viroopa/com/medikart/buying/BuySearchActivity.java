package viroopa.com.medikart.buying;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.SearchRecyclerAdapter;
import viroopa.com.medikart.buying.common.BadgeDrawable;
import viroopa.com.medikart.buying.model.productlist;
import viroopa.com.medikart.helper.SQLiteHandler;

/*import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;*/
//import android.support.v7.widget.SearchView;

/**
 * Created by admin on 07/08/2015.
 */
public class BuySearchActivity
        extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    private String sMemberId;
    private TextView no_data;

    private ProgressDialog pDialog;
    private List<productlist> productheaderlist = new ArrayList<productlist>();

    private ListView listView;
    private SearchView searchView_medicine;
    AppController globalVariable;
    static Button notifCount;
    static int mNotifCount = 0;
    private RecyclerView mRecyclerView;



    public static final int Medicine = 0;
    public static final int Salt = 1;
    public static final int General_products = 2;
    SearchView mysearchView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RelativeLayout search_main;



    SQLiteHandler db;
    SearchRecyclerAdapter recycleradapter;
    private Menu mToolbarMenu;
    Integer nAddtocart_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_search);
        search_main=(RelativeLayout)findViewById(R.id.search_main);
        setupUI(search_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        globalVariable = (AppController) getApplicationContext();
        db = new SQLiteHandler(BuySearchActivity.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        no_data=(TextView)findViewById(R.id.no_data);

       // mRecyclerView.scro
        getIntenet();



        pDialog = new ProgressDialog(this);


        recycleradapter = new SearchRecyclerAdapter(this, productheaderlist,sMemberId);
        mRecyclerView.setAdapter(recycleradapter);

        searchMedicine_data();

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

            //Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            recycleradapter.notifyDataSetChanged();
          //  Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }

    private void get_db_search(String p_medicine_name) {

        Cursor cursor_data = db.get_search_medicine(p_medicine_name);

        if ((cursor_data == null) || (cursor_data.getCount()<=0))
        {
            get_productsearch(p_medicine_name);
        }
        else
        {
            // showPdialog("Loading. . .");

            if (cursor_data.moveToFirst()) {
                productheaderlist.clear();
                productlist oproductlist = new productlist();

                oproductlist.setId("0");
                oproductlist.setPrice("");
                oproductlist.setProductname("");
                oproductlist.setForm_Name("");
                oproductlist.setPackSize("");
                oproductlist.setPackSize("");
                oproductlist.setImageExist("");
                oproductlist.setHeading("Suggested Medicines");
                oproductlist.setSearch_count("10 of 50 results");
                do {
                  oproductlist = new productlist();

                    oproductlist.setId(cursor_data.getString(cursor_data.getColumnIndex("medicine_id")));
                    oproductlist.setProductname(cursor_data.getString(cursor_data.getColumnIndex("medicine_name")));
                    oproductlist.setPrice(cursor_data.getString(cursor_data.getColumnIndex("price")));
                    oproductlist.setForm_Name(cursor_data.getString(cursor_data.getColumnIndex("form_name")));
                    oproductlist.setPackSize(cursor_data.getString(cursor_data.getColumnIndex("packsize")));
                    oproductlist.setImageExist(cursor_data.getString(cursor_data.getColumnIndex("img_url")));
                    oproductlist.setJson_type(cursor_data.getString(cursor_data.getColumnIndex("json_type")));
                    oproductlist.setHeading("");
                    oproductlist.setSearch_count("");
                    productheaderlist.add(oproductlist);
                } while (cursor_data.moveToNext());

               // product_adapter.notifyDataSetChanged();

            }

            hidePDialog();
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
        inflater.inflate(R.menu.search_medicine, menu);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));

        /*MenuItem item_cart = menu.findItem(R.id.cart_count);
        item_cart.setTitle(sAddtocart_count);
*/
        MenuItem item_search = menu.findItem(R.id.action_search);
        mysearchView = (SearchView) MenuItemCompat.getActionView(item_search);
        mysearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        mysearchView.setFocusable(true);
        mysearchView.setIconified(false);
        mysearchView.setQueryHint("Search your medicines");

        int searchPlateId = mysearchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = mysearchView.findViewById(searchPlateId);
        //if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.WHITE);
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            final TextView searchText = (TextView) searchPlate.findViewById(searchTextId);


        String s_wrd=getIntent().getStringExtra("Search_word");
        if(s_wrd!=null)
        {
            searchText.setText(s_wrd);
        }


     //   Typeface type = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        //searchText.setTypeface(type);

            if (searchText!=null) {
                searchText.setTextColor(Color.parseColor("#4e5572"));
                searchText.setHintTextColor(Color.LTGRAY);
            }

        int searchCloseID = searchPlate.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView iv_searchClose = (ImageView) searchPlate.findViewById(searchCloseID);

        iv_searchClose.setImageResource(R.drawable.close2);

        iv_searchClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(searchText.getText().toString().isEmpty())
                {
                    finish();
                }
                else{
                    searchText.setText("");
                }

            }
        });
        //}

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

    @Override
    public boolean onQueryTextChange(String newText) {



        if (newText.length() > 1 ) {
            globalVariable.setSearchWord(newText);
            get_productsearch(newText);
           // get_db_search(newText);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void get_productsearch(String sSearchText) {

        //showPdialog("Loading. . .");

        productheaderlist.clear();
        String query="";
        //String MyStrting = URLEncoder.encode(sSearchText, "UTF-8");
        try {

            query = URLEncoder.encode(sSearchText, "utf-8");
        }catch (Exception e)
        {

        }

       // String url = String.format(AppConfig.URL_GET_SEARCHPRODUCT, query, "0", "s");
        String url = String.format(AppConfig.URL_GET_NEW_SEARCHPRODUCT, query, "Q", "''");



        RequestQueue queue = Volley.newRequestQueue(BuySearchActivity.this);

        JsonObjectRequest jar_getsearchproduct = new JsonObjectRequest(Request.Method.GET,
                url,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
              //  Log.d(TAG, response.toString());

                try
                {
                    productheaderlist.clear();
                    JSONArray jsonArray_search_pro = response.getJSONArray("medicine");
                    JSONArray jsonArray_search_salt = response.getJSONArray("salt");
                    JSONArray jsonArray_search_general = response.getJSONArray("GeneralProducts");
                    if (jsonArray_search_pro.length() <= 0 && jsonArray_search_salt.length() <= 0 && jsonArray_search_general.length() <= 0) {
                        no_data.setVisibility(View.VISIBLE);
                        recycleradapter.notifyDataSetChanged();

                    }else
                    {
                        no_data.setVisibility(View.GONE);
                        load_product_list(response);
                        recycleradapter.notifyDataSetChanged();
                    }




                } catch (JSONException e) {
                    hidePDialog();
                    e.printStackTrace();
                    f_alert_ok("Error : ","Network not available");
                    //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), "main Server Error :" + error.getMessage(), Toast.LENGTH_LONG).show();

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
        new AlertDialog.Builder(BuySearchActivity.this)
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
        String sSearch_product = pref.getString("search_product", "");



        if (sSearch_product != "") {
            //load_product_list(sSearch_product);
        }
    }

    public void load_product_list(JSONObject p_search_product) {

        try {
                JSONArray jsonArray_search_salt = null;
                try {
                    jsonArray_search_salt = p_search_product.getJSONArray("salt");
                } catch (JSONException e) {
                }

                if (jsonArray_search_salt.length() <= 0) {

                    //f_alert_ok("Reason Data", "Order Data not found on sever.");
                } else {
                    productlist oproductlist = new productlist();

                    oproductlist.setId("0");
                    oproductlist.setPrice("");
                    oproductlist.setProductname("");
                    oproductlist.setForm_Name("");
                    oproductlist.setPackSize("");
                    oproductlist.setPackSize("");
                    oproductlist.setImageExist("");
                    oproductlist.setJson_type("");
                    oproductlist.setCatalogue("");
                    oproductlist.setHeading("Suggested Salt");
                    oproductlist.setSearch_count("10 of 50 results");
                    productheaderlist.add(oproductlist);
                    // Parsing json
                    for (int i = 0; i < jsonArray_search_salt.length(); i++) {
                        try {
                            JSONObject obj_json = jsonArray_search_salt.getJSONObject(i);

                            if (!obj_json.equals(null)) {

                                oproductlist = new productlist();
                                oproductlist.setId(obj_json.getString("ID"));
                                oproductlist.setPrice(obj_json.getString("MRP"));
                                oproductlist.setProductname(obj_json.getString("Primary"));
                                oproductlist.setForm_Name(obj_json.getString("ProductFormImage"));
                                oproductlist.setPackSize(obj_json.getString("packsize"));
                                oproductlist.setImageExist(obj_json.getString("MainImagePath"));
                                oproductlist.setMfg(obj_json.getString("Secondary"));
                                oproductlist.setJson_type(obj_json.getString("json_type"));
                                oproductlist.setHeading("");
                                oproductlist.setCatalogue("");
                                oproductlist.setSearch_count("");

                                productheaderlist.add(oproductlist);

                                db.insert_medicine_master(
                                        obj_json.getInt("ID"),
                                        obj_json.getString("Primary"),
                                        obj_json.getString("MRP"),
                                        obj_json.getString("packsize"),
                                        obj_json.getString("ProductFormImage"),
                                        obj_json.getString("MainImagePath"),
                                        obj_json.getString("Secondary"),
                                        obj_json.getString("json_type"),
                                        false

                                );
                            }
                        } catch (JSONException e) {
                           // Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                    oproductlist = new productlist();

                    oproductlist.setId("0");
                    oproductlist.setPrice("");
                    oproductlist.setProductname("");
                    oproductlist.setForm_Name("");
                    oproductlist.setPackSize("");
                    oproductlist.setPackSize("");
                    oproductlist.setImageExist("");
                    oproductlist.setJson_type("");
                    oproductlist.setCatalogue("");
                    oproductlist.setHeading("Show All Salts");
                    oproductlist.setSearch_count("");
                    productheaderlist.add(oproductlist);
                }

            JSONArray jsonArray_search_pro=null;
            try {
                jsonArray_search_pro = p_search_product.getJSONArray("medicine");
            }catch (JSONException e)
            {

            }


            if (jsonArray_search_pro.length() <= 0) {

                //f_alert_ok("Reason Data", "Order Data not found on sever.");
            } else {
                productlist oproductlist = new productlist();

                oproductlist.setId("0");
                oproductlist.setPrice("");
                oproductlist.setProductname("");
                oproductlist.setForm_Name("");
                oproductlist.setPackSize("");
                oproductlist.setPackSize("");
                oproductlist.setImageExist("");
                oproductlist.setCatalogue("");
                oproductlist.setHeading("Suggested Products");
                oproductlist.setSearch_count("10 of 50 results");
                oproductlist.setJson_type("");
                productheaderlist.add(oproductlist);
                // Parsing json
                for (int i = 0; i < jsonArray_search_pro.length(); i++) {
                    try {
                        JSONObject obj_json = jsonArray_search_pro.getJSONObject(i);

                        if (!obj_json.equals(null)) {


                            oproductlist = new productlist();
                            oproductlist.setId(obj_json.getString("ID"));
                            oproductlist.setPrice(obj_json.getString("MRP"));
                            oproductlist.setProductname(obj_json.getString("Primary"));
                            oproductlist.setForm_Name(obj_json.getString("ProductFormImage"));
                            oproductlist.setPackSize(obj_json.getString("packsize"));
                            oproductlist.setImageExist(obj_json.getString("MainImagePath"));
                            oproductlist.setMfg(obj_json.getString("Secondary"));
                            oproductlist.setCatalogue(obj_json.getString("Catalogue"));
                            oproductlist.setJson_type(obj_json.getString("json_type"));
                            oproductlist.setIsPharma(obj_json.getBoolean("Pharma"));
                            oproductlist.setCategoryId(obj_json.getString("Parent_ID"));
                            oproductlist.setHeading("");

                            oproductlist.setSearch_count("");


                            productheaderlist.add(oproductlist);

                            db.insert_medicine_master(
                                    obj_json.getInt("ID"),
                                    obj_json.getString("Primary"),
                                    obj_json.getString("MRP"),
                                    obj_json.getString("packsize"),
                                    obj_json.getString("ProductFormImage"),
                                    obj_json.getString("MainImagePath"),
                                    obj_json.getString("Secondary"),
                                    obj_json.getString("json_type"),
                                    obj_json.getBoolean("Pharma")
                            );
                        }
                    } catch (JSONException e) {
                       // Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                oproductlist = new productlist();

                oproductlist.setId("0");
                oproductlist.setPrice("");
                oproductlist.setProductname("");
                oproductlist.setForm_Name("");
                oproductlist.setPackSize("");
                oproductlist.setPackSize("");
                oproductlist.setCatalogue("");
                oproductlist.setImageExist("");
                oproductlist.setHeading("Show All Products");
                oproductlist.setSearch_count("");
                oproductlist.setJson_type("");
                productheaderlist.add(oproductlist);
            }

                    JSONArray jsonArray_search_general_products = null;
                    try {
                        jsonArray_search_general_products = p_search_product.getJSONArray("GeneralProducts");
                    } catch (JSONException e) {
                    }

                    if (jsonArray_search_general_products.length() <= 0) {

                        //f_alert_ok("Reason Data", "Order Data not found on sever.");
                    } else {
                        productlist oproductlist = new productlist();

                        oproductlist.setId("0");
                        oproductlist.setPrice("");
                        oproductlist.setProductname("");
                        oproductlist.setForm_Name("");
                        oproductlist.setPackSize("");
                        oproductlist.setPackSize("");
                        oproductlist.setCatalogue("");
                        oproductlist.setImageExist("");
                        oproductlist.setJson_type("");
                        oproductlist.setHeading("Suggested general products");
                        oproductlist.setSearch_count("10 of 50 results");
                        productheaderlist.add(oproductlist);
                        // Parsing json
                        for (int i = 0; i < jsonArray_search_general_products.length(); i++) {
                            try {
                                JSONObject obj_json = jsonArray_search_general_products.getJSONObject(i);

                                if (!obj_json.equals(null)) {

                                    oproductlist = new productlist();
                                    oproductlist.setId(obj_json.getString("ID"));
                                    oproductlist.setPrice(obj_json.getString("MRP"));
                                    oproductlist.setProductname(obj_json.getString("Primary"));
                                    oproductlist.setForm_Name(obj_json.getString("ProductFormImage"));
                                    oproductlist.setPackSize(obj_json.getString("packsize"));
                                    oproductlist.setCatalogue(obj_json.getString("Catalogue"));
                                    oproductlist.setImageExist(obj_json.getString("MainImagePath"));
                                    oproductlist.setMfg(obj_json.getString("Secondary"));
                                    oproductlist.setJson_type(obj_json.getString("json_type"));
                                    oproductlist.setCategoryId(obj_json.getString("Parent_ID"));
                                    oproductlist.setHeading("");
                                    oproductlist.setSearch_count("");
                                    productheaderlist.add(oproductlist);

                                    db.insert_medicine_master(
                                            obj_json.getInt("ID"),
                                            obj_json.getString("Primary"),
                                            obj_json.getString("MRP"),
                                            obj_json.getString("packsize"),
                                            obj_json.getString("ProductFormImage"),
                                            obj_json.getString("MainImagePath"),
                                            obj_json.getString("Secondary"),
                                            obj_json.getString("json_type"),
                                            false
                                    );
                                }
                            } catch (JSONException e) {
                              //  Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                        oproductlist = new productlist();

                        oproductlist.setId("0");
                        oproductlist.setPrice("");
                        oproductlist.setProductname("");
                        oproductlist.setForm_Name("");
                        oproductlist.setPackSize("");
                        oproductlist.setPackSize("");
                        oproductlist.setCatalogue("");
                        oproductlist.setImageExist("");
                        oproductlist.setJson_type("");
                        oproductlist.setHeading("Show All General Products");
                        oproductlist.setSearch_count("");
                        productheaderlist.add(oproductlist);
                        // product_adapter.notifyDataSetChanged();
                    }



        } catch (Exception e) {

            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }





    private void get_checkout() {

       // showPdialog("Loading. . .");

        //String url = "http://198.50.198.184:86/Api/Checkout/Checkout?iProductId=10152&iMemberId=184";

        String url = String.format(AppConfig.URL_GET_CHECKOUT,sMemberId);

        RequestQueue queue = Volley.newRequestQueue(BuySearchActivity.this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // hidePDialog();
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

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);
    }

    private void Success_checkout(JSONObject response) {
        try
        {

          //  Intent  buycartcheckout_intenet= new Intent(BuySearchActivity.this, NewCartSummary.class);
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
           // editor.putString("cart_checkout_ParentClass", "BuySearchActivity");
            globalVariable.setParentClass("BuySearchActivity");
            editor.commit();

           // startActivity(buycartcheckout_intenet);
        } catch (JSONException e) {
            hidePDialog();
            e.printStackTrace();
            // f_alert_ok("Error ",e.getMessage());
        }
    }

    private void Error_checkout(VolleyError error) {
        //f_alert_ok("Error ",error.getMessage());
    }
    private void searchMedicine_data() {
        Cursor cursor_data = db.get_searched_medicine_data();

        if ((cursor_data == null) || (cursor_data.getCount() <= 0)) {
            // get_productsearch(p_medicine_name);
        } else {

            cursor_data.moveToFirst();
            if (cursor_data.moveToFirst()) {
                productheaderlist.clear();
                do {
                    productlist oproductlist = new productlist();

                    oproductlist.setId(cursor_data.getString(cursor_data.getColumnIndex("medicine_id")));
                    oproductlist.setProductname(cursor_data.getString(cursor_data.getColumnIndex("medicine_name")));
                    oproductlist.setPrice(cursor_data.getString(cursor_data.getColumnIndex("price")));
                    oproductlist.setForm_Name(cursor_data.getString(cursor_data.getColumnIndex("form_name")));
                    oproductlist.setPackSize(cursor_data.getString(cursor_data.getColumnIndex("packsize")));
                    oproductlist.setImageExist(cursor_data.getString(cursor_data.getColumnIndex("img_url")));
                    oproductlist.setMfg(cursor_data.getString(cursor_data.getColumnIndex("mfg")));

                    if(cursor_data.getString(cursor_data.getColumnIndex("isPharma"))!=null)
                    {
                        oproductlist.setIsPharma(Boolean.parseBoolean(cursor_data.getString(cursor_data.getColumnIndex("isPharma"))));
                    }
                    oproductlist.setHeading("");
                    oproductlist.setSearch_count("");
                    oproductlist.setJson_type(cursor_data.getString(cursor_data.getColumnIndex("product_type")));
                    productheaderlist.add(oproductlist);
                } while (cursor_data.moveToNext());

                recycleradapter.notifyDataSetChanged();


            }
            cursor_data.close();


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
                    hideSoftKeyboard(BuySearchActivity.this);
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

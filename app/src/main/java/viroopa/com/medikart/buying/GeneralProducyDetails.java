package viroopa.com.medikart.buying;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import viroopa.com.medikart.R;
import viroopa.com.medikart.adapter.AD_adapterCombo;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.GeneralProductExpandableAdapter;
import viroopa.com.medikart.buying.adapter.adaptergeneralproductGrid;
import viroopa.com.medikart.buying.common.BadgeDrawable;
import viroopa.com.medikart.buying.model.Category;
import viroopa.com.medikart.buying.model.ItemDetail;
import viroopa.com.medikart.buying.model.M_singleorder_detail;
import viroopa.com.medikart.buying.model.productlist;
import viroopa.com.medikart.helper.SQLiteHandler;

public class GeneralProducyDetails extends AppCompatActivity {

    private int packQnty = 1;
    private JSONObject j_productInformation;
    private ProgressDialog pDialog;
    private TextView product_price;
    private TextView ProductName, gneder_name;
    private ExpandableListView expandableListView1;
    private TextView product_name;
    private TextView companyname;
    private TextView manufacturer_name;
    private TextView dosageform;
    private TextView route;
    private TextView category;
    private TextView packsize;
    private TextView type;
    private ImageView med_img;
    private View btnAddtocarts, btnplus, btnminus;
    private TextView txt_out_of_stock;
    AppController globalVariable;
    private Integer nAddtocart_count = 0;
    private Menu mToolbarMenu;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private TextView price_h, avl_packsize_txt, weight, form_name, flavour, description, Qty;
    private RelativeLayout msgfirst;
    private ViewPager pager;
    private TabLayout tabLayout;
    SQLiteHandler db;
    private AD_adapterCombo adapter_bestproduct, adapter_alternative;
    private LinearLayout size_lnr, lnr_best_pro, lnr_alternative;
    private ListView lv_best_product, lv_alternative;
    private TabHost tabs;
    private String sMemberId;
    private List<Category> catList;
    private List<productlist> LProductList = new ArrayList<productlist>();
    private ArrayList<productlist> bestproductlist = new ArrayList<productlist>();
    private ArrayList<productlist> Alternativeproductlist = new ArrayList<productlist>();
    private ArrayList<M_singleorder_detail> alternativeproductlist = new ArrayList<M_singleorder_detail>();
    private CharSequence Titles[] = {"Details", "Related Products", "Best Seller"};
    DecimalFormat df = new DecimalFormat("#.00");
    private Double Sel_Price = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_producy_details);
        globalVariable = (AppController) getApplicationContext();
        db = new SQLiteHandler(GeneralProducyDetails.this);
        initImageLoader();

        getIntenet();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        pDialog = new ProgressDialog(this);

        pager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Related Products"));
        tabLayout.addTab(tabLayout.newTab().setText("Best Seller"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setTabTextColors(Color.parseColor("#428bca"), Color.parseColor("#dfe8f9"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });


        getintent();

    }

    private void load_main_page_detial(View v) {
        product_price = (TextView) v.findViewById(R.id.product_price);
        ProductName = (TextView) v.findViewById(R.id.ProductName);
        product_name = (TextView) v.findViewById(R.id.product_name);
        gneder_name = (TextView) v.findViewById(R.id.gneder_name);
        companyname = (TextView) v.findViewById(R.id.companyname);
        manufacturer_name = (TextView) v.findViewById(R.id.manufacturer_name);
        dosageform = (TextView) v.findViewById(R.id.dosageform);
        route = (TextView) v.findViewById(R.id.route);
        category = (TextView) v.findViewById(R.id.category);
        packsize = (TextView) v.findViewById(R.id.packsize);
        type = (TextView) v.findViewById(R.id.type);
        med_img = (ImageView) v.findViewById(R.id.thumbnail);
        btnAddtocarts = v.findViewById(R.id.btnAddtocarts);
        btnplus = v.findViewById(R.id.btnplus);
        btnminus = v.findViewById(R.id.btnminus);
        txt_out_of_stock = (TextView) v.findViewById(R.id.txt_out_of_stock);
        price_h = (TextView) v.findViewById(R.id.price_h);
        avl_packsize_txt = (TextView) v.findViewById(R.id.avl_packsize_txt);
        weight = (TextView) v.findViewById(R.id.weight);
        form_name = (TextView) v.findViewById(R.id.form_name);
        msgfirst = (RelativeLayout) v.findViewById(R.id.msgfirst);
        flavour = (TextView) v.findViewById(R.id.flavour);
        size_lnr = (LinearLayout) v.findViewById(R.id.LinearLayout15);

        expandableListView1 = (ExpandableListView) v.findViewById(R.id.expandableListView1);

        description = (TextView) v.findViewById(R.id.description);
        Qty = (TextView) v.findViewById(R.id.Qty);


        btnplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                packQnty++;
                product_price.setText(String.valueOf(df.format(Sel_Price * packQnty)));
                Qty.setText(String.valueOf(packQnty));
            }
        });

        btnminus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (packQnty > 1) {
                    packQnty--;
                    product_price.setText(String.valueOf(df.format(Sel_Price * packQnty)));

                    Qty.setText(String.valueOf(packQnty));
                } else {
                    Toast.makeText(GeneralProducyDetails.this, "minimum 1 Quantity", Toast.LENGTH_LONG).show();
                }

            }
        });

        med_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                image_zoom();
            }
        });

        try {

            ProductName.setText(j_productInformation.getString("ProductName"));
            getSupportActionBar().setTitle(j_productInformation.getString("ProductName"));
            product_price.setText("Rs." + j_productInformation.getString("MRP"));
            form_name.setText(j_productInformation.getString("Category"));

            globalVariable.setImagePathForZoom(j_productInformation.getString("ProductImagePath"));
            imageLoader.displayImage(j_productInformation.getString("ProductImagePath"), med_img);

            manufacturer_name.setText(j_productInformation.getString("Manufacturer"));
            gneder_name.setText(j_productInformation.getString("Gender"));

            description.setText(j_productInformation.getJSONObject("InfomationTab").getString("ProductDescription"));

            if (j_productInformation.getJSONObject("SpecificationTab").getString("Flavour").equals("")) {
                flavour.setText("No flavour available");
            } else {
                flavour.setText(j_productInformation.getJSONObject("SpecificationTab").getString("Flavour"));
            }

            if (j_productInformation.getJSONObject("SpecificationTab").getString("MaxWeight").equals("") ||
                    j_productInformation.getJSONObject("SpecificationTab").getString("MaxWeight").equals("0.00")) {
                weight.setText("Weight is not available");
            } else {
                weight.setText(j_productInformation.getJSONObject("SpecificationTab").getString("MaxWeight"));
            }


            Double double_price = 0.00;

            if (j_productInformation.getString("MRP") != null) {
                double_price = Double.parseDouble(j_productInformation.getString("MRP"));
            }
            Sel_Price = double_price;
            product_price.setText("Rs." + " " + df.format(double_price).toString());

            if (j_productInformation.getString("MRP").equals("0") || j_productInformation.getInt("MRP") == 0) {
                txt_out_of_stock.setVisibility(View.VISIBLE);
                btnAddtocarts.setVisibility(View.GONE);
            } else {
                txt_out_of_stock.setVisibility(View.GONE);
                btnAddtocarts.setVisibility(View.VISIBLE);
            }

            Integer dbcount = db.getSearcedMedicineRowCount();


            int nFlag = Integer.parseInt(db.check_medicine_exist_search_table(Integer.valueOf(j_productInformation.getString("ProductId"))));


            if (nFlag == 0) {
                if (dbcount == 5) {
                    db.Delete_last_searched_medicine_data(1);
                    db.Insert_search_data_table(j_productInformation.getString("ProductId"), j_productInformation.getString("ProductName"),
                            j_productInformation.getString("MRP"), "", "",
                            j_productInformation.getString("ProductImagePath"), j_productInformation.getString("Manufacturer"),
                          "", "false");
                } else {
                    db.Insert_search_data_table(j_productInformation.getString("ProductId"), j_productInformation.getString("ProductName"),
                            j_productInformation.getString("MRP"), "", "",
                            j_productInformation.getString("ProductImagePath"), j_productInformation.getString("Manufacturer"),
                            "", "false");
                }
            }


            btnAddtocarts.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    try {
                        Addtocart(j_productInformation.getString("ProductId"));
                    } catch (JSONException e) {

                    }
                }
            });

        } catch (JSONException e) {

        }
        fillExpandableAdapter(expandableListView1);

    }

    private void fillExpandableAdapter(final ExpandableListView exp) {


        exp.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                setListViewHeight(exp, groupPosition);
                return false;

            }
        });


        catList = new ArrayList<Category>();
        List<ItemDetail> result = null;
        ItemDetail item = null;
        Category cat1 = null;
        JSONObject response = null;
        Object value = null;
        try {
            try {
                response = j_productInformation.getJSONObject("InfomationTab");
            } catch (JSONException e) {
            }
            result = new ArrayList<ItemDetail>();
            Iterator<String> iter = response.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    value = response.get(key);
                } catch (JSONException e) {
                }
                String ss = value.toString();
                if (!ss.equals("") && !key.equals("References") && !ss.equals("0.00") && !ss.equals("0.0")) {
                    item = new ItemDetail(1, key, value.toString(), "", "");
                    result.add(item);
                }
            }


            cat1 = new Category(1, "General Information", "");
            if (result.size() == 0) {
                item = new ItemDetail(1, "NoData", "", "", "");
                result.add(item);
            }
            cat1.setItemList(result);
            catList.add(cat1);

            response = j_productInformation.getJSONObject("SpecificationTab");
            result = new ArrayList<ItemDetail>();
            iter = null;
            iter = response.keys();
            while (iter.hasNext()) {
                String key = iter.next();

                try {

                    value = response.get(key);
                    String ss = value.toString();
                    if (!ss.equals("") && !ss.equals("0.00") && !ss.equals("0.0")) {
                        item = new ItemDetail(1, key, value.toString(), "", "");
                        result.add(item);
                    }


                } catch (JSONException e) {
                    e.toString();
                }
            }
            cat1 = new Category(1, "Specifications", "");
            if (result.size() == 0) {
                item = new ItemDetail(1, "NoData", "", "", "");
                result.add(item);
            }
            cat1.setItemList(result);
            catList.add(cat1);

            response = j_productInformation.getJSONObject("DosageTab");
            result = new ArrayList<ItemDetail>();
            iter = null;
            iter = response.keys();
            while (iter.hasNext()) {
                String key = iter.next();

                try {

                    value = response.get(key);
                    String ss = value.toString();
                    if (!ss.equals("") && !ss.equals("0.00") && !ss.equals("0.0") && !ss.equals("0.0")) {
                        item = new ItemDetail(1, key, value.toString(), "", "");
                        result.add(item);
                    } else {

                    }


                } catch (JSONException e) {
                }
            }
            cat1 = new Category(1, "Dosage", "");
            if (result.size() == 0) {
                item = new ItemDetail(1, "NoData", "", "", "");
                result.add(item);
            }
            cat1.setItemList(result);
            catList.add(cat1);


            response = j_productInformation.getJSONObject("MeasurementTab");
            result = new ArrayList<ItemDetail>();
            iter = null;
            iter = response.keys();
            while (iter.hasNext()) {
                String key = iter.next();

                try {

                    value = response.get(key);
                    String ss = value.toString();
                    if (!ss.equals("") && !ss.equals("0.00") && !ss.equals("0.0")) {
                        item = new ItemDetail(1, key, value.toString(), "", "");
                        result.add(item);
                    }


                } catch (JSONException e) {
                }
            }
            cat1 = new Category(1, "Meassurement", "");
            if (result.size() == 0) {
                item = new ItemDetail(1, "NoData", "", "", "");
                result.add(item);
            }
            cat1.setItemList(result);
            catList.add(cat1);


            response = j_productInformation.getJSONObject("PackingTab");
            result = new ArrayList<ItemDetail>();
            iter = null;
            iter = response.keys();
            while (iter.hasNext()) {
                String key = iter.next();

                try {

                    value = response.get(key);
                    String ss = value.toString();
                    if (!ss.equals("") && !ss.equals("0.00") && !ss.equals("0.0")) {
                        item = new ItemDetail(1, key, value.toString(), "", "");
                        result.add(item);

                    }

                } catch (JSONException e) {
                }
            }
            cat1 = new Category(1, "Packaging", "");
            if (result.size() == 0) {
                item = new ItemDetail(1, "NoData", "", "", "");
                result.add(item);
            }
            cat1.setItemList(result);
            catList.add(cat1);

        } catch (JSONException e) {
            e.toString();
        }

        GeneralProductExpandableAdapter exAdpt = new GeneralProductExpandableAdapter(catList, this);

        exp.setAdapter(exAdpt);
        setListViewHeight(exp);
    }


    private void get_productdetail(String iProductid) {
        showPdialog("loading...");
        if (iProductid == null) {
            iProductid = "25012";
        }

        String url = String.format(AppConfig.URL_GET_GENERAL_PRODUCT_DATA, iProductid);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_state = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //  try {
                j_productInformation = response;
                pager.setAdapter(new MyPagesAdapter(Titles));
                pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                hidePDialog();
                // } catch (Exception  e) {
                //   e.toString();
                //  }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                });

        queue.add(jsonObjReq_state);
    }

    class MyPagesAdapter extends PagerAdapter {
        CharSequence Titles[];

        public MyPagesAdapter(CharSequence mTitles[]) {

            this.Titles = mTitles;


        }

        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return 3;
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = null;

            switch (position) {
                case 0:
                    view = inflater.inflate(R.layout.g_product_page_detail, container, false);
                    load_main_page_detial(view);
                    break;
                case 1:
                    view = inflater.inflate(R.layout.general_product_altrnative, container, false);
                    GridView gridView = (GridView) view.findViewById(R.id.gridView1);
                    load_alternative_products();
                    adaptergeneralproductGrid adapter;

                    gridView.setAdapter(new adaptergeneralproductGrid(GeneralProducyDetails.this, Alternativeproductlist));

                    break;
                case 2:
                    view = inflater.inflate(R.layout.general_product_altrnative, container, false);
                    GridView gridViewBestSeller = (GridView) view.findViewById(R.id.gridView1);
                    load_best_seller_products();
                    gridViewBestSeller.setAdapter(new adaptergeneralproductGrid(GeneralProducyDetails.this, bestproductlist));
                    break;


            }

            container.addView(view);
            return view;
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object = null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }
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


    private void load_alternative_products() {


        Alternativeproductlist.clear();


        try {

            JSONArray j_bst_product = j_productInformation.getJSONArray("RelatedProducts");

            for (int i = 0; i < j_bst_product.length(); i++) {

                JSONObject obj_json = j_bst_product.getJSONObject(i);

                if (!obj_json.equals(null)) {

                    productlist O_productlist = new productlist();

                    O_productlist.setId(obj_json.getString("iProductId"));
                    O_productlist.setProductname(obj_json.getString("ProductName"));
                    O_productlist.setPrice(obj_json.getString("MRP"));
                    O_productlist.setImagePath(obj_json.getString("ProductImagePath"));

                    Alternativeproductlist.add(O_productlist);


                }
            }


        } catch (JSONException e) {
            hidePDialog();
            Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    private void load_best_seller_products() {


        bestproductlist.clear();


        try {

            JSONArray j_bst_product = j_productInformation.getJSONArray("BestSeller");

            for (int i = 0; i < j_bst_product.length(); i++) {

                JSONObject obj_json = j_bst_product.getJSONObject(i);

                if (!obj_json.equals(null)) {

                    productlist O_productlist = new productlist();

                    O_productlist.setId(obj_json.getString("iProductId"));
                    O_productlist.setProductname(obj_json.getString("ProductName"));
                    O_productlist.setPrice(obj_json.getString("MRP"));
                    O_productlist.setImagePath(obj_json.getString("ProductImagePath"));

                    bestproductlist.add(O_productlist);


                }
            }


        } catch (JSONException e) {
            hidePDialog();
            Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    private void getintent() {
        Intent i = getIntent();
        get_productdetail(i.getStringExtra("Generalproductid"));

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

    private void Addtocart(String productid) {

        showPdialog("loading");


        String url = String.format(AppConfig.URL_GET_ADDTOCARTWITHQTY, productid, sMemberId, "p", packQnty, "M");


        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_Addtocart = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
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

        jsonObjReq_Addtocart.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq_Addtocart);
    }

    private void Success_addtocart(JSONObject response) {

        try {

            SharedPreferences pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cartid", response.getString("iCartId"));
            editor.putString("addtocart_count", response.getString("TotalCartCount"));
            editor.commit();
            setNotif();
            f_alert_ok("Success", j_productInformation.getString("ProductName") + " added to the cart");


        } catch (Exception e) {

            e.printStackTrace();
            f_alert_ok("Error :", e.getMessage());
        }


    }

    private void Error_addtocart(VolleyError error) {
        f_alert_ok("Error :", error.getMessage());
    }


    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(this)
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

    private void setListViewHeight(ExpandableListView listView) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupView = listAdapter.getGroupView(i, true, null, listView);
            groupView.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupView.getMeasuredHeight();

            if (listView.isGroupExpanded(i)) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void image_zoom() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("product_name", ProductName.getText().toString());
        editor.commit();
     //   Intent Intenet_imageZoom = new Intent(this, ImageZoomProcduct.class);
      //  startActivity(Intenet_imageZoom);
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

    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        mToolbarMenu = paramMenu;
        createCartBadge(nAddtocart_count);
        return super.onPrepareOptionsMenu(paramMenu);
    }

    private void setNotif() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_cart) {
            if (nAddtocart_count > 0) {
                get_checkout();
            } else {
                f_alert_ok("Information", "Your cart is empty");
            }


        }
        if (id == R.id.order_transaction) {
            Show_Order_Transaction();
        }
        if (id == R.id.return_cancel_policies) {
            Show_Return_cancel_policies();
        }
        if (id == R.id.termscondition) {
            Show_termscondition();
        }
        return super.onOptionsItemSelected(item);
    }

    private void get_checkout() {

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_CHECKOUT, sMemberId);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
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
                        //Error_checkout(error);
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
        try {
            Intent buycartcheckout_intenet = new Intent(this, NewCartSummary.class);

            SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cart_productdetail", response.getJSONArray("ProductDetail").toString());
            editor.putString("cart_pricesummarylist", response.getJSONArray("PriceSummaryList").toString());

            globalVariable.setScartjson(response.getJSONArray("ProductDetail").toString());
            globalVariable.setSsummaryjson(response.getJSONArray("PriceSummaryList").toString());

            if (response.getString("PromoCode").toString().equals("null")) {
                editor.putString("cart_promocode", "");
                globalVariable.setsPromoCode("");
            } else {
                editor.putString("cart_promocode", response.getString("PromoCode").toString());
                globalVariable.setsPromoCode(response.getString("PromoCode").toString());
            }
            globalVariable.setParentClass("BuySearchActivity");

            editor.commit();

            startActivity(buycartcheckout_intenet);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            f_alert_ok("Error ", e.getMessage());
        }
    }

    public void Show_Order_Transaction() {
     //   Intent Intenet_change = new Intent(this, Order_Transaction.class);
     //   startActivity(Intenet_change);
    }

    public void Show_Return_cancel_policies() {
      //  Intent Intenet_change = new Intent(this, TermsCondition.class);
       // startActivity(Intenet_change);
    }

    public void Show_termscondition() {
       // Intent Intenet_change = new Intent(this, TermsCondition.class);
       // startActivity(Intenet_change);
    }


}

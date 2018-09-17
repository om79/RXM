package viroopa.com.medikart.buying;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.buying.model.productlist;
import viroopa.com.medikart.util.RangeSeekBar;

public class FilterConditions extends AppCompatActivity implements RangeSeekBar.OnValueChangeRangeListener {

    private LinearLayout lnr_show_categories;
    private LinearLayout lnr_brands;
    private String CategoryName;
    private String subCategoryId,Category_id,Brand_id="",FromPrice="0",ToPrice="0",SortBy="LH";
    private RangeSeekBar rangeSeekBar;
    private TextView txt_min_max_range;
    private ProgressDialog pDialog;
    private  static int FILTER_VALUE_SELECTED_CODE=32;
    private Snackbar snack;

    private   LinearLayout horizotal_scroll;
    private   TextView tv ;
    private TextView txt_close_page;
    private Button btn_apply_filter;
    private RadioGroup rdo_grp;




    private ArrayList<TextView> Textviewlist = new ArrayList<TextView>();
    private ArrayList<TextView> SelectedBrandlistTextview = new ArrayList<TextView>();
    private ArrayList<String> SelectedBrands= new ArrayList<String>();
    private ArrayList<productlist> productCategoryList = new ArrayList<productlist>();
    private ArrayList<productlist> productBrand = new ArrayList<productlist>();
    private LinearLayout.LayoutParams lnr_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_filter_conditions);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        lnr_show_categories = (LinearLayout) findViewById(R.id.lnr_show_categories);
        rangeSeekBar = (RangeSeekBar) findViewById(R.id.rangeBar);
        txt_min_max_range = (TextView) findViewById(R.id.txt_min_max_range);
        lnr_brands=(LinearLayout) findViewById(R.id.lnr_brands);
        txt_close_page= (TextView) findViewById(R.id.txt_close_page);
        btn_apply_filter= (Button) findViewById(R.id.btn_apply_filter);
        rdo_grp = (RadioGroup) findViewById(R.id.rdo_grp);

        rangeSeekBar.InitialiseCallBack(this);
        rdo_grp.check(R.id.rdo_lh);


        rdo_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==0)
                {
                    SortBy="LH";
                }else {
                    SortBy="HL";
                }
            }
        });


        Intent i=getIntent();

        fill_category_list(i.getStringExtra("Category_id"));
        fill_Brand_list(i.getStringExtra("subCategoryId"));
        CategoryName=i.getStringExtra("CategoryName");

        txt_close_page.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               finish();
            }
        });

        btn_apply_filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (SelectedBrands.size() > 0) {
                    Brand_id=TextUtils.join(",",  SelectedBrands);
                }
                Intent intent= new Intent(FilterConditions.this, GeneralProductSearchPage.class);

                intent.putExtra("Categoryname",CategoryName);
                intent.putExtra("subCategoryId",subCategoryId);
                intent.putExtra("CategoryId",Category_id);
                intent.putExtra("SortBy",SortBy);
                intent.putExtra("BrandId",Brand_id);
                intent.putExtra("Fromprice",FromPrice);
                intent.putExtra("Toprice",ToPrice);


                startActivityForResult(intent, FILTER_VALUE_SELECTED_CODE);
                finish();


            }
        });


    }



    @Override
    public void onRangeValueChange(int MinValue, int MaxValue) {

      FromPrice=String.valueOf(MinValue);
       ToPrice=String.valueOf(MaxValue);
        txt_min_max_range.setText("Rs." + MinValue + " - " + "Rs." + MaxValue);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void show_filter_optiions( ArrayList<productlist> Titles,LinearLayout lnr_main,final String cat_name)
    {
        SelectedBrands.clear();
        SelectedBrandlistTextview.clear();
        lnr_main.removeAllViews();
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, LinearLayout.LayoutParams.WRAP_CONTENT);
      //  params.weight=1;
        params.gravity=Gravity.CENTER;
        params.setMargins(8,8,8,8);
        params.weight=1;

        int count=0;
        LinearLayout rw=new LinearLayout(this);
        rw.setOrientation(LinearLayout.HORIZONTAL);

        for (int i=0;i<Titles.size();i++) {
            tv = new TextView(this);
            final productlist feedItem = Titles.get(i);
            tv.setText(feedItem.getProductname());
            tv.setBackgroundColor(getResources().getColor(R.color.sky_blue));
            tv.setTag(R.id.key_product_id,feedItem.getId());
            tv.setTextSize(12);
            if(!cat_name.equals("Category")) {
                tv.setId(i);

            }else
            {
                tv.setTag(R.id.key_category_name,feedItem.getProductname());
                if(CategoryName.equals(feedItem.getProductname()))
                {
                    tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    tv.setTextColor(getResources().getColor(R.color.white));
                    tv.setTag(R.id.key_is_pressed, "true");
                }
            }
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setMaxLines(1);
            tv.setSingleLine(true);
            tv.setTextColor(getResources().getColor(R.color.rx_medikart_small_text_half_dark));
            tv.setPadding(5,8,5,8);
            tv.setLayoutParams(params);
            tv.setTag(R.id.key_is_pressed,"false");

            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {



                    if((view.getTag(R.id.key_is_pressed)).equals("false")) {

                        if(cat_name.equals("Category")) {
                            for(int i=0;i<Textviewlist.size();i++)
                            {
                                Textviewlist.get(i).setTextColor(getResources().getColor(R.color.rx_medikart_small_text_half_dark));
                                Textviewlist.get(i).setBackgroundColor(getResources().getColor(R.color.sky_blue));
                            }
                            CategoryName=((String) view.getTag(R.id.key_category_name));
                            fill_Brand_list(((String) view.getTag(R.id.key_product_id)));
                            if(snack!=null) {
                                snack.dismiss();
                                snack = null;
                            }
                            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                            view.setTag(R.id.key_is_pressed, "true");
                        }else {

                            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            ((TextView) view).setTextColor(getResources().getColor(R.color.white));

                            view.setTag(R.id.key_is_pressed, "true");
                         //   Snackbar.make(findViewById(R.id.container), "Hey there!", Snackbar.LENGTH_LONG).show();

                            lnr_params.setMargins(5,5,5,5);
                            lnr_params.gravity=Gravity.CENTER;
                            TextView txt_view=new TextView(FilterConditions.this);

                            txt_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            txt_view.setTextColor(getResources().getColor(R.color.white));
                            txt_view.setText( ((TextView) view).getText().toString());
                            txt_view.setPadding(5,8,5,8);
                            txt_view.setSingleLine(true);
                            txt_view.setTextSize(12);
                            if(!cat_name.equals("Category")) {
                                txt_view.setId(view.getId());
                                txt_view.setTag(R.id.key_brand_id,feedItem.getId());
                            }

                            txt_view.setLayoutParams(lnr_params);
                          SelectedBrands.add((String)txt_view.getTag(R.id.key_brand_id));

                          SelectedBrandlistTextview.add(txt_view);
                            create_snackBar();

                          //  mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);



                        }

                    }else {
                        view.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                        ((TextView) view).setTextColor(getResources().getColor(R.color.rx_medikart_small_text_half_dark));
                        view.setTag(R.id.key_is_pressed,"false");
                        if(!cat_name.equals("Category")) {
                            if (snack != null) {
                                TextView vw = (TextView) snack.getView().findViewById(view.getId());

                               SelectedBrandlistTextview.remove(vw);
                               SelectedBrands.remove((String) vw.getTag(R.id.key_brand_id));
                                create_snackBar();
                            }
                        }
                    }
                }
            });


            if(count==2)
            {
                rw.addView(tv);
                Textviewlist.add(tv);
                lnr_main.addView(rw);
                rw=new LinearLayout(this);
                rw.setOrientation(LinearLayout.HORIZONTAL);
                count=0;
            }else {
                rw.addView(tv);
                Textviewlist.add(tv);
                count++;
            }

        }
        lnr_main.addView(rw);



    }

    private  void fill_category_list(String CategoryId )
    {
       Category_id=CategoryId;
     showPdialog("loading....");

        String url = String.format(AppConfig.URL_GET_GENERAL_PRODUCT_CATEGORY_LIST,CategoryId );

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonObjReq_state = new JsonArrayRequest(Request.Method.GET,
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj_json = response.getJSONObject(i);

                        if (!obj_json.equals(null)) {

                            productlist O_productlist = new productlist();

                            O_productlist.setId(obj_json.getString("SubCategoryId"));
                            O_productlist.setProductname(obj_json.getString("SubCategorydesc"));

                            productCategoryList.add(O_productlist);



                        }
                    }
                    show_filter_optiions(productCategoryList,lnr_show_categories,"Category");
                    hidePDialog();
                }catch (JSONException e){  hidePDialog();}
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
    private  void fill_Brand_list(String SubCategoryId)
    {
        subCategoryId=SubCategoryId;
        showPdialog("loading....");

        productBrand.clear();
        String url = String.format(AppConfig.URL_GET_GENERAL_PRODUCT_BRAND_LIST,SubCategoryId,Category_id );

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonObjReq_state = new JsonArrayRequest(Request.Method.GET,
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj_json = response.getJSONObject(i);

                        if (!obj_json.equals(null)) {

                            productlist O_productlist = new productlist();

                            O_productlist.setId(obj_json.getString("BrandId"));
                            O_productlist.setProductname(obj_json.getString("BrandName"));

                            productBrand.add(O_productlist);



                        }
                    }
                    show_filter_optiions(productBrand,lnr_brands,"Brands");
                 hidePDialog();

                }catch (JSONException e){
                    hidePDialog();
                }


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
   private  void create_snackBar()
   {

    if(snack==null)
    {
        snack = Snackbar.make(findViewById(R.id.container), "", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snack.getView();
        View Sview = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)Sview.getLayoutParams();
        params.gravity = Gravity.TOP;
        Sview.setLayoutParams(params);
        Sview.setBackgroundColor(Color.WHITE);
        LayoutInflater mInflater = LayoutInflater.from(FilterConditions.this);
        View snackView = mInflater.inflate(R.layout.snack_bar_filter, null);

       horizotal_scroll=(LinearLayout)snackView.findViewById(R.id.horizotal_scroll);
        TextView txt_close=(TextView) snackView.findViewById(R.id.txt_close);

        txt_close.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            snack.dismiss();
            snack=null;
           SelectedBrandlistTextview.clear();
           SelectedBrands.clear();
            try {
                show_filter_optiions(productBrand, lnr_brands, "Brands");
            }catch (Exception e)
            {

            }
        }
    });

        horizotal_scroll.removeAllViews();
        for(int i=0; i< SelectedBrandlistTextview.size();i++)
        {
           SelectedBrandlistTextview.get(i).setOnTouchListener(iv_click);
            horizotal_scroll.addView( SelectedBrandlistTextview.get(i));
        }







        layout.addView(snackView,0);

        snack.show();
    }else {
        horizotal_scroll=(LinearLayout) snack.getView().findViewById(R.id.horizotal_scroll);
        horizotal_scroll.removeAllViews();
        if(SelectedBrandlistTextview.size()==0)
        {
            snack.dismiss();
            snack=null;
        }
        for(int i=0; i< SelectedBrandlistTextview.size();i++)
        {
           SelectedBrandlistTextview.get(i).setOnTouchListener(iv_click);
            horizotal_scroll.addView( SelectedBrandlistTextview.get(i));
        }
    }

   }
    View.OnTouchListener iv_click = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {

                    break;
                }
                case MotionEvent.ACTION_UP: {

                    snackbar_click_item(v);
                    break;

                }
                case MotionEvent.ACTION_CANCEL: {

                    break;
                }
            }
            return true;
        }
    };

    private  void snackbar_click_item(View v)
    {
       SelectedBrandlistTextview.remove(v);
       SelectedBrands.remove((String)v.getTag(R.id.key_brand_id));
        create_snackBar();
        TextView vw=(TextView)lnr_brands.findViewById(v.getId()) ;

        vw.setTag(R.id.key_is_pressed,"false");
        vw.setBackgroundColor(getResources().getColor(R.color.sky_blue));
        vw.setTextColor(getResources().getColor(R.color.rx_medikart_small_text_half_dark));

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
       // Toast.makeText(getApplicationContext(), "Exit without applying filter", Toast.LENGTH_LONG)
               // .show();
    }

    private  void fill_range_bar()
    {
        rangeSeekBar.setSelectedMinValue(5);
        rangeSeekBar.setSelectedMaxValue(500);
    }
}

package viroopa.com.medikart.buying.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.BuyTabs;
import viroopa.com.medikart.buying.adapter.adapterpacksize;
import viroopa.com.medikart.buying.model.M_packetsize;
import viroopa.com.medikart.helper.SQLiteHandler;

public class DetailFragment extends Fragment {

    private String productid,productname, sMemberId;

    private static final String MEMBERID = "";
    private static final String ARG_POSITION = "position";
    private TextView ProductName;
    private SQLiteHandler db;
    private int packQnty=1;
    private RadioGroup horizontal_buttons;
    private Double Sel_Price=0.00;
    private LinearLayout lnr_no_pack_size;
    DecimalFormat df = new DecimalFormat("#.00");
    //private static final String PRODUCTJSON = "productjson";
    //private static final String PACKSIZEJSON = "packsizejson";

    private int position,nTotalCartCount,nCartId;
    private String sproductdetial,spacksize ;

    private TextView firstbtn;
    private TextView secondbtn;
    private TextView thirdbtn;
    private TextView fourthbtnpe;
    private TextView fifthbtn;

    private ProgressDialog pDialog;
    static Button notifCount,btnplus,btnminus;
    private  TextView Qty;
    static int mNotifCount = 0;
    AppController globalVariable;
    private Menu mToolbarMenu;
    Integer nAddtocart_count = 0;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    List<M_packetsize> packetsizelist = new ArrayList<M_packetsize>();
    adapterpacksize adapter ;

    RelativeLayout rl_no_size_available;

    public static DetailFragment newInstance(int position,String MemberId) {

        DetailFragment f = new DetailFragment();
        Bundle b = new Bundle();

        // sMemberId  = MemberId ;

        b.putInt(ARG_POSITION, position);
        //b.putString(PRODUCTJSON, ja_objproductsubdetails.toString());
        //b.putString(PACKSIZEJSON, ja_objPackSizedetails.toString());
        b.putString(MEMBERID, MemberId);

        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);

        SharedPreferences pref = getActivity().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setNotifCount(int count){
        mNotifCount = count;

        //notifCount.setText(String.valueOf(mNotifCount));
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(ARG_POSITION);
        sMemberId = getArguments().getString(MEMBERID);
        globalVariable = (AppController) getActivity().getApplicationContext();
        db = new SQLiteHandler(getActivity());


       if(globalVariable.getProduct_packsizedetails()!=null) {
           spacksize = globalVariable.getProduct_packsizedetails();
       }else{
           spacksize="[]";
       }

        if(globalVariable.getProduct_subdetails()!=null) {
            sproductdetial = globalVariable.getProduct_subdetails();
        }else{
            sproductdetial="[]";
        }

        View rootView ;
        rootView = inflater.inflate(R.layout.page_detail, container, false);
        initImageLoader();
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        rl_no_size_available  = (RelativeLayout) rootView.findViewById(R.id.rl_no_size_available);

        final TextView product_price  = (TextView) rootView.findViewById(R.id.product_price);
        final TextView ProductName  = (TextView) rootView.findViewById(R.id.ProductName);
        final TextView product_name  = (TextView) rootView.findViewById(R.id.product_name);
        final TextView companyname   = (TextView) rootView.findViewById(R.id.companyname);
        final TextView form          = (TextView) rootView.findViewById(R.id.form);
        final TextView dosageform    = (TextView) rootView.findViewById(R.id.dosageform);
        final TextView route         = (TextView) rootView.findViewById(R.id.route);
        final TextView category      = (TextView) rootView.findViewById(R.id.category);
        final TextView packsize      = (TextView) rootView.findViewById(R.id.packsize);
        final TextView type          = (TextView) rootView.findViewById(R.id.type);
        final ImageView med_img      = (ImageView) rootView.findViewById(R.id.thumbnail);
        final View btnAddtocarts = rootView.findViewById(R.id.btnAddtocarts);
        final TextView txt_out_of_stock= (TextView) rootView.findViewById(R.id.txt_out_of_stock);


         firstbtn        = (TextView) rootView.findViewById(R.id.firstbtn);
         secondbtn       = (TextView) rootView.findViewById(R.id.secondbtn);
         thirdbtn        = (TextView) rootView.findViewById(R.id.thirdbtn);
         fourthbtnpe     = (TextView) rootView.findViewById(R.id.fourthbtn);
         fifthbtn        = (TextView) rootView.findViewById(R.id.fifthbtn);

        Qty        = (TextView) rootView.findViewById(R.id.Qty);

        btnplus            = (Button) rootView.findViewById(R.id.btnplus);
        btnminus           = (Button) rootView.findViewById(R.id.btnminus);
        lnr_no_pack_size   = (LinearLayout) rootView.findViewById(R.id.lnr_no_pack_size);
        horizontal_buttons = (RadioGroup) rootView.findViewById(R.id.horizontal_buttons);

       // firstbtn.setBackgroundColor(Color.parseColor("#08446a"));
        //firstbtn.setTextColor(Color.WHITE);
        getintent();
        Drawable loginActivityBackground = rootView.findViewById(R.id.mainrel).getBackground();
        loginActivityBackground.setAlpha(127);



        View.OnTouchListener iv_click = new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {




                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                     //   f_Touch_Down(v);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        TextView iv = (TextView)v;
                        int id = iv.getId();
                        iv.setBackgroundColor(Color.parseColor("#08446a"));
                        iv.setTextColor(Color.WHITE);

                        if(iv!=firstbtn)
                        {
                            firstbtn.setBackgroundResource(R.drawable.list_row_selector);
                            firstbtn.setTextColor(Color.parseColor("#484848"));

                        }
                        if(iv!=secondbtn)
                        {
                            secondbtn.setBackgroundResource(R.drawable.list_row_selector);
                            secondbtn.setTextColor(Color.parseColor("#484848"));
                        }
                        if(iv!=thirdbtn)
                        {
                            thirdbtn.setBackgroundResource(R.drawable.list_row_selector);
                            thirdbtn.setTextColor(Color.parseColor("#484848"));
                        }
                        if(iv!=fourthbtnpe)
                        {
                            fourthbtnpe.setBackgroundResource(R.drawable.list_row_selector);
                            fourthbtnpe.setTextColor(Color.parseColor("#484848"));
                        }
                        if(iv!=fifthbtn)
                        {
                            fifthbtn.setBackgroundResource(R.drawable.list_row_selector);
                            fifthbtn.setTextColor(Color.parseColor("#484848"));
                        }
                        String productid = (String) v.getTag(R.id.key_product_id);
                        get_singleproduct(productid);

                    }
                    case MotionEvent.ACTION_CANCEL: {
                       // f_Touch_Cancel(v);
                        break;
                    }
                }
                return true;



            }
        };


        firstbtn.setOnTouchListener(iv_click);
        secondbtn.setOnTouchListener(iv_click);
        thirdbtn.setOnTouchListener(iv_click);
        fourthbtnpe.setOnTouchListener(iv_click);
        fifthbtn.setOnTouchListener(iv_click);




        btnplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                packQnty++;
                product_price.setText(String.valueOf(df.format(Sel_Price * packQnty)));
                Qty.setText(String.valueOf(packQnty));
            }
        });

        btnminus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(  packQnty>1) {
                    packQnty--;
                    product_price.setText(String.valueOf(df.format(Sel_Price*packQnty)));

                    Qty.setText(String.valueOf(packQnty));
                }else{
                    Toast.makeText(getActivity(), "minimum 1 Quantity", Toast.LENGTH_LONG).show();
                }

            }
        });


        med_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
             image_zoom();
            }
        });


        try {
            JSONArray ja_product = new JSONArray(sproductdetial);

            String sRupee = "Rs.";

            for (int i = 0; i < ja_product.length(); i++) {
                JSONObject obj_json = ja_product.getJSONObject(i);


                Double double_price=0.00;

                if(obj_json.getString("Price")!=null) {
                    double_price = Double.parseDouble(obj_json.getString("Price"));
                }
                Sel_Price=double_price;
                product_price.setText(sRupee + " " + df.format(double_price).toString());

                if(obj_json.getString("Price").equals("0"))
                {
                    txt_out_of_stock.setVisibility(View.VISIBLE);
                    btnAddtocarts.setVisibility(View.GONE);
                }else
                {
                    txt_out_of_stock.setVisibility(View.GONE);
                    btnAddtocarts.setVisibility(View.VISIBLE);
                }

               // product_price.setText(sRupee + " " + obj_json.getString("Price"));

                product_name.setText(obj_json.getString("SearchName"));
                ProductName.setText(obj_json.getString("SearchName"));
                companyname.setText(obj_json.getString("ManufactureName"));
                form.setText(obj_json.getString("Form_Name"));
                dosageform.setText(obj_json.getString("DosageForm_Name"));
                route.setText(obj_json.getString("RouteofAdministration"));
                category.setText(obj_json.getString("Category"));
                packsize.setText(obj_json.getString("PackSize"));
                type.setText(obj_json.getString("Type"));
                productid = obj_json.getString("Id");
                productname = obj_json.getString("SearchName");

                if(obj_json.getString("ProductImagePath")!=null) {
                    globalVariable.setImagePathForZoom(obj_json.getString("ProductImagePath"));
                    imageLoader.displayImage(obj_json.getString("ProductImagePath"), med_img);
                }


                Integer dbcount = db.getSearcedMedicineRowCount();



                int nFlag = Integer.parseInt(db.check_medicine_exist_search_table(Integer.valueOf(productid)));


                if(nFlag==0) {
                    if (dbcount == 5) {
                        db.Delete_last_searched_medicine_data(1);
                        db.Insert_search_data_table(productid, productname, obj_json.getString("Price"), obj_json.getString("Form_Name"), obj_json.getString("PackSize"), obj_json.getString("ProductImagePath"), obj_json.getString("ManufactureName"),obj_json.getString("Form_Name"),"true");
                    } else {
                        db.Insert_search_data_table(productid, productname, obj_json.getString("Price"), obj_json.getString("Form_Name"), obj_json.getString("PackSize"),obj_json.getString("ProductImagePath"),obj_json.getString("ManufactureName"),obj_json.getString("Form_Name"),"true");
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        btnAddtocarts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Addtocart();
            }
        });

      //  adapter = new adapterpacksize(getActivity(),packetsizelist,sMemberId);
       // ListView UI_lv = (ListView)rootView.findViewById(R.id.list_packsize);
       // UI_lv.setAdapter(adapter);

        packSizeCheck();
      //  ListUtils.setDynamicHeight(UI_lv);
        return rootView;
    }

    private void packSizeCheck(){
        try {
            packetsizelist.clear();

            if(!spacksize.equals("[]")) {
                lnr_no_pack_size.setVisibility(View.GONE);
                horizontal_buttons.setVisibility(View.VISIBLE);

                JSONArray ja_packsize = new JSONArray(spacksize);

                if (ja_packsize.length() > 0) {
                    rl_no_size_available.setVisibility(View.GONE);
                }

                for (int i = 0; i < ja_packsize.length(); i++) {

                    JSONObject obj_json = ja_packsize.getJSONObject(i);

                  if(i==0)
                  {
                      firstbtn.setTag(R.id.key_product_id, obj_json.getString("PackSizeProductId"));
                      firstbtn.setText(obj_json.getString("Packsizeproduct"));

                      firstbtn.setVisibility(View.VISIBLE);
                      if(obj_json.getString("PackSizeProductId").equals(productid)) {
                          firstbtn.setBackgroundColor(Color.parseColor("#08446a"));
                          firstbtn.setTextColor(Color.WHITE);
                          firstbtn.setClickable(false);
                          firstbtn.setFocusable(false);
                          firstbtn.setEnabled(false);
                      }
                      secondbtn.setVisibility(View.GONE);
                      thirdbtn.setVisibility(View.GONE);
                      fourthbtnpe.setVisibility(View.GONE);
                      fifthbtn.setVisibility(View.GONE);
                  }
                    if(i==1)
                    {
                        secondbtn.setTag(R.id.key_product_id,obj_json.getString("PackSizeProductId"));
                        secondbtn.setText(obj_json.getString("Packsizeproduct"));

                        if(obj_json.getString("PackSizeProductId").equals(productid)) {
                            secondbtn.setBackgroundColor(Color.parseColor("#08446a"));
                            secondbtn.setTextColor(Color.WHITE);
                            secondbtn.setClickable(false);
                            secondbtn.setFocusable(false);
                            secondbtn.setEnabled(false);
                        }

                      //  firstbtn.setVisibility(View.GONE);
                        secondbtn.setVisibility(View.VISIBLE);
                      //  thirdbtn.setVisibility(View.GONE);
                       // fourthbtnpe.setVisibility(View.GONE);
                        //fifthbtn.setVisibility(View.GONE);
                    }
                    if(i==2)
                    {
                        thirdbtn.setTag(R.id.key_product_id,obj_json.getString("PackSizeProductId"));
                        thirdbtn.setText(obj_json.getString("Packsizeproduct"));

                        if(obj_json.getString("PackSizeProductId").equals(productid)) {
                            thirdbtn.setBackgroundColor(Color.parseColor("#08446a"));
                            thirdbtn.setTextColor(Color.WHITE);
                            thirdbtn.setClickable(false);
                            thirdbtn.setFocusable(false);
                            thirdbtn.setEnabled(false);
                        }

                        //firstbtn.setVisibility(View.GONE);
                       // secondbtn.setVisibility(View.GONE);
                        thirdbtn.setVisibility(View.VISIBLE);
                       // fourthbtnpe.setVisibility(View.GONE);
                      //  fifthbtn.setVisibility(View.GONE);
                    }
                    if(i==3)
                    {
                        fourthbtnpe.setTag(R.id.key_product_id,obj_json.getString("PackSizeProductId"));
                        fourthbtnpe.setText(obj_json.getString("Packsizeproduct"));

                        if(obj_json.getString("PackSizeProductId").equals(productid)) {
                            fourthbtnpe.setBackgroundColor(Color.parseColor("#08446a"));
                            fourthbtnpe.setTextColor(Color.WHITE);
                            fourthbtnpe.setClickable(false);
                            fourthbtnpe.setFocusable(false);
                            fourthbtnpe.setEnabled(false);
                        }

                       // firstbtn.setVisibility(View.GONE);
                       // secondbtn.setVisibility(View.GONE);
                       // thirdbtn.setVisibility(View.GONE);
                        fourthbtnpe.setVisibility(View.VISIBLE);
                       // fifthbtn.setVisibility(View.GONE);
                    }
                    if(i==4)
                    {
                        fifthbtn.setTag(R.id.key_product_id,obj_json.getString("PackSizeProductId"));
                        fifthbtn.setText(obj_json.getString("Packsizeproduct"));

                        if(obj_json.getString("PackSizeProductId").equals(productid)) {
                            fifthbtn.setBackgroundColor(Color.parseColor("#08446a"));
                            fifthbtn.setTextColor(Color.WHITE);
                            fifthbtn.setClickable(false);
                            fifthbtn.setFocusable(false);
                            fifthbtn.setEnabled(false);
                        }

                      //  firstbtn.setVisibility(View.GONE);
                      //  secondbtn.setVisibility(View.GONE);
                      //  thirdbtn.setVisibility(View.GONE);
                      //  fourthbtnpe.setVisibility(View.GONE);
                        fifthbtn.setVisibility(View.VISIBLE);
                    }


                  /*  opacketsize.setPackSize(obj_json.getString("Packsizeproduct"));
                    opacketsize.setPrice(obj_json.getString("PackSizePrice"));
                    opacketsize.setProductId(obj_json.getString("PackSizeProductId"));*/


                }
            }else{
                lnr_no_pack_size.setVisibility(View.VISIBLE);
                horizontal_buttons.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
    private void showcart(){
        TextView tv = new TextView(getActivity());
        tv.setText("rs" + " " + counter);
        tv.setTextColor(Color.WHITE);
        tv.setOnClickListener(this);
        tv.setPadding(5, 0, 5, 0);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(20);
        menu.add(0, 232, 1, R.string.app_name).setActionView(tv)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }
    */
    private void  Addtocart() {

        showPdialog("loading");


        String url = String.format(AppConfig.URL_GET_ADDTOCARTWITHQTY, productid, sMemberId,"p",String.valueOf(packQnty),"M");


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjReq_Addtocart = new JsonObjectRequest(Request.Method.GET,
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

        jsonObjReq_Addtocart.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_Addtocart);

        queue.add(jsonObjReq_Addtocart);
    }

    private void Success_addtocart(JSONObject response) {
        //final messagebox mb = new messagebox();
        Integer i =0 ;

        try {
            //Integer.parseInt(et.getText().toString());

            nTotalCartCount = Integer.parseInt(response.getString("TotalCartCount"));
            nCartId = Integer.parseInt(response.getString("iCartId"));
            //JSONArray ja_cart = response.getJSONArray("objproductsubdetails");

            SharedPreferences pref = this.getActivity().getSharedPreferences("Global",  Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cartid", response.getString("iCartId"));
            editor.putString("addtocart_count", response.getString("TotalCartCount"));
            editor.commit();

            setNotifCount(nTotalCartCount);

            f_alert_ok("Success", productname + " added to the cart");


        } catch (Exception e) {

            e.printStackTrace();
            f_alert_ok("Error :", e.getMessage());
        }


    }

    private void Error_addtocart(VolleyError error) {
        f_alert_ok("Error :", error.getMessage());
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
        new AlertDialog.Builder(getActivity())
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                getActivity()).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void getintent()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("Global", Context.MODE_PRIVATE);
        String productName=pref.getString("product_name", "");
        //ProductName.setText(productName);
    }

    private void get_singleproduct(String p_productid) {
        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_PRODUCTDETAIL, p_productid, sMemberId);



        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hidePDialog();
                Success_singleorder(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Toast.makeText(getActivity(), "Something went wrong please try after sometime.", Toast.LENGTH_LONG).show();
                    }
                });

//        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
//                50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);

    }

    private void Success_singleorder(JSONObject response) {
        try {
            SharedPreferences pref = globalVariable.getSharedPreference();
            SharedPreferences.Editor prefsEditor = pref.edit();

            globalVariable.setProduct_moleculedetails(response.getJSONArray("objMoleculedetails").toString());
            globalVariable.setProduct_medicinedetails(response.getJSONArray("objMedicinedetails").toString());
            globalVariable.setProduct_packsizedetails(response.getJSONArray("objPackSizedetails").toString());
            globalVariable.setProduct_otherproductdetails(response.getJSONArray("objOtherProductdetails").toString());
            globalVariable.setProduct_subdetails(response.getJSONArray("objproductsubdetails").toString());
            globalVariable.setDrugInteraction(response.getJSONArray("objDrugDrugInteraction").toString());
            globalVariable.setMoleculeinteraction(response.getJSONArray("objDrugMoleculeInteraction").toString());


            prefsEditor.putString("product_name", productname);
            prefsEditor.commit();

            Intent buytabsIntenet = new Intent(getActivity(), BuyTabs.class);
            buytabsIntenet.putExtra("productid", productid);

            getActivity().startActivity(buytabsIntenet);
            getActivity().finish();


        } catch (JSONException e) {
            hidePDialog();
            e.printStackTrace();
            f_alert_ok("Error : ",e.getMessage());
        }
    }

    private  void image_zoom()
    {
       // Intent Intenet_imageZoom = new Intent(getActivity(), ImageZoomProcduct.class);

       // startActivity(Intenet_imageZoom);
    }


    private void get_plus_minus_quantity(String plus_minus, String p_productid) {
        if (!p_productid.equals(""))
        {
            showPdialog("Loading. . .");

            String url="" ;//= String.format(AppConfig.URL_GET_PLUSMINUSQTY, p_productid, sCardId, plus_minus);

            RequestQueue queue = Volley.newRequestQueue(getActivity());


            JsonObjectRequest jsonObjReq_pm = new JsonObjectRequest(Request.Method.GET,
                    url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    success_plus_minus();
                    hidePDialog();
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidePDialog();
                           // Error_plus_minus(error);
                        }
                    });

        jsonObjReq_pm.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjReq_pm);

        }
    }
    private void success_plus_minus()
    {

    }

}
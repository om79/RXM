package viroopa.com.medikart.buying.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.BuyTabs;
import viroopa.com.medikart.buying.model.M_productalternative;

/**
 * Created by prakash on 13/08/15.
 */
public class adapterproductalternative extends BaseAdapter {

    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private String productid,productname, jsonresponse,sMemberId;
    private Activity currentactivity;
    private LayoutInflater inflater;
    AppController globalVariable;


    private List<M_productalternative> arraylist;
    private int nTotalCartCount,nCartId;
    static int mNotifCount = 0;

    private ProgressDialog pDialog;


    private List<M_productalternative> alternativeitems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public adapterproductalternative(Activity currentactivity, List<M_productalternative> calternativeitems, String MemberId) {
        this.currentactivity = currentactivity;
        this.alternativeitems = calternativeitems;
        this.sMemberId = MemberId;
        globalVariable = (AppController)currentactivity.getApplicationContext();
        pDialog = new ProgressDialog(currentactivity);
        this.arraylist = new ArrayList<M_productalternative>();
        this.arraylist.addAll(alternativeitems);
        initImageLoader();
    }


    @Override
    public int getCount() {
        return alternativeitems.size();
    }

    @Override
    public Object getItem(int location) {
        return alternativeitems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (inflater == null)
            inflater = (LayoutInflater) currentactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.page_alternative_item, null);
        }
        // prakash
        //if (imageLoader == null)
        //    imageLoader = AppController.getInstance().getImageLoader();


        // getting Invoice data for the row
        M_productalternative O_productalternative = alternativeitems.get(position);
        if (O_productalternative != null) {


            RelativeLayout sub= (RelativeLayout) convertView.findViewById(R.id.sub);
            TextView description = (TextView) convertView.findViewById(R.id.txt_description);
           TextView packsize = (TextView) convertView.findViewById(R.id.txt_size);
           TextView price = (TextView) convertView.findViewById(R.id.txt_price);
           TextView txt_mfg = (TextView) convertView.findViewById(R.id.txt_mfg);
           TextView txt_more_percent = (TextView) convertView.findViewById(R.id.txt_more_percent);
           TextView txt_out_of_stock= (TextView) convertView.findViewById(R.id.txt_out_of_stock);

            RelativeLayout btnaddtocart = (RelativeLayout) convertView.findViewById(R.id.btnaddtocart);

            String sRupee = "Rs.";

            description.setText(O_productalternative.getOtherProductName());
            packsize.setText(O_productalternative.getOtherPackSizeproduct());

            Double double_price=0.00;
            DecimalFormat df = new DecimalFormat("#.00");
            if( O_productalternative.getOtherPrice()!=null) {
                double_price = Double.parseDouble( O_productalternative.getOtherPrice());
            }
            price.setText(sRupee + " " + df.format(double_price).toString());

            if(O_productalternative.getOtherPrice().equals("0"))
            {
                txt_out_of_stock.setVisibility(View.VISIBLE);
                btnaddtocart.setEnabled(false);
            }else
            {
                txt_out_of_stock.setVisibility(View.GONE);
                btnaddtocart.setEnabled(true);
            }
            txt_mfg.setText(O_productalternative.getOtherManufacture());
           // price.setText(sRupee + " " + O_productalternative.getOtherPrice());



            //btnaddtocart.setTag(O_productalternative.getOtherProductId());
            btnaddtocart.setTag(R.id.key_product_id, O_productalternative.getOtherProductId());
            btnaddtocart.setTag(R.id.key_product_name, O_productalternative.getOtherProductName());
            sub.setTag(R.id.key_product_id, O_productalternative.getOtherProductId());
            sub.setTag(R.id.key_product_name, O_productalternative.getOtherProductName());



            txt_more_percent.setText(O_productalternative.getPricePercent());

            if(O_productalternative.getPricePercent().contains("Save"))
            {
                txt_more_percent.setTextColor(Color.parseColor("#009688"));
            }else
            {
                txt_more_percent.setTextColor(Color.RED);
            }


            btnaddtocart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String gt_Product_id = (String) view.getTag(R.id.key_product_id);
                    productname = (String) view.getTag(R.id.key_product_name);

                    //Addtocart(view.getTag().toString());
                    Addtocart(gt_Product_id);
                }
            });

            sub.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String gt_Product_id = (String) view.getTag(R.id.key_product_id);
                    productname = (String) view.getTag(R.id.key_product_name);
                    get_singleproduct(gt_Product_id);

                }
            });


          /*  btnaddtocart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String gt_Product_id = (String) view.getTag(R.id.key_product_id);
                    productname = (String) view.getTag(R.id.key_product_name);

                    //Addtocart(view.getTag().toString());
                    Addtocart(gt_Product_id);
                }
            });*/
        }
        return convertView;
    }

    private void  Addtocart(String p_productid) {

        showPdialog("loading");

        //String urlJsonObj = "http://198.50.198.184:86/Api/Product/AddToCart?iProductId=10152&iMemberId=184";

        String url = String.format(AppConfig.URL_GET_ADDTOCARTWITHQTY, p_productid, sMemberId,"p","1","M");


        RequestQueue queue = Volley.newRequestQueue(currentactivity);

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

            SharedPreferences pref = currentactivity.getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cartid", response.getString("iCartId"));
            editor.putString("addtocart_count", response.getString("TotalCartCount"));
            editor.commit();

            setNotifCount(nTotalCartCount);

            f_alert_ok("Success",productname + " added to the cart");

/*            new AlertDialogWrapper.Builder(currentactivity)
                    .setTitle("Data Saved")
                    .setMessage("Data Saved.")
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();*/

        } catch (Exception e) {
            hidePDialog();
            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Error_addtocart(VolleyError error) {
        //hidePDialog();
    }

    private void setNotifCount(int count){
        mNotifCount = count;
        //notifCount.setText(String.valueOf(mNotifCount));
        currentactivity.invalidateOptionsMenu();
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
        new AlertDialog.Builder(currentactivity)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void get_singleproduct(String p_productid) {
        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_PRODUCTDETAIL, p_productid, sMemberId);
        RequestQueue queue = Volley.newRequestQueue(currentactivity);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Success_singleorder(response);
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                       // Error_singleorder(error);
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
            SharedPreferences pref = currentactivity.getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = pref.edit();

            // prefsEditor.putString("product_moleculedetails", response.getJSONArray("objMoleculedetails").toString());
            //prefsEditor.putString("product_medicinedetails", response.getJSONArray("objMedicinedetails").toString());
            //prefsEditor.putString("product_packsizedetails", response.getJSONArray("objPackSizedetails").toString());
            //prefsEditor.putString("product_otherproductdetails", response.getJSONArray("objOtherProductdetails").toString());
            //prefsEditor.putString("product_subdetails", response.getJSONArray("objproductsubdetails").toString());

            globalVariable.setProduct_moleculedetails(response.getJSONArray("objMoleculedetails").toString());
            globalVariable.setProduct_medicinedetails(response.getJSONArray("objMedicinedetails").toString());
            globalVariable.setProduct_packsizedetails(response.getJSONArray("objPackSizedetails").toString());
            globalVariable.setProduct_otherproductdetails(response.getJSONArray("objOtherProductdetails").toString());
            globalVariable.setProduct_subdetails(response.getJSONArray("objproductsubdetails").toString());
            globalVariable.setDrugInteraction(response.getJSONArray("objDrugDrugInteraction").toString());
            globalVariable.setMoleculeinteraction(response.getJSONArray("objDrugMoleculeInteraction").toString());


            prefsEditor.putString("product_name", productname);
            prefsEditor.commit();

            create_buytabs();

        } catch (JSONException e) {
            hidePDialog();
            e.printStackTrace();
            f_alert_ok("Error : ",e.getMessage());
        }
    }

    private void create_buytabs() {


        Intent buytabsIntenet = new Intent(currentactivity, BuyTabs.class);
        buytabsIntenet.putExtra("productid", productid);

        currentactivity.startActivity(buytabsIntenet);
        currentactivity.finish();
    }
    public void filter(String charText) {
        if(arraylist.size()<1) {
            this.arraylist.addAll(alternativeitems);
        }
        alternativeitems.clear();
        if (charText.length() == 0) {
            alternativeitems.addAll(arraylist);
        } else {
            for (M_productalternative wp : arraylist) {

                if (wp.getOtherManufacture().toUpperCase()
                        .contains(charText.toUpperCase())) {
                    alternativeitems.add(wp);
                }else  if (wp.getOtherPrice()
                        .contains(charText)) {
                    alternativeitems.add(wp);
                }else  if (wp.getOtherProductName().toUpperCase()
                        .contains(charText.toUpperCase())) {
                    alternativeitems.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                currentactivity).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }
}



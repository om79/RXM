package viroopa.com.medikart.buying.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.buying.BuyMainActivity;
import viroopa.com.medikart.buying.NewCartSummary;
import viroopa.com.medikart.buying.model.M_cart;

/**
 * Created by prakash on 18/08/15.
 */
public class adaptercart extends BaseAdapter {


    private String sMemberId,sCardId;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private ProgressDialog pDialog;
    private adapterprice priceadapter;

    private Activity currentactivity;
    private LayoutInflater inflater;
    NewCartSummary newcontext;
    private JSONArray ja_cart;
    private String GoToMain;
       Context scontext;
    private List<M_cart> cartitem;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    //RequestQueue queue ;



    public adaptercart(Activity currentactivity, List<M_cart> Cart, String MemberId, Context con, String gotoMain) {
        this.currentactivity = currentactivity;
        this.cartitem = Cart;
        this.sMemberId = MemberId;
        this.scontext=con;
        this.GoToMain=gotoMain;
        initImageLoader();
        pDialog = new ProgressDialog(currentactivity);
        pDialog.setCancelable(false);

        SharedPreferences pref = currentactivity.getSharedPreferences("Global", Context.MODE_PRIVATE);
        sCardId = pref.getString("cartid", "");

//        if (queue != null) {
//            queue.cancelAll(TAG);
//        }
//        else
//        {
//            queue = Volley.newRequestQueue(currentactivity);
//        }
    }

    @Override
    public int getCount() {
        return cartitem.size();
    }

    @Override
    public Object getItem(int location) {
        return cartitem.get(location);
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
            convertView = inflater.inflate(R.layout.page_cart_detail_item, null);
        }


        M_cart O_cart = cartitem.get(position);
        if (O_cart != null) {

            //NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
            TextView productname = (TextView) convertView.findViewById(R.id.productname);
            TextView product_size = (TextView) convertView.findViewById(R.id.product_size);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView Qty = (TextView) convertView.findViewById(R.id.Qty);

            Button btnplus = (Button) convertView.findViewById(R.id.btnplus);
            Button btnminus = (Button) convertView.findViewById(R.id.btnminus);
            Button btnremove = (Button) convertView.findViewById(R.id.btnremove);
            ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

            btnremove.setTag(O_cart.getCheckOutId());
            btnplus.setTag(O_cart.getCheckOutId());
            //btnminus.setTag(O_cart.getCheckOutId());
            btnminus.setTag(R.id.key_product_id, O_cart.getCheckOutId());
            btnminus.setTag(R.id.key_product_qty, O_cart.getQTY());

            String sRupee = "Rs.";

            productname.setText(O_cart.getProductName());
            product_size.setText("Size : " + O_cart.getPackSize().trim());

            Double double_price=0.00;
            DecimalFormat df = new DecimalFormat("#.00");
            if(O_cart.getAmount()!=null &&O_cart.getAmount()!="") {
                double_price = Double.parseDouble(O_cart.getAmount().trim());
            }
            price.setText(sRupee + " " + df.format(double_price).toString());

            Qty.setText(O_cart.getQTY());

           if(O_cart.getImage()!=null) {
               imageLoader.displayImage(O_cart.getImage(), thumbnail);
           }


            btnremove.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    get_remove_cart(view.getTag().toString());
                }
            });

            btnplus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    get_plus_minus("P",view.getTag().toString());

                }
            });

            btnminus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //get_plus_minus("M",view.getTag().toString());

                    String gt_Product_id = (String) view.getTag(R.id.key_product_id);
                    String gt_Product_qty = (String) view.getTag(R.id.key_product_qty);
                    if (gt_Product_qty.equals("1"))
                    {
                        f_alert_ok("Information","Minimum Quantity 1");
                    }
                    else
                    {
                        get_plus_minus("M",gt_Product_id);
                    }
                }
            });
        }
        return convertView;
    }

    private void get_plus_minus(String plus_minus, String p_productid) {
        if (!p_productid.equals(""))
        {
            showPdialog("Loading. . .");

            String url = String.format(AppConfig.URL_GET_PLUSMINUSQTY, sMemberId,p_productid, plus_minus,"M");

            RequestQueue queue = Volley.newRequestQueue(currentactivity);


            JsonArrayRequest jsonObjReq_pm = new JsonArrayRequest(Request.Method.GET,
                    url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Success_plus_minus(response);
                    hidePDialog();
                    //AppController.cancelPendingRequests(jsonObjReq_pm);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hidePDialog();
                    Error_plus_minus(error);
                }
            });

        /*jsonObjReq_pm.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

            //AppController.getInstance().addToRequestQueue(jsonObjReq_pm,"remove");

            queue.add(jsonObjReq_pm);

        }
    }

    private void Success_plus_minus(JSONArray response) {
        try {
            ja_cart = response;
            //ja_summary   = response.getJSONArray("PriceSummaryList");

            SharedPreferences pref = currentactivity.getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cart_productdetail", response.toString());
            editor.putString("refresh_pricing", "1");
            editor.commit();

            get_pricing_detais();
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Error_plus_minus(VolleyError error) {
        f_alert_ok("Error : ", error.getMessage());
    }

    private void get_remove_cart(String p_productid) {
        if (!p_productid.equals(""))
        {
            showPdialog("Loading. . .");

            String url = String.format(AppConfig.URL_GET_REMOVEPRODUCT, p_productid, sCardId, sMemberId);

            RequestQueue queue = Volley.newRequestQueue(currentactivity);

            JsonObjectRequest jsonObjReq_remove = new JsonObjectRequest(Request.Method.GET,
                    url,  new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Success_remove(response);
                    hidePDialog();

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hidePDialog();
                    Error_remove(error);

                }
            });



            queue.add(jsonObjReq_remove);

        }
    }

    private void Success_remove(JSONObject response) {
        try {
            ja_cart = response.getJSONArray("ProductDetail");

            SharedPreferences pref = currentactivity.getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("cart_productdetail", response.getJSONArray("ProductDetail").toString());
            editor.putString("addtocart_count", response.getString("TotalCartCount"));
            editor.putString("refresh_pricing", "1");
            editor.commit();
            get_pricing_detais();
           // NewCartSummary.refresh(currentactivity.getApplicationContext());

           // priceadapter.notifyDataSetChanged();

            if (ja_cart.length() == 0)
            {
                if(GoToMain==null) {
                    currentactivity.finish();
                }else{
                    Intent  gotoMain_intenet= new Intent(currentactivity, BuyMainActivity.class);
                    currentactivity.startActivity(gotoMain_intenet);
                    currentactivity.finish();
                }

            }
            else {
                insertdatatoadapter();



            }
        }
        catch (Exception e) {
            e.printStackTrace();
            f_alert_ok("Error : ",e.getMessage());
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Error_remove(VolleyError error) {
        f_alert_ok("Error : ",error.getMessage());
    }

    private void insertdatatoadapter(){
        try {
            cartitem.clear();
            //JSONArray response =  new JSONArray(ja_cart);
            for (int i = 0; i < ja_cart.length(); i++) {

                JSONObject cartjson = (JSONObject) ja_cart.get(i);

                M_cart O_cart = new M_cart();
                O_cart.setProductName(cartjson.getString("ProductName"));
                O_cart.setPackSize(cartjson.getString("PackSize"));
                O_cart.setPrice(cartjson.getString("Price"));
                O_cart.setAmount(cartjson.getString("Amount"));
                O_cart.setQTY(cartjson.getString("QTY"));
                O_cart.setCheckOutId(cartjson.getString("CheckOutId"));

                cartitem.add(O_cart);
            }

            notifyDataSetChanged();
            //adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(currentactivity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void get_pricing_detais()
    {
        showPdialog("getting prcing details. . .");

        String url = String.format(AppConfig.URL_GET_PRICING_DETAIL, sCardId);

        RequestQueue queue = Volley.newRequestQueue(currentactivity);

        JsonArrayRequest jsonObjReq_remove = new JsonArrayRequest(Request.Method.GET,
                url,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                success_pricing(response);
                hidePDialog();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Error_remove(error);

                    }
                });



        queue.add(jsonObjReq_remove);
    }
    private void success_pricing(JSONArray response)
    {
        SharedPreferences pref = currentactivity.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("cart_pricesummarylist", response.toString());
        editor.commit();
        ((NewCartSummary)scontext).get_intent();
        ((NewCartSummary)scontext).insertdatatoadapter();
    }
}


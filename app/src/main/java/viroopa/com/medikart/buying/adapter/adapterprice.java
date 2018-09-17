package viroopa.com.medikart.buying.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.buying.NewCartSummary;
import viroopa.com.medikart.buying.model.M_pricing;

/**
 * Created by prakash on 18/08/15.
 */
public class adapterprice extends BaseAdapter {
    private String sMemberId,sCardId;


    private ProgressDialog pDialog;

    private Activity currentactivity;
    private LayoutInflater inflater;
    Double double_price=0.00;
    private JSONArray ja_summary = null;
    DecimalFormat df = new DecimalFormat("#.00");
    private List<M_pricing> priceitem;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public adapterprice(Activity currentactivity, List<M_pricing> Cart, String MemberId) {
        this.currentactivity = currentactivity;
        this.priceitem = Cart;
        this.sMemberId = MemberId;

        pDialog = new ProgressDialog(currentactivity);
        pDialog.setCancelable(false);
    }

    @Override
    public int getCount() {
        return priceitem.size();
    }

    @Override
    public Object getItem(int location) {
        return priceitem.get(location);
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
            convertView = inflater.inflate(R.layout.page_cart_summary_item, null);
        }


        String sRupee = "Rs.";

        // getting Invoice data for the row
        M_pricing o_pricing = priceitem.get(position);
        if (o_pricing != null) {

            TextView tax_name = (TextView) convertView.findViewById(R.id.tax_name);
            TextView tax_amt = (TextView) convertView.findViewById(R.id.tax_amt);

            if(position==0) {
                if (!currentactivity.getClass().getSimpleName().equals("SaltDetail")) {

                    Typeface type = Typeface.createFromAsset(currentactivity.getAssets(),"Roboto-Bold.ttf");
                    tax_name.setTypeface(type);
                    tax_amt.setTypeface(type);
                }
            }

            if (currentactivity.getClass().getSimpleName().equals("SaltDetail")) {
                tax_name.setTextSize(12);
                tax_amt.setTextSize(12);
            }
            if(o_pricing.getDiscription().equals("Grand Total"))
            {
               /* tax_name.setTypeface(null, Typeface.BOLD);
                tax_amt.setTypeface(null, Typeface.BOLD);
                tax_name.setTextSize(15);
                tax_amt.setTextSize(15);*/
               // tax_name.setText(o_pricing.getDiscription());
                //tax_amt.setText(sRupee + " " + o_pricing.getAmount());
                tax_name.setVisibility(View.GONE);
                tax_amt.setVisibility(View.GONE);

                if (o_pricing.getAmount()!=null && o_pricing.getAmount()!="") {
                    double_price = Double.parseDouble( o_pricing.getAmount());
                }
                String packageName=currentactivity.getClass().getSimpleName();

                if(packageName.equals("NewCartSummary")) {
                    ((NewCartSummary) currentactivity).change_total_price(sRupee + " " + df.format(double_price).toString());
                }
            }else {
                tax_name.setVisibility(View.VISIBLE);
                tax_amt.setVisibility(View.VISIBLE);
                tax_name.setText(o_pricing.getDiscription());



                if (o_pricing.getAmount()!=null && o_pricing.getAmount()!="") {
                    double_price = Double.parseDouble( o_pricing.getAmount());
                }
             //   tax_amt.setText(sRupee + " " + df.format(double_price).toString());
                tax_amt.setText(sRupee + " " + double_price.toString());



            }
        }

        //get_summary();

        SharedPreferences pref = currentactivity.getSharedPreferences("Global", Context.MODE_PRIVATE);
        String refresh_pricing   = pref.getString("refresh_pricing", "0");

        if (refresh_pricing == "1")
        {
            //f_alert_ok("Testing : ",o_pricing.getDiscription());
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("refresh_pricing", "0");
            editor.commit();

            Refresh_summary();
        }

        return convertView;
    }



    private void get_summary() {

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_CHECKOUT, sMemberId);

        RequestQueue queue = Volley.newRequestQueue(currentactivity);

        JsonObjectRequest jsonObjReq_price = new JsonObjectRequest(Request.Method.GET,
                url,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Success_summary(response);
                hidePDialog();
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hidePDialog();
                    Error_summary(error);
                }
            });

        /*jsonObjReq_price.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        //AppController.getInstance().addToRequestQueue(jsonObjReq_price);

        queue.add(jsonObjReq_price);
    }

    private void Success_summary(JSONObject response) {
        try {
            ja_summary   = response.getJSONArray("PriceSummaryList");
            insertdatatoadapter();
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    private void Error_summary(VolleyError error) {
        f_alert_ok("Error : ",error.getMessage());
    }

    public void insertdatatoadapter(){
        try {

            priceitem.clear();
            //JSONArray response =  new JSONArray(ja_summary);
            for (int i = 0; i < ja_summary.length(); i++) {

                JSONObject cartjson = (JSONObject) ja_summary.get(i);

                M_pricing O_pricing = new M_pricing();
                O_pricing.setDiscription(cartjson.getString("Discription"));
                O_pricing.setAmount(cartjson.getString("Amount"));

                priceitem.add(O_pricing);
            }
            notifyDataSetChanged();
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

    private void Refresh_summary() {
        try {
            SharedPreferences pref = currentactivity.getSharedPreferences("Global", Context.MODE_PRIVATE);
            ja_summary   = new JSONArray(pref.getString("cart_pricesummarylist", ""));

            insertdatatoadapter();
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }




}


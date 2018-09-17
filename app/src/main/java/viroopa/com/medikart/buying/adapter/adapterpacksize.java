package viroopa.com.medikart.buying.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.buying.model.M_packetsize;

/**
 * Created by prakash on 14/08/15.
 */
public class adapterpacksize extends BaseAdapter {


    private String productid, jsonresponse,sMemberId;
    private Activity currentactivity;
    private LayoutInflater inflater;

    private int nTotalCartCount,nCartId;
    static int mNotifCount = 0;

    private ProgressDialog pDialog;

    private List<M_packetsize> packsizeitems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public adapterpacksize(Activity currentactivity, List<M_packetsize> cpacksizeitem, String MemberId) {
        this.currentactivity = currentactivity;
        this.packsizeitems = cpacksizeitem;
        this.sMemberId = MemberId;

        pDialog = new ProgressDialog(currentactivity);
    }


    @Override
    public int getCount() {
        return packsizeitems.size();
    }

    @Override
    public Object getItem(int location) {
        return packsizeitems.get(location);
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
            convertView = inflater.inflate(R.layout.page_packsize_item, null);
        }
        // prakash
        //if (imageLoader == null)
        //    imageLoader = AppController.getInstance().getImageLoader();


        // getting Invoice data for the row
        M_packetsize O_packetsize = packsizeitems.get(position);
        if (O_packetsize != null) {

//          NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
//          TextView srno = (TextView) convertView.findViewById(R.id.txt_srno);
            TextView packsize = (TextView) convertView.findViewById(R.id.txt_size);
            TextView price = (TextView) convertView.findViewById(R.id.txt_price);

            String sRupee = currentactivity.getResources().getString(R.string.Rs);

//          srno.setText(O_packetsize.getSrNo());
            packsize.setText(O_packetsize.getPackSize());
            price.setText(sRupee + " " + O_packetsize.getPrice());

            ImageView btnaddtocart = (ImageView) convertView.findViewById(R.id.btnaddtocart);

            btnaddtocart.setTag(O_packetsize.getProductId());
            btnaddtocart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Addtocart(view.getTag().toString());
                }
            });
        }

        return convertView;
    }

    private void  Addtocart(String p_productid) {

        showPdialog("loading");

        String url = String.format(AppConfig.URL_GET_ADDTOCART, p_productid, sMemberId);

        RequestQueue queue = Volley.newRequestQueue(currentactivity);

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

            SharedPreferences pref = currentactivity.getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cartid", response.getString("iCartId"));
            editor.putString("addtocart_count", response.getString("TotalCartCount"));
            editor.commit();

            setNotifCount(nTotalCartCount);

            f_alert_ok("Success", "Added to the cart");

        } catch (Exception e) {

            e.printStackTrace();

            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // hidePDialog();
    }

    private void Error_addtocart(VolleyError error) {

        f_alert_ok("Error : ", error.getMessage());
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


}



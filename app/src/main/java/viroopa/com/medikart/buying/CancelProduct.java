package viroopa.com.medikart.buying;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.ObjectItem;
import viroopa.com.medikart.R;
import viroopa.com.medikart.adapter.AD_adapterCombo;
import viroopa.com.medikart.app.AppConfig;

public class CancelProduct extends AppCompatActivity {
    private ProgressDialog pDialog;
    private String sMemberId;
    private String cancelReasonId;
    private TextView order_no,order_date,product_name,dosage_form,invoice_no,order_amount,txt_cancel_reason,txt_Comments;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private ImageView thumbnail;
    private Button btncancel_product,btn_back;
    AD_adapterCombo Reason_adapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
    DateFormat dateFormat_display = new SimpleDateFormat("dd-LLL-yyy   HH:mm");

    ObjectItem[] ObjectItemReasons=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_product);
        pDialog = new ProgressDialog(this);

        order_no     =(TextView)findViewById(R.id.order_no) ;
        order_date   =(TextView)findViewById(R.id.order_date) ;
        product_name =(TextView)findViewById(R.id.product_name) ;
        dosage_form  =(TextView)findViewById(R.id.dosage_form) ;
        invoice_no   =(TextView)findViewById(R.id.invoice_no) ;
        txt_Comments  =(EditText)findViewById(R.id.txt_Comments) ;
        order_amount =(TextView)findViewById(R.id.order_amount) ;
        thumbnail    =(ImageView)findViewById(R.id.thumbnail);
        txt_cancel_reason=(TextView)findViewById(R.id.txt_cancel_reason);
        btncancel_product=(Button) findViewById(R.id.btncancel_product);
        btn_back  =(Button) findViewById(R.id.btn_back);

        txt_cancel_reason.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    if(ObjectItemReasons!=null) {
                        if(ObjectItemReasons.length!=0) {

                            Reason_dialog();
                        }else
                        {
                            get_product_cancel_reasons();
                        }
                    }else {
                        get_product_cancel_reasons();
                    }

                }catch (Exception e)
                {}
            }
        });

        get_product_cancel_reasons();
        initImageLoader();
        getIntenet();
        get_product_cancel();

        btncancel_product.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(cancelReasonId!=null) {
                    post_cancelproduct((String) view.getTag(R.id.key_order_id),
                            (String) view.getTag(R.id.key_product_id),
                            (String) view.getTag(R.id.key_Order_item_no));
                }else {
                    Toast.makeText(getApplicationContext(),"Please select a Reason", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            finish();
            }
        });



    }
    private void get_product_cancel() {
        Intent intent_refilorder = getIntent();

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_CANCEL_PRODUCT_DATA, intent_refilorder.getStringExtra("Order_id"),intent_refilorder.getStringExtra("Product_id"), sMemberId);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                success_response(response);
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();

                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);
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
    private void getIntenet(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
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

    private  void success_response(JSONObject response)
    {
        try {

            order_no.setText(response.getString("OrderNo"));



            try {
                Date current_date = Calendar.getInstance().getTime();
                current_date = dateFormat.parse(response.getString("Orderdate"));
                order_date.setText(dateFormat_display.format(current_date));
            }catch(Exception e)
            {

            }



            product_name.setText(response.getString("ProductName"));
            dosage_form.setText(response.getString("ProductForm"));
            invoice_no.setText(response.getString("InvoiceNo"));
            order_amount.setText(response.getString("OrderAmt"));

            btncancel_product.setTag(R.id.key_order_id,response.getString("OrderId"));
            btncancel_product.setTag(R.id.key_product_id,response.getString("ProductId"));
            btncancel_product.setTag(R.id.key_Order_item_no,response.getString("OrderItemNo"));

            imageLoader.displayImage(response.getString("ProductImgPath"), thumbnail);

        }catch (JSONException e)
        {
            e.toString();
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void Reason_dialog()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);
        builder.setView(dialogview);
        Reason_adapter = new AD_adapterCombo(this, R.layout.list_item,ObjectItemReasons);
        lv.setAdapter(Reason_adapter);

        final TextView Titile =(TextView)dialogview.findViewById(R.id.textView);
        Titile.setText("Select a reason for cancel");

        final TextView btncancel=(TextView)dialogview.findViewById(R.id.btncancel);
        final Dialog dlg= builder.create();
        lv.setOnItemClickListener(new OnItemClickListenerListViewItemReason(dlg));

        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dlg.cancel();
            }
        });

        dlg.show();
    }

    public class OnItemClickListenerListViewItemReason implements AdapterView.OnItemClickListener {
        Dialog d=null;
        OnItemClickListenerListViewItemReason(Dialog dlg)
        {
            d=dlg;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Context context = view.getContext();


            TextView textViewItem = ((TextView) view.findViewById(R.id.product_name));
            String listItemText = textViewItem.getText().toString();
            String listItemId = textViewItem.getTag().toString();
            cancelReasonId=listItemId;
            txt_cancel_reason.setText(listItemText);


            d.cancel();

        }
    }



    private  void post_cancelproduct(String Order_id,String product_id,String Order_item_number) {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("OrderId", Order_id);
        params.put("ProductId",product_id);
        params.put("OrderAmt", order_amount.getText().toString());
        params.put("OrderItemNo", Order_item_number);
        params.put("CancelledBy", sMemberId);
        params.put("CancelReason", cancelReasonId);
        params.put("CancelRemarks",txt_Comments.getText().toString());

        JSONObject jparams = new JSONObject(params);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_CANCEL_ORDER,

                jparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hidePDialog();
                        Toast.makeText(getApplicationContext(), "Your order is cancelled succesfully", Toast.LENGTH_LONG)
                                .show();
                       // Intent Intenet_buy = new Intent(CancelProduct.this, Order_Transaction.class);
                       // Intenet_buy.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);;
                     //   startActivityForResult(Intenet_buy,0);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("charset", "utf-8");
                headers.put("User-agent", "Buying");
                return headers;
            }
        };

        jor_inhurry_post.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jor_inhurry_post);
    }

    private void get_product_cancel_reasons() {


        String sParameter="CanOrder";

        showPdialog("Loading. . .");
        String url =   String.format(AppConfig.URL_GET_REASONS,sParameter);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                success_cancel_reasons(response);
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();

                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);
    }
    private  void success_cancel_reasons(JSONObject response)
    {
        try {
            JSONArray j_array = response.getJSONArray("v_Reasonlist");
            ObjectItemReasons = new ObjectItem[j_array.length()];
            for (int i = 0; i < j_array.length(); i++) {
                JSONObject jsonobject = j_array.getJSONObject(i);
                ObjectItemReasons[i] = new ObjectItem(jsonobject.getInt("ReasonId"),jsonobject.getString("Reason"));
            }

        }catch (JSONException e)
        {}
    }
}

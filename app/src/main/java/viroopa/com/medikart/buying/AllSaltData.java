package viroopa.com.medikart.buying;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.model.productlist;

public class AllSaltData extends AppCompatActivity {
    private ListView Lst_all_salt;
    ArrayList<productlist> medicine_data=new ArrayList<productlist>();
    private all_salt_med_adapter Med_adappter;
    private ProgressDialog pDialog;
    private String sMemberId;
    private  String Salt_id,TotalCount="10";
    AppController globalVariable;
    private String productname,product_prize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        globalVariable = (AppController) getApplicationContext();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        setContentView(R.layout.activity_all_salt_data);
        Lst_all_salt=(ListView)findViewById(R.id.Lst_all_salt);
        getIntenet();


        Med_adappter=new all_salt_med_adapter(this,medicine_data);
        Lst_all_salt.setAdapter(Med_adappter);
        Lst_all_salt.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        insert_into_adapter();

    }



    private void insert_into_adapter()
    {
        medicine_data.clear();
        showPdialog("loading....");

        String url = String.format(AppConfig.URL_GET_SALT_PRODUCT_DETAIL,Salt_id,TotalCount);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonObjReq_single = new JsonArrayRequest(Request.Method.GET,
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                success_medicine(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Error_user_details(error);
                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq_single);
    }
    private  void success_medicine(JSONArray response)
    {
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonobject = response.getJSONObject(i);

                productlist med_list = new productlist();

                med_list.setProductname(jsonobject.optString("ProductName"));
                med_list.setId(jsonobject.optString("ProductId"));
                med_list.setPrice(jsonobject.optString("MRP"));
                medicine_data.add(med_list);

            }catch (JSONException e)
            {
                e.toString();
            }

        }
        Med_adappter.notifyDataSetChanged();
        hidePDialog();
    }



    public class all_salt_med_adapter extends ArrayAdapter<productlist> {
        public all_salt_med_adapter(Context context, ArrayList<productlist> Medicines) {
            super(context, 0, Medicines);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position


            productlist Medicine_data = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_salt_med, parent, false);
            }
            // Lookup view for data population

            final TextView Medicine_Name = (TextView) convertView.findViewById(R.id.Medicine_Name);
            final TextView price = (TextView) convertView.findViewById(R.id.price);
            final RelativeLayout  Main_relice = (RelativeLayout) convertView.findViewById(R.id.Main_rel);
            Medicine_Name.setText(Medicine_data.getProductname());

            Main_relice.setTag(R.id.key_product_id, Medicine_data.getId());
            Main_relice.setTag(R.id.key_product_price, Medicine_data.getPrice());
            Main_relice.setTag(R.id.key_product_name, Medicine_data.getProductname());

            price.setText("Rs."+Medicine_data.getPrice());

            Main_relice.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    String productid = (String) view.getTag(R.id.key_product_id);
                    productname = (String) view.getTag(R.id.key_product_name);
                    product_prize = (String) view.getTag(R.id.key_product_price);

                    get_singleproduct(productid);
                }
            });


            return convertView;
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

    private void getIntenet() {
        try
        {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
            Intent i=getIntent();
            Salt_id=i.getStringExtra("Salt_id");
            sMemberId = pref.getString("memberid", "Salt_id");
            TotalCount=i.getStringExtra("total_count");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void create_buytabs() {
        Intent buytabsIntenet = new Intent(this, BuyTabs.class);
        this.startActivity(buytabsIntenet);
    }

    private void get_singleproduct(String p_productid) {
        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_PRODUCTDETAIL, p_productid, sMemberId);

        RequestQueue queue = Volley.newRequestQueue(this);

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
                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
               50000,
               DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
              DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq_single);

    }
    private void Success_singleorder(JSONObject response) {
        try {
            SharedPreferences pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = pref.edit();
            globalVariable.setProduct_moleculedetails(response.getJSONArray("objMoleculedetails").toString());
            globalVariable.setProduct_medicinedetails(response.getJSONArray("objMedicinedetails").toString());
            globalVariable.setProduct_packsizedetails(response.getJSONArray("objPackSizedetails").toString());
            globalVariable.setProduct_otherproductdetails(response.getJSONArray("objOtherProductdetails").toString());
            globalVariable.setProduct_subdetails(response.getJSONArray("objproductsubdetails").toString());
            globalVariable.setDrugInteraction(response.getJSONArray("objDrugDrugInteraction").toString());
            globalVariable.setMoleculeinteraction(response.getJSONArray("objDrugMoleculeInteraction").toString());
            prefsEditor.putString("product_name", productname);
            prefsEditor.putString("product_price", product_prize);
            prefsEditor.commit();

            create_buytabs();


        } catch (JSONException e) {
            hidePDialog();
            e.printStackTrace();
        }
    }
}

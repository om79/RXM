package viroopa.com.medikart.buying;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.buying.adapter.PrescriptionListExpandableAdapter;
import viroopa.com.medikart.buying.model.Category;
import viroopa.com.medikart.buying.model.ItemDetail;
import viroopa.com.medikart.buying.model.orderModel;

public class Upload_prescription_list extends AppCompatActivity {

    private ProgressDialog pDialog;
    private  String sMemberId;
    private List<Category> catList= new ArrayList<Category>();;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat dateFormat_month = new SimpleDateFormat("LLLL");
    DateFormat dateFormat_year = new SimpleDateFormat("yyyy");
    private TextView txt_no_prescriptions;
    Date current_date = Calendar.getInstance().getTime();
    private ExpandableListView exList;

    private PrescriptionListExpandableAdapter expandableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_prescription_list);
        pDialog = new ProgressDialog(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        getIntenet();

        exList = (ExpandableListView) findViewById(R.id.expandableListView1);
        txt_no_prescriptions= (TextView) findViewById(R.id.txt_no_prescriptions);

        get_prescriptionList();


    }

    private void get_prescriptionList() {

        showPdialog("Loading. . .");


        String url = String.format(AppConfig.URL_GET_PRESCRIPTION_LIST, sMemberId);



        RequestQueue queue = Volley.newRequestQueue(Upload_prescription_list.this);


        JsonObjectRequest refillrequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    SuccessResponse(response.getJSONArray("PrescriptionDetail"));
                    hidePDialog();
                }catch (JSONException e)
                {

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                });

        refillrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(refillrequest);

    }

    private void SuccessResponse(JSONArray response) {
        catList = new ArrayList<Category>();
        List<ItemDetail> result=null;
        ArrayList<orderModel.ItemList> mItemListArray1 = new ArrayList<orderModel.ItemList>();

        mItemListArray1.clear();
        ItemDetail item=null;
        Category cat1=null;
        String smymonthyear = "-99";
        String OrderMonth="";
        String OrderYear="";

        if( response.length()==0)
        {
            txt_no_prescriptions.setVisibility(View.VISIBLE);
        }else {
            txt_no_prescriptions.setVisibility(View.GONE);
        }


        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj_json = response.getJSONObject(i);
                if (!obj_json.equals(null))
                {
                    try {
                        Date current_date = Calendar.getInstance().getTime();
                        current_date = dateFormat.parse(obj_json.getString("UploadedOn").trim());
                        OrderMonth = (dateFormat_month.format(current_date));
                        OrderYear = (dateFormat_year.format(current_date));

                    }catch(Exception e)
                    {

                    }

                    String runingmonthyear = OrderMonth+" - "+OrderYear;

                    if (!runingmonthyear.equals(smymonthyear)) {
                        if (cat1!=null)
                        {
                            catList.add(cat1);
                        }


                        result = new ArrayList<ItemDetail>();
                        smymonthyear = runingmonthyear;


                        cat1 = new Category(i,OrderMonth,OrderYear);
                    }
                    item = new ItemDetail(i,obj_json.getString("UploadedOn"),obj_json.getString("RequestNo"),obj_json.getString("PrescriptionName"),obj_json.getString("PrescriptionId"));

                    result.add(item);
                    cat1.setItemList(result);

                }

            } catch (Exception e) {
                e.toString();

            }
        }

        if (cat1!=null)
        {
            catList.add(cat1);

        }

        expandableAdapter = new PrescriptionListExpandableAdapter(catList, this);

        exList.setAdapter(expandableAdapter);


        int count =  catList.size();
        for (int i = 0; i <count ; i++)
            exList.expandGroup(i);

    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    private void showPdialog(String sMessage) {
        if (pDialog != null) {
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }

    private void getIntenet() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
    }

}

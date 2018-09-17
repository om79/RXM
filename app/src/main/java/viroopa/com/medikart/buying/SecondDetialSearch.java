package viroopa.com.medikart.buying;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.SearchRecyclerAdapter;
import viroopa.com.medikart.buying.model.productlist;

public class SecondDetialSearch extends AppCompatActivity {
    private ListView listView;
    private  String sMemberId;
    private  String SearchText,Searchtype;
    SearchRecyclerAdapter recycleradapter;
    AppController globalVariable;
    private RecyclerView mRecyclerView;
    private ProgressDialog pDialog;
    private List<productlist> productheaderlist = new ArrayList<productlist>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_detial_search);
        pDialog = new ProgressDialog(this);
        globalVariable = (AppController) getApplicationContext();
        getIntenet();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recycleradapter = new SearchRecyclerAdapter(this, productheaderlist,sMemberId);
        mRecyclerView.setAdapter(recycleradapter);
        get_productsearch(SearchText);
    }
    private void getIntenet() {
        Intent intent_search = getIntent();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        Searchtype=intent_search.getStringExtra("producttype");
        SearchText=globalVariable.getSearchWord();

    }

    private void get_productsearch(String sSearchText) {

        showPdialog("Loading. . .");

        productheaderlist.clear();
        String query="";
        //String MyStrting = URLEncoder.encode(sSearchText, "UTF-8");
        try {

            query = URLEncoder.encode(sSearchText, "utf-8");
        }catch (Exception e)
        {

        }

        // String url = String.format(AppConfig.URL_GET_SEARCHPRODUCT, query, "0", "s");
        String url = String.format(AppConfig.URL_GET_NEW_SEARCHPRODUCT, query, "D", Searchtype);



        RequestQueue queue = Volley.newRequestQueue(SecondDetialSearch.this);

        JsonObjectRequest jar_getsearchproduct = new JsonObjectRequest(Request.Method.GET,
                url,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    JSONArray jsonArray_search_pro=null;
                    if(Searchtype.equals("S"))
                    {
                        jsonArray_search_pro = response.getJSONArray("salt");
                        getSupportActionBar().setTitle("All Salts");
                    }else if(Searchtype.equals("M")) {
                         jsonArray_search_pro = response.getJSONArray("medicine");
                        getSupportActionBar().setTitle("All Medicines");
                    }else
                    {
                        jsonArray_search_pro = response.getJSONArray("GeneralProducts");
                        getSupportActionBar().setTitle("All General Products");
                    }

                    if (jsonArray_search_pro.length() > 0)
                    {
                        productheaderlist.clear();
                        load_product_list(jsonArray_search_pro);
                        recycleradapter.notifyDataSetChanged();
                        hidePDialog();
                    }



                } catch (JSONException e) {
                    hidePDialog();
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  VolleyLog.d(TAG, "Error: " + error.getMessage());
              //  Toast.makeText(getApplicationContext(), "main Server Error :" + error.getMessage(), Toast.LENGTH_LONG).show();

                hidePDialog();
            }
        });



        queue.add(jar_getsearchproduct);

    }
    public void load_product_list(JSONArray p_search_product) {

        try {

            JSONArray jsonArray_search_pro=null;

                jsonArray_search_pro = p_search_product;



            if (jsonArray_search_pro.length() <= 0) {

                //f_alert_ok("Reason Data", "Order Data not found on sever.");
            } else {
                productlist oproductlist = new productlist();

                // Parsing json
                for (int i = 0; i < jsonArray_search_pro.length(); i++) {
                    try {
                        JSONObject obj_json = jsonArray_search_pro.getJSONObject(i);

                        if (!obj_json.equals(null)) {


                            oproductlist = new productlist();
                            oproductlist.setId(obj_json.getString("ID"));
                            oproductlist.setPrice(obj_json.getString("MRP"));
                            oproductlist.setProductname(obj_json.getString("Primary"));
                            oproductlist.setForm_Name(obj_json.getString("ProductFormImage"));
                            oproductlist.setPackSize(obj_json.getString("packsize"));
                            oproductlist.setImageExist(obj_json.getString("MainImagePath"));
                            oproductlist.setMfg(obj_json.getString("Secondary"));
                            oproductlist.setJson_type(obj_json.getString("json_type"));
                            oproductlist.setHeading("");
                            oproductlist.setSearch_count("");
                            oproductlist.setCatalogue(obj_json.getString("Catalogue"));
                            oproductlist.setIsPharma(obj_json.getBoolean("Pharma"));
                            oproductlist.setCategoryId(obj_json.getString("Parent_ID"));

                            productheaderlist.add(oproductlist);

                         /*   db.insert_medicine_master(
                                    obj_json.getInt("Product_ID"),
                                    obj_json.getString("Primary"),
                                    obj_json.getString("MRP"),
                                    obj_json.getString("packsize"),
                                    obj_json.getString("ProductFormImage"),
                                    obj_json.getString("ProductFormImage")
                            );*/
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }



            }

        } catch (Exception e) {

            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

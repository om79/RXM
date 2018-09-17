package viroopa.com.medikart.buying;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.adaptergeneralproductGrid;
import viroopa.com.medikart.buying.model.productlist;

public class GeneralProductSearchPage extends AppCompatActivity {
    private ProgressDialog pDialog;
    private GridView gridView;
    AppController globalVariable;
    private String subCategoryId,Category_id,Brand_id,FromPrice,ToPrice,SortBy;
    private TextView cat_name,txt_count;
    private  static int FILTER_VALUE_SELECTED_CODE=32;
    public static GeneralProductSearchPage searcObject;
    private Button btn_filter;
    adaptergeneralproductGrid gridAdapter;
    private ArrayList<productlist> productlist = new ArrayList<productlist>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_general_product_search_page);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        searcObject=this;
        globalVariable = (AppController) getApplicationContext();
        cat_name=(TextView)findViewById(R.id.cat_name);
        txt_count=(TextView)findViewById(R.id.txt_count);
        btn_filter=(Button) findViewById(R.id.btn_filter) ;
        gridView=(GridView)findViewById(R.id.gridView) ;
/*
        LinearLayout.LayoutParams lnr_params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);

        TextView empty_text = new TextView(this);
        empty_text.setLayoutParams(lnr_params);
        empty_text.setText("No item available");
        gridView.setEmptyView(empty_text);*/


        gridAdapter= new adaptergeneralproductGrid(this, productlist );
        gridView.setAdapter( gridAdapter );
        gridView.setEmptyView(findViewById(R.id.emptyview));

        btn_filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intenet = new Intent(GeneralProductSearchPage.this, FilterConditions.class);
                intenet.putExtra("subCategoryId",subCategoryId);
                intenet.putExtra("Category_id",Category_id );
                intenet.putExtra("CategoryName",cat_name.getText().toString() );

                //new FilterConditions().InitialiseCallBack(GeneralProductSearchPage.this);
                startActivity(intenet);
            }
        });

        get_intenet();

        get_productdetail();

    }

    private void get_productdetail()
    {





        showPdialog("loading...");


        String url = String.format(AppConfig.URL_GET_GENERAL_PRODUCT_SEARCH_DATA, subCategoryId,0,SortBy,Brand_id,FromPrice,ToPrice,"true",Category_id,"");

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonObjReq_state = new JsonArrayRequest(Request.Method.GET,
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Load_product_data(response);
                hidePDialog();
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

    private  void Load_product_data(JSONArray response)
    {



        productlist.clear();


        try {



            for (int i = 0; i < response.length(); i++) {

                JSONObject obj_json = response.getJSONObject(i);

                if (!obj_json.equals(null)) {

                    productlist O_productlist = new productlist();

                    O_productlist.setId(obj_json.getString("ProductId"));
                    O_productlist.setProductname(obj_json.getString("ProductName"));
                    O_productlist.setPrice(obj_json.getString("MRP"));
                    O_productlist.setImagePath(obj_json.getString("ProductImage"));

                    productlist.add(O_productlist);
                    txt_count.setText("("+obj_json.getString("ProductCount")+" products)");


                }
            }
            gridAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            hidePDialog();
            Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }




    }


    private  void get_intenet()
    {
        Intent i =getIntent();

            subCategoryId = i.getStringExtra("subCategoryId");
            Category_id = i.getStringExtra("CategoryId");
            SortBy = i.getStringExtra("SortBy");
            Brand_id = i.getStringExtra("BrandId");
            FromPrice = i.getStringExtra("Fromprice");
            ToPrice = i.getStringExtra("Toprice");
            cat_name.setText(i.getStringExtra("Categoryname"));

        }



   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == FILTER_VALUE_SELECTED_CODE) {
            Bundle extras = data.getExtras();



            this.subCategoryId= extras.getString("subCategoryId");
            this.Brand_id= extras.getString("Brand_ids");
            this.FromPrice= extras.getString("FromPrice");
            this.ToPrice= extras.getString("ToPrice");
            this.SortBy= extras.getString("SortBy");
            get_productdetail();

        }
    }*/

}

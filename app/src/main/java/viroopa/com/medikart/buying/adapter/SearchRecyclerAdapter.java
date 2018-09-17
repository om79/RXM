package viroopa.com.medikart.buying.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.BuyTabs;
import viroopa.com.medikart.buying.GeneralProductSearchPage;
import viroopa.com.medikart.buying.GeneralProducyDetails;
import viroopa.com.medikart.buying.SaltDetail;
import viroopa.com.medikart.buying.SecondDetialSearch;
import viroopa.com.medikart.buying.model.productlist;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {


    private List<productlist> feedItemList;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private Context mContext;
    private ProgressDialog pDialog;
    private String sMemberId;
    private String productid,productname,product_prize;
    AppController globalVariable;

    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


    public SearchRecyclerAdapter(Context context, List<productlist> feedItemList, String memberid) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.sMemberId = memberid;
        initImageLoader();
        pDialog = new ProgressDialog(mContext);
        globalVariable = (AppController)mContext.getApplicationContext();
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_header, null);
        FeedListRowHolder mh = new FeedListRowHolder(v);
       // mh.setIsRecyclable(false);

        return mh;
    }

    @Override
    public void onBindViewHolder(final FeedListRowHolder feedListRowHolder, int i) {

        if(i<feedItemList.size()) {
            int height = 0; //your textview height
           // feedListRowHolder.space.getLayoutParams().height = height;
            feedListRowHolder.line_view.setVisibility(View.GONE);
            feedListRowHolder.btn_showall.setVisibility(View.GONE);

            feedListRowHolder.price.setVisibility(View.GONE);
            feedListRowHolder.productname.setVisibility(View.GONE);
            feedListRowHolder.formName.setVisibility(View.GONE);
            feedListRowHolder.catalog.setVisibility(View.GONE);
            feedListRowHolder.packSize.setVisibility(View.GONE);
            feedListRowHolder.group_heading.setVisibility(View.GONE);
            feedListRowHolder.thumbnail.setVisibility(View.GONE);

            feedListRowHolder.group_heading.setText("");

            productlist feedItem = feedItemList.get(i);

            String sRupee = "Rs.";
            //rl_product_item.setTag(feedItem.getId());

            String suggeting = feedItem.getHeading();
            if (suggeting == null) {
                suggeting = "";
            }

            if(suggeting.equals("Show All Products")) {
               height = 0; //your textview height
               // feedListRowHolder.space.getLayoutParams().height = height;
                feedListRowHolder.line_view.setVisibility(View.GONE);
                feedListRowHolder.btn_showall.setVisibility(View.VISIBLE);

                feedListRowHolder.price.setVisibility(View.GONE);
                feedListRowHolder.productname.setVisibility(View.GONE);
                feedListRowHolder.formName.setVisibility(View.GONE);
                feedListRowHolder.catalog.setVisibility(View.GONE);
                feedListRowHolder.packSize.setVisibility(View.GONE);
                feedListRowHolder.group_heading.setVisibility(View.GONE);
                feedListRowHolder.thumbnail.setVisibility(View.GONE);

                feedListRowHolder.btn_showall.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent detail_intent = new Intent(mContext, SecondDetialSearch.class);
                        detail_intent.putExtra("producttype", "M");
                        mContext.startActivity(detail_intent);
                    }
                });
              /*  feedListRowHolder.footer.setVisibility(View.VISIBLE);
                feedListRowHolder.lnr_heading.setVisibility(View.GONE);
                feedListRowHolder.main_data_layout.setVisibility(View.GONE);*/

            } if(suggeting.equals("Show All Salts")) {
                height = 0; //your textview height
               // feedListRowHolder.space.getLayoutParams().height = height;
                feedListRowHolder.line_view.setVisibility(View.GONE);
                feedListRowHolder.btn_showall.setVisibility(View.VISIBLE);

                feedListRowHolder.price.setVisibility(View.GONE);
                feedListRowHolder.productname.setVisibility(View.GONE);
                feedListRowHolder.formName.setVisibility(View.GONE);
                feedListRowHolder.catalog.setVisibility(View.GONE);
                feedListRowHolder.packSize.setVisibility(View.GONE);
                feedListRowHolder.group_heading.setVisibility(View.GONE);
                feedListRowHolder.thumbnail.setVisibility(View.GONE);
                feedListRowHolder.group_heading.setVisibility(View.GONE);

                feedListRowHolder.btn_showall.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {


                        Intent detail_intent = new Intent(mContext, SecondDetialSearch.class);
                        detail_intent.putExtra("producttype", "S");
                        mContext.startActivity(detail_intent);
                    }
                });


            } if(suggeting.equals("Show All General Products")) {
                feedListRowHolder.line_view.setVisibility(View.GONE);
                height = 0; //your textview height
              //  feedListRowHolder.space.getLayoutParams().height = height;
                feedListRowHolder.btn_showall.setVisibility(View.GONE);
               // feedListRowHolder.space.setHeight(0);
                feedListRowHolder.price.setVisibility(View.GONE);
                feedListRowHolder.productname.setVisibility(View.GONE);
                feedListRowHolder.formName.setVisibility(View.GONE);
                feedListRowHolder.catalog.setVisibility(View.GONE);
                feedListRowHolder.packSize.setVisibility(View.GONE);
                feedListRowHolder.group_heading.setVisibility(View.GONE);
                feedListRowHolder.thumbnail.setVisibility(View.GONE);

                feedListRowHolder.group_heading.setVisibility(View.GONE);

                feedListRowHolder.btn_showall.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {


                        Intent detail_intent = new Intent(mContext, SecondDetialSearch.class);
                        detail_intent.putExtra("producttype", "G");
                        mContext.startActivity(detail_intent);
                    }
                });
              /*  feedListRowHolder.footer.setVisibility(View.VISIBLE);
                feedListRowHolder.lnr_heading.setVisibility(View.GONE);
                feedListRowHolder.main_data_layout.setVisibility(View.GONE);*/


            } if (suggeting.contains("Suggested")) {

                feedListRowHolder.group_heading.setText(feedItem.getHeading());
                feedListRowHolder.result_count.setText(feedItem.getSearch_count());
               height = 0; //your textview height
            //    feedListRowHolder.space.getLayoutParams().height = height;
                //feedListRowHolder.line_view.setVisibility(View.GONE);
                feedListRowHolder.btn_showall.setVisibility(View.GONE);

                feedListRowHolder.price.setVisibility(View.GONE);
                feedListRowHolder.productname.setVisibility(View.GONE);
                feedListRowHolder.formName.setVisibility(View.GONE);
                feedListRowHolder.catalog.setVisibility(View.GONE);
                feedListRowHolder.packSize.setVisibility(View.GONE);
                feedListRowHolder.group_heading.setVisibility(View.GONE);
                feedListRowHolder.thumbnail.setVisibility(View.GONE);

                feedListRowHolder.group_heading.setVisibility(View.VISIBLE);



                if (feedItem.getHeading().equals("Suggested Medicines")) {
                    feedListRowHolder.btn_showall.setTag("M");
                }
                if (feedItem.getHeading().equals("Suggested Salt")) {
                    feedListRowHolder.btn_showall.setTag("S");
                }
                if (feedItem.getHeading().equals("Suggested general products")) {
                    feedListRowHolder.btn_showall.setTag("P");
                }



            } else if(!suggeting.contains("Show All"))  {
                feedListRowHolder. main_data_layout.setVisibility(View.VISIBLE);
                 height = 0; //your textview height
            //    feedListRowHolder.space.getLayoutParams().height = height;
                feedListRowHolder.LnrClick.setTag(R.id.key_product_id, feedItem.getId());
                feedListRowHolder.LnrClick.setTag(R.id.key_product_name, feedItem.getProductname());
                feedListRowHolder.LnrClick.setTag(R.id.key_product_qty, feedItem.getJson_type());
                feedListRowHolder.LnrClick.setTag(R.id.key_product_price, feedItem.getPrice());
                feedListRowHolder.LnrClick.setTag(R.id.key_category_id, feedItem.getCategoryId());
                feedListRowHolder.LnrClick.setTag(R.id.key_category_name, feedItem.getCatalogue());
                feedListRowHolder.LnrClick.setTag(R.id.key_is_pharma, feedItem.getIsPharma());

                if (feedItem.getImagePath() != null) {
                    imageLoader.displayImage(feedItem.getImagePath(), feedListRowHolder.thumbnail);
                }

                if(feedItem.getJson_type().equals("Salt")||feedItem.getJson_type().equals("General Product"))
                {
                    feedListRowHolder.line_view.setVisibility(View.VISIBLE);
                    feedListRowHolder.btn_showall.setVisibility(View.GONE);

                    feedListRowHolder.price.setVisibility(View.GONE);
                    feedListRowHolder.productname.setVisibility(View.VISIBLE);
                    feedListRowHolder.formName.setVisibility(View.GONE);
                    feedListRowHolder.catalog.setVisibility(View.GONE);
                    feedListRowHolder.packSize.setVisibility(View.GONE);
                    feedListRowHolder.group_heading.setVisibility(View.GONE);
                    feedListRowHolder.thumbnail.setVisibility(View.GONE);
                    feedListRowHolder.productname.setPadding(10, 5, 0, 5);


                    if(feedItem.getJson_type().equals("General Product"))
                    {
                        feedListRowHolder.catalog.setVisibility(View.VISIBLE);
                        feedListRowHolder.catalog.setText( feedItem.getCatalogue());

                        params.addRule(RelativeLayout.RIGHT_OF, R.id.productname);
                        params.setMargins(5,0,0,0);
                        params.addRule(RelativeLayout.CENTER_IN_PARENT);
                        feedListRowHolder.catalog.setLayoutParams(params);
                    }else
                    {
                        feedListRowHolder.catalog.setVisibility(View.GONE);
                    }

                     height = 0; //your textview height
              //      feedListRowHolder.space.getLayoutParams().height = height;



                  /*  RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams ( RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT );
                    lp.addRule(RelativeLayout.BELOW,  feedListRowHolder.productname.getId());
                    feedListRowHolder.line_view.setLayoutParams(lp);*/

                }
                else {
                    height = 65; //your textview height
              //      feedListRowHolder.space.getLayoutParams().height = height;
                    feedListRowHolder.line_view.setVisibility(View.VISIBLE);
                    feedListRowHolder.btn_showall.setVisibility(View.GONE);

                    feedListRowHolder.price.setVisibility(View.VISIBLE);
                    feedListRowHolder.productname.setVisibility(View.VISIBLE);
                    feedListRowHolder.formName.setVisibility(View.VISIBLE);
                    feedListRowHolder.catalog.setVisibility(View.VISIBLE);
                    feedListRowHolder.packSize.setVisibility(View.VISIBLE);
                    feedListRowHolder.thumbnail.setVisibility(View.VISIBLE);


                    feedListRowHolder.group_heading.setVisibility(View.GONE);
                }

                if (feedItem.getImageExist() != null) {
                    imageLoader.displayImage(feedItem.getImageExist(), feedListRowHolder.thumbnail);
                }

                feedListRowHolder.productname.setText(feedItem.getProductname());
                feedListRowHolder.formName.setText("Form : " + feedItem.getForm_Name());
                feedListRowHolder.catalog.setText( feedItem.getCatalogue());

                String mfgNAme="";
                if(feedItem.getMfg()==null || feedItem.getMfg().equals("null"))
                {
                    mfgNAme="Mfg : Not available";
                }else{
                    mfgNAme="Mfg : " + feedItem.getMfg();
                }

                feedListRowHolder.packSize.setText(mfgNAme);


                Double double_price=0.00;
                DecimalFormat df = new DecimalFormat("#.00");
                if(feedItem.getPrice()!=null && !feedItem.getPrice().equals("")) {
                    double_price = Double.parseDouble(feedItem.getPrice());
                }

                feedListRowHolder.price.setText(sRupee + " " + df.format(double_price).toString());


                feedListRowHolder.LnrClick.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        String productid = (String) view.getTag(R.id.key_product_id);
                        productname = (String) view.getTag(R.id.key_product_name);
                        String Json_type = (String) view.getTag(R.id.key_product_qty);
                        product_prize = (String) view.getTag(R.id.key_product_price);
                        Boolean IsPharma=(Boolean) view.getTag(R.id.key_is_pharma);

                        if(Json_type.equals("General Product"))
                        {
                            Intent generalIntenet = new Intent(mContext, GeneralProductSearchPage.class);
                            generalIntenet.putExtra("CategoryId",  (String) view.getTag(R.id.key_category_id));
                            generalIntenet.putExtra("subCategoryId", productid);
                            generalIntenet.putExtra("Categoryname",(String) view.getTag(R.id.key_category_name));
                            generalIntenet.putExtra("SortBy","LH");
                            generalIntenet.putExtra("BrandId","");
                            generalIntenet.putExtra("Fromprice","0");
                            generalIntenet.putExtra("Toprice","0");
                            mContext.startActivity(generalIntenet);
                        }
                        else if(!Json_type.equals("Salt")) {
                            if(IsPharma==true) {
                                get_singleproduct(productid);
                            }else
                            {  Intent Intenet_change = new Intent(mContext,GeneralProducyDetails.class);
                                Intenet_change.putExtra("Generalproductid",productid);
                                mContext.startActivity(Intenet_change);
                            }
                        }

                        else
                        {
                          Intent SaltIntenet = new Intent(mContext, SaltDetail.class);
                          SaltIntenet.putExtra("productid", productid);
                          mContext.startActivity(SaltIntenet);
                        }

                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                mContext).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void get_singleproduct(String p_productid) {
        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_PRODUCTDETAIL, p_productid, sMemberId);


        RequestQueue queue = Volley.newRequestQueue(mContext);

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
                        Error_singleorder();
                    }
                });

//        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
//                50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(jsonObjReq_single);

    }

    private void Success_singleorder(JSONObject response) {
        try {
            SharedPreferences pref = mContext.getSharedPreferences("Global", Context.MODE_PRIVATE);
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
            prefsEditor.putString("product_price", product_prize);
            prefsEditor.commit();

            create_buytabs();

        } catch (JSONException e) {
            hidePDialog();
            e.printStackTrace();
            f_alert_ok("Error : ",e.getMessage());
        }
    }

    private void create_buytabs() {


       Intent buytabsIntenet = new Intent(mContext, BuyTabs.class);
        buytabsIntenet.putExtra("productid", productid);

        mContext.startActivity(buytabsIntenet);
    }

    private void Error_singleorder() {
        f_alert_ok("Error :","Network not available");
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
    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(mContext)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

        }



        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                String Myresponse = urlConnection.getResponseMessage();

                /* 200 represents HTTP OK */
                if (statusCode == 200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject x = new JSONObject(response.toString());


                    Success_singleorder(x);

                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {

            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {


            hidePDialog();

            if (result == 1) {


            } else {

                Error_singleorder();
            }
        }
    }
}

package viroopa.com.medikart.buying;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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

import viroopa.com.medikart.ImageZoomProcduct;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;

public class PrescriptionDetails extends AppCompatActivity {


    private TextView presc_name,presc_for,patient_name,txt_gender,doctor_name,best_time,txt_comment,txt_isemergency,requestNumber;
    private ImageView img_first,img_second,img_third,img_fourth;
    private ProgressDialog pDialog;
    private String Prescption_id;
    AppController globalVariable;
    private  String Prescription_req_no;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);
        initImageLoader();
        globalVariable = (AppController) getApplicationContext();

        pDialog = new ProgressDialog(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        presc_name=(TextView)findViewById(R.id.presc_name);
        presc_for=(TextView)findViewById(R.id.presc_for);
        patient_name=(TextView)findViewById(R.id.patient_name);
        txt_gender=(TextView)findViewById(R.id.txt_gender);
        doctor_name=(TextView)findViewById(R.id.doctor_name);
        best_time=(TextView)findViewById(R.id.best_time);
        txt_comment=(TextView)findViewById(R.id.txt_comment);
        txt_isemergency=(TextView)findViewById(R.id.txt_isemergency);
        requestNumber=(TextView)findViewById(R.id.requestNumber);

        img_first=(ImageView) findViewById(R.id.img_first);
        img_second=(ImageView) findViewById(R.id.img_second);

        img_third=(ImageView) findViewById(R.id.img_third);
        img_fourth=(ImageView) findViewById(R.id.img_fourth);;

       // img_first.setOnTouchListener(iv_click);
        img_second.setOnTouchListener(iv_click);
        img_third.setOnTouchListener(iv_click);
        img_fourth.setOnTouchListener(iv_click);

        get_intent();
        get_prescriptionDetail();


    }

    private void get_prescriptionDetail() {

        showPdialog("Loading. . .");


        String url = String.format(AppConfig.URL_GET_PRESCRIPTION_DETAILS, Prescption_id);



        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest refillrequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    SuccessResponse(response.getJSONObject("prescription"));
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

    private  void SuccessResponse(JSONObject response)
    {
        try {
            if(!response.getString("RequestNo").equals("null")) {
                Prescription_req_no=response.getString("RequestNo");
                requestNumber.setText(response.getString("RequestNo"));
            }else
            {
                requestNumber.setText("");
            }

            if(!response.getString("PrescriptionName").equals("null")) {
                presc_name.setText(response.getString("PrescriptionName"));
            }else
            {
                presc_name.setText("");
            }

            if(!response.getString("PrescriptionFor").equals("null")) {
                presc_for.setText(response.getString("PrescriptionFor"));
            }else
            {
                presc_for.setText("");
            }

            if(!response.getString("MemberName").equals("null")) {
                patient_name.setText(response.getString("MemberName"));
            }else
            {
                patient_name.setText("");
            }

            if(!response.getString("MemberGender").equals("null")) {
                txt_gender.setText(response.getString("MemberGender"));
            }else
            {
                txt_gender.setText("");
            }

            if(!response.getString("DoctorName").equals("null")) {
                doctor_name.setText(response.getString("DoctorName"));
            }else
            {
                doctor_name.setText("");
            }
            if(!response.getString("Comment").equals("null")) {
                txt_comment.setText(response.getString("Comment"));
            }else
            {
                txt_comment.setText("");
            }

            if(!response.getString("isEmergencyDelivery").equals("null")) {
                txt_isemergency.setText(response.getString("isEmergencyDelivery"));
            }else
            {
                txt_isemergency.setText("");
            }


            switch (response.getString("BestTimeForCall")) {


                case "12":
                    best_time.setText("08 AM-12 NOON");
                    break;
                case "13":
                    best_time.setText("12 PM-03 PM");
                    break;
                case "14":
                    best_time.setText("03 PM-06 PM");
                    break;
                case "15":
                    best_time.setText("06 PM-08 PM");
                    break;
                case "17":
                    best_time.setText("ASAP");
                    break;

            }

            JSONArray imgArray= response.getJSONArray("preImg");




            for (int i = 0; i < imgArray.length(); i++) {
                JSONObject obj_json = imgArray.getJSONObject(i);
                imageLoader.clearDiskCache();
                imageLoader.clearMemoryCache();
                switch (i) {
                    case 0:
                        img_first.setTag(obj_json.getString("ImagePath"));
                        imageLoader.displayImage(obj_json.getString("ImagePath"), img_first);
                        break;
                    case 1:
                        img_second.setTag(obj_json.getString("ImagePath"));
                        imageLoader.displayImage(obj_json.getString("ImagePath"), img_second);
                        break;
                    case 2:
                        img_third.setTag(obj_json.getString("ImagePath"));
                        imageLoader.displayImage(obj_json.getString("ImagePath"), img_third);
                        break;
                    case 3:
                        img_fourth.setTag(obj_json.getString("ImagePath"));
                        imageLoader.displayImage(obj_json.getString("ImagePath"), img_fourth);
                        break;

                }


            }


        }catch (JSONException e)
        {
            e.toString();
        }


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

    private  void get_intent()
    {
        Intent i =getIntent();
        Prescption_id=i.getStringExtra("Prescption_id");
    }


    View.OnTouchListener iv_click = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event) {




            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    //   f_Touch_Down(v);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                  globalVariable.setImagePathForZoom((String)v.getTag());
                    image_zoom();
                }
                case MotionEvent.ACTION_CANCEL: {
                    // f_Touch_Cancel(v);
                    break;
                }
            }
            return true;



        }
    };

    private  void image_zoom()
    {
        Intent Intenet_imageZoom = new Intent(this, ImageZoomProcduct.class);
        //Intenet_imageZoom.putExtra("imagepath", imgpath);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("product_name", Prescription_req_no);
        editor.commit();
      //  product_name
        startActivity(Intenet_imageZoom);
    }

}

package viroopa.com.medikart;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import java.io.InputStream;
import java.net.URL;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.app.AppController;


public class ImageZoomProcduct extends AppCompatActivity {
    private WebView wb;
    private String imagepathforzoom;
    private   String productName="";
    AppController globalVariable;

    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom_procduct);
        wb= (WebView) findViewById(R.id.imgView2);
        globalVariable = (AppController) getApplicationContext();
        get_intent();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        getSupportActionBar().setTitle(productName);


        wb.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);

        wb.getSettings().setBuiltInZoomControls(true);
        //wb.getSettings().setUseWideViewPort(true);
       // wb.getSettings().setLoadWithOverviewMode(true);
        wb.setWebViewClient(new WebViewClient());
       // wb.loadUrl(imagepathforzoom);

        wb.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{width:100%25;} </style></head><body><img src='"+imagepathforzoom+"'/></body></html>", "text/html", "UTF-8");

        // new DownloadImageTask().execute(imagepathforzoom);



    }
    private void get_intent() {

        imagepathforzoom = globalVariable.getImagePathForZoom();
        SharedPreferences pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        productName=pref.getString("product_name", "");
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageTask() {

        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];

            Bitmap mIcon11 = null;
            try {
                URL url =new URL(urldisplay);
                InputStream in = new URL(urldisplay).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(urldisplay).getContent());
                mIcon11=bitmap;
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
           // mImage.setImageBitmap(result, null, -1, -1);
        }
    }

}

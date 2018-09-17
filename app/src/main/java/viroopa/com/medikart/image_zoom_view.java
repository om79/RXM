package viroopa.com.medikart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.app.AppController;

public class image_zoom_view extends AppCompatActivity {
    private ImageView imgView;
    private String imagepathforzoom;
    private Button okbtn;
    AppController globalVariable;
    private Bitmap mybitmap;
    //private WebView wb;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVariable = (AppController) getApplicationContext();
        get_intent();
        setContentView(R.layout.activity_image_zoom_view);
        imgView = (ImageView) findViewById(R.id.imgView2);
       // wb= (WebView) findViewById(R.id.imgView2);
        okbtn = (Button) findViewById(R.id.button6);

        if (mybitmap != null) {
            mybitmap.recycle();
            mybitmap = null;
        }

        mybitmap = BitmapFactory.decodeFile(imagepathforzoom);




       imgView.setImageBitmap(mybitmap);
//        if(mybitmap!=null)
//        {
//            mybitmap.recycle();
//            mybitmap=null;
//        }
        //imgView.setImageBitmap(BitmapFactory.decodeFile(imagepathforzoom));


        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void get_intent() {
        Intent intent_zoom = getIntent();
        //imagepathforzoom = intent_zoom.getStringExtra("imagepath");
        imagepathforzoom = globalVariable.getImagePathForZoom();
    }

    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getParentActivityIntent();
        intent = null;
        return intent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

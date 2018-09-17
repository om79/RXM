package viroopa.com.medikart.buying;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;

/**
 * Created by admin on 29/06/2015.
 */
public class networkerror extends AppCompatActivity {
    private  Button btnrefresh;
    private ProgressDialog pDialog;
    private AppController globalVariable;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.networkerror);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        globalVariable = (AppController) getApplicationContext();
        btnrefresh=(Button)findViewById(R.id.refreshbtn);


        btnrefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showPdialog("Checking Network........");
                if(haveNetworkConnection()==true)
                {
                    globalVariable.setIsNetwrkpageVisible(false);
                    finish();
                }
            }
        });
            }
    public Intent getSupportParentActivityIntent() {
        Intent newIntent = null;

        return null;
    }

    @Override
    public void onBackPressed() {
        showPdialog("Checking Network........");
        if(haveNetworkConnection()==true)
        {
            globalVariable.setIsNetwrkpageVisible(false);
            finish();
        }
        // super.onBackPressed();
        // finish();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        hidePDialog();
        return haveConnectedWifi || haveConnectedMobile;

    }


    private void network_check()
    {
        if(haveNetworkConnection() == true)
        {

        }else{
            Intent intent_network = new Intent(networkerror.this, networkerror.class);
            startActivity(intent_network);
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

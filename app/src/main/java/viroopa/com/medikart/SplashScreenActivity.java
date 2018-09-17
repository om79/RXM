package viroopa.com.medikart;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.app.ConnectionDetector;
import viroopa.com.medikart.downloadservice.DownloadDoctorService;
import viroopa.com.medikart.downloadservice.DownloadMemberService;
import viroopa.com.medikart.downloadservice.DownloadResultReceiver;
import viroopa.com.medikart.downloadservice.DownloadService;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SessionManager;
import viroopa.com.medikart.helper.SqliteMRHandler;
import viroopa.com.medikart.services.CheckAppVersion;


public class SplashScreenActivity extends Activity
        implements DownloadResultReceiver.Receiver {

    private String sMemberId = "";
    AppController globalVariable;
    private SQLiteHandler db;
    private SqliteMRHandler db_mr;
    private SessionManager session;
    String sImeNo = "";
    private Boolean isSlidingScreensShown = false;

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private DownloadResultReceiver mReceiver;

    private static final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Start_app_check();
        registerReceiver(broadcastReceiver, new IntentFilter("VersionCheckReciever"));
        globalVariable = (AppController) getApplicationContext();
        //GetIMEI();
        //String number = getMobileNumber();
        //Toast.makeText(getApplicationContext(), number,Toast.LENGTH_SHORT).show();
        if (isIMEIPermissionGranted() == true) {
            TelephonyManager mngr;
            mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            sImeNo = mngr.getDeviceId();
            globalVariable.setIMEInumber(sImeNo);
        }
        downloaddatafromserver();
    }


    private void downloaddatafromserver() {
        String url = String.format(AppConfig.URL_GET_CITY, "", "0");
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(SplashScreenActivity.this);
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadService.class);
        download_intent.putExtra("url", url);
        download_intent.putExtra("receiver", mReceiver);
        download_intent.putExtra("requestId", 101);
        startService(download_intent);
    }

    private void showmainscreen() {
        db = new SQLiteHandler(getApplicationContext());
        db_mr = new SqliteMRHandler(getApplicationContext());
        sMemberId = db.getMemberId();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("memberid", sMemberId);
        editor.commit();
        // User is already logged in. Take him to main activity
        //Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("memberid", sMemberId);
        startActivity(intent);
        finish();
    }

    private void showSlidingScreens() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isSlidingScreensShown", true);
        editor.commit();
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showloginscreen() {
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public String getMobileNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String strMobileNumber = telephonyManager.getLine1Number();
        return strMobileNumber;
    }

    /*
     * Async Task to make http call
     */
    /*private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls
            Log.e("JSON", "Pre execute");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            *//*
             * Will make http call here This call will download required data
             * before launching the app
             * example:
             * 1. Downloading and storing SQLite
             * 2. Downloading images
             * 3. Parsing the xml / json
             * 4. Sending device information to server
             * 5. etc.,
             *//*

            *//*JsonParser jsonParser = new JsonParser();
            String json = jsonParser
                    .getJSONFromUrl("http://api.androidhive.info/game/game_stats.json");


            Log.e("Response: ", "> " + json);
            if (json != null) {
                try {
                    JSONObject jObj = new JSONObject(json)
                            .getJSONObject("game_stat");
                    now_playing = jObj.getString("now_playing");
                    earned = jObj.getString("earned");
                    Log.e("JSON", "> " + now_playing + earned);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }*//*
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity
            //Intent i = new Intent(SplashScreen.this, MainActivity.class);
            //i.putExtra("now_playing", now_playing);
            //i.putExtra("earned", earned);
            //startActivity(i);
            // close this activity
            finish();
        }

    }*/

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
           /* case DownloadService.STATUS_RUNNING:

                //setProgressBarIndeterminateVisibility(true);
                setProgressBarIndeterminateVisibility(true);
                break;
            case DownloadService.STATUS_FINISHED:
                *//* Hide progress & extract result from bundle *//*
                //setProgressBarIndeterminateVisibility(false);

                setProgressBarIndeterminateVisibility(false);

                //String[] results = resultData.getStringArray("result");

                //arrayAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, results);
                //myspinner.setAdapter(arrayAdapter);

                break;
            case DownloadService.STATUS_ERROR:
                *//* Handle the error *//*
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;*/
        }
    }

    /* private int checkcityExist()
     {

         SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
         String sCity = (pref.getString("citylist", ""));
         return sCity.length();
     }

     private int checkDoctorListExist()
     {

         SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
         String sCity = (pref.getString("doctorlist", ""));
         return sCity.length();
     }*/
    private void downloaddoctordatafromserver() {
        /* Starting Download Service */
        String url = String.format(AppConfig.URL_GET_CITY, "", "0");
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(SplashScreenActivity.this);
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadDoctorService.class);
        //Send optional extras to Download IntentService
        download_intent.putExtra("url", url);
        download_intent.putExtra("receiver", mReceiver);
        download_intent.putExtra("requestId", 101);
        startService(download_intent);
        /* End Download Service */
    }

    private void downloadmemberdatafromserver() {
        /* Starting Download Service */
        String url = String.format(AppConfig.URL_GET_CITY, "", "0");
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(SplashScreenActivity.this);
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadMemberService.class);
        //Send optional extras to Download IntentService
        download_intent.putExtra("url", url);
        download_intent.putExtra("receiver", mReceiver);
        download_intent.putExtra("requestId", 101);
        startService(download_intent);

        /* End Download Service */
    }

    private void GetIMEI() {


        TelephonyManager mngr;
        mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (isIMEIPermissionGranted() == true) {
            sImeNo = mngr.getDeviceId();
        } else {
            sImeNo = "1001";

        }

        //return sImeNo ;
    }


    public boolean isIMEIPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_PHONE_STATE)) {
                    sImeNo = "1001";
                    globalVariable.setIMEInumber(sImeNo);

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                }
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);

            TelephonyManager mngr;
            mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            sImeNo = mngr.getDeviceId();
            globalVariable.setIMEInumber(sImeNo);
            //resume tasks needing this permission
        } else {
            sImeNo = "1001";
            globalVariable.setIMEInumber(sImeNo);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("message");
            if (message.equals("updated")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /* Create an Intent that will start the Menu-Activity. */
                        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
                        // check for Internet status
                       // if (isInternetPresent) {
                      //      new PrefetchData().execute();
                      //  }
                        // Session manager
                        session = new SessionManager(getApplicationContext());
                        // Check if user is already logged in or not
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                        isSlidingScreensShown = pref.getBoolean("isSlidingScreensShown", false);
                        if (isSlidingScreensShown) {
                            if (session.isLoggedIn()) {
                                showmainscreen();
                                downloaddoctordatafromserver();
                                downloadmemberdatafromserver();
                            } else {
                                showloginscreen();
                            }
                        } else {
                            showSlidingScreens();
                        }
                    }
                }, SPLASH_DISPLAY_LENGTH);
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            //if the user agrees to upgrade
                            public void onClick(DialogInterface dialog, int id) {
                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        });
                       /* .setNegativeButton("Remind Later", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                *//* Create an Intent that will start the Menu-Activity. *//*
                                        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                                        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false


                                        // check for Internet status
                                        if (isInternetPresent) {
                                            new PrefetchData().execute();
                                        }
                                        // Session manager
                                        session = new SessionManager(getApplicationContext());

                                        // Check if user is already logged in or not
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                                        isSlidingScreensShown= pref.getBoolean("isSlidingScreensShown",false);

                                        if(isSlidingScreensShown) {

                                            if (session.isLoggedIn()) {
                                                showmainscreen();
                                                downloaddoctordatafromserver();
                                                downloadmemberdatafromserver();

                                            } else {
                                                showloginscreen();
                                            }
                                        }else{
                                            showSlidingScreens();
                                        }
                                    }
                                }, SPLASH_DISPLAY_LENGTH);
                            }
                        }*/

                //show the alert message
                builder.create().show();
            }
        }
    };

    private void Start_app_check() {
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, CheckAppVersion.class);
        startService(download_intent);

    }
}

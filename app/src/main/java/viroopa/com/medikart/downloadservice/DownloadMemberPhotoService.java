package viroopa.com.medikart.downloadservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import viroopa.com.medikart.app.AppConfig;

public class DownloadMemberPhotoService extends IntentService {


    private String sMemberId;
    private static final String TAG = "DownloadService";
    JSONArray jsonarray;
    public DownloadMemberPhotoService () {
        super(DownloadMemberPhotoService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");
            try {

                getIntenet();
                get_Member_Phto();

        }catch (Exception e)
            {}
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }



    private void get_Member_Phto() {

        //showPdialog("loading...");
        final Integer results =  0;

        //String url = String.format(AppConfig.URL_GET_MEMBER_PHOTO,sMemberId);
        String url = String.format( AppConfig.URL_GET_MEMBER_PHOTO, sMemberId);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest staterequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // hidePDialog();
                        try
                        {


                            if (null != response ) {




                                if(response.optString("ImageStream")!=null) {
                                    byte[] xya =  Base64.decode(response.optString("ImageStream"), 0);
                                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
                                    String dwnldImage = mediaStorageDir.getPath() + File.separator + response.optString("ImageName");
                                    File obj_compressfile = new File(dwnldImage);
                                    try {


                                        FileOutputStream fos = new FileOutputStream(obj_compressfile);
                                        fos.write(xya);
                                        fos.flush();
                                        fos.close();
                                    } catch (Exception e) {
                                    }
                                }


                                jsonarray = response.getJSONArray("GetFamilyImageList");


                                for (int i = 0; i < jsonarray.length(); i++) {

                                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                                    String imageName=jsonobject.optString("ImageName");
                                    if(jsonobject.optString("ImageStream")!=null) {
                                        if (!imageName.startsWith("avtar")) {

                                            byte[] xya = Base64.decode(jsonobject.optString("ImageStream"), 0);
                                            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);

                                            if (!mediaStorageDir.exists())
                                            {
                                                mediaStorageDir.mkdirs();
                                                Log.d(TAG , "Folder created at: "+mediaStorageDir.toString());
                                            }
                                            String dwnldImage = mediaStorageDir.getPath() + File.separator + imageName;
                                            File obj_compressfile = new File(dwnldImage);

                                            try {


                                                FileOutputStream fos = new FileOutputStream(obj_compressfile);
                                                fos.write(xya);
                                                fos.flush();
                                                fos.close();
                                            } catch (Exception e) {
                                            }

                                        }
                                    }
                                }
                            }


                        }
                        catch (Exception e) {
                            //hidePDialog();
                            //results = Boolean.TRUE;
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //hidePDialog();
                    }
                });


        queue.add(staterequest);
    }




    private void getIntenet()
    {
        //Intent intent_buymainactivity = getIntent();
        //sMemberId = intent_buymainactivity.getStringExtra("memberid");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

    }
}
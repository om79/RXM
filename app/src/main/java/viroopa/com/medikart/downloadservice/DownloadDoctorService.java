package viroopa.com.medikart.downloadservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import viroopa.com.medikart.app.AppConfig;

public class DownloadDoctorService extends IntentService {
    private String sMemberId;
    private static final String TAG = "DownloadService";

    public DownloadDoctorService () {
        super(DownloadDoctorService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");
            try {
                getIntenet();
                get_doctorlist();
            } catch (Exception e) {
            }

    }
    private void get_doctorlist() {
        //showPdialog("loading...");
        String url = String.format( AppConfig.URL_GET_LOCALDOCTORLIST, "0", sMemberId);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest refillrequest = new JsonArrayRequest(url,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "Register Response: " + response.toString());
                success_doctor(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Register Response: " + error.getMessage());

                        error_doctor(error);
                    }
                });

        queue.add(refillrequest);
    }
    public  void success_doctor(JSONArray response)
    {
        try
        {
            if (null != response )
            {
                String sDoctorList = response.toString();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = pref.edit();
                prefsEditor.putString("doctorlist", sDoctorList);
                prefsEditor.commit();
            }
        }
        catch (Exception e) {
            //hidePDialog();
            //results = Boolean.TRUE;
            e.printStackTrace();
        }
    }

    public void error_doctor(VolleyError error)
    {
        error.toString();
    }

    private int checkDoctorExist()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        String sCity = (pref.getString("doctorlist", ""));
        return sCity.length();
    }

    private void getIntenet()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

    }
}
package viroopa.com.medikart.downloadservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import viroopa.com.medikart.app.AppConfig;

public class DownloadService extends IntentService {


    private String sMemberId;
    private static final String TAG = "DownloadService";

    public DownloadService () {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");


            try {

                getIntenet();

                if(checkStateExist() <= 0)
                {
                    get_statelist();
                }
                if(checkcityExist() <= 0)
                {
                    get_citylist();
                }

            } catch (Exception e) {


            }

    }

    private void get_citylist() {

        //showPdialog("loading...");
        final Integer results =  0;

        String url = String.format( AppConfig.URL_GET_CITY, "", "0");

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest cityrequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // hidePDialog();
                        try
                        {


                            if (null != response )
                            {
                                String sCitylist = response.getJSONArray("cityModel").toString();
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = pref.edit();
                                prefsEditor.putString("citylist", sCitylist);
                                prefsEditor.commit();
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


        queue.add(cityrequest);
    }

    private void get_statelist() {

        //showPdialog("loading...");
        final Integer results =  0;

        String url = AppConfig.URL_GET_STATE;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest staterequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // hidePDialog();
                        try
                        {


                            if (null != response )
                            {
                                String sStatelist = response.getJSONArray("StateList").toString();
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = pref.edit();
                                prefsEditor.putString("statelist", sStatelist);
                                prefsEditor.commit();
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


    private int checkStateExist()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        String sCity = (pref.getString("statelist", ""));
        return sCity.length();
    }
    private int checkcityExist()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        String sCity = (pref.getString("citylist", ""));
        return sCity.length();
    }

    private void getIntenet()
    {
        //Intent intent_buymainactivity = getIntent();
        //sMemberId = intent_buymainactivity.getStringExtra("memberid");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        String doctor=pref.getString("doctorlist", "");
    }
}
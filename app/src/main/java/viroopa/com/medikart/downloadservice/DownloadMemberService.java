package viroopa.com.medikart.downloadservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.Memberlist;

public class DownloadMemberService extends IntentService {

    private String sMemberId;
    private static final String TAG = "DownloadService";
    JSONArray jsonarray;

    public DownloadMemberService() {
        super(DownloadMemberService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        try {
            getIntenet();
            getFamilyMemberlist();


        } catch (Exception e) {

        }

    }

    private void getFamilyMemberlist() {
        //showPdialog("loading...");
        String tag_string_req = "req_FamilyMember";
        //final messagebox mb = new messagebox();
        String tag_FamilyMember_list = "json_FamilyMember";
        //String url = String.format(AppConfig.URL_GET_MEMBERLIST, sMemberId);
        String url = String.format(AppConfig.URL_GET_MEMBERFamilyLIST, sMemberId);
        String vmembername = "";
        JsonObjectRequest FamilyMemberrequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Success_FamilyMember(response);
                        // hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d(TAG, "Register Response: " + error.getMessage());
                        //hidePDialog();
                        Error_FamilyMember(error);
                    }
                });

        FamilyMemberrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(FamilyMemberrequest, tag_string_req);
    }


    private void Success_FamilyMember(JSONObject response) {
        //final messagebox mb = new messagebox();
        try {
            //memberList.clear();

            jsonarray = response.getJSONArray("PatientList");

            // ObjectItemData = new M_objectItem[jsonarray.length()];
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                Memberlist memberdetails = new Memberlist();

                memberdetails.setMemberId(jsonobject.optInt("MemberId"));
                memberdetails.setMemberName(jsonobject.optString("MemberName"));
                memberdetails.setMemberGender(jsonobject.optString("MemberGender"));
                memberdetails.setRelationshipId(jsonobject.optInt("RelationshipId"));
                memberdetails.setMemberDOB(jsonobject.optString("MemberDOB"));

                String vmembername = jsonobject.getString("MemberName");

                String sMemberList = response.getJSONArray("PatientList").toString();

                SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = pref.edit();
                prefsEditor.putString("memberList", sMemberList);
                prefsEditor.commit();
            }

        } catch (Exception e) {

            Log.e("Error", e.getMessage());
            //mb.showAlertDialog(fra, "Error", e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void Error_FamilyMember(VolleyError error) {

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(this, "TimeoutError", Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            //TODO
            Toast.makeText(this, "AuthFailureError", Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            //TODO
            Toast.makeText(this, "ServerError", Toast.LENGTH_LONG).show();
        } else if (error instanceof NetworkError) {
            //TODO
            Toast.makeText(this, "NetworkError", Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            //TODO
            Toast.makeText(this, "ParseError", Toast.LENGTH_LONG).show();
        }


        //final messagebox mb = new messagebox();
        //mb.showAlertDialog(this, "error", "ErrorListener", true);

    }


    private int checkMemberListExist() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        String sCity = (pref.getString("memberList", ""));
        return sCity.length();
    }

    private void getIntenet() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

    }

    private void download_profile_pic() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        String profilePic = (pref.getString("imagepath", ""));

        String imgeName = profilePic.substring(profilePic.lastIndexOf('/') + 1, profilePic.length());

        String iconsStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + AppConfig.IMAGE_DIRECTORY_NAME;

        File pathfile = new File(iconsStoragePath + File.separator + imgeName);
        Bitmap bmp = BitmapFactory.decodeFile(pathfile.getPath());
        if (bmp == null) {

        }

    }
}
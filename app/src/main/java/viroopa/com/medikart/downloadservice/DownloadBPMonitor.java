package viroopa.com.medikart.downloadservice;

import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import java.util.HashMap;
import java.util.Map;

import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.helper.SqliteBPHandler;

public class DownloadBPMonitor extends IntentService {
    private String sMemberId;
    SqliteBPHandler db;
    private String BPId, member_id, Relationship_ID;
    private Integer getSelectedRelationshipId;
    private static final String TAG = "DownloadService";
    AppController globalVariable;

    public DownloadBPMonitor() {
        super(DownloadBPMonitor.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service Started!");
        try {
            db = new SqliteBPHandler(this);
            globalVariable = (AppController) getApplicationContext();
            getIntenet();
            UploadBPDetail();
        } catch (Exception e) {
        }
    }

    private void UploadBPDetail() {
        Cursor cursor_data;
        Integer iMemId = Integer.parseInt(sMemberId);
        cursor_data = db.getAllBPMonitorData(iMemId, getSelectedRelationshipId);
        cursor_data.moveToFirst();
        while (!cursor_data.isAfterLast()) {
            BPId = cursor_data.getString(cursor_data.getColumnIndex("SYNC_BPID"));
            member_id = cursor_data.getString(cursor_data.getColumnIndex("Member_Id"));
            String body_part = cursor_data.getString(cursor_data.getColumnIndex("Body_Part"));
            String position = cursor_data.getString(cursor_data.getColumnIndex("Position"));
            String systolic = cursor_data.getString(cursor_data.getColumnIndex("Systolic"));
            String diastolic = cursor_data.getString(cursor_data.getColumnIndex("Diastolic"));
            String pulse = cursor_data.getString(cursor_data.getColumnIndex("Pulse"));
            String weight = cursor_data.getString(cursor_data.getColumnIndex("Weight"));
            String weight_unit = cursor_data.getString(cursor_data.getColumnIndex("Weight_Unit"));
            String arrthythmia = cursor_data.getString(cursor_data.getColumnIndex("Arrthythmia"));
            String comments = cursor_data.getString(cursor_data.getColumnIndex("Comments"));
            String bp_date = cursor_data.getString(cursor_data.getColumnIndex("Bp_Date"));
            String bp_time = cursor_data.getString(cursor_data.getColumnIndex("Bp_Time"));
            String bptimehr = cursor_data.getString(cursor_data.getColumnIndex("BpTimehr"));
            String kg = cursor_data.getString(cursor_data.getColumnIndex("kg"));
            String lb = cursor_data.getString(cursor_data.getColumnIndex("lb"));
            Relationship_ID = cursor_data.getString(cursor_data.getColumnIndex("RelationshipId"));
            String IMEI_no = cursor_data.getString(cursor_data.getColumnIndex("IMEI_No"));
            Map<String, String> params = new HashMap<String, String>();

            params.put("user_id", sMemberId);
            params.put("body_part", body_part);
            params.put("position", position);
            params.put("systolic", systolic);
            params.put("diastolic", diastolic);
            params.put("pulse", pulse);
            params.put("weight", weight);
            params.put("weight_unit", weight_unit);
            params.put("arrthythmia", arrthythmia);
            params.put("comments", comments);
            params.put("bp_date", bp_date);
            params.put("bp_time", bp_time);
            params.put("bptimehr", sMemberId);
            params.put("Relationship_ID", Relationship_ID);
            params.put("lb", lb);
            params.put("kg", kg);
            JSONObject jparams = new JSONObject(params);
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConfig.URL_POST_ADDBPDATAJson,
                    //  new JSONObject(params),
                    jparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Success_add_BPDetail(response);
                            // hidePDialog();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Error_add_BPDetail(error);
                            //hidePDialog();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("charset", "utf-8");
                    headers.put("User-agent", "medikart");
                    return headers;
                }
            };
            jor_inhurry_post.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //AppController.getInstance().addToRequestQueue(jor_inhurry_post, Post_registration);
            queue.add(jor_inhurry_post);
            cursor_data.moveToNext();
        }
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void Success_add_BPDetail(JSONObject response) {
        try {
            if (response.getString("bReturnFlag").equals("true")) {
                db.deleteSyncBPDetails(BPId, member_id, Relationship_ID);
            } else {
                //f_alert_ok("Information", response.getString("sReturnMsg"));
            }
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void Error_add_BPDetail(VolleyError error) {

    }

    private void getIntenet() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        if (globalVariable.getRealationshipId() != null) {
            getSelectedRelationshipId = Integer.parseInt(globalVariable.getRealationshipId());
        } else {
            getSelectedRelationshipId = 8;
        }
    }
}
package viroopa.com.medikart.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.helper.SQLiteHandler;


/**
 * Created by ABCD on 04/03/2016.
 */
public class SyncDataService extends IntentService {

    public static final String TAG = "RxReminder";
    private SQLiteHandler db;
    private Integer[] list;
    JsonObjectRequest jor_inhurry_post;
    private String I_Type ;
    private String Controller ;
    private String Parameter ;
    private String JsonObject ;
    private String Created_Date ;
    private String UUID ;
    private String MemberId ;
    private String Module_Name ;
    private String Mode;
    private String ControllerName;
    private String MethodName ;
    private String IMEI ;
    private String Upload_Download;
    public SyncDataService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sync_data();
    }

    private void sync_data()
    {
        db = new SQLiteHandler(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        Cursor cursor = db.getAllSyncData();
        int i=0;
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer SyncId = cursor.getInt(cursor.getColumnIndex("SyncId"));
                list = new Integer[cursor.getCount()];
                list[i] = SyncId;
                I_Type = cursor.getString(cursor.getColumnIndex("I_Type"));
                Controller = cursor.getString(cursor.getColumnIndex("Controller"));
                Parameter = cursor.getString(cursor.getColumnIndex("Parameter"));
                JsonObject = cursor.getString(cursor.getColumnIndex("JsonObject"));
                Created_Date = cursor.getString(cursor.getColumnIndex("Created_Date"));
                UUID = cursor.getString(cursor.getColumnIndex("UUID"));
                Upload_Download = cursor.getString(cursor.getColumnIndex("Upload_Download"));
                MemberId = cursor.getString(cursor.getColumnIndex("MemberId"));
                Module_Name = cursor.getString(cursor.getColumnIndex("Module_Name"));
                Mode = cursor.getString(cursor.getColumnIndex("Mode"));
                ControllerName = cursor.getString(cursor.getColumnIndex("ControllerName"));
                MethodName = cursor.getString(cursor.getColumnIndex("MethodName"));
                IMEI = cursor.getString(cursor.getColumnIndex("IMEI"));
                Map<String, String> params = new HashMap<String, String>();
                params.put("Id", "0");
                params.put("I_Type", I_Type);
                params.put("Controller", Controller);
                params.put("Parameter", Parameter);
                params.put("JsonObject", JsonObject);
                params.put("Created_Date", Created_Date);
                params.put("UUID", UUID);
                params.put("Upload_Download", "true");
                params.put("MemberId", MemberId);
                params.put("Module_Name", Module_Name);
                params.put("Mode", Mode);
                params.put("ControllerName", ControllerName);
                params.put("MethodName", MethodName);
                params.put("IMEI", IMEI);
                params.put("ReqFrom", "A");
                JSONObject jparams = new JSONObject(params);
                jor_inhurry_post = new JsonObjectRequest(
                        Request.Method.POST,
                        AppConfig.URL_POST_ADDSYNCLOGJson,
                        //  new JSONObject(params),
                        jparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Success(response);
                                //hidePDialog();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Error(error);
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
                cursor.moveToNext();
                i++;
            }

            cursor.close();
            queue.add(jor_inhurry_post);
            for(int j=0;j<list.length;j++)
            {
                db.deleteSyncTableById((list[j]));
            }
        }
    }
    private void Success(JSONObject response)
    {
        response.toString();
    }
    private void Error(VolleyError error)
    {
        error.toString();
    }
}

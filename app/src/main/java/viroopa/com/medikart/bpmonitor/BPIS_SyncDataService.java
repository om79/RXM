package viroopa.com.medikart.bpmonitor;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.helper.SQLiteHandler;

public class BPIS_SyncDataService extends IntentService {

    private static final String TAG = "BPIS_SyncDataService";

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    private String sMemberId;

    private Integer[] list;
    private SQLiteHandler db;
    JSONArray jsonarray;
    JsonObjectRequest jor_inhurry_post;

    public BPIS_SyncDataService () {
        super(BPIS_SyncDataService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {


                getIntenet();
                sync_data();

               /* if(checkMemberListExist() <= 0)
                {
                    //get_doctorlist();
                    getFamilyMemberlist();
                }*/


                receiver.send(STATUS_FINISHED, bundle);

            } catch (Exception e) {

                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }


    private String[] downloadData(String requestUrl) throws IOException, DownloadException {
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);

        urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
        urlConnection.setRequestMethod("GET");

        int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            String response = convertInputStreamToString(inputStream);

            String[] results = parseResult(response);

            return results;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private String[] parseResult(String result) {

        String[] blogTitles = null;
        try {
            JSONObject response = new JSONObject(result);

            JSONArray posts = response.optJSONArray("posts");

            blogTitles = new String[posts.length()];

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");

                blogTitles[i] = title;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return blogTitles;
    }

    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }


    private void getIntenet() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

    }

    private void sync_data() {
        //db = new SQLiteHandler(this);
        RequestQueue queue = Volley.newRequestQueue(this);

        db = SQLiteHandler.getInstance (this);
        Cursor cursor = db.getAllSyncData();
        int i = 0;

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {


                Integer SyncId = cursor.getInt(cursor.getColumnIndex("SyncId"));

                list = new Integer[cursor.getCount()];
                list[i] = SyncId;

                String I_Type = cursor.getString(cursor.getColumnIndex("I_Type"));
                String Controller = cursor.getString(cursor.getColumnIndex("Controller"));
                String Parameter = cursor.getString(cursor.getColumnIndex("Parameter"));
                String JsonObject = cursor.getString(cursor.getColumnIndex("JsonObject"));
                String Created_Date = cursor.getString(cursor.getColumnIndex("Created_Date"));
                String UUID = cursor.getString(cursor.getColumnIndex("UUID"));
                String Upload_Download = cursor.getString(cursor.getColumnIndex("Upload_Download"));
                String MemberId = cursor.getString(cursor.getColumnIndex("MemberId"));
                String Module_Name = cursor.getString(cursor.getColumnIndex("Module_Name"));
                String Mode = cursor.getString(cursor.getColumnIndex("Mode"));
                String ControllerName = cursor.getString(cursor.getColumnIndex("ControllerName"));
                String MethodName = cursor.getString(cursor.getColumnIndex("MethodName"));
                String IMEI = cursor.getString(cursor.getColumnIndex("IMEI"));

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

                //AppController.getInstance().addToRequestQueue(jor_inhurry_post, Post_registration);

                cursor.moveToNext();
                i++;
            }

            cursor.close();
            queue.add(jor_inhurry_post);

            for (int j = 0; j < list.length; j++)

            {
                db.deleteSyncTableById((list[j]));
            }
        }
    }

    private void Success(JSONObject response) {
        response.toString();
    }

    private void Error(VolleyError error) {
        error.toString();
    }

}
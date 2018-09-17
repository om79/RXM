package viroopa.com.medikart.services;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import viroopa.com.medikart.Reciever.VersionCheckReciever;
import viroopa.com.medikart.app.AppConfig;
/**
 * Created by ABCD on 14/06/2016.
 */
public class CheckAppVersion extends IntentService {
    public static final String RESPONSE_MESSAGE = "myResponseMessage";
    private String URL = null;
    String version_number="0";
    public CheckAppVersion() {
        super("CheckAppVersion");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String message=get_version_number_playstore();
    }

    private String get_version_number_playstore() {
        URL=String.format(AppConfig.URL_GET_VERSION_NUMBER);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest staterequest = new JsonObjectRequest(Request.Method.GET,URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // hidePDialog();
                        try
                        {
                            version_number=response.getString("version");
                            Intent broadcastIntent = new Intent(CheckAppVersion.this, VersionCheckReciever.class);
                            broadcastIntent.putExtra(RESPONSE_MESSAGE, version_number);
                            sendBroadcast(broadcastIntent);
                        }
                        catch (Exception e) {
                            Intent i = new Intent("VersionCheckReciever");
                            i.putExtra("message", "updated");
                            sendBroadcast(i);
                            //hidePDialog();
                            //results = Boolean.TRUE;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent i = new Intent("VersionCheckReciever");
                        i.putExtra("message", "updated");
                        sendBroadcast(i);
                        //hidePDialog();
                    }
                });
        queue.add(staterequest);
        return version_number;
    }

}
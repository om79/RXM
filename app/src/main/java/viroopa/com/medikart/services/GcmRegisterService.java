package viroopa.com.medikart.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.ConstData;

public class GcmRegisterService extends IntentService {

    GoogleCloudMessaging gcmObj;
    AppController globalVariable;
    SharedPreferences pref;
    private String sMemberId;

    public GcmRegisterService() {
        super(GcmRegisterService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        globalVariable = (AppController) getApplicationContext();
        pref = globalVariable.getSharedPreferences("Global", MODE_PRIVATE);
        try {
            if (!Get_local_GCM_id()) {
                registerGCM();
            }

        } catch (Exception e) {

        }
    }

    private void registerGCM() {
        SharedPreferences.Editor editor = pref.edit();
        try {
            if (gcmObj == null) {
                gcmObj = GoogleCloudMessaging
                        .getInstance(getApplicationContext());
            }
            String reg_id = gcmObj.register(AppConfig.GOOGLE_PROJ_ID);
            editor.putString(ConstData.SP_GCM_REGISTERED_ID, reg_id);
            update_new_gcm_id(reg_id);
            editor.commit();

        } catch (IOException ex) {
            GcmRegisterService.this.stopSelf();
            ex.getMessage();
        }
    }
    private Boolean Get_local_GCM_id() {
        String id = pref.getString(ConstData.SP_GCM_REGISTERED_ID, "");
        if (id.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private void update_new_gcm_id(String id) {
        sMemberId = pref.getString("memberid", "");
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("iMemberid", sMemberId);
        jsonParams.put("gcm_id", id);
        JSONObject jparams = new JSONObject(jsonParams);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                AppConfig.URL_POST_GCM_ID,
                jparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);
    }
}
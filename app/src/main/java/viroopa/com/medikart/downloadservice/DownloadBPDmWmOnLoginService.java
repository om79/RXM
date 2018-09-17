package viroopa.com.medikart.downloadservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.helper.SqliteBPHandler;
import viroopa.com.medikart.helper.SqliteDMHandler;
import viroopa.com.medikart.helper.SqliteMRHandler;
import viroopa.com.medikart.helper.SqliteWMHandler;


/**
 * Created by ABCD on 04/03/2016.
 */
public class DownloadBPDmWmOnLoginService extends IntentService {


    AppController globalVariable;

    private SqliteMRHandler db_mr;
    private SqliteBPHandler db_bp;
    private SqliteDMHandler db_dm;
    private SqliteWMHandler db_wm;
    SimpleDateFormat dateFormat_query_popup = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormat_sql = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat dateFormat_orginal = new SimpleDateFormat("yyyy-MM-dd");
    private String sMemberId;
    private Date current_date = Calendar.getInstance().getTime();

    public DownloadBPDmWmOnLoginService() {

        super("MR_GetMedfriedDataService");
        // Log.d(TAG, "NotificationService: ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        db_mr = new SqliteMRHandler(this);
        db_bp= new SqliteBPHandler(this);
        db_dm= new SqliteDMHandler(this);
        db_wm= new SqliteWMHandler(this);

        getIntenet();


        String url = String.format(AppConfig.URL_GET_MED_FRIEND_DATA, sMemberId);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest datarequest = new JsonObjectRequest(url,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                success_member_Data(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.toString();
                    }
                });

        queue.add(datarequest);
    }





    private void getIntenet() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

    }

    private  void success_member_Data(JSONObject response)
    {
        String Bp_date="";
        try {
            JSONArray bp_log_array = response.getJSONArray("v_cfe_user_bp_log");
            if(bp_log_array.length()>0)
            {
                db_bp.deleteBPData();
            }

            for (int i = 0; i < bp_log_array.length(); i++) {

                JSONObject jsonobject = bp_log_array.getJSONObject(i);

                try {
                    current_date = dateFormat_sql.parse(  jsonobject.optString("BPDate"));
                    Bp_date=dateFormat_orginal.format(current_date);

                } catch (Exception e) {
                }

                db_bp.addBPDetails(
                        sMemberId,
                        jsonobject.optString("Position"),
                        jsonobject.optString("BodyPart"),
                        jsonobject.optString("Systolic"),
                        jsonobject.optString("Diastolic"),
                        jsonobject.optString("Pulse"),
                        jsonobject.optString("Weight"),
                        jsonobject.optString("Weight_Unit"),
                        jsonobject.optInt("Arrthythmia"),
                        jsonobject.optString("Comment"),
                        Bp_date,
                        jsonobject.optString("BPTime"),
                        jsonobject.optInt("BPTimeHour"),
                        jsonobject.optString("kg"),
                        jsonobject.optString("lb"),
                        jsonobject.optInt("Relationship_ID"),
                        jsonobject.optLong("IMEI"),
                        jsonobject.optString("UUID"));
            }

        }catch (JSONException e)
        {

        }
        try {
            JSONArray dm_log_array = response.getJSONArray("v_user_dm_log");
            if(dm_log_array.length()>0)
            {
                db_dm.deleteDMDataAll();
            }

            for (int i = 0; i < dm_log_array.length(); i++) {

                JSONObject jsonobject = dm_log_array.getJSONObject(i);

                db_dm.addDMDetails(Integer.parseInt(sMemberId),
                        jsonobject.optInt("relation_id"),
                        jsonobject.optString("weight_number"),
                        jsonobject.optString("weight_unit"),
                        jsonobject.optString("g_date"),
                        jsonobject.optString("g_time"),
                        jsonobject.optString("g_value"),
                        jsonobject.optString("g_unit"),
                        jsonobject.optString("add_note"),
                        jsonobject.optString("g_category"),
                        jsonobject.optString("s_reminder"),
                        jsonobject.optString("IMEI"),
                        jsonobject.optString("UUID"),
                        jsonobject.optString("Injection_Site"),
                        jsonobject.optInt("Injection_Position"),
                        jsonobject.optString("AMPM"),
                                  "a",
                        jsonobject.optString("g_mmolval"),
                        jsonobject.optString("lbweight_number"));
            }

        }catch (JSONException e)
        {

        }

        try {
            String wm_date="";
            JSONArray wm_setting_array = response.getJSONArray("v_wm_setting");
            if(wm_setting_array.length()>0)
            {
                db_wm.delete_wm_settings();
            }

            for (int i = 0; i < wm_setting_array.length(); i++) {

                JSONObject jsonobject = wm_setting_array.getJSONObject(i);


                try {
                    current_date = dateFormat_sql.parse(  jsonobject.optString("created_date"));
                    wm_date=dateFormat_orginal.format(current_date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                db_wm.addWMSetting(Integer.parseInt(sMemberId),
                        jsonobject.optString("main_uuid"),
                        jsonobject.optInt("Goal_set"),
                        wm_date,
                        jsonobject.optString("IMEI_main"),
                        jsonobject.optInt("Relationship_ID"),
                      250);//  jsonobject.optInt("glass_size")
            }


        }catch (JSONException e)
        {

        }



        try {
            String wm_date="";
            JSONArray wm_entries_array = response.getJSONArray("v_wm_entries");
            if(wm_entries_array.length()>0)
            {
                db_wm.delete_wm_entries();
            }

            for (int i = 0; i < wm_entries_array.length(); i++) {

                JSONObject jsonobject = wm_entries_array.getJSONObject(i);


                try {
                    current_date = dateFormat_sql.parse(  jsonobject.optString("created_date"));
                    wm_date=dateFormat_orginal.format(current_date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                db_wm.addWMEntries(Integer.parseInt(sMemberId),
                        wm_date,
                        jsonobject.optString("quantity"),
                        jsonobject.optInt("Relationship_ID"),
                        jsonobject.optString("UUID"),
                        jsonobject.optString("IMEI"),
                       "");
            }


        }catch (JSONException e)
        {

        }


    }


}

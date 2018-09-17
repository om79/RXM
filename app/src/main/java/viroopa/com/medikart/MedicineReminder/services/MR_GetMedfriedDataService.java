package viroopa.com.medikart.MedicineReminder.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import viroopa.com.medikart.MedicineReminder.MRA_reminder_notification;
import viroopa.com.medikart.MedicineReminder.Reciever.NotifyActivityHandler;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.helper.SqliteMRHandler;


/**
 * Created by ABCD on 04/03/2016.
 */
public class MR_GetMedfriedDataService extends IntentService {


    AppController globalVariable;

    private SqliteMRHandler db_mr;
    SimpleDateFormat dateFormat_query_popup = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormat_sql = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private String sMemberId;
    private Date current_date = Calendar.getInstance().getTime();

    public MR_GetMedfriedDataService() {

        super("MR_GetMedfriedDataService");
        // Log.d(TAG, "NotificationService: ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        db_mr = new SqliteMRHandler(this);

        getIntenet(intent);


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





    private void getIntenet(Intent intent) {


        sMemberId = intent.getStringExtra("member_id");

    }

    private  void success_member_Data(JSONObject response)
    {

        String schedule_start_date="";
        try {
            JSONArray Med_rem_array = response.getJSONArray("v_cfe_app_med_remider_m");

            for (int i = 0; i < Med_rem_array.length(); i++) {

                JSONObject jsonobject = Med_rem_array.getJSONObject(i);

                db_mr.delete_medreminder_table_on_reminder_id(jsonobject.optString("reminder_id"),jsonobject.optString("MemberId"));

                try {
                    current_date = dateFormat_sql.parse(  jsonobject.optString("schedule_start_date"));
                    schedule_start_date=dateFormat_query_popup.format(current_date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String refill_flag="0";

                if( jsonobject.optBoolean("RefillRem")==true)
                {
                    refill_flag="1";
                }


                db_mr. addMedRemMaster_insert_from_server(jsonobject.optString("reminder_id"),
                        jsonobject.optString("medicine_id"),
                        jsonobject.optString("medicine_name"),
                        jsonobject.optInt("reminder_type_id"),
                        jsonobject.optString("reminder_value"),
                        jsonobject.optInt("schedule_duration_type_id"),
                        jsonobject.optInt("schedule_duration_value"),
                        schedule_start_date,
                        jsonobject.optInt("days_intervel_type_id"),
                        jsonobject.optString("days_intervel_value"),
                        jsonobject.optInt("use_placebo"),
                        jsonobject.optInt("image_id"),
                        jsonobject.optInt("first_color_id"),
                        jsonobject.optInt("second_color_id"),
                        jsonobject.optString("instructions"),
                        jsonobject.optString("dosage_type"),
                        jsonobject.optInt("dosage_value"),
                        jsonobject.optString("condition"),
                        jsonobject.optString("doctor_id"),
                        jsonobject.optString("medfriend_id"),
                        jsonobject.optString("MemberId")
                        ,jsonobject.optInt("RelationshipId"),
                        refill_flag,
                        jsonobject.optString("RefillDate"),
                        jsonobject.optString("PackSize")
                        );
            }

        }catch (JSONException e)
        {

        }
        try {
            JSONArray Med_rem_med_master = response.getJSONArray("v_cfe_app_med_reminder_medicine_master");

            for (int i = 0; i < Med_rem_med_master.length(); i++) {

                JSONObject jsonobject = Med_rem_med_master.getJSONObject(i);

                db_mr.Delete_Medicine_master(jsonobject.optString("medicine_id"),jsonobject.optString("MemberId"));

                long id= db_mr.insert_Medicine_master_value(jsonobject.optString("medicine_id"),
                        jsonobject.optString("medicine_name"),
                        jsonobject.optInt("image_id"),
                        jsonobject.optInt("first_color_id"),
                        jsonobject.optInt("second_color_id"),
                        jsonobject.optString("MemberId"),
                        jsonobject.optInt("RelationshipId"),
                        jsonobject.optString("Med_actual_id"));
            }

        }catch (JSONException e)
        {

        }

        try {
            JSONArray Med_rem_med_schedule = response.getJSONArray("v_cfe_app_med_reminder_schedule");

            for (int i = 0; i < Med_rem_med_schedule.length(); i++) {

                JSONObject jsonobject = Med_rem_med_schedule.getJSONObject(i);

                db_mr.delete_med_reminder_schedule_on_schedule_id(jsonobject.optString("schedule_id"),  jsonobject.optString("MemberId"));

                String Date_time_set="",date_time_taken="";

                try {
                    current_date = dateFormat_sql.parse(  jsonobject.optString("datetime_set"));
                    Date_time_set=dateFormat_query_popup.format(current_date);

                    current_date = dateFormat_sql.parse( jsonobject.optString("datetime_taken"));
                    date_time_taken=dateFormat_query_popup.format(current_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                db_mr.insert_med_reminder_schedule(  jsonobject.optString("schedule_id"),
                        jsonobject.optString("reminder_id"),
                        jsonobject.optString("STATUS"),
                        Date_time_set,
                        date_time_taken,
                        jsonobject.optInt("sequence"),
                        jsonobject.optString("dosage_type"),
                        jsonobject.optDouble("quantity"),
                        jsonobject.optDouble("quantity"),
                        jsonobject.optString("MemberId"),
                        jsonobject.optInt("RelationshipId"));
            }


        }catch (JSONException e)
        {
            e.toString();
        }
        Intent i = new Intent("GCMNotificationMedfriendIntentService");
        sendBroadcast(i);
    }


}

package viroopa.com.medikart.common;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import viroopa.com.medikart.MedicineReminder.MRA_Add_pilly_Budy_On_Code;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteMRHandler;
import viroopa.com.medikart.services.SyncDataService;

public class ConstData {
    private static SqliteMRHandler db;

    private static Pattern pattern;
    private static Matcher matcher;
    private static final String ALARM_SOUND = "alarm_sound";
    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static String SP_GCM_REGISTERED_ID;
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static int gallery_cup_images[] = {
            R.drawable.cup1,
            R.drawable.cup6,
            R.drawable.cup8,
            R.drawable.cup9,
            R.drawable.cup10,
            R.drawable.cup4
    };


    public static int colr_array[] = {
            Color.WHITE,
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.parseColor("#df6301"),
            Color.RED,
            Color.YELLOW,
            Color.GRAY,
            Color.parseColor("#304f9f"),
            Color.parseColor("#08446a"),
            Color.parseColor("#26ae90"),
            Color.parseColor("#006400"),
            Color.parseColor("#FF1493"),
            Color.parseColor("#00FF00"),
            Color.MAGENTA
    };

    public static int Medicine_array1[] = {
            R.drawable.med1_1,
            R.drawable.med2_1,
            R.drawable.med3_1,
            R.drawable.med4_1,
            R.drawable.med5_1,
            R.drawable.med6_1,
            R.drawable.med7_1,
            R.drawable.med8_1,
            R.drawable.med9_1,
            R.drawable.med10_1,
            R.drawable.med11_1,
            R.drawable.med12_1,
            R.drawable.med13_1,
            R.drawable.med14_1,
            R.drawable.med15_1,
            R.drawable.med16_1,
            R.drawable.med17_1,
            R.drawable.med18_1,
            R.drawable.med19_1,
            R.drawable.med20_1,
            R.drawable.med21_1


    };

    public static Uri getCurrentAlarmSound(Context ctx) {
        Uri sound = null;
        // Look up sound in preferences.
        SharedPreferences pref = ctx.getSharedPreferences("Global", ctx.MODE_PRIVATE);
        String uriString = pref.getString(ALARM_SOUND, null);
        if (uriString != null) {
            sound = Uri.parse(uriString);
        }
        // Look up default alarm sound.
        if (sound == null) {
            sound = RingtoneManager.getActualDefaultRingtoneUri(
                    ctx,
                    RingtoneManager.TYPE_ALARM);
        }

        return sound;
    }

    public static void create_json_from_table(String table_name, String Mode, String Medicine_id, String Reminder_id, String sMemberId, String SelDate, Context ctx) {
        db = new SqliteMRHandler(ctx);
        Map<String, String> params = null;
        Cursor cursor_all_medicine = null;
        Cursor cursor_reminder_master = null;
        Cursor cursor_schedule = null;
        JSONObject jparams = null;
        if (table_name.equals("medicine_master")) {

            if (Mode.equals("A")) {
                cursor_all_medicine = db.get_last_entry_reminder_medicine(sMemberId, Medicine_id);
            } else if (Mode.equals("E")) {
                cursor_all_medicine = db.get__entry_on_medicine_id_reminder_medicine(Medicine_id, sMemberId);
            }


            if ((cursor_all_medicine != null) || (cursor_all_medicine.getCount() > 0)) {

                if (cursor_all_medicine.moveToFirst()) {
                    do {
                        params = new HashMap<String, String>();
                        for (int i = 0; i < cursor_all_medicine.getColumnCount(); i++) {
                            params.put(cursor_all_medicine.getColumnName(i), cursor_all_medicine.getString(i));

                        }
                        jparams = new JSONObject(params);

                    } while (cursor_all_medicine.moveToNext());


                }
            }

            SyncLogTable(jparams.toString(), "", SelDate, "RMM", Mode, "", sMemberId, ctx);
        } else if (table_name.equals("medicine_reminder")) {
            cursor_reminder_master = db.get_med_master_data(Reminder_id, sMemberId);
            if ((cursor_reminder_master != null) || (cursor_reminder_master.getCount() > 0)) {
                if (cursor_reminder_master.moveToFirst()) {
                    do {
                        params = new HashMap<String, String>();
                        for (int i = 0; i < cursor_reminder_master.getColumnCount(); i++) {
                            params.put(cursor_reminder_master.getColumnName(i), cursor_reminder_master.getString(i));
                        }

                    } while (cursor_reminder_master.moveToNext());
                }
                jparams = new JSONObject(params);
                SyncLogTable(jparams.toString(), "", SelDate, "RM", Mode, "", sMemberId, ctx);
            }
        } else if (table_name.equals("reminder_schedule")) {


            if (Mode.equals("A")) {
                cursor_schedule = db.get_schedule_on_reminder_id(Reminder_id, sMemberId);
            } else if (Mode.equals("E")) {
                cursor_schedule = db.get_schedule_on_schedule_id(Reminder_id, sMemberId);
            }


            if ((cursor_schedule != null) || (cursor_schedule.getCount() > 0)) {

                JSONArray resultSet = new JSONArray();

                if (cursor_schedule.moveToFirst()) {
                    do {
                        JSONObject f_jobject = new JSONObject();
                        try {
                            for (int i = 0; i < cursor_schedule.getColumnCount(); i++) {
                                f_jobject.put(cursor_schedule.getColumnName(i), cursor_schedule.getString(i));
                            }
                            resultSet.put(f_jobject);
                        } catch (JSONException e) {
                        }

                    } while (cursor_schedule.moveToNext());

                    if (Mode.equals("E")) {
                        send_schedule_data(sMemberId, resultSet, ctx);
                    }


                    SyncLogTable(String.valueOf(resultSet), "", SelDate, "RS", Mode, "", sMemberId, ctx);
                }
            }
        }


    }


    public static void SyncLogTable(String JsonObject, String api_path, String c_Date, String F_KEY_MODULE_NAME, String F_KEY_MODE,
                                    String F_KEY_MethodName, String sMemberId, Context ctx) {

        String SUUID = UUID.randomUUID().toString();
        String I_Type = "Post";
        String Parameter = "";
        String F_KEY_UPLOAD_DOWNLOAD = "true";
        String F_KEY_ControllerName = "Monitor";

        Long IMEINO = 00000000000l;

        db.InsertSyncTable(I_Type,
                api_path,
                Parameter,
                JsonObject.trim(),
                c_Date,
                SUUID,
                F_KEY_UPLOAD_DOWNLOAD,
                Integer.parseInt(sMemberId),
                F_KEY_MODULE_NAME,
                F_KEY_MODE,
                F_KEY_ControllerName,
                F_KEY_MethodName,
                IMEINO);
        StartServerSync(ctx);
    }

    private static void StartServerSync(Context ctx) {
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, ctx, SyncDataService.class);
        ctx.startService(download_intent);

    }

    private static void send_schedule_data(String Mem_id, JSONArray json, Context ctx) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        Map<Object, Object> params = new HashMap<Object, Object>();

        params.put("iMemberId", Mem_id);
        params.put("medicine_data", json);//.toString().replaceAll("\\\\", "")

        JSONObject jparams = new JSONObject(params);


        JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_GET_SEND_DATA_SCHEDULE,
                jparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        response.toString();

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.toString();
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


        queue.add(jor_inhurry_post);
    }

    public static void get_medfriend_data(Context ctx, final String sMemberId) {
        db = new SqliteMRHandler(ctx);

        String url = String.format(AppConfig.URL_GET_MED_FRIEND_LIST, sMemberId);


        RequestQueue queue = Volley.newRequestQueue(ctx);

        JsonObjectRequest staterequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String main_phone_number = "", main_email_id = "", Local_phone_number = "", Local_email_id = "";
                            JSONArray medfriend_array = response.getJSONArray("v_med_friend_list");

                            if (medfriend_array.length() > 0) {
                                db.delete_med_friend_all_data();
                            }

                            for (int i = 0; i < medfriend_array.length(); i++) {

                                JSONObject jsonobject = medfriend_array.getJSONObject(i);

                                main_phone_number = jsonobject.getString("Friend_Phone_No");
                                main_email_id = jsonobject.getString("Friend_Email");

                                Cursor cursor_all_pillbuddy = db.get_all_pill_buddyr(sMemberId);


                                if ((cursor_all_pillbuddy != null) || (cursor_all_pillbuddy.getCount() > 0)) {
                                    if (cursor_all_pillbuddy.moveToFirst()) {
                                        do {
                                            Local_phone_number = cursor_all_pillbuddy.getString(cursor_all_pillbuddy.getColumnIndex("reminder_phone_no"));
                                            Local_email_id = cursor_all_pillbuddy.getString(cursor_all_pillbuddy.getColumnIndex("reminder_email_id"));

                                            if (main_phone_number.equals(Local_phone_number) || main_email_id.equals(Local_email_id)) {
                                                db.delete_med_friend(cursor_all_pillbuddy.getString(cursor_all_pillbuddy.getColumnIndex("id")));


                                            }


                                        } while (cursor_all_pillbuddy.moveToNext());
                                        cursor_all_pillbuddy.close();


                                    }
                                }

                                db.addMedFriend(jsonobject.getString("Friend_Memberid"),
                                        jsonobject.getString("Friend_Name"),
                                        jsonobject.getString("Friend_Phone_No"),
                                        jsonobject.getString("Friend_Email"),
                                        jsonobject.getString("Friend_Image"),
                                        "",
                                        true);
                            }


                        } catch (JSONException e) {
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

    public static String getValueOrDefault(String value, String defaultValue) {
        return isNotNullOrEmpty(value) ? value : defaultValue;
    }

    private static boolean isNotNullOrEmpty(String str) {
        return (str != null && !str.isEmpty());
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static void WIPE_DATA_FROM_SERVER(String Member_id, String Module_id, Context ctx) {


        String url = String.format(AppConfig.URL_WIPE_DATA_FROM_SERVER, Member_id, Module_id);


        RequestQueue queue = Volley.newRequestQueue(ctx);

        JsonObjectRequest staterequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                        } catch (Exception e) {

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

}

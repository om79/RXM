package viroopa.com.medikart.MedicineReminder.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.helper.SqliteMRHandler;

public class InvitationReciecver extends BroadcastReceiver {
    public static final int notifyID = 9001;
    private SqliteMRHandler db_mr;
    String ns;
    NotificationManager nMgr;
    private String sMemberId;
    SimpleDateFormat dateFormat_query_popup = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date current_date = Calendar.getInstance().getTime();
    @Override
    public void onReceive(Context context, Intent intent) {
        db_mr = new SqliteMRHandler(context.getApplicationContext());
        ns = context.NOTIFICATION_SERVICE;
        nMgr = (NotificationManager) context.getSystemService(ns);

        getIntenet(context);

        String action = (String) intent.getExtras().get("do_action");
        String req_sender_memberid = (String) intent.getExtras().get("req_sender_memberid");
        if (action != null) {
            if(action.equals("Accept"))
            {

                Accept_Reject_invite(req_sender_memberid,action,context);
                Toast.makeText(context, "accepted", Toast.LENGTH_LONG)
                        .show();


                nMgr.cancel(notifyID);
            }else if(action.equals("Reject"))
            {
                Accept_Reject_invite(req_sender_memberid,action,context);
                nMgr.cancel(notifyID);
            }
        }
    }

    public Date date_addMinutes(Date date, int Minute)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, Minute);
        return c.getTime();
    }

    private  void close_all_activity(Context ctx)
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    private void getIntenet(Context ctx) {

        SharedPreferences pref = ctx.getSharedPreferences("Global", ctx.MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

    }

    private void Accept_Reject_invite(String req_sender_memberid,String flag,Context context) {
        String url="";
        if(flag.equals("Accept"))
        {
            url  = String.format(AppConfig.URL_GET_ACCEPT_INVITE, sMemberId, req_sender_memberid);
        } else if(flag.equals("Reject"))
        {
            url  = String.format(AppConfig.URL_GET_REJECT_INVITE, sMemberId, req_sender_memberid);
        }




        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest staterequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {


                        }
                        catch (Exception e) {
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

}

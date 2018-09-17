package viroopa.com.medikart.MedicineReminder.Reciever;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.widget.Toast;


import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import viroopa.com.medikart.MedicineReminder.MRA_ReminderMain;
import viroopa.com.medikart.MedicineReminder.MRA_reminder_notification;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SqliteMRHandler;

import static android.content.Context.MODE_PRIVATE;

public class NotifyActivityHandler extends BroadcastReceiver {
    public static final String PERFORM_NOTIFICATION_BUTTON = "perform_notification_button";
    private SqliteMRHandler db_mr;
    String ns;
    NotificationManager nMgr;
    private String sMemberId;
    private static String SNOOZE_COUNT="snooze_count";
    private int snooze_intervel;
    public static final int notifyID = 1234;
    SimpleDateFormat dateFormat_query_popup = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date current_date = Calendar.getInstance().getTime();
    @Override
    public void onReceive(Context context, Intent intent) {
        db_mr = new SqliteMRHandler(context.getApplicationContext());
        getIntenet(context);
        String action = (String) intent.getExtras().get("do_action");
        if (action != null) {
            if (action.equals("Take")) {
                String Mede_Schedule_id = (String) intent.getExtras().get("Schedule_id");
                db_mr.Update_Medicine_Schedule(Mede_Schedule_id, "taken", dateFormat_query_popup.format(Calendar.getInstance().getTime()));
               ConstData.create_json_from_table("reminder_schedule","E","",Mede_Schedule_id,sMemberId,dateFormat_query_popup.format(Calendar.getInstance().getTime()),context);
                db_mr.delete_from_notification_table(Mede_Schedule_id);
                ns = context.NOTIFICATION_SERVICE;
                nMgr = (NotificationManager) context.getSystemService(ns);
                nMgr.cancel(notifyID);

                MRA_reminder_notification.objsecondActivity.fill_notification_list();

               // Intent Intenet_home = new Intent(context, MRA_ReminderMain.class);
             //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // context.startActivity(Intenet_home);

            } else if (action.equals("Cancel")) {
                ns = context.NOTIFICATION_SERVICE;
                nMgr = (NotificationManager) context.getSystemService(ns);
                nMgr.cancel(notifyID);
                MRA_reminder_notification.objsecondActivity.closeActivity();
               // close_all_activity(context);
            } else if (action.equals("Snooze")) {

                String Mede_Schedule_id = (String) intent.getExtras().get("Schedule_id");
                String Mede_Schedule_date = (String) intent.getExtras().get("Schedule_date");
                String Mede_Schedule_Name = (String) intent.getExtras().get("Medicine_name");
                Integer Mede_Schedule_Snooze_count = (Integer) intent.getExtras().get("Snooze_count");
                MRA_reminder_notification.objsecondActivity.fill_notification_list();


                try {
                    current_date = dateFormat_query_popup.parse(Mede_Schedule_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(Mede_Schedule_Snooze_count>=3)
                {
                    //send sms
                    String msg="Hello Your friend does not take medicine "+Mede_Schedule_Name+" on time "+Mede_Schedule_date+" .It may be effect his health";
                    send_sms(msg);
                }

                 db_mr.Snooze_Medicine_Schedule(Mede_Schedule_id,dateFormat_query_popup.format(date_addMinutes(current_date, snooze_intervel)),Mede_Schedule_Snooze_count+1);
                ConstData.create_json_from_table("reminder_schedule","E","",Mede_Schedule_id,sMemberId,dateFormat_query_popup.format(date_addMinutes(current_date, 5)),context);
                Toast.makeText(context, "Snoozed for "+snooze_intervel+" mins", Toast.LENGTH_SHORT).show();
                ns = context.NOTIFICATION_SERVICE;
                nMgr = (NotificationManager) context.getSystemService(ns);
                nMgr.cancel(notifyID);

               MRA_reminder_notification.objsecondActivity.closeActivity();
                //close_all_activity(context);
                // close current notification
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
    private void send_sms(String Msg) {
        SmsManager sms = SmsManager.getDefault();

        Cursor cursor_all_medicine = db_mr.get_all_pill_buddyr(sMemberId);


        if ((cursor_all_medicine != null) || (cursor_all_medicine.getCount() > 0)) {

            if (cursor_all_medicine.moveToFirst()) {
                do {
                  String phone_number=cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("reminder_phone_no"));
                    if(phone_number!=null)
                    {
                        if(StringUtils.isNumeric(phone_number))
                        {
                            sms.sendTextMessage(phone_number, null, Msg, null, null);
                        }
                    }

                } while (cursor_all_medicine.moveToNext());
            }


        }
    }
    private void getIntenet(Context ctx) {

        SharedPreferences pref = ctx.getSharedPreferences("Global", ctx.MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        snooze_intervel=Integer.parseInt(pref.getString(SNOOZE_COUNT,"5 minutes").replaceAll("\\D+",""));

    }

}

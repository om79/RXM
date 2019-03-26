package viroopa.com.medikart.MedicineReminder.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import viroopa.com.medikart.MedicineReminder.MRA_reminder_notification;
import viroopa.com.medikart.MedicineReminder.Reciever.NotifyActivityHandler;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.BuySearchActivity;
import viroopa.com.medikart.helper.SqliteMRHandler;


/**
 * Created by ABCD on 04/03/2016.
 */
public class MR_NotificationService extends IntentService {

    public static final String TAG = "RxReminder";
    AppController globalVariable;
    private String notification_Date;
    public Date current_date = Calendar.getInstance().getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat dateFormat_refill = new SimpleDateFormat("yyyy-MM-dd");
    private SqliteMRHandler db_mr;
    public static final int notifyID = 1234;

    public static final int notifyID_refill = 1238;
    private Notification  notification;
    private String sMemberId;
    private static String MRA_MUTE_NOTIFICATION="mra_mute_notification";
    private static String MRA_DEFAULT_NOTIFICATION_RINGTONE="mra_notification_ringtone";

    public MR_NotificationService() {

        super("NotificationService");
        // Log.d(TAG, "NotificationService: ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {



        // Log.d(TAG, "onHandleIntent: ");
        getIntenet();

        int notificationId = intent.getIntExtra("notificationId", 0);
        if(notificationId!=0) {
            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }else {

            try {
                //myfireNotification();
                current_date = Calendar.getInstance().getTime();
                notification_Date = dateFormat.format(current_date);
                //notification_Date="2016-03-07 16:43:00";
                db_mr = new SqliteMRHandler(this);
                globalVariable = (AppController) getApplicationContext();
                String reminder_id = "";
                String Schedule_id = "";
                String date_time = "";

                Cursor cursor_refill=db_mr.get_refill_reminder(dateFormat_refill.format(current_date),sMemberId);


                if ((cursor_refill != null) || (cursor_refill.getCount() > 0)) {
                    if (cursor_refill.moveToFirst()) {
                        refill_notification(cursor_refill.getString(cursor_refill.getColumnIndex("medicine_name")));
                        db_mr.addMedRemMaster_update_refill_notified(cursor_refill.getString(cursor_refill.getColumnIndex("reminder_id")),1);
                    }
                }



                Cursor cursor_notification = db_mr.get_top_scheduled_time_on_current_time(notification_Date + ":00",sMemberId);
                int c = cursor_notification.getCount();

                if ((cursor_notification != null) || (cursor_notification.getCount() > 0)) {
                    if (cursor_notification.moveToFirst()) {
                        do {
                            reminder_id = cursor_notification.getString(cursor_notification.getColumnIndex("reminder_id"));
                            Schedule_id = cursor_notification.getString(cursor_notification.getColumnIndex("schedule_id"));
                            date_time = cursor_notification.getString(cursor_notification.getColumnIndex("datetime_set"));





                            if(db_mr.check_schedule_exist(Schedule_id)>0)
                            {


                            }else
                            {

                                Cursor check_rem_status=db_mr.check_reminder_status_on_reminder_id(reminder_id);

                                if ((check_rem_status != null) || (check_rem_status.getCount() > 0)) {
                                    if (check_rem_status.moveToFirst()) {
                                        if(check_rem_status.getInt(check_rem_status.getColumnIndex("no_reminder"))==0)
                                        {
                                            db_mr.Insert_into_notification(reminder_id, Schedule_id, date_time, "", "5");
                                            Showmr();
                                            myfireNotification(Schedule_id,  db_mr.get_ringtone_reminder(reminder_id));
                                        }

                                    }
                                }
                            }


                        } while (cursor_notification.moveToNext());
                    }
                }
            } catch (Exception e) {
                this.stopSelf();
                e.toString();
            }

        }

    }




    private void myfireNotification(String schedule_id,String selected_sound) {






        Uri alarmSound;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);

        if(pref.getBoolean(MRA_MUTE_NOTIFICATION,false))
        {
            alarmSound =null;
        }else
        {
            if(selected_sound!=null)
            {
                alarmSound=Uri.parse(selected_sound);
            }else {
                alarmSound = Uri.parse(pref.getString(MRA_DEFAULT_NOTIFICATION_RINGTONE,
                        (RingtoneManager.getActualDefaultRingtoneUri(this.getApplicationContext(), RingtoneManager.TYPE_RINGTONE).toString())));

            }
        }



        Intent myIntent = new Intent(this, MRA_reminder_notification.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                myIntent,
                PendingIntent.FLAG_ONE_SHOT);






        Cursor cursor_notification =   db_mr.get_notification_data_for_schedule_id(schedule_id,sMemberId);
        int c = cursor_notification.getCount();

        if ((cursor_notification != null) || (cursor_notification.getCount() > 0)) {
            if (cursor_notification.moveToFirst()) {
                //   do {

                Intent buttonsIntent = new Intent(this, NotifyActivityHandler.class);
                buttonsIntent.putExtra("do_action", "Take");
                buttonsIntent.putExtra("Schedule_id",cursor_notification.getString(cursor_notification.getColumnIndex("schedule_id")));

                PendingIntent pendingTakeIntent = PendingIntent.getBroadcast(
                        this,
                        0,
                        buttonsIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);


                Intent buttonsCancelIntent = new Intent(this, NotifyActivityHandler.class);
                buttonsCancelIntent.putExtra("do_action", "Cancel");

                PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(
                        this,
                        1,
                        buttonsCancelIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                Intent buttonsSnoozeIntent = new Intent(this, NotifyActivityHandler.class);
                buttonsSnoozeIntent.putExtra("do_action", "Snooze");
                buttonsSnoozeIntent.putExtra("Schedule_id",cursor_notification.getString(cursor_notification.getColumnIndex("schedule_id")));
                buttonsSnoozeIntent.putExtra("Schedule_date",cursor_notification.getString(cursor_notification.getColumnIndex("datetime_set")));
                buttonsSnoozeIntent.putExtra("Snooze_count",cursor_notification.getInt(cursor_notification.getColumnIndex("snooze_count")));
                buttonsSnoozeIntent.putExtra("Medicine_name",cursor_notification.getString(cursor_notification.getColumnIndex("medicine_name")));
                PendingIntent pendingSnoozeIntent = PendingIntent.getBroadcast(
                        this,
                        2,
                        buttonsSnoozeIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);


                notification = new NotificationCompat.Builder(this)
                        .setSound(alarmSound)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(pendingIntent)
                        . setContentTitle(cursor_notification.getString(cursor_notification.getColumnIndex("medicine_name")))
                        .setAutoCancel(true)
                        .setSubText("Take  "+cursor_notification.getString(cursor_notification.getColumnIndex("dosage")))
                        .addAction(R.drawable.tick,"Take", pendingTakeIntent)
                        .addAction(R.drawable.close,"Cancel", pendingCancelIntent)
                        .addAction(R.drawable.close,"Snooze", pendingSnoozeIntent)
                        .setContentText("its time to take your medicines")
                        .setSmallIcon(R.drawable.rxlogo).build();

                notification.priority=Notification.PRIORITY_MAX;

            }
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, notification);
        this.stopSelf();
    }

    private void Showmr(){
        Intent Intenet_mr = new Intent(this, MRA_reminder_notification.class);
        Intenet_mr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intenet_mr.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Intenet_mr);
        this.stopSelf();
    }

    private void getIntenet() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

    }

    private void refill_notification(String Medname)
    {


        Intent p_Intent = new Intent(this, BuySearchActivity.class);
        p_Intent.putExtra("Search_word", Medname);
        PendingIntent pending_Intent = PendingIntent.getActivity(
                this,
                0,
                p_Intent,
                PendingIntent.FLAG_ONE_SHOT);



        Uri alarmSound = Uri.parse
                (RingtoneManager.getActualDefaultRingtoneUri(this.getApplicationContext(), RingtoneManager.TYPE_RINGTONE).toString());


        notification = new NotificationCompat.Builder(this)
                .setSound(alarmSound)
                .setContentIntent(pending_Intent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                . setContentTitle("Refill Reminder")
                .setAutoCancel(true)
                .setSubText(Medname)
                .setContentText("It's time to refill your medicine")
                .setSmallIcon(R.drawable.rxlogo).build();

        notification.priority=Notification.PRIORITY_MAX;


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID_refill, notification);
    }


}

package viroopa.com.medikart.wmMonitor.WMCommon;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import viroopa.com.medikart.R;
import viroopa.com.medikart.Reciever.CancelButtonReceiver;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SqliteMRHandler;
import viroopa.com.medikart.helper.SqliteWMHandler;
import viroopa.com.medikart.wmMonitor.WMA_watermain;


/**
 * Created by ABCD on 04/03/2016.
 */
public class WM_NotificationService extends IntentService {

    public static final String TAG = "RxReminder";
    private static final Integer NOTIFICATION_ID=4001;
    AppController globalVariable;
    private SqliteWMHandler db_water_monitor;
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    public Date current_date = Calendar.getInstance().getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SqliteMRHandler db_mr;
    private Double Drinkpercent;
    private Integer relationShipId=8;
    private Integer glassSize;
    private String date;
    private    Bitmap largeIcon=null;
    private static String WMA_MUTE_NOTIFICATION="wma_mute_notification";

    public WM_NotificationService() {

        super("NotificationService");
        // Log.d(TAG, "NotificationService: ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Log.d(TAG, "onHandleIntent: ");
        db_water_monitor =  SqliteWMHandler.getInstance(this);
        globalVariable = (AppController) getApplicationContext();
        try {
            water_monitor_reminder();
            this.stopSelf();
        }catch (Exception e)
        {
            e.toString();
            this.stopSelf();
        }



    }
  private  void water_monitor_reminder()
  {
      date= dateFormat_query.format(current_date);

      Integer PerGoal=0;
      String MaxId=db_water_monitor.getMAxIdWMSetting(relationShipId);
      if(MaxId!=null) {
          PerGoal = db_water_monitor.getLastSetGoal(relationShipId,date);
          glassSize=db_water_monitor.getLastSetGlassSize(relationShipId,date);
          String Pertotal= db_water_monitor.getTotalDrink_quantity(date,relationShipId);

          if(Pertotal==null)
          {
              Drinkpercent=0.0;

          }else {
              Drinkpercent = (( Double.parseDouble(Pertotal)/ PerGoal)) * 100;
          }



      }
      if(Drinkpercent<50.0) {

          largeIcon=null;
         largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.rxlogo);

          SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
          Uri alarmSound = null;
          if(!pref.getBoolean(WMA_MUTE_NOTIFICATION,false))
          {
              alarmSound =RingtoneManager.getActualDefaultRingtoneUri(this.getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
          }


          Intent myIntent = new Intent(this, WMA_watermain.class);
          myIntent.putExtra("notificationId",NOTIFICATION_ID);
          PendingIntent pendingIntent = PendingIntent.getActivity(
                  this,
                  0,
                  myIntent,
                  PendingIntent.FLAG_UPDATE_CURRENT);



          Intent buttonIntent = new Intent(this, CancelButtonReceiver.class);
          buttonIntent.putExtra("notificationId",NOTIFICATION_ID);
          PendingIntent btPendingIntent = PendingIntent.getBroadcast(this, 0, buttonIntent, PendingIntent.FLAG_CANCEL_CURRENT);



          Notification notification = new NotificationCompat.Builder(this)
                  .setSound(alarmSound)

                  .setDefaults(Notification.DEFAULT_VIBRATE)
                  .setContentIntent(pendingIntent)
                  .setContentTitle("Water Reminder")
                  .setAutoCancel(true)
                  .setLargeIcon(largeIcon)
                  .setVibrate(new long[] {500,1000})
                  .addAction(R.drawable.tick, "Drink", pendingIntent)
                  .addAction(R.drawable.close, "Cancel",btPendingIntent )
                  .setContentText("Its time to drink water")
                  .setSmallIcon(R.drawable.rxlogo).build();

          notification.priority = notification.PRIORITY_MAX;
          NotificationManager notificationManager =
                  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
          notificationManager.notify(NOTIFICATION_ID, notification);
      }
  }

}

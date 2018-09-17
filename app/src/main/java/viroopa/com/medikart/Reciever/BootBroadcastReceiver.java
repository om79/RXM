package viroopa.com.medikart.Reciever;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.widget.Toast;

import viroopa.com.medikart.MedicineReminder.services.MR_NotificationService;
import viroopa.com.medikart.wmMonitor.WMCommon.WM_NotificationService;


/**
 * Created by ABCD on 09/05/2016.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    private  static  int ONE_HOUR=1000*60*60;
    private  static int HALF_HOUR=1000*60*30;
    private  static int TWO_HOUR=1000*60*60*2;

    private  int selected_notification_intervel;


    @Override
    public void onReceive(Context pContext, Intent intent) {
        // Do your work related to alarm manager
        try {
            get_notification_intervel(pContext);
            scheduleAlarms(pContext);
        }catch (Exception e)
        {
            Toast.makeText(pContext,  e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
     void scheduleAlarms(Context ctxt) {

         AlarmManager am = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
         Intent intent = new Intent(ctxt, WM_NotificationService.class);

         Intent intentMR = new Intent(ctxt, MR_NotificationService.class);

         PendingIntent pendingIntentWater = PendingIntent.getService(ctxt, 101, intent, 0);
         PendingIntent pendingIntentMR = PendingIntent.getService(ctxt, 102, intentMR, 0);

         am.cancel(pendingIntentWater);


         am.setRepeating(AlarmManager.RTC_WAKEUP,
                 System.currentTimeMillis() +selected_notification_intervel ,selected_notification_intervel,pendingIntentWater );

         am.setRepeating(AlarmManager.RTC_WAKEUP,
                 System.currentTimeMillis() ,1000*60,pendingIntentMR );
    }

    void get_notification_intervel(Context ctxt)
    {
        SharedPreferences pref = ctxt.getSharedPreferences("Global", Context.MODE_PRIVATE);
        selected_notification_intervel = pref.getInt("selected_notification_intervel", ONE_HOUR);

    }
}
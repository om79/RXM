package viroopa.com.medikart.Reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by ABCD on 14/06/2016.
 */
public class VersionCheckReciever extends BroadcastReceiver {
    public static final String RESPONSE_MESSAGE = "myResponseMessage";
    public static final String PROCESS_RESPONSE = "com.as400samplecode.intent.action.PROCESS_RESPONSE";

    @Override
    public void onReceive(final Context context, Intent intent) {

        String latestVersion = intent.getStringExtra(VersionCheckReciever.RESPONSE_MESSAGE);
        String versionCode=getVersionCode(context);


        //parse the JSON response
        try {
            if(latestVersion.equals(versionCode.toString())){

                Intent i = new Intent("VersionCheckReciever");
                i.putExtra("message", "There is newer version of this application available,Please update the application to work properly, click OK to upgrade now.");

                context.sendBroadcast(i);
            }else
            {
                Intent i = new Intent("VersionCheckReciever");
                i.putExtra("message", "updated");
                context.sendBroadcast(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  String getVersionCode(Context context) {
        String verName="";
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            verName = pi.versionName;
        } catch (PackageManager.NameNotFoundException ex) {}
        return verName;
    }

}
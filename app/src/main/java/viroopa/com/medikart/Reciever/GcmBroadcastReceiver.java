package viroopa.com.medikart.Reciever;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;


import viroopa.com.medikart.MedicineReminder.services.GCMNotificationMedfriendIntentService;
import viroopa.com.medikart.common.WakeLocker;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		WakeLocker.acquire(context);
		ComponentName comp = new ComponentName(context.getPackageName(),
				GCMNotificationMedfriendIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
		WakeLocker.release();
	}
}

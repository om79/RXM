package viroopa.com.medikart.MedicineReminder.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import viroopa.com.medikart.MedicineReminder.MRA_ReminderMain;
import viroopa.com.medikart.R;
import viroopa.com.medikart.Reciever.GcmBroadcastReceiver;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SqliteMRHandler;


public class GCMNotificationMedfriendIntentService extends IntentService {
	// Sets an ID for the notification, so it can be updated
	public static final int notifyID = 9001;

	public static final int notifyID_RX = 90012;
	NotificationCompat.Builder builder;
	private String Request_type;
	private SqliteMRHandler db;

	public GCMNotificationMedfriendIntentService() {
		super("GcmIntentService");
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		Intent i = new Intent("GCMNotificationMedfriendIntentService");
		sendBroadcast(i);

		Request_type=extras.getString("req_type");

		if(Request_type.equals("P"))
		{
				          update_schedule_table(    extras.getString("schedule_id"),
													extras.getString("datetime_set"),
													extras.getString("datetime_taken"),
													extras.getString("status"));

		} else if(Request_type.equals("I"))
		{
			String req_sender_memberid = extras.getString("req_sender_memberid");

			if (!extras.isEmpty() && GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				String messge=extras.getString("message");

				Intent invite_intent = new Intent("Invitenotification");
				invite_intent.putExtra("msg",messge);
				invite_intent.putExtra("req_sender_memberid",req_sender_memberid);
				sendBroadcast(invite_intent);

				Invite_Notification(messge,req_sender_memberid);
			}
		}else if(Request_type.equals("A"))
		{
			SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
			ConstData.get_medfriend_data(getApplicationContext(),pref.getString("memberid", ""));
			String req_sender_memberid = extras.getString("req_sender_memberid");
			String messge=extras.getString("message");



			if (!extras.isEmpty() && GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				Accept_Notification(messge,req_sender_memberid);
				Intent invite_intent = new Intent("AcceptNotification");
				invite_intent.putExtra("msg",messge);
				sendBroadcast(invite_intent);

			}
		}else if (Request_type.equals("PA"))
		{
			try
			{
				String msg= extras.getString("message");
				show_rx_messages(msg);
			}
			catch (Exception e)
			{
				e.toString();
			}



		}


		GcmBroadcastReceiver.completeWakefulIntent(intent);
		this.stopSelf();
	}

	private void Invite_Notification(String msg,String req_sender_memberid) {


		Intent buttonsIntent = new Intent(this, InvitationReciecver.class);

		buttonsIntent.putExtra("req_sender_memberid",req_sender_memberid);
		buttonsIntent.putExtra("do_action", "Accept");

		PendingIntent pendingAcceptIntent = PendingIntent.getBroadcast(
				this,
				0,
				buttonsIntent,
				PendingIntent.FLAG_ONE_SHOT);

		Intent cancelIntent = new Intent(this, InvitationReciecver.class);
		cancelIntent.putExtra("do_action", "Cancel");
		cancelIntent.putExtra("req_sender_memberid",req_sender_memberid);
		PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(
				this,
				0,
				cancelIntent,
				PendingIntent.FLAG_ONE_SHOT);



	        Intent resultIntent = new Intent(this, MRA_ReminderMain.class);
	        resultIntent.putExtra("msg", msg);
		    resultIntent.putExtra("req_sender_memberid", req_sender_memberid);
		    resultIntent.putExtra("Invite", "Invite");
	        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
	                resultIntent, PendingIntent.FLAG_ONE_SHOT);
	        
	        NotificationCompat.Builder mNotifyBuilder;
	        NotificationManager mNotificationManager;
	        
	        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        
	        mNotifyBuilder = new NotificationCompat.Builder(this)
	                .setContentTitle("MedFriend Request")
	                .setContentText(msg)
	                .setSmallIcon(R.drawable.add_pillbuddy_white);
	        mNotifyBuilder.setContentIntent(resultPendingIntent);
	        
	        // Set Vibrate, Sound and Light	        
	        int defaults = 0;
	        defaults = defaults | Notification.DEFAULT_LIGHTS;
	        defaults = defaults | Notification.DEFAULT_VIBRATE;
	        defaults = defaults | Notification.DEFAULT_SOUND;
	        
	        mNotifyBuilder.setDefaults(defaults);
		mNotifyBuilder .addAction(R.drawable.tick,"Accept", pendingAcceptIntent);
		mNotifyBuilder.addAction(R.drawable.close,"Reject", pendingCancelIntent);
	        mNotifyBuilder.setAutoCancel(true);
		mNotifyBuilder.setColor(getResources().getColor(R.color.colorPrimary));
	        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}

	private void Accept_Notification(String msg,String req_sender_memberid) {

		Intent resultIntent = new Intent(this, MRA_ReminderMain.class);
		resultIntent.putExtra("msg", msg);
		resultIntent.putExtra("Accept", "Accept");
		resultIntent.putExtra("req_sender_memberid", req_sender_memberid);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_ONE_SHOT);

		NotificationCompat.Builder mNotifyBuilder;
		NotificationManager mNotificationManager;

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotifyBuilder = new NotificationCompat.Builder(this)
				.setContentTitle("Med friend Added")
				.setContentText(msg)
				.setSmallIcon(R.drawable.add_pillbuddy_white);
		mNotifyBuilder.setContentIntent(resultPendingIntent);

		// Set Vibrate, Sound and Light
		int defaults = 0;
		defaults = defaults | Notification.DEFAULT_LIGHTS;
		defaults = defaults | Notification.DEFAULT_VIBRATE;
		defaults = defaults | Notification.DEFAULT_SOUND;

		mNotifyBuilder.setDefaults(defaults);

		mNotifyBuilder.setAutoCancel(true);
		mNotifyBuilder.setColor(getResources().getColor(R.color.colorPrimary));
		mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}
  private  void update_schedule_table(String schedule_id,String datetime_set,String datetime_taken,String status) {


	  db =    new SqliteMRHandler(getApplicationContext());
			  db.Update_Medicine_Schedule_server(schedule_id,
					                             datetime_set,
					                             datetime_taken,
					                             status);


		  Intent i = new Intent("GCMNotificationMedfriendIntentService");
		  sendBroadcast(i);


  }

	private void show_rx_messages(String Message)
	{
		NotificationCompat.Builder mNotifyBuilder;
		NotificationManager mNotificationManager;

		Intent i = new Intent();

		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				i, PendingIntent.FLAG_ONE_SHOT);

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotifyBuilder = new NotificationCompat.Builder(this)
				.setContentTitle("RxMedikart")

				.setContentText(Message)
				.setSmallIcon(R.drawable.rxlogo);
		mNotifyBuilder.setContentIntent(resultPendingIntent);

		// Set Vibrate, Sound and Light
		int defaults = 0;
		defaults = defaults | Notification.DEFAULT_LIGHTS;
		defaults = defaults | Notification.DEFAULT_VIBRATE;
		defaults = defaults | Notification.DEFAULT_SOUND;

		mNotifyBuilder.setDefaults(defaults);

		mNotifyBuilder.setAutoCancel(true);
		mNotifyBuilder.setColor(getResources().getColor(R.color.colorPrimary));
		mNotificationManager.notify(notifyID_RX, mNotifyBuilder.build());
	}

}

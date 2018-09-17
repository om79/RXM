package viroopa.com.medikart.MedicineReminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.MedicineReminder.Model.mednotificationList;
import viroopa.com.medikart.MedicineReminder.adapter.RAD_reminderNotification;
import viroopa.com.medikart.R;
import viroopa.com.medikart.helper.SqliteMRHandler;

public class MRA_reminder_notification extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SqliteMRHandler db_mr;
    private String sMemberId;
    private RAD_reminderNotification recycleadapter;
    private List<mednotificationList> notificationlist = new ArrayList<mednotificationList>();
    public static MRA_reminder_notification objsecondActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_reminder_notification);
        db_mr = new SqliteMRHandler(this);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId  = pref.getString("memberid", "");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        objsecondActivity=this;
        recycleadapter = new RAD_reminderNotification(this,this, notificationlist);
        mRecyclerView.setAdapter(recycleadapter);
        fill_notification_list();


    }

    public  void fill_notification_list()
    {
        notificationlist.clear();


        Cursor cursor_notification = db_mr.get_all_notification_data(sMemberId);
        int c = cursor_notification.getCount();

        if ((cursor_notification != null) || (cursor_notification.getCount() > 0)) {
            if (cursor_notification.moveToFirst()) {
                do {
                    mednotificationList o_mednotificationList = new mednotificationList();

                    o_mednotificationList.setId(cursor_notification.getString(cursor_notification.getColumnIndex("schedule_id")));
                    o_mednotificationList.setMedicineName(cursor_notification.getString(cursor_notification.getColumnIndex("medicine_name")));
                    o_mednotificationList.setDoasge(cursor_notification.getString(cursor_notification.getColumnIndex("dosage_value")));
                    o_mednotificationList.setSchedule_date_time(cursor_notification.getString(cursor_notification.getColumnIndex("datetime_set")));
                    o_mednotificationList.setImage_id(cursor_notification.getInt(cursor_notification.getColumnIndex("image_id")));
                    o_mednotificationList.setF_color_id(cursor_notification.getInt(cursor_notification.getColumnIndex("first_color_id")));
                    o_mednotificationList.setS_color_id(cursor_notification.getInt(cursor_notification.getColumnIndex("second_color_id")));
                    o_mednotificationList.setSnooze_count(cursor_notification.getInt(cursor_notification.getColumnIndex("snooze_count")));

                    notificationlist.add(o_mednotificationList);


                } while (cursor_notification.moveToNext());
            }
        }



       if(notificationlist.size()>0)
       {}else{finish();}



    }
   public void removeListItem(View view) {

        final Animation animation = AnimationUtils.loadAnimation(MRA_reminder_notification.this, R.anim.scale);
       view.startAnimation(animation);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {

            @Override
            public void run() {
                recycleadapter.notifyDataSetChanged();
            }
        }, 500);

    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public  void closeActivity(){
        finish();
    }

}


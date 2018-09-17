package viroopa.com.medikart.MedicineReminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import viroopa.com.medikart.MedicineReminder.Model.mednotificationList;
import viroopa.com.medikart.MedicineReminder.adapter.RAD_sessionMedicines;
import viroopa.com.medikart.R;
import viroopa.com.medikart.helper.SqliteMRHandler;

public class MRA_session_wise_medicines extends AppCompatActivity {

    private TextView session_name,session_date,session_time;
    private RecyclerView mRecyclerView;
    private SqliteMRHandler db_mr;
    private String sMemberId;
    private RAD_sessionMedicines recycleadapter;
    private List<mednotificationList> Medicinelist = new ArrayList<mednotificationList>();
    private String Sch_DateTime="",session="",sessionTime="";
    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dateFormat_changed = new SimpleDateFormat("MMM dd, cccc");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_reminder_session_wise_medicines);


        db_mr = new SqliteMRHandler(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        session_name=(TextView)findViewById(R.id.session_name);
        session_date=(TextView)findViewById(R.id.session_date);
        session_time=(TextView)findViewById(R.id.session_time);


        getintent();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        recycleadapter = new RAD_sessionMedicines(this, Medicinelist);
        mRecyclerView.setAdapter(recycleadapter);

        fill_Medicine_list();

    }





    public  void fill_Medicine_list()
    {
        Medicinelist.clear();

        Cursor cursor_notification = db_mr.get_med_rem_sch_daywise_session(session, Sch_DateTime,sMemberId);
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
                    o_mednotificationList.setStatus(cursor_notification.getString(cursor_notification.getColumnIndex("status")));
                    Medicinelist.add(o_mednotificationList);


                } while (cursor_notification.moveToNext());
            }
        }



        if(Medicinelist.size()>0)
        {}else{finish();}



        final Animation animation = AnimationUtils.loadAnimation(MRA_session_wise_medicines.this, android.R.anim.slide_out_right);
        mRecyclerView.getRootView().startAnimation(animation);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {

            @Override
            public void run() {
                recycleadapter.notifyDataSetChanged();
            }
        }, 1000);


    }
    private  void getintent()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId  = pref.getString("memberid", "");
        Intent int_i= getIntent();
        Sch_DateTime=int_i.getStringExtra("sch_date");
        session=int_i.getStringExtra("Session_name");
        sessionTime=int_i.getStringExtra("Session_time");

        if(session!=null)
        {
            session_name.setText(session.equals("m") ? "Morning": session.equals("a") ? "Afternoon":(session.equals("e") ? "Evening":"Night"));
        }

        if(Sch_DateTime!=null)
        {

            try {
                current_date = dateFormat.parse(Sch_DateTime);
                session_date.setText(dateFormat_changed.format(current_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if(sessionTime!=null)
        {
            session_time.setText(sessionTime);
        }


    }
    @Override
    public void onBackPressed() {
        finish();
    }

}

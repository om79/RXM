package viroopa.com.medikart.MedicineReminder;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;


import java.util.ArrayList;

import viroopa.com.medikart.MainActivity;
import viroopa.com.medikart.MedicineReminder.Model.m_medicine_list;
import viroopa.com.medikart.MedicineReminder.adapter.AD_schedule_all_medicine;
import viroopa.com.medikart.R;
import viroopa.com.medikart.helper.SqliteMRHandler;


/**
 * Created by prakash on 17/08/15.
 */
public class MRA_Schedule_All_Medicine extends AppCompatActivity {
    // Log tag

    private ProgressDialog pDialog;
    private String sMemberId;

    private ArrayList<m_medicine_list> medicine_list = new ArrayList<m_medicine_list>();
    private ListView listView;
    private AD_schedule_all_medicine adapter_schedule_all_medicine;
    private ImageView homeBtn;
    private SqliteMRHandler db_mr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.med_rem_all_medicine);

        db_mr = new SqliteMRHandler(getApplicationContext());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        getIntenet();

        pDialog = new ProgressDialog(this);

        listView = (ListView) findViewById(R.id.list_medicinelist);
        homeBtn= (ImageView) findViewById(R.id.homeBtn);
        adapter_schedule_all_medicine = new AD_schedule_all_medicine(this, medicine_list);
        listView.setAdapter(adapter_schedule_all_medicine);

        get_all_medicine();
        homeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent Intenet_home = new Intent(MRA_Schedule_All_Medicine.this, MainActivity.class);
                startActivity(Intenet_home);
                finish();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            //pDialog = null;
        }
    }

    private void showPdialog(String sMessage) {
        if (pDialog != null) {
            // Showing progress dialog before making http request
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminder_pill_box, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addMedicineBtn) {
            Intent Intenet_add = new Intent(this, MRA_ReminderMain.class);
            startActivity(Intenet_add);
            finish();
            return true;
        }
        if (id == R.id.action_settings) {
            Intent Intenet_add = new Intent(this, MRA_MonitorSetting.class);
            startActivity(Intenet_add);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getIntenet() {
        Intent intent_med_rem_ = getIntent();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(MRA_Schedule_All_Medicine.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void get_all_medicine() {
        medicine_list.clear();
        Cursor cursor_all_medicine = db_mr.get_med_all_medicine_master(sMemberId);


        if ((cursor_all_medicine != null) || (cursor_all_medicine.getCount()> 0))
        {
            if (cursor_all_medicine.moveToFirst()) {
                do {
                    m_medicine_list O_medicine_list = new m_medicine_list();

                    O_medicine_list.setMedicine_Id(cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("medicine_id")));
                    O_medicine_list.setMedicine_Name(cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("medicine_name")));
                    O_medicine_list.setImd_id(cursor_all_medicine.getInt(cursor_all_medicine.getColumnIndex("image_id")));
                    O_medicine_list.setFirst_color_id(cursor_all_medicine.getInt(cursor_all_medicine.getColumnIndex("first_color_id")));
                    O_medicine_list.setSecond_color_id(cursor_all_medicine.getInt(cursor_all_medicine.getColumnIndex("second_color_id")));

                    medicine_list.add(O_medicine_list);

                } while (cursor_all_medicine.moveToNext());
            }
        }

        final Animation animation = AnimationUtils.loadAnimation(MRA_Schedule_All_Medicine.this, android.R.anim.slide_out_right);
        listView.getRootView().startAnimation(animation);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {

            @Override
            public void run() {
                adapter_schedule_all_medicine.notifyDataSetChanged();
            }
        }, 500);

        if(medicine_list.size()<1)
        {
            Intent Intenet_mr = new Intent(MRA_Schedule_All_Medicine.this, MRA_Welcomeactivity.class);
            startActivity(Intenet_mr);
            finish();
        }
    }
}


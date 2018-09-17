package viroopa.com.medikart.MedicineReminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import viroopa.com.medikart.MedicineReminder.Model.m_medicine_list;
import viroopa.com.medikart.MedicineReminder.adapter.AD_AllPillBuddyList;
import viroopa.com.medikart.R;
import viroopa.com.medikart.helper.SqliteMRHandler;


public class MRA_PillBuddy extends AppCompatActivity {
    private  Menu objMemberMenu;
    private ListView lst_pillbuddy;
    private Button add_pill_buddy;
    AD_AllPillBuddyList adapterallPillBuddy;
    private SqliteMRHandler db_mr;

    private String sMemberId;

    private ArrayList<m_medicine_list> PillBuddy_list = new ArrayList<m_medicine_list>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_reminder_pill_buddy);

        lst_pillbuddy=(ListView)findViewById(R.id.lst_pillbuddy);
        add_pill_buddy=(Button)findViewById(R.id.add_pill_buddy);

        db_mr = new SqliteMRHandler(getApplicationContext());
        getIntenet();
        adapterallPillBuddy=new AD_AllPillBuddyList(this,PillBuddy_list);
        lst_pillbuddy.setAdapter(adapterallPillBuddy);
        lst_pillbuddy.setEmptyView(findViewById(R.id.no_pill_buddy));
        get_all_pillBuddy();


        add_pill_buddy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

           Show_add_MediFriends();

            }
        });


    }
    @Override

    public void onResume() {
        super.onResume();
        get_all_pillBuddy();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pill_buddy, menu);
        this.objMemberMenu=menu;
        TextView textHeading = new TextView(this);
        textHeading.setText("Have a Pill Buddy Code ?");
        textHeading.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.add_pillbuddy_white, //left
                0, //top
                0, //right
                0);
        textHeading.setTextColor(Color.GRAY);

        textHeading.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Show_add_MediFriend_oncode();

            }
        });

        objMemberMenu.findItem(R.id.circlularImage).setActionView(textHeading);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent Intenet_adds = new Intent(this, MRA_MonitorSetting.class);
            startActivity(Intenet_adds);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private void get_all_pillBuddy() {

        PillBuddy_list.clear();



        Cursor cursor_all_medicine = db_mr.get_all_pill_buddyr(sMemberId);


        if ((cursor_all_medicine != null) || (cursor_all_medicine.getCount()> 0))
        {

            if (cursor_all_medicine.moveToFirst()) {
                do {
                    m_medicine_list O_pillBuddy_list = new m_medicine_list();

                    O_pillBuddy_list.setMedicine_Id(cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("medfriend_id")));
                    O_pillBuddy_list.setMedicine_Name(cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("reminder_friendname")));
                    O_pillBuddy_list.setPill_buddy_image_name(cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("reminder_image_name")));

                    PillBuddy_list.add(O_pillBuddy_list);


                } while (cursor_all_medicine.moveToNext());


                adapterallPillBuddy.notifyDataSetChanged();
            }
        }else
        {

        }



    }

    private void Show_add_MediFriends(){
        Intent Intenet_add = new Intent(this, MRA_MedFriend.class);
        startActivity(Intenet_add);
    }

    private void Show_add_MediFriend_oncode(){
        Intent Intenet_add = new Intent(this, MRA_Add_pilly_Budy_On_Code.class);
        startActivity(Intenet_add);
    }
    private void getIntenet() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

    }

}

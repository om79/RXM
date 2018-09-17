package viroopa.com.medikart.MedicineReminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cepheuen.progresspageindicator.ProgressPageIndicator;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.MedicineReminder.services.MR_NotificationService;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.helper.SqliteMRHandler;
import viroopa.com.medikart.wmMonitor.WMCommon.WM_NotificationService;

public class MRA_Welcomeactivity extends AppCompatActivity {

    private  Menu objMemberMenu;
    private SqliteMRHandler db_mr;
    LayoutInflater inflater;	//Used to create individual pages
    ProgressPageIndicator pagerIndicator;
    AppController globalVariable;
    private String sMemberId;
    String pageData[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mr_welcomeactivity);
        db_mr = new SqliteMRHandler(getApplicationContext());
        globalVariable = (AppController)getApplicationContext();
        getIntenet();
        triger_notification();
        check_data_availabity();

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mr_welcome, menu);
        this.objMemberMenu=menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.circlularImage) {
            Intent Intenet_add = new Intent(this, MRA_SetReminder.class);
             startActivity(Intenet_add);
             finish();
            return true;
        }

        if (id == R.id.action_settings) {
            Intent Intenet_adds = new Intent(this, MRA_MonitorSetting.class);
            startActivity(Intenet_adds);
          //  finish();
            return true;
        }

        if (id == R.id.reminder_all_medicine) {
            // Show_All_Medicine();
        }

        return super.onOptionsItemSelected(item);
    }

    private void Show_add_Medicine(){
       // Intent Intenet_add = new Intent(this, MedReminderSetReminder.class);
       // startActivity(Intenet_add);
    }

    private void check_data_availabity()
    {
       String count=  db_mr.check_schedule(sMemberId);
        if(!count.equals("0"))
        {
            Intent Intenet_add = new Intent(this, MRA_ReminderMain.class);
            startActivity(Intenet_add);
            finish();
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    class MyPagesAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return 3;
        }
        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(View collection, int position) {
            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          //  Toast.makeText(getApplicationContext(),String.valueOf(position), Toast.LENGTH_LONG)
                  //  .show();
            View view=null;
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.layout.add_doctor;
                     view = inflater.inflate(resId, null);
                    TextView textView=(TextView)view.findViewById(R.id.textView);

                   // textView.setText("testingggggg");
                    break;
                case 1:
                    resId = R.layout.bp_date_entry;
                    view = inflater.inflate(resId, null);

                    break;
                case 2:
                    resId = R.layout.activity_login;
                    view = inflater.inflate(resId, null);
                    break;

            }


            ((ViewPager) collection).addView(view, 0);
            return view;
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0==(View)arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object=null;
        }
    }
    private void getIntenet() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

    }

    private void triger_notification()
    {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MR_NotificationService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 102, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() ,1000*60,pendingIntent );


    }

}

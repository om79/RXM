package viroopa.com.medikart.wmMonitor;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.dm_SetGoal_Dialog;
import viroopa.com.medikart.common.numerdialog;
import viroopa.com.medikart.helper.SqliteWMHandler;
import viroopa.com.medikart.wmMonitor.WMCommon.WM_NotificationService;
import viroopa.com.medikart.wmMonitor.WMCommon.WM_SetGoal_Dialog;

public class WMA_Welcome extends AppCompatActivity  implements WM_SetGoal_Dialog.OnWaterGoalDialogDoneListener,
                                                                   numerdialog.OnNumberDialogDoneListener{

    private  Menu objMemberMenu;


    AppController globalVariable;
    private String sMemberId;
    Integer relationShipId=8;
    private String date;

    private  static  int ONE_HOUR=1000*60*60;
    private  static int HALF_HOUR=1000*60*30;
    private  static int TWO_HOUR=1000*60*60*2;

    private SharedPreferences pref ;
    private static String LITRE_SELECTED="litre_selected";


    private  int selected_notification_intervel;

    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");


    private SqliteWMHandler db;

    @Override
    public void OnWaterGoalChange(int value, boolean isDatasave,boolean litre_selected){

      if(isDatasave)
      {
          SharedPreferences.Editor edt= pref.edit();
          edt.putBoolean(LITRE_SELECTED,litre_selected);
          edt.commit();
          Show_add_water_goal(litre_selected);
      }
    }
    @Override
    public void onDone(int value,String sClass) {
        if(getFragmentRefreshListener()!=null){
            getFragmentRefreshListener().onRefresh(sClass,Integer.toString(value));
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_welcomeactivity);
        globalVariable = (AppController) getApplicationContext();
        date= dateFormat_query.format(current_date);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        db = new SqliteWMHandler(getApplicationContext());

        pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);

        initialize_bottom_bar();
        get_notification_intervel();
        triger_notification();
        getIntenet();
        check_data_availabity();
    }
    public interface FragmentRefreshListener{
        void onRefresh(String p_object, String p_value);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_wm_welcome, menu);
        this.objMemberMenu=menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.circlularImage) {
            WM_SetGoal_Dialog myDiag = WM_SetGoal_Dialog.newInstance(Integer.parseInt(sMemberId), relationShipId,date);
            myDiag.show(getFragmentManager(), "Diag");
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent_setting = new Intent(WMA_Welcome.this, WMA_Settings
                    .class);
            startActivity(intent_setting);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Show_add_water_goal(Boolean unit_sel){
         Intent Intenet_add = new Intent(this, WMA_watermain.class);
         Intenet_add.putExtra("UnitSelected",unit_sel);
         startActivity(Intenet_add);
        finish();
    }

    private void check_data_availabity()
    {
        String count=  db.check_water_entry(relationShipId);
        if(!count.equals("0"))
        {
              Intent Intenet_add = new Intent(this, WMA_watermain.class);
             startActivity(Intenet_add);
            finish();
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void getIntenet()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

        if(globalVariable.getRealationshipId()!=null)
        {
            relationShipId=Integer.parseInt(globalVariable.getRealationshipId());
        }else
        {
            relationShipId=8;
        }

    }





    private void initialize_bottom_bar()
    {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home_icon_white, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Set Goal", R.drawable.setgoal_white, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("View Progress", R.drawable.report_white, R.color.colorPrimary);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));

        bottomNavigation.setAccentColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setUseElevation(true);
        bottomNavigation.setForceTitlesDisplay(true);

        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {

                if(position==0 )
                {
                    finish();
                }else
                if(position==1)
                {
                    WM_SetGoal_Dialog myDiag = WM_SetGoal_Dialog.newInstance(Integer.parseInt(sMemberId), relationShipId,date);
                    myDiag.show(getFragmentManager(), "Diag");
                }else
                if(position==2)
                {
                    WM_SetGoal_Dialog myDiag = WM_SetGoal_Dialog.newInstance(Integer.parseInt(sMemberId), relationShipId,date);
                    myDiag.show(getFragmentManager(), "Diag");
                }

            }
        });
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    private void triger_notification()
    {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WM_NotificationService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 101, intent, 0);
        am.cancel(pendingIntent);


        am.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis()+selected_notification_intervel ,selected_notification_intervel,pendingIntent );


    }
    void get_notification_intervel()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        selected_notification_intervel = pref.getInt("selected_notification_intervel", ONE_HOUR);

    }
}

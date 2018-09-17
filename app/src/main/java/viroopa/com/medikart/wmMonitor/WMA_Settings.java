package viroopa.com.medikart.wmMonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.common.SettingListwithRadiodialog;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteMRHandler;
import viroopa.com.medikart.helper.SqliteWMHandler;
import viroopa.com.medikart.wmMonitor.WMCommon.WM_NotificationService;

public class WMA_Settings extends AppCompatActivity  implements AdapterView.OnItemClickListener ,
                                                                SettingListwithRadiodialog.OnRadioButtonSelectListener{
    ArrayList<ContentItem> objects = new ArrayList<ContentItem>();
    private String sMemberId;
    private TextView def_wight_unit_txt,def_activity_level,def_not_interval;
    private String setting_weight_unit,setting_activity_level,setting_enable_sound;
    AppController globalVariable;
    private ListView lv;
    private CheckBox chk_mute_sound;
    private String formatted_today_date;
    private SQLiteHandler db;
    private SqliteWMHandler db_wm;
    private String RelationShipId;
    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    private   String Activitylist[] ;
    private   String Notification_Interval_array[] ;
    private String array_weight_unit[] ;

    private  static  int ONE_HOUR=1000*60*60;
    private  static int TWO_HOUR=1000*60*60*2;
    private  static int THREE_HOUR=1000*60*60*3;
    private  static int FOUR_HOUR=1000*60*60*4;

    private  int selected_notification_intervel,setting_notification_interval_array_id;

    SharedPreferences pref;

    private static String WMA_MUTE_NOTIFICATION="wma_mute_notification";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma__settings);
        pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        db = SQLiteHandler.getInstance(this);
        db_wm=new SqliteWMHandler(this);
        globalVariable = (AppController) getApplicationContext();
        Activitylist=getResources().getStringArray(R.array.Activity_Level);
        Notification_Interval_array=getResources().getStringArray(R.array.notification_Interval_array);
        array_weight_unit = getResources().getStringArray(R.array.array_weight_unit);

        objects.add(new ContentItem("GENERAL", ""));
        objects.add(new ContentItem("Select Weight Unit", "Select default weight unit"));
        objects.add(new ContentItem("Activity Level", "Select your default Activity level"));
        objects.add(new ContentItem("TOOLS", ""));
        objects.add(new ContentItem("Water Sound", "Mute Sound"));
        objects.add(new ContentItem("Wipe Data", "Wipe all entries"));
        objects.add(new ContentItem("Intervel For Notification", "Select time intervel for water Notification"));
        objects.add(new ContentItem("Sound Alert", "check to Disable Notification Sound Alert"));

        MyAdapter adapter = new MyAdapter(this, objects);

        lv = (ListView) findViewById(R.id.listChart);

        getIntenet();
        lv.setAdapter(adapter);
        lv.setItemsCanFocus(true);

        lv.setOnItemClickListener(this);




    }
    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {

        CheckBox cbSound = (CheckBox) v.findViewById(R.id.cbBox);
        switch (pos) {

            case 1:
                load_selected_data();
                SettingListwithRadiodialog myDiag = SettingListwithRadiodialog.newInstance(array_weight_unit,"selectWeight","Select Weight Unit", Arrays.asList(array_weight_unit).indexOf(setting_weight_unit));
                myDiag.show(getFragmentManager(), "Diag");
                break;
            case 2:
                load_selected_data();
                SettingListwithRadiodialog myActivityDiag = SettingListwithRadiodialog.newInstance(Activitylist,"SelectActivity","Select Default Activity Level", Arrays.asList(Activitylist).indexOf(setting_activity_level));
                myActivityDiag.show(getFragmentManager(), "Diag");
                break;

            case 4:
                load_selected_data();
                check_Enable_Sound(cbSound);
                break;

            case 5:
                f_bp_wipe_data();
                break;
            case 6:

                if(selected_notification_intervel==ONE_HOUR)
                {
                    setting_notification_interval_array_id=0;
                }else  if(selected_notification_intervel==TWO_HOUR)
                {
                    setting_notification_interval_array_id=1;
                }else if(selected_notification_intervel==THREE_HOUR)
                {
                    setting_notification_interval_array_id=2;
                }else if(selected_notification_intervel==FOUR_HOUR)
                {
                    setting_notification_interval_array_id=3;
                }


                    SettingListwithRadiodialog myNotifiDiag = SettingListwithRadiodialog.newInstance(Notification_Interval_array,"SelectNotInterval","Select Default Notification Interval", setting_notification_interval_array_id);
                myNotifiDiag.show(getFragmentManager(), "Diag");
                break;
            case 7:
                mute_notification_sound_check(cbSound);
                break;

        }
    }
    private class ContentItem {
        String name;
        String desc;

        public ContentItem(String n, String d) {
            name = n;
            desc = d;
        }
    }

    private void f_bp_wipe_data() {

        new AlertDialog.Builder(this)
                .setTitle("Wipe data")
                .setMessage("This operation will delete all stored entries.\n Continue?")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        wipe_all_data();

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }



    private void getIntenet() {
        String setting_name = "";


        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        if (globalVariable.getRealationshipId() != null) {
            RelationShipId = globalVariable.getRealationshipId();
        } else {
            RelationShipId = "8";
        }

        try {

            formatted_today_date = (dateFormat_query.format(current_date));

        } catch (Exception e) {

        }

        Cursor cursor_session = db.getAllSetting_data(RelationShipId, sMemberId, "3");

        if (!cursor_session.moveToFirst()) {

            db.InsertSettingData(sMemberId, RelationShipId, "wm_weight_unit", "kg", "3", "WM_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, RelationShipId, "wm_weight_unit", "kg", "3", "WM_Monitor", formatted_today_date, "", "", "A");

            db.InsertSettingData(sMemberId, RelationShipId, "def_activity_level", "false", "3", "WM_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, RelationShipId, "def_activity_level", "false", "3", "WM_Monitor", formatted_today_date, "", "", "A");

            db.InsertSettingData(sMemberId, RelationShipId, "Enable_Sound", "false", "3", "WM_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, RelationShipId, "Enable_Sound", "false", "3", "WM_Monitor", formatted_today_date, "", "", "A");




        } else {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("wm_weight_unit")) {
                    setting_weight_unit = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("def_activity_level")) {
                    setting_activity_level = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("Enable_Sound")) {
                    setting_enable_sound = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }



            } while (cursor_session.moveToNext());


        }

    }

    private void SavedataToServerSettingEntries(String member_id, String relation_ship_id, String setting_name, String setting_value,
                                                String module_id, String module_description, String Created_date, String UUID, String IMEI, String Mode) {
        try {

            //showPdialog("wait..");
            Map<String, String> params = new HashMap<String, String>();
            String id = "0";

            id = db.getSetting_id(relation_ship_id, member_id, module_id, setting_name);
            if (id == null) {
                id = "0";
            }

            params.put("id", id);
            params.put("member_id", member_id);
            params.put("Relationship_ID", relation_ship_id);
            params.put("Name", setting_name);
            params.put("Value", setting_value);
            params.put("Module_Id", module_id);
            params.put("Module_Description", module_description);
            params.put("UUID", UUID);
            params.put("IMEI", IMEI);
            params.put("Mode", Mode);

            JSONObject jparams = new JSONObject(params);
            String I_Type = "Post";
            String Controller = AppConfig.URL_POST_WATERENTRY;
            String Parameter = "";
            String JsonObject = String.valueOf(jparams);
            String Created_Date = Created_date;

            String F_KEY_UPLOAD_DOWNLOAD = "true";
            Integer F_KEY_SYNCMEMBERID = Integer.parseInt(member_id);
            String F_KEY_MODULE_NAME = "WE";
            String F_KEY_MODE = "A";
            String F_KEY_ControllerName = "Monitor";
            String F_KEY_MethodName = "MonitorSettingBAL";

            db.InsertSyncTable(I_Type, Controller, Parameter,
                    JsonObject, Created_Date, UUID, F_KEY_UPLOAD_DOWNLOAD, F_KEY_SYNCMEMBERID,
                    F_KEY_MODULE_NAME, F_KEY_MODE, F_KEY_ControllerName, F_KEY_MethodName);


            //f_alert_ok("Sucess", "Entry Saved");


        } catch (Exception E) {
            E.toString();
            // f_alert_ok("Error","Error"+E.getMessage());
        }

    }


    private class MyAdapter extends ArrayAdapter<ContentItem> {

        public MyAdapter(Context context, List<ContentItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ContentItem c = getItem(position);
            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemone, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvDescr);
                holder.tvDesc = (TextView) convertView.findViewById(R.id.tvPrice);


                if (position == 0 || position == 3) {
                    convertView.setBackgroundColor(Color.rgb(255, 153, 0));
                    convertView.setLayoutParams(new AbsListView.LayoutParams(lv.getWidth(), 40));
                    holder.tvName.setTextSize(10);
                    holder.tvName.setTextColor(Color.WHITE);


                }

                if (position == 4 ) {
                    CheckBox cbBuy = (CheckBox) convertView.findViewById(R.id.cbBox);
                    cbBuy.setVisibility(convertView.VISIBLE);
                    if (setting_enable_sound != null) {
                        if (setting_enable_sound.equals("true")) {
                            cbBuy.setChecked(true);
                        } else {
                            cbBuy.setChecked(false);
                        }
                    }
                }

                if (position == 7) {
                    CheckBox cbBuy = (CheckBox) convertView.findViewById(R.id.cbBox);
                    cbBuy.setVisibility(convertView.VISIBLE);
                    chk_mute_sound = (CheckBox) convertView.findViewById(R.id.cbBox);

                    if (pref.getBoolean(WMA_MUTE_NOTIFICATION,false)) {
                        chk_mute_sound.setChecked(true);
                    } else {
                        chk_mute_sound.setChecked(false);
                    }


                }


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvName.setText(c.name);
            holder.tvDesc.setText(c.desc);
            initialize_textview(position, holder.tvDesc);
            convertView.setTag(holder);
            return convertView;
        }

        private class ViewHolder {

            TextView tvName, tvDesc;
        }
    }

    private void load_selected_data() {
        String setting_name = "";
        Cursor cursor_session =  db.getAllSetting_data(RelationShipId, sMemberId, "3");

        if (cursor_session.moveToFirst()) {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("wm_weight_unit")) {
                    setting_weight_unit = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("def_activity_level")) {
                    setting_activity_level = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("Enable_Sound")) {
                    setting_enable_sound = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }



            } while (cursor_session.moveToNext());
        }
    }

    private void check_Enable_Sound(CheckBox chk) {
        CheckBox ckh_bp = chk;


        if (!ckh_bp.isChecked()) {
            ckh_bp.setChecked(true);
            db.updateSettingData(sMemberId, RelationShipId, "Enable_Sound", "true", "3", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, RelationShipId, "Enable_Sound", "true", "3", "WM_Monitor", formatted_today_date, "", "", "E");

        } else {
            ckh_bp.setChecked(false);
            db.updateSettingData(sMemberId, RelationShipId, "Enable_Sound", "false", "3", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, RelationShipId, "Enable_Sound", "false", "3", "WM_Monitor", formatted_today_date, "", "", "E");
        }

    }

    @Override
    public void onRadioButtonSelect(int value,String sClass) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        switch (sClass) {

            case "selectWeight":
                def_wight_unit_txt.setText(array_weight_unit[value]);
                db.updateSettingData(sMemberId, RelationShipId, "wm_weight_unit", array_weight_unit[value], "3", formatted_today_date);
                SavedataToServerSettingEntries(sMemberId, RelationShipId, "wm_weight_unit", array_weight_unit[value], "3", "WM_Monitor", formatted_today_date, "", "", "E");

                break;

            case "SelectActivity":
                def_activity_level.setText(Activitylist[value]);
                db.updateSettingData(sMemberId, RelationShipId, "def_activity_level", Activitylist[value], "3", formatted_today_date);
                SavedataToServerSettingEntries(sMemberId, RelationShipId, "def_activity_level", Activitylist[value], "3", "WM_Monitor", formatted_today_date, "", "", "E");

                break;

            case "SelectNotInterval":
                def_not_interval.setText(Notification_Interval_array[value]);
                SharedPreferences.Editor editor = pref.edit();
                if(value==0)
                {
                    editor.putInt("selected_notification_intervel",ONE_HOUR);
                }else if(value==1)
                {
                    editor.putInt("selected_notification_intervel",TWO_HOUR);
                }else if(value==2)
                {
                    editor.putInt("selected_notification_intervel",THREE_HOUR);
                }else if(value==3)
                {
                    editor.putInt("selected_notification_intervel",FOUR_HOUR);
                }

                    editor.commit();
                get_notification_intervel();
                triger_notification();
                    break;

        }


    }

    private void initialize_textview(int i,TextView tv) {
        if (i == 1) {
            def_wight_unit_txt = tv;
            if(setting_weight_unit!=null) {
                def_wight_unit_txt.setText(setting_weight_unit);
            }
        } else if (i == 2) {
            def_activity_level = tv;
            if(setting_activity_level!=null) {
                if(!setting_activity_level.equals("false")) {
                    def_activity_level.setText(setting_activity_level);
                }
            }
        } else if (i == 6) {
            def_not_interval = tv;
            get_notification_intervel();
            if(selected_notification_intervel==ONE_HOUR)
            {
                def_not_interval.setText(Notification_Interval_array[0]);
            }else  if(selected_notification_intervel==TWO_HOUR)
            {
                def_not_interval.setText(Notification_Interval_array[1]);
            }else if(selected_notification_intervel==THREE_HOUR)
            {
                def_not_interval.setText(Notification_Interval_array[2]);
            }else if(selected_notification_intervel==FOUR_HOUR)
            {
                def_not_interval.setText(Notification_Interval_array[3]);
            }


        }


    }

    void get_notification_intervel()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        selected_notification_intervel = pref.getInt("selected_notification_intervel", ONE_HOUR);

    }
    private void triger_notification()
    {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WM_NotificationService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 101, intent, 0);

        am.cancel(pendingIntent);

        am.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis()+selected_notification_intervel ,selected_notification_intervel,pendingIntent );


    }

    private void mute_notification_sound_check(CheckBox chk)
    {
        CheckBox ckh_bp = chk;

        SharedPreferences.Editor edt=pref.edit();
        if (!ckh_bp.isChecked()) {
            ckh_bp.setChecked(true);
            Toast.makeText(WMA_Settings.this, "Notification is muted", Toast.LENGTH_LONG).show();
            edt.putBoolean(WMA_MUTE_NOTIFICATION,true);

        } else {
            ckh_bp.setChecked(false);
            Toast.makeText(WMA_Settings.this, "Notification is unmuted", Toast.LENGTH_LONG).show();
            edt.putBoolean(WMA_MUTE_NOTIFICATION,false);
        }
        edt.commit();
    }

    private void wipe_all_data(){

        db_wm.delete_all_data();
        db.delete_module_wise_setting_module_id("3");
        ConstData.WIPE_DATA_FROM_SERVER(sMemberId,"WM",this);

        Intent Intenet_mr = new Intent(WMA_Settings.this, WMA_Welcome.class);
        Intenet_mr.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Intenet_mr);
        finish();
    }

}

package viroopa.com.medikart.MedicineReminder;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import viroopa.com.medikart.MainActivity;
import viroopa.com.medikart.MedicineReminder.Model.M_alarmsoundlist;
import viroopa.com.medikart.MedicineReminder.adapter.AD_alarmsound;
import viroopa.com.medikart.MedicineReminder.adapter.AD_timeslot;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.bpmonitor.BPA_MonitorSetting;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.common.SettingListwithRadiodialog;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteBPHandler;
import viroopa.com.medikart.helper.SqliteMRHandler;


/**
 * Created by 1144 on 06-10-2016.
 */

public class MRA_MonitorSetting extends AppCompatActivity implements OnItemClickListener,
        SettingListwithRadiodialog.OnRadioButtonSelectListener {

    private static final String TAG = "MRA_MonitorSetting";

    String get_def_site;
    String get_def_pos;
    private String get_def_snooze,get_def_sound_alert,get_def_notiTone;
    private TextView def_sound_alert,def_snooze,def_notiTone;
    List<M_alarmsoundlist> sounds_selected = new ArrayList<M_alarmsoundlist>();

    String setting_weight_unit="";
    private SqliteMRHandler db_mr;
    private SQLiteHandler db;
    private CheckBox chk_mute_sound;
    AppController globalVariable;
    private String formatted_today_date="";
    private String sMemberId;
    private ListView lv;
    private String getSelectedRelationshipId="";
    private String module_id="4";
    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    private String array_position[];
    private String array_positiontype[] ;
    private String array_weight_unit[] ;
    private String array_snooze[];
    private String selected_ringtone="";

    private static String SNOOZE_COUNT="snooze_count";
    private static String MRA_MUTE_NOTIFICATION="mra_mute_notification";
    private static String MRA_DEFAULT_NOTIFICATION_RINGTONE="mra_notification_ringtone";

    AD_timeslot adapter ;
    AD_alarmsound alarmadapter;
    SharedPreferences pref;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db_mr = SqliteMRHandler.getInstance(this);
        db=SQLiteHandler.getInstance(this);
        pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);


        selected_ringtone=pref.getString(MRA_DEFAULT_NOTIFICATION_RINGTONE,
                (RingtoneManager.getActualDefaultRingtoneUri(this.getApplicationContext(), RingtoneManager.TYPE_RINGTONE).toString()));

        array_position = getResources().getStringArray(R.array.array_position);
        array_positiontype = getResources().getStringArray(R.array.array_positiontype);
        array_weight_unit = getResources().getStringArray(R.array.array_weight_unit);
        array_snooze=getResources().getStringArray(R.array.array_snooze);

        setContentView(R.layout.mra_main_setting);
        globalVariable = (AppController) getApplicationContext();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        getIntenet();


        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();

        objects.add(new ContentItem("GENERAL", ""));
        //1 option
        objects.add(new ContentItem("Sound Alert", "check to Disable Notification Sound Alert"));
        //2 option
       // objects.add(new ContentItem("Snooze Interval", "Select snooze time interval"));

        if (get_def_snooze != null)
        {
            if (!get_def_snooze.equals("false")){
                objects.add(new ContentItem("Snooze Interval", get_def_snooze));
            } else

                objects.add(new ContentItem("Snooze Interval", "Select snooze time interval"));

        }else
        {
            objects.add(new ContentItem("Snooze Interval", "Select snooze time interval"));
        }

        //3 option
        if(!selected_ringtone.equals(""))
        {
            sounds_selected=getStandardAlarms();

            for (int i = 0; i <    sounds_selected.size(); i++) {
                if ( sounds_selected.get(i).getUri().toString().equals(selected_ringtone)) {
                    objects.add(new ContentItem("Select Notification tone",  sounds_selected.get(i).getTitle()));

                }
            }


        }else
        {
            objects.add(new ContentItem("Select Notification tone", "Select tone for Notification"));
        }

        //4 option

        objects.add(new ContentItem("Wipe Data", "Delete all monitoring data"));



        MyAdapter adapter = new MyAdapter(this, objects);

        lv = (ListView) findViewById(R.id.listChart);


        lv.setAdapter(adapter);
        lv.setItemsCanFocus(true);

        lv.setOnItemClickListener(this);
        chk_mute_sound = (CheckBox) findViewById(R.id.cbBox);


    }




    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {

        CheckBox cbBuy = (CheckBox) v.findViewById(R.id.cbBox);
        switch (pos) {
            case 0:
                break;
            case 1:
                mute_notification_sound_check(cbBuy);
                break;
            case 2:
                load_selected_data();
                defaultSnoozeinterval();
                break;
            case 3:
                setUpListViewRingtones(def_notiTone);
                break;
            case 4:
                load_selected_data();
                f_bp_wipe_data();
                break;
               /*  case 8:
           f_bp_wipe_data();
                break;*/

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



    private void setUpListViewRingtones(final TextView pick_ringtone) {

        LayoutInflater inflater = LayoutInflater.from(MRA_MonitorSetting.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(MRA_MonitorSetting.this);
        final View dialog = inflater.inflate(R.layout.activity_select_rigtone, null);
        builder.setView(dialog);
        final Dialog dlg = builder.create();

        final TextView btncancel= (TextView)dialog.findViewById(R.id.btncancel);
        final Button btnok= (Button)dialog.findViewById(R.id.btnok);
        final ListView list_view= (ListView)dialog.findViewById(R.id.list_view);
        List<M_alarmsoundlist> sounds = new ArrayList<M_alarmsoundlist>();

        sounds.addAll(getStandardAlarms());
        alarmadapter = new AD_alarmsound(this, sounds);

        list_view.setAdapter(alarmadapter);

        if(selected_ringtone!=null)
        {try {
            setSelected(Uri.parse(selected_ringtone));
        }catch (Exception e)
        {}
        }

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.cancel();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alarmadapter.player.stop();
                saveCurrentSoundAndExit(pick_ringtone);
                dlg.cancel();
            }
        });
     /*   dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;*/
        dlg.show();
    }

     private List<M_alarmsoundlist> getStandardAlarms() {
        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_ALARM | RingtoneManager.TYPE_RINGTONE);
        List<M_alarmsoundlist> sounds = new ArrayList<M_alarmsoundlist>();
        Cursor cursor = manager.getCursor();
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext(), i++) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            sounds.add(new M_alarmsoundlist(title, manager.getRingtoneUri(i)));
        }
        cursor.deactivate();
        return sounds;
    }



    private void saveCurrentSoundAndExit(TextView ringtext) {
        Uri uri = alarmadapter.getItem(alarmadapter.getSelected()).getUri();
        ringtext.setText(alarmadapter.getItem(alarmadapter.getSelected()).getTitle());
        SharedPreferences.Editor edt=pref.edit();
        edt.putString(MRA_DEFAULT_NOTIFICATION_RINGTONE,uri.toString());
        selected_ringtone=uri.toString();
       // globalVariable.setCurrentAlarmSound(uri);
        edt.commit();
    }
    private void setSelected(Uri sound) {
        // Skip sound at position 0, because it is custom sound
        // with uri equals to null.
        for (int i = 1; i < alarmadapter.getCount(); i++) {
            if (alarmadapter.getItem(i).getUri().equals(sound)) {
                alarmadapter.setSelected(i);
                return;
            }
        }
        alarmadapter.getItem(0).setUri(sound);
        alarmadapter.setSelected(0);
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
            MyAdapter.ViewHolder holder = null;

            if (convertView == null) {

                holder = new MyAdapter.ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemone, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvDescr);
                holder.tvDesc = (TextView) convertView.findViewById(R.id.tvPrice);


                initialize_textview(position, holder.tvDesc);



                if (position == 0 || position == 5 || position == 7) {
                    convertView.setBackgroundColor(Color.rgb(255, 153, 0));
                    convertView.setLayoutParams(new AbsListView.LayoutParams(lv.getWidth(), 38));
                    holder.tvName.setTextSize(12);
                    holder.tvName.setTextColor(Color.WHITE);


                }
                if (position == 1) {
                    CheckBox cbBuy = (CheckBox) convertView.findViewById(R.id.cbBox);
                    cbBuy.setVisibility(convertView.VISIBLE);
                    chk_mute_sound = (CheckBox) convertView.findViewById(R.id.cbBox);

                        if (pref.getBoolean(MRA_MUTE_NOTIFICATION,false)) {
                            chk_mute_sound.setChecked(true);
                        } else {
                            chk_mute_sound.setChecked(false);
                        }


                }


            } else {
                holder = (MyAdapter.ViewHolder) convertView.getTag();
            }

            holder.tvName.setText(c.name);
            holder.tvDesc.setText(c.desc);
            convertView.setTag(holder);
            return convertView;
        }

        private class ViewHolder {

            TextView tvName, tvDesc;
        }
    }



    private void defaultSnoozeinterval() {
        SettingListwithRadiodialog myDiag = SettingListwithRadiodialog.newInstance(array_snooze,"selectSnoozeInterval","Select snooze time Interval", Arrays.asList(array_snooze).indexOf(get_def_snooze));
        myDiag.show(getFragmentManager(), "Diag");

    }



    private void getIntenet() {
        String setting_name = "";


        sMemberId = pref.getString("memberid", "");
        if (globalVariable.getRealationshipId() != null) {
            getSelectedRelationshipId = globalVariable.getRealationshipId();
        } else {
            getSelectedRelationshipId = "8";
        }

        try {

            formatted_today_date = (dateFormat_query.format(current_date));

        } catch (Exception e) {


        }

        Cursor cursor_session = db.getAllSetting_data(getSelectedRelationshipId, sMemberId, module_id);

        if (!cursor_session.moveToFirst()) {

            db.InsertSettingData(sMemberId, getSelectedRelationshipId, "sound_alert", "false", module_id, "MRA_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "sound_alert", "false", module_id, "MRA_Monitor", formatted_today_date, "", "", "A");

            db.InsertSettingData(sMemberId, getSelectedRelationshipId, "notification_tone", "path", module_id, "MRA_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "notification_tone", "path", module_id, "MRA_Monitor", formatted_today_date, "", "", "A");


        } else {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("sound_alert")) {
                    get_def_sound_alert = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }

                if (setting_name.equals("notification_tone")) {
                    get_def_notiTone = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }

            } while (cursor_session.moveToNext());
        }

        get_def_snooze=pref.getString(SNOOZE_COUNT,"5 minutes");

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
            String F_KEY_MODULE_NAME = "MS";
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



    private void load_selected_data() {
        String setting_name = "";
        Cursor cursor_session = db.getAllSetting_data(getSelectedRelationshipId, sMemberId, "1");

        if (cursor_session.moveToFirst()) {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("sound_alert")) {
                    get_def_sound_alert = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("snooze_interval")) {
                    get_def_snooze = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("notification_tone")) {
                    get_def_notiTone = ConstData.getValueOrDefault( cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
               /* if (setting_name.equals("use_last_entered_values")) {
                    setting_last_enterd_values = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }*/


            } while (cursor_session.moveToNext());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }



    public Intent getSupportParentActivityIntent() {

        Intent newIntent=null;
        finish();
        return newIntent;
    }


    @Override
    public void onRadioButtonSelect(int value,String sClass) {


        switch (sClass) {

            case "selectSnoozeInterval":
                def_snooze.setText(array_snooze[value]);
               // db.updateSettingData(sMemberId, getSelectedRelationshipId, "snooze_interval", array_snooze[value], module_id, formatted_today_date);
                //SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "weight_unit", array_snooze[value], module_id, "MRA_Monitor", formatted_today_date, "", "", "E");

                SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                SharedPreferences.Editor editor= pref.edit();

                editor.putString(SNOOZE_COUNT,array_snooze[value]);
                editor.commit();


                break;


        }


    }
    private void initialize_textview(int i,TextView tv) {
        if (i == 1) {
            def_sound_alert = tv;
        } else if (i == 2) {
            def_snooze = tv;
        } else if (i == 3) {
            def_notiTone = tv;
        }

    }

    private void mute_notification_sound_check(CheckBox chk)
    {
        CheckBox ckh_bp = chk;

        SharedPreferences.Editor edt=pref.edit();
        if (!ckh_bp.isChecked()) {
            ckh_bp.setChecked(true);
            Toast.makeText(MRA_MonitorSetting.this, "Notification is muted", Toast.LENGTH_LONG).show();
            edt.putBoolean(MRA_MUTE_NOTIFICATION,true);

        } else {
            ckh_bp.setChecked(false);
            Toast.makeText(MRA_MonitorSetting.this, "Notification is unmuted", Toast.LENGTH_LONG).show();
            edt.putBoolean(MRA_MUTE_NOTIFICATION,false);
        }
        edt.commit();
    }
    private void wipe_all_data(){

        db_mr.delete_all_data();
        db.delete_module_wise_setting_module_id(module_id);

        ConstData.WIPE_DATA_FROM_SERVER(sMemberId,"MR",this);

        Intent Intenet_mr = new Intent(MRA_MonitorSetting.this, MRA_Welcomeactivity.class);
        Intenet_mr.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intenet_mr.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(Intenet_mr);
        finish();
    }


}



package viroopa.com.medikart.bpmonitor;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import viroopa.com.medikart.helper.SqliteBPHandler;


/**
 * Created by Administrator on 03/Sep/2015.
 */

public class BPA_MonitorSetting extends AppCompatActivity implements OnItemClickListener,
        SettingListwithRadiodialog.OnRadioButtonSelectListener {

    private static final String TAG = "BPA_MonitorSetting";

    String get_def_site;
    String get_def_pos;
    private TextView def_site_txt,def_pos_txt,def_wight_unit_txt;
    String setting_last_enterd_values="";
    String setting_weight_unit="";
    private SqliteBPHandler db_bp;
    private CheckBox input_chk_lastWeightf;
    AppController globalVariable;
    private String formatted_today_date="";
    private String sMemberId;
    private ListView lv;
    private String getSelectedRelationshipId="";
    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    private String array_position[];
    private String array_positiontype[] ;
    private String array_weight_unit[] ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db_bp = SqliteBPHandler.getInstance(this);
        array_position = getResources().getStringArray(R.array.array_position);
        array_positiontype = getResources().getStringArray(R.array.array_positiontype);
        array_weight_unit = getResources().getStringArray(R.array.array_weight_unit);

        setContentView(R.layout.main_setting);
        globalVariable = (AppController) getApplicationContext();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        getIntenet();


        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();

        objects.add(new ContentItem("GENERAL", ""));
        if (get_def_site != null)
        {
            if (!get_def_site.equals("false")){
                objects.add(new ContentItem("Defualt Site", get_def_site));
            } else

            objects.add(new ContentItem("Defualt Site", "Select default reading site"));

        }else
        {
            objects.add(new ContentItem("Defualt Site", "Select default reading site"));
        }

        if (get_def_pos != null)
        {
            if (!get_def_pos.equals("false")){
                objects.add(new ContentItem("Default Position", get_def_pos));
            } else

                objects.add(new ContentItem("Default Position", "Select default reading position"));

        }else
        {
            objects.add(new ContentItem("Default Position", "Select default reading position"));
        }

        if (setting_weight_unit != null)
        {
            if (!setting_weight_unit.equals("false")){
                objects.add(new ContentItem("Select Weight Unit", setting_weight_unit));
            } else

                objects.add(new ContentItem("Select Weight Unit", "Select default weight unit"));

        }else
        {
            objects.add(new ContentItem("Select Weight Unit", "Select default weight unit"));
        }



        objects.add(new ContentItem("Use Last Entered Data", "Show last entered data while adding new entry"));
       /* objects.add(new ContentItem("HISTORY", ""));
        objects.add(new ContentItem("History Length", "Maximum Number of entries to show in history tab"));
        objects.add(new ContentItem("TOOLS", ""));
        objects.add(new ContentItem("Wipe Data", ""));*/


        MyAdapter adapter = new MyAdapter(this, objects);

        lv = (ListView) findViewById(R.id.listChart);


        lv.setAdapter(adapter);
        lv.setItemsCanFocus(true);

        lv.setOnItemClickListener(this);
        input_chk_lastWeightf = (CheckBox) findViewById(R.id.cbBox);


    }




    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {

        CheckBox cbBuy = (CheckBox) v.findViewById(R.id.cbBox);
        switch (pos) {
            case 0:
                break;
            case 1:

                load_selected_data();
                defualtsite();

                break;
            case 2:
                load_selected_data();
                defualtposition();
                break;
            case 3:
                load_selected_data();
                defualtweight_unit();
                break;
            case 4:
                load_selected_data();
                check_last_enterd_data(cbBuy);
                break;
            case 8:
                f_bp_wipe_data();
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

    private void check_last_enterd_data(CheckBox chk) {
        CheckBox ckh_bp = chk;


        if (!ckh_bp.isChecked()) {
            ckh_bp.setChecked(true);
            db_bp.updateSettingData(sMemberId, getSelectedRelationshipId, "use_last_entered_values", "true", "1", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "use_last_entered_values", "true", "1", "BP_Monitor", formatted_today_date, "", "", "E");

        } else {
            ckh_bp.setChecked(false);
            db_bp.updateSettingData(sMemberId, getSelectedRelationshipId, "use_last_entered_values", "false", "1", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "use_last_entered_values", "false", "1", "BP_Monitor", formatted_today_date, "", "", "E");
        }

    }

    private void f_bp_wipe_data() {

        new AlertDialog.Builder(BPA_MonitorSetting.this)
                .setTitle("Wipe data")
                .setMessage("This operation will delete all stored entries.\n Continue?")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void f_bp_wipe_backup() {

        new AlertDialog.Builder(BPA_MonitorSetting.this)
                .setTitle("Wipe backups")
                .setMessage("This operation will delete all backup copies.\n Continue?")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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


                initialize_textview(position, holder.tvDesc);

                if (position == 4) {


                }


                if (position == 0 || position == 5 || position == 7) {
                    convertView.setBackgroundColor(Color.rgb(255, 153, 0));
                    convertView.setLayoutParams(new AbsListView.LayoutParams(lv.getWidth(), 38));
                    holder.tvName.setTextSize(12);
                    holder.tvName.setTextColor(Color.WHITE);


                }
                if (position == 4 ) {
                    CheckBox cbBuy = (CheckBox) convertView.findViewById(R.id.cbBox);
                    cbBuy.setVisibility(convertView.VISIBLE);
                    input_chk_lastWeightf = (CheckBox) convertView.findViewById(R.id.cbBox);
                    if (setting_last_enterd_values != null) {
                        if (setting_last_enterd_values.equals("true")) {
                            input_chk_lastWeightf.setChecked(true);
                        } else {
                            input_chk_lastWeightf.setChecked(false);
                        }
                    }

                }


            } else {
                holder = (ViewHolder) convertView.getTag();
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


    private void defualtposition() {
        SettingListwithRadiodialog myDiag = SettingListwithRadiodialog.newInstance(array_positiontype,"selectPositionType","Select Default Position Type", Arrays.asList(array_positiontype).indexOf(get_def_pos));
        myDiag.show(getFragmentManager(), "Diag");

    }

    private void defualtsite() {

        SettingListwithRadiodialog myDiag = SettingListwithRadiodialog.newInstance(array_position,"selectPosition","Select Default Position", Arrays.asList(array_position).indexOf(get_def_site));
        myDiag.show(getFragmentManager(), "Diag");



    }

    private void defualtweight_unit() {

        SettingListwithRadiodialog myDiag = SettingListwithRadiodialog.newInstance(array_weight_unit,"selectWeight","Select Weight Unit", Arrays.asList(array_weight_unit).indexOf(setting_weight_unit));
        myDiag.show(getFragmentManager(), "Diag");



    }


    private void getIntenet() {
        String setting_name = "";


        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
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

        Cursor cursor_session = db_bp.getAllSetting_datat(getSelectedRelationshipId, sMemberId, "1");

        if (!cursor_session.moveToFirst()) {

            db_bp.InsertSettingData(sMemberId, getSelectedRelationshipId, "default_site", "false", "1", "BP_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "default_site", "false", "1", "BP_Monitor", formatted_today_date, "", "", "A");

            db_bp.InsertSettingData(sMemberId, getSelectedRelationshipId, "default_position", "false", "1", "BP_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "default_position", "false", "1", "BP_Monitor", formatted_today_date, "", "", "A");

            db_bp.InsertSettingData(sMemberId, getSelectedRelationshipId, "weight_unit", "kg", "1", "BP_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "weight_unit", "kg", "1", "BP_Monitor", formatted_today_date, "", "", "A");

            db_bp.InsertSettingData(sMemberId, getSelectedRelationshipId, "use_last_entered_values", "false", "1", "BP_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "use_last_entered_values", "false", "1", "BP_Monitor", formatted_today_date, "", "", "A");

            db_bp.InsertSettingData(sMemberId, getSelectedRelationshipId, "history_length", "false", "1", "BP_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "history_length", "false", "1", "BP_Monitor", formatted_today_date, "", "", "A");

        } else {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("default_site")) {
                    get_def_site = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("default_position")) {
                    get_def_pos = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("weight_unit")) {
                    setting_weight_unit = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("use_last_entered_values")) {
                    setting_last_enterd_values = cursor_session.getString(cursor_session.getColumnIndex("value"));
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

            id = db_bp.getSetting_id(relation_ship_id, member_id, module_id, setting_name);
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

            db_bp.InsertSyncTable(I_Type, Controller, Parameter,
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
        Cursor cursor_session = db_bp.getAllSetting_datat(getSelectedRelationshipId, sMemberId, "1");

        if (cursor_session.moveToFirst()) {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("default_site")) {
                    get_def_site = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("default_position")) {
                    get_def_pos = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("weight_unit")) {
                    setting_weight_unit = ConstData.getValueOrDefault( cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("use_last_entered_values")) {
                    setting_last_enterd_values = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }


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

            case "selectWeight":
                def_wight_unit_txt.setText(array_weight_unit[value]);
                db_bp.updateSettingData(sMemberId, getSelectedRelationshipId, "weight_unit", array_weight_unit[value], "1", formatted_today_date);
                SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "weight_unit", array_weight_unit[value], "1", "BP_Monitor", formatted_today_date, "", "", "E");

                break;

            case "selectPosition":
                def_site_txt.setText(array_position[value]);
                db_bp.updateSettingData(sMemberId, getSelectedRelationshipId, "default_site", array_position[value], "1", formatted_today_date);
                SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "default_site", array_position[value], "1", "BP_Monitor", formatted_today_date, "", "", "E");

                break;

            case "selectPositionType":

                def_pos_txt.setText(array_positiontype[value]);
                db_bp.updateSettingData(sMemberId, getSelectedRelationshipId, "default_position", array_positiontype[value], "1", formatted_today_date);
                SavedataToServerSettingEntries(sMemberId, getSelectedRelationshipId, "default_position", array_positiontype[value], "1", "BP_Monitor", formatted_today_date, "", "", "E");


                break;
        }


    }
    private void initialize_textview(int i,TextView tv) {
        if (i == 1) {
            def_site_txt = tv;
        } else if (i == 2) {
            def_pos_txt = tv;
        } else if (i == 3) {
            def_wight_unit_txt = tv;
        }

    }


    }



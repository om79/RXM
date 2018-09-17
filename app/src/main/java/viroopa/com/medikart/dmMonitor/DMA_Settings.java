package viroopa.com.medikart.dmMonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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


public class DMA_Settings extends AppCompatActivity implements AdapterView.OnItemClickListener,
                                                   SettingListwithRadiodialog.OnRadioButtonSelectListener
{
    ArrayList<ContentItem> objects = new ArrayList<ContentItem>();
    private String sMemberId;
    AppController globalVariable;
    private ListView lv;
    private TextView def_condition_txt,def_weight_txt,def_g_unit_txt;
    private String setting_weight_unit="",setting_def_condition="",setting_last_enterd_values="",setting_def_glucose_unit="";
    private SQLiteHandler db ;
    private    String[] array_category;
    private String array_weight_unit[] ;
    private String[] arrayglucose_unit;
    private String SelectedRelationshipId;
    private String formatted_today_date;
    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dma__settings);
        db = SQLiteHandler.getInstance(this);
        array_category = getResources().getStringArray(R.array.array_category);
        array_weight_unit = getResources().getStringArray(R.array.array_weight_unit);
        arrayglucose_unit=getResources().getStringArray(R.array.dm_Unit);

        globalVariable = (AppController) getApplicationContext();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);


        objects.add(new ContentItem("GENERAL", ""));
        objects.add(new ContentItem("Select Glucose Unit", "Select default Glucose unit"));
        objects.add(new ContentItem("Select Weight Unit", "Select default weight unit"));
        objects.add(new ContentItem("Default Condition", "Select Default Condition"));
        objects.add(new ContentItem("Use Last Entered Data", "Show last entered data while adding new entry"));

       /* objects.add(new ContentItem("HISTORY", ""));
        objects.add(new ContentItem("History Length", "Maximum Number of entries to show in history tab"));
        objects.add(new ContentItem("TOOLS", ""));
        objects.add(new ContentItem("Wipe Data", ""));*/
        getIntenet();


        MyAdapter adapter = new MyAdapter(this, objects);

        lv = (ListView) findViewById(R.id.listChart);


        lv.setAdapter(adapter);
        lv.setItemsCanFocus(true);

        lv.setOnItemClickListener(this);



    }
    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {

        CheckBox cbBuy = (CheckBox) v.findViewById(R.id.cbBox);
        switch (pos) {
            case 1:
                load_selected_data();
                SettingListwithRadiodialog mygUnitDiag = SettingListwithRadiodialog.newInstance(arrayglucose_unit,"selectGunit","Select Default Glucose Unit", Arrays.asList(arrayglucose_unit).indexOf(setting_def_glucose_unit));
                mygUnitDiag.show(getFragmentManager(), "Diag");
                break;
            case 2:
                load_selected_data();
                SettingListwithRadiodialog myWeightDiag = SettingListwithRadiodialog.newInstance(array_weight_unit,"selectWeight","Select Default Position", Arrays.asList(array_weight_unit).indexOf(setting_weight_unit));
                myWeightDiag.show(getFragmentManager(), "Diag");
                break;
            case 3:
                load_selected_data();
                SettingListwithRadiodialog myDiag = SettingListwithRadiodialog.newInstance(array_category,"selectCondition","Select Default Condition", Arrays.asList(array_category).indexOf(setting_def_condition));
                myDiag.show(getFragmentManager(), "Diag");
                break;
            case 4:
                load_selected_data();
                check_last_enterd_data(cbBuy);
                break;


        }
    }

    private void getIntenet() {
        String setting_name = "";


        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        if (globalVariable.getRealationshipId() != null) {
            SelectedRelationshipId = globalVariable.getRealationshipId();
        } else {
            SelectedRelationshipId = "8";
        }

        try {

            formatted_today_date = (dateFormat_query.format(current_date));

        } catch (Exception e) {

        }

        Cursor cursor_session = db.getAllSetting_data(SelectedRelationshipId, sMemberId, "2");

        if (!cursor_session.moveToFirst()) {



            db.InsertSettingData(sMemberId, SelectedRelationshipId, "dm_weight_unit", "kg", "2", "DM_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, SelectedRelationshipId, "dm_weight_unit", "kg", "2", "DM_Monitor", formatted_today_date, "", "", "A");


            db.InsertSettingData(sMemberId, SelectedRelationshipId, "dm_def_condition", "false", "2", "DM_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, SelectedRelationshipId, "dm_def_condition", "false", "2", "DM_Monitor", formatted_today_date, "", "", "A");

            db.InsertSettingData(sMemberId, SelectedRelationshipId, "dm_def_glucose_unit", "mg/dl", "2", "DM_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, SelectedRelationshipId, "dm_def_glucose_unit", "mg/dl", "2", "DM_Monitor", formatted_today_date, "", "", "A");


            db.InsertSettingData(sMemberId, SelectedRelationshipId, "dm_use_last_entered_values", "false", "2", "DM_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, SelectedRelationshipId, "dm_use_last_entered_values", "false", "2", "DM_Monitor", formatted_today_date, "", "", "A");

            db.InsertSettingData(sMemberId, SelectedRelationshipId, "dm_history_length", "false", "2", "DM_Monitor", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, SelectedRelationshipId, "dm_history_length", "false", "2", "DM_Monitor", formatted_today_date, "", "", "A");

        } else {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("dm_weight_unit")) {
                    setting_weight_unit = ConstData.getValueOrDefault( cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("dm_def_condition")) {
                    setting_def_condition = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("dm_use_last_entered_values")) {
                    setting_last_enterd_values = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("dm_def_glucose_unit")) {
                    setting_def_glucose_unit =ConstData.getValueOrDefault( cursor_session.getString(cursor_session.getColumnIndex("value")), "");
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
        Cursor cursor_session = db.getAllSetting_data(SelectedRelationshipId, sMemberId, "2");

        if (cursor_session.moveToFirst()) {
            do {



                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("dm_weight_unit")) {
                    setting_weight_unit = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("dm_def_condition")) {
                    setting_def_condition = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("dm_use_last_entered_values")) {
                    setting_last_enterd_values = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("dm_def_glucose_unit")) {
                    setting_def_glucose_unit = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }


            } while (cursor_session.moveToNext());
        }
    }


    private void check_last_enterd_data(CheckBox chk) {
        CheckBox ckh_bp = chk;


        if (!ckh_bp.isChecked()) {
            ckh_bp.setChecked(true);
            db.updateSettingData(sMemberId, SelectedRelationshipId, "dm_use_last_entered_values", "true", "2", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, SelectedRelationshipId, "use_last_entered_values", "true", "2", "DM_Monitor", formatted_today_date, "", "", "E");

        } else {
            ckh_bp.setChecked(false);
            db.updateSettingData(sMemberId, SelectedRelationshipId, "dm_use_last_entered_values", "false", "2", formatted_today_date);
            SavedataToServerSettingEntries(sMemberId, SelectedRelationshipId, "use_last_entered_values", "false", "2", "DM_Monitor", formatted_today_date, "", "", "E");
        }

    }
    @Override
    public void onRadioButtonSelect(int value,String sClass) {


        switch (sClass) {
            case"selectGunit":
                def_g_unit_txt.setText(arrayglucose_unit[value]);
                db.updateSettingData(sMemberId, SelectedRelationshipId, "dm_def_glucose_unit", arrayglucose_unit[value], "2", formatted_today_date);
                SavedataToServerSettingEntries(sMemberId, SelectedRelationshipId, "dm_def_glucose_unit", arrayglucose_unit[value], "2", "DM_Monitor", formatted_today_date, "", "", "E");

                break;

            case "selectWeight":
                def_weight_txt.setText(array_weight_unit[value]);
                db.updateSettingData(sMemberId, SelectedRelationshipId, "dm_weight_unit", array_weight_unit[value], "2", formatted_today_date);
                SavedataToServerSettingEntries(sMemberId, SelectedRelationshipId, "dm_weight_unit", array_weight_unit[value], "2", "DM_Monitor", formatted_today_date, "", "", "E");

                break;

            case "selectCondition":
                def_condition_txt.setText(array_category[value]);
                db.updateSettingData(sMemberId, SelectedRelationshipId, "dm_def_condition", array_category[value], "2", formatted_today_date);
                SavedataToServerSettingEntries(sMemberId, SelectedRelationshipId, "dm_def_condition", array_category[value], "2", "DM_Monitor", formatted_today_date, "", "", "E");

                break;


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


                if (position == 0 || position == 5 || position == 7) {
                    convertView.setBackgroundColor(Color.rgb(255, 153, 0));
                    convertView.setLayoutParams(new LinearLayout.LayoutParams(lv.getWidth(), 40));
                    holder.tvName.setTextSize(10);
                    holder.tvName.setTextColor(Color.WHITE);


                }
                if(position==4)
                {
                    CheckBox cbLastData = (CheckBox) convertView.findViewById(R.id.cbBox);
                    cbLastData.setVisibility(convertView.VISIBLE);
                    if (setting_last_enterd_values != null) {
                        if (setting_last_enterd_values.equals("true")) {
                            cbLastData.setChecked(true);
                        } else {
                            cbLastData.setChecked(false);
                        }
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


    private class ContentItem {
        String name;
        String desc;

        public ContentItem(String n, String d) {
            name = n;
            desc = d;
        }
    }
    private void initialize_textview(int i,TextView tv) {
        if (i == 1) {
            def_g_unit_txt= tv;
            def_g_unit_txt.setText(setting_def_glucose_unit);
        }
        if (i == 2) {
            def_weight_txt = tv;
            def_weight_txt.setText(setting_weight_unit);
        } else if (i == 3) {
            def_condition_txt = tv;
            if(!setting_def_condition.equals("false")) {
                def_condition_txt.setText(setting_def_condition);
            }
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
}

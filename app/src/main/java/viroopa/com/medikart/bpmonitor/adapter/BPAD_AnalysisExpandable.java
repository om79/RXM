package viroopa.com.medikart.bpmonitor.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.nfc.FormatException;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import viroopa.com.medikart.bpmonitor.BPA_AnalysisDisplayActivity;
import viroopa.com.medikart.bpmonitor.BPA_NewEntry;
import viroopa.com.medikart.bpmonitor.BPA_WelcomeActivity;
import viroopa.com.medikart.bpmonitor.Module.BPM_Analysis_Heading;
import viroopa.com.medikart.bpmonitor.Module.BPM_Analysis_Item_Detail;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.helper.SqliteBPHandler;


public class BPAD_AnalysisExpandable extends BaseExpandableListAdapter {

    private List<BPM_Analysis_Heading > catList;
//    private int itemLayoutId;
//    private int groupLayoutId;
    private Context ctx;
    private LinearLayout lnrItemDetail, lnrsecond;
    private SqliteBPHandler db_bp;
    private Integer BPid;
    private String sMemberId;
    private String weight_unit = "";
    private Integer getSelectedRelationshipId;
    private String FilterCondition = "";
    private String FilterAMPMCondition = "";
    private Integer Bpid;
    private AppController globalVariable;
    Activity act;
    Date current_date = Calendar.getInstance().getTime();

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");

    public BPAD_AnalysisExpandable(List<BPM_Analysis_Heading> catList, Context ctx, Activity act) {

        this.catList = catList;
        this.ctx = ctx;
        this.act = act;
        globalVariable = (AppController) ctx.getApplicationContext();
        db_bp = new SqliteBPHandler(ctx);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return catList.get(groupPosition).getItemList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return catList.get(groupPosition).getItemList().get(childPosition).hashCode();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        View v = convertView;


        v = null;

        if (groupPosition == 0) {
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.bp_analysis_weekly_blank, parent, false);
            }
            lnrItemDetail = (LinearLayout) v.findViewById(R.id.lnrweeklyBlank);

            BPM_Analysis_Item_Detail det = catList.get(groupPosition).getItemList().get
                    (childPosition);
            FilterCondition = det.getFilterCondition();

            FilterAMPMCondition = det.getFilterAMPMCondition();
            try {


                show_all_Analysistop(FilterCondition, FilterAMPMCondition,det.getSetting_weight_unit(),det.getToDate());

            } catch (Exception e) {
                String s = String.valueOf(e);
            }


        }


        if (groupPosition == 1) {
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.bp_weekly_detail_header, parent, false);
            }
            lnrsecond = (LinearLayout) v.findViewById(R.id.lnrItemDetail);


            BPM_Analysis_Item_Detail det = catList.get(groupPosition).getItemList().get
                    (childPosition);
            FilterCondition = det.getFilterCondition();
            FilterAMPMCondition = det.getFilterAMPMCondition();
            try {


                show_all_Analysis(FilterCondition, FilterAMPMCondition,det.getSetting_weight_unit(),det.getToDate());

            } catch (Exception e) {

            }


        }
        return v;
    }


    private void getIntenet() {

        SharedPreferences pref = ctx.getSharedPreferences("Global", Context.MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

        getSelectedRelationshipId = pref.getInt("RelationshipId", 0);

        if (globalVariable.getRealationshipId() != null) {
            getSelectedRelationshipId = Integer.parseInt(globalVariable.getRealationshipId());
        } else {
            getSelectedRelationshipId = 8;
        }


    }

    private void show_all_Analysis(String FilterCondition, String FilterAMPMCondition,String setting_weight_unit,String toDate) {
        try {


            getIntenet();
            lnrsecond.removeAllViews();
            int i = 1;


            String Filter = FilterCondition;
            String AMPM = FilterAMPMCondition;




            // Prakash K Bhandary
            // Why object is created inside the loop ???
            /*
            try {
                if (cursor_session.moveToFirst()) {
                    do {

                    } while (cursor_session.moveToNext());
                }
            } catch (Exception e) {
                //Log.d(TAG, "Error while trying to get posts from database");
            } finally {
                if (cursor_session != null && !cursor_session.isClosed()) {
                    cursor_session.close();
                }
            }
            */
            Integer Hour = 0;
            Integer minutes = 0;
            String sAMPM = "";
            Integer setBpid = 0;
            String sHour = "";
            String sminutes = "";
            String Weightunit="";

            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mLinearView = inflater.inflate(R.layout.bp_weekly_detail, null);

             RelativeLayout relweeklydetail=null;
             TextView txtDate=null;
             TextView txtTime=null;
             TextView txtSystolic=null;
             TextView txtDiastolic=null;
             TextView txtPulse =null;
             TextView txtWeight=null;
             TextView txtLocation=null;
             TextView txtbtDate =null;
             TextView txtbtTime=null;
             TextView txtbtWeightunit=null;
             TextView txtbtPosition =null;
             ImageView first_image_circle=null;


            Cursor cursor_session = db_bp.show_BPAnalysisFilterData_Chart(Integer.parseInt(sMemberId),
                    getSelectedRelationshipId, Filter, AMPM,toDate);
            if ((cursor_session != null) && (cursor_session.getCount() > 0)) {
                if (cursor_session.moveToFirst()) {
                    do {

                        mLinearView = inflater.inflate(R.layout.bp_weekly_detail, null);
                        relweeklydetail= (RelativeLayout) mLinearView.findViewById(R.id.relweeklydetail);
                        txtDate = (TextView) mLinearView.findViewById(R.id.txtDate);
                        txtTime = (TextView) mLinearView.findViewById(R.id.txtTime);
                        txtSystolic = (TextView) mLinearView.findViewById(R.id.txtSystolic);
                        txtDiastolic = (TextView) mLinearView.findViewById(R.id.txtDiastolic);
                        txtPulse = (TextView) mLinearView.findViewById(R.id.txtPulse);
                        txtWeight = (TextView) mLinearView.findViewById(R.id.txtWeight);
                        txtLocation = (TextView) mLinearView.findViewById(R.id.txtLocation);
                        txtbtDate = (TextView) mLinearView.findViewById(R.id.txtbtDate);
                        txtbtTime = (TextView) mLinearView.findViewById(R.id.txtbtTime);
                        txtbtWeightunit = (TextView) mLinearView.findViewById(R.id.txtbtWeightunit);
                        txtbtPosition = (TextView) mLinearView.findViewById(R.id.txtbtPosition);
                        first_image_circle = (ImageView) mLinearView.findViewById(R.id.first_image_circle);


                        try {
                            setBpid = Integer.parseInt(cursor_session.getString(cursor_session
                                    .getColumnIndex("_id")));
                            //go on as normal
                        } catch (NumberFormatException e) {
                            //handle error
                            setBpid = 0;
                        }

                        relweeklydetail.setTag(R.id.key_product_name, String.valueOf(setBpid));


                        relweeklydetail.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                String getBpid = (String) view.getTag(R.id.key_product_name);
                                Bpid = Integer.parseInt(getBpid);
                                data_details_display();

                            }
                        });

                        Weightunit = cursor_session.getString(cursor_session.getColumnIndex("weight_unit"));

                        txtDate.setText(cursor_session.getString(cursor_session.getColumnIndex("bpdate")));
                        txtTime.setText(cursor_session.getString(cursor_session.getColumnIndex("bp_time")));
                        txtSystolic.setText(cursor_session.getString(cursor_session.getColumnIndex("systolic")));
                        txtDiastolic.setText(cursor_session.getString(cursor_session.getColumnIndex("diastolic")));
                        txtPulse.setText(cursor_session.getString(cursor_session.getColumnIndex("pulse")));

                        String t=cursor_session.getString(cursor_session.getColumnIndex("bp_date"));

                        try {
                            current_date = sdf.parse(cursor_session.getString(cursor_session.getColumnIndex("bp_time")));
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                            txtTime.setText(sdfs.format(current_date));


                        if (setting_weight_unit.equals("lbs")) {
                            //txtWeight.setText(cursor_session.getString(cursor_session.getColumnIndex("kg")));
                            txtWeight.setText(cursor_session.getString(cursor_session.getColumnIndex("lb")));
                        } else {

                            txtWeight.setText(cursor_session.getString(cursor_session.getColumnIndex("kg")));

                        }

                        //String sAMPM=cursor_session.getString(cursor_session.getColumnIndex("AMPM"));
                        txtLocation.setText(cursor_session.getString(cursor_session.getColumnIndex("body_part")));
                        txtbtDate.setText(cursor_session.getString(cursor_session.getColumnIndex("month")));
                        //txtbtTime.setText(sAMPM);
                        txtbtWeightunit.setText(setting_weight_unit);
                        txtbtPosition.setText(cursor_session.getString(cursor_session.getColumnIndex("position")));

                        mLinearView.setBackgroundColor(i % 2 == 0 ? Color.parseColor("#dfe8f9") : Color.WHITE);

                        first_image_circle.setImageResource(i % 2 == 0 ? R.drawable.usecircle : R.drawable.circle_2_3);
                        i++;
                        lnrsecond.addView(mLinearView);

                    } while (cursor_session.moveToNext());


                }
                cursor_session.close();
            }


        } catch (Exception e) {
             String.valueOf(e);
        }

    }


    private void show_all_Analysistop(String FilterCondition, String FilterAMPMCondition,String setting_weight_unit,String todate) {
        try {

            String Weightunit="";
            getIntenet();

            LayoutInflater inflater = null;

            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.bp_analysis_weekly, null);
            final TextView sys_max = (TextView) mLinearView.findViewById(R.id.sys_max);
            final TextView sys_min = (TextView) mLinearView.findViewById(R.id.sys_min);
            final TextView avg_sys = (TextView) mLinearView.findViewById(R.id.avg_sys);
            final TextView dia_max = (TextView) mLinearView.findViewById(R.id.dia_max);
            final TextView dia_min = (TextView) mLinearView.findViewById(R.id.dia_min);
            final TextView avg_dia = (TextView) mLinearView.findViewById(R.id.avg_dia);
            final TextView max_pulse = (TextView) mLinearView.findViewById(R.id.max_pulse);
            final TextView min_pulse = (TextView) mLinearView.findViewById(R.id.min_pulse);
            final TextView avg_pulse = (TextView) mLinearView.findViewById(R.id.avg_pulse);
            final TextView max_wt = (TextView) mLinearView.findViewById(R.id.max_wt);
            final TextView min_wt = (TextView) mLinearView.findViewById(R.id.min_wt);
            final TextView avg_wt = (TextView) mLinearView.findViewById(R.id.avg_wt);
            final TextView con_weight = (TextView) mLinearView.findViewById(R.id.weight);



            lnrItemDetail.removeAllViews();
            String Filter = FilterCondition;
            Cursor cursor_session = db_bp.show_BPMinMaxAnalysisFilterData_Chartnew(Integer.parseInt(sMemberId), getSelectedRelationshipId, Filter, FilterAMPMCondition,todate);

            if ((cursor_session != null) && (cursor_session.getCount() > 0)) {
                if (cursor_session.moveToFirst()) {
                    do {

                        sys_max.setText(cursor_session.getString(cursor_session.getColumnIndex("sys_max")));
                        sys_min.setText(cursor_session.getString(cursor_session.getColumnIndex("sys_min")));
                        avg_sys.setText(cursor_session.getString(cursor_session.getColumnIndex("avg_sys")));
                        dia_max.setText(cursor_session.getString(cursor_session.getColumnIndex("dia_max")));
                        dia_min.setText(cursor_session.getString(cursor_session.getColumnIndex("dia_min")));
                        avg_dia.setText(cursor_session.getString(cursor_session.getColumnIndex("avg_dia")));
                        max_pulse.setText(cursor_session.getString(cursor_session.getColumnIndex("max_pulse")));
                        min_pulse.setText(cursor_session.getString(cursor_session.getColumnIndex("min_pulse")));
                        avg_pulse.setText(cursor_session.getString(cursor_session.getColumnIndex("avg_pulse")));


                        con_weight.setText("Weight (" + setting_weight_unit + ")");


                        if (setting_weight_unit.equals("lbs")) {

                            max_wt.setText(cursor_session.getString(cursor_session.getColumnIndex("max_wtlb")));
                            min_wt.setText(cursor_session.getString(cursor_session.getColumnIndex("min_wtlb")));
                            avg_wt.setText(cursor_session.getString(cursor_session.getColumnIndex("avg_wtlb")));

                        } else {

                            max_wt.setText(cursor_session.getString(cursor_session.getColumnIndex("max_wt")));
                            min_wt.setText(cursor_session.getString(cursor_session.getColumnIndex("min_wt")));
                            avg_wt.setText(cursor_session.getString(cursor_session.getColumnIndex("avg_wt")));

                        }

                        lnrItemDetail.addView(mLinearView);

                    } while (cursor_session.moveToNext());


                }
                cursor_session.close();
            }

        } catch (Exception e) {
            String.valueOf(e);
        }


    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = catList.get(groupPosition).getItemList().size();
        System.out.println("Child for group [" + groupPosition + "] is [" + size + "]");
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return catList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return catList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return catList.get(groupPosition).hashCode();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View v = convertView;
        BPM_Analysis_Heading cat = catList.get(groupPosition);
        weight_unit = cat.getWeight_unit();
        if (cat.getHeadingName().equals("Weight")) {
            v = null;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.bp_lastreadingweight, parent, false);


                final LinearLayout lnrweightLastReading = (LinearLayout) v.findViewById(R.id.lnrweight);
                final TextView txtWeight_Detail = (TextView) v.findViewById(R.id.txtWeight_Detail);
                final TextView txtBodyLocation = (TextView) v.findViewById(R.id.txtBodyLocation);
                final TextView txtBodyPosition = (TextView) v.findViewById(R.id.txtBodyPosition);


                txtWeight_Detail.setText(cat.getMin().toString());
                txtBodyLocation.setText(cat.getMax().toString());
                txtBodyPosition.setText(cat.getAvg().toString());
                lnrweightLastReading.setClickable(false);
                lnrweightLastReading.setEnabled(false);
                v.setClickable(false);
                v.setEnabled(false);

            }

        } else if (cat.getHeadingName().equals("Last Reading")) {
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.bp_lastreading, parent, false);

                final LinearLayout lnrLastReading = (LinearLayout) v.findViewById(R.id.lnrLastReading);

                final TextView heading = (TextView) v.findViewById(R.id.heading);
                final TextView edtvalue = (TextView) v.findViewById(R.id.edtvalue);
                final TextView edtvalueCondi = (TextView) v.findViewById(R.id.edtvalueCondi);

                if (cat.getMin().equals("Blank")) {

                    lnrLastReading.setVisibility(View.GONE);


                } else {
                    edtvalue.setTextColor(Color.parseColor("#f38630"));//"#f38630"
                    if (cat.getMin().equals("Systolic")) {
                        edtvalueCondi.setTextColor(Color.parseColor("#6cd720"));//"#f38630"
                    } else if (cat.getMin().equals("Diastolic")) {
                        edtvalueCondi.setTextColor(Color.parseColor("#bdbdbd"));
                    } else {
                        edtvalueCondi.setTextColor(Color.parseColor("#ffffff"));
                    }

                    heading.setText(cat.getMin());
                    edtvalue.setText(cat.getMax());
                    edtvalueCondi.setText(cat.getAvg());
                    getIntenet();

                    Cursor cursor_session = db_bp.getLastReadingBPMonitarData(sMemberId, getSelectedRelationshipId);
                    int i = 1;

                    Integer setBpid = 0;
                    if ((cursor_session != null) && (cursor_session.getCount() > 0)) {
                        if (cursor_session.moveToFirst()) {
                            do {

                                try {
                                    setBpid = Integer.parseInt(cursor_session.getString(cursor_session.getColumnIndex("_id")));
                                    lnrLastReading.setTag(R.id.key_product_name, String.valueOf(setBpid));
                                    //go on as normal
                                } catch (NumberFormatException e) {
                                    //handle error
                                    setBpid = 0;
                                }

                            } while (cursor_session.moveToNext());
                        }
                    }

                    lnrLastReading.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            String getBpid = (String) view.getTag(R.id.key_product_name);
                            Bpid = Integer.parseInt(getBpid);
                            edit_data();

                        }
                    });

                }
            }

        } else {
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.bp_analysis_weekly_tophed, parent, false);
            }



            final TextView txttitle = (TextView) v.findViewById(R.id.txttitle);
            final TextView max = (TextView) v.findViewById(R.id.max);
            final TextView min = (TextView) v.findViewById(R.id.min);
            final TextView avg = (TextView) v.findViewById(R.id.avg);
            final ImageView avgRightImg = (ImageView) v.findViewById(R.id.imgavgright);

            if (groupPosition == 1) {
                v.setBackgroundColor(Color.parseColor("#f38630"));//("#df6301");
                txttitle.setTextColor(Color.WHITE);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

                avgRightImg.setImageResource(R.drawable.arrow_right_white);


            } else {
                v.setBackgroundColor(Color.WHITE);//("#df6301");
                avgRightImg.setVisibility(View.VISIBLE);
            }


            txttitle.setText(cat.getHeadingName());
            max.setText(cat.getMax());
            min.setText(cat.getMin());
            avg.setText(cat.getAvg());
        }

        return v;

    }

    private void delete_data_dialog() {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        final View dialogview = inflater.inflate(R.layout.bp_delete_confirmation, null);

        final Button Deletebtn = (Button) dialogview.findViewById(R.id.btnDelete);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);

        builder.setView(dialogview);
        final Dialog dlg = builder.create();

        Deletebtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dlg.dismiss();
                delete_data(Bpid);

            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        dlg.show();

    }

    private void InsertintosyncTabledata(Integer BPid) {

        try {
            Cursor cursor = db_bp.getAllBPMonitarDataOnBpID(BPid);
            if (cursor.moveToFirst()) {
                do {


                    String sMemberId = cursor.getString(cursor.getColumnIndex("member_id"));
                    String sBodyPartId = cursor.getString(cursor.getColumnIndex("body_part"));
                    String sPositionId = cursor.getString(cursor.getColumnIndex("position"));
                    String systolic = cursor.getString(cursor.getColumnIndex("systolic"));
                    String diastolic = cursor.getString(cursor.getColumnIndex("diastolic"));
                    String pulse = cursor.getString(cursor.getColumnIndex("pulse"));
                    String weight = cursor.getString(cursor.getColumnIndex("weight"));
                    String weight_unit = cursor.getString(cursor.getColumnIndex("weight_unit"));
                    String arrthythmia = cursor.getString(cursor.getColumnIndex("arrthythmia"));
                    String comments = cursor.getString(cursor.getColumnIndex("comments"));
                    String bp_date = cursor.getString(cursor.getColumnIndex("bp_date"));

                    String bp_time = cursor.getString(cursor.getColumnIndex("bp_time"));
                    String bptimehr = cursor.getString(cursor.getColumnIndex("bptimehr"));
                    String lb = cursor.getString(cursor.getColumnIndex("lb"));
                    String kg = cursor.getString(cursor.getColumnIndex("kg"));
                    String Relationship_ID = cursor.getString(cursor.getColumnIndex("Relationship_ID"));
                    String IMEI_no = cursor.getString(cursor.getColumnIndex("IMEI_no"));
                    String SUUID = cursor.getString(cursor.getColumnIndex("UUID"));
                    try {


                        Map<String, String> params = new HashMap<String, String>();

                        params.put("user_id", sMemberId);
                        params.put("body_part", sBodyPartId);
                        params.put("position", sPositionId);
                        params.put("systolic", systolic);
                        params.put("diastolic", diastolic);
                        params.put("pulse", pulse);
                        params.put("weight", weight);
                        params.put("weight_unit", weight_unit);
                        params.put("arrthythmia", arrthythmia);
                        params.put("comments", comments);
                        params.put("bp_date", bp_date);
                        params.put("bp_time", bp_time);
                        params.put("bptimehr", sMemberId);
                        params.put("Relationship_ID", Relationship_ID);
                        params.put("lb", lb);
                        params.put("kg", kg);
                        params.put("IMEI", IMEI_no);
                        params.put("UUID", SUUID);

                        JSONObject jparams = new JSONObject(params);


                        String I_Type = "Post";
                        String Controller = AppConfig.URL_POST_ADDBPDATAJson;
                        String Parameter = "";
                        String JsonObject = String.valueOf(jparams);
                        String Created_Date = bp_date;

                        String F_KEY_UPLOAD_DOWNLOAD = "true";
                        String F_KEY_SYNCMEMBERID =sMemberId;
                        String F_KEY_MODULE_NAME = "BP";
                        String F_KEY_MODE = "D";
                        String F_KEY_ControllerName = "Monitor";
                        String F_KEY_MethodName = "BPMonitorBAL";
                        db_bp.InsertSyncTable(I_Type, Controller, Parameter,
                                JsonObject, Created_Date, SUUID, F_KEY_UPLOAD_DOWNLOAD, F_KEY_SYNCMEMBERID,
                                F_KEY_MODULE_NAME, F_KEY_MODE, F_KEY_ControllerName, F_KEY_MethodName,
                                Long.parseLong(IMEI_no));

                    } catch (Exception E) {

                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception E) {
            //f_alert_ok("Error","Error"+E.getMessage());
        }

    }

    private void delete_data(Integer BpId) {

        InsertintosyncTabledata(BpId);
        db_bp.deleteBPDataById(BpId);
        ((BPA_AnalysisDisplayActivity ) ctx).fillReading(FilterCondition);
        ((BPA_AnalysisDisplayActivity ) ctx).create_statistics_pie_chart("LastReading");
       // ((BPA_AnalysisDisplayActivity) ctx).showLinechart();


        new AlertDialog.Builder(ctx)
                .setTitle("Delete")
                .setMessage("Data Deleted Successfully")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //act.recreate();
                        dialog.dismiss();
                        Intent Intenet_change = new Intent(act,BPA_WelcomeActivity.class);
                        act.finish();
                        act.startActivity(Intenet_change);

                    }
                }).show();
    }


    private void edit_data() {

        //Intent intent_edit = new Intent(ctx, BPMonitorMain.class);
        Intent intent_edit = new Intent(ctx, BPA_NewEntry.class);
        intent_edit.putExtra("Bpid", String.valueOf(Bpid));

        ctx.startActivity(intent_edit);
        act.finish();



    }


    private void data_details_display() {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        final View dialogview = inflater.inflate(R.layout.bp_deatails, null);

        final Button Deletebtn = (Button) dialogview.findViewById(R.id.btnDelete);
        final Button editbtn = (Button) dialogview.findViewById(R.id.btnEdit);
        final Button closebtn = (Button) dialogview.findViewById(R.id.btncancel);

        final TextView Bloodpressuretxt = (TextView) dialogview.findViewById(R.id.txtbloodpressure);
        final TextView pulsetxt = (TextView) dialogview.findViewById(R.id.txtpulse);
        final TextView weighttxt = (TextView) dialogview.findViewById(R.id.txtweight);
        final TextView commenttxt = (TextView) dialogview.findViewById(R.id.txtcomment);
        final TextView dateOfBirth = (TextView) dialogview.findViewById(R.id.txtdateofbirth);

        builder.setView(dialogview);
        final Dialog dlg = builder.create();

        //db_bp = new SqliteBPHandler(ctx);
        Cursor cursor = db_bp.getAllBPMonitarDataOnBpID(Bpid);
        if (cursor.moveToFirst()) {
            do {
                String ed_weight = cursor.getString(cursor.getColumnIndex("weight"));
                String ed_diastolic = cursor.getString(cursor.getColumnIndex("diastolic"));
                String ed_pulse = cursor.getString(cursor.getColumnIndex("pulse"));
                String ed_comment = cursor.getString(cursor.getColumnIndex("comments"));
                String Systolic = cursor.getString(cursor.getColumnIndex("systolic"));
                String ed_dateOfBirth = cursor.getString(cursor.getColumnIndex("bp_date"));
                String ed_arrthythmia = cursor.getString(cursor.getColumnIndex("arrthythmia"));

                Integer MAP = ((2 *Integer.parseInt(ed_diastolic))+Integer.parseInt(Systolic))/ 3;


                Bloodpressuretxt.setText(MAP.toString());
                pulsetxt.setText(ed_pulse);
                weighttxt.setText(ed_weight);
                dateOfBirth.setText(ed_comment);
                commenttxt.setText(ed_dateOfBirth);


            } while (cursor.moveToNext());
        }
        cursor.close();


        Deletebtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dlg.dismiss();
                delete_data_dialog();

            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                edit_data();
            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        dlg.show();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

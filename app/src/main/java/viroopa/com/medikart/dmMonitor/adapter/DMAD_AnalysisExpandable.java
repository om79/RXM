package viroopa.com.medikart.dmMonitor.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.dmMonitor.DMA_AnalysisDisplayActivity;
import viroopa.com.medikart.dmMonitor.DMA_NewEntry;
import viroopa.com.medikart.dmMonitor.Module.DMM_AnalysisHeading;
import viroopa.com.medikart.dmMonitor.Module.DMM_AnalysisItemDetail;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteDMHandler;

public class DMAD_AnalysisExpandable extends BaseExpandableListAdapter {

    private List<DMM_AnalysisHeading> catList;
    private int itemLayoutId;
    private int groupLayoutId;
    private Context ctx;
    private LinearLayout lnrItemDetail, lnrsecond;
    private SqliteDMHandler db_dm;
    private SQLiteHandler db ;
    private String setting_weight_unit,setting_def_condition,setting_last_enterd_values,setting_def_glucose_unit;
    private Integer nMemberId,nRelationshipId;
    private SharedPreferences pref ;

    private  String FilterCondition="";
    private  String FilterAMPMCondition="";
    private Integer DMID;
    AppController globalVariable;
    Activity act;

    private SimpleDateFormat format_date_day = new SimpleDateFormat("dd");
    private SimpleDateFormat format_date_month = new SimpleDateFormat("MMM");

    private SimpleDateFormat format_time = new SimpleDateFormat("hh:mm");
    private SimpleDateFormat format_time_ampm = new SimpleDateFormat("a");

    private SimpleDateFormat format_date_save = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format_time_save = new SimpleDateFormat("HH:mm:ss");


    public DMAD_AnalysisExpandable(List<DMM_AnalysisHeading> catList, Context ctx, Activity act) {

        this.itemLayoutId = R.layout.dm_monitor_item;
        this.groupLayoutId = R.layout.dm_bs_detail;
        this.catList = catList;
        this.ctx = ctx;
        this.act=act;
        globalVariable = (AppController)ctx.getApplicationContext();
        db_dm=new SqliteDMHandler(ctx);
        db = SQLiteHandler.getInstance(ctx);

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
        v=null;

        if (groupPosition == 0) {
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.dm_monitor_analysis_weekly_blank, parent, false);
            }
            lnrItemDetail = (LinearLayout) v.findViewById(R.id.lnrweeklyBlank);

            DMM_AnalysisItemDetail det = catList.get(groupPosition).getItemList().get(childPosition);
            FilterCondition= det.getFilterCondition();
            FilterAMPMCondition=det.getFilterAMPMCondition();

            try
            {
                show_all_Analysistop(FilterCondition,FilterAMPMCondition,det.getToDate());
            }
            catch (Exception e) {
                String s=String.valueOf(e);
            }
        }

        if (groupPosition == 1) {
            if(v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.dm_monitor_weekly_detail_header, parent, false);
            }
            lnrsecond = (LinearLayout) v.findViewById(R.id.lnrItemDetail);

            DMM_AnalysisItemDetail det = catList.get(groupPosition).getItemList().get(childPosition);
            FilterCondition= det.getFilterCondition();
            FilterAMPMCondition=det.getFilterAMPMCondition();

            try
            {
                show_all_Analysis(FilterCondition,FilterAMPMCondition,det.getToDate());
            }
            catch (Exception e)
            {
                String s=String.valueOf(e);
            }
        }
        return v;
    }

    private void getIntenet()
    {
        pref = ctx.getSharedPreferences("Global", Context.MODE_PRIVATE);
        nMemberId = Integer.parseInt(pref.getString("memberid", ""));

        if(globalVariable.getRealationshipId()!=null)
        {
            nRelationshipId = Integer.parseInt(globalVariable.getRealationshipId());
        }
        else
        {
            nRelationshipId = 8;
        }

        get_defaults();
    }

    private void show_all_Analysis( String FilterCondition,String FilterAMPMCondition,String todate)
    {
        try
        {
            getIntenet();
            lnrsecond.removeAllViews();
            Cursor cursor_session = db_dm.show_DMAnalysisFilterData_Chart(nMemberId, nRelationshipId, FilterCondition, FilterAMPMCondition,todate);
            int i = 1;

            if ((cursor_session != null) || (cursor_session.getCount()> 0))
            {
                if (cursor_session.moveToFirst()) {
                    do {
                        LayoutInflater inflater = null;

                        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mLinearView = inflater.inflate(R.layout.dm_monitor_weekly_detail, null);

                        final RelativeLayout relweeklydetail = (RelativeLayout) mLinearView.findViewById(R.id.relweeklydetail);

                        final TextView txtDate = (TextView) mLinearView.findViewById(R.id.txtDate);
                        final TextView txtbtDate = (TextView) mLinearView.findViewById(R.id.txtbtDate);
                        final TextView txtTime = (TextView) mLinearView.findViewById(R.id.txtTime);
                        final TextView txtbtTime = (TextView) mLinearView.findViewById(R.id.txtbtTime);

                        final TextView txtBS = (TextView) mLinearView.findViewById(R.id.txtBS);
                        final TextView txtbtbsunit = (TextView) mLinearView.findViewById(R.id.txtbtbsunit);
                        final TextView txtWeight = (TextView) mLinearView.findViewById(R.id.txtWeight);
                        final TextView txtbtWeightunit = (TextView) mLinearView.findViewById(R.id.txtbtWeightunit);

                        final ImageView first_image_circle = (ImageView) mLinearView.findViewById(R.id.first_image_circle);

                        relweeklydetail.setTag(R.id.key_product_name, cursor_session.getString(cursor_session.getColumnIndex("DMID")));

                        relweeklydetail.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                String getDMID = (String) view.getTag(R.id.key_product_name);
                                DMID = Integer.parseInt(getDMID);
                                data_details_display();
                            }
                        });

                        String g_date = String.valueOf(cursor_session.getString(cursor_session.getColumnIndex("g_date")));
                        String g_time = String.valueOf(cursor_session.getString(cursor_session.getColumnIndex("g_time")));
                        String g_date_day="", g_date_month="", g_time_apmp="";

                        Date date_time ;

                        date_time = format_date_save.parse(g_date);
                        g_date_day = format_date_day.format(date_time);
                        g_date_month = format_date_month.format(date_time);
                        date_time = format_time_save.parse(g_time);
                        g_time = format_time.format(date_time);
                        g_time_apmp = format_time_ampm.format(date_time);

                        txtbtDate.setText(g_date_month);
                        txtDate.setText(g_date_day);
                        txtTime.setText(g_time);
                        txtbtTime.setText(g_time_apmp);

                        if(setting_def_glucose_unit.equals("mg/dl")) {
                            txtBS.setText(cursor_session.getString(cursor_session.getColumnIndex("g_value")));
                        }else
                        {
                            txtBS.setText(cursor_session.getString(cursor_session.getColumnIndex("g_mmolval")));
                        }
                        txtbtbsunit.setText(setting_def_glucose_unit);
                        txtbtWeightunit.setText(setting_weight_unit);

                        if(setting_weight_unit.equals("kg")) {
                            txtWeight.setText(cursor_session.getString(cursor_session.getColumnIndex("weight_number")));
                        }else
                        {
                            txtWeight.setText(cursor_session.getString(cursor_session.getColumnIndex("lbweight_number")));
                        }

                        mLinearView.setBackgroundColor(i % 2 == 0 ? Color.parseColor("#dfe8f9") : Color.WHITE);
                        first_image_circle.setImageResource(i % 2 == 0 ? R.drawable.usecircle : R.drawable.circle_2_3);
                        i++;
                        lnrsecond.addView(mLinearView);

                    } while (cursor_session.moveToNext());
                }
                cursor_session.close();
            }
        }
        catch (Exception e)
        {
            String s=String.valueOf(e);
        }
    }

    private void show_all_Analysistop( String FilterCondition ,String FilterAMPMCondition,String Todate)
    {
        try {
            getIntenet();

            lnrItemDetail.removeAllViews();
            Cursor cursor_session = db_dm.show_DMMinMaxAnalysisFilterData_Chartnew(nMemberId, nRelationshipId, FilterCondition, FilterAMPMCondition,Todate);

            if ((cursor_session != null) || (cursor_session.getCount()> 0))
            {
                if (cursor_session.moveToFirst()) {
                    do {
                        LayoutInflater inflater = null;

                        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mLinearView = inflater.inflate(R.layout.dm_monitor_analysis_weekly, null);
                        final TextView bs_max = (TextView) mLinearView.findViewById(R.id.bs_max);
                        final TextView bs_min = (TextView) mLinearView.findViewById(R.id.bs_min);
                        final TextView avg_bs = (TextView) mLinearView.findViewById(R.id.avg_bs);

                        final TextView bs_maxunit = (TextView) mLinearView.findViewById(R.id.bs_maxunit);
                        final TextView bs_minunit = (TextView) mLinearView.findViewById(R.id.bs_minunit);
                        final TextView avg_bsunit = (TextView) mLinearView.findViewById(R.id.avg_bsunit);

                        final TextView max_wt = (TextView) mLinearView.findViewById(R.id.max_wt);
                        final TextView min_wt = (TextView) mLinearView.findViewById(R.id.min_wt);
                        final TextView avg_wt = (TextView) mLinearView.findViewById(R.id.avg_wt);

                        final TextView max_wtunit = (TextView) mLinearView.findViewById(R.id.max_wtunit);
                        final TextView min_wtunit = (TextView) mLinearView.findViewById(R.id.min_wtunit);
                        final TextView avg_wtunit = (TextView) mLinearView.findViewById(R.id.avg_wtunit);


                        String bsmax="";
                        String bsmin="";
                        String bsavg="";
                        if(setting_def_glucose_unit.equals("mg/dl")) {
                            bsmax = cursor_session.getString(cursor_session.getColumnIndex("bs_max"));
                            bsmin = cursor_session.getString(cursor_session.getColumnIndex("bs_min"));
                            bsavg = cursor_session.getString(cursor_session.getColumnIndex("avg_bs"));
                        }else
                        {
                            bsmax = cursor_session.getString(cursor_session.getColumnIndex("bsmmol_max"));
                            bsmin = cursor_session.getString(cursor_session.getColumnIndex("bsmmol_min"));
                            bsavg = cursor_session.getString(cursor_session.getColumnIndex("avg_bsmmol"));
                        }
                        bs_max.setText(bsmax);
                        bs_min.setText(bsmin);
                        avg_bs.setText(bsavg);

                        bs_maxunit.setText(" (" + setting_def_glucose_unit + ")");
                        bs_minunit.setText(setting_weight_unit);
                        avg_bsunit.setText(setting_weight_unit);


                        max_wtunit.setText(" ( "+setting_weight_unit+" )");
                        min_wtunit.setText(setting_weight_unit);
                        avg_wtunit.setText(setting_weight_unit);



                        String smax_wt="";
                        String smin_wt="";
                        String savg_wt="";
                        if(setting_weight_unit.equals("kg")) {
                            smax_wt = cursor_session.getString(cursor_session.getColumnIndex("max_wt"));
                            smin_wt = cursor_session.getString(cursor_session.getColumnIndex("min_wt"));
                            savg_wt = cursor_session.getString(cursor_session.getColumnIndex("avg_wt"));
                        }else
                        {
                            smax_wt = cursor_session.getString(cursor_session.getColumnIndex("maxlb_wt"));
                            smin_wt = cursor_session.getString(cursor_session.getColumnIndex("minlb_wt"));
                            savg_wt = cursor_session.getString(cursor_session.getColumnIndex("avg_lbwt"));
                        }

                        max_wt.setText(smax_wt);
                        min_wt.setText(smin_wt);
                        avg_wt.setText(savg_wt);

                        lnrItemDetail.addView(mLinearView);

                    } while (cursor_session.moveToNext());
                }
                cursor_session.close();
            }
        }
        catch (Exception e)
        {
            String s=String.valueOf(e);
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = catList.get(groupPosition).getItemList().size();
        System.out.println("Child for group ["+groupPosition+"] is ["+size+"]");
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
        DMM_AnalysisHeading cat = catList.get(groupPosition);

        if(cat.getHeadingName().equals("Category"))
        {
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.dm_monitor_lastreadingweight, parent, false);

                final LinearLayout lnrcategoryLastReading = (LinearLayout) v.findViewById(R.id.lnrcategory);
                final TextView txtcategory_Detail = (TextView) v.findViewById(R.id.txtcategory_Detail);
                final TextView txtdmDATETIME = (TextView) v.findViewById(R.id.txtdmDATETIME);

                lnrcategoryLastReading.setVisibility(View.VISIBLE);

                txtcategory_Detail.setText(cat.getMin().toString());
                txtcategory_Detail.setTag("Category");
                txtdmDATETIME.setText(cat.getMax().toString());
                //txtBodyPosition.setText(cat.getAvg().toString());
                lnrcategoryLastReading.setClickable(false);
                lnrcategoryLastReading.setEnabled(false);
                v.setClickable(false);
                v.setEnabled(false);
            }
        }
        else if(cat.getHeadingName().equals("Last Reading"))
        {
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.dm_monitor_lastreading, parent, false);

                final LinearLayout lnrLastReading = (LinearLayout) v.findViewById(R.id.lnrLastReading);

                final TextView heading = (TextView) v.findViewById(R.id.heading);
                final TextView edtvalue = (TextView) v.findViewById(R.id.edtvalue);
                final TextView edtvalueCondi = (TextView) v.findViewById(R.id.edtvalueCondi);

                if (cat.getMin().equals("Blank"))
                {
                    lnrLastReading.setVisibility(View.GONE);
                }
                else
                {
                    edtvalue.setTextColor(Color.parseColor("#f38630"));//"#f38630"
                    if (cat.getMin().equals("BloodSugar"))
                    {
                        edtvalueCondi.setTextColor(Color.parseColor("#6cd720"));//"#f38630"
                    }
                    else if (cat.getMin().equals("Weight"))
                    {
                        edtvalueCondi.setTextColor(Color.parseColor("#bdbdbd"));
                    }
                    else
                    {
                        //edtvalueCondi.setTextColor(Color.parseColor("#ffffff"));
                    }

                    heading.setText(cat.getMin());
                    edtvalue.setText(cat.getMax());
                    edtvalueCondi.setText(cat.getAvg());
                    getIntenet();
                    Cursor cursor_session = db_dm.getLastReadingDMMonitarData(nMemberId, nRelationshipId);
                    int i = 1;
                    if ((cursor_session != null) || (cursor_session.getCount() > 0))
                    {
                        if (cursor_session.moveToFirst())
                        {
                            do
                            {
                                lnrLastReading.setTag(R.id.key_product_name, cursor_session.getString(cursor_session.getColumnIndex("DMID")));
                            } while (cursor_session.moveToNext());
                        }
                    }

                    lnrLastReading.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view)
                        {
                            String getDMID = (String) view.getTag(R.id.key_product_name);
                            DMID = Integer.parseInt(getDMID);
                            edit_data();
                        }
                    });
                }
            }
        }
        else
        {
            if (v == null)
            {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.dm_monitor_analysis_weekly_tophed, parent, false);
            }

            final TextView txttitle = (TextView) v.findViewById(R.id.txttitle);
            final TextView max = (TextView) v.findViewById(R.id.max);
            final TextView min = (TextView) v.findViewById(R.id.min);
            final TextView avg = (TextView) v.findViewById(R.id.avg);
            final  ImageView avgRightImg=(ImageView)v.findViewById(R.id.imgavgright);

            if (groupPosition == 1) {
                v.setBackgroundColor(Color.parseColor("#f38630"));//("#df6301");
                txttitle.setTextColor(Color.WHITE);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                //txttitle.setLayoutParams(param);
                avgRightImg.setImageResource(R.drawable.arrow_right_white);

                //txttitle.setTextSize(R.dimen.custom_font_Level_high);
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

    private void delete_data_dialog()
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        final View dialogview = inflater.inflate(R.layout.dm_monitor_data_delete_confirmation, null);

        final Button Deletebtn = (Button) dialogview.findViewById(R.id.btnDelete);
        final Button cancelbtn = (Button) dialogview.findViewById(R.id.btnCancel);
        builder.setView(dialogview);
        final Dialog dlg= builder.create();

        Deletebtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dlg.dismiss();
                delete_data(DMID);
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        dlg.show();

    }
    private void InsertintosyncTabledata(Integer DMid) {
        try
        {
            db_dm = new SqliteDMHandler(ctx);
            Cursor cursor = db_dm. getAllDMMonitarDataOnDmID(DMid);
            if (cursor.moveToFirst())
            {
                do
                {
                    String EditDMId= cursor.getString(cursor.getColumnIndex("DMID"));
                    String sMemberId = cursor.getString(cursor.getColumnIndex("member_id"));
                    String getSelectedRelationshipId = cursor.getString(cursor.getColumnIndex("relation_id"));
                    String weight_number= cursor.getString(cursor.getColumnIndex("weight_number"));
                    String weight_unit= cursor.getString(cursor.getColumnIndex("weight_unit"));
                    String Date= cursor.getString(cursor.getColumnIndex("g_date"));
                    String g_time=cursor.getString(cursor.getColumnIndex("g_time"));
                    String g_value = cursor.getString(cursor.getColumnIndex("g_value"));
                    String g_unit = cursor.getString(cursor.getColumnIndex("g_unit"));

                    String Note= cursor.getString(cursor.getColumnIndex("add_note"));

                    String g_category= cursor.getString(cursor.getColumnIndex("g_category"));
                    String s_reminder= cursor.getString(cursor.getColumnIndex("s_reminder"));
                    String DMIMEI= cursor.getString(cursor.getColumnIndex("DMIMEI"));
                    String DMUUID= cursor.getString(cursor.getColumnIndex("DMUUID"));
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("DMID", String.valueOf(EditDMId));
                    params.put("MemberId",sMemberId);
                    params.put("Relationship_ID",getSelectedRelationshipId);
                    params.put("Weight_No",weight_number);
                    params.put("weight_unit", weight_unit);
                    params.put("g_date",Date);
                    params.put("g_time", g_time);
                    params.put("g_value", g_value);
                    params.put("g_unit", "mg/dl");
                    params.put("add_note",  Note);
                    params.put("g_category",  g_category);
                    params.put("s_reminder", s_reminder);
                    params.put("IMEI", DMIMEI);
                    params.put("UUID", DMUUID);
                    params.put("Mode","D");
                    JSONObject jparams = new JSONObject(params);

                    String SUUID= UUID.randomUUID().toString();
                    String I_Type="Post";
                    String Controller= AppConfig.URL_POST_ADDDMDATAJson;
                    String Parameter="";
                    String JsonObject=String.valueOf(jparams);
                    String Created_Date=Date;

                    String F_KEY_UPLOAD_DOWNLOAD="true";
                    Integer F_KEY_SYNCMEMBERID=Integer.parseInt(sMemberId);
                    String F_KEY_MODULE_NAME="DM";
                    String F_KEY_MODE="D";
                    String F_KEY_ControllerName="Monitor";
                    String F_KEY_MethodName="DMMonitorBAL";
                    db_dm.InsertSyncTable(I_Type, Controller, Parameter,
                            JsonObject, Created_Date, SUUID, F_KEY_UPLOAD_DOWNLOAD, F_KEY_SYNCMEMBERID,
                            F_KEY_MODULE_NAME, F_KEY_MODE,F_KEY_ControllerName,F_KEY_MethodName,Long.parseLong(DMIMEI));

//                    f_alert_ok("Sucess", "Entry Saved");
//                    clearData();

                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch ( Exception E )
        {
            //f_alert_ok("Error","Error"+E.getMessage());
        }
    }

    private  void delete_data(Integer DMID)
    {
        db_dm = new SqliteDMHandler(ctx);
        InsertintosyncTabledata(DMID);
        db_dm.deleteDMDataById(DMID);
        ((DMA_AnalysisDisplayActivity) ctx).fillReading(FilterCondition);
        ((DMA_AnalysisDisplayActivity)ctx).fill_chart(FilterCondition,"bs");
        ((DMA_AnalysisDisplayActivity)ctx).create_statistics_pie_chart(FilterCondition);

        new AlertDialog.Builder(ctx)
                .setTitle("Delete")
                .setMessage("Data Deleted Successfully")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //act.recreate();
                        dialog.dismiss();
                    }
                }).show();
    }

    private  void edit_data()
    {
        Intent intent_edit = new Intent(ctx, DMA_NewEntry.class);
        intent_edit.putExtra("DMID", String.valueOf(DMID));

        ctx.startActivity(intent_edit);
        //act.getFragmentManager().beginTransaction().remove(act).commit();
        act.finish();

        db_dm = new SqliteDMHandler(ctx);
        // db. getAllBPMonitarDataOnDMID(DMID);
    }

    private void data_details_display()
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        final View dialogview = inflater.inflate(R.layout.dm_monitor_deatails, null);

        final Button Deletebtn = (Button) dialogview.findViewById(R.id.btnDelete);
        final Button editbtn = (Button) dialogview.findViewById(R.id.btnEdit);
        final Button closebtn = (Button) dialogview.findViewById(R.id.btncancel);

        final TextView glucosetxt = (TextView) dialogview.findViewById(R.id.txtglucose);
        final TextView Categorytxt = (TextView) dialogview.findViewById(R.id.txtCategory);
        final TextView timetxt = (TextView) dialogview.findViewById(R.id.txttime);
        final TextView dmdatetxt = (TextView) dialogview.findViewById(R.id.txtdmdate);
        TextView Commenttxt = (TextView) dialogview.findViewById(R.id.txtcomment);

        builder.setView(dialogview);
        final Dialog dlg= builder.create();
        db_dm = new SqliteDMHandler(ctx);
        Cursor cursor = db_dm. getAllDMMonitarDataOnDmID(DMID);
        if (cursor.moveToFirst()){
            do{
                String glucose = cursor.getString(cursor.getColumnIndex("g_value"));
                String category= cursor.getString(cursor.getColumnIndex("g_category"));
                String time= cursor.getString(cursor.getColumnIndex("g_time"));
                String dmdate= cursor.getString(cursor.getColumnIndex("g_date"));
                String Note= cursor.getString(cursor.getColumnIndex("add_note"));

                glucosetxt.setText(glucose);
                Categorytxt.setText(category);
                timetxt.setText(time);
                dmdatetxt.setText(dmdate);
                Commenttxt.setText(Note);

            }while(cursor.moveToNext());
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
    private void get_defaults()
    {
        String setting_name = "";
        Cursor cursor_session = db.getAllSetting_data(String.valueOf(nRelationshipId), String.valueOf(nMemberId), "2");

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

        if(setting_def_glucose_unit!=null)
        {

        }else
        {
            setting_def_glucose_unit="mg/dl";

        }


        if(setting_weight_unit!=null)
        {

        }else
        {
            setting_weight_unit="kg";

        }





    }

}
package viroopa.com.medikart.dmMonitor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import viroopa.com.medikart.MainActivity;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.Add_Doctor_Dialog;
import viroopa.com.medikart.common.Change_member_Dialog;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.common.dm_SetGoal_Dialog;
import viroopa.com.medikart.common.numerdialog;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteDMHandler;

/**
 * Created by Administrator on 15/Sep/2015.
 */

public class DMA_NewEntry extends AppCompatActivity implements  numerdialog.OnNumberDialogDoneListener,
                                                                DatePickerDialog.OnDateSetListener,
                                                                TimePickerDialog.OnTimeSetListener,
                                                                Add_Doctor_Dialog.OnDoctorSelectListener,
                                                                Change_member_Dialog.OnMemberSelectListener {

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottom_navigation;

    @BindView(R.id.txtGlucose)
    TextView txtGlucose;

    @BindView(R.id.txtGlucoseUnit)
    TextView txtGlucoseUnit;

    @BindView(R.id.txtSelectedInjsite)
    TextView txtSelectedInjsite;

    @BindView(R.id.txtWeight)
    TextView txtWeight;
    @BindView(R.id.txtWeightUnit)
    TextView txtWeightUnit;

    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtTime)
    TextView txtTime;

    @BindView(R.id.spnrCategory)
    Spinner sp_spnrCategory;
    @BindView(R.id.edtNote)
    EditText txtNote;

    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnAnalysis)
    Button btnAnalysis;


    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;



    //private LayoutInflater inflater;


/*    @BindView(R.id.txtSetGoal)
    TextView txtSetGoal;
    @BindView(R.id.txtHome)
    ImageView txtHome;*/

    private static final String TAG = MainActivity.class.getSimpleName();

    private  Menu objMemberMenu;
    private SqliteDMHandler db_dm;

    private Boolean EditMode=false;
    private Integer EditDMId=0;
    private Boolean FlagData=false;
    private ProgressDialog pDialog;
    private double valueweight = 0;
    private String  sCategoryGrpId="";
    private String kg ,lb , mgdl , mmol;
    private boolean onResumecalled=false;

    private Integer Injection_Position;
    private String Injection_Site="";

    String sInjection_Site="";
    String sInjection_Position="";
    private SQLiteHandler db ;
    private Integer wgt=0;
    private String wgtType;
    private String sUUID="" , sIMEI="";

    private Integer nMemberId, nRelationshipId;
    private String sDate,sTime,sAMPM,setting_weight_unit,setting_def_condition,setting_last_enterd_values,setting_def_glucose_unit;
    private    String[] array_category;
    private String array_weight_unit[] ;
    private String[] arrayglucose_unit;

    SharedPreferences pref ;

    ArrayAdapter adpt_Category;



    private SimpleDateFormat format_date = new SimpleDateFormat("LLL dd,yyyy");
    private SimpleDateFormat format_time = new SimpleDateFormat("hh:mm a");

    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");

    private SimpleDateFormat sdHour = new SimpleDateFormat("HH");
    private SimpleDateFormat sdMinute = new SimpleDateFormat("mm");

    private SimpleDateFormat format_date_save = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format_time_save = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat format_time_ampm_save = new SimpleDateFormat("a");

    AppController globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm_entry);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        globalVariable = (AppController) getApplicationContext();
        pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        db = SQLiteHandler.getInstance(this);
        array_category = getResources().getStringArray(R.array.array_category);
        array_weight_unit = getResources().getStringArray(R.array.array_weight_unit);
        arrayglucose_unit=getResources().getStringArray(R.array.dm_Unit);
        initialize_bottom_bar();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        getIntenet();

        db_dm = new SqliteDMHandler(this);
        db_dm.deleteCategory();
        db_dm.InsertBSCategory(1, "Before Dinner", "b");
        db_dm.InsertBSCategory(2, "After Dinner","a");
        db_dm.InsertBSCategory(3, "Before Lunch","b");
        db_dm.InsertBSCategory(4, "After Lunch", "a");
        db_dm.InsertBSCategory(5, "Before Breakfast","b");
        db_dm.InsertBSCategory(6, "After Breakfast","a");
        db_dm.InsertBSCategory(7, "Unfiled","u");

        show_analysis();

        show_default_date_time();
        fill_Combo();
        get_defaults();


        if(setting_last_enterd_values!=null) {
            if (setting_last_enterd_values.equals("true")) {
                getLastEntry();
            }
        }

        Edit_data();
    }

    private void show_analysis()
    {
        Cursor cursor_data = db_dm.getTop1DMMonitarData(nMemberId, nRelationshipId);
        if ((cursor_data != null) && (cursor_data.getCount() > 0))
        {
            btnAnalysis.setVisibility(View.VISIBLE);
        }
        else
        {
            btnAnalysis.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSelectMember(String Relationship_id, String name,String Imagename)
    {
        globalVariable.setRealationshipId(Relationship_id);
        nRelationshipId = Integer.parseInt(Relationship_id);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserName", name);
        editor.commit();

        show_analysis();

        ImageLoad(name,Imagename);
    }

    private  void ImageLoad(String name,String imageName)
    {
        View o_view=objMemberMenu.findItem(R.id.circlularImage).getActionView();
        CircularImageView crImage = (CircularImageView)o_view.findViewById(R.id.imgView_circlularImage);
        TextView txtmemberName = (TextView)o_view.findViewById(R.id.txtUsername);

        crImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Change_member_Dialog dpd = Change_member_Dialog.newInstance("crImage");
                dpd.show(getFragmentManager(), "Change_member_Dialog");
            }
        });

        if(imageName!=null) {
            String BPimgeName=imageName.substring(imageName.lastIndexOf('/') + 1, imageName.length());

            txtmemberName.setText(name);


            if (BPimgeName.startsWith("avtar")) {
                Resources res = getApplicationContext().getResources();

                int resourceId = res.getIdentifier(BPimgeName, "drawable", getApplicationContext().getPackageName());
                Drawable drawable = res.getDrawable(resourceId);
                crImage.setImageDrawable(drawable);
            } else {
                String iconsStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + AppConfig.IMAGE_DIRECTORY_NAME;
                File pathfile = new File(iconsStoragePath + File.separator + BPimgeName);
                Bitmap mybitmap = BitmapFactory.decodeFile(pathfile.getPath());
                if (mybitmap != null) {
                    Drawable d = new BitmapDrawable(getResources(), mybitmap);
                    crImage.setImageDrawable(d);

                }
            }
        }
    }




   /* @OnClick(R.id.txtHome)
    void txtHome() {
        try {
            Intent Intenet_edit_profile = new Intent(DMA_NewEntry.this, MainActivity.class);
            startActivity(Intenet_edit_profile);

        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
    }

    @OnClick(R.id.txtSetGoal)
    void txtSetGoal() {
        try {
            //SetGoalDialog();

            dm_SetGoal_Dialog myDiag = dm_SetGoal_Dialog.newInstance(nMemberId, nRelationshipId);
            myDiag.show(getFragmentManager(), "Diag");

        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
    }*/

    @OnClick(R.id.txtGlucose)
    void txtGlucose() {
        try {
            int iGlucose = 70 ;
            try{
                iGlucose= Integer.parseInt(txtGlucose.getText().toString().trim());
            }catch(NumberFormatException ex){ // handle your exception
                iGlucose = 70 ;
                String s = String.valueOf(ex);
            }

            numerdialog myDiag = numerdialog.newInstance(1,iGlucose,100,500,"txtGlucose","Select Glucose");
            myDiag.show(getFragmentManager(), "Diag");
        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
    }

    @OnClick(R.id.txtWeight)
    void txtWeight() {
        try {
            int iWeight = 0 ;
            try{
                iWeight= Integer.parseInt(txtWeight.getText().toString().trim());
            }catch(NumberFormatException ex){ // handle your exception
                iWeight = 0 ;
                String s = String.valueOf(ex);
            }

            numerdialog myDiag = numerdialog.newInstance(1,iWeight,60,500,"txtWeight","Select Weight");
            myDiag.show(getFragmentManager(), "Diag");

        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
    }

    public interface FragmentRefreshListener{
        void onRefresh(String p_object, String p_value);
    }

    @Override
    public void onDone(int value,String sClass) {

        switch (sClass) {

            case "txtWeight":
                txtWeight.setText(Integer.toString(value));
                break;
            case "txtGlucose":
                txtGlucose.setText(Integer.toString(value));
                break;
            case "txtSetGoalGlucose":
                if(getFragmentRefreshListener()!=null){
                    getFragmentRefreshListener().onRefresh(sClass,Integer.toString(value));
                }
                break;
            case "txtSetGoalWeight":
                if(getFragmentRefreshListener()!=null){
                    getFragmentRefreshListener().onRefresh(sClass,Integer.toString(value));
                }
                break;
        }
    }

    @OnClick(R.id.txtSelectedInjsite)
    void txtSelectedInjsite() {
        try {
            f_Select_InjectionSite();
        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
    }

    @OnClick(R.id.txtDate)
    void txtDate() {
        try {
            int nYear = 0 , nMonth = 0, nDay = 0;
            Date date = Calendar.getInstance().getTime();

            try {
                date = format_date.parse(txtDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            nYear=Integer.parseInt(sdYear.format(date));
            nMonth=Integer.parseInt(sdMonth.format(date)) - 1 ;
            nDay=Integer.parseInt(sdDay.format(date));

            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    this,
                    nYear,
                    nMonth,
                    nDay
            );
            dpd.show(getFragmentManager(), "Datepickerdialog");




            dpd.setMaxDate(Calendar.getInstance());;

        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
    }

    @OnClick(R.id.txtTime)
    void txtTime() {
        try {
            int nHour = 0 , nMinute = 0 ;
            Date date = Calendar.getInstance().getTime();
            try {
                date = format_time.parse(txtTime.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            nHour=Integer.parseInt(sdHour.format(date));
            nMinute=Integer.parseInt(sdMinute.format(date));

            TimePickerDialog dpd = TimePickerDialog.newInstance(
                    this,
                    nHour,
                    nMinute,
                    true
            );
            dpd.show(getFragmentManager(), "Timepickerdialog");
        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        txtDate.setText(format_date.format(getDate_Format(year,monthOfYear,dayOfMonth)));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute,int second) {
        //String time = hourOfDay+"h"+minute;
        txtTime.setText(format_time.format(getTime_Format(hourOfDay,minute)));
    }

    public Date getDate_Format(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date getTime_Format(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @OnClick(R.id.btnClear)
    void btnClear() {
        try {
            clearData();
        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
    }

    @OnClick(R.id.btnAnalysis)
    void btnAnalysis() {
        try {
            Intent Intenet_dm = new Intent(DMA_NewEntry.this, DMA_AnalysisDisplayActivity.class);
            startActivity(Intenet_dm);

        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
    }

    @OnClick(R.id.btnSave)
    void btnSave() {
        f_Save();
    }

    private boolean f_Save()
    {
        try {
            //if (txtGlucose.getText().toString().equals("Add Current Blood Sugar Reading")) {
            if (txtGlucose.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please Enter Glucose", Toast.LENGTH_LONG).show();
                return false;
            }
            //if (txtSelectedInjsite.getText().toString().equals("Injection Site")) {
            if (txtSelectedInjsite.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please Select Injection Site", Toast.LENGTH_LONG).show();
                return false;
            }
            if (txtWeight.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please Enter Weight", Toast.LENGTH_LONG).show();
                return false;
            }
            if (txtDate.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_LONG).show();
                return false;
            }
            if (txtTime.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please Select Time", Toast.LENGTH_LONG).show();
                return false;
            }
            //if (!sp_spnrCategory.getSelectedItem().toString().equals("Condition")) {
           /* if (sp_spnrCategory.getSelectedItemPosition() == 0) {
                Toast.makeText(getApplicationContext(), "Please Select a Condition", Toast.LENGTH_LONG).show();
                return false;
            }*/

            Date date_time ;
            date_time = format_date.parse(txtDate.getText().toString());
            sDate = format_date_save.format(date_time);
            date_time = format_time.parse(txtTime.getText().toString());
            sTime = format_time_save.format(date_time);
            sAMPM = format_time_ampm_save.format(date_time);


            String sCategory = sp_spnrCategory.getSelectedItem().toString();
            Cursor cursor = db_dm.getCategoryGrpIdByCategoryName(sCategory);
            if (cursor.moveToFirst()) {
                do {
                    sCategoryGrpId = cursor.getString(cursor.getColumnIndex("categorygroupid"));
                } while (cursor.moveToNext());
            }
            cursor.close();

            if (EditMode == true) {
                EditSavedata();
            } else {
                Savedata();
            }

            return true;

        } catch (Exception ex) {
            String s = String.valueOf(ex);
            return false ;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Minflater = getMenuInflater();
        //Minflater.inflate(R.menu.menu_new_add_medicine, menu);
        getMenuInflater().inflate(R.menu.menu_common, menu);
        this.objMemberMenu=menu;

        LayoutInflater inflater = LayoutInflater.from ( this );
        View mCustomView = inflater.inflate(R.layout.circula_image, null);
        objMemberMenu.findItem(R.id.circlularImage).setActionView(mCustomView);

        pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        ImageLoad(pref.getString("UserName",""),pref.getString("imagename",""));
        return true;
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dm_goal, menu);
        this.objMemberMenu=menu;

        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(DMA_NewEntry.this, DMA_Settings
                    .class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getIntenet() {
        Intent intent_mainactivity = getIntent();
        FlagData = intent_mainactivity.getBooleanExtra("FlagData",false);

        if(globalVariable.getRealationshipId()!=null) {
            nRelationshipId = Integer.parseInt(globalVariable.getRealationshipId());
        }
        else
        {
            nRelationshipId=8;
        }

        nMemberId = Integer.parseInt(pref.getString("memberid", ""));

        wgtType=pref.getString("weightType","");
        wgtType="Kg";
        wgt=pref.getInt("weight", 0);

        if(globalVariable.getWeight()!=null) {
            Double weightD=Double.parseDouble(globalVariable.getWeight());
            wgt =weightD.intValue();
        }
        wgt=60;
    }

    private void Edit_data()
    {
        Intent int_edit =this.getIntent();
        if(int_edit.getStringExtra("DMID") != null) {
            EditMode = true;
            EditDMId = Integer.parseInt(int_edit.getStringExtra("DMID"));


            Cursor cursor = db_dm. getAllDMMonitarDataOnDmID(EditDMId);



            if ((cursor != null) || (cursor.getCount()> 0)) {
                if (cursor.moveToFirst()) {
                    cursor.moveToFirst();

                    txtGlucose.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("g_value"))));
                    if(setting_weight_unit.equals("kg"))
                    {
                        txtWeight.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("weight_number"))));
                    }else {
                        txtWeight.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("lbweight_number"))));
                    }

                    txtNote.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("add_note"))));

                    sInjection_Site = String.valueOf(cursor.getString(cursor.getColumnIndex("injection_site")));
                    sInjection_Position = String.valueOf(cursor.getString(cursor.getColumnIndex("injection_position")));
                    txtSelectedInjsite.setText(sInjection_Site + " " + sInjection_Position);

                    String sCategory = String.valueOf(cursor.getString(cursor.getColumnIndex("g_category")));
                    //sCategoryGrpId = String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("bscategorygrpid")));
                    if (!sCategory.equals(null)) {
                        int spinnerPosition = adpt_Category.getPosition(sCategory);
                        sp_spnrCategory.setSelection(spinnerPosition);
                    }

                    try
                    {
                        String _sDate,_sTime ;
                        _sDate = String.valueOf(cursor.getString(cursor.getColumnIndex("g_date")));
                        _sTime = String.valueOf(cursor.getString(cursor.getColumnIndex("g_time")));

                        Date date_time ;
                        date_time = format_date_save.parse(_sDate);
                        _sDate = format_date.format(date_time);
                        date_time = format_time_save.parse(_sTime);
                        _sTime = format_time.format(date_time);

                        txtDate.setText(_sDate);
                        txtTime.setText(_sTime);


                    } catch (Exception ex) {
                        String s = String.valueOf(ex);
                    }


                }
            }


        }
    }

    public void clearData(){

        txtGlucose.setText("");
        txtSelectedInjsite.setText("");
        txtWeight.setText("");

        show_default_date_time();

        sp_spnrCategory.setSelection(0);
        txtNote.setText("");

        sDate = "";
        sTime = "";
        sAMPM = "";

        Injection_Position = 0;
        Injection_Site = "";
        sInjection_Site = "";
        sInjection_Position = "";
    }

    private void EditSavedata() {

        try{
            String sCategory = sp_spnrCategory.getSelectedItem().toString(); //sp_spnrCategory .getTextView().toString();
            String F_KEY_MODE="E";
            Long IMEINO;
            String myimeno = globalVariable.getIMEInumber().toString();
            if(myimeno.equals(""))
            {
                IMEINO=00000000000l;
            }
            else
            {
                IMEINO = Long.parseLong(myimeno);
            }

            String sweightTypeId=Integer.toString(wgt);
            String sweightunit=wgtType;
            String dm_Glucose=txtGlucose.getText().toString();
            String dm_getNote=txtNote.getText().toString();
            //String dm_getReminder=txt_SelectedReminder.getText().toString();
            String dm_getReminder="";
            String dm_IMEI=sIMEI;
            String SUUID= sUUID;

            sCategory=sp_spnrCategory.getSelectedItem().toString();

            double valuemmol=0;
            double valuemgdl=0;

            double valuekgweight=0;
            double valuelbweight=0;

            if(setting_def_glucose_unit.equals("mg/dl")) {
                valuemgdl = Double.parseDouble(dm_Glucose);
                mgdl=Double.toString(valuemgdl);
                valuemmol= Math.round(valuemgdl /18);
                mmol=Double.toString(valuemmol);
            }
            else
            {
                valuemmol = Double.parseDouble(dm_Glucose);
                mmol=Double.toString(valuemmol);
                valuemmol= Math.round(valuemmol * 18);
                mgdl = Double.toString(valuemmol);
            }

            valueweight = Double.parseDouble( txtWeight.getText().toString());

            if(setting_weight_unit.equals("kg")) {
                kg=Double.toString(valueweight);
                valuekgweight = Math.round(valueweight * 2.20462);
                lb=Double.toString(valuekgweight);
            }
            else
            {   lb=Double.toString(valueweight);
                valuelbweight = Math.round(valueweight *0.453592 );
                kg = Double.toString(valuelbweight);
            }

            db_dm.EditSaveDMDetails(nMemberId, nRelationshipId, sweightTypeId, sweightunit,
                    sDate, sTime, mgdl, setting_def_glucose_unit, dm_getNote, sCategory,
                                    dm_getReminder, String.valueOf(IMEINO), EditDMId, SUUID,
                                    Injection_Site, Injection_Position, sAMPM,  sCategoryGrpId,
                                    mmol,lb);

            Map<String, String> params = new HashMap<String, String>();

            params.put("DMID", String.valueOf(EditDMId));
            params.put("MemberId",String.valueOf(nMemberId));
            params.put("Mode", F_KEY_MODE);
            params.put("Relationship_ID",String.valueOf(nRelationshipId));
            params.put("Weight_No",sweightTypeId);
            params.put("weight_unit", sweightunit);
            params.put("g_date",sDate);
            params.put("g_time", sTime);
            params.put("g_value", mgdl);
            params.put("g_unit", setting_def_glucose_unit);
            params.put("add_note",  dm_getNote);
            params.put("g_category",  sCategory);
            params.put("s_reminder", dm_getReminder);
            params.put("IMEI",String.valueOf(IMEINO));
            params.put("UUID", SUUID);
            params.put("InjectionPosition", String.valueOf(Injection_Position));
            params.put("InjectionSite", Injection_Site);
            params.put("AMPM", sAMPM);
            params.put("bscategorygrpid", sCategoryGrpId);
            params.put("g_mmolval", mmol);
            params.put("lbweight_number", lb);

            JSONObject jparams = new JSONObject(params);
            String sRelationshipId=String.valueOf(nRelationshipId);
            String I_Type="Post";
            String Controller= AppConfig.URL_POST_ADDDMDATAJson;
            String Parameter="";
            String JsonObject=String.valueOf(jparams);
            String Created_Date=sDate;

            String F_KEY_UPLOAD_DOWNLOAD="true";
            Integer F_KEY_SYNCMEMBERID=nMemberId;
            String F_KEY_MODULE_NAME="DM";
            String F_KEY_ControllerName="Monitor";
            String F_KEY_MethodName="DMMonitorBAL";
            db_dm.InsertSyncTable(I_Type, Controller, Parameter,
                    JsonObject, Created_Date, SUUID, F_KEY_UPLOAD_DOWNLOAD, F_KEY_SYNCMEMBERID,
                    F_KEY_MODULE_NAME, F_KEY_MODE,F_KEY_ControllerName,F_KEY_MethodName,IMEINO);

            f_alert_ok("Sucess", "Entry Saved");

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("goal_bs", dm_Glucose);
            editor.putString("goal_wt", sweightTypeId);
            editor.commit();
            globalVariable.setBp_last_bs(dm_Glucose);
            globalVariable.setRealationshipId(sRelationshipId);

            clearData();

            btnAnalysis.setVisibility(View.VISIBLE);
        }
        catch ( Exception E )
        {
            //f_alert_ok("Error","Error"+E.getMessage());
        }

    }

    private void Savedata() {
        try{

            String sCategory = sp_spnrCategory.getSelectedItem().toString();
            String F_KEY_MODE="A";
            Long IMEINO=00000000000l;

            String sweightTypeId=Integer.toString(wgt);
            String sweightunit=wgtType;

            String dm_Glucose=txtGlucose.getText().toString();
            String dm_getNote=txtNote.getText().toString();


//            String dm_getReminder=txt_SelectedReminder.getText().toString();
            String dm_getReminder="";
            // Integer nRelationshipId=1;
            String myimeno = globalVariable.getIMEInumber().toString();
            if(myimeno.equals(""))
            {
                IMEINO=00000000000l;
            }
            else {
                IMEINO = Long.parseLong(myimeno);
            }

            double valuemmol=0;
            double valuemgdl=0;
            double valuekgweight=0;
            double valuelbweight=0;

            if(setting_def_glucose_unit.equals("mg/dl")) {
                valuemgdl = Double.parseDouble(dm_Glucose);
                mgdl=Double.toString(valuemgdl);
                valuemmol= Math.round(valuemgdl /18);
                mmol=Double.toString(valuemmol);
            }
            else
            {
                valuemmol = Double.parseDouble(dm_Glucose);
                mmol=Double.toString(valuemmol);
                valuemmol= Math.round(valuemmol * 18);
                mgdl = Double.toString(valuemmol);
            }

            valueweight = Double.parseDouble( txtWeight.getText().toString());

            if(setting_weight_unit.equals("kg")) {
                kg=Double.toString(valueweight);
                valuekgweight = Math.round(valueweight *2.20462 );
                lb=Double.toString(valuekgweight);
            }
            else
            {   lb=Double.toString(valueweight);
                valuelbweight = Math.round(valueweight *0.453592 );
                kg = Double.toString(valuelbweight);
            }

            // String dm_IMEI=txt_SelectedReminder.getText().toString();
            String SUUID=UUID.randomUUID().toString();
            sCategory = sp_spnrCategory.getSelectedItem().toString();
            db_dm.addDMDetails(nMemberId, nRelationshipId, txtWeight.getText().toString(),
                                txtWeightUnit.getText().toString(), sDate, sTime, mgdl,
                    setting_def_glucose_unit, dm_getNote, sCategory, dm_getReminder, String.valueOf(IMEINO),
                                SUUID, Injection_Site, Injection_Position, sAMPM, sCategoryGrpId,mmol,lb);

            Map<String, String> params = new HashMap<String, String>();

            params.put("DMID", "0");
            params.put("MemberId",String.valueOf(nMemberId));
            params.put("Mode", F_KEY_MODE);
            params.put("member_id",String.valueOf(nMemberId));
            params.put("Relationship_ID",String.valueOf(nRelationshipId));
            params.put("Weight_No",sweightTypeId);
            params.put("weight_unit", sweightunit);
            params.put("g_date",sDate);
            params.put("g_time", sTime);
            params.put("AMPM", sAMPM);
            params.put("g_value", dm_Glucose);
            params.put("g_unit", setting_def_glucose_unit);
            params.put("add_note",  dm_getNote);
            params.put("g_category",  sCategory);
            params.put("s_reminder", dm_getReminder);
            //  params.put("IMEI", dm_IMEI);
            params.put("UUID", SUUID);
            params.put("IMEI",String.valueOf(IMEINO));
            params.put("InjectionPosition", String.valueOf(Injection_Position));
            params.put("InjectionSite", Injection_Site);
            params.put("bscategorygrpid", sCategoryGrpId);
            params.put("g_mmolval", mmol);
            params.put("lbweight_number", lb);

            JSONObject jparams = new JSONObject(params);

            String sRelationshipId=String.valueOf(nRelationshipId);
            // String SUUID= UUID.randomUUID().toString();
            String I_Type="Post";
            String Controller= AppConfig.URL_POST_ADDDMDATAJson;
            String Parameter="";
            String JsonObject=String.valueOf(jparams);
            String Created_Date=sDate;

            String F_KEY_UPLOAD_DOWNLOAD="true";
            Integer F_KEY_SYNCMEMBERID=nMemberId;
            String F_KEY_MODULE_NAME="DM";

            String F_KEY_ControllerName="Monitor";
            String F_KEY_MethodName="DMMonitorBAL";
            db_dm.InsertSyncTable(I_Type, Controller, Parameter,
                    JsonObject, Created_Date, SUUID, F_KEY_UPLOAD_DOWNLOAD, F_KEY_SYNCMEMBERID,
                    F_KEY_MODULE_NAME, F_KEY_MODE, F_KEY_ControllerName, F_KEY_MethodName, IMEINO);

            f_alert_ok("Sucess", "Entry Saved");

            globalVariable.setBp_last_bs(dm_Glucose);
            globalVariable.setRealationshipId(sRelationshipId);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("goal_bs", dm_Glucose);
            editor.putString("goal_wt", sweightTypeId);
            editor.commit();

            clearData();

            btnAnalysis.setVisibility(View.VISIBLE);
        }
        catch ( Exception E )
        {
            E.toString();//f_alert_ok("Error","Error"+E.getMessage());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void f_Select_InjectionSite()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.dm_injection_site, null);

        final EditText inputSearch = (EditText) dialogview.findViewById(R.id.inputSearch);
        final TextView Title = (TextView) dialogview.findViewById(R.id.textView);
        final Button btncancel=(Button) dialogview.findViewById(R.id.btncancel);
        final Button btnok=(Button) dialogview.findViewById(R.id.btnok);
        Title.setText("Select Injection site");

        final Button btnf1=(Button) dialogview.findViewById(R.id.btnFront1);
        final Button btnf2=(Button) dialogview.findViewById(R.id.btnFront2);
        final Button btnf3=(Button) dialogview.findViewById(R.id.btnFront3);
        final Button btnb1=(Button) dialogview.findViewById(R.id.btnBack1);
        final Button btnb2=(Button) dialogview.findViewById(R.id.btnBack2);
        final Button btnb3=(Button) dialogview.findViewById(R.id.btnBack3);
        final Button btnb4=(Button) dialogview.findViewById(R.id.btnBack4);

        btnf1.setTag(R.id.key_injectionsite_type,"F");
        btnf2.setTag(R.id.key_injectionsite_type,"F");
        btnf3.setTag(R.id.key_injectionsite_type,"F");
        btnb1.setTag(R.id.key_injectionsite_type,"B");
        btnb2.setTag(R.id.key_injectionsite_type,"B");
        btnb3.setTag(R.id.key_injectionsite_type,"B");
        btnb4.setTag(R.id.key_injectionsite_type,"B");

        btnf1.setTag(R.id.key_injectionsite_id,"1");
        btnf2.setTag(R.id.key_injectionsite_id,"2");
        btnf3.setTag(R.id.key_injectionsite_id,"3");
        btnb1.setTag(R.id.key_injectionsite_id,"1");
        btnb2.setTag(R.id.key_injectionsite_id,"2");
        btnb3.setTag(R.id.key_injectionsite_id,"3");
        btnb4.setTag(R.id.key_injectionsite_id,"4");

        View.OnClickListener btn_click = new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                sInjection_Site =  (String) view.getTag(R.id.key_injectionsite_type);
                sInjection_Position = (String) view.getTag(R.id.key_injectionsite_id);
                Injection_Site = sInjection_Site;
                Injection_Position = Integer.valueOf(sInjection_Position);

                btnf1.setBackgroundResource(R.drawable.rounded_button);
                btnf2.setBackgroundResource(R.drawable.rounded_button);
                btnf3.setBackgroundResource(R.drawable.rounded_button);
                btnb1.setBackgroundResource(R.drawable.rounded_button);
                btnb2.setBackgroundResource(R.drawable.rounded_button);
                btnb3.setBackgroundResource(R.drawable.rounded_button);
                btnb4.setBackgroundResource(R.drawable.rounded_button);

                view.setBackgroundResource(R.drawable.rounded_green_button);
            }
        };

        btnf1.setOnClickListener(btn_click);
        btnf2.setOnClickListener(btn_click);
        btnf3.setOnClickListener(btn_click);
        btnb1.setOnClickListener(btn_click);
        btnb2.setOnClickListener(btn_click);
        btnb3.setOnClickListener(btn_click);
        btnb4.setOnClickListener(btn_click);

        btnf1.setBackgroundResource(R.drawable.rounded_button);
        btnf2.setBackgroundResource(R.drawable.rounded_button);
        btnf3.setBackgroundResource(R.drawable.rounded_button);
        btnb1.setBackgroundResource(R.drawable.rounded_button);
        btnb2.setBackgroundResource(R.drawable.rounded_button);
        btnb3.setBackgroundResource(R.drawable.rounded_button);
        btnb4.setBackgroundResource(R.drawable.rounded_button);

        if(sInjection_Site.equals("F")) {
            if (sInjection_Position.equals("1")) {
                btnf1.setBackgroundResource(R.drawable.rounded_green_button);
            }
            if (sInjection_Position.equals("2")) {
                btnf2.setBackgroundResource(R.drawable.rounded_green_button);
            }
            if (sInjection_Position.equals("3")) {
                btnf3.setBackgroundResource(R.drawable.rounded_green_button);
            }
        }
        else
        {
            if (sInjection_Position.equals("1")) {
                btnb1.setBackgroundResource(R.drawable.rounded_green_button);
            }
            if (sInjection_Position.equals("2")) {
                btnb2.setBackgroundResource(R.drawable.rounded_green_button);
            }
            if (sInjection_Position.equals("3")) {
                btnb3.setBackgroundResource(R.drawable.rounded_green_button);
            }
            if (sInjection_Position.equals("4")) {
                btnb4.setBackgroundResource(R.drawable.rounded_green_button);
            }
        }

        builder.setView(dialogview);
        final Dialog dlg= builder.create();

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(!Injection_Site.toString().equals("")) {//&& !Injection_Position.equals("null")
                    txtSelectedInjsite.setText(Injection_Site + " " + String.valueOf(Injection_Position));
                    dlg.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), "Please select Injection Site", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        dlg.show();
    }

    private void getLastEntry()
    {
        Cursor Cursor_Last = db_dm. getLastReadingDMMonitarData(nMemberId, nRelationshipId);
        int count = Cursor_Last.getCount();
        if ((Cursor_Last != null) || (Cursor_Last.getCount()> 0)) {
            if (Cursor_Last.moveToFirst()) {
                Cursor_Last.moveToFirst();

                txtGlucose.setText(String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("g_value"))));
                if(setting_weight_unit.equals("kg"))
                {
                    txtWeight.setText(String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("weight_number"))));
                }else {
                    txtWeight.setText(String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("lbweight_number"))));
                }

                txtNote.setText(String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("add_note"))));

                sInjection_Site = String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("injection_site")));
                sInjection_Position = String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("injection_position")));
                txtSelectedInjsite.setText(sInjection_Site + " " + sInjection_Position);

                String sCategory = String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("g_category")));
                //sCategoryGrpId = String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("bscategorygrpid")));
                if (!sCategory.equals(null)) {
                    int spinnerPosition = adpt_Category.getPosition(sCategory);
                    sp_spnrCategory.setSelection(spinnerPosition);
                }

                try
                {
                    String _sDate,_sTime ;
                    _sDate = String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("g_date")));
                    _sTime = String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex("g_time")));

                    Date date_time ;
                    date_time = format_date_save.parse(_sDate);
                    _sDate = format_date.format(date_time);
                    date_time = format_time_save.parse(_sTime);
                    _sTime = format_time.format(date_time);

                    txtDate.setText(_sDate);
                    txtTime.setText(_sTime);


                } catch (Exception ex) {
                    String s = String.valueOf(ex);
                }


            }
        }
    }

    private void show_default_date_time()
    {
        Calendar ca = Calendar.getInstance();
        txtDate.setText(format_date.format(ca.getTime()));
        txtTime.setText(format_time.format(ca.getTime()));
    }

    private void fill_Combo()
    {
        try{

            adpt_Category = new ArrayAdapter<String>(this,R.layout.rxspinner_simple_text_layout,array_category);
            sp_spnrCategory.setAdapter(adpt_Category);
        }
        catch (Exception ex)
        {
            String s=String.valueOf(ex);
        }
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }



    private void initialize_bottom_bar() {

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home_icon_white, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Add Doctor", R.drawable.add_doctor_white, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Set Goal", R.drawable.setgoal_white, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("View Summary", R.drawable.graphicon, R.color.colorPrimary);

        // Add items
        bottom_navigation.addItem(item1);
        bottom_navigation.addItem(item2);
        bottom_navigation.addItem(item3);
        bottom_navigation.addItem(item4);

        // Set background color
        bottom_navigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));

        // Disable the translation inside the CoordinatorLayout
        // bottomNavigation.setBehaviorTranslationEnabled(false);

        // Change colors
        bottom_navigation.setAccentColor(Color.parseColor("#FFFFFF"));
        bottom_navigation.setInactiveColor(Color.parseColor("#747474"));
        bottom_navigation.setUseElevation(true);

        // Force to tint the drawable (useful for font with icon for example)
        // bottomNavigation.setForceTint(true);

        // Force the titles to be displayed (against Material Design guidelines!)
        bottom_navigation.setForceTitlesDisplay(true);

        // Use colored navigation with circle reveal effect
        // bottomNavigation.setColored(true);

        // Set current item programmatically
        //bottomNavigation.setCurrentItem(1);

        // Customize notification (title, background, typeface)
        bottom_navigation.setNotificationBackgroundColor(getResources().getColor(R.color.colorPrimary));

        bottom_navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {

                if(position == 0)
                {
                    Intent Intenet_main = new Intent(DMA_NewEntry.this, MainActivity.class);
                    startActivity(Intenet_main);
                }
                else if(position == 1)
                {
                    Add_Doctor_Dialog myDiag = Add_Doctor_Dialog.newInstance("add_doctor");
                    myDiag.show(getFragmentManager(), "Diag");
                }
                else if(position == 2)
                {
                    dm_SetGoal_Dialog myDiag = dm_SetGoal_Dialog.newInstance(nMemberId, nRelationshipId);
                    myDiag.show(getFragmentManager(), "Diag");
                }

                else if(position == 3)
                {
                    Cursor cursor_data;
                    cursor_data = db_dm.getTop1DMMonitarData(nMemberId, nRelationshipId);

                    Intent Intenet_dm;
                    if ((cursor_data != null) || (cursor_data.getCount() > 0)) {
                        if (cursor_data.moveToFirst()) {
                            Intenet_dm = new Intent(DMA_NewEntry.this, DMA_AnalysisDisplayActivity.class);
                            Intenet_dm.putExtra("FlagData", true);
                            startActivity(Intenet_dm);

                        }
                    }
                }
            }
        });
    }

    @Override
    public void onSelectDoctor(String Doc_id,String Doc_name,String email) {

//        add_doctor.setText(Doc_name);
//        Doctor_id=Doc_id;
        Add_Doctor_Dialog myDiag = Add_Doctor_Dialog.newInstance("add_doctor");
        myDiag.show(getFragmentManager(), "Diag");

    }

    private void get_defaults()
    {
        String setting_name = "";
        Cursor cursor_session = db.getAllSetting_data(String.valueOf(nRelationshipId), String.valueOf(nMemberId), "2");

        if (cursor_session.moveToFirst()) {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("dm_weight_unit")) {
                    setting_weight_unit =  ConstData.getValueOrDefault(  cursor_session.getString(cursor_session.getColumnIndex("value")), "kg");
                }
                if (setting_name.equals("dm_def_condition")) {
                    setting_def_condition =  ConstData.getValueOrDefault(  cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("dm_use_last_entered_values")) {
                    setting_last_enterd_values =  ConstData.getValueOrDefault(  cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("dm_def_glucose_unit")) {
                    setting_def_glucose_unit =  ConstData.getValueOrDefault(  cursor_session.getString(cursor_session.getColumnIndex("value")), "mg/dl");
                }


            } while (cursor_session.moveToNext());
        }

        if(setting_def_glucose_unit!=null)
        {
            if(!setting_def_glucose_unit.equals("false"))
            {
               // txtWeightUnit.setText(setting_def_glucose_unit);
                txtGlucoseUnit.setText(setting_def_glucose_unit);
            }
        }else
        {
            setting_def_glucose_unit="mg/dl";
          //  txtWeightUnit.setText(setting_def_glucose_unit);
            txtGlucoseUnit.setText(setting_def_glucose_unit);
        }


        if(setting_weight_unit!=null)
        {
            if(!setting_weight_unit.equals("false"))
            {
                txtWeightUnit.setText(setting_weight_unit);
            }
        }else
        {
            setting_weight_unit="kg";
            txtWeightUnit.setText(setting_weight_unit);
        }


        if(setting_def_condition!=null)
        {
            if(!setting_def_condition.equals("false"))
            {
                sp_spnrCategory.setSelection( Arrays.asList(array_category).indexOf(setting_def_condition));
            }
        }


    }
    @Override
    public void onResume() {

        super.onResume();
        if(onResumecalled) {
            //finish();
            //startActivity(getIntent());
        }
        onResumecalled=true;
    }

}
package viroopa.com.medikart.wmMonitor.WMCommon;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.numerdialog;
import viroopa.com.medikart.dmMonitor.DMA_AnalysisDisplayActivity;
import viroopa.com.medikart.dmMonitor.DMA_NewEntry;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteDMHandler;
import viroopa.com.medikart.helper.SqliteWMHandler;
import viroopa.com.medikart.wmMonitor.WMA_Welcome;
import viroopa.com.medikart.wmMonitor.WMA_watermain;

public class WM_SetGoal_Dialog extends DialogFragment implements numerdialog.OnNumberDialogDoneListener{

    private static final String	ARG_MemberId = "MemberId";
    private static final String	ARG_RelationshipId = "RelationshipId";
    private static final String	ARG_SELECTED_DATE = "selected_date";

    AppController globalVariable;
    private SqliteWMHandler db_wm;
    private Integer glassSize=250;
    private Boolean water_unit_changed=false;
    private TextView weighttxt,watertxt,Dosage_unit_selected;
    private RadioGroup Rdo_Grp_Activity;
    private Button dpbtn_water_needed,btncancel,set;
    private Integer nMemberId,nRelationshipId;
    private Spinner spinnerMlSelect;
    private  String selected_water_unit="ml";
    private OnWaterGoalDialogDoneListener mListner;
    private String sMemberId,RelationShipId,setting_weight_unit,setting_activity_level;
    private SQLiteHandler db;

    private LinearLayout lnrw_first;
    private LinearLayout btn_layout;
    private String actvtyLevel= "Regular",date;
    private Integer Actualweight,WaterNeeded,WaterCalculated;
    private boolean LitreSelected=false;

    private boolean isSpinnerTouched = false;

    private   String Activitylist[] ;
    private String array_weight_unit[] ;

    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");


    public WM_SetGoal_Dialog() {
        // Required empty public constructor
    }

    public static WM_SetGoal_Dialog newInstance(int MemberId, int RelationshipId,String date_selected) {

        WM_SetGoal_Dialog setGoal_dialog = new WM_SetGoal_Dialog();
        Bundle args = new Bundle();

        args.putInt(ARG_MemberId, MemberId);
        args.putInt(ARG_RelationshipId, RelationshipId);
        args.putString(ARG_SELECTED_DATE, date_selected);


        setGoal_dialog.setArguments(args);

        return setGoal_dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalVariable = (AppController)getActivity().getApplicationContext();
        db_wm = new SqliteWMHandler(getActivity());
        db = SQLiteHandler.getInstance(getActivity());

        Activitylist=getResources().getStringArray(R.array.Activity_Level);
        array_weight_unit = getResources().getStringArray(R.array.array_weight_unit);
        if (getArguments() != null) {
            nMemberId = getArguments().getInt(ARG_MemberId);
            nRelationshipId =	getArguments().getInt(ARG_RelationshipId);
            date=getArguments().getString(ARG_SELECTED_DATE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.wm_activity_water_goal, container, false);

        dpbtn_water_needed=(Button)rootView.findViewById(R.id.dpbtn_water_needed);
        spinnerMlSelect=(Spinner)rootView.findViewById(R.id.spinnerMlSelect);
        btncancel=(Button)rootView.findViewById(R.id.btncancel);
        lnrw_first=(LinearLayout)rootView.findViewById(R.id.LinearLayout055);
        btn_layout=(LinearLayout)rootView.findViewById(R.id.btn_layout);
        weighttxt=(TextView)rootView.findViewById(R.id.weight);
        Dosage_unit_selected=(TextView)rootView.findViewById(R.id.Dosage_unit_selected);
        watertxt=(EditText)rootView.findViewById(R.id.watertxt);
        //watertxt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        set=(Button)rootView.findViewById(R.id.btnset);
        Rdo_Grp_Activity=(RadioGroup)rootView.findViewById(R.id.Rdo_Grp_Activity);

       // watertxt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        final String[] condition_array = getResources().getStringArray(R.array.water_monitor_ml_l);
        ArrayAdapter autocompletetextAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.rxspinner_simple_text_layout
                ,condition_array);


        spinnerMlSelect.setAdapter(autocompletetextAdapter);

        load_selected_data();

        if(setting_activity_level!=null)
        {
            if(setting_activity_level.equals("false"))
            {
              //  spinnerMlSelect.setSelection(Arrays.asList(Activitylist).indexOf(setting_activity_level));
            }
        }


        if(setting_weight_unit!=null)
        {
            if(!setting_weight_unit.equals("false"))
            {
                Dosage_unit_selected.setText(setting_weight_unit);
            }
        }

        weighttxt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            int iWeight = 1;
                            try {
                                iWeight = Integer.parseInt(weighttxt.getText().toString().trim());
                            } catch (NumberFormatException ex) { // handle your exception
                                iWeight = 1;
                                String s = String.valueOf(ex);
                            }

                            numerdialog myDiag = numerdialog.newInstance(1, iWeight, 1, 500, "weighttxt", "Select Weight");
                            myDiag.show(getFragmentManager(), "Diag");

                        } catch (Exception ex) {
                            String s = String.valueOf(ex);
                        }

                    }
                });

        try
        {
            if ((WMA_Welcome) getActivity() != null) {
                ((WMA_Welcome) getActivity()).setFragmentRefreshListener(new WMA_Welcome.FragmentRefreshListener() {
                    @Override
                    public void onRefresh(String p_object, String p_value) {

                        switch (p_object) {
                            case "weighttxt":
                                weighttxt.setText(p_value);
                                break;
                        }
                    }
                });
            }
        }
        catch (Exception ex)
        {
        }

        try
        {
            if ((WMA_watermain) getActivity() != null) {
                ((WMA_watermain) getActivity()).setFragmentRefreshListener(new WMA_watermain.FragmentRefreshListener() {
                    @Override
                    public void onRefresh(String p_object, String p_value) {

                        switch (p_object) {
                            case "weighttxt":
                                weighttxt.setText(p_value);
                                break;
                        }
                    }
                });
            }
        }
        catch (Exception ex)
        {
        }


        Rdo_Grp_Activity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.rdo_regular) {
                            actvtyLevel="Regular";

                            if (!weighttxt.getText().toString().isEmpty()) {
                                WaterCalculated = wateramountChange(Integer.parseInt(weighttxt.getText().toString()), setting_weight_unit, actvtyLevel);

                                if (LitreSelected == false) {

                                    watertxt.setText(String.valueOf(WaterCalculated));

                                } else {


                                    watertxt.setText( String.format("%.2f", WaterCalculated / 1000.00));

                                }


                            }else {
                               // Toast.makeText(getActivity(), "Please enter Weight", Toast.LENGTH_SHORT)
                                       // .show();
                            }
                        } else if (checkedId == R.id.rdo_sedentry) {
                            actvtyLevel="Sedentary";

                            if (!weighttxt.getText().toString().isEmpty()) {
                                WaterCalculated = wateramountChange(Integer.parseInt(weighttxt.getText().toString()), setting_weight_unit, actvtyLevel);

                                if (LitreSelected == false) {

                                    watertxt.setText(String.valueOf(WaterCalculated));

                                } else {


                                    watertxt.setText( String.format("%.2f", WaterCalculated / 1000.00));

                                }

                            }

                        }else if (checkedId == R.id.rdo_active) {

                            actvtyLevel="Active";
                            if (!weighttxt.getText().toString().isEmpty()) {
                                WaterCalculated = wateramountChange(Integer.parseInt(weighttxt.getText().toString()), setting_weight_unit, actvtyLevel);

                                if (LitreSelected == false) {

                                    watertxt.setText(String.valueOf(WaterCalculated));

                                } else {


                                    watertxt.setText(String.format("%.2f", WaterCalculated / 1000.00));

                                }
                            }


                        }

                    }

                });



        dpbtn_water_needed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (weighttxt.length() > 0 ) {
                    if (Rdo_Grp_Activity.getCheckedRadioButtonId() != -1){

                        Integer selWeight = Integer.parseInt(weighttxt.getText().toString());

                    WaterCalculated = wateramountChange(selWeight,setting_weight_unit, actvtyLevel);

                        if (LitreSelected == false) {
                            isSpinnerTouched = false;
                            spinnerMlSelect.setSelection(0);

                            watertxt.setText(String.valueOf(WaterCalculated ));

                        } else {

                            isSpinnerTouched = false;
                            spinnerMlSelect.setSelection(1);

                            watertxt.setText( String.format("%.2f", WaterCalculated / 1000.00));

                        }
                        lnrw_first.setVisibility(View.VISIBLE);
                        btn_layout.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(getActivity(), "please select level of activity", Toast.LENGTH_SHORT)
                            .show();
                }


                }else {
                    Toast.makeText(getActivity(), "please enter your weight", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });


        watertxt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {



                if (watertxt.length() > 3) {
                    if (LitreSelected == true) {
                        if(Double.parseDouble(watertxt.getText().toString())>10.0)
                        {
                            watertxt.setText("");
                            Toast.makeText(getActivity(), "Maximum 10L", Toast.LENGTH_SHORT)
                                    .show();
                        }else {
                           // isSpinnerTouched = false;
                           // spinnerMlSelect.setSelection(0);
                            Double wamount = Double.parseDouble(watertxt.getText().toString()) * 1000;
                            WaterCalculated = wamount.intValue();
                        }
                    } else {
                        if (Double.parseDouble(watertxt.getText().toString()) > 10000.0) {
                            watertxt.setText("");
                            Toast.makeText(getActivity(), "Maximum 10000 ml", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                          //  isSpinnerTouched = false;
                          //  spinnerMlSelect.setSelection(1);
                            Double wamount = Double.parseDouble(watertxt.getText().toString());
                            WaterCalculated = wamount.intValue();
                        }
                    }


                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(LitreSelected==true)
                {
                    watertxt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2,2)});

                }else
                {
                    watertxt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!watertxt.getText().toString().isEmpty() && !weighttxt.getText().toString().isEmpty()) {
                    boolean isDataSaved=savedata();

                    mListner.OnWaterGoalChange(WaterCalculated,isDataSaved,LitreSelected);
                  dismiss();

                } else {
                    Toast.makeText(getActivity(), "Please enter details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


       /* spinnerMlSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
            }
        });*/

        spinnerMlSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                try {

                  ///  if (isSpinnerTouched==true) {


                        String selected = (String) parent.getItemAtPosition(pos);
                        if (selected_water_unit.equals(selected)) {

                        } else {

                            if (pos == 0) {
                                LitreSelected = false;
                                if (!watertxt.getText().toString().isEmpty() && water_unit_changed == true) {
                                    watertxt.setText(String.format("%.2f", (Double.parseDouble(watertxt.getText().toString()) * 1000.00)));
                                }
                            } else {
                                LitreSelected = true;
                                if (!watertxt.getText().toString().isEmpty()) {
                                    watertxt.setText(String.format("%.2f", (Double.parseDouble(watertxt.getText().toString()) / 1000.00)));
                                }
                            }
                        }
                        water_unit_changed = true;
                        selected_water_unit = selected;
                 //   }
                }catch (Exception e)
                {
                    e.toString();
                }


            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        });


        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               dismiss();
            }
        });

        this.getDialog().getWindow().requestFeature(STYLE_NO_TITLE);

        return rootView;

    }

    @Override
    public void onDone(int value,String sClass) {
    }

    @Override
    public void onResume() {
        super.onResume();

       // Toast.makeText(getActivity(), "OnResume Start", Toast.LENGTH_LONG).show();

    }




    public Integer wateramountChange(Integer weight,String unit,String vactive_level )
    {

        if(unit.equals("kg"))
        {
            Actualweight = weight;


        }else if((unit.equals("lbs")))
        {
            Actualweight = (int)(long)Math.round(((weight)*(2.20462)));

        }
        WaterNeeded  = (((Actualweight-20)*15)+1500);
        if(vactive_level.equals("Regular"))
        {


        }

        if(vactive_level.equals("Sedentary"))
        {

            if(Actualweight<50)
            {
                WaterNeeded =  WaterNeeded-350;
            }else  if(Actualweight>50 && Actualweight<100)
            {
                WaterNeeded =  WaterNeeded-450;
            }

        }

        if(vactive_level.equals("Active"))
        {

            if(Actualweight<50)
            {
                WaterNeeded =  WaterNeeded+350;
            }else  if(Actualweight>50 && Actualweight<100)
            {
                WaterNeeded =  WaterNeeded+450;
            }

        }

        return  WaterNeeded;



    }
    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern= Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
    private boolean savedata()
    {
        String sradomstring = UUID.randomUUID().toString();

        Cursor cursor = db_wm.getDateUuidWMSetting(date,nRelationshipId);

        if (cursor.moveToFirst()){
            do{
                globalVariable.setUUIDforWM(cursor.getString(cursor.getColumnIndex("main_uuid")));

                db_wm.update_Goal(WaterCalculated,date,nRelationshipId);
                return true;

            }while(cursor.moveToNext());
        }else {

            Long id = db_wm.addWMSetting(nMemberId, sradomstring, WaterCalculated, date, globalVariable.getIMEI(), nRelationshipId, glassSize);
            if (id == 0) {
                return false;
            } else {
                SavedataToServerWMSettings(String.valueOf(nMemberId), date, WaterCalculated.toString(), String.valueOf(nRelationshipId), sradomstring, globalVariable.getIMEI(), "A",glassSize.toString());
                return true;
            }
        }

    }

    private void SavedataToServerWMSettings(String Member_id,String Created_date,String GoalSet,String RelationShip_id,String UUID,String IMEI,String Mode,String Glass_size) {
        try{

            Map<String, String> params = new HashMap<String, String>();


            params.put("member_id",Member_id);
            params.put("main_uuid", UUID);
            params.put("Goal_set",GoalSet);
            params.put("created_date", Created_date);
            params.put("glass_size", Glass_size);
            params.put("IMEI_main",IMEI );
            params.put("Relationship_ID",RelationShip_id);
            params.put("Mode", Mode);

            JSONObject jparams = new JSONObject(params);
            String I_Type="Post";
            String Controller= AppConfig.URL_POST_WATERSETTING;
            String Parameter="";
            String JsonObject=String.valueOf(jparams);
            String Created_Date=Created_date;

            String F_KEY_UPLOAD_DOWNLOAD="true";
            Integer F_KEY_SYNCMEMBERID=Integer.parseInt(Member_id);
            String F_KEY_MODULE_NAME="WS";
            String F_KEY_MODE="A";
            String F_KEY_ControllerName="Monitor";
            String F_KEY_MethodName="SaveWSMonitor";
            db_wm.InsertSyncTable(I_Type, Controller, Parameter,
                    JsonObject, Created_Date, UUID, F_KEY_UPLOAD_DOWNLOAD, F_KEY_SYNCMEMBERID,
                    F_KEY_MODULE_NAME, F_KEY_MODE, F_KEY_ControllerName, F_KEY_MethodName);

        }
        catch ( Exception E )
        {

        }

    }
    public interface OnWaterGoalDialogDoneListener {
        public void OnWaterGoalChange(int value, boolean isDatasave,boolean litre_selected);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
            mListner = (OnWaterGoalDialogDoneListener) activity;
        }
        catch (ClassCastException e) {
            throw
                    new ClassCastException(activity.toString()
                            + " must implement OnWaterGoalDialogDoneListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListner = null;
    }

    private void load_selected_data() {
        SharedPreferences pref = getActivity().getSharedPreferences("Global", getActivity().MODE_PRIVATE);

        sMemberId = pref.getString("memberid", "");
        if (globalVariable.getRealationshipId() != null) {
            RelationShipId = globalVariable.getRealationshipId();
        } else {
            RelationShipId = "8";
        }
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



            } while (cursor_session.moveToNext());
        }
        if(setting_weight_unit==null)
        {
            setting_weight_unit="kg";
        }
        if(setting_activity_level==null)
        {
            setting_activity_level="false";
        }
    }

}
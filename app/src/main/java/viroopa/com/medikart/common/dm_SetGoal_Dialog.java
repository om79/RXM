package viroopa.com.medikart.common;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.dmMonitor.DMA_AnalysisDisplayActivity;
import viroopa.com.medikart.dmMonitor.DMA_NewEntry;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteDMHandler;

public class dm_SetGoal_Dialog extends DialogFragment implements numerdialog.OnNumberDialogDoneListener{

    private static final String	ARG_MemberId = "MemberId";
    private static final String	ARG_RelationshipId = "RelationshipId";

    AppController globalVariable;
    private SqliteDMHandler db_dm;
    private SQLiteHandler db ;
    private    String[] array_category;
    private String array_weight_unit[] ;
    private String setting_weight_unit, mmol,setting_def_glucose_unit;

    private TextView txtSetGoalGlucose,txtSetGoalGlucoseUnit,txtSetGoalWeight,txtSetGoalWeightUnit;
    private Button btnCancel,btnSave_SetGoal;
    private Integer nMemberId,nRelationshipId;

    public dm_SetGoal_Dialog() {
        // Required empty public constructor
    }

    public static dm_SetGoal_Dialog newInstance(int MemberId, int RelationshipId) {

        dm_SetGoal_Dialog setGoal_dialog = new dm_SetGoal_Dialog();
        Bundle args = new Bundle();

        args.putInt(ARG_MemberId, MemberId);
        args.putInt(ARG_RelationshipId, RelationshipId);

        setGoal_dialog.setArguments(args);

        return setGoal_dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalVariable = (AppController)getActivity().getApplicationContext();
        db_dm = new SqliteDMHandler(getActivity());
        db = new SQLiteHandler(getActivity());
        array_category = getResources().getStringArray(R.array.array_category);
        array_weight_unit = getResources().getStringArray(R.array.array_weight_unit);

        if (getArguments() != null) {
            nMemberId = getArguments().getInt(ARG_MemberId);
            nRelationshipId =	getArguments().getInt(ARG_RelationshipId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dm_setgoal, container, false);

        txtSetGoalGlucose = (TextView) rootView.findViewById(R.id.txtSetGoalGlucose);
        txtSetGoalGlucoseUnit = (TextView) rootView.findViewById(R.id.txtSetGoalGlucoseUnit);
        txtSetGoalWeight = (TextView) rootView.findViewById(R.id.txtSetGoalWeight);
        txtSetGoalWeightUnit = (TextView) rootView.findViewById(R.id.txtSetGoalWeightUnit);

        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnSave_SetGoal = (Button) rootView.findViewById(R.id.btnSave_SetGoal);
        get_defaults();
        Cursor cursor_datagoal;
        String g_Weight="";
        String g_Glucose="";
        cursor_datagoal = db_dm.getgoalDMMonitarData(nMemberId, nRelationshipId);

        if ((cursor_datagoal != null) || (cursor_datagoal.getCount() > 0)) {
            if (cursor_datagoal.moveToFirst()) {
                cursor_datagoal.moveToFirst();


                if(setting_weight_unit.equals("kg")) {
                     g_Weight = String.valueOf(cursor_datagoal.getString(cursor_datagoal.getColumnIndex("dm_kg")));
                }else
                {
                    g_Weight = String.valueOf(cursor_datagoal.getString(cursor_datagoal.getColumnIndex("dm_lb")));
                }

                if(setting_def_glucose_unit.equals("mg/dl")) {
                    g_Glucose = String.valueOf(cursor_datagoal.getString(cursor_datagoal.getColumnIndex("goalbloodsugar")));
                }else
                {
                    g_Glucose = String.valueOf(cursor_datagoal.getString(cursor_datagoal.getColumnIndex("g_mmolval")));
                }

                txtSetGoalGlucose.setText(g_Glucose);
                txtSetGoalWeight.setText(g_Weight);
                txtSetGoalWeightUnit.setText(setting_weight_unit);
            }
        }

        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

        btnSave_SetGoal.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        f_Save();
                    }
                });

        txtSetGoalGlucose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int iGlucose = 70;
                            try {
                                iGlucose = Integer.parseInt(txtSetGoalGlucose.getText().toString().trim());
                            } catch (NumberFormatException ex) { // handle your exception
                                iGlucose = 70;
                                String s = String.valueOf(ex);
                            }

                            numerdialog myDiag = numerdialog.newInstance(1, iGlucose, 70, 500, "txtSetGoalGlucose", "Select Glucose");
                            myDiag.show(getFragmentManager(), "Diag");
                        } catch (Exception ex) {
                            String s = String.valueOf(ex);
                        }
                    }
                });

        txtSetGoalWeight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int iWeight = 1;
                            try {
                                iWeight = Integer.parseInt(txtSetGoalWeight.getText().toString().trim());
                            } catch (NumberFormatException ex) { // handle your exception
                                iWeight = 1;
                                String s = String.valueOf(ex);
                            }

                            numerdialog myDiag = numerdialog.newInstance(1, iWeight, 1, 200, "txtSetGoalWeight", "Select Weight");
                            myDiag.show(getFragmentManager(), "Diag");

                        } catch (Exception ex) {
                            String s = String.valueOf(ex);
                        }
                    }
                });

        try
        {
            if ((DMA_NewEntry) getActivity() != null) {
                ((DMA_NewEntry) getActivity()).setFragmentRefreshListener(new DMA_NewEntry.FragmentRefreshListener() {
                    @Override
                    public void onRefresh(String p_object, String p_value) {

                        switch (p_object) {
                            case "txtSetGoalGlucose":
                                txtSetGoalGlucose.setText(p_value);
                                break;
                            case "txtSetGoalWeight":
                                txtSetGoalWeight.setText(p_value);
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
            if ((DMA_AnalysisDisplayActivity) getActivity() != null) {
                ((DMA_AnalysisDisplayActivity) getActivity()).setFragmentRefreshListener(new DMA_AnalysisDisplayActivity.FragmentRefreshListener() {
                    @Override
                    public void onRefresh(String p_object, String p_value) {

                        switch (p_object) {
                            case "txtSetGoalGlucose":
                                txtSetGoalGlucose.setText(p_value);
                                break;
                            case "txtSetGoalWeight":
                                txtSetGoalWeight.setText(p_value);
                                break;
                        }
                    }
                });
            }
        }
        catch (Exception ex)
        {
        }



       // this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getDialog().getWindow().requestFeature(STYLE_NO_TITLE);

/*        WindowManager manager = (WindowManager)getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();


        this.getDialog().getWindow().setLayout(display.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);*/



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

    private boolean f_Save() {
        try{
            if (txtSetGoalGlucose.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Please Enter Glucose", Toast.LENGTH_LONG).show();
                return false;
            }

            if (txtSetGoalWeight.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Please Enter Weigth", Toast.LENGTH_LONG).show();
                return false;
            }
            String sGlucose     = (txtSetGoalGlucose.getText().toString());
            String sGlucoseUnit = (txtSetGoalGlucoseUnit.getText().toString());
            String sWeight      = txtSetGoalWeight.getText().toString();
            String bs_unit="mg/dl";

            Double dWeight = Double.parseDouble(sWeight);
            String sKG,sLBS,mgdl;
            double valuemmol=0;
            double valuemgdl=0;

            if(setting_weight_unit.equals("kg"))
            {
                sKG  = sWeight;
                sLBS = Double.toString(Math.round(dWeight *2.20462 ));
            }
            else
            {
                sKG  = Double.toString(Math.round(dWeight *0.453592 ));
                sLBS = sWeight;
            }

            if(bs_unit.equals("mg/dl")) {
                valuemgdl = Double.parseDouble(sGlucose);
                mgdl=Double.toString(valuemgdl);
                valuemmol= Math.round(valuemgdl /18);
                mmol=Double.toString(valuemmol);
            }
            else
            {
                valuemmol = Double.parseDouble(sGlucose);
                mmol=Double.toString(valuemmol);
                valuemmol= Math.round(valuemmol * 18);
                mgdl = Double.toString(valuemmol);
            }

            db_dm.deleteGoalDMDetails(nMemberId, nRelationshipId);
            db_dm.InsertDMGOALTable(nMemberId, nRelationshipId, sWeight,setting_weight_unit, mgdl, sKG, sLBS,sGlucoseUnit,mmol);

            f_alert_ok("Save", "Data Saved");

            dismiss();
            return true;
        }
        catch (Exception E)
        {
            f_alert_ok("Error","Error"+E.getMessage());
            return false;
        }
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(getActivity())
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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
                if (setting_name.equals("dm_def_glucose_unit")) {
                    setting_def_glucose_unit = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }



            } while (cursor_session.moveToNext());
        }


        if(setting_weight_unit!=null)
        {
            if(!setting_weight_unit.equals("false"))
            {
                txtSetGoalWeightUnit.setText(setting_weight_unit);
            }
        }else
        {
            setting_weight_unit="kg";
            txtSetGoalWeightUnit.setText(setting_weight_unit);

        }

        if(setting_def_glucose_unit!=null)
        {
            if(!setting_def_glucose_unit.equals("false"))
            {
                txtSetGoalGlucoseUnit.setText(setting_def_glucose_unit);
            }
        }else
        {
            setting_def_glucose_unit="mg/dl";
            txtSetGoalGlucoseUnit.setText(setting_def_glucose_unit);

        }


    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
            mListener = (OnSetGoalDialogDoneListener) activity;
        }
        catch (ClassCastException e) {
            throw
                    new ClassCastException(activity.toString()
                            + " must implement OnNumberDialogDoneListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnSetGoalDialogDoneListener {
        public void onSetGoalGulcoseDone(int value, String sreturnobject);
    }
*/
}
package viroopa.com.medikart.common;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import viroopa.com.medikart.R;

/**
 * Created by prakash on 16/05/16.
 */
public class SettingListwithRadiodialog extends DialogFragment {
    private static final String	ARG_saveobject = "saveobject";
    private static final String	ARG_sHeading = "sHeading";
    private static final String	ARG_objId = "objectid";
    private static final String	ARG_DefaultSelected = "default";
    private static final String	ARG_array = "array";

    private String[] text_name_array;

    private String sreturnobject,sHeading;
    private Integer default_selected_intex;

    private OnRadioButtonSelectListener mListener;


    public SettingListwithRadiodialog() {
        // Required empty public constructor
    }

    public static SettingListwithRadiodialog newInstance(
           String[] textArray, String sObjectName, String sHeaderText,Integer Default) {

        SettingListwithRadiodialog numdialog = new SettingListwithRadiodialog();
        Bundle args = new Bundle();

        args.putStringArray(ARG_array, textArray);
        args.putString(ARG_saveobject, sObjectName);
        args.putString(ARG_sHeading, sHeaderText);
        args.putInt(ARG_DefaultSelected, Default);

        numdialog.setArguments(args);
        return numdialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            sreturnobject = getArguments().getString(ARG_saveobject);
            sHeading = getArguments().getString(ARG_sHeading);
            text_name_array= getArguments().getStringArray(ARG_array);
            default_selected_intex= getArguments().getInt(ARG_DefaultSelected);

        }

    }




    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bp_searchlist, container, false);
        this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        final ListView lv = (ListView) rootView.findViewById(R.id.list_view);
        final TextView Title = (TextView) rootView.findViewById(R.id.textView);
        final Button btncancel = (Button) rootView.findViewById(R.id.btncancel);
        final Button btnok = (Button) rootView.findViewById(R.id.btnok);


        ArrayAdapter<String>   adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, android.R.id.text1, text_name_array);

        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setItemChecked(default_selected_intex,true);


        Title.setText(sHeading);

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                dismiss();
            }
        });

        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                dismiss();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                mListener.onRadioButtonSelect(position,sreturnobject);

            }


        });


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
            mListener = (OnRadioButtonSelectListener) activity;
        }
        catch (ClassCastException e) {
            throw
                    new ClassCastException(activity.toString()
                            + " must implement OnRadioButtonSelectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnRadioButtonSelectListener {
        public void onRadioButtonSelect(int value, String sreturnobject);
    }
}
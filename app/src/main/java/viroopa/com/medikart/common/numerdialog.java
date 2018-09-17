package viroopa.com.medikart.common;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import viroopa.com.medikart.R;

/**
 * Created by prakash on 16/05/16.
 */
public class numerdialog extends DialogFragment {
    private static final String	ARG_numDials = "numDials";
    private static final String	ARG_initValue = "initValue";
    private static final String	ARG_minvalue = "minvalue";
    private static final String	ARG_maxvalue = "maxvalue";
    private static final String	ARG_saveobject = "saveobject";
    private static final String	ARG_objecttext = "objecttext";
    private static final String	ARG_objId = "objectid";

    private int numDials,currentValue,nmax,nmin;

    private String sreturnobject,sObjectName,sHeaderText;


    private NumberPicker[] numPickers;
    private OnNumberDialogDoneListener mListener;


    public numerdialog() {
        // Required empty public constructor
    }

    public static numerdialog newInstance(
            int numDials, int initValue, int nmin, int nMax, String sObjectName, String sHeaderText) {

        numerdialog numdialog = new numerdialog();
        Bundle args = new Bundle();
        args.putInt(ARG_numDials, numDials);
        args.putInt(ARG_initValue, initValue);
        args.putString(ARG_saveobject, sObjectName);
        args.putString(ARG_objecttext, sHeaderText);
        args.putInt(ARG_minvalue, nmin);
        args.putInt(ARG_maxvalue, nMax);
        numdialog.setArguments(args);
        return numdialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            numDials = getArguments().getInt(ARG_numDials);
            currentValue =	getArguments().getInt(ARG_initValue);
            nmin = getArguments().getInt(ARG_minvalue);
            nmax = getArguments().getInt(ARG_maxvalue);
            sreturnobject = getArguments().getString(ARG_saveobject);
            sObjectName = getArguments().getString(ARG_objecttext);
            numPickers = new NumberPicker[numDials];
        }
        if (savedInstanceState != null) {
            currentValue = savedInstanceState.getInt("CurrentValue");
        }
    }

    private int getDigit(int d, int i) {
        String temp = Integer.toString(d);
        if (temp.length() <= i) return 0;
        int r = Character.getNumericValue(
                temp.charAt(temp.length() - i - 1));
        return r;
    }

    private int getValue() {
        int value = 0;
        int mult = 1;
        for (int i = 0; i < numDials; i++) {
            value += numPickers[i].getValue() * mult;
            mult *= 10;
        }
        return value;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        WindowManager manager = (WindowManager)getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params_ok_btn = new LinearLayout.LayoutParams(150,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params_textview = new LinearLayout.LayoutParams(((display.getWidth()/4)*3)-10,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params_button = new LinearLayout.LayoutParams((display.getWidth()/4)+10,
                LinearLayout.LayoutParams.MATCH_PARENT);


        LinearLayout linLayoutV = new LinearLayout(getActivity());
         linLayoutV.setOrientation(LinearLayout.VERTICAL);
        linLayoutV.setLayoutParams(params);

        LinearLayout linLayoutH = new LinearLayout(getActivity());
        linLayoutH.setOrientation(LinearLayout.HORIZONTAL);
        linLayoutH.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                80);
        linLayoutH.setLayoutParams(params);
        linLayoutH.setLayoutParams(params);
        params.gravity=Gravity.CENTER_VERTICAL;


        // Added by prakash
        TextView TxtHeader = new TextView(getActivity());
       // params_textview.weight= (float) 0.7;

        TxtHeader.setLayoutParams(params_textview);
        TxtHeader.setText(sObjectName);
        TxtHeader.setPadding(15,10,10,10);
        TxtHeader.setTextColor(getResources().getColor(R.color.white));
        linLayoutH.addView(TxtHeader);

        Button close_btn=new Button(getActivity());
        close_btn.setText("X");
        close_btn.setPadding(10,10,10,10);
        close_btn.setLayoutParams(params_button);
        close_btn.setTextColor(getResources().getColor(R.color.white));
        close_btn.setTextSize(15);
        close_btn.setBackground(null);

        close_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

        linLayoutH.addView(close_btn);

        linLayoutV.addView(linLayoutH);

        final int i=0;

      /*  for (final int i = numDials - 1; i >= 0; i--)
        {*/
            numPickers[i] = new NumberPicker(getActivity());
            numPickers[i].setMaxValue(nmax);
            numPickers[i].setMinValue(nmin);
           // numPickers[i].setValue(getDigit(currentValue, i));
            numPickers[i].setValue(currentValue);
           // numPickers[i].setDescendantFocusability(NumberPicker.FOCUSABLES_ALL);




            EditText input = findInput(  numPickers[i]);
            TextWatcher tw = new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() != 0) {
                        Integer value = Integer.parseInt(s.toString());
                        if (value >=   numPickers[i].getMinValue()) {
                            numPickers[i].setValue(value);
                        }
                    }
                }
            };
            input.addTextChangedListener(tw);
            linLayoutV.addView(numPickers[i]);
        //}

        Button okButton = new Button(getActivity());
        okButton.setBackground(getResources().getDrawable(R.drawable.orgbtn));
        okButton.setTextColor(getResources().getColor(R.color.white));
        okButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentValue = getValue();
                        if (mListener != null) {
                            mListener.onDone(currentValue, sreturnobject);
                        }
                        ;
                        numerdialog.this.getDialog().getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        );
                        dismiss();

                    }
                });
        params_ok_btn.gravity = Gravity.CENTER_HORIZONTAL;
        params_ok_btn.bottomMargin=5;
        okButton.setLayoutParams(params_ok_btn);
        okButton.setText("Ok");
        linLayoutV.addView(okButton);

        WindowManager.LayoutParams w_params = this.getDialog().getWindow().getAttributes();
        w_params.gravity = Gravity.CENTER;
        w_params.windowAnimations = 1;


        params.width = display.getWidth()*4/4;
       // params.height = display.getHeight()/2;

        this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return linLayoutV;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
            mListener = (OnNumberDialogDoneListener) activity;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentValue", getValue());
    }

    public interface OnNumberDialogDoneListener {
        public void onDone(int value, String sreturnobject);
    }
    private EditText findInput(ViewGroup np) {
        int count = np.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = np.getChildAt(i);
            if (child instanceof ViewGroup) {
                findInput((ViewGroup) child);
            } else if (child instanceof EditText) {
                return (EditText) child;
            }
        }
        return null;
    }
}
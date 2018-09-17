package viroopa.com.medikart.common;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import viroopa.com.medikart.ObjectItem;
import viroopa.com.medikart.R;
import viroopa.com.medikart.adapter.AD_adapterCombo;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Add_Doctor_Dialog extends DialogFragment {

    private static final String	ARG_OBJNAME = "objName";

    private AppController globalVariable;
    private Boolean AddDoctorShowVisible=false;
    private AD_adapterCombo doctorAdapter;
    private ListView lv ;
    private ProgressBar progressBar;
    private String sMemberId;
    private ProgressDialog pDialog;
    private  ObjectItem[] ObjectItemDoctor=null;
    private EditText txtdoctorname,clinicname,doctor_email,doctor_mobile;
    private ImageView heading_icon ;
    private OnDoctorSelectListener mListener;
    private LinearLayout AddDoctorShow ,AddDoctorShowHide,DoctorShowFirst,DoctorShowSecond;
    private TextView cancelbtn,Titile,textViewExp,btncancel;
    private ImageView btnDoctorcancel;
    private Button btnExp,okbtn;
    private RequestQueue queue ;


    private String Objname;

    public Add_Doctor_Dialog() {
        // Required empty public constructor
    }

    public static Add_Doctor_Dialog newInstance(String sObjectName) {

        Add_Doctor_Dialog Doctor_dialog = new Add_Doctor_Dialog();
        Bundle args = new Bundle();

        args.putString(ARG_OBJNAME, sObjectName);

        Doctor_dialog.setArguments(args);

        return Doctor_dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this.getActivity());
        globalVariable = (AppController)getActivity().getApplicationContext();
        getIntenet();


        if (getArguments() != null) {
            Objname = getArguments().getString(ARG_OBJNAME);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_searchlist, container, false);

        lv = (ListView)rootView. findViewById(R.id.list_view);
       progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        heading_icon = (ImageView) rootView.findViewById(R.id.heading_icon);
        AddDoctorShow = (LinearLayout) rootView.findViewById(R.id.addDoctorlnr);
        AddDoctorShowHide = (LinearLayout) rootView.findViewById(R.id.AddDoctorShowHide);
        DoctorShowFirst = (LinearLayout) rootView.findViewById(R.id.DoctorShowFirst);
        DoctorShowSecond = (LinearLayout) rootView.findViewById(R.id.DoctorShowSecond);
        okbtn = (Button) rootView.findViewById(R.id.btnok);
        btnDoctorcancel = (ImageView) rootView.findViewById(R.id.btnDoctorcancel);
        cancelbtn = (TextView) rootView.findViewById(R.id.btncancel);
        Titile =(TextView)rootView.findViewById(R.id.textView);
        textViewExp =(TextView)rootView.findViewById(R.id.  textViewExp);
        btnExp =(Button)rootView.findViewById(R.id.btnExp);
        btncancel=(TextView)rootView.findViewById(R.id.btncancel);
        txtdoctorname = (EditText) rootView.findViewById(R.id.doctorname);
        clinicname = (EditText) rootView.findViewById(R.id.clinicname);
        doctor_email= (EditText) rootView.findViewById(R.id.doctor_email);
        doctor_mobile= (EditText) rootView.findViewById(R.id.doctor_mobile);

        Titile.setText("Select doctor");
        AddDoctorShowHide.setVisibility(View.VISIBLE);
        heading_icon.setImageResource(R.drawable.add_doctor_white);
        getdoctorlist();
        lv.setOnItemClickListener(new OnItemClickListenerListViewDoctor());

        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

              dismiss();
            }
        });



        btnExp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (AddDoctorShowVisible == false) {
                    DoctorShowFirst.setVisibility(view.GONE);
                    DoctorShowSecond.setVisibility(view.GONE);
                    AddDoctorShow.setVisibility(view.VISIBLE);
                    AddDoctorShowVisible = true;
                    textViewExp.setText("Show Doctor List");
                    btnExp.setVisibility(view.GONE);
                }
            }
        });
        AddDoctorShowHide.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (AddDoctorShowVisible == true) {
                    AddDoctorShowVisible = false;
                    DoctorShowFirst.setVisibility(view.VISIBLE);
                    DoctorShowSecond.setVisibility(view.VISIBLE);
                    AddDoctorShow.setVisibility(view.GONE);
                    btnExp.setVisibility(view.VISIBLE);
                    textViewExp.setText("Doctor Not in List ? +");

                }
            }
        });




        txtdoctorname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                okbtn.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (validate()) {
                    post_new_doctor(txtdoctorname.getText().toString(), clinicname.getText().toString(),doctor_email.getText().toString(),doctor_mobile.getText().toString());
                    dismiss();
                }
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
            }
        });
        btnDoctorcancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
            }
        });

        this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return rootView;
    }
    public class OnItemClickListenerListViewDoctor implements AdapterView.OnItemClickListener {
        OnItemClickListenerListViewDoctor()
        {
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView textViewItem = ((TextView) view.findViewById(R.id.product_name));
            String listItemText = textViewItem.getText().toString();
            String listItemId = textViewItem.getTag().toString();
            String Email_id = view.getTag().toString();

            if (mListener != null) {
                mListener.onSelectDoctor(listItemId, listItemText,Email_id);
            }
            ;

            dismiss();

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
    private void post_new_doctor(final String doctor,String Clinic,String email_id,String mobile_number) {

        queue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap<String, String>();

        params.put("iMemberId", sMemberId);
        params.put("iDistrictid","35");
        params.put("sDoctorName", doctor);
        params.put("sClinicName",  Clinic);
        params.put("DoctorId", "0");
        params.put("DoctorId", "0");
        params.put("Mobile_No",mobile_number);
        params.put("email_id", email_id);

        JSONObject jparams = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_ADDDoctorJson,
                //  new JSONObject(params),
                jparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Success_add_doctor(response,doctor);
                        hidePDialog();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Error(error);
                        hidePDialog();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("charset", "utf-8");
                headers.put("User-agent", "medikart");
                return headers;
            }
        };

        jor_inhurry_post.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(jor_inhurry_post);


    }

    private void Success_add_doctor(JSONObject response,String doctor){
        try {

          //  f_alert_ok("Success", "Doctor Add Successfully");
            if (mListener != null) {
                mListener.onSelectDoctor( response.getString("DoctorId"),doctor,response.getString("DoctorName"));
            }
            dismiss();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Error(VolleyError error) {

        error.printStackTrace();

    }

    private void getdoctorlist() {

        showPdialog("loading...");


        queue = Volley.newRequestQueue(getActivity());

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        String url = String.format(AppConfig.URL_GET_LOCALDOCTORLIST, "0", sMemberId);

        JsonArrayRequest doctorrequest = new JsonArrayRequest(url,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Success_doctor(response);
                progressBar.setVisibility(View.GONE);
                progressBar.setProgress(100);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        progressBar.setVisibility(View.GONE);
                        progressBar.setProgress(100);

                        Error(error);
                    }
                });

        doctorrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(doctorrequest);


    }

    private void Success_doctor(JSONArray response) {
        try {
  if(response!=null) {
      ObjectItemDoctor = new ObjectItem[response.length()];
      for (int i = 0; i < response.length(); i++) {
          JSONObject jsonobject = response.getJSONObject(i);
          ObjectItemDoctor[i] = new ObjectItem(jsonobject.optInt("ID"), jsonobject.getString("DoctorName"), jsonobject.getString("EmailId"));
      }

      doctorAdapter = new AD_adapterCombo(getActivity(), R.layout.list_item, ObjectItemDoctor);
      lv.setAdapter(doctorAdapter);

      hidePDialog();
  }
        } catch (Exception e) {
            hidePDialog();
            e.printStackTrace();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
            mListener = (OnDoctorSelectListener) activity;
        }
        catch (ClassCastException e) {
            throw
                    new ClassCastException(activity.toString()
                            + " must implement OnNumberDialogDoneListener");
        }
    }

    private void hidePDialog() {

        if (pDialog != null) {
            pDialog.dismiss();
            //pDialog = null;
        }
    }

    private void showPdialog(String sMessage) {

        if (pDialog != null) {
            pDialog.setIcon(R.drawable.logo);
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }
    public interface OnDoctorSelectListener {

        public void onSelectDoctor(String DocId, String DocName,String email_id);
    }
    private void getIntenet()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("Global",getActivity(). MODE_PRIVATE);
        sMemberId  = pref.getString("memberid", "");
    }

    private Boolean validate()
    {

            if (txtdoctorname.getText().toString().isEmpty())
            {
                f_alert_ok("Add Doctor", "Please Enter doctor name");
                return false;
            }
            if(clinicname.getText().toString().isEmpty()) {

           f_alert_ok("Add Doctor", "Please Enter clinic name");
           return false;
        }
        if(!ConstData.validate(doctor_email.getText().toString())) {

            f_alert_ok("Add Doctor", "Please Enter a valid Mail address");
            return false;
        }
        if(doctor_mobile.getText().toString().isEmpty()) {

            f_alert_ok("Add Doctor", "Please Enter clinic name");
            return false;
        }

        return true;

    }
}
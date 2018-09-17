package viroopa.com.medikart.common;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import viroopa.com.medikart.R;
import viroopa.com.medikart.adapter.AD_memberAdapter;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.model.M_memberlist;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Change_member_Dialog extends DialogFragment {

    private static final String	ARG_OBJNAME = "objName";

    private AppController globalVariable;
    private ListView lv ;
    private String sMemberId;
    private ProgressDialog pDialog;
    private OnMemberSelectListener mListener;
    private TextView Titile,btncancel;
    private AD_memberAdapter memberadapter;
    List<M_memberlist> MemberData = new ArrayList<M_memberlist>();
    SharedPreferences pref ;

    private String Objname;

    public Change_member_Dialog() {
        // Required empty public constructor
    }

    public static Change_member_Dialog newInstance(String sObjectName) {

        Change_member_Dialog Member_dialog = new Change_member_Dialog();
        Bundle args = new Bundle();

        args.putString(ARG_OBJNAME, sObjectName);

        Member_dialog.setArguments(args);

        return Member_dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pDialog = new ProgressDialog(getActivity());

        globalVariable = (AppController)getActivity().getApplicationContext();

        pref = getActivity().getSharedPreferences("Global",getActivity(). MODE_PRIVATE);

        getIntenet();

        if (getArguments() != null) {
            Objname = getArguments().getString(ARG_OBJNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_searchlist, container, false);

        lv = (ListView)rootView. findViewById(R.id.list_view);
        Titile =(TextView)rootView.findViewById(R.id.textView);
        btncancel=(TextView)rootView.findViewById(R.id.btncancel);
        get_family_detail_local();
        Titile.setText("Select Member");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                if (mListener != null) {
                    mListener.onSelectMember( (String)v.getTag(R.id.key_RelationShipId),(String) v.getTag(R.id.key_MemberName),(String) v.getTag(R.id.key_MemberImage));
                };

                dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dismiss();
            }
        });
        this.setCancelable(false);
        this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return rootView;
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

    private void Error(VolleyError error) {
        error.printStackTrace();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
            mListener = (OnMemberSelectListener) activity;
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
            pDialog.setMessage(sMessage);
            pDialog.show();
        }
    }
    public interface OnMemberSelectListener {
        public void onSelectMember(String RelashionShip_id, String name, String Imagename);
    }
    private void getIntenet()
    {
        sMemberId  = pref.getString("memberid", "");
    }

    private void get_family_detail_local()
    {
        showPdialog("loading.....");
        String m_Data=pref.getString("memberList","");

        try {
            MemberData.clear();
            M_memberlist memberdetails = new M_memberlist();

            memberdetails.setMemberId(Integer.parseInt(pref.getString("memberid", "")) );
            memberdetails.setMemberName("Me");
            memberdetails.setMemberGender(pref.getString("MemberGender", ""));
            memberdetails.setRelationshipId(8);
            memberdetails.setMemberDOB(pref.getString("MemberDOB", ""));
            memberdetails.setImageurl(pref.getString("ImageUrl",""));
            memberdetails.setPatientId("8");
            MemberData.add(memberdetails);


            JSONArray response = new JSONArray(m_Data);
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonobject = response.getJSONObject(i);
                memberdetails = new M_memberlist();
                memberdetails.setMemberId(jsonobject.optInt("MemberId"));
                memberdetails.setMemberName(jsonobject.optString("MemberName"));
                memberdetails.setMemberGender(jsonobject.optString("MemberGender"));
                memberdetails.setRelationshipId(jsonobject.optInt("RelationshipId"));
                memberdetails.setMemberDOB(jsonobject.optString("MemberDOB"));
                memberdetails.setImageurl(jsonobject.getString("ImageUrl"));
                memberdetails.setPatientId(jsonobject.getString("PatientId"));

                MemberData.add(memberdetails);

            }
            memberadapter=new AD_memberAdapter(getActivity(),MemberData,sMemberId);
            lv.setAdapter(memberadapter);
            hidePDialog();
        }catch (Exception e)
        {}
    }
}
package viroopa.com.medikart.buying.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.MoleculeInfoExpandableAdapter;
import viroopa.com.medikart.buying.model.MoleculeCategory;
import viroopa.com.medikart.buying.model.MoleculeItemDetail;


public class MoleculeInfoFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;
    private TextView activity_main_row_header;
    private String smolecules,sInfo,SMoleculeInteraction,sdrugInteraction;
    AppController globalVariable;
    static Button notifCount;
    static int mNotifCount = 0;
    private List<MoleculeCategory> catList= new ArrayList<MoleculeCategory>();;
    Integer nAddtocart_count = 0;
    private ExpandableListView exList;

    private JSONArray ja_objMoleculedetails,ja_objMedicinedetails;

    public static MoleculeInfoFragment newInstance(int position) {

        MoleculeInfoFragment f = new MoleculeInfoFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        globalVariable = (AppController)  getActivity().getApplicationContext();

        SharedPreferences pref = getActivity().getSharedPreferences("Global", Context.MODE_PRIVATE);
        //smolecules = pref.getString("product_moleculedetails", "[]");
        //sInfo = pref.getString("product_medicinedetails", "[]");

        if(globalVariable.getProduct_moleculedetails()!=null) {
            smolecules = globalVariable.getProduct_moleculedetails();
        }else{
            smolecules="[]";
        }

        if(globalVariable.getProduct_medicinedetails()!=null) {
            sInfo = globalVariable.getProduct_medicinedetails();
        }else{
            sInfo="[]";
        }
        if(globalVariable.getDrugInteraction()!=null) {
            sdrugInteraction = globalVariable.getDrugInteraction();
        }else{
            sdrugInteraction="[]";
        }
        if(globalVariable.getMoleculeinteraction()!=null) {
            SMoleculeInteraction = globalVariable.getMoleculeinteraction();
        }else{
            SMoleculeInteraction="[]";
        }

        //fillquestionAnswer();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        SharedPreferences pref = getActivity().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setNotifCount(int count){
        mNotifCount = count;
        //notifCount.setText(String.valueOf(mNotifCount));
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(ARG_POSITION);

        SharedPreferences pref = getActivity().getSharedPreferences("Global", Context.MODE_PRIVATE);
        String sAddtocart_count = pref.getString("addtocart_count", "0");
        Integer nAddtocart_count = Integer.parseInt(sAddtocart_count) ;
        setNotifCount(nAddtocart_count);

        View rootView = inflater.inflate(R.layout.page_molecule_info, container, false);
        exList = (ExpandableListView) rootView.findViewById(R.id.expandableListView1);
        activity_main_row_header= (TextView) rootView.findViewById(R.id.activity_main_row_header);
        getintent();
        Bind_expandable_MoleculeInteraction_view();
        MoleculeInfoExpandableAdapter exAdpt = new MoleculeInfoExpandableAdapter(catList, getActivity());
        exList.setAdapter(exAdpt);
        int count =  catList.size();
      /*  for (int i = 0; i <count ; i++)
            exList.expandGroup(i);
*/

        /*smolecules = pref.getString("product_moleculedetails", "[]");
        sInfo = pref.getString("product_medicinedetails", "[]");

        fillquestionAnswer();*/

        //bindvalue();

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



    private void Bind_expandable_MoleculeInteraction_view() {

        try {

            List<MoleculeItemDetail> result = null;

            ja_objMedicinedetails = new JSONArray(sInfo);
            ja_objMoleculedetails = new JSONArray(smolecules);


            MoleculeItemDetail item = null;
            MoleculeCategory cat1 = null;

            for (int i = 0; i < ja_objMoleculedetails.length(); i++) {
                try {
                    JSONObject obj_json = ja_objMoleculedetails.getJSONObject(i);


                    result = new ArrayList<MoleculeItemDetail>();

                    cat1 = new MoleculeCategory(Integer.parseInt(obj_json.getString("MoleId").trim()), obj_json.getString("MoleculeName").trim(), obj_json.getString("UOM").trim());

                    item = new MoleculeItemDetail(Integer.parseInt(obj_json.getString("MoleId").trim()), sInfo, SMoleculeInteraction, sdrugInteraction, "", obj_json.getString("MoleculeName").trim(), "");

                    result.add(item);
                    cat1.setItemList(result);
                    catList.add(cat1);


                } catch (Exception e) {
                    e.toString();

                }
            }


        } catch (JSONException e) {
            e.toString();
        }
    }
        private void getintent()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("Global", Context.MODE_PRIVATE);
        String productName=pref.getString("product_name","");
        activity_main_row_header.setText(productName);
    }


}

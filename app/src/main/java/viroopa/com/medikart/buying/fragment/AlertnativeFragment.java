package viroopa.com.medikart.buying.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.adapterproductalternative;
import viroopa.com.medikart.buying.model.M_productalternative;

/**
 * Created by prakash on 08/08/15.
 */
public class AlertnativeFragment extends Fragment {

    private String sMemberId;
    private String Sorting_Selected="";

    private static final String MEMBERID = "";
    private static final String ARG_POSITION = "position";
    private  String productname="",product_price="";
    //private static final String ALTERNATIVE = "productjson";
    private  RelativeLayout no_data_available;

    private int position;
    private String salternative;
    AppController globalVariable;
    static Button notifCount;
    static int mNotifCount = 0;

    List<M_productalternative> alternativelist = new ArrayList<M_productalternative>();
    adapterproductalternative alternative_adapter;

    private Menu mToolbarMenu;
    Integer nAddtocart_count = 0;


    public static AlertnativeFragment newInstance(int position,String MemberId) { //} JSONArray ja_objotherProductdetails) {

        //,JSONArray ja_alternative
        AlertnativeFragment f = new AlertnativeFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        //b.putString(ALTERNATIVE, ja_objotherProductdetails.toString());
        b.putString(MEMBERID, MemberId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        globalVariable = (AppController) getActivity().getApplicationContext();
        SharedPreferences pref = getActivity().getSharedPreferences("Global", Context.MODE_PRIVATE);

        //salternative = pref.getString("product_otherproductdetails", "[]";
        if(globalVariable.getProduct_otherproductdetails()!=null) {
            salternative = globalVariable.getProduct_otherproductdetails();
        }
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
        //salternative = getArguments().getString(ALTERNATIVE);
        sMemberId = getArguments().getString(MEMBERID);

        SharedPreferences pref = getActivity().getSharedPreferences("Global", Context.MODE_PRIVATE);
        String sAddtocart_count = pref.getString("addtocart_count", "0");
        Integer nAddtocart_count = Integer.parseInt(sAddtocart_count) ;
        setNotifCount(nAddtocart_count);
        getIntenet();
        //salternative = pref.getString("product_otherproductdetails", "[]");

        View rootView ;
        rootView = inflater.inflate(R.layout.page_alternative, container, false);

        alternative_adapter = new adapterproductalternative(getActivity(),alternativelist,sMemberId);
        ListView UI_lv = (ListView)rootView.findViewById(R.id.list_alternative);
        no_data_available = (RelativeLayout)rootView.findViewById(R.id.no_data_available);
        TextView txt_medicine= (TextView)rootView.findViewById(R.id.txt_medicine);

        TextView sort_btn= (TextView)rootView.findViewById(R.id.sort_btn);
        final TextView inputSearch= (TextView)rootView.findViewById(R.id.inputSearch);
        TextView tx_sub_description= (TextView)rootView.findViewById(R.id.tx_sub_description);
        txt_medicine.setText(productname);
        tx_sub_description.setText("Substitute of " + productname);


        UI_lv.setAdapter( alternative_adapter);

        insertdatatoadapter();

        sort_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

           /*     Collections.sort(alternativelist, new Comparator<M_productalternative>(){
                    public int compare(M_productalternative obj1, M_productalternative obj2)
                    {
                        // TODO Auto-generated method stub
                        return obj1.getOtherManufacture().compareToIgnoreCase(obj2.getOtherManufacture());
                    }
                });

                Collections.sort(alternativelist, new Comparator<M_productalternative>(){
                    public int compare(M_productalternative obj1, M_productalternative obj2)
                    {
                        // TODO Auto-generated method stub
                        return (Double.parseDouble(obj1.getOtherPrice()) < Double.parseDouble(obj2.getOtherPrice())) ? -1: (Double.parseDouble(obj1.getOtherPrice()) > Double.parseDouble(obj2.getOtherPrice())) ? 1:0 ;
                    }
                });*/
               /* Collections.sort(alternativelist, new Comparator<M_productalternative>(){
                    public int compare(M_productalternative obj1, M_productalternative obj2)
                    {
                        // TODO Auto-generated method stub
                        return (Double.parseDouble(obj1.getOtherPrice()) > Double.parseDouble(obj2.getOtherPrice())) ? -1: (Double.parseDouble(obj1.getOtherPrice()) > Double.parseDouble(obj2.getOtherPrice())) ? 1:0 ;
                    }
                });*/

                sorting_dialog();


            }
        });



        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text



            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = inputSearch.getText().toString();
                alternative_adapter.filter(text);

            }
        });

        return rootView;
    }


    private void insertdatatoadapter() {
        try {
            // Parsing json array response
            // loop through each json object

            JSONArray response = new JSONArray(salternative);
            for (int i = 0; i < response.length(); i++) {

                JSONObject obj_json = (JSONObject) response.get(i);

                M_productalternative O_productalternative = new M_productalternative();

                O_productalternative.setOtherProductName(obj_json.getString("OtherProductName"));
                O_productalternative.setOtherPackSizeproduct(obj_json.getString("OtherPackSizeproduct"));
                O_productalternative.setOtherPrice(obj_json.getString("OtherPrice"));
                O_productalternative.setOtherProductId(obj_json.getString("OtherProductId"));
                O_productalternative.setOtherManufacture(obj_json.getString("ManufactureName"));
                O_productalternative.setPricePercent(obj_json.getString("save_per"));
                O_productalternative.setImagepath(obj_json.getString("MainImagePath"));

                String price=obj_json.getString("OtherPrice");



                alternativelist.add(O_productalternative);
            }

            if(response.length()<1)
            {
                no_data_available.setVisibility(View.VISIBLE);
            }else
            {
                no_data_available.setVisibility(View.GONE);
            }

            salternative = "[]";

            alternative_adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void getIntenet() {

        SharedPreferences pref = getActivity().getSharedPreferences("Global",Context.MODE_PRIVATE);

        productname = pref.getString("product_name", "");
        product_price = pref.getString("product_price", "");
    }
    public class IdSorter implements Comparator<M_productalternative> {

        public int compare(M_productalternative anEmployee, M_productalternative anotherEmployee) {
            return anEmployee.getOtherManufacture().compareTo(anotherEmployee.getOtherManufacture());
        }
    }

    private void sorting_dialog() {

        final String sorting_options[] = {"Price-Low to High", "Price-High to Low"};
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View dialogview = inflater.inflate(R.layout.sorting_alternate_data, null);


        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);
        final ListView lv = (ListView) dialogview.findViewById(R.id.list_view);


        ArrayAdapter arradapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, android.R.id.text1, sorting_options);

        lv.setAdapter(arradapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        builder.setView(dialogview);
        final Dialog dlg= builder.create();

        if (Arrays.asList(sorting_options).contains(Sorting_Selected)) {
            for (int i = 0; i < sorting_options.length; i++) {
                if (Arrays.asList(sorting_options[i]).contains(Sorting_Selected) ){
                    lv.setItemChecked(i, true);
                }
                // return i;
            }
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                CheckedTextView textView = (CheckedTextView) v;
                textView.setChecked(false);


                if (position == 0) {
                    Sorting_Selected = "Price-Low to High";

                    Collections.sort(alternativelist, new Comparator<M_productalternative>() {
                        public int compare(M_productalternative obj1, M_productalternative obj2) {
                            // TODO Auto-generated method stub
                            return (Double.parseDouble(obj1.getOtherPrice()) < Double.parseDouble(obj2.getOtherPrice())) ? -1 : (Double.parseDouble(obj1.getOtherPrice()) > Double.parseDouble(obj2.getOtherPrice())) ? 1 : 0;
                        }
                    });
                    alternative_adapter.notifyDataSetChanged();
                    dlg.dismiss();
                }
                if (position == 1) {
                    Sorting_Selected = "Price-High to Low";
                    Collections.sort(alternativelist, new Comparator<M_productalternative>() {
                        public int compare(M_productalternative obj1, M_productalternative obj2) {
                            // TODO Auto-generated method stub
                            return (Double.parseDouble(obj1.getOtherPrice()) > Double.parseDouble(obj2.getOtherPrice())) ? -1 : (Double.parseDouble(obj1.getOtherPrice()) > Double.parseDouble(obj2.getOtherPrice())) ? 1 : 0;
                        }
                    });
                    alternative_adapter.notifyDataSetChanged();
                    dlg.dismiss();
                }
            }


        });



        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });


        dlg.show();
    }
}

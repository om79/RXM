package viroopa.com.medikart.buying;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.easebuzz.payment.kit.PWECouponsActivity;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datamodels.StaticDataModel;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.TermsCondition;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.buying.adapter.adapterprice;
import viroopa.com.medikart.buying.model.M_pricing;
import viroopa.com.medikart.buying.model.addressModel;
import viroopa.com.medikart.helper.SQLiteHandler;

/**
 * Created by prakash on 13/08/15.
 */
public class ShippingAddress extends AppCompatActivity  {

    private String sMemberId,sCardId,sTransdId="",sUsername="",sEmailId="",sPhoneNumber="";
    private   Boolean Success_pincode=false;
    private EditText input_name,input_address,landamrk,pincode,phoneno;
    private EditText sp_state,sp_city;
    private Button btnRegister,btnaddaddress;
    private JSONArray ja_state,ja_city;
    private ProgressDialog pDialog;
    private adapterprice adapterprice;
    private String Address_id_for_edit="";
    private String Total_price;

    Float number = null;
    private SQLiteHandler db;
    private String AddressTitile;
    private String Address_person_name;
    private String Saddressone,Saddresstwo;
    private String Slandmark;
    private String Spincode;
    private String Sphoneno;
    private String Scity;
    private Integer nCityId;
    private  Boolean isAddressAvailable=false;
    private AdressAdapter addressadapter;
    private boolean ignoreOnLoadTextChange = true;
    private  ListView lst_address;
    Integer selected_position = -1;
    private  RelativeLayout nextlayout,previouslayout;
    private  Integer Adress_id=-99;
    List<M_pricing> pricelist = new ArrayList<M_pricing>();
    public static final String TAG = "PayUMoney";

    private  static String Mobile_pattern=  "^[789]\\d{9}$";//"^[789]\d{9}$";
    ArrayList<addressModel> Useraddress=new ArrayList<addressModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shippingaddress);

        db = new SQLiteHandler(getApplicationContext());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        pDialog = new ProgressDialog(this);

        sp_state = (EditText) findViewById(R.id.sp_state);
        sp_city = (EditText) findViewById(R.id.sp_city);

        lst_address=(ListView) findViewById(R.id.lst_address);
        getIntenet();

        input_name = (EditText) findViewById(R.id.input_name);
        input_address = (EditText) findViewById(R.id.input_address);
        landamrk = (EditText) findViewById(R.id.landamrk);
        pincode = (EditText) findViewById(R.id.pincode);
        phoneno = (EditText) findViewById(R.id.phoneno);

        nextlayout= (RelativeLayout) findViewById(R.id.nextlayout);
        previouslayout= (RelativeLayout) findViewById(R.id.previouslayout);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnaddaddress= (Button) findViewById(R.id.btnaddaddress);

        //load_state();
        // load_city("");


        addressadapter=new AdressAdapter(this,Useraddress);

        lst_address.setAdapter(addressadapter);
        lst_address.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        get_shippingaddress();


        nextlayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("goingTONext","Click TO Next");
                Log.e("goingTONext","Click TO Next");

                Save_Checkout_confirmation_dialog();

                /*if (Adress_id != -99) {
                    if(isAddressAvailable) {
                        Save_Checkout_confirmation_dialog();
                    }else{
                        Toast.makeText(ShippingAddress.this, "Address field is missing in selected adress.Please update the address to continue", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ShippingAddress.this, "Please select address to continue", Toast.LENGTH_LONG).show();
                }*/


            }
        });
        previouslayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnaddaddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addAdressDialog("A");
            }
        });

        pincode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


                if (pincode.length() == 6) {
                    //getcitylistWisePincode(pincode.getText().toString());
                } else {
                    //f_alert_ok("Pincode","please enter proper pincode");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        pincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pincode.length() != 6) {
                        f_alert_ok("Pincode", "please enter proper pincode");
                    }

                }
            }
        });


        sp_state.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                f_alert_ok("Pincode", "please enter pincode to get state");

            }
        });
        sp_city.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                f_alert_ok("City", "please enter pincode to get city");



            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


            }
        });
    }

    private void get_shippingaddress() {

        showPdialog("loading....");
        String url = String.format(AppConfig.URL_GET_MULTIPLE_ADDRESS, sMemberId);

        RequestQueue queue = Volley.newRequestQueue(ShippingAddress.this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Success_shippingaddress(response);
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Error_shippingaddress(error);
                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);
    }

    private void Success_shippingaddress(JSONObject response) {
        try
        {
            Log.d("getResponced", String.valueOf(response));
            Useraddress.clear();
            JSONArray j_array= response.getJSONArray("checkoutAddessDtlModel");


            for (int i = 0; i < j_array.length(); i++) {

                JSONObject addressjson = (JSONObject)j_array.get(i);
                addressModel O_address=new addressModel();
                O_address.setAddress(addressjson.getString("Address"));
                O_address.setAdressid(addressjson.getString("AddressId"));
                O_address.setAdressHeadin(addressjson.getString("Title"));
                O_address.setCity(addressjson.getString("CityName"));
                O_address.setName(addressjson.getString("PersonName"));
                O_address.setPhone_number(addressjson.getString("Phone"));
                O_address.setPincode(addressjson.getString("PinCode"));
                O_address.setLandMark(addressjson.getString("LandMark"));
                O_address.setState(addressjson.getString("StateName"));
                O_address.setDistrict_id(addressjson.getString("District_ID"));


                Useraddress.add(O_address);

            }
            addressadapter.notifyDataSetChanged();


        } catch (Exception e) {

            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Error_shippingaddress(VolleyError error) {
        try {
        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(this, "Error: Network not available" , Toast.LENGTH_LONG).show();
        }
    }

    private void PostShippingaddress(String p_name, final String address,String pincode,String phoneNr,String landmark,String district_id ) {

        showPdialog("Getting Pricing Details ...");



        Map<String, String> params = new HashMap<String, String>();


        params.put("MemberId", sMemberId);
        params.put("iCartId", sCardId);
        params.put("PersonName",p_name );
        params.put("Address", address);
        params.put("PinCode", pincode);
        params.put("Phone", phoneNr);
        params.put("LandMark",landmark);
        params.put("District_ID", district_id);
        params.put("ApplicableFor", "M");

        RequestQueue queue = Volley.newRequestQueue(ShippingAddress.this);

        JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_SAVESHIPPINGDETAILS,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        postinhurrySuccess(response);
                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        postinhurryerror(error);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("charset", "utf-8");
                headers.put("User-agent", "Buying");
                return headers;
            }
        };

        jor_inhurry_post.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(jor_inhurry_post);
    }

    private void postinhurrySuccess(JSONObject response) {
        try
        {
            if (response.getBoolean("bReturnFlag") == true)
            {
                hidePDialog();
                get_pricing_detais();

            }
            else {
                hidePDialog();
                //  f_alert_ok("Information ",response.getString("bReturnFlag"));
            }

        } catch (JSONException e) {
            hidePDialog();
            e.printStackTrace();
            f_alert_ok("Error ", e.getMessage());
        }
    }

    private void postinhurryerror(VolleyError error) {

        // mb.showAlertDialog(this, "Registration", "error" + error.getMessage(), true);
    }

    private void Post_save_checkout() {

        Log.d("clickTocard11","ys Click");

        showPdialog("loading...");

        Map<String, String> params = new HashMap<String, String>();

        params.put("iMemberId", sMemberId);
        params.put("iCartId",sCardId);
        params.put("TimeSlot", "");
        params.put("EmergencyDate", "");
        params.put("Emergencytime", "");
        params.put("RequestFrom", "A");

        Log.d("clickTocard12",sMemberId+""+sCardId);

        RequestQueue queue = Volley.newRequestQueue(ShippingAddress.this);

        JsonObjectRequest jor_savecheckout_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_SAVECHECKOUT,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("clickTocard1123", String.valueOf(response));
                        post_save_checkoutSuccess(response,"false");
                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        post_save_checkouterror(error);
                    }
                }) {


        };
        jor_savecheckout_post.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jor_savecheckout_post);
    }

    private void post_save_checkoutSuccess(JSONObject response,String payment_id) {
        try
        {
            Log.d("postsaveResponce",response+""+payment_id);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cartid", "");
            editor.putString("addtocart_count", "0");
            editor.commit();

            show_oder_placed(response.getString("OrderId"),response.getString("OrderNo"),payment_id);
            finish();

        } catch (JSONException e) {
            hidePDialog();
            e.printStackTrace();
            f_alert_ok("Error ",e.getMessage());
        }
    }

    private void Showbuy(){
        Intent Intenet_buy = new Intent(ShippingAddress.this, BuyMainActivity.class);
        startActivity(Intenet_buy);
    }

    private void post_save_checkouterror(VolleyError error) {
        // f_alert_ok("Error", error.getMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_threedot_forall, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_transaction:
                Show_Order_Transaction();
                return true;
            case R.id.return_cancel_policies:
                Show_Return_cancel_policies();
                return true;
            case R.id.termscondition:
                Show_termscondition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void Show_Order_Transaction() {
        Intent Intenet_change = new Intent(ShippingAddress.this, Order_Transaction.class);
        startActivity(Intenet_change);
    }
    public void Show_Return_cancel_policies() {
        Intent Intenet_change = new Intent(ShippingAddress.this, TermsCondition.class);
        startActivity(Intenet_change);
    }
    public void Show_termscondition() {
        Intent Intenet_change = new Intent(ShippingAddress.this, TermsCondition.class);
        startActivity(Intenet_change);
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


    private void getIntenet()
    {
        try
        {


            ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
            test.add(db.getUserDetails());

            HashMap<String, String> m = test.get(0);

            sUsername=m.get("name");
            sEmailId=m.get("email");
            sPhoneNumber=m.get("phoneno");


            SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
            sMemberId = pref.getString("memberid", "");
            sCardId = pref.getString("cartid", "");
            ja_state = new JSONArray(pref.getString("statelist", ""));
            ja_city = new JSONArray(pref.getString("citylist", ""));


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void show_oder_placed(String orderid,String orderno,String payment_id)
    {
        Intent Intenet_order = new Intent(ShippingAddress.this, orderPlacedData.class);
        Intenet_order.putExtra("orderid",orderid);
        Intenet_order.putExtra("orderNO",orderno);
        Intenet_order.putExtra("payment_id",payment_id);
        Intenet_order.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Intenet_order);
        finish();
    }


    private void getcitylistWisePincode(String pincode,final EditText State,final EditText city) {
        showPdialog("please wait ...");

        String tag_string_req = "req_city";

        // final messagebox mb = new messagebox();
        //String tag_city_list = "json_city";


        String uri = String.format(AppConfig.URL_GET_CITY_ON_PINCODE, pincode);

        RequestQueue queue = Volley.newRequestQueue(ShippingAddress.this);

        JsonObjectRequest cityrequest = new JsonObjectRequest(Request.Method.GET,uri,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, "Register Response: " + response.toString());


                        Success_CityOn_pincode(response,State,city);
                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d(TAG, "Register Response: " + error.getMessage());
                        hidePDialog();

                        f_alert_ok("Error",error.toString());
                    }
                });

        cityrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(cityrequest, tag_string_req);

        queue.add(cityrequest);

    }









    private void Success_CityOn_pincode(JSONObject response,final EditText s_state,final EditText s_city) {
        // final messagebox mb = new messagebox();
        try {
            //citylist.clear();

            String city_id = response.getString("CityId");
            String city_name = response.getString("CityName");
            String StateName=response.getString("StateName");
            if (Integer.parseInt(city_id)==0)
            {
                s_city.setText("");
                f_alert_ok("pincode","pincode is not proper");
            }
            else {
                //selected_cityid=Integer.parseInt(city_id);

                s_city.setText(city_name);
                s_state.setText(StateName);
                nCityId = Integer.parseInt(city_id);
                Scity = nCityId.toString();
            }



        } catch (Exception e) {
            hidePDialog();
            Log.e("Error", e.getMessage());
            // mb.showAlertDialog(ShippingAddress.this, "Error", e.getMessage(), true);
            e.printStackTrace();
        }

    }

    public class AdressAdapter extends ArrayAdapter<addressModel> {
        public AdressAdapter(Context context, ArrayList<addressModel> address) {
            super(context, 0, address);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position


            addressModel addressdata = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.adress_item, parent, false);
            }
            // Lookup view for data population
            final RelativeLayout Main_rel_address = (RelativeLayout) convertView.findViewById(R.id.Main_rel_address);
            final CheckBox chk_address = (CheckBox) convertView.findViewById(R.id.address_chk);
            final TextView address_name = (TextView) convertView.findViewById(R.id.address_name);
            final TextView adress_detail = (TextView) convertView.findViewById(R.id.address_details);
            final TextView DeleletAdress= (TextView) convertView.findViewById(R.id.DeleletAdress);
            final TextView address_City= (TextView) convertView.findViewById(R.id.address_City);
            final TextView address_State= (TextView) convertView.findViewById(R.id.address_State);
            final TextView address_Phone_number= (TextView) convertView.findViewById(R.id.address_Phone_number);

            DeleletAdress.setTag(R.id.key_product_id, addressdata.getAdressid());

            chk_address.setTag(R.id.key_product_id, addressdata.getAdressid());
            chk_address.setTag(R.id.key_adress_check, addressdata.getAddress());
            chk_address.setTag(R.id.key_adress_person_name, addressdata.getName());
            chk_address.setTag(R.id.key_adress_adress, addressdata.getAddress()+", "+addressdata.getCity()+", "+addressdata.getState());
            chk_address.setTag(R.id.key_adress_landmark, addressdata.getLandMark());
            chk_address.setTag(R.id.key_adress_phone, addressdata.getPhone_number());
            chk_address.setTag(R.id.key_adress_pincode, addressdata.getPincode());
            chk_address.setTag(R.id.key_adress_district_id, addressdata.getDistrict_id());



            Main_rel_address.setTag(R.id.key_product_id, addressdata.getAdressid());

            chk_address.setText(addressdata.getAdressHeadin());
            adress_detail.setText(addressdata.getAddress());
            address_City.setText(addressdata.getCity()+" - "+addressdata.getPincode());
            address_State.setText(addressdata.getState());
            address_Phone_number.setText(addressdata.getPhone_number());
            address_name.setText(addressdata.getName());


            if(position==selected_position)
            {
                chk_address.setChecked(true);
                chk_address.setTextColor(Color.parseColor("#e47219"));
                adress_detail.setTextColor(Color.parseColor("#08446a"));
                address_City.setTextColor(Color.parseColor("#08446a"));
                address_State.setTextColor(Color.parseColor("#08446a"));
                address_Phone_number.setTextColor(Color.parseColor("#08446a"));
                address_name.setTextColor(Color.parseColor("#08446a"));
            }
            else
            {
                chk_address.setChecked(false);
                chk_address.setTextColor(Color.parseColor("#999999"));
                adress_detail.setTextColor(Color.parseColor("#999999"));
                address_City.setTextColor(Color.parseColor("#999999"));
                address_State.setTextColor(Color.parseColor("#999999"));
                address_Phone_number.setTextColor(Color.parseColor("#999999"));
                address_name.setTextColor(Color.parseColor("#999999"));
            }




            chk_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    try {
                        if ((String) view.getTag(R.id.key_adress_check) == null || ((String) view.getTag(R.id.key_adress_check)).equals("")) {
                            isAddressAvailable=false;
                        }else
                        {
                            isAddressAvailable=true;
                        }


                        if (((CheckBox) view).isChecked()) {
                            selected_position = position;
                            Adress_id = Integer.parseInt((String) view.getTag(R.id.key_product_id));



                            showPdialog("Checking address. . .");

                            String url = String.format(AppConfig.URL_GET_CHECK_PINCODE,(String) view.getTag(R.id.key_adress_pincode));

                            RequestQueue queue = Volley.newRequestQueue(ShippingAddress.this);

                            JsonObjectRequest jsonObjReq_remove = new JsonObjectRequest(Request.Method.GET,
                                    url,  new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {

                                        if (response.getBoolean("bReturnFlag") == true) {

                                            nextlayout.setEnabled(true);
                                            nextlayout.setActivated(true);
                                            Success_pincode = true;
                                            PostShippingaddress((String) view.getTag(R.id.key_adress_person_name), (String) view.getTag(R.id.key_adress_adress), (String) view.getTag(R.id.key_adress_pincode), (String) view.getTag(R.id.key_adress_phone),
                                                    (String) view.getTag(R.id.key_adress_landmark), (String) view.getTag(R.id.key_adress_district_id));
                                        }else
                                        {
                                            Success_pincode= false;
                                            pincode_service_unavailable_dialog();
                                            nextlayout.setEnabled(false);
                                            nextlayout.setActivated(false);
                                            hidePDialog();
                                        }

                                    }catch (JSONException e)
                                    {
                                        hidePDialog();
                                    }
                                }
                            },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            hidePDialog();
                                            Success_pincode= false;
                                            pincode_service_unavailable_dialog();
                                            nextlayout.setEnabled(false);
                                            nextlayout.setActivated(false);
                                            //   Error_remove(error);

                                        }
                                    });



                            queue.add(jsonObjReq_remove);




                        } else {
                            selected_position = -1;
                            Adress_id=-99;
                        }

                        notifyDataSetChanged();


                    }catch (Exception e)
                    {
                        e.toString();
                    }
                }

            });



            DeleletAdress.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    String address_id = (String) view.getTag(R.id.key_product_id);
                    delete_confirmation_dialog(address_id);

                }
            });
            Main_rel_address.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    String address_id = (String) view.getTag(R.id.key_product_id);
                    Address_id_for_edit=address_id;
                    for(int i = 0; i < Useraddress.size(); i++) {

                        addressModel o_address= Useraddress.get(i);
                        if(o_address.getAdressid().equals(address_id))
                        {
                            AddressTitile =o_address.getAdressHeadin();
                            Saddressone =o_address.getAddress();
                            // Saddresstwo =o_address.getAddress();
                            Slandmark =o_address.getLandMark();
                            Spincode =o_address.getPincode();
                            Sphoneno =o_address.getPhone_number();
                            Address_person_name=o_address.getName();
                        }

                    }



                    addAdressDialog("E");

                }
            });





            return convertView;
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private  void addAdressDialog(final String Mode)
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.add_address_dialog, null);

        final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);

        final TextView textView_main_title = (TextView) dialogview.findViewById(R.id.textView_main_title);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);

        final EditText adres_title = (EditText) dialogview.findViewById(R.id.adres_title);
        final EditText input_address_one = (EditText) dialogview.findViewById(R.id.input_address_one);
        final EditText input_address_two = (EditText) dialogview.findViewById(R.id.input_address_two);
        final EditText edt_pincode = (EditText) dialogview.findViewById(R.id.edt_pincode);
        final EditText edt_state = (EditText) dialogview.findViewById(R.id.edt_state);
        final EditText edt_city = (EditText) dialogview.findViewById(R.id.edt_city);
        final EditText edt_landamrk = (EditText) dialogview.findViewById(R.id.edt_landamrk);
        final EditText edt_phoneno = (EditText) dialogview.findViewById(R.id.edt_phoneno);
        final EditText input_person_nmae = (EditText) dialogview.findViewById(R.id.input_person_nmae);


        if(Mode.equals("E"))
        {
            textView_main_title.setText("Edit Address");
            okbtn.setText("Update");
            adres_title.setText(AddressTitile);
            input_address_one.setText(Saddressone);
            edt_pincode.setText(Spincode);
            edt_landamrk.setText(Slandmark);
            edt_phoneno.setText(Sphoneno);
            getcitylistWisePincode(edt_pincode.getText().toString(),edt_state,edt_city);
            input_person_nmae.setText(Address_person_name);
        }

        builder.setView(dialogview);
        final Dialog dlg= builder.create();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });


        edt_pincode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


                if (edt_pincode.length() == 6) {
                    getcitylistWisePincode(edt_pincode.getText().toString(),edt_state,edt_city);
                } else {
                    //f_alert_ok("Pincode","please enter proper pincode");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        edt_pincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (edt_pincode.length() != 6) {
                        f_alert_ok("Pincode", "please enter proper pincode");
                    }

                }
            }
        });
        edt_state.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                f_alert_ok("Pincode", "please enter pincode to get state");

            }
        });
        edt_city.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                f_alert_ok("City", "please enter pincode to get city");


            }
        });


        // Integer nCityId = currentcity.get(sp_city.getSelectedItemPosition()).getCityId();
        //Scity = nCityId.toString();

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AddressTitile = adres_title.getText().toString();
                Saddressone = input_address_one.getText().toString();
                Saddresstwo = input_address_two.getText().toString();
                Slandmark = edt_landamrk.getText().toString();
                Spincode = edt_pincode.getText().toString();
                Sphoneno = edt_phoneno.getText().toString();
                Address_person_name = input_person_nmae.getText().toString();


                if (!AddressTitile.isEmpty()) {
                    if (!Address_person_name.isEmpty()) {
                        if (!Saddressone.isEmpty() || !Saddresstwo.isEmpty()) {
                            //if(!Slandmark.isEmpty()) {
                            if (!Spincode.isEmpty()&&Spincode.length()==6) {
                                if (!Sphoneno.isEmpty()) {
                                    if (true ==Sphoneno.matches(Mobile_pattern)) {

                                        if (Mode.equals("E")) {
                                            EditMemmberAddress();
                                        }else
                                        {
                                            addMemmberAddress();
                                        }


                                        dlg.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please enter valid  phone number!", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please enter phone number!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please enter valid pincode!", Toast.LENGTH_LONG).show();
                            }
                            // }else{Toast.makeText(getApplicationContext(), "Please enter landmark!", Toast.LENGTH_LONG).show();}
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter address!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a Title!", Toast.LENGTH_LONG).show();
                    }
                }

                else

                {
                    Toast.makeText(getApplicationContext(), "Please enter name!", Toast.LENGTH_LONG).show();
                }
            }
        });

        dlg.show();
    }


    private void addMemmberAddress()
    {
        showPdialog("loading...");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        String sCardId = pref.getString("cartid", "");

        Map<String, String> params = new HashMap<String, String>();

        params.put("DistrictId", Scity);
        params.put("MemberId", sMemberId);
        params.put("Address", Saddressone+", "+Saddresstwo);
        params.put("AddressTitle", AddressTitile);
        params.put("AddressType", "oth");
        params.put("PinCode", Spincode);
        params.put("Phone", Sphoneno);
        params.put("LandMark", Slandmark);
        params.put("PersonName", Address_person_name);
        params.put("CityId", Scity);



        RequestQueue queue = Volley.newRequestQueue(ShippingAddress.this);

        JsonObjectRequest jor_ADD_ADDRESS_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_ADD_MEMBER_ADDRESS,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        success_add_address(response);
                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        add_address_error(error);
                    }
                });

        jor_ADD_ADDRESS_post.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        queue.add(jor_ADD_ADDRESS_post);
    }
    private void success_add_address(JSONObject sResponse)
    {
        Success_shippingaddress(sResponse);
        Toast.makeText(getApplicationContext(), "Address added successfully", Toast.LENGTH_LONG).show();
    }
    private void add_address_error(VolleyError error) {

        Toast.makeText(getApplicationContext(), "error" + error.getMessage(), Toast.LENGTH_LONG).show();

    }


    private void EditMemmberAddress()
    {
        showPdialog("loading...");


        Map<String, String> params = new HashMap<String, String>();

        params.put("AddressId", Address_id_for_edit);
        params.put("DistrictId", Scity);
        params.put("MemberId", sMemberId);
        params.put("Address", Saddressone+", "+Saddresstwo);
        params.put("AddressTitle", AddressTitile);
        params.put("AddressType", "oth");
        params.put("PinCode", Spincode);
        params.put("Phone", Sphoneno);
        params.put("LandMark", Slandmark);
        params.put("PersonName", Address_person_name);
        params.put("CityId", Scity);



        RequestQueue queue = Volley.newRequestQueue(ShippingAddress.this);

        JsonObjectRequest jor_ADD_ADDRESS_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_EDIT_MEMBER_ADDRESS,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        success_edit_address(response);
                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        add_address_error(error);
                    }
                });

        jor_ADD_ADDRESS_post.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        queue.add(jor_ADD_ADDRESS_post);
    }
    private void success_edit_address(JSONObject sResponse)
    {
        selected_position = -1;
        Adress_id=-99;
        Success_shippingaddress(sResponse);
        Toast.makeText(getApplicationContext(), "Address edited successfully", Toast.LENGTH_LONG).show();
    }

    private void Save_Checkout_confirmation_dialog()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.checkout_confirmation_dialog, null);

        final TextView okbtn = (TextView) dialogview.findViewById(R.id.btnok);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);
        final RadioGroup rdb_group_selection = (RadioGroup) dialogview.findViewById(R.id.rdb_group_selection);

        builder.setView(dialogview);
        final Dialog dlg= builder.create();





        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                switch (rdb_group_selection.getCheckedRadioButtonId()) {
                    case -1:
                        show_snack_bar( "Please select a payment method to continue");
                        break;
                    case R.id.rdo_PayUmoney:
                        // makePayment();
                        break;
                    case R.id.rdo_Cod:
                        Post_save_checkout();
                        break;
                    case R.id.rdo_easebuzz:
                        make_payment_ebizz();
                        break;

                }

                dlg.dismiss();



            }
        });
        dlg.show();


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private boolean isValidPhone(String sphoneno)
    {

        Boolean ph=false;

        ph=Patterns.PHONE.matcher(sphoneno).matches();

        return ph;
    }
    private void Delete_address(String Address_id) {
        showPdialog("please wait ...");



        String uri = String.format(AppConfig.URL_GET_DELETE_ADDRESS, Address_id,sMemberId);

        RequestQueue queue = Volley.newRequestQueue(ShippingAddress.this);

        JsonObjectRequest cityrequest = new JsonObjectRequest(Request.Method.GET,uri,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, "Register Response: " + response.toString());

                        success_delete_address(response);

                        hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d(TAG, "Register Response: " + error.getMessage());
                        hidePDialog();

                        // f_alert_ok("Error",error.toString());
                    }
                });

        cityrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(cityrequest, tag_string_req);

        queue.add(cityrequest);

    }

    private void success_delete_address(JSONObject sResponse)
    {
        Success_shippingaddress(sResponse);
        Toast.makeText(getApplicationContext(), "Address Deleted successfully", Toast.LENGTH_LONG).show();
    }

    private void delete_confirmation_dialog(final String id) {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.checkout_confirmation_dialog, null);

        final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);

        final TextView textView_main_heading = (TextView) dialogview.findViewById(R.id.textView_main_heading);
        final TextView msg = (TextView) dialogview.findViewById(R.id.msg);

        textView_main_heading.setText("Delete Address");
        msg.setText("Do you want to delete this address");
        okbtn.setText("Delete");

        builder.setView(dialogview);
        final Dialog dlg= builder.create();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Delete_address(id);
                dlg.dismiss();
            }
        });
        dlg.show();
    }
    private void show_final_price_summary_dialog()    //this will implement later
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.price_summary_dialog, null);

        final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);
        final ListView lst_price = (ListView) dialogview.findViewById(R.id.lst_price);
        final TextView TotalPrice = (TextView) dialogview.findViewById(R.id.TotalPrice);

        adapterprice = new adapterprice(this, pricelist, sMemberId);
        lst_price.setAdapter(adapterprice);

        builder.setView(dialogview);
        final Dialog dlg= builder.create();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        Double double_price=0.00;
        DecimalFormat df = new DecimalFormat("#.00");
        if(Total_price!=null && Total_price!="") {
            double_price = Double.parseDouble(Total_price);
        }

        TotalPrice.setText("Rs." + " " + df.format(double_price).toString());

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                dlg.dismiss();
            }
        });
        dlg.show();
    }
    private void get_pricing_detais()
    {
        showPdialog("getting prcing details. . .");

        String url = String.format(AppConfig.URL_GET_PRICING_DETAIL,sCardId);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonObjReq_remove = new JsonArrayRequest(Request.Method.GET,
                url,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                success_pricing(response);
                hidePDialog();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        //   Error_remove(error);

                    }
                });



        queue.add(jsonObjReq_remove);
    }
    private void success_pricing(JSONArray response)
    {

        insertdatatoadapter(response);
    }

    public void insertdatatoadapter(JSONArray response) {
        try {
            Log.e("response111",response.toString());
            pricelist.clear();
            for (int i = 0; i < response.length(); i++) {

                JSONObject cartjson = (JSONObject) response.get(i);

                M_pricing O_pricing = new M_pricing();
                O_pricing.setDiscription(cartjson.getString("Discription"));
                O_pricing.setAmount(cartjson.getString("Amount"));
                if(cartjson.getString("Discription").equals("Grand Total"))
                {
                    if(!cartjson.getString("Amount").equals("")  || cartjson.getString("Amount")!=null)
                    {
                        Total_price=cartjson.getString("Amount");
                        Log.e("price",Total_price);
                        //Total_price="1";
                    }
                }
                pricelist.add(O_pricing);
            }
            show_final_price_summary_dialog();

        } catch (JSONException e) {
            e.printStackTrace();
            // Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Boolean Check_pin_code(String Pincode)
    {


        showPdialog("Checking address. . .");

        String url = String.format(AppConfig.URL_GET_CHECK_PINCODE,Pincode);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_remove = new JsonObjectRequest(Request.Method.GET,
                url,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getBoolean("bReturnFlag") == true) {

                        nextlayout.setEnabled(true);
                        nextlayout.setActivated(true);
                        Success_pincode = true;
                    }else
                    {
                        Success_pincode= false;
                        pincode_service_unavailable_dialog();
                        nextlayout.setEnabled(false);
                        nextlayout.setActivated(false);
                        hidePDialog();
                    }

                }catch (JSONException e)
                {
                    hidePDialog();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Success_pincode= false;
                        pincode_service_unavailable_dialog();
                        nextlayout.setEnabled(false);
                        nextlayout.setActivated(false);
                        //   Error_remove(error);

                    }
                });



        queue.add(jsonObjReq_remove);
        return  Success_pincode;
    }

    private void pincode_service_unavailable_dialog()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.checkout_confirmation_dialog, null);

        final TextView okbtn = (TextView) dialogview.findViewById(R.id.btnok);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);
        final TextView textView_main_heading = (TextView) dialogview.findViewById(R.id.textView_main_heading);
        final TextView msg = (TextView) dialogview.findViewById(R.id.msg);
        final RadioGroup rdb_group_selection = (RadioGroup) dialogview.findViewById(R.id.rdb_group_selection);
        rdb_group_selection.setVisibility(View.GONE);
        msg.setVisibility(View.VISIBLE);
        textView_main_heading.setText("Sorry!");
        msg.setText("Service is not available on this area");
        okbtn.setText("Ok");
        cancelbtn.setVisibility(View.GONE);
        builder.setView(dialogview);
        final Dialog dlg= builder.create();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();



            }
        });
        dlg.show();

    }

    private String getTxnId() {
        return ("0nf7" + System.currentTimeMillis());
    }

    public void makePayment() {




        sTransdId = getTxnId();
        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();

        builder
                .setAmount(Double.parseDouble(Total_price))
                //.setAmount(1)
                .setTnxId(sTransdId)
                .setPhone(sPhoneNumber)
                .setProductName("PharmaProducts")
                .setFirstName(sUsername)
                .setEmail(sEmailId)
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setIsDebug(false)
/*                .setKey("dRQuiA")
                .setMerchantId("4928174");
*/
                .setKey("DgWBWJ6B")
                .setMerchantId("5290474");

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();

            /*
             server side call required to calculate hash with the help of <salt>
             <salt> is already shared along with merchant <key>
             serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)
             (e.g.)
             sha512(FCstqb|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||MBgjYaFG)
             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc
            */


        // Recommended
        calculateServerSideHashAndInitiatePayment_me(paymentParam);

           /*
            testing purpose
            String serverCalculatedHash="9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc";
            paymentParam.setMerchantHash(serverCalculatedHash);
            PayUmoneySdkInitilizer.startPaymentActivityForResult(this, paymentParam);
            */
    }


    private void calculateServerSideHashAndInitiatePayment_me(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        //String url = "http://192.168.1.26:8095/api/HashKey";


        String url = AppConfig.POST_Url_HashKey;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("key", "DgWBWJ6B");
     //   params.put("merchantId", "5588905");
        params.put("txnid", sTransdId);
        params.put("amount",String.format("%.1f",Double.parseDouble(Total_price)));
      //  params.put("surl", "https://www.payumoney.com/mobileapp/payumoney/success.php");
     //   params.put("furl", "https://www.payumoney.com/mobileapp/payumoney/failure.php");
        params.put("productinfo", "PharmaProducts");
        params.put("email", sEmailId);
        params.put("firstname", sUsername);
        //params.put("phone", sPhoneNumber);
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");

       // Toast.makeText(this, "Please wait... Generating hash from server ... ", Toast.LENGTH_LONG).show();


        /*StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String hash = response;
                    paymentParam.setMerchantHash(hash);
                    PayUmoneySdkInitilizer.startPaymentActivityForResult(QuestionActivity.this, paymentParam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(QuestionActivity.this,QuestionActivity.this.getString(R.string.connect_to_internet),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuestionActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //return paymentParam.getParams();
                return params;

            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);*/


        //String url_json = "http://192.168.1.26:8095/api/HashKeyJSON";

        showPdialog("Loading. . .");

        JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //JSONObject jsonObject1 = new JSONObject(response);
                            hidePDialog();
                            String hash = response.getString("Key");
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);
                            PayUmoneySdkInitilizer.startPaymentActivityForResult(ShippingAddress.this, paymentParam);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(ShippingAddress.this, ShippingAddress.this.getString(R.string.connect_to_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ShippingAddress.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("charset", "utf-8");
                headers.put("User-agent", "Tragindia");
                return headers;
            }
        };

        jor_inhurry_post.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(jor_inhurry_post);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            /*if(data != null && data.hasExtra("result")){
              String responsePayUmoney = data.getStringExtra("result");
                if(SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }*/


            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                String Mode = data.getStringExtra(SdkConstants.MODE);

                showDialogMessage("Payment Success Id : " + paymentId);
              //  payu_save_checkout(paymentId);

            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "failure");
                showDialogMessage("cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        showDialogMessage("failure");
                    }
                }
                //Write your code if there's no result
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                showDialogMessage("User returned without login");
            }
        }else {
            String result = data.getStringExtra("result");
            String payment_response = data.getStringExtra("payment_response");
            if(result.contains(StaticDataModel.TXN_SUCCESS_CODE))
            {
               try{
                   JSONObject j_obj=new JSONObject(payment_response);

                   if(j_obj.getString("status").equals("success"))
                   {
                       eazbuzz_save_checkout(j_obj.getString("txnid"));
                   }else
                   {
                       Toast.makeText(ShippingAddress.this, "Payment failed", Toast.LENGTH_LONG).show();
                   }


               }catch (Exception e)
               {

               }
            }else if(result.contains(StaticDataModel.TXN_TIMEOUT_CODE))
            {
                Toast.makeText(ShippingAddress.this, "Transaction timeout please try again", Toast.LENGTH_LONG).show();
            }
        }


    }
    private void showDialogMessage(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }



    private  void eazbuzz_save_checkout(final String transactionid)
    {
        showPdialog("Loading...");



        final Map<String, String> params = new HashMap<String, String>();
        params.put("iMemberId", sMemberId);
        params.put("sRequestFrom", "A");
        params.put("sPaymentFrom", "PG");
        params.put("sMode", "DC");
        params.put("Amount", Total_price);
        params.put("sTxnNo", transactionid);
        params.put("sPaymentGateway", "EB");

        RequestQueue queue = Volley.newRequestQueue(ShippingAddress.this);

        JsonObjectRequest jor_post_trag_detail = new JsonObjectRequest(
                Request.Method.POST,
                //AppConfig.Url_TragingDetail,
                AppConfig.POST_Url_EAZBUZZ_SAVECHECKOUT,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidePDialog();
                        Log.d("dialogresponce", String.valueOf(response));
                        post_save_checkoutSuccess(response,transactionid);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        show_snack_bar("Please check your network connection");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("charset", "utf-8");
                headers.put("User-agent", "Buying");
                return headers;
            }
        };

        jor_post_trag_detail.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jor_post_trag_detail);


    }

    private void show_snack_bar(String mesaage)
    {
        Snackbar mySnackbar = Snackbar.make(getWindow().getDecorView(), mesaage, Snackbar.LENGTH_SHORT);
        mySnackbar.show();
    }

    void make_payment_ebizz()
    {
       // number = Float.parseFloat(Total_price);

        Log.d("comeTomethods11","YESyes");
        /*sTransdId = getTxnId();
        Intent intent=new Intent(this,PWECouponsActivity.class);
        intent.putExtra("trxn_id",sTransdId);
        intent.putExtra("trxn_amount",Float.parseFloat(Total_price));
        intent.putExtra("trxn_prod_info","PharmaProducts");
        intent.putExtra("trxn_firstname",sUsername);
        intent.putExtra("trxn_email_id",sEmailId);
        intent.putExtra("trxn_phone",sPhoneNumber);
        intent.putExtra("trxn_key","X0UL4VFTX9");
        intent.putExtra("trxn_udf1","");
        intent.putExtra("trxn_udf2","");
        intent.putExtra("trxn_udf3","");
        intent.putExtra("trxn_udf4","");
        intent.putExtra("trxn_udf5","");
        intent.putExtra("trxn_address1","");
        intent.putExtra("trxn_address2","");
        intent.putExtra("trxn_city","");
        intent.putExtra("trxn_state","");
        intent.putExtra("trxn_country","");
        intent.putExtra("trxn_zipcode","");
        intent.putExtra("trxn_is_coupon_enabled",1);
        intent.putExtra("trxn_salt","B3E9YBAQBV");
        intent.putExtra("merchant_id","");*/



    // changes Apply
        sTransdId = getTxnId();
       // String sTransdId = "1001";
        String Total_price = "1000";
        String sUsername = "Harish";
        String sEmailId = "badgujar.harish9@gmail.com";
        String sPhoneNumber = "9673356844";
        String transaction_key = "X0UL4VFTX9";
        Intent intent=new Intent(this,PWECouponsActivity.class);
        intent.putExtra("trxn_id",sTransdId);
        intent.putExtra("trxn_amount",Total_price);
        intent.putExtra("trxn_prod_info","PharmaProducts");
        intent.putExtra("trxn_firstname",sUsername);
        intent.putExtra("trxn_email_id",sEmailId);
        intent.putExtra("trxn_phone",sPhoneNumber);
        intent.putExtra("trxn_key",transaction_key);
        intent.putExtra("trxn_udf1","");
        intent.putExtra("trxn_udf2","");
        intent.putExtra("trxn_udf3","");
        intent.putExtra("trxn_udf4","");
        intent.putExtra("trxn_udf5","");
        intent.putExtra("trxn_address1","");
        intent.putExtra("trxn_address2","");
        intent.putExtra("trxn_city","");
        intent.putExtra("trxn_state","");
        intent.putExtra("trxn_country","");
        intent.putExtra("trxn_zipcode","");
        intent.putExtra("trxn_is_coupon_enabled",1);
        intent.putExtra("trxn_salt","B3E9YBAQBV");
        intent.putExtra("merchant_id","");







        Log.d("comeTomethods12",sTransdId);
        Log.d("comeTomethods13",Total_price);
        Log.d("comeTomethods14",sUsername);
        Log.d("comeTomethods15",sEmailId);
        Log.d("comeTomethods16",sPhoneNumber);


        startActivityForResult(intent,StaticDataModel.PWE_REQUEST_CODE);


    }


}


package viroopa.com.medikart.buying;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.TermsCondition;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.adaptercart;
import viroopa.com.medikart.buying.adapter.adapterprice;
import viroopa.com.medikart.buying.model.M_cart;
import viroopa.com.medikart.buying.model.M_pricing;

public class NewCartSummary extends AppCompatActivity {

    private String sMemberId,sCardId,scartjson,ssummaryjson,sPromoCode;

    List<M_pricing> pricelist = new ArrayList<M_pricing>();
    List<M_cart> cartlist = new ArrayList<M_cart>();

    private Dialog promo_dialog;
    private ProgressDialog pDialog;
    private ListView LV_Cart,LV_Price;
    private CheckBox chkEmergency;
    private  TextView txtemergenctDate;


    private  RelativeLayout bottom_relative;
    adaptercart adaptercart;
    adapterprice adapterprice;
    AppController globalVariable;

    private  Integer  hour ;
    private  Integer  minutes;
    private ImageView btnprocess,btnNext;
    private TextView promocode_open,txt_price;

    private Button btnPromcode,btnPromcodeCancel;
    private EditText input_name ;
    private  String gotoMain;
    String emergencydate="",emergencyTime="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cart_summary);
        globalVariable = (AppController) getApplicationContext();
        get_intent();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

       // input_name = (EditText)findViewById(R.id.input_name);
        btnPromcode = (Button)findViewById(R.id.btnPromcode);
        btnprocess = (ImageView)findViewById(R.id.btnprocess);
        chkEmergency=(CheckBox)findViewById(R.id.chkisemergency);
        txtemergenctDate=(TextView)findViewById(R.id.txtemergenctDate);
        promocode_open=(TextView)findViewById(R.id.promocode_open);
        btnNext=(ImageView)findViewById(R.id.btnNext);
        bottom_relative=(RelativeLayout)findViewById(R.id.bottom_relative);

        txt_price=(TextView)findViewById(R.id.txt_price);
        if (sPromoCode.equals(""))
        {
            check_promocode(false);
        }
        else
        {
            check_promocode(true);
        }

        chkEmergency.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                   // txtemergenctDate.setVisibility(View.VISIBLE);
                   // select_dateAndTimeEmergency();\
                    emergey_click("true");

                }
                else
                {
                    txtemergenctDate.setVisibility(View.GONE);
                    emergey_click("false");
                }


            }
        });

     /*   btnPromcode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if ((Integer) btnPromcode.getTag() == 0)
                {
                    if (!input_name.getText().toString().isEmpty()) {
                        get_promocode(input_name.getText().toString());
                    } else {
                        f_alert_ok("Information", "Enter the Promo Code");
                    }
                }
                else
                {
                    get_promocode_cancel();
                }
            }
        });*/


        promocode_open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if ((Integer) promocode_open.getTag() == 0)
                {
                    promo_code_dialog();
                }
                else
                {
                    get_promocode_cancel();
                }


            }
        });

        bottom_relative.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {



                Prescription_confirmation_dialog();


            }
        });

        txtemergenctDate.setOnClickListener(new View.OnClickListener() {
                                                public void onClick(View view) {
                                                    select_dateAndTimeEmergency();

                                                }
                                            });

        /*btnPromcodeCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                get_promocode_cancel();
            }
        });
*/
                  btnprocess.setOnClickListener(new View.OnClickListener() {
                      public void onClick(View view) {

                          get_state_city();
                      }
                  });

                  adaptercart = new adaptercart(this, cartlist, sMemberId, this, gotoMain);
                  LV_Cart = (ListView) findViewById(R.id.list_cart);
                  LV_Cart.setAdapter(adaptercart);

                  adapterprice = new adapterprice(this, pricelist, sMemberId);
                  LV_Price = (ListView) findViewById(R.id.list_pricelist);
                  LV_Price.setAdapter(adapterprice);

                  insertdatatoadapter();

                  ListUtils.setDynamicHeight(LV_Cart);
                  ListUtils.setDynamicHeight(LV_Price);
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
                  Intent Intenet_change = new Intent(NewCartSummary.this, Order_Transaction.class);
                  startActivity(Intenet_change);
              }

              public void Show_Return_cancel_policies() {
                  Intent Intenet_change = new Intent(NewCartSummary.this, TermsCondition.class);
                  startActivity(Intenet_change);
              }

              public void Show_termscondition() {Intent Intenet_change = new Intent(NewCartSummary.this, TermsCondition.class);

                  startActivity(Intenet_change);
              }

              public void get_intent() {
                  SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                  Intent buy_intent = getIntent();
                  sCardId = pref.getString("cartid", "");
                  scartjson = pref.getString("cart_productdetail", "");
                  ssummaryjson = pref.getString("cart_pricesummarylist", "");
                  sPromoCode = pref.getString("cart_promocode", "");

                  // scartjson=globalVariable.getScartjson();
                  //ssummaryjson=globalVariable.getSsummaryjson();
                  // sPromoCode=globalVariable.getsPromoCode();
                  sMemberId = pref.getString("memberid", "");
                  gotoMain = buy_intent.getStringExtra("GotoMain");

              }

              public void insertdatatoadapter() {
                  try {
                      cartlist.clear();
                      JSONArray response = new JSONArray(scartjson);
                      for (int i = 0; i < response.length(); i++) {

                          JSONObject cartjson = (JSONObject) response.get(i);

                          M_cart O_cart = new M_cart();
                          O_cart.setProductName(cartjson.getString("ProductName"));
                          O_cart.setPackSize(cartjson.getString("PackSize"));
                          O_cart.setPrice(cartjson.getString("Price"));
                          O_cart.setAmount(cartjson.getString("Amount"));
                          O_cart.setQTY(cartjson.getString("QTY"));
                          O_cart.setCheckOutId(cartjson.getString("CheckOutId"));
                         // O_cart.setImage(cartjson.getString("MobileProductImagePath"));
                          O_cart.setImage(cartjson.getString("ProductImagePath"));


                          cartlist.add(O_cart);
                      }
                      adaptercart.notifyDataSetChanged();
                      ListUtils.setDynamicHeight(LV_Cart);
                  } catch (JSONException e) {
                      e.printStackTrace();
                      //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                  }

                  try {

                      pricelist.clear();
                      JSONArray response = new JSONArray(ssummaryjson);
                      for (int i = 0; i < response.length(); i++) {

                          JSONObject cartjson = (JSONObject) response.get(i);

                          M_pricing O_pricing = new M_pricing();
                          O_pricing.setDiscription(cartjson.getString("Discription"));
                          O_pricing.setAmount(cartjson.getString("Amount"));

                          if (cartjson.getString("Discription").equals("Base Price")) {
                              globalVariable.setEmergencyBaseprice(cartjson.getString("Amount"));
                          }
                          if (cartjson.getString("Discription").equals("Promo Code Discount")) {
                              globalVariable.setEmergencyPromocodeAmount(cartjson.getString("Amount"));
                          }
                          pricelist.add(O_pricing);
                      }
                      adapterprice.notifyDataSetChanged();
                      ListUtils.setDynamicHeight(LV_Cart);

                  } catch (JSONException e) {
                      e.printStackTrace();
                     // Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

              private void get_promocode(final String user_promo_code) {

                  showPdialog("Loading. . .");


                  String url = String.format(AppConfig.URL_GET_APPLYPROMOCODE, user_promo_code, sCardId, sMemberId,"M");

                  RequestQueue queue = Volley.newRequestQueue(this);

                  JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                          url,  new Response.Listener<JSONObject>() {
                      @Override
                      public void onResponse(JSONObject response) {
                          hidePDialog();
                          Success_promocode(response,user_promo_code);
                      }
                  },
                          new Response.ErrorListener() {
                              @Override
                              public void onErrorResponse(VolleyError error) {
                                  hidePDialog();
                                  Error_promocode(error);
                              }
                          });

                  jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                          50000,
                          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                  //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

                  queue.add(jsonObjReq_single);
              }

              private void Success_promocode(JSONObject response,String user_promo_code) {
                  try {
                      if (response.getString("Flag").equals("true")) {
                          f_alert_ok("Success", response.getString("Msg"));
                          sPromoCode =user_promo_code;
                          check_promocode(true);

                          get_pricing_detais();
                          promo_dialog.dismiss();


                      } else {
                          f_alert_ok("Information", response.getString("Msg"));
                      }

                  } catch (Exception e) {
                      e.printStackTrace();
                      //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                  }
              }

              private void Error_promocode(VolleyError error) {
                  try {
                  } catch (Exception e) {

                      e.printStackTrace();
                      //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                  }
              }

              private void get_promocode_cancel() {

                  showPdialog("Loading. . .");

                  String url = String.format(AppConfig.URL_GET_CANCELPROMOCODE,sPromoCode, sCardId, sMemberId,"M");

                  RequestQueue queue = Volley.newRequestQueue(this);

                  StringRequest jsonObjReq_single = new StringRequest(Request.Method.GET,
                          url,  new Response.Listener<String>() {
                      @Override
                      public void onResponse(String response) {
                          hidePDialog();
                          Success_promocode_cancel(response);
                      }
                  },
                          new Response.ErrorListener() {
                              @Override
                              public void onErrorResponse(VolleyError error) {
                                  hidePDialog();
                                  Error_promocode_cancel(error);
                              }
                          });

                  jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                          50000,
                          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                  //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

                  queue.add(jsonObjReq_single);
              }

              private void Success_promocode_cancel(String response) {
                  try {

                          f_alert_ok("Success", response);

                          check_promocode(false);

                         get_pricing_detais();

                  } catch (Exception e) {
                      e.printStackTrace();
                      //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                  }
              }

              private void Error_promocode_cancel(VolleyError error) {
                  try {
                  } catch (Exception e) {

                      e.printStackTrace();
                      //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

              public static class ListUtils {
                  public static void setDynamicHeight(ListView mListView) {
                      ListAdapter mListAdapter = mListView.getAdapter();
                      if (mListAdapter == null) {
                          // when adapter is null
                          return;
                      }
                      int height = 0;
                      int desiredWidth = MeasureSpec.makeMeasureSpec(mListView.getWidth(), MeasureSpec.UNSPECIFIED);
                      for (int i = 0; i < mListAdapter.getCount(); i++) {
                          View listItem = mListAdapter.getView(i, null, mListView);
                          listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                          height += listItem.getMeasuredHeight();
                      }

                      ViewGroup.LayoutParams params = mListView.getLayoutParams();
                      params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
                      mListView.setLayoutParams(params);
                      mListView.requestLayout();
                  }
              }

              private void get_state_city() {
                  try {
                      SharedPreferences pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
                      sMemberId = pref.getString("memberid", "");

                      if (pref.getString("statelist", "").equals("")) {
                          get_statelist();
                      } else if (pref.getString("citylist", "").equals("")) {
                          get_citylist();
                      } else {
                          Show_ShippingAddress();
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }

              private void get_statelist() {

                  showPdialog("loading...");

                  String url = AppConfig.URL_GET_STATE;



                  RequestQueue queue = Volley.newRequestQueue(this);

                  JsonObjectRequest jsonObjReq_state = new JsonObjectRequest(Request.Method.GET,
                          url, new Response.Listener<JSONObject>() {
                      @Override
                      public void onResponse(JSONObject response) {
                          //hidePDialog();
                          try {
                              JSONArray sStatelist = new JSONArray();
                              sStatelist = response.getJSONArray("StateList");
                              SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
                              SharedPreferences.Editor prefsEditor = pref.edit();
                              prefsEditor.putString("statelist", sStatelist.toString());
                              prefsEditor.commit();

                              if (pref.getString("citylist", "").equals("")) {
                                  get_citylist();
                              } else {
                                  Show_ShippingAddress();
                              }

                          } catch (Exception e) {
                              hidePDialog();
                              e.printStackTrace();
                          }
                      }
                  },
                          new Response.ErrorListener() {
                              @Override
                              public void onErrorResponse(VolleyError error) {
                                  hidePDialog();
                              }
                          });

                  jsonObjReq_state.setRetryPolicy(new DefaultRetryPolicy(
                          50000,
                          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                  //AppController.getInstance().addToRequestQueue(jsonObjReq_state);

                  queue.add(jsonObjReq_state);
              }

              private void get_citylist() {

                  showPdialog("loading...");

                  String url = String.format(AppConfig.URL_GET_CITY, "", "0");
                  new getCityAsyncHttpTask().execute(url);

                  RequestQueue queue = Volley.newRequestQueue(this);

                  JsonObjectRequest cityrequest = new JsonObjectRequest(Request.Method.GET, url,
                          new Response.Listener<JSONObject>() {
                              @Override
                              public void onResponse(JSONObject response) {
                                  hidePDialog();
                                  try {
                                      String sCitylist = response.getJSONArray("cityModel").toString();
                                      SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
                                      SharedPreferences.Editor prefsEditor = pref.edit();
                                      prefsEditor.putString("citylist", sCitylist);
                                      prefsEditor.commit();

                                      Show_ShippingAddress();
                                  } catch (Exception e) {
                                      hidePDialog();
                                      e.printStackTrace();
                                  }
                              }
                          },
                          new Response.ErrorListener() {
                              @Override
                              public void onErrorResponse(VolleyError error) {
                                  hidePDialog();
                              }
                          });

                  cityrequest.setRetryPolicy(new DefaultRetryPolicy(
                          500000,
                          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                  // AppController.getInstance().addToRequestQueue(cityrequest, "req_city");

                  queue.add(cityrequest);
              }

              private void Show_ShippingAddress() {
                  Intent i = new Intent(this, ShippingAddress.class);
                  startActivity(i);
              }

              private void check_promocode(Boolean bFlag) {
                  if (bFlag == true) {
                      promocode_open.setTag(1);
                      promocode_open.setText("Remove Code");
                     // input_name.setText(sPromoCode);
                      //input_name.setEnabled(false);
                      //input_name.setFocusableInTouchMode(false);
                  } else {
                      promocode_open.setTag(0);
                      promocode_open.setText("Have a Promo Code?");
                      //input_name.setText("");
                      //input_name.setEnabled(true);
                     // input_name.setFocusableInTouchMode(true);
                  }
              }

              public Intent getSupportParentActivityIntent() {
                  SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                  // String className = pref.getString("cart_checkout_ParentClass", "");
                  String className = globalVariable.getParentClass();
                  String package_name = getResources().getString(R.string.package_name);

                  Intent newIntent = null;
                  try {
                      //you need to define the class with package name
                      newIntent = new Intent(NewCartSummary.this, Class.forName(package_name +"buying."+ className));
                      startActivity(newIntent);
                      finish();
                  } catch (ClassNotFoundException e) {
                      e.printStackTrace();
                      f_alert_ok("Error ", e.getMessage());
                  }
                  return newIntent;
              }

              @Override
              public void onBackPressed() {
                  SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                  // String className = pref.getString("cart_checkout_ParentClass", "");
                  String className = globalVariable.getParentClass();
                  String package_name = getResources().getString(R.string.package_name);

                  Intent newIntent = null;
                  try {
                      //you need to define the class with package name
                      newIntent = new Intent(NewCartSummary.this, Class.forName(package_name +"buying."+ className));
                      startActivity(newIntent);
                      finish();
                  } catch (ClassNotFoundException e) {
                      e.printStackTrace();
                      f_alert_ok("Error ", e.getMessage());
                  }
                  // super.onBackPressed();
                  // finish();
              }

              private void select_dateAndTimeEmergency() {
                  LayoutInflater inflater = LayoutInflater.from(this);
                  AlertDialog.Builder builder = new AlertDialog.Builder(this);
                  final View dialogview = inflater.inflate(R.layout.date_time_emergency_delivery, null);
                  final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
                  final Button cancelbtn = (Button) dialogview.findViewById(R.id.btnclear);
                  final DatePicker dtpckr = (DatePicker) dialogview.findViewById(R.id.datedmPicker);
                  final TimePicker timePckr = (TimePicker) dialogview.findViewById(R.id.dmtimePicker);
                  builder.setView(dialogview);
                  final Dialog dlg = builder.create();


                  okbtn.setOnClickListener(new View.OnClickListener() {
                      public void onClick(View view) {
                          Calendar cal = Calendar.getInstance();
//                Integer Hour =cal.HOUR;
//                Integer minutes =cal.MINUTE;


                          Integer Hour = cal.get(Calendar.HOUR);
                          Integer minutes = cal.get(Calendar.MINUTE);

                          String sHour = "";
                          String sminutes = "";
                          if (Hour < 10) {
                              sHour = "0" + String.valueOf(Hour);

                          } else {
                              sHour = String.valueOf(Hour);
                          }


                          if (minutes < 10) {
                              sminutes = "0" + String.valueOf(minutes);

                          } else {
                              sminutes = String.valueOf(minutes);
                          }
                          //hour = hourOfDay;
                          //minutes = minute;
                          //time_pckr = sHour + ":" + sminutes;

                         /* if (emergencyTime == null) {
                              emergencyTime = String.valueOf(sHour) + ":" + String.valueOf(sminutes);
                          }*/

                          String year = "";
                          String dayOfMonth = "";
                          String monthOfYear = "";

                          year = String.valueOf(dtpckr.getYear());
                          if (dtpckr.getDayOfMonth() < 10) {
                              dayOfMonth = "0" + dtpckr.getDayOfMonth();

                          } else {
                              dayOfMonth = String.valueOf(dtpckr.getDayOfMonth());
                          }
                          if (dtpckr.getMonth() + 1 < 10) {
                              monthOfYear = "0" + String.valueOf(dtpckr.getMonth() + 1);
                          } else {
                              monthOfYear = String.valueOf(dtpckr.getMonth() + 1);
                          }
                            if(emergencyTime==null)
                            {
                                emergencyTime = String.valueOf(sHour) + ":" + String.valueOf(sminutes);
                            }

                          emergencydate = year + "-" + (monthOfYear) + "-" + dayOfMonth;
                          if (emergencyTime != null) {
                              txtemergenctDate.setText("Emergency Delivery Date : " + emergencydate + "  and Time : " + emergencyTime);
                              emergey_click("true");
                              dlg.cancel();
                          }
                          //Toast.makeText(getApplicationContext(), "Please select time", Toast.LENGTH_LONG).show();
                          //datedm= year + "-" + (monthOfYear) + "-" + dayOfMonth;
                          //txt_dmDate.setText(emergencydate + " " + time_pckr);

                      }
                  });
                  cancelbtn.setOnClickListener(new View.OnClickListener() {
                      public void onClick(View view) {
                          dlg.dismiss();
                      }
                  });

                  timePckr.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

                      public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {


                          String sHour = "";
                          String sminutes = "";
                          if (hourOfDay < 10) {
                              sHour = "0" + String.valueOf(hour);

                          } else {
                              sHour = String.valueOf(hour);
                          }


                          if (minute < 10) {
                              sminutes = "0" + String.valueOf(minute);

                          } else {
                              sminutes = String.valueOf(minute);
                          }
                          hour = hourOfDay;
                          minutes = minute;
                          emergencyTime = sHour + ":" + sminutes;
                          emergencyTime = String.valueOf(hour) + ":" + String.valueOf(minutes);


                      }
                  });
                  dlg.show();dlg.setCancelable(false);
              }

              private void emergey_click(final String IsEmergency) {
                  showPdialog("loading.........");
                  String url = String.format(AppConfig.URL_GET_ISEMERGENCY,sMemberId,sCardId,IsEmergency);

                  RequestQueue queue = Volley.newRequestQueue(this);

                  StringRequest stringRequest = new StringRequest( url,
                          new Response.Listener<String>() {
                              @Override
                              public void onResponse(String response) {
                                  // Display the first 500 characters of the response string.
                                  isEmergencySuccess(response ,IsEmergency);
                                  hidePDialog();
                              }
                          }, new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {
                         isEmergencyError(error);
                          hidePDialog();
                      }
                  });



                  queue.add(stringRequest);
              }

              private void isEmergencySuccess(String response,String emergency) {
                  response.toString();
                  hidePDialog();

                  if(response.equals("true"))
                  {
                      get_pricing_detais();
                  }



              }

              private void isEmergencyError(VolleyError error) {
                  error.toString();
                  hidePDialog();
              }
    public class getStaeAsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

        }



        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();



                /* 200 represents HTTP OK */
                if (statusCode == 200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject x = new JSONObject(response.toString());


                    try {
                        JSONArray sStatelist = new JSONArray();
                        sStatelist = x.getJSONArray("StateList");
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = pref.edit();
                        prefsEditor.putString("statelist", sStatelist.toString());
                        prefsEditor.commit();

                       

                    } catch (Exception e) {
                        hidePDialog();
                        e.printStackTrace();
                    }

                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {

            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {


            hidePDialog();

            if (result == 1) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
                if (pref.getString("citylist", "").equals("")) {
                    get_citylist();
                } else {
                    Show_ShippingAddress();
                }

            } else {

              
            }
        }
    }
    public class getCityAsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

        }



        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                String Myresponse = urlConnection.getResponseMessage();

                /* 200 represents HTTP OK */
                if (statusCode == 200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject x = new JSONObject(response.toString());


                    try {
                        String sCitylist = x.getJSONArray("cityModel").toString();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = pref.edit();
                        prefsEditor.putString("citylist", sCitylist);
                        prefsEditor.commit();


                    } catch (Exception e) {
                        hidePDialog();
                        e.printStackTrace();
                    }

                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {

            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {


            hidePDialog();

            if (result == 1) {
                Show_ShippingAddress();

            } else {


            }
        }
    }

    private void promo_code_dialog()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.promo_code_dialog, null);

        final Button btnApplyPromcode = (Button) dialogview.findViewById(R.id.btnApplyPromcode);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);
          final EditText promo_input_name = (EditText) dialogview.findViewById(R.id.promo_input_name);
        builder.setView(dialogview);
        final Dialog dlg= builder.create();


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        btnApplyPromcode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                    if (!promo_input_name.getText().toString().isEmpty()) {
                        get_promocode(promo_input_name.getText().toString());
                    } else {
                        f_alert_ok("Information", "Enter the Promo Code");
                    }

            }
        });
        promo_dialog=dlg;
        dlg.show();

    }
    public void change_total_price(String price)
    {
        txt_price.setText(price);
    }

    private  void Prescription_confirmation_dialog()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.upload_prescription_confirmation_dialog, null);

        final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);
        final Button btndismiss = (Button) dialogview.findViewById(R.id.btndismiss);

        builder.setView(dialogview);
        final Dialog dlg= builder.create();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent Intenet_upload = new Intent(NewCartSummary.this, uploadprescriptionActivity.class);
               Intenet_upload.putExtra("Cart_id",sCardId);
                boolean flag=chkEmergency.isChecked();
                Intenet_upload.putExtra("Is_Emergency",chkEmergency.isChecked());
                startActivity(Intenet_upload);
                dlg.dismiss();
            }
        });
        btndismiss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

               // Toast.makeText(getApplicationContext(), "Kindly keep your prescription ready at the time of your medicine delivery as it is mandatory as per goverment regulation.", Toast.LENGTH_LONG).show();


               message_for_prescription();
                dlg.dismiss();
            }
        });
        dlg.show();
    }

    private void message_for_prescription()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.upload_prescription_message_dialog, null);

        final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);

        builder.setView(dialogview);
        final Dialog dlg= builder.create();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent Intenet_change = new Intent(NewCartSummary.this, ShippingAddress.class);
                startActivity(Intenet_change);
                dlg.dismiss();
            }
        });
        dlg.show();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void get_pricing_detais()
    {
        showPdialog("getting prcing details. . .");

        String url = String.format(AppConfig.URL_GET_PRICING_DETAIL, sCardId);

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
        SharedPreferences pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("cart_pricesummarylist", response.toString());
        emergencyTime=null;
        editor.commit();
        get_intent();
        insertdatatoadapter();
    }
          }


package viroopa.com.medikart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.adapter.MemberRecyclerAdapter;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.model.M_memberlist;

public class EditAndAddMember extends AppCompatActivity {
  // private ListView mainListview;
    private  Button AddMember;
    private ProgressDialog pDialog;
    private Integer memberCount=0;
    private String sMemberId;
    JSONArray jsonarray;
    private RelativeLayout headinglayout;
    ArrayList<String> MemberName = new ArrayList<String>();
    ArrayList<String> Relation = new ArrayList<String>();
    ArrayList<String> MemberID = new ArrayList<String>();
    ArrayList<String> imgUrl = new ArrayList<String>();
    private RecyclerView mRecyclerView;
    MemberRecyclerAdapter recyclerAdapter;
    private LinearLayout no_data_found_layout;
    List<M_memberlist> MemberData = new ArrayList<M_memberlist>();

    private String sInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_and_add_member);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
      //  mainListview=(ListView)findViewById(R.id.list_info);
        AddMember=(Button)findViewById(R.id.btnadd);
        headinglayout=(RelativeLayout) findViewById(R.id.relativeLayout6);
        no_data_found_layout=(LinearLayout)findViewById(R.id.no_data_found_layout);

        getIntenet();


        check_local_Storage();


        AddMember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (memberCount < 6) {
                    Intent Intenet_AddMEmber = new Intent(EditAndAddMember.this, AddMember.class);
                    Intenet_AddMEmber.putExtra("Mode", "A");
                    startActivity(Intenet_AddMEmber);
                } else {
                    Toast.makeText(getApplicationContext(), "you have added the maximum number of members", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

      /*  mainListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub


            }
        });
*/
    }
    @Override
    public void onResume() {
        super.onResume();
        getFamilyMemberlist();
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
       // Intent Intenet_change = new Intent(EditAndAddMember.this, Order_Transaction.class);
      //  startActivity(Intenet_change);
    }
    public void Show_Return_cancel_policies() {
      //  Intent Intenet_change = new Intent(EditAndAddMember.this, TermsCondition.class);
      //  startActivity(Intenet_change);
    }
    public void Show_termscondition() {
      //  Intent Intenet_change = new Intent(EditAndAddMember.this, TermsCondition.class);
       // startActivity(Intenet_change);
    }




    private void getFamilyMemberlist() {

        showPdialog("loading...");

        String tag_string_req = "req_FamilyMember";



        //String url = String.format(AppConfig.URL_GET_MEMBERLIST, sMemberId);
        String url = String.format(AppConfig.URL_GET_MEMBERFamilyLIST, sMemberId);

      //  new AsyncHttpTask().execute(url);
        String vmembername = "";
        JsonObjectRequest FamilyMemberrequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Success_FamilyMember(response);
                       hidePDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d(TAG, "Register Response: " + error.getMessage());
                        hidePDialog();
                        Error_FamilyMember(error);
                    }
                });

        FamilyMemberrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(FamilyMemberrequest, tag_string_req);
    }


    private void Success_FamilyMember(JSONObject response) {
        try {
           // memberList.clear();
            MemberName.clear();
            Relation.clear();
            MemberID.clear();
            imgUrl.clear();

            MemberData.clear();
            jsonarray = response.getJSONArray("PatientList");
            memberCount=0;
           // ObjectItemData = new ObjectItem[jsonarray.length()];
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                M_memberlist memberdetails = new M_memberlist();

                memberdetails.setMemberId(jsonobject.optInt("MemberId"));
                memberdetails.setMemberName(jsonobject.optString("MemberName"));
                memberdetails.setMemberGender(jsonobject.optString("MemberGender"));
                memberdetails.setRelationshipId(jsonobject.optInt("RelationshipId"));
                memberdetails.setMemberDOB(jsonobject.optString("MemberDOB"));
                memberdetails.setImageurl(jsonobject.getString("ImageUrl"));
                memberdetails.setPatientId(jsonobject.getString("PatientId"));

                memberdetails.setRelation_name(jsonobject.getString("Relationship_name"));
                MemberData.add(memberdetails);
                //show_image();

                String sMemberList = response.getJSONArray("PatientList").toString();
                MemberName.add(jsonobject.getString("MemberName"));
                Relation.add(jsonobject.getString("RelationshipId"));
                MemberID.add(jsonobject.getString("PatientId"));
                imgUrl.add(jsonobject.getString("ImageUrl"));

                SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = pref.edit();
                prefsEditor.putString("memberList", sMemberList);
                prefsEditor.commit();
                memberCount++;

            }

            if(MemberData.size()<1)
            {
                headinglayout.setVisibility(View.GONE);
                no_data_found_layout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }else {
                recyclerAdapter = new MemberRecyclerAdapter(EditAndAddMember.this, MemberData, sMemberId);
                mRecyclerView.setAdapter(recyclerAdapter);
            }






        } catch (Exception e) {
            hidePDialog();
            Log.e("Error", e.getMessage());
            //mb.showAlertDialog(fra, "Error", e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void Error_FamilyMember(VolleyError error) {

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(this, "TimeoutError", Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            //TODO
            Toast.makeText(this,"AuthFailureError", Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            //TODO
            Toast.makeText(this,"ServerError", Toast.LENGTH_LONG).show();
        } else if (error instanceof NetworkError) {
            //TODO
            Toast.makeText(this,"NetworkError", Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            //TODO
            Toast.makeText(this,"ParseError", Toast.LENGTH_LONG).show();
        }

        hidePDialog();


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

    private void getIntenet()
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
    }

    private  void check_local_Storage()
    {
        if(checkMemberListExist() <= 0)
        {

        }

    }
    private int checkMemberListExist()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        String sMemberList = (pref.getString("memberList", ""));
        sInfo=sMemberList;
       // return sMemberList.length();
        return 0;
    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
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

                    //parseResult(response.toString());

                    //parseResult(response.toString());
                    //load_product_list
                    Success_FamilyMember(x);


                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
               // Log.d(TAG, e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

            setProgressBarIndeterminateVisibility(false);
            hidePDialog();
            /* Download complete. Lets update UI */
            if (result == 1) {

                recyclerAdapter = new MemberRecyclerAdapter(EditAndAddMember.this, MemberData,sMemberId);
                mRecyclerView.setAdapter(recyclerAdapter);
            } else {
               // Log.e(TAG, "Failed to fetch data!");
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

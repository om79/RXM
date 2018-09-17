package viroopa.com.medikart.MedicineReminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteMRHandler;

public class MRA_Add_pilly_Budy_On_Code extends AppCompatActivity {

    EditText edt_pill_buddy_code;
    Button btn_add_friend;
    private SQLiteHandler db;
    private String sMemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mra__add_pilly__budy__on__code);
        getIntenet();
        edt_pill_buddy_code=(EditText)findViewById(R.id.edt_pill_buddy_code);
        btn_add_friend=(Button)findViewById(R.id.btn_add_friend);

        btn_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_pill_buddy_code.getText().toString().isEmpty())
                {
                    ACCEPT_MEDFRIED_ON_CODE(edt_pill_buddy_code.getText().toString());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        edt_pill_buddy_code.clearAnimation();
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.milkshake);
        edt_pill_buddy_code.startAnimation(animRotate);
    }

    public  void ACCEPT_MEDFRIED_ON_CODE(String invite_code)
    {
        db = new SQLiteHandler(this);

        ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
        test.add(db.getUserDetails());



        HashMap<String, String> m = test.get(0);

        String  url = String.format(AppConfig.URL_GET_ACCEPT_PIIBUDDY_ON_CODE,sMemberId,invite_code,m.get("phoneno"),m.get("email"));


        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest staterequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                                ConstData.get_medfriend_data(getApplicationContext(), sMemberId);
                                Toast.makeText(MRA_Add_pilly_Budy_On_Code.this, response.getString("sReturnMsg"), Toast.LENGTH_LONG).show();
                                finish();

                        }catch (Exception e)
                        {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //hidePDialog();
                        Toast.makeText(MRA_Add_pilly_Budy_On_Code.this, "Network error, please try after sometime.", Toast.LENGTH_LONG).show();
                    }
                });


        queue.add(staterequest);
    }



    private void getIntenet() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
    }

    }

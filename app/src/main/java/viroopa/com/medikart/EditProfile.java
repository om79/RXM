package viroopa.com.medikart;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.Order_Transaction;
import viroopa.com.medikart.helper.SQLiteHandler;

public class EditProfile extends AppCompatActivity {

    private String sMemberId, gender;

    private EditText input_name, input_mobile, input_email, input_pincode, input_lastname,input_address;
    Integer x = 1;
    private RadioGroup rdbgender;
    private RadioButton rdbMale;
    private RadioButton rdbFemale;
    private RadioButton rdbother;
    private LinearLayout ll_image;
    Integer count = 0;
    private String AvatarImage;
    private int flag = 0;
    private Button btnupdate;
    private ImageView profile_pic;
    private static final String TAG = MainActivity.class.getSimpleName();
    private SQLiteHandler db;
    private String picname1 = "";
    private ProgressDialog pDialog;
    private  LinearLayout mai_layout;
    private String ProfilePicName="";
    private String imageNamefromserver,ImageStream;
    private Integer ProfilePicFlag = 0;
    private String ImageType = "";
    private String selectDate = "";
    private String picname = "";
    private  String imageName;
    private  String getimageName;
    private  Boolean ImageClick=false;
    private  String getimageFolder;
    private ImageLoader imageLoader;
    AppController globalVariable;
    DisplayImageOptions options;

    static final Integer objAvatar = 0 ;
    static final Integer objCamera = 1 ;
    static final Integer objBrowse = 2 ;
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        mai_layout=(LinearLayout)findViewById(R.id.mai_layout);
        setupUI(mai_layout);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        globalVariable = (AppController) getApplicationContext();
        pDialog = new ProgressDialog(this);


        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();


        input_name = (EditText) findViewById(R.id.input_name);
        input_mobile = (EditText) findViewById(R.id.mobile);
        input_email = (EditText) findViewById(R.id.email);
        input_pincode = (EditText) findViewById(R.id.pincode);
        input_address = (EditText) findViewById(R.id.input_address);

        input_lastname = (EditText) findViewById(R.id.input_laname);

        profile_pic = (ImageView) findViewById(R.id.profile_pic);

        rdbgender = (RadioGroup) findViewById(R.id.sexgroup);
        rdbMale = (RadioButton) findViewById(R.id.btnmale);
        rdbFemale = (RadioButton) findViewById(R.id.btnfemale);
        rdbother = (RadioButton) findViewById(R.id.btnother);
        Calendar c = Calendar.getInstance();

        final Integer curYear = c.get(Calendar.YEAR);
        final Integer curMonth = c.get(Calendar.MONTH) + 1;
        final Integer curDay = c.get(Calendar.DAY_OF_MONTH);
        final Integer curHH = c.get(Calendar.HOUR);
        final Integer curMM = c.get(Calendar.MINUTE);
        final Integer curSS = c.get(Calendar.SECOND);
        selectDate = curYear + "-" + curMonth + "-" + curDay + "-" + curHH + "-" + curMM + "-" + curSS;

        input_email.setFocusable(false);
        input_email.setClickable(true);
        input_mobile.setFocusable(false);
        input_mobile.setClickable(true);

        btnupdate = (Button) findViewById(R.id.btnupdate);

        initImageLoader();
        getIntenet();

        db = new SQLiteHandler(getApplicationContext());

        ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
        test.add(db.getUserDetails());



        HashMap<String, String> m = test.get(0);

        input_name.setText(m.get("name"));
        input_mobile.setText(m.get("phoneno"));
        input_email.setText(m.get("email"));
        input_pincode.setText(m.get("pincode"));
        if(m.get("MemberAddress")!=null && !m.get("MemberAddress").equals("null")) {
            input_address.setText(m.get("MemberAddress"));
        }
        gender = m.get("gender").toString();
        // profile_pic.setImageResource(R.drawable.avtar1);
        if (gender.equals("M")) {
            rdbMale.setChecked(true);
        } else if (gender.equals("F")) {
            rdbFemale.setChecked(true);
        } else if (gender.equals("O")) {
            rdbother.setChecked(true);
        }
        ImageType=m.get("MemberImageType");


        btnupdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                if (! input_name.getText().toString().isEmpty()
                        && !input_mobile.getText().toString().isEmpty()
                        // && !input_email.getText().toString().isEmpty()
                        && !gender.isEmpty()
                        && !input_pincode.getText().toString().isEmpty()
                        ) {
                    if(input_mobile.getText().toString().length()==10)
                    {

                        if(input_pincode.getText().toString().length()==6) {



                            if (!input_email.getText().toString().isEmpty()) {
                                if (CheckEmail_pattern(input_email.getText().toString())) {
                                    CheckEditProfileMobileNo();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please enter a valid email id!", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }else{
                                CheckEditProfileMobileNo();
                            }


                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please enter valid Pincode number", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please enter valid phone number", Toast.LENGTH_LONG)
                                .show();
                    }
                }

                else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }

            }

        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                set_setttings_ProfilePicFromFlagString("ProfilePicFromFlag", "EditProfile");
                Show_Change_Profile_Picture();
            }
        });


        rdbgender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    if (rdbMale.isChecked()) {
                        gender = "M";
                    } else if (rdbFemale.isChecked()) {
                        gender = "F";
                    } else if (rdbother.isChecked()) {
                        gender = "O";
                    }
                }
            }
        });
    }

    private void Show_Change_Profile_Picture() {
        Intent Intenet_add_profile_pic = new Intent(EditProfile.this, AddProfilePic.class);
        ImageClick=true;
        startActivity(Intenet_add_profile_pic);

    }

    private void set_setttings_ProfilePicString(String s_name, String sttng) {
        SharedPreferences pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //editor.putString("memberid", response.optString("UserID"));
        String shrd_name;

        shrd_name = s_name;

        editor.putString(shrd_name, sttng);
        // editor.putString("Bp_Default_Position", def_pos);
        editor.commit();
    }

    private void update_profile() {


        JSONArray imgarr = new JSONArray();
        JSONObject imgobj = new JSONObject();
        try {
            if (ProfilePicName.toString().equals("")) {
            } else {
                imgobj = new JSONObject();
                imgobj.put("ImageName", ProfilePicName);
                imgarr.put(imgobj);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        set_setttings_ProfilePicString("ProfilePicName", AvatarImage);

        Map<String, String> params = new HashMap<String, String>();
        params.put("MemberId", sMemberId);
        params.put("MemberFName", input_name.getText().toString());
        params.put("MemberLName", input_lastname.getText().toString());
        params.put("MemberMobileNo", input_mobile.getText().toString());
        params.put("MemberEmailID", input_email.getText().toString());
        params.put("MemberGender", gender);
        if (ImageType.equals("I")) {
            params.put("ImageStream",ImageStream);
            if(picname==null || picname.equals("")||picname.equals("null"))
            {
                params.put("ProfileImage", ProfilePicName);
            }
            else {
                params.put("ProfileImage", picname);
            }
        } else {
            params.put("ImageStream",null);
            if(AvatarImage==null || AvatarImage.equals("")||AvatarImage.equals("null"))
            {
                params.put("ProfileImage", ProfilePicName);
            }
            else {
                params.put("ProfileImage", AvatarImage);
            }
        }

        params.put("Pincode", input_pincode.getText().toString());
        params.put("RAddress", input_address.getText().toString());
        params.put("Address", "");
        params.put("RLandmark", "");
        params.put("RPincode", "0");
        params.put("RDistrictId", "0");
        params.put("SourcePath", "");
        params.put("DestPath", "");
        params.put("ImageType", ImageType);
        String Post_registration = "upload_profile";
        JSONObject jparams = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(EditProfile.this);

        JsonObjectRequest jor_edit_profile = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_SaveEditProfile,
                jparams,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        sucess_profile(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error_profile(error);
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

        queue.add(jor_edit_profile);
    }

    private void sucess_profile(JSONObject response) {
        try {
            hidePDialog();
            // refresh_profile_data(response);

            if (response.getString("bReturnFlag").equals("true")) {



                SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("memberid", response.optString("MemberID"));
                editor.putString("ImageName", response.optString("ImageName"));
                editor.putString("FolderPath", response.optString("FolderPath"));
                editor.putString("imagepath", response.optString("ImageUrl"));
                editor.putString("imagename", response.optString("ProfileImage"));
                editor.putString("imageType", response.optString("ImageType"));
                editor.commit();
                imageNamefromserver=response.optString("ProfileImage");

                Map<String, String> params = new HashMap<String, String>();
                params.put("MemberId", sMemberId);
                params.put("MemberFName", input_name.getText().toString());
                params.put("MemberLName", input_lastname.getText().toString());
                params.put("MemberMobileNo", input_mobile.getText().toString());
                params.put("MemberEmailID", input_email.getText().toString());
                params.put("MemberGender", gender);
                String MemberImageName = "";
                if (ImageType.equals("I")) {
                    params.put("ProfileImage", picname);
                    MemberImageName = picname;
                } else {
                    params.put("ProfileImage", AvatarImage);
                    MemberImageName = AvatarImage;
                }
                // params.put("ProfileImage", ProfilePicName);
                params.put("Pincode", input_pincode.getText().toString());
                params.put("ImageType", ImageType);
                db.update_profile(sMemberId, input_name.getText().toString(),
                        input_mobile.getText().toString(), gender, input_pincode.getText().toString(), ImageType, MemberImageName, input_address.getText().toString());
                //f_alert_ok("Success", response.getString("sReturnMsg"));
                hidePDialog();
                final Toast toast = Toast.makeText(getApplicationContext(), " Profile updated successfully ", Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                        finish();
                    }
                }, 3000);


                //f_alert_ok("Success", " profile updated successfully");

            } else {
                f_alert_ok("Information", response.getString("sReturnMsg"));
                hidePDialog();
            }
        } catch (JSONException e) {
            hidePDialog();
            Log.e("Error", e.getMessage());
            f_alert_ok("Error ", e.getMessage());
        }
    }

    private void error_profile(VolleyError error) {
        String json = null;


        hidePDialog();


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
        Intent Intenet_change = new Intent(EditProfile.this, Order_Transaction.class);
        startActivity(Intenet_change);
    }
    public void Show_Return_cancel_policies() {
        Intent Intenet_change = new Intent(EditProfile.this, TermsCondition.class);
        startActivity(Intenet_change);
    }
    public void Show_termscondition() {
        Intent Intenet_change = new Intent(EditProfile.this, TermsCondition.class);
        startActivity(Intenet_change);
    }

    private void getIntenet() {
        Intent intent_buymainactivity = getIntent();
        // sMemberId = intent_buymainactivity.getStringExtra("memberid");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");


        getimageName= pref.getString("ImageName", "");
        getimageFolder= pref.getString("FolderPath", "");
        // imgPathfromServer= pref.getString("imagepath", "");
        //imageNamefromserver= pref.getString("imagename", "");

        //Intent int_pic= getIntent();


        ProfilePicName= pref.getString("imagename", "");

        if(!ProfilePicName.equals("")&& !ProfilePicName.equals("null"))
        {
            if(ProfilePicName.startsWith("avtar"))
            {
                Resources res = getResources();

                int resourceId = res.getIdentifier(ProfilePicName, "drawable", getPackageName());
                Drawable drawable = res.getDrawable(resourceId);
                profile_pic.setImageDrawable(drawable);
            }else{

                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),AppConfig.IMAGE_DIRECTORY_NAME);
                // mybitmap = BitmapFactory.decodeFile(mediaStorageDir.getPath()+"/"+ProfilePicName);
                imageLoader.displayImage("file://" + mediaStorageDir.getPath()+"/"+ProfilePicName, profile_pic,options);

                // profile_pic.setImageBitmap(mybitmap);
            }
        }else{

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

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(EditProfile.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }






    private void show_hide_image_latout() {
        if (ll_image.getVisibility() == View.GONE) {
            ll_image.setVisibility(View.VISIBLE);
        }
    }

    private void set_setttings_ProfilePicFromFlagString(String s_name, String sttng) {
        SharedPreferences pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //editor.putString("memberid", response.optString("UserID"));
        String shrd_name;

        shrd_name = s_name;

        editor.putString(shrd_name, sttng);
        // editor.putString("Bp_Default_Position", def_pos);
        editor.commit();
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        imageName = globalVariable.getMemberImage();
        if (globalVariable.getMemberImage()!=null) {

            ProfilePicName = globalVariable.getMemberImage();
            imageName = globalVariable.getMemberImage();
            ProfilePicFlag = globalVariable.getProfilePicFlag();
            ImageStream= globalVariable.getStreamImage();
            ImageClick =false;
            if (ProfilePicName != null) { // image found

                flag = 1;
                profile_pic.setImageBitmap(null);
                profile_pic.setImageDrawable(null);
                profile_pic.setBackgroundDrawable(null);

                if (ProfilePicFlag.equals(objAvatar)) {

                    Integer nDrawable = -99;
                    if (ProfilePicName.equals("avtar1")) {
                        profile_pic.setImageResource(R.drawable.avtar1);

                    }

                    if (ProfilePicName.equals("avtar2")) {
                        profile_pic.setImageResource(R.drawable.avtar2);
                    }
                    if (ProfilePicName.equals("avtar3")) {
                        profile_pic.setImageResource(R.drawable.avtar3);
                    }
                    if (ProfilePicName.equals("avtar4")) {
                        profile_pic.setImageResource(R.drawable.avtar4);
                    }
                    if (ProfilePicName.equals("avtar5")) {
                        profile_pic.setImageResource(R.drawable.avtar5);
                    }
                    if (ProfilePicName.equals("avtar6")) {
                        profile_pic.setImageResource(R.drawable.avtar6);
                    }
                    if (ProfilePicName.equals("avtar7")) {
                        profile_pic.setImageResource(R.drawable.avtar7);
                    }
                    if (ProfilePicName.equals("avtar8")) {
                        profile_pic.setImageResource(R.drawable.avtar8);
                    }
                    if (ProfilePicName.equals("avtar9")) {
                        profile_pic.setImageResource(R.drawable.avtar9);
                    }
                    if (ProfilePicName.equals("avtar10")) {
                        profile_pic.setImageResource(R.drawable.avtar10);
                    }
                    if (ProfilePicName.equals("avtar11")) {
                        profile_pic.setImageResource(R.drawable.avtar11);
                    }
                    if (ProfilePicName.equals("avtar12")) {
                        profile_pic.setImageResource(R.drawable.avtar12);
                    }
                    if (ProfilePicName.equals("avtar13")) {
                        profile_pic.setImageResource(R.drawable.avtar13);
                    }
                    if (ProfilePicName.equals("avtar14")) {
                        profile_pic.setImageResource(R.drawable.avtar14);
                    }
                    if (ProfilePicName.equals("avtar15")) {
                        profile_pic.setImageResource(R.drawable.avtar15);
                    }
                    if (ProfilePicName.equals("avtar16")) {
                        profile_pic.setImageResource(R.drawable.avtar16);
                    }

                    picname = ProfilePicName;
                    ImageType = "A";
                    AvatarImage = ProfilePicName;


                }

                // Camera
                if (ProfilePicFlag.equals(objCamera)) {

                    ImageType = "I";
                    try {
                        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
                        imageLoader.displayImage("file://" + mediaStorageDir.getPath() + "/" + ProfilePicName, profile_pic,options);
                        //profile_pic.setImageBitmap(mybitmap);
                    } catch (Exception e) {
                    }
                    picname = imageName;
                    AvatarImage = ProfilePicName;
                }
                // Browse
                if (ProfilePicFlag.equals(objBrowse)) {
                    ImageType = "I";
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
                    imageLoader.displayImage("file://" + mediaStorageDir.getPath() + "/" + ProfilePicName, profile_pic,options);
                    //profile_pic.setImageBitmap(mybitmap);
                    picname = imageName;
                    AvatarImage = ProfilePicName;
                }
            }
        }
        globalVariable.setMemberImage(null);
    }
    private void CheckEditProfileMobileNo() {

        showPdialog("Loading. . .");
        String url = String.format(AppConfig.URL_GET_CHECKDUPLICATEEMAILIDMOBILENO, sMemberId, input_email.getText().toString(), input_mobile.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(EditProfile.this);

        String tag_string_req = "req_login";

        RequestFuture<JSONObject> future=RequestFuture.newFuture();

        JsonObjectRequest get_login_req = new JsonObjectRequest(Request.Method.GET,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.d(TAG, "Register Response: " + response.toString());

                if (response.optBoolean("bReturnFlag") == false)
                {
                    f_alert_ok("Error",response.optString("sReturnMsg"));
                    // lCheckEmailid = false;
                }
                else
                {
                    update_profile();
                    /*if (flag == 0) {

                        update_profile();

                    } else {


                        if (ImageType.equals("I")) {
                            new UploadFileToServer(AvatarImage, x).execute();
                        } else {
                            update_profile();
                        }

                    }*/
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        hidePDialog();
                        Log.e("HttpClient", "error: " + error.toString());
                        f_alert_ok("Error", "Server error");
                    }
                });


        queue.add(get_login_req);

    }

    public int getFlagResource(Context context, String name) {
        int resId = context.getResources().getIdentifier(name, "drawable", "com.viroopa.Buying");
        return resId;
    }
    private boolean CheckEmail_pattern(String emailid) {
        return EMAIL_PATTERN.matcher(emailid).matches();
    }


    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }


    private void show_personal_data()
    {

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(EditProfile.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
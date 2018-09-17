package viroopa.com.medikart;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.adapter.AD_ComboList;
import viroopa.com.medikart.adapter.AD_adapterCombo;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.helper.SQLiteHandler;


public class AddMember extends AppCompatActivity {

    private String sMemberId,setDate,gender;
    // private RadioButton male,female,other;
    private EditText firstname,lastname;
    private Spinner relation;
    private RadioGroup rdbgender;
    private RadioButton rdbMale;
    private RadioButton rdbFemale;
    private RadioButton rdbother;
    private Button seldate,submitbtn,clearbtn;
    private SQLiteHandler db;
    private static final String TAG = MainActivity.class.getSimpleName();
    long totalSize = 0;
    private ImageView profile_pic;
    private String ProfilePicName,genderForEdit;
    private Integer ProfilePicFlag=0;
    private   String StoragePath ="";
    private static int RESULT_LOAD_IMAGE = 1;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    Integer count = 0;
    private String image1,Mode="A";
    private int flag = 0;
    private Bitmap mybitmap;
    private String picname1 = "";
    private  String ImageType="I";
    Integer x=1;
    private TextView heading;
    private String selectDate="";
    private String form="";
    private String picname ="";
    private String FamilyImageName="";
    private EditText relationView;
    private  String MemberID,Smode;
    AD_adapterCombo relationadapter;
    ObjectItem[] ObjectItemData=null;
    private ProgressDialog pDialog;
    private String sInfo;
    Integer rel_id=8;
    private LinearLayout main_layout;
    private Integer relationcounter=0;
    private  Boolean AddpicFlag=false;
    private String ImageStream,ImageName;
    AppController globalVariable;
    private Boolean ImageClick=false;
    DisplayImageOptions options;

    Date current_date = Calendar.getInstance().getTime();
    //DateFormat dateFormat = new SimpleDateFormat("MMM dd, cccc");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat dateFormat_display = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateFormat_set_update = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat dateFormat_string = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_deatils);
        main_layout=(LinearLayout) findViewById(R.id.main_layout);
        setupUI(main_layout);
        globalVariable = (AppController) getApplicationContext();

        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        seldate=(Button)findViewById(R.id.txtpass);
        firstname = (EditText) findViewById(R.id.txtfname);
        submitbtn = (Button) findViewById(R.id.btnsbmit);
        clearbtn= (Button) findViewById(R.id.btnnocode);
        rdbgender = (RadioGroup) findViewById(R.id.sexgroup);
        rdbMale = (RadioButton) findViewById(R.id.btnmale);
        rdbFemale = (RadioButton) findViewById(R.id.btnfemale);
        rdbother = (RadioButton) findViewById(R.id.btnother);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        relationView=(EditText) findViewById(R.id.boxtextview);
        heading=(TextView)findViewById(R.id.textView8);
        db = new SQLiteHandler(getApplicationContext());


        Calendar c = Calendar.getInstance();
        final Integer curYear=c.get(Calendar.YEAR);
        final Integer curMonth=c.get(Calendar.MONTH)+1;
        final Integer curDay=c.get(Calendar.DAY_OF_MONTH);
        setDate=curYear  + "/" + curMonth + "/" + curDay;


        form="Load";
        initImageLoader();
        getIntenet();
        mode_selection();
      get_Relationship();


        // profile_pic.setImageResource(R.drawable.avtar1);


        seldate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                date_select();
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (Mode.equals("E"))
                {
                    relationcounter=1;
                }

                if(!firstname.getText().toString().isEmpty()) {
                    if(!seldate.getText().toString().isEmpty()) {
                        if(rdbMale.isChecked()||rdbFemale.isChecked()||rdbother.isChecked()) {
                            if (relationcounter==1) {
                                showPdialog("Loading ...");
                                //update_profile();
                                if (flag == 0  ) {
                                    update_profile();
                                } else {

                                    //  new UploadFileToServer("/storage/sdcard0/Pictures/Buying/mem_2_1.jpeg", x).execute();


                                   /* if (ImageType.equals("I")) {
                                        new UploadFileToServer(image1, x).execute();
                                    } else {
                                        update_profile();
                                    }*/
                                    update_profile();

                                }
                            }else{Toast.makeText(getApplicationContext(), "Please select relation", Toast.LENGTH_LONG)
                                    .show();}
                        }
                        else{Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_LONG)
                                .show();}
                    }else{ Toast.makeText(getApplicationContext(), "Please select Date of Birth", Toast.LENGTH_LONG)
                            .show();}
                    //  new UploadFileToServer("/storage/sdcard0/Pictures/Buying/mem_2_1.jpeg", x).execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_LONG)
                            .show();
                }
                // update_profile();
            }
        });

        relationView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(ObjectItemData!=null) {
                    relation_search();
                }else
                {
                    Toast.makeText(getApplicationContext(), "Unable to load the relations", Toast.LENGTH_LONG)
                            .show();
                    get_Relationship();
                }
            }
        });
        clearbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                firstname.setText("");
                //lastname.setText("");
                seldate.setText("");
                relationView.setText("");
                profile_pic.setImageResource(R.drawable.avtar1);
                rdbMale.setChecked(false);
                rdbFemale.setChecked(false);
                rdbother.setChecked(false);


            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                set_setttings_ProfilePicFromFlagString("ProfilePicFromFlag" ,"AddMember");
                ImageClick=true;
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
                        genderForEdit=gender;
                    } else if (rdbFemale.isChecked()) {
                        gender = "F";
                        genderForEdit=gender;
                    } else if (rdbother.isChecked()) {
                        gender = "O";
                        genderForEdit=gender;
                    }
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if ( globalVariable.getMemberImage()!=null) {
            ProfilePicName = globalVariable.getMemberImage();
            ImageClick = false;
            ProfilePicFlag = globalVariable.getProfilePicFlag();
            ImageStream = globalVariable.getStreamImage();
            if (ProfilePicName != null) {
                profile_pic.setImageBitmap(null);
                profile_pic.setImageDrawable(null);
                profile_pic.setBackgroundDrawable(null);
                flag = 1;


                if (ProfilePicFlag.equals(0)) {
                    if (ProfilePicName.equals("avtar1")) {
                        String imageUri = "drawable://" + R.drawable.avtar1;
                       // mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar1);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }

                    }
                    if (ProfilePicName.equals("avtar2")) {
                        String imageUri = "drawable://" + R.drawable.avtar2;
                       // mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar2);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar3")) {
                        String imageUri = "drawable://" + R.drawable.avtar3;
                       // mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar3);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar4")) {
                        String imageUri = "drawable://" + R.drawable.avtar4;
                       // mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar4);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar5")) {
                        String imageUri = "drawable://" + R.drawable.avtar5;
                       // mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar5);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar6")) {
                        String imageUri = "drawable://" + R.drawable.avtar6;
                       // mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar6);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar7")) {
                        String imageUri = "drawable://" + R.drawable.avtar7;
                       // mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar7);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar8")) {
                        String imageUri = "drawable://" + R.drawable.avtar8;
                       // mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar8);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar9")) {
                        String imageUri = "drawable://" + R.drawable.avtar9;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar9);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar10")) {
                        String imageUri = "drawable://" + R.drawable.avtar10;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar10);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar11")) {
                        String imageUri = "drawable://" + R.drawable.avtar11;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar11);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar12")) {
                        String imageUri = "drawable://" + R.drawable.avtar12;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar12);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar13")) {
                        String imageUri = "drawable://" + R.drawable.avtar13;
                       // mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar13);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar14")) {
                        String imageUri = "drawable://" + R.drawable.avtar14;
                       // mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar14);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar15")) {
                        String imageUri = "drawable://" + R.drawable.avtar15;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar15);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    if (ProfilePicName.equals("avtar16")) {
                        String imageUri = "drawable://" + R.drawable.avtar16;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        profile_pic.setImageResource(R.drawable.avtar16);
                        if (mybitmap != null) {
                            mybitmap.recycle();
                            mybitmap = null;
                        }
                    }
                    picname = ProfilePicName;
                    image1 = picname;
                    ImageType = "A";
                }

                if (ProfilePicFlag.equals(2)) {
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
                    //mybitmap = BitmapFactory.decodeFile(mediaStorageDir.getPath() + "/" + ProfilePicName);
                    File pathfile = new File(mediaStorageDir.getPath() + "/" + ProfilePicName);
                    imageLoader.displayImage("file://" + pathfile.getPath(), profile_pic,options);
                   // profile_pic.setImageBitmap(mybitmap);

                    ImageType = "I";
                    picname = ProfilePicName;

                }
                if (ProfilePicFlag.equals(1)) {

                    ImageType = "I";
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
                    File pathfile = new File(mediaStorageDir.getPath() + "/" + ProfilePicName);
                    imageLoader.displayImage("file://" + pathfile.getPath(), profile_pic,options);
                    picname = ProfilePicName;
                }


            }

        }
        globalVariable.setMemberImage(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.termscondition:
                Show_termscondition();
                return true;
            case R.id.return_cancel_policies:
                Show_Return_cancel_policies();
                return true;
            case R.id.order_transaction:
                Show_Order_Transaction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void Show_Order_Transaction() {
      //  Intent Intenet_change = new Intent(AddMember.this, Order_Transaction.class);
       // startActivity(Intenet_change);
    }
    public void Show_termscondition() {
       // Intent Intenet_change = new Intent(AddMember.this, TermsCondition.class);
       // startActivity(Intenet_change);
    }
    public void Show_Return_cancel_policies() {
       // Intent Intenet_change = new Intent(AddMember.this, TermsCondition.class);
       // startActivity(Intenet_change);
    }

    private void Show_Change_Profile_Picture() {
        Intent Intenet_add_profile_pic = new Intent(AddMember.this, AddProfilePic.class);
        if(Mode.equals("E")) {
            Intenet_add_profile_pic.putExtra("Mode", "E");
        }else{
            Intenet_add_profile_pic.putExtra("Mode", "A");
        }
        Intenet_add_profile_pic.putExtra("memberIdforEdit", MemberID);
        startActivity(Intenet_add_profile_pic);

    }
    private void set_setttings_ProfilePicString(String s_name,String sttng) {
        SharedPreferences pref =  this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //editor.putString("memberid", response.optString("UserID"));
        String shrd_name;

        shrd_name=s_name;

        editor.putString(shrd_name, sttng);
        // editor.putString("Bp_Default_Position", def_pos);
        editor.commit();
    }

    private void update_profile() {


        //Integer rel_id=relation.getSelectedItemPosition()+1;

        //String picname = "familymember" + "_" + sMemberId + "_" + "1" + ".jpeg";
        //String picname = sMemberId + "_"+ selectDate+ ".jpeg";
        // String sradomstring = UUID.randomUUID().toString();
        //String picname = sradomstring + ".jpeg";
        Map<String, String> params = new HashMap<String, String>();
        params.put("sMode", Smode);
        params.put("MemberId", sMemberId);
        params.put("FamilyMemberId", sMemberId);
        params.put("FamilyFullName", firstname.getText().toString());
        // params.put("MemberLName",  lastname.getText().toString());
        params.put("DOB", setDate);

        params.put("FamilyRelationship", String.valueOf(rel_id));
        params.put("PhoneNo", "");
        params.put("EmailId", "");
        params.put("InviteCode", "gigngn6");
        params.put("Gcm_Id", "");
        params.put("AcceptedId","");

        if(Smode.equals("E"))
        {
            params.put("ID", MemberID);
            params.put("FamilyGender", genderForEdit);
        }
        else{
            params.put("ID", "0");
            params.put("FamilyGender", gender);
        }

        if(ImageType.equals("I"))
        {
            params.put("ProfileImage",picname);
            FamilyImageName=picname;
            params.put("ImageStream",ImageStream);
        }
        else
        {
            params.put("ProfileImage",image1);
            FamilyImageName=image1;
            params.put("ImageStream","");
        }
        // params.put("ProfileImage", image1);
        params.put("SourcePath","");
        params.put("DestPath","");
        params.put("ImageType", ImageType);
        //params.put("ProfileImage", "fsf");
        //params.put("ProfileImage", "fsf");
        String Post_registration = "upload_profile";
        JSONObject jparams = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(AddMember.this);

        JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_ACCEPT_INVITE,
                //  new JSONObject(params),
                jparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        sucess_profile(response);
                        hidePDialog();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error_profile(error);
                        hidePDialog();
                    }
                })
        {
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

        //AppController.getInstance().addToRequestQueue(jor_inhurry_post, Post_registration);

        queue.add(jor_inhurry_post);
    }

    private void sucess_profile(JSONObject response) {

        //mb.showAlertDialog(this, "Add Member", "Member Added Successfully!", true);
        if(Smode.equals("E")) {

         /*   new AlertDialogWrapper.Builder(AddMember.this)
                    .setTitle("Edit Member")
                    .setMessage("Member Edited Successfully!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                            Intent Intenet_add_profile_pic = new Intent(AddMember.this, EditAndAddMember.class);
                            startActivity(Intenet_add_profile_pic);
                        }
                    }).show();*/


            final Toast toast = Toast.makeText(getApplicationContext(), "Member Edited Successfully! ", Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                    finish();
                    Intent Intenet_add_profile_pic = new Intent(AddMember.this, EditAndAddMember.class);
                    startActivity(Intenet_add_profile_pic);
                }
            }, 3000);
            // mb.showAlertDialog(this, "Edit Member", "Member Edited Successfully!", true);
            // finish();
        }else{

            db.addFAMILYMEMBER(Integer.parseInt(sMemberId), "9", firstname.getText().toString(),
                    0, setDate, gender, ImageType, FamilyImageName);

          /*  new AlertDialogWrapper.Builder(AddMember.this)
                    .setTitle("Add Member")
                    .setMessage( "Member Added Successfully!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                            Intent Intenet_add_profile_pic = new Intent(AddMember.this, EditAndAddMember.class);
                            startActivity(Intenet_add_profile_pic);
                        }
                    }).show();*/


            final Toast toast = Toast.makeText(getApplicationContext(), "Member Added Successfully! ", Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                    finish();
                    Intent Intenet_add_profile_pic = new Intent(AddMember.this, EditAndAddMember.class);
                    startActivity(Intenet_add_profile_pic);
                }
            }, 3000);
            //mb.showAlertDialog(this, "Add Member", "Member Added Successfully!", true);
            // finish();
        }


    }

    private void error_profile(VolleyError error) {
        String json = null;

        // mb.showAlertDialog(this, "Add Member", "error" + error.getMessage(), true);
        f_alert_ok("error",  "error" + error.getMessage());

    }



    public void date_select()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.date_med_reminder, null);



        final DatePicker Datepckr = (DatePicker) dialogview.findViewById(R.id.datePicker);
        final Button set = (Button) dialogview.findViewById(R.id.button4);
        final Button cancel = (Button) dialogview.findViewById(R.id.btncancel);
        builder.setView(dialogview);
        final Dialog dlg= builder.create();
        set.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                setDate = String.valueOf(Datepckr.getYear()) +"/"+ String.valueOf(Datepckr.getMonth()+1)+"/" + String.valueOf(Datepckr.getDayOfMonth());
                String displayDate = String.valueOf(Datepckr.getDayOfMonth()) +"/"+ String.valueOf(Datepckr.getMonth()+1)+"/" +String.valueOf(Datepckr.getYear());
                seldate.setText(displayDate);
                dlg.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                dlg.cancel();
            }
        });

        Datepckr.getCalendarView().setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Log.d("tag", "finally found the listener, the date is: year " + year + ", month " + month + ", dayOfMonth " + dayOfMonth);

                //   seldate.setText(year + "/" + month + "/" + dayOfMonth);


                //  setDate = String.valueOf(year) +"/"+ String.valueOf(month)+"/" + String.valueOf(dayOfMonth);
            }
        });



        dlg.show();

    }



    private void getIntenet()
    {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);

        Intent int_i= getIntent();
        sMemberId = pref.getString("memberid", "");
        String sMemberList = (pref.getString("memberList", ""));
        sInfo=sMemberList;

        ImageName=pref.getString("imagename", "");
        Mode=int_i.getStringExtra("Mode");
        MemberID=int_i.getStringExtra("memberIdforEdit");

      /*if(!ImageName.equals(""))
      {
         if(ImageName.startsWith("avtar"))
         {
             Resources res = getResources();

             int resourceId = res.getIdentifier(ImageName, "drawable", getPackageName());
             Drawable drawable = res.getDrawable(resourceId);
             profile_pic.setImageDrawable(drawable);
         }else{
             profile_pic.setImageBitmap(null);
             profile_pic.setImageDrawable(null);
             profile_pic.setBackgroundDrawable(null);
             File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),AppConfig.IMAGE_DIRECTORY_NAME);
             mybitmap = BitmapFactory.decodeFile(mediaStorageDir.getPath()+"/"+ProfilePicName);
             profile_pic.setImageBitmap(mybitmap);
         }
      }else{*/
          profile_pic.setImageBitmap(null);
          profile_pic.setImageDrawable(null);
          profile_pic.setBackgroundDrawable(null);
          profile_pic.setImageResource(R.drawable.avtar1);
      //}




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
    private void set_setttings_ProfilePicFromFlagString(String s_name,String sttng) {
        SharedPreferences pref =  this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //editor.putString("memberid", response.optString("UserID"));
        String shrd_name;

        shrd_name=s_name;

        editor.putString(shrd_name, sttng);
        // editor.putString("Bp_Default_Position", def_pos);
        editor.commit();
    }
    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(AddMember.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }





    public  void relation_search()
    {

        Integer pos;
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);
        final EditText inputSearch = (EditText) dialogview.findViewById(R.id.inputSearch);
        builder.setView(dialogview);
        // Adding items to listview


        relationadapter = new AD_adapterCombo(this, R.layout.list_item,ObjectItemData);
        lv.setAdapter(relationadapter);

        final TextView Titile =(TextView)dialogview.findViewById(R.id.textView);
        Titile.setText("Select Relation");
        inputSearch.setHint("Enter Relation");
        final TextView btncancel=(TextView)dialogview.findViewById(R.id.btncancel);
        final Dialog dlg= builder.create();
        lv.setOnItemClickListener(new OnItemClickListenerListViewItem(dlg));

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String text = cs.toString().toLowerCase(Locale.getDefault());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });




        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dlg.cancel();
            }
        });

        dlg.show();
    }
    public class OnItemClickListenerListViewItem implements AdapterView.OnItemClickListener {
        Dialog d=null;
        OnItemClickListenerListViewItem(Dialog dlg)
        {
            d=dlg;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Context context = view.getContext();
            relationcounter=1;

            TextView textViewItem = ((TextView) view.findViewById(R.id.product_name));
            String listItemText = textViewItem.getText().toString();
            String listItemId = textViewItem.getTag().toString();
            rel_id= Integer.parseInt(listItemId);

            relationView.setText(listItemText);
            d.cancel();

        }
    }
    private void objectItemLoad(JSONObject response)
    {
        try {
            JSONArray j_array = response.getJSONArray("v_Relationlist");

            ObjectItemData = new ObjectItem[j_array.length()];
            for(int i=0;i<j_array.length();i++)
            {
                JSONObject j_obj=j_array.getJSONObject(i);
                ObjectItemData[i] = new ObjectItem(j_obj.optInt("Relationship_ID"),j_obj.getString("Relationship_Name"));
            }

        }catch (Exception e)
        {

        }



    }
    private void mode_selection()
    {

        if(Mode.equals("E"))
        {
            Smode="E";
            heading.setText("Edit Member");
            try {
                JSONArray response = new JSONArray(sInfo);

                for (int i = 0; i < response.length(); i++) {
                    JSONObject info = (JSONObject) response.get(i);
                    String RelatinShpId=info.getString("PatientId");



                    if(RelatinShpId.equals(MemberID))
                    {

                        firstname.setText(info.getString("MemberName"));


                        String ImagePath=info.getString("ImageUrl");


                        String imgeName=ImagePath.substring(ImagePath.lastIndexOf('/') + 1, ImagePath.length());




                            if (imgeName.startsWith("avtar")) {
                                Resources res = getResources();
                                picname = imgeName;
                                int resourceId = res.getIdentifier(imgeName, "drawable", getPackageName());
                                Drawable drawable = res.getDrawable(resourceId);
                                profile_pic.setImageDrawable(drawable);
                            } else {
                                String iconsStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + AppConfig.IMAGE_DIRECTORY_NAME;

                                File pathfile = new File(iconsStoragePath + File.separator + imgeName);
                                picname = imgeName;
                               // mybitmap = BitmapFactory.decodeFile(pathfile.getPath());
                                if (pathfile.exists()) {

                                    imageLoader.displayImage("file://" + pathfile.getPath(), profile_pic,options);
                                   // profile_pic.setImageBitmap(mybitmap);
                                } else {
                                    try {
                                        imageLoader.displayImage(ImagePath, profile_pic,options);
                                    } catch (Exception e) {
                                    }
                                }
                            }

                        String sSession_Date = info.getString("MemberDOB");

                        if (!sSession_Date.equals(""))
                        {
                            try {
                                current_date = dateFormat.parse(sSession_Date);

                                seldate.setText(dateFormat_display.format(current_date));
                                setDate=dateFormat_set_update.format(current_date);


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        //seldate.setText(info.getString("MemberDOB"));


                        String gender=info.getString("MemberGender");

                        if(gender.equals("M"))
                        {
                            rdbMale.setChecked(true);
                            genderForEdit="M";


                        }
                        if(gender.equals("F"))
                        {
                            rdbFemale.setChecked(true);
                            genderForEdit="F";
                        }
                        if(gender.equals("O")) {
                            rdbother.setChecked(true);
                            genderForEdit="O";
                        }

                        try {
                            rel_id = Integer.parseInt(info.getString("RelationshipId"));

                        }catch (NumberFormatException e)
                        {
                            e.toString();
                        }
                       /* if(info.getString("RelationshipId").equals("1")) {
                            relationView.setText("Mother");
                        }
                        if(info.getString("RelationshipId").equals("2")) {
                            relationView.setText("Father");
                        }
                        if(info.getString("RelationshipId").equals("3")) {
                            relationView.setText("Brother");
                        } if(info.getString("RelationshipId").equals("4")) {
                        relationView.setText("Sister");
                    }
                        if(info.getString("RelationshipId").equals("5")) {
                            relationView.setText("Wife");
                        }
                        if(info.getString("RelationshipId").equals("6")) {
                            relationView.setText("Son");
                        }
                        if(info.getString("RelationshipId").equals("7")) {
                            relationView.setText("Daughter");
                        }
                        if(info.getString("RelationshipId").equals("8")) {
                            relationView.setText("Self");
                        }
                        if(info.getString("RelationshipId").equals("9")) {
                            relationView.setText("Friend");
                        }*/

                        relationView.setText(info.getString("Relationship_name"));
                    }
                }


            }catch (Exception e)
            {

            }

        }else
        { Smode="A";

        }
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                AddMember.this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }



    public void update_local_storage(String name)
    {
        set_setttings_ProfilePicString("MemberProfilePicName", name);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        //Intent Intenet_add_profile_pic = new Intent(AddMember.this, AddProfilePic.class);
        //startActivity(Intenet_add_profile_pic);
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
                    hideSoftKeyboard(AddMember.this);
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

    private void get_Relationship() {


        String sParameter="r";

        showPdialog("Loading. . .");
        String url =   String.format(AppConfig.URL_GET_RELATIONSHIPS,sParameter);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                objectItemLoad(response);
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Toast.makeText(getApplicationContext(), "Unable to connect .Please try after sometime" , Toast.LENGTH_LONG)
                                .show();

                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);
    }
}
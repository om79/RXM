package viroopa.com.medikart.buying;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.AddMember;
import viroopa.com.medikart.MainActivity;
import viroopa.com.medikart.ObjectItem;
import viroopa.com.medikart.R;
import viroopa.com.medikart.TermsCondition;
import viroopa.com.medikart.adapter.AD_adapterCombo;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.gallery.Action;
import viroopa.com.medikart.buying.gallery.CustomGallery;
import viroopa.com.medikart.buying.gallery.GalleryAdapter;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.image_zoom_view;
import viroopa.com.medikart.model.M_doctorlist;
import viroopa.com.medikart.model.M_memberlist;
import viroopa.com.medikart.model.citylist;
import viroopa.com.medikart.util.ImageUtils;

/**
 * Created by admin on 03/08/2015.
 */
public class uploadprescriptionActivity extends AppCompatActivity {

    private String sMemberId;
    int     selected_memberid=0;
    int     selected_MemberFamilyNo=0;
    String  selected_Member_Name="";
    int     selected_Member_Relationship_ID=0;
    String  selected_Member_DOB="";
    String  selected_Member_Gender="";
    String  sSelfGender="";
    private Integer sDoctId=0;
    AppController globalVariable;
    private RelativeLayout nextlayout,previouslayout;

    private Boolean doctorRefresh=false;

    private Spinner spnrobjFamilyMember;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CAMERA=12457;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE=12458;
    private static final int BROWSE_CODE = 200;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String IMEINumber_1;
    private static String IMEINumber_2;
    private static String IMEINumber_3;
    private static String IMEINumber_4;
    JSONObject jsonobject;
    JSONArray jsonarray;
    private Button btnbrowse;
    private Button btnTake;
    private Button btnsubmit;
    private Button btncancel;
    Boolean AddDoctorShowVisible=false;
    AD_adapterCombo   besttimeadapter,doctorAdapter,patientforAdapter;
    private   Integer PatientId=0;
    ArrayList<String> member_list;
    private Integer iPrescriptionFor=0;
    ArrayList<M_memberlist> Currentmember_list;
    int selected_FamilyMemberid = -99;
    private TextView btndoctor ;
    ArrayAdapter<String> dataDoctorAdapter;
    ArrayAdapter<String> dataPatientForAdapter;
    private Integer selected_MemberFamilyid = -99;
    private String selected_MemberFamilyName = "";

    private String prescriptionfor;
    private String prsname;
    private Integer timeslot=0;
    private String msg;
    private String image1;
    private String image2;
    private String image3;
    private String image4;

    private String picname1="";
    private String picname2="";
    private String picname3="";
    private String picname4="";
    private String sSelfName="";
    private Integer serial = 0;
    private Bitmap mybitmap;
    private  String sCardId;

    private static int RESULT_LOAD_IMAGE = 1;
    private ProgressDialog pDialog;

    private static String IMEINumber;


    private EditText txtFamilyname;

    private Uri fileUri;
    private String selectedCountry = null;

    private Spinner sp_cityname,sp_self,bestime;
    private Button sp_doctor;

    private EditText input_name;
    private EditText input_message;

    private TextView input_sp_prescription;
    private TextView input_timeslot;

    ArrayList<citylist> currentcity;

    GridView gridGallery;
    GalleryAdapter obj_GalleryAdapter;
    String action;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    ImageView imgSinglePick;

    ArrayList<M_doctorlist> doctorlists = new ArrayList<M_doctorlist>();
    ArrayList<String> doctorname = new ArrayList<String>();


    ArrayList<CustomGallery> imagePathArray = new ArrayList<CustomGallery>();
    String TakeImage,CompressedImage;

    private EditText txtdoctorname;
    private EditText clinicname;

    private RadioGroup sexgroup;
    ProgressDialog dialog = null;
    AD_adapterCombo FamilyMemberadapter;
    ObjectItem[] ObjectItemData=null;
    ObjectItem[] ObjectItemBestTime=null;
    ObjectItem[] ObjectItemPrescriptionFor=null;
    ObjectItem[] ObjectItemDoctor=null;
    Integer x=1;
    Integer count=0;
    String sNewDoctorName, sNewClinicName;
    Integer nNewCityId;
    private  String sradomstringCamera ="";
    private SQLiteHandler db;
    private String imagepathforzoom;
    private EditText txtprescriptionName;
    private RadioGroup rdbgender;
    private RadioButton rdbMale;
    private RadioButton rdbFemale;
    private RadioButton rdbother;
    private LinearLayout ll_image;
    private RelativeLayout main_layout;
    private String gender="";
    private int Familydetail=0;
    private String selectDate="";
    private String sPrescriptionFor="S";
    private Integer Selectedgender;
    private  boolean isEmergency=false;
    private CheckBox chkisEmergency;
    private  RelativeLayout top_flow_relative,bottom_relative;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};


    JSONArray imgarr = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        setContentView(R.layout.upload_prescription);
        main_layout=(RelativeLayout)findViewById(R.id.main_layout);
        setupUI(main_layout);
        globalVariable = (AppController) getApplicationContext();
        mybitmap = null;
        objectItemBestTimeLoad();
        objectItemPrescriptionForLoad();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        spnrobjFamilyMember= (Spinner) findViewById(R.id.spnrFamilyMember);
        top_flow_relative= (RelativeLayout) findViewById(R.id.top_flow_relative);
        bottom_relative= (RelativeLayout) findViewById(R.id.bottom_relative);

        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        btncancel = (Button) findViewById(R.id.btncancel);

        getIntenet();
        getFamilymember_list(1);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        Calendar c = Calendar.getInstance();

        final Integer curYear=c.get(Calendar.YEAR);
        final Integer curMonth=c.get(Calendar.MONTH)+1;
        final Integer curDay=c.get(Calendar.DAY_OF_MONTH);
        final Integer curHH=c.get(Calendar.HOUR);
        final Integer curMM=c.get(Calendar.MINUTE);
        final Integer curSS=c.get(Calendar.SECOND);
        selectDate=curYear  + "-" + curMonth + "-" + curDay +"-"+curHH +"-"+curMM+"-"+curSS;

        sp_cityname = (Spinner) findViewById(R.id.sp_cityname);
        sp_doctor = (Button) findViewById(R.id.sp_doctor);
        input_sp_prescription = (TextView) findViewById(R.id.sp_prescription);

        btnbrowse = (Button) findViewById(R.id.btnbrowse);
        btnTake = (Button) findViewById(R.id.btnTake);

        member_list = new ArrayList<String>();
        Currentmember_list = new ArrayList<M_memberlist>();
        btndoctor = (TextView) findViewById(R.id.btn_doctor);
        chkisEmergency = (CheckBox) findViewById(R.id.chkisemergency);
        nextlayout= (RelativeLayout) findViewById(R.id.nextlayout);
        previouslayout= (RelativeLayout) findViewById(R.id.previouslayout);


        txtFamilyname=(EditText) findViewById(R.id.txtFamilyname);
        txtprescriptionName=(EditText) findViewById(R.id.txtprescriptionName);
        rdbgender = (RadioGroup) findViewById(R.id.sexgroup);
        rdbMale = (RadioButton) findViewById(R.id.btnmale);
        rdbFemale = (RadioButton) findViewById(R.id.btnfemale);
        rdbother = (RadioButton) findViewById(R.id.btnother);

        sexgroup = (RadioGroup) findViewById(R.id.sexgroup);

        txtFamilyname.setFocusable(false);
        txtFamilyname.setClickable(true);
        input_name  = (EditText) findViewById(R.id.name);
        input_message = (EditText) findViewById(R.id.message);
        input_timeslot = (TextView) findViewById(R.id.bestime);

        input_sp_prescription.setText("Self");

        ll_image = (LinearLayout) findViewById(R.id.ll_image);
        //getFamilymember_list(0);
        /*img1cancel=(TextView)findViewById(R.id.cancel1);
        img2cancel=(TextView)findViewById(R.id.cancel2);
        img3cancel=(TextView)findViewById(R.id.cancel3);
        img4cancel=(TextView)findViewById(R.id.cancel4);
*/
        initImageLoader();
        init();

        chkisEmergency.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    isEmergency = true;

                } else {
                    isEmergency = true;
                }

            }
        });


        input_timeslot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                best_time_for_call();
            }
        });


        nextlayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (!gender.equals(""))
                {
                    //sDoctId = doctorlists.get(sp_doctor.getSelectedItemPosition()).getDoctorid();
//                    String sPrescriptionFor=input_sp_prescription.getSelectedItem().toString();
                    // String sPrescriptionFor=sPrescriptionFor;

                    if (! input_name.getText().toString().isEmpty()) {
                        if(!input_sp_prescription.getText().toString().isEmpty()) {

                            if( sDoctId!=0) {
                                if(!txtprescriptionName.getText().toString().isEmpty()) {
                                    if (imagePathArray.size() > 0) {
                                        showPdialog("Loading ...");
                                        //prescriptionfor = input_sp_prescription.getSelectedItem().toString();
                                        prescriptionfor = sPrescriptionFor;
                                        prsname = input_name.getText().toString();
                                        // timeslot = input_timeslot.getSelectedItem().toString();
                                        msg = input_message.getText().toString();
                                        post_uploadprption();
                                        // new UploadFileToServerFirst(imagePathArray.get(0).sdcardPath, x).execute();
                                        //f_alert_ok("Information","Upload Successfully.");
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please Upload Prescription!", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please Enter Prescription Name!", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Please select Doctor!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Please select a Prescription Name!", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please Enter Name!", Toast.LENGTH_LONG)
                                .show();
                    }


                }
                else

                {
                    Toast.makeText(getApplicationContext(), "Please select gender!", Toast.LENGTH_LONG)
                            .show();

                }

                //f_clear_form();





            }
        });
        previouslayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });

        spnrobjFamilyMember.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

                selected_memberid = 0;
                selected_MemberFamilyNo = 0;
                selected_Member_Name = "";
                selected_Member_Relationship_ID = 0;
                selected_Member_DOB = "";
                selected_Member_Gender = "";
                selected_FamilyMemberid = -99;

                //  Familydetail = 0;
                if (!Currentmember_list.isEmpty() && (position >= 0)) {
                    Familydetail = 1;
                    selected_Member_Relationship_ID = Currentmember_list.get(position).getRelationshipId();
                    selected_memberid = Currentmember_list.get(position).getMemberId();
                    selected_MemberFamilyNo = Currentmember_list.get(position).getMemberFamilyNo();
                    selected_Member_Name = Currentmember_list.get(position).getMemberName();
                    selected_Member_Relationship_ID = Currentmember_list.get(position).getRelationshipId();
                    selected_Member_DOB = Currentmember_list.get(position).getMemberDOB();
                    selected_Member_Gender = Currentmember_list.get(position).getMemberGender();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
//                selected_memberid = 0;
//                selected_MemberFamilyNo = 0;
//                selected_Member_Name = "";
//                selected_Member_Relationship_ID = 0;
//                selected_Member_DOB = "";
//                selected_Member_Gender = "";
                selected_FamilyMemberid = -99;
            }
        });

        txtFamilyname.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (iPrescriptionFor == 1) {
                    sPrescriptionFor = "R";
                    // getFamilymember_list(1);
                    FamilyMember_search();
                }


            }
        });


        // Register Button Click event
        input_sp_prescription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // best_time_for_call();
                Patient_for();
            }
        });


        sp_doctor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                add_doctor_combo_box();
            }
        });


        sp_cityname.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // f_alert_ok("", "City : position = " + position + " id=" + id);
                        if (position > 0) {
                            getdoctorlist(position);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // f_alert_ok("", "City: Unselected");
                    }
                });


        // Link to Login Screen
       /* btnbrowse.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent browse_intenet = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(browse_intenet, RESULT_LOAD_IMAGE);
            }
        });*/

        btnTake.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (imagePathArray.size() <= 3) {
                    serial++;
                    captureImage();
                } else {
                    Toast.makeText(getApplicationContext(), "you can take only four images", Toast.LENGTH_LONG).show();
                }
            }
        });

        btndoctor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

/*                if (sp_cityname.getSelectedItemPosition() > 0)
                {
                    f_add_doctor();
                }
                else
                {
                    f_alert_ok("Information"," Select City");
                }*/

                f_add_doctor();

            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if (!txtFamilyname.getText().toString().equals(""))
                {
                    if (!gender.equals(""))
                    {
                        //sDoctId = doctorlists.get(sp_doctor.getSelectedItemPosition()).getDoctorid();
//                    String sPrescriptionFor=input_sp_prescription.getSelectedItem().toString();
                        // String sPrescriptionFor=sPrescriptionFor;

                        if (! input_name.getText().toString().isEmpty()) {
                            if(!input_sp_prescription.getText().toString().isEmpty()) {

                                if( sDoctId!=0) {
                                    if(!txtprescriptionName.getText().toString().isEmpty()) {
                                        if (imagePathArray.size() > 0) {
                                            showPdialog("Loading ...");
                                            //prescriptionfor = input_sp_prescription.getSelectedItem().toString();
                                            prescriptionfor = sPrescriptionFor;
                                            prsname = input_name.getText().toString();
                                            // timeslot = input_timeslot.getSelectedItem().toString();
                                            msg = input_message.getText().toString();
                                            post_uploadprption();
                                            // new UploadFileToServerFirst(imagePathArray.get(0).sdcardPath, x).execute();
                                            //f_alert_ok("Information","Upload Successfully.");
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please Upload Prescription!", Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Please Enter Prescription Name!", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please select Doctor!", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Please select a Prescription Name!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please Enter Patient Name!", Toast.LENGTH_LONG)
                                    .show();
                        }


                    }
                    else

                    {
                        Toast.makeText(getApplicationContext(), "Please select gender!", Toast.LENGTH_LONG)
                                .show();
                        hidePDialog();
                    }
                }
                else

                {
                    Toast.makeText(getApplicationContext(), "Please enter name!", Toast.LENGTH_LONG)
                            .show();
                    hidePDialog();
                }

                //f_clear_form();
            }
        });
 /* Attach CheckedChangeListener to radio group */
        rdbgender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    //valueweight = 0;


                    gender = "M";
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


        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // fp_Clear();
                product_test();
                // finish();


            }
        });

        doctorname.clear();
        doctorlists.clear();

        // Default add Doctor
        M_doctorlist doctordefault = new M_doctorlist();

        doctordefault.setCityId(0);
        doctordefault.setMemberid(0);
        doctordefault.setDoctorid("0");
        doctordefault.setDoctorname("Select Doctor");

        doctorlists.add(doctordefault);
        doctorname.add("Select Doctor");

        //getcitylist();
        getdoctorlist(0);

        get_member_data();
        DefualtPatientName();
    }

    @Override
    public void onResume() {
        super.onResume();
        obj_GalleryAdapter.notifyDataSetChanged();
        if(doctorRefresh==true) {
            getdoctorlist(0);
            doctorRefresh=false;
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
        Intent Intenet_change = new Intent(uploadprescriptionActivity.this, Order_Transaction.class);
        startActivity(Intenet_change);
    }
    public void Show_Return_cancel_policies() {
        Intent Intenet_change = new Intent(uploadprescriptionActivity.this, TermsCondition.class);
        startActivity(Intenet_change);
    }
    public void Show_termscondition() {
        Intent Intenet_change = new Intent(uploadprescriptionActivity.this, TermsCondition.class);
        startActivity(Intenet_change);
    }
    //    private void f_add_doctor() {
//        MaterialDialog dialog = new MaterialDialog.Builder(this)
//                .title("Add Doctor")
//                .customView(R.layout.add_doctor, true)
//                .positiveText("Add")
//                .negativeText("Cancel")
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
//                        post_new_doctor();
//                    }
//
//                    @Override
//                    public void onNegative(MaterialDialog dialog) {
//                    }
//
//                }).build();
//
//        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
//        //noinspection ConstantConditions
//        txtdoctorname = (EditText) dialog.getCustomView().findViewById(R.id.doctorname);
//        txtdoctorname.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                positiveAction.setEnabled(s.toString().trim().length() > 0);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        clinicname = (EditText) dialog.getCustomView().findViewById(R.id.clinicname);
//
//        /*int widgetColor = ThemeSingleton.get().widgetColor;
//        MDTintHelper.setTint(txtdoctorname,widgetColor == 0 ? getResources().getColor(R.color.material_teal_500) : widgetColor);
//        MDTintHelper.setTint(clinicname,widgetColor == 0 ? getResources().getColor(R.color.material_teal_500) : widgetColor);*/
//
//        dialog.show();
//        if (txtdoctorname.length() > 0)
//        {
//            positiveAction.setEnabled(true); // disabled by default
//        }
//        else{
//            positiveAction.setEnabled(false); // disabled by default
//        }
//    }


    private void f_add_doctor()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.add_doctor, null);

        final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
        final Button cancelbtn = (Button) dialogview.findViewById(R.id.btnclear);
        builder.setView(dialogview);
        final Dialog dlg= builder.create();

        txtdoctorname = (EditText) dialogview.findViewById(R.id.doctorname);
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

        clinicname = (EditText) dialogview.findViewById(R.id.clinicname);

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(!txtdoctorname.getText().toString().isEmpty() &&!clinicname.getText().toString().isEmpty()) {
                    post_new_doctor(txtdoctorname.getText().toString(), clinicname.getText().toString());
                    AddDoctorShowVisible=false;
                    dlg.dismiss();
                }
                else
                {
                    f_alert_ok("Add Doctor","Please Enter doctor name or clinic name");
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

    private void get_member_data() {
        db = new SQLiteHandler(getApplicationContext());

        ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
        test.add(db.getUserDetails());

        HashMap<String, String> m = test.get(0);
        nNewCityId = Integer.parseInt(m.get("city").toString());
        input_name.setText(m.get("name").toString());
        sSelfName=m.get("name").toString();
        input_name.setFocusable(false);
        input_name.setClickable(true);
        String getGender=m.get("gender").toString();
        sSelfGender=m.get("gender").toString();
//        getGender="F";
        if(getGender.equals("M")) {
            rdbMale.setChecked(true);
        }
        else if(getGender.equals("F")) {
            rdbFemale.setChecked(true);
        }
        else if(getGender.equals("O")) {
            rdbother.setChecked(true);
        }
//        // rdbMale.setChecked(true);
//        rdbMale.setEnabled(false);
//        rdbFemale.setEnabled(false);
//        rdbother.setEnabled(false);
    }

    public void DefualtOthers(){
        spnrobjFamilyMember.setAdapter(null);
        M_memberlist memberdetails = new M_memberlist();


        String vmembername =  input_name.getText().toString();

        member_list.add(vmembername);
        Currentmember_list.add(memberdetails);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,member_list );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrobjFamilyMember.setAdapter(dataAdapter);
        spnrobjFamilyMember.setFocusableInTouchMode(true);
        input_name.setText("Others");

    }

    private void post_new_doctor(String doctor,String Clinic) {


        sNewDoctorName = doctor;
        sNewClinicName = Clinic ;

        Map<String, String> params = new HashMap<String, String>();

        params.put("iMemberId", sMemberId);
        params.put("iDistrictid", nNewCityId.toString());
        params.put("sDoctorName", doctor);
        // params.put("MemberLName",  lastname.getText().toString());
        params.put("sClinicName", Clinic);
        params.put("DoctorId", "0");
        params.put("Mobile_No", "");
        params.put("email_id", "");

        String Post_registration = "ADDDoctorJson";
        JSONObject jparams = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(uploadprescriptionActivity  .this);

        JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_ADDDoctorJson,
                //  new JSONObject(params),
                jparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Success_add_doctor(response);
                        hidePDialog();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Error_add_doctor(error);
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

        //AppController.getInstance().addToRequestQueue(jor_inhurry_post, Post_registration);

        queue.add(jor_inhurry_post);


    }

    public void DefualtPatientName(){
        txtFamilyname.setFocusable(false);
        txtFamilyname.setClickable(true);
        M_memberlist memberdetails = new M_memberlist();
        Currentmember_list.clear();
        String vmembername =  sSelfName;

        member_list.add(vmembername);
        Currentmember_list.add(memberdetails);
        txtFamilyname .setText(vmembername);
        input_name.setText(vmembername);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,member_list );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // ObjectItemData = new ObjectItem[1];
        // ObjectItemData[0] = new ObjectItem(8,vmembername);
        // FamilyMember_search();
        spnrobjFamilyMember.setAdapter(dataAdapter);
        spnrobjFamilyMember.setFocusableInTouchMode(true);


        //input_name.setEnabled(false);
        // txtFamilyname.setEnabled(false);
        if(sSelfGender.equals("M")) {
            rdbMale.setChecked(true);
        }
        else if(sSelfGender.equals("F")) {
            rdbFemale.setChecked(true);
        }
        else if(sSelfGender.equals("O")) {
            rdbother.setChecked(true);
        }
        //input_name.setText("Self");

    }

    private void getFamilymember_list(int position ) {

        showPdialog("loading...");
        String url = String.format(AppConfig.URL_GET_MEMBERFamilyLIST, sMemberId);

        RequestQueue queue = Volley.newRequestQueue(uploadprescriptionActivity.this);

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
        queue.add(FamilyMemberrequest);

    }


    private void Success_FamilyMember(JSONObject response) {
        try {
            member_list.clear();
            Currentmember_list.clear();
            jsonarray = response.getJSONArray("PatientList");

            ObjectItemData = new ObjectItem[jsonarray.length()];
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                M_memberlist memberdetails = new M_memberlist();

                memberdetails.setMemberId(jsonobject.optInt("MemberId"));
                memberdetails.setMemberName(jsonobject.optString("MemberName"));
                memberdetails.setMemberGender(jsonobject.optString("MemberGender"));
                memberdetails.setRelationshipId(jsonobject.optInt("RelationshipId"));
                memberdetails.setMemberDOB(jsonobject.optString("MemberDOB"));
                memberdetails.setPatientId(jsonobject.optString("PatientId"));

                String vmembername = jsonobject.getString("MemberName");

                member_list.add(vmembername);
                Currentmember_list.add(memberdetails);


                ObjectItemData[i] = new ObjectItem(jsonobject.optInt("RelationshipId"), jsonobject.optString("MemberName"));

                Integer nMemberId=Integer.parseInt(jsonobject.getString("MemberId"));
                String sMemberFamilyNo="";
                String sMemberName=jsonobject.getString("MemberName");
                Integer iRelationshipId=jsonobject.optInt("RelationshipId");
                String MemberDOB=jsonobject.getString("MemberDOB");
                String sMemberGender=jsonobject.getString("MemberGender");

                db.addFAMILYMEMBER(nMemberId, sMemberFamilyNo, sMemberName, iRelationshipId, MemberDOB, sMemberGender,"I","");
                //f_alert_ok("Sucess","Entry Saved");
            }
            spnrobjFamilyMember.setAdapter(null);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,member_list );
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spnrobjFamilyMember.setAdapter(dataAdapter);
            spnrobjFamilyMember.setFocusableInTouchMode(true);


            hidePDialog();
            //mb.showAlertDialog(RegisterActivity.this, "done", "done", true);

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
    private void Success_add_doctor(JSONObject response){
        try {
            // Add new Doctor
            //ja_cart = response.getJSONArray("ProductDetail");
            doctorRefresh=true;

            String sDoctorid_res = response.optString("DoctorId");
            M_doctorlist doctornew = new M_doctorlist();

            doctornew.setCityId(nNewCityId);
            doctornew.setMemberid(Integer.parseInt(sMemberId));
            doctornew.setDoctorid(sDoctorid_res);
            doctornew.setDoctorname(sNewDoctorName);

            doctorlists.add(doctornew);
            doctorname.add(sNewDoctorName);
            dataDoctorAdapter.notifyDataSetChanged();

            getdoctorlist(1);
            sp_doctor.setText(sNewDoctorName);
            sDoctId=Integer.parseInt(sDoctorid_res);
            // Add new Doctor

            f_alert_ok("Success", "Doctor Added Successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Error_add_doctor(VolleyError error) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void post_uploadprption() {


        if (!gender.isEmpty())
        {

            Map<String, Object> params = new HashMap<String, Object>();

            if(sCardId!=null)
            {
                params.put("CartId",sCardId);
            }else
            {
                params.put("CartId","");
            }



            params.put("PrescriptionName",txtprescriptionName.getText().toString());
            params.put("PrescriptionFor", input_sp_prescription.getText().toString());
            params.put("PatientName", txtFamilyname.getText().toString());
            params.put("AlternatePhoneNo", "");
            params.put("DistrictId", nNewCityId);
            params.put("Comment", input_message.getText().toString());
            params.put("RequestFrom", "A"); // Android
            params.put("DestPath", "");
            params.put("SourcePath", "");
            params.put("preImg", imgarr);
            params.put("DoctorId", sDoctId);
            params.put("MemberFamilyId", PatientId);
            params.put("DestPath", "");
            params.put("SourcePath", "");
            params.put("MemberId", sMemberId);
            params.put("BestTimeForCall", timeslot);
            params.put("EmergencyDate", "");
            params.put("Other", "");
            params.put("isEmergencyDelivery", isEmergency);

            Log.d("descrptionLog11",txtprescriptionName.getText().toString());
            Log.d("descrptionLog12",input_sp_prescription.getText().toString());
            Log.d("descrptionLog13",txtFamilyname.getText().toString());
            Log.d("descrptionLog14", String.valueOf(nNewCityId));
            Log.d("descrptionLog15",input_message.getText().toString());
            Log.d("descrptionLog16", String.valueOf(imgarr));
            Log.d("descrptionLog17", String.valueOf(sDoctId));
            Log.d("descrptionLog18", String.valueOf(PatientId));
            Log.d("descrptionLog19", String.valueOf(sMemberId));
            Log.d("descrptionLog111", String.valueOf(timeslot));
            Log.d("descrptionLog112", String.valueOf(isEmergency));

            JSONObject jparams = new JSONObject(params);

            RequestQueue queue = Volley.newRequestQueue(uploadprescriptionActivity.this);

            JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConfig.URL_POST_UPLOADPRESCRIPTION,

                    jparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("uploadDecript", String.valueOf(response));
                            post_uploadsucess(response);
                            hidePDialog();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("uploadDecriptERROR", String.valueOf(error));
                            post_uploaderror(error);
                            hidePDialog();
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

            //AppController.getInstance().addToRequestQueue(jor_inhurry_post, Post_registration);

            queue.add(jor_inhurry_post);
        }
        else

        {
            Toast.makeText(getApplicationContext(), "Please select gender!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void post_uploadsucess(JSONObject response) {



        try
        {
            if (response.getString("bReturnFlag").equals("true")) {

                if(sCardId==null) {


                    new AlertDialog.Builder(uploadprescriptionActivity.this)
                            .setTitle("Success")
                            .setMessage(response.getString("sReturnMsg"))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (sCardId != null) {

                                    } else {
                                        finish();
                                    }


                                }
                            }).show();
                }else
                { Intent Intenet_change = new Intent(uploadprescriptionActivity.this, ShippingAddress.class);
                    startActivity(Intenet_change);}
                //f_alert_ok("Success", response.getString("sReturnMsg"));
            }
            else
            {
                f_alert_ok("Information", response.getString("sReturnMsg"));
            }
        }
        catch(JSONException e){
            hidePDialog();
            Log.e("Error", e.getMessage());
            f_alert_ok("Error ", e.getMessage());
        }
    }

    private void post_uploaderror(VolleyError error) {
        String json = null;




    }




    @Override
    public void onDestroy() {
        if(mybitmap!=null) {

            mybitmap.recycle();
            mybitmap=null;

        }

        super.onDestroy();

    }

    @Override


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // compressimage_and_send(fileUri.getPath());
                compres_image(fileUri.getPath());


            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }



        if (requestCode == BROWSE_CODE && resultCode == Activity.RESULT_OK) {

            String[] all_path = data.getStringArrayExtra("all_path");

            Integer MyCount = 0;

            for (String CurrentFileName : all_path) {

                MyCount = MyCount + 1;


                CustomGallery item = new CustomGallery();
                if(imagePathArray.size()<=3) {
                    item.sdcardPath = CurrentFileName;
                    String filename = CurrentFileName.substring(CurrentFileName.lastIndexOf('/') + 1, CurrentFileName.length());
                    compres_image(CurrentFileName);
                    //compressimage_and_send(CurrentFileName);
                }else{
                    Toast.makeText(this, "you can select only 4 images", Toast.LENGTH_LONG).show();
                }



                //if (!filename.startsWith("A_P")) {

                /*} else {


                    imagePathArray.add(item);
                    viewSwitcher.setDisplayedChild(0);
                    obj_GalleryAdapter.addAll(imagePathArray);
                    obj_GalleryAdapter.notifyDataSetChanged();
                }
*/
            }


        }
    }

    private boolean storeImage(Bitmap imageData, String filename) {
        //get path to external storage (SD card)
        //String iconsStoragePath = Environment.getExternalStorageDirectory() + "/myAppDir/myImages/";

        String iconsStoragePath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/" +AppConfig.IMAGE_DIRECTORY_NAME ;
        File sdIconStorageDir = new File(iconsStoragePath);
        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();

        try {
            String filePath =  filename;
            FileOutputStream fileOutputStream = new FileOutputStream(sdIconStorageDir+"/"+filename);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            //choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }

        return true;
    }

    private void getcitylist() {

        showPdialog("loading...");

        String tag_string_req = "req_city";

        String tag_city_list = "json_city";

        String url = String.format(AppConfig.URL_GET_CITY, "", "0");

        RequestQueue queue = Volley.newRequestQueue(uploadprescriptionActivity.this);

        JsonObjectRequest cityrequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Register Response: " + response.toString());
                        hidePDialog();

                        Success_city(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Register Response: " + error.getMessage());
                        hidePDialog();

                        Error_city(error);
                    }
                });

        cityrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(cityrequest, tag_string_req);

        queue.add(cityrequest);
    }

    private void Success_city(JSONObject response) {
        try {
            //citylist.clear();
            JSONArray jsoncityarray;
            //ArrayList<citylist> currentcity;
            ArrayList<String> citylist;

            citylist = new ArrayList<String>();
            currentcity = new ArrayList<citylist>();

            // Default add
            citylist citydefault = new citylist();

            citydefault.setCityId(0);
            citydefault.setStateId(0);
            citydefault.setCityDesc("Select City");
            citydefault.setCityName("Select City");
            citydefault.setStateName("Select State");
            currentcity.add(citydefault);
            citylist.add("Select City");
            // Default add

            jsoncityarray = response.getJSONArray("cityModel");

            for (int i = 0; i < jsoncityarray.length(); i++) {
                JSONObject jsonobject = jsoncityarray.getJSONObject(i);

                citylist citydetails = new citylist();

                citydetails.setCityId(jsonobject.optInt("CityId"));
                citydetails.setStateId(jsonobject.optInt("StateId"));
                citydetails.setCityDesc(jsonobject.optString("CityDesc"));
                citydetails.setCityName(jsonobject.optString("CityName"));
                citydetails.setStateName(jsonobject.optString("StateName"));

                currentcity.add(citydetails);

                String vcityname = jsonobject.getString("CityName");
                citylist.add(vcityname);

            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(uploadprescriptionActivity.this,
                    android.R.layout.simple_spinner_item, citylist);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_cityname.setAdapter(dataAdapter);
            sp_cityname.setFocusableInTouchMode(true);


            hidePDialog();
            //mb.showAlertDialog(RegisterActivity.this, "done", "done", true);

        } catch (Exception e) {
            hidePDialog();
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private void Error_city(VolleyError error) {
        hidePDialog();
        // final messagebox mb = new messagebox();
        // mb.showAlertDialog(uploadprescriptionActivity.this, "error", "ErrorListener", true);
    }

    private void getdoctorlist(Integer p_pos) {

        showPdialog("loading...");

        String tag_string_req = "req_doctor";

        //Integer nCityId = currentcity.get(p_pos).getCityId(); //nCityId.toString(),

        String url = String.format(AppConfig.URL_GET_LOCALDOCTORLIST, "0", sMemberId);

        new getDoctorListAsyncHttpTask().execute(url);

      /*  RequestQueue queue = Volley.newRequestQueue(uploadprescriptionActivity.this);

        JsonArrayRequest doctorrequest = new JsonArrayRequest(url,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hidePDialog();

                Success_doctor(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Register Response: " + error.getMessage());
                        hidePDialog();

                        Error_city(error);
                    }
                });

        doctorrequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(doctorrequest, tag_string_req);

        queue.add(doctorrequest);*/


    }

    private void Success_doctor(JSONArray response) {
        try {
            JSONArray jsondoctorarray;

            doctorname.clear();
            doctorlists.clear();

            // Default add Doctor
            M_doctorlist doctordefault = new M_doctorlist();

            doctordefault.setCityId(0);
            doctordefault.setMemberid(0);
            doctordefault.setDoctorid("0");
            doctordefault.setDoctorname("Select Doctor");

            doctorlists.add(doctordefault);
            doctorname.add("Select Doctor");
            // Default add
            String vdoctorname="";

            // jsondoctorarray = response.getJSONArray("cityModel");
            jsondoctorarray = response;
            ObjectItemDoctor = new ObjectItem[ jsondoctorarray.length()];
            for (int i = 0; i < jsondoctorarray.length(); i++) {
                JSONObject jsonobject = jsondoctorarray.getJSONObject(i);

                M_doctorlist Odoctorlist = new M_doctorlist();

                Odoctorlist.setCityId(jsonobject.optInt("DistrictId"));
                Odoctorlist.setMemberid(jsonobject.optInt("MemberId"));
                Odoctorlist.setDoctorid(jsonobject.optString("ID"));
                Odoctorlist.setDoctorname(jsonobject.optString("DoctorName"));

                String docId=jsonobject.optString("ID");

                doctorlists.add(Odoctorlist);
                vdoctorname = jsonobject.getString("DoctorName");
                doctorname.add(vdoctorname);



                ObjectItemDoctor[i] = new ObjectItem(Integer.parseInt(docId),vdoctorname);

            }

            dataDoctorAdapter = new ArrayAdapter<String>(uploadprescriptionActivity.this,
                    android.R.layout.simple_spinner_item, doctorname);
            dataDoctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // sp_doctor.setAdapter(dataDoctorAdapter);
            // sp_doctor.setFocusableInTouchMode(true);
            // sp_doctor.setSelection(dataDoctorAdapter.getPosition(vdoctorname));

            hidePDialog();
            //mb.showAlertDialog(RegisterActivity.this, "done", "done", true);


        } catch (Exception e) {
            hidePDialog();
            Log.e("Error", e.getMessage());
            //mb.showAlertDialog(uploadprescriptionActivity.this, "Error", e.getMessage(), true);
            e.printStackTrace();
        }

    }
    private void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri() {

        sradomstringCamera = UUID.randomUUID().toString();
        if(serial==0)
        {
            picname1=sradomstringCamera +"_"+"1"+ "_"+".jpeg";
        }
        else if(serial==1)
        {
            picname2=sradomstringCamera +"_"+"2"+ "_"+".jpeg";
        }
        else if(serial==2)
        {
            picname3=sradomstringCamera +"_"+"3"+ "_"+".jpeg";
        }
        else if(serial==3)
        {
            picname4=sradomstringCamera +"_"+"4"+ "_"+".jpeg";
        }

        return Uri.fromFile(getOutputMediaFile(sMemberId,serial,sradomstringCamera));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(String sMemberId,Integer serial,String sradomstringCamera) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AppConfig.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "+ AppConfig.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name


        File mediaFile;
        String sFilename, sDirectory = "";

        //sFilename = getfilename();
        String sradomstring = UUID.randomUUID().toString();
        Integer count=serial+1;

        //mediaFile = new File(mediaStorageDir.getPath() + File.separator + "mem_" + sMemberId +"_"+serial+ ".jpg");
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +  sradomstringCamera +"_"+count+ "_"+ ".jpeg");

        //mediaFile = new File(mediaStorageDir.getPath() + sFilename);
        return mediaFile;
    }

    private static String getfilename(){
        String sfilename = "";

        if ( IMEINumber_1.toString().length() <= 0)
        {
            IMEINumber_1 =  IMEINumber+".jpg";
            sfilename = IMEINumber_1 ;
            return sfilename;
        }

        if ( IMEINumber_2.toString().length() <= 0)
        {
            IMEINumber_2 = IMEINumber+".jpg";
            sfilename = IMEINumber_2 ;
            return sfilename;
        }

        if ( IMEINumber_3.toString().length() <= 0)
        {
            IMEINumber_3 = IMEINumber+".jpg";
            sfilename = IMEINumber_3 ;
            return sfilename;
        }

        if ( IMEINumber_4.toString().length() <= 0)
        {
            IMEINumber_4 = IMEINumber+".jpg";
            sfilename = IMEINumber_4 ;
            return sfilename;
        }
        return sfilename;
    }

    private void clear_images(){
        IMEINumber_1 = "";
        IMEINumber_2 = "";
        IMEINumber_3 = "";
        IMEINumber_4 = "";
    }

    public void deletePhotos()
    {
        String folder=Environment.getExternalStorageDirectory() +AppConfig.IMAGE_DIRECTORY_NAME;
        File f=new File(folder);
        if(f.isDirectory())
        {
            File[] files=f.listFiles();
            Log.v("Load Image", "Total Files To Delete=====>>>>>"+files.length);
            for(int i=0;i<files.length;i++)
            {
                String fpath=folder+File.separator+files[i].getName().toString().trim();
                System.out.println("File Full Path======>>>"+fpath);
                File nf=new File(fpath);
                if(nf.exists())
                {
                    nf.delete();
                }
            }
        }
    }





    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(uploadprescriptionActivity.this)
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
        Intent intent_buymainactivity = getIntent();
        //sMemberId = intent_buymainactivity.getStringExtra("memberid");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        sCardId=intent_buymainactivity.getStringExtra("Cart_id");
        if(sCardId!=null)
        {
            top_flow_relative.setVisibility(View.VISIBLE);
            bottom_relative.setVisibility(View.VISIBLE);
            btnsubmit.setVisibility(View.GONE);
            btncancel.setVisibility(View.GONE);
        }
    }



    public  void FamilyMember_search()
    {

        Integer pos;
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);
        final EditText inputSearch = (EditText) dialogview.findViewById(R.id.inputSearch);
        final LinearLayout AddMemberShowHide = (LinearLayout) dialogview.findViewById(R.id.AddMemberShowHide);
        builder.setView(dialogview);
        // Adding items to listview
        final Button btnmem = (Button) dialogview.findViewById(R.id.btnmem);

        AddMemberShowHide.setVisibility(View.VISIBLE);
        FamilyMemberadapter = new AD_adapterCombo(this, R.layout.list_item,ObjectItemData);


        // create a new ListView, set the adapter and item click listener

        //ListView listViewItems = new ListView(this);

        lv.setAdapter(FamilyMemberadapter);



        final TextView Titile =(TextView)dialogview.findViewById(R.id.textView);
        Titile.setText("Select Family Member");
        inputSearch.setHint("Enter Family Member");

        final TextView btnFamilyMembercancel=(TextView)dialogview.findViewById(R.id.btncancel);

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

            //lv.notifyDataSetChanged();
        });


        btnmem.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent Intenet_mr = new Intent(uploadprescriptionActivity.this, AddMember.class);
                Intenet_mr.putExtra("Mode", "A");
                startActivity(Intenet_mr);
                dlg.dismiss();
            }
        });


        btnFamilyMembercancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dlg.cancel();
            }
        });



        dlg.show();
        // dlg.getWindow().setLayout(500, 500);

    }
    public class OnItemClickListenerListViewItem implements AdapterView.OnItemClickListener {
        Dialog d=null;
        OnItemClickListenerListViewItem(Dialog dlg)
        {
            d=dlg;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView textViewItem = ((TextView) view.findViewById(R.id.product_name));
            String listItemText = textViewItem.getText().toString();
            String listItemId = textViewItem.getTag().toString();
            gender=Currentmember_list.get(position).getMemberGender();
            PatientId=Integer.parseInt(Currentmember_list.get(position).getPatientId());

            if(gender==null) {

                gender="M";
            }

            if(gender.equals("M")) {

                rdbMale.setChecked(true);
            }
            else if(gender.equals("F")) {

                rdbFemale.setChecked(true);
            }
            else if(gender.equals("O")) {

                rdbother.setChecked(true);
            }

            //Toast.makeText(context, "Item: " + listItemText + ", Item ID: " + listItemId, Toast.LENGTH_SHORT).show();
            selected_MemberFamilyid = -99;
            selected_MemberFamilyName = "";


            selected_MemberFamilyid =  Integer.parseInt(listItemId);
            selected_MemberFamilyName = listItemText;
            //selected_stateid =  Integer.parseInt(listItemId);

            txtFamilyname .setText(listItemText);
            input_name.setText(listItemText);
            d.cancel();

        }
    }


    private  void image_zoom(String imgpath)
    {
        Intent Intenet_imageZoom = new Intent(uploadprescriptionActivity.this, image_zoom_view.class);
        //Intenet_imageZoom.putExtra("imagepath", imgpath);
        globalVariable.setImagePathForZoom(imgpath);
        startActivity(Intenet_imageZoom);
    }
    private void Patient_for()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);
        builder.setView(dialogview);
        // Adding items to listview



        patientforAdapter = new AD_adapterCombo(this, R.layout.list_item,ObjectItemPrescriptionFor);
        lv.setAdapter(patientforAdapter);

        final TextView Titile =(TextView)dialogview.findViewById(R.id.textView);
        Titile.setText("Select Prescription For");
        final TextView btncancel=(TextView)dialogview.findViewById(R.id.btncancel);
        final Dialog dlg= builder.create();
        lv.setOnItemClickListener(new OnItemClickListenerListViewItempatientfor(dlg));






        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dlg.cancel();
            }
        });

        dlg.show();
    }


    public class OnItemClickListenerListViewItempatientfor implements AdapterView.OnItemClickListener {
        Dialog d=null;
        OnItemClickListenerListViewItempatientfor(Dialog dlg)
        {
            d=dlg;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Context context = view.getContext();


            TextView textViewItem = ((TextView) view.findViewById(R.id.product_name));
            String listItemText = textViewItem.getText().toString();
            String listItemId = textViewItem.getTag().toString();
            // timeslot = Integer.parseInt(listItemId);
            input_sp_prescription.setText(listItemText);
            if(listItemText.equals("Self")) {
                iPrescriptionFor = 0;
                sPrescriptionFor = "S";
                txtFamilyname.setClickable(true);
                txtFamilyname.setFocusableInTouchMode(false);
                DefualtPatientName();


            }else if(listItemText.equals("Relative")) {

                iPrescriptionFor = 1;
                sPrescriptionFor="R";
                txtFamilyname.setText("");

                input_name.setEnabled(true);
                txtFamilyname.setEnabled(true);
                txtFamilyname.setFocusable(true);
                txtFamilyname.setClickable(true);
                txtFamilyname.setFocusableInTouchMode(false);


            }else if(listItemText.equals("Others")) {

                iPrescriptionFor = 2;
                sPrescriptionFor = "O";
                txtFamilyname.setText("");
                input_name.setText("");
                input_name.setEnabled(true);
                txtFamilyname.setEnabled(true);
                txtFamilyname.setFocusable(true);
                txtFamilyname.setFocusableInTouchMode(true);
                txtFamilyname.setClickable(false);

                DefualtOthers();
            }


            d.cancel();

        }
    }
    private void best_time_for_call()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);
        final EditText inputSearch = (EditText) dialogview.findViewById(R.id.inputSearch);
        builder.setView(dialogview);
        // Adding items to listview


        besttimeadapter = new AD_adapterCombo(this, R.layout.list_item,ObjectItemBestTime);
        lv.setAdapter(besttimeadapter);

        final TextView Titile =(TextView)dialogview.findViewById(R.id.textView);
        Titile.setText("Select Best Time to Call");
        inputSearch.setHint("Enter Relation");
        final TextView btncancel=(TextView)dialogview.findViewById(R.id.btncancel);
        final Dialog dlg= builder.create();
        lv.setOnItemClickListener(new OnItemClickListenerListViewItemBestTime(dlg));

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
    public class OnItemClickListenerListViewItemBestTime implements AdapterView.OnItemClickListener {
        Dialog d=null;
        OnItemClickListenerListViewItemBestTime(Dialog dlg)
        {
            d=dlg;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Context context = view.getContext();


            TextView textViewItem = ((TextView) view.findViewById(R.id.product_name));
            String listItemText = textViewItem.getText().toString();
            String listItemId = textViewItem.getTag().toString();
            timeslot = Integer.parseInt(listItemId);
            input_timeslot.setText(listItemText);


            d.cancel();

        }
    }

    private void objectItemBestTimeLoad()
    {

        ObjectItemBestTime = new ObjectItem[5];

        ObjectItemBestTime[0] = new ObjectItem(12,"08 AM-12 NOON");
        ObjectItemBestTime[1] = new ObjectItem(13,"12 PM-03 PM");
        ObjectItemBestTime[2] = new ObjectItem(14,"03 PM-06 PM");
        ObjectItemBestTime[3] = new ObjectItem(15,"06 PM-08 PM");
        ObjectItemBestTime[4] = new ObjectItem(17,"ASAP");
    }
    private void objectItemPrescriptionForLoad()
    {

        ObjectItemPrescriptionFor = new ObjectItem[3];

        ObjectItemPrescriptionFor[0] = new ObjectItem(1,"Self");
        ObjectItemPrescriptionFor[1] = new ObjectItem(2,"Relative");
        ObjectItemPrescriptionFor[2] = new ObjectItem(3,"Others");

    }
    private void add_doctor_combo_box()
    {


        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);
        final EditText inputSearch = (EditText) dialogview.findViewById(R.id.inputSearch);
        final ImageView heading_icon = (ImageView) dialogview.findViewById(R.id.heading_icon);


        final LinearLayout AddDoctorShow = (LinearLayout) dialogview.findViewById(R.id.addDoctorlnr);
        final LinearLayout AddDoctorShowHide = (LinearLayout) dialogview.findViewById(R.id.AddDoctorShowHide);
        final LinearLayout DoctorShowFirst = (LinearLayout) dialogview.findViewById(R.id.DoctorShowFirst);
        final LinearLayout DoctorShowSecond = (LinearLayout) dialogview.findViewById(R.id.DoctorShowSecond);
        AddDoctorShowHide.setVisibility(dialogview.VISIBLE);
        builder.setView(dialogview);
        // Adding items to listview
        final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
        final ImageView btnDoctorcancel = (ImageView) dialogview.findViewById(R.id.btnDoctorcancel);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);

        doctorAdapter = new AD_adapterCombo(this, R.layout.list_item,ObjectItemDoctor);
        lv.setAdapter(doctorAdapter);
        AddDoctorShowVisible=false;
        final TextView Titile =(TextView)dialogview.findViewById(R.id.textView);
        final TextView   textViewExp =(TextView)dialogview.findViewById(R.id.  textViewExp);
        final Button   btnExp =(Button)dialogview.findViewById(R.id.btnExp);
        Titile.setText("Select doctor");
        heading_icon.setImageResource(R.drawable.add_doctor_white);
        inputSearch.setHint("Enter Relation");
        final TextView btncancel=(TextView)dialogview.findViewById(R.id.btncancel);
        final Dialog dlg= builder.create();
        lv.setOnItemClickListener(new OnItemClickListenerListViewDoctor(dlg));

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
                AddDoctorShowVisible=false;
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
                } /*else {
                    AddDoctorShowVisible = false;
                    DoctorShowFirst.setVisibility(view.VISIBLE);
                    DoctorShowSecond.setVisibility(view.VISIBLE);
                    AddDoctorShow.setVisibility(view.GONE);

                    textViewExp.setText("Doctor Not in List ? +");

                }*/
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



        txtdoctorname = (EditText) dialogview.findViewById(R.id.doctorname);
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

        clinicname = (EditText) dialogview.findViewById(R.id.clinicname);

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(!txtdoctorname.getText().toString().isEmpty() &&!clinicname.getText().toString().isEmpty()) {
                    post_new_doctor(txtdoctorname.getText().toString(), clinicname.getText().toString());
                    dlg.dismiss();
                }
                else
                {
                    f_alert_ok("Add Doctor","Please Enter doctor name or clinic name");
                }
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        btnDoctorcancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        dlg.show();
    }
    public class OnItemClickListenerListViewDoctor implements AdapterView.OnItemClickListener {
        Dialog d=null;
        OnItemClickListenerListViewDoctor(Dialog dlg)
        {
            d=dlg;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Context context = view.getContext();


            TextView textViewItem = ((TextView) view.findViewById(R.id.product_name));
            String listItemText = textViewItem.getText().toString();
            String listItemId = textViewItem.getTag().toString();
            sDoctId = Integer.parseInt(listItemId);
            sp_doctor.setText(listItemText);


            d.cancel();

        }
    }


    private  void  fp_Clear()
    {
        iPrescriptionFor = 0;
        sPrescriptionFor = "S";
        image1=null;
        image2=null;
        image3=null;
        image4=null;
        picname1="";
        picname2="";
        picname3="";
        picname4 = "";
        input_sp_prescription.setText("");
        txtFamilyname.setText("");
        input_message.setText("");
        sDoctId= 0;
        getFamilymember_list(1);
        txtFamilyname.setClickable(true);
        txtFamilyname.setFocusableInTouchMode(false);
        DefualtPatientName();
        sp_doctor.setText("");
        timeslot = 17;
        input_timeslot.setText("");
        objectItemBestTimeLoad();
        objectItemPrescriptionForLoad();
        getdoctorlist(0);


//        getGender="F";
        if(sSelfGender.equals("M")) {
            rdbMale.setChecked(true);
        }
        else if(sSelfGender.equals("F")) {
            rdbFemale.setChecked(true);
        }
        else if(sSelfGender.equals("O")) {
            rdbother.setChecked(true);
        }

//        Intent Intenet_Prescription = new Intent(uploadprescriptionActivity.this, LoginActivity.class);
//        startActivity(Intenet_Prescription);
    }
    private  void compres_image(String Imgpath)
    {
        TakeImage = Imgpath;
        byte[] ImagBytes =  ImageUtils.compressImage(Imgpath);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);

        String sradomstring = "";
        sradomstring = "A_P_" + UUID.randomUUID().toString();
        //CompressedImage = Environment.getExternalStorageDirectory().toString()+"/A_P_"+filename;//TakeImage ;
        CompressedImage = mediaStorageDir.getPath() + File.separator + sradomstring + ".jpeg";

        File obj_compressfile = new File(CompressedImage);
        //File obj_compressfile = new File(CompressedImage);
        try {



            FileOutputStream fos = new FileOutputStream(obj_compressfile);
            fos.write(ImagBytes);
            fos.flush();
            fos.close();


            CustomGallery item = new CustomGallery();
            item.sdcardPath = CompressedImage;

            imagePathArray.add(item);
            //String imgeName = CompressedImage.substring(CompressedImage.lastIndexOf('/') + 1, CompressedImage.length());


            try {
                String encodedImage = Base64.encodeToString(ImagBytes, Base64.DEFAULT);
                JSONObject imgobj = new JSONObject();
                // Toast.makeText(getBaseContext(), String.valueOf(imgarr.length()),Toast.LENGTH_SHORT).show();
                if(imgarr.length()<=3) {
                    imgobj.put("ImageName", sradomstring+".jpeg");
                    imgobj.put("ImageStream", encodedImage);
                    imgarr.put(imgobj);
                }
                //imgobj = null ;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }



            viewSwitcher.setDisplayedChild(0);
            obj_GalleryAdapter.addAll(imagePathArray);
            viewSwitcher.invalidate();
            gridGallery.invalidate();


           /* if(txtprescriptionName.getText().toString().isEmpty()) {
                show_keyboard();
            }*/




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

    }
    private void init() {


        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);

        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {

                imagepathforzoom=imagePathArray.get(position).sdcardPath;
                image_zoom(imagepathforzoom);


            }

        });


        obj_GalleryAdapter = new GalleryAdapter(getApplicationContext(), imageLoader,"V",this);
        obj_GalleryAdapter.setMultiplePick(false);
        gridGallery.setAdapter(obj_GalleryAdapter);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);


        // btnGalleryPickMul = (Button) findViewById(R.id.btnGalleryPickMul);


        btnbrowse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (imagePathArray.size() <= 3) {
                    Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                    startActivityForResult(i, 200);
                } else {
                    Toast.makeText(getApplicationContext(), "you can select only four images", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public void clear_array(int x) {
        //dataT.
        try {
            imagePathArray.remove(x);
            //viewSwitcher.setDisplayedChild(0);
            obj_GalleryAdapter.addAll(imagePathArray);
            imgarr.remove(x);
            obj_GalleryAdapter.notifyDataSetChanged();
        }catch (Exception e){

        }
    }


    private void delete_folder()
    {
        //File dir = new File(Environment.getExternalStorageDirectory()+"Dir_name_here");
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
    public class getDoctorListAsyncHttpTask extends AsyncTask<String, Void, Integer> {

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

                    JSONArray x = new JSONArray(response.toString());


                    Success_doctor(x);

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


            } else {


            }
        }
    }

    public class MemberfamilyAsyncHttpTask extends AsyncTask<String, Void, Integer> {

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


            } else {
                // Log.e(TAG, "Failed to fetch data!");
            }
        }
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
                    hideSoftKeyboard(uploadprescriptionActivity.this);
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
    private void show_keyboard()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    private  void product_test()
    {
        showPdialog("loading....");
        String url =  "http://198.50.198.184/MedikartApi/Api/Product/ProductDetail?iProductId=98913&iMemberId=11";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);
    }
    void request_camera_permission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(), "You need to give permission for camera to upload a prescription!", Toast.LENGTH_LONG)
                    .show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_ACCESS_CAMERA);


        }else
        {

        }
    }

    void request_gallery_permission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(), "You need to give permission for Gallery to upload a prescription!", Toast.LENGTH_LONG)
                    .show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


        }else
        {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode==PERMISSION_ALL){
            if (grantResults.length > 0)
            {
                for(int i=0;i<grantResults.length;i++)
                {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(getApplicationContext(), "RxMedikart need  permissions for uploading a prescription!", Toast.LENGTH_LONG)
                                .show();
                        finish();
                    }
                }
            }

        }

    }





    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}


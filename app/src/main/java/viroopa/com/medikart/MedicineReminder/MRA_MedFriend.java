package viroopa.com.medikart.MedicineReminder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.AddProfilePic;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SqliteMRHandler;


public class MRA_MedFriend extends AppCompatActivity {

    // private ImageButton btnbp;
    // private ImageView btnbuy, btnbp, btnmr, btnwm, btndm;
    private String sMemberId;
    JSONArray medfriend_array;
    private TextView contact;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private String random_number;
    private Uri uriContact;
    private EditText edt_name,edt_email;
    private String contactID;
    private Button btnsend,btngenerate;
    private RelativeLayout relativeLayout8;
    private String contactNumber = null,emailIdOfContact="";
    private String contactName = null;
    private String email_id;
    private Integer getSelectedRelationshipId;
    private TextView msgtxt;
    private  Boolean ImageClick=false;
    private SqliteMRHandler db_MedRem;
    private ImageView profile_pic;
    private String ProfilePicName="avtar1",genderForEdit;
    private Integer ProfilePicFlag=0;
    private String ImageStream,ImageName;
    AppController globalVariable;
    private  EditText edt_phone_number;
    private ProgressDialog pDialog;
    private CircularImageView img_profile;
    private LinearLayout generate_serial_number;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.your_invite_code);
        pDialog = new ProgressDialog(this);
        contact =(TextView)findViewById(R.id.contact);
        edt_name=(EditText)findViewById(R.id.edt_name);
        btnsend=(Button)findViewById(R.id.btnsave);
        btngenerate=(Button)findViewById(R.id.btngenerate);
        relativeLayout8=(RelativeLayout)findViewById(R.id.relativeLayout8);
        edt_phone_number=(EditText)findViewById(R.id.edt_phone_number);
        edt_email=(EditText)findViewById(R.id.edt_email);
        msgtxt=(TextView)findViewById(R.id.textView52);
        globalVariable = (AppController)getApplicationContext();
        db_MedRem=new SqliteMRHandler(this);
         img_profile=(CircularImageView)findViewById(R.id.img_profile);
        generate_serial_number=(LinearLayout)findViewById(R.id.generate_serial_number);

        f_random();
        getIntenet();
        initImageLoader();
        msgtxt.setText("I want you to be notified if i forgot to take my medicines.Get Medikart App using this link http://goog.gl/medi23jk. Your invite code is " + random_number);
        contact.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

            }


        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                ImageClick=true;
                Show_Change_Profile_Picture();
            }
        });

        btngenerate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (!edt_name.getText().toString().isEmpty()) {
                    if (edt_phone_number.length()>=10) {
                        if (!edt_email.getText().toString().isEmpty() && ConstData.validate(edt_email.getText().toString())) {

                            post_save_pillbuddy_code(edt_name.getText().toString(),edt_email.getText().toString(),edt_phone_number.getText().toString(),
                                    random_number);



                        } else {
                            Toast.makeText(MRA_MedFriend.this, "Please Enter Proper Email ID", Toast.LENGTH_LONG).show();
                        }
                    }else
                    {
                        Toast.makeText(MRA_MedFriend.this, "Please Enter Proper Phone number", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(MRA_MedFriend.this, "Please enter Friend Name", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                email_id=edt_email.getText().toString();


                if (!edt_name.getText().toString().isEmpty()) {


                        if (!edt_email.getText().toString().isEmpty()) {
                            SaveData();
                            SEND_INVITE(email_id,"123144");

                            Intent Intenet_buy = new Intent(MRA_MedFriend.this, MRA_ShareInvitation.class);
                            Intenet_buy.putExtra("contact_name", edt_name.getText().toString());
                            Intenet_buy.putExtra("contact_nr", edt_phone_number.getText().toString());
                            Intenet_buy.putExtra("email_id", email_id);
                            Intenet_buy.putExtra("random_nr", random_number);
                            startActivity(Intenet_buy);


                            finish();
                        } else {
                            Toast.makeText(MRA_MedFriend.this, "Please Enter Proper Email ID", Toast.LENGTH_LONG).show();
                        }

                } else {
                    Toast.makeText(MRA_MedFriend.this, "Please enter Friend Name", Toast.LENGTH_LONG).show();
                }

              //  db_MedRem.addMedFriend(0,name.getText().toString(),contact.getText().toString(),emailtxt.getText().toString(),"",random_number,true);
            }


        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            // Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            //retrieveContactPhoto();

        }
    }
    private void getIntenet()
    {
        SharedPreferences pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        //Intent intent_buymainactivity = getIntent();
        sMemberId = pref.getString("memberid", "");

        getSelectedRelationshipId=pref.getInt("RelationshipId",0);

        if(globalVariable.getRealationshipId()!=null) {
            getSelectedRelationshipId = Integer.parseInt(globalVariable.getRealationshipId());
        }
        else
        {
            getSelectedRelationshipId=8;
        }



    }
    private void SaveData()
    {
  boolean MemberExist=false;
        try {


            String SUUID = UUID.randomUUID().toString();

            Cursor cursor_all_pillbuddy = db_MedRem.get_all_pill_buddyr(sMemberId);


            if ((cursor_all_pillbuddy != null) || (cursor_all_pillbuddy.getCount() > 0)) {
                if (cursor_all_pillbuddy.moveToFirst()) {
                    do {

                        if ((cursor_all_pillbuddy.getString(cursor_all_pillbuddy.getColumnIndex("reminder_phone_no")).equals(edt_phone_number.getText().toString())
                                || (cursor_all_pillbuddy.getString(cursor_all_pillbuddy.getColumnIndex("reminder_email_id"))).equals(edt_email.getText().toString()))) {
                            MemberExist = true;
                        }

                    } while (cursor_all_pillbuddy.moveToNext());
                    cursor_all_pillbuddy.close();


                }
            }
            if (!MemberExist) {
                db_MedRem.addMedFriend("-99", edt_name.getText().toString(), edt_phone_number.getText().toString(), edt_email.getText().toString(), ProfilePicName, random_number, true);
                Toast.makeText(getApplicationContext(), "Pill Buddy Added Successfully", Toast.LENGTH_SHORT)
                        .show();
            }else {
                Toast.makeText(getApplicationContext(), "Request was already sent to this friend", Toast.LENGTH_SHORT)
                        .show();
            }
        }catch (Exception e)
        {}
    }
    private void retrieveContactName() {



        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            edt_name.setText(contactName);
        }

        cursor.close();

        //Log.d(TAG, "Contact Name: " + contactName);

    }


    private void retrieveContactNumber() {



        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        //Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber indianNumberProto = phoneUtil.parse(contactNumber, "IN");
                contactNumber=String.valueOf(indianNumberProto.getNationalNumber());
            } catch (NumberParseException e)  {
                System.err.println("NumberParseException was thrown: " + e.toString());
            }

        }

        cursorPhone.close();

        Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactID, null, null);
        while (emails.moveToNext()) {
             emailIdOfContact = emails.getString(emails
                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

        }
        emails.close();

        edt_phone_number.setText(contactNumber);
        edt_email.setText(emailIdOfContact);

    }

    public void f_random()
    {
        String sradomstring = UUID.randomUUID().toString();
        String[] separated = sradomstring.split("-");
        //srandom.setText(separated[0].trim());
        random_number =separated[0].trim();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void Show_Change_Profile_Picture() {
        Intent Intenet_add_profile_pic = new Intent(this, AddProfilePic.class);
        ImageClick=true;
        startActivity(Intenet_add_profile_pic);

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
                img_profile.setImageBitmap(null);
                img_profile.setImageDrawable(null);
                img_profile.setBackgroundDrawable(null);



                if (ProfilePicFlag.equals(0)) {
                    if (ProfilePicName.equals("avtar1")) {
                        String imageUri = "drawable://" + R.drawable.avtar1;
                        // mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar1);

                    }
                    if (ProfilePicName.equals("avtar2")) {
                        String imageUri = "drawable://" + R.drawable.avtar2;
                        // mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar2);

                    }
                    if (ProfilePicName.equals("avtar3")) {
                        String imageUri = "drawable://" + R.drawable.avtar3;
                        // mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar3);

                    }
                    if (ProfilePicName.equals("avtar4")) {
                        String imageUri = "drawable://" + R.drawable.avtar4;
                        // mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar4);

                    }
                    if (ProfilePicName.equals("avtar5")) {
                        String imageUri = "drawable://" + R.drawable.avtar5;
                        // mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar5);

                    }
                    if (ProfilePicName.equals("avtar6")) {
                        String imageUri = "drawable://" + R.drawable.avtar6;
                        // mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar6);

                    }
                    if (ProfilePicName.equals("avtar7")) {
                        String imageUri = "drawable://" + R.drawable.avtar7;
                        // mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar7);

                    }
                    if (ProfilePicName.equals("avtar8")) {
                        String imageUri = "drawable://" + R.drawable.avtar8;
                        // mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar8);

                    }
                    if (ProfilePicName.equals("avtar9")) {
                        String imageUri = "drawable://" + R.drawable.avtar9;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar9);

                    }
                    if (ProfilePicName.equals("avtar10")) {
                        String imageUri = "drawable://" + R.drawable.avtar10;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar10);

                    }
                    if (ProfilePicName.equals("avtar11")) {
                        String imageUri = "drawable://" + R.drawable.avtar11;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar11);

                    }
                    if (ProfilePicName.equals("avtar12")) {
                        String imageUri = "drawable://" + R.drawable.avtar12;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar12);

                    }
                    if (ProfilePicName.equals("avtar13")) {
                        String imageUri = "drawable://" + R.drawable.avtar13;
                        // mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar13);

                    }
                    if (ProfilePicName.equals("avtar14")) {
                        String imageUri = "drawable://" + R.drawable.avtar14;
                        // mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar14);

                    }
                    if (ProfilePicName.equals("avtar15")) {
                        String imageUri = "drawable://" + R.drawable.avtar15;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar15);

                    }
                    if (ProfilePicName.equals("avtar16")) {
                        String imageUri = "drawable://" + R.drawable.avtar16;
                        //mybitmap = BitmapFactory.decodeFile(imageUri);
                        img_profile.setImageResource(R.drawable.avtar16);

                    }

                }

                if (ProfilePicFlag.equals(2)) {
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
                    //mybitmap = BitmapFactory.decodeFile(mediaStorageDir.getPath() + "/" + ProfilePicName);
                    File pathfile = new File(mediaStorageDir.getPath() + "/" + ProfilePicName);
                    imageLoader.displayImage("file://" + pathfile.getPath(), img_profile);
                    // profile_pic.setImageBitmap(mybitmap);


                }
                if (ProfilePicFlag.equals(1)) {


                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
                    File pathfile = new File(mediaStorageDir.getPath() + "/" + ProfilePicName);
                    imageLoader.displayImage("file://" + pathfile.getPath(), img_profile);

                }


            }

        }
        globalVariable.setMemberImage(null);
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

    private void SEND_INVITE(String mail,String phone) {

        String url="";
        try {
             url = String.format(AppConfig.URL_GET_SEND_INVITE, mail, phone,sMemberId);
        }catch (Exception e)
        {
            e.toString();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest staterequest = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {


                        }
                        catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //hidePDialog();
                    }
                });


        queue.add(staterequest);
    }

    private  void post_save_pillbuddy_code(String friend_name,String email_id,String Mobilenumber,String invitation_code) {

        Map<String, Object> params = new HashMap<String, Object>();
        showPdialog("Generating code...");

        params.put("MemberId", sMemberId);
        params.put("FriendId","0");
        params.put("FriendName", friend_name);
        params.put("FriendMobileNo", Mobilenumber);
        params.put("FriendEmailId", email_id);
        params.put("InvitationCode", invitation_code);

        JSONObject jparams = new JSONObject(params);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jor_inhurry_post = new JsonObjectRequest(
                Request.Method.POST,
                AppConfig.URL_POST_INVITE_CODE_PILLBUDDY,

                jparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hidePDialog();
                        relativeLayout8.setVisibility(View.VISIBLE);
                        btnsend.setVisibility(View.VISIBLE);
                        btngenerate.setVisibility(View.GONE);
                        contact.setVisibility(View.GONE);
                        btnsend.setEnabled(true);
                        generate_serial_number.setVisibility(View.GONE);
                        edt_name.setEnabled(false);
                        edt_phone_number.setEnabled(false);
                        edt_email.setEnabled(false);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Toast.makeText(getApplicationContext(), "Unable to contact the server ..please check your internet connection", Toast.LENGTH_LONG)
                                .show();
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

}
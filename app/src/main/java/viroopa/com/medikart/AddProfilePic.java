package viroopa.com.medikart;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.util.ImageUtils;


public class AddProfilePic extends AppCompatActivity {

    private static final String TAG = AddProfilePic.class.getSimpleName();

    private Button btn_cancel;
    private String sMemberId;
    private String ProfilePicName;
    private ImageView iv_avatar,iv_camera,iv_browse;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private LinearLayout ll_image;
    private Uri fileUri;
    private Bitmap bm;
    Integer x=1;
    private Integer serial = 0;
    private ProgressDialog pDialog;
    Integer count = 0;
    private String image;
    private String picname1 = "";
    private   String ProfilePicFlagFrom ="";
    private  String actualFileName;
    private  String ImageFileName;
    private Uri uriSavedImage;
    private Boolean lSync=false;
    private Integer ProfilePicFlag=0;
    private String editMemeberMode,editMemberId;
    AppController globalVariable;

    String TakeImage,CompressedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile_pic);
        globalVariable = (AppController) getApplicationContext();
        getIntenet();

        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        iv_camera = (ImageView) findViewById(R.id.iv_camera);
        iv_browse = (ImageView) findViewById(R.id.iv_browse);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        pDialog = new ProgressDialog(this);

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                globalVariable.setProfilePicFlag(0);
                ProfilePicFlag=0;
                if(ProfilePicFlagFrom.equals("AddMember"))
                {
                    set_setttings_MemberprofilePicFlagString("MemberProfilePicFlag", 0);
                }
                else {
                    //set_setttings_profilePicFlagString("ProfilePicFlag", 0);

                }

                f_avatar();
            }
        });

        iv_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                globalVariable.setProfilePicFlag(1);
                ProfilePicFlag=1;
                if(ProfilePicFlagFrom.equals("AddMember"))
                {
                    set_setttings_MemberprofilePicFlagString("MemberProfilePicFlag", 1);
                }
                else {
                    //set_setttings_profilePicFlagString("ProfilePicFlag", 1);

                }


                f_camera();
                //finish();
            }
        });

        iv_browse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                globalVariable.setProfilePicFlag(2);
                ProfilePicFlag=2;
                if(ProfilePicFlagFrom.equals("AddMember"))
                {
                    set_setttings_MemberprofilePicFlagString("MemberProfilePicFlag", 2);

                }
                else {
                    //set_setttings_profilePicFlagString("ProfilePicFlag", 2);

                }

                f_browse();
            }
        });

    }

    private void f_avatar()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.avatar, null);
        builder.setView(dialogview);
        final Dialog dialog = builder.create();

        ImageView iv_avtar1  = (ImageView)dialogview.findViewById(R.id.avtar1);
        ImageView iv_avtar2  = (ImageView)dialogview.findViewById(R.id.avtar2);
        ImageView iv_avtar3  = (ImageView)dialogview.findViewById(R.id.avtar3);
        ImageView iv_avtar4  = (ImageView)dialogview.findViewById(R.id.avtar4);
        ImageView iv_avtar5  = (ImageView)dialogview.findViewById(R.id.avtar5);
        ImageView iv_avtar6  = (ImageView)dialogview.findViewById(R.id.avtar6);
        ImageView iv_avtar7  = (ImageView)dialogview.findViewById(R.id.avtar7);
        ImageView iv_avtar8  = (ImageView)dialogview.findViewById(R.id.avtar8);
        ImageView iv_avtar9  = (ImageView)dialogview.findViewById(R.id.avtar9);
        ImageView iv_avtar10 = (ImageView)dialogview.findViewById(R.id.avtar10);
        ImageView iv_avtar11 = (ImageView)dialogview.findViewById(R.id.avtar11);
        ImageView iv_avtar12 = (ImageView)dialogview.findViewById(R.id.avtar12);
        ImageView iv_avtar13 = (ImageView)dialogview.findViewById(R.id.avtar13);
        ImageView iv_avtar14 = (ImageView)dialogview.findViewById(R.id.avtar14);
        ImageView iv_avtar15 = (ImageView)dialogview.findViewById(R.id.avtar15);
        ImageView iv_avtar16 = (ImageView)dialogview.findViewById(R.id.avtar16);

        View.OnTouchListener iv_click = new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        f_Touch_Down(v);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        ProfilePicName=v.getTag().toString();

                            globalVariable.setMemberImage(ProfilePicName);
                            finish();
                            break;
                        //{
                            /*if(editMemeberMode==null) {
                                Intent Intenet_add_profile_pic = new Intent(AddProfilePic.this, AddMember.class);
                                Intenet_add_profile_pic.putExtra("intent_pic_path", ProfilePicName);
                                Intenet_add_profile_pic.putExtra("ProfilePicFlag", ProfilePicFlag);
                                Intenet_add_profile_pic.putExtra("AddpicFlag", true);
                                startActivity(Intenet_add_profile_pic);

                            }else{

                                Intent Intenet_add_profile_pic = new Intent(AddProfilePic.this, AddMember.class);
                                Intenet_add_profile_pic.putExtra("intent_pic_path", ProfilePicName);
                                Intenet_add_profile_pic.putExtra("Mode","E");
                                Intenet_add_profile_pic.putExtra("memberIdforEdit",editMemberId);
                                Intenet_add_profile_pic.putExtra("ProfilePicFlag", ProfilePicFlag);
                                Intenet_add_profile_pic.putExtra("AddpicFlag", true);
                                startActivity(Intenet_add_profile_pic);

                            }




                        }
                        else {

                            set_setttings_ProfilePicString("ProfilePicName", ProfilePicName);
                            Intent Intenet_add_profile_pic = new Intent(AddProfilePic.this, EditProfile.class);
                            Intenet_add_profile_pic.putExtra("intent_pic_path", ProfilePicName);

                            startActivity(Intenet_add_profile_pic);
                            finish();
                        }*/

                    }
                    case MotionEvent.ACTION_CANCEL: {
                        f_Touch_Cancel(v);
                        break;
                    }
                }
                return true;
            }
        };

        iv_avtar1.setOnTouchListener(iv_click);
        iv_avtar2.setOnTouchListener(iv_click);
        iv_avtar3.setOnTouchListener(iv_click);
        iv_avtar4.setOnTouchListener(iv_click);
        iv_avtar5.setOnTouchListener(iv_click);
        iv_avtar6.setOnTouchListener(iv_click);
        iv_avtar7.setOnTouchListener(iv_click);
        iv_avtar8.setOnTouchListener(iv_click);
        iv_avtar9.setOnTouchListener(iv_click);
        iv_avtar10.setOnTouchListener(iv_click);
        iv_avtar11.setOnTouchListener(iv_click);
        iv_avtar12.setOnTouchListener(iv_click);
        iv_avtar13.setOnTouchListener(iv_click);
        iv_avtar14.setOnTouchListener(iv_click);
        iv_avtar15.setOnTouchListener(iv_click);
        iv_avtar16.setOnTouchListener(iv_click);


        dialog.show();
    }

    private void f_browse()
    {
        Intent browse_intenet = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(browse_intenet, RESULT_LOAD_IMAGE);
    }

    private void f_camera()
    {

        //camera stuff
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String sradomstring = UUID.randomUUID().toString();

        //folder stuff
        File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AppConfig.IMAGE_DIRECTORY_NAME);
        imagesFolder.mkdirs();

        File image = new File(imagesFolder, sradomstring + ".jpg");
        uriSavedImage = Uri.fromFile(image);

        actualFileName=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+ AppConfig.IMAGE_DIRECTORY_NAME+"/"+sradomstring+ ".jpg";
        ImageFileName=sradomstring+ ".jpg";
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(imageIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);




/*
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri();
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
*/

    }

    private static File getOutputMediaFile(String sMemberId,Integer serial) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AppConfig.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + AppConfig.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String sradomstring = UUID.randomUUID().toString();

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + sradomstring + ".jpeg");

        return mediaFile;
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile(sMemberId,serial));
    }

    private void getIntenet()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

        ProfilePicFlagFrom =  pref.getString("ProfilePicFromFlag", "");

        Intent int_editMem = getIntent();

        editMemeberMode= int_editMem.getStringExtra("Mode");
        editMemberId=int_editMem.getStringExtra("memberIdforEdit");
    }

    private void f_Touch_Down(View v) {
        ImageView view = (ImageView) v;
        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
        view.invalidate();
    }

    private void f_Touch_Cancel(View v) {
        ImageView view = (ImageView) v;
        view.getDrawable().clearColorFilter();
        view.invalidate();
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(AddProfilePic.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    private void set_setttings_ProfilePicString(String s_name,String sttng) {
        SharedPreferences pref =  this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String shrd_name;

        shrd_name=s_name;

        editor.putString(shrd_name, sttng);
        editor.commit();
    }


    private void set_setttings_MemberProfilePicString(String s_name,String sttng) {
        SharedPreferences pref =  this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //editor.putString("memberid", response.optString("UserID"));
        String shrd_name;

        shrd_name=s_name;

        editor.putString(shrd_name, sttng);
        // editor.putString("Bp_Default_Position", def_pos);
        editor.commit();
    }
    private void set_setttings_profilePicFlagString(String s_name,Integer sttng) {
        SharedPreferences pref =  this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //editor.putString("memberid", response.optString("UserID"));
        String shrd_name;

        shrd_name = s_name;

        editor.putInt(shrd_name, sttng);
        // editor.putString("Bp_Default_Position", def_pos);
        editor.commit();
    }

    private void set_setttings_MemberprofilePicFlagString(String s_name,Integer sttng) {
        SharedPreferences pref =  this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //editor.putString("memberid", response.optString("UserID"));
        String shrd_name;

        shrd_name = s_name;

        editor.putInt(shrd_name, sttng);
        // editor.putString("Bp_Default_Position", def_pos);
        editor.commit();
    }
    private boolean storeImage(Bitmap imageData, String filename) {
        //get path to external storage (SD card)
        //String iconsStoragePath = Environment.getExternalStorageDirectory() + "/myAppDir/myImages/";

        String iconsStoragePath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/" + AppConfig.IMAGE_DIRECTORY_NAME ;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                image_compress(uriSavedImage.getPath());
               //compressimage_and_send(uriSavedImage.getPath());

               /* ImageFileName= actualFileName.substring(actualFileName.lastIndexOf('/') + 1, actualFileName.length());
                if(ProfilePicFlagFrom.equals("AddMember"))
                {
                    set_setttings_MemberProfilePicString("MemberProfilePicName", actualFileName);
                    set_setttings_MemberProfilePicString("imageName", ImageFileName);
                }
                else {
                    set_setttings_ProfilePicString("ProfilePicName" ,actualFileName);
                    set_setttings_MemberProfilePicString("imageName", ImageFileName);
                }
*/
            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture

                Toast.makeText(getApplicationContext(),"User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();


            image_compress(picturePath);
        }
                //compressimage_and_send(picturePath);

            // ImageFileName= actualFileName.substring(actualFileName.lastIndexOf('/') + 1, actualFileName.length());


           /* String StoragePath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + "/" +AppConfig.IMAGE_DIRECTORY_NAME ;

            //   bm=BitmapFactory.decodeFile(picturePath);
            String  picname="mem"+"_"+sMemberId+"_"+"4"+".jpeg";
            //picname="mem"+"_"+sMemberId+"_"+"1"+".jpeg";

            String sradomstring = UUID.randomUUID().toString();
            picname=sradomstring+".jpeg";

//            storeImage(bm,picname);
//            image=StoragePath+"/"+picname;




            if(ProfilePicFlagFrom.equals("AddMember"))
            {
                set_setttings_MemberProfilePicString("MemberProfilePicName", picturePath);
            }
            else {
                set_setttings_ProfilePicString("ProfilePicName", picturePath);
            }



        }
*/
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
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private void image_compress(String Imgpath)

    {


        //showPdialog("loading....");

        if (Imgpath.length() > 0)
        {

            byte[] ImagBytes =  ImageUtils.compressImage(Imgpath);

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);

            if(ProfilePicFlagFrom.equals("AddMember")) {
                String sradomstring = "A_M_"+UUID.randomUUID().toString();
                CompressedImage = mediaStorageDir.getPath() + File.separator + sradomstring + ".jpeg";
            }
            else
            {
                String sradomstring = "A_S_"+UUID.randomUUID().toString();
                CompressedImage = mediaStorageDir.getPath() + File.separator + sradomstring + ".jpeg";
            }

            File obj_compressfile = new File(CompressedImage);
            try {
                FileOutputStream fos=new FileOutputStream(obj_compressfile);
                fos.write(ImagBytes);
                fos.flush();
                fos.close();
        }catch(Exception e)
            {
            }
            String encodedImage = Base64.encodeToString(ImagBytes, Base64.DEFAULT);
            ImageFileName= CompressedImage.substring(CompressedImage.lastIndexOf('/') + 1, CompressedImage.length());
            globalVariable.setMemberImage(ImageFileName);
            globalVariable.setStreamImage(encodedImage);
            finish();
        }
    }

}
package viroopa.com.medikart.MedicineReminder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import viroopa.com.medikart.MainActivity;
import viroopa.com.medikart.MedicineReminder.Model.M_medicinelist;
import viroopa.com.medikart.MedicineReminder.Model.m_medicine_list;
import viroopa.com.medikart.MedicineReminder.services.MR_GetMedfriedDataService;
import viroopa.com.medikart.R;
import viroopa.com.medikart.adapter.AD_memberAdapter;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.common.Change_member_Dialog;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.helper.SqliteMRHandler;
import viroopa.com.medikart.model.M_memberlist;


public class MRA_ReminderMain extends AppCompatActivity implements Change_member_Dialog.OnMemberSelectListener,
        TimePickerDialog.OnTimeSetListener {

    private String sMemberId,User_id;
    private ArrayList<m_medicine_list> medicine_list = new ArrayList<m_medicine_list>();
    private ProgressDialog pDialog;
    List<M_memberlist> MemberData = new ArrayList<M_memberlist>();
    private SqliteMRHandler db_mr;
    private String sSession_Date;
    private   Boolean dataavailable=false;
    private String picked_time="-99";
    private Menu objMemberMenu;
    private String image_name="";
    private AD_memberAdapter memberadapter;
    private LayoutInflater inflater;
    final static  String SCHEDULED_DATE="sceduled_date";
    final static  String SCHEDULED_ID="schedule_id";
    SharedPreferences pref ;
    private TimePickerDialog dpd;
    private RelativeLayout rl_main;
    private ProgressBar progressBar;

    private  TextView night_extra_medicne_count,evening_extra_medicne_count,morning_extra_medicne_count,afternoon_extra_medicne_count;
    private LinearLayout night_show_layout,morning_show_layout,evening_show_layout,afternoon_show_layout;


    private ImageView img_left,img_right,homeBtn,imageViewaddPillBuddy,imageViewAddMedicine,imageViewprogress,mid_img_profile;
    private RelativeLayout img_night,img_morning,img_afternoon,img_evening;
    private TextView txt_date;


    private TextView main_message,second_message;


    private ImageView n_ImgMorningtick1,imgMorningtick2,imgMorningtick3,imgMorningtick4,
            imgMorningtick5,imgtickMorning6,imgtickMorning7,imgtickMorning8;

    private ImageView n_Imgevetick1,imgevetick2,imgevetick3,imgevetick4,
            imgevetick5,imgtickeve6,imgtickeve7,imgtickeve8;

    private ImageView n_Imgafternoontick1,imgafternoontick2,imgafternoontick3,imgafternoontick4,
            imgafternoontick5,imgticafternoon6,imgtickafternoon7,imgtickafternoon8;

    private ImageView n_Imgnighttick1,imgnighttick2,imgnighttick3,imgnighttick4,
            imgnighttick5,imgtickcnight6,imgticknight7,imgticknight8;

    private  LinearLayout ImgMorning1,ImgMorning2,imgMorning3,imgMorning4,
            imgMorning5,imgMorning6,imgMorning7,imgMorning8,Imgeve1,Imgeve2,imgeve3,imgeve4,imgeve5,imgeve6
            ,imgeve7,imgeve8, Imgafternoon1,Imgafternoon2,imgafternoon3,imgafternoon4,imgafternoon5,imgafternoon6,imgafternoon7,imgafternoon8
            , Imgafternight1,Imgnight2,imgnight3,imgnight4,imgnight5,imgnight6,imgnight7,imgnight8;

    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(15,33);
    ImageView img_first_part ;
    ImageView img_second_part ;

    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("MMM dd, cccc");
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat dateFormat_query_popup = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormat_sql = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:ss");

    DateFormat dateFormat_reshedule = new SimpleDateFormat("yyyy-MM-dd");

    DateFormat dateFormat_popup = new SimpleDateFormat("hh.mm a , MMM dd");

    @Override
    public void onSelectMember(String RelashionShip_id, String name,String Imagename) {

        ImageLoad(name,Imagename);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        //String time = hourOfDay+"h"+minute;


        final String sch_id = dpd.getArguments().getString(SCHEDULED_ID);


        picked_time = dpd.getArguments().getString(SCHEDULED_DATE)+" "+getTime_Format(hourOfDay,minute);

        try {

            db_mr.update_Reschedule_time(sch_id,picked_time);
            db_mr.delete_from_notification_table(sch_id);
            clear_screen();
            get_daywise_session("m", dateFormat_query.format(current_date));
            get_daywise_session("n",dateFormat_query.format(current_date));
            get_daywise_session("e",dateFormat_query.format(current_date));
            get_daywise_session("a", dateFormat_query.format(current_date));
            show_message_on_performance(dateFormat_query.format(current_date));
            show_current_session_area();
        } catch (Exception e) {
            e.toString();
        }
    }



    private  void ImageLoad(String name,String imageName)
    {
        if(imageName==null ||imageName.equals("null"))
        {
            imageName="avtar1";
        }

        image_name=imageName;
        View o_view=objMemberMenu.findItem(R.id.circlularImage).getActionView();
        ImageView crImage=(ImageView)o_view.findViewById(R.id.circularimg_view);
        TextView txtmemberName=(TextView)o_view.findViewById(R.id.textViewdm);

        crImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                show_medfriends_dialog();

            }
        });

        if(imageName!=null) {
            String BPimgeName=imageName.substring(imageName.lastIndexOf('/') + 1, imageName.length());

            txtmemberName.setText(name);


            if (BPimgeName.startsWith("avtar")) {
                Resources res = getApplicationContext().getResources();

                int resourceId = res.getIdentifier(BPimgeName, "drawable", getApplicationContext().getPackageName());
                Drawable drawable = res.getDrawable(resourceId);
                crImage.setImageDrawable(drawable);
            } else {
                String iconsStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + AppConfig.IMAGE_DIRECTORY_NAME;
                File pathfile = new File(iconsStoragePath + File.separator + BPimgeName);
                Bitmap mybitmap = BitmapFactory.decodeFile(pathfile.getPath());
                if (mybitmap != null) {
                    Drawable d = new BitmapDrawable(getResources(), mybitmap);
                    crImage.setImageDrawable(d);
                }
            }
        }
        mid_img_profile.setImageDrawable(crImage.getDrawable());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.med_reminder_night_day_meds);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        inflater = LayoutInflater.from(this);
        db_mr = new SqliteMRHandler(this);
        pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        pDialog = new ProgressDialog(this);
        registerReceiver(broadcastReceiver, new IntentFilter("GCMNotificationMedfriendIntentService"));


        registerReceiver(InvitebroadcastReceiver, new IntentFilter("Invitenotification"));
        registerReceiver(AcceptedbroadcastReceiver, new IntentFilter("AcceptNotification"));

        getIntenet();

        // createfloatingmenu();
        initMedicineViews();
        txt_date = (TextView) findViewById(R.id.txt_date);
        homeBtn= (ImageView) findViewById(R.id.homeBtn);
        img_left = (ImageView) findViewById(R.id.img_left);
        img_right = (ImageView) findViewById(R.id.img_right);
        rl_main=(RelativeLayout)findViewById(R.id.rl_main);
        // imageViewadd = (ImageView) findViewById(R.id.imageViewadd);
        img_night = (RelativeLayout) findViewById(R.id.img_night);
        img_morning = (RelativeLayout) findViewById(R.id.img_morning);
        img_afternoon = (RelativeLayout) findViewById(R.id.img_afternoon);
        img_evening = (RelativeLayout) findViewById(R.id.img_evening);

        imageViewaddPillBuddy= (ImageView) findViewById(R.id.imageViewaddPillBuddy);
        imageViewAddMedicine= (ImageView) findViewById(R.id.imageViewAddMedicine);
        imageViewprogress= (ImageView) findViewById(R.id.imageViewprogress);
        mid_img_profile= (ImageView) findViewById(R.id.mid_img_profile);
        main_message= (TextView) findViewById(R.id.textView3);
        second_message= (TextView) findViewById(R.id.edtMedicine);

        night_extra_medicne_count     = (TextView) findViewById(R.id.night_extra_medicne_count);
        evening_extra_medicne_count   = (TextView) findViewById(R.id.evening_extra_medicne_count);
        morning_extra_medicne_count   = (TextView) findViewById(R.id.morning_extra_medicne_count);
        afternoon_extra_medicne_count = (TextView) findViewById(R.id.afternoon_extra_medicne_count);

        show_current_session_area();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent Intenet_home = new Intent(MRA_ReminderMain.this, MainActivity.class);
                startActivity(Intenet_home);
                finish();
            }
        });


        sSession_Date = pref.getString("session_date", "");
        if (!sSession_Date.equals(""))
        {
            try {
                current_date = dateFormat_query.parse(sSession_Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        sSession_Date =dateFormat_query.format(current_date);

        txt_date.setText(dateFormat.format(current_date));

        clear_screen();
        get_daywise_session("m", sSession_Date);
        get_daywise_session("n",sSession_Date);
        get_daywise_session("e",sSession_Date);
        get_daywise_session("a", sSession_Date);

        show_message_on_performance(sSession_Date);

        View.OnTouchListener iv_click = new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {

                        LinearLayout iv = (LinearLayout) v;
                        Integer id = (Integer) iv.getTag(R.id.key_MemberImage);

                        popFunction((String) iv.getTag(R.id.key_schedule_id), (String) iv.getTag(R.id.key_schedule_date), (String) iv.getTag(R.id.key_schedule_dosage), (Integer) iv.getTag(R.id.key_MemberImage),(Integer) iv.getTag(R.id.key_first_color_id),(Integer) iv.getTag(R.id.key_second_color_id), (String) iv.getTag(R.id.key_medicine_name),
                                (String) iv.getTag(R.id.key_medicine_status),(String) iv.getTag(R.id.key_refill_date),(int)iv.getTag(R.id.key_no_reminder_flag),(int)iv.getTag(R.id.key_refill_flag));
                    }

                }

                return true;
            }
        };



        ImgMorning1.setOnTouchListener(iv_click);
        ImgMorning2.setOnTouchListener(iv_click);
        imgMorning3.setOnTouchListener(iv_click);
        imgMorning4.setOnTouchListener(iv_click);
        imgMorning5.setOnTouchListener(iv_click);
        imgMorning6.setOnTouchListener(iv_click);
        imgMorning7.setOnTouchListener(iv_click);
        imgMorning8.setOnTouchListener(iv_click);

        Imgeve1.setOnTouchListener(iv_click);
        Imgeve2.setOnTouchListener(iv_click);
        imgeve3.setOnTouchListener(iv_click);
        imgeve4.setOnTouchListener(iv_click);
        imgeve5.setOnTouchListener(iv_click);
        imgeve6.setOnTouchListener(iv_click);
        imgeve7.setOnTouchListener(iv_click);
        imgeve8.setOnTouchListener(iv_click);

        Imgafternoon1.setOnTouchListener(iv_click);
        Imgafternoon2.setOnTouchListener(iv_click);
        imgafternoon3.setOnTouchListener(iv_click);
        imgafternoon4.setOnTouchListener(iv_click);
        imgafternoon5.setOnTouchListener(iv_click);
        imgafternoon6.setOnTouchListener(iv_click);
        imgafternoon7.setOnTouchListener(iv_click);
        imgafternoon8.setOnTouchListener(iv_click);




        Imgafternight1.setOnTouchListener(iv_click);
        Imgnight2.setOnTouchListener(iv_click);
        imgnight3.setOnTouchListener(iv_click);
        imgnight4.setOnTouchListener(iv_click);
        imgnight5.setOnTouchListener(iv_click);
        imgnight6.setOnTouchListener(iv_click);
        imgnight7.setOnTouchListener(iv_click);
        imgnight8.setOnTouchListener(iv_click);




        txt_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                current_date = Calendar.getInstance().getTime();
                txt_date.setText(dateFormat.format(current_date));

                clear_screen();
                get_daywise_session("m", dateFormat_query.format(current_date));
                get_daywise_session("n", dateFormat_query.format(current_date));
                get_daywise_session("e", dateFormat_query.format(current_date));
                get_daywise_session("a", dateFormat_query.format(current_date));
                show_message_on_performance(dateFormat_query.format(current_date));
                show_current_session_area();


            }
        });

        img_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dataavailable=false;
                current_date = date_addDays(current_date, -1);
                txt_date.setText(dateFormat.format(current_date));
                clear_screen();
                get_daywise_session("m", dateFormat_query.format(current_date));
                get_daywise_session("n",dateFormat_query.format(current_date));
                get_daywise_session("e",dateFormat_query.format(current_date));
                get_daywise_session("a", dateFormat_query.format(current_date));
                show_message_on_performance(dateFormat_query.format(current_date));

                show_current_session_area();
                start_layout_animation();
            }
        });



        morning_extra_medicne_count.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Show_session_wise_all_medicines("m", dateFormat_query.format(current_date), "06:00 - 12:00");

            }
        });
        night_extra_medicne_count.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Show_session_wise_all_medicines("n",dateFormat_query.format(current_date),"00:00 - 06:00");

            }
        });
        evening_extra_medicne_count.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Show_session_wise_all_medicines("e",dateFormat_query.format(current_date),"18:00 - 24:00");

            }
        });
        afternoon_extra_medicne_count.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Show_session_wise_all_medicines("a",dateFormat_query.format(current_date),"12:00 - 18:00");

            }
        });



        imageViewaddPillBuddy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Show_Invite_MediFriends();

            }
        });

        imageViewAddMedicine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Show_Add_Medicine();

            }
        });
        imageViewprogress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Show_progres();

            }
        });

        img_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dataavailable=false;
                current_date = date_addDays(current_date, 1);
                txt_date.setText(dateFormat.format(current_date));
                clear_screen();
                get_daywise_session("m", dateFormat_query.format(current_date));
                get_daywise_session("n",dateFormat_query.format(current_date));
                get_daywise_session("e",dateFormat_query.format(current_date));
                get_daywise_session("a",dateFormat_query.format(current_date));
                show_message_on_performance(dateFormat_query.format(current_date));
                show_current_session_area();
                start_layout_animation();
            }
        });

        img_night.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Show_Daywise_Session("n");
            }
        });
        img_morning.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Show_Daywise_Session("m");
            }
        });
        img_afternoon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Show_Daywise_Session("a");
            }
        });
        img_evening.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //  Show_Daywise_Session("e");
            }
        });



    }

    @Override

    public void onResume() {
        super.onResume();
        clear_screen();
        get_daywise_session("m", sSession_Date);
        get_daywise_session("n",sSession_Date);
        get_daywise_session("e",sSession_Date);
        get_daywise_session("a", sSession_Date);
        show_message_on_performance(dateFormat_query.format(current_date));
        show_current_session_area();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Minflater = getMenuInflater();
        Minflater.inflate(R.menu.menu_new_add_medicine, menu);
        this.objMemberMenu=menu;

        View mCustomView = inflater.inflate(R.layout.circula_image_icon_dm, null);
        objMemberMenu.findItem(R.id.circlularImage).setActionView(mCustomView);

        ImageLoad(pref.getString("User_Name",""),pref.getString("imagename",""));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent Intenet_adds = new Intent(this, MRA_MonitorSetting.class);
            startActivity(Intenet_adds);
            return true;
        }


        if (id == R.id.action_allmeds) {
            Show_All_Medicine();
        }

        return super.onOptionsItemSelected(item);
    }



    private void Show_All_Medicine(){
        Intent Intenet_all_medicine = new Intent(MRA_ReminderMain.this, MRA_Schedule_All_Medicine.class);
        startActivity(Intenet_all_medicine);
    }

    private void Show_Add_Medicine(){
        Intent Intenet_buy = new Intent(MRA_ReminderMain.this, MRA_SetReminder.class);
        startActivity(Intenet_buy);
    }


    private void Show_Invite_MediFriends(){
        Intent Intenet_add = new Intent(MRA_ReminderMain.this, MRA_PillBuddy.class);
        startActivity(Intenet_add);
    }
    private void Show_progres(){
        Intent Intenet_add = new Intent(MRA_ReminderMain.this, MRA_AnalysisData.class);
        Intenet_add.putExtra("sMemberId",sMemberId);
        Intenet_add.putExtra("ImageName",image_name);
        startActivity(Intenet_add);
    }
    private void Show_session_wise_all_medicines(String s_name,String s_date,String s_time){
        Intent Intenet_add = new Intent(MRA_ReminderMain.this, MRA_session_wise_medicines.class);
        Intenet_add.putExtra("sch_date",s_date);
        Intenet_add.putExtra("Session_name",s_name);
        Intenet_add.putExtra("Session_time",s_time);
        startActivity(Intenet_add);
    }





    private void getIntenet() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        User_id = pref.getString("memberid", "");
        Intent i =getIntent();
         String req_sender_memberid=i.getStringExtra("req_sender_memberid");

        if(i.getStringExtra("Invite")!=null)
        {
            show_invite_request(i.getStringExtra("msg"),req_sender_memberid);
        }

        else if(i.getStringExtra("Accept")!=null)
        {
            show_invite_request_accept(i.getStringExtra("msg"),req_sender_memberid);
        }

        ConstData.get_medfriend_data(getApplicationContext(),sMemberId);

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
        new AlertDialog.Builder(MRA_ReminderMain.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void f_Touch_Down(View v) {
        ImageView view = (ImageView) v;
        //overlay is black with transparency of 0x77 (119)
        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
        view.invalidate();
    }

    private void f_Touch_Cancel(View v) {
        ImageView view = (ImageView) v;
        //clear the overlay
        view.getDrawable().clearColorFilter();
        view.invalidate();
    }




    public Date date_addDays(Date date, int days)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }





    private void popFunction(String Schedule_id, final String Sch_Datetime,String Sch_dosage,Integer img_id, Integer f_color_id,
                             Integer s_color_id,String MedName,String Status,String refill_date,int no_rem_flag,int refill_flag){

        final String Mede_Schedule_id=Schedule_id;
        String Schedule_Date=Sch_Datetime;
        String Current_status="";
        String taken_time="";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.popup, null);
        builder.setView(dialogview);
        final Dialog dialog = builder.create();

       /* WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.windowAnimations = 1;*/

      /*  WindowManager manager = (WindowManager)MedReminderMain.this.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        params.width = display.getWidth()*4/4;
        params.height = display.getHeight()/2;*/

        final TextView Scheduledatetimetxt =(TextView)dialogview.findViewById(R.id.Scheduledatetimetxt);
        final TextView pop_dosage =(TextView)dialogview.findViewById(R.id.pop_dosage);
        final TextView pop_medname =(TextView)dialogview.findViewById(R.id.pop_medname);
        final LinearLayout pop_mimg =(LinearLayout)dialogview.findViewById(R.id.pop_mimg);
        final Button takebtn= (Button)dialogview.findViewById(R.id.pop_taken);
        final TextView refill_reminder_txt= (TextView)dialogview.findViewById(R.id.refill_reminder_txt);
        final TextView txt_no_notification= (TextView)dialogview.findViewById(R.id.txt_no_notification);


        if(no_rem_flag==0)
        {
            txt_no_notification.setVisibility(View.GONE);
        }else {
            txt_no_notification.setVisibility(View.VISIBLE);
        }




        Cursor cursor_notification =   db_mr.get_notification_data_for_schedule_id(Schedule_id,sMemberId);
        int c = cursor_notification.getCount();

        if ((cursor_notification != null) || (cursor_notification.getCount() > 0)) {
            if (cursor_notification.moveToFirst()) {

                Current_status=cursor_notification.getString(cursor_notification.getColumnIndex("status"));
                taken_time=cursor_notification.getString(cursor_notification.getColumnIndex("datetime_taken"));
                if(Current_status==null)
                {
                    Current_status="";
                }
                if(Current_status.equals("S")) {
                    Schedule_Date = cursor_notification.getString(cursor_notification.getColumnIndex("datetime_taken"));
                }
            }

        }
        cursor_notification.close();


        if(refill_flag==1) {

            if (refill_date != null) {
                if (!refill_date.equals("")) {
                    if (!refill_date.equals("null")) {
                        refill_reminder_txt.setText("Refill set on : " + refill_date);
                    }
                }
            }
        }

        if(Current_status.equals("S"))
        {
            try {
                current_date = dateFormat_query_popup.parse(Schedule_Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Scheduledatetimetxt.setText("Skipped : " + dateFormat_popup.format(current_date));
            takebtn.setText("Undo");
            dialogview.findViewById(R.id.pop_reschedule).setVisibility(View.GONE);
            dialogview.findViewById(R.id.skip).setVisibility(View.GONE);
            pop_dosage.setText("Dosage : " + Sch_dosage);
            pop_medname.setText(MedName);
            add_image_views(pop_mimg,img_id,f_color_id,s_color_id,new LinearLayout.LayoutParams(25,40));
        }


        else
        if(Current_status.equals("SUS"))
        {
            try {
                current_date = dateFormat_query_popup.parse(Schedule_Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Scheduledatetimetxt.setText("Suspended");
            Scheduledatetimetxt.setTextColor(Color.parseColor("#b20e0f"));
            takebtn.setVisibility(View.GONE);
            dialogview.findViewById(R.id.pop_reschedule).setVisibility(View.GONE);
            dialogview.findViewById(R.id.skip).setVisibility(View.GONE);
            pop_dosage.setText("Dosage : " + Sch_dosage);
            pop_medname.setText(MedName);
            add_image_views(pop_mimg,img_id,f_color_id,s_color_id,new LinearLayout.LayoutParams(25,40));
        }else
        {

            try {
                current_date = dateFormat_query_popup.parse(Schedule_Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Schedule_Date = dateFormat_popup.format(current_date);

            pop_dosage.setText("Dosage : " + Sch_dosage);
            pop_medname.setText(MedName);

            add_image_views(pop_mimg,img_id,f_color_id,s_color_id,new LinearLayout.LayoutParams(25,40));

            Scheduledatetimetxt.setText("Scheduled on : " + Schedule_Date);


            if (Status != null) {
                if (Status.equals("T")) {

                    takebtn.setText("Undo");
                    dialogview.findViewById(R.id.pop_reschedule).setVisibility(View.GONE);
                    dialogview.findViewById(R.id.skip).setVisibility(View.GONE);

                    try {
                        current_date = dateFormat_query_popup.parse(taken_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Scheduledatetimetxt.setText("Taken on : " + dateFormat_popup.format(current_date));
                }
            }

        }

        dialogview.findViewById(R.id.btt_close).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




        dialogview.findViewById(R.id.pop_reschedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select_time(Mede_Schedule_id,Sch_Datetime);
               // med_rem_reschedule(Mede_Schedule_id,Sch_Datetime);

                dialog.dismiss();
            }
        });



        dialogview.findViewById(R.id.pop_taken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!takebtn.getText().toString().equals("Undo")) {
                    dialog.dismiss();
                    db_mr.Update_Medicine_Schedule(Mede_Schedule_id, "T", dateFormat_query_popup.format(Calendar.getInstance().getTime()));
                    db_mr.delete_from_notification_table(Mede_Schedule_id);
                   ConstData.create_json_from_table("reminder_schedule","E","",Mede_Schedule_id,sMemberId,dateFormat_query_popup.format(Calendar.getInstance().getTime()),getApplicationContext());
                    get_daywise_session("m", dateFormat_query.format(current_date));
                    get_daywise_session("n", dateFormat_query.format(current_date));
                    get_daywise_session("e", dateFormat_query.format(current_date));
                    get_daywise_session("a", dateFormat_query.format(current_date));
                    show_message_on_performance(dateFormat_query.format(current_date));
                    show_current_session_area();
                }else
                {
                    dialog.dismiss();
                    db_mr.Update_Medicine_Schedule(Mede_Schedule_id, "SCH", dateFormat_query_popup.format(Calendar.getInstance().getTime()));
                    db_mr.delete_from_notification_table(Mede_Schedule_id);
                    ConstData.create_json_from_table("reminder_schedule","E","",Mede_Schedule_id,sMemberId,dateFormat_query_popup.format(Calendar.getInstance().getTime()),MRA_ReminderMain.this);

                    get_daywise_session("m", dateFormat_query.format(current_date));
                    get_daywise_session("n", dateFormat_query.format(current_date));
                    get_daywise_session("e", dateFormat_query.format(current_date));
                    get_daywise_session("a", dateFormat_query.format(current_date));
                    show_message_on_performance(dateFormat_query.format(current_date));
                    show_current_session_area();
                }

            }
        });
        dialogview.findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_mr.Update_Medicine_Schedule(Mede_Schedule_id,"S",dateFormat_query_popup.format(Calendar.getInstance().getTime()));
                ConstData.create_json_from_table("reminder_schedule","E","",Mede_Schedule_id,sMemberId,dateFormat_query_popup.format(Calendar.getInstance().getTime()),MRA_ReminderMain.this);
                dialog.dismiss();
            }
        });

        if(!User_id.equals(sMemberId))
        {
            dialogview.findViewById(R.id.pop_taken).setVisibility(View.GONE);
            dialogview.findViewById(R.id.skip).setVisibility(View.GONE);
            dialogview.findViewById(R.id.pop_reschedule).setVisibility(View.GONE);
        }

        dialog.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;

        dialog.show();
    }



    private void initMedicineViews()
    {
        ImgMorning1= (LinearLayout) findViewById(R.id.ImgMorning1);
        n_ImgMorningtick1= (ImageView) findViewById(R.id.n_ImgMorningtick1);
        ImgMorning2 = (LinearLayout) findViewById(R.id.ImgMorning2);
        imgMorningtick2= (ImageView) findViewById(R.id.imgMorningtick2);
        imgMorning3= (LinearLayout) findViewById(R.id.imgMorning3);
        imgMorningtick3= (ImageView) findViewById(R.id.imgMorningtick3);
        imgMorning4= (LinearLayout) findViewById(R.id.imgMorning4);
        imgMorningtick4= (ImageView) findViewById(R.id.imgMorningtick4);
        imgMorning5= (LinearLayout) findViewById(R.id.imgMorning5);
        imgMorningtick5= (ImageView) findViewById(R.id.imgMorningtick5);
        imgMorning6= (LinearLayout) findViewById(R.id.imgMorning6);
        imgtickMorning6= (ImageView) findViewById(R.id.imgtickMorning6);
        imgMorning7= (LinearLayout) findViewById(R.id.imgMorning7);
        imgtickMorning7= (ImageView) findViewById(R.id.imgtickMorning7);
        imgMorning8= (LinearLayout) findViewById(R.id.imgMorning8);
        imgtickMorning8= (ImageView) findViewById(R.id.imgtickMorning8);

        Imgeve1= (LinearLayout) findViewById(R.id.Imgeve1);
        n_Imgevetick1= (ImageView) findViewById(R.id.n_Imgevetick1);
        Imgeve2= (LinearLayout) findViewById(R.id.Imgeve2);
        imgevetick2= (ImageView) findViewById(R.id.imgevetick2);
        imgeve3= (LinearLayout) findViewById(R.id.imgeve3);
        imgevetick3= (ImageView) findViewById(R.id.imgevetick3);
        imgeve4= (LinearLayout) findViewById(R.id.imgeve4);
        imgevetick4= (ImageView) findViewById(R.id.imgevetick4);
        imgeve5= (LinearLayout) findViewById(R.id.imgeve5);
        imgevetick5= (ImageView) findViewById(R.id.imgevetick5);
        imgeve6= (LinearLayout) findViewById(R.id.imgeve6);
        imgtickeve6= (ImageView) findViewById(R.id.imgtickeve6);
        imgeve7= (LinearLayout) findViewById(R.id.imgeve7);
        imgtickeve7= (ImageView) findViewById(R.id.imgtickeve7);
        imgeve8= (LinearLayout) findViewById(R.id.imgeve8);
        imgtickeve8= (ImageView) findViewById(R.id.imgtickeve8);

        Imgafternoon1= (LinearLayout) findViewById(R.id.Imgafternoon1);
        n_Imgafternoontick1= (ImageView) findViewById(R.id.n_Imgafternoontick1);
        Imgafternoon2= (LinearLayout) findViewById(R.id.Imgafternoon2);
        imgafternoontick2= (ImageView) findViewById(R.id.imgafternoontick2);
        imgafternoon3= (LinearLayout) findViewById(R.id.imgafternoon3);
        imgafternoontick3= (ImageView) findViewById(R.id.imgafternoontick3);
        imgafternoon4= (LinearLayout) findViewById(R.id.imgafternoon4);
        imgafternoontick4= (ImageView) findViewById(R.id.imgafternoontick4);
        imgafternoon5= (LinearLayout) findViewById(R.id.imgafternoon5);
        imgafternoontick5= (ImageView) findViewById(R.id.imgafternoontick5);
        imgafternoon6= (LinearLayout) findViewById(R.id.imgafternoon6);
        imgticafternoon6= (ImageView) findViewById(R.id.imgticafternoon6);
        imgafternoon7= (LinearLayout) findViewById(R.id.imgafternoon7);
        imgtickafternoon7= (ImageView) findViewById(R.id.imgtickafternoon7);
        imgafternoon8= (LinearLayout) findViewById(R.id.imgafternoon8);
        imgtickafternoon8= (ImageView) findViewById(R.id.imgtickafternoon8);




        Imgafternight1= (LinearLayout) findViewById(R.id.Imgafternight1);
        n_Imgnighttick1= (ImageView) findViewById(R.id.n_Imgnighttick1);
        Imgnight2= (LinearLayout) findViewById(R.id.Imgnight2);
        imgnighttick2= (ImageView) findViewById(R.id.imgnighttick2);
        imgnight3= (LinearLayout) findViewById(R.id.imgnight3);
        imgnighttick3= (ImageView) findViewById(R.id.imgnighttick3);
        imgnight4= (LinearLayout) findViewById(R.id.imgnight4);
        imgnighttick4= (ImageView) findViewById(R.id.imgnighttick4);
        imgnight5= (LinearLayout) findViewById(R.id.imgnight5);
        imgnighttick5= (ImageView) findViewById(R.id.imgnighttick5);
        imgnight6= (LinearLayout) findViewById(R.id.imgnight6);
        imgtickcnight6= (ImageView) findViewById(R.id.imgtickcnight6);
        imgnight7= (LinearLayout) findViewById(R.id.imgnight7);
        imgticknight7= (ImageView) findViewById(R.id.imgticknight7);
        imgnight8= (LinearLayout) findViewById(R.id.imgnight8);
        imgticknight8= (ImageView) findViewById(R.id.imgticknight8);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);



    }


    private void get_daywise_session(String  Session,String Sch_DateTime) {
        medicine_list.clear();
        Cursor cursor_session = db_mr.get_med_rem_sch_daywise_session(Session,Sch_DateTime,sMemberId);
        int numm=cursor_session.getCount();
        if ((cursor_session != null) || (cursor_session.getCount()> 0))
        {


            if (cursor_session.moveToFirst()) {
                do {
                    m_medicine_list O_medicine_list = new m_medicine_list();

                    O_medicine_list.setMedicine_Id(cursor_session.getString(cursor_session.getColumnIndex("reminder_id")));
                    O_medicine_list.setMedicine_Name(cursor_session.getString(cursor_session.getColumnIndex("medicine_name")));
                    O_medicine_list.setSchedule_Id(cursor_session.getString(cursor_session.getColumnIndex("schedule_id")));
                    O_medicine_list.setRefill_flag(cursor_session.getInt(cursor_session.getColumnIndex("RefillRem")));
                    O_medicine_list.setSchedule_date(cursor_session.getString(cursor_session.getColumnIndex("datetime_set")));
                    O_medicine_list.setSchedule_status(cursor_session.getString(cursor_session.getColumnIndex("status")));
                    O_medicine_list.setSchedule_dosage(cursor_session.getString(cursor_session.getColumnIndex("dosage")));
                    O_medicine_list.setImd_id(cursor_session.getInt(cursor_session.getColumnIndex("image_id")));
                    O_medicine_list.setFirst_color_id(cursor_session.getInt(cursor_session.getColumnIndex("first_color_id")));
                    O_medicine_list.setSecond_color_id(cursor_session.getInt(cursor_session.getColumnIndex("second_color_id")));
                    O_medicine_list.setRefill_date(cursor_session.getString(cursor_session.getColumnIndex("RefillDate")));
                    O_medicine_list.setNo_reminder_flag(cursor_session.getInt(cursor_session.getColumnIndex("no_reminder")));


                    // Integer imgidnr=Integer.parseInt(imgidstr);

                    medicine_list.add(O_medicine_list);


                } while (cursor_session.moveToNext());

                //adapter_daywise_session.notifyDataSetChanged();
            }else
            {

            }
            cursor_session.close();
            if(Session.equals("m") && medicine_list.size()>=1)
            {
                dataavailable=true;
                Show_Medicine_data_Morning();
            }
            if(Session.equals("a") && medicine_list.size()>=1)
            {
                dataavailable=true;
                Show_Medicine_data_afternoon();
            }
            if(Session.equals("e") && medicine_list.size()>=1)
            {
                dataavailable=true;
                Show_Medicine_data_Evening();
            }
            if(Session.equals("n") && medicine_list.size()>=1)
            {
                dataavailable=true;
                Show_Medicine_data_Night();
            }
            if(dataavailable==false)
            {
                clear_screen();
            }
        }

    }


    private void Show_Medicine_data_afternoon()
    {
        for(int i=0;i<medicine_list.size();i++)
        {
            m_medicine_list O_medicine_list = medicine_list.get(i);


            if (O_medicine_list != null) {
                switch (i) {
                    case 0:



                        add_image_views(Imgafternoon1,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(Imgafternoon1,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            n_Imgafternoontick1.setVisibility(View.VISIBLE);
                        }else  {
                            n_Imgafternoontick1.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 1:

                        add_image_views(Imgafternoon2,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(Imgafternoon2,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgafternoontick2.setVisibility(View.VISIBLE);
                        }else  {
                            imgafternoontick2.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 2:
                        add_image_views(imgafternoon3,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgafternoon3,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgafternoontick3.setVisibility(View.VISIBLE);
                        }else  {
                            imgafternoontick3.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 3:
                        add_image_views(imgafternoon4,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgafternoon4,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgafternoontick4.setVisibility(View.VISIBLE);
                        }else  {
                            imgafternoontick4.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 4:
                        add_image_views(imgafternoon5,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgafternoon5,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgafternoontick5.setVisibility(View.VISIBLE);
                        }else  {
                            imgafternoontick5.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 5:
                        add_image_views(imgafternoon6,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgafternoon6,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgticafternoon6.setVisibility(View.VISIBLE);
                        }else  {
                            imgticafternoon6.setVisibility(View.INVISIBLE);
                        }



                        break;
                    case 6:
                        add_image_views(imgafternoon7,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgafternoon7,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgtickafternoon7.setVisibility(View.VISIBLE);
                        }else  {
                            imgtickafternoon7.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 7:
                        add_image_views(imgafternoon8,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgafternoon8,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgtickafternoon8.setVisibility(View.VISIBLE);
                        }else  {
                            imgtickafternoon8.setVisibility(View.INVISIBLE);
                        }


                        break;

                    case 8:

                        Integer extra_count=medicine_list.size()-8;
                        afternoon_extra_medicne_count.setText(String.valueOf(extra_count)+"+");
                        afternoon_extra_medicne_count.setVisibility(View.VISIBLE);

                        break;
                }





            }
        }
    }
    private void Show_Medicine_data_Morning()
    {
        for(int i=0;i<medicine_list.size();i++)
        {
            m_medicine_list O_medicine_list = medicine_list.get(i);



            if (O_medicine_list != null) {
                switch (i) {
                    case 0:
                        add_image_views(ImgMorning1,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(ImgMorning1,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            n_ImgMorningtick1.setVisibility(View.VISIBLE);
                        }else
                        {
                            n_ImgMorningtick1.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 1:
                        add_image_views(ImgMorning2,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(ImgMorning2,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgMorningtick2.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgMorningtick2.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 2:
                        add_image_views(imgMorning3,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);

                        set_tag_for_imageviews(imgMorning3,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgMorningtick3.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgMorningtick3.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 3:
                        add_image_views(imgMorning4,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgMorning4,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgMorningtick4.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgMorningtick4.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 4:
                        add_image_views(imgMorning5,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgMorning5,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgMorningtick5.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgMorningtick5.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 5:
                        add_image_views(imgMorning6,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgMorning6,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgtickMorning6.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgtickMorning6.setVisibility(View.INVISIBLE);
                        }



                        break;
                    case 6:

                        add_image_views(imgMorning7,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgMorning7,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgtickMorning7.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgtickMorning7.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 7:
                        add_image_views(imgMorning8,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgMorning8,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgtickMorning8.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgtickMorning8.setVisibility(View.INVISIBLE);
                        }


                        break;


                    case 8:

                        Integer extra_count=medicine_list.size()-8;
                        morning_extra_medicne_count.setText(String.valueOf(extra_count)+"+");
                        morning_extra_medicne_count.setVisibility(View.VISIBLE);

                        break;
                }





            }
        }
    }
    private void Show_Medicine_data_Evening()
    {
        for(int i=0;i<medicine_list.size();i++)
        {
            m_medicine_list O_medicine_list = medicine_list.get(i);

            if (O_medicine_list != null) {
                switch (i) {
                    case 0:
                        add_image_views(Imgeve1,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(Imgeve1,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            n_Imgevetick1.setVisibility(View.VISIBLE);
                        }else
                        {
                            n_Imgevetick1.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 1:
                        add_image_views(Imgeve2,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(Imgeve2,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgevetick2.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgevetick2.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 2:
                        add_image_views(imgeve3,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgeve3,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgevetick3.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgevetick3.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 3:
                        add_image_views(imgeve4,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgeve4,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgevetick4.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgevetick4.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 4:
                        add_image_views(imgeve5,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgeve5,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgevetick5.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgevetick5.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 5:
                        add_image_views(imgeve6,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgeve6,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgtickeve6.setVisibility(View.VISIBLE);
                        }



                        break;
                    case 6:
                        add_image_views(imgeve7,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgeve7,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgtickeve7.setVisibility(View.VISIBLE);
                        }


                        break;
                    case 7:
                        add_image_views(imgeve8,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgeve8,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgtickeve8.setVisibility(View.VISIBLE);
                        }


                        break;


                    case 8:

                        Integer extra_count=medicine_list.size()-8;
                        evening_extra_medicne_count.setText(String.valueOf(extra_count)+"+");
                        evening_extra_medicne_count.setVisibility(View.VISIBLE);

                        break;

                }





            }
        }
    }






    private void Show_Medicine_data_Night()
    {
        for(int i=0;i<medicine_list.size();i++)
        {
            m_medicine_list O_medicine_list = medicine_list.get(i);

            if (O_medicine_list != null) {
                switch (i) {
                    case 0:
                        add_image_views(Imgafternight1,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(Imgafternight1,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            n_Imgnighttick1.setVisibility(View.VISIBLE);
                        }else
                        {
                            n_Imgnighttick1.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 1:
                        add_image_views(Imgnight2,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(Imgnight2,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgnighttick2.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgnighttick2.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 2:
                        add_image_views(imgnight3,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgnight3,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgnighttick3.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgnighttick3.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 3:
                        add_image_views(imgnight4,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgnight4,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgnighttick4.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgnighttick4.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 4:
                        add_image_views(imgnight5,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgnight5,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgnighttick5.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgnighttick5.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 5:
                        add_image_views(imgnight6,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgnight6,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgtickcnight6.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgtickcnight6.setVisibility(View.INVISIBLE);
                        }



                        break;
                    case 6:
                        add_image_views(imgnight7,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgnight7,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgticknight7.setVisibility(View.VISIBLE);
                        }else
                        {
                            imgticknight7.setVisibility(View.INVISIBLE);
                        }


                        break;
                    case 7:
                        add_image_views(imgnight8,O_medicine_list.getImd_id(),O_medicine_list.getFirst_color_id(),O_medicine_list.getSecond_color_id(),param);
                        set_tag_for_imageviews(imgnight8,O_medicine_list);

                        if(O_medicine_list.getSchedule_status().equals("T"))
                        {
                            imgticknight8.setVisibility(View.VISIBLE);
                        }


                        break;

                    case 8:

                        Integer extra_count=medicine_list.size()-8;
                        night_extra_medicne_count.setText(String.valueOf(extra_count)+"+");
                        night_extra_medicne_count.setVisibility(View.VISIBLE);

                        break;
                }





            }
        }
    }



    private void clear_screen()
    {
        ImgMorning1.setVisibility(View.INVISIBLE);
        n_ImgMorningtick1.setVisibility(View.INVISIBLE);
        ImgMorning2.setVisibility(View.INVISIBLE);
        imgMorningtick2.setVisibility(View.INVISIBLE);
        imgMorning3.setVisibility(View.INVISIBLE);
        imgMorningtick3.setVisibility(View.INVISIBLE);
        imgMorning4.setVisibility(View.INVISIBLE);
        imgMorningtick4.setVisibility(View.INVISIBLE);
        imgMorning5.setVisibility(View.INVISIBLE);
        imgMorningtick5.setVisibility(View.INVISIBLE);
        imgMorning6.setVisibility(View.INVISIBLE);
        imgtickMorning6.setVisibility(View.INVISIBLE);
        imgMorning7.setVisibility(View.INVISIBLE);
        imgtickMorning7.setVisibility(View.INVISIBLE);
        imgMorning8.setVisibility(View.INVISIBLE);
        imgtickMorning8.setVisibility(View.INVISIBLE);
        Imgeve1.setVisibility(View.INVISIBLE);
        n_Imgevetick1.setVisibility(View.INVISIBLE);
        Imgeve2.setVisibility(View.INVISIBLE);
        imgevetick2.setVisibility(View.INVISIBLE);
        imgeve3.setVisibility(View.INVISIBLE);
        imgevetick3.setVisibility(View.INVISIBLE);
        imgeve4.setVisibility(View.INVISIBLE);
        imgevetick4.setVisibility(View.INVISIBLE);
        imgeve5.setVisibility(View.INVISIBLE);
        imgevetick5.setVisibility(View.INVISIBLE);
        imgeve6.setVisibility(View.INVISIBLE);
        imgtickeve6.setVisibility(View.INVISIBLE);
        imgeve7.setVisibility(View.INVISIBLE);
        imgtickeve7.setVisibility(View.INVISIBLE);
        imgeve8.setVisibility(View.INVISIBLE);
        imgtickeve8.setVisibility(View.INVISIBLE);
        Imgafternoon1.setVisibility(View.INVISIBLE);
        n_Imgafternoontick1.setVisibility(View.INVISIBLE);
        Imgafternoon2.setVisibility(View.INVISIBLE);
        imgafternoontick2.setVisibility(View.INVISIBLE);
        imgafternoon3.setVisibility(View.INVISIBLE);
        imgafternoontick3.setVisibility(View.INVISIBLE);
        imgafternoon4.setVisibility(View.INVISIBLE);
        imgafternoontick4.setVisibility(View.INVISIBLE);
        imgafternoon5.setVisibility(View.INVISIBLE);
        imgafternoontick5.setVisibility(View.INVISIBLE);
        imgafternoon6.setVisibility(View.INVISIBLE);
        imgticafternoon6.setVisibility(View.INVISIBLE);
        imgafternoon7.setVisibility(View.INVISIBLE);
        imgtickafternoon7.setVisibility(View.INVISIBLE);
        imgafternoon8.setVisibility(View.INVISIBLE);
        imgtickafternoon8.setVisibility(View.INVISIBLE);

        Imgafternight1.setVisibility(View.INVISIBLE);
        n_Imgnighttick1.setVisibility(View.INVISIBLE);
        Imgnight2.setVisibility(View.INVISIBLE);
        imgnighttick2.setVisibility(View.INVISIBLE);
        imgnight3.setVisibility(View.INVISIBLE);
        imgnighttick3.setVisibility(View.INVISIBLE);
        imgnight4.setVisibility(View.INVISIBLE);
        imgnighttick4.setVisibility(View.INVISIBLE);
        imgnight5.setVisibility(View.INVISIBLE);
        imgnighttick5.setVisibility(View.INVISIBLE);
        imgnight6.setVisibility(View.INVISIBLE);
        imgtickcnight6.setVisibility(View.INVISIBLE);
        imgnight7.setVisibility(View.INVISIBLE);
        imgticknight7.setVisibility(View.INVISIBLE);
        imgnight8.setVisibility(View.INVISIBLE);
        imgticknight8.setVisibility(View.INVISIBLE);

        night_extra_medicne_count.setVisibility(View.INVISIBLE);
        evening_extra_medicne_count.setVisibility(View.INVISIBLE);
        morning_extra_medicne_count.setVisibility(View.INVISIBLE);
        afternoon_extra_medicne_count.setVisibility(View.INVISIBLE);
    }

    public  void med_rem_reschedule(final String Schedule_id,String scheduled_date) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogview = inflater.inflate(R.layout.med_rem_onitem_click, null);

        try {
            current_date = dateFormat_query.parse(scheduled_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);

        dialogbuilder.setView(dialogview);
        final TimePicker timePkr = (TimePicker) dialogview.findViewById(R.id.timePicker1);
        final NumberPicker nbrselectdays= (NumberPicker) dialogview.findViewById(R.id.numberPicker3);
        nbrselectdays.setVisibility(View.GONE);
        final Button btnok = (Button) dialogview.findViewById(R.id.btnok);
        final Button btncancel = (Button) dialogview.findViewById(R.id.btncancel);
        final Dialog dialog =dialogbuilder.create();

        btnok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    if(picked_time.equals("-99"))
                    {
                        picked_time= dateFormat_query_popup.format(current_date);
                    }
                    db_mr.update_Reschedule_time(Schedule_id,picked_time);
                    db_mr.delete_from_notification_table(Schedule_id);
                    clear_screen();
                    get_daywise_session("m", dateFormat_query.format(current_date));
                    get_daywise_session("n",dateFormat_query.format(current_date));
                    get_daywise_session("e",dateFormat_query.format(current_date));
                    get_daywise_session("a", dateFormat_query.format(current_date));
                    show_message_on_performance(dateFormat_query.format(current_date));
                    show_current_session_area();
                } catch (Exception e) {
                    e.toString();
                }

                dialog.dismiss();

            }


        });

        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dialog.dismiss();
            }


        });
        timePkr.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                String f_minute="";String f_hour="";

                if(minute<10)
                {
                    f_minute="0"+minute;
                }else
                {
                    f_minute=String.valueOf(minute);
                }

                if(hourOfDay<10)
                {
                    f_hour="0"+hourOfDay;
                }else
                {
                    f_hour=String.valueOf(hourOfDay);
                }

                picked_time = dateFormat_reshedule.format(current_date)+" "+f_hour + ":" + f_minute+":00";





            }
        });


        timePkr.setIs24HourView(true);

        dialog.show();


    }

    private  void show_current_session_area()
    {
        night_show_layout=(LinearLayout)findViewById(R.id.night_show_layout);
        morning_show_layout=(LinearLayout)findViewById(R.id.morning_show_layout);
        evening_show_layout=(LinearLayout)findViewById(R.id.evening_show_layout);
        afternoon_show_layout=(LinearLayout)findViewById(R.id.afternoon_show_layout);

        night_show_layout.setVisibility(View.GONE);
        morning_show_layout.setVisibility(View.GONE);
        evening_show_layout.setVisibility(View.GONE);
        afternoon_show_layout.setVisibility(View.GONE);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 6 && timeOfDay < 12){


            morning_show_layout.setVisibility(View.VISIBLE);
            night_show_layout.setVisibility(View.GONE);
            evening_show_layout.setVisibility(View.GONE);
            afternoon_show_layout.setVisibility(View.GONE);

            img_night.setBackgroundResource(R.drawable.nightcircle_shade);
            img_morning.setBackgroundResource(R.drawable.center_morning);
            img_afternoon.setBackgroundResource(R.drawable.aftercircle_shade);
            img_evening.setBackgroundResource(R.drawable.eveingcircle_shade);

        }else if(timeOfDay >= 12 && timeOfDay < 18){

            morning_show_layout.setVisibility(View.GONE);
            night_show_layout.setVisibility(View.GONE);
            evening_show_layout.setVisibility(View.GONE);
            afternoon_show_layout.setVisibility(View.VISIBLE);

            img_night.setBackgroundResource(R.drawable.nightcircle_shade);
            img_morning.setBackgroundResource(R.drawable.morcircle_shade);
            img_afternoon.setBackgroundResource(R.drawable.center_afternoon);
            img_evening.setBackgroundResource(R.drawable.eveingcircle_shade);

        }else if(timeOfDay >= 18 && timeOfDay <= 24){

            morning_show_layout.setVisibility(View.GONE);
            night_show_layout.setVisibility(View.GONE);
            evening_show_layout.setVisibility(View.VISIBLE);
            afternoon_show_layout.setVisibility(View.GONE);

            img_night.setBackgroundResource(R.drawable.nightcircle_shade);
            img_morning.setBackgroundResource(R.drawable.morcircle_shade);
            img_afternoon.setBackgroundResource(R.drawable.aftercircle_shade);
            img_evening.setBackgroundResource(R.drawable.center_evening);


        }else if(timeOfDay >= 1 && timeOfDay < 6){

            morning_show_layout.setVisibility(View.GONE);
            night_show_layout.setVisibility(View.VISIBLE);
            evening_show_layout.setVisibility(View.GONE);
            afternoon_show_layout.setVisibility(View.GONE);

            img_night.setBackgroundResource(R.drawable.center_night);
            img_morning.setBackgroundResource(R.drawable.morcircle_shade);
            img_afternoon.setBackgroundResource(R.drawable.aftercircle_shade);
            img_evening.setBackgroundResource(R.drawable.eveingcircle_shade);

        }

    }
    private void show_message_on_performance(String Sch_DateTime) {





        Double total_taken_count = 0.0;
        Double total_pending_count = 0.0;

        Cursor cursor_session = db_mr.get_med_rem_sch_daywise_session("all", Sch_DateTime,sMemberId);
        int numm = cursor_session.getCount();
        if ((cursor_session != null) || (cursor_session.getCount() > 0)) {


            if (cursor_session.moveToFirst()) {
                do {


                    if(cursor_session.getString(cursor_session.getColumnIndex("status")).equals("T"))
                    {
                        total_taken_count++;
                    }else {
                        total_pending_count++;
                    }


                } while (cursor_session.moveToNext());

            }

            try {
                // pending_all_count=taken_all_count-pending_all_count;

                Double adhenrence_first = (total_taken_count / (total_taken_count+total_pending_count));

                if(adhenrence_first.isInfinite()==true)
                {
                    adhenrence_first=0.0;
                }
                Double adherence_percent = adhenrence_first * 100;

                if(adherence_percent<1 )
                {
                    main_message.setText("Bad !!");
                    second_message.setText("Please start your medicines");
                }
                else if(adherence_percent>0 && adherence_percent<35)
                {
                    main_message.setText("Poor !!");
                    second_message.setText("Take your medicines");
                }else if(adherence_percent>35 && adherence_percent<70)
                {
                    main_message.setText("good !!");
                    second_message.setText("Try to take all medicines on time");
                }
                else if( adherence_percent>70)
                {
                    main_message.setText("Awesome !!");
                    second_message.setText("You doing well");
                }

            }catch (Exception e)
            {}
        }
        cursor_session.close();
    }

    private void add_image_views(LinearLayout lnr,Integer Image_id,Integer f_color_id,Integer s_color_id, LinearLayout.LayoutParams customparam )
    {
        lnr.removeAllViews();
        img_first_part = new ImageView(this);
        img_second_part = new ImageView(this);
        // LinearLayout.LayoutParams param = new LinearLayout.LayoutParams();
        param.gravity=Gravity.CENTER_VERTICAL;
        lnr.setGravity(Gravity.CENTER);
        if(s_color_id!=null)
        {
            if(s_color_id!=-99)
            {
                img_first_part.setLayoutParams(customparam);
                img_second_part.setLayoutParams(customparam);
                img_first_part.setImageResource(R.drawable.med_img_part_frst);
                img_second_part.setImageResource(R.drawable.med_img_part_second);
                img_first_part.setColorFilter(ConstData.colr_array[f_color_id], PorterDuff.Mode.MULTIPLY);
                img_second_part.setColorFilter(ConstData.colr_array[s_color_id], PorterDuff.Mode.MULTIPLY);
                lnr.addView(img_first_part);
                lnr.addView(img_second_part);
            }else
            {
                img_first_part.setImageResource(ConstData.Medicine_array1[Image_id]);
                img_first_part.setColorFilter(ConstData.colr_array[f_color_id], PorterDuff.Mode.MULTIPLY);
                lnr.addView(img_first_part);
            }
        }



    }
    private void set_tag_for_imageviews(LinearLayout lnr, m_medicine_list O_medicine_list )
    {


        lnr.setVisibility(View.VISIBLE);
        lnr.setTag(R.id.key_MemberImage,O_medicine_list.getImd_id());
        lnr.setTag(R.id.key_first_color_id,O_medicine_list.getFirst_color_id());
        lnr.setTag(R.id.key_second_color_id,O_medicine_list.getSecond_color_id());
        lnr.setTag(R.id.key_schedule_id, O_medicine_list.getSchedule_Id());
        lnr.setTag(R.id.key_refill_flag, O_medicine_list.getRefill_flag());
        lnr.setTag(R.id.key_schedule_date, O_medicine_list.getSchedule_date());
        lnr.setTag(R.id.key_schedule_dosage, O_medicine_list.getSchedule_dosage());
        lnr.setTag(R.id.key_medicine_name, O_medicine_list.getMedicine_Name());
        lnr.setTag(R.id.key_medicine_status, O_medicine_list.getSchedule_status());
        lnr.setTag(R.id.key_refill_date, O_medicine_list.getRefill_date());
        lnr.setTag(R.id.key_no_reminder_flag, O_medicine_list.getNo_reminder_flag());



        lnr.setVisibility(View.VISIBLE);
    }

    private  void get_med_friend_data_from_server()
    {
      //  Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, MR_GetMedfriedDataService.class);
       // download_intent.putExtra("member_id",sMemberId);
       // startService(download_intent);
        showPdialog("Loading");

        String url = String.format(AppConfig.URL_GET_MED_FRIEND_DATA, sMemberId);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest datarequest = new JsonObjectRequest(url,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                new SuperTask(MRA_ReminderMain.this,response).execute();
                hidePDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.toString();
                        hidePDialog();
                    }
                });

        queue.add(datarequest);
    }


    public class SuperTask extends AsyncTask<String,String,Void> {
        private final Context mContext;
        private JSONObject ASresponse=null;


        // can use UI thread here
        @Override
        protected void onPreExecute() {
          //  this.dialog.setMessage("Inserting data...");
          //  this.dialog.show();

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        public SuperTask(Context context,JSONObject response) {
            super();
            this.mContext = context;

            this.ASresponse = response;
        }

        protected Void doInBackground(String... params) {
            // using this.mContext
            success_member_Data(ASresponse);
            return null;

        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            refresh_screen();
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
           // super.onPostExecute(result);
        }



    }


    private  void success_member_Data(JSONObject response)
    {
        try {
            JSONArray Med_rem_array = response.getJSONArray("v_cfe_app_med_remider_m");

            for (int i = 0; i < Med_rem_array.length(); i++) {

                JSONObject jsonobject = Med_rem_array.getJSONObject(i);

                db_mr.delete_medreminder_table_on_reminder_id(jsonobject.optString("reminder_id"),jsonobject.optString("MemberId"));

                String refill_flag="0";
                if( jsonobject.optBoolean("RefillRem")==true)
                {
                    refill_flag="1";
                }



                db_mr. addMedRemMaster_insert_from_server(jsonobject.optString("reminder_id"),
                        jsonobject.optString("medicine_id"),
                        jsonobject.optString("medicine_name"),
                        jsonobject.optInt("reminder_type_id"),
                        jsonobject.optString("reminder_value"),
                        jsonobject.optInt("schedule_duration_type_id"),
                        jsonobject.optInt("schedule_duration_value"),
                        jsonobject.optString("schedule_start_date"),
                        jsonobject.optInt("days_intervel_type_id"),
                        jsonobject.optString("days_intervel_value"),
                        jsonobject.optInt("use_placebo"),
                        jsonobject.optInt("image_id"),
                        jsonobject.optInt("first_color_id"),
                        jsonobject.optInt("second_color_id"),
                        jsonobject.optString("instructions"),
                        jsonobject.optString("dosage_type"),
                        jsonobject.optInt("dosage_value"),
                        jsonobject.optString("condition"),
                        jsonobject.optString("doctor_id"),
                        jsonobject.optString("medfriend_id"),
                        jsonobject.optString("MemberId")
                        ,jsonobject.optInt("RelationshipId"),
                        refill_flag,
                        jsonobject.optString("RefillDate"),
                        jsonobject.optString("PackSize"));
            }

        }catch (JSONException e)
        {

        }
        try {
            JSONArray Med_rem_med_master = response.getJSONArray("v_cfe_app_med_reminder_medicine_master");

            for (int i = 0; i < Med_rem_med_master.length(); i++) {

                JSONObject jsonobject = Med_rem_med_master.getJSONObject(i);

                db_mr.Delete_Medicine_master(jsonobject.optString("medicine_id"),jsonobject.optString("MemberId"));

                long id= db_mr.insert_Medicine_master_value(jsonobject.optString("medicine_id"),
                        jsonobject.optString("medicine_name"),
                        jsonobject.optInt("image_id"),
                        jsonobject.optInt("first_color_id"),
                        jsonobject.optInt("second_color_id"),
                        jsonobject.optString("MemberId"),
                        jsonobject.optInt("RelationshipId"),
                        jsonobject.optString("Med_actual_id"));
            }

        }catch (JSONException e)
        {

        }

        try {
            JSONArray Med_rem_med_schedule = response.getJSONArray("v_cfe_app_med_reminder_schedule");

            for (int i = 0; i < Med_rem_med_schedule.length(); i++) {

                JSONObject jsonobject = Med_rem_med_schedule.getJSONObject(i);

                db_mr.delete_med_reminder_schedule_on_schedule_id(jsonobject.optString("schedule_id"),  jsonobject.optString("MemberId"));

                String Date_time_set="",date_time_taken="";

                try {
                    current_date = dateFormat_sql.parse(  jsonobject.optString("datetime_set"));
                    Date_time_set=dateFormat_query_popup.format(current_date);

                    current_date = dateFormat_sql.parse( jsonobject.optString("datetime_taken"));
                    date_time_taken=dateFormat_query_popup.format(current_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                db_mr.insert_med_reminder_schedule(  jsonobject.optString("schedule_id"),
                        jsonobject.optString("reminder_id"),
                        jsonobject.optString("STATUS"),
                        Date_time_set,
                        date_time_taken,
                        jsonobject.optInt("sequence"),
                        jsonobject.optString("dosage_type"),
                        jsonobject.optDouble("dosage"),
                        jsonobject.optDouble("quantity"),
                        jsonobject.optString("MemberId"),
                        jsonobject.optInt("RelationshipId"));
            }


        }catch (JSONException e)
        {

        }

    }

    private void show_invite_request(String msg,final String req_sender_memberid)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.med_friend_invite_dialog, null);
        builder.setView(dialogview);
        final Dialog dlg = builder.create();

        final TextView txt_msg = (TextView)dialogview.findViewById(R.id.txt_msg);
        final Button btn_accept = (Button)dialogview.findViewById(R.id.btn_accept);
        final Button btn_rject = (Button)dialogview.findViewById(R.id.btn_rject);

        txt_msg.setText(msg);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Accept_Reject_invite(req_sender_memberid,"Accept");
                dlg.cancel();
            }
        });
        btn_rject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Accept_Reject_invite(req_sender_memberid,"Reject");
                dlg.cancel();
            }
        });

        dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dlg.show();

    }

    private void show_invite_request_accept(String msg,final String req_sender_memberid)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.med_friend_invite_dialog, null);
        builder.setView(dialogview);
        final Dialog dlg = builder.create();

        final TextView txt_msg = (TextView)dialogview.findViewById(R.id.txt_msg);
        final Button btn_accept = (Button)dialogview.findViewById(R.id.btn_accept);
        final Button btn_rject = (Button)dialogview.findViewById(R.id.btn_rject);

        txt_msg.setText(msg);

        btn_accept.setVisibility(View.GONE);
        btn_rject.setVisibility(View.GONE);

        dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dlg.show();

    }
    private void Accept_Reject_invite(String req_sender_memberid,String flag) {
        String url="";
        if(flag.equals("Accept"))
        {
            url  = String.format(AppConfig.URL_GET_ACCEPT_INVITE, sMemberId, req_sender_memberid);
        } else if(flag.equals("Reject"))
        {
            url  = String.format(AppConfig.URL_GET_REJECT_INVITE, sMemberId, req_sender_memberid);
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

    private void select_time(final String Schedule_id,String scheduled_date)
    {
        try {
            current_date = dateFormat_query.parse(scheduled_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();

        dpd = TimePickerDialog.newInstance(
                this,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
        );


        Bundle b= new Bundle();
        b.putString(SCHEDULED_DATE,dateFormat_reshedule.format(current_date));
        b.putString(SCHEDULED_ID,Schedule_id);
        dpd.setArguments(b);
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }
    public String getTime_Format(int Hour, int Minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, Hour);
        cal.set(Calendar.MINUTE, Minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return  time_format.format(cal.getTime());
    }

    private void show_medfriends_dialog()
    {

        MemberData.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        builder.setView(dialogview);
        final Dialog dialog = builder.create();

        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);
        final TextView Titile =(TextView)dialogview.findViewById(R.id.textView);
        final TextView btncancel=(TextView)dialogview.findViewById(R.id.btncancel);

        memberadapter=new AD_memberAdapter(this,MemberData,sMemberId);
        lv.setAdapter(memberadapter);

        Cursor cursor_medfriend =   db_mr.get_all_pill_buddyr(sMemberId);

        M_memberlist memberdetails = new M_memberlist();

        memberdetails.setMemberId(Integer.parseInt(pref.getString("memberid", "")) );
        memberdetails.setMemberName(pref.getString("UserName",""));
        memberdetails.setMemberGender(pref.getString("MemberGender", ""));
        memberdetails.setRelationshipId(8);
        memberdetails.setMemberDOB(pref.getString("MemberDOB", ""));
        memberdetails.setImageurl(pref.getString("imagename",""));
        memberdetails.setPatientId(pref.getString("memberid", ""));
        MemberData.add(memberdetails);

        if ((cursor_medfriend != null) || (cursor_medfriend.getCount()> 0))
        {
            if (cursor_medfriend.moveToFirst()) {
                do {


                    memberdetails = new M_memberlist();

                    memberdetails.setMemberId(cursor_medfriend.getInt(cursor_medfriend.getColumnIndex("medfriend_id")) );
                    memberdetails.setMemberName(cursor_medfriend.getString(cursor_medfriend.getColumnIndex("reminder_friendname")));
                    memberdetails.setRelationshipId(8);
                    memberdetails.setImageurl(cursor_medfriend.getString(cursor_medfriend.getColumnIndex("reminder_image_name")));

                    memberdetails.setPatientId(cursor_medfriend.getString(cursor_medfriend.getColumnIndex("medfriend_id")));
                    MemberData.add(memberdetails);



                } while (cursor_medfriend.moveToNext());

            }
        }

        cursor_medfriend.close();
        Titile.setText("MedFriends");

        btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                   sMemberId=(String)v.getTag(R.id.key_RelationShipId);
                if(!sMemberId.equals("-99")) {
                    current_date = Calendar.getInstance().getTime();
                    txt_date.setText(dateFormat.format(current_date));
                    clear_screen();
                    get_daywise_session("m", dateFormat_query.format(current_date));
                    get_daywise_session("n", dateFormat_query.format(current_date));
                    get_daywise_session("e", dateFormat_query.format(current_date));
                    get_daywise_session("a", dateFormat_query.format(current_date));
                    show_message_on_performance(dateFormat_query.format(current_date));
                    ImageLoad((String) v.getTag(R.id.key_MemberName), (String) v.getTag(R.id.key_MemberImage));
                    show_current_session_area();
                    get_med_friend_data_from_server();

                    dialog.dismiss();

                    hidePDialog();
                }else
                {
                    Toast.makeText(MRA_ReminderMain.this, "Friend request pending", Toast.LENGTH_LONG).show();
                }

            }
        });
        memberadapter.notifyDataSetChanged();
        dialog.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
  public  void refresh_screen()
  {
      current_date = Calendar.getInstance().getTime();
      clear_screen();
      get_daywise_session("m", dateFormat_query.format(current_date));
      get_daywise_session("n",dateFormat_query.format(current_date));
      get_daywise_session("e",dateFormat_query.format(current_date));
      get_daywise_session("a", dateFormat_query.format(current_date));
      show_message_on_performance(dateFormat_query.format(current_date));
      show_current_session_area();
  }

    BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh_screen();


        }
    };
    BroadcastReceiver InvitebroadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh_screen();

            show_invite_request(intent.getExtras().getString("msg"),intent.getExtras().getString("req_sender_memberid"));
        }
    };

    BroadcastReceiver AcceptedbroadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh_screen();

            show_invite_request_accept(intent.getExtras().getString("msg"),intent.getExtras().getString("req_sender_memberid"));
        }
    };
    private void start_layout_animation()
    {
        rl_main.animate()
                .translationY(0)
                .alpha(0.0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        rl_main.animate()  .alpha(1.0f);
                    }
                });
    }

    public void onDestroy() {

        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(InvitebroadcastReceiver);
        unregisterReceiver(AcceptedbroadcastReceiver);

    }


}
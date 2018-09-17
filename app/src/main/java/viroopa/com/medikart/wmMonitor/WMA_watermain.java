package viroopa.com.medikart.wmMonitor;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.github.siyamed.shapeimageview.CircularImageView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.itangqi.waveloadingview.WaveLoadingView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.bpmonitor.BPA_MonitorSetting;
import viroopa.com.medikart.common.Change_member_Dialog;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.common.numerdialog;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteWMHandler;
import viroopa.com.medikart.wmMonitor.WMCommon.WM_SetGoal_Dialog;
import viroopa.com.medikart.wmMonitor.adapter.AD_cup_selectAdapter;
import viroopa.com.medikart.wmMonitor.adapter.CoverFlow;


public class WMA_watermain extends AppCompatActivity implements  Change_member_Dialog.OnMemberSelectListener,
        WM_SetGoal_Dialog.OnWaterGoalDialogDoneListener,
        numerdialog.OnNumberDialogDoneListener{

    private Integer glassSize=250;
    private String date;
    private String sMemberId;
    private String setting_weight_unit,setting_activity_level,setting_enable_sound;
    String sUnit = "kg";
    private EditText watertxt;
    private SqliteWMHandler db;
    private SQLiteHandler db_all;
    private ProgressDialog pDialog;
    private Integer waterUnit,Actualweight,WateActualUnit;
    private Button  set;
    private ImageView drink,empty;
    private WaveLoadingView mWaveLoadingView;
    private AD_cup_selectAdapter wmCupSizeAdapter;
    private Boolean LitreSAelected=false;
    private Boolean water_unit_changed=false;
    private  String selected_water_unit="ml";
    private  Menu objMemberMenu;
    private SharedPreferences pref ;
    private Integer water_drink;
    private TextView drink_percnt,goal_show_txt,weighttxt;
    Integer repeat ;
    Integer quantity;
    Double Drinkpercent=0.0;
    AlertDialog dialogAnim;
    private  ImageView mAnimLogo;
    ArrayAdapter<String> adapter;
    Integer counter;
    Integer DrinkCount=0;
    AppController globalVariable;
    private  String  RelMemberNAme,RelMemberImagePath;
    private ImageView imgCup;
    private  Animation in,out;
    private Animation text_animation;
    List<String> cup_sizes = Arrays.asList(new String[] {"250 ml","300 ml","350 ml","500 ml","750 ml","1000 ml"});

    Date current_date = Calendar.getInstance().getTime();
    Date Today_date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");

    private   AnimationSet as;
    Integer relationShipId=8;
    private LayoutInflater inflater ;

    private static String LITRE_SELECTED="litre_selected";

    private TextView cup_size_show_txt,goal_show_litre_txt,icons_on_water,minus_count,plus_count,date_txt;
    private  ImageView img_previous_date,img_next_date,main_img_background;
    CoverFlow coverFlow;

    private SoundPool soundPool;
    private int soundID_drink,soundID_empty;
    boolean plays = false, loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_activity_watermain);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        inflater = LayoutInflater.from(this);
        db =  SqliteWMHandler.getInstance(this);
        db_all=SQLiteHandler.getInstance(this);
        globalVariable = (AppController) getApplicationContext();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);

        load_sound();

        cancel_notification();
        initialize_bottom_bar();
        initialize_controls();


        text_animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);


        initialize_animations();
        initialize_date();

        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        getIntenet();
        load_selected_data();
        img_next_date.setVisibility(View.GONE);

        imgCup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Glass_list_select();

            }
        });



        drink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    check_next_drink_entry(quantity);

                    minus_count.startAnimation(text_animation);
                    plus_count.startAnimation(text_animation);

                }catch (Exception e)
                {

                }

            }
        });


        empty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                empty();
                minus_count.startAnimation(text_animation);
                plus_count.startAnimation(text_animation);

            }
        });

        img_previous_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                current_date=date_addDays(current_date,-1);
                date=dateFormat_query.format(current_date);
                MakeRandomAndCheckDate();
                img_next_date.setVisibility(View.VISIBLE);
                date_over_all_change(date);
                date_txt.setText(dateFormat.format(current_date));



            }
        });
        img_next_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                current_date=date_addDays(current_date,1);
                date=dateFormat_query.format(current_date);
                MakeRandomAndCheckDate();
                date_over_all_change(date);
                date_txt.setText(dateFormat.format(current_date));
                if(dateFormat_query.format(current_date).equals(dateFormat_query.format(Today_date)))
                {
                    img_next_date.setVisibility(View.GONE);
                }else
                {
                    img_next_date.setVisibility(View.VISIBLE);
                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_watermain, menu);
        this.objMemberMenu=menu;
        View mCustomView = inflater.inflate(R.layout.circula_image, null);
        objMemberMenu.findItem(R.id.circlularImage).setActionView(mCustomView);

        pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        ImageLoad(pref.getString("UserName",""),pref.getString("imagename",""));
        return true;
    }

    @Override
    public void onSelectMember(String Relationship_id, String name,String Imagename)
    {
        globalVariable.setRealationshipId(Relationship_id);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserName", name);
        editor.commit();
        check_data_availabity(Integer.parseInt(Relationship_id));



        ImageLoad(name,Imagename);
    }
    private  void ImageLoad(String name,String imageName)
    {
        View o_view=objMemberMenu.findItem(R.id.circlularImage).getActionView();
        CircularImageView crImage = (CircularImageView)o_view.findViewById(R.id.imgView_circlularImage);
        TextView txtmemberName = (TextView)o_view.findViewById(R.id.txtUsername);

        crImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Change_member_Dialog dpd = Change_member_Dialog.newInstance("crImage");
                dpd.show(getFragmentManager(), "Change_member_Dialog");
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent_setting = new Intent(WMA_watermain.this, WMA_Settings
                    .class);
            startActivity(intent_setting);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void drink()
    {

        try {

            water_drinking_sound();
            db.addWMEntries(Integer.parseInt(sMemberId), date, String.valueOf(quantity), relationShipId, globalVariable.getUUIDforWM(), globalVariable.getIMEI(), String.valueOf(Drinkpercent));
            SavedataToServerWmEntries("0", sMemberId, date, String.valueOf(quantity), String.valueOf(relationShipId), String.valueOf(globalVariable.getUUIDforWM()), globalVariable.getIMEI(), "A");

            percentge_Calculation();
            if(Drinkpercent>=100.0 && Drinkpercent<=110.0)
            {


            }
            else if(Drinkpercent>125)
            {

                icons_on_water.startAnimation(as);
                icons_on_water.setText("Over Drinking");
                icons_on_water.startAnimation(in);

            }
            String total= db.getTotalDrink_quantity(date,relationShipId);
            if(total==null)
            {
                mWaveLoadingView.setBottomTitle("0"+" ml");
            }else {
                mWaveLoadingView.setBottomTitle(total + " ml");
            }



        }catch(Exception e)
        {
            e.toString();
        }

    }
    private void getIntenet()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

        if(globalVariable.getRealationshipId()!=null)
        {
            relationShipId=Integer.parseInt(globalVariable.getRealationshipId());
        }else
        {
            relationShipId=8;
        }

        if(RelMemberNAme!=null && RelMemberImagePath!=null)
        {
            globalVariable.setMemberName(RelMemberNAme);
            globalVariable.setMemberImage(RelMemberImagePath);
        }


        calc();
        MakeRandomAndCheckDate();
        DrinkCount=db.getDrinkCountofWM_Entries(date, relationShipId);

        Integer wtrrGoal=1000;


        String MaxId=db.getMAxIdWMSetting(relationShipId);
        if(MaxId!=null) {
            wtrrGoal = db.getLastSetGoalonDate(relationShipId,date);//db.getLastSetGoal(Integer.parseInt(MaxId));
            water_drink=wtrrGoal;
        }

        if(wtrrGoal==null)
        {   goal_show_txt.setText("1");
            goal_show_litre_txt.setText("Litre");}
        else {
            /*goal_show_txt.setText(String.valueOf(wtrrGoal / 1000.0));
            goal_show_litre_txt.setText("Litre");*/

            litre_ml_selection(wtrrGoal);

        }

        percentge_Calculation();
        String total= db.getTotalDrink_quantity(date,relationShipId);
        if(total==null)
        {
            mWaveLoadingView.setBottomTitle("0"+ " ml");
        }else {
            mWaveLoadingView.setBottomTitle(total + " ml");
        }



    }




    private void savedata()
    {

        if(LitreSAelected==true) {
            goal_show_txt.setText(String.valueOf(WateActualUnit / 1000.0));
            goal_show_litre_txt.setText("Litre");
        }else
        {
            goal_show_txt.setText(String.valueOf(WateActualUnit));
            goal_show_litre_txt.setText("ml");
        }
        percentge_Calculation();
    }





    public void calc()
    {
        String MaxId=db.getMAxIdWMSetting(relationShipId);
        if(MaxId!=null) {
            glassSize=db.getLastSetGlassSize(relationShipId,date);
            water_drink = db.getLastSetGoal(relationShipId,date);

        }



        if(glassSize==null||glassSize==0) {
            glassSize = 250;
        }
        repeat = water_drink / glassSize;
        quantity = glassSize;

    }


    private void empty()
    {
        String MaxQid= db.getMAxIdWMEntries(relationShipId,date);
        DrinkCount=db.getDrinkCountofWM_Entries(date, relationShipId);
        if(DrinkCount>0) {

            water_empty_sound();

            db.delete_entries(MaxQid);

            SavedataToServerWmEntries(MaxQid, sMemberId, date, String.valueOf(quantity), String.valueOf(relationShipId), String.valueOf(globalVariable.getUUIDforWM()), globalVariable.getIMEI(), "D");

            String total= db.getTotalDrink_quantity(date,relationShipId);
            percentge_Calculation();
            if(total==null)
            {
                mWaveLoadingView.setBottomTitle("0"+" ml");
            }else {
                mWaveLoadingView.setBottomTitle(total + " ml");
            }
        }
    }





    public  void water_drinking_sound()
    {


       // MediaPlayer mp = MediaPlayer.create(this, R.raw.water_drinking);
        try {
            if(!setting_enable_sound.equals("true"))
            {
               /* if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                    mp = MediaPlayer.create(this, R.raw.water_drinking);
                } mp.start();*/
                stopSound(soundID_drink);
                playSound(soundID_drink);

            }
        } catch(Exception e) {
            e.printStackTrace(); }

    }


    public  void water_empty_sound()
    {
        //MediaPlayer mp = MediaPlayer.create(this, R.raw.water_empty_sound);
        try {
            if(!setting_enable_sound.equals("true"))
            {

               /* if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                    mp = MediaPlayer.create(this, R.raw.water_empty_sound);
                } mp.start();*/

                stopSound(soundID_empty);
                playSound(soundID_empty);

            }
        } catch(Exception e) { e.printStackTrace(); }


    }

    public void cup_size_change(Integer GlassSize)
    {
        String MaxId=db.getMAxIdWMSetting(relationShipId);
        quantity=GlassSize;
        db.update_GlassSize(GlassSize, date, relationShipId);
        percentge_Calculation();
        cup_size_show_txt.setText(String.valueOf(GlassSize) + " ml");

    }

    public void chart()
    {
        Intent Intenet_edit_profile = new Intent(this, WMA_BarChart.class);
        startActivity(Intenet_edit_profile);
    }

    private void goal_change(Integer CurrentGoal)

    {
        //water_drink
        repeat = CurrentGoal / glassSize;
        DrinkCount=db.getDrinkCountofWM_Entries(date, relationShipId);

        repeat=repeat-DrinkCount;
        quantity = glassSize;

        db.update_percentage(String.valueOf(Drinkpercent), date, relationShipId);

    }



    private void MakeRandomAndCheckDate()
    {
        Cursor cursor = db.getDateUuidWMSetting(date,relationShipId);
        if (cursor.moveToFirst()){
            do{
                globalVariable.setUUIDforWM(cursor.getString(cursor.getColumnIndex("main_uuid")));
            }while(cursor.moveToNext());
        }
        if(cursor.getCount()<=0)
        {
            String MaxId=db.getMAxIdWMSetting(relationShipId);
            if(MaxId==null)
            {
                Integer settingTableCount=db.getRowCountofWM_Setting_table(relationShipId);

                if(settingTableCount<=0) {
                    String sradomstring = UUID.randomUUID().toString();
                    globalVariable.setUUIDforWM(sradomstring);
                    db.addWMSetting(Integer.parseInt(sMemberId), sradomstring, 1000, date, globalVariable.getIMEI(), relationShipId,glassSize);
                    SavedataToServerWMSettings(sMemberId,date,"1000",String.valueOf(relationShipId),sradomstring,globalVariable.getIMEI(), "A",glassSize);
                }
            }else{
                Integer waterGoal=db.getLastSetGoal(relationShipId);
                String sradomstring = UUID.randomUUID().toString();
                globalVariable.setUUIDforWM(sradomstring);
                db.addWMSetting(Integer.parseInt(sMemberId),sradomstring, waterGoal, date, globalVariable.getIMEI(), relationShipId,glassSize);
                SavedataToServerWMSettings(sMemberId, date, String.valueOf(waterGoal), String.valueOf(relationShipId), sradomstring,globalVariable.getIMEI(), "A",glassSize);
            }
        }
        cursor.close();
    }

    private void SavedataToServerWmEntries(String id,String Member_id,String Created_date,String Quantity,String RelationShip_id,String UUID,String IMEI,String Mode) {
        try{

            String MaxId=db.getMAxIdWMSetting(relationShipId);



            Map<String, String> params = new HashMap<String, String>();
            if(id.equals("0")) {
                params.put("id", MaxId);
            }else{
                params.put("id", id);
            }
            params.put("member_id",Member_id);
            params.put("created_date", Created_date);
            params.put("quantity",Quantity);
            params.put("Relationship_ID",RelationShip_id);
            params.put("UUID", UUID);
            params.put("IMEI",IMEI );
            params.put("Mode", Mode);

            JSONObject jparams = new JSONObject(params);
            String I_Type="Post";
            String Controller= AppConfig.URL_POST_WATERENTRY;
            String Parameter="";
            String JsonObject=String.valueOf(jparams);
            String Created_Date=Created_date;

            String F_KEY_UPLOAD_DOWNLOAD="true";
            Integer F_KEY_SYNCMEMBERID=Integer.parseInt(Member_id);
            String F_KEY_MODULE_NAME="WE";
            String F_KEY_MODE="A";
            String F_KEY_ControllerName="Monitor";
            String F_KEY_MethodName="SaveWEMonitor";
            db.InsertSyncTable(I_Type, Controller, Parameter,
                    JsonObject, Created_Date, UUID, F_KEY_UPLOAD_DOWNLOAD, F_KEY_SYNCMEMBERID,
                    F_KEY_MODULE_NAME, F_KEY_MODE, F_KEY_ControllerName, F_KEY_MethodName);



            //f_alert_ok("Sucess", "Entry Saved");


        }
        catch ( Exception E )
        {
            // f_alert_ok("Error","Error"+E.getMessage());
        }

    }
    private void SavedataToServerWMSettings(String Member_id,String Created_date,String GoalSet,String RelationShip_id,String UUID,String IMEI,String Mode,Integer Glass_size) {
        try{

            Map<String, String> params = new HashMap<String, String>();


            params.put("member_id",Member_id);
            params.put("main_uuid", UUID);
            params.put("Goal_set",GoalSet);
            params.put("created_date", Created_date);
            params.put("IMEI_main",IMEI );
            params.put("Relationship_ID",RelationShip_id);
            params.put("Mode", Mode);
            params.put("glass_size", Glass_size.toString());

            JSONObject jparams = new JSONObject(params);
            String I_Type="Post";
            String Controller= AppConfig.URL_POST_WATERSETTING;
            String Parameter="";
            String JsonObject=String.valueOf(jparams);
            String Created_Date=Created_date;

            String F_KEY_UPLOAD_DOWNLOAD="true";
            Integer F_KEY_SYNCMEMBERID=Integer.parseInt(Member_id);
            String F_KEY_MODULE_NAME="WS";
            String F_KEY_MODE="A";
            String F_KEY_ControllerName="Monitor";
            String F_KEY_MethodName="SaveWSMonitor";
            db.InsertSyncTable(I_Type, Controller, Parameter,
                    JsonObject, Created_Date, UUID, F_KEY_UPLOAD_DOWNLOAD, F_KEY_SYNCMEMBERID,
                    F_KEY_MODULE_NAME, F_KEY_MODE, F_KEY_ControllerName, F_KEY_MethodName);



            //f_alert_ok("Sucess", "Entry Saved");


        }
        catch ( Exception E )
        {
            // f_alert_ok("Error","Error"+E.getMessage());
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





    private void animation(Double drinkpercnt)
    {

        int i = drinkpercnt.intValue();
        mWaveLoadingView.setProgressValue(i);

        if(drinkpercnt==0) {


            icons_on_water.startAnimation(as);
            icons_on_water.setText("Have your First Glass of water for the day");
            icons_on_water.startAnimation(in);

            //main_img_background.setBackgroundResource(R.drawable.drop20);
            // mAnimLogo.setBackgroundResource(R.drawable.drop20);
        } else  if(drinkpercnt>00.00 && drinkpercnt<=10.00 ) {


            icons_on_water.startAnimation(as);
            icons_on_water.setText("Nice");
            icons_on_water.startAnimation(in);



        }else  if(drinkpercnt>10.00 && drinkpercnt<=20.00) {



            icons_on_water.startAnimation(as);
            icons_on_water.setText("Nice");
            icons_on_water.startAnimation(in);


        }else  if(drinkpercnt>20.00 && drinkpercnt<=30.00) {


            icons_on_water.startAnimation(as);
            icons_on_water.setText("Good");
            icons_on_water.startAnimation(in);


        }else  if(drinkpercnt>30.00 && drinkpercnt<=40.00 ) {
            icons_on_water.startAnimation(as);
            icons_on_water.setText("Good");
            icons_on_water.startAnimation(in);


        }else  if(drinkpercnt>40.00 && drinkpercnt<=50.00 ) {
            icons_on_water.startAnimation(as);
            icons_on_water.setText("Better");
            icons_on_water.startAnimation(in);



        }else  if(drinkpercnt>50.00 && drinkpercnt<=60.00) {
            icons_on_water.startAnimation(as);
            icons_on_water.setText("Better");
            icons_on_water.startAnimation(in);

        }else  if(drinkpercnt>60.00 && drinkpercnt<=70.00) {

            icons_on_water.startAnimation(as);
            icons_on_water.setText("Very Good");
            icons_on_water.startAnimation(in);


        }else  if(drinkpercnt>70.00 && drinkpercnt<=80.00) {
            icons_on_water.startAnimation(as);
            icons_on_water.setText("Very Good");
            icons_on_water.startAnimation(in);

        }else  if(drinkpercnt>80.00 && drinkpercnt<=90.00) {

            icons_on_water.startAnimation(as);
            icons_on_water.setText("Awesome");
            icons_on_water.startAnimation(in);


        }else  if(drinkpercnt>90.00 && drinkpercnt<=100.00) {

            icons_on_water.startAnimation(as);
            icons_on_water.setText("Awesome");
            icons_on_water.startAnimation(in);


        }

        else  if(drinkpercnt>=100.00 &&  drinkpercnt<=124.00) {

            icons_on_water.startAnimation(as);
            icons_on_water.setText("Completed");
            icons_on_water.startAnimation(in);


        }
        else  if(drinkpercnt>125.00) {

            icons_on_water.startAnimation(as);
            icons_on_water.setText("Over Drinking");
            icons_on_water.startAnimation(in);


        }





    }



    private void Glass_list_select()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.dialog_cup_select, null);


        coverFlow = (CoverFlow)dialogview.findViewById(R.id.coverFlow);

        coverFlow.setAdapter(new ImageAdapter(this));

        ImageAdapter coverImageAdapter =  new ImageAdapter(this);

        coverImageAdapter.createReflectedImages();

        coverFlow.setAdapter(coverImageAdapter);

        coverFlow.setSpacing(25);
        coverFlow.setSelection(cup_sizes.indexOf(cup_size_show_txt.getText().toString()), true);
        coverFlow.setAnimationDuration(1000);
        coverFlow.setMaxRotationAngle(50);


        builder.setView(dialogview);
        final Dialog dlg= builder.create();

        dialogview. findViewById(R.id.btncancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (coverFlow.getSelectedItemPosition() == position) {
                    imgCup.setImageResource(ConstData.gallery_cup_images[position]);
                    if (position == 0) {

                        cup_size_change(250);
                        dlg.dismiss();
                    } else if (position == 1) {

                        cup_size_change(300);
                        dlg.dismiss();
                    } else if (position == 2) {

                        cup_size_change(350);

                        dlg.dismiss();
                    } else if (position == 3) {

                        cup_size_change(500);


                        dlg.dismiss();
                    } else if (position == 4) {

                        cup_size_change(750);

                        dlg.dismiss();
                    } else if (position == 5) {

                        cup_size_change(1000);


                        dlg.dismiss();
                    }
                }
            }
        });


        coverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  view.setBackgroundColor(getResources().getColor(R.color.sky_blue));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // coverFlow.setMaxZoom(-200);


       /* lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                imgCup.setImageResource(ConstData.gallery_cup_images[position]);
                if (position == 0) {

                    cup_size_change(250);
                    dlg.dismiss();
                } else if (position == 1) {

                    cup_size_change(300);
                    dlg.dismiss();
                }else if(position==2)
                {

                    cup_size_change(350);

                    dlg.dismiss();
                }else if(position==3)
                {

                    cup_size_change(500);


                    dlg.dismiss();
                }else if(position==4)
                {

                    cup_size_change(750);

                    dlg.dismiss();
                }else if(position==5)
                {

                    cup_size_change(1000);


                    dlg.dismiss();
                }

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                dlg.dismiss();
            }
        });*/
        dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dlg.show();
    }

    private void percentge_Calculation()
    {
        Integer PerGoal=0;
        String MaxId=db.getMAxIdWMSetting(relationShipId);
        if(MaxId!=null) {
            PerGoal = db.getLastSetGoal(relationShipId,date);
            glassSize=db.getLastSetGlassSize(relationShipId,date);
            quantity=glassSize;
            String Pertotal= db.getTotalDrink_quantity(date,relationShipId);
            cup_size_show_txt.setText(String.valueOf(glassSize) + " ml");


            imgCup.setImageResource(ConstData.gallery_cup_images[cup_sizes.indexOf(String.valueOf(glassSize) + " ml")]);
            if(Pertotal==null)
            {
                get_pending_and_drink_count(PerGoal,"0",glassSize);
                Drinkpercent=0.0;
                drink_percnt.setText("0.0 "+"%");
                animation(Drinkpercent);
            }else {
                get_pending_and_drink_count(PerGoal,Pertotal,glassSize);
                Drinkpercent = (( Double.parseDouble(Pertotal)/ PerGoal)) * 100;
                drink_percnt.setText(String.format("%.0f", Drinkpercent)+"%");
                animation(Drinkpercent);
            }



        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void get_pending_and_drink_count(Integer Goal,String Drunk_quantity,Integer Glass_size)
    {

        Integer drink_Count =db.getdrink_count(relationShipId,date); //Math.ceil((double)Integer.parseInt(Drunk_quantity) / Glass_size);
        if(drink_Count<10)
        {
            plus_count.setText(" "+drink_Count.toString()); //plus_count.setText(" "+String.format("%.0f",drink_Count));
        }else {
            plus_count.setText(drink_Count.toString());// plus_count.setText(String.format("%.0f",drink_Count));
        }

        Double  f_1=(double)Goal/Glass_size;
        Double f_2=Double.parseDouble(Drunk_quantity)/Glass_size;

        Double pending_drink_Count=Math.ceil(f_1-f_2);

        if(pending_drink_Count<0.0 || pending_drink_Count==-0.0)
        { minus_count.setText(" 0");}else {
            if(pending_drink_Count<10.0)
            {
                minus_count.setText(" "+String.format("%.0f",pending_drink_Count));
            }else {
                minus_count.setText(String.format("%.0f",pending_drink_Count));
            }

        }

    }

    private void date_over_all_change(String Date_changed)
    {

        DrinkCount=db.getDrinkCountofWM_Entries(Date_changed, relationShipId);
        Integer wtrrGoal=1000;


        String MaxId=db.getMAxIdWMSetting(relationShipId);
        if(MaxId!=null) {
            wtrrGoal = db.getLastSetGoalonDate(relationShipId,Date_changed);
            water_drink=wtrrGoal;
        }

        if(wtrrGoal==null)
        {   goal_show_txt.setText("1");
            goal_show_litre_txt.setText("Litre");}else {
           /* goal_show_txt.setText(String.valueOf(wtrrGoal / 1000.0));
            goal_show_litre_txt.setText("Litre");*/
            litre_ml_selection(wtrrGoal);
        }

        percentge_Calculation();

        String total= db.getTotalDrink_quantity(Date_changed,relationShipId);
        if(total==null)
        {
            mWaveLoadingView.setBottomTitle("0 " + " ml");
        }else {
            mWaveLoadingView.setBottomTitle(total + " ml");
        }
    }
    public Date date_addDays(Date date, int days)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }



    private void initialize_controls()
    {
        mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        // lnr_view_progress= (Button) findViewById(R.id.lnr_view_progress);
        cup_size_show_txt=(TextView) findViewById(R.id.cup_size_show_txt);
        goal_show_litre_txt=(TextView) findViewById(R.id.goal_show_litre_txt);
        icons_on_water=(TextView) findViewById(R.id.icons_on_water);
        minus_count=(TextView) findViewById(R.id.minus_count);
        plus_count=(TextView) findViewById(R.id.plus_count);
        img_previous_date=(ImageView) findViewById(R.id.img_previous_date);
        img_next_date=(ImageView) findViewById(R.id.img_next_date);
        date_txt=(TextView) findViewById(R.id.date_txt);
        //lnr_set_goal=(Button) findViewById(R.id.lnr_set_goal);
        main_img_background=(ImageView) findViewById(R.id.background);
        drink=(ImageView)findViewById(R.id.image_add);
        goal_show_txt=(TextView)findViewById(R.id.goal_show_txt);
        drink_percnt=(TextView)findViewById(R.id.textView38);
        empty=(ImageView)findViewById(R.id.textView109);
        mAnimLogo=(ImageView)findViewById(R.id.loading_image);
        imgCup=(ImageView)findViewById(R.id.imgCup);

    }
    private void initialize_animations()
    {

        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(300);

        out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(300);

        as = new AnimationSet(true);
        as.addAnimation(out);
        in.setStartOffset(300);
        as.addAnimation(in);

    }
    private  void initialize_date()
    {
        date= dateFormat_query.format(current_date);
        date_txt.setText(dateFormat.format(current_date));

    }

    private void initialize_bottom_bar()
    {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home_icon_white, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Set Goal", R.drawable.setgoal_white, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("View Progress", R.drawable.report_white, R.color.colorPrimary);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));

        bottomNavigation.setAccentColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setUseElevation(true);

        bottomNavigation.setForceTitlesDisplay(true);

        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.colorPrimary));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {

                if(position==0 )
                {
                    finish();
                }else
                if(position==1)
                {
                    WM_SetGoal_Dialog myDiag = WM_SetGoal_Dialog.newInstance(Integer.parseInt(sMemberId), relationShipId,dateFormat_query.format(current_date));
                    myDiag.show(getFragmentManager(), "Diag");
                }else
                if(position==2)
                {
                    chart();
                }

            }
        });
    }

    @Override
    public void OnWaterGoalChange(int value, boolean isDatasave,boolean litre_selected){

        if(isDatasave)
        {
            WateActualUnit=value;
            LitreSAelected=litre_selected;
            goal_change(WateActualUnit);
            SharedPreferences.Editor edt= pref.edit();
            edt.putBoolean(LITRE_SELECTED,LitreSAelected);
            edt.commit();
            savedata();

        }
    }

    @Override
    public void onDone(int value,String sClass) {
        if(getFragmentRefreshListener()!=null){
            getFragmentRefreshListener().onRefresh(sClass,Integer.toString(value));
        }

    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }
    public interface FragmentRefreshListener{
        void onRefresh(String p_object, String p_value);
    }

    private FragmentRefreshListener fragmentRefreshListener;

    private void check_data_availabity(Integer rel_id)
    {
        String count=  db.check_water_entry(rel_id);
        if(!count.equals("0"))
        {
            getIntenet();
        }else
        {
            Intent Intenet_edit_profile = new Intent(this, WMA_Welcome.class);
            startActivity(Intenet_edit_profile);
            finish();
        }
    }

    private void load_selected_data() {
        String setting_name = "";
        Cursor cursor_session =  db_all.getAllSetting_data(String.valueOf(relationShipId), sMemberId, "3");

        if (cursor_session.moveToFirst()) {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("wm_weight_unit")) {
                    setting_weight_unit = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("def_activity_level")) {
                    setting_activity_level = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("Enable_Sound")) {
                    setting_enable_sound = cursor_session.getString(cursor_session.getColumnIndex("value"));

                }



            } while (cursor_session.moveToNext());
        }
        if(setting_enable_sound==null)
        {
            setting_enable_sound="";
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        load_selected_data();

    }
    public class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;

        private FileInputStream fis;



        private ImageView[] mImages;

        public ImageAdapter(Context c) {
            mContext = c;
            mImages = new ImageView[ ConstData.gallery_cup_images.length];
        }
        public boolean createReflectedImages() {
            //The gap we want between the reflection and the original image
            final int reflectionGap = 4;


            int index = 0;
            for (int imageId :  ConstData.gallery_cup_images) {
                Bitmap originalImage = BitmapFactory.decodeResource(getResources(),
                        imageId);
                int width = originalImage.getWidth();
                int height = originalImage.getHeight();


                //This will not scale but will flip on the Y axis
                Matrix matrix = new Matrix();
                matrix.preScale(1, -1);

                //Create a Bitmap with the flip matrix applied to it.
                //We only want the bottom half of the image
                Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);


                //Create a new bitmap with same width but taller to fit reflection
                Bitmap bitmapWithReflection = Bitmap.createBitmap(width
                        , (height + height/2), Bitmap.Config.ARGB_8888);

                //Create a new Canvas with the bitmap that's big enough for
                //the image plus gap plus reflection
                Canvas canvas = new Canvas(bitmapWithReflection);
                //Draw in the original image
                canvas.drawBitmap(originalImage, 0, 0, null);
                //Draw in the gap
                Paint deafaultPaint = new Paint();
                canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
                //Draw in the reflection
                canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);

                //Create a shader that is a linear gradient that covers the reflection
                Paint paint = new Paint();
                LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
                        bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
                        Shader.TileMode.CLAMP);
                //Set the paint to use this shader (linear gradient)
                paint.setShader(shader);
                //Set the Transfer mode to be porter duff and destination in
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                //Draw a rectangle using the paint with our linear gradient
                canvas.drawRect(0, height, width,
                        bitmapWithReflection.getHeight() + reflectionGap, paint);

                ImageView imageView = new ImageView(mContext);
                imageView.setImageBitmap(bitmapWithReflection);
                imageView.setLayoutParams(new CoverFlow.LayoutParams(120, 180));
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                mImages[index++] = imageView;

            }
            return true;
        }

        public int getCount() {
            return  ConstData.gallery_cup_images.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            final View c_view = inflater.inflate(R.layout.wm_cupsize_select_list, null);
            final   String cup_sizes[] = {"250ml","300ml","350ml","500ml","750ml","1000ml"};
            //Use this code if you want to load from resources
            //ImageView i = new ImageView(mContext);
            ImageView i = (ImageView) c_view.findViewById(R.id.imageView5);
            i.setImageResource( ConstData.gallery_cup_images[position]);
            // i.setLayoutParams(new CoverFlow.LayoutParams(130, 130));
            i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            TextView productname = (TextView) c_view.findViewById(R.id.editText6);
            productname.setText(cup_sizes[position]);
            //Make sure we set anti-aliasing otherwise we get jaggies
            BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
            drawable.setAntiAlias(true);
            return c_view;

            //return mImages[position];
        }
        /** Returns the size (0.0f to 1.0f) of the views
         * depending on the 'offset' to the center. */
        public float getScale(boolean focused, int offset) {
        /* Formula: 1 / (2 ^ offset) */
            return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));
        }

    }

    private  void cancel_notification()
    {
        int notificationId = getIntent().getIntExtra("notificationId", 0);


            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);

    }

    private void load_sound()
    {
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        //Hardware buttons setting to adjust the media sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // the counter will help us recognize the stream id of the sound played  now
        counter_play = 0;

        // Load the sounds
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        soundID_drink = soundPool.load(this, R.raw.water_drinking, 1);
        soundID_empty = soundPool.load(this, R.raw.water_empty_sound, 1);

    }

    public void playSound(int sound_id) {
        // Is the sound loaded does it already play?
        if (loaded && !plays) {
            soundPool.play(sound_id, volume, volume, 1, 0, 1f);
            counter_play = counter_play++;
            plays = true;
        }
    }
    public void stopSound(int sound_id) {
        if (plays) {
            soundPool.stop(sound_id);
         /*   soundID = soundPool.load(this, R.raw.beep, counter);
            Toast.makeText(this, "Stop sound", Toast.LENGTH_SHORT).show();*/
            plays = false;
        }
    }

    private  void check_next_drink_entry(Integer Cupsize)
    {
        Integer PerGoal = db.getLastSetGoal(relationShipId,date);
        String Pertotal= ConstData.getValueOrDefault(  db.getTotalDrink_quantity(date,relationShipId),"1");
        Double remaining_water=PerGoal-Double.parseDouble(Pertotal);

        if(Double.parseDouble(Pertotal)<PerGoal)
        {
        Double percent = (( (Double.parseDouble(Pertotal)+Cupsize)/ PerGoal)) * 100;

          if(percent>125.00) {
              overDrinkWarning(remaining_water);
          }else {
              drink();
          }
    }else {
            drink();
        }
    }

    private void overDrinkWarning(Double pending)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.wm_overdrinking, null);
        final Button btncancel=(Button) dialogview.findViewById(R.id.btn_cancel);
        final TextView TextView=(TextView) dialogview.findViewById(R.id.analysisMessage);
        final TextView textView=(TextView) dialogview.findViewById(R.id.textView);
        final Button btnSave=(Button) dialogview.findViewById(R.id.btn_Saves);
        //final LinearLayout lnrdisplay=(LinearLayout) dialogview.findViewById(R.id.lnrdisplay);
        builder.setView(dialogview);
        final Dialog dlg= builder.create();

        textView.setText("Warning!");
        btnSave.setText("ok");
        TextView.setText("Please change the cupsize .You have only"+ pending+"ml pending.If you continue with the current cupsize it will be overdriking!");

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                drink();
                dlg.dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Glass_list_select();
                dlg.dismiss();

            }
        });

        dlg.show();
    }

    private void litre_ml_selection(Integer water_g)
    {
        LitreSAelected= pref.getBoolean(LITRE_SELECTED,false);//getIntent().getBooleanExtra("UnitSelected",false);
        if(LitreSAelected==true) {
            goal_show_txt.setText(String.valueOf(water_g / 1000.0));
            goal_show_litre_txt.setText("Litre");
        }else
        {
            goal_show_txt.setText(String.valueOf(water_g));
            goal_show_litre_txt.setText("ml");
        }
    }
}


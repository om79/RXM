package viroopa.com.medikart.MedicineReminder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.AddMember;
import viroopa.com.medikart.MedicineReminder.MedicineImageColorShape.AbstractWheel;
import viroopa.com.medikart.MedicineReminder.MedicineImageColorShape.AbstractWheelTextAdapter;
import viroopa.com.medikart.MedicineReminder.MedicineImageColorShape.OnWheelChangedListener;
import viroopa.com.medikart.MedicineReminder.Model.M_alarmsoundlist;
import viroopa.com.medikart.MedicineReminder.Model.M_medicinelist;
import viroopa.com.medikart.MedicineReminder.Model.M_productlist;
import viroopa.com.medikart.MedicineReminder.adapter.AD_timeslot;
import viroopa.com.medikart.MedicineReminder.adapter.AD_alarmsound;
import viroopa.com.medikart.ObjectItem;
import viroopa.com.medikart.R;
import viroopa.com.medikart.adapter.AD_adapterCombo;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.Add_Doctor_Dialog;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.common.CustomAutoCompleteView;
import viroopa.com.medikart.common.numerdialog;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteMRHandler;

public class MRA_SetReminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        Add_Doctor_Dialog.OnDoctorSelectListener,
        numerdialog.OnNumberDialogDoneListener {

    private  AllMedAdapter o_AllMedAdapter;
    private Dialog dialog_all_med;
    int num_of_days_check=0,frequency_check=0;
    private SqliteMRHandler db;
    private int packQnty=1;
    private TextView add_pill_buddy;
    private String sMemberId;
    private ImageView img_med;
    private ProgressDialog pDialog;
    private Integer frq_pos=-99;
    private AbstractWheel ABS_image,ABS_firstColors,ABS_SecondColors;
    private Boolean DoubleImageVisible=false;
    private Spinner add_frequency;
    private AutoCompleteTextView medicine_name;
    private Button duration , btndays;
    private  String Doctor_id="0",pillbuddy_id="0";
    private CustomAutoCompleteView search_medicine;
    private TextView med_name_txt;
    private String selected_tone;
    private Button next_button;
    private LinearLayout img_selected_med;
    Integer selected_position = -1;
    private  ListView lst_all_med;
    private LayoutInflater inflater;
    private  String refill_Packsize="-99";
    private int Refill_flag=0;
    private Integer Total_No_Of_Units;
    private int reminder_flag=0;

    Animation scale ;




    private static final String PLACEBO_ID="placebo";




    private Integer Med_img_id=1,Med_img_color_first_id=5,Med_img_color_second_id=-99;

    public static MRA_SetReminder objsecondActivity;
    private MediaPlayer player;

    private  Integer hour;
    private Integer minutes;
    ObjectItem[] ObjectItempillBuddy=null;
    private  String Selected_Medicine_id,Selected_medicine_name;
    private  ImageView frst_part, second_part,img_single;

    private  Menu objMemberMenu;

    private LinearLayout show_medicines_list,add_medicine_button_layout,double_layout;
    private RelativeLayout add_new_medicine_layout;

    private Button set_dose;
    String time_pckr,Med_actual_id="-99";
    ListView lvcom;
    AD_adapterCombo pillbuddyadapter;

    private Integer dayintervel;
    private Button med_shapecolor;
    private TextView selectDate;
    AppController globalVariable;
    private EditText med_instructions;

    ArrayList<M_medicinelist> listItems = new ArrayList<M_medicinelist>();

    Integer everyday=1;
    String new_rem_id;

    Integer duration_type_id=0;
    Integer reminder_type_id;
    String reminder_type;
    Integer duration_value;

    Double quntity;
    Integer change_time=0;
    Integer s_timeslot_id;

    AD_timeslot adapter ;
    AD_alarmsound alarmadapter;

    private Integer setdosAmount;
    private String setDosageunit="pills";
    private String setcycleDate;
    private String number_ofDays;
    private String SelDate;
    private Integer daysIntervelId;
    private  String sel_condition="1";
    private String daysIntervelValue;
    private String selctDaysInterverl;
    private Spinner condtn;
    private Integer useplacebo;
    private String cycle_period;
    private  SQLiteHandler db_new;
    private Integer nNewCityId;

    JSONArray imgarr = new JSONArray();
    JSONArray set_cycle = new JSONArray();
    private ProgressBar progressBar;
    JSONArray days_interval = new JSONArray();
    JSONArray selected_days = new JSONArray();
    private List<M_productlist> productheaderlist = new ArrayList<M_productlist>();
    private  Button savebtn,clearbtn;
    private TextView Dosage_Amount;
    private  CheckBox chk_refil_reminder;
    private Spinner select_condition,Dosage_unit_selected,select_duration,select_frequency;
    private TextView pick_medicine,select_date,btnSetReminder,add_doctor;
    private  String[] units ;
    private  ArrayAdapter unittAdapter;

    Date current_date = Calendar.getInstance().getTime();
    DateFormat dateFormat_month_in_words = new SimpleDateFormat("LLLL dd,yyyy");
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
    private  int nYear,nMonth, Nday;

    ArrayList<M_medicinelist> all_medicine_list=new ArrayList<M_medicinelist>();

    LinearLayout.LayoutParams param_custom = new LinearLayout.LayoutParams(20,40);
    ImageView img_first_part ;
    ImageView img_second_part ;

    @Override
    public void onDone(int value,String sClass) {


        switch (sClass) {

            case "Dosage_Amount":
                setdosAmount =value;
                Dosage_Amount.setText(Integer.toString(value));
                break;
            case "select_duration":

                try {
                    number_ofDays = String.valueOf(value);
                    selected_days.getJSONObject(0).put("reminder_id", new_rem_id);
                    selected_days.getJSONObject(1).put("selected_days", number_ofDays);
                }catch (JSONException e)
                {}

                break;
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mr_new_add_medicine);
        player = new MediaPlayer();
        inflater = LayoutInflater.from(MRA_SetReminder.this);
        db = new SqliteMRHandler(getApplicationContext());
        db_new = new SQLiteHandler(getApplicationContext());
        objsecondActivity=this;

        setupUI(findViewById(R.id.main_screen));

        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        scale.reset();

        globalVariable = (AppController) getApplicationContext();
        add_frequency = (Spinner) findViewById(R.id.editText12);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);


        chk_refil_reminder=(CheckBox)findViewById(R.id.chk_refil_reminder);
        duration = (Button) findViewById(R.id.editText11);
        med_shapecolor = (Button) findViewById(R.id.txt_doctor);
        medicine_name = (AutoCompleteTextView) findViewById(R.id.editText7);
        med_instructions = (EditText) findViewById(R.id.message);
        set_dose = (Button) findViewById(R.id.editText9);
        btndays = (Button) findViewById(R.id.button2);
      //  addmedFriend=(EditText)findViewById(R.id.editText15);
        selectDate=(TextView) findViewById(R.id.textView4);
        condtn=(Spinner) findViewById(R.id.editText13);
        savebtn=(Button) findViewById(R.id.btnsave);
        clearbtn=(Button) findViewById(R.id.btnclear);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        select_condition=(Spinner)findViewById(R.id.select_condition);
        Dosage_Amount=(TextView) findViewById(R.id.Dosage_Amount);
        Dosage_unit_selected=(Spinner)findViewById(R.id.Dosage_unit_selected);
        select_duration=(Spinner)findViewById(R.id.select_duration);
        select_frequency=(Spinner)findViewById(R.id.select_frequency);

        pick_medicine=(TextView)findViewById(R.id.pick_medicine);
        add_pill_buddy=(TextView)findViewById(R.id.add_pill_buddy);
        select_date=(TextView)findViewById(R.id.select_date);
        btnSetReminder=(TextView)findViewById(R.id.btnSetReminder);
        add_doctor=(TextView)findViewById(R.id.add_doctor);
        img_selected_med=(LinearLayout)findViewById(R.id.img_selected_med);

        getWindow().getDecorView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        //insert_placebo();


        fill_pill_buddy();
        init_rxSpinners();




        getSupportActionBar().setIcon(R.drawable.rxlogo);



        SelDate=dateFormat_query.format(current_date);

        select_date.setText("Starting From "+dateFormat_month_in_words.format(current_date));


        setcycleDate =SelDate;


        try {
            getIntenet();
            Load_json_data();
            init_tabledata();



        } catch (Exception e) {
        }


        pick_medicine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                get_all_medicine_dialog();


            }
        });
        pick_medicine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //clear_refill_reminder();
            }
        });

        add_pill_buddy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                add_pill_buddyr_combo_box();


            }
        });

        select_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                select_date();

            }
        });
        btnSetReminder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Set_reminder_time();

            }
        });

        add_doctor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Add_Doctor_Dialog myDiag = Add_Doctor_Dialog.newInstance("add_doctor");
                myDiag.show(getFragmentManager(), "Diag");
            }
        });



        try {
            add_frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    frq_pos = position;
                    reminder_type_id = position;
                    String[] mTestArray;
                    mTestArray = getResources().getStringArray(R.array.medadd_frequency);
                    reminder_type = mTestArray[position].toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });
        } catch (Exception e) {


        }
        duration.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                tstdialog();
            }
        });

        try {

        } catch (Exception e) {

        }
        Dosage_Amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f_set_Dosage();

            }
        });

        set_dose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setDosage();

            }


        });
        try {
            btndays.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    select_days();

                }


            });
        } catch (Exception e) {
            e.toString();
        }
        med_shapecolor.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            }
        });





        selectDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                {


                    AlertDialog.Builder builder = new AlertDialog.Builder(MRA_SetReminder.this);
                    final View dialog = inflater.inflate(R.layout.date_med_reminder, null);
                    builder.setView(dialog);
                    final Dialog dlg = builder.create();


                    final Button btnok= (Button)dialog.findViewById(R.id.button4);
                    final Button btncancel= (Button)dialog.findViewById(R.id.btncancel);

                    final DatePicker Datepckr = (DatePicker) dialog.findViewById(R.id.datePicker);

                    btnok.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {



                            selectDate.setText(Datepckr.getDayOfMonth() + "-" + (Datepckr.getMonth()+1) + "-" + Datepckr.getYear());

                            SelDate = Datepckr.getDayOfMonth() + "-" + (Datepckr.getMonth()+1)+ "-" + Datepckr.getYear();

                            setcycleDate = String.valueOf(String.valueOf(Datepckr.getYear() + "-" + String.valueOf( Datepckr.getMonth()+1) + "-" +  Datepckr.getDayOfMonth()));

                            dlg.cancel();
                        }
                    });

                    btncancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {

                            dlg.cancel();
                        }
                    });

                    dlg.getWindow().getAttributes().windowAnimations =
                            R.style.dialog_med_animation;
                    dlg.show();

                }
            }
        });


        medicine_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (medicine_name.getText().toString().length() > 2) {

                    get_productsearch(medicine_name.getText().toString());


                } else {

                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        savebtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if(Selected_medicine_name!=null) {
                      if(setdosAmount!=null) {
                    if(!select_date.getText().toString().isEmpty()|| !select_date.getText().toString().equals("")) {
                        if(duration_type_id!=-99) {
                            if(!sel_condition.equals("")) {
                                if(frq_pos!=-99) {


                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.setProgress(0);
                                    savedata();
                                    // db.addMedDetails_update(nCount, 1);
                                    generate_schedule(new_rem_id);

                                    progressBar.setVisibility(View.GONE);
                                    progressBar.setProgress(100);

                                   Intent intentTst = new Intent(MRA_SetReminder.this, MRA_ReminderMain.class);
                                    startActivity(intentTst);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(),"please set remindertime", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Toast.makeText(getApplicationContext(),"please select condition", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"please select duration", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"please select date", Toast.LENGTH_SHORT).show();
                    }
                      }else{
                          Toast.makeText(getApplicationContext(),"please select dosage", Toast.LENGTH_SHORT).show();
                      }
                }else{
                    Toast.makeText(getApplicationContext(),"please select a medicine", Toast.LENGTH_SHORT).show();
                }
            }


        });
        clearbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                medicine_name.setText("");
                med_instructions.setText("");
                reminder_type_id=0;
                reminder_type="";
                duration_type_id=0;
                duration_value=0;
                SelDate="";
                daysIntervelId=0;
                daysIntervelValue="";
                useplacebo=0;

                setdosAmount=0;
                setDosageunit="";
                sel_condition="";
                selectDate.setText(SelDate);
            }


        });


        chk_refil_reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Refill_flag=1;
                    refill_reminder_selection_dialog();

                }else
                {
                    Refill_flag=0;
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_only_setting, menu);
        this.objMemberMenu=menu;

      /*  View mCustomView = inflater.inflate(R.layout.circula_image_icon_dm, null);
        objMemberMenu.findItem(R.id.circlularImage).setActionView(mCustomView);
        */


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {
            Intent Intenet_adds = new Intent(this, MRA_MonitorSetting.class);
            startActivity(Intenet_adds);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void update_data()

    {
        AutocompleteCustomArrayAdapter modeAdapter = new AutocompleteCustomArrayAdapter(this,listItems);
        search_medicine.setAdapter(modeAdapter);

    }

    private void Set_reminder_dialog() {

        try {



            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View dialogview = inflater.inflate(R.layout.med_rem_reminder_times, null);
            builder.setView(dialogview);

            //final ListView modeList = new ListView(this);
            final ListView modeList = (ListView)dialogview.findViewById(R.id.listView2);
            lvcom=modeList;

            final Cursor cursor_data;

            cursor_data = db.getAllDataMedTimeslot(frq_pos);


            adapter = new AD_timeslot(getApplicationContext(), cursor_data, 1);
            modeList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            final Dialog dialog = builder.create();
            final Button btnok=(Button)dialogview.findViewById(R.id.btnok);
            final Button btncancel=(Button)dialogview.findViewById(R.id.btncancel);


            btnok.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    try {


                    }catch(Exception e)
                    {
                        e.toString();
                    }
                    dialog.cancel();

                }


            });
            btncancel.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    try {


                    }catch(Exception e)
                    {
                        e.toString();
                    }
                    dialog.cancel();

                }


            });


            modeList.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3)


                {

                    try {

                        String tmslt = cursor_data.getString(cursor_data.getColumnIndex("_id"));
                        s_timeslot_id=Integer.parseInt(tmslt);
                        med_rem_time_click();

                    }catch(Exception e)
                    {e.toString();}
                    Integer xy=0;

                    dialog.dismiss();

                    if (change_time==1 && xy==position)
                    {
                        //ListView modeListsec = new ListView(MedReminderSetReminder.this);
                        try {
                            //TextView tv = (TextView)view.findViewById(android.R.id.text1);
                        }catch(Exception e)
                        {
                            e.toString();
                        }

                    }

                }
            });

            dialog.show();
        }catch(Exception e){
        }

    }




    public  void med_rem_time_click() {
        View dialogview = inflater.inflate(R.layout.med_rem_onitem_click, null);
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        dialogbuilder.setView(dialogview);
        final TimePicker timePkr = (TimePicker) dialogview.findViewById(R.id.timePicker1);
        final NumberPicker nbrselectdays= (NumberPicker) dialogview.findViewById(R.id.numberPicker3);

        final Button btnok = (Button) dialogview.findViewById(R.id.btnok);
        final Button btncancel = (Button) dialogview.findViewById(R.id.btncancel);
        nbrselectdays.setMinValue(1);// restricted number to minimum value i.e 1
        nbrselectdays.setMaxValue(1000);//
        nbrselectdays.setWrapSelectorWheel(false);
        final Dialog dialog =dialogbuilder.create();

        btnok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {

                    nbrselectdays.clearFocus();
                    quntity = Double.parseDouble(String.valueOf(nbrselectdays.getValue()));
                    Integer slot_id = 0;
                    if (hour == 8 || hour == 9 || hour == 10 || hour == 11 || hour == 12) {
                        slot_id = 1;
                    }
                    if (hour == 13 || hour == 14 || hour == 15 || hour == 16) {
                        slot_id = 2;
                    }
                    if (hour == 13 || hour == 14 || hour == 15 || hour == 16) {
                        slot_id = 2;
                    }
                    if (hour == 17 || hour == 18 || hour == 19 || hour == 20) {
                        slot_id = 3;
                    }
                    if (hour > 20) {
                        slot_id = 4;
                    }


                    if (change_time == 1) {

                        db.addMedtimeSlotUpdate_update(s_timeslot_id, time_pckr, hour, slot_id, quntity);

                        dialog.dismiss();
                        try {

                            Cursor cursor_data = db.getAllDataMedTimeslot(frq_pos);

                            if (cursor_data.moveToFirst()) {
                                ArrayList<String> timeSlots = new ArrayList<String>();
                                while (!cursor_data.isAfterLast()) {
                                    timeSlots.add(cursor_data.getString(cursor_data.getColumnIndex("time_slots")));
                                    cursor_data.moveToNext();
                                }
                                cursor_data.close();


                                btnSetReminder.setText(reminder_type + " (" + TextUtils.join(",", timeSlots) + ")");
                            }
                        }catch (Exception e)
                        {

                        }
                        Set_reminder_dialog();

                        change_time = 0;
                    }

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
                change_time=1;

                hour = hourOfDay;
                minutes = minute;

                String F_Hour="";
                String F_minute="";



                if (hour < 10) {
                    F_Hour = "0" + hour;

                } else {
                    F_Hour = String.valueOf(hour);
                }
                if (minutes  < 10) {
                    F_minute = "0" + String.valueOf(minutes );
                } else {
                    F_minute = String.valueOf(minutes );
                }




                time_pckr = F_Hour + ":" + F_minute;




            }
        });



        timePkr.setIs24HourView(true);





        dialog.show();


    }




    public  void days_intervel() {
        try {
            View dialogview = inflater.inflate(R.layout.med_rem_days_intervel, null);
            AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
            dialogbuilder.setView(dialogview);
            final Dialog dlg=dialogbuilder.create();

            final Button plusdaysbtn = (Button) dialogview.findViewById(R.id.btndaysplus);
            final Button  minusdaysbtn = (Button) dialogview.findViewById(R.id.btndaysminus);
            final EditText txtnum = (EditText) dialogview.findViewById(R.id.edtdaysnumber);
            final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
            final Button cancelbtn = (Button) dialogview.findViewById(R.id.btncancel);
            dayintervel = 2;
            txtnum.setText("every  " + dayintervel + "  days");
            plusdaysbtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    dayintervel = dayintervel + 1;
                    String tst = String.valueOf(dayintervel);
                    txtnum.setText("every  " + tst + "  days");

                }
            });
            minusdaysbtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    if (dayintervel > 2) {
                        dayintervel = dayintervel - 1;
                        String tst = String.valueOf(dayintervel);
                        txtnum.setText("every  " + tst + "  days");
                    }


                }
            });
            okbtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    try {
                        days_interval.getJSONObject(0).put("reminder_id", new_rem_id);
                        days_interval.getJSONObject(0).put("days_interval", dayintervel);
                        selctDaysInterverl=String.valueOf(dayintervel);
                        dlg.dismiss();
                    } catch (Exception e) {
                    }

                }
            });

            cancelbtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    dlg.dismiss();
                }
            });




            dlg.show();
        }catch(Exception e)
        {
        }

    }


    public void select_days()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.med_rem_select_days, null);
        builder.setView(dialogview);

        final ListView modeList = (ListView)dialogview.findViewById(R.id.listView2);
        String[]srrr=new String[]{"every day","specific days of week","days interval","birth control cycle"};
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, android.R.id.text1, srrr);
        modeList.setAdapter(modeAdapter);

        final Button btncancel =(Button) dialogview.findViewById(R.id.btncancel);
        final Button btnok =(Button) dialogview.findViewById(R.id.btnok);

        final Dialog dialog = builder.create();
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                CheckedTextView textView = (CheckedTextView) v;
                textView.setChecked(!textView.isChecked());
                daysIntervelId = position;
                if (position == 0) {
                    everyday = 1;
                    dialog.dismiss();

                }
                if (position == 1) {
                    pick_days();
                    dialog.dismiss();
                }

                if (position == 2) {

                    days_intervel();
                    dialog.dismiss();
                }

                if (position == 3) {

                    f_set_cycle();
                    dialog.dismiss();
                }
            }


        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (everyday != 0) {
                    dialog.cancel();
                } else {
                    Toast.makeText(getApplicationContext(), "please select days", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dialog.show();
    }
    public void pick_days()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.med_rem_select_weekdays, null);
        builder.setView(dialogview);

        final ListView modeList = (ListView)dialogview.findViewById(R.id.listView2);
        String[] stringArray = new String[] { "Sun", "Mon","TUE","Wed","Thu","Fri","Sat" };
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, android.R.id.text1, stringArray);
        modeList.setAdapter(modeAdapter);

        final Dialog dialog = builder.create();
        final Button btncancel =(Button) dialogview.findViewById(R.id.btncancel);
        final Button btnok =(Button) dialogview.findViewById(R.id.btnok);



        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });



        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                final CheckedTextView textView = (CheckedTextView) v;
                textView.setChecked(!textView.isChecked());
                // dialog.dismiss();
                switch (position) {
                    case 0:
                        if (textView.isChecked()) {
                            try {
                                imgarr.getJSONObject(0).put("status", 1);
                            } catch (JSONException e) {
                            }
                        } else {
                            try {
                                imgarr.getJSONObject(0).put("status", 0);
                            } catch (JSONException e) {
                            }
                        }
                        break;

                    case 1:
                        if (textView.isChecked()) {
                            try {
                                imgarr.getJSONObject(1).put("status", 1);
                            } catch (JSONException e) {
                            }
                        } else {
                            try {
                                imgarr.getJSONObject(1).put("status", 0);
                            } catch (JSONException e) {
                            }
                        }

                        break;
                    case 2:
                        if (textView.isChecked()) {
                            try {
                                imgarr.getJSONObject(2).put("status", 1);
                            } catch (JSONException e) {
                            }
                        } else {
                            try {
                                imgarr.getJSONObject(2).put("status", 0);
                            } catch (JSONException e) {
                            }
                        }


                        break;
                    case 3:

                        if (textView.isChecked()) {
                            try {
                                imgarr.getJSONObject(3).put("status", 1);
                            } catch (JSONException e) {
                            }
                        } else {
                            try {
                                imgarr.getJSONObject(3).put("status", 0);
                            } catch (JSONException e) {
                            }
                        }
                        break;
                    case 4:
                        if (textView.isChecked()) {
                            try {
                                imgarr.getJSONObject(4).put("status", 1);
                            } catch (JSONException e) {
                            }
                        } else {
                            try {
                                imgarr.getJSONObject(4).put("status", 0);
                            } catch (JSONException e) {
                            }
                        }

                        break;
                    case 5:
                        if (textView.isChecked()) {
                            try {
                                imgarr.getJSONObject(5).put("status", 1);
                            } catch (JSONException e) {
                            }
                        } else {
                            try {
                                imgarr.getJSONObject(5).put("status", 0);
                            } catch (JSONException e) {
                            }
                        }

                        break;
                    case 6:
                        if (textView.isChecked()) {
                            try {
                                imgarr.getJSONObject(6).put("status", 1);
                            } catch (JSONException e) {
                            }
                        } else {
                            try {
                                imgarr.getJSONObject(6).put("status",0);
                            } catch (JSONException e) {
                            }
                        }

                        break;




                }

                btnok.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if(!textView.isChecked())
                        {
                            Toast.makeText(getApplicationContext(), "Please select days", Toast.LENGTH_SHORT).show();
                        }else {
                            dialog.cancel();
                        }
                    }
                });

            }
        });



        dialog.show();
    }

    public  void number_of_days() {
        try {

           int n_Number_of_days=1;

            try{
                n_Number_of_days=Integer.parseInt(number_ofDays);
            }catch(NumberFormatException ex){ // handle your exception

            }
            numerdialog myDiag = numerdialog.newInstance(1,n_Number_of_days,0,500,"select_duration","Select Duration");
            myDiag.show(getFragmentManager(), "Diag");


        }catch(Exception e)
        {
        }

    }
    public  void setDosage() {
        try {
            View dialogview = inflater.inflate(R.layout.med_rem_set_dosage, null);


            AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);

            dialogbuilder.setView(dialogview);

            final Spinner spiner = (Spinner) dialogview.findViewById(R.id.spinner2);
            final NumberPicker np = (NumberPicker)dialogview.findViewById(R.id.numberPicker);
            np.setMinValue(1);// restricted number to minimum value i.e 1
            np.setMaxValue(1000);// restricked number to maximum value i.e. 31
            np.setWrapSelectorWheel(false);
            final Dialog dialog= dialogbuilder.create();


            final Button btnok= (Button)dialogview.findViewById(R.id.btnok);
            final Button btncancel= (Button)dialogview.findViewById(R.id.btncancel);



            btnok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    np.clearFocus();
                    setdosAmount=np.getValue();
                    setDosageunit=spiner.getSelectedItem().toString();
                    set_dose.setText(setdosAmount+" "+setDosageunit);


                    dialog.dismiss();
                }
            });

            btncancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    dialog.dismiss();
                }
            });





            dialog.show();
        }catch(Exception e)
        {
        }

    }


    private void f_set_cycle() {



        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.med_rem_set_cycle, null);


        final  AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        //dialogbuilder.setTitle("Set Days Interval");
        dialogbuilder.setView(dialogview);

        final AlertDialog alertDialog = dialogbuilder.create();
        final Button cancelbtn = (Button)dialogview.findViewById(R.id.btncancel);
        final Button okbtn = (Button)dialogview.findViewById(R.id.btnok);

        okbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {



                final Spinner spr = (Spinner) dialogview.findViewById(R.id.spinner2);

                final CheckBox chkbox = (CheckBox) dialogview.findViewById(R.id.checkBox3);

                String peroid = spr.getSelectedItem().toString();
                cycle_period = peroid;

                Integer checked = 0;
                if (chkbox.isChecked()) {
                    checked = 1;
                    useplacebo = 1;
                }


                try {
                    set_cycle.getJSONObject(0).put("reminder_id", new_rem_id);
                    set_cycle.getJSONObject(1).put("Cycle_period", peroid);
                    set_cycle.getJSONObject(2).put("Date", setcycleDate);
                    set_cycle.getJSONObject(3).put("Use_placebo", checked);
                } catch (Exception e) {
                }
                alertDialog.cancel();
            }

        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //alert.dismiss();
                alertDialog.cancel();

            }

        });


        final DatePicker Datepckr = (DatePicker) dialogview.findViewById(R.id.datePicker);


        Datepckr.getCalendarView().setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Log.d("tag", "finally found the listener, the date is: year " + year + ", month " + month + ", dayOfMonth " + dayOfMonth);


                setcycleDate = String.valueOf(dayOfMonth) + "-" + String.valueOf(month) + "-" + String.valueOf(year);
            }
        });

        //dialogbuilder.create();


        alertDialog.show();

    }

    public void Load_json_data()
    {
        try {
            imgarr.put(new JSONObject("{\"id\":1,\"status\":0}"));
            imgarr.put( new JSONObject("{\"id\":2,\"status\":0}"));
            imgarr.put( new JSONObject("{\"id\":3,\"status\":0}"));
            imgarr.put( new JSONObject("{\"id\":4,\"status\":0}"));
            imgarr.put( new JSONObject("{\"id\":5,\"status\":0}"));
            imgarr.put( new JSONObject("{\"id\":6,\"status\":0}"));
            imgarr.put( new JSONObject("{\"id\":7,\"status\":0}"));




            JSONObject jscycle = new JSONObject();
            try {
                jscycle.put("Cycle_period", 0);
                set_cycle.put(jscycle);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            JSONObject jsusePlacebo = new JSONObject();
            try {
                jsusePlacebo.put("Use_placebo", 0);
                set_cycle.put(jsusePlacebo);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }






        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        //json for days intervel

        JSONObject days_intervl_rem_id = new JSONObject();
        JSONObject days_intervl_days = new JSONObject();

        try {
            days_intervl_rem_id.put("reminder_id", 0);
            days_intervl_days.put("days_interval",0);
            days_interval.put(days_intervl_rem_id);
            days_interval.put(days_intervl_days);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //json for selected number of days
        JSONObject sel_days_rem_id = new JSONObject();
        JSONObject numberofDays = new JSONObject();
        try {
            sel_days_rem_id.put("reminder_id", 0);
            numberofDays.put("selected_days",0);
            selected_days.put(sel_days_rem_id);
            selected_days.put(numberofDays);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }

    public void savedata()
    {
        String instructn=med_instructions.getText().toString();

       // sel_condition=condtn.getSelectedItem().toString();
        // Toast.makeText(getApplicationContext(),reminder_type+xxy, Toast.LENGTH_SHORT).show();

        try{
            if(duration_type_id==0)
            {
                duration_value=30;
            }
            else
            {
                duration_value=Integer.parseInt(number_ofDays);
            }

            if(daysIntervelId==0) {
                daysIntervelValue="0";
            }
            if(daysIntervelId==1) {
                daysIntervelValue=imgarr.toString();
            }
            if(daysIntervelId==2) {
                daysIntervelValue=selctDaysInterverl;
            }
            if(daysIntervelId==3) {
                daysIntervelValue=cycle_period;
            }
            String refill_date="";
            if(Refill_flag==1)
            {
                refill_date=calculate_refill_date(Total_No_Of_Units,setDosageunit,duration_value,reminder_type_id,setdosAmount);
            }

            //db.addMedRemMaster(1,med_name,cond, reminder_type_id, reminder_type, duration_type_id,duration_type,2, shape_colr, "round", instructn);
            db.addMedRemMaster_update(new_rem_id,Selected_Medicine_id, Selected_medicine_name, reminder_type_id,
                    reminder_type, duration_type_id, duration_value, SelDate, daysIntervelId, daysIntervelValue,
                    useplacebo, Med_img_id,Med_img_color_first_id,Med_img_color_second_id, instructn, setdosAmount,
                    setDosageunit, sel_condition,Doctor_id,pillbuddy_id,sMemberId,8,selected_tone,Refill_flag,
                    refill_date,Total_No_Of_Units,refill_Packsize,0,reminder_flag);

            ConstData.create_json_from_table("medicine_reminder","A","",new_rem_id,sMemberId,SelDate,this);
            Toast.makeText(getApplicationContext(),Selected_medicine_name+" scheduled", Toast.LENGTH_SHORT).show();
        }



        catch ( Exception E )
        {
            // Toast.makeText(getApplicationContext(),E.toString(), Toast.LENGTH_SHORT).show();
        }

    }





    public Date date_addDays(Date date, int days)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public int date_dayofweek(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }


   /* public void generate_schedule(String p_id)
    {
        // First Delete Last Schedule Row
        db.delete_med_reminder_schedule(p_id,sMemberId);

        Cursor cursor_med_master = db.get_med_master_data(p_id,sMemberId);
        Cursor cursor_med_detail = db.get_med_detail_data(p_id,sMemberId);

        if ((cursor_med_master == null) || (cursor_med_master.getCount()<=0) ||
                (cursor_med_detail == null) || (cursor_med_detail.getCount()<=0))
        {
            //f_alert_ok("Information", "No Data Found");
        }
        else
        {
            cursor_med_master.moveToFirst();

            Double p_dosage = cursor_med_master.getDouble(cursor_med_master.getColumnIndex("dosage_value"));
            String p_dosage_type = cursor_med_master.getString(cursor_med_master.getColumnIndex("dosage_type"));
            Integer use_palcebo= cursor_med_master.getInt(cursor_med_master.getColumnIndex("use_placebo"));

            String p_schedule_start_date = cursor_med_master.getString(cursor_med_master.getColumnIndex("schedule_start_date"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dSchedule_start_date_temp = new Date();
            try {
                dSchedule_start_date_temp = dateFormat.parse(p_schedule_start_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Integer p_duration_type = cursor_med_master.getInt(cursor_med_master.getColumnIndex("schedule_duration_type_id"));
            Integer p_duration_value = cursor_med_master.getInt(cursor_med_master.getColumnIndex("schedule_duration_value"));

            Integer p_days_interval = cursor_med_master.getInt(cursor_med_master.getColumnIndex("days_intervel_type_id"));
            String p_days_interval_value = cursor_med_master.getString(cursor_med_master.getColumnIndex("days_intervel_value"));
            Integer p_days_interval_value_temp = 0 ,p_days_interval_value1 = 0;
            String p_days_Selected = "";
            JSONArray ja_days_selected = new JSONArray();

            if (p_days_interval == 1)
            {
                try
                {
                    ja_days_selected = new JSONArray(p_days_interval_value);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (p_days_interval == 2)
            {
                p_days_interval_value_temp = Integer.parseInt(p_days_interval_value);
                p_days_interval_value1 = p_days_interval_value_temp;
            }



            for (int i = 1; i <= p_duration_value; i++)
            {
                if (p_days_interval == 0)
                {
                    if (cursor_med_detail.moveToFirst()) {
                        do {
                            Integer p_sequence = cursor_med_detail.getInt(cursor_med_detail.getColumnIndex("details_id"));
                            Double p_quantity = cursor_med_detail.getDouble(cursor_med_detail.getColumnIndex("medicine_quantity"));
                            String SUUID=UUID.randomUUID().toString().replace("-","");
                            String timeslot = cursor_med_detail.getString(cursor_med_detail.getColumnIndex("timeslot"));
                            String p_datetime_set = dateFormat.format(dSchedule_start_date_temp) + " " + timeslot + ":00";
                            db.insert_med_reminder_schedule(SUUID,p_id,"pending",p_datetime_set,p_datetime_set,
                                    p_sequence,p_dosage_type,p_dosage,p_quantity,sMemberId,8);
                        } while (cursor_med_detail.moveToNext());
                    }
                }

                if (p_days_interval == 1)
                {
                    int day = date_dayofweek(dSchedule_start_date_temp);
                    if (ja_days_selected.toString().contains("\"id\":" + day+",\"status\":1"))
                    {
                        if (cursor_med_detail.moveToFirst()) {
                            do {
                                Integer p_sequence = cursor_med_detail.getInt(cursor_med_detail.getColumnIndex("details_id"));
                                Double p_quantity = cursor_med_detail.getDouble(cursor_med_detail.getColumnIndex("medicine_quantity"));
                                String SUUID=UUID.randomUUID().toString().replace("-","");
                                String timeslot = cursor_med_detail.getString(cursor_med_detail.getColumnIndex("timeslot"));
                                String p_datetime_set = dateFormat.format(dSchedule_start_date_temp) + " " + timeslot + ":00";
                                db.insert_med_reminder_schedule(SUUID,p_id,"pending",p_datetime_set,p_datetime_set,
                                        p_sequence,p_dosage_type,p_dosage,p_quantity,sMemberId,8);
                            } while (cursor_med_detail.moveToNext());
                        }
                    }

                }

                if (p_days_interval == 2)
                {
                    if ((p_days_interval_value_temp % p_days_interval_value1) == 0)
                    {
                        if (cursor_med_detail.moveToFirst()) {
                            do {
                                Integer p_sequence = cursor_med_detail.getInt(cursor_med_detail.getColumnIndex("details_id"));
                                Double p_quantity = cursor_med_detail.getDouble(cursor_med_detail.getColumnIndex("medicine_quantity"));
                                String SUUID=UUID.randomUUID().toString().replace("-","");
                                String timeslot = cursor_med_detail.getString(cursor_med_detail.getColumnIndex("timeslot"));
                                String p_datetime_set = dateFormat.format(dSchedule_start_date_temp) + " " + timeslot + ":00";
                                db.insert_med_reminder_schedule(SUUID,p_id,"pending",p_datetime_set,p_datetime_set,
                                        p_sequence,p_dosage_type,p_dosage,p_quantity,sMemberId,8);
                            } while (cursor_med_detail.moveToNext());
                        }
                    }
                    p_days_interval_value_temp = p_days_interval_value_temp + 1;
                }
                if (p_days_interval == 3)
                {
                    if (cursor_med_detail.moveToFirst()) {
                        do {
                            Integer p_sequence = cursor_med_detail.getInt(cursor_med_detail.getColumnIndex("details_id"));
                            Double p_quantity = cursor_med_detail.getDouble(cursor_med_detail.getColumnIndex("medicine_quantity"));
                            String SUUID=UUID.randomUUID().toString().replace("-","");
                            String timeslot = cursor_med_detail.getString(cursor_med_detail.getColumnIndex("timeslot"));
                            String p_datetime_set = dateFormat.format(dSchedule_start_date_temp) + " " + timeslot + ":00";


                            String s_birth_controll_start_days = p_days_interval_value.substring(0, 2);
                            String s_birth_controll_intervel_days = p_days_interval_value.substring(0,p_days_interval_value.length()-1);

                            Integer n_birth_controll_start_days=Integer.parseInt(s_birth_controll_start_days);
                            Integer n_birth_controll_intervel_days=Integer.parseInt(s_birth_controll_intervel_days);
                            Integer n_birth_controll_intervel_days_temp=n_birth_controll_intervel_days;


                            if(i<=n_birth_controll_start_days) {
                                db.insert_med_reminder_schedule(SUUID, p_id, "pending", p_datetime_set, p_datetime_set,
                                        p_sequence, p_dosage_type, p_dosage, p_quantity, sMemberId, 8);
                            }else if(use_palcebo==1)
                            {

                                    db.insert_med_reminder_schedule(SUUID, PLACEBO_ID, "pending", p_datetime_set, p_datetime_set,
                                            p_sequence, p_dosage_type, p_dosage, p_quantity, sMemberId, 8);

                            }
                        } while (cursor_med_detail.moveToNext());
                    }
                }

                dSchedule_start_date_temp = date_addDays(dSchedule_start_date_temp, 1);

            }
        }

        ConstData.create_json_from_table("reminder_schedule","A","",p_id,sMemberId,SelDate,this);

    }
*/
    public void generate_schedule(String p_id)
    {
        // First Delete Last Schedule Row
        db.delete_med_reminder_schedule(p_id,sMemberId);

        Cursor cursor_med_master = db.get_med_master_data(p_id,sMemberId);
        Cursor cursor_med_detail = db.get_med_detail_data(p_id,sMemberId);

        if ((cursor_med_master == null) || (cursor_med_master.getCount()<=0) ||
                (cursor_med_detail == null) || (cursor_med_detail.getCount()<=0))
        {
            //f_alert_ok("Information", "No Data Found");
        }
        else
        {
            cursor_med_master.moveToFirst();

            Double p_dosage = cursor_med_master.getDouble(cursor_med_master.getColumnIndex("dosage_value"));
            String p_dosage_type = cursor_med_master.getString(cursor_med_master.getColumnIndex("dosage_type"));


            String p_schedule_start_date = cursor_med_master.getString(cursor_med_master.getColumnIndex("schedule_start_date"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dSchedule_start_date_temp = new Date();
            try {
                dSchedule_start_date_temp = dateFormat.parse(p_schedule_start_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Integer p_duration_type = cursor_med_master.getInt(cursor_med_master.getColumnIndex("schedule_duration_type_id"));
            Integer p_duration_value = cursor_med_master.getInt(cursor_med_master.getColumnIndex("schedule_duration_value"));

            Integer p_days_interval = cursor_med_master.getInt(cursor_med_master.getColumnIndex("days_intervel_type_id"));
            String p_days_interval_value = cursor_med_master.getString(cursor_med_master.getColumnIndex("days_intervel_value"));
            Integer p_days_interval_value_temp = 0 ,p_days_interval_value1 = 0;
            String p_days_Selected = "";
            JSONArray ja_days_selected = new JSONArray();

            Integer p_birth_ctrl_use_palcebo = cursor_med_master.getInt(cursor_med_master.getColumnIndex("use_placebo"));
            Integer p_birth_ctrl_total_days = 0, p_birth_ctrl_intervel_days = 0;

            Integer p_birth_ctrl_total_days_Temp = 1;

            //-----------------------------------------//

            if (p_days_interval == 1)
            {
                try
                {
                    ja_days_selected = new JSONArray(p_days_interval_value);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (p_days_interval == 2)
            {
                p_days_interval_value_temp = Integer.parseInt(p_days_interval_value);
                p_days_interval_value1 = p_days_interval_value_temp;
            }
            else if (p_days_interval == 3)
            {
                p_birth_ctrl_total_days    = Integer.parseInt(p_days_interval_value.substring(0, 2)); //21
                p_birth_ctrl_intervel_days = Integer.parseInt(p_days_interval_value.substring(p_days_interval_value.length()-1)); //7
            }

            //-----------------------------------------//
            for (int i = 1; i <= p_duration_value; i++)
            {


                if (p_days_interval == 0)
                {
                    if (cursor_med_detail.moveToFirst()) {
                        do {
                            Integer p_sequence = cursor_med_detail.getInt(cursor_med_detail.getColumnIndex("details_id"));
                            Double p_quantity = cursor_med_detail.getDouble(cursor_med_detail.getColumnIndex("medicine_quantity"));
                            String SUUID=UUID.randomUUID().toString().replace("-","");
                            String timeslot = cursor_med_detail.getString(cursor_med_detail.getColumnIndex("timeslot"));
                            String p_datetime_set = dateFormat.format(dSchedule_start_date_temp) + " " + timeslot + ":00";
                            db.insert_med_reminder_schedule(SUUID,p_id,"SCH",p_datetime_set,p_datetime_set,
                                    p_sequence,p_dosage_type,p_dosage,p_quantity,sMemberId,8);
                        } while (cursor_med_detail.moveToNext());
                    }
                }

                if (p_days_interval == 1)
                {
                    int day = date_dayofweek(dSchedule_start_date_temp);
                    if (ja_days_selected.toString().contains("\"id\":" + day+",\"status\":1"))
                    {
                        if (cursor_med_detail.moveToFirst()) {
                            do {
                                Integer p_sequence = cursor_med_detail.getInt(cursor_med_detail.getColumnIndex("details_id"));
                                Double p_quantity = cursor_med_detail.getDouble(cursor_med_detail.getColumnIndex("medicine_quantity"));
                                String SUUID=UUID.randomUUID().toString().replace("-","");
                                String timeslot = cursor_med_detail.getString(cursor_med_detail.getColumnIndex("timeslot"));
                                String p_datetime_set = dateFormat.format(dSchedule_start_date_temp) + " " + timeslot + ":00";
                                db.insert_med_reminder_schedule(SUUID,p_id,"SCH",p_datetime_set,p_datetime_set,
                                        p_sequence,p_dosage_type,p_dosage,p_quantity,sMemberId,8);
                            } while (cursor_med_detail.moveToNext());
                        }
                    }

                }

                if (p_days_interval == 2)
                {
                    if ((p_days_interval_value_temp % p_days_interval_value1) == 0)
                    {
                        if (cursor_med_detail.moveToFirst()) {
                            do {
                                Integer p_sequence = cursor_med_detail.getInt(cursor_med_detail.getColumnIndex("details_id"));
                                Double p_quantity = cursor_med_detail.getDouble(cursor_med_detail.getColumnIndex("medicine_quantity"));
                                String SUUID=UUID.randomUUID().toString().replace("-","");
                                String timeslot = cursor_med_detail.getString(cursor_med_detail.getColumnIndex("timeslot"));
                                String p_datetime_set = dateFormat.format(dSchedule_start_date_temp) + " " + timeslot + ":00";
                                db.insert_med_reminder_schedule(SUUID,p_id,"SCH",p_datetime_set,p_datetime_set,
                                        p_sequence,p_dosage_type,p_dosage,p_quantity,sMemberId,8);
                            } while (cursor_med_detail.moveToNext());
                        }
                    }
                    p_days_interval_value_temp = p_days_interval_value_temp + 1;
                }

                if (p_days_interval == 3)
                {
                    Integer nncheck = 0;
                    if (p_birth_ctrl_total_days_Temp >= p_birth_ctrl_total_days)
                    {
                        nncheck = p_birth_ctrl_total_days_Temp % p_birth_ctrl_total_days ;
                    }

                    if (nncheck > 0 && (nncheck <= p_birth_ctrl_intervel_days))
                    {
                        if (p_birth_ctrl_use_palcebo == 1)
                        {
                            if (cursor_med_detail.moveToFirst()) {
                                do {
                                    Integer p_sequence = cursor_med_detail.getInt(cursor_med_detail.getColumnIndex("details_id"));
                                    Double p_quantity = cursor_med_detail.getDouble(cursor_med_detail.getColumnIndex("medicine_quantity"));
                                    String SUUID = UUID.randomUUID().toString().replace("-", "");
                                    String timeslot = cursor_med_detail.getString(cursor_med_detail.getColumnIndex("timeslot"));
                                    String p_datetime_set = dateFormat.format(dSchedule_start_date_temp) + " " + timeslot + ":00";

                                    db.insert_med_reminder_schedule(SUUID, p_id, "SCH", p_datetime_set, p_datetime_set,
                                            p_sequence, p_dosage_type, p_dosage, p_quantity, sMemberId, 8);

                                } while (cursor_med_detail.moveToNext());
                            }
                        }
                    }
                    else
                    {
                        if (cursor_med_detail.moveToFirst()) {
                            do {
                                Integer p_sequence = cursor_med_detail.getInt(cursor_med_detail.getColumnIndex("details_id"));
                                Double p_quantity = cursor_med_detail.getDouble(cursor_med_detail.getColumnIndex("medicine_quantity"));
                                String SUUID=UUID.randomUUID().toString().replace("-","");
                                String timeslot = cursor_med_detail.getString(cursor_med_detail.getColumnIndex("timeslot"));
                                String p_datetime_set = dateFormat.format(dSchedule_start_date_temp) + " " + timeslot + ":00";

                                db.insert_med_reminder_schedule(SUUID, p_id, "SCH", p_datetime_set, p_datetime_set,
                                        p_sequence, p_dosage_type, p_dosage, p_quantity, sMemberId, 8);

                            } while (cursor_med_detail.moveToNext());
                        }
                    }

                    if (p_birth_ctrl_total_days_Temp == (p_birth_ctrl_total_days + p_birth_ctrl_intervel_days))
                    {
                        p_birth_ctrl_total_days_Temp = 1;
                    }
                    else
                    {
                        p_birth_ctrl_total_days_Temp++;
                    }
                }



                dSchedule_start_date_temp = date_addDays(dSchedule_start_date_temp, 1);

            }
        }

        ConstData.create_json_from_table("reminder_schedule","A","",p_id,sMemberId,SelDate,this);





    }



    private void get_productsearch(String sSearchText) {

        showPdialog("Loading. . .");

        productheaderlist.clear();

        String url = String.format(AppConfig.URL_GET_SEARCHPRODUCT, sSearchText, "0", "s");

        RequestQueue queue = Volley.newRequestQueue(MRA_SetReminder.this);

        JsonObjectRequest jar_getsearchproduct = new JsonObjectRequest(Request.Method.GET,
                url,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Log.d(TAG, response.toString());

                try
                {
                    JSONArray jsonArray_search_pro = response.getJSONArray("medicine");
                    if (jsonArray_search_pro.length() <= 0) {


                    }

                    if (jsonArray_search_pro.length() > 0)
                    {
                        load_product_list(response.getJSONArray("medicine").toString());
                        //product_adapter.notifyDataSetChanged();
                    }

                    hidePDialog();



                } catch (JSONException e) {
                    hidePDialog();
                    e.printStackTrace();
                    f_alert_ok("Error : ", e.getMessage());
                    //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());

                // Toast.makeText(getApplicationContext(), "main Server Error :" + error.getMessage(), Toast.LENGTH_LONG).show();

                hidePDialog();
            }
        });



        queue.add(jar_getsearchproduct);

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
        new AlertDialog.Builder(MRA_SetReminder.this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void load_product_list(String p_search_product) {

        try {
            listItems.clear();
            JSONArray jsonArray_search_pro = new JSONArray(p_search_product);

            if (jsonArray_search_pro.length() <= 0) {
               // f_alert_ok("Reason Data", "Order Data not found on sever.");
            } else {


                // Parsing json
                for (int i = 0; i < jsonArray_search_pro.length(); i++) {
                    try {
                        JSONObject obj_json = jsonArray_search_pro.getJSONObject(i);

                        if (!obj_json.equals(null)) {
                           /* M_productlist oproductlist = new M_productlist();

                            oproductlist.setId(obj_json.getString("Id"));
                            oproductlist.setPrice(obj_json.getString("Price"));
                            oproductlist.setProductname(obj_json.getString("SearchName"));
                            oproductlist.setForm_Name(obj_json.getString("Form_Name"));
                            oproductlist.setPackSize(obj_json.getString("PackSize"));
                            oproductlist.setUOM(obj_json.getString("UOM"));


                            productheaderlist.add(oproductlist);*/

                            M_medicinelist o_M_medicine_add=new M_medicinelist();

                            o_M_medicine_add.setMed_id((obj_json.getString("Product_ID")));
                            o_M_medicine_add.setMedicineName(obj_json.getString("Product_Desc"));
                            o_M_medicine_add.setPackSize(obj_json.getString("Packsize"));
                            o_M_medicine_add.setUOM(obj_json.getString("ProductForm"));

                            listItems.add(o_M_medicine_add);


                        }
                    } catch (JSONException e) {
                        //  Toast.makeText(getApplicationContext(), "Error jsonexception", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                update_data();
                // product_adapter.notifyDataSetChanged();

            }
        } catch (JSONException e) {

            e.printStackTrace();
            //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }




    private void getIntenet()
    {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId  = pref.getString("memberid", "");
        ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
        test.add(db_new.getUserDetails());
        HashMap<String, String> m = test.get(0);
        nNewCityId = Integer.parseInt(m.get("city").toString());


    }

    public void tstdialog()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.med_rem_duation_select, null);
        builder.setView(dialogview);

        final ListView modeList = (ListView)dialogview.findViewById(R.id.listView2);
        String[]srrr=new String[]{"Continuous","number of days"};
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, android.R.id.text1, srrr);
        modeList.setAdapter(modeAdapter);
        final Button btncancel =(Button) dialogview.findViewById(R.id.btncancel);
        final Dialog dialog = builder.create();

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                CheckedTextView textView = (CheckedTextView) v;
                textView.setChecked(false);
                if (position == 0) {
                    // Toast.makeText(getApplicationContext(), "Continous", Toast.LENGTH_SHORT).show();
                    //def_pos_continuous();
                    duration_type_id=0;
                    textView.setChecked(true);
                    dialog.cancel();

                }
                if (position == 1) {
                    //Toast.makeText(getApplicationContext(), "u clicked 2", Toast.LENGTH_SHORT).show();
                    textView.setChecked(true);
                    duration_type_id=1;
                    number_of_days();
                }
            }


        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        //
        dialog.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dialog.show();

    }







    private int getWinWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public void onSelectDoctor(String Doc_id,String Doc_name,String email) {

        add_doctor.setText(Doc_name);
        Doctor_id=Doc_id;

    }

    private void get_all_medicine_dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.get_all_medicine_dialog, null);
        builder.setView(dialogview);
        dialog_all_med = builder.create();




        final Button btncancel = (Button)dialogview.findViewById(R.id.btncancel);
        final Button btnsave_med = (Button)dialogview.findViewById(R.id.btnsave_med);
        double_layout=(LinearLayout)dialogview. findViewById(R.id.double_layout);
        frst_part=(ImageView) dialogview.findViewById(R.id.frst_part);
        second_part=(ImageView) dialogview.findViewById(R.id.second_part);
        img_single=(ImageView) dialogview.findViewById(R.id.img_single);
        lst_all_med = (ListView)dialogview.findViewById(R.id.lst_all_med);
        final Button btnSelect = (Button)dialogview.findViewById(R.id.btnSelect);
        final TextView pill_heading = (TextView)dialogview.findViewById(R.id.pill_heading);
        final LinearLayout lnr_shape_colr_pick = (LinearLayout)dialogview.findViewById(R.id.linearLayout5);
       // img_med= (ImageView)dialogview.findViewById(R.id.img_med);

        med_name_txt=(TextView) dialogview.findViewById(R.id.med_name_txt);
        next_button= (Button)dialogview.findViewById(R.id.next_button);
        search_medicine= (CustomAutoCompleteView) dialogview.findViewById(R.id.search_medicine);

     /*   mContainer1 = (RelativeLayout)dialogview.findViewById(R.id.relativeLayout);
        mContainer2 = (RelativeLayout)dialogview.findViewById(R.id.relativeLayout_second);
        mContainer3 = (RelativeLayout)dialogview.findViewById(R.id.relativeLayout_third);*/

        initialize_color_shape_wheel_view(dialogview);

        // Initialize_image_scroll();

        lst_all_med.setClickable(true);


        add_medicine_button_layout=(LinearLayout)dialogview.findViewById(R.id.add_medicine_button_layout);
        add_new_medicine_layout=(RelativeLayout)dialogview.findViewById(R.id.add_new_medicine_layout);
        show_medicines_list=(LinearLayout)dialogview.findViewById(R.id.show_medicines_list);
        get_all_medicine();

        o_AllMedAdapter= new AllMedAdapter(this,all_medicine_list);
        lst_all_med.setAdapter(o_AllMedAdapter);



        search_medicine.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (search_medicine.getText().toString().length() > 1) {
                    get_productsearch(search_medicine.getText().toString());

                } else {
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refill_Packsize="-99";
            }
        });




     /*   add_new_medicine_layout.getViewTreeObserver().addOnPreDrawListener(    // this code is kept for reference by akhil
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        add_new_medicine_layout.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        add_new_medicine_layout.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(
                                0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec
                                .makeMeasureSpec(0,
                                        View.MeasureSpec.UNSPECIFIED);
                        add_new_medicine_layout.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0,
                                add_new_medicine_layout.getMeasuredHeight(),add_new_medicine_layout);
                        return true;
                    }
                });
*/

        btnsave_med.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                units = getResources().getStringArray(R.array.array_setdoseunit);
                unittAdapter = new ArrayAdapter<String>(
                        MRA_SetReminder.this, R.layout.rxspinner_simple_text_layout
                        ,units);

                Dosage_unit_selected.setAdapter(unittAdapter);
                Save_medicine_data(med_name_txt.getText().toString(), dialog_all_med);

            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                hideSoftKeyboard(search_medicine);
              //  imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                if(!search_medicine.getText().toString().isEmpty()) {
                    med_name_txt.setText(search_medicine.getText().toString());
                    add_medicine_button_layout.setVisibility(View.GONE);
                    add_new_medicine_layout.setVisibility(View.VISIBLE);
                    lnr_shape_colr_pick.setVisibility(View.VISIBLE);
                    Selected_medicine_name =search_medicine.getText().toString();
                }
                else if(Selected_medicine_name!=null)
                {
                    med_name_txt.setText(Selected_medicine_name);
                    add_medicine_button_layout.setVisibility(View.GONE);
                    lnr_shape_colr_pick.setVisibility(View.VISIBLE);
                    add_new_medicine_layout.setVisibility(View.VISIBLE);
                    show_previuos_schedule(Selected_Medicine_id);
                    if(ABS_image!=null)
                    {
                        ABS_image.setCurrentItem(Med_img_id);
                        ABS_image.setAllItemsVisible(true);

                      //  ABS_image.invalidate();


                    }
                    if(ABS_firstColors!=null)
                    {
                        ABS_firstColors.setCurrentItem(Med_img_color_first_id);
                        ABS_firstColors.setAllItemsVisible(true);


                    }
                    if(ABS_SecondColors!=null)
                    {
                        if (Med_img_color_second_id!=-99) {
                            ABS_SecondColors.setCurrentItem(Med_img_color_second_id);
                            ABS_SecondColors.setAllItemsVisible(true);
                        }
                    }


                    if(Med_img_color_second_id!=-99)
                    {
                        double_layout.setVisibility(View.VISIBLE);
                        frst_part.setColorFilter(ConstData.colr_array[Med_img_color_first_id], PorterDuff.Mode.MULTIPLY);
                        second_part.setColorFilter(ConstData.colr_array[Med_img_color_second_id], PorterDuff.Mode.MULTIPLY);
                    }else {
                        double_layout.setVisibility(View.GONE);
                       //img_med.setImageResource(ConstData.Medicine_array1[Med_img_id]);
                       // img_med.setColorFilter(ConstData.colr_array[Med_img_color_first_id], PorterDuff.Mode.MULTIPLY);
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "please type a medicine name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pill_heading.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (lnr_shape_colr_pick.getVisibility() == View.VISIBLE) {
                    lnr_shape_colr_pick.setVisibility(View.GONE);
                } else {
                    lnr_shape_colr_pick.setVisibility(View.VISIBLE);
                }
            }


        });


        lst_all_med.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                hideSoftKeyboard(search_medicine);

                try {
                    for (int i = 0; i < all_medicine_list.size(); i++) {
                        RadioButton rdbchecked = (RadioButton) lst_all_med.getChildAt(i).findViewById(R.id.chk_medicine);
                        if (rdbchecked != null) {
                            rdbchecked.setChecked(false);
                        }

                    }
                }catch (Exception e)
                {
                    e.toString();
                }
                RadioButton rdb= (RadioButton)v.findViewById(R.id.chk_medicine);

                if (rdb.isChecked()) {

                    rdb.setChecked(false);

                }else
                {
                    rdb.setChecked(true);

                    String gt_Product_id = (String) v.getTag(R.id.key_product_id);
                    String productname = (String) v.getTag(R.id.key_product_name);
                    Integer medicine_image_id =(Integer) v.getTag(R.id.key_medicine_image_id);
                    Integer f_color_id =(Integer) v.getTag(R.id.key_first_color_id);
                    Med_img_color_second_id =(Integer) v.getTag(R.id.key_second_color_id);

                    if(productname!=null)
                    {
                        pick_medicine.setText(productname);
                        Selected_medicine_name =productname;
                    }

                    if(gt_Product_id!=null)
                    {
                        Selected_Medicine_id=gt_Product_id;
                    }

                    if(medicine_image_id!=null)
                    {
                        Med_img_id=medicine_image_id;
                    }
                    if(f_color_id!=null)
                    {
                        Med_img_color_first_id=f_color_id;
                    }



                }






            }
        });


        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog_all_med.cancel();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog_all_med.cancel();
            }
        });
        dialog_all_med.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dialog_all_med.show();
        dialog_all_med.getWindow().getDecorView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(search_medicine);
               // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });


    }


    public class AllMedAdapter extends ArrayAdapter<M_medicinelist> {
        public AllMedAdapter(Context context, ArrayList<M_medicinelist> address) {

            super(context, 0, address);

        }

        @Override
        public View getView(final int position,  View convertView, ViewGroup parent) {
            // Get the data item for this position


            M_medicinelist all_med_data = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_medicine_dialog, parent, false);
            }
            // Lookup view for data population
            final TextView txtmedicine = (TextView) convertView.findViewById(R.id.txtmedicine);
            final LinearLayout imgmedicine = (LinearLayout) convertView.findViewById(R.id.imgmedicine);
            final ImageView img_delete = (ImageView) convertView.findViewById(R.id.del_med_img);

            final RadioButton radio = (RadioButton) convertView.findViewById(R.id.chk_medicine);

            convertView.setTag(R.id.key_product_id,all_med_data.getMed_id());
            convertView.setTag(R.id.key_product_name, all_med_data.getMedicineName());
            convertView.setTag(R.id.key_first_color_id, all_med_data.getFirst_color_id());
            convertView.setTag(R.id.key_second_color_id, all_med_data.getSecond_color_id());
            convertView.setTag(R.id.key_medicine_image_id, all_med_data.getImage_id());



            radio.setTag(R.id.key_product_id,all_med_data.getMed_id());
            radio.setTag(R.id.key_product_name, all_med_data.getMedicineName());
            radio.setTag(R.id.key_first_color_id, all_med_data.getFirst_color_id());
            radio.setTag(R.id.key_second_color_id, all_med_data.getSecond_color_id());
            radio.setTag(R.id.key_medicine_image_id, all_med_data.getImage_id());

            img_delete.setTag(R.id.key_product_id,all_med_data.getMed_id());
            // convertView.setTag(R.id.key_medicine_status, all_med_data.getStatus());

            txtmedicine.setText(all_med_data.getMedicineName());

            add_image_views(imgmedicine,all_med_data.getImage_id(),all_med_data.getFirst_color_id(),all_med_data.getSecond_color_id(),new LinearLayout.LayoutParams(15,30));

            if(position==selected_position)
            {
                radio.setChecked(true);

            }
            else
            {
                radio.setChecked(false);

            }



            radio.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (((RadioButton) v).isChecked()) {
                        selected_position = position;


                        String gt_Product_id = (String) v.getTag(R.id.key_product_id);
                        String productname = (String) v.getTag(R.id.key_product_name);
                        Integer medicine_image_id =(Integer) v.getTag(R.id.key_medicine_image_id);
                        Integer f_color_id =(Integer) v.getTag(R.id.key_first_color_id);


                      //  Med_actual_id


                        if( v.getTag(R.id.key_second_color_id)==null)
                        {
                            Med_img_color_second_id =null;
                        }else
                        {Med_img_color_second_id =(Integer) v.getTag(R.id.key_second_color_id);}

                        if(productname!=null)
                        {
                            pick_medicine.setText(productname);
                            Selected_medicine_name =productname;
                        }

                        if(gt_Product_id!=null)
                        {
                            Selected_Medicine_id=gt_Product_id;
                        }
                        if(medicine_image_id!=null)
                        {
                            Med_img_id=medicine_image_id;
                        }
                        if(f_color_id!=null)
                        {
                            Med_img_color_first_id=f_color_id;
                        }




                    }else
                    {
                        selected_position = -1;

                    }
                    notifyDataSetChanged();





                }
            });


            img_delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    delete_medicine((String) view.getTag(R.id.key_product_id));

                }
            });




            return convertView;
        }
    }
    public Date getDate_Format(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        try {
            current_date = getDate_Format(year,monthOfYear,dayOfMonth);
            SelDate=dateFormat_query.format(current_date);
            setcycleDate=SelDate;

        } catch (Exception e) {
        }
        select_date.setText("Starting From "+dateFormat_month_in_words.format(current_date));

    }

    private void select_date()
    {

        try {
            current_date = dateFormat_query.parse(SelDate);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {

            nYear=Integer.parseInt(sdYear.format(current_date));
            nMonth=Integer.parseInt(sdMonth.format(current_date))-1;
            Nday=Integer.parseInt(sdDay.format(current_date));

        }catch (NumberFormatException e)
        {
            e.printStackTrace();
        }


        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                nYear,
                nMonth,
                Nday
        );

        dpd.show(getFragmentManager(), "Datepickerdialog");

    }


    private void Set_reminder_time()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(MRA_SetReminder.this);
        final View dialog = inflater.inflate(R.layout.med_rem_reminder_set_reminder, null);
        builder.setView(dialog);
        final Dialog dlg = builder.create();

        final Button btncancel= (Button)dialog.findViewById(R.id.btncancel);
        final Button btnok= (Button)dialog.findViewById(R.id.btnok);
        final TextView pick_ringtone= (TextView)dialog.findViewById(R.id.pick_ringtone);
        final TextView pick_inervel_times= (TextView)dialog.findViewById(R.id.pick_inervel_times);
        final Spinner reminder_frequency= (Spinner)dialog.findViewById(R.id.reminder_frequency);
        final CheckBox no_rem_chk= (CheckBox)dialog.findViewById(R.id.no_rem_chk);


        final String[] array_frequency = getResources().getStringArray(R.array.medadd_frequency);

        ArrayAdapter autocompletetextAdapter = new ArrayAdapter<String>(
                this, R.layout.rxspinner_simple_text_layout
                ,array_frequency);

        reminder_frequency.setAdapter(autocompletetextAdapter);
        reminder_frequency.setSelection(1);

        no_rem_chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    reminder_flag=1;
                    Toast.makeText(getApplicationContext(), "No notification will come for the reminder", Toast.LENGTH_SHORT).show();
                }else
                {
                    reminder_flag=0;
                    Toast.makeText(getApplicationContext(), "Reminder notification enabled", Toast.LENGTH_SHORT).show();
                }

            }
        });

        reminder_frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                frq_pos =  Integer.parseInt(String.valueOf(id));

                reminder_type_id = Integer.parseInt(String.valueOf(id));
                reminder_type = array_frequency[position].toString();
                try {

                    Cursor cursor_data = db.getAllDataMedTimeslot(frq_pos);

                    if (cursor_data.moveToFirst()) {
                        ArrayList<String> timeSlots = new ArrayList<String>();
                        while (!cursor_data.isAfterLast()) {
                            timeSlots.add(cursor_data.getString(cursor_data.getColumnIndex("time_slots")));
                            cursor_data.moveToNext();
                        }
                        cursor_data.close();


                        btnSetReminder.setText(reminder_type + " (" + TextUtils.join(",", timeSlots) + ")");
                    }
                }catch (Exception e)
                {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                db.delete_med_rem_details(new_rem_id);
                db.addMedDetailsfinal(new_rem_id, frq_pos);
                dlg.cancel();
            }
        });
        pick_ringtone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setUpListViewRingtones(pick_ringtone);
            }
        });

        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                db.delete_med_rem_details(new_rem_id);
                db.addMedDetailsfinal(new_rem_id, frq_pos);
                dlg.cancel();
            }
        });
        pick_inervel_times.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(frq_pos!=-99) {
                    Set_reminder_dialog();
                }else
                {
                    Toast.makeText(getApplicationContext(), "please select frequency", Toast.LENGTH_SHORT).show();
                }
                // dlg.cancel();
            }
        });
        dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dlg.show();
    }


    private void get_all_medicine() {

        Cursor cursor_all_medicine = db.get_med_all_medicine_master(sMemberId);
        all_medicine_list.clear();



        if ((cursor_all_medicine != null) || (cursor_all_medicine.getCount()> 0))
        {
            if (cursor_all_medicine.moveToFirst()) {
                do {
                    // m_medicine_list O_medicine_list = new m_medicine_list();
                    M_medicinelist o_medicinelist= new M_medicinelist();
                    o_medicinelist.setMed_id(cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("medicine_id")));
                    o_medicinelist.setMedicineName(cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("medicine_name")));
                    o_medicinelist.setImage_id(cursor_all_medicine.getInt(cursor_all_medicine.getColumnIndex("image_id")));
                    o_medicinelist.setFirst_color_id(cursor_all_medicine.getInt(cursor_all_medicine.getColumnIndex("first_color_id")));
                    o_medicinelist.setSecond_color_id(cursor_all_medicine.getInt(cursor_all_medicine.getColumnIndex("second_color_id")));


                    all_medicine_list.add(o_medicinelist);
                } while (cursor_all_medicine.moveToNext());


            }
        }


        if(all_medicine_list.size()>0)
        {

            show_medicines_list.setVisibility(View.VISIBLE);


        }else
        {
            show_medicines_list.setVisibility(View.GONE);
        }


    }

    private void init_rxSpinners()
    {


        final String[] condition_array = getResources().getStringArray(R.array.array_category);
        ArrayAdapter autocompletetextAdapter = new ArrayAdapter<String>(
                this, R.layout.rxspinner_simple_text_layout
                ,condition_array);


        select_condition.setAdapter(autocompletetextAdapter);

        select_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_condition = String.valueOf(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[]srrr=new String[]{"Continuous","number of days"};
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, R.layout.rxspinner_simple_text_layout, srrr);
        select_duration.setAdapter(modeAdapter);




        select_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(num_of_days_check==0) {
                    if (position == 0) {
                        duration_type_id = 0;
                    }
                    if (position == 1) {
                        duration_type_id = 1;
                        number_of_days();
                    }
                }
                num_of_days_check=0;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        select_frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(frequency_check==0)
                {
                daysIntervelId = position;
                if (position == 0) {
                    everyday = 1;
                    //Toast.makeText(getApplicationContext(), "Every Day", Toast.LENGTH_SHORT).show();

                }
                if (position == 1) {
                    everyday = 0;
                    pick_days();
                }

                if (position == 2) {
                    everyday = 0;
                    days_intervel();
                }

                if (position == 3) {
                    everyday = 0;
                    f_set_cycle();
                }
                }
                frequency_check=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[]freq=new String[]{"every day","specific days of week","days interval","birth control cycle"};
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<String>(this, R.layout.rxspinner_simple_text_layout, freq);
        select_frequency.setAdapter(freqAdapter);





         units = getResources().getStringArray(R.array.array_setdoseunit);
       unittAdapter = new ArrayAdapter<String>(
                this, R.layout.rxspinner_simple_text_layout
                ,units);

        Dosage_unit_selected.setAdapter(unittAdapter);

        Dosage_unit_selected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDosageunit = units[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    private void init_tabledata() {
        //Integer g = db.getMeddetial();

        new_rem_id=UUID.randomUUID().toString().replace("-","");
        // nCount = db.getMaxid_med_reminder(sMemberId);
        Cursor cursor_empty_fields = db.get_med_Empty_data(sMemberId);

        if ((cursor_empty_fields != null) || (cursor_empty_fields.getCount()> 0))
        {
            if (cursor_empty_fields.moveToFirst()) {
                do {
                    db.Delete_empty_fields(cursor_empty_fields.getString(cursor_empty_fields.getColumnIndex("reminder_id")));
                } while (cursor_empty_fields.moveToNext());
            }
        }


        db.addMedRemMaster_null_values(new_rem_id,sMemberId,8);




        db.Delete_TimeslotMaster();


        db.addMed_timeslot_master(1, new_rem_id, "08:00", 8, 1, 1);
        db.addMed_timeslot_master(2, new_rem_id, "09:00", 9, 1, 1);
        db.addMed_timeslot_master(3, new_rem_id, "09:30", 9, 1, 1);
        db.addMed_timeslot_master(4, new_rem_id, "10:00", 10, 1, 1);
        db.addMed_timeslot_master(5, new_rem_id, "10:30", 10, 1, 1);
        db.addMed_timeslot_master(6, new_rem_id, "11:00", 11, 1, 1);
        db.addMed_timeslot_master(7, new_rem_id, "11:30", 11, 1, 1);
        db.addMed_timeslot_master(8, new_rem_id, "12:00", 12, 1, 1);
        db.addMed_timeslot_master(9, new_rem_id, "12:30", 12, 1, 1);
        db.addMed_timeslot_master(10, new_rem_id, "13:00", 13, 2, 1);
        db.addMed_timeslot_master(11, new_rem_id, "13:30", 13, 2, 1);
        db.addMed_timeslot_master(12, new_rem_id, "14:00", 14, 2, 1);
        db.addMed_timeslot_master(13, new_rem_id, "15:00", 15, 2, 1);
        db.addMed_timeslot_master(14, new_rem_id, "15:30", 15, 2, 1);
        db.addMed_timeslot_master(15, new_rem_id, "16:00", 16, 2, 1);
        db.addMed_timeslot_master(16, new_rem_id, "16:30", 16, 2, 1);
        db.addMed_timeslot_master(17, new_rem_id, "17:00", 17, 3, 1);
        db.addMed_timeslot_master(18, new_rem_id, "17:30", 17, 3, 1);
        db.addMed_timeslot_master(19, new_rem_id, "18:00", 18, 3, 1);
        db.addMed_timeslot_master(20, new_rem_id, "18:30", 18, 3, 1);
        db.addMed_timeslot_master(21, new_rem_id, "19:00", 19, 3, 1);
        db.addMed_timeslot_master(22, new_rem_id, "19:30", 19, 3, 1);
        db.addMed_timeslot_master(23, new_rem_id, "20:00", 20, 3, 1);


    }







    private void add_pill_buddyr_combo_box()
    {


        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        final ListView lv = (ListView)dialogview. findViewById(R.id.list_view);


        builder.setView(dialogview);
        // Adding items to listview
        final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
        final ImageView btnDoctorcancel = (ImageView) dialogview.findViewById(R.id.btnDoctorcancel);
        final TextView cancelbtn = (TextView) dialogview.findViewById(R.id.btncancel);

        final LinearLayout addpillbuddyShowHide = (LinearLayout) dialogview.findViewById(R.id.addpillbuddyShowHide);
        final Button btn_add_pill_buddy = (Button) dialogview.findViewById(R.id.btn_add_pill_buddy);


        addpillbuddyShowHide.setVisibility(View.VISIBLE);
        fill_pill_buddy();

        pillbuddyadapter = new AD_adapterCombo(this, R.layout.list_item,ObjectItempillBuddy);
        lv.setAdapter(pillbuddyadapter);

        final TextView Titile =(TextView)dialogview.findViewById(R.id.textView);

        Titile.setText("Select pill buddy");

        final TextView btncancel=(TextView)dialogview.findViewById(R.id.btncancel);
        final Dialog dlg= builder.create();
        lv.setOnItemClickListener(new OnItemClickListenerListViewpillbuddy(dlg));




        btn_add_pill_buddy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dlg.cancel();
                Show_add_MediFriends();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dlg.cancel();
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
        dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dlg.show();
    }


    public class OnItemClickListenerListViewpillbuddy implements AdapterView.OnItemClickListener {
        Dialog d=null;
        OnItemClickListenerListViewpillbuddy(Dialog dlg)
        {
            d=dlg;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Context context = view.getContext();


            TextView textViewItem = ((TextView) view.findViewById(R.id.product_name));
            String listItemText = textViewItem.getText().toString();
            String listItemId = textViewItem.getTag().toString();
            //sDoctId = Integer.parseInt(listItemId);
            pillbuddy_id=listItemId;
            add_pill_buddy.setText(listItemText);



            d.cancel();

        }
    }





    private void fill_pill_buddy() {

        Cursor cursor_all_medicine = db.get_all_pill_buddyr(sMemberId);
        int i = 0;

        if ((cursor_all_medicine != null) || (cursor_all_medicine.getCount() > 0)) {
            ObjectItempillBuddy = new ObjectItem[cursor_all_medicine.getCount()];
        }

        if (cursor_all_medicine.moveToFirst()) {
            do {


                ObjectItempillBuddy[i] = new ObjectItem(Integer.parseInt(cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("medfriend_id"))),cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("reminder_friendname")));
                i++;
            } while (cursor_all_medicine.moveToNext());


        }
    }

    private void Show_add_MediFriends(){
        Intent Intenet_add = new Intent(this, MRA_PillBuddy.class);
        startActivity(Intenet_add);
    }


    public class AutocompleteCustomArrayAdapter extends ArrayAdapter<M_medicinelist> {


        Context mContext;

        public AutocompleteCustomArrayAdapter(Context mContext,ArrayList<M_medicinelist> med_data) {

            super(mContext,0,med_data);


            this.mContext = mContext;

        }

        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {

            try{
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.med_autocomplete_layout, parent, false);
                }

                M_medicinelist med_data = getItem(position);
                final LinearLayout lnr_med_main = (LinearLayout) convertView.findViewById(R.id.lnr_med_main);

                final TextView product_name= (TextView) convertView.findViewById(R.id.product_name);
                final ImageView med_img= (ImageView) convertView.findViewById(R.id.med_img);

                lnr_med_main.setTag(R.id.key_product_name, med_data.getMedicineName());
                lnr_med_main.setTag(R.id.key_uom, med_data.getUOM());
                lnr_med_main.setTag(R.id.key_pack_size, med_data.getPackSize());
                lnr_med_main.setTag(R.id.key_product_actual_id, med_data.getMed_id());

                product_name.setText(med_data.getMedicineName());


                lnr_med_main.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {


                        hideSoftKeyboard(search_medicine);
                        String medicine_name = (String) view.getTag(R.id.key_product_name);
                        if(medicine_name!=null)
                        {
                            pick_medicine.setText(medicine_name);
                            med_name_txt.setText(medicine_name);
                            Selected_medicine_name =medicine_name;
                            refill_Packsize=(String) view.getTag(R.id.key_pack_size);
                            units=new String[]{(String) view.getTag(R.id.key_uom)};
                            Med_actual_id=(String) view.getTag(R.id.key_product_actual_id);
                            unittAdapter = new ArrayAdapter<String>(
                                    MRA_SetReminder.this, R.layout.rxspinner_simple_text_layout
                                    ,units);

                            Dosage_unit_selected.setAdapter(unittAdapter);
                        }

                     /*   if(gt_Product_id!=null)
                        {
                            Selected_Medicine_id=String.valueOf(gt_Product_id);
                        }
                        if(medicine_array!=null)
                        {
                            selecte_medicine_array=medicine_array;
                        }else
                        {
                            selecte_medicine_array="Medicine_array";
                        }
                        if(medicine_image_id!=null)
                        {
                            selected_medicineimage_id=String.valueOf(medicine_image_id);
                        }else {
                            selected_medicineimage_id="1";
                        }*/



                        add_medicine_button_layout.setVisibility(View.GONE);
                        add_new_medicine_layout.setVisibility(View.VISIBLE);


                    }
                });

                convertView.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            InputMethodManager imm = (InputMethodManager) getContext()
                                    .getSystemService(
                                            Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(
                                    product_name.getWindowToken(), 0);
                        }

                        return false;
                    }
                });


            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;

        }


    }


    private void Save_medicine_data(String Med_Name,Dialog dlg)
    {

        Cursor cursor_data = db.get_search_medicine(Med_Name,sMemberId);

        if ((cursor_data == null) || (cursor_data.getCount()<=0)){


            String SUUID=UUID.randomUUID().toString().replace("-","");
            long id= db.insert_Medicine_master_value(SUUID,Med_Name,Med_img_id, Med_img_color_first_id,Med_img_color_second_id,sMemberId,8,Med_actual_id);
            ConstData.create_json_from_table("medicine_master","A",SUUID,"0",sMemberId,SelDate,this);
            if(id>0)
            {
                pick_medicine.setText(Med_Name);
                Selected_Medicine_id=SUUID;
                dlg.cancel();
            }else{
                dlg.cancel();
            }
        }
        else
        {
            cursor_data.moveToFirst();

            String medicine_id = cursor_data.getString(cursor_data.getColumnIndex("medicine_id"));
            db.update_Medicine_master(Med_Name, Med_img_id, Med_img_color_first_id, Med_img_color_second_id,medicine_id);
            ConstData.create_json_from_table("medicine_master","E",medicine_id,"0",sMemberId,SelDate,this);
            pick_medicine.setText(Med_Name);


            dlg.cancel();
        }

        add_image_views(img_selected_med,Med_img_id,Med_img_color_first_id,Med_img_color_second_id,param_custom);
        pick_medicine.setText(Med_Name);
        img_selected_med.setVisibility(View.VISIBLE);
    }

    private void delete_medicine(final String p_id) {


        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.delete_medicine_master, null);
        builder.setView(dialogview);
        final Dialog dlg= builder.create();
        final Button okbtn = (Button) dialogview.findViewById(R.id.btnok);
        final Button btncancel = (Button) dialogview.findViewById(R.id.btncancel);




        btncancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                dlg.cancel();
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                String reminder_id="0";

                Cursor cursor_remid=db.get_reminder_id_of_Medicine(p_id,sMemberId);



                if ((cursor_remid != null) || (cursor_remid.getCount()> 0)) {
                    if (cursor_remid.moveToFirst()) {
                        do {
                            reminder_id=cursor_remid.getString(cursor_remid.getColumnIndex("reminder_id"));
                            db.Delete_Medicine_master_details(reminder_id,sMemberId);
                        } while (cursor_remid.moveToNext());
                    }
                }

                db.Delete_Medicine_master(p_id,sMemberId);

                dlg.cancel();
                dialog_all_med.cancel();
                get_all_medicine_dialog();

            }
        });
        dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dlg.show();
    }

    public void f_set_Dosage()
    {
        int iDosage = 1 ;
        try{
            iDosage= Integer.parseInt(Dosage_Amount.getText().toString().trim());
        }catch(NumberFormatException ex){ // handle your exception

        }
        numerdialog myDiag = numerdialog.newInstance(1,iDosage,0,250,"Dosage_Amount","Select Dosage");
        myDiag.show(getFragmentManager(), "Diag");

    }

    private void setUpListViewRingtones(final TextView pick_ringtone) {

        LayoutInflater inflater = LayoutInflater.from(MRA_SetReminder.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(MRA_SetReminder.this);
        final View dialog = inflater.inflate(R.layout.activity_select_rigtone, null);
        builder.setView(dialog);
        final Dialog dlg = builder.create();

        final TextView btncancel= (TextView)dialog.findViewById(R.id.btncancel);
        final Button btnok= (Button)dialog.findViewById(R.id.btnok);
        final ListView list_view= (ListView)dialog.findViewById(R.id.list_view);
        List<M_alarmsoundlist> sounds = new ArrayList<M_alarmsoundlist>();
        sounds.addAll(getStandardAlarms());
        alarmadapter =
                new AD_alarmsound(this, sounds);

        list_view.setAdapter(alarmadapter);
        if(selected_tone!=null)
        {
            try {
                setSelected(Uri.parse(selected_tone));
            }catch (Exception e)
            {}
        }

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dlg.cancel();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alarmadapter.player.stop();
                saveCurrentSoundAndExit(pick_ringtone);
                dlg.cancel();
            }
        });
        dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dlg.show();
    }

    private List<M_alarmsoundlist> getStandardAlarms() {
        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_ALARM | RingtoneManager.TYPE_RINGTONE);
        List<M_alarmsoundlist> sounds = new ArrayList<M_alarmsoundlist>();
        Cursor cursor = manager.getCursor();
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext(), i++) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            sounds.add(new M_alarmsoundlist(title, manager.getRingtoneUri(i)));
        }
        cursor.deactivate();
        return sounds;
    }

  /*  public class SoundClickListener {

        public void onClick(int position) {
            M_alarmsoundlist sound = alarmadapter.getItem(position);
            // Open file dialog if "Custom" option selected.
            if (position == 0) {
                if (player.isPlaying()) {
                    player.stop();
                }
                return;
            }
            // Play music otherwise.
            try {
                player.reset();
                player.setDataSource(MRA_SetReminder.this, sound.getUri());
                player.prepare();
                player.start();
            } catch (Exception e) {
                // Don't know what should I do here.
                Log.e(getClass().getName(), "Can't play sound", e);
            }
        }
    }*/

    private void saveCurrentSoundAndExit(TextView ringtext) {
        Uri uri = alarmadapter.getItem(alarmadapter.getSelected()).getUri();
        ringtext.setText(alarmadapter.getItem(alarmadapter.getSelected()).getTitle());
        selected_tone=uri.toString();
      //  globalVariable.setCurrentAlarmSound(uri);

    }
    private void setSelected(Uri sound) {
        // Skip sound at position 0, because it is custom sound
        // with uri equals to null.
        for (int i = 0; i < alarmadapter.getCount(); i++) {
            if (alarmadapter.getItem(i).getUri().equals(sound)) {
                alarmadapter.setSelected(i);
                return;
            }
        }
        alarmadapter.getItem(0).setUri(sound);
        alarmadapter.setSelected(0);
    }


    private void add_image_views(LinearLayout lnr,Integer Image_id,Integer f_color_id,Integer s_color_id, LinearLayout.LayoutParams customparam )
    {
        lnr.removeAllViews();
        img_first_part = new ImageView(this);
        img_second_part = new ImageView(this);
        // LinearLayout.LayoutParams param = new LinearLayout.LayoutParams();
        customparam.gravity=Gravity.CENTER_VERTICAL;
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


    private void initialize_color_shape_wheel_view(View view)
    {
        ABS_image = (AbstractWheel)view. findViewById(R.id.ABS_image);
        ABS_firstColors= (AbstractWheel)view. findViewById(R.id.ABS_firstColors);
        ABS_SecondColors = (AbstractWheel)view. findViewById(R.id.ABS_SecondColors);


        final AbstractWheelTextAdapter minAdapter = new AbstractWheelTextAdapter(this, ConstData.Medicine_array1, "image");
        minAdapter.setItemResource(R.layout.wheel_text_centered_dark_back);
        minAdapter.setItemTextResource(R.id.text);
        ABS_image.setViewAdapter(minAdapter);

        //  minAdapter.



        AbstractWheelTextAdapter colorAdapter = new AbstractWheelTextAdapter(this, ConstData.colr_array, "");
        colorAdapter.setItemResource(R.layout.wheel_text_centered_dark_back);
        ABS_firstColors.setViewAdapter(colorAdapter);

        AbstractWheelTextAdapter seccolorAdapter = new AbstractWheelTextAdapter(this, ConstData.colr_array, "");
        seccolorAdapter.setItemResource(R.layout.wheel_text_centered_dark_back);
        ABS_SecondColors.setViewAdapter(seccolorAdapter);



        ABS_firstColors.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {

                ABS_image.setAllItemsVisible(true);
                ABS_firstColors.setAllItemsVisible(true);
                ABS_SecondColors.setAllItemsVisible(true);


                img_single.setColorFilter(ConstData.colr_array[newValue], PorterDuff.Mode.MULTIPLY);
                frst_part.setColorFilter(ConstData.colr_array[newValue], PorterDuff.Mode.MULTIPLY);
                Med_img_color_first_id=newValue;
               ABS_image.invalidate();
            }
        });

        ABS_SecondColors.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                ABS_image.setAllItemsVisible(true);
                ABS_firstColors.setAllItemsVisible(true);
                ABS_SecondColors.setAllItemsVisible(true);

                Med_img_color_second_id=newValue;


                second_part.setColorFilter(ConstData.colr_array[newValue], PorterDuff.Mode.MULTIPLY);
                ABS_image.invalidate();
            }
        });



        ABS_image.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
              //  ABS_image.invalidate();
                ABS_image.setAllItemsVisible(true);
                ABS_firstColors.setAllItemsVisible(true);
                ABS_SecondColors.setAllItemsVisible(true);
                if(newValue==2)
                {
                   ABS_SecondColors.setVisibility(View.VISIBLE);
                    img_single.setVisibility(View.INVISIBLE);
                    double_layout.setVisibility(View.VISIBLE);
                }else {
                    Med_img_color_second_id=-99;
                    img_single.setVisibility(View.VISIBLE);
                    double_layout.setVisibility(View.INVISIBLE);
                    ABS_SecondColors.setVisibility(View.INVISIBLE);
                    img_single.setImageResource(ConstData.Medicine_array1[newValue]);
                }

                Med_img_id=newValue;



            }
        });

    }

    private void show_previuos_schedule(String Med_id)
    {
        Cursor cursor_empty_fields = db.get_previous_reminder_data(sMemberId,Med_id);

        if ((cursor_empty_fields != null) || (cursor_empty_fields.getCount()> 0))
        {
            if (cursor_empty_fields.moveToFirst()) {
                do {

                    selected_tone=cursor_empty_fields.getString(cursor_empty_fields.getColumnIndex("ringtone"));
                    setdosAmount =cursor_empty_fields.getInt(cursor_empty_fields.getColumnIndex("dosage_value"));
                    Dosage_Amount.setText(Integer.toString(setdosAmount));
                    setDosageunit=cursor_empty_fields.getString(cursor_empty_fields.getColumnIndex("dosage_type"));
                   Dosage_unit_selected.setSelection(getIndex(Dosage_unit_selected, setDosageunit));



                    try {
                        current_date = Calendar.getInstance().getTime();
                        SelDate=dateFormat_query.format(current_date);
                        setcycleDate=SelDate;

                    } catch (Exception e) {
                    }
                    select_date.setText("Starting From "+dateFormat_month_in_words.format(current_date));



                    duration_type_id=cursor_empty_fields.getInt(cursor_empty_fields.getColumnIndex("schedule_duration_type_id"));

                    duration_value=cursor_empty_fields.getInt(cursor_empty_fields.getColumnIndex("schedule_duration_value"));


                    select_duration.setSelection(duration_type_id);
                    num_of_days_check=1;
                    try {
                        number_ofDays = String.valueOf(duration_value);
                        selected_days.getJSONObject(0).put("reminder_id", new_rem_id);
                        selected_days.getJSONObject(1).put("selected_days", number_ofDays);
                    }catch (JSONException e)
                    {}


                    daysIntervelId=cursor_empty_fields.getInt(cursor_empty_fields.getColumnIndex("days_intervel_type_id"));
                    daysIntervelValue=cursor_empty_fields.getString(cursor_empty_fields.getColumnIndex("days_intervel_value"));

                    select_frequency.setSelection(daysIntervelId);

                    frequency_check=1;
                    sel_condition=cursor_empty_fields.getString(cursor_empty_fields.getColumnIndex("condition"));
                   // select_condition.setSelection(getIndex(select_condition,sel_condition));

                    select_condition.setSelection(Integer.valueOf(sel_condition)+1);


                    frq_pos =  cursor_empty_fields.getInt(cursor_empty_fields.getColumnIndex("reminder_type_id"));

                    reminder_type_id = frq_pos;
                    reminder_type = cursor_empty_fields.getString(cursor_empty_fields.getColumnIndex("reminder_value"));
                    try {

                        Cursor cursor_data = db.getAllDataMedTimeslot(frq_pos);

                        if (cursor_data.moveToFirst()) {
                            ArrayList<String> timeSlots = new ArrayList<String>();
                            while (!cursor_data.isAfterLast()) {
                                timeSlots.add(cursor_data.getString(cursor_data.getColumnIndex("time_slots")));
                                cursor_data.moveToNext();
                            }
                            cursor_data.close();


                            btnSetReminder.setText(reminder_type + " (" + TextUtils.join(",", timeSlots) + ")");
                        }
                    }catch (Exception e)
                    {

                    }

                    db.delete_med_rem_details(new_rem_id);
                    db.addMedDetailsfinal(new_rem_id, frq_pos);


                } while (cursor_empty_fields.moveToNext());
            }
        }
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

private  void insert_placebo()
{
   if(!db.check_insert_placebo(sMemberId)) {
       long id = db.insert_Medicine_master_value(PLACEBO_ID, "Placebo", 0, 0, Med_img_color_second_id, sMemberId, 8,Med_actual_id);


      /* db. addMedRemMaster_insert_from_server(PLACEBO_ID,
               PLACEBO_ID,
              "Placebo",0,"0",0,0,"0",0,"0",0,0,0,-99,"0",0,"0","0","0","0",sMemberId,8,"","");
*/

   }
}
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
      //  imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                   // hideSoftKeyboard(MRA_SetReminder.this);
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
    public  void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow( view.getWindowToken(), 0);
    }

    private  void refill_reminder_selection_dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MRA_SetReminder.this);
        final View dialog = inflater.inflate(R.layout.mr_refill_dialog, null);
        builder.setView(dialog);
        final Dialog dlg = builder.create();



        final Button btnok= (Button)dialog.findViewById(R.id.btn_ok);
        final Button btncancel= (Button)dialog.findViewById(R.id.btncancel);
        final TextView refill_units= (TextView)dialog.findViewById(R.id.refill_units);
        final LinearLayout lnr_pack_size= (LinearLayout)dialog.findViewById(R.id.lnr_pack_size);
        final TextView txt_refill_packsize= (TextView)dialog.findViewById(R.id.txt_refill_packsize);
        final TextView txt_select_qty= (TextView)dialog.findViewById(R.id.txt_select_qty);
        final TextView edt_total_units= (TextView)dialog.findViewById(R.id.edt_total_units);
        final LinearLayout lnr_qty= (LinearLayout)dialog.findViewById(R.id.lnr_qty);
        final Button btnplus  = (Button)dialog.findViewById(R.id.btnplus);
        final Button btnminus = (Button)dialog.findViewById(R.id.btnminus);
        final TextView Qty= (TextView)dialog.findViewById(R.id.Qty);


        refill_units.setText(setDosageunit);

        if(!refill_Packsize.equals("-99"))
        {
            lnr_pack_size.setVisibility(View.VISIBLE);
            txt_refill_packsize.setText(refill_Packsize);
            edt_total_units.setEnabled(false);
            edt_total_units.setText(""+refill_Packsize);
            txt_select_qty.setVisibility(View.VISIBLE);
            lnr_qty.setVisibility(View.VISIBLE);
        }else
        {
            lnr_pack_size.setVisibility(View.GONE);
            txt_select_qty.setVisibility(View.GONE);
            lnr_qty.setVisibility(View.GONE);
        }


        btnok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(!edt_total_units.getText().toString().isEmpty()) {
                    Total_No_Of_Units = Integer.parseInt(edt_total_units.getText().toString());
                    dlg.cancel();
                }else
                {
                    Toast.makeText(MRA_SetReminder.this, "Please Enter Units", Toast.LENGTH_LONG).show();
                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clear_refill_reminder();
                dlg.cancel();
            }
        });



        btnplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                packQnty++;
                Qty.setText(String.valueOf(packQnty));
                Total_No_Of_Units=Integer.parseInt(refill_Packsize)*packQnty;
                        edt_total_units.setText(""+Total_No_Of_Units);
            }
        });

        btnminus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(  packQnty>1) {
                    packQnty--;
                    Qty.setText(String.valueOf(packQnty));
                    Total_No_Of_Units=Integer.parseInt(refill_Packsize)*packQnty;
                    edt_total_units.setText(""+Total_No_Of_Units);
                }else{
                    Toast.makeText(MRA_SetReminder.this, "minimum 1 Quantity", Toast.LENGTH_LONG).show();
                }

            }
        });


     /* dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
          @Override
          public void onDismiss(DialogInterface dialog) {
              clear_refill_reminder();
          }
      });*/
        dlg.setCancelable(false);

        dlg.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dlg.show();
        refill_units.clearAnimation();
        refill_units.startAnimation(scale);

    }

    private String calculate_refill_date(Integer total_quantity,String UOM,Integer no_of_days,Integer frequency,Integer selected_dosage)
    {

        String Refill_Date="";
        Integer RefillDays=0;
        Integer days_medicine_complete=0;

      /*  if(UOM.equals("ml")||UOM.equals("drops"))
        {

        }else
        {*/
            days_medicine_complete=total_quantity/(frequency*selected_dosage);
        //}


        try {
            current_date = dateFormat_query.parse(SelDate);

        String Medicine_completed_date=dateFormat_query.format(date_addDays(current_date, days_medicine_complete));

        long diff=(dateFormat_query.parse(Medicine_completed_date).getTime()-
                dateFormat_query.parse(SelDate).getTime());


            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long Medicine_completed_days = hours / 24;



        if(Medicine_completed_days<=14)
        {
            RefillDays=3;
        }else
        {
            RefillDays=7;
        }

           Refill_Date= dateFormat_query.format(date_addDays(dateFormat_query.parse(Medicine_completed_date), -RefillDays));
        } catch (Exception e) {
        }
        return Refill_Date;
    }

    private void clear_refill_reminder()
    {
        Total_No_Of_Units=0;

        Refill_flag=0;
        chk_refil_reminder.setChecked(false);
    }



}
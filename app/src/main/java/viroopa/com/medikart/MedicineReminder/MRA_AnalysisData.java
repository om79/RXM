package viroopa.com.medikart.MedicineReminder;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.MainActivity;
import viroopa.com.medikart.MedicineReminder.Model.Category;
import viroopa.com.medikart.MedicineReminder.Model.ItemDetail;
import viroopa.com.medikart.MedicineReminder.Model.M_medicinelist;
import viroopa.com.medikart.MedicineReminder.Model.m_medicine_list;
import viroopa.com.medikart.MedicineReminder.adapter.EXAD_AdherenceDetails;
import viroopa.com.medikart.R;
import viroopa.com.medikart.adapter.AD_memberAdapter;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.Style;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteMRHandler;
import viroopa.com.medikart.model.M_memberlist;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.width;
import static com.itextpdf.text.html.HtmlTags.ALIGN_CENTER;
import static com.itextpdf.text.html.HtmlTags.ITALIC;
import static com.itextpdf.text.html.HtmlTags.TABLE;
import static com.itextpdf.text.pdf.internal.PdfVersionImp.HEADER;

/**
 * Created by prakash on 08/08/15.
 */
public class MRA_AnalysisData extends AppCompatActivity {

    private SqliteMRHandler db_mr;
    private SQLiteHandler db;
    HashMap<String, String> m;
    private TextView Percentage;
    AppController globalVariable;
    private ImageView profile_pic;
    private  File dir;
    private File file;
    private Menu objMemberMenu;
    private String image_name = "";
    private AD_memberAdapter memberadapter;
    private LayoutInflater inflater;
    SharedPreferences pref;
    List<M_memberlist> MemberData = new ArrayList<M_memberlist>();
    private String current_filter;
    private  PdfWriter writer;
    private TextView txtdate;
    private String sMemberId, ImageName;
    private TextView spnr_send_report;
    private String Medicine_id = "0";
    private String s_Start_date, S_End_date;
    private ImageView homeBtn;
    DisplayImageOptions options;
    private ImageLoader imageLoader;
    private ExpandableListView exList;
    private List<Category> catList = new ArrayList<Category>();
    ;
    private List<M_medicinelist> pdf_medicine_names = new ArrayList<M_medicinelist>();
    Date current_date = Calendar.getInstance().getTime();
    Date taken_date= Calendar.getInstance().getTime();
    Date current_date_pdf_year = Calendar.getInstance().getTime();
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateFormat_header = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat dateFormat_time = new SimpleDateFormat("hh.mm a");
    DateFormat dateFormat_heading_display = new SimpleDateFormat("LLL dd,EEEE");
    DateFormat dateFormat_day = new SimpleDateFormat("dd");
    private Spinner spnr_Lastweek, spnr_med_filter;
    private ArrayList<m_medicine_list> medicine_list = new ArrayList<m_medicine_list>();
    private String selected_med_name = "";
    private TextView send_report;
    private SimpleDateFormat format_date_for_pdf = new SimpleDateFormat("LLLL dd,yyyy hh:mm" );
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.NORMAL, BaseColor.RED);

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    Rectangle rect = new Rectangle(30, 30, 550, 800);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_report_med_reminder);

        inflater = LayoutInflater.from(this);
        pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        // setHasOptionsMenu(true);
        initImageLoader();
        globalVariable = (AppController) this.getApplicationContext();
        db_mr = new SqliteMRHandler(this.getApplicationContext());
        db = new SQLiteHandler(this.getApplicationContext());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        homeBtn = (ImageView) findViewById(R.id.homeBtn);
        exList = (ExpandableListView) findViewById(R.id.expandableListView1);
        Percentage = (TextView) findViewById(R.id.Percentage);
        txtdate = (TextView) findViewById(R.id.txtdate);
        spnr_Lastweek = (Spinner) findViewById(R.id.spnr_Lastweek);
        spnr_med_filter = (Spinner) findViewById(R.id.spnr_med_filter);
        spnr_send_report = (TextView) findViewById(R.id.send_report);
        send_report = (TextView) findViewById(R.id.send_report);

        profile_pic = (ImageView) findViewById(R.id.profile_pic);

        getIntenet();

        image_load();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent Intenet_home = new Intent(MRA_AnalysisData.this, MainActivity.class);
                startActivity(Intenet_home);
                finish();
            }
        });


        String[] array_filter_week = new String[]{"Day", "Week", "Month", "Year"};


        ArrayAdapter filterWeektAdapter = new ArrayAdapter<String>(
                this, R.layout.rxspinner_simple_text_layout
                , array_filter_week);
        init_All_med_spinner();

        getWeeklyAdherence_medicine_wise(dateFormat_query.format(current_date), dateFormat_query.format(current_date), Medicine_id, selected_med_name);
        //getWeeklyAdherence(dateFormat_query.format(current_date), dateFormat_query.format(current_date), Medicine_id);

        s_Start_date = dateFormat_query.format(current_date);
        S_End_date = dateFormat_query.format(current_date);

        try {
            current_date = dateFormat_query.parse(s_Start_date);
            String dayLetter = (dateFormat_heading_display.format(current_date));
            txtdate.setText(dayLetter);
        } catch (Exception e) {

        }


        spnr_Lastweek.setAdapter(filterWeektAdapter);

        spnr_Lastweek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                String selected = (String) parent.getItemAtPosition(position);
                if (selected != null) {
                    current_filter=selected;
                    date_filter(current_filter);
                }
                // getWeeklyAdherence(s_Start_date, S_End_date, Medicine_id);
                getWeeklyAdherence_medicine_wise(s_Start_date, S_End_date, Medicine_id, selected_med_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        exList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandablelistview,
                                        View clickedView, int groupPosition, int childPosition, long childId) {
                go_to_medrem_main();
                return false;
            }
        });





        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoad(pref.getString("User_Name", ""), pref.getString("imagename", ""));
            }
        });

        send_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate_pdf_report(current_filter);
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater Minflater = getMenuInflater();
        Minflater.inflate(R.menu.menu_mr_analysis, menu);
        this.objMemberMenu = menu;


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


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void getWeeklyAdherence_medicine_wise(String StartDate, String EndDate, String reminder_id, String MedicineName) {

        catList = new ArrayList<Category>();
        catList.clear();
        EXAD_AdherenceDetails exAdpt = new EXAD_AdherenceDetails(catList, this, sMemberId);

        exList.setAdapter(exAdpt);
        List<ItemDetail> result = null;

        String rem_id = "";


        ItemDetail item = null;
        Category cat1 = null;
        String smymonthyear = "-99";
        Double pending_all_count = 0.0, taken_all_count = 0.0;
        int nGroup = 0;


        Cursor cursor_session = db_mr.get_med_rem_sch_all_medicine_wise_pending_taken(StartDate, EndDate, reminder_id, sMemberId);
        int c = cursor_session.getCount();
        int i = 0;
        if ((cursor_session != null) || (cursor_session.getCount() > 0)) {
            if (cursor_session.moveToFirst()) {
                do {

                    result = new ArrayList<ItemDetail>();

                    cat1 = new Category(i, cursor_session.getString(cursor_session.getColumnIndex("datetime_set")), cursor_session.getString(cursor_session.getColumnIndex("total_taken")), cursor_session.getString(cursor_session.getColumnIndex("total_pending")));
                    item = new ItemDetail(i, cursor_session.getString(cursor_session.getColumnIndex("datetime_set")), cursor_session.getString(cursor_session.getColumnIndex("datetime_set")), MedicineName, "");
                    pending_all_count = pending_all_count + cursor_session.getInt(cursor_session.getColumnIndex("total_pending"));
                    taken_all_count = taken_all_count + cursor_session.getInt(cursor_session.getColumnIndex("total_taken"));
                    result.add(item);
                    cat1.setItemList(result);
                    catList.add(cat1);


                    i++;
                } while (cursor_session.moveToNext());

                if (cat1 != null) {


                }
                exAdpt = new EXAD_AdherenceDetails(catList, this, sMemberId);

                exList.setAdapter(exAdpt);
                //  exList.expandGroup(0);

                try {
                    // pending_all_count=taken_all_count-pending_all_count;

                    Double adhenrence_first = (taken_all_count / (taken_all_count + pending_all_count));

                    if (adhenrence_first.isInfinite() == true) {
                        adhenrence_first = 0.0;
                    }
                    Double adherence_percent = adhenrence_first * 100;


                    Percentage.setText(String.format("%.2f", adherence_percent) + " % ");
                } catch (Exception e) {
                }

            } else {
                //NoDataFoundDialog();
            }


            //  }

        }
    }


    public Date date_addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public Date date_minusMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        return c.getTime();
    }

    public Date dateminusYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);
        return c.getTime();
    }


    private void go_to_medrem_main() {
        Intent intent_refill = new Intent(this, MRA_ReminderMain.class);
        startActivity(intent_refill);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void send_report_to_doctor(String Path) {
        Intent intent_report = new Intent(this, MRA_SendReprtToDoctor.class);
        intent_report.putExtra("Email_heading","Medicine Reminder report");
        intent_report.putExtra("path",Path);
        startActivity(intent_report);

    }


    private void date_filter(String filter) {

        current_date = Calendar.getInstance().getTime();
        if (filter.equals("Day")) {
            s_Start_date = dateFormat_query.format(current_date);
            S_End_date = dateFormat_query.format(current_date);

            try {
                current_date = dateFormat_query.parse(s_Start_date);
                String dayLetter = (dateFormat_heading_display.format(current_date));
                txtdate.setText(dayLetter);
            } catch (Exception e) {

            }
        }
        if (filter.equals("Week")) {
            s_Start_date = dateFormat_query.format(date_addDays(current_date, -6));
            S_End_date = dateFormat_query.format(current_date);

            try {
                current_date = dateFormat_query.parse(s_Start_date);
                String dayLetterf = (dateFormat_heading_display.format(current_date));
                current_date = dateFormat_query.parse(S_End_date);

                String seconddyaleeter = (dateFormat_heading_display.format(current_date));
                txtdate.setText(dayLetterf + " - " + seconddyaleeter);
            } catch (Exception e) {

            }

            try {
                current_date = dateFormat_query.parse(s_Start_date);
                String dayLetterf = (dateFormat_heading_display.format(s_Start_date));
                current_date = dateFormat_query.parse(S_End_date);

                String seconddyaleeter = (dateFormat_heading_display.format(S_End_date));
                txtdate.setText(dayLetterf + " - " + seconddyaleeter);
            } catch (Exception e) {

            }
        }
        if (filter.equals("Month")) {
            s_Start_date = dateFormat_query.format(date_minusMonth(current_date));
            S_End_date = dateFormat_query.format(current_date);
            try {
                current_date = dateFormat_query.parse(s_Start_date);
                String dayLetterf = (dateFormat_heading_display.format(current_date));
                current_date = dateFormat_query.parse(S_End_date);

                String seconddyaleeter = (dateFormat_heading_display.format(current_date));
                txtdate.setText(dayLetterf + " - " + seconddyaleeter);
            } catch (Exception e) {

            }
        }
        if (filter.equals("Year")) {
            s_Start_date = dateFormat_query.format(dateminusYear(current_date));
            S_End_date = dateFormat_query.format(current_date);
            try {
                current_date = dateFormat_query.parse(s_Start_date);
                String dayLetterf = (dateFormat_heading_display.format(current_date));
                current_date = dateFormat_query.parse(S_End_date);

                String seconddyaleeter = (dateFormat_heading_display.format(current_date));
                txtdate.setText(dayLetterf + " - " + seconddyaleeter);
            } catch (Exception e) {

            }
        }
    }

    private void init_All_med_spinner() {


        Cursor cursor_all_medicine = db_mr.get_med_all_medicine_master(sMemberId);

        medicine_list.clear();

        m_medicine_list O_medicine_list = new m_medicine_list();

        O_medicine_list.setMedicine_Id("");
        O_medicine_list.setMedicine_Name("All Medicines");
        // O_medicine_list.setMed_shape_type_array("");
        // O_medicine_list.setSchedule_image_id(0);


        medicine_list.add(O_medicine_list);

        if ((cursor_all_medicine != null) || (cursor_all_medicine.getCount() > 0)) {
            if (cursor_all_medicine.moveToFirst()) {
                do {
                    O_medicine_list = new m_medicine_list();

                    O_medicine_list.setMedicine_Id(cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("medicine_id")));
                    O_medicine_list.setMedicine_Name(cursor_all_medicine.getString(cursor_all_medicine.getColumnIndex("medicine_name")));
                    O_medicine_list.setImd_id(cursor_all_medicine.getInt(cursor_all_medicine.getColumnIndex("image_id")));
                    O_medicine_list.setFirst_color_id(cursor_all_medicine.getInt(cursor_all_medicine.getColumnIndex("first_color_id")));
                    O_medicine_list.setSecond_color_id(cursor_all_medicine.getInt(cursor_all_medicine.getColumnIndex("second_color_id")));


                    medicine_list.add(O_medicine_list);

                } while (cursor_all_medicine.moveToNext());
            }
        }
        String[] array_filter_medicine = null;
        if (medicine_list.size() > 0) {
            array_filter_medicine = new String[medicine_list.size()];

            for (int i = 0; i < medicine_list.size(); i++) {
                O_medicine_list = new m_medicine_list();
                O_medicine_list = medicine_list.get(i);
                if (O_medicine_list != null) {
                    array_filter_medicine[i] = O_medicine_list.getMedicine_Name();
                }
            }


      /* }else
       {
           array_filter_medicine = new String[]{"All Medicines"};
       }*/

            ArrayAdapter filter_medicinetAdapter = new ArrayAdapter<String>(
                    this, R.layout.rxspinner_simple_text_layout
                    , array_filter_medicine);

            spnr_med_filter.setAdapter(filter_medicinetAdapter);

            spnr_med_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    String selected = (String) parent.getItemAtPosition(position);
                    String med_id = "0";
                    Medicine_id = "0";
                    if (selected.equals("All Medicines")) {
                        selected_med_name = "";
                        //getWeeklyAdherence(s_Start_date, S_End_date, Medicine_id);
                        getWeeklyAdherence_medicine_wise(s_Start_date, S_End_date, Medicine_id, selected_med_name);
                    } else {

                        Cursor cursor_data_med = db_mr.get_search_medicine(selected, sMemberId);

                        if ((cursor_data_med != null) || (cursor_data_med.getCount() > 0)) {
                            if (cursor_data_med.moveToFirst()) {
                                med_id = cursor_data_med.getString(cursor_data_med.getColumnIndex("medicine_id"));
                            }

                        }
                        Medicine_id = med_id;
                        selected_med_name = selected;
                        getWeeklyAdherence_medicine_wise(s_Start_date, S_End_date, Medicine_id, selected_med_name);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
    }

    private void getIntenet() {

        Intent i = getIntent();
        sMemberId = i.getStringExtra("sMemberId");
        ImageName = i.getStringExtra("ImageName");

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

    private void image_load() {
        if (!ImageName.equals("")) {
            if (ImageName.startsWith("avtar")) {
                Resources res = getResources();

                int resourceId = res.getIdentifier(ImageName, "drawable", getPackageName());
                Drawable drawable = res.getDrawable(resourceId);
                profile_pic.setImageDrawable(drawable);
            } else {

                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
                File pathfile = new File(mediaStorageDir.getPath() + "/" + ImageName);
                imageLoader.displayImage("file://" + pathfile.getPath(), profile_pic, options);
            }

        }
    }

    private void ImageLoad(String name, String imageName) {
        if (imageName == null || imageName.equals("null")) {
            imageName = "avtar1";
        }

        image_name = imageName;
        profile_pic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                show_medfriends_dialog();

            }
        });

        if (imageName != null) {
            String BPimgeName = imageName.substring(imageName.lastIndexOf('/') + 1, imageName.length());

            //txtmemberName.setText(name);


            if (BPimgeName.startsWith("avtar")) {
                Resources res = getApplicationContext().getResources();

                int resourceId = res.getIdentifier(BPimgeName, "drawable", getApplicationContext().getPackageName());
                Drawable drawable = res.getDrawable(resourceId);
                profile_pic.setImageDrawable(drawable);
            } else {
                String iconsStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + AppConfig.IMAGE_DIRECTORY_NAME;
                File pathfile = new File(iconsStoragePath + File.separator + BPimgeName);
                Bitmap mybitmap = BitmapFactory.decodeFile(pathfile.getPath());
                if (mybitmap != null) {
                    Drawable d = new BitmapDrawable(getResources(), mybitmap);
                    profile_pic.setImageDrawable(d);
                }
            }
        }
    }

    private void show_medfriends_dialog() {

        MemberData.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogview = inflater.inflate(R.layout.activity_searchlist, null);
        builder.setView(dialogview);
        final Dialog dialog = builder.create();

        final ListView lv = (ListView) dialogview.findViewById(R.id.list_view);
        final TextView Titile = (TextView) dialogview.findViewById(R.id.textView);
        final TextView btncancel = (TextView) dialogview.findViewById(R.id.btncancel);

        memberadapter = new AD_memberAdapter(this, MemberData, sMemberId);
        lv.setAdapter(memberadapter);

        Cursor cursor_medfriend = db_mr.get_all_pill_buddyr(sMemberId);

        M_memberlist memberdetails = new M_memberlist();

        memberdetails.setMemberId(Integer.parseInt(pref.getString("memberid", "")));
        memberdetails.setMemberName(pref.getString("UserName", ""));
        memberdetails.setMemberGender(pref.getString("MemberGender", ""));
        memberdetails.setRelationshipId(8);
        memberdetails.setMemberDOB(pref.getString("MemberDOB", ""));
        memberdetails.setImageurl(pref.getString("imagename", ""));
        memberdetails.setPatientId(pref.getString("memberid", ""));
        MemberData.add(memberdetails);

        if ((cursor_medfriend != null) || (cursor_medfriend.getCount() > 0)) {
            if (cursor_medfriend.moveToFirst()) {
                do {


                    memberdetails = new M_memberlist();

                    memberdetails.setMemberId(cursor_medfriend.getInt(cursor_medfriend.getColumnIndex("medfriend_id")));
                    memberdetails.setMemberName(cursor_medfriend.getString(cursor_medfriend.getColumnIndex("reminder_friendname")));
                    memberdetails.setRelationshipId(8);
                    memberdetails.setImageurl(cursor_medfriend.getString(cursor_medfriend.getColumnIndex("reminder_image_name")));

                    memberdetails.setPatientId(cursor_medfriend.getString(cursor_medfriend.getColumnIndex("medfriend_id")));
                    MemberData.add(memberdetails);


                } while (cursor_medfriend.moveToNext());

            }
        }


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
                sMemberId = (String) v.getTag(R.id.key_RelationShipId);
                if (!sMemberId.equals("-99")) {
                    current_date = Calendar.getInstance().getTime();
                    ImageLoad((String) v.getTag(R.id.key_MemberName), (String) v.getTag(R.id.key_MemberImage));
                    init_All_med_spinner();
                    getWeeklyAdherence_medicine_wise(dateFormat_query.format(current_date), dateFormat_query.format(current_date), Medicine_id, selected_med_name);
                    dialog.dismiss();

                } else {
                    Toast.makeText(MRA_AnalysisData.this, "Friend request pending", Toast.LENGTH_LONG).show();
                }

            }
        });
        memberadapter.notifyDataSetChanged();
        dialog.getWindow().getAttributes().windowAnimations =
                R.style.dialog_med_animation;
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private void generate_pdf_report(String filter ) {

        pdf_medicine_names.clear();
        float[]table_widhts=null;

        float[]table_widhts1=new float[]{5f, 5f, 5f,5f};
        float[]width6=new float[]{5f, 5f, 5f,5f,5f,5f};
        float[]width10=new float[]{5f, 5f, 5f,5f,5f,5f,5f,5f,5f,5f};
        float[]width3=new float[]{5f, 5f, 5f,5f};
        int report_length=0;

        Document doc = new Document(PageSize.A4);

        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);
        Font bfBold12_white = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);
        Font bfBold13 = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.BLACK);

        Font bfBold13_white = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.WHITE);

        Font h1 = new Font(Font.FontFamily.COURIER, 24, Font.NORMAL, BaseColor.BLACK);
        Font h1_blue = new Font(Font.FontFamily.COURIER, 24, Font.NORMAL, new BaseColor(0,121,182));
        Font h3 = new Font(Font.FontFamily.COURIER, 15, Font.NORMAL, BaseColor.BLACK);

        Font bfBold14 = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.BLACK);
        Font bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.BLACK);
        Font paraFont = new Font(Font.FontFamily.COURIER,14, Font.NORMAL,BaseColor.BLACK);

        Font secparaFont = new Font(Font.FontFamily.UNDEFINED,12, Font.NORMAL, new BaseColor(223,99,1));
        //  Font dateparaFont = new Font(Font.FontFamily.UNDEFINED,12, Font.NORMAL, BaseColor.LIGHT_GRAY);
        Font dateparaFont =  new Font(Font.FontFamily.COURIER, 12, Font.NORMAL, BaseColor.BLACK);

        Font fontlink = new Font(Font.FontFamily.COURIER,12, Font.NORMAL, BaseColor.DARK_GRAY);

        Font blankFont = new Font();

        DottedLineSeparator dottedline = new DottedLineSeparator();
        dottedline.setOffset(-2);
        dottedline.setGap(1f);
        dottedline.setLineColor(new BaseColor(223,99,1));
        // dottedline.setLineColor(BaseColor.DARK_GRAY);

        String myName="",Age="",weight="",goal_bs="",goal_weight="",gender="";



        try {

            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream2);
            Image myImg = Image.getInstance(stream2.toByteArray());
            myImg.setAlignment(Image.ALIGN_LEFT);
            myImg.scaleAbsolute(25, 25);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap1 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.tick);
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image tick = Image.getInstance(stream.toByteArray());
            tick.setAlignment(Element.ALIGN_CENTER);
            tick.scaleAbsolute(5f, 5f);


            ByteArrayOutputStream stream_close = new ByteArrayOutputStream();
            Bitmap bitmap_close = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.close2);
            bitmap_close.compress(Bitmap.CompressFormat.PNG, 100, stream_close);
            Image close = Image.getInstance(stream_close.toByteArray());
            close.setAlignment(Element.ALIGN_CENTER);
            close.scaleAbsolute(5f, 5f);




            if (filter.equals("Day")) {
                report_length=1;
                table_widhts=new float[]{6f, 3f, 1f};

            }else   if (filter.equals("Week")) {
                report_length=7;
                table_widhts=new float[]{6f, 3f, 1f,1f, 1f, 1f, 1f, 1f, 1f};
            }else  if (filter.equals("Month")) {
                report_length=30;

                table_widhts=new float[]{6f, 3f, 1f,1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f,
                        1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f
                        , 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
            }else  if (filter.equals("Year")) {
                report_length=30;

                table_widhts=new float[]{6f, 3f, 1f,1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f,
                        1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f
                        , 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
            }

// profile data get
            db = new SQLiteHandler(getApplicationContext());

            ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
            test.add(db.getUserDetails());



            HashMap<String, String> m = test.get(0);

            myName= m.get("name");
            String email= m.get("email");

            String  g = m.get("gender").toString();

            if (g.equals("M")) {
                gender="Male";
            } else if (g.equals("F")) {
                gender="Female";
            } else if (g.equals("O")) {
                gender="Other";
            }
//



            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RxMedikart";

            dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();


            file = new File(dir, "RxMedikart_medicine_schedule_report.pdf");
            FileOutputStream fOut = new FileOutputStream(file);



            writer= PdfWriter.getInstance(doc, fOut);

            writer.setBoxSize("art", rect);
            writer.setPageEvent(new HeaderAndFooter());
            doc.open();


            //  doc.setMargins(50, 45, 50, 60);
            doc.setMarginMirroring(false);

            PdfPTable table_head = new PdfPTable(1);
            table_head.setWidthPercentage(100);
            Chunk cp0 = new Chunk();

            cp0 = new Chunk("www.rxmedikart.com", fontlink).setAnchor("http://www.rxmedikart.com");
            insertCellwithBorder_and_color(table_head,null, "Medicine Reminder", "row", null, Element.ALIGN_RIGHT, 1, h1_blue, true, 0, 0, 0, 0,null);
            insertCellwithBorder_and_color(table_head,cp0, "", "row", null, Element.ALIGN_RIGHT, 1, h1_blue, true, 0, 0, 0, 0,null);
            table_head.setSpacingAfter(5);



            doc.add(table_head);

            doc.add(dottedline);
            doc.add( Chunk.NEWLINE );

            //  Paragraph p1 = new Paragraph();




            // doc.add( Chunk.NEWLINE );

            PdfPTable table1 = new PdfPTable(4);
            table1.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table1.setSpacingBefore(10f);
            table1.setSpacingAfter(0f);
            table1.setWidthPercentage(100);
            table1.setWidths(width3);

            insertCellwithBorder_and_color(table1,null, " ", "r", null, Element.ALIGN_CENTER, 4, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));

            insertCellwithBorder_and_color(table1,null, "Name : ", "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.1f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder_and_color(table1,null, myName + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);


            insertCellwithBorder_and_color(table1,null, "Gender : " , "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder_and_color(table1,null, gender + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);

            insertCellwithBorder_and_color(table1,null, "Email : ", "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder_and_color(table1,null,  email + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);
            insertCellwithBorder_and_color(table1,null, " ", "r", null, Element.ALIGN_CENTER, 2, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));


            //  insertCellwithBorder(table1, "Gender : "+gender+"","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);


            //  insertCellwithBorder(table1, "Goal BS : "+goal_bs+"","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);

            //  insertCellwithBorder(table1, "Goal Weight : "+goal_weight+"","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);


            doc.add(table1);

            doc.add( Chunk.NEWLINE );

            //
            Paragraph p23 = new Paragraph();
            Chunk cp23 = new Chunk("Indicators",h3);
            p23.add(cp23);

            p23.setAlignment(Paragraph.ALIGN_LEFT);
            doc.add(p23);
            //

            PdfPTable table2 = new PdfPTable(10);
            table2.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table2.setSpacingBefore(10f);
            table2.setSpacingAfter(0f);
            table2.setWidthPercentage(100);
            table2.setWidths(width10);

            insertCellwithBorder(table2, "","row_taken", tick, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0,0.01f);

            insertCellwithBorder(table2, "Taken","row_taken", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0.01f,0.01f,0.01f);

            insertCellwithBorder(table2, "","row_taken_early", tick, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0,0.01f);

            insertCellwithBorder(table2, "Taken Early","row_taken_early", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0.01f,0.01f,0.01f);

            insertCellwithBorder(table2, "","row_taken_late", tick, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0,0.01f);

            insertCellwithBorder(table2, "Taken Late","row_taken_late", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0.01f,0.01f,0.01f);

            insertCellwithBorder(table2, "","row_pending", close, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0,0.01f);

            insertCellwithBorder(table2, "Pending","row_pending", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0.01f,0.01f,0.01f);

            insertCellwithBorder(table2, "","row_skiped", close, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0,0.01f);

            insertCellwithBorder(table2, "Skipped","row_skiped", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0.01f,0.01f,0.01f);



            doc.add(table2);

            doc.add( Chunk.NEWLINE );

          /*  Chunk c = new Chunk(myImg, 0, 0,true);
            p1.add(c);
            c = new Chunk(" ",paraFont);
            p1.add(c);
            c = new Chunk("RxMedikart",paraFont);
            p1.add(c);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(p1);

            doc.add( Chunk.NEWLINE );*/


            Paragraph p2 = new Paragraph("Weekly Report Of Medicine Reminder",secparaFont);
            p2.setAlignment(Paragraph.ALIGN_LEFT);
            p2.setSpacingBefore(0);
            doc.add(p2);

            doc.add( Chunk.NEWLINE );


//            Chunk chunk = new Chunk("www.rxmedikart.com", fontlink)
//                    .setAnchor("http://www.rxmedikart.com");
//            Paragraph plink = new Paragraph(chunk);
//
//            plink.setAlignment(Paragraph.ALIGN_RIGHT);
//            plink.setSpacingAfter(5);
//            doc.add(plink);
//            doc.add(dottedline);



            if(filter.equals("Year")){
                String loop_s_year="",loop_e_year="";

                try {
                    current_date_pdf_year = dateFormat_query.parse(s_Start_date);

                }catch (ParseException e)
                {
                    e.toString();
                }
                for(int o=0;o<12;o++){

                   /* if(o!=0)
                    {
                       doc.newPage();
                    }*/

                    loop_s_year = dateFormat_query.format(current_date_pdf_year);
                    current_date_pdf_year=date_addDays(current_date_pdf_year, 29);
                    loop_e_year = dateFormat_query.format(current_date_pdf_year);


                    Paragraph p3 = new Paragraph(" Report from " + loop_s_year + " to " + loop_e_year, dateparaFont);
                    p2.setAlignment(Paragraph.ALIGN_LEFT);
                    doc.add(p3);


                    PdfPTable table = new PdfPTable(report_length + 2);
                    table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
                    table.setSpacingBefore(10f);
                    table.setSpacingAfter(0f);
                    table.setWidthPercentage(100);
                    table.setWidths(table_widhts);

                    insertCell(table, "Medicine Schedule Report","header", null, Element.ALIGN_CENTER, report_length + 2, bfBold12_white);
                    insertCell(table, "Medicine Name","row", null, Element.ALIGN_CENTER, 1, bfBold13);
                    insertCell(table, "Timings","row", null, Element.ALIGN_CENTER, 1, bfBold13);


                    for (int i = 0; i < report_length; i++) {
                        try {
                            String day_number = dateFormat_day.format(date_addDays(dateFormat_query.parse(loop_s_year), i));
                            insertCell(table, day_number,"row", null, Element.ALIGN_CENTER, 1, bfBold13);
                        } catch (Exception e) {

                        }

                    }


                    Cursor cursor_session = db_mr.get_med_rem_all_schedule_medicine_for_pdf(loop_s_year, loop_e_year, "", sMemberId);

                    if ((cursor_session != null) || (cursor_session.getCount() > 0)) {
                        if (cursor_session.moveToFirst()) {
                            do {
                                Boolean dataavailabe = false;

                      /*  if(pdf_displayed_medicine_list.size()>0)
                        {*/

                                try {

                                    current_date = dateFormat_header.parse(cursor_session.getString(cursor_session.getColumnIndex("datetime_set")));
                                    String time = (dateFormat_time.format(current_date));


                                    if (pdf_medicine_names.size() > 0)

                                    {
                                        for (int arr = 0; arr < pdf_medicine_names.size(); arr++) {
                                            if (pdf_medicine_names.get(arr).getMedicineName().equals(cursor_session.getString(cursor_session.getColumnIndex("medicine_name"))) && pdf_medicine_names.get(arr).getTime().equals(time)) {
                                                dataavailabe = true;

                                            }
                                        }
                                    }

                                    if (!dataavailabe) {
                                        M_medicinelist o_M_medicinelist = new M_medicinelist();
                                        o_M_medicinelist.setMedicineName(cursor_session.getString(cursor_session.getColumnIndex("medicine_name")));
                                        o_M_medicinelist.setTime(time);
                                        // pdf_medicine_times.add(time);
                                        pdf_medicine_names.add(o_M_medicinelist);

                                        insertCell(table, cursor_session.getString(cursor_session.getColumnIndex("medicine_name")),"row", null, Element.ALIGN_CENTER, 1, bfBold14);
                                        //insertCell(table,dateFormat_heading_display.format(current_date),null, Element.ALIGN_CENTER, 1, bfBold14);
                                        insertCell(table, time,"row", null, Element.ALIGN_CENTER, 1, bfBold14);

                                        for (int a = 0; a < report_length; a++) {

                                            String next_date = dateFormat_query.format(date_addDays(dateFormat_query.parse(loop_s_year), a));

                                            Cursor cursor_datewise = db_mr.get_schedule_status_for_pdf(next_date, cursor_session.getString(cursor_session.getColumnIndex("reminder_id")), sMemberId);

                                            if ((cursor_datewise != null) || (cursor_datewise.getCount() > 0)) {
                                                if (cursor_datewise.moveToFirst()) {
                                                    do {

                                                        current_date = dateFormat_header.parse(cursor_datewise.getString(cursor_datewise.getColumnIndex("datetime_set")));
                                                        String sch_time = (dateFormat_time.format(current_date));

                                                        if (time.equals(sch_time)) {
                                                            if (cursor_datewise.getString(cursor_datewise.getColumnIndex("status")).equalsIgnoreCase("taken"))
                                                            {

                                                                taken_date = dateFormat_header.parse(cursor_datewise.getString(cursor_datewise.getColumnIndex("datetime_taken")));
                                                                // String tkn_time= (dateFormat_time.format(taken_date));
                                                                long difference=(current_date.getTime()-taken_date.getTime());

                                                                long seconds = difference / 1000;
                                                                long minutes = seconds / 60;

                                                                if(minutes>=11)
                                                                {
                                                                    insertCell(table, "","row_taken_early", tick, Element.ALIGN_CENTER, 1, blankFont);
                                                                }else if(minutes<=(-10))
                                                                {
                                                                    insertCell(table, "","row_taken_late", tick, Element.ALIGN_CENTER, 1, blankFont);
                                                                }
                                                                else if(minutes<=0)
                                                                {
                                                                    insertCell(table, "","row_taken", tick, Element.ALIGN_CENTER, 1, blankFont);
                                                                }
                                                                else if(minutes==0)
                                                                {
                                                                    insertCell(table, "","row_taken", tick, Element.ALIGN_CENTER, 1, blankFont);
                                                                }else if(minutes>=0)
                                                                {
                                                                    insertCell(table, "","row_taken", tick, Element.ALIGN_CENTER, 1, blankFont);
                                                                }


                                                            } else if (cursor_datewise.getString(cursor_datewise.getColumnIndex("status")).equalsIgnoreCase("pending")) {
                                                                insertCell(table, "","row_pending", close, Element.ALIGN_CENTER, 1, bfBold14);
                                                            } else if (cursor_datewise.getString(cursor_datewise.getColumnIndex("status")).equalsIgnoreCase("skipped")) {
                                                                insertCell(table, "","row_skiped", close, Element.ALIGN_CENTER, 1, bfBold14);
                                                            } else {
                                                                insertCell(table, "","row", null, Element.ALIGN_CENTER, 1, bfBold14);
                                                            }
                                                        } else {
                                                            insertCell(table, "","row", null, Element.ALIGN_CENTER, 1, bfBold14);
                                                        }

                                                    } while (cursor_datewise.moveToNext());
                                                } else {
                                                    insertCell(table, "","row", null, Element.ALIGN_CENTER, 1, bfBold14);
                                                }

                                            } else {
                                                insertCell(table, "","row", null, Element.ALIGN_CENTER, 1, bfBold14);
                                            }

                                        }
                                    }

                                } catch (Exception e) {
                                    e.toString();
                                }


                            } while (cursor_session.moveToNext());

                            //code for medicine indicator

                        }else
                        {
                            insertCell(table, "No Schedules","row", null, Element.ALIGN_CENTER, report_length + 2, bfBold14);
                        }

                    }
                    doc.add(table);
                }




            }else
            {


                Paragraph p3 = new Paragraph(" Report from " + s_Start_date + " to " + S_End_date, dateparaFont);
                p2.setAlignment(Paragraph.ALIGN_LEFT);
                doc.add(p3);


                PdfPTable table = new PdfPTable(report_length + 2);
                table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(0f);
                table.setWidthPercentage(100);
                table.setWidths(table_widhts);

                insertCell(table, "Medicine Schedule Report","header", null, Element.ALIGN_CENTER, report_length + 2, bfBold12_white);
                insertCell(table, "Medicine Name","row", null, Element.ALIGN_CENTER, 1, bfBold13);
                insertCell(table, "Timings","row", null, Element.ALIGN_CENTER, 1, bfBold13);


                for (int i = 0; i < report_length; i++) {
                    try {
                        String day_number = dateFormat_day.format(date_addDays(dateFormat_query.parse(s_Start_date), i));
                        insertCell(table, day_number,"row", null, Element.ALIGN_CENTER, 1, bfBold13);
                    } catch (Exception e) {

                    }

                }


                Cursor cursor_session = db_mr.get_med_rem_all_schedule_medicine_for_pdf(s_Start_date, S_End_date, "", sMemberId);

                if ((cursor_session != null) || (cursor_session.getCount() > 0)) {
                    if (cursor_session.moveToFirst()) {
                        do {
                            Boolean dataavailabe = false;

                      /*  if(pdf_displayed_medicine_list.size()>0)
                        {*/

                            try {

                                current_date = dateFormat_header.parse(cursor_session.getString(cursor_session.getColumnIndex("datetime_set")));
                                String time = (dateFormat_time.format(current_date));


                                if (pdf_medicine_names.size() > 0)

                                {
                                    for (int arr = 0; arr < pdf_medicine_names.size(); arr++) {
                                        if (pdf_medicine_names.get(arr).getMedicineName().equals(cursor_session.getString(cursor_session.getColumnIndex("medicine_name"))) && pdf_medicine_names.get(arr).getTime().equals(time)) {
                                            dataavailabe = true;

                                        }
                                    }
                                }

                                if (!dataavailabe) {
                                    M_medicinelist o_M_medicinelist = new M_medicinelist();
                                    o_M_medicinelist.setMedicineName(cursor_session.getString(cursor_session.getColumnIndex("medicine_name")));
                                    o_M_medicinelist.setTime(time);
                                    // pdf_medicine_times.add(time);
                                    pdf_medicine_names.add(o_M_medicinelist);

                                    insertCell(table, cursor_session.getString(cursor_session.getColumnIndex("medicine_name")),"row", null, Element.ALIGN_CENTER, 1, bfBold14);
                                    //insertCell(table,dateFormat_heading_display.format(current_date),null, Element.ALIGN_CENTER, 1, bfBold14);
                                    insertCell(table, time,"row", null, Element.ALIGN_CENTER, 1, bfBold14);

                                    for (int a = 0; a < report_length; a++) {

                                        String next_date = dateFormat_query.format(date_addDays(dateFormat_query.parse(s_Start_date), a));

                                        Cursor cursor_datewise = db_mr.get_schedule_status_for_pdf(next_date, cursor_session.getString(cursor_session.getColumnIndex("reminder_id")), sMemberId);

                                        if ((cursor_datewise != null) || (cursor_datewise.getCount() > 0)) {
                                            if (cursor_datewise.moveToFirst()) {
                                                do {

                                                    current_date = dateFormat_header.parse(cursor_datewise.getString(cursor_datewise.getColumnIndex("datetime_set")));
                                                    String sch_time = (dateFormat_time.format(current_date));

                                                    if (time.equals(sch_time)) {
                                                        if (cursor_datewise.getString(cursor_datewise.getColumnIndex("status")).equalsIgnoreCase("taken")) {

                                                            taken_date = dateFormat_header.parse(cursor_datewise.getString(cursor_datewise.getColumnIndex("datetime_taken")));
                                                            // String tkn_time= (dateFormat_time.format(taken_date));
                                                            long difference=(current_date.getTime()-taken_date.getTime());

                                                            long seconds = difference / 1000;
                                                            long minutes = seconds / 60;

                                                            if(minutes>=11)
                                                            {
                                                                insertCell(table, "","row_taken_early", tick, Element.ALIGN_CENTER, 1, blankFont);
                                                            }else if(minutes<=(-10))
                                                            {
                                                                insertCell(table, "","row_taken_late", tick, Element.ALIGN_CENTER, 1, blankFont);
                                                            }
                                                            else if(minutes<=0)
                                                            {
                                                                insertCell(table, "","row_taken", tick, Element.ALIGN_CENTER, 1, blankFont);
                                                            }
                                                            else if(minutes==0)
                                                            {
                                                                insertCell(table, "","row_taken", tick, Element.ALIGN_CENTER, 1, blankFont);
                                                            }else if(minutes>=0)
                                                            {
                                                                insertCell(table, "","row_taken", tick, Element.ALIGN_CENTER, 1, blankFont);
                                                            }

                                                        } else if (cursor_datewise.getString(cursor_datewise.getColumnIndex("status")).equalsIgnoreCase("pending")) {
                                                            insertCell(table, "","row_pending", close, Element.ALIGN_CENTER, 1, bfBold14);
                                                        } else if (cursor_datewise.getString(cursor_datewise.getColumnIndex("status")).equalsIgnoreCase("skipped")) {
                                                            insertCell(table, "","row_skiped", close, Element.ALIGN_CENTER, 1, bfBold14);
                                                        } else {
                                                            insertCell(table, "","row", null, Element.ALIGN_CENTER, 1, bfBold14);
                                                        }
                                                    } else {
                                                        insertCell(table, "","row", null, Element.ALIGN_CENTER, 1, bfBold14);
                                                    }

                                                } while (cursor_datewise.moveToNext());
                                            } else {
                                                insertCell(table, "","row", null, Element.ALIGN_CENTER, 1, bfBold14);
                                            }

                                        } else {
                                            insertCell(table, "","row", null, Element.ALIGN_CENTER, 1, bfBold14);
                                        }

                                    }
                                }

                            } catch (Exception e) {
                                e.toString();
                            }


                        } while (cursor_session.moveToNext());
                    }

                }


                doc.add(table);
            }


        } catch (DocumentException de) {
            de.toString();

        } catch (IOException e) {

        } finally {

            doc.close();
            send_report_to_doctor(file.getAbsolutePath());
        }

    }
    private void insertCell(PdfPTable table, String text,String type,Image img, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        // cell.setBorderWidth(0.01f);
        // cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(2);
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            if(img!=null)
            {
                // cell.addElement(new Chunk(img, 0, 0));
                cell.addElement(img);

            }

        }

        //row style
        if(type=="header")
        {
            Style.headerCellStyle(cell);
        }else if(type=="row")
        {
            Style.rowStyle(cell);
        }else if(type=="row_taken")
        {
            Style.rowStyle_taken(cell);
        }
        else if(type=="row_taken_late")
        {
            Style.rowStyle_taken_late(cell);
        }
        else if(type=="row_taken_early")
        {
            Style.rowStyle_taken_early(cell);
        }
        else if(type=="row_pending")
        {
            Style.rowStyle_pending(cell);
        }
        else if(type=="row_skiped")
        {
            Style.rowStyle_skiped(cell);
        }

        //add the call to the table
        table.addCell(cell);

    }

    private void insertCellwithBorder(PdfPTable table, String text,String type,Image img, int align, int colspan, Font font,Boolean Border,float leftBorder,float topBorder,float rightBorder,float bottomBorder){



        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        // cell.setBorderWidth(0.01f);
        cell.setPadding(2);
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        if(Border)
        {
            cell.setBorderWidthLeft(leftBorder);
            cell.setBorderWidthTop(topBorder);
            cell.setBorderWidthRight(rightBorder);
            cell.setBorderWidthBottom(bottomBorder);
            cell.setBorderColor(new BaseColor(0,121,182));
            // cell.setBackgroundColor(BaseColor.DARK_GRAY);

        }
        else
        {
            cell.setBorderWidth(0);
        }

        if(type=="header")
        {
            Style.headerCellStyle(cell);
        }else if(type=="row")
        {
            Style.rowStyle(cell);
        }
        else if(type=="row_taken")
        {
            Style.rowStyle_taken(cell);
        }
        else if(type=="row_taken_late")
        {
            Style.rowStyle_taken_late(cell);
        }
        else if(type=="row_taken_early")
        {
            Style.rowStyle_taken_early(cell);
        }
        else if(type=="row_pending")
        {
            Style.rowStyle_pending(cell);
        }
        else if(type=="row_skiped")
        {
            Style.rowStyle_skiped(cell);
        }
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            if(img!=null)
            {
                // cell.addElement(new Chunk(img, 0, 0));
                cell.addElement(img);

            }

        }
        //add the call to the table
        table.addCell(cell);

    }
    private void insertCellwithBorder_and_color(PdfPTable table,Chunk c, String text, String type, Image img, int align, int colspan,
                                      Font font, Boolean Border, float leftBorder, float topBorder, float rightBorder,
                                      float bottomBorder,BaseColor background) {


        //create a new cell with the specified Text and Font
        PdfPCell cell=null;
        if(c!=null)
        {
            Paragraph p=new Paragraph(c);
            cell=new PdfPCell(p);
        }else {
            cell = new PdfPCell(new Phrase(text.trim(), font));
        }

        if(background!=null)
        {
            cell.setBackgroundColor(background);
        }

        // cell.setBorderWidth(0.01f);
        cell.setPadding(2);
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        if (Border) {
            cell.setBorderWidthLeft(leftBorder);
            cell.setBorderWidthTop(topBorder);
            cell.setBorderWidthRight(rightBorder);
            cell.setBorderWidthBottom(bottomBorder);
            cell.setBorderColor(new BaseColor(223, 99, 1));
            // cell.setBackgroundColor(BaseColor.DARK_GRAY);

        } else {
            cell.setBorderWidth(0);
        }
        if (type == "header") {
            Style.headerCellStyle(cell);
        } else if (type == "row") {
            Style.rowStyle(cell);
        }
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            if (img != null) {
                // cell.addElement(new Chunk(img, 0, 0));
                cell.addElement(img);

            }

        }
        //add the call to the table
        table.addCell(cell);

    }
    public class HeaderAndFooter extends PdfPageEventHelper {
        // private Font footerFont;
        Image  background;
        public HeaderAndFooter() {
            super();

            //  footerFont.setStyle(Font.ITALIC);
        }



        public void onEndPage(PdfWriter writer, Document document) {
           /* PdfContentByte canvas = writer.getDirectContent();
            canvas.rectangle(50, 30, 500, 780);
            canvas.setColorStroke(BaseColor.LIGHT_GRAY);
            canvas.stroke();*/



            try
            {


                PdfContentByte cb = writer.getDirectContent();
                //   footerFont.setSize(15);

                Rectangle rect = writer.getBoxSize("art");

                Font c_footer = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL, new BaseColor(204, 102, 0));

                Chunk c_date= new Chunk("Report Date : "+format_date_for_pdf.format(Calendar.getInstance().getTime()),c_footer);

                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_RIGHT,new Phrase(c_date),
                        rect.getRight(), rect.getBottom() + 6, 0);


                Font c_footer_count = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL,BaseColor.WHITE);
                PdfPTable table = new PdfPTable(2);

                table.setWidths(new float[]{2f, 7f });
                table.setTotalWidth(523);
                PdfPCell cell = new PdfPCell(new Phrase("Stay Healthy",c_footer_count));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                cell.setBackgroundColor(new BaseColor(223, 99, 1));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Page " + writer.getPageNumber(),c_footer_count));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                cell.setBackgroundColor(new BaseColor(223, 99, 1));
                table.addCell(cell);
                table.setSpacingBefore(5);
                table.writeSelectedRows(0, -1, 36, rect.getBottom() - 5, writer.getDirectContent());

               // ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase("Page "+writer.getPageNumber()), rect.getRight(), rect.getBottom()-5, 0);

                // ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase("____________________________________________________________"), ((document.left() + document.right())/2)+1f , document.bottom(), 0);
                // ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(String.format("www.rxmedikart.com")), (document.left() + document.right())/2 , document.bottom()-30, 0);
                // ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_BOTTOM, new Phrase("www.rxmedikart.com"), document.right(), document.bottom(), 0);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap1 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.logo_rx);
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image  background = Image.getInstance(stream.toByteArray());

                background.scaleAbsolute(20f, 50f);

                if(writer.getPageNumber()==1) {


                    writer.getDirectContent().addImage(background, 180, 0, 0, 30, 30, 765);
                }



            }catch (Exception e)
            {}




        }

    }




}

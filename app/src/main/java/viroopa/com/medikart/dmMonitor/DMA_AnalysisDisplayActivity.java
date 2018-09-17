package viroopa.com.medikart.dmMonitor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.cepheuen.progresspageindicator.ProgressPageIndicator;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import viroopa.com.medikart.MainActivity;
import viroopa.com.medikart.MedicineReminder.MRA_AnalysisData;
import viroopa.com.medikart.MedicineReminder.MRA_SendReprtToDoctor;
import viroopa.com.medikart.R;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;

import viroopa.com.medikart.common.Add_Doctor_Dialog;
import viroopa.com.medikart.common.Change_member_Dialog;
import viroopa.com.medikart.common.ConstData;
import viroopa.com.medikart.common.DayAxisTestValueFormatter;
import viroopa.com.medikart.common.DayAxisValueFormatter;
import viroopa.com.medikart.common.Style;
import viroopa.com.medikart.common.dm_SetGoal_Dialog;
import viroopa.com.medikart.common.numerdialog;
import viroopa.com.medikart.dmMonitor.Module.DMM_AnalysisHeading;
import viroopa.com.medikart.dmMonitor.Module.DMM_AnalysisItemDetail;
import viroopa.com.medikart.dmMonitor.adapter.DMAD_AnalysisExpandable;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteDMHandler;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DMA_AnalysisDisplayActivity extends AppCompatActivity
        implements Add_Doctor_Dialog.OnDoctorSelectListener,
        numerdialog.OnNumberDialogDoneListener,
        Change_member_Dialog.OnMemberSelectListener {

    public static final String CON_Type_LastReading = "LastReading";
    public static final String CON_Type_Weekly = "Weekly";
    public static final String CON_Type_Last10 = "Last10";
    public static final String CON_Type_Monthly = "Monthly";
    public static final String CON_Type_Yearly = "Yearly";

    public static final String SHOW_TYPE_SUGAR = "bs";
    public static final String SHOW_TYPE_WEIGHT = "wt";
    private int mFillColor = Color.argb(150, 51, 181, 229);

    private String date_range_fro_graph="";

    private File dir, file;
    private PdfWriter writer;
    private  Image GraphImage,Graph_line,Graph_bar;

    private SimpleDateFormat format_date_for_pdf = new SimpleDateFormat("LLLL dd,yyyy hh:mm");

    DateFormat format_single_date = new SimpleDateFormat("dd");
    DateFormat format_single_date_month = new SimpleDateFormat("MM");


    private Cursor crs_graph;

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottom_navigation;

    @BindView(R.id.txtGoalBS)
    TextView txtGoalBS;
    @BindView(R.id.txtGoalWeight)
    TextView txtGoalWeight;

    @Nullable
    @BindView(R.id.img_prvs)
    ImageView _img_prvs;


    @Nullable
    @BindView(R.id.img_next)
    ImageView _img_next;

    @BindView(R.id.txtFromDate)
    TextView txtFromDate;

    @BindView(R.id.txtGraph)
    TextView txtGraph;
    @BindView(R.id.txtRdbbs)
    TextView txtRdbbs;
    @BindView(R.id.txtRdbwt)
    TextView txtRdbwt;

    @BindView(R.id.lnr_glucose_value_indicator)
    LinearLayout lnr_glucose_value_indicator;

    @BindView(R.id.lnr_weght_value_indicator)
    LinearLayout lnr_weght_value_indicator;



    @BindView(R.id.txtDM_NewEntry)
    TextView txtDM_NewEntry;
    @BindView(R.id.txtSetGoal)
    TextView txtSetGoal;
    @BindView(R.id.txtAddDoctor)
    TextView txtAddDoctor;
    @BindView(R.id.txtSendReport)
    TextView txtSendReport;


    @BindView(R.id.btnLastRead)
    Button btnLastRead;
    @BindView(R.id.btnWeekly)
    Button btnWeekly;
    @BindView(R.id.btnLast10)
    Button btnLast10;
    @BindView(R.id.btnMonthly)
    Button btnMonthly;
    @BindView(R.id.btnYearly)
    Button btnYearly;

    @BindView(R.id.spnrAMPM)
    Spinner spnrAMPM;

    @BindView(R.id.piechart_pdf)
    PieChart mPiechart_statistics;

    @BindView(R.id.expndLV_Reading)
    ExpandableListView expndLV_Reading;

    @BindView(R.id.pageIndicator)
    ProgressPageIndicator pageIndicator;

    @BindView(R.id.axis_desc)
    LinearLayout axis_desc;

    @BindView(R.id.lnrweekly)
    LinearLayout lnrweekly;

    @BindView(R.id.Linechart)
    LineChart mLineChart;

    @BindView(R.id.txt_axis_indicator)
    TextView txt_axis_indicator;


    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;


    private Menu objMemberMenu;
    private SharedPreferences pref;
    ViewPager viewPager_Graph;
    private SqliteDMHandler db_dm;
    AppController globalVariable;
    float normal_value_percent=0;
    float high_value_percent=0;
    float low_value_percent=0;
    Date Todate = Calendar.getInstance().getTime();
    private SQLiteHandler db;
    private String setting_weight_unit, setting_def_condition, setting_last_enterd_values, setting_def_glucose_unit;
    private String[] array_category;
    private String array_weight_unit[];
    private String[] arrayglucose_unit;

    private String sConditionFilter = "", sAMPMFilter = "", sChartfilterType;
    private Integer nMemberId, nRelationshipId;
    private boolean onResumecalled = false;

    private List<DMM_AnalysisHeading> catList = new ArrayList<DMM_AnalysisHeading>();
    ;
    private DMAD_AnalysisExpandable exAdpt;

    String sOnlyStartDate, sOnlyEndDate;
    private String sChartFilterValue;

    private BarChart mBarChart;
    private PieChart mPiechart_goal, mpiechart_normal;
    private Typeface tf;

    String fromdate, todate;

    String sGOAL_BloodSugar = "", sGOAL_Weight = "";

    DateFormat changedateFormat = new SimpleDateFormat("dd MMM , yyyy");
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat OnlydateFormat_query = new SimpleDateFormat("dd");

    Date current_date = Calendar.getInstance().getTime();

    private SimpleDateFormat format_date = new SimpleDateFormat("LLLL dd,yyyy");
    private SimpleDateFormat format_time = new SimpleDateFormat("hh:mm a");

    private SimpleDateFormat format_date_save = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format_time_save = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat format_time_hour = new SimpleDateFormat("H");

    Rectangle rect = new Rectangle(30, 30, 550, 800);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm_monitor_analysis);

        ButterKnife.bind(this);
        db = SQLiteHandler.getInstance(this);
        globalVariable = (AppController) getApplicationContext();
        pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        initialize_bottom_bar();
        db_dm = new SqliteDMHandler(this);
        array_category = getResources().getStringArray(R.array.array_category);
        array_weight_unit = getResources().getStringArray(R.array.array_weight_unit);
        arrayglucose_unit = getResources().getStringArray(R.array.dm_Unit);
        catList.clear();

        getIntenet();
        get_defaults();

        txt_axis_indicator.setText("Y-Glucose("+setting_def_glucose_unit+"),X-Date");

        viewPager_Graph = (ViewPager) findViewById(R.id.viewPager_Graph);
        viewPager_Graph.setOffscreenPageLimit(4);
        viewPager_Graph.setAdapter(new MyPagesAdapter());

        pageIndicator.setStrokeColor(Color.parseColor("#f38630"));

        mPiechart_statistics.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int newVis = mLineChart.getVisibility();
                if(newVis==View.VISIBLE)
                {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            getBitmapFromView(mPiechart_statistics,"P");
                            mPiechart_statistics.saveToGallery("akhil_piechart",100);

                        }
                    }, 500);

                }
            }
        });



        viewPager_Graph.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
                pageIndicator.setViewPager(viewPager_Graph);
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_LONG)
//                        .show();
                pageIndicator.setViewPager(viewPager_Graph);
            }

            public void onPageSelected(int position) {

                if(position==0 || position==1)
                {
                    axis_desc.setVisibility(View.VISIBLE);
                    if(sChartfilterType.equals("bs"))
                    {
                        lnr_glucose_value_indicator.setVisibility(View.VISIBLE);
                        lnr_weght_value_indicator.setVisibility(View.GONE);
                    }else
                    {
                        lnr_weght_value_indicator.setVisibility(View.VISIBLE);
                        lnr_glucose_value_indicator.setVisibility(View.GONE);
                    }


                }else
                {
                    axis_desc.setVisibility(View.INVISIBLE);
                    lnr_glucose_value_indicator.setVisibility(View.GONE);
                    lnr_weght_value_indicator.setVisibility(View.GONE);
                }

                pageIndicator.setViewPager(viewPager_Graph);
//                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_LONG)
//                        .show();
                // Check if this is the page you want.
            }
        });

        get_Goal();

        String[] array_AMPM = getResources().getStringArray(R.array.AMPM_array);
        ArrayAdapter adpt_AMPM = new ArrayAdapter<String>(this, R.layout.rxspinner_simple_text_layout, array_AMPM);
        spnrAMPM.setAdapter(adpt_AMPM);


        sChartfilterType = "bs";

        txtFromDate.setText(changedateFormat.format(current_date));

        date_range_fro_graph="Report for : "+changedateFormat.format(current_date);

        fromdate = dateFormat_query.format(current_date);
        todate = dateFormat_query.format(current_date);

        sOnlyStartDate = OnlydateFormat_query.format(current_date);
        sOnlyEndDate = OnlydateFormat_query.format(current_date);


        f_set_default(btnLastRead, "Graphical Representation", CON_Type_LastReading, "0");



       /* btnLastRead.setTextColor(Color.GRAY);
        btnWeekly.setTextColor(Color.GRAY);
        btnLast10.setTextColor(Color.GRAY);
        btnMonthly.setTextColor(Color.GRAY);
        btnYearly.setTextColor(Color.GRAY);

        btnLastRead.setBackgroundColor(Color.WHITE);
        btnWeekly.setBackgroundColor(Color.WHITE);
        btnLast10.setBackgroundColor(Color.WHITE);
        btnMonthly.setBackgroundColor(Color.WHITE);
        btnYearly.setBackgroundColor(Color.WHITE);

        btnLastRead.setBackgroundResource(R.drawable.rx_curve_orange_background);
        btnLastRead.setTextColor(Color.WHITE);

        txtGraph.setText("Graphical Representation");
        sConditionFilter = CON_Type_LastReading ;
        sChartFilterValue = "0";


        fillReading(sConditionFilter);

        setListViewHeight(expndLV_Reading);

        sAMPMFilter = spnrAMPM.getSelectedItem().toString();

        fillchart();*/

        setListViewHeight(expndLV_Reading);


        expndLV_Reading.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPos, long id) {
                boolean sFlag = false;
                ImageView img = (ImageView) v.findViewById(R.id.imgavgright);

                TextView txtcategory_Detail = (TextView) v.findViewById(R.id.txtcategory_Detail);
                if (txtcategory_Detail != null) {
                    if (txtcategory_Detail.getTag() != null) {
                        if (txtcategory_Detail.getTag().toString().contains("Category")) {
                            sFlag = true;
                        }
                    }
                }
                if (groupPos == 0) {
                    if (expndLV_Reading.isGroupExpanded(0)) {
                        img.setImageResource(R.drawable.gray_down_arrows);
                        // setListViewHeight(expndLV_Reading);
                    } else {
                        img.setImageResource(R.drawable.gray_up_arrows);
                        // setListViewHeight(expndLV_Reading);
                    }
                }

                ExpandableListAdapter listAdapter = expndLV_Reading.getExpandableListAdapter();

                setListViewHeight(parent, groupPos);
                return sFlag;
            }
        });

        spnrAMPM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                sAMPMFilter = spnrAMPM.getSelectedItem().toString();

                fillReading(sConditionFilter);
                create_statistics_pie_chart(sConditionFilter);
                fill_chart(sConditionFilter, sChartfilterType);


            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @OnClick(R.id.viewpager_previous)
    void previous_click()
    {
        int currentPage = viewPager_Graph.getCurrentItem();
        int totalPages = viewPager_Graph.getAdapter().getCount();

        int previousPage = currentPage-1;
        if (previousPage < 0) {
            // We can't go back anymore.
            // Loop to the last page. If you don't want looping just
            // return here.
            previousPage = totalPages - 1;
        }

        viewPager_Graph.setCurrentItem(previousPage, true);


    }


    @OnClick(R.id.viewpager_next)
    void next_click()
    {
        int currentPage = viewPager_Graph.getCurrentItem();
        int totalPages = viewPager_Graph.getAdapter().getCount();

        int nextPage = currentPage+1;
        if (nextPage >= totalPages) {
            nextPage = 0;
        }

        viewPager_Graph.setCurrentItem(nextPage, true);

    }


    @Override
    public void onSelectMember(String Relationship_id, String name, String Imagename) {
        globalVariable.setRealationshipId(Relationship_id);
        nRelationshipId = Integer.parseInt(Relationship_id);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserName", name);
        editor.commit();

        get_Goal();
        f_set_default(btnLastRead, "Graphical Representation", CON_Type_LastReading, "0");
        setListViewHeight(expndLV_Reading);

        ImageLoad(name, Imagename);
    }

    private void ImageLoad(String name, String imageName) {
        View o_view = objMemberMenu.findItem(R.id.circlularImage).getActionView();
        CircularImageView crImage = (CircularImageView) o_view.findViewById(R.id.imgView_circlularImage);
        TextView txtmemberName = (TextView) o_view.findViewById(R.id.txtUsername);

        crImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Change_member_Dialog dpd = Change_member_Dialog.newInstance("crImage");
                dpd.show(getFragmentManager(), "Change_member_Dialog");
            }
        });

        if (imageName != null) {
            String BPimgeName = imageName.substring(imageName.lastIndexOf('/') + 1, imageName.length());

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


    public interface FragmentRefreshListener {
        void onRefresh(String p_object, String p_value);
    }

    @Override
    public void onDone(int value, String sClass) {

        switch (sClass) {
            case "txtSetGoalGlucose":
                if (getFragmentRefreshListener() != null) {
                    getFragmentRefreshListener().onRefresh(sClass, Integer.toString(value));
                }
                break;
            case "txtSetGoalWeight":
                if (getFragmentRefreshListener() != null) {
                    getFragmentRefreshListener().onRefresh(sClass, Integer.toString(value));
                }
                break;
        }
    }


    @OnClick(R.id.txtRdbbs)
    void txtRdbbs() {
        txtRdbbs.setTextColor(Color.parseColor("#f38630"));
        txtRdbwt.setTextColor(Color.parseColor("#bdbdbd"));
        lnr_glucose_value_indicator.setVisibility(View.VISIBLE);
        lnr_weght_value_indicator.setVisibility(View.GONE);
        sChartfilterType = "bs";
        txt_axis_indicator.setText("Y-Glucose("+setting_def_glucose_unit+"),X-Date");
        fill_chart(sConditionFilter, sChartfilterType);

        if(mpiechart_normal!=null) {
            create_statistics_pie_chart(sConditionFilter);
        }
    }

    @OnClick(R.id.txtRdbwt)
    void txtRdbwt() {
        txtRdbwt.setTextColor(Color.parseColor("#f38630"));
        txtRdbbs.setTextColor(Color.parseColor("#bdbdbd"));
        txt_axis_indicator.setText("Y-Weight("+setting_weight_unit+"),X-Date");
        sChartfilterType = "wt";
        lnr_glucose_value_indicator.setVisibility(View.GONE);
        lnr_weght_value_indicator.setVisibility(View.VISIBLE);

        fill_chart(sConditionFilter, sChartfilterType);
        if(mpiechart_normal!=null) {
            create_analysis_weight_pie(sConditionFilter);
          /*  mpiechart_normal.setData(null);
            mpiechart_normal.setNoDataText("Not available for weight");
            mpiechart_normal.invalidate();*/
           // fill_pie_chart_against_normal();
        }

    }

    @OnClick(R.id.txtHeader)
    void txtheaderclick() {
        dm_SetGoal_Dialog myDiag = dm_SetGoal_Dialog.newInstance(nMemberId, nRelationshipId);
        myDiag.show(getFragmentManager(), "Diag");
    }

    @OnClick(R.id.btnLastRead)
    void btnLastRead() {
        try {
            _img_prvs.setVisibility(View.GONE);
            _img_next.setVisibility(View.GONE);

            Todate = Calendar.getInstance().getTime();
            current_date = Calendar.getInstance().getTime();
            fromdate = changedateFormat.format(current_date);

            txtFromDate.setText(fromdate);
            date_range_fro_graph="Reports of:"+fromdate;

            sOnlyStartDate = OnlydateFormat_query.format(current_date);
            sOnlyEndDate = OnlydateFormat_query.format(current_date);

            f_set_default(btnLastRead, "Graphical Representation", CON_Type_LastReading, "0");
        } catch (Exception ex) {
        }
    }

    @OnClick(R.id.img_prvs)
    void img_previous_click() {
        if (sConditionFilter.equals(CON_Type_Weekly)) {
            Todate = date_addDays(Todate, -7);
            f_set_default(btnWeekly, "Weekly Progress Graph", CON_Type_Weekly, "-7");

            txtFromDate.setText(changedateFormat.format(date_addDays(Todate, -7)) + " - " + changedateFormat.format(Todate));
            date_range_fro_graph="Reports from "+changedateFormat.format(date_addDays(Todate, -7))+" to "+changedateFormat.format(Todate);

        }
        if (sConditionFilter.equals(CON_Type_Monthly)) {
            Todate = date_addDays(Todate, -30);
            int i = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
            i = -i;

            f_set_default(btnMonthly, "Monthly Progress Graph", CON_Type_Monthly, String.valueOf(i));

            txtFromDate.setText(changedateFormat.format(date_addDays(Todate, -30)) + " - " + changedateFormat.format(Todate));
            date_range_fro_graph="Reports from "+changedateFormat.format(date_addDays(Todate, -30))+" to "+changedateFormat.format(Todate);
        }

        if (sConditionFilter.equals(CON_Type_Yearly)) {
            Todate = date_addDays(Todate, -365);
            f_set_default(btnYearly, "Yearly Progress Graph", CON_Type_Yearly, "-365");
            txtFromDate.setText(changedateFormat.format(date_addDays(Todate, -365)) + " - " + changedateFormat.format(Todate));
            date_range_fro_graph="Reports from "+changedateFormat.format(date_addDays(Todate, -365))+" to "+changedateFormat.format(Todate);
        }

    }

    @OnClick(R.id.img_next)
    void img_img_next_click() {
        if (sConditionFilter.equals(CON_Type_Weekly)) {
            Todate = date_addDays(Todate, 7);
            f_set_default(btnWeekly, "Weekly Progress Graph", CON_Type_Weekly, "-7");

            txtFromDate.setText(changedateFormat.format(date_addDays(Todate, -7)) + " - " + changedateFormat.format(Todate));
            date_range_fro_graph="Reports from "+changedateFormat.format(date_addDays(Todate, -7))+" to "+changedateFormat.format(Todate);
        }
        if (sConditionFilter.equals(CON_Type_Monthly)) {
            Todate = date_addDays(Todate, 30);
            int i = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
            i = -i;

            f_set_default(btnMonthly, "Monthly Progress Graph", CON_Type_Monthly, String.valueOf(i));

            txtFromDate.setText(changedateFormat.format(date_addDays(Todate, -30)) + " - " + changedateFormat.format(Todate));
            date_range_fro_graph="Reports from "+changedateFormat.format(date_addDays(Todate, -30))+" to "+changedateFormat.format(Todate);
        }

        if (sConditionFilter.equals(CON_Type_Yearly)) {
            Todate = date_addDays(Todate, 365);
            f_set_default(btnYearly, "Yearly Progress Graph", CON_Type_Yearly, "-365");
            txtFromDate.setText(changedateFormat.format(date_addDays(Todate, -365)) + " - " + changedateFormat.format(Todate));
            date_range_fro_graph="Reports from "+changedateFormat.format(date_addDays(Todate, -365))+" to "+changedateFormat.format(Todate);
        }

    }

    @OnClick(R.id.btnWeekly)
    void btnWeekly() {
        try {
            _img_prvs.setVisibility(View.VISIBLE);
            _img_next.setVisibility(View.VISIBLE);

            current_date = Calendar.getInstance().getTime();
            Todate = Calendar.getInstance().getTime();
            fromdate = changedateFormat.format(current_date);

            current_date = date_addDays(current_date, -7);
            todate = changedateFormat.format(current_date);
            txtFromDate.setText(todate + " - " + fromdate);
            date_range_fro_graph="Reports from "+todate+" to "+fromdate;

            sOnlyStartDate = OnlydateFormat_query.format(current_date);
            sOnlyEndDate = OnlydateFormat_query.format(current_date);

            f_set_default(btnWeekly, "Weekly Progress Graph", CON_Type_Weekly, "-8");
        } catch (Exception ex) {
        }
    }

    @OnClick(R.id.btnLast10)
    void btnLast10() {
        try {
            _img_prvs.setVisibility(View.GONE);
            _img_next.setVisibility(View.GONE);

            current_date = Calendar.getInstance().getTime();
            fromdate = changedateFormat.format(current_date);
            Todate = Calendar.getInstance().getTime();
            current_date = date_addDays(current_date, -10);
            todate = changedateFormat.format(current_date);

           // txtFromDate.setText(todate + " - " + fromdate);
             txtFromDate.setText("Last 10 Readings");
            date_range_fro_graph="Last 10 Readings";
            f_set_default(btnLast10, "Last 10 Reading Progress Graph", CON_Type_Last10, "-11");
        } catch (Exception ex) {
        }
    }

    @OnClick(R.id.btnMonthly)
    void btnMonthly() {
        try {
            _img_prvs.setVisibility(View.VISIBLE);
            _img_next.setVisibility(View.VISIBLE);
            Todate = Calendar.getInstance().getTime();
            current_date = Calendar.getInstance().getTime();
            fromdate = changedateFormat.format(current_date);

            current_date = date_addDays(current_date, -30);
            todate = changedateFormat.format(current_date);

            txtFromDate.setText(todate + " - " + fromdate);
            date_range_fro_graph="Reports from "+todate+" to "+fromdate;

            int i = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
            i = -i;

            f_set_default(btnMonthly, "Monthly Progress Graph", CON_Type_Monthly, String.valueOf(i));
        } catch (Exception ex) {
        }
    }

    @OnClick(R.id.btnYearly)
    void btnYearly() {
        try {
            _img_prvs.setVisibility(View.VISIBLE);
            _img_next.setVisibility(View.VISIBLE);

            Todate = Calendar.getInstance().getTime();

            current_date = Calendar.getInstance().getTime();
            fromdate = changedateFormat.format(current_date);

            current_date = date_addDays(current_date, -365);
            todate = changedateFormat.format(current_date);

            txtFromDate.setText(todate + " - " + fromdate);
            date_range_fro_graph="Reports from "+todate+" to "+fromdate;

            f_set_default(btnYearly, "Yearly Progress Graph", CON_Type_Yearly, "-365");
        } catch (Exception ex) {
        }
    }

    private void f_set_default(Button p_object, String p_GraphText, String p_CurrentGraph, String p_ChartFilter) {

        btnLastRead.setTextColor(Color.GRAY);
        btnWeekly.setTextColor(Color.GRAY);
        btnLast10.setTextColor(Color.GRAY);
        btnMonthly.setTextColor(Color.GRAY);
        btnYearly.setTextColor(Color.GRAY);

        btnLastRead.setBackgroundColor(Color.WHITE);
        btnWeekly.setBackgroundColor(Color.WHITE);
        btnLast10.setBackgroundColor(Color.WHITE);
        btnMonthly.setBackgroundColor(Color.WHITE);
        btnYearly.setBackgroundColor(Color.WHITE);

        p_object.setBackgroundResource(R.drawable.rx_curve_orange_background);
        p_object.setTextColor(Color.WHITE);

        txtGraph.setText(p_GraphText);
        sConditionFilter = p_CurrentGraph;
        sChartFilterValue = p_ChartFilter;
        sAMPMFilter = spnrAMPM.getSelectedItem().toString();

        fillReading(p_CurrentGraph);
        create_statistics_pie_chart(sConditionFilter);
        fill_chart(sConditionFilter, sChartfilterType);


    }

    @OnClick(R.id.txtDM_NewEntry)
    void txtDM_NewEntry() {

    }



 /*   private void fillchart() {

        //mLineChart.setOnChartGestureListener(this);
        //mLineChart.setOnChartValueSelectedListener(this);
        mLineChart.setDrawGridBackground(false);

        // no description text
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("No Data Available.");

        // enable value highlighting

        // enable touch gestures
        mLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(true);


        //  BPViewer mv = new BPViewer(this, R.layout.custom_marker_view);

        // set the marker to the chart
        // mLineChart.setMarkerView(mv);

        LimitLine llXAxis = new LimitLine(0f, "");

        llXAxis.setTextSize(10f);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.addLimitLine(llXAxis);


        YAxis RIGHTAxis = mLineChart.getAxisRight();
        RIGHTAxis.removeAllLimitLines();
        RIGHTAxis.setAxisMinValue(0f);
        RIGHTAxis.setStartAtZero(false);

        Cursor  cursor_Chart = db_dm.getAnalysisMaxLineChartData(nMemberId, nRelationshipId,sChartFilterValue,sAMPMFilter);

        ArrayList<String> xVals = new ArrayList<String>();

        ArrayList<Integer> iIndex = new ArrayList<Integer>();
        float fMax=0;
        float fMin=0;
        float fMaxwt=0;
        float fMinwt=0;
        YAxis leftAxis = mLineChart.getAxisLeft();
        mLineChart.getAxisLeft().removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        mLineChart.getAxisLeft().setAxisMinValue(0f);
        mLineChart.getAxisLeft().setStartAtZero(false);
        //mLineChart.setViewPortOffsets(0, 20, 0, 0);
        // mLineChart.setBackgroundColor(Color.rgb(104, 241, 175));
        if(cursor_Chart!=null || !cursor_Chart.equals("")) {


            Integer J = cursor_Chart.getCount();
            cursor_Chart.moveToFirst();
            if (cursor_Chart.getCount() > 0) {

                // fMax = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bs_avg"))));
                fMax = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bs_avg"))));
                fMin = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bs_min"))));
                fMaxwt = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("max_wt"))));
                fMinwt = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("min_wt"))));

                if(sChartfilterType.equals("bs")) {
                    if(sConditionFilter.equals(CON_Type_LastReading)) {
                        if(fMax==fMin)
                        {
                            mLineChart.getAxisLeft().setAxisMinValue(0f);
                        }
                        else
                        {
                            mLineChart.getAxisLeft().setAxisMinValue(fMin);
                        }
                    }
                    else {
                        if(fMax==fMin)
                        {
                            mLineChart.getAxisLeft().setAxisMinValue(0f);
                        }
                        else
                        {
                            mLineChart.getAxisLeft().setAxisMinValue(fMin);
                        }
                    }
                    //mLineChart.getAxisLeft().setAxisMaxValue(fMax);
                }
                else {
                    if(sConditionFilter.equals(CON_Type_LastReading)) {


                        //mLineChart.getAxisLeft().setAxisMinValue(0f);
                        if(fMinwt==fMaxwt)
                        {
                            mLineChart.getAxisLeft().setAxisMinValue(0f);
                        }
                        else {
                            mLineChart.getAxisLeft().setAxisMinValue(fMinwt);
                        }
                    }
                    else {
                        if(fMinwt==fMaxwt)
                        {
                            mLineChart.getAxisLeft().setAxisMinValue(0f);
                        }
                        else {
                            mLineChart.getAxisLeft().setAxisMinValue(fMinwt);
                        }
                    }
                    //mLineChart.getAxisLeft().setAxisMaxValue(fMaxwt);
                }
//                mLineChart.getAxisLeft().setStartAtZero(false);
            }


        }


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // limit lines are drawn behind data (and not on top)

        mLineChart.getAxisRight().setDrawGridLines(false);

        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getXAxis().setDrawGridLines(false);

        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getAxisRight().removeAllLimitLines();
        mLineChart.getAxisLeft().removeAllLimitLines();
        mLineChart.getXAxis().removeAllLimitLines();


        if(sConditionFilter.equals(CON_Type_LastReading)) {

            if(sChartfilterType.equals("bs")) {
                set2Data();
            }
            else {
                set2DataWT();
            }
        }
        else
        {
            if(sChartfilterType.equals("bs")) {
                setconditionData();
            }
            else {
                setconditionDataWT();
            }
        }

        mLineChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

        Legend l = mLineChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        // mChart.setFillFormatter(new fillformator());
    }*/

 /*   private void setconditionDataWT() {

        Cursor cursor_Chart;
        Cursor cursor_MinMaxChart;
        Integer g_time = 0;
        String AMPM = "";

        cursor_Chart = db_dm.getAllData_Chart(nMemberId, nRelationshipId);
        current_date = Calendar.getInstance().getTime();
        todate = dateFormat_query.format(current_date);
        //  Integer iDays=Integer.parseInt(sChartFilterValue)+1;
        Integer iDays=0;

        if(sConditionFilter.equals(CON_Type_Monthly)){
            iDays=Integer.parseInt(sChartFilterValue);
        }
        else{
            iDays=Integer.parseInt(sChartFilterValue)+1;
        }
        current_date = date_addDays(current_date, iDays);
        // current_date = date_addDays(current_date, -7);
        fromdate = dateFormat_query.format(current_date);
        // fromdate = "2016-03-19";
        //todate = "2016-03-22";

        cursor_Chart = db_dm.showAllData_Chart(nMemberId, nRelationshipId, fromdate, todate);

        cursor_MinMaxChart = db_dm.showMINMAXAllData_Chart(nMemberId, nRelationshipId, fromdate, todate);
        ArrayList<String> xVals = new ArrayList<String>();

        cursor_MinMaxChart.moveToFirst();
        String sMinval = String.valueOf(cursor_MinMaxChart.getString(cursor_MinMaxChart.getColumnIndex("MinVal")));
        String sMaxval = String.valueOf(cursor_MinMaxChart.getString(cursor_MinMaxChart.getColumnIndex("MaxVal")));

        cursor_Chart.moveToFirst();

        cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId, sChartFilterValue, sAMPMFilter);

        xVals = new ArrayList<String>();

        ArrayList<Integer> iIndex = new ArrayList<Integer>();
        if (cursor_Chart != null || !cursor_Chart.equals("")) {


            Integer J = cursor_Chart.getCount();
            cursor_Chart.moveToFirst();
            if (cursor_Chart.getCount() > 0) {


                ArrayList<Date> dates = new ArrayList<Date>();

                iIndex = new ArrayList<Integer>();
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

                Date date1 = null;
                Date date2 = null;

                try {
                    current_date = Calendar.getInstance().getTime();
                    String stodate = dateFormat_query.format(current_date);
                    // dateFormat_query
                    date2 = df1.parse(stodate);

//                  Integer i=  Integer.parseInt(sChartFilterValue);

                    if (sChartFilterValue.equals("0")) {
                        current_date = Calendar.getInstance().getTime();
                    } else {
                        current_date = date_addDays(current_date, iDays);
                    }
                    String sfromdate = dateFormat_query.format(current_date);
                    date1 = df1.parse(sfromdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date1);


                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(date2);

                while (!cal1.after(cal2)) {
                    dates.add(cal1.getTime());
                    sOnlyStartDate = OnlydateFormat_query.format(cal1.getTime());
                    cal1.add(Calendar.DATE, 1);
                    iIndex.add(Integer.parseInt(sOnlyStartDate));

                    xVals.add(String.valueOf(sOnlyStartDate));
                }
            }


            int i = 0;

            ArrayList<Entry> yVals = new ArrayList<Entry>();

            i = 0;
            cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId, sChartFilterValue, sAMPMFilter);
            cursor_Chart.moveToFirst();
            while (!cursor_Chart.isAfterLast()) {

                float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("max_wt"))));

                String dateofDrink = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("g_date")));

                Calendar c = Calendar.getInstance();
                Date sdate = new Date();
                SimpleDateFormat sdate_format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    sdate = sdate_format.parse(dateofDrink);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.setTime(sdate);
                int dm_day = c.get(Calendar.DATE);


                if (sChartFilterValue.equals("0")) {

                    yVals.add(new Entry(f1, i));
                } else {
                    int z = iIndex.indexOf((dm_day));

                    yVals.add(new Entry(f1, z));
                }
                // yVals.add(new Entry(f1, (dm_day-1)));
                i++;
                cursor_Chart.moveToNext();
            }



            cursor_Chart.close();
            String MinMax = "Heighest-" + sMaxval + "- Lowest-" + sMinval;


            // create a dataset and give it a type
            LineDataSet set1 = new LineDataSet(yVals, "Weight");
            //LineDataSet set1 = new LineDataSet(yVals,MinMax);
            //set1.setFillAlpha(110);
            set1.setFillColor(Color.RED);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
//            set1.setColor(Color.RED);
//            set1.setCircleColor(Color.GREEN);
            set1.setCircleColor(Color.parseColor("#f38630"));
            set1.setColor(Color.parseColor("#f38630"));
            set1.setLineWidth(1f);
            set1.setCircleSize(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            //set1.setFillAlpha(65);
            // set1.setFillColor(Color.RED);
            set1.setDrawFilled(true);
            //  set1.setFillColor(Color.rgb(8, 68, 106));
            set1.setFillColor(Color.parseColor("#f38630"));
            set1.setValueTextColor(Color.TRANSPARENT);

//        set1.setDrawFilled(true);
            // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
            // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

            ArrayList<Entry> yVals2 = new ArrayList<Entry>();
            //  cursor_Chart.moveToFirst();

            ArrayList<LineDataSet> dataSets;
            dataSets = new ArrayList<LineDataSet>();
            dataSets.add(set1); // add the datasets

          //  data.notifyDataChanged();
            // set data
          //  mLineChart.setData(data);
        }
    }
*/
  /*  private void setconditionData() {


        Cursor cursor_Chart;
        Cursor cursor_MinMaxChart;
        Integer g_time = 0;
        String AMPM = "";

        cursor_Chart = db_dm.getAllData_Chart(nMemberId, nRelationshipId);

        current_date = Calendar.getInstance().getTime();
        todate = dateFormat_query.format(current_date);
        Integer iDays=0;
        if(sConditionFilter.equals(CON_Type_Monthly)){
            iDays=Integer.parseInt(sChartFilterValue);
        }
        else{
            iDays=Integer.parseInt(sChartFilterValue)+1;
        }
        // Integer iDays=Integer.parseInt(sChartFilterValue)+1;
        current_date = date_addDays(current_date, iDays);
        // current_date = date_addDays(current_date, -7);
        fromdate = dateFormat_query.format(current_date);
        cursor_Chart = db_dm.showAllData_Chart(nMemberId, nRelationshipId, fromdate, todate);

        cursor_MinMaxChart = db_dm.showMINMAXAllData_Chart(nMemberId, nRelationshipId, fromdate, todate);
        ArrayList<String> xVals = new ArrayList<String>();

        cursor_MinMaxChart.moveToFirst();
        String sMinval = String.valueOf(cursor_MinMaxChart.getString(cursor_MinMaxChart.getColumnIndex("MinVal")));
        String sMaxval = String.valueOf(cursor_MinMaxChart.getString(cursor_MinMaxChart.getColumnIndex("MaxVal")));


        cursor_Chart.moveToFirst();

        cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId, sChartFilterValue, sAMPMFilter);

        xVals = new ArrayList<String>();

        ArrayList<Integer> iIndex = new ArrayList<Integer>();
        if (cursor_Chart != null || !cursor_Chart.equals("")) {


            Integer J = cursor_Chart.getCount();
            cursor_Chart.moveToFirst();
            if (cursor_Chart.getCount() > 0) {


                ArrayList<Date> dates = new ArrayList<Date>();

                iIndex = new ArrayList<Integer>();
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

                Date date1 = null;
                Date date2 = null;

                try {
                    current_date = Calendar.getInstance().getTime();
                    String stodate = dateFormat_query.format(current_date);
                    // dateFormat_query
                    date2 = df1.parse(stodate);

//                  Integer i=  Integer.parseInt(sChartFilterValue);

                    if (sChartFilterValue.equals("0")) {
                        current_date = Calendar.getInstance().getTime();
                    } else {
                        current_date = date_addDays(current_date, iDays);
                    }
                    String sfromdate = dateFormat_query.format(current_date);
                    date1 = df1.parse(sfromdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date1);


                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(date2);

                while (!cal1.after(cal2)) {
                    dates.add(cal1.getTime());
                    sOnlyStartDate = OnlydateFormat_query.format(cal1.getTime());
                    cal1.add(Calendar.DATE, 1);
                    iIndex.add(Integer.parseInt(sOnlyStartDate));

                    xVals.add(String.valueOf(sOnlyStartDate));
                }
            }


            int i = 0;
            cursor_Chart.close();
            ArrayList<Entry> yVals = new ArrayList<Entry>();

            i = 0;
            cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId, sChartFilterValue, sAMPMFilter);
            cursor_Chart.moveToFirst();
            while (!cursor_Chart.isAfterLast()) {

                float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bs_avg"))));

                String dateofDrink = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("g_date")));

                Calendar c = Calendar.getInstance();
                Date sdate = new Date();
                SimpleDateFormat sdate_format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    sdate = sdate_format.parse(dateofDrink);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.setTime(sdate);
                int dm_day = c.get(Calendar.DATE);


                if (sChartFilterValue.equals("0")) {

                    yVals.add(new Entry(f1, i));
                } else {
                    int z = iIndex.indexOf((dm_day));

                    yVals.add(new Entry(f1, z));
                }
                // yVals.add(new Entry(f1, (dm_day-1)));
                i++;
                cursor_Chart.moveToNext();
            }



            cursor_Chart.close();
            String MinMax = "Heighest-" + sMaxval + "- Lowest-" + sMinval;


            // create a dataset and give it a type
            LineDataSet set1 = new LineDataSet(yVals, "Blood Sugar");
            //LineDataSet set1 = new LineDataSet(yVals,MinMax);
            //set1.setFillAlpha(110);
            set1.setFillColor(Color.RED);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
//            set1.setColor(Color.RED);
//            set1.setCircleColor(Color.GREEN);
            set1.setCircleColor(Color.parseColor("#f38630"));
            set1.setColor(Color.parseColor("#f38630"));
            set1.setLineWidth(1f);
            set1.setCircleSize(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            //set1.setFillAlpha(65);
            set1.setFillColor(Color.RED);
            set1.setDrawFilled(true);
            set1.setFillColor(Color.parseColor("#f38630"));
            set1.setValueTextColor(Color.TRANSPARENT);
            // set1.setFillColor(Color.BLUE);
//        set1.setDrawFilled(true);
            // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
            // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

            ArrayList<Entry> yVals2 = new ArrayList<Entry>();
            //  cursor_Chart.moveToFirst();

            ArrayList<LineDataSet> dataSets;
            dataSets = new ArrayList<LineDataSet>();
            dataSets.add(set1); // add the datasets

        //    LineData data = new LineData(xVals, dataSets);
          //  data.notifyDataChanged();
            // set data
          //  mLineChart.setData(data);
        }
    }*/

 /*   private void set2DataWT() {

        Cursor cursor_Chart;
        Cursor cursor_MinMaxChart;
        Integer g_time = 0;
        String AMPM="";

        cursor_Chart = db_dm.getAllData_Chart(nMemberId, nRelationshipId);

        current_date = Calendar.getInstance().getTime();
        fromdate = dateFormat_query.format(current_date);
        current_date = date_addDays(current_date, Integer.parseInt(sChartFilterValue));
        todate = dateFormat_query.format(current_date);
        cursor_Chart = db_dm.showAllData_Chart(nMemberId, nRelationshipId, fromdate, todate);

        cursor_MinMaxChart = db_dm.showMINMAXAllData_Chart(nMemberId, nRelationshipId, fromdate, todate);
        ArrayList<String> xVals = new ArrayList<String>();

        cursor_MinMaxChart.moveToFirst();
        String sMinval = String.valueOf(cursor_MinMaxChart.getString(cursor_MinMaxChart.getColumnIndex("MinVal")));
        String sMaxval = String.valueOf(cursor_MinMaxChart.getString(cursor_MinMaxChart.getColumnIndex("MaxVal")));


        cursor_Chart.moveToFirst();


        for (Integer k = 1; k <= 24; k = k + 1) {
            xVals.add(String.valueOf(k));
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId,sChartFilterValue, sAMPMFilter);
        Integer h=cursor_Chart.getCount();
        cursor_Chart.moveToFirst();
        while (!cursor_Chart.isAfterLast()) {

            float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("max_wt"))));

            try
            {
                Date date_time ;
                String Hours = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("g_time")));
                date_time = format_time_save.parse(Hours);
                g_time = Integer.parseInt(format_time_hour.format(date_time));

            } catch (Exception ex) {
                String s = String.valueOf(ex);
            }





            yVals.add(new Entry(f1, (g_time - 1) ));

            cursor_Chart.moveToNext();
        }
        cursor_Chart.close();
        String MinMax = "Heighest-" + sMaxval + "- Lowest-" + sMinval;

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Weight");
        //LineDataSet set1 = new LineDataSet(yVals,MinMax);
        //set1.setFillAlpha(110);
        set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
//        set1.setColor(Color.RED);
//        set1.setCircleColor(Color.GREEN);
        set1.setCircleColor(Color.parseColor("#f38630"));
        set1.setColor(Color.parseColor("#f38630"));
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setValueTextColor(Color.TRANSPARENT);
        //set1.setFillAlpha(65);
        // set1.setFillColor(Color.RED);

        set1.setDrawFilled(true);
        set1.setFillColor(Color.parseColor("#f38630"));

        //  set1.setFillColor(Color.BLUE);
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        //  cursor_Chart.moveToFirst();

        ArrayList<LineDataSet> dataSets;
        dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        //LineData data = new LineData(xVals, dataSets);
      //  data.notifyDataChanged();
        // set data
      //  mLineChart.setData(data);
    }
*/
  /*  private void set2Data() {

        Cursor cursor_Chart;
        Cursor cursor_MinMaxChart;
        Integer g_time = 0;

        current_date = Calendar.getInstance().getTime();
        fromdate = dateFormat_query.format(current_date);
        current_date = date_addDays(current_date, Integer.parseInt(sChartFilterValue));
        todate = dateFormat_query.format(current_date);

        cursor_MinMaxChart = db_dm.showMINMAXAllData_Chart(nMemberId, nRelationshipId, fromdate, todate);
        ArrayList<String> xVals = new ArrayList<String>();

        cursor_MinMaxChart.moveToFirst();
        String sMinval = String.valueOf(cursor_MinMaxChart.getString(cursor_MinMaxChart.getColumnIndex("MinVal")));
        String sMaxval = String.valueOf(cursor_MinMaxChart.getString(cursor_MinMaxChart.getColumnIndex("MaxVal")));

        for (Integer k = 1; k <= 24; k = k + 1) {
        //for (Integer k = 0; k <= 23; k = k + 1) {
            xVals.add(String.valueOf(k));
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId,sChartFilterValue, sAMPMFilter);
        Integer h=cursor_Chart.getCount();
        cursor_Chart.moveToFirst();
        while (!cursor_Chart.isAfterLast()) {

            float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bs_avg"))));

            try
            {
                Date date_time ;
                String Hours = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("g_time")));
                date_time = format_time_save.parse(Hours);
                g_time = Integer.parseInt(format_time_hour.format(date_time));

            } catch (Exception ex) {
                String s = String.valueOf(ex);
            }


            yVals.add(new Entry(f1, (g_time-1) ));

            cursor_Chart.moveToNext();
        }
        cursor_Chart.close();
        String MinMax = "Heighest-" + sMaxval + "- Lowest-" + sMinval;


        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Blood Sugar");
        //LineDataSet set1 = new LineDataSet(yVals,MinMax);
        //set1.setFillAlpha(110);
        set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
//        set1.setColor(Color.RED);
//        set1.setCircleColor(Color.RED);
        set1.setCircleColor(Color.parseColor("#f38630"));
        set1.setColor(Color.parseColor("#f38630"));
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setValueTextColor(Color.TRANSPARENT);
        // set1.setFillAlpha(65);
//        set1.setFillColor(Color.RED);

        set1.setDrawFilled(true);
        // set1.setFillColor(Color.BLUE);
        set1.setFillColor(Color.parseColor("#f38630"));

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        //  cursor_Chart.moveToFirst();

        ArrayList<LineDataSet> dataSets;
        dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

      //  LineData data = new LineData(xVals, dataSets);
     //   data.notifyDataChanged();
        // set data
      ///  mLineChart.setData(data);
    }*/

    public void fillReading(String p_Filter) {
        try {
            catList = new ArrayList<DMM_AnalysisHeading>();
            List<DMM_AnalysisItemDetail> result = null;
            DMM_AnalysisItemDetail item = null;
            DMM_AnalysisHeading cat1 = null;

            String g_value = "";
            String weight_number = "";

            if (p_Filter.equals(CON_Type_LastReading)) {
                Cursor cursordata = null;
                cursordata = db_dm.getLastReadingDMMonitarData(nMemberId, nRelationshipId);
                if (cursordata != null) {
                    if (cursordata.moveToFirst()) {
                        do {
                            if (setting_def_glucose_unit.equals("mg/dl")) {
                                g_value = cursordata.getString(cursordata.getColumnIndex("g_value"));
                            } else {
                                g_value = cursordata.getString(cursordata.getColumnIndex("g_mmolval"));
                            }

                            if (setting_weight_unit.equals("kg")) {
                                weight_number = cursordata.getString(cursordata.getColumnIndex("weight_number"));
                            } else {
                                weight_number = cursordata.getString(cursordata.getColumnIndex("lbweight_number"));
                            }


                            String g_category = cursordata.getString(cursordata.getColumnIndex("g_category"));
                            String g_date = cursordata.getString(cursordata.getColumnIndex("g_date"));
                            String g_time = cursordata.getString(cursordata.getColumnIndex("g_time"));
                            String AMPM = cursordata.getString(cursordata.getColumnIndex("AMPM"));
                            String g_unit = cursordata.getString(cursordata.getColumnIndex("g_unit"));

                            result = new ArrayList<DMM_AnalysisItemDetail>();

                           /* cat1 = new DMM_AnalysisHeading(0,"Last Reading", "Blank", "","");
                            item = new DMM_AnalysisItemDetail(99,"Blank", p_Filter,sAMPMFilter );
                            result.add(item);
                            cat1.setItemList(result);
                            catList.add(cat1);*/

                            String bsDec = g_value + " " + setting_def_glucose_unit;
                            String sBsStatus = get_percentage_calcution(sGOAL_BloodSugar, g_value);
                            cat1 = new DMM_AnalysisHeading(0, "Last Reading", "Blood Sugar", bsDec, sBsStatus);
                            item = new DMM_AnalysisItemDetail(99, "99", p_Filter, sAMPMFilter, dateFormat_query.format(Todate));
                            result.add(item);
                            cat1.setItemList(result);
                            catList.add(cat1);

                            String wtDec = weight_number + " " + setting_weight_unit;
                            String sWtStatus = get_percentage_calcution(sGOAL_Weight, weight_number);
                            cat1 = new DMM_AnalysisHeading(0, "Last Reading", "Weight", wtDec, sWtStatus);
                            item = new DMM_AnalysisItemDetail(99, "99", p_Filter, sAMPMFilter, dateFormat_query.format(Todate));
                            result.add(item);
                            cat1.setItemList(result);
                            catList.add(cat1);

                            result = new ArrayList<DMM_AnalysisItemDetail>();
                            Date date_time;
                            date_time = format_date_save.parse(g_date);
                            g_date = format_date.format(date_time);
                            date_time = format_time_save.parse(g_time);
                            g_time = format_time.format(date_time);

                            cat1 = new DMM_AnalysisHeading(0, "Category", g_category, g_date + " " + g_time, "");
                            item = new DMM_AnalysisItemDetail(99, "99", p_Filter, sAMPMFilter, dateFormat_query.format(Todate));
                            result.add(item);
                            cat1.setItemList(result);
                            catList.add(cat1);

                        } while (cursordata.moveToNext());
                    }
                    cursordata.close();
                }
            } else {
                result = new ArrayList<DMM_AnalysisItemDetail>();

                cat1 = new DMM_AnalysisHeading(0, "Blood Pressure", "Min", "Max", "Avg");
                item = new DMM_AnalysisItemDetail(0, "0", p_Filter, sAMPMFilter, dateFormat_query.format(Todate));
                result.add(item);
                cat1.setItemList(result);
                catList.add(cat1);

                result = new ArrayList<DMM_AnalysisItemDetail>();
                cat1 = new DMM_AnalysisHeading(1, "Read all " + p_Filter + " readings", "", "", "");
                item = new DMM_AnalysisItemDetail(1, "1", p_Filter, sAMPMFilter, dateFormat_query.format(Todate));
                result.add(item);
                cat1.setItemList(result);
                catList.add(cat1);
            }

            exAdpt = new DMAD_AnalysisExpandable(catList, DMA_AnalysisDisplayActivity.this, DMA_AnalysisDisplayActivity.this);
            expndLV_Reading.setAdapter(exAdpt);
            if (!p_Filter.equals(CON_Type_LastReading)) {
                expndLV_Reading.expandGroup(0);
            }

            setListViewHeight(expndLV_Reading, (expndLV_Reading.getAdapter().getCount() - 1));
            //expndLV_Reading.expandGroup(0);
        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
    }

    private String get_percentage_calcution(String Goal_Value, String Actual_Value) {
        String value = "";
        Double Actual_value = 0.0;
        Double f_value = 0.0;
        Double s_value = 0.0;

        try {
            if ((Goal_Value == null) || (Goal_Value == "")) {
                value = "Goal is not set";
            } else if ((Actual_Value == null) || (Actual_Value == "")) {
                value = "Value not found";
            } else {
                f_value = Double.parseDouble(Goal_Value);
                s_value = Double.parseDouble(Actual_Value);

                if (f_value > s_value) {
                    Actual_value = ((f_value - s_value) / f_value) * 100;
                    value = String.format("%.2f", Actual_value) + "% Less than the desire goal";
                } else if (s_value > f_value) {
                    Actual_value = ((s_value - f_value) / f_value) * 100;
                    value = String.format("%.2f", Actual_value) + "% More than the desire goal";
                } else if (s_value.equals(f_value)) {
                    value = "Congrats your goal is achived";
                }
            }
        } catch (Exception e) {
            e.toString();
        }
        return value;
    }

    private void getIntenet() {

        nMemberId = Integer.parseInt(pref.getString("memberid", ""));

        if (globalVariable.getRealationshipId() != null) {
            nRelationshipId = Integer.parseInt(globalVariable.getRealationshipId());
        } else {
            nRelationshipId = 8;
        }
    }

    private void get_Goal() {
        Cursor cursordata;
        cursordata = db_dm.getgoalDMMonitarData(nMemberId, nRelationshipId);
        if (cursordata != null) {
            if (cursordata.moveToFirst()) {
                do {
                    if (setting_def_glucose_unit.equals("mg/dl")) {
                        sGOAL_BloodSugar = cursordata.getString(cursordata.getColumnIndex("goalbloodsugar"));
                    } else {
                        sGOAL_BloodSugar = cursordata.getString(cursordata.getColumnIndex("g_mmolval"));
                    }

                    if (setting_weight_unit.equals("kg")) {
                        sGOAL_Weight = cursordata.getString(cursordata.getColumnIndex("dm_kg"));
                    } else {
                        sGOAL_Weight = cursordata.getString(cursordata.getColumnIndex("dm_lb"));
                    }

                    txtGoalBS.setText(sGOAL_BloodSugar + " " + setting_def_glucose_unit);
                    txtGoalWeight.setText(sGOAL_Weight + " " + setting_weight_unit);

                } while (cursordata.moveToNext());
            }
            cursordata.close();
        }
    }

    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(this)
                .setTitle(p_title)
                .setMessage(p_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public Date date_addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }


 /*   private void initializeBSBarchart() {

        mBarChart = (BarChart) findViewById(R.id.barchart);
        //mBarChart.setOnChartValueSelectedListener(this);
        mBarChart.setDescription("");

//        mChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //  dm_Viewer mv = new dm_Viewer(this, R.layout.custom_marker_view);

        // mBarChart.setMarkerView(mv);


        //tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        Legend l = mBarChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        // l.setTypeface(tf);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xl = mBarChart.getXAxis();
        // xl.setTypeface(tf);

        YAxis leftAxis = mBarChart.getAxisLeft();
        //leftAxis.setTypeface(tf);
        //leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);

        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        xl.setDrawGridLines(false);
        mBarChart.getAxisRight().setEnabled(false);


        db_dm = new SqliteDMHandler(this);
        Cursor cursor_Chart;
        cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId, sChartFilterValue, sAMPMFilter);
        Integer j = cursor_Chart.getCount();

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId, sChartFilterValue, sAMPMFilter);

        xVals = new ArrayList<String>();

        ArrayList<Integer> iIndex = new ArrayList<Integer>();
        if (cursor_Chart != null || !cursor_Chart.equals("")) {


            Integer J = cursor_Chart.getCount();
            cursor_Chart.moveToFirst();
            if (cursor_Chart.getCount() > 0) {


                ArrayList<Date> dates = new ArrayList<Date>();

                iIndex = new ArrayList<Integer>();
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

                Date date1 = null;
                Date date2 = null;

                try {
                    current_date = Calendar.getInstance().getTime();
                    String stodate = dateFormat_query.format(current_date);
                    // dateFormat_query
                    date2 = df1.parse(stodate);

//                  Integer i=  Integer.parseInt(sChartFilterValue);
                    Integer iDays=0;
                    if(sConditionFilter.equals(CON_Type_Monthly)){
                        iDays=Integer.parseInt(sChartFilterValue);
                    }
                    else{
                        iDays=Integer.parseInt(sChartFilterValue)+1;
                    }
                    // Integer iDays = Integer.parseInt(sChartFilterValue) + 1;
                    if (sChartFilterValue.equals("0")) {
                        current_date = Calendar.getInstance().getTime();
                    } else {
                        current_date = date_addDays(current_date, iDays);
                    }
                    String sfromdate = dateFormat_query.format(current_date);
                    date1 = df1.parse(sfromdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date1);


                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(date2);

                while (!cal1.after(cal2)) {
                    dates.add(cal1.getTime());
                    sOnlyStartDate = OnlydateFormat_query.format(cal1.getTime());
                    cal1.add(Calendar.DATE, 1);
                    iIndex.add(Integer.parseInt(sOnlyStartDate));

                    xVals.add(String.valueOf(sOnlyStartDate));
                }
            }


            cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId, sChartFilterValue, sAMPMFilter);
            cursor_Chart.moveToFirst();
            int i = 0;
            while (!cursor_Chart.isAfterLast()) {

                float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bs_avg"))));

                String dateofDrink = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("g_date")));

                Calendar c = Calendar.getInstance();
                Date sdate = new Date();
                SimpleDateFormat sdate_format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    sdate = sdate_format.parse(dateofDrink);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.setTime(sdate);
                int dm_day = c.get(Calendar.DATE);


                if (sChartFilterValue.equals("0")) {

                    yVals1.add(new BarEntry(f1, i));
                } else {
                    int z = iIndex.indexOf((dm_day));

                    yVals1.add(new BarEntry(f1, z));
                }
                // yVals.add(new Entry(f1, (dm_day-1)));
                i++;
                cursor_Chart.moveToNext();
            }


            cursor_Chart.close();

            BarDataSet set1 = new BarDataSet(yVals1, "Blood Sugar");

            set1.setColor(Color.rgb(255, 140, 0));
            set1.setValueTextColor(Color.TRANSPARENT);

            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(set1);


        //    BarData data = new BarData(xVals, dataSets);

      //      data.setGroupSpace(80f);


          //  mBarChart.setData(data);
            mBarChart.invalidate();
            //End Barchart
        }


    }*/

 /*   private void initializeWTBarchart() {

        mBarChart = (BarChart) findViewById(R.id.barchart);
        //mBarChart.setOnChartValueSelectedListener(this);
        mBarChart.setDescription("");

//        mChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        mBarChart.setDrawBarShadow(false);

        mBarChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //   dm_Viewer mv = new dm_Viewer(this, R.layout.custom_marker_view);

        //  mBarChart.setMarkerView(mv);

        //tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        Legend l = mBarChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        // l.setTypeface(tf);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xl = mBarChart.getXAxis();
        // xl.setTypeface(tf);

        YAxis leftAxis = mBarChart.getAxisLeft();
        //leftAxis.setTypeface(tf);
      //  leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);

        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawGridLines(false);

        mBarChart.getAxisRight().setEnabled(false);


        db_dm = new SqliteDMHandler(this);
        Cursor cursor_Chart;
        cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId, sChartFilterValue, sAMPMFilter);
        Integer j = cursor_Chart.getCount();

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId, sChartFilterValue, sAMPMFilter);

        xVals = new ArrayList<String>();

        ArrayList<Integer> iIndex = new ArrayList<Integer>();
        if (cursor_Chart != null || !cursor_Chart.equals("")) {


            Integer J = cursor_Chart.getCount();
            cursor_Chart.moveToFirst();
            if (cursor_Chart.getCount() > 0) {


                ArrayList<Date> dates = new ArrayList<Date>();

                iIndex = new ArrayList<Integer>();
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

                Date date1 = null;
                Date date2 = null;

                try {
                    current_date = Calendar.getInstance().getTime();
                    String stodate = dateFormat_query.format(current_date);
                    // dateFormat_query
                    date2 = df1.parse(stodate);

//                  Integer i=  Integer.parseInt(sChartFilterValue);
                    //Integer iDays = Integer.parseInt(sChartFilterValue) + 1;
                    Integer iDays=0;
                    if(sConditionFilter.equals(CON_Type_Monthly)){
                        iDays=Integer.parseInt(sChartFilterValue);
                    }
                    else{
                        iDays=Integer.parseInt(sChartFilterValue)+1;
                    }
                    if (sChartFilterValue.equals("0")) {
                        current_date = Calendar.getInstance().getTime();
                    } else {
                        current_date = date_addDays(current_date, iDays);
                    }
                    String sfromdate = dateFormat_query.format(current_date);
                    date1 = df1.parse(sfromdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date1);


                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(date2);

                while (!cal1.after(cal2)) {
                    dates.add(cal1.getTime());
                    sOnlyStartDate = OnlydateFormat_query.format(cal1.getTime());
                    cal1.add(Calendar.DATE, 1);
                    iIndex.add(Integer.parseInt(sOnlyStartDate));

                    xVals.add(String.valueOf(sOnlyStartDate));
                }
            }


            cursor_Chart = db_dm.getAnalysisLineChartData(nMemberId, nRelationshipId, sChartFilterValue, sAMPMFilter);
            cursor_Chart.moveToFirst();
            int i = 0;
            while (!cursor_Chart.isAfterLast()) {

                float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("max_wt"))));

                String dateofDrink = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("g_date")));

                Calendar c = Calendar.getInstance();
                Date sdate = new Date();
                SimpleDateFormat sdate_format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    sdate = sdate_format.parse(dateofDrink);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.setTime(sdate);
                int dm_day = c.get(Calendar.DATE);


                if (sChartFilterValue.equals("0")) {

                    yVals1.add(new BarEntry(f1, i));
                } else {
                    int z = iIndex.indexOf((dm_day));

                    yVals1.add(new BarEntry(f1, z));
                }
                // yVals.add(new Entry(f1, (dm_day-1)));
                i++;
                cursor_Chart.moveToNext();
            }


            cursor_Chart.close();

            BarDataSet set1 = new BarDataSet(yVals1, "Weight");

            set1.setColor(Color.rgb(255, 140, 0));
            set1.setValueTextColor(Color.TRANSPARENT);

            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(set1);


           // BarData data = new BarData(xVals, dataSets);

         //   data.setGroupSpace(80f);


         //   mBarChart.setData(data);
            mBarChart.invalidate();
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Minflater = getMenuInflater();
        //Minflater.inflate(R.menu.menu_new_add_medicine, menu);
        getMenuInflater().inflate(R.menu.menu_common, menu);
        this.objMemberMenu = menu;

        LayoutInflater inflater = LayoutInflater.from(this);
        View mCustomView = inflater.inflate(R.layout.circula_image, null);
        objMemberMenu.findItem(R.id.circlularImage).setActionView(mCustomView);

        pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        ImageLoad(pref.getString("UserName", "Me"), pref.getString("imagename", ""));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(DMA_AnalysisDisplayActivity.this, DMA_Settings
                    .class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListViewHeight(ExpandableListView listView) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupView = listAdapter.getGroupView(i, true, null, listView);
            groupView.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupView.getMeasuredHeight();

            if (listView.isGroupExpanded(i)) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    if (listItem != null) {
                        listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                        totalHeight += listItem.getMeasuredHeight();
                    }
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    class MyPagesAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return 4;
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(View collection, int position) {
            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = null;
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.layout.dm_monitor_linechart;
                    view = inflater.inflate(resId, null);
                    mLineChart = (LineChart) view.findViewById(R.id.Linechart);
                    fill_chart(sConditionFilter, sChartfilterType);
                    break;

                case 1:
                    resId = R.layout.dm_monitor_barchart;
                    view = inflater.inflate(resId, null);
                    mBarChart = (BarChart) view.findViewById(R.id.barchart);
                    fill_chart(sConditionFilter, sChartfilterType);


                    break;
                case 2:
                    resId = R.layout.dm_monitor_piechart;
                    view = inflater.inflate(resId, null);
                    mPiechart_goal = (PieChart) view.findViewById(R.id.piechart);
                    fill_chart(sConditionFilter, sChartfilterType);
                    break;

                case 3:
                    resId = R.layout.dm_monitor_postpiechart;
                    view = inflater.inflate(resId, null);
                    mpiechart_normal = (PieChart) view.findViewById(R.id.piepostchart);

                    if(sChartfilterType.equals("wt"))
                    {
                        if(mpiechart_normal!=null) {
                            create_analysis_weight_pie(sConditionFilter);
                         /*   mpiechart_normal.setData(null);
                            mpiechart_normal.setNoDataText("Not available for weight");
                            mpiechart_normal.invalidate();*/
                           // fill_pie_chart_against_normal();
                        }
                    }else
                    {
                        create_statistics_pie_chart(sConditionFilter);
                    }

                //    fill_chart(sConditionFilter, sChartfilterType);
                    break;
            }

            ((ViewPager) collection).addView(view, 0);
            return view;
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object = null;
        }
    }
    //BarChart

    /*   private void initializePieChart()
       {
           try
           {
               // mPiechart = (PieChart) findViewById(R.id.piechart);
               mPiechart.setUsePercentValues(true);
               mPiechart.setDescription("");

               mPiechart.setDragDecelerationFrictionCoef(0.95f);

               //  tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

               //  mPiechart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

               mPiechart.setDrawHoleEnabled(true);
           //    mPiechart.setHoleColorTransparent(true);

               mPiechart.setTransparentCircleColor(Color.WHITE);
               //mPiechart.setTransparentCircleAlpha(110);

               mPiechart.setHoleRadius(58f);
               mPiechart.setTransparentCircleRadius(61f);

               mPiechart.setDrawCenterText(true);

               mPiechart.setRotationAngle(0);
               // enable rotation of the chart by touch
               mPiechart.setRotationEnabled(true);

               // mPiechart.setUnit(" €");
               // mPiechart.setDrawUnitsInChart(true);

               // add a selection listener
               ///mPiechart.setOnChartValueSelectedListener(this);

               //mPiechart.setCenterText("MPAndroidChart\nby Philipp Jahoda");
               // mPiechart.setCenterText("RXMedikart");
               mPiechart.setCenterText("");
               setPieData(4);

               mPiechart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
               // mPiechart.spin(2000, 0, 360);

               Legend l = mPiechart.getLegend();
               l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
               l.setXEntrySpace(7f);
               l.setYEntrySpace(0f);
               l.setYOffset(0f);
           }
           catch(Exception ex)
           {
               String s=String.valueOf(ex.toString());

           }
       }*/
  /*  private void setPieData(int count) {

        Cursor cursorPreNormal_Chart;
        Cursor cursorPreHigh_Chart;
        Cursor cursorPreLow_Chart;

        current_date = Calendar.getInstance().getTime();
        todate = dateFormat_query.format(current_date);
        Integer iDays = Integer.parseInt(sChartFilterValue) + 1;
        current_date = date_addDays(current_date, iDays);
        // current_date = date_addDays(current_date, -7);
        fromdate = dateFormat_query.format(current_date);
        Integer o = 0;
        //  cursorHypotension_Chart = db_bp.getHypotension_PieChart(nMemberId, nRelationshipId, fromdate, todate);

        cursorPreNormal_Chart = db_dm.getPreNormalPieChart(nMemberId, nRelationshipId, sChartFilterValue, "AM/PM");
        o = cursorPreNormal_Chart.getCount();
        cursorPreNormal_Chart.moveToFirst();

        // cursorNormal_Chart = db_bp.getNormal_PieChart(nMemberId, nRelationshipId, fromdate, todate);
        cursorPreHigh_Chart = db_dm.getPreHighPieChart(nMemberId, nRelationshipId, sChartFilterValue, "AM/PM");
        o = cursorPreHigh_Chart.getCount();
        cursorPreHigh_Chart.moveToFirst();

        //cursorPrehyper_Chart = db_bp.getPrehyper_pieChart(nMemberId, nRelationshipId, fromdate, todate);
        cursorPreLow_Chart = db_dm.getPreLowPieChart(nMemberId, nRelationshipId, sChartFilterValue, "AM/PM");
        o = cursorPreLow_Chart.getCount();
        cursorPreLow_Chart.moveToFirst();


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();


        int H, N, L;
        if (cursorPreNormal_Chart != null || !cursorPreNormal_Chart.equals("")) {


            N = Integer.parseInt(cursorPreNormal_Chart.getString(cursorPreNormal_Chart.getColumnIndex("PreNormalSugarLevel")));
            H = Integer.parseInt(cursorPreHigh_Chart.getString(cursorPreHigh_Chart.getColumnIndex("PreHighSugarLevel")));
            L = Integer.parseInt(cursorPreLow_Chart.getString(cursorPreLow_Chart.getColumnIndex("PreLowSugarLevel")));

            if (N > 0) {
                yVals1.add(new Entry((N), 0));
            }
            if (H > 0) {
                yVals1.add(new Entry((H), 1));

            }
            if (L > 0) {
                yVals1.add(new Entry((L), 2));
            }


            String[] mParties = new String[]{
                    "Normal ", "Hyperglycemia ", "Hypoglycemia"
            };
            ArrayList<String> xVals = new ArrayList<String>();

            for (int i = 0; i < count + 1; i++)
                xVals.add(mParties[i % mParties.length]);

            //   PieDataSet dataSet = new PieDataSet(yVals1, "");
            // DataSet dataSet =  new DataSet(yVals1,"Hi");
            // dataSet.setSliceSpace(3f);
            //   dataSet.setSelectionShift(5f);

            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);


            colors.add(ColorTemplate.getHoloBlue());

            //    dataSet.setColors(colors);

            //   PieData data = new PieData(xVals, dataSet);
            //   data.setValueFormatter(new PercentFormatter());
            //  data.setValueTextSize(11f);
            //   data.setValueTextColor(Color.RED);
            //   data.setValueTypeface(tf);
            //   mPiechart.setData(data);

            // undo all highlights
            mPiechart.highlightValues(null);

            mPiechart.invalidate();
        }
    }*/

 /*   private void initializePostPieChart()
    {
        try
        {
            // mPiechart = (PieChart) findViewById(R.id.piechart);
            mPiechart.setUsePercentValues(true);
            mPiechart.setDescription("");

            mPiechart.setDragDecelerationFrictionCoef(0.95f);

            //  tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

            //  mPiechart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

            mPiechart.setDrawHoleEnabled(true);
         //   mPiechart.setHoleColorTransparent(true);

            mPiechart.setTransparentCircleColor(Color.WHITE);
            //mPiechart.setTransparentCircleAlpha(110);

            mPiechart.setHoleRadius(58f);
            mPiechart.setTransparentCircleRadius(61f);

            mPiechart.setDrawCenterText(true);

            mPiechart.setRotationAngle(0);
            // enable rotation of the chart by touch
            mPiechart.setRotationEnabled(true);

            // mPiechart.setUnit(" €");
            // mPiechart.setDrawUnitsInChart(true);

            // add a selection listener
            //mPiechart.setOnChartValueSelectedListener(this);

            //mPiechart.setCenterText("MPAndroidChart\nby Philipp Jahoda");
            mPiechart.setCenterText("");

            setPostPieData(4);

            mPiechart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
            // mPiechart.spin(2000, 0, 360);

            Legend l = mPiechart.getLegend();
            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);
        }
        catch(Exception ex)
        {
            String s=String.valueOf(ex.toString());

        }
    }*/

   /* private void setPostPieData(int count) {

        Cursor cursorPostNormal_Chart;
        Cursor cursorPostHigh_Chart;
        Cursor cursorPostLow_Chart;

        current_date = Calendar.getInstance().getTime();
        todate = dateFormat_query.format(current_date);
        Integer iDays=Integer.parseInt(sChartFilterValue)+1;
        current_date = date_addDays(current_date, iDays);
        // current_date = date_addDays(current_date, -7);
        fromdate = dateFormat_query.format(current_date);
        Integer o=0;
        //  cursorHypotension_Chart = db_bp.getHypotension_PieChart(nMemberId, nRelationshipId, fromdate, todate);

        cursorPostNormal_Chart = db_dm.getPostNormalPieChart(nMemberId, nRelationshipId, sChartFilterValue, "AM/PM");
        o=cursorPostNormal_Chart.getCount();
        cursorPostNormal_Chart.moveToFirst();

        // cursorNormal_Chart = db_bp.getNormal_PieChart(nMemberId, nRelationshipId, fromdate, todate);
        cursorPostHigh_Chart = db_dm.getPostHighPieChart(nMemberId, nRelationshipId, sChartFilterValue, "AM/PM");
        o=cursorPostHigh_Chart.getCount();
        cursorPostHigh_Chart.moveToFirst();

        //cursorPrehyper_Chart = db_bp.getPrehyper_pieChart(nMemberId, nRelationshipId, fromdate, todate);
        cursorPostLow_Chart = db_dm.getPostLowPieChart(nMemberId, nRelationshipId, sChartFilterValue, "AM/PM");
        o=cursorPostLow_Chart.getCount();
        cursorPostLow_Chart.moveToFirst();

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        int H,N,L;
        if(cursorPostNormal_Chart!=null|| !cursorPostNormal_Chart.equals("")) {

            N=  Integer.parseInt(cursorPostNormal_Chart.getString(cursorPostNormal_Chart.getColumnIndex("PostNormalSugarLevel")));
            H = Integer.parseInt(cursorPostHigh_Chart.getString(cursorPostHigh_Chart.getColumnIndex("PostHighSugarLevel")));
            L =Integer.parseInt(cursorPostLow_Chart.getString(cursorPostLow_Chart.getColumnIndex("PostLowSugarLevel")));


            if(N>0){
                yVals1.add(new Entry((N), 0));
            }
            if(H>0){
                yVals1.add(new Entry((H), 1));

            }if(L>0){
                yVals1.add(new Entry((L), 2));}


            String[] mParties = new String[]{
                    "Normal ", "Hyperglycemia ", "Hypoglycemia"
            };
            ArrayList<String> xVals = new ArrayList<String>();

            for (int i = 0; i < count + 1; i++)
                xVals.add(mParties[i % mParties.length]);

         //   PieDataSet dataSet = new PieDataSet(yVals1, "");
            // DataSet dataSet =  new DataSet(yVals1,"Hi");
          //  dataSet.setSliceSpace(3f);
         //   dataSet.setSelectionShift(5f);

            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());

          //  dataSet.setColors(colors);

         //   PieData data = new PieData(xVals, dataSet);
       //     data.setValueFormatter(new PercentFormatter());
        //    data.setValueTextSize(11f);
        //    data.setValueTextColor(Color.RED);
        //    data.setValueTypeface(tf);
        //    mPiechart.setData(data);

            // undo all highlights
            mPiechart.highlightValues(null);
            mPiechart.invalidate();
        }
    }*/

    private void initialize_bottom_bar() {

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home_icon_white, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("New Entry", R.drawable.round_plus_white, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Add Doctor", R.drawable.add_doctor_white, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Set Goal", R.drawable.setgoal_white, R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Send Report", R.drawable.setgoal_white, R.color.colorPrimary);

        // Add items
        bottom_navigation.addItem(item1);
        bottom_navigation.addItem(item2);
        bottom_navigation.addItem(item3);
        bottom_navigation.addItem(item4);
        bottom_navigation.addItem(item5);

        // Set background color
        bottom_navigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));

        // Disable the translation inside the CoordinatorLayout
        // bottomNavigation.setBehaviorTranslationEnabled(false);

        // Change colors
        bottom_navigation.setAccentColor(Color.parseColor("#FFFFFF"));
        bottom_navigation.setInactiveColor(Color.parseColor("#747474"));
        bottom_navigation.setUseElevation(true);

        // Force to tint the drawable (useful for font with icon for example)
        // bottomNavigation.setForceTint(true);

        // Force the titles to be displayed (against Material Design guidelines!)
        bottom_navigation.setForceTitlesDisplay(true);

        // Use colored navigation with circle reveal effect
        // bottomNavigation.setColored(true);

        // Set current item programmatically
        //bottomNavigation.setCurrentItem(1);

        // Customize notification (title, background, typeface)
        bottom_navigation.setNotificationBackgroundColor(getResources().getColor(R.color.colorPrimary));

        bottom_navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {

                if (position == 0) {
                    finish();
                    Intent Intenet_bp = new Intent(DMA_AnalysisDisplayActivity.this, MainActivity.class);
                    startActivity(Intenet_bp);
                } else if (position == 1) {
                    Intent Intenet_bp = new Intent(DMA_AnalysisDisplayActivity.this, DMA_NewEntry.class);
                    startActivity(Intenet_bp);
                } else if (position == 2) {
                    Add_Doctor_Dialog myDiag = Add_Doctor_Dialog.newInstance("add_doctor");
                    myDiag.show(getFragmentManager(), "Diag");
                } else if (position == 3) {
                    dm_SetGoal_Dialog myDiag = dm_SetGoal_Dialog.newInstance(nMemberId, nRelationshipId);
                    myDiag.show(getFragmentManager(), "Diag");
                } else if (position == 4) {
                    create_pdf(sConditionFilter);
                }
            }
        });
    }

    @Override
    public void onSelectDoctor(String Doc_id, String Doc_name, String email) {

//        add_doctor.setText(Doc_name);
//        Doctor_id=Doc_id;
        Add_Doctor_Dialog myDiag = Add_Doctor_Dialog.newInstance("add_doctor");
        myDiag.show(getFragmentManager(), "Diag");

    }

    @Override
    public void onResume() {

        super.onResume();
        if (onResumecalled) {
            finish();
            startActivity(getIntent());
        }
        onResumecalled = true;
    }

    private void get_defaults() {
        String setting_name = "";
        Cursor cursor_session = db.getAllSetting_data(String.valueOf(nRelationshipId), String.valueOf(nMemberId), "2");

        if (cursor_session.moveToFirst()) {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("dm_weight_unit")) {
                    setting_weight_unit = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "kg");
                }
                if (setting_name.equals("dm_def_condition")) {
                    setting_def_condition = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                    ;
                }
                if (setting_name.equals("dm_use_last_entered_values")) {
                    setting_last_enterd_values = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "");
                }
                if (setting_name.equals("dm_def_glucose_unit")) {
                    setting_def_glucose_unit = ConstData.getValueOrDefault(cursor_session.getString(cursor_session.getColumnIndex("value")), "mg/dl");
                }


            } while (cursor_session.moveToNext());
        }

        if (setting_def_glucose_unit != null) {

        } else {
            setting_def_glucose_unit = "mg/dl";

        }


        if (setting_weight_unit != null) {

        } else {
            setting_weight_unit = "kg";

        }

    }

    public void fill_chart(String filter_condition, String show_type) {

        int counter=0;

        crs_graph = db_dm.show_DMMinMaxAnalysisFilterData_Chart(nMemberId, nRelationshipId,
                filter_condition, sAMPMFilter, dateFormat_query.format(Todate));


        switch (filter_condition) {
            case CON_Type_LastReading:
                counter=1;
                break;
            case CON_Type_Weekly:

                counter=7;
                break;
            case CON_Type_Last10:


                break;
            case CON_Type_Monthly:
                counter=30;
                break;
            case CON_Type_Yearly:
                counter=365;
                break;
        }

        if (mLineChart != null) {
            fill_shaded_graph(show_type,counter);
        }

        if (mBarChart != null) {
            fill_bar_chart(show_type);
        }
        if (mPiechart_goal != null) {
            fill_pie_chart_against_goal(show_type);
        }

        if (mpiechart_normal != null) {

                if(sChartfilterType.equals("bs"))
                {
                    create_statistics_pie_chart(sConditionFilter);
                }else{
                    create_analysis_weight_pie(sConditionFilter);
                }


        }


    }

    private void fill_shaded_graph(String show_type,int count) {

        mLineChart.setBackgroundColor(Color.WHITE);
        mLineChart.setDrawGridBackground(false);

        mLineChart.getXAxis().setGranularity(1f);

        mLineChart.setDrawBorders(false);
        mLineChart.getAxisLeft().setSpaceTop(20);

      /*  AxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mLineChart);
        mLineChart.getXAxis().setValueFormatter(xAxisFormatter);*/


        mLineChart.getXAxis().setGranularity(1);

        Legend l = mLineChart.getLegend();
        l.setEnabled(false);
      /*  l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);*/


        mLineChart.setDescription("");
        XAxis xAxis = mLineChart.getXAxis();


        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getAxisLeft().setEnabled(false);
        mLineChart.getAxisRight().setEnabled(false);
        LineDataSet set_glucose = null, set_weight = null;


        ArrayList<Entry> entries_gliucose = new ArrayList<Entry>();
        List<Integer> entries_condition = new ArrayList<Integer>();
        ArrayList<Entry> entries_weight = new ArrayList<Entry>();


        if ((crs_graph != null) && (crs_graph.getCount() > 0)) {
            if (crs_graph.moveToFirst()) {
                do {

                    try {
                        current_date = dateFormat_query.parse(crs_graph.getString(crs_graph.getColumnIndex("g_date")));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String dt = format_single_date.format(current_date);
                    float f = Float.parseFloat(dt);
                    if (setting_def_glucose_unit.equals("mg/dl")) {

                        entries_gliucose.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)), crs_graph.getFloat(crs_graph.getColumnIndex("g_value"))));

                    } else {
                        entries_gliucose.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)), crs_graph.getFloat(crs_graph.getColumnIndex("g_mmolval"))));
                    }

                   // String cat_name=crs_graph.getString(crs_graph.getColumnIndex("g_category"));

                   /* Cursor cursor = db_dm.getCategoryGrpIdByCategoryName(cat_name);
                    if (cursor.moveToFirst()) {
                        do {
                            entries_condition.add(cursor.getInt(cursor.getColumnIndex("categorygroupid")));
                        } while (cursor.moveToNext());
                    }
*/

                    entries_weight.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)), crs_graph.getFloat(crs_graph.getColumnIndex("weight_number"))));

                } while (crs_graph.moveToNext());


                if (entries_gliucose.size() > 0) {

                    int[] colrs_dia = new int[entries_gliucose.size()];

                    for (int a = 0; a < entries_gliucose.size(); a++) {


                        float v_dia = entries_gliucose.get(a).getY();
                        // int condition_id=entries_condition.get(a);

                             /*   if(condition_id==1||condition_id==3||condition_id==5)
                                {
                                    if (v_dia < 69) {
                                        colrs_dia[a] = Color.RED;
                                    } else if (v_dia >= 70 && v_dia < 100) {
                                        colrs_dia[a] = Color.GREEN;

                                    } else if (v_dia >= 100 && v_dia <= 125) {
                                        colrs_dia[a] = Color.parseColor("#f38630");
                                    }  else if (v_dia > 125) {
                                        colrs_dia[a] = Color.RED;
                                    }
                                }else
                                {*/
                        if (v_dia < 69) {
                            colrs_dia[a] = Color.RED;
                        } else if (v_dia >= 70 && v_dia < 139) {
                            colrs_dia[a] = Color.GREEN;

                        } else if (v_dia >= 139 && v_dia <= 199) {
                            colrs_dia[a] = Color.parseColor("#f38630");
                        } else if (v_dia > 199) {
                            colrs_dia[a] = Color.RED;
                        }
                    }

                               /* }




                            }*/






                    set_glucose = new LineDataSet(entries_gliucose, "Glucose");
                    set_weight = new LineDataSet(entries_weight, "Weight");


                    set_glucose.setCircleColors(colrs_dia);


                    if (entries_weight.size() > 0) {
                        int[] colrs_weght = new int[entries_weight.size()];
                        if(sGOAL_Weight!=null) {
                            if (!sGOAL_Weight.equals("")) {



                                for (int a = 0; a < entries_weight.size(); a++) {
                                    float v_weight = entries_weight.get(a).getY();

                                    Float f_g_weight=Float.parseFloat(sGOAL_Weight);


                                    if (v_weight > f_g_weight)
                                    {
                                        Float percent = ((v_weight - f_g_weight) / f_g_weight) * 100;
                                        if(percent>15)
                                        {
                                            colrs_weght[a] = Color.RED;
                                        }else {
                                            colrs_weght[a] = Color.GREEN;
                                        }
                                    }else {
                                        colrs_weght[a] = Color.GREEN;
                                    }

                                }
                                set_weight.setCircleColors(colrs_weght);
                            }
                        }
                    }



                    set_glucose.setDrawFilled(true);
                    set_weight.setFillColor(Color.CYAN);
                    set_weight.setDrawFilled(true);

                    if (show_type.equals(SHOW_TYPE_SUGAR)) {
                        LineData d = new LineData(set_glucose);
                        mLineChart.setData(d);
                    } else if (show_type.equals(SHOW_TYPE_WEIGHT)) {
                        LineData d = new LineData(set_weight);
                        mLineChart.setData(d);
                    }


                }


            } else {
                mLineChart.setData(null);
            }

        }
        mLineChart.notifyDataSetChanged();
            mLineChart.invalidate();
        getBitmapFromView(mLineChart,"L");
      //  mLineChart.saveToGallery("akhil_test_linechart",100);


    }

    private void fill_bar_chart(String show_type) {
        mBarChart.setDrawBarShadow(false);

        mBarChart.setDrawGridBackground(false);
        mBarChart.setDescription("");
        mBarChart.setBackgroundColor(Color.WHITE);
     //   mBarChart.getAxisLeft().setAxisMinValue(40f);
       // mBarChart.setDragEnabled(false);
       // mBarChart.setScaleXEnabled(false);

        mBarChart.getXAxis().setGranularity(1f);
        Legend l = mBarChart.getLegend();
        l.setEnabled(false);
       /* l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);*/

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(10);

        XAxis xAxis = mBarChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(false);
        generateLineData(leftAxis, show_type);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarChart.setScaleEnabled(false);
        mBarChart.getAxisRight().setEnabled(false);
    //    mBarChart.setPinchZoom(false);

        mBarChart.setData(generateBarData(show_type));
        mBarChart.notifyDataSetChanged();
        mBarChart.invalidate();
        getBitmapFromView(mBarChart,"B");
     //   test_getChartBitmap(mBarChart);
        //mBarChart.saveToGallery("akhil_test_barchart",100);
    }

    private void generateLineData(YAxis lAxis, String show_type) {

        lAxis.removeAllLimitLines();
        LimitLine limit_glucose_goal = null, limit_glucose_normal = null,
                limit_line_weight_goal = null;

        Cursor cursor_data;
        cursor_data = db_dm.getgoalDMMonitarData(nMemberId,
                nRelationshipId);

        if ((cursor_data != null) || (cursor_data.getCount() > 0)) {

            if (cursor_data.moveToFirst()) {

                if (show_type.equals(SHOW_TYPE_SUGAR)) {

                    limit_glucose_goal = new LimitLine(cursor_data.getFloat(cursor_data.getColumnIndex(
                            "goalbloodsugar")), "Glucose Goal");
                    limit_glucose_goal.setLineWidth(4f);
                    limit_glucose_goal.enableDashedLine(10f, 10f, 0f);

                    limit_glucose_goal.setLineColor(Color.GREEN);
                    limit_glucose_goal.setTextColor(Color.LTGRAY);


                    limit_glucose_normal = new LimitLine(110, "Glucose Normal");
                    limit_glucose_normal.setLineWidth(4f);
                    limit_glucose_normal.setLineColor(Color.BLUE);
                    limit_glucose_normal.setTextColor(Color.LTGRAY);
                    limit_glucose_normal.enableDashedLine(10f, 10f, 0f);

                    lAxis.addLimitLine(limit_glucose_goal);
                    lAxis.addLimitLine(limit_glucose_normal);

                } else if (show_type.equals(SHOW_TYPE_WEIGHT)) {

                    limit_line_weight_goal = new LimitLine(cursor_data.getFloat(cursor_data.getColumnIndex(
                            "goalweight")), "Weight Goal");
                    limit_line_weight_goal.setLineWidth(4f);
                    limit_line_weight_goal.enableDashedLine(10f, 10f, 0f);

                    limit_line_weight_goal.setLineColor(Color.GREEN);
                    limit_line_weight_goal.setTextColor(Color.LTGRAY);
                    lAxis.addLimitLine(limit_line_weight_goal);


                }
            }
        }


    }

    private BarData generateBarData(String show_type) {

        BarData d = null;
        BarDataSet set_glucose = null, set_wight = null;
        List<Integer> entries_condition = new ArrayList<Integer>();
        ArrayList<BarEntry> entries_glucose = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries_weight = new ArrayList<BarEntry>();


        if ((crs_graph != null) && (crs_graph.getCount() > 0)) {
            if (crs_graph.moveToFirst()) {
                do {

                    try {
                        current_date = dateFormat_query.parse(crs_graph.getString(crs_graph.getColumnIndex("g_date")));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (setting_def_glucose_unit.equals("mg/dl")) {
                        entries_glucose.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)+"."+format_single_date_month.format(current_date)), crs_graph.getFloat(crs_graph.getColumnIndex("g_value"))));
                    } else {
                        entries_glucose.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)+"."+format_single_date_month.format(current_date)), crs_graph.getFloat(crs_graph.getColumnIndex("g_mmolval"))));
                    }

                   /* Cursor cursor = db_dm.getCategoryGrpIdByCategoryName(crs_graph.getString(crs_graph.getColumnIndex("g_category")));
                    if (cursor.moveToFirst()) {
                        do {
                            entries_condition.add(cursor.getInt(cursor.getColumnIndex("categorygroupid")));
                        } while (cursor.moveToNext());
                    }*/

                    entries_weight.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)+"."+format_single_date_month.format(current_date)), crs_graph.getFloat(crs_graph.getColumnIndex("weight_number"))));


                } while (crs_graph.moveToNext());
            }
        }
       // mBarChart.setVisibleXRangeMaximum(entries_glucose.size());
        if (entries_glucose.size() > 0) {


            int[] colrs_dia = new int[entries_glucose.size()];

            for (int a = 0; a < entries_glucose.size(); a++) {
                float v_dia = entries_glucose.get(a).getY();


            //    int condition_id=entries_condition.get(a);

              /*  if(condition_id==1||condition_id==3||condition_id==5)
                {
                    if (v_dia < 69) {
                        colrs_dia[a] = Color.RED;
                    } else if (v_dia >= 70 && v_dia < 100) {
                        colrs_dia[a] = Color.GREEN;

                    } else if (v_dia >= 100 && v_dia <= 125) {
                        colrs_dia[a] = Color.parseColor("#f38630");
                    }  else if (v_dia > 125) {
                        colrs_dia[a] = Color.RED;
                    }
                }else*/
                {
                    if (v_dia < 69) {
                        colrs_dia[a] = Color.RED;
                    } else if (v_dia >= 70 && v_dia < 139) {
                        colrs_dia[a] = Color.GREEN;

                    } else if (v_dia >= 139 && v_dia <= 199) {
                        colrs_dia[a] = Color.parseColor("#f38630");
                    }  else if (v_dia > 199) {
                        colrs_dia[a] = Color.RED;
                    }
                }




            }
            set_glucose = new BarDataSet(entries_glucose, "Glucose");

            set_glucose.setColors(colrs_dia);




            set_glucose.setAxisDependency(YAxis.AxisDependency.LEFT);

        }

        float barWidth = 0.3f;
        float groupSpace = 0.2f;
        float barSpace = 0.05f;
        if (show_type.equals(SHOW_TYPE_SUGAR)) {
            if (set_glucose != null) {
                d = new BarData(set_glucose);
              // set_glucose.set

                d.setBarWidth(barWidth);

            }

        } else if (show_type.equals(SHOW_TYPE_WEIGHT)) {
            if (entries_weight != null) {
                set_wight = new BarDataSet(entries_weight, "Weight");

                if (entries_weight.size() > 0) {
                    int[] colrs_weght = new int[entries_weight.size()];
                    if(sGOAL_Weight!=null) {
                        if (!sGOAL_Weight.equals("")) {



                            for (int a = 0; a < entries_weight.size(); a++) {
                                float v_weight = entries_weight.get(a).getY();

                                Float f_g_weight=Float.parseFloat(sGOAL_Weight);


                                if (v_weight > f_g_weight)
                                {
                                    Float percent = ((v_weight - f_g_weight) / f_g_weight) * 100;
                                    if(percent>15)
                                    {
                                        colrs_weght[a] = Color.RED;
                                    }else {
                                        colrs_weght[a] = Color.GREEN;
                                    }
                                }else {
                                    colrs_weght[a] = Color.GREEN;
                                }

                            }
                            set_wight.setColors(colrs_weght);
                        }
                    }
                }

                d = new BarData(set_wight);

            }
        }


        return d;
    }


    private void fill_pie_chart_against_goal(String show_type) {
        mPiechart_goal.setUsePercentValues(true);
        mPiechart_goal.setDescription("");
        mPiechart_goal.setDragDecelerationFrictionCoef(0.95f);
        mPiechart_goal.setRotationAngle(0);
        //  mpiechart_normal.getLegend().setEnabled(false);
        // enable rotation of the chart by touch
        mPiechart_goal.setEntryLabelColor(Color.BLUE);
        mPiechart_goal.setRotationEnabled(false);
        mPiechart_goal.setHighlightPerTapEnabled(true);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        float glucose_value = 0f, glucose_goal = 0f, pie_value = 0f, weight_goal = 0f, weight_value = 0f;
        String pie_msg = "";


        Cursor cursor_data;
        cursor_data = db_dm.getgoalDMMonitarData(nMemberId,
                nRelationshipId);

        if ((cursor_data != null) || (cursor_data.getCount() > 0)) {

            if (cursor_data.moveToFirst()) {
                glucose_goal = cursor_data.getFloat(cursor_data.getColumnIndex(
                        "goalbloodsugar"));

                weight_goal = cursor_data.getFloat(cursor_data.getColumnIndex(
                        "goalweight"));
            }
        }


        if ((crs_graph != null) && (crs_graph.getCount() > 0)) {
            if (crs_graph.moveToFirst()) {
                do {
                   // if (setting_def_glucose_unit.equals("mg/dl")) {
                        glucose_value = crs_graph.getFloat(crs_graph.getColumnIndex("g_value"));
                    /*  } else {
                        glucose_value = crs_graph.getFloat(crs_graph.getColumnIndex("g_mmolval"));
                    }*/

                    weight_value = crs_graph.getFloat(crs_graph.getColumnIndex("weight_number"));
                } while (crs_graph.moveToNext());
            }
        }


        if (glucose_goal == 0 || weight_goal == 0) {
            mPiechart_goal.setDescription("Please set goal");
        } else {

            if (show_type.equals(SHOW_TYPE_SUGAR)) {
                if (glucose_value > glucose_goal) {
                    pie_value = ((glucose_value - glucose_goal) / glucose_goal) * 100;
                    pie_msg = "Percent more than goal";

                } else if (glucose_goal > glucose_value) {
                    pie_value = ((glucose_goal - glucose_value) / glucose_goal) * 100;
                    pie_msg = "Percent less than goal";

                } else if (glucose_goal == glucose_value) {
                    pie_value = 0f;
                    pie_msg = "Goal Achieved";
                }
            } else if (show_type.equals(SHOW_TYPE_WEIGHT)) {
                if (weight_value > weight_goal) {
                    pie_value = ((weight_value - weight_goal) / weight_goal) * 100;
                    pie_msg = "Percent Less than goal";

                } else if (weight_goal > weight_value) {
                    pie_value = ((weight_goal - weight_value) / weight_goal) * 100;
                    pie_msg = "Percent More than goal";

                } else if (weight_goal == weight_value) {
                    pie_value = 0f;
                    pie_msg = "Goal Achieved";
                }
            }
        }


        entries.add(new PieEntry(pie_value, pie_msg));
        entries.add(new PieEntry((100 - pie_value), ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextColor(Color.BLUE);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());


        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.BLUE);
        mPiechart_goal.setData(data);
        mPiechart_goal.getLegend().setEnabled(false);
        mPiechart_goal.notifyDataSetChanged();
        mPiechart_goal.invalidate();


    }

    private void fill_pie_chart_against_normal() {
        mpiechart_normal.setUsePercentValues(true);
        mpiechart_normal.setDescription("");
        mpiechart_normal.setDragDecelerationFrictionCoef(0.95f);
        mpiechart_normal.setRotationAngle(0);
        // enable rotation of the chart by touch
        mpiechart_normal.setRotationEnabled(false);
        mpiechart_normal.setHighlightPerTapEnabled(true);

        mpiechart_normal.setEntryLabelColor(Color.BLUE);
        //  mpiechart_normal.getLegend().setEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        float glucose_value = 0f, glucose_normal = 70f, pie_value = 0f;
        String pie_msg = "";


        if ((crs_graph != null) && (crs_graph.getCount() > 0)) {
            if (crs_graph.moveToFirst()) {
                do {



                    if (setting_def_glucose_unit.equals("mg/dl")) {
                        glucose_value = crs_graph.getFloat(crs_graph.getColumnIndex("g_value"));
                    } else {
                        glucose_value = crs_graph.getFloat(crs_graph.getColumnIndex("g_mmolval"));
                    }


                } while (crs_graph.moveToNext());
            }
        }


        if (glucose_value > glucose_normal) {
            pie_value = ((glucose_value - glucose_normal) / glucose_value) * 100;
            pie_msg = "Percent more than the normal value";

        } else if (glucose_normal > glucose_value) {
            pie_value = ((glucose_normal - glucose_value) / glucose_value) * 100;
            pie_msg = "Percent less than the normal value";

        } else if (glucose_normal == glucose_value) {
            pie_value = 0f;
            pie_msg = "Normal Range";
        }


        entries.add(new PieEntry(pie_value, pie_msg));
        entries.add(new PieEntry((100 - pie_value), ""));
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());


        dataSet.setColors(colors);
        //  dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.BLUE);
        mpiechart_normal.setData(data);
        mpiechart_normal.getLegend().setEnabled(false);
        mpiechart_normal.notifyDataSetChanged();
        mpiechart_normal.invalidate();


    }

    private void create_pdf(String filter) {
        float[] table_widhts = new float[]{5f, 5f, 5f, 5f};
        float[] width6 = new float[]{5f, 5f, 5f, 5f, 5f, 5f};
        float[] width5 = new float[]{5f, 5f, 5f, 5f, 5f};
        float[] width3 = new float[]{5f, 5f, 5f};
        float[] width2 = new float[]{5f, 5f,5f,5f};
        int report_length = 0;
        Paragraph p3 = null;
        String inserted_date = "";

        Document doc = new Document(PageSize.A4);


        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.WHITE);
        Font bfBold12_white = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);
        Font bfBold13 = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.BLACK);

        Font bfBold13_white = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.WHITE);
        Font bfBold_graph = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.DARK_GRAY);

        Font h1 = new Font(Font.FontFamily.COURIER, 24, Font.NORMAL, BaseColor.BLACK);
        Font h1_blue = new Font(Font.FontFamily.COURIER, 20, Font.NORMAL, new BaseColor(0, 121, 182));
        Font h3 = new Font(Font.FontFamily.COURIER, 15, Font.NORMAL, BaseColor.BLACK);


        Font axis_desc_font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL, new BaseColor(0,121,182));

        Font bfBold14 = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.BLACK);
        Font bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.BLACK);
        Font paraFont = new Font(Font.FontFamily.COURIER, 14, Font.NORMAL, BaseColor.BLACK);

        Font secparaFont = new Font(Font.FontFamily.UNDEFINED, 12, Font.NORMAL, BaseColor.BLUE);
        Font dateparaFont = new Font(Font.FontFamily.UNDEFINED, 12, Font.NORMAL, BaseColor.LIGHT_GRAY);

        Font fontlink = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL, BaseColor.DARK_GRAY);

        Font blankFont = new Font();

        DottedLineSeparator dottedline = new DottedLineSeparator();
        dottedline.setOffset(-2);
        dottedline.setGap(1f);
        dottedline.setLineColor(new BaseColor(223, 99, 1));

        String myName = "", Age = "", weight = "", goal_bs = "", goal_weight = "", gender = "";


        try {

            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.logo_rx);
            bitmap.compress(Bitmap.CompressFormat.PNG, 82, stream2);
            Image myImg = Image.getInstance(stream2.toByteArray());
            myImg.setAlignment(Image.ALIGN_LEFT);
            //myImg.setAbsolutePosition(5f,5f);
            myImg.scaleAbsolute(20f, 50f);



            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap1 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.logo_rx);
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image background = Image.getInstance(stream.toByteArray());

            background.scaleAbsolute(20f, 50f);
            // myImg.setBackgroundColor(BaseColor.WHITE);

            // case CON_Type_Last10:


            if (filter.equals(CON_Type_LastReading)) {
                report_length = -1;
                p3 = new Paragraph("Last reading Report", h3);
            } else if (filter.equals(CON_Type_Weekly)) {
                report_length = -7;
                p3 = new Paragraph("Report from " + format_date.format(date_addDays(Todate, report_length)) + " to " + format_date.format(Todate), h3);
            } else if (filter.equals(CON_Type_Last10)) {
                report_length = -7;
                p3 = new Paragraph("Last 10 reading Report", h3);
            } else if (filter.equals(CON_Type_Monthly)) {
                report_length = -30;
                p3 = new Paragraph("Report from " + format_date.format(date_addDays(Todate, report_length)) + " to " + format_date.format(Todate), h3);

            } else if (filter.equals(CON_Type_Yearly)) {
                report_length = -30;
                p3 = new Paragraph("Report from " + format_date.format(date_addDays(Todate, -365)) + " to " + format_date.format(Todate), h3);


            }


            // profile data get
            db = new SQLiteHandler(getApplicationContext());

            ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
            test.add(db.getUserDetails());


            HashMap<String, String> m = test.get(0);

            myName = m.get("name");
            String email = m.get("email");

            String g = m.get("gender").toString();

            if (g.equals("M")) {
                gender = "Male";
            } else if (g.equals("F")) {
                gender = "Female";
            } else if (g.equals("O")) {
                gender = "Other";
            }
//


            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RxMedikart";

            dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();


            file = new File(dir, "RxMedikart_diabetes_report.pdf");
            FileOutputStream fOut = new FileOutputStream(file);


            writer = PdfWriter.getInstance(doc, fOut);

            writer.setBoxSize("art", rect);
            writer.setPageEvent(new HeaderAndFooter());
            doc.open();


            //  doc.setMargins(50, 45, 50, 60);


            doc.setMarginMirroring(false);

          //  Paragraph p0 = new Paragraph();


            PdfPTable table_head = new PdfPTable(1);
            table_head.setWidthPercentage(100);
            Chunk cp0 = new Chunk();

            cp0 = new Chunk("www.rxmedikart.com", fontlink).setAnchor("http://www.rxmedikart.com");
            insertCellwithBorder(table_head,null, "Glucose Monitor", "row", null, Element.ALIGN_RIGHT, 1, h1_blue, true, 0, 0, 0, 0,null);
            insertCellwithBorder(table_head,cp0, "", "row", null, Element.ALIGN_RIGHT, 1, h1_blue, true, 0, 0, 0, 0,null);
            table_head.setSpacingAfter(5);



            doc.add(table_head);

            doc.add(dottedline);


            PdfPTable table_date_rage = new PdfPTable(1);
            table_date_rage.setHorizontalAlignment(Element.ALIGN_CENTER);
            insertCellwithBorder(table_date_rage,null, date_range_fro_graph, "", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.001f, 0.001f,0.001f,0.001f,null);
            table_date_rage.setSpacingAfter(10);
            table_date_rage.setSpacingBefore(10);

            doc.add(table_date_rage);



            Paragraph p1 = new Paragraph();
            Chunk cp1 = new Chunk("Blood Sugar Log", h3);
            p1.add(cp1);
            cp1 = new Chunk("                                     ", paraFont);
            p1.add(cp1);
         //   cp1 = new Chunk("All Data", paraFont);
        //    p1.add(cp1);
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            doc.add(p1);

            // doc.add( Chunk.NEWLINE );


            //data function
            Cursor goal_cursor = db_dm.getgoalDMMonitarData(nMemberId, nRelationshipId);

            if ((goal_cursor != null) && (goal_cursor.getCount() > 0)) {
                if (goal_cursor.moveToFirst()) {
                    do {

                        if (setting_weight_unit.equals("kg")) {

                            goal_weight = goal_cursor.getString(goal_cursor.getColumnIndex("dm_kg"));
                        } else {

                            goal_weight = goal_cursor.getString(goal_cursor.getColumnIndex("dm_lb"));
                        }

                        if (setting_def_glucose_unit.equals("mg/dl")) {

                            goal_bs = goal_cursor.getString(goal_cursor.getColumnIndex("goalbloodsugar"));
                        } else {

                            goal_bs = goal_cursor.getString(goal_cursor.getColumnIndex("g_mmolval"));
                        }


                    } while (goal_cursor.moveToNext());

                } else {
                    //insertCell(table, "No Entries", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                    goal_bs = "No Entries";
                    goal_weight = "No Entries";

                }
            } else {
                // insertCell(table, "No Entries", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                goal_bs = "No Entries";
                goal_weight = "No Entries";
            }
            //

            PdfPTable table1 = new PdfPTable(4);
            table1.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table1.setSpacingBefore(10f);
            table1.setSpacingAfter(0f);
            table1.setWidthPercentage(100);
            table1.setWidths(width2);


            insertCellwithBorder(table1,null, " ", "r", null, Element.ALIGN_CENTER, 4, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));

            insertCellwithBorder(table1,null, "Name : ", "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.1f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null, myName + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);


            insertCellwithBorder(table1,null, "Gender : " , "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null, gender + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);

            insertCellwithBorder(table1,null, "Email : ", "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null,  email + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);

            //  insertCellwithBorder(table1, "Gender : "+gender+"","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);

            insertCellwithBorder(table1,null, "Goal BS (" + setting_def_glucose_unit + "): " , "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null,  goal_bs + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);

            insertCellwithBorder(table1,null, "Goal Weight (" + setting_weight_unit + "): " , "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null,goal_weight + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);

            insertCellwithBorder(table1,null, " ", "r", null, Element.ALIGN_CENTER, 4, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));

            doc.add(table1);

            //
            doc.add(Chunk.NEWLINE);

            Paragraph p2 = new Paragraph();
            Chunk cp2 = new Chunk("Statistic", h3);
            p2.add(cp2);

            p2.setAlignment(Paragraph.ALIGN_LEFT);
            doc.add(p2);


            //data function
            Cursor static_cursor = db_dm.show_DMMinMaxAnalysisFilterData_Static(nMemberId, nRelationshipId,
                    filter, sAMPMFilter, dateFormat_query.format(Todate));

            String max_weight_kg, min_weight_kg, avg_weight_kg;
            String injection_site, injection_position;
            String max_g_value, min_g_value, avg_g_value;

            if ((static_cursor != null) && (static_cursor.getCount() > 0)) {
                if (static_cursor.moveToFirst()) {
                    do {

                        if (setting_def_glucose_unit.equals("mg/dl")) {

                            max_g_value = static_cursor.getString(static_cursor.getColumnIndex("max_g_value"));
                            min_g_value = static_cursor.getString(static_cursor.getColumnIndex("min_g_value"));
                            avg_g_value = static_cursor.getString(static_cursor.getColumnIndex("avg_g_value"));

                        } else {

                            max_g_value = static_cursor.getString(static_cursor.getColumnIndex("max_g_mmolval"));
                            min_g_value = static_cursor.getString(static_cursor.getColumnIndex("min_g_mmolval"));
                            avg_g_value = static_cursor.getString(static_cursor.getColumnIndex("avg_g_mmolval"));

                        }

                        if (setting_weight_unit.equals("kg")) {

                            max_weight_kg = static_cursor.getString(static_cursor.getColumnIndex("max_weight_kg"));
                            min_weight_kg = static_cursor.getString(static_cursor.getColumnIndex("min_weight_kg"));
                            avg_weight_kg = static_cursor.getString(static_cursor.getColumnIndex("avg_weight_kg"));

                        } else {

                            max_weight_kg = static_cursor.getString(static_cursor.getColumnIndex("avg_weight_lb"));
                            min_weight_kg = static_cursor.getString(static_cursor.getColumnIndex("avg_weight_lb"));
                            avg_weight_kg = static_cursor.getString(static_cursor.getColumnIndex("avg_weight_lb"));

                        }


                    } while (static_cursor.moveToNext());

                } else {
                    //insertCell(table, "No Entries", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                    max_g_value = "No Entries";
                    min_g_value = "No Entries";
                    avg_g_value = "No Entries";

                    max_weight_kg = "No Entries";
                    min_weight_kg = "No Entries";
                    avg_weight_kg = "No Entries";

                }
            } else {
                // insertCell(table, "No Entries", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                max_g_value = "No Entries";
                min_g_value = "No Entries";
                avg_g_value = "No Entries";

                max_weight_kg = "No Entries";
                min_weight_kg = "No Entries";
                avg_weight_kg = "No Entries";
            }



            float[]width_graph=new float[]{3f, 5f};

            PdfPTable table_graph = new PdfPTable(2);
            table_graph.setWidths(width_graph);
            table_graph.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table_graph.setSpacingBefore(10f);
            table_graph.setSpacingAfter(0f);
            table_graph.setWidthPercentage(100);

            insertCellwithBorder(table_graph,null, " ","header", null, Element.ALIGN_CENTER, 2, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);

            insertCellwithBorder(table_graph,null, "","row", GraphImage, Element.ALIGN_LEFT, 1, bfBold13_white,true,0.01f,0.01f,0.0f,0.01f,null);


            Paragraph p_desc = new Paragraph();



                p_desc.add(new Paragraph(String.format( "%.2f", normal_value_percent)+"%  Normal.",bfBold_graph));

                p_desc.add(new Paragraph(String.format( "%.2f", high_value_percent)+"% Hyperglcemia.",bfBold_graph));

                p_desc.add(new Paragraph(String.format( "%.2f", low_value_percent)+"% Hypoglcemia.",bfBold_graph));





            PdfPCell cell = new PdfPCell();
            cell.addElement(p_desc);
            cell.setColspan(1);
            cell.setBorderWidthLeft(0);
            cell.setPaddingTop(30);
            cell.setBorderColor(new BaseColor(0,121,182));

            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table_graph.addCell(cell);


            doc.add(table_graph);





            PdfPTable table2 = new PdfPTable(4);
            table2.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table2.setSpacingBefore(10f);
            table2.setSpacingAfter(0f);
            table2.setWidthPercentage(100);
            table2.setWidths(table_widhts);

            insertCellwithBorder(table2,null, "", "header", null, Element.ALIGN_CENTER, 1, bfBold13_white, true, 0.01f, 0.01f, 0.01f, 0.01f,null);
            insertCellwithBorder(table2,null, "Maximum", "header", null, Element.ALIGN_CENTER, 1, bfBold13_white, true, 0.01f, 0.01f, 0.01f, 0.01f,null);
            // insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, "Minimum", "header", null, Element.ALIGN_CENTER, 1, bfBold13_white, true, 0.01f, 0.01f, 0.01f, 0.01f,null);
            // insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, "Average", "header", null, Element.ALIGN_CENTER, 1, bfBold13_white, true, 0.01f, 0.01f, 0.01f, 0.01f,null);

            insertCellwithBorder(table2,null, "BS (" + setting_def_glucose_unit + ")", "row", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0, 0, 0,null);
            insertCellwithBorder(table2,null, max_g_value, "row", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0, 0, 0, 0,null);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, min_g_value, "row", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0, 0, 0, 0,null);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, avg_g_value, "row", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0, 0, 0.01f, 0,null);

            insertCellwithBorder(table2,null, "Weight (" + setting_weight_unit + ")", "row", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0, 0, 0.01f,null);
            insertCellwithBorder(table2,null, max_weight_kg, "row", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0, 0, 0, 0.01f,null);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, min_weight_kg, "row", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0, 0, 0, 0.01f,null);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, avg_weight_kg, "row", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0, 0, 0.01f, 0.01f,null);


            doc.add(table2);


            Paragraph p_graph = new Paragraph();
            Chunk c_p_graph = new Chunk("Graphs", h3);
            p_graph.add(c_p_graph);
            p_graph.setSpacingBefore(10);
            p_graph.setAlignment(Paragraph.ALIGN_LEFT);
            doc.add(p_graph);


            Paragraph p_graph_add = new Paragraph();


            Graph_line.setBorder(Rectangle.BOX);
            Graph_line.setBorderColor(BaseColor.BLACK);
            Graph_line.setBorderWidth(3);


            Graph_bar.setBorder(Rectangle.BOX);
            Graph_bar.setBorderColor(BaseColor.BLACK);
            Graph_bar.setBorderWidth(3);

            Chunk c_p_graph_space_start = new Chunk("                      ");
            Chunk c_p_graph_one = new Chunk(Graph_line, 0, 0, true);
            Chunk c_p_graph_space = new Chunk("                       ");
            Chunk c_p_graph_two = new Chunk(Graph_bar, 0, 0, true);
            p_graph_add.add(c_p_graph_space_start);
            p_graph_add.add(c_p_graph_one);
            p_graph_add.add(c_p_graph_space);
            p_graph_add.add(c_p_graph_two);
            doc.add(p_graph_add);

          ///  Paragraph p_graph_axis_desc = new Paragraph();
           // p_graph_axis_desc.setAlignment(Element.ALIGN_CENTER);
           // PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
         //   Chunk c_desc = new Chunk(txt_axis_indicator.getText().toString(), axis_desc_font);
           // p_graph_axis_desc.setSpacingAfter(10);

           // p_graph_axis_desc.add(c_desc);
//
            PdfPTable table_desc = new PdfPTable(1);
            table_desc.setSpacingBefore(3);
            table_desc.setWidthPercentage(100);
            PdfPCell cell_desc =  new PdfPCell(new Phrase(txt_axis_indicator.getText().toString(), axis_desc_font));
            cell_desc.setPadding(3);
            cell_desc.setBackgroundColor(BaseColor.LIGHT_GRAY);
           // Style.headerCellStyle(cell_desc);
            cell_desc.setHorizontalAlignment(Element.ALIGN_CENTER);
          // cell_desc.setVerticalAlignment(Element.ALIGN_MIDDLE);
           // cell_desc.setHorizontalAlignment(Element.ALIGN_MIDDLE);
           // cell_desc.addElement(p_graph_axis_desc);
            table_desc.addCell(cell_desc);

            doc.add(table_desc);


         /*   PdfPTable table_multiple_graphs = new PdfPTable(2);
            table_multiple_graphs.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table_multiple_graphs.setSpacingBefore(10f);
            table_multiple_graphs.setSpacingAfter(0f);
            table_multiple_graphs.setWidthPercentage(100);
            table_multiple_graphs.setWidths(table_widhts);*/



           /* Paragraph p2 = new Paragraph(" Report Of Diabetes ",secparaFont);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setSpacingBefore(0);
            doc.add(p2);

            doc.add( Chunk.NEWLINE );*/
           /* Chunk chunk = new Chunk("www.rxmedikart.com", fontlink)
                    .setAnchor("http://www.rxmedikart.com");
            Paragraph plink = new Paragraph(chunk);

            plink.setAlignment(Paragraph.ALIGN_RIGHT);
            plink.setSpacingAfter(5);
            doc.add(plink);*/


            Cursor pdf_cursor = db_dm.show_DMMinMaxAnalysisFilterData_pdf(nMemberId, nRelationshipId,
                    filter, sAMPMFilter, dateFormat_query.format(Todate));


            //p2.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph p_detail_heading=new Paragraph("Reading Report(Blood Sugar level)");
            doc.add(p_detail_heading);


            PdfPTable table = new PdfPTable(5);
            table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(0f);
            table.setWidthPercentage(100);
            table.setWidths(width5);

            insertCell(table, "Blood Sugar levels", "header", null, Element.ALIGN_CENTER, 5, bfBold12_white, false);
            insertCell(table, "Date", "header", null, Element.ALIGN_CENTER, 1, bfBold13_white, false);
            insertCell(table, "Category", "header", null, Element.ALIGN_CENTER, 1, bfBold13_white, false);
            insertCell(table, "Time", "header", null, Element.ALIGN_CENTER, 1, bfBold13_white, false);
            insertCell(table, "Blood Sugar(" + setting_def_glucose_unit + ")", "header", null, Element.ALIGN_CENTER, 1, bfBold13_white, false);
            insertCell(table, "Injection Site", "header", null, Element.ALIGN_CENTER, 1, bfBold13_white, false);

            int condition_id=0;
            if ((pdf_cursor != null) && (pdf_cursor.getCount() > 0)) {
                if (pdf_cursor.moveToFirst()) {
                    do {

                        Integer i_glucose = Integer.parseInt(pdf_cursor.getString(pdf_cursor.getColumnIndex("g_value")));

                       /* Cursor cursor = db_dm.getCategoryGrpIdByCategoryName(pdf_cursor.getString(pdf_cursor.getColumnIndex("g_category")));
                        if (cursor.moveToFirst()) {
                            do {
                                condition_id=cursor.getInt(cursor.getColumnIndex("categorygroupid"));
                            } while (cursor.moveToNext());
                        }*/


                          /*  if(condition_id==1||condition_id==3||condition_id==5)
                            {
                                if (i_glucose < 69) {
                                    bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
                                } else if (i_glucose >= 70 && i_glucose < 100) {
                                    bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.GREEN);

                                } else if (i_glucose >= 100 && i_glucose <= 125) {
                                    bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.ORANGE);
                                }  else if (i_glucose > 125) {
                                    bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
                                }
                            }else*/
                            {
                                if (i_glucose < 69) {
                                    bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
                                } else if (i_glucose >= 70 && i_glucose < 139) {
                                    bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.GREEN);

                                } else if (i_glucose >= 139 && i_glucose <= 199) {
                                    bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.ORANGE);
                                }  else if (i_glucose > 199) {
                                    bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
                                }
                            }



                        if (!inserted_date.equals(pdf_cursor.getString(pdf_cursor.getColumnIndex("g_date")))) {
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("g_date")), "row", null, Element.ALIGN_CENTER, 1, bfBold13, false);
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("g_category")), "row", null, Element.ALIGN_CENTER, 1, bfBold13, false);
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("g_time")), "row", null, Element.ALIGN_CENTER, 1, bfBold13, false);

                            if (setting_def_glucose_unit.equals("mg/dl")) {
                                insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("g_value")), "row", null, Element.ALIGN_CENTER, 1, bfBoldRangeClor, false);
                            } else {
                                insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("g_mmolval")), "row", null, Element.ALIGN_CENTER, 1, bfBoldRangeClor, false);
                            }

                            injection_site = pdf_cursor.getString(pdf_cursor.getColumnIndex("injection_site"));
                            injection_position = pdf_cursor.getString(pdf_cursor.getColumnIndex("injection_position"));

                            insertCell(table, injection_site + " " + injection_position, "row", null, Element.ALIGN_CENTER, 1, bfBoldRangeClor, false);


                            inserted_date = pdf_cursor.getString(pdf_cursor.getColumnIndex("g_date"));
                        } else {
                            insertCell(table, "", "row", null, Element.ALIGN_CENTER, 1, bfBold13, false);
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("g_category")), "row", null, Element.ALIGN_CENTER, 1, bfBold13, false);
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("g_time")), "row", null, Element.ALIGN_CENTER, 1, bfBold13, false);

                            if (setting_def_glucose_unit.equals("mg/dl")) {
                                insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("g_value")), "row", null, Element.ALIGN_CENTER, 1, bfBoldRangeClor, false);
                            } else {
                                insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("g_mmolval")), "row", null, Element.ALIGN_CENTER, 1, bfBoldRangeClor, false);
                            }
                            injection_site = pdf_cursor.getString(pdf_cursor.getColumnIndex("injection_site"));
                            injection_position = pdf_cursor.getString(pdf_cursor.getColumnIndex("injection_position"));

                            insertCell(table, injection_site + " " + injection_position, "row", null, Element.ALIGN_CENTER, 1, bfBoldRangeClor, false);


                            inserted_date = pdf_cursor.getString(pdf_cursor.getColumnIndex("g_date"));
                        }


                    } while (pdf_cursor.moveToNext());

                } else {
                    insertCell(table, "No Entries", "row", null, Element.ALIGN_CENTER, 1, bfBold13, false);
                }
            } else {
                insertCell(table, "No Entries", "row", null, Element.ALIGN_CENTER, 1, bfBold13, false);
            }

            doc.add(table);

        } catch (DocumentException de) {
            de.toString();

        } catch (IOException e) {

        } finally {

            doc.close();
            send_report_to_doctor(file.getAbsolutePath());
        }
    }


    private void insertCell(PdfPTable table, String text, String type, Image img, int align, int colspan, Font font, Boolean Border) {


        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(new BaseColor(0, 121, 182));
        cell.setPadding(2);
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        if (Border) {
            cell.setBorderWidth(0);
        }

        //row style
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

    private void insertCellwithBorder(PdfPTable table,Chunk c, String text, String type, Image img, int align, int colspan,
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
        public HeaderAndFooter() {
            super();

            //  footerFont.setStyle(Font.ITALIC);
        }


        @Override
        public void onEndPage(PdfWriter writer, Document document) {
           /* PdfContentByte canvas = writer.getDirectContent();
            canvas.rectangle(50, 30, 500, 780);
            canvas.setColorStroke(BaseColor.LIGHT_GRAY);
            canvas.stroke();*/

            try {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap1 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.logo_rx);
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image background = Image.getInstance(stream.toByteArray());

                background.scaleAbsolute(20f, 50f);


                ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                Bitmap bitmap2 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.dm_injection_site_report);
                bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                Image dm_injection_site_report = Image.getInstance(stream1.toByteArray());
                dm_injection_site_report.setAlignment(Element.ALIGN_CENTER);
                dm_injection_site_report.scaleAbsolute(3f, 3f);

                DottedLineSeparator dottedline = new DottedLineSeparator();
                dottedline.setOffset(-2);
                dottedline.setGap(1f);
                dottedline.setLineColor(new BaseColor(223, 99, 1));

                Chunk c = new Chunk(dm_injection_site_report, 0, -45);

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

               // ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("Page " + writer.getPageNumber()), rect.getRight(), rect.getBottom() - 5, 0);


                if(writer.getPageNumber()==1) {


                    writer.getDirectContent().addImage(background, 180, 0, 0, 30, 30, 765);
                }
                writer.getDirectContentUnder().addImage(dm_injection_site_report, 130, 0, 0, 50, 30, 23);

            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }


        }

    }

    private void send_report_to_doctor(String Path) {
        Intent intent_report = new Intent(this, MRA_SendReprtToDoctor.class);
        intent_report.putExtra("Email_heading", "Diabetes Monitor report");
        intent_report.putExtra("path", Path);
        startActivity(intent_report);

    }

   public void create_statistics_pie_chart(String Filter_condition) {


        float total_count=0;
        float Hypoglycemia_low_glucose_count=0;
        float normal_glucose_count=0;
        float Hyperglycemia_high_glucose_count=0;


        mPiechart_statistics.setUsePercentValues(true);
        mPiechart_statistics.setDescription("");
        mPiechart_statistics.setHovered(false);
        mPiechart_statistics.setDragDecelerationFrictionCoef(0.95f);
        mPiechart_statistics.setDrawHoleEnabled(true);
        mPiechart_statistics.setTransparentCircleColor(Color.WHITE);
        mPiechart_statistics.setHoleRadius(58f);
        mPiechart_statistics.setTransparentCircleRadius(61f);
        mPiechart_statistics.setDrawCenterText(true);
        mPiechart_statistics.setRotationAngle(0);
        mPiechart_statistics.setRotationEnabled(false);
        mPiechart_statistics.setCenterText("");
        mPiechart_statistics.setEntryLabelColor(Color.BLUE);
       mPiechart_statistics.setDrawEntryLabels(false);

       if(mpiechart_normal!=null) {
           mpiechart_normal.setUsePercentValues(true);
           mpiechart_normal.setDescription("");
           mpiechart_normal.setHovered(false);
           mpiechart_normal.setDragDecelerationFrictionCoef(0.95f);
           mpiechart_normal.setDrawHoleEnabled(true);
           mpiechart_normal.setTransparentCircleColor(Color.WHITE);
           mpiechart_normal.setHoleRadius(58f);
           mpiechart_normal.setTransparentCircleRadius(61f);
           mpiechart_normal.setDrawCenterText(true);
           mpiechart_normal.setRotationAngle(0);
           mpiechart_normal.setRotationEnabled(false);
           mpiechart_normal.setCenterText("");
           mpiechart_normal.setEntryLabelColor(Color.BLUE);
           mpiechart_normal.setDrawEntryLabels(false);

       }
        // mPiechart.spin(2000, 0, 360);

        String[] mParties ;
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        Cursor cursor_pdf_graph_values = db_dm.show_analysisi_for_pdf_graph(nMemberId, nRelationshipId, Filter_condition, sAMPMFilter, dateFormat_query.format(Todate));


        if (cursor_pdf_graph_values != null) {
            if (cursor_pdf_graph_values.moveToFirst()) {
                do {
                    total_count=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("total_count"));
                    Hypoglycemia_low_glucose_count=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("Hypoglycemia_low_glucose"));
                    normal_glucose_count=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("normal_glucose"));
                    Hyperglycemia_high_glucose_count=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("Hyperglycemia_high_glucose"));

                } while (cursor_pdf_graph_values.moveToNext());

            }
        }

         normal_value_percent=(normal_glucose_count/total_count)*100;

         high_value_percent=(Hyperglycemia_high_glucose_count/total_count)*100;

         low_value_percent=(Hypoglycemia_low_glucose_count/total_count)*100;

        if(normal_value_percent>0) {
            yVals1.add(new PieEntry((normal_value_percent),"Normal"));
        }

        if(high_value_percent>0) {
            yVals1.add(new PieEntry((high_value_percent),"Hyperglycemia"));
        }
        if(low_value_percent>0) {
            yVals1.add(new PieEntry((low_value_percent),"Hypoglycemia"));
        }



        PieDataSet dataSet = new PieDataSet(yVals1, "");


        ArrayList<Integer> colors = new ArrayList<Integer>();



        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        data.setValueTextSize(11f);
        data.setValueTextColor(Color.RED);
        mPiechart_statistics.setData(data);
        mPiechart_statistics.highlightValues(null);
        mPiechart_statistics.animateY(500, Easing.EasingOption.EaseInOutQuad);
       mPiechart_statistics.notifyDataSetChanged();
        mPiechart_statistics.invalidate();
     //  test_getChartBitmap(mPiechart_statistics);
       if(mpiechart_normal!=null) {
           mpiechart_normal.setData(data);
           mpiechart_normal.highlightValues(null);
           mpiechart_normal.animateY(500, Easing.EasingOption.EaseInOutQuad);
           mpiechart_normal.notifyDataSetChanged();
           mpiechart_normal.invalidate();
       //    test_getChartBitmap(mpiechart_normal);
       }

      // mPiechart_statistics.saveToGallery("akhil_test_mPiechart_statistics",100);
     //  mpiechart_normal.saveToGallery("akhil_test_mpiechart_normal",100);


    }

    void getBitmapFromView(View v,String Type) {
        try {


                Bitmap b = getChartBitmap(v,Type);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 100, stream);

            if(Type.equals("P")) {
                GraphImage = Image.getInstance(stream.toByteArray());
                GraphImage.scaleAbsolute(150, 150);
                GraphImage.setAlignment(Element.ALIGN_RIGHT);
            }else if(Type.equals("L")) {

                Graph_line = Image.getInstance(stream.toByteArray());
                Graph_line.scaleAbsolute(150, 150);
                Graph_line.setAlignment(Element.ALIGN_RIGHT);
            }else if(Type.equals("B")) {

                Graph_bar = Image.getInstance(stream.toByteArray());
                Graph_bar.scaleAbsolute(150, 150);
                Graph_bar.setAlignment(Element.ALIGN_LEFT);
            }
               /* ((ImageView)findViewById(R.id.test)).setImageBitmap(b);*/


        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    Bitmap getChartBitmap(View v, String type) {
        // Define a bitmap with the same size as the view

        Bitmap returnedBitmap=null;
        if(v.getVisibility()==View.VISIBLE)
        {

            if(type.equals("P"))
            {
                returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.RGB_565);
            }else
            {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                returnedBitmap = Bitmap.createBitmap(size.x, size.y-(size.y/2), Bitmap.Config.RGB_565);
            }
         //

            // Bind a canvas to it
            Canvas canvas = new Canvas(returnedBitmap);

            canvas.drawColor(Color.WHITE);
            // draw the view on the canvas
            v.draw(canvas);
        }

        // return the bitmap
        return returnedBitmap;
    }

    public void create_analysis_weight_pie(String Filter_condition) {


        float total_count=0;
        float over_weight=0;
        float normal_weight=0;
        float normal_weight_percent=0,over_weight_percent=0;


        mPiechart_statistics.setUsePercentValues(true);
        mPiechart_statistics.setDescription("");
        mPiechart_statistics.setHovered(false);
        mPiechart_statistics.setDragDecelerationFrictionCoef(0.95f);
        mPiechart_statistics.setDrawHoleEnabled(true);
        mPiechart_statistics.setTransparentCircleColor(Color.WHITE);
        mPiechart_statistics.setHoleRadius(58f);
        mPiechart_statistics.setTransparentCircleRadius(61f);
        mPiechart_statistics.setDrawCenterText(true);
        mPiechart_statistics.setRotationAngle(0);
        mPiechart_statistics.setRotationEnabled(false);
        mPiechart_statistics.setCenterText("");
        mPiechart_statistics.setEntryLabelColor(Color.BLUE);
        mPiechart_statistics.setNoDataText("Please set goal");
        mPiechart_statistics.setDrawEntryLabels(false);

        if(mpiechart_normal!=null) {
            mpiechart_normal.setNoDataText("Please set goal");
            mpiechart_normal.setUsePercentValues(true);
            mpiechart_normal.setDescription("");
            mpiechart_normal.setHovered(false);
            mpiechart_normal.setDragDecelerationFrictionCoef(0.95f);
            mpiechart_normal.setDrawHoleEnabled(true);
            mpiechart_normal.setTransparentCircleColor(Color.WHITE);
            mpiechart_normal.setHoleRadius(58f);
            mpiechart_normal.setTransparentCircleRadius(61f);
            mpiechart_normal.setDrawCenterText(true);
            mpiechart_normal.setRotationAngle(0);
            mpiechart_normal.setRotationEnabled(false);
            mpiechart_normal.setCenterText("");
            mpiechart_normal.setEntryLabelColor(Color.BLUE);
            mpiechart_normal.setDrawEntryLabels(false);
        }
        // mPiechart.spin(2000, 0, 360);

        String[] mParties ;
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        Cursor cursor_pdf_graph_values = db_dm.show_weight_analysis_data(nMemberId, nRelationshipId, Filter_condition, sAMPMFilter, dateFormat_query.format(Todate));


        if (cursor_pdf_graph_values != null) {
            if (cursor_pdf_graph_values.moveToFirst()) {
                do {
                    over_weight=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("over_weight"));
                    normal_weight=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("normal_weight"));

                } while (cursor_pdf_graph_values.moveToNext());

            }
        }
        total_count=over_weight+normal_weight;


        normal_weight_percent=(normal_weight/total_count)*100;

        over_weight_percent=(over_weight/total_count)*100;



            yVals1.add(new PieEntry((normal_weight_percent),"Fair weight"));

            yVals1.add(new PieEntry((over_weight_percent),"Over Weight"));



        PieDataSet dataSet = new PieDataSet(yVals1, "");


        ArrayList<Integer> colors = new ArrayList<Integer>();



        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        data.setValueTextSize(11f);
        data.setValueTextColor(Color.RED);
        mPiechart_statistics.setData(data);
        mPiechart_statistics.highlightValues(null);
        mPiechart_statistics.animateY(500, Easing.EasingOption.EaseInOutQuad);
        mPiechart_statistics.notifyDataSetChanged();
        mPiechart_statistics.invalidate();

        if(mpiechart_normal!=null) {
            mpiechart_normal.setData(data);
            mpiechart_normal.highlightValues(null);
            mpiechart_normal.animateY(500, Easing.EasingOption.EaseInOutQuad);
            mpiechart_normal.notifyDataSetChanged();
            mpiechart_normal.invalidate();
        }




    }
}




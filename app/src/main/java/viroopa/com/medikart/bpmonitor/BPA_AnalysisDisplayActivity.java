package viroopa.com.medikart.bpmonitor;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.cepheuen.progresspageindicator.ProgressPageIndicator;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.MainActivity;

import viroopa.com.medikart.MedicineReminder.MRA_SendReprtToDoctor;
import viroopa.com.medikart.R;
import viroopa.com.medikart.adapter.AD_ComboList;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.bpmonitor.Module.BPM_Analysis_Heading;
import viroopa.com.medikart.bpmonitor.Module.BPM_Analysis_Item_Detail;
import viroopa.com.medikart.bpmonitor.adapter.BPAD_AnalysisExpandable;
import viroopa.com.medikart.common.Add_Doctor_Dialog;
import viroopa.com.medikart.common.Change_member_Dialog;
import viroopa.com.medikart.common.DayAxisTestValueFormatter;
import viroopa.com.medikart.common.DayAxisValueFormatter;
import viroopa.com.medikart.common.DayWithMonthAxisValueFormatter;
import viroopa.com.medikart.common.Style;
import viroopa.com.medikart.common.numerdialog;
import viroopa.com.medikart.dmMonitor.DMA_AnalysisDisplayActivity;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteBPHandler;
import viroopa.com.medikart.model.M_doctorlist;
import viroopa.com.medikart.model.M_memberlist;
import viroopa.com.medikart.model.M_objectItem;


public class BPA_AnalysisDisplayActivity extends AppCompatActivity implements numerdialog.OnNumberDialogDoneListener,
        Change_member_Dialog.OnMemberSelectListener,
        Add_Doctor_Dialog.OnDoctorSelectListener{

    @Nullable
    @BindView(R.id.txtheader)
    TextView _txtheader;


    @Nullable
    @BindView(R.id.axis_desc)
    LinearLayout axis_desc;

    @BindView(R.id.lnr_bp_value_indicator)
    LinearLayout lnr_bp_value_indicator;

    @BindView(R.id.lnr_weght_value_indicator)
    LinearLayout lnr_weght_value_indicator;



    @Nullable
    @BindView(R.id.txt_axis_indicator)
    TextView txt_axis_indicator;
    @Nullable
    @BindView(R.id.txtSys)
    TextView _txtSys;

    @Nullable
    @BindView(R.id.txtdias)
    TextView _txtdias;

    @Nullable
    @BindView(R.id.txtFromdate)
    TextView _txtFromdate;

    @Nullable
    @BindView(R.id.txtgoalSystolic)
    TextView _txtgoalSystolic;

    @Nullable
    @BindView(R.id.txtgoalDiastolic)
    TextView _txtgoalDiastolic;

    @Nullable
    @BindView(R.id.txtgoalweight)
    TextView _txtgoalweight;

    @Nullable
    @BindView(R.id.txtgoalWeightunit)
    TextView _txtgoalweightunit;

    @Nullable
    @BindView(R.id.btnLastRead)
    Button _btnLastRead;

    @Nullable
    @BindView(R.id.btnWeekly)
    Button _btnWeekly;

    @Nullable
    @BindView(R.id.btnLast10)
    Button _btnLast10;


    @Nullable
    @BindView(R.id.btnMonthly)
    Button _btnMonthly;

    @Nullable
    @BindView(R.id.btnYearly)
    Button _btnYearly;

    @Nullable
    @BindView(R.id.lnrweekly)
    LinearLayout _lnrweekly;

    @Nullable
    @BindView(R.id.expandableListDMView1)
    ExpandableListView _expandableListDMView1;


    @BindView(R.id.piechart_pdf)
    PieChart mPiechart_statistics;

    @Nullable
    @BindView(R.id.txtGraph)
    TextView _txtGraph;

    @Nullable
    @BindView(R.id.img_previous)
    ImageView _img_previous;

    @Nullable
    @BindView(R.id.img_next)
    ImageView _img_next;

    @Nullable
    @BindView(R.id.rdbbs)
    TextView _rdbbs;

    @Nullable
    @BindView(R.id.spnrAMPM)
    Spinner _spnrAMPM;

    @Nullable
    @BindView(R.id.rdbwt)
    TextView _rdbwt;

    @Nullable
    @BindView(R.id.viewPager)
    ViewPager _viewPager;

    @Nullable
    @BindView(R.id.pageIndicator)
    ProgressPageIndicator _pageIndicator;

    @Nullable
    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    Rectangle rect = new Rectangle(30, 30, 550, 800);
    private SimpleDateFormat format_date_for_pdf = new SimpleDateFormat("LLLL dd,yyyy hh:mm" );
    private  Image GraphImage,Graph_line,Graph_bar;
    private  String sGoalWeight;


    float low_bp_percent=0,normal_value_percent=0,Prehypertension_percent=0,Hypertension_stage1_percent=0,Hypertension_stage2_percent=0,
            hypertensive_crisis_percent=0 ;

    @Override
    public void onSelectDoctor(String Doc_id, String Doc_name,String mail_id) {
        Add_Doctor_Dialog myDiag = Add_Doctor_Dialog.newInstance("add_doctor");
        myDiag.show(getFragmentManager(), "Diag");
    }

    @OnClick(R.id.btnLastRead)
    void btnLastRead_click() {

        _img_previous.setVisibility(View.GONE);
        _img_next.setVisibility(View.GONE);

        change_btn_color(_btnLastRead);
        _txtGraph.setText("Graphical Representation");
        sAMPMFilter = _spnrAMPM.getSelectedItem().toString();

        sChartFilterValue = "0";

        Todate= Calendar.getInstance().getTime();
        current_date = Calendar.getInstance().getTime();
        fromdate = changedateFormat.format(current_date);

        _txtFromdate.setText("");
        _txtFromdate.setAnimation(anim);
        _txtFromdate.setText(fromdate);
        sOnlyStartDate = OnlydateFormat_query.format(current_date);
        sOnlyEndDate = OnlydateFormat_query.format(current_date);


        // txt_Todate.setVisibility ( View.GONE );
        _txtFromdate.setGravity(Gravity.CENTER);
        sConditionFilter = "LastReading";

        if(_viewPager.getCurrentItem()==0  || _viewPager.getCurrentItem()==1)
        {
            lnr_bp_value_indicator.setVisibility(View.VISIBLE);
        }


        lnr_weght_value_indicator.setVisibility(View.GONE);

        fillReading("LastReading");
        create_statistics_pie_chart("LastReading");

        initializePieChart();
        fill_chart(sConditionFilter,sChartfilterType);


    }


    @OnClick(R.id.img_previous)

    void img_previous_click()
    {

        if( sConditionFilter.equals(CON_Type_Weekly))
        {
            Todate = date_addDays(Todate, -7);
            fillReading(sConditionFilter);
            create_statistics_pie_chart(sConditionFilter);

            fill_chart(sConditionFilter,sChartfilterType);


            _txtFromdate.setText("");
            _txtFromdate.setAnimation(anim);
            _txtFromdate.setText( changedateFormat.format( date_addDays(Todate, -7)) + " - " +  changedateFormat.format(Todate));
        }

        if( sConditionFilter.equals(CON_Type_Monthly))
        {
            Todate = date_addDays(Todate, -30);
            fillReading(sConditionFilter);
            create_statistics_pie_chart(sConditionFilter);

            fill_chart(sConditionFilter,sChartfilterType);
            initializePieChart();
            _txtFromdate.setText("");
            _txtFromdate.setAnimation(anim);
            _txtFromdate.setText( changedateFormat.format( date_addDays(Todate, -30)) + " - " +  changedateFormat.format(Todate));

        }

        if( sConditionFilter.equals(CON_Type_Yearly))
        {
            Todate = date_addDays(Todate, -365);
            fillReading(sConditionFilter);
            create_statistics_pie_chart(sConditionFilter);
            fill_chart(sConditionFilter,sChartfilterType);
            initializePieChart();

            _txtFromdate.setText("");
            _txtFromdate.setAnimation(anim);
            _txtFromdate.setText( changedateFormat.format( date_addDays(Todate, -365)) + " - " +  changedateFormat.format(Todate));

        }


    }

    @OnClick(R.id.img_next)

    void img_next_click()
    {
        if(!Todate.equals(Calendar.getInstance().getTime())) {
            if (sConditionFilter.equals(CON_Type_Weekly)) {
                Todate = date_addDays(Todate, 7);

                fillReading(sConditionFilter);
                create_statistics_pie_chart(sConditionFilter);
                fill_chart(sConditionFilter,sChartfilterType);
                _txtFromdate.setText("");
                _txtFromdate.setAnimation(anim);
                _txtFromdate.setText( changedateFormat.format(date_addDays(Todate, -7))  + " - " + changedateFormat.format(Todate));


            }

            if (sConditionFilter.equals(CON_Type_Monthly)) {
                Todate = date_addDays(Todate, 30);
                current_date = Todate;
                fillReading(sConditionFilter);
                create_statistics_pie_chart(sConditionFilter);

                fill_chart(sConditionFilter,sChartfilterType);
                _txtFromdate.setText("");
                _txtFromdate.setAnimation(anim);
                _txtFromdate.setText( changedateFormat.format(date_addDays(Todate, -30))+ " - " +  changedateFormat.format(Todate));

            }

            if (sConditionFilter.equals(CON_Type_Yearly)) {
                Todate = date_addDays(Todate, 365);
                current_date = Todate;
                fillReading(sConditionFilter);
                create_statistics_pie_chart(sConditionFilter);
                fill_chart(sConditionFilter,sChartfilterType);

                _txtFromdate.setText("");
                _txtFromdate.setAnimation(anim);
                _txtFromdate.setText(changedateFormat.format(date_addDays(Todate, -365))  + " - " +  changedateFormat.format(Todate));

            }

        }

    }

    @OnClick(R.id.btnWeekly)
    void btnWeekly_click() {
        _img_previous.setVisibility(View.VISIBLE);
        _img_next.setVisibility(View.VISIBLE);
        try {
            Todate = Calendar.getInstance().getTime();
            change_btn_color(_btnWeekly);

            _txtGraph.setText("Weekly Progress Graph");


            sAMPMFilter = _spnrAMPM.getSelectedItem().toString();
            fillReading("Weekly");
            create_statistics_pie_chart("Weekly");
            sConditionFilter = "Weekly";
            sChartFilterValue = "-8";
            current_date = Calendar.getInstance().getTime();
            fromdate = changedateFormat.format(current_date);


            sFromChartFilter = changedateFormat_query.format(current_date);
            sOnlyStartDate = OnlydateFormat_query.format(current_date);

            current_date = date_addDays(current_date, -7);
            todate = changedateFormat.format(current_date);

            _txtFromdate.setText("");
            _txtFromdate.setAnimation(anim);
            _txtFromdate.setText(todate + " - " + fromdate);


            sTodateChartFilter = changedateFormat_query.format(current_date);
            sOnlyEndDate = OnlydateFormat_query.format(current_date);
            fill_chart(sConditionFilter,sChartfilterType);
        } catch (Exception ex) {
        }

    }

    @OnClick(R.id.btnLast10)
    void btnLast10_click() {
        try {
            _img_previous.setVisibility(View.GONE);
            _img_next.setVisibility(View.GONE);
            Todate = Calendar.getInstance().getTime();
            change_btn_color(_btnLast10);
            _txtGraph.setText("Last 10 Reading Progress Graph");

            sAMPMFilter = _spnrAMPM.getSelectedItem().toString();
            sConditionFilter = "Last10";
            fillReading("Last10");
            create_statistics_pie_chart(sConditionFilter);

            sChartFilterValue = "-11";
            current_date = Calendar.getInstance().getTime();
            fromdate = changedateFormat.format(current_date);

            current_date = date_addDays(current_date, -10);
            todate = changedateFormat.format(current_date);
            _txtFromdate.setText("");
            _txtFromdate.setAnimation(anim);
           // _txtFromdate.setText(todate + " - " + fromdate);
            _txtFromdate.setText("Last 10 Readings");

            fill_chart(sConditionFilter,sChartfilterType);
        } catch (Exception ex) {
        }

    }


    @OnClick(R.id.btnMonthly)
    void btnMonthly_click() {
        try {
            _img_previous.setVisibility(View.VISIBLE);
            _img_next.setVisibility(View.VISIBLE);
            Todate = Calendar.getInstance().getTime();
            change_btn_color(_btnMonthly);
            _txtGraph.setText("Monthly Progress Graph");


            sAMPMFilter = _spnrAMPM.getSelectedItem().toString();


            sConditionFilter = "Monthly";

            fillReading("Monthly");
            create_statistics_pie_chart(sConditionFilter);
            int i = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
            i = -i;
            sChartFilterValue = String.valueOf(i);

            current_date = Calendar.getInstance().getTime();
            fromdate = changedateFormat.format(current_date);

            current_date = date_addDays(current_date, i);
            todate = changedateFormat.format(current_date);
            _txtFromdate.setText("");
            _txtFromdate.setAnimation(anim);
            _txtFromdate.setText(todate + " - " + fromdate);
            fill_chart(sConditionFilter,sChartfilterType);
        } catch (Exception ex) {
        }
    }


    @OnClick(R.id.btnYearly)
    void btnYearly_click() {
        try {
            _img_previous.setVisibility(View.VISIBLE);
            _img_next.setVisibility(View.VISIBLE);
            Todate = Calendar.getInstance().getTime();
            change_btn_color(_btnYearly);
            _txtGraph.setText("Yearly Progress Graph");
            _btnYearly.setBackgroundResource(R.drawable.rx_curve_orange_background);
            sAMPMFilter = _spnrAMPM.getSelectedItem().toString();


            sConditionFilter = "Yearly";
            fillReading("Yearly");
            create_statistics_pie_chart(sConditionFilter);

            sChartFilterValue = "-365";
            current_date = Calendar.getInstance().getTime();
            fromdate = changedateFormat.format(current_date);

            current_date = date_addDays(current_date, -365);
            todate = changedateFormat.format(current_date);
            _txtFromdate.setText("");
            _txtFromdate.setAnimation(anim);
            _txtFromdate.setText(todate + " - " + fromdate);

            fill_chart(sConditionFilter,sChartfilterType);
        } catch (Exception ex) {
        }
    }


    @OnClick(R.id.rdbbs)
    void rdbbs_click() {

        _rdbbs.setTextColor(Color.parseColor("#df6301"));
        _rdbwt.setTextColor(getResources().getColor(R.color.gray));
        sChartfilterType = "bp";
        txt_axis_indicator.setText("Y-sys/dia values, X-Date");

        if(_viewPager.getCurrentItem()==0  || _viewPager.getCurrentItem()==1)
        {
            lnr_bp_value_indicator.setVisibility(View.VISIBLE);
        }


        lnr_weght_value_indicator.setVisibility(View.GONE);

        fill_chart(sConditionFilter,sChartfilterType);
        if(sChartfilterType!=null) {
            if (sChartfilterType.equals("wt")) {
                create_analysis_weight_pie(sConditionFilter);
              /*  mpiechart_normal.setData(null);
                mpiechart_normal.invalidate();*/
            } else {
                create_statistics_pie_chart(sConditionFilter);
            }
        }
    }


    @OnClick(R.id.rdbwt)
    void rdbwt_click() {
        _rdbwt.setTextColor(Color.parseColor("#df6301"));
        _rdbbs.setTextColor(getResources().getColor(R.color.gray));
        sChartfilterType = "wt";
        txt_axis_indicator.setText("Y-weight, X-Date");
        lnr_bp_value_indicator.setVisibility(View.GONE);

        if(_viewPager.getCurrentItem()==0  || _viewPager.getCurrentItem()==1)
        {
            lnr_weght_value_indicator.setVisibility(View.VISIBLE);
        }

        fill_chart(sConditionFilter,sChartfilterType);
        initializePieChart();

        if(sChartfilterType!=null) {
            if (sChartfilterType.equals("wt")) {
               if(mpiechart_normal!=null){
                   create_analysis_weight_pie(sConditionFilter);
                    /*mpiechart_normal.setData(null);
                    mpiechart_normal.invalidate();*/
                }
            } else {
                create_statistics_pie_chart(sConditionFilter);
            }
        }

    }


    @OnItemSelected(R.id.spnrAMPM)
    void spnrAMPM_onItemSelected(AdapterView<?> parent, int position) {

        if(((TextView) parent.getChildAt(0))!=null) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        }

        if(((TextView) parent.getChildAt(0))!=null) {
            ((TextView) parent.getChildAt(0)).setTextSize(10);
        }

        sAMPMFilter = _spnrAMPM.getSelectedItem().toString();
        fillReading(sConditionFilter);
        create_statistics_pie_chart(sConditionFilter);
        fill_chart(sConditionFilter,sChartfilterType);


    }


    @OnClick(R.id.viewpager_previous)
    void previous_click()
    {
        int currentPage = _viewPager.getCurrentItem();
        int totalPages = _viewPager.getAdapter().getCount();

        int previousPage = currentPage-1;
        if (previousPage < 0) {
            // We can't go back anymore.
            // Loop to the last page. If you don't want looping just
            // return here.
            previousPage = totalPages - 1;
        }

        _viewPager.setCurrentItem(previousPage, true);


    }


    @OnClick(R.id.viewpager_next)
    void next_click()
    {
        int currentPage = _viewPager.getCurrentItem();
        int totalPages = _viewPager.getAdapter().getCount();

        int nextPage = currentPage+1;
        if (nextPage >= totalPages) {
            nextPage = 0;
        }

        _viewPager.setCurrentItem(nextPage, true);

    }

    //ViewPager pager;
    String sAMPMFilter = "";
    AppController globalVariable;
    String sNewDoctorName;
    Boolean AddDoctorShowVisible = false;
    ArrayList<M_doctorlist> doctorlists = new ArrayList<M_doctorlist>();
    ArrayList<String> doctorname = new ArrayList<String>();
    ArrayList<Button> data_button_arr = new ArrayList<Button>();
    M_objectItem[] mobjectItemDoctor = null;

    private int mFillColor = Color.argb(150, 51, 181, 229);

    List<M_memberlist> MemberData = new ArrayList<M_memberlist>();
    String sFromChartFilter, sTodateChartFilter, sOnlyStartDate, sOnlyEndDate;
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat OnlydateFormat_query = new SimpleDateFormat("dd");
    Date current_date = Calendar.getInstance().getTime();
    Date Todate = Calendar.getInstance().getTime();
    String fromdate, todate;
    SharedPreferences pref;
    public static final String SHOW_TYPE_BP = "bp";
    public static final String SHOW_TYPE_WEIGHT = "wt";

    public static final String CON_Type_LastReading = "LastReading";
    public static final String CON_Type_Weekly = "Weekly";
    public static final String CON_Type_Last10 = "Last10";
    public static final String CON_Type_Monthly = "Monthly";
    public static final String CON_Type_Yearly = "Yearly";
    SQLiteHandler db;

    DateFormat changedateFormat = new SimpleDateFormat("dd MMM , yyyy");
    DateFormat changedateFormat_query = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format_date = new SimpleDateFormat("LLLL dd,yyyy");

    private  File dir,file;
    private  PdfWriter writer;

    private Menu objMemberMenu;
    private SqliteBPHandler db_bp;
    private Animation anim;
    private double valueweight = 0;


    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    SimpleDateFormat sdfs = new SimpleDateFormat("HH");
    private String kg;
    private String sConditionFilter = "";
    private String lb;
    private String weight, sChartfilterType;
    private String weight_unit;
    private String sMemberId;
    private String setting_get_def_site,setting_get_def_pos,setting_weight_unit,setting_last_enterd_values;
    private String goalSys;
    private String goalDias;
    private ProgressDialog pDialog;
    private boolean onResumecalled=false;
    private  Cursor crs_graph;
    ;
    private EditText clinicname;
    private Integer getSelectedRelationshipId;
    //Chart
    private SQLiteHandler db_main;
    private EditText txtdoctorname;
    private BarChart mBarChart;
    private PieChart mpiechart_normal;
    private List<BPM_Analysis_Heading> catList = new ArrayList<BPM_Analysis_Heading>();
    private BPAD_AnalysisExpandable exAdpt;
    private String sChartFilterValue;
    private PieChart mPiechart;
    private LineChart mLineChart;
    DateFormat format_single_date = new SimpleDateFormat("dd.MM");
    private Dialog dlg;
    //Chart

    private static boolean isNotNullOrEmpty(String str) {
        return (str != null && !str.isEmpty());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp_analysis);
        ButterKnife.bind(this);
        db=new SQLiteHandler(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);

        pref = this.getSharedPreferences("Global", MODE_PRIVATE);
        anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fab_slide_out_to_right);
        globalVariable = (AppController) getApplicationContext();
        db_bp = SqliteBPHandler.getInstance(this);
        db_main = SQLiteHandler.getInstance(this);

        initialize_bottom_bar();

        initialize_buttons();


        String[] time_filtres = getResources().getStringArray(R.array.AMPM_array);
        ArrayAdapter time_filtresAdpter = new ArrayAdapter<String>(this,
                R.layout.rxspinner_simple_text_layout
                ,
                time_filtres);
        _spnrAMPM.setAdapter(time_filtresAdpter);


        _viewPager.setAdapter(new MyPagesAdapter());
        _pageIndicator.setStrokeColor(Color.parseColor("#f38630"));


        _viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

                _pageIndicator.setViewPager(_viewPager);
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                _pageIndicator.setViewPager(_viewPager);
            }

            public void onPageSelected(int position) {






                if(position==0 || position==1)
                {
                    axis_desc.setVisibility(View.VISIBLE);
                    if(sChartfilterType.equals("bp"))
                    {
                        lnr_bp_value_indicator.setVisibility(View.VISIBLE);
                        lnr_weght_value_indicator.setVisibility(View.GONE);
                    }else
                    {
                        lnr_weght_value_indicator.setVisibility(View.VISIBLE);
                        lnr_bp_value_indicator.setVisibility(View.GONE);
                    }


                }else
                {
                    axis_desc.setVisibility(View.INVISIBLE);
                    lnr_bp_value_indicator.setVisibility(View.GONE);
                    lnr_weght_value_indicator.setVisibility(View.GONE);
                }



                _pageIndicator.setViewPager(_viewPager);

            }
        });


        show_default_date();


        getIntenet();
        get_defaults();
        getgoal();
        //getLastReading();


        fillReading("LastReading");
        create_statistics_pie_chart("LastReading");

        if(_expandableListDMView1!=null) {
            setListViewHeight(_expandableListDMView1);
        }

        sChartFilterValue = "0";
        sAMPMFilter = _spnrAMPM.getSelectedItem().toString();
        sChartfilterType = "bp";
        sConditionFilter = "LastReading";


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


        _expandableListDMView1.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPos, long id) {
                boolean sFlag = false;
                ImageView img = (ImageView) v.findViewById(R.id.imgavgright);

                TextView txtWeight_Detail = (TextView) v.findViewById(R.id.txtWeight_Detail);
                if (txtWeight_Detail != null) {

                    if (txtWeight_Detail.getText().toString().contains("Weight")) {
                        sFlag = true;
                    }
                }
                if (groupPos == 0) {
                    if (_expandableListDMView1.isGroupExpanded(0)) {
                        img.setImageResource(R.drawable.gray_down_arrows);
                        // setListViewHeight(exList);
                    } else {
                        img.setImageResource(R.drawable.gray_up_arrows);
                        // setListViewHeight(exList);
                    }
                }


                setListViewHeight(parent, groupPos);
                return sFlag;
            }
        });


    }

    public void fillReading(String Filter) {
        try {
            String sWeight="";

            catList.clear();

            List<BPM_Analysis_Item_Detail> result = null;
            BPM_Analysis_Item_Detail item = null;
            BPM_Analysis_Heading cat1 = null;

            if (Filter.equals("LastReading")) {
                Cursor cursordata;
                cursordata = db_bp.getLastReadingBPMonitarData(sMemberId, getSelectedRelationshipId);
                if (cursordata != null) {
                    if (cursordata.moveToFirst()) {
                        do {
                            weight = cursordata.getString(cursordata.getColumnIndex("weight"));
                            weight_unit = getValueOrDefault(cursordata.getString(cursordata.getColumnIndex("weight_unit")), "");
                            try {
                                current_date = dateFormat_query.parse(cursordata.getString(cursordata.getColumnIndex("bp_date")));
                            } catch (ParseException e) {
                                e.toString();
                            }

                            result = new ArrayList<BPM_Analysis_Item_Detail>();
                            cat1 = new BPM_Analysis_Heading(0, "Last Reading", "Systolic",
                                    cursordata.getString(cursordata.getColumnIndex("sys_avg")), get_percentage_calcution(goalSys, cursordata.getString(cursordata.getColumnIndex("sys_avg"))));
                            item = new BPM_Analysis_Item_Detail(99, "99", Filter, sAMPMFilter,setting_weight_unit,dateFormat_query.format(Todate));
                            result.add(item);
                            cat1.setItemList(result);
                            catList.add(cat1);

                            result = new ArrayList<BPM_Analysis_Item_Detail>();
                            cat1 = new BPM_Analysis_Heading(0, "Last Reading", "Diastolic", cursordata.getString(cursordata.getColumnIndex("dia_avg")), get_percentage_calcution(goalDias, cursordata.getString(cursordata.getColumnIndex("dia_avg"))));
                            item = new BPM_Analysis_Item_Detail(99, "99", Filter, sAMPMFilter,setting_weight_unit,dateFormat_query.format(Todate));
                            result.add(item);
                            cat1.setItemList(result);
                            catList.add(cat1);

                            result = new ArrayList<BPM_Analysis_Item_Detail>();
                            cat1 = new BPM_Analysis_Heading(0, "Last Reading", "Pulse", cursordata.getString(cursordata.getColumnIndex("pulse")), "");
                            item = new BPM_Analysis_Item_Detail(99, "99", Filter, sAMPMFilter,setting_weight_unit,dateFormat_query.format(Todate));
                            result.add(item);
                            cat1.setItemList(result);
                            catList.add(cat1);


                            result = new ArrayList<BPM_Analysis_Item_Detail>();
                            if(setting_weight_unit.equals("kg"))
                            {
                                sWeight = "Weight :" + cursordata.getString(cursordata.getColumnIndex("kg")) + " " + setting_weight_unit;
                            }else {
                                sWeight = "Weight :" + cursordata.getString(cursordata.getColumnIndex("lb")) + " " + setting_weight_unit;
                            }

                            String sBodyLocation = "Body Location : " + cursordata.getString(cursordata.getColumnIndex("body_part"));
                            String sBodyPosition = "Body Position : " + cursordata.getString(cursordata.getColumnIndex("position"));
                            cat1 = new BPM_Analysis_Heading(0, "Weight", sWeight, sBodyLocation, sBodyPosition);
                            item = new BPM_Analysis_Item_Detail(99, "99", Filter, sAMPMFilter,setting_weight_unit,dateFormat_query.format(Todate));
                            result.add(item);
                            cat1.setItemList(result);

                            catList.add(cat1);

                        } while (cursordata.moveToNext());
                    }
                    cursordata.close();
                }
            } else {
                result = new ArrayList<BPM_Analysis_Item_Detail>();

                cat1 = new BPM_Analysis_Heading(0, "Blood Pressure", "Min", "Max", "Avg");
                item = new BPM_Analysis_Item_Detail(0, "0", Filter, sAMPMFilter,setting_weight_unit,dateFormat_query.format(Todate));
                result.add(item);
                cat1.setItemList(result);
                catList.add(cat1);

                result = new ArrayList<BPM_Analysis_Item_Detail>();
                cat1 = new BPM_Analysis_Heading(1, "Read all " + Filter + " readings", "", "", "");
                item = new BPM_Analysis_Item_Detail(1, "1", Filter, sAMPMFilter,setting_weight_unit,dateFormat_query.format(Todate));
                result.add(item);
                cat1.setItemList(result);


                catList.add(cat1);
            }
            exAdpt = new BPAD_AnalysisExpandable(catList, BPA_AnalysisDisplayActivity.this, BPA_AnalysisDisplayActivity.this);
            _expandableListDMView1.setAdapter(exAdpt);
            if (!Filter.equals("LastReading")) {
                _expandableListDMView1.expandGroup(0);
            }


            //exList.expandGroup(0);

        } catch (Exception ex) {
            String s = String.valueOf(ex);
        }
        if (_expandableListDMView1.getAdapter() != null) {
            setListViewHeight(_expandableListDMView1, (_expandableListDMView1.getAdapter().getCount() - 1));
        }
    }

    private void getIntenet() {
        SharedPreferences pref = this.getSharedPreferences("Global", Context.MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");

        getSelectedRelationshipId = pref.getInt("RelationshipId", 0);

        if (globalVariable.getRealationshipId() != null) {
            getSelectedRelationshipId = Integer.parseInt(globalVariable.getRealationshipId());
        } else {
            getSelectedRelationshipId = 8;
        }

    }

    private void getgoal() {
        Cursor cursordata;

        cursordata = db_bp.getgoalBPMonitarData(Integer.parseInt(sMemberId), getSelectedRelationshipId);
        if (cursordata != null) {
            if (cursordata.moveToFirst()) {
                do {
                    goalSys = cursordata.getString(cursordata.getColumnIndex("bp_goalsystolic"));
                    goalDias = cursordata.getString(cursordata.getColumnIndex("bp_goaldiastolic"));
                    String goalWeightUnit = cursordata.getString(cursordata.getColumnIndex("bp_weightunit"));
                    String goalWeightKg = cursordata.getString(cursordata.getColumnIndex("bp_kg"));
                    String goalWeightlb = cursordata.getString(cursordata.getColumnIndex("bp_lb"));

                    if(setting_weight_unit.equals("kg"))
                    {
                        sGoalWeight=goalWeightKg;
                    }else
                    {
                        sGoalWeight=goalWeightlb;
                    }

                    _txtSys.setText(goalSys);
                    _txtdias.setText(goalDias);

                } while (cursordata.moveToNext());
            }
            cursordata.close();
        }
    }

    private void getLastReading() {
        Cursor cursordata;

        cursordata = db_bp.getLastReadingBPMonitarData(sMemberId, getSelectedRelationshipId);
        if (cursordata != null) {
            if (cursordata.moveToFirst()) {
                do {
                    String pulse = cursordata.getString(cursordata.getColumnIndex("pulse"));
                    String weight = cursordata.getString(cursordata.getColumnIndex("weight"));
                    String weight_unit = cursordata.getString(cursordata.getColumnIndex("weight_unit"));
                    String weight_kg = cursordata.getString(cursordata.getColumnIndex("kg"));
                    String weight_lb = cursordata.getString(cursordata.getColumnIndex("lb"));
                    String body_part = cursordata.getString(cursordata.getColumnIndex("body_part"));
                    String position = cursordata.getString(cursordata.getColumnIndex("position"));
                    String DisplayWeight = "";


                } while (cursordata.moveToNext());
            }
            cursordata.close();
        }
    }

    private void SetGoalDialog() {


        Cursor cursor_datagoal = null;
        //try {
        LayoutInflater inflater = LayoutInflater.from(this);

        final View dialogview = inflater.inflate(R.layout.bp_setgoal, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogview);
        //ButterKnife.bind(this, dialogview);


        _txtgoalSystolic = ButterKnife.findById(dialogview, R.id.txtgoalSystolic);
        _txtgoalDiastolic = ButterKnife.findById(dialogview, R.id.txtgoalDiastolic);
        _txtgoalweight = ButterKnife.findById(dialogview, R.id.txtgoalweight);
        _txtgoalweightunit = ButterKnife.findById(dialogview, R.id.txtgoalWeightunit);


        final Button _btncancel = ButterKnife.findById(dialogview, R.id.btn_setgoal_cancel);
        final Button _btnsave = ButterKnife.findById(dialogview, R.id.btn_setgoal_save);

        _txtgoalweightunit.setText(setting_weight_unit);

        show_previous_goal_data();
        dlg = builder.create();

        _txtgoalSystolic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int iSystolic = 70;
                try {
                    iSystolic = Integer.parseInt(_txtgoalSystolic.getText().toString().trim());
                } catch (NumberFormatException ex) { // handle your exception
                }
                numerdialog myDiag = numerdialog.newInstance(1, iSystolic, 1, 250, "_txtgoalSystolic", "SYSTOLIC");
                myDiag.show(getFragmentManager(), "Diag");
            }
        });

        _txtgoalDiastolic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int iSystolic = 85;
                try {
                    iSystolic = Integer.parseInt(_txtgoalDiastolic.getText().toString().trim());
                } catch (NumberFormatException ex) { // handle your exception

                }
                numerdialog myDiag = numerdialog.newInstance(1, iSystolic, 1, 100, "_txtgoalDiastolic", "DIASTOLIC");
                myDiag.show(getFragmentManager(), "Diag");
            }
        });

        _txtgoalweight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int iSystolic = 85;
                try {
                    iSystolic = Integer.parseInt(_txtgoalweight.getText().toString().trim());
                } catch (NumberFormatException ex) { // handle your exception

                }
                numerdialog myDiag = numerdialog.newInstance(1, iSystolic, 1, 100, "_txtgoalweight", "WEIGHT");
                myDiag.show(getFragmentManager(), "Diag");

            }
        });


        _btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        _btnsave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //_txtgoalSystolic.setText(inumerpicker.toString());
                //_txtgoalDiastolic.setText(inumerpicker.toString());

                if (!_txtgoalSystolic.getText().toString().isEmpty()) {

                    if (!_txtgoalDiastolic.getText().toString().isEmpty()) {
                        if (!_txtgoalweight.getText().toString().isEmpty()) {
                            Savegoaldata();
                            dlg.dismiss();

                        } else {
                            Toast.makeText(BPA_AnalysisDisplayActivity.this,
                                    "Please select weight",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(BPA_AnalysisDisplayActivity.this,
                                "Please enter Diastolic",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(BPA_AnalysisDisplayActivity.this,
                            "Please enter Systolic",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


        dlg.show();


//		} catch (Exception e) {
//
//			Log.d(TAG, "Error getgoalBPMonitarData");
//
//		} finally {
//			//cursor_datagoal.close();
//		}


    }


    private void Savegoaldata() {

        try {

            String sWEIGHT = _txtgoalweight.getText().toString();
            String sGoalSystolic = (_txtgoalSystolic.getText().toString());
            String sGoalDiastolic = (_txtgoalDiastolic.getText().toString());
            String sGoalWeightunit = (_txtgoalweightunit.getText().toString());
            ;


            double valuekgweight = 0;
            double valuelbweight = 0;


            if (setting_weight_unit.equals("kg")) {
                valueweight = Double.parseDouble(sWEIGHT);
                kg = Double.toString(valueweight);
                valuekgweight = Math.round(valueweight * 2.20462);
                lb = Double.toString(valuekgweight);


            } else {
                valueweight = Double.parseDouble(sWEIGHT);
                lb = Double.toString(valueweight);
                valuelbweight = Math.round(valueweight * 0.453592);
                kg = Double.toString(valuelbweight);

            }

            String skg = kg;
            String slb = lb;


            db_bp.deleteGoalBPDetails(sMemberId, getSelectedRelationshipId);

            db_bp.InsertBPGOALTable(sMemberId,
                    getSelectedRelationshipId,
                    sWEIGHT,
                    sGoalWeightunit,
                    sGoalSystolic,
                    sGoalDiastolic,
                    skg,
                    slb,
                    "");


            f_alert_ok("Goal", "Data Saved");


        } catch (Exception E) {
            f_alert_ok("Error", "Error" + E.getMessage());
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










    //Chart

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void show_default_date() {
        fromdate = changedateFormat.format(current_date);

        _txtFromdate.setText(fromdate);

    }

    public Date date_addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }


    private void initializePieChart() {
        try {
            mPiechart.setUsePercentValues(true);
            mPiechart.setDescription("");
            mPiechart.setDragDecelerationFrictionCoef(0.95f);
            mPiechart.setDrawHoleEnabled(true);
            mPiechart.setTransparentCircleColor(Color.WHITE);
            mPiechart.setHoleRadius(58f);
            mPiechart.setTransparentCircleRadius(61f);
            mPiechart.setDrawCenterText(true);
            mPiechart.setRotationAngle(0);
            mPiechart.setRotationEnabled(true);
            mPiechart.setCenterText("");

            mPiechart.setData(fill_pie_chart());
            mPiechart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        } catch (Exception ex) {
            String.valueOf(ex.toString());

        }
    }


    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }


    public int getArrayIndex(Integer[] arr, int value) {

        int k = 0;
        for (int i = 0; i < arr.length; i++) {

            if (arr[i] == value) {
                k = i;
                break;
            }
        }
        return k;
    }

    //Chart
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bp_menu, menu);
        this.objMemberMenu = menu;
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.circula_image, null);
        objMemberMenu.findItem(R.id.circlularImage).setActionView(mCustomView);
        ImageLoad(pref.getString("UserName",""),pref.getString("imagename",""));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent_network = new Intent(BPA_AnalysisDisplayActivity.this, BPA_MonitorSetting
                    .class);
            startActivity(intent_network);

            return true;
        }


        return super.onOptionsItemSelected(item);
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

   /* private void fillchart() {


        // limit lines are drawn behind data (and not on top)

        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getAxisRight().removeAllLimitLines();
        mLineChart.getAxisLeft().removeAllLimitLines();
        mLineChart.getXAxis().removeAllLimitLines();
        mLineChart.getXAxis().setAxisLineWidth(1f);
        mLineChart.getXAxis().setAxisLineColor(getResources().getColor(R.color.colorPrimary));
        mLineChart.getAxisLeft().setAxisLineWidth(1f);
        mLineChart.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.colorPrimary));
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("No Data Available.");
      //  mLineChart.setTouchEnabled(false);
      //  mLineChart.setDragEnabled(false);
      //  mLineChart.setScaleEnabled(false);
       // mLineChart.setPinchZoom(true);

        LimitLine llXAxis = new LimitLine(0f, "");
        llXAxis.setTextSize(10f);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.addLimitLine(llXAxis);

        YAxis RIGHTAxis = mLineChart.getAxisRight();
        RIGHTAxis.removeAllLimitLines();
        RIGHTAxis.setAxisMinValue(0f);
        RIGHTAxis.setStartAtZero(false);

        Integer iMemId = Integer.parseInt(sMemberId);
        Cursor cursor_Chart = db_bp.getAnalysisMaxLineChartData(iMemId, getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);


        float fMax = 0;
        float fMin = 0;
        float fMaxdia = 0;
        float fMindia = 0;
        float fMaxwt = 0;
        float fMinwt = 0;

        mLineChart.getAxisLeft().removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        mLineChart.getAxisLeft().setAxisMinValue(0f);
     //   mLineChart.getAxisLeft().setStartAtZero(false);
        if (cursor_Chart != null || !cursor_Chart.equals("")) {


            Integer J = cursor_Chart.getCount();
            cursor_Chart.moveToFirst();
            if (cursor_Chart.getCount() > 0) {

                // fMax = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bs_avg"))));
                fMax = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("sys_avg"))));
                fMin = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("sys_min"))));

                fMaxdia = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("dia_avg"))));
                fMindia = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("dia_min"))));
                fMaxwt = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("max_wt"))));
                fMinwt = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("min_wt"))));

                if (sChartfilterType.equals("bp")) {
                    if (sConditionFilter.equals("LastReading")) {
                        mLineChart.getAxisLeft().setAxisMinValue(0f);
                    } else {
                        if (fMax == fMin) {
                            mLineChart.getAxisLeft().setAxisMinValue(0f);
                        } else {

                            if (fMin < fMindia) {
                                mLineChart.getAxisLeft().setAxisMinValue(fMin);
                            } else {
                                mLineChart.getAxisLeft().setAxisMinValue(fMindia);
                            }

                        }
                    }

                    if (fMax > fMaxdia) {
                        mLineChart.getAxisLeft().setAxisMaxValue(fMax+50);
                    } else {
                        mLineChart.getAxisLeft().setAxisMaxValue(fMaxdia);
                    }

                } else {
                    if (sConditionFilter.equals("LastReading")) {


                        //mLineChart.getAxisLeft().setAxisMinValue(0f);
                        if (fMinwt == fMaxwt) {
                            mLineChart.getAxisLeft().setAxisMinValue(0f);
                        } else {
                            mLineChart.getAxisLeft().setAxisMinValue(fMinwt);
                        }
                    } else {
                        if (fMinwt == fMaxwt) {
                            mLineChart.getAxisLeft().setAxisMinValue(0f);
                        } else {


                            mLineChart.getAxisLeft().setAxisMinValue(fMinwt);
                        }
                    }
                    mLineChart.getAxisLeft().setAxisMaxValue(fMaxwt);
                }
            }


        }


        if (sConditionFilter.equals("LastReading")) {

            if (sChartfilterType.equals("bp")) {
                set2Data();
            } else {
                set2DataWT();
            }
        } else {
            if (sChartfilterType.equals("bp")) {
                setconditionData();
            } else {
                setconditionDataWT();
            }
        }

        mLineChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);


    }*/

    @Override
    public void onDone(int value, String sClass) {


        switch (sClass) {
            case "_txtgoalSystolic":
                _txtgoalSystolic.setText(Integer.toString(value));
                break;
            case "_txtgoalDiastolic":
                _txtgoalDiastolic.setText(Integer.toString(value));
                break;
            case "_txtgoalweight":
                _txtgoalweight.setText(Integer.toString(value));
                break;
        }


    }

 /*   private void setconditionDataWT() {
        current_date = Calendar.getInstance().getTime();


        Cursor cursor_Chart;
        //  Cursor cursor_MinMaxChart;
        Integer iMemId = Integer.parseInt(sMemberId);
        cursor_Chart = db_bp.getAllData_Chart(iMemId, getSelectedRelationshipId);
        todate = dateFormat_query.format(current_date);
        // Integer iDays=Integer.parseInt(sChartFilterValue)+1;
        Integer iDays = 0;
        if (sConditionFilter.equals("Monthly")) {
            iDays = Integer.parseInt(sChartFilterValue);
        } else {
            iDays = Integer.parseInt(sChartFilterValue) + 1;
        }
        current_date = date_addDays(current_date, iDays);
        fromdate = dateFormat_query.format(current_date);

        cursor_Chart = db_bp.showAllData_Chart(iMemId, getSelectedRelationshipId, fromdate, todate);
        ArrayList<String> xVals = new ArrayList<String>();

        cursor_Chart.moveToFirst();

        iMemId = Integer.parseInt(sMemberId);

        cursor_Chart = db_bp.getAnalysisLineChartData(iMemId, getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);

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
                    date2 = df1.parse(stodate);

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
            cursor_Chart = db_bp.getAnalysisLineChartData(iMemId, getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);
            cursor_Chart.moveToFirst();
            while (!cursor_Chart.isAfterLast()) {

                float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("max_wt"))));

                String dateofDrink = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bp_date")));

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
                i++;
                cursor_Chart.moveToNext();
            }


            cursor_Chart.close();


            LineDataSet set1 = new LineDataSet(yVals, "Weight");
            set1.setFillColor(Color.RED);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.setCircleColor(Color.parseColor("#f38630"));
            set1.setColor(Color.parseColor("#f38630"));
            set1.setLineWidth(1f);
            set1.setCircleSize(5f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setFillColor(Color.RED);
            set1.setDrawFilled(true);
            set1.setFillColor(Color.parseColor("#f38630"));
            set1.setValueTextColor(Color.TRANSPARENT);

            ArrayList<Entry> yVals2 = new ArrayList<Entry>();

            ArrayList<LineDataSet> dataSets;
            dataSets = new ArrayList<LineDataSet>();
            dataSets.add(set1); // add the datasets

            LineData data = new LineData(xVals, dataSets);
            data.notifyDataChanged();
            // set data
            mLineChart.setData(data);
            mLineChart.invalidate();

        }
    }*/

 /*   private void setconditionData() {
        current_date = Calendar.getInstance().getTime();
        ArrayList<String> xVals = new ArrayList<String>();

        Cursor cursor_Chart;
        // Cursor cursor_MinMaxChart;
        Integer iMemId = Integer.parseInt(sMemberId);
        cursor_Chart = db_bp.getAllData_Chart(iMemId, getSelectedRelationshipId);
        Integer iDays = 0;
        todate = dateFormat_query.format(current_date);
        if (sConditionFilter.equals("Monthly")) {
            iDays = Integer.parseInt(sChartFilterValue);
        } else {
            iDays = Integer.parseInt(sChartFilterValue) + 1;
        }
        current_date = date_addDays(current_date, iDays);
        fromdate = dateFormat_query.format(current_date);
        cursor_Chart = db_bp.showAllData_Chart(iMemId, getSelectedRelationshipId, fromdate, todate);


        cursor_Chart.moveToFirst();

        iMemId = Integer.parseInt(sMemberId);

        cursor_Chart = db_bp.getAnalysisLineChartData(iMemId, getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);

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
                    date2 = df1.parse(stodate);

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
            ArrayList<Entry> yVals1 = new ArrayList<Entry>();
            i = 0;
            cursor_Chart = db_bp.getAnalysisLineChartData(iMemId, getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);
            cursor_Chart.moveToFirst();
            while (!cursor_Chart.isAfterLast()) {

                float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("sys_avg"))));
                float f2 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("dia_avg"))));
                String dateofDrink = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bp_date")));

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
                    yVals1.add(new Entry(f2, i));
                } else {
                    int z = iIndex.indexOf((dm_day));

                    yVals.add(new Entry(f1, z));
                    yVals1.add(new Entry(f2, z));
                }
                i++;
                cursor_Chart.moveToNext();
            }


            cursor_Chart.close();
            // create a dataset and give it a type
            LineDataSet set1 = new LineDataSet(yVals, "Systolic");
            LineDataSet set2 = new LineDataSet(yVals1, "Diastolic");

            //set1.setFillColor(Color.RED);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.setCircleColor(Color.parseColor("#f38630"));
            set1.setColor(Color.parseColor("#f38630"));
            set1.setLineWidth(1f);
            set1.setCircleSize(5f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setValueTextColor(Color.TRANSPARENT);

            set1.setDrawFilled(true);
            set1.setFillColor(Color.parseColor("#f38630"));
            set2.enableDashedLine(10f, 5f, 0f);
            set2.setColor(Color.GREEN);
            set2.setCircleColor(Color.GREEN);
            set2.setLineWidth(1f);
            set2.setCircleSize(5f);
            set2.setDrawCircleHole(false);
            set2.setValueTextColor(Color.TRANSPARENT);
            set2.setValueTextSize(9f);
            set2.setDrawFilled(true);
            set2.setFillColor(Color.GREEN);
            ArrayList<LineDataSet> dataSets;
            dataSets = new ArrayList<LineDataSet>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2); // add the datasets

            LineData data = new LineData(xVals, dataSets);
            data.notifyDataChanged();
            // set data
            mLineChart.setData(data);
            mLineChart.invalidate();
        }
    }
*/

  /*  private void set2DataWT() {


        Cursor cursor_Chart;
        current_date = Calendar.getInstance().getTime();
        // Cursor cursor_MinMaxChart;
        Integer g_time = 0;
        Integer iMemId = Integer.parseInt(sMemberId);
        //  cursor_Chart = db_bp.getAllData_Chart(iMemId, getSelectedRelationshipId);

        fromdate = dateFormat_query.format(current_date);
        current_date = date_addDays(current_date, Integer.parseInt(sChartFilterValue));
        todate = dateFormat_query.format(current_date);
        //cursor_Chart = db_bp.showWeightLastReading(iMemId, getSelectedRelationshipId);

        ArrayList<String> xVals = new ArrayList<String>();

        //  cursor_Chart.moveToFirst();


        for (Integer k = 0; k <= 23; k = k + 1) {
            xVals.add(String.valueOf(k));
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        cursor_Chart = db_bp.getAnalysisLineChartData(iMemId, getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);
        //cursor_Chart = db_bp.getAnalysisLineChartData(iMemId, getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);
        Integer h = cursor_Chart.getCount();

        if ((cursor_Chart != null) || (cursor_Chart.getCount() > 0)) {
            if (cursor_Chart.moveToFirst()) {
                float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("max_wt"))));

                String dateofDrink = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bp_date")));

                Calendar c = Calendar.getInstance();
                Date sdate = new Date();


                String Hours = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bp_time")));


                try {
                    current_date = sdf.parse(Hours);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    g_time = Integer.parseInt(sdfs.format(current_date));
                } catch (NumberFormatException e) {
                    e.toString();
                }


                SimpleDateFormat sdate_format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    sdate = sdate_format.parse(dateofDrink);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.setTime(sdate);


                yVals.add(new Entry(f1, (g_time )));

                cursor_Chart.moveToNext();
            }
        }
        cursor_Chart.close();
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Weight");
        set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.setCircleColor(Color.parseColor("#f38630"));
        set1.setColor(Color.parseColor("#f38630"));
        set1.setLineWidth(1f);
        set1.setCircleSize(5f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillColor(Color.RED);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.parseColor("#f38630"));
        set1.setValueTextColor(Color.TRANSPARENT);

        ArrayList<LineDataSet> dataSets;
        dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        LineData data = new LineData(xVals, dataSets);
        data.notifyDataChanged();
        // set data
        mLineChart.setData(data);
        mLineChart.invalidate();
    }*/


  /*  private void set2Data() {


        Cursor cursor_Chart;
        current_date = Calendar.getInstance().getTime();
        // Cursor cursor_MinMaxChart;
        Integer g_time = 0;
        Integer iMemId = Integer.parseInt(sMemberId);

        fromdate = dateFormat_query.format(current_date);
        current_date = date_addDays(current_date, Integer.parseInt(sChartFilterValue));
        todate = dateFormat_query.format(current_date);


        ArrayList<String> xVals = new ArrayList<String>();


        for (Integer k = 0; k <= 23; k = k + 1) {
            xVals.add(String.valueOf(k));
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        cursor_Chart =  db_bp.getAnalysisLineChartData(iMemId, getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);

        if ((cursor_Chart != null) || (cursor_Chart.getCount() > 0)) {
            if (cursor_Chart.moveToFirst()) {

                float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("sys_avg"))));

                float f2 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("dia_avg"))));
                String dateofDrink = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bp_date")));
                String Hours = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bp_time")));
                Calendar c = Calendar.getInstance();
                Date sdate = new Date();


                SimpleDateFormat sdate_format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    sdate = sdate_format.parse(dateofDrink);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.setTime(sdate);

                try {
                    current_date = sdf.parse(Hours);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    g_time = Integer.parseInt(sdfs.format(current_date));
                } catch (NumberFormatException e) {
                    e.toString();
                }

                yVals.add(new Entry(f1, (g_time )));
                yVals1.add(new Entry(f2, (g_time)));

                // yVals.add(new Entry(f1, (dm_day-1)));
                //i++;
                cursor_Chart.moveToNext();
            }
        }
        cursor_Chart.close();


        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Systolic");
        LineDataSet set2 = new LineDataSet(yVals1, "Diastolic");
        //LineDataSet set1 = new LineDataSet(yVals,MinMax);
        //set1.setFillAlpha(110);
        set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.setCircleColor(Color.parseColor("#f38630"));
        set1.setColor(Color.parseColor("#f38630"));
        set1.setLineWidth(1f);
        set1.setCircleSize(5f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillColor(Color.RED);
        set1.setDrawFilled(true);
        set1.setValueTextColor(Color.TRANSPARENT);
        set1.setFillColor(Color.parseColor("#f38630"));
        set2.setFillColor(Color.parseColor("#083157"));
        // set the line to be drawn like this "- - - - - -"
        set2.enableDashedLine(10f, 5f, 0f);
        set2.setColor(Color.GREEN);
        set2.setCircleColor(Color.GREEN);
        set2.setLineWidth(1f);
        set2.setCircleSize(5f);
        set2.setDrawCircleHole(false);
        set2.setValueTextSize(9f);
        //set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawFilled(true);
        set2.setFillColor(Color.RED);
        set2.setValueTextColor(Color.TRANSPARENT);
        ArrayList<LineDataSet> dataSets;
        dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2); // add the datasets


        LineData data = new LineData(xVals, dataSets);
        data.notifyDataChanged();
        // set data
        mLineChart.setData(data);
        mLineChart.invalidate();
    }*/


 /*   private void initializeBPBarchart() {


        float f1 = 0;
        float f2 = 0;


        mBarChart.setDescription("");
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawHighlightArrow(false);
        XAxis xl = mBarChart.getXAxis();
        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawGridLines(false);
        mBarChart.getAxisRight().setEnabled(false);

        Cursor cursor_Chart;
        Integer iMemId = Integer.parseInt(sMemberId);


        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();


        ArrayList<Integer> iIndex = new ArrayList<Integer>();


        ArrayList<Date> dates = new ArrayList<Date>();

        iIndex = new ArrayList<Integer>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        cursor_Chart = db_bp.getAnalysisLineChartData(iMemId, getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);
        Integer j = cursor_Chart.getCount();

        if (cursor_Chart.moveToFirst()) {
            do {

                try {
                    current_date = Calendar.getInstance().getTime();
                    String stodate = dateFormat_query.format(current_date);
                    date2 = df1.parse(stodate);

                    Integer iDays = 0;
                    if (sConditionFilter.equals("Monthly")) {
                        iDays = Integer.parseInt(sChartFilterValue);
                    } else {
                        iDays = Integer.parseInt(sChartFilterValue) + 1;
                    }
                    // Integer iDays = Integer.parseInt(sChartFilterValue) + 1;
                    if (sChartFilterValue.equals("0")) {
                        current_date = dateFormat_query.parse(cursor_Chart.getString(cursor_Chart.getColumnIndex("bp_date")));
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

            } while (cursor_Chart.moveToNext());
        }

        int i = 0;

        if (cursor_Chart.moveToFirst()) {
            do {
                f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("sys_avg"))));
                f2 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("dia_avg"))));
                String dateofDrink = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bp_date")));

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
                    yVals2.add(new BarEntry(f2, i));
                } else {
                    int z = iIndex.indexOf((dm_day));




                    yVals1.add(new BarEntry(f1, ((z+1)*2)+2));
                    yVals2.add(new BarEntry(f2, ((z+1)*2)+2));
                }
                // yVals.add(new Entry(f1, (dm_day-1)));
                i++;
                cursor_Chart.moveToNext();
            } while (cursor_Chart.moveToNext());
        }


        cursor_Chart.close();

        BarDataSet set1 = new BarDataSet(yVals1, "Systolic");
        BarDataSet set2 = new BarDataSet(yVals2, "Diastolic");


        set1.setColor(Color.rgb(255, 140, 0));
        set2.setColor(Color.GREEN);
        set1.setValueTextColor(Color.TRANSPARENT);
        set2.setValueTextColor(Color.TRANSPARENT);
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);

        BarData data = new BarData(xVals, dataSets);

        data.setGroupSpace(80f);


        mBarChart.setData(data);
        mBarChart.invalidate();
        //End Barchart
    }*/



 /*   private void initializeWTBarchart() {


        mBarChart = (BarChart) findViewById(R.id.barchart);

        mBarChart.setDescription("");

        mBarChart.setPinchZoom(false);

        mBarChart.setDrawBarShadow(false);

        mBarChart.setDrawGridBackground(false);

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
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);

        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawGridLines(false);

        mBarChart.getAxisRight().setEnabled(false);


        Cursor cursor_Chart;
        Integer iMemId = Integer.parseInt(sMemberId);


        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        iMemId = Integer.parseInt(sMemberId);

        cursor_Chart = db_bp.getAnalysisLineChartData(iMemId, getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);

        xVals = new ArrayList<String>();

        ArrayList<Integer> iIndex = new ArrayList<Integer>();
        if (cursor_Chart != null || !cursor_Chart.equals("")) {


            Integer J = cursor_Chart.getCount();
            cursor_Chart.moveToFirst();

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
                        Integer iDays = 0;
                        if (sConditionFilter.equals("Monthly")) {
                            iDays = Integer.parseInt(sChartFilterValue);
                        } else {
                            iDays = Integer.parseInt(sChartFilterValue) + 1;
                        }
                        //Integer iDays = Integer.parseInt(sChartFilterValue) + 1;
                        if (sChartFilterValue.equals("0")) {
                            current_date = dateFormat_query.parse(cursor_Chart.getString(cursor_Chart.getColumnIndex("bp_date")));
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



            int i = 0;
            if (cursor_Chart.moveToFirst()) {
                do {

                    float f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("max_wt"))));

                    String dateofDrink = String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("bp_date")));

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
                }while (cursor_Chart.moveToNext());
            }


            cursor_Chart.close();

            BarDataSet set1 = new BarDataSet(yVals1, "Weight");

            set1.setColor(Color.rgb(255, 140, 0));
            set1.setValueTextColor(Color.TRANSPARENT);

            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(set1);


            BarData data = new BarData(xVals, dataSets);
            mBarChart.setData(data);
            mBarChart.invalidate();
        }


    }*/

    public String getValueOrDefault(String value, String defaultValue) {
        return isNotNullOrEmpty(value) ? value : defaultValue;
    }



    class MyPagesAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return 3;
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
                    resId = R.layout.linechart;
                    view = inflater.inflate(resId, null);
                    mLineChart = (LineChart) view.findViewById(R.id.Linechart);
                    fill_chart(sConditionFilter,sChartfilterType);


                    break;
                case 1:
                    resId = R.layout.barchart;
                    view = inflater.inflate(resId, null);
                    mBarChart = (BarChart) view.findViewById(R.id.barchart);
                    fill_chart(sConditionFilter,sChartfilterType);

                    break;

                case 2:
                    resId = R.layout.dm_monitor_postpiechart;
                    view = inflater.inflate(resId, null);
                    mpiechart_normal = (PieChart) view.findViewById(R.id.piepostchart);

                    if(sChartfilterType.equals("wt"))
                    {
                        /*mpiechart_normal.setData(null);
                        mpiechart_normal.invalidate();*/
                        create_analysis_weight_pie(sConditionFilter);
                    }else
                    {
                        create_statistics_pie_chart(sConditionFilter);
                    }


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

    private void initialize_bottom_bar() {

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home_icon_white, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("New Entry", R.drawable.round_plus_white, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Add Doctor", R.drawable.add_doctor_white, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Set Goal", R.drawable.setgoal_white, R.color.colorPrimary);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Send Report", R.drawable.report_white, R.color.colorPrimary);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));

// Disable the translation inside the CoordinatorLayout
        // bottomNavigation.setBehaviorTranslationEnabled(false);

// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setUseElevation(true);

// Force to tint the drawable (useful for font with icon for example)
        // bottomNavigation.setForceTint(true);

// Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);

// Use colored navigation with circle reveal effect
        // bottomNavigation.setColored(true);

// Set current item programmatically
        //bottomNavigation.setCurrentItem(1);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.colorPrimary));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {

                if (position == 0) {
                    gotohome();
                } else if (position == 1) {
                    Intent Intenet_bp = new Intent(BPA_AnalysisDisplayActivity.this, BPA_NewEntry.class);
                    startActivity(Intenet_bp);
                } else if (position == 2) {
                    Add_Doctor_Dialog myDiag = Add_Doctor_Dialog.newInstance("add_doctor");
                    myDiag.show(getFragmentManager(), "Diag");
                } else if (position == 3) {
                    SetGoalDialog();
                } else if (position == 4) {
                    create_pdf(sConditionFilter);
                }

            }
        });
    }

    private void gotohome() {
        try {
            Intent Intenet_main = new Intent(this, MainActivity.class);
            startActivity(Intenet_main);

        } catch (Exception ex) {

        }

    }

    private void initialize_buttons() {
        data_button_arr.clear();
        data_button_arr.add(_btnLastRead);
        data_button_arr.add(_btnWeekly);
        data_button_arr.add(_btnLast10);
        data_button_arr.add(_btnMonthly);
        data_button_arr.add(_btnYearly);

        _btnLastRead.setBackgroundResource(R.drawable.rx_curve_orange_background);
        _btnLastRead.setTextColor(Color.WHITE);
        _btnWeekly.setTextColor(Color.GRAY);
        _btnLast10.setTextColor(Color.GRAY);
        _btnMonthly.setTextColor(Color.GRAY);
        _btnYearly.setTextColor(Color.GRAY);


        _btnWeekly.setBackgroundResource(R.drawable.btn_transperant);
        _btnLast10.setBackgroundResource(R.drawable.btn_transperant);
        _btnMonthly.setBackgroundResource(R.drawable.btn_transperant);
        _btnYearly.setBackgroundResource(R.drawable.btn_transperant);
    }

    private void change_btn_color(Button btn) {
        for (int i = 0; i < data_button_arr.size(); i++) {
            if (data_button_arr.get(i) == btn) {
                data_button_arr.get(i).setBackgroundResource(R.drawable.rx_curve_orange_background);
                data_button_arr.get(i).setTextColor(Color.WHITE);

            } else {
                data_button_arr.get(i).setTextColor(Color.GRAY);
                data_button_arr.get(i).setBackgroundResource(R.drawable.btn_transperant);
            }
        }
    }

    private void show_previous_goal_data() {
        Cursor cursor_data;
        cursor_data = db_bp.getgoalBPMonitarData(Integer.parseInt(sMemberId),
                getSelectedRelationshipId);

        if ((cursor_data != null) || (cursor_data.getCount() > 0)) {
            if (cursor_data.moveToFirst()) {
                cursor_data.moveToFirst();

                String sgoal_Systolic = String.valueOf(
                        cursor_data.getString(cursor_data.getColumnIndex(
                                "bp_goalsystolic")));
                String sgoal_Diastolic = String.valueOf(
                        cursor_data.getString(cursor_data.getColumnIndex(
                                "bp_goaldiastolic")));
                String sgoal_weightkg = String.valueOf(
                        cursor_data.getString(cursor_data.getColumnIndex(
                                "bp_kg")));
                String sgoal_weightlb = String.valueOf(
                        cursor_data.getString(cursor_data.getColumnIndex(
                                "bp_lb")));
                String sgoal_wtunit = String.valueOf(
                        cursor_data.getString(cursor_data.getColumnIndex(
                                "bp_weightunit")));
                _txtgoalSystolic.setText(sgoal_Systolic);
                _txtgoalDiastolic.setText(sgoal_Diastolic);


                if (setting_weight_unit.equals("kg")) {
                    _txtgoalweight.setText(sgoal_weightkg);
                } else {
                    _txtgoalweight.setText(sgoal_weightlb);
                }
            } else {

            }
        } else {
            _txtgoalSystolic.setText("72");
            _txtgoalDiastolic.setText("72");
            _txtgoalweight.setText("72");
            _txtgoalweightunit.setText("Kg");
        }
    }

    private String get_percentage_calcution(String Goal_Value, String Actual_Value) {
        String value = "";
        Double Actual_value = 0.0;
        Double f_value = 0.0;
        Double s_value = 0.0;

        try
        {
            if ((Goal_Value == null) || (Goal_Value == ""))
            {
                value = "Goal is not set";
            }
            else if((Actual_Value == null) || (Actual_Value == ""))
            {
                value = "Value not found";
            }
            else
            {
                f_value = Double.parseDouble(Goal_Value);
                s_value = Double.parseDouble(Actual_Value);

                if (f_value > s_value)
                {
                    Actual_value = ((f_value - s_value) / f_value) * 100;
                    value = String.format("%.2f", Actual_value) + "% Less than the desire goal";
                }
                else if (s_value > f_value)
                {
                    Actual_value = ((s_value - f_value) / f_value) * 100;
                    value = String.format("%.2f", Actual_value) + "% More than the desire goal";
                }
                else if (s_value.equals(f_value))
                {
                    value = "Congrats your goal is achived";
                }
            }
        } catch (Exception e) {
            e.toString();
        }
        return value;
    }

    private PieData fill_pie_chart() {

        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
        Float diastolic = 0f, systolic = 0f, pulse = 0f;
        PieData data = null;
        Cursor cursor_Chart = db_bp.getAnalysisLineChartData(Integer.parseInt(sMemberId), getSelectedRelationshipId, sChartFilterValue, sAMPMFilter);

        if ((cursor_Chart != null) || (cursor_Chart.getCount() > 0)) {
            if (cursor_Chart.moveToFirst()) {
                do {
                    try {
                        diastolic = Float.parseFloat(cursor_Chart.getString(cursor_Chart.getColumnIndex("dia_avg")));
                        systolic = Float.parseFloat(cursor_Chart.getString(cursor_Chart.getColumnIndex("sys_avg")));
                        pulse = Float.parseFloat(cursor_Chart.getString(cursor_Chart.getColumnIndex("pulse_avg")));

                    } catch (NumberFormatException e) {
                        e.toString();
                    }
                } while (cursor_Chart.moveToNext());
            }
        }

                   /* yVals1.add(new Entry((systolic), 0));
                    yVals1.add(new Entry((diastolic), 1));
                    yVals1.add(new Entry((pulse), 2));
*/

        String[] mParties = new String[]{
                "Systolic ", "Diastolic ", "Pulse"
        };
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < mParties.length; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "");


        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        dataSet.setColors(colors);

        data = new PieData();
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.RED);
        mPiechart.setData(data);

        return data;
    }
    @Override
    public void onSelectMember(String Relationship_id, String name,String Imagename)
    {
        globalVariable.setRealationshipId(Relationship_id);
        getSelectedRelationshipId = Integer.parseInt(Relationship_id);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserName", name);
        editor.commit();
        check_member_data();
        show_default_date();


        getIntenet();
        getgoal();
        getLastReading();


        fillReading("LastReading");
        create_statistics_pie_chart("LastReading");
        setListViewHeight(_expandableListDMView1);

        ImageLoad(name,Imagename);
    }

    private void check_member_data()
    {
        Cursor cursor_data;
        cursor_data = db_bp.getBPMonitorCount(Integer.parseInt(sMemberId),getSelectedRelationshipId);

        if ((cursor_data != null) && (cursor_data.getCount() > 0)) {
            if (cursor_data.moveToFirst()) {
               /* Intent Intenet_dm = new Intent(this, BPA_AnalysisDisplayActivity.class);
                startActivity(Intenet_dm);*/
            }
        }else{
            Intent Intenet_dm = new Intent(this, BPA_WelcomeActivity.class);
            startActivity(Intenet_dm);
            finish();
        }
    }

    private void get_defaults()
    {
        String setting_name = "";
        Cursor cursor_session = db_bp.getAllSetting_datat(String.valueOf(getSelectedRelationshipId), sMemberId, "1");

        if (cursor_session.moveToFirst()) {
            do {


                setting_name = cursor_session.getString(cursor_session.getColumnIndex("name"));

                if (setting_name.equals("default_site")) {
                    setting_get_def_site = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("default_position")) {
                    setting_get_def_pos = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }
                if (setting_name.equals("weight_unit")) {
                    setting_weight_unit = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }

                if (setting_name.equals("use_last_entered_values")) {
                    setting_last_enterd_values = cursor_session.getString(cursor_session.getColumnIndex("value"));
                }


            } while (cursor_session.moveToNext());
        }

        if(setting_weight_unit==null)
        {
            setting_weight_unit="kg";
        }


    }
    @Override
    public void onResume() {

        super.onResume();
        if(onResumecalled) {
            finish();
            startActivity(getIntent());
        }
        onResumecalled=true;
    }

    private void fill_chart(String filter_condition,String show_type) {

        crs_graph = db_bp.show_BPMinMaxAnalysisFilterData_chart(Integer.parseInt(sMemberId), getSelectedRelationshipId,
                filter_condition,sAMPMFilter, dateFormat_query.format(Todate));


        switch (filter_condition) {
            case CON_Type_LastReading:

                break;
            case CON_Type_Weekly:


                break;
            case CON_Type_Last10:



                break;
            case CON_Type_Monthly:

                break;
            case CON_Type_Yearly:

                break;
        }
        try {

            if (mLineChart != null) {
                fill_shaded_graph(show_type);
            }

            if (mBarChart != null) {
                fill_bar_chart(show_type);
            }
            if(mpiechart_normal!=null) {
                if (sChartfilterType.equals("wt")) {
                    create_analysis_weight_pie(sConditionFilter);
              /*  mpiechart_normal.setData(null);
                mpiechart_normal.invalidate();*/
                } else {
                    create_statistics_pie_chart(sConditionFilter);
                }
            }

        }catch (Exception e)
        {
            e.toString();
        }

    }

    private void fill_shaded_graph(String show_type)
    {

        mLineChart.setBackgroundColor(Color.WHITE);
        mLineChart.setDrawGridBackground(false);
        mLineChart.invalidate();

        mLineChart.setDrawBorders(false);
        mLineChart.getXAxis().setGranularity(1f);

        Legend l = mLineChart.getLegend();
        l.setEnabled(false);
       /* l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);*/


       /* AxisValueFormatter xAxisFormatter = new DayAxisTestValueFormatter(mLineChart);
        mLineChart.getXAxis().setValueFormatter(xAxisFormatter);*/

      /*  AxisValueFormatter xAxisFormatter = new DayWithMonthAxisValueFormatter(mLineChart);
        mLineChart.getXAxis().setValueFormatter(xAxisFormatter);*/
       // mLineChart.getXAxis().setValueFormatter(null);


        mLineChart.setDescription("");

        XAxis xAxis = mLineChart.getXAxis();


        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getAxisLeft().setEnabled(false);
        mLineChart.getAxisRight().setEnabled(false);
        LineDataSet set_sys=null,set_dia=null,set_weight=null;


        ArrayList<Entry> entries_sys = new ArrayList<Entry>();
        ArrayList<Entry> entries_dia = new ArrayList<Entry>();
        ArrayList<Entry> entries_weight = new ArrayList<Entry>();

        if ((crs_graph != null) && (crs_graph.getCount() > 0)) {
            if (crs_graph.moveToFirst()) {
                do {

                    try {
                        current_date = dateFormat_query.parse(crs_graph.getString(crs_graph.getColumnIndex("bp_date")));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String dt=format_single_date.format(current_date);
                  //  float f=Float.parseFloat(dt);

                    entries_sys.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)),crs_graph.getFloat(crs_graph.getColumnIndex("systolic"))));
                    entries_dia.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)),crs_graph.getFloat(crs_graph.getColumnIndex("diastolic"))));
                    entries_weight.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)),crs_graph.getFloat(crs_graph.getColumnIndex("weight"))));

                } while (crs_graph.moveToNext());

                set_dia = new LineDataSet(entries_dia, "Diastolic");
                set_sys= new LineDataSet(entries_sys, "Systolic");
                set_weight=new LineDataSet(entries_weight, "Weight");

                int[]colrs_dia=new int[entries_dia.size()];
                for(int a=0;a<entries_dia.size();a++) {
                    float v_dia = entries_dia.get(a).getY();
                    if (v_dia < 50) {
                        colrs_dia[a] = Color.RED;
                    } else if (v_dia > 50 && v_dia <= 80) {
                        colrs_dia[a] = Color.GREEN;
                    } else if (v_dia > 80 && v_dia < 90) {
                        colrs_dia[a] = Color.rgb(255, 140, 0);
                    } else if (v_dia > 90) {
                        colrs_dia[a] = Color.RED;
                    }
                }



                int[] colrs_sys = new int[entries_sys.size()];
                float v_sys=0;

                for (int a = 0; a < entries_sys.size(); a++) {
                    v_sys =entries_sys.get(a).getY();
                    if (v_sys < 80) {
                        colrs_sys[a] = Color.parseColor("#f56138");
                    } else if (v_sys > 80 && v_sys <= 120) {
                        colrs_sys[a] = Color.parseColor("#2b971a");
                    } else if (v_sys > 120 && v_sys < 139) {
                        colrs_sys[a] = Color.parseColor("#733d07");
                    } else if (v_sys > 139) {
                        colrs_sys[a] = Color.parseColor("#f56138");
                    }


                }


                if (entries_weight.size() > 0) {
                    int[] colrs_weght = new int[entries_weight.size()];
                    if(sGoalWeight!=null) {
                        if (!sGoalWeight.equals("")) {



                            for (int a = 0; a < entries_weight.size(); a++) {
                                float v_weight = entries_weight.get(a).getY();

                                Float f_g_weight=Float.parseFloat(sGoalWeight);


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



                set_dia.setColors(colrs_dia);
                set_dia.setCircleColors(colrs_dia);
                set_sys.setColors(colrs_sys);
                set_sys.setCircleColors(colrs_sys);
                set_dia.setFillColor(mFillColor);
                set_dia.setDrawFilled(true);
                set_sys.setFillColor(Color.CYAN);
                set_sys.setDrawFilled(true);

                if(show_type.equals(SHOW_TYPE_BP))
                {
                    LineData d = new LineData(set_sys, set_dia);
                    mLineChart.setData(d);
                    mLineChart.invalidate();
                }else if(show_type.equals(SHOW_TYPE_WEIGHT))
                {
                    LineData d = new LineData(set_weight);
                    mLineChart.setData(d);
                    mLineChart.invalidate();
                }



            }

        }else
        {
            mLineChart.setData(null);
        }


        mLineChart.invalidate();
        getBitmapFromView(mLineChart,"L");
    }

    private void fill_bar_chart(String show_type)
    {
        mBarChart.setDrawBarShadow(false);
        mBarChart.invalidate();
        mBarChart.setDrawGridBackground(false);

        mBarChart.setBackgroundColor(Color.WHITE);
        mBarChart.setDescription("");
        mBarChart.getXAxis().setGranularity(1f);
        Legend l = mBarChart.getLegend();
        l.setEnabled(false);
        /*l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);*/

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinValue(40f);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(false);
        leftAxis.removeAllLimitLines();


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarChart.getAxisRight().setEnabled(false);


        mBarChart.setData(generateBarData(leftAxis,show_type));

        mBarChart.invalidate();
        getBitmapFromView(mBarChart,"B");
    }

    private void generateLineData(YAxis lAxis,String show_type ) {

        LimitLine limit_systolic=null,limit_Diastolic=null,limit_Dia_normal=null,
                limit_Sys_normal=null,
                limit_line_weight_goal=null;

        Cursor cursor_data;
        cursor_data = db_bp.getgoalBPMonitarData(Integer.parseInt(sMemberId),
                8);

        if ((cursor_data != null) || (cursor_data.getCount() > 0)) {

            if (cursor_data.moveToFirst()) {

                if (show_type.equals(SHOW_TYPE_BP)) {

                    limit_systolic = new LimitLine(cursor_data.getFloat(cursor_data.getColumnIndex(
                            "bp_goalsystolic")), "Systolic Goal");
                    limit_systolic.setLineWidth(4f);
                    limit_systolic.enableDashedLine(10f, 10f, 0f);

                    limit_systolic.setLineColor(Color.GREEN);
                    limit_systolic.setTextColor(Color.LTGRAY);

                    limit_Diastolic = new LimitLine(cursor_data.getFloat(cursor_data.getColumnIndex(
                            "bp_goaldiastolic")), "Diastolic Goal");
                    limit_Diastolic.setLineWidth(4f);
                    limit_Diastolic.enableDashedLine(10f, 10f, 0f);

                    limit_Diastolic.setLineColor(Color.GREEN);
                    limit_Diastolic.setTextColor(Color.LTGRAY);


                    limit_Dia_normal = new LimitLine(80, "Diastolic Normal");
                    limit_Dia_normal.setLineWidth(4f);
                    limit_Dia_normal.enableDashedLine(10f, 10f, 0f);
                    limit_Dia_normal.setLineColor(Color.BLUE);
                    limit_Dia_normal.setTextColor(Color.LTGRAY);

                    limit_Sys_normal = new LimitLine(110, "systolic Normal");
                    limit_Sys_normal.setLineWidth(4f);
                    limit_Sys_normal.setLineColor(Color.BLUE);
                    limit_Sys_normal.setTextColor(Color.LTGRAY);
                    limit_Sys_normal.enableDashedLine(10f, 10f, 0f);

                    lAxis.addLimitLine(limit_systolic);
                    lAxis.addLimitLine(limit_Diastolic);
                    lAxis.addLimitLine(limit_Dia_normal);
                    lAxis.addLimitLine(limit_Sys_normal);

                }else if(show_type.equals(SHOW_TYPE_WEIGHT))
                {

                    limit_line_weight_goal = new LimitLine(cursor_data.getFloat(cursor_data.getColumnIndex(
                            "bp_goalweight")), "Weight Goal");
                    limit_line_weight_goal.setLineWidth(4f);
                    limit_line_weight_goal.enableDashedLine(10f, 10f, 0f);

                    limit_line_weight_goal.setLineColor(Color.GREEN);
                    limit_line_weight_goal.setTextColor(Color.LTGRAY);
                    lAxis.addLimitLine(limit_line_weight_goal);


                }
            }
        }








    }

    private BarData generateBarData(YAxis lAxis,String show_type) {

        BarData d= null;
        BarDataSet set_sys=null,set_dia=null,set_wight=null;

        ArrayList<BarEntry> entries_sys = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries_dia = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries_weight = new ArrayList<BarEntry>();




        if ((crs_graph != null) && (crs_graph.getCount() > 0)) {
            if (crs_graph.moveToFirst()) {
                do {

                    try {
                        current_date = dateFormat_query.parse(crs_graph.getString(crs_graph.getColumnIndex("bp_date")));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    entries_sys.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)),crs_graph.getFloat(crs_graph.getColumnIndex("systolic"))));
                    entries_dia.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)),crs_graph.getFloat(crs_graph.getColumnIndex("diastolic"))));
                    entries_weight.add(new BarEntry(Float.parseFloat(format_single_date.format(current_date)),crs_graph.getFloat(crs_graph.getColumnIndex("weight"))));
                } while (crs_graph.moveToNext());
            }
        }

        if(entries_dia.size()>0) {


            int[]colrs_dia=new int[entries_dia.size()];

            for(int a=0;a<entries_dia.size();a++) {
                float v_dia = entries_dia.get(a).getY();
                if (v_dia < 50) {
                    colrs_dia[a] = Color.RED;
                } else if (v_dia > 50 && v_dia <= 80) {
                    colrs_dia[a] = Color.GREEN;
                } else if (v_dia > 80 && v_dia < 90) {
                    colrs_dia[a] = Color.rgb(255, 140, 0);
                } else if (v_dia > 90) {
                    colrs_dia[a] = Color.RED;
                }
            }
            set_dia = new BarDataSet(entries_dia, "Diastolic");

            set_dia.setColors(colrs_dia);
            set_dia.setAxisDependency(YAxis.AxisDependency.LEFT);

        }
        if(entries_sys.size()>0) {

            generateLineData(lAxis,show_type);

            int[] colrs_sys = new int[entries_sys.size()];
            float v_sys=0;

            for (int a = 0; a < entries_sys.size(); a++) {
                v_sys =entries_sys.get(a).getY();
                if (v_sys < 80) {
                    colrs_sys[a] = Color.parseColor("#f56138");
                } else if (v_sys > 80 && v_sys <= 120) {
                    colrs_sys[a] = Color.parseColor("#2b971a");
                } else if (v_sys > 120 && v_sys < 139) {
                    colrs_sys[a] = Color.parseColor("#733d07");
                } else if (v_sys > 139) {
                    colrs_sys[a] = Color.parseColor("#f56138");
                }


            }

            set_sys = new BarDataSet(entries_sys, "Systolic");
            set_sys.setColors(colrs_sys);
            set_sys.setAxisDependency(YAxis.AxisDependency.LEFT);


        }
        float barWidth = 0.4f;
        float groupSpace = 0.2f;
        float barSpace = 0.05f;
        if(show_type.equals(SHOW_TYPE_BP))
        {
            if(set_sys!=null &&set_dia!=null) {
                d = new BarData(set_sys, set_dia);



                d.setBarWidth(barWidth);
                // d.groupBars(0, groupSpace, barSpace);
            }

        }else if(show_type.equals(SHOW_TYPE_WEIGHT))
        {
            if(entries_weight!=null) {
                set_wight = new BarDataSet(entries_weight, "Weight");

                if (entries_weight.size() > 0) {
                    int[] colrs_weght = new int[entries_weight.size()];
                    if(sGoalWeight!=null) {
                        if (!sGoalWeight.equals("")) {



                            for (int a = 0; a < entries_weight.size(); a++) {
                                float v_weight = entries_weight.get(a).getY();

                                Float f_g_weight=Float.parseFloat(sGoalWeight);


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


    private void create_pdf(String filter)
    {
        float[]table_widhts=new float[]{5f, 5f, 5f,5f};

        float[]width6=new float[]{5f, 5f, 5f,5f,5f,5f};
        float[]width3=new float[]{5f, 5f, 5f,5f};

        int report_length=0;
        Chunk p3=null;
        String inserted_date="";

        Document doc = new Document(PageSize.A4);

        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);
        Font bfBold12_white = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);
        Font bfBold13 = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.BLACK);

        Font axis_desc_font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL, new BaseColor(0,121,182));

        Font bfBold13_white = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.WHITE);
        Font bfBold_graph = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.DARK_GRAY);

        Font h1 = new Font(Font.FontFamily.COURIER, 24, Font.NORMAL, BaseColor.BLACK);
        Font h1_blue = new Font(Font.FontFamily.COURIER, 24, Font.NORMAL, new BaseColor(0,121,182));
        Font h3 = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL, BaseColor.BLACK);

        Font bfBold14 = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.BLACK);
        Font bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.BLACK);
        Font paraFont = new Font(Font.FontFamily.COURIER,14, Font.NORMAL,BaseColor.BLACK);

        Font secparaFont = new Font(Font.FontFamily.UNDEFINED,12, Font.NORMAL, BaseColor.BLUE);
        Font dateparaFont = new Font(Font.FontFamily.UNDEFINED,12, Font.NORMAL, BaseColor.LIGHT_GRAY);

        Font fontlink = new Font(Font.FontFamily.COURIER,12, Font.NORMAL, BaseColor.DARK_GRAY);

        Font blankFont = new Font();

        DottedLineSeparator dottedline = new DottedLineSeparator();
        dottedline.setOffset(-2);
        dottedline.setGap(1f);
        dottedline.setLineColor(BaseColor.DARK_GRAY);

        String myName="",Age="",weight="",goal_sys="",goal_dia="",goal_weight="",gender="";

        try {

            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher);
            bitmap.compress(Bitmap.CompressFormat.PNG, 82, stream2);
            Image myImg = Image.getInstance(stream2.toByteArray());
            myImg.setAlignment(Image.ALIGN_LEFT);
            myImg.scaleAbsolute(25, 25);



            // case CON_Type_Last10:



            if (filter.equals(CON_Type_LastReading)) {
                report_length=-1;
                p3 = new Chunk(" Last reading Report" , h3);
            }else   if (filter.equals(CON_Type_Weekly)) {
                report_length=-7;
                p3 = new Chunk(" Report from " +format_date.format(date_addDays(Todate,report_length)) + " to " + format_date.format(Todate) , h3);
            }
            else   if (filter.equals(CON_Type_Last10)) {
                report_length=-7;
                p3 = new Chunk(" Last 10 reading Report" , h3);
            }else  if (filter.equals(CON_Type_Monthly)) {
                report_length=-30;
                p3 = new Chunk(" Report from " +format_date.format(date_addDays(Todate,report_length)) + " to " + format_date.format(Todate) , h3);

            }else  if (filter.equals(CON_Type_Yearly)) {
                report_length=-30;
                p3 = new Chunk(" Report from " +format_date.format(date_addDays(Todate,-365)) + " to " + format_date.format(Todate) , h3);


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


            file = new File(dir, "RxMedikart_blood_pressure_report.pdf");
            FileOutputStream fOut = new FileOutputStream(file);



            writer= PdfWriter.getInstance(doc, fOut);

            writer.setBoxSize("art", rect);
            writer.setPageEvent(new HeaderAndFooter());
            doc.open();


            //  doc.setMargins(50, 45, 50, 60);
            doc.setMarginMirroring(false);




           // Paragraph p0 = new Paragraph();

            Chunk cp0 = new Chunk();
           // p0.add(cp0);

          //  cp0 = new Chunk("Blood Pressure Monitor",h1_blue);
          //  p0.add(cp0);

           // cp0 = new Chunk("     ",paraFont);
          //  p0.add(cp0);

           // cp0 = new Chunk("www.rxmedikart.com", fontlink).setAnchor("http://www.rxmedikart.com");
          //  p0.add(cp0);

           // p0.setAlignment(Paragraph.ALIGN_LEFT);
           // p0.setSpacingAfter(5);
          //  doc.add(p0);

            PdfPTable table_head = new PdfPTable(1);
            PdfPCell cell_head =  new PdfPCell();

            table_head.setWidthPercentage(100);
            cp0 = new Chunk("www.rxmedikart.com", fontlink).setAnchor("http://www.rxmedikart.com");
           // cp0 = new Chunk("www.rxmedikart.com", fontlink).setAnchor("http://www.rxmedikart.com");
            insertCellwithBorder(table_head,null, "Blood Pressure Monitor", "row", null, Element.ALIGN_RIGHT, 1, h1_blue, true, 0, 0, 0, 0,null);
            insertCellwithBorder(table_head,cp0, "", "row", null, Element.ALIGN_RIGHT, 1, h1_blue, true, 0, 0, 0, 0,null);
            table_head.setSpacingAfter(5);



            doc.add(table_head);



            doc.add(dottedline);
            doc.add( Chunk.NEWLINE );


           // Paragraph p2 = new Paragraph(" Report Of Blood Pressure ",secparaFont);
          //  p2.setAlignment(Paragraph.ALIGN_CENTER);
          //  p2.setSpacingBefore(0);


            PdfPTable table_date_rage = new PdfPTable(1);
            table_date_rage.setHorizontalAlignment(Element.ALIGN_CENTER);
            insertCellwithBorder(table_date_rage,p3, "", "", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.001f, 0.001f,0.001f,0.001f,null);
            table_date_rage.setSpacingAfter(10);
            table_date_rage.setSpacingBefore(10);

            doc.add(table_date_rage);

            //doc.add(p2);
/*

            doc.add( Chunk.NEWLINE );

            Chunk chunk = new Chunk("www.rxmedikart.com", fontlink)
                    .setAnchor("http://www.rxmedikart.com");
            Paragraph plink = new Paragraph(chunk);

            plink.setAlignment(Paragraph.ALIGN_RIGHT);
            plink.setSpacingAfter(5);
            doc.add(plink);
            doc.add(dottedline);
*/

            //data function
            Cursor goal_cursor = db_bp.getgoalBPMonitarData(Integer.parseInt(sMemberId), getSelectedRelationshipId);

            if ((goal_cursor != null) && (goal_cursor.getCount() > 0)) {
                if (goal_cursor.moveToFirst()) {
                    do {

                        if(setting_weight_unit.equals("kg")) {

                            goal_weight= goal_cursor.getString(goal_cursor.getColumnIndex("bp_kg"));
                        }else
                        {

                            goal_weight= goal_cursor.getString(goal_cursor.getColumnIndex("bp_lb"));
                        }

                        goal_sys= goal_cursor.getString(goal_cursor.getColumnIndex("bp_goalsystolic"));
                        goal_dia= goal_cursor.getString(goal_cursor.getColumnIndex("bp_goaldiastolic"));

                    } while (goal_cursor.moveToNext());

                }else {
                    //insertCell(table, "No Entries", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                    goal_sys= "No Entries";
                    goal_dia= "No Entries";
                    goal_weight= "No Entries";

                }
            }else {
                // insertCell(table, "No Entries", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                goal_sys= "No Entries";
                goal_dia= "No Entries";
                goal_weight= "No Entries";
            }
            //
            PdfPTable table1 = new PdfPTable(4);
            table1.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table1.setSpacingBefore(10f);
            table1.setSpacingAfter(0f);
            table1.setWidthPercentage(100);
            table1.setWidths(width3);


            insertCellwithBorder(table1,null, " ", "r", null, Element.ALIGN_CENTER, 4, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));

            insertCellwithBorder(table1,null, "Name : ", "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.1f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null, myName + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);


          //  insertCellwithBorder(table1 ,null, "Name : "+myName+"","row", null, Element.ALIGN_LEFT, 3, bfBold13,true,0.01f,0.01f,0.01f,0.01f,null);

            insertCellwithBorder(table1,null, "Gender : " , "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null, gender + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);

            insertCellwithBorder(table1,null, "Email : ", "r", null, Element.ALIGN_CENTER, 1, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null,  email + "", "r", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.01f, 0.01f, 0.01f, 0.01f,BaseColor.WHITE);


            // insertCellwithBorder(table1, "Gender : "+gender+"","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);

            insertCellwithBorder(table1,null, "Goal Systolic : ","r", null, Element.ALIGN_CENTER, 1, bfBold12,true,0.01f,0.01f,0.01f,0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null, goal_sys+"","r", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f,BaseColor.WHITE);

            insertCellwithBorder(table1,null, "Goal Diastolic : ","r", null, Element.ALIGN_CENTER, 1, bfBold12,true,0.01f,0.01f,0.01f,0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null, goal_dia+"","r", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f,BaseColor.WHITE);

            insertCellwithBorder(table1,null, "Goal Weight("+setting_weight_unit+") : ","r", null, Element.ALIGN_CENTER, 1, bfBold12,true,0.01f,0.01f,0.01f,0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null, goal_weight+"","r", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f,BaseColor.WHITE);

            doc.add(table1);

            //
            doc.add( Chunk.NEWLINE );

            Paragraph ps = new Paragraph();
            Chunk cp2 = new Chunk("Statistic",h3);
            ps.add(cp2);

            ps.setAlignment(Paragraph.ALIGN_LEFT);
            doc.add(ps);
            //

            //data function
            Cursor static_cursor = db_bp.show_BPMinMaxAnalysisFilterData_Static(Integer.parseInt(sMemberId), getSelectedRelationshipId,
                    filter,sAMPMFilter, dateFormat_query.format(Todate));

            String max_weight,min_weight,avg_weight;
            String max_systolic,min_systolic,avg_systolic;
            String max_diastolic,min_diastolic,avg_diastolic;


            if ((static_cursor != null) && (static_cursor.getCount() > 0)) {
                if (static_cursor.moveToFirst()) {
                    do {


                        if(setting_weight_unit.equals("kg")) {

                            max_weight= static_cursor.getString(static_cursor.getColumnIndex("max_weight_kg"));
                            min_weight= static_cursor.getString(static_cursor.getColumnIndex("min_weight_kg"));
                            avg_weight= static_cursor.getString(static_cursor.getColumnIndex("avg_weight_kg"));

                        }else
                        {

                            max_weight= static_cursor.getString(static_cursor.getColumnIndex("avg_weight_lb"));
                            min_weight= static_cursor.getString(static_cursor.getColumnIndex("avg_weight_lb"));
                            avg_weight= static_cursor.getString(static_cursor.getColumnIndex("avg_weight_lb"));

                        }

                        max_systolic= static_cursor.getString(static_cursor.getColumnIndex("max_systolic"));
                        min_systolic= static_cursor.getString(static_cursor.getColumnIndex("min_systolic"));
                        avg_systolic= static_cursor.getString(static_cursor.getColumnIndex("avg_systolic"));

                        max_diastolic= static_cursor.getString(static_cursor.getColumnIndex("max_diastolic"));
                        min_diastolic= static_cursor.getString(static_cursor.getColumnIndex("min_diastolic"));
                        avg_diastolic= static_cursor.getString(static_cursor.getColumnIndex("avg_diastolic"));


                    } while (static_cursor.moveToNext());

                }else {
                    //insertCell(table, "No Entries", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                    max_systolic= "No Entries";
                    min_systolic= "No Entries";
                    avg_systolic= "No Entries";

                    max_diastolic= "No Entries";
                    min_diastolic= "No Entries";
                    avg_diastolic= "No Entries";

                    max_weight= "No Entries";
                    min_weight= "No Entries";
                    avg_weight= "No Entries";

                }
            }else {
                // insertCell(table, "No Entries", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                max_systolic= "No Entries";
                min_systolic= "No Entries";
                avg_systolic= "No Entries";

                max_diastolic= "No Entries";
                min_diastolic= "No Entries";
                avg_diastolic= "No Entries";

                max_weight= "No Entries";
                min_weight= "No Entries";
                avg_weight= "No Entries";
            }


            float[]width_graph=new float[]{3f, 5f};

            PdfPTable table_graph = new PdfPTable(2);
            table_graph.setWidths(width_graph);
            table_graph.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table_graph.setSpacingBefore(10f);
            table_graph.setSpacingAfter(0f);
            table_graph.setWidthPercentage(100);

            insertCellwithBorder(table_graph,null, " ","header", null, Element.ALIGN_CENTER, 2, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);

            insertCellwithBorder(table_graph,null, "","", GraphImage, Element.ALIGN_LEFT, 1, bfBold13_white,true,0.01f,0.01f,0.0f,0.01f,null);


            Paragraph p_desc = new Paragraph();


            p_desc.add(new Paragraph(String.format( "%.2f", low_bp_percent)+"%  Low.",bfBold_graph));
            p_desc.add(new Paragraph(String.format( "%.2f", normal_value_percent)+"%  Normal.",bfBold_graph));

            p_desc.add(new Paragraph(String.format( "%.2f", Prehypertension_percent)+"% Prehypertension.",bfBold_graph));

            p_desc.add(new Paragraph(String.format( "%.2f", Hypertension_stage1_percent)+"% Hypertension Stage1.",bfBold_graph));
            p_desc.add(new Paragraph(String.format( "%.2f", Hypertension_stage2_percent)+"% Hypertension Stage2.",bfBold_graph));
            p_desc.add(new Paragraph(String.format( "%.2f", hypertensive_crisis_percent)+"% hypertensive Crisis.",bfBold_graph));





            PdfPCell cell = new PdfPCell();
            cell.addElement(p_desc);
            cell.setColspan(1);
            cell.setBorderWidthLeft(0);
            cell.setPaddingTop(20);
            cell.setBorderColor(new BaseColor(0,121,182));

            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table_graph.addCell(cell);


            doc.add(table_graph);



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


            //

            Paragraph p_analys = new Paragraph();
            Chunk cp_analys = new Chunk("Readings Analysis",h3);
            p_analys.add(cp_analys);
            p_analys.setSpacingBefore(15);
            p_analys.setAlignment(Paragraph.ALIGN_LEFT);
            doc.add(p_analys);



            PdfPTable table2 = new PdfPTable(4);
            table2.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table2.setSpacingBefore(10f);
            table2.setSpacingAfter(0f);
            table2.setWidthPercentage(100);
            table2.setWidths(table_widhts);

            insertCellwithBorder(table2,null, "","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);
            insertCellwithBorder(table2,null, "Maximum","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);
            // insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, "Minimum","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);
            // insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, "Average","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);

            insertCellwithBorder(table2,null, "Systolic","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0,0,0,null);
            insertCellwithBorder(table2,null, max_systolic,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0,0,null);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, min_systolic,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0,0,null);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, avg_systolic,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0.01f,0,null);

            insertCellwithBorder(table2,null, "Diastolic","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0,0,0,null);
            insertCellwithBorder(table2,null, max_diastolic,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0,0,null);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, min_diastolic,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0,0,null);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, avg_diastolic,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0.01f,0,null);

            insertCellwithBorder(table2,null, "Weight ("+setting_weight_unit+")","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0,0,0.01f,null);
            insertCellwithBorder(table2,null, max_weight,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0,0.01f,null);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, min_weight,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0,0.01f,null);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, avg_weight,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0.01f,0.01f,null);



            doc.add(table2);
            //

            Paragraph p_log = new Paragraph();
            Chunk cp_log = new Chunk("Readings Report",h3);
            p_log.add(cp_log);
            p_log.setSpacingBefore(15);
            p_log.setAlignment(Paragraph.ALIGN_LEFT);
            doc.add(p_log);



            Cursor pdf_cursor = db_bp.show_BPMinMaxAnalysisFilterData_graph(Integer.parseInt(sMemberId), getSelectedRelationshipId,
                    filter,sAMPMFilter, dateFormat_query.format(Todate));
//show_BPMinMaxAnalysisFilterData_Static

           // p2.setAlignment(Paragraph.ALIGN_LEFT);
           // doc.add(p3);


            PdfPTable table = new PdfPTable(4);
            table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(0f);
            table.setWidthPercentage(100);
            table.setWidths(table_widhts);

            insertCell(table, "Blood Pressure levels","header", null, Element.ALIGN_CENTER, 4, bfBold12_white,false);

            insertCell(table, "Date","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,false);
            insertCell(table, "Time","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,false);
            insertCell(table, "Sys/Dia","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,false);
            insertCell(table, "Pulse","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,false);






            if ((pdf_cursor != null) && (pdf_cursor.getCount() > 0)) {
                if (pdf_cursor.moveToFirst()) {
                    do {

                        // Integer i_glucose=Integer.parseInt(pdf_cursor.getString(pdf_cursor.getColumnIndex("g_value")));

                       /* if(i_glucose<70)
                        {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
                        }else  if(i_glucose>70&&i_glucose<110)
                        {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.GREEN);
                        }else  if(i_glucose>110&&i_glucose<150)
                        {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.ORANGE);
                        }else  if(i_glucose>150)
                        {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
                        }*/




                        if(!inserted_date.equals(pdf_cursor.getString(pdf_cursor.getColumnIndex("bp_date"))))
                        {
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("bp_date")),"row", null, Element.ALIGN_CENTER, 1, bfBold14,false);
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("bp_time")),"row", null, Element.ALIGN_CENTER, 1, bfBold14,false);
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("systolic"))+"/"+pdf_cursor.getString(pdf_cursor.getColumnIndex("diastolic")),"row", null, Element.ALIGN_CENTER, 1, get_color_coding_on_values(pdf_cursor.getInt(pdf_cursor.getColumnIndex("systolic")),pdf_cursor.getInt(pdf_cursor.getColumnIndex("diastolic"))),false);
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("pulse")),"row", null, Element.ALIGN_CENTER, 1, bfBold14,false);


                            inserted_date= pdf_cursor.getString(pdf_cursor.getColumnIndex("bp_date"));
                        }else
                        {
                            insertCell(table, "","row", null, Element.ALIGN_CENTER, 1, bfBold14,false);
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("bp_time")),"row", null, Element.ALIGN_CENTER, 1, bfBold14,false);
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("systolic"))+"/"+pdf_cursor.getString(pdf_cursor.getColumnIndex("diastolic")),"row", null, Element.ALIGN_CENTER, 1,  get_color_coding_on_values(pdf_cursor.getInt(pdf_cursor.getColumnIndex("systolic")),pdf_cursor.getInt(pdf_cursor.getColumnIndex("diastolic"))),false);
                            insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("pulse")),"row", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                            inserted_date= pdf_cursor.getString(pdf_cursor.getColumnIndex("bp_date"));
                        }












                    } while (pdf_cursor.moveToNext());

                }else {
                    insertCell(table, "No Entries","row", null, Element.ALIGN_CENTER, 1, bfBold14,false);
                }
            }else {
                insertCell(table, "No Entries","row", null, Element.ALIGN_CENTER, 1, bfBold14,false);
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


    private void insertCell(PdfPTable table, String text,String type,Image img, int align, int colspan, Font font,Boolean Border){



        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(new BaseColor(0,121,182));
        cell.setPadding(2);
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        if(Border)
        {
            cell.setBorderWidth(0);
        }

        //row style
        if(type=="header")
        {
            Style.headerCellStyle(cell);
        }else
        if(type=="row")
        {
            Style.rowStyle(cell);
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



            try
            {



                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap1 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.logo_rx);
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image background = Image.getInstance(stream.toByteArray());

                background.scaleAbsolute(20f,50f);

                Rectangle rect = writer.getBoxSize("art");
              //  ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase(format_date_for_pdf.format(Calendar.getInstance().getTime())), rect.getRight(), rect.getBottom()+6, 0);


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


            //    ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase("Page "+writer.getPageNumber()), rect.getRight(), rect.getBottom()-5, 0);

                // ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase("____________________________________________________________"), ((document.left() + document.right())/2)+1f , document.bottom(), 0);
                // ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(String.format("www.rxmedikart.com")), (document.left() + document.right())/2 , document.bottom()-30, 0);
                // ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_BOTTOM, new Phrase("www.rxmedikart.com"), document.right(), document.bottom(), 0);

                if(writer.getPageNumber()==1) {

                    writer.getDirectContentUnder().addImage(background, 180, 0, 0, 30, 30, 765);
                }
               // writer.getDirectContentUnder().addImage(background, 200, 0, 0,  48, 30, 20);
            }
            catch (BadElementException e)
            {
                e.printStackTrace();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (DocumentException e)
            {
                e.printStackTrace();
            }



        }

    }
    private void send_report_to_doctor(String Path) {
        Intent intent_report = new Intent(this, MRA_SendReprtToDoctor.class);
        intent_report.putExtra("path",Path);
        intent_report.putExtra("Email_heading","B.P Monitor report");
        startActivity(intent_report);

    }

    public void create_statistics_pie_chart(String Filter_condition) {


        float total_count=0;
        float Low_bp=0;
        float Normal_bp=0;
        float Prehypertension=0, Hypertension_stage1=0, Hypertension_stage2=0, hypertensive_crisis=0;


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

        if(mpiechart_normal!=null)
        {
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
            mPiechart_statistics.setDrawEntryLabels(false);
        }
        // mPiechart.spin(2000, 0, 360);

        String[] mParties ;
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        Cursor cursor_pdf_graph_values = db_bp.show_analysisi_for_pdf_graph(Integer.parseInt(sMemberId), getSelectedRelationshipId, Filter_condition, sAMPMFilter, dateFormat_query.format(Todate));


        if (cursor_pdf_graph_values != null) {
            if (cursor_pdf_graph_values.moveToFirst()) {
                do {
                    total_count=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("total_count"));
                    Low_bp=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("Low_bp"));
                    Normal_bp=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("Normal_bp"));
                    Prehypertension=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("Prehypertension"));
                    Hypertension_stage1=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("Hypertension_stage1"));
                    Hypertension_stage2=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("Hypertension_stage2"));
                    hypertensive_crisis=  cursor_pdf_graph_values.getFloat(cursor_pdf_graph_values.getColumnIndex("hypertensive_crisis"));

                } while (cursor_pdf_graph_values.moveToNext());

            }
        }



        low_bp_percent=(Low_bp/total_count)*100;
        normal_value_percent=(Normal_bp/total_count)*100;
        Prehypertension_percent=(Prehypertension/total_count)*100;
        Hypertension_stage1_percent=(Hypertension_stage1/total_count)*100;
        Hypertension_stage2_percent=(Hypertension_stage2/total_count)*100;
        hypertensive_crisis_percent=(hypertensive_crisis/total_count)*100;

        if(low_bp_percent>0) {
            yVals1.add(new PieEntry((low_bp_percent),"Low"));
        }


        if(normal_value_percent>0) {
            yVals1.add(new PieEntry((normal_value_percent),"Normal"));
        }

        if(Prehypertension_percent>0) {
            yVals1.add(new PieEntry((Prehypertension_percent),"Prehypertension"));
        }
        if(Hypertension_stage1_percent>0) {
            yVals1.add(new PieEntry((Hypertension_stage1_percent),"Hypertension Stage1"));
        }
        if(Hypertension_stage2_percent>0) {
            yVals1.add(new PieEntry((Hypertension_stage2_percent),"Hypertension Stage2"));
        }

        if(hypertensive_crisis_percent>0) {
            yVals1.add(new PieEntry((hypertensive_crisis_percent),"Hypertensive Crisis Stage1"));
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
        mPiechart_statistics.invalidate();

        if(mpiechart_normal!=null)
        {
            mpiechart_normal.setData(data);
            mpiechart_normal.highlightValues(null);
            mpiechart_normal.animateY(500, Easing.EasingOption.EaseInOutQuad);
            mpiechart_normal.invalidate();

        }
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


               // returnedBitmap = Bitmap.createBitmap(600, 400, Bitmap.Config.RGB_565);
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


        Cursor cursor_pdf_graph_values = db_bp.show_weight_analysis_data(Integer.parseInt(sMemberId), getSelectedRelationshipId, Filter_condition, sAMPMFilter, dateFormat_query.format(Todate));


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
        mPiechart_statistics.invalidate();

        if(mpiechart_normal!=null) {
            mpiechart_normal.setData(data);
            mpiechart_normal.highlightValues(null);
            mpiechart_normal.animateY(500, Easing.EasingOption.EaseInOutQuad);
            mpiechart_normal.invalidate();
        }




    }
   Font get_color_coding_on_values(Integer Systolic,Integer Diastolic) {

       Font bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.BLACK);

       if (Systolic <= 90 || Diastolic <= 60) {
           bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
       }
       else if ((Systolic >= 91 && Systolic <= 120) || (Diastolic >= 61 && Diastolic < 80)) {
           bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.GREEN);
       } else if ((Systolic >= 120 && Systolic <= 139) || (Diastolic >= 80 && Diastolic <= 89)) {
           bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.ORANGE);
       } else if ((Systolic >= 140 && Systolic <= 159) || (Diastolic >= 90 && Diastolic <= 99)) {
           bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
       } else if (Systolic >= 160 || Diastolic >= 100) {
           bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
       } else if (Systolic >= 180 || Diastolic >= 110) {
           bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
       }
       return bfBoldRangeClor;
   }
}

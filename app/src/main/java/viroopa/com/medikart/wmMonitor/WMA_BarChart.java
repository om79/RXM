
package viroopa.com.medikart.wmMonitor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.progresspageindicator.ProgressPageIndicator;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.awt.geom.Rectangle2D;
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
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;


import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import viroopa.com.medikart.MedicineReminder.MRA_SendReprtToDoctor;
import viroopa.com.medikart.R;
import viroopa.com.medikart.SplashScreenActivity;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.app.ConnectionDetector;
import viroopa.com.medikart.app.messagebox;
import viroopa.com.medikart.common.DayAxisValueFormatter;
import viroopa.com.medikart.common.DayWithMonthAxisValueFormatter;
import viroopa.com.medikart.common.MyMarkerView;
import viroopa.com.medikart.common.Style;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SessionManager;
import viroopa.com.medikart.helper.SqliteWMHandler;

import static viroopa.com.medikart.R.id.rect;

public class WMA_BarChart extends AppCompatActivity {


    private TextView firstdate, seconddate;
    private ImageView btnprevious, btncancel;
    private SqliteWMHandler db_water;
    AppController globalVariable;
    Integer relationShipId=8;
    String toDate, StartDate, EndaDate;
    private SimpleDateFormat format_date_for_pdf = new SimpleDateFormat("LLLL dd,yyyy hh:mm" );
    private  ImageView viewpager_previous,viewpager_next;
    private LineChart mLineChart;
    private BarChart mBarChart;
    DateFormat format_single_date = new SimpleDateFormat("dd");
    private PieChart mPiechart;
    private PieChart mPiechart_statistics;
    private ViewPager vp;
    int cursor_count=0;
    Rectangle rect = new Rectangle(30, 30, 550, 800);
    private Animation text_animation;
    private TextView MainMessage;
    SQLiteHandler db;
    File dir,file;
    private  Image GraphImage,Graph_line,Graph_bar;
    PdfWriter writer;
    private LinearLayout line_chart_below,axis_desc;
    private ProgressPageIndicator pagerIndicator;
    private String ChartType = "0";
    private Integer Water_goal;
    DateFormat OnlydateFormat_query = new SimpleDateFormat("dd");

    float normal_value_percent=0;
    float not_achieved_value_percent=0;
    float overdrinking_value_percent=0;

   private TextView txt_axis_indicator;
    Date Today_date = Calendar.getInstance().getTime();
    Date current_date = Calendar.getInstance().getTime();
    Date graph_format_date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("LLL dd, cccc");
    DateFormat dateFormat_query = new SimpleDateFormat("yyyy-MM-dd");

    private float weekly_drunk=0,weekly_pending=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_bar_chart_main);
        globalVariable = (AppController) getApplicationContext();
        text_animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_dialog);
        db =new SQLiteHandler(this);
        db_water=new SqliteWMHandler(getApplicationContext());
        firstdate = (TextView) findViewById(R.id.firstdate);
        seconddate = (TextView) findViewById(R.id.seconddate);
        btnprevious = (ImageView) findViewById(R.id.btnprevious);
        btncancel = (ImageView) findViewById(R.id.btncancel);
        MainMessage = (TextView) findViewById(R.id.MainMessage);
        line_chart_below= (LinearLayout) findViewById(R.id.line_chart_below);
        axis_desc= (LinearLayout) findViewById(R.id.axis_desc);
        mPiechart_statistics= (PieChart) findViewById(R.id.piechart_pdf);
        txt_axis_indicator= (TextView) findViewById(R.id.txt_axis_indicator);
        viewpager_previous= (ImageView) findViewById(R.id.viewpager_previous);
        viewpager_next= (ImageView) findViewById(R.id.viewpager_next);
        btncancel.setVisibility(View.GONE);


        if(globalVariable.getRealationshipId()!=null)
        {
            relationShipId=Integer.parseInt(globalVariable.getRealationshipId());
        }else
        {
            relationShipId=8;
        }


        try {
            toDate = dateFormat_query.format(current_date);
            // current_date = dateFormat_query.parse(toDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String MaxId=db_water.getMAxIdWMSetting(relationShipId);
        if(MaxId!=null) {
            Water_goal = db_water.getLastSetGoal(relationShipId,toDate);

        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);





        EndaDate = dateFormat_query.format(current_date);
        seconddate.setText(dateFormat.format(current_date));
        current_date = date_addDays(current_date, -6);
        StartDate = dateFormat_query.format(current_date);

        firstdate.setText(dateFormat.format(current_date));

        set_drink_message_on_percent(StartDate, EndaDate);

        create_statistics_pie_chart(StartDate, EndaDate);
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

                        }
                    }, 500);

                }
            }
        });


        btnprevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                btncancel.setVisibility(View.VISIBLE);
                seconddate.setText(dateFormat.format(current_date));
                EndaDate = dateFormat_query.format(current_date);
                current_date = date_addDays(current_date, -6);
                StartDate = dateFormat_query.format(current_date);
                firstdate.setText(dateFormat.format(current_date));
                create_statistics_pie_chart(StartDate, EndaDate);
                fill_Linechart_data(StartDate, EndaDate);
                fill_Bar_chart_data(StartDate, EndaDate);
                fill_pie_chart_data(StartDate, EndaDate);
                set_drink_message_on_percent(StartDate, EndaDate);
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                firstdate.setText(dateFormat.format(current_date));
                StartDate = dateFormat_query.format(current_date);
                current_date = date_addDays(current_date, 6);
                EndaDate = dateFormat_query.format(current_date);
                seconddate.setText(dateFormat.format(current_date));
                set_drink_message_on_percent(StartDate, EndaDate);

                create_statistics_pie_chart(StartDate, EndaDate);
                fill_Linechart_data(StartDate, EndaDate);
                fill_Bar_chart_data(StartDate, EndaDate);
                fill_pie_chart_data(StartDate, EndaDate);

                if(dateFormat_query.format(current_date).equals(dateFormat_query.format(Today_date)))
                {
                    btncancel.setVisibility(View.GONE);
                }else
                {
                    btncancel.setVisibility(View.VISIBLE);
                }
            }
        });

        vp = (ViewPager) findViewById(R.id.viewPager);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(new MyPagesAdapter());


        viewpager_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current_item= vp.getCurrentItem();
                vp.setCurrentItem(getItem(+1), true);
            }
        });
             //   viewpager_next= (I


        pagerIndicator = (ProgressPageIndicator) findViewById(R.id.pageIndicator);
        pagerIndicator.setStrokeColor(Color.parseColor("#f38630"));

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

                pagerIndicator.setViewPager(vp);
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pagerIndicator.setViewPager(vp);
            }

            public void onPageSelected(int position) {


                pagerIndicator.setViewPager(vp);
                ChartType = String.valueOf(position);

                if(ChartType.equals("2"))
                {
                    line_chart_below.setVisibility(View.INVISIBLE);
                    axis_desc.setVisibility(View.INVISIBLE);
                }else{ line_chart_below.setVisibility(View.VISIBLE);
                    axis_desc.setVisibility(View.VISIBLE);;}

            }
        });


        findViewById(R.id.viewpager_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage = vp.getCurrentItem();
                int totalPages = vp.getAdapter().getCount();

                int previousPage = currentPage-1;
                if (previousPage < 0) {
                    // We can't go back anymore.
                    // Loop to the last page. If you don't want looping just
                    // return here.
                    previousPage = totalPages - 1;
                }

                vp.setCurrentItem(previousPage, true);
            }
        });

        findViewById(R.id.viewpager_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage = vp.getCurrentItem();
                int totalPages = vp.getAdapter().getCount();

                int nextPage = currentPage+1;
                if (nextPage >= totalPages) {
                    nextPage = 0;
                }

                vp.setCurrentItem(nextPage, true);
            }
        });


    }
    private int getItem(int i) {
        return vp.getCurrentItem() + i;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waterbarchart, menu);
        LayoutInflater mInflater = LayoutInflater.from(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sendreport) {
            if (cursor_count>0) {
                create_pdf(StartDate, EndaDate);
            }else {

                Toast.makeText(this,
                        "No data for the selected date range",
                        Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == R.id.action_member) {
            //  meber_show();
            Intent intent_setting = new Intent(WMA_BarChart.this, WMA_Settings
                    .class);
            startActivity(intent_setting);
            return true;
        }
        if (id == R.id.action_settings) {

            Intent intent_setting = new Intent(WMA_BarChart.this, WMA_Settings
                    .class);
            startActivity(intent_setting);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Date date_addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
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
            TextView xaxis_label=null;
            switch (position) {
                case 0:
                    resId = R.layout.dm_monitor_linechart;
                    view = inflater.inflate(resId, null);
                    mLineChart = (LineChart) view.findViewById(R.id.Linechart);
                    xaxis_label=(TextView) view.findViewById(R.id.xaxis_label);
                 /*   xaxis_label.setVisibility(View.VISIBLE);
                    mLineChart.setDescription("Days");
                    xaxis_label.setText("water(ml)");*/
                    fill_Linechart_data(StartDate,EndaDate);


                    break;
                case 1:
                    resId = R.layout.dm_monitor_barchart;
                    view = inflater.inflate(resId, null);
                    mBarChart = (BarChart) view.findViewById(R.id.barchart);

                   /*  xaxis_label=(TextView) view.findViewById(R.id.xaxis_label);
                    xaxis_label.setVisibility(View.VISIBLE);
                    mLineChart.setDescription("Days");
                    xaxis_label.setText("Water Amount in ml");*/
                    fill_Bar_chart_data(StartDate,EndaDate);



                    break;
                case 2:
                    resId = R.layout.dm_monitor_piechart;
                    view = inflater.inflate(resId, null);
                    mPiechart = (PieChart) view.findViewById(R.id.piechart);
                    fill_pie_chart_data( StartDate,EndaDate);
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




    private  void fill_Linechart_data(String sfromdate ,String stodate)
    {


        mLineChart.setDrawGridBackground(false);

        // no description text
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("");

        // enable value highlighting
        mLineChart.setBackgroundColor(Color.WHITE);
        //mLineChart.setDescription();
        // enable touch gestures
        mLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(true);


        XAxis xAxis = mLineChart.getXAxis();




        YAxis RIGHTAxis = mLineChart.getAxisRight();
        RIGHTAxis.removeAllLimitLines();
        //  RIGHTAxis.setAxisMinValue(0f);
       // RIGHTAxis.setStartAtZero(true);
        RIGHTAxis.setSpaceTop(10f);

        mLineChart.getAxisLeft().removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        // mLineChart.getAxisLeft().setAxisMinValue(100f);
        mLineChart.getAxisLeft().setSpaceTop(10f);


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // limit lines are drawn behind data (and not on top)

        mLineChart.getAxisRight().setDrawGridLines(false);

        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getLegend().setEnabled(false);
        //mLineChart.setPinchZoom(false);
        xAxis.setGranularity(1f);
      //  mLineChart.getAxisLeft().setGranularity(50f);

        AxisValueFormatter xAxisFormatter = new DayWithMonthAxisValueFormatter(mLineChart);
        mLineChart.getXAxis().setValueFormatter(null);




        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.getAxisRight().setEnabled(false);
      /*  mLineChart.getAxisRight().removeAllLimitLines();
        mLineChart.getAxisLeft().removeAllLimitLines();
        mLineChart.getXAxis().removeAllLimitLines();
*/

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
     /*   mv.set(mBarChart); // For bounds control*/
        mLineChart.setMarkerView(mv);

        ArrayList<Entry> values = new ArrayList<Entry>();



        Cursor cursor_Chart;

        cursor_Chart = db_water.getAllData_Chart(relationShipId.toString(), sfromdate,stodate);

        cursor_count=cursor_Chart.getCount();
        cursor_Chart.moveToFirst();
        float f1=0;
        while (!cursor_Chart.isAfterLast()) {
            if(cursor_Chart.getString(cursor_Chart.getColumnIndex("maxqty"))!=null) {

                try {
                    graph_format_date = dateFormat_query.parse(cursor_Chart.getString(cursor_Chart.getColumnIndex("created_date")));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                values.add(new Entry(Float.parseFloat(format_single_date.format(graph_format_date)),cursor_Chart.getFloat(cursor_Chart.getColumnIndex("maxqty"))));
            }

            cursor_Chart.moveToNext();
        }
        if(values.size()>0){
            mLineChart.getXAxis().setAxisMinValue(values.get(0).getX()-1);
        }

        LineDataSet  set1 = new LineDataSet(values, "Date");
        //set1.setValueFormatter(new MyValueFormatter());

        set1.setLabel("Date");

        set1.setLineWidth(2f);
        set1.setCircleSize(7f);
        set1.setDrawCircleHole(true);
        set1.setValueTextSize(9f);
        set1.setDrawValues(true);
        set1.setColor(Color.CYAN);



      //  set1.setFillAlpha(65);
        //set1.setFillColor(Color.BLACK);
        // set1.setHighLightColor(Color.rgb(255, 165, 0));










        cursor_Chart.moveToFirst();





        int[]colrs=new int[values.size()];


        // colrs[2]=Color.GREEN;

        for(int a=0;a<values.size();a++)
        {
            float xy=values.get(a).getY();
            float m=(xy/Water_goal)*100;

            if(m<35)
            {
                colrs[a]=Color.RED;
                // set1.setCircleColors(colrs);
            }else if(m<=80){
                colrs[a]=Color.rgb(255, 140, 0);
                //  set1.setCircleColor(Color.YELLOW);
            }else if(m>80 && m<=124){
                colrs[a]=Color.GREEN;
                // set1.setCircleColor(Color.GREEN);
            }else if(m>125){
                colrs[a]=Color.RED;
            }

        }
        set1.setCircleColors(colrs);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);
        if(dataSets.size()>0) {
            LineData data = new LineData(dataSets);
            mLineChart.setData(data);
        }
        mLineChart.invalidate();
        //mLineChart.animateX(500, Easing.EasingOption.EaseInOutQuart);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBitmapFromView(mLineChart,"L");
                //Do something after 100ms
            }
        }, 300);



        // mLineChart.saveToGallery("akhilchart",100);



    }

    private  void fill_Bar_chart_data(String sfromdate ,String stodate ) {


        mBarChart.setDescription("");
       // mBarChart.animateXY(1000, 1000);

        YAxis leftAxis = mBarChart.getAxisLeft();
        YAxis rightaxis = mBarChart.getAxisRight();
        XAxis xAxis = mBarChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(false);
        rightaxis.setDrawGridLines(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setPinchZoom(false);
        mBarChart.setDoubleTapToZoomEnabled(false);
        mBarChart.setBackgroundColor(Color.TRANSPARENT);
        mBarChart.getXAxis().setGranularity(1f);
        mBarChart.getAxisLeft().setGranularity(50f);
        // chart.setHighlightIndicatorEnabled(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBarShadow(false);
      //  mBarChart.setPinchZoom(false);
        mBarChart.setScaleYEnabled(false);
        mBarChart.setScaleXEnabled(false);
        mBarChart.setDrawValueAboveBar(false);
        mBarChart.getLegend().setEnabled(false);

        mBarChart.getAxisRight().setDrawGridLines(false);

        mBarChart.getAxisLeft().setDrawGridLines(false);
        mBarChart.getAxisLeft().setSpaceTop(10f);
        mBarChart.getAxisLeft().setSpaceBottom(10f);

        mBarChart.getXAxis().setDrawGridLines(false);
        mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarChart.getAxisRight().setDrawGridLines(false);
        mBarChart.getAxisRight().setEnabled(false);


        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
     /*   mv.set(mBarChart); // For bounds control*/
        mBarChart.setMarkerView(mv);


        ArrayList<BarEntry> valueSet1 = new ArrayList<>();


        Cursor cursor_Chart;

        cursor_Chart = db_water.getAllData_Chart(relationShipId.toString(), sfromdate, stodate);


        cursor_Chart.moveToFirst();
        while (!cursor_Chart.isAfterLast()) {
            if (cursor_Chart.getString(cursor_Chart.getColumnIndex("maxqty")) != null) {

                try {
                    graph_format_date = dateFormat_query.parse(cursor_Chart.getString(cursor_Chart.getColumnIndex("created_date")));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                valueSet1.add(new BarEntry(Float.parseFloat(format_single_date.format(graph_format_date)), cursor_Chart.getFloat(cursor_Chart.getColumnIndex("maxqty"))));

            }


            cursor_Chart.moveToNext();
        }


        cursor_Chart.moveToFirst();


        if (valueSet1.size() > 0) {


            int[] colrs = new int[valueSet1.size()];

            for (int a = 0; a < valueSet1.size(); a++) {
                float xy = valueSet1.get(a).getY();
                float m = (xy / Water_goal) * 100;

                if (m < 35) {
                    colrs[a] = Color.RED;
                    // set1.setCircleColors(colrs);
                } else if (m <= 80) {
                    colrs[a] = Color.rgb(255, 140, 0);
                } else if (m > 80 && m <= 124) {
                    colrs[a] = Color.GREEN;
                } else if (m > 125) {
                    colrs[a] = Color.RED;
                }

            }

            if (valueSet1.size() > 0) {
                mBarChart.getXAxis().setAxisMinValue(valueSet1.get(0).getX() - 1);
            }


            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Date");
            barDataSet1.setColors(colrs);


            BarData data = new BarData(barDataSet1);
            data.setBarWidth(0.3f);
            mBarChart.setData(data);
        } else {
            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "");

            BarData data = new BarData();
            mBarChart.setData(data);
        }

        //mBarChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        mBarChart.invalidate();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBitmapFromView(mBarChart,"B");
                //Do something after 100ms
            }
        }, 300);



    }




    private  void fill_pie_chart_data(String sfromdate,String stodate) {
        try {

            Float total_count=0f,not_achieved=0f,Normal=0f,overdrinking=0f;

            float normal_value_percent=0;
            float not_achieved_value_percent=0;
            float overdrinking_value_percent=0;

            mPiechart.setUsePercentValues(true);
            mPiechart.setDescription("");
            mPiechart.setHovered(false);

            mPiechart.setDragDecelerationFrictionCoef(0.95f);


            mPiechart.setDrawHoleEnabled(true);

            mPiechart.setTransparentCircleColor(Color.WHITE);

            mPiechart.setHoleRadius(58f);
            mPiechart.setTransparentCircleRadius(61f);

            mPiechart.setDrawCenterText(true);

            mPiechart.setRotationAngle(0);
            // enable rotation of the chart by touch
            mPiechart.setRotationEnabled(false);


            mPiechart.setCenterText("");

            mPiechart.setEntryLabelColor(Color.BLUE);
            // mPiechart.spin(2000, 0, 360);

            Cursor crs =db_water.getAllData_Chart_log_for_pie_chart(relationShipId.toString(), sfromdate, stodate);

            if (crs != null) {
                if (crs.moveToFirst()) {
                    do {
                        not_achieved=  crs.getFloat(crs.getColumnIndex("not_achieved"));
                        Normal=  crs.getFloat(crs.getColumnIndex("Normal"));
                        overdrinking=  crs.getFloat(crs.getColumnIndex("overdrinking"));

                    } while (crs.moveToNext());

                }
            }

            total_count=not_achieved+Normal +overdrinking;

            normal_value_percent=(Normal/total_count)*100;
            not_achieved_value_percent=(not_achieved/total_count)*100;
            overdrinking_value_percent=(overdrinking/total_count)*100;

            ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

            if(normal_value_percent>0) {
                yVals1.add(new PieEntry((normal_value_percent),"Goal Achieved"));
            }

            if(not_achieved_value_percent>0) {
                yVals1.add(new PieEntry((not_achieved_value_percent),"Goal Not Achieved"));
            }
            if(overdrinking_value_percent>0) {
                yVals1.add(new PieEntry((overdrinking_value_percent),"Over Drinking"));
            }


            PieDataSet dataSet = new PieDataSet(yVals1, "");

            dataSet.setValueFormatter(new MyValueFormatter());
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
            mPiechart.setData(data);
            mPiechart.highlightValues(null);
            mPiechart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
            mPiechart.invalidate();


        }
        catch(Exception ex)
        {
            String s=String.valueOf(ex.toString());

        }
    }


    private void set_drink_message_on_percent(String sfromdate,String stodate)
    {
        Cursor cursor_Chart,Cursor_total_goal;

        cursor_Chart = db_water.getAllData_Chart(relationShipId.toString(), sfromdate, stodate);
        Cursor_total_goal= db_water.getAllDrink_goal_Data_Chart(relationShipId.toString(), sfromdate, stodate);
        float f1=0,total_goal=0;
        int i=0;
        cursor_Chart.moveToFirst();
        Cursor_total_goal.moveToFirst();

        while (!cursor_Chart.isAfterLast()) {
            if(cursor_Chart.getString(cursor_Chart.getColumnIndex("maxqty"))!=null) {
                f1 = Float.parseFloat(String.valueOf(cursor_Chart.getString(cursor_Chart.getColumnIndex("maxqty")))) + f1;


                i++;

            }
            cursor_Chart.moveToNext();
        }

        while (!Cursor_total_goal.isAfterLast()) {

            if(Cursor_total_goal.getString(Cursor_total_goal.getColumnIndex("maxqty"))!=null) {
                total_goal = Float.parseFloat(String.valueOf(Cursor_total_goal.getString(Cursor_total_goal.getColumnIndex("maxqty"))));
            }
            i++;


            Cursor_total_goal.moveToNext();
        }
        float drink_percent=0;

        drink_percent=(f1/total_goal)*100;

        if(drink_percent>=0 && drink_percent<30)
        {
            MainMessage.setText("Seems you are not drinking enough water");
        }else if(drink_percent>40&& drink_percent<80)
        {
            MainMessage.setText("Good going but complete your Goal!");
        }else if(drink_percent>=80 && drink_percent<=124)
        {
            MainMessage.setText("Congrats! You are drinking well");
        }else if(drink_percent>125){
            MainMessage.setText("Caution! Over Drinking will affect your health");
        }else {  MainMessage.setText("Seems you are not drinking enough water");}

        MainMessage.startAnimation(text_animation);

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void create_pdf(String sfromdate,String stodate)
    {
        float[]table_widhts=new float[]{5f, 5f, 5f,5f};
        float[]width6=new float[]{5f, 5f, 5f,5f,5f,5f};
        float[]width3=new float[]{5f, 5f, 5f};
        float[]width2=new float[]{5f, 5f,5f,5f};
        int report_length=0;
        Paragraph p3=new Paragraph();
        String inserted_date="";

        Document doc = new Document(PageSize.A4);




        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);
        Font bfBold12_white = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);
        Font bfBold13 = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.BLACK);

        Font bfBold_graph = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.DARK_GRAY);

        Font bfBold13_white = new Font(Font.FontFamily.UNDEFINED, 7, Font.NORMAL, BaseColor.WHITE);

        Font h1 = new Font(Font.FontFamily.COURIER, 24, Font.NORMAL, BaseColor.BLACK);
        Font h1_blue = new Font(Font.FontFamily.COURIER, 24, Font.NORMAL, new BaseColor(0,121,182));
        Font h3 = new Font(Font.FontFamily.COURIER, 15, Font.NORMAL, BaseColor.BLACK);

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
        dottedline.setLineColor(new BaseColor(223,99,1));

        String myName="",Age="",last_set_goal="",goal_bs="",goal_weight="",gender="";



        PdfContentByte content=null;



        try {

            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher);
            bitmap.compress(Bitmap.CompressFormat.PNG, 82, stream2);
            Image myImg = Image.getInstance(stream2.toByteArray());
            myImg.setAlignment(Image.ALIGN_LEFT);
            //myImg.setAbsolutePosition(5f,5f);
            myImg.scaleAbsolute(25, 25);
            // myImg.setBackgroundColor(BaseColor.WHITE);



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


            file = new File(dir, "RxMedikart_water_monitor_report.pdf");
            FileOutputStream fOut = new FileOutputStream(file);



            writer= PdfWriter.getInstance(doc, fOut);

            writer.setBoxSize("art", rect);

            doc.open();

            //  doc.setMargins(50, 45, 50, 60);
            doc.setMarginMirroring(false);

            PdfPTable table_head = new PdfPTable(1);
            table_head.setWidthPercentage(100);
            Chunk cp0 = new Chunk();

            cp0 = new Chunk("www.rxmedikart.com", fontlink).setAnchor("http://www.rxmedikart.com");
            insertCellwithBorder(table_head,null, "Water Monitor", "row", null, Element.ALIGN_RIGHT, 1, h1_blue, true, 0, 0, 0, 0,null);
            insertCellwithBorder(table_head,cp0, "", "row", null, Element.ALIGN_RIGHT, 1, h1_blue, true, 0, 0, 0, 0,null);
            table_head.setSpacingAfter(5);



            doc.add(table_head);



            doc.add(dottedline);
            doc.add( Chunk.NEWLINE );

           /* Paragraph p1 = new Paragraph();
            Chunk cp1 = new Chunk("Water Monitor Log",h3);
            p1.add(cp1);
            cp1 = new Chunk("                              ",paraFont);
            p1.add(cp1);*/
           /* cp1 = new Chunk("All Data",paraFont);
            p1.add(cp1);*/
            ///p1.setAlignment(Paragraph.ALIGN_LEFT);




            PdfPTable table_date_rage = new PdfPTable(1);
            table_date_rage.setHorizontalAlignment(Element.ALIGN_CENTER);
            insertCellwithBorder(table_date_rage,null,  "Report from "+sfromdate+" to "+stodate+".", "", null, Element.ALIGN_CENTER, 1, bfBold13, true, 0.001f, 0.001f,0.001f,0.001f,null);
            table_date_rage.setSpacingAfter(10);
            table_date_rage.setSpacingBefore(10);

            doc.add(table_date_rage);

            // doc.add( Chunk.NEWLINE );

            //data function
            Integer set_goal = db_water.getLastSetGoal(relationShipId);


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


            //   insertCellwithBorder(table1, "Gender : "+gender+"","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);

         //   insertCellwithBorder(table1,null, " ", "r", null, Element.ALIGN_CENTER, 4, bfBold12, true, 0.01f, 0.01f, 0.01f, 0.01f,new BaseColor(223, 99, 1));

            insertCellwithBorder(table1,null, "Last set goal : ","r", null, Element.ALIGN_CENTER, 1, bfBold12,true,0.01f,0.01f,0.01f,0.01f,new BaseColor(223, 99, 1));
            insertCellwithBorder(table1,null, set_goal+"","r", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f,BaseColor.WHITE);

            //  insertCellwithBorder(table1, "Goal Weight : "+goal_weight+"","row", null, Element.ALIGN_LEFT, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);



            doc.add(table1);

            //
            doc.add( Chunk.NEWLINE );

            Paragraph p2 = new Paragraph();
            Chunk cp2 = new Chunk("Statistic",h3);
            p2.add(cp2);

            p2.setAlignment(Paragraph.ALIGN_LEFT);
            doc.add(p2);


            //data function
            Cursor static_cursor = db_water.getAllData_Chart_Static(relationShipId.toString(),sfromdate,stodate);

            String max_weight_kg,min_weight_kg,avg_weight_kg;
            String drinkStatus;
            String max_g_value,min_g_value,avg_g_value;

            if ((static_cursor != null) && (static_cursor.getCount() > 0)) {
                if (static_cursor.moveToFirst()) {
                    do {


                        max_g_value= static_cursor.getString(static_cursor.getColumnIndex("max_qty"));
                        min_g_value= static_cursor.getString(static_cursor.getColumnIndex("min_qty"));
                        avg_g_value= static_cursor.getString(static_cursor.getColumnIndex("avg_qty"));

                          /*  max_weight_kg= static_cursor.getString(static_cursor.getColumnIndex("avg_weight_lb"));
                            min_weight_kg= static_cursor.getString(static_cursor.getColumnIndex("avg_weight_lb"));
                            avg_weight_kg= static_cursor.getString(static_cursor.getColumnIndex("avg_weight_lb"));
*/
                    } while (static_cursor.moveToNext());

                }else {
                    //insertCell(table, "No Entries", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                    max_g_value= "No Entries";
                    min_g_value= "No Entries";
                    avg_g_value= "No Entries";

                    max_weight_kg= "No Entries";
                    min_weight_kg= "No Entries";
                    avg_weight_kg= "No Entries";

                }
            }else {
                // insertCell(table, "No Entries", null, Element.ALIGN_CENTER, 1, bfBold14,false);

                max_g_value= "No Entries";
                min_g_value= "No Entries";
                avg_g_value= "No Entries";

                max_weight_kg= "No Entries";
                min_weight_kg= "No Entries";
                avg_weight_kg= "No Entries";
            }

            float[]width_graph=new float[]{3f, 5f};

            PdfPTable table_graph = new PdfPTable(2);
            table_graph.setWidths(width_graph);
            table_graph.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table_graph.setSpacingBefore(10f);
            table_graph.setSpacingAfter(0f);
            table_graph.setWidthPercentage(100);

            insertCellwithBorder(table_graph,null, "Over All Weekly Analysis ","header", null, Element.ALIGN_CENTER, 2, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);

            insertCellwithBorder(table_graph,null, "","row", GraphImage, Element.ALIGN_LEFT, 1, bfBold13_white,true,0.01f,0.01f,0.0f,0.01f,null);


            Paragraph p_desc_1 = new Paragraph();


            p_desc_1.add(new Paragraph(String.format( "%.2f", normal_value_percent)+"%  Goal Achieved.",bfBold_graph));

            p_desc_1.add(new Paragraph(String.format( "%.2f", not_achieved_value_percent)+"% Goal not achieved.",bfBold_graph));

            p_desc_1.add(new Paragraph(String.format( "%.2f", overdrinking_value_percent)+"% Overdrinking.",bfBold_graph));





            PdfPCell cell_1 = new PdfPCell();
            cell_1.addElement(p_desc_1);
            cell_1.setColspan(1);
            cell_1.setBorderWidthLeft(0);
            cell_1.setPaddingTop(30);
            cell_1.setBorderColor(new BaseColor(0,102,0));

            cell_1.setVerticalAlignment(Element.ALIGN_CENTER);
            table_graph.addCell(cell_1);


            //doc.add(table_graph);


            Paragraph p_desc = new Paragraph();
            p_desc.add(new Paragraph(String.format( "%.2f", weekly_drunk)+"%  drunk in this week.",bfBold_graph));
            p_desc.add(new Paragraph(String.format( "%.2f", weekly_pending)+"% pending in this week.",bfBold_graph));


            PdfPCell cell = new PdfPCell();
            cell.addElement(p_desc);
            cell.setColspan(1);
            cell.setBorderWidthLeft(0);
            cell.setPaddingTop(30);
            cell.setBorderColor(new BaseColor(0,102,0));

            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table_graph.addCell(cell);




         //   insertCellwithBorder(table_graph, "Over All Weekly Analysis","", null, Element.ALIGN_LEFT, 1, bfBold13,true,0.0f,0.01f,0.01f,0.01f);

            doc.add(table_graph);


            PdfPTable table2 = new PdfPTable(4);
            table2.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table2.setSpacingAfter(0f);
            table2.setWidthPercentage(100);
            table2.setWidths(table_widhts);

            insertCellwithBorder(table2,null, "","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);
            insertCellwithBorder(table2,null, "Maximum","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);
            // insertCellwithBorder(tab,nullle2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, "Minimum","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);
            // insertCellwithBorder(tab,nullle2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, "Average","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,true,0.01f,0.01f,0.01f,0.01f,null);

            insertCellwithBorder(table2,null, "Water (ml)","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0,0,0.01f,null);
            insertCellwithBorder(table2,null, max_g_value,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0,0.01f,null);
            //  insertCellwithBorder(ta,nullble2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, min_g_value,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0,0.01f,null);
            //  insertCellwithBorder(ta,nullble2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            insertCellwithBorder(table2,null, avg_g_value,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0.01f,0.01f,null);

            //   insertCellwithBorder(table2, "Weight ("+setting_weight_unit+")","row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0,0,0.01f);
            //    insertCellwithBorder(table2, max_weight_kg,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0,0.01f);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            //    insertCellwithBorder(table2, min_weight_kg,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0,0.01f);
            //  insertCellwithBorder(table2, "Date", null, Element.ALIGN_CENTER, 1, bfBold13,true,0.01f,0.01f,0.01f,0.01f);
            //    insertCellwithBorder(table2, avg_weight_kg,"row", null, Element.ALIGN_CENTER, 1, bfBold13,true,0,0,0.01f,0.01f);



            doc.add(table2);





            db_water.getAllData_Chart_log_for_pie_chart(relationShipId.toString(),sfromdate,stodate);


            Cursor pdf_cursor = db_water.getAllData_Chart_log(relationShipId.toString(),sfromdate,stodate);



            //p2.setAlignment(Paragraph.ALIGN_LEFT);


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
            Font axis_desc_font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL, new BaseColor(0,121,182));
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

            p3=new Paragraph("Readings Report",h3);
            doc.add(p3);


            PdfPTable table = new PdfPTable(4);
            table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(0f);
            table.setWidthPercentage(100);
            table.setWidths(table_widhts);

            insertCell(table, "Water Log","header", null, Element.ALIGN_CENTER, 4, bfBold12_white,false);
            insertCell(table, "Date","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,false);
            insertCell(table, "Quantity","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,false);
            insertCell(table, "Goal Set","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,false);
            insertCell(table, "Drinking Status","header", null, Element.ALIGN_CENTER, 1, bfBold13_white,false);


            if ((pdf_cursor != null) && (pdf_cursor.getCount() > 0)) {
                if (pdf_cursor.moveToFirst()) {

                    int c=pdf_cursor.getCount();
                    do {

                        Double qty=pdf_cursor.getDouble(pdf_cursor.getColumnIndex("totalqty"));
                        Double goal=pdf_cursor.getDouble(pdf_cursor.getColumnIndex("goal_set"));

//
                        Double drinkpercnt = (qty/goal)*100;


                        if(drinkpercnt==0) {

                            drinkStatus="Have your First Glass of water for the day";

                        } else  if(drinkpercnt>00.00 && drinkpercnt<=10.00 ) {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);

                            drinkStatus="Nice But Drink More";

                        }else  if(drinkpercnt>10.00 && drinkpercnt<=20.00) {

                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
                            drinkStatus="Nice But Drink More";

                        }else  if(drinkpercnt>20.00 && drinkpercnt<=34.00) {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
                            drinkStatus="Nice But Drink More";

                        }else  if(drinkpercnt>35.00 && drinkpercnt<=40.00 ) {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.ORANGE);
                            drinkStatus="Good";

                        }else  if(drinkpercnt>40.00 && drinkpercnt<=50.00 ) {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.ORANGE);
                            drinkStatus="Good";

                        }else  if(drinkpercnt>50.00 && drinkpercnt<=60.00) {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.ORANGE);
                            drinkStatus="Better";

                        }else  if(drinkpercnt>60.00 && drinkpercnt<=70.00) {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.ORANGE);
                            drinkStatus="Very Good";

                        }else  if(drinkpercnt>70.00 && drinkpercnt<=80.00) {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.ORANGE);
                            drinkStatus="Very Good";

                        }else  if(drinkpercnt>80.00 && drinkpercnt<=90.00) {
                            drinkStatus="Awesome";
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.GREEN);
                            //icons_on_water.setText("Awesome");

                        }else  if(drinkpercnt>90.00 && drinkpercnt<=100.00) {
                            drinkStatus="Awesome";
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.GREEN);
                            //icons_on_water.setText("Awesome");

                        }

                        else  if(drinkpercnt>=100.00 &&  drinkpercnt<=124.00) {
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.GREEN);
                            drinkStatus="Completed";
                            /// icons_on_water.setText("Completed");

                        }
                        else  if(drinkpercnt>125.00) {
                            drinkStatus="Over Drinking";
                            bfBoldRangeClor = new Font(Font.FontFamily.UNDEFINED, 6, Font.NORMAL, BaseColor.RED);
                            // icons_on_water.setText("Over Drinking");
                        }else
                        {
                            drinkStatus="";
                        }


                        insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("created_date")),"row", null, Element.ALIGN_CENTER, 1, bfBold13,false);
                        insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("totalqty")),"row", null, Element.ALIGN_CENTER, 1, bfBoldRangeClor,false);
                        insertCell(table, pdf_cursor.getString(pdf_cursor.getColumnIndex("goal_set")),"row", null, Element.ALIGN_CENTER, 1, bfBold13,false);
                        insertCell(table,drinkStatus ,"row", null, Element.ALIGN_CENTER, 1, bfBoldRangeClor,false);

                    } while (pdf_cursor.moveToNext());

                }else {
                    insertCell(table, "No Entries","row", null, Element.ALIGN_CENTER, 1, bfBold13,false);
                }
            }else {
                insertCell(table, "No Entries","row", null, Element.ALIGN_CENTER, 1, bfBold13,false);
            }

            doc.add(table);

        } catch (DocumentException de) {
            de.toString();

        } catch (IOException e) {

        } finally {
            writer.setPageEvent(new WMA_BarChart.HeaderAndFooter());
            doc.close();
            send_report_to_doctor(file.getAbsolutePath());
        }
    }

    private void insertCell(PdfPTable table, String text,String type,Image img, int align, int colspan, Font font,Boolean Border){



        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setBorderWidth(0.01f);
        cell.setBorderColor(new BaseColor(223, 99, 1));
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

                DottedLineSeparator dottedline = new DottedLineSeparator();
                dottedline.setOffset(-2);
                dottedline.setGap(1f);
                dottedline.setLineColor(new BaseColor(223,99,1));

                Chunk c = new Chunk(background, 0, -45);

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

              //  ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase("Page "+writer.getPageNumber()), rect.getRight(), rect.getBottom()-5, 0);



                writer.getDirectContentUnder().addImage(background, 180, 0, 0, 30, 30, 765);
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
        intent_report.putExtra("Email_heading","Water Monitor report");
        intent_report.putExtra("path",Path);
        startActivity(intent_report);

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


    void create_statistics_pie_chart(String sfromdate,String stodate )
    {
        Float total_count=0f,not_achieved=0f,Normal=0f,overdrinking=0f;


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
        // enable rotation of the chart by touch
        mPiechart_statistics.setRotationEnabled(false);


        mPiechart_statistics.setCenterText("");

        mPiechart_statistics.setEntryLabelColor(Color.BLUE);
        // mPiechart.spin(2000, 0, 360);


        Cursor crs =db_water.getAllData_Chart_log_for_pie_chart(relationShipId.toString(), sfromdate, stodate);

        if (crs != null) {
            if (crs.moveToFirst()) {
                do {
                    not_achieved=  crs.getFloat(crs.getColumnIndex("not_achieved"));
                    Normal=  crs.getFloat(crs.getColumnIndex("Normal"));
                    overdrinking=  crs.getFloat(crs.getColumnIndex("overdrinking"));

                } while (crs.moveToNext());

            }
        }

        total_count=not_achieved+Normal +overdrinking;

        normal_value_percent=(Normal/total_count)*100;
        not_achieved_value_percent=(not_achieved/total_count)*100;
        overdrinking_value_percent=(overdrinking/total_count)*100;

        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        if(normal_value_percent>0) {
            yVals1.add(new PieEntry((normal_value_percent),"Goal Achieved"));
        }

        if(not_achieved_value_percent>0) {
            yVals1.add(new PieEntry((not_achieved_value_percent),"Goal Not Achieved"));
        }
        if(overdrinking_value_percent>0) {
            yVals1.add(new PieEntry((overdrinking_value_percent),"Over Drinking"));
        }


        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setValueFormatter(new MyValueFormatter());

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




    }

    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {

        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return value + "%"; // e.g. append a dollar-sign
        }
    }

}


package viroopa.com.medikart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

import viroopa.com.medikart.MedicineReminder.MRA_Welcomeactivity;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.bpmonitor.BPA_AnalysisDisplayActivity;
import viroopa.com.medikart.bpmonitor.BPA_WelcomeActivity;
import viroopa.com.medikart.buying.BuyMainActivity;
import viroopa.com.medikart.buying.Order_Transaction;
import viroopa.com.medikart.buying.Return_Cancel_Policies;
import viroopa.com.medikart.buying.Upload_prescription_list;
import viroopa.com.medikart.buying.networkerror;
import viroopa.com.medikart.dmMonitor.DMA_AnalysisDisplayActivity;
import viroopa.com.medikart.dmMonitor.DMA_WelcomeActivity;
import viroopa.com.medikart.downloadservice.DownloadBPMonitor;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SessionManager;
import viroopa.com.medikart.helper.SqliteBPHandler;
import viroopa.com.medikart.helper.SqliteDMHandler;
import viroopa.com.medikart.services.GcmRegisterService;
import viroopa.com.medikart.services.SyncDataService;
import viroopa.com.medikart.wmMonitor.WMA_Welcome;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView btnbuy, btnbp, btnmr, btnwm, btndm;
    private String sMemberId;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SqliteDMHandler db_dm;
    private SqliteBPHandler db_bp;
    private SessionManager session;
    private Integer getSelectedRelationshipId;
    AppController globalVariable;
    private Menu mToolbarMenu;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private static final long DRAWER_CLOSE_DELAY_MS = 100;
    private static final String NAV_ITEM_ID = "navItemId";
    HashMap<String, String> m;
    private final Handler mDrawerActionHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;
    private SharedPreferences pref;
    private ImageView profile_pic;
    private Integer ProfilePicFlag = 0;
    private RelativeLayout lnrdrawablebuymain;
    private TextView txt_profile_name, txt_profile_email;
    private String ProfilePicName, StoragePath = "", picname1 = "", image1;
    private DisplayImageOptions options;
    private CoordinatorLayout coordinatorLayout;
    private TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageLoader();
        setContentView(R.layout.activity_main_nav);
        SyncBpDataToserver();
        GCM_check();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        link = (TextView) findViewById(R.id.btnwebsite);
        globalVariable = (AppController) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (null == savedInstanceState) {
            //  mNavItemId = R.id.nav_familytree;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }
        // listen for navigation events
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_first_heading).setTitle(Html.fromHtml("<font color='#df6301' size='20' font-weight:bold ><b>PERSONAL INFORMATION</b></font>"));
        navigationView.getMenu().findItem(R.id.nav_second_heading).setTitle(Html.fromHtml("<font color='#df6301' size='30' font-weight:bold ><b>MY ACTIVITY</b></font>"));
        navigationView.getMenu().findItem(R.id.nav_third_heading).setTitle(Html.fromHtml("<font color='#df6301' font-weight:bold><b>HELP</b></font>"));
        // select the correct nav menu item
        // navigationView.getMenu().findItem(mNavItemId).setChecked(true);
        // set up the hamburger icon to open and close the drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        navigate(mNavItemId);
        txt_profile_name = (TextView) findViewById(R.id.txt_profile_name);
        txt_profile_email = (TextView) findViewById(R.id.txt_profile_email);
        profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);
        db = new SQLiteHandler(getApplicationContext());
        db_dm = new SqliteDMHandler(getApplicationContext());
        db_bp = new SqliteBPHandler(getApplicationContext());
        ArrayList<HashMap<String, String>> test = new ArrayList<HashMap<String, String>>();
        test.add(db.getUserDetails());
        m = test.get(0);
        pDialog = new ProgressDialog(this);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        getIntenet();
        network_check();
        btnbuy = (ImageView) findViewById(R.id.btn_buy);
        btnmr = (ImageView) findViewById(R.id.btn_mr);
        btnbp = (ImageView) findViewById(R.id.btn_bp);
        btnwm = (ImageView) findViewById(R.id.btn_wm);
        btndm = (ImageView) findViewById(R.id.btn_dm);
        // link button Click Event
        link.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Show_website();
            }
        });

        btnbuy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        f_Touch_Down(v);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        Showbuy();
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        f_Touch_Cancel(v);
                        break;
                    }
                }
                return true;
            }
        });

        btnmr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        f_Touch_Down(v);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        Showmr();
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        f_Touch_Cancel(v);
                        break;
                    }
                }
                return true;
            }
        });

        btnbp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        f_Touch_Down(v);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        Showbp();
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        f_Touch_Cancel(v);
                        break;
                    }
                }
                return true;
            }
        });

        btnwm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        f_Touch_Down(v);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        Showwm();
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        f_Touch_Cancel(v);
                        break;
                    }
                }
                return true;
            }
        });

        btndm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        f_Touch_Down(v);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        Showdm();
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        f_Touch_Cancel(v);
                        break;
                    }
                }
                return true;
            }
        });
    }
    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        mToolbarMenu = paramMenu;
        ondraweropenload();
        return super.onPrepareOptionsMenu(paramMenu);
    }

    private void Showbuy() {
        Intent Intenet_buy = new Intent(MainActivity.this, BuyMainActivity.class);
        startActivity(Intenet_buy);
    }
    private void ondraweropenload() {
        try {
            if (txt_profile_name == null) {
                txt_profile_name = (TextView) mDrawerLayout.findViewById(R.id.txt_profile_name);
                txt_profile_email = (TextView) mDrawerLayout.findViewById(R.id.txt_profile_email);
                profile_pic = (ImageView) mDrawerLayout.findViewById(R.id.iv_profile_pic);
                lnrdrawablebuymain = (RelativeLayout) mDrawerLayout.findViewById(R.id.lnrdrawablebuymain);
            }
            if (!m.isEmpty()) {
                txt_profile_name.setText(m.get("name"));
                txt_profile_email.setText(m.get("email"));
            }
            load_profile_pic();
            lnrdrawablebuymain.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                    //  Intent Intenet_mr = new Intent(MainActivity.this, EditProfile.class);
                    // startActivity(Intenet_mr);
                }
            });
        } catch (Exception e) {
            e.toString();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (profile_pic != null) {
            load_profile_pic();
        }
    }

    private void load_profile_pic() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        ProfilePicName = pref.getString("imagename", "");
        ProfilePicFlag = pref.getInt("ProfilePicFlag", 0);
        if (ProfilePicName == null) {
            ProfilePicName = "avtar1";
        }
        if (!ProfilePicName.equals("")) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppConfig.IMAGE_DIRECTORY_NAME);
            String picturePath = mediaStorageDir.getPath() + "/" + ProfilePicName;
            if (!ProfilePicName.startsWith("avtar")) {
                imageLoader.displayImage("file://" + picturePath, profile_pic, options);
            }
            if (ProfilePicName.equals("avtar1")) {
                profile_pic.setImageResource(R.drawable.avtar1);
            }
            if (ProfilePicName.equals("avtar2")) {
                profile_pic.setImageResource(R.drawable.avtar2);
            }
            if (ProfilePicName.equals("avtar3")) {
                profile_pic.setImageResource(R.drawable.avtar3);
            }
            if (ProfilePicName.equals("avtar4")) {
                profile_pic.setImageResource(R.drawable.avtar4);
            }
            if (ProfilePicName.equals("avtar5")) {
                profile_pic.setImageResource(R.drawable.avtar5);
            }
            if (ProfilePicName.equals("avtar6")) {
                profile_pic.setImageResource(R.drawable.avtar6);
            }
            if (ProfilePicName.equals("avtar7")) {
                profile_pic.setImageResource(R.drawable.avtar7);
            }
            if (ProfilePicName.equals("avtar8")) {
                profile_pic.setImageResource(R.drawable.avtar8);
            }
            if (ProfilePicName.equals("avtar9")) {
                profile_pic.setImageResource(R.drawable.avtar9);
            }
            if (ProfilePicName.equals("avtar10")) {
                profile_pic.setImageResource(R.drawable.avtar10);
            }
            if (ProfilePicName.equals("avtar11")) {
                profile_pic.setImageResource(R.drawable.avtar11);
            }
            if (ProfilePicName.equals("avtar12")) {
                profile_pic.setImageResource(R.drawable.avtar12);
            }
            if (ProfilePicName.equals("avtar13")) {
                profile_pic.setImageResource(R.drawable.avtar13);
            }
            if (ProfilePicName.equals("avtar14")) {
                profile_pic.setImageResource(R.drawable.avtar14);
            }
            if (ProfilePicName.equals("avtar15")) {
                profile_pic.setImageResource(R.drawable.avtar15);
            }
            if (ProfilePicName.equals("avtar16")) {
                profile_pic.setImageResource(R.drawable.avtar16);
            }
            profile_pic.requestLayout();
            String picname = "";
            StoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + "/" + AppConfig.IMAGE_DIRECTORY_NAME;

            if (ProfilePicFlag.equals(0)) {
                image1 = picname;
            }
            if (ProfilePicFlag.equals(2)) {
                picname = "mem" + "_" + sMemberId + "_" + "1" + ".jpeg";
                picname1 = "mem" + "_" + sMemberId + "_" + "1" + ".jpeg";
                image1 = StoragePath + "/" + picname;
            } else {
                image1 = picturePath;
            }
            if (profile_pic.getDrawable() == null) {
                profile_pic.setImageResource(R.drawable.avtar1);
            }
        }
    }

    private void Showbp() {
        int aa = Integer.parseInt(sMemberId);
        Cursor cursor_data;
        /*cursor_data = db_bp.getBPMonitorCount(Integer.parseInt(sMemberId), getSelectedRelationshipId);*/
        cursor_data = db_bp.getBPMonitorCount(aa, getSelectedRelationshipId);
        if ((cursor_data != null) && (cursor_data.getCount() > 0)) {
            if (cursor_data.moveToFirst()) {
                Intent Intenet_dm = new Intent(MainActivity.this, BPA_AnalysisDisplayActivity.class);
                Intenet_dm.putExtra("FlagData", true);
                startActivity(Intenet_dm);
            }
        } else {
            Intent Intenet_bp = new Intent(MainActivity.this, BPA_WelcomeActivity.class);
            startActivity(Intenet_bp);
        }
        uploadBPDetails();
    }

    private void Showmr() {
        Intent Intenet_mr = new Intent(MainActivity.this, MRA_Welcomeactivity.class);
        startActivity(Intenet_mr);
    }

    private void Showwm() {
        Intent Intenet_buy = new Intent(MainActivity.this, WMA_Welcome.class);
        startActivity(Intenet_buy);
    }


    private void Showdm() {
/*        Intent Intenet_dm = new Intent(MainActivity.this,DMA_WelcomeActivity.class);
        startActivity(Intenet_dm);*/
        Cursor cursor_data;
        cursor_data = db_dm.getTop1DMMonitarData(Integer.parseInt(sMemberId), getSelectedRelationshipId);
        Intent Intenet_dm;
        if ((cursor_data != null) || (cursor_data.getCount() > 0)) {
            if (cursor_data.moveToFirst()) {
                Intenet_dm = new Intent(MainActivity.this, DMA_AnalysisDisplayActivity.class);
                Intenet_dm.putExtra("FlagData", true);
                startActivity(Intenet_dm);
            } else {
                Intenet_dm = new Intent(MainActivity.this, DMA_WelcomeActivity.class);
                Intenet_dm.putExtra("FlagData", false);
                startActivity(Intenet_dm);
            }
        } else {
            Intenet_dm = new Intent(MainActivity.this, DMA_WelcomeActivity.class);
            Intenet_dm.putExtra("FlagData", false);
            startActivity(Intenet_dm);
        }
    }

    public void Show_Change_Password() {
        Intent Intenet_change = new Intent(MainActivity.this, ChangePassword.class);
        startActivity(Intenet_change);
    }
    public void Show_Add_Member() {
        Intent Intenet_member = new Intent(MainActivity.this, EditAndAddMember.class);
        startActivity(Intenet_member);
    }

    public void Show_policy() {
        Intent Intenet_member = new Intent(MainActivity.this, Return_Cancel_Policies.class);
        startActivity(Intenet_member);
    }
    public void Show_Settings() {
//        Intent Intenet_member = new Intent(MainActivity.this, MainSetting.class);
//        startActivity(Intenet_member);
    }

    private void Show_Edit_Profile() {
        Intent Intenet_edit_profile = new Intent(MainActivity.this, EditProfile.class);
        startActivity(Intenet_edit_profile);
    }
    private void Show_Logout() {
        showPdialog("loading...");
        db.logout_delete_all();
        session.setLogin(false);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.clear();
        prefsEditor.commit();
/*        Map<String, ?> allEntries = pref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }*/

        hidePDialog();
        Intent Intenet_edit_profile = new Intent(MainActivity.this, LoginActivity.class);
        Intenet_edit_profile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intenet_edit_profile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intenet_edit_profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intenet_edit_profile);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen, menu);

        MenuItem item_exportdb = menu.findItem(R.id.exportdb);
        //item_exportdb.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.edit_profile:
                Show_Edit_Profile();
                return true;
            case R.id.change_password:
                Show_Change_Password();
                return true;
            case R.id.add_member:
                Show_Add_Member();
                return true;
            case R.id.logout:
                Show_Logout();
                return true;
            case R.id.exportdb:
                exportDB();
                return true;
            case R.id.faqs:
                Show_faqs();
                return true;
            case R.id.termscondition:
                Show_termscondition();
                return true;
            case R.id.return_cancel_policies:
                Show_Return_cancel_policies();
                return true;
            case R.id.aboutus:
                Show_Aboutus();
                return true;
            case R.id.contactus:
                Show_Contactus();
                return true;
            case R.id.order_transaction:
                Show_Order_Transaction();
                return true;
            case R.id.btnwebsite:
                Show_website();
                return true;

            case android.support.v7.appcompat.R.id.home:
                return mDrawerToggle.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
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

    private void getIntenet() {
        Intent intent_register = getIntent();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        sMemberId = pref.getString("memberid", "");
        if (globalVariable.getRealationshipId() != null) {
            getSelectedRelationshipId = Integer.parseInt(globalVariable.getRealationshipId());
        } else {
            getSelectedRelationshipId = 8;
        }
        /*String sState = pref.getString("statelist", "");
        String sCity = pref.getString("citylist", "");
        if (sState == "")
        {
            get_statelist();
        }
        if (sCity == "")
        {
            get_citylist();
        }*/
    }
    private void f_alert_ok(String p_title, String p_msg) {
        new AlertDialog.Builder(MainActivity.this)
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

    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String backupDBPath = "medikart";
        String currentDBPath = "/data/" + "viroopa.com.medikart" + "/databases/" + backupDBPath;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
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
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void navigate(final int itemId) {
        // perform the actual navigation logic, updating the main content fragment etc
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            exit_notification();

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {

        // update highlighted item in the navigation menu
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();

        // allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());

                if (mNavItemId == R.id.nav_edit_profile) {
                    Show_Edit_Profile();
                } else if (mNavItemId == R.id.nav_changepassword) {
                    Show_Change_Password();
                } else if (mNavItemId == R.id.nav_faq) {
                    Show_faqs();
                } else if (mNavItemId == R.id.nav_contactus) {
                    Show_Contactus();
                } else if (mNavItemId == R.id.nav_logout) {
                    Show_Logout();
                } else if (mNavItemId == R.id.nav_familytree) {
                    Show_Add_Member();
                } else if (mNavItemId == R.id.nav_policy) {
                    Show_policy();

                } else if (mNavItemId == R.id.nav_myorders) {
                    Show_Order_Transaction();
                } else if (mNavItemId == R.id.nav_myprescriptionlist) {
                    Show_Prescription_list();
                }


            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

    private void exit_notification() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure want to Exit")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    private void network_check() {
        if (haveNetworkConnection() == true) {
            SyncBpDataToserver();
        } else {
            Intent intent_network = new Intent(MainActivity.this, networkerror.class);
            startActivity(intent_network);
        }
    }

    public void Show_faqs() {
        Intent Intenet_change = new Intent(MainActivity.this, FAQs.class);
        startActivity(Intenet_change);
    }

    public void Show_termscondition() {
        Intent Intenet_change = new Intent(MainActivity.this, TermsCondition.class);
        startActivity(Intenet_change);
    }

    public void Show_Return_cancel_policies() {
        Intent Intenet_change = new Intent(MainActivity.this, TermsCondition.class);
        startActivity(Intenet_change);
    }

    public void Show_Aboutus() {
        Intent Intenet_change = new Intent(MainActivity.this, Aboutus.class);
        startActivity(Intenet_change);
    }

    public void Show_Contactus() {
        Intent Intenet_change = new Intent(MainActivity.this, Contactus.class);
        startActivity(Intenet_change);
    }

    public void Show_Order_Transaction() {
        Intent Intenet_change = new Intent(MainActivity.this, Order_Transaction.class);
        startActivity(Intenet_change);
    }

    public void Show_website() {
        Snackbar snackbar = Snackbar
                .make(mDrawerLayout, "want to visit rxmedikart.com ?", Snackbar.LENGTH_LONG)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                                   /* Snackbar snackbar1 = Snackbar.make(laymain, "Message is restored!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();*/
                        Uri uri = Uri.parse("http://www.rxmedikart.com");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

        snackbar.show();
    }

    public void Show_Prescription_list() {
        Intent Intenet_change = new Intent(MainActivity.this, Upload_prescription_list.class);
        startActivity(Intenet_change);
    }

    private void uploadBPDetails() {
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadBPMonitor.class);
        startService(download_intent);
    }

    private void SyncBpDataToserver() {
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, SyncDataService.class);
        startService(download_intent);
        /* End Download Service */
    }

    private void GCM_check() {
        Intent download_intent = new Intent(Intent.ACTION_SYNC, null, this, GcmRegisterService.class);
        startService(download_intent);
    }

}


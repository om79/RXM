package viroopa.com.medikart.buying;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.R;
import viroopa.com.medikart.TermsCondition;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.buying.adapter.adapterprice;
import viroopa.com.medikart.buying.common.BadgeDrawable;
import viroopa.com.medikart.buying.model.saltDetailModel;
import viroopa.com.medikart.helper.SQLiteHandler;

public class SaltDetail extends AppCompatActivity {
    private TextView Salt_Heading, Salt_Description, Salt_headin_second;
    private ListView lst_med_combination;
    private adapterprice containsadapter;
    private String sMemberId;
    AppController globalVariable;
    private ProgressDialog pDialog;
    private CombinationAdapter o_combinationadapter;
    private TextView no_medicine;
    private Integer nAddtocart_count = 0;
    private Menu mToolbarMenu;
    private TabHost tabs;
    private   String pr_id;
    private SQLiteHandler db;
    private Double double_price=0.00;
    private DecimalFormat df = new DecimalFormat("#.00");

    private Integer total_med_count = 0;
    private LinearLayout lnrtest,lnrSecond,lnrthird,drug_molecule_linear_heading;
    private LayoutInflater inflater = null;
    private JSONArray ja_objquestionAswer,ja_objMoleculeInteraction,ja_objDruginteraction;


    ArrayList<saltDetailModel> med_combination = new ArrayList<saltDetailModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salt_detail);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pDialog = new ProgressDialog(this);
        globalVariable = (AppController) getApplicationContext();
        db = new SQLiteHandler(this);
        tabs = (TabHost) findViewById(R.id.dmTabHost);

        tabs.setup();


        TabHost.TabSpec tabpage1 = tabs.newTabSpec("one");
        tabpage1.setContent(R.id.scroll_first);
        tabpage1.setIndicator("Salt Details", this.getResources().getDrawable(R.drawable.orgbtn));
        TabHost.TabSpec tabpage2 = tabs.newTabSpec("two");
        tabpage2.setContent(R.id.second_layout);

        tabpage2.setIndicator("General Information", this.getResources().getDrawable(R.drawable.arrowdwn));
        // setTabColor(tabs);

        tabs.setFocusable(false);
        tabs.addTab(tabpage1);
        tabs.addTab(tabpage2);
        final TextView tv = (TextView) tabs.getTabWidget().getChildAt(tabs.getCurrentTab()).findViewById(android.R.id.title);
        tv.setTextColor(Color.WHITE);

        for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
            View xy = tabs.getTabWidget().getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tvx = (TextView) xy.findViewById(android.R.id.title);
            if (tvx == null) {
                continue;

            }
            tvx.setAllCaps(false);
            tvx.setTextColor(Color.parseColor("#bdbdbd"));
            xy.setBackgroundResource(R.drawable.tab_selector);
        }




        Salt_Heading = (TextView) findViewById(R.id.Salt_Heading);
        Salt_Description = (TextView) findViewById(R.id.Salt_Description);

        lst_med_combination = (ListView)findViewById(R.id.lst_med_contains);




        o_combinationadapter = new CombinationAdapter(this, med_combination);
        lst_med_combination.setAdapter(o_combinationadapter);
        getIntenet();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rxlogo);
        getSupportActionBar().setTitle("Search Medicine");


        toolbar.setTitle("Search Medicine");
        toolbar.setTitleTextColor(Color.WHITE);


        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showsearchmedicine("");
            }
        });



    }

    public boolean onPrepareOptionsMenu(Menu paramMenu) {
        mToolbarMenu = paramMenu;

        createCartBadge(nAddtocart_count);

        return super.onPrepareOptionsMenu(paramMenu);
    }

    private void createCartBadge(int paramInt) {
        if (Build.VERSION.SDK_INT <= 15) {
            return;
        }
        if (mToolbarMenu != null) {
            MenuItem cartItem = this.mToolbarMenu.findItem(R.id.action_cart);
            LayerDrawable localLayerDrawable = (LayerDrawable) cartItem.getIcon();
            Drawable cartBadgeDrawable = localLayerDrawable
                    .findDrawableByLayerId(R.id.ic_badge);
            BadgeDrawable badgeDrawable;
            if ((cartBadgeDrawable != null)
                    && ((cartBadgeDrawable instanceof BadgeDrawable))
                    && (paramInt < 10)) {
                badgeDrawable = (BadgeDrawable) cartBadgeDrawable;
            } else {
                badgeDrawable = new BadgeDrawable(this);
            }
            badgeDrawable.setCount(paramInt);
            localLayerDrawable.mutate();
            localLayerDrawable.setDrawableByLayerId(R.id.ic_badge, badgeDrawable);
            cartItem.setIcon(localLayerDrawable);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buy, menu);


        //MenuItem item_exportdb = menu.findItem(R.id.exportdb);
        //item_exportdb.setVisible(false);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_searchmedicine:
                showsearchmedicine("");
                return true;
            case R.id.edit_profile:
                // Show_Edit_Profile();
                return true;
            case R.id.change_password:
                // Show_Change_Password();
                return true;
            case R.id.add_member:
                //  Show_Add_Member();
                return true;
            case R.id.logout:
                // Show_Logout();
                return true;
            case R.id.exportdb:
                // exportDB();
                return true;
            case R.id.faqs:
                // Show_faqs();
                return true;
            case R.id.termscondition:
                Show_termscondition();
                return true;
            case R.id.return_cancel_policies:
                Show_Return_cancel_policies();
                return true;
            case R.id.aboutus:
                //  Show_Aboutus();
                return true;
            case R.id.contactus:
                // Show_Contactus();
                return true;
            case R.id.order_transaction:
                Show_Order_Transaction();
                return true;
            case R.id.action_cart:
                Show_cart();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getIntenet() {
        try {
            Intent intent_buymainactivity = getIntent();

            SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
            sMemberId = pref.getString("memberid", "");
            nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));
            pr_id = intent_buymainactivity.getStringExtra("productid");
            get_salt_information(pr_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get_salt_information(String salt_id) {
        showPdialog("loading...");

        String url = String.format(AppConfig.URL_GET_SALT_DATA, salt_id);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_state = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONObject object = response.getJSONObject("SaltDtl");
                    load_salt_data(object);
                    // ListUtils.setDynamicHeight(lst_med_contains);
                    //  ListUtils.setDynamicHeight(lst_med_combination);
                    hidePDialog();
                } catch (JSONException e) {
                    e.toString();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                });

        queue.add(jsonObjReq_state);
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

    public class CombinationAdapter extends ArrayAdapter<saltDetailModel> {
        public CombinationAdapter(Context context, ArrayList<saltDetailModel> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            saltDetailModel combination = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            convertView=null;
            if(combination.gettype().equals("Combination")) {

                convertView =  inflater.inflate(R.layout.salt_detail_combination_item, null);

                TextView comb_name = (TextView) convertView.findViewById(R.id.comb_name);
                TextView comd_count = (TextView) convertView.findViewById(R.id.comd_count);

                comb_name.setText(combination.getName());
                comd_count.setText(combination.getCount_combination() + " Medicines");

            }else  if(combination.gettype().equals("total_count"))
            {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,1);
                param.setMargins(10,5,10,5);

                LinearLayout lr= new LinearLayout(SaltDetail.this);
                lr.setOrientation(LinearLayout.VERTICAL);

                View vw=new View(SaltDetail.this);
                vw.setBackgroundColor(getResources().getColor(R.color.gray));
                vw.setLayoutParams(param);


                TextView t_cout=new TextView(SaltDetail.this);
                t_cout.setText(combination.gettotal_count());
                t_cout.setTextColor(getResources().getColor(R.color.colorPrimary));
                t_cout.setTextSize(10);
                Typeface type = Typeface.createFromAsset(getAssets(),"Roboto-Bold.ttf");
                t_cout.setTypeface(type);
                t_cout.setPadding(2,5,0,10);
                lr.addView(vw);
                lr.addView(t_cout);


                if(combination.getPr_id()!=null)
                {
                    lr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i_salt = new Intent(SaltDetail.this, AllSaltData.class);
                            i_salt.putExtra("Salt_id",pr_id);
                            i_salt.putExtra("total_count","10");
                            startActivity(i_salt);
                        }
                    });
                }


                lr.setBackground(getResources().getDrawable(R.drawable.text_view_selector));

                convertView=lr;


            }else  if(combination.gettype().equals("molecule"))
            {

                convertView = inflater.inflate(R.layout.page_cart_summary_item, null);

                TextView tax_name = (TextView) convertView.findViewById(R.id.tax_name);
                TextView tax_amt = (TextView) convertView.findViewById(R.id.tax_amt);

                tax_name.setText(combination.getName());
                Typeface type = Typeface.createFromAsset(getAssets(),"Roboto-Bold.ttf");
                tax_name.setTypeface(type);
                tax_amt.setTypeface(type);

                if (combination.getprice()!=null && combination.getprice()!="") {
                    double_price = Double.parseDouble( combination.getprice());
                }
                tax_amt.setText("Rs." + " " + df.format(double_price).toString());
            }else  if(combination.gettype().equals("heading"))
            {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,1);
                param.setMargins(10,5,10,5);

                LinearLayout lr= new LinearLayout(SaltDetail.this);
                lr.setOrientation(LinearLayout.VERTICAL);

                View vw=new View(SaltDetail.this);
                vw.setBackgroundColor(getResources().getColor(R.color.gray));
                vw.setLayoutParams(param);

                TextView t_cout=new TextView(SaltDetail.this);
                t_cout.setTextColor(getResources().getColor(R.color.colorPrimary));
                t_cout.setTextSize(10);
                Typeface type = Typeface.createFromAsset(getAssets(),"Roboto-Bold.ttf");
                t_cout.setTypeface(type);
                t_cout.setText(combination.getName());
               t_cout.setPadding(2,8,0,10);


                lr.addView(vw);
                lr.addView(t_cout);
                convertView=lr;
            }
            return convertView;
        }
    }



    private void load_salt_data(JSONObject response) {
        try {
            String saltName = response.getString("SaltName");
            String Salt_description = response.getString("SaltDescription");

            JSONArray salt_combination_json_array = response.getJSONArray("SaltCombinations");
            JSONArray salt_product_detail_json_array = response.getJSONArray("ProductDtl");
            ja_objquestionAswer= response.getJSONArray("PharmacologyDtl");
            ja_objMoleculeInteraction= response.getJSONArray("DrugMoleInteDtl");
            ja_objDruginteraction= response.getJSONArray("DrugDrugInteDtl");

            set_up_general_information_tab();
            load_combination(salt_combination_json_array,salt_product_detail_json_array,saltName);
            insert_salt_data_table(response.getString("SaltId"), response.getString("SaltName"));

            if(Salt_description.equals(""))
            {
                Salt_Description.setText("No information availabe");
            }else {
                Salt_Description.setText(Salt_description);
            }


        } catch (JSONException e) {
            e.toString();
        }
    }

    private void load_combination(JSONArray j_array,JSONArray mol_array,String saltName) {
        try {

            String Total_count="";
            med_combination.clear();

            saltDetailModel O_comb = new saltDetailModel();

            O_comb.setName("Medicine containig this Molecule");
            O_comb.setCount_combination("");
            O_comb.setprice("");
            O_comb.settotal_count("");
            O_comb.settype("heading");
            med_combination.add(O_comb);


            if (mol_array != null || !mol_array.equals("[]")) {

                for (int i = 0; i < mol_array.length(); i++) {
                    JSONObject mol_contain = (JSONObject) mol_array.get(i);
                    O_comb = new saltDetailModel();
                    O_comb.setprice(mol_contain.getString("MRP"));
                    O_comb.setName(mol_contain.getString("ProductName"));
                    O_comb.settype("molecule");
                    total_med_count = Integer.parseInt(mol_contain.getString("TotalCount"));
                    med_combination.add(O_comb);

                }

                O_comb = new saltDetailModel();
                O_comb.setName("");
                O_comb.setPr_id(pr_id);
                O_comb.setCount_combination("");
                O_comb.setprice("");
                O_comb.settotal_count("View all " +total_med_count+ " Medicines");
                O_comb.settype("total_count");
                med_combination.add(O_comb);

                //ListUtils.setDynamicHeight(lst_med_contains);
            }


            O_comb = new saltDetailModel();
            O_comb.setName("Other Molecule in combination with "+saltName);
            O_comb.setCount_combination("");
            O_comb.setprice("");
            O_comb.settotal_count("");
            O_comb.settype("heading");
            med_combination.add(O_comb);


            for (int i = 0; i < j_array.length(); i++) {

                JSONObject addressjson = (JSONObject) j_array.get(i);
                O_comb = new saltDetailModel();
                O_comb.setName(addressjson.getString("SaltCombination"));
                O_comb.setCount_combination(addressjson.getString("TotalCount"));
                O_comb.settype("Combination");
                med_combination.add(O_comb);

            }
            O_comb = new saltDetailModel();
            O_comb.setName("");
            O_comb.setCount_combination("");
            O_comb.setprice("");
            O_comb.settotal_count("View all " +Total_count+ " combinations");
            O_comb.settype("total_count");
            med_combination.add(O_comb);






            o_combinationadapter.notifyDataSetChanged();
            // ListUtils.setDynamicHeight(lst_med_combination);
        } catch (JSONException e) {
            e.toString();
        }
    }



    private void showsearchmedicine(String p_search_product) {
        Intent intent_buysearch = new Intent(this, BuySearchActivity.class);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("search_product", p_search_product);
        editor.commit();

        startActivity(intent_buysearch);
        finish();
    }

    public void Show_termscondition() {
        Intent Intenet_change = new Intent(this, TermsCondition.class);
        startActivity(Intenet_change);
    }

    public void Show_Return_cancel_policies() {
        Intent Intenet_change = new Intent(this, TermsCondition.class);
       startActivity(Intenet_change);
    }

    public void Show_Order_Transaction() {
        Intent Intenet_change = new Intent(this, Order_Transaction.class);
        startActivity(Intenet_change);
    }

    public void Show_cart() {
        if (nAddtocart_count > 0) {
            get_checkout();
        } else {
            f_alert_ok("Information", "Your cart is empty");
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

    private void get_checkout() {

        showPdialog("Loading. . .");

        String url = String.format(AppConfig.URL_GET_CHECKOUT, sMemberId);
        // new  CheckoutAsyncHttpTask().execute(url);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_single = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Success_checkout(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Error_checkout(error);
                    }
                });

        jsonObjReq_single.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //AppController.getInstance().addToRequestQueue(jsonObjReq_single);

        queue.add(jsonObjReq_single);
    }

    private void Success_checkout(JSONObject response) {
        try {
            Intent buycartcheckout_intenet = new Intent(this, NewCartSummary.class);
            buycartcheckout_intenet.putExtra("GotoMain", "true");
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("cart_productdetail", response.getJSONArray("ProductDetail").toString());
            editor.putString("cart_pricesummarylist", response.getJSONArray("PriceSummaryList").toString());

            globalVariable.setScartjson(response.getJSONArray("ProductDetail").toString());
            globalVariable.setSsummaryjson(response.getJSONArray("PriceSummaryList").toString());


            if (response.getString("PromoCode").toString().equals("null")) {
                //editor.putString("cart_promocode", "");
                globalVariable.setsPromoCode("");

            } else {
                //editor.putString("cart_promocode", response.getString("PromoCode").toString());
                globalVariable.setsPromoCode(response.getString("PromoCode").toString());

            }
            // editor.putString("cart_checkout_ParentClass", "BuyMainActivity");
            globalVariable.setParentClass("BuyMainActivity");
            editor.commit();

            startActivity(buycartcheckout_intenet);
            hidePDialog();
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            f_alert_ok("Error ", e.getMessage());
        }
    }

    private void Error_checkout(VolleyError error) {
        f_alert_ok("Error ", error.getMessage());
    }


    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }


            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void insert_salt_data_table(String productid, String productname) {
        Integer dbcount = db.getSearcedMedicineRowCount();


        int nFlag = Integer.parseInt(db.check_medicine_exist_search_table(Integer.valueOf(productid)));


        if (nFlag == 0) {
            if (dbcount == 5) {
                db.Delete_last_searched_medicine_data(1);
                db.Insert_search_data_table(productid, productname, "", "Salt", "", "", "", "Salt","false");
            } else {
                db.Insert_search_data_table(productid, productname, "", "Salt", "", "", "", "Salt","false");
            }
        }
    }


    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View groupView = listAdapter.getView(i, null, listView);
            groupView.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupView.getMeasuredHeight();


        }

    }
    @Override
    public void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);
        nAddtocart_count = Integer.parseInt(pref.getString("addtocart_count", "0"));

        createCartBadge(nAddtocart_count);
    }

    private void set_up_general_information_tab()
    {
        TabHost general_infromation_tabs = (TabHost) findViewById(R.id.dmTabHostSec);
        //important_advisory_txt=(TextView)findViewById(R.id.important_advisory_txt);
        lnrtest=(LinearLayout) findViewById(R.id.lnrtest);
        lnrSecond=(LinearLayout) findViewById(R.id.lnrSecond);
        lnrthird=(LinearLayout) findViewById(R.id.lnrthird);
        drug_molecule_linear_heading=(LinearLayout) findViewById(R.id.drug_molecule_linear_heading);

        general_infromation_tabs.setup();

        show_question_answer();
        show_molecule_interaction();
        show_Drug_interaction();

        TabHost.TabSpec tabpage1 = general_infromation_tabs.newTabSpec("one");
        tabpage1.setContent(R.id.question_scroll);
        tabpage1.setIndicator("General Information", this.getResources().getDrawable(R.drawable.orgbtn));
        TabHost.TabSpec tabpage2 = general_infromation_tabs.newTabSpec("two");
        tabpage2.setContent(R.id.second_layoutSec);

        tabpage2.setIndicator("Drug-Drug interaction", this.getResources().getDrawable(R.drawable.arrowdwn));
        // setTabColor(tabs);

        general_infromation_tabs.setFocusable(false);
        general_infromation_tabs.addTab(tabpage1);
        general_infromation_tabs.addTab(tabpage2);
        final TextView tv = (TextView) general_infromation_tabs.getTabWidget().getChildAt(general_infromation_tabs.getCurrentTab()).findViewById(android.R.id.title);
        tv.setTextColor(Color.WHITE);

        for(int i = 0; i < general_infromation_tabs.getTabWidget().getChildCount(); i++) {
            View xy = general_infromation_tabs.getTabWidget().getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tvx = (TextView)xy.findViewById(android.R.id.title);
            if(tvx == null) {
                continue;

            }
            tvx.setAllCaps(false);
            tvx.setTextColor(Color.parseColor("#bdbdbd"));
            xy.setBackgroundResource(R.drawable.tab_selector);
        }
    }
    private void show_question_answer()
    {
        try{
            lnrtest.removeAllViews();

            if(ja_objquestionAswer==null)
            {
                ja_objquestionAswer=new JSONArray("[]");
            }

            if (ja_objquestionAswer.length()>0) {
                for (int i = 0; i < ja_objquestionAswer.length(); i++) {
                    JSONObject obj_json = ja_objquestionAswer.getJSONObject(i);
                    View mLinearView = inflater.inflate(R.layout.question_answer_details, null);
                    final TextView txt_question = (TextView) mLinearView.findViewById(R.id.textViewItemName);
                    final TextView txt_answer = (TextView) mLinearView.findViewById(R.id.textViewItemPrice);

                    if(obj_json.getString("PharmacologyOption").equals("")||obj_json.getString("PharmacologyOption").equals("null"))
                    {
                        txt_question.setText("No Information available");
                    }else
                    {
                        //  question_Ans_available=true;
                        txt_question.setText(obj_json.getString("PharmacologyOption"));
                    }

                    if(obj_json.getString("PharmacologyDesc").equals("")||obj_json.getString("PharmacologyDesc").equals("null"))
                    {
                        txt_answer.setText("No Information available");
                    }else
                    {
                        txt_answer.setText(obj_json.getString("PharmacologyDesc"));
                    }


                    lnrtest.addView(mLinearView);

                }
            }else
            {
                TextView tv = new TextView(this);
                tv.setText("No Data Available");
                lnrtest.addView(tv);

            }
        }catch (JSONException e)
        {
            e.toString();
        }


    }
    private void show_molecule_interaction()
    {
        try{
            lnrthird.removeAllViews();
            if(ja_objMoleculeInteraction==null)
            {
                ja_objMoleculeInteraction=new JSONArray("[]");
            }
            if (ja_objMoleculeInteraction.length()>0 ) {
                drug_molecule_linear_heading.setVisibility(View.VISIBLE);
                for (int i = 0; i < ja_objMoleculeInteraction.length(); i++) {

                    JSONObject obj_json = ja_objMoleculeInteraction.getJSONObject(i);
                    View mLinearView = inflater.inflate(R.layout.molecule_interaction_item, null);
                    final TextView mol_heading = (TextView) mLinearView.findViewById(R.id.mol_heading);
                    final TextView mol_details = (TextView) mLinearView.findViewById(R.id.mol_details);

                    if(obj_json.getString("DrugInteractionName").equals("")||obj_json.getString("DrugInteractionName").equals("null"))
                    {
                        mol_heading.setText("No Information available");
                    }else
                    {
                        mol_heading.setText(obj_json.getString("DrugInteractionName"));
                    }

                    if(obj_json.getString("DrugInteractionDesc").equals("")||obj_json.getString("DrugInteractionDesc").equals("null"))
                    {
                        mol_details.setText("No Information available");
                    }else
                    {
                        mol_details.setText(obj_json.getString("DrugInteractionDesc"));
                    }
                    lnrthird.addView(mLinearView);

                }
            }else
            {

                drug_molecule_linear_heading.setVisibility(View.INVISIBLE);
            }
        }catch (JSONException e)
        {
            e.toString();
        }

    }
    private void show_Drug_interaction()
    {

        try{
            lnrSecond.removeAllViews();
            if(ja_objDruginteraction==null)
            {
                ja_objDruginteraction=new JSONArray("[]");
            }


            if (ja_objDruginteraction.length()>0) {

                for (int i = 0; i < ja_objDruginteraction.length(); i++) {

                    JSONObject obj_json = ja_objDruginteraction.getJSONObject(i);

                    View mLinearView = inflater.inflate(R.layout.drug_interaction_detail, null);
                    final TextView drug_heading = (TextView) mLinearView.findViewById(R.id.drug_heading);
                    final TextView drug_detail = (TextView) mLinearView.findViewById(R.id.drug_detail);

                    if(obj_json.getString("DrugDrugInteractionDesc").equals("")||obj_json.getString("DrugDrugInteractionDesc").equals("null"))
                    {
                        drug_heading.setText("No Information available");
                    }else
                    {
                        drug_heading.setText(obj_json.getString("DrugDrugInteractionDesc"));
                    }


                    if(obj_json.getString("DrugInteractionMoleculeDetail").equals("")||obj_json.getString("DrugInteractionMoleculeDetail").equals("null"))
                    {
                        drug_detail.setText("No Information available");
                    }else
                    {
                        drug_detail.setText(obj_json.getString("DrugInteractionMoleculeDetail"));
                    }

                    lnrSecond.addView(mLinearView);

                }
            }else
            {
                TextView tv = new TextView(this);
                tv.setText("No Data Available");
                lnrSecond.addView(tv);
            }
        }catch (JSONException e)
        {
            e.toString();
        }




    }

}

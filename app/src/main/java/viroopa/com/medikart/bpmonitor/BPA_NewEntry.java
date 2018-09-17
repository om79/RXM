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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
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
import com.github.siyamed.shapeimageview.CircularImageView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import viroopa.com.medikart.MainActivity;

import viroopa.com.medikart.R;
import viroopa.com.medikart.adapter.AD_ComboList;
import viroopa.com.medikart.app.AppConfig;
import viroopa.com.medikart.app.AppController;
import viroopa.com.medikart.common.Add_Doctor_Dialog;
import viroopa.com.medikart.common.Change_member_Dialog;
import viroopa.com.medikart.common.Memberlist;
import viroopa.com.medikart.common.numerdialog;
import viroopa.com.medikart.helper.SQLiteHandler;
import viroopa.com.medikart.helper.SqliteBPHandler;
import viroopa.com.medikart.model.M_doctorlist;
import viroopa.com.medikart.model.M_memberlist;
import viroopa.com.medikart.model.M_objectItem;

public class BPA_NewEntry extends AppCompatActivity implements numerdialog.OnNumberDialogDoneListener ,
		                                                       DatePickerDialog.OnDateSetListener,
		                                                       Change_member_Dialog.OnMemberSelectListener,
		                                                       Add_Doctor_Dialog.OnDoctorSelectListener
{

	private static final String TAG = "BPA_NewEntry";

	NumberPicker numweight;

	@BindView(R.id.bottom_navigation)
	AHBottomNavigation bottomNavigation;


	@BindView(R.id.mainlayout)
	RelativeLayout _mainlayout;

	@Nullable
	@BindView(R.id.analysisMessage)
	TextView _analysisMessage;

	@Nullable
	@BindView(R.id.txt_weight_h)
	TextView _txt_weight_h;



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

	@BindView(R.id.txtbpdate)
	EditText _txtbpDate;

	@BindView(R.id.txtselectDate)
	Button _txtseldate;
	@Nullable
	@BindView(R.id.btn_Saves)
	Button btn_Saves;
	@Nullable
	@BindView(R.id.btn_cancel)
	Button btn_cancel;
	@BindView(R.id.spArrhythnia)
	Spinner _spnrArrhythnia;
	@BindView(R.id.spnrPosition)
	Spinner _spnrPosition;
	@BindView(R.id.spnrPositiontype)
	Spinner _spnrPositiontype;
	@BindView(R.id.edtsystolic)
	TextView _edtsystolic;
	@BindView(R.id.edtdiastolic)
	TextView _edtdiastolic;
	@BindView(R.id.edtpulse)
	TextView _edtpulse;
	@BindView(R.id.edtweight)
	TextView _edtweight;
	@BindView(R.id.edtcomment)
	EditText _edtcomment;
	@BindView(R.id.txtDate)
	TextView txtDate;
	@BindView(R.id.btnanalysis)
	Button btnanalysis;
	M_objectItem[] mobjectItemDoctor = null;
	String sNewDoctorName, sNewClinicName;
	Integer nNewCityId=11;
	ArrayAdapter<String> dataDoctorAdapter;
	ArrayList<M_doctorlist> doctorlists = new ArrayList<M_doctorlist>();
	ArrayList<String> doctorname = new ArrayList<String>();
	String[] bpCondition;
	String[] Positions,PositionTypes;
	List<M_memberlist> MemberData = new ArrayList<M_memberlist>();
	JSONArray jsonarray;
	ArrayList<String> memberList;
	Boolean AddDoctorShowVisible=false;
	ArrayList<Memberlist> CurrentmemberList;
	DateFormat dateFormat_month_in_words = new SimpleDateFormat("LLLL dd,yyyy");
	DateFormat dateFormat_orginal = new SimpleDateFormat("yyyy-MM-dd");
	AppController globalVariable;
	SharedPreferences pref ;
	private Dialog dlg;
	private Menu objMemberMenu;
	private Integer sPosition = 0;
	private Integer sPositionType = 0;
	private String sUUID = "";
	private EditText txtdoctorname;
	private Button sp_doctor;
	private EditText clinicname;
	private Boolean EditMode = false;
	private Integer EditBpId = 0;
	private Integer nRelationshipId;
	private boolean onResumecalled=false;
	private Integer wgt;
	private Integer get_last_weight;
	private Integer Date;
	private Integer Month;
	private Integer Year;
	private String setBPdate;
	private String setDate;
	private View positiveAction;
	private ProgressDialog pDialog;
	private SQLiteHandler db_main;
	private SqliteBPHandler db_bp;
	private String weight;
	private String weight_unit;

	private Date current_date = Calendar.getInstance().getTime();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdYear = new SimpleDateFormat("yyyy");
	private SimpleDateFormat sdMonth = new SimpleDateFormat("MM");
	private SimpleDateFormat sdDay = new SimpleDateFormat("dd");
	private  int nYear,nMonth, Nday;

	private String bp_time;
	private String bptimehr;
	private String kg;
	private String lb;
	private double valueweight = 0;

	private Integer nArrhythnia = 0;
	private Integer inumerpicker = 0;
	//  private  Spinner spnrobjFamilyMember;
	private String sMemberId,setting_get_def_site,setting_get_def_pos,setting_last_enterd_values,setting_weight_unit;

	//@OnAutoCompleteItemClick
	@OnItemSelected(R.id.spArrhythnia)
	void onItemSelected(int position) {
		//	Toast.makeText(this, "Selected position " + position + "!", Toast.LENGTH_SHORT).show();


	}

	@Override
	public void onSelectDoctor(String Doc_id, String Doc_name,String email) {

		Add_Doctor_Dialog myDiag = Add_Doctor_Dialog.newInstance("add_doctor");
		myDiag.show(getFragmentManager(), "Diag");
	}


	@OnClick(R.id.txtDate)
	void enterDate() {


		try {
			current_date = sdf.parse(setDate);

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


		DatePickerDialog  dpd = DatePickerDialog.newInstance(
				this,
				nYear,
				nMonth,
				Nday
		);
		dpd.show(getFragmentManager(), "Datepickerdialog");

		dpd.setMaxDate(Calendar.getInstance());;

	}


	@OnClick(R.id.edtweight)
	void enterweight() {
		int iSystolic = 50 ;
		try{
			iSystolic= Integer.parseInt(_edtweight.getText().toString().trim());
		}catch(NumberFormatException ex){ // handle your exception

		}
		numerdialog myDiag = numerdialog.newInstance(1,iSystolic,1,200,"edtweight","WEIGHT");
		myDiag.show(getFragmentManager(), "Diag");
	}

	@OnClick(R.id.edtsystolic)
	void entersystolic() {

		int iSystolic = 120 ;
		try{
			iSystolic= Integer.parseInt(_edtsystolic.getText().toString().trim());
		}catch(NumberFormatException ex){ // handle your exception

		}
		numerdialog myDiag = numerdialog.newInstance(1,iSystolic,50,250,"edtsystolic","SYSTOLIC");
		myDiag.show(getFragmentManager(), "Diag");
	}

	@Override
	public void onDone(int value,String sClass) {


		switch (sClass) {

			case "edtdiastolic":
				_edtdiastolic.setText(Integer.toString(value));
				break;
			case "edtpulse":
				_edtpulse.setText(Integer.toString(value));
				break;
			case "_txtgoalSystolic":
				_txtgoalSystolic.setText(Integer.toString(value));
				break;
			case "_txtgoalDiastolic":
				_txtgoalDiastolic.setText(Integer.toString(value));
				break;
			case "_txtgoalweight":
				_txtgoalweight.setText(Integer.toString(value));
				break;
			case "edtweight":
				_edtweight.setText(Integer.toString(value));
				break;
			case "edtsystolic":
				_edtsystolic.setText(Integer.toString(value));
				break;


		}


	}


	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

		try {
			current_date = getDate_Format(year,monthOfYear,dayOfMonth);
			setDate=dateFormat_orginal.format(current_date);

		} catch (Exception e) {
		}
		txtDate.setText(dateFormat_month_in_words.format(current_date));

	}



	@OnClick(R.id.edtdiastolic)
	void enterdiastolic() {
		int iSystolic = 80 ;
		try{
			iSystolic= Integer.parseInt(_edtdiastolic.getText().toString().trim());
		}catch(NumberFormatException ex){ // handle your exception

		}
		numerdialog myDiag = numerdialog.newInstance(1,iSystolic,30,200,"edtdiastolic","DIASTOLIC");
		myDiag.show(getFragmentManager(), "Diag");
	}

	@OnClick(R.id.edtpulse)
	void enterpulse() {

		int iSystolic = 72 ;
		try{
			iSystolic= Integer.parseInt(_edtpulse.getText().toString().trim());
		}catch(NumberFormatException ex){ // handle your exception

		}
		numerdialog myDiag = numerdialog.newInstance(1,iSystolic,1,200,"edtpulse","PULSE");
		myDiag.show(getFragmentManager(), "Diag");
	}



	private void gotohome() {
		try {
			Intent Intenet_main = new Intent(this, MainActivity.class);
			startActivity(Intenet_main);

		} catch (Exception ex) {

		}

	}


	@OnClick(R.id.btnClear)
	void cleardata() {
		try
		{
			_edtsystolic.setText("");

			_edtpulse.setText("");
			_edtweight.setText("");
			_edtcomment.setText("");

			_spnrPosition.setSelection(0);
			_spnrPositiontype.setSelection(0);
			_spnrArrhythnia.setSelection(0);

			_edtdiastolic.setText("");

			_edtpulse.setText("");

			//fill_Combo();
			show_default_date();
			Toast.makeText(this, "Entry cleared", Toast.LENGTH_LONG);
		} catch (Exception ex) {

		}
	}


	@OnClick(R.id.btnanalysis)
	void btnanalysis() {
		try {

			Intent Intenet_dm = new Intent(BPA_NewEntry.this, BPA_AnalysisDisplayActivity.class);
			startActivity(Intenet_dm);
			finish();

		} catch (Exception ex) {
			String s = String.valueOf(ex);
		}
	}



	@OnClick(R.id.btnSave)
	public void btnSaves() {


		try {
			if (saverecords()) {

				GlucoseCalculationDialog(Integer.parseInt(_edtsystolic.getText().toString()),
						82);
			}
			;

		} catch (Exception ex) {
			ex.toString();
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
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bp_entry);

		ButterKnife.bind(this);
		pref = this.getSharedPreferences("Global", MODE_PRIVATE);
		globalVariable = (AppController) getApplicationContext();
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.rxlogo);

		initialize_bottom_bar();


		memberList = new ArrayList<String>();
		CurrentmemberList = new ArrayList<Memberlist>();

		db_main = SQLiteHandler.getInstance(this);
		db_bp = SqliteBPHandler.getInstance(this);

		pDialog = new ProgressDialog(this);



		bpCondition = getResources().getStringArray(R.array.array_bpcondition);



		pDialog.setCancelable(false);

		getIntenet();



		show_analysis_button();




		ArrayAdapter ConditionAdapter = new ArrayAdapter<String>(this,
				R.layout.rxspinner_simple_text_layout
				,
				bpCondition);

		 Positions = getResources().getStringArray(R.array.array_position);
		ArrayAdapter PositionAdapter = new ArrayAdapter<String>(this,
				R.layout.rxspinner_simple_text_layout
				,
				Positions);

		 PositionTypes = getResources().getStringArray(R.array.array_positiontype);
		ArrayAdapter PositionTypesAdpter = new ArrayAdapter<String>(this,
				R.layout.rxspinner_simple_text_layout
				,
				PositionTypes);


		_spnrPosition.setAdapter(PositionAdapter);
		_spnrPositiontype.setAdapter(PositionTypesAdpter);
		_spnrArrhythnia.setAdapter(ConditionAdapter);


		show_default_date();
		get_defaults();
		getIntenet();

		if(setting_last_enterd_values.equals("true")) {
			getLastEntry();
		}


		Edit_data();
		if (EditMode == true) {
			Cursor cursor = db_bp.getAllBPMonitarDataOnBpID(EditBpId);

			if (cursor.moveToFirst()) {
				do {
					String ed_weight = cursor.getString(cursor.getColumnIndex("weight"));
					String ed_diastolic = cursor.getString(
							cursor.getColumnIndex("diastolic"));
					String ed_systolic = cursor.getString(cursor.getColumnIndex("systolic"));
					String ed_pulse = cursor.getString(cursor.getColumnIndex("pulse"));
					String ed_comment = cursor.getString(cursor.getColumnIndex("comments"));
					String ed_position = cursor.getString(cursor.getColumnIndex("position"));
					String ed_site = cursor.getString(cursor.getColumnIndex("body_part"));
					String ed_dateOfBirth = cursor.getString(
							cursor.getColumnIndex("bp_date"));
					String ed_arrthythmia = cursor.getString(
							cursor.getColumnIndex("arrthythmia"));


					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					setDate = ed_dateOfBirth;
					sUUID = cursor.getString(cursor.getColumnIndex("UUID"));


					try {
						current_date=dateFormat_orginal.parse(dateFormat_orginal.format(setDate));


					} catch (Exception e) {
					}

					if (ed_arrthythmia.equals("1")) {

						//nArrhythnia = "Arrhythnia";
						nArrhythnia = 1;


					} else if (ed_arrthythmia.equals("2")) {

						// nArrhythnia = "Gestational";

						nArrhythnia = 2;


					} else if (ed_arrthythmia.equals("3")) {

						//nArrhythnia= "Hypertension";
						nArrhythnia = 3;


					} else if (ed_arrthythmia.equals("0")) {


						nArrhythnia = 0;

					}

					for(int i=0; i < _spnrPosition.getAdapter().getCount(); i++) {
						if (ed_site.equals(_spnrPosition.getAdapter().getItem(i).toString())) {
							_spnrPosition.setSelection(i);
							break;
						}
					}

					for(int c=0; c < _spnrPositiontype.getAdapter().getCount(); c++) {
						if (ed_position.equals(_spnrPosition.getAdapter().getItem(c).toString())) {
							_spnrPositiontype.setSelection(c);
							break;
						}
					}


					_edtweight.setText(ed_weight);
					_edtsystolic.setText(ed_systolic);
					_edtdiastolic.setText(ed_diastolic);

					_edtpulse.setText(ed_pulse);
					_edtcomment.setText(ed_comment);
					_txtseldate.setText(ed_dateOfBirth);
					txtDate.setText(ed_dateOfBirth);
				}
				while (cursor.moveToNext());
			}
			cursor.close();

		} else {

			if (get_last_weight.equals(1)) {
				String sgetWeight = Integer.toString(wgt);
				_edtweight.setText(sgetWeight);
			}

		}




		return;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		try {

			getMenuInflater().inflate(R.menu.bp_menu, menu);
			this.objMemberMenu = menu;

			LayoutInflater mInflater = LayoutInflater.from ( this );
			View mCustomView = mInflater.inflate ( R.layout.circula_image, null );

			objMemberMenu.findItem ( R.id.circlularImage ).setActionView ( mCustomView );


			ImageLoad (pref.getString("UserName","Me"),pref.getString("imagename",""));

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void onSelectMember(String Relationship_id, String name,String Imagename)
	{
		globalVariable.setRealationshipId(Relationship_id);
		nRelationshipId = Integer.parseInt(Relationship_id);

		SharedPreferences.Editor editor = pref.edit();
		editor.putString("UserName", name);
		editor.commit();

		show_analysis_button();

		ImageLoad(name,Imagename);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement

		if (id == R.id.action_settings) {
			//meber_show();
			Intent intent_network = new Intent(BPA_NewEntry.this, BPA_MonitorSetting.class);
			startActivity(intent_network);
			//finish();
			//showPdialog("loading.........");


			return true;
		}


		return super.onOptionsItemSelected(item);
	}


//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//
//		// Check current message count
//		//boolean haveMessages = mMessageCount != 0;
//
//		// Set 'delete' menu item state depending on count
//		MenuItem deleteItem = menu.findItem(R.id.circlularImage);
//		deleteItem.setTitle("prakash k bhandary");
//		//deleteItem.setEnabled(haveMessages);
//
//
//		MenuItem actionViewItem = menu.findItem(R.id.miActionButton);
//		// Retrieve the action-view from menu
//		View v = MenuItemCompat.getActionView(actionViewItem);
//		// Find the button within action-view
//		Button b = (Button) v.findViewById(R.id.btnCustomAction);
//		b.setText("prakash");
//		// Handle button click here
//		return super.onPrepareOptionsMenu(menu);
//
//	}

	public void profileclick(MenuItem mi) {
		// handle click here


		try {
			Toast.makeText(this, "prakash", Toast.LENGTH_LONG).show();
			//finish ( );

		} catch (Exception e) {

		}
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


	private void f_alert_ok(String p_title,
							String p_msg) {
		new AlertDialog.Builder(this)
				.setTitle(p_title)
				.setMessage(p_msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
										int which) {
						dialog.dismiss();
					}
				}).show();
	}







	private void Savedata() {

		try {

			Integer nbptimehr = Integer.parseInt(bptimehr);
			String SUUID = UUID.randomUUID().toString();
			Long IMEINO= 00000000000l;

			double valuekgweight = 0;
			double valuelbweight = 0;

			valueweight = Double.parseDouble(_edtweight.getText().toString());

			if (setting_weight_unit.equals("kg")) {



				kg = Double.toString(valueweight);
				valuekgweight = Math.round(valueweight * 2.20462);
				lb = Double.toString(valuekgweight);


			} else {
				lb = Double.toString(valueweight);
				valuelbweight = Math.round(valueweight *0.45359);
				kg = Double.toString(valuelbweight);

			}



			db_bp.addBPDetails(
					sMemberId,
					_spnrPosition.getSelectedItem().toString(),
					_spnrPositiontype.getSelectedItem().toString(),
					_edtsystolic.getText().toString().trim(),
					_edtdiastolic.getText().toString().trim(),
					_edtpulse.getText().toString().trim(),
					_edtweight.getText().toString(),
					setting_last_enterd_values,
					nArrhythnia,
					_edtcomment.getText().toString().trim(),
					setDate,
					bp_time,
					nbptimehr,
					kg, lb,
					nRelationshipId,
					IMEINO,
					SUUID);

			{
				String sRelationshipId = String.valueOf(nRelationshipId);

				Map<String, String> params = new HashMap<String, String>();

				params.put("user_id", sMemberId);
				params.put("body_part", _spnrPosition.getSelectedItem().toString());
				params.put("position", _spnrPositiontype.getSelectedItem().toString());
				params.put("systolic", _edtsystolic.getText().toString().trim());
				params.put("diastolic", _edtdiastolic.getText().toString().trim());
				params.put("pulse", _edtpulse.getText().toString().trim());
				params.put("weight", _edtweight.getText().toString().trim());
				params.put("weight_unit", setting_last_enterd_values);
				// prakash
				String arrthythmia = "0";

				params.put("arrthythmia", arrthythmia);
				params.put("comments", _edtcomment.getText().toString().trim());
				params.put("bp_date", setDate);
				params.put("bp_time", bp_time);
				params.put("bptimehr", sMemberId);
				params.put("Relationship_ID", sRelationshipId);
				params.put("lb", lb);
				params.put("kg", kg);
				params.put("IMEI", String.valueOf(IMEINO));
				params.put("UUID", SUUID);
				JSONObject jparams = new JSONObject(params);


				String I_Type = "Post";
				String Controller = AppConfig.URL_POST_ADDBPDATAJson;
				String Parameter = "";
				String JsonObject = String.valueOf(jparams);
				String Created_Date = setDate;

				String F_KEY_UPLOAD_DOWNLOAD = "true";
				String F_KEY_SYNCMEMBERID = sMemberId;
				String F_KEY_MODULE_NAME = "BP";
				String F_KEY_MODE = "A";
				String F_KEY_ControllerName = "Monitor";
				String F_KEY_MethodName = "BPMonitorBAL";

				db_bp.InsertSyncTable(I_Type,
						Controller,
						Parameter,
						JsonObject,
						Created_Date,
						SUUID,
						F_KEY_UPLOAD_DOWNLOAD,
						F_KEY_SYNCMEMBERID,
						F_KEY_MODULE_NAME,
						F_KEY_MODE,
						F_KEY_ControllerName,
						F_KEY_MethodName,
						IMEINO);

				globalVariable.setRealationshipId(sRelationshipId);
				globalVariable.setWeight(weight);
				globalVariable.setWeightType(setting_last_enterd_values);
			}
			f_alert_ok("Success", "Data Saved successfully");

			cleardata();

			if(setting_last_enterd_values.equals("true")) {
				getLastEntry();
			}

			btnanalysis.setVisibility(View.VISIBLE);
		} catch (Exception E) {
			f_alert_ok("Error", "Error" + E.getMessage());
		}

	}






	private void hideDialog() {
		if (pDialog.isShowing()) {
			pDialog.dismiss();
		}
	}


	private void getIntenet() {
		sMemberId = pref.getString("memberid", "");

		get_last_weight = 0;
		wgt = 0;


		nRelationshipId = pref.getInt("RelationshipId", 0);
		if (globalVariable.getRealationshipId() != null) {
			nRelationshipId = Integer.parseInt(globalVariable.getRealationshipId());
		} else {
			nRelationshipId = 8;
		}

	}


	private void Edit_data() {
		Intent int_edit = this.getIntent();

		String tst = int_edit.getStringExtra("Bpid");
		if (int_edit.getStringExtra("Bpid") != null) {
			EditMode = true;
			EditBpId = Integer.parseInt(int_edit.getStringExtra("Bpid"));

		}

	}

	private void EditSavedata() {

		try {


			Long IMEINO;
			String myimeno = globalVariable.getIMEInumber().toString();
			if (myimeno.equals("")) {
				IMEINO = 00000000000l;
			} else {
				IMEINO = Long.parseLong(myimeno);
			}



			Integer nbptimehr = Integer.parseInt(bptimehr);


			double valuekgweight = 0;
			double valuelbweight = 0;

			valueweight = Double.parseDouble(_edtweight.getText().toString());

			if (setting_last_enterd_values.equals("kg")) {

				kg = Double.toString(valueweight);
				valuekgweight = Math.round(valueweight * 0.453592);
				lb = Double.toString(valuekgweight);


			} else {
				lb = Double.toString(valueweight);
				valuelbweight = Math.round(valueweight * 2.20462);
				kg = Double.toString(valuelbweight);

			}




			Integer RelationshipId = nRelationshipId;
			db_bp.EditSaveBPDetails(sMemberId,
					_spnrPosition.getSelectedItem().toString(),
					_spnrPositiontype.getSelectedItem().toString(),
					_edtsystolic.getText().toString().trim(),
					_edtdiastolic.getText().toString().trim(),
					_edtpulse.getText().toString().trim(),
					_edtweight.getText().toString(),
					setting_last_enterd_values,
					nArrhythnia,
					_edtcomment.getText().toString().trim(),
					setDate,
					bp_time,
					nbptimehr,
					kg, lb,
					nRelationshipId,
					IMEINO,
					EditBpId,
					sUUID);


			{
				String sRelationshipId = String.valueOf(RelationshipId);

				Map<String, String> params = new HashMap<String, String>();

				params.put("user_id", sMemberId);
				params.put("body_part", _spnrPosition.getSelectedItem().toString());
				params.put("position", _spnrPositiontype.getSelectedItem().toString());
				params.put("systolic", _edtsystolic.getText().toString().trim());
				params.put("diastolic", _edtdiastolic.getText().toString().trim());
				params.put("pulse", _edtpulse.getText().toString().trim());
				params.put("weight", _edtweight.getText().toString());
				params.put("weight_unit", weight_unit);
				// prakash
				String arrthythmia = "0";

				params.put("arrthythmia", arrthythmia);
				params.put("comments", _edtcomment.getText().toString().trim());
				params.put("bp_date", setDate);
				params.put("bp_time", bp_time);
				params.put("bptimehr", sMemberId);
				params.put("Relationship_ID", sRelationshipId);
				params.put("lb", lb);
				params.put("kg", kg);
				params.put("IMEI", String.valueOf(IMEINO));
				params.put("UUID", sUUID);
				JSONObject jparams = new JSONObject(params);


				String I_Type = "Post";
				String Controller = AppConfig.URL_POST_ADDBPDATAJson;
				String Parameter = "";
				String JsonObject = String.valueOf(jparams);
				String Created_Date = setBPdate;

				String F_KEY_UPLOAD_DOWNLOAD = "true";
				String F_KEY_SYNCMEMBERID = sMemberId;
				String F_KEY_MODULE_NAME = "BP";
				String F_KEY_MODE = "E";
				String F_KEY_ControllerName = "Monitor";
				String F_KEY_MethodName = "BPMonitorBAL";
				db_bp.InsertSyncTable(I_Type,
						Controller,
						Parameter,
						JsonObject,
						Created_Date,
						sUUID,
						F_KEY_UPLOAD_DOWNLOAD,
						F_KEY_SYNCMEMBERID,
						F_KEY_MODULE_NAME,
						F_KEY_MODE,
						F_KEY_ControllerName,
						F_KEY_MethodName,
						IMEINO);

				setBPdate = _txtseldate.getText().toString();

				globalVariable.setRealationshipId(sRelationshipId);
				globalVariable.setWeight(_edtweight.getText().toString());
				globalVariable.setWeightType(setting_last_enterd_values);
			}

			f_alert_ok("Success", "Data Saved successfully");
			cleardata();
			if(setting_last_enterd_values.equals("true")) {
				getLastEntry();
			}


		} catch (Exception E) {
			f_alert_ok("Error", "Error" + E.getMessage());
		}

	}






	private void getLastEntry() {



		Cursor Cursor_Last = db_bp.getLastReadingBPMonitarData(sMemberId,
				nRelationshipId);
		int count = Cursor_Last.getCount();
		if ((Cursor_Last != null) || (Cursor_Last.getCount() > 0)) {
			if (Cursor_Last.moveToFirst()) {
				Cursor_Last.moveToFirst();


				String systollic = String.valueOf(
						Cursor_Last.getString(Cursor_Last.getColumnIndex(
								"systolic")));
				String diastollic = String.valueOf(
						Cursor_Last.getString(Cursor_Last.getColumnIndex(
								"diastolic")));
				String pulse = String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex(
						"pulse")));
				String weightKg = String.valueOf(
						Cursor_Last.getString(Cursor_Last.getColumnIndex(
								"kg")));

				String weightLb = String.valueOf(
						Cursor_Last.getString(Cursor_Last.getColumnIndex(
								"lb")));
				String weightunit = String.valueOf(
						Cursor_Last.getString(Cursor_Last.getColumnIndex(
								"weight_unit")));
				String site = String.valueOf(Cursor_Last.getString(Cursor_Last.getColumnIndex(
						"position")));
				String position = String.valueOf(
						Cursor_Last.getString(Cursor_Last.getColumnIndex(
								"body_part")));

				Integer condition =
						Cursor_Last.getInt(Cursor_Last.getColumnIndex(
								"arrthythmia"));

				if (!systollic.equals("null")) {
					_edtsystolic.setText(systollic);
				}
				if (!diastollic.equals("null")) {
					_edtdiastolic.setText(diastollic);
				}
				if (!pulse.equals("null")) {
					_edtpulse.setText(pulse);
				}


				if (setting_weight_unit.equals("kg")) {

					globalVariable.setWeightType(setting_weight_unit);

					if (!weightKg.equals("null")) {
						globalVariable.setWeight(weightKg);
						get_last_weight = Integer.parseInt(weightKg);
						_edtweight.setText(weightKg);
					}

				}else {
					globalVariable.setWeightType(setting_weight_unit);

					if (!weightLb.equals("null")) {
						globalVariable.setWeight(weightLb);
						get_last_weight = Integer.parseInt(weightLb);
						_edtweight.setText(weightLb);
					}
				}

				if (!position.equals("null")) {
					globalVariable.setBp_Default_Pos(position);
					_spnrPosition.setSelection( Arrays.asList(Positions).indexOf(position));
				}
				if (!site.equals("null")) {
					globalVariable.setBp_Default_Site(site);
					_spnrPositiontype.setSelection( Arrays.asList(PositionTypes).indexOf(site));
				}

				if (condition!=null) {
					_spnrArrhythnia.setSelection(condition);
				}

			}
		}
	}

	private void show_default_date() {
		Date Current_formatted_date = null;


		try {
			current_date= Calendar.getInstance().getTime();
			setDate=dateFormat_orginal.format(current_date);
			Current_formatted_date = dateFormat_orginal.parse(setDate);

		} catch (Exception e) {
		}
		txtDate.setText(dateFormat_month_in_words.format(Current_formatted_date));
	}

	protected void GlucoseCalculationDialog(Integer Systolic,
											Integer Diastolic) {

		LayoutInflater inflater = LayoutInflater.from(BPA_NewEntry.this);
		AlertDialog.Builder builder = new AlertDialog.Builder(BPA_NewEntry.this);

		final View dialogview = inflater.inflate(R.layout.bp_data_analysis, null);

		final Button _cancel = ButterKnife.findById(dialogview, R.id.btn_cancel);
		final TextView _analysisMessage = ButterKnife.findById(dialogview, R.id.analysisMessage);
		final Button _Save = ButterKnife.findById(dialogview, R.id.btn_Saves);


		builder.setView(dialogview);
		final Dialog dlg = builder.create();
		if (Systolic <= 90 || Diastolic <= 60) {
			_analysisMessage.setText(" Low Blood Pressure");
			// TextView.setTextColor(Color.GREEN);
		}
		//  else if(Systolic<=120 && Diastolic<=80)
		else if ((Systolic >= 91 && Systolic <= 120) || (Diastolic >= 61 && Diastolic < 80)) {
			_analysisMessage.setText(" Normal Blood Pressure");
			// TextView.setTextColor(Color.GREEN);
		} else if ((Systolic >= 120 && Systolic <= 139) || (Diastolic >= 80 && Diastolic <= 89)) {
			_analysisMessage.setText("Prehypertension Blood Pressure ");
			//TextView.setTextColor(Color.YELLOW);
		} else if ((Systolic >= 140 && Systolic <= 159) || (Diastolic >= 90 && Diastolic <= 99)) {
			_analysisMessage.setText("High Blood Pressure(Hypertension) Stage 1");
			// TextView.setTextColor(Color.parseColor("#E69F1A"));
		} else if (Systolic >= 160 || Diastolic >= 100) {
			_analysisMessage.setText("High Blood Pressure(Hypertension) Stage 2");
			//TextView.setTextColor(Color.parseColor("#DC6B1A"));;
		} else if (Systolic >= 180 || Diastolic >= 110) {
			_analysisMessage.setText("Hypertensive Crisis(Emergency care needed)");
			//TextView.setTextColor(Color.RED);
		} else {
			_analysisMessage.setText("Entries are not Valid");

		}


		_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});

		_Save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (EditMode == true) {
					EditSavedata();
				} else {
					Savedata();
				}
				dlg.dismiss();
			}
		});


		dlg.show();
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



		show_previous_goal_data();
		dlg = builder.create();

		_txtgoalSystolic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				int iSystolic = 120 ;
				try{
					iSystolic= Integer.parseInt(_txtgoalSystolic.getText().toString().trim());
				}catch(NumberFormatException ex){ // handle your exception
				}
				numerdialog myDiag = numerdialog.newInstance(1,iSystolic,50,250,"_txtgoalSystolic","SYSTOLIC");
				myDiag.show(getFragmentManager(), "Diag");
			}
		});

		_txtgoalDiastolic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				int iSystolic = 80 ;
				try{
					iSystolic= Integer.parseInt(_txtgoalDiastolic.getText().toString().trim());
				}catch(NumberFormatException ex){ // handle your exception

				}
				numerdialog myDiag = numerdialog.newInstance(1,iSystolic,30,500,"_txtgoalDiastolic","DIASTOLIC");
				myDiag.show(getFragmentManager(), "Diag");
			}
		});

		_txtgoalweight.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				int iSystolic = 50 ;
				try{
					iSystolic= Integer.parseInt(_txtgoalweight.getText().toString().trim());
				}catch(NumberFormatException ex){ // handle your exception

				}
				numerdialog myDiag = numerdialog.newInstance(1,iSystolic,1,200,"_txtgoalweight","WEIGHT");
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
							Toast.makeText(BPA_NewEntry.this,
									"Please select weight",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(BPA_NewEntry.this,
								"Please enter Diastolic",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(BPA_NewEntry.this,
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
			//String sGoalWeightunit=spnrgoalWeightType.getSelectedItem().toString();



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






			db_bp.deleteGoalBPDetails(sMemberId, nRelationshipId);

			db_bp.InsertBPGOALTable(sMemberId,
					nRelationshipId,
					sWEIGHT,
					sGoalWeightunit,
					sGoalSystolic,
					sGoalDiastolic,
					kg,
					lb,
					"");




			f_alert_ok("Goal", "Goal Saved");


		} catch (Exception E) {
			f_alert_ok("Error", "Error" + E.getMessage());
		}

	}
	private  void show_previous_goal_data()
	{
		Cursor cursor_data;
		cursor_data = db_bp.getgoalBPMonitarData(Integer.parseInt(sMemberId),
				nRelationshipId);

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

				_txtgoalweightunit.setText(setting_weight_unit);
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
			_txtgoalweightunit.setText(setting_weight_unit);
		}
	}




	private void fill_Combo() {
		try {
			Cursor cursorCondition = db_bp.getAllCondition_datat();
			if (cursorCondition != null) {
				Integer i = cursorCondition.getCount();

				bpCondition = new String[cursorCondition.getCount()];

				if (cursorCondition.moveToFirst()) {
					do {

						bpCondition[cursorCondition.getInt(cursorCondition.getColumnIndex(
								"condition_id"))] =
								cursorCondition.getString(cursorCondition.getColumnIndex(
										"conditiondesc"));

					}
					while (cursorCondition.moveToNext());
				}

				cursorCondition.close();
				ArrayAdapter ConditionAdapter = new ArrayAdapter<String>(this,
						R.layout.rxspinner_simple_text_layout
						,
						bpCondition);
				_spnrArrhythnia.setAdapter(ConditionAdapter);

			}
			String[] Positions = getResources().getStringArray(R.array.array_position);
			ArrayAdapter PositionAdapter = new ArrayAdapter<String>(this,
					R.layout.rxspinner_simple_text_layout
					,
					Positions);

			String[] PositionTypes = getResources().getStringArray(R.array.array_positiontype);
			ArrayAdapter PositionTypesAdpter = new ArrayAdapter<String>(this,
					R.layout.rxspinner_simple_text_layout
					,
					PositionTypes);


			_spnrPosition.setAdapter(PositionAdapter);
			_spnrPositiontype.setAdapter(PositionTypesAdpter);
		} catch (Exception ex) {
			String s = String.valueOf(ex);
		}
	}



	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}


	private boolean saverecords() {

		Boolean return_value=false;
		weight_unit = setting_last_enterd_values;

		if (!_spnrPosition.getSelectedItem().toString().equals("Body Location")) {
			if (!_spnrPositiontype.getSelectedItem().toString().equals("Body Position")) {
				if (!_edtsystolic.getText().toString().isEmpty()) {
					if (!_edtpulse.getText().toString().isEmpty()) {
						if (!_edtweight.getText().toString().isEmpty()) {
							if (!txtDate.getText().toString().isEmpty()) {
								if (!_spnrArrhythnia.getSelectedItem().toString().equals("Select Condition")) {

									Calendar cal = Calendar.getInstance();
									DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
									bp_time = dateFormat1.format(cal.getTime());
									DateFormat dateFormatTime = new SimpleDateFormat("HH");
									bptimehr = dateFormatTime.format(cal.getTime());

									return_value=true;


								} else {
									Toast.makeText(getApplicationContext(),
											"Please Select Condition",
											Toast.LENGTH_LONG)
											.show();
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"Please Select Date",
										Toast.LENGTH_LONG)
										.show();
							}
						} else {
							Toast.makeText(getApplicationContext(),
									"Please enter Weight",
									Toast.LENGTH_LONG)
									.show();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"Please enter Pulse",
								Toast.LENGTH_LONG)
								.show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Please enter Systolic",
							Toast.LENGTH_LONG)
							.show();
				}

			} else {
				Toast.makeText(getApplicationContext(),
						"Please Body Position",
						Toast.LENGTH_LONG)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Please Body Location", Toast.LENGTH_LONG)
					.show();
		}
		return return_value;
	}


	@OnItemSelected({R.id.spArrhythnia})
	void durationSelected(Spinner spinner,
						  int position) {
		nArrhythnia=position;

		//	Toast.makeText(this, "Selected position " + position + "!", Toast.LENGTH_SHORT).show();


	}


	private void sendreports() {
		Cursor cursor_data;
		cursor_data = db_bp.getBPMonitorCount(Integer.parseInt(sMemberId),nRelationshipId);

		if ((cursor_data != null) && (cursor_data.getCount() > 0)) {
			if (cursor_data.moveToFirst()) {
				Intent Intenet_dm = new Intent(this, BPA_AnalysisDisplayActivity.class);
				startActivity(Intenet_dm);
			}
		}

	}
	private void initialize_bottom_bar() {

// Create items
		AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home_icon_white, R.color.colorPrimary);
		AHBottomNavigationItem item2 = new AHBottomNavigationItem("Add Doctor", R.drawable.add_doctor_white, R.color.colorPrimary);
		AHBottomNavigationItem item3 = new AHBottomNavigationItem("Set Goal", R.drawable.setgoal_white, R.color.colorPrimary);
		AHBottomNavigationItem item4 = new AHBottomNavigationItem("View Summary", R.drawable.graphicon, R.color.colorPrimary);

// Add items
		bottomNavigation.addItem(item1);
		bottomNavigation.addItem(item2);
		bottomNavigation.addItem(item3);
		bottomNavigation.addItem(item4);

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

				if(position==0 )
				{
					gotohome();
				}else
				if(position==1)
				{

					Add_Doctor_Dialog myDiag = Add_Doctor_Dialog.newInstance("add_doctor");
					myDiag.show(getFragmentManager(), "Diag");
				}else
				if(position==2)
				{
					SetGoalDialog();
				}else
				if(position==3)
				{
					sendreports();
				}

			}
		});
	}




	private void show_analysis_button()
	{
		Cursor cursor_data;
		cursor_data = db_bp.getBPMonitorCount(Integer.parseInt(sMemberId),
				nRelationshipId);

		if ((cursor_data != null) && (cursor_data.getCount() > 0)) {
			btnanalysis.setVisibility(View.VISIBLE);

		} else {
			btnanalysis.setVisibility(View.GONE);

		}
	}
	private void get_defaults()
	{
		String setting_name = "";
		Cursor cursor_session = db_bp.getAllSetting_datat(String.valueOf(nRelationshipId), sMemberId, "1");

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

		if(setting_get_def_site!=null)
		{
			if(!setting_get_def_site.equals("false"))
			{

				_spnrPosition.setSelection( Arrays.asList(Positions).indexOf(setting_get_def_site));
			}
		}

		if(setting_weight_unit==null)
		{
			setting_weight_unit="kg";
		}

		if(setting_get_def_pos!=null)
		{
			if(!setting_get_def_pos.equals("false"))
			{
				_spnrPositiontype.setSelection( Arrays.asList(PositionTypes).indexOf(setting_get_def_pos));
			}
		}

		if(setting_last_enterd_values==null)
		{

			setting_last_enterd_values="false";

		}

		_txt_weight_h.setText("Weight("+setting_weight_unit+")");
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


	private EditText findInput(ViewGroup np) {
	int count = np.getChildCount();
	for (int i = 0; i < count; i++) {
		final View child = np.getChildAt(i);
		if (child instanceof ViewGroup) {
			findInput((ViewGroup) child);
		} else if (child instanceof EditText) {
			return (EditText) child;
		}
	}
	return null;
}



}



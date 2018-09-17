/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package viroopa.com.medikart.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	private static SQLiteHandler sSqlite_obj;

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "medikart";

	// TABLE //

	///////////////////////////   COMMON    ///////////////////////////

	private static final String TABLE_Medikart_SYNC = "Medikart_Sync";
	// Login Table Columns names
	private static final String F_KEY_SYNCID = "SyncId";
	private static final String F_KEY_I_TYPE = "I_Type";
	private static final String F_KEY_CONTROLLER = "Controller";
	private static final String F_KEY_PARAMETER = "Parameter";
	private static final String F_KEY_JSONOBJECT = "JsonObject";
	private static final String F_KEY_CREATED_DATE = "Created_Date";
	private static final String F_KEY_UUID = "UUID";
	private static final String F_KEY_UPLOAD_DOWNLOAD= "Upload_Download";
	private static final String F_KEY_SYNCMEMBERID = "MemberId";
	private static final String F_KEY_MODULE_NAME = "Module_Name";
	private static final String F_KEY_MODE = "Mode";
	private static final String F_KEY_CONTROLLERNAME = "ControllerName";
	private static final String F_KEY_METHODNAME = "MethodName";
	private static final String F_KEY_IMEI = "IMEI";


	private static final String TABLE_LOGIN = "login";
	// Login Table Columns names
	private static final String F_KEY_ID = "id";
	private static final String F_KEY_FNAME = "fname";
	private static final String F_KEY_LNAME = "lname";
	private static final String F_KEY_EMAIL = "email";
	private static final String F_KEY_GENDER = "gender";
	private static final String F_KEY_CITY = "city";
	private static final String F_KEY_PINCODE = "pincode";
	private static final String F_KEY_PHONENO = "phoneno";
	private static final String F_KEY_CREATED_AT = "created_at";
	private static final String F_KEY_MEMBERID = "memberid";
	private static final String F_MEMBERIMAGETYPE = "MemberImageType";
	private static final String F_MEMBERIMAGE = "MemberImage";
	private static final String F_MEMBERADDRESS = "MemberAddress";




	private static final String TABLE_DMGOAL = "set_dm_Goal";
	// Login Table Columns names
	private static final String F_KEY_goalID = "goalid";
	private static final String F_GOALMEMBERID = "goalmember_id";
	private static final String F_KEY_GOALRELATIONSHIP_ID = "goalrelationship_ID";
	private static final String F_KEY_GOALWEIGHT = "goalweight";
	private static final String F_KEY_GOALBLOODSUGAR = "goalbloodsugar";
	private static final String F_KEY_GOALWEIGHTUNIT = "dm_weightunit";
	private static final String F_KEY_GOALDMKG = "dm_kg";
	private static final String F_KEY_GOALDMLB= "dm_lb";
	private static final String F_KEY_GOALBSUNIT = "dm_BSunit";
	private static final String F_KEY_MMOLVALUE = "g_mmolval";


	private static final String TABLE_BPCONDITION = "bp_condition";
	// Login Table Columns names
	private static final String F_KEY_BPCONID = "bpconid";
	private static final String F_CONDITIONID = "condition_id";
	private static final String F_KEY_CONDITIONDESC = "conditiondesc";


	private static final String TABLE_BSCATEGORY= "bs_category";
	// Login Table Columns names
	private static final String F_KEY_BSCONID = "bScatid";
	private static final String F_CATEGORYID = "category_id";
	private static final String F_KEY_CATEGORYDESC = "categorydesc";
	private static final String F_KEY_CATEGORYGRPID = "categorygroupid";

	private static final String TABLE_BPGOAL = "set_bp_Goal";
	// Login Table Columns names
	private static final String F_KEY_BPGOALID = "bp_goalid";
	private static final String F_BPGOALMEMBERID = "bp_goalmember_id";
	private static final String F_KEY_BPGOALRELATIONSHIP_ID = "bp_goalrelationship_ID";
	private static final String F_KEY_BPGOALWEIGHT = "bp_goalweight";
	private static final String F_KEY_BPGOALSYSTOLIC  = "bp_goalsystolic";
	private static final String F_KEY_BPGOALDIASTOLIC = "bp_goaldiastolic";
	private static final String F_KEY_BPGOALWEIGHTUNIT = "bp_weightunit";
	private static final String F_KEY_BPGOALKG   = "bp_kg";
	private static final String F_KEY_BPGOALLB = "bp_lb";
	//private static final String F_KEY_BPGOALBPUNIT  = "bp_BPunit";

	private static final String TABLE_FAMILYMEMBER = "cfe_memberfamily_m";
	//Family Member Table Columns names
	private static final String F_KEY_MEMBERFAMILYID = "id";
	private static final String F_MEMBERID = "member_id";
	private static final String F_MEMBERFAMILYNO = "MemberFamilyNo";
	private static final String F_MEMBER_NAME = "Member_Name";
	private static final String F_MEMBER_RELATIONSHIP_ID= "Member_Relationship_ID";
	private static final String F_MEMBER_DOB = "Member_DOB";
	private static final String F_MEMBER_GENDER = "Member_Gender";
	private static final String F_IMAGETYPE = "Member_ImageType";
	private static final String F_MEMBERFAMILYIMAGE = "MemberFamilyImage";

	//  RelationShip table name
	private static final String TABLE_RELATIONSHIP = "cfe_relationship_c";
	//RelationShip Table Columns names
	private static final String F_KEY_RELATIONSHIP_ID = "Relationship_ID";
	private static final String F_RELATIONSHIP_NAME = "Relationship_Name";
	private static final String F_ACTIVE= "Active";


	//  RelationShip table name
	private static final String TABLE_MEDICINE_MASTER = "medicine_master";
	//RelationShip Table Columns names
	private static final String F_KEY_MEDICINE_ID = "medicine_id";
	private static final String F_MEDICINE_NAME = "medicine_name";
	private static final String F_MEDICINE_PRICE = "price";
	private static final String F_MEDICINE_PACKSIZE = "packsize";
	private static final String F_MEDICINE_FORM = "form_name";
	private static final String F_MEDICINE_IMAGE_URL = "img_url";
	private static final String F_MEDICINE_MFG = "mfg";
	private static final String F_MEDICINE_SET_TYPE = "product_type";
	private static final String F_IS_PHARMA = "isPharma";


	private static final String user_id = "name";
	private static final String body_part = "email";
	private static final String position = "uid";

	///////////////////////////   COMMON    ///////////////////////////


	private static final String TABLE_BP_SYNC = "user_bp_log_sync";
	// Bp Log Table Columns names
	private static final String F_KEY_SYNCBPID = "SYNC_BPID";
	private static final String F_SYNCMEMBER_ID = "sync_member_id";
	private static final String F_SYNCBODY_PART = "sync_body_part";
	private static final String F_SYNCPOSITION = "sync_position";
	private static final String F_SYNCSYSTOLIC = "sync_systolic";
	private static final String F_SYNCDIASTOLIC = "sync_diastolic";
	private static final String F_SYNCPULSE = "sync_pulse";
	private static final String F_SYNCWEIGHT = "sync_weight";
	private static final String F_SYNCWEIGHT_UNIT = "sync_weight_unit";
	private static final String F_SYNCARRTHYTHMIA = "sync_arrthythmia";
	private static final String F_SYNCCOMMENTS = "sync_comments";
	private static final String F_SYNCBP_DATE = "sync_bp_date";
	private static final String F_SYNCBP_TIME = "sync_bp_time";
	private static final String F_SYNCBPTIMEHR = "sync_bptimehr";
	private static final String F_SYNCKG = "sync_kg";
	private static final String F_SYNCLB = "sync_lb";
	private static final String F_SYNCRELATIONSHIP_ID= "sync_Relationship_ID";
	private static final String F_SYNCIMEI_NO= "sync_IMEI_no";
	private static final String F_SYNCUUID= "sync_UUID";

	///////////////////////////   BLOOD PRESSURE    ///////////////////////////

	private static final String TABLE_BP = "user_bp_log";
	// Bp Log Table Columns names
	private static final String F_KEY_BPID = "BPID";
	private static final String F_MEMBER_ID = "member_id";
	private static final String F_BODY_PART = "body_part";
	private static final String F_POSITION = "position";
	private static final String F_SYSTOLIC = "systolic";
	private static final String F_DIASTOLIC = "diastolic";
	private static final String F_PULSE = "pulse";
	private static final String F_WEIGHT = "weight";
	private static final String F_WEIGHT_UNIT = "weight_unit";
	private static final String F_ARRTHYTHMIA = "arrthythmia";
	private static final String F_COMMENTS = "comments";
	private static final String F_BP_DATE = "bp_date";
	private static final String F_BP_TIME = "bp_time";
	private static final String F_BPTIMEHR = "bptimehr";
	private static final String F_KG = "kg";
	private static final String F_LB = "lb";
	private static final String F_RELATIONSHIP_ID= "Relationship_ID";
	private static final String FIMEI_NO= "IMEI_no";
	private static final String F_UUID= "UUID";

	///////////////////////////   BLOOD PRESSURE    ///////////////////////////


	///////////////////////////   MEDICINE REMINDER    ///////////////////////////


	private static final String TABLE_MED_REM_DOCTORMASTER="med_reminder_doctor_aster";
	//columns for  med reminder details
	private static final String mdrem_detail_remDocid = "reminder_doctid";
	private static final String mdrem_Doctid = "ID";
	private static final String medrem_DoctorName = "DoctorName";
	private static final String medrem_DistrictId= "DistrictId";
	private static final String medrem_GUID= "Guid";




	private static final String TABLE_MED_REM_DETAIL="med_reminder_details";
	//columns for  med reminder details
	private static final String mdrem_detail_remid = "reminder_id";
	private static final String mdrem_detail_id = "details_id";
	private static final String medrem_detail_timeslot = "timeslot";
	private static final String medrem_detail_timeslot_id = "timeslot_id";
	private static final String medrem_detail_quantity = "medicine_quantity";

	private static final String TABLE_MED_REM_SET_TIME_QUANTITY="med_reminder_master";
	//columns for medicine reminder set time and quntity
	private static final String mdid = "id";
	private static final String mdrem_id = "reminder_id";
	private static final String med_id = "medicine_id";
	private static final String med_name = "medicine_name";
	private static final String med_rem_type_id = "reminder_type_id";
	private static final String med_rem_type = "reminder_value";
	private static final String med_rem_sch_duration_id = "schedule_duration_type_id";
	private static final String med_rem_sch_duration_value = "schedule_duration_value";
	private static final String med_rem_sch_duration_date = "schedule_start_date";
	private static final String med_rem_days_interval_id = "days_intervel_type_id";
	private static final String med_rem_days_interval_value = "days_intervel_value";
	private static final String med_rem_days_interval_useplacebo = "use_placebo";
	private static final String medrem_image_id = "image_id";
	private static final String medrem_image_first_color_id = "first_color_id";
	private static final String medrem_image_second_color_id = "second_color_id";
	private static final String med_rem_instructions="instructions";
	private static final String med_rem_dos_amount="dosage_type";
	private static final String med_rem_dos_unit="dosage_value";
	private static final String med_rem_conditions="condition";
	private static final String med_rem_med_doctor = "doctor_id";
	private static final String med_rem_pill_buddy = "medfriend_id";
	private static final String med_rem_member_id = "MemberId";
	private static final String med_rem_relationship_id = "RelationshipId";
	private static final String med_rem_ringtone = "ringtone";
	private static final String med_rem_refill_flag = "RefillRem";
	private static final String med_rem_refill_date = "RefillDate";
	private static final String med_rem_no_of_units = "NoOfUnits";
	private static final String med_rem_packsize = "PackSize";
	private static final String med_rem_notified = "notified";
	private static final String med_rem_no_reminder = "no_reminder";

	/*private static final String med_rem_json_sweekdays="json_weekdays";
	private static final String med_rem_everyday="everyday";
	private static final String med_rem_json_birthcontrol="birthcontrol_json";
	private static final String med_rem_json_specific_days="json_specific_days";
	private static final String med_rem_json_numberof_days="json_number_of_days";*/

	//table for med_reminder_schedule
	private static final String TABLE_MED_REM_SCHEDULE = "med_reminder_schedule";
	//column for med_reminder_schedule
	private static final String MDREM_SCH_ID = "id";
	private static final String MDREM_SCHEDULE_ID = "schedule_id";
	private static final String MDREM_SCHEDULE_REM_ID = "reminder_id";
	private static final String MDREM_SCHEDULE_STATUS = "status";
	private static final String MDREM_SCHEDULE_DATETIME_SET = "datetime_set";
	private static final String MDREM_SCHEDULE_DATETIME_TAKEN = "datetime_taken";
	private static final String MDREM_SCHEDULE_SEQUENCE = "sequence";
	private static final String MDREM_SCHEDULE_DOSAGE_TYPE = "dosage_type";
	private static final String MDREM_SCHEDULE_DOSAGE = "dosage";
	private static final String MDREM_SCHEDULE_QUANTITY = "quantity";
	private static final String MDREM_SCHEDULE_SNOOZE_COUNT = "snooze_count";
	private static final String MDREM_SCHEDULE_member_id = "MemberId";
	private static final String MDREM_SCHEDULE_relationship_id = "RelationshipId";

	//table for timeslot
	private static final String TABLE_MED_TIMESLOT_MASTER="med_reminder_timeslot_master";
	//column for timeslot
	private static final String mdrem_timeslotm_id= "timeslot_id";
	private static final String mdrem_timeslot_rem_id= "reminder_id";
	private static final String mdrem_timeslots= "time_slots";
	private static final String mdrem_timeslots_hour= "time_hour";
	private static final String mdrem_timeslots_section= "time_section";
	private static final String medrem_timeslot_quantity = "medicine_quantity";

	//table for med reminder med friend
//table for med reminder med friend
	private static final String TABLE_MED_REM_MED_FRIEND_TABLE="med_reminder_medfriend";
	//columns for med reminder med friend
	private static final String mdfriend_id= "id";
	private static final String mdrem_medfriend_id= "medfriend_id";
	//private static final String mdrem_medfrined_rem_id= "reminder_id";
	private static final String mdrem_medfrined_friendname= "reminder_friendname";
	private static final String mdrem_medfrined_phone_no= "reminder_phone_no";
	private static final String mdrem_medfrined_email_id= "reminder_email_id";
	private static final String mdrem_medfrined_image_name= "reminder_image_name";
	private static final String mdrem_medfrined_invitation_code= "reminder_invitation_code";

	private static final String mdrem_medfrined_accept_flag= "reminder_accept_flag";

	//table for med_reminder_notification
	private static final String TABLE_MED_REM_NOTIFICATION="med_reminder_notification";
	//columns for  med reminder notification
	private static final String mdrem_notify_id= "notify_id";
	private static final String medrem_schedule_id= "schedule_id";
	private static final String mdrem_reminder_id = "reminder_id";
	private static final String medrem_scheduled_time = "scheduled_time";
	private static final String medrem_rescheduled_time= "rescheduled_time";
	private static final String medrem_snooze_duration= "snooze_duration";


	// TABLE //medicine master for medreminder
	private static final String TABLE_MED_REM_MEDICINE_MASTER = "med_reminder_medicine_master";
	//columns for  med reminder details
	private static final String mdremid = "id";
	private static final String mdrem_medicine_id = "medicine_id";
	private static final String mdrem_actual_medicine_id = "actual_medicine_id";
	private static final String mdrem_medicine_name= "medicine_name";
	private static final String medrem_medicine_image_id = "image_id";
	private static final String medrem_medicine_image_first_color_id = "first_color_id";
	private static final String medrem_medicine_image_second_color_id = "second_color_id";
	private static final String  medrem_medicine_member_id = "MemberId";
	private static final String  medrem_medicine_relationship_id = "RelationshipId";


	///////////////////////////   MEDICINE REMINDER    ///////////////////////////


	///////////////////////////   DIABITES MONITOR    ///////////////////////////

	private static final String TABLE_DM = "user_dm_log";
	// DM Log Table Columns names
	private static final String F_KEY_DMID = "DMID";
	private static final String F_DMMEMBER_ID = "member_id";
	private static final String F_RELATION_ID = "relation_id";
	private static final String F_WEIGHT_N0= "weight_number";
	private static final String F_DM_WEIGHT_UNIT = "weight_unit";
	private static final String F_DATE = "g_date";
	private static final String F_TIME = "g_time";
	private static final String F_VALUE = "g_value";
	private static final String F_UNIT = "g_unit";
	private static final String F_ADD_NOTE = "add_note";
	private static final String F_CATEGORY = "g_category";
	private static final String F_REMINDER = "s_reminder";
	private static final String F_DMIMEI = "DMIMEI";
	private static final String F_DMUUID = "DMUUID";
	private static final String F_INJECTION_SITE = "injection_site";
	private static final String F_INJECTION_POSITION = "injection_position";
	private static final String F_AMPM = "AMPM";
	private static final String F_KEY_BSCATEGORYGRPID = "bscategorygrpid";
	private static final String F_MMOLVALUE = "g_mmolval";
	private static final String F_LBWEIGHT_N0= "lbweight_number";
//	private static final String F_KEY_BSUNIT = "bsunit";
	///////////////////////////   DIABITES MONITOR    ///////////////////////////

	private static final String TABLE_SYNC_DM = "user_sync_dm_log";


	// DM Log Table Columns names

	private static final String F_KEY_SYNC_DMID = "sync_DMID";
	private static final String F_SYNC_DMMEMBER_ID = "sync_member_id";
	private static final String F_SYNC_RELATION_ID = "sync_relation_id";
	private static final String F_SYNC_WEIGHT_N0= "sync_weight_number";
	private static final String F_SYNC_DM_WEIGHT_UNIT = "sync_weight_unit";
	private static final String F_SYNC_DATE = "sync_g_date";
	private static final String F_SYNC_TIME = "sync_g_time";
	private static final String F_SYNC_VALUE = "sync_g_value";
	private static final String F_SYNC_UNIT = "sync_g_unit";
	private static final String F_SYNC_ADD_NOTE = "sync_add_note";
	private static final String F_SYNC_CATEGORY = "sync_g_category";
	private static final String F_SYNC_REMINDER = "sync_s_reminder";
	private static final String F_SYNC_DMIMEI = "sync_DMIMEI";
	private static final String F_SYNC_DMUUID = "sync_DMUUID";
	///////////////////////////   WATER MONITOR    ///////////////////////////

	private static final String TABLE_WM_ENTRIES = "wm_entries";
	// water monitor Columns names
	private static final String WM_ID = "id";
	private static final String WM_MEMBER_ID = "member_id";
	private static final String WM_DATE = "created_date";
	private static final String WM_QUANTITY = "quantity";
	private static final String WM_RELATIONSHIP_ID = "Relationship_ID";
	private static final String WM_UUID = "UUID";
	private static final String WM_IMEI= "IMEI";
	private static final String WM_WATER_DRINK_PERCNT= "water_drink_percent";
	//table for water monitor settings
	//table for water monitor settings
	private static final String TABLE_WM_SETTINGS = "wm_setting";
	// water monitor settings Columns names
	private static final String WM_MAIN_ID = "wm_id";
	private static final String WM_SETTMEMBER_ID = "member_id";
	private static final String WM_MAIN_UUID = "main_uuid";
	private static final String WM_GOAL_SET = "Goal_set";
	private static final String WM_MAIN_DATE = "created_date";
	private static final String WM_IMEI_MAIN = "IMEI_main";
	private static final String WM_SETT_RELATIONSHIP_ID = "Relationship_ID";
	private static final String WM_SETT_GLASS_SIZE = "glass_size";

	///////////////////////////   WATER MONITOR    ///////////////////////////








///////////////////////////   Buying app SEARCH TABLE created by Akhil   ///////////////////////////

	//table for SEARCH MEDICINE DATA
	private static final String TABLE_MEDIKART_SEARCH_DATA = "search_data_table";
	// SEARCH MEDICINE DATA Columns names

	private static final String MEDIKART_ID="id";
	private static final String MEDIKART_MEDICINE_ID = "medicine_id";
	private static final String MEDIKART_MEDICINE_NAME = "medicine_name";
	private static final String MEDIKART_PRICE = "price";
	private static final String MEDIKART_FORM_NAME = "form_name";
	private static final String MEDIKART_PACKSIZE = "packsize";
	private static final String MEDIKART_IMAGE_URL = "img_url";



	///////////////////////////   Buying app SEARCH TABLE created by Akhil   ///////////////////////////

	//table for SEARCH MEDICINE DATA
	private static final String TABLE_Buying_SEARCH_DATA = "search_data_table";
	// SEARCH MEDICINE DATA Columns names

	private static final String Buying_ID="id";
	private static final String Buying_MEDICINE_ID = "medicine_id";
	private static final String Buying_MEDICINE_NAME = "medicine_name";
	private static final String Buying_PRICE = "price";
	private static final String Buying_FORM_NAME = "form_name";
	private static final String Buying_PACKSIZE = "packsize";
	private static final String Buying_IMAGE_URL = "img_url";
	private static final String Buying_PRODUCT_MFG = "mfg";
	private static final String Buying_PRODUCT_TYPE = "product_type";
	private static final String Buying_PRODUCT_Is_PHARMA = "isPharma";



	///////////////////////////   SETTING TABLE created by Akhil   ///////////////////////////



	private static final String TABLE_MEDIKART_SETTING = "main_setting";

	private static final String SETTING_ID="id";
	private static final String SETTING_MEMBER_ID = "member_id";
	private static final String SETTING_RELATIONSHIP_ID = "relationship_id";
	private static final String SETTING_NAME = "name";
	private static final String SETTING_VALUE = "value";
	private static final String SETTING_MODULE_ID = "module_id";
	private static final String SETTING_MODULE_DESCRIPTION = "module_description";
	private static final String SETTING_CREATED_DATE = "created_date";



	///////////////////////////   SETTING TABLE created by Akhil   ///////////////////////////

	public static synchronized SQLiteHandler getInstance ( Context context ) {
		if ( sSqlite_obj == null ) {
			sSqlite_obj = new SQLiteHandler ( context.getApplicationContext ( ) );
		}
		return sSqlite_obj;
	}

	public SQLiteHandler ( Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		///////////////////////////   COMMON    ///////////////////////////

		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ F_KEY_ID + " INTEGER PRIMARY KEY,"
				+ F_KEY_FNAME + " TEXT,"
				+ F_KEY_LNAME + " TEXT,"
				+ F_KEY_CITY + " INTEGER,"
				+ F_KEY_PINCODE + " TEXT,"
				+ F_KEY_PHONENO + " TEXT,"
				+ F_KEY_EMAIL + " TEXT UNIQUE,"
				+ F_KEY_CREATED_AT + " TEXT,"
				+ F_KEY_MEMBERID + " TEXT,"
				+ F_KEY_GENDER + " TEXT,"
				+ F_MEMBERIMAGETYPE + " TEXT,"
		        + F_MEMBERADDRESS + " TEXT,"
				+ F_MEMBERIMAGE + " TEXT "+ ")";


		db.execSQL(CREATE_LOGIN_TABLE);
		Log.d(TAG, "Database tables created");



		String CREATE_BPGOAL_TABLE = "CREATE TABLE " + TABLE_BPGOAL  + "("
				+ F_KEY_BPGOALID  + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
				+ F_BPGOALMEMBERID  + " INTEGER,"
				+ F_KEY_BPGOALRELATIONSHIP_ID  + " INTEGER,"
				+ F_KEY_BPGOALWEIGHT  + " TEXT,"
				+ F_KEY_BPGOALSYSTOLIC   + " TEXT,"
				+ F_KEY_BPGOALDIASTOLIC  + " TEXT,"
				+ F_KEY_BPGOALWEIGHTUNIT  + " TEXT,"
				+ F_KEY_BPGOALKG  + " TEXT,"
				+ F_KEY_BPGOALLB + " TEXT"+ ")";
				//+ F_KEY_BPGOALBPUNIT  + " TEXT "+ ")";


		db.execSQL(CREATE_BPGOAL_TABLE);
		Log.d(TAG, "Database tables created");


		String CREATE_DMGOAL_TABLE = "CREATE TABLE " + TABLE_DMGOAL + "("
				+ F_KEY_goalID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
				+ F_GOALMEMBERID + " INTEGER,"
				+ F_KEY_GOALRELATIONSHIP_ID + " INTEGER,"
				+ F_KEY_GOALWEIGHT + " TEXT,"
				+ F_KEY_GOALBLOODSUGAR + " TEXT,"
		        + F_KEY_MMOLVALUE + " TEXT,"
				+ F_KEY_GOALWEIGHTUNIT + " TEXT,"
				+ F_KEY_GOALDMKG + " TEXT,"
				+ F_KEY_GOALDMLB + " TEXT,"
				+ F_KEY_GOALBSUNIT + " TEXT "+ ")";


		db.execSQL(CREATE_DMGOAL_TABLE);
		Log.d(TAG, "Database tables created");

		String CREATE_BSCATEGORY_TABLE = "CREATE TABLE " + TABLE_BSCATEGORY  + "("
				+ F_KEY_BSCONID  + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
				+ F_CATEGORYID  + " INTEGER,"
				+ F_KEY_CATEGORYDESC  + " TEXT,"
				+ F_KEY_CATEGORYGRPID  + " TEXT "+ ")";


		db.execSQL(CREATE_BSCATEGORY_TABLE);
		Log.d(TAG, "Database tables created");


		String CREATE_BPCONDITION_TABLE = "CREATE TABLE " + TABLE_BPCONDITION  + "("
				+ F_KEY_BPCONID  + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
				+ F_CONDITIONID  + " INTEGER,"
				+ F_KEY_CONDITIONDESC  + " TEXT "+ ")";


		db.execSQL(CREATE_BPCONDITION_TABLE);
		Log.d(TAG, "Database tables created");


		String CREATE_FAMILYMEMBER_TABLE = "CREATE TABLE " + TABLE_FAMILYMEMBER + "("
				+ F_KEY_MEMBERFAMILYID + " INTEGER PRIMARY KEY,"
				+ F_MEMBERID + " INTEGER,"
				+ F_MEMBERFAMILYNO + " INTEGER,"
				+ F_MEMBER_NAME + " TEXT,"
				+ F_MEMBER_RELATIONSHIP_ID + " INTEGER,"
				+ F_MEMBER_DOB + " DATE,"
				+ F_MEMBER_GENDER  + " TEXT,"
				+ F_IMAGETYPE  + " TEXT,"
				+ F_MEMBERFAMILYIMAGE+ " TEXT " + ")";

		db.execSQL(CREATE_FAMILYMEMBER_TABLE);
		Log.d(TAG, "Database tables created");

		String CREATE_RELATIONSHIP_TABLE = "CREATE TABLE " + TABLE_RELATIONSHIP + "("
				+ F_KEY_RELATIONSHIP_ID + " INTEGER PRIMARY KEY,"
				+ F_RELATIONSHIP_NAME + " TEXT,"
				+ F_ACTIVE + " BOOLEAN"+")";

		db.execSQL(CREATE_RELATIONSHIP_TABLE);
		Log.d(TAG, "Database tables created");

		String CREATE_MEDICINE_MASTER = "CREATE VIRTUAL TABLE " + TABLE_MEDICINE_MASTER + " USING fts4  ("
				+ F_KEY_MEDICINE_ID + " INTEGER PRIMARY KEY,"
				+ F_MEDICINE_NAME + " TEXT,"
				+ F_MEDICINE_PRICE + " TEXT,"
				+ F_MEDICINE_PACKSIZE + " TEXT,"
				+ F_MEDICINE_IMAGE_URL+ " TEXT,"
				+ F_MEDICINE_MFG+ " TEXT,"
				+ F_MEDICINE_SET_TYPE+ " TEXT, "
				+ F_IS_PHARMA+ " TEXT, "
				+ F_MEDICINE_FORM + " TEXT " + ")";

		db.execSQL(CREATE_MEDICINE_MASTER);
		Log.d(TAG, "Database tables created : " + TABLE_MEDICINE_MASTER);

		///////////////////////////   COMMON    ///////////////////////////

		///////////////////////////   BLOOD PRESSURE SYNC   ///////////////////////////

		String CREATE_SYNC_BP_TABLE = "CREATE TABLE " + TABLE_BP_SYNC + "("
				+ F_KEY_SYNCBPID + " INTEGER PRIMARY KEY AUTOINCREMENT," + F_SYNCMEMBER_ID + " INTEGER,"
				+ F_SYNCBODY_PART + " TEXT,"+ F_SYNCPOSITION + " TEXT,"
				+ F_SYNCSYSTOLIC + " INTEGER,"+ F_SYNCDIASTOLIC + " INTEGER,"
				+ F_SYNCPULSE + " INTEGER,"
				+ F_SYNCWEIGHT + " DECIMAL(6,2),"
				+ F_SYNCWEIGHT_UNIT + " TEXT,"
				+ F_SYNCARRTHYTHMIA + " INTEGER,"
				+ F_SYNCCOMMENTS + " TEXT,"
				+ F_SYNCBP_DATE + " DATE,"
				+ F_SYNCBP_TIME + " DateTime,"
				+ F_SYNCBPTIMEHR + " INTEGER, "
				+ F_SYNCKG + "  DECIMAL(6,2), "
				+ F_SYNCLB + "  DECIMAL(6,2), "
				+ F_SYNCRELATIONSHIP_ID + "  INTEGER, "
				+ F_SYNCIMEI_NO + "  INTEGER, "
				+ F_SYNCUUID + "  TEXT " + ")";

		try
		{
			db.execSQL(CREATE_SYNC_BP_TABLE);
			Log.d(TAG, "Database tables created");
		}
		catch(Exception e)
		{
			e.toString();
		}

		///////////////////////////   BLOOD PRESSURE    ///////////////////////////

		String CREATE_BP_TABLE = "CREATE TABLE " + TABLE_BP + "("
				+ F_KEY_BPID + " INTEGER PRIMARY KEY AUTOINCREMENT," + F_MEMBER_ID + " INTEGER,"
				+ F_BODY_PART + " TEXT,"+ F_POSITION + " TEXT,"
				+ F_SYSTOLIC + " INTEGER,"+ F_DIASTOLIC + " INTEGER,"
				+ F_PULSE + " INTEGER,"
				+ F_WEIGHT + " DECIMAL(6,2),"
				+ F_WEIGHT_UNIT + " TEXT,"
				+ F_ARRTHYTHMIA + " INTEGER,"
				+ F_COMMENTS + " TEXT,"
				+ F_BP_DATE + " DATE,"
				+ F_BP_TIME + " DateTime,"
				+ F_BPTIMEHR + " INTEGER, "
				+ F_KG + "  DECIMAL(6,2), "
				+ F_LB + "  DECIMAL(6,2), "
				+ F_RELATIONSHIP_ID  + " INTEGER, "
				+ FIMEI_NO  + " INTEGER, "
				+ F_UUID + "  TEXT " + ")";

		try
		{
			db.execSQL(CREATE_BP_TABLE);
			Log.d(TAG, "Database tables created");
		}
		catch(Exception e)
		{
			e.toString();
		}




		String CREATE_TABLE_Medikart_SYNC = "CREATE TABLE " + TABLE_Medikart_SYNC + "("
				+ F_KEY_SYNCID + " INTEGER PRIMARY KEY AUTOINCREMENT," + F_KEY_I_TYPE + " TEXT,"
				+ F_KEY_CONTROLLER + " TEXT,"+ F_KEY_PARAMETER + " TEXT,"
				+ F_KEY_JSONOBJECT + " TEXT,"+ F_KEY_CREATED_DATE + " DateTime,"
				+ F_KEY_UUID + " TEXT,"
				+ F_KEY_UPLOAD_DOWNLOAD + " TEXT,"
				+ F_KEY_SYNCMEMBERID + " TEXT,"
				+ F_KEY_MODULE_NAME + " TEXT,"
				+ F_KEY_MODE + " TEXT,"
				+ F_KEY_CONTROLLERNAME  + " TEXT,"
				+ F_KEY_METHODNAME  + " TEXT,"
				+ F_KEY_IMEI  + "  INTEGER " + ")";

		try
		{
			db.execSQL(CREATE_TABLE_Medikart_SYNC);
			Log.d(TAG, "Database tables created");
		}
		catch(Exception e)
		{
			e.toString();
		}


		///////////////////////////   BLOOD PRESSURE    ///////////////////////////



		///////////////////////////   MEDICINE REMINDER    ///////////////////////////

		String CREATE_MED_REM_SET_TIME_QUANTITY_TABLE = "CREATE TABLE " + TABLE_MED_REM_SET_TIME_QUANTITY + "("
				+ mdid  + " INTEGER PRIMARY KEY AUTOINCREMENT  , "
				+ mdrem_id  + " TEXT  , "
				+ med_name  + " TEXT , "
				+ med_id  + "  TEXT , "
				+ med_rem_type_id + "  INTEGER , "
				+med_rem_type+"  TEXT, "
				+med_rem_sch_duration_id+" INTEGER,"
				+med_rem_sch_duration_value+" INTEGER,"
				+med_rem_sch_duration_date +" TEXT,"
				+med_rem_days_interval_id+" INTEGER,"
				+med_rem_days_interval_value+" TEXT,"
				+med_rem_days_interval_useplacebo+" INTEGER,"
				+medrem_image_id+ " INTEGER,"
				+ medrem_image_first_color_id + " INTEGER ,"
				+ medrem_image_second_color_id + " INTEGER ,"
				+ med_rem_instructions + "  TEXT, "
				+med_rem_dos_amount + "  TEXT,"
				+med_rem_dos_unit + " REAL, "
				+med_rem_med_doctor + " INTEGER, "
				+med_rem_pill_buddy + " INTEGER, "
				+med_rem_member_id + " INTEGER, "
				+med_rem_relationship_id + " INTEGER, "
				+med_rem_ringtone + "  TEXT,"
				+med_rem_refill_flag + "  INTEGER,"
				+med_rem_refill_date + "  TEXT,"
				+med_rem_no_of_units + "  TEXT,"
			    +med_rem_packsize + "  TEXT,"
		        +med_rem_notified + "  INTEGER,"
		        +med_rem_no_reminder + "  INTEGER,"
				+med_rem_conditions+" TEXT "
				+")";

		db.execSQL(CREATE_MED_REM_SET_TIME_QUANTITY_TABLE);
		Log.d(TAG, "Database tables created");

		String CREATE_INDEX_REMINDER = "CREATE INDEX reminder_id_idx ON med_reminder_master(reminder_id)";


		try {
			db.execSQL(CREATE_INDEX_REMINDER);
			Log.d(TAG, "Index  created");
		}catch(Exception e)
		{
			e.toString();
		}


		String CREATE_MED_REM_SET_TIME_DETAIL_TABLE = "CREATE TABLE " + TABLE_MED_REM_DETAIL + "("
				+ mdrem_detail_remid  + " INTEGER ,"
				+ mdrem_detail_id  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ medrem_detail_timeslot + " TEXT,"
				+ medrem_detail_quantity +" REAL"
				+ ")";

		try
		{
			db.execSQL(CREATE_MED_REM_SET_TIME_DETAIL_TABLE);
			Log.d(TAG, "Database tables created");
		}
		catch(Exception e)
		{
			e.toString();
		}


		String CREATE_MED_REM_DOCTORMASTER_TABLE = "CREATE TABLE " + TABLE_MED_REM_DOCTORMASTER + "("

				+ mdrem_detail_remDocid   + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ mdrem_Doctid   + " INTEGER ,"
				+ medrem_DoctorName  + " TEXT,"
				+ medrem_DistrictId  +" INTEGER,"
				+ medrem_GUID  +" TEXT"
				+ ")";

		try
		{
			db.execSQL(CREATE_MED_REM_DOCTORMASTER_TABLE);
			Log.d(TAG, "Database tables created");
		}
		catch(Exception e)
		{
			e.toString();
		}



		String CREATE_MED_REM_SET_TIME_MED_FRIEND_TABLE = "CREATE TABLE " + TABLE_MED_REM_MED_FRIEND_TABLE + "("
				+ mdrem_medfriend_id  + " INTEGER ,"
				+ mdfriend_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ mdrem_medfrined_friendname  + " TEXT,"
				+ mdrem_medfrined_phone_no  + " TEXT,"
				+ mdrem_medfrined_email_id  + " TEXT,"
				+ mdrem_medfrined_image_name  + " TEXT,"
				+ mdrem_medfrined_invitation_code  + " TEXT,"
				+ mdrem_medfrined_accept_flag+" BOOLEAN"+
				")";

		try {
			db.execSQL(CREATE_MED_REM_SET_TIME_MED_FRIEND_TABLE);
			Log.d(TAG, "Database tables created");
		}catch(Exception e)
		{
			e.toString();
		}


		String CREATE_MED_REM_TIMESLOT_MASTER_TABLE = "CREATE TABLE " + TABLE_MED_TIMESLOT_MASTER + "("
				+   mdrem_timeslotm_id  + " INTEGER ,"
				+   mdrem_timeslot_rem_id  + " INTEGER,"
				+   mdrem_timeslots+" TEXT,"
				+   mdrem_timeslots_hour+" INTEGER,"
				+   mdrem_timeslots_section+" INTEGER ,"+
				medrem_timeslot_quantity + " REAL"
				+")";

		try {
			db.execSQL(CREATE_MED_REM_TIMESLOT_MASTER_TABLE);
			Log.d(TAG, "Database tables created");
		}catch(Exception e)
		{
			e.toString();
		}

		String CREATE_MED_REM_SCHEDULE = "CREATE TABLE " + TABLE_MED_REM_SCHEDULE + "("
				+ MDREM_SCH_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ MDREM_SCHEDULE_ID  + " TEXT,"
				+ MDREM_SCHEDULE_REM_ID  + " TEXT,"
				+ MDREM_SCHEDULE_STATUS +" TEXT,"
				+ MDREM_SCHEDULE_DATETIME_SET +" NUMERIC,"
				+ MDREM_SCHEDULE_DATETIME_TAKEN +" NUMERIC,"
				+ MDREM_SCHEDULE_SEQUENCE +" INTEGER,"
				+ MDREM_SCHEDULE_member_id +" INTEGER,"
				+ MDREM_SCHEDULE_relationship_id +" INTEGER,"
				+ MDREM_SCHEDULE_DOSAGE_TYPE +" TEXT,"
				+ MDREM_SCHEDULE_DOSAGE +" REAL,"
				+ MDREM_SCHEDULE_SNOOZE_COUNT +" INTEGER,"
				+ MDREM_SCHEDULE_QUANTITY +" REAL "+
				")";

		try {
			db.execSQL(CREATE_MED_REM_SCHEDULE);
			Log.d(TAG, "Database tables created");
		}catch(Exception e)
		{
			e.toString();
		}

		String CREATE_INDEX_SCHEDULE = "CREATE INDEX sch_id_idx ON med_reminder_schedule(schedule_id)";


		try {
			db.execSQL(CREATE_INDEX_SCHEDULE);
			Log.d(TAG, "Index  created");
		}catch(Exception e)
		{
			e.toString();
		}


		String CREATE_MED_REM_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_MED_REM_NOTIFICATION + "("
				+   mdrem_notify_id  + " INTEGER PRIMARY KEY AUTOINCREMENT  ,"
				+   medrem_schedule_id  + " TEXT,"
				+   mdrem_reminder_id  + " TEXT,"
				+   medrem_scheduled_time+" NUMERIC,"
				+   medrem_rescheduled_time+" NUMERIC,"
				+   medrem_snooze_duration + " INTEGER"
				+")";

		try {
			db.execSQL(CREATE_MED_REM_NOTIFICATION_TABLE);
			Log.d(TAG, "Database tables created");
		}catch(Exception e)
		{
			e.toString();
		}



		String CREATE_REMINDER_MEDICINE_MASTER_TABLE = "CREATE TABLE " + TABLE_MED_REM_MEDICINE_MASTER     + "("
				+ mdremid     + "  INTEGER PRIMARY KEY AUTOINCREMENT  ,"
				+ mdrem_medicine_id     + " TEXT ,"
				+ mdrem_medicine_name     + "   TEXT  ,"
				+ medrem_medicine_image_first_color_id       + "  INTEGER  ,"
				+ medrem_medicine_image_second_color_id       + "  INTEGER  ,"
				+ medrem_medicine_member_id       + "  INTEGER  ,"
				+ medrem_medicine_relationship_id       + "  INTEGER  ,"
		        + mdrem_actual_medicine_id     + "   TEXT  ,"
				+ medrem_medicine_image_id     +"  INTEGER  "+
				" )";


		try {
			db.execSQL(CREATE_REMINDER_MEDICINE_MASTER_TABLE);
			Log.d(TAG, "Database tables created");
		}catch(Exception e)
		{
			e.toString();
		}

		///////////////////////////   MEDICINE REMINDER    ///////////////////////////



		///////////////////////////   DIABITES MONITOR    ///////////////////////////

		String CREATE_DM_TABLE = "CREATE TABLE " + TABLE_DM + "("
				+ F_KEY_DMID + " INTEGER PRIMARY KEY AUTOINCREMENT," + F_DMMEMBER_ID + " INTEGER,"
				+ F_RELATION_ID + " INTEGER,"+ F_WEIGHT_N0 + " DECIMAL(6,2),"
				+ F_DM_WEIGHT_UNIT + " TEXT,"+ F_DATE + " DATE,"
				+ F_TIME + " DateTime,"
				+ F_VALUE + " DECIMAL(5,2),"
				+ F_UNIT + " TEXT,"
				+ F_ADD_NOTE + " TEXT,"
				+ F_CATEGORY + " TEXT,"
				+ F_REMINDER  + " TIME,"
				+ F_DMIMEI  + " TEXT,"
				+ F_DMUUID  + " TEXT,"
				+ F_INJECTION_SITE   + " TEXT,"
				+ F_INJECTION_POSITION   + " INTEGER,"
				+ F_AMPM   + " TEXT,"
				+ F_KEY_BSCATEGORYGRPID   + " TEXT,"
				+ F_MMOLVALUE    + " DECIMAL(5,2),"
				+ F_LBWEIGHT_N0  + " DECIMAL(6,2))";



		db.execSQL(CREATE_DM_TABLE);



		String CREATE_SYNC_DM_TABLE = "CREATE TABLE " + TABLE_SYNC_DM + "("
				+ F_KEY_SYNC_DMID + " INTEGER PRIMARY KEY AUTOINCREMENT," + F_SYNC_DMMEMBER_ID + " INTEGER,"
				+ F_SYNC_RELATION_ID + " INTEGER,"+ F_SYNC_WEIGHT_N0 + " DECIMAL(6,2),"
				+ F_SYNC_DM_WEIGHT_UNIT + " TEXT,"+ F_SYNC_DATE + " DATE,"
				+ F_SYNC_TIME + " DateTime,"
				+ F_SYNC_VALUE + " DECIMAL(5,2),"
				+ F_SYNC_UNIT + " TEXT,"
				+ F_SYNC_ADD_NOTE + " TEXT,"
				+ F_SYNC_CATEGORY + " TEXT,"
				+ F_SYNC_REMINDER  + " TIME,"
				+ F_SYNC_DMIMEI  + " TEXT,"
				+ F_SYNC_DMUUID + " TEXT"+ ")";

		db.execSQL(CREATE_SYNC_DM_TABLE);


		///////////////////////////   DIABITES MONITOR    ///////////////////////////


		///////////////////////////   WATER MONITOR    ///////////////////////////





		String CREATE_WM_ENTRY_TABLE = "CREATE TABLE " + TABLE_WM_ENTRIES  + "("
				+ WM_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ WM_MEMBER_ID   + " INTEGER,"
				+ WM_DATE  +" DATE ,"
				+ WM_QUANTITY  +" REAL,"
				+ WM_UUID  +" TEXT,"
				+ WM_IMEI  +" TEXT,"
				+ WM_WATER_DRINK_PERCNT  +" TEXT,"
				+ WM_RELATIONSHIP_ID  +" INTEGER"+
				")";

		try {
			db.execSQL(CREATE_WM_ENTRY_TABLE);
			Log.d(TAG, "Database tables created");
		}catch(Exception e)
		{
			e.toString();
		}

		String CREATE_WM_SETTING_TABLE = "CREATE TABLE " + TABLE_WM_SETTINGS   + "("
				+ WM_MAIN_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ WM_SETTMEMBER_ID    + " INTEGER ,"
				+ WM_MAIN_UUID     + " TEXT  ,"
				+ WM_GOAL_SET    +"  INTEGER,"
				+ WM_MAIN_DATE    +" TEXT,"
				+ WM_IMEI_MAIN    +" TEXT,"
				+ WM_SETT_GLASS_SIZE    +" TEXT,"
				+ WM_SETT_RELATIONSHIP_ID   +" INTEGER"+
				")";

		try {
			db.execSQL(CREATE_WM_SETTING_TABLE);
			Log.d(TAG, "Database tables created");
		}catch(Exception e)
		{
			e.toString();
		}


		///////////////////////////   WATER MONITOR    ///////////////////////////

///////////////////////////   Buying app MEDICINE SEARC DATA created by Akhil   ///////////////////////////

		String CREATE_SEARCH_DATA_TABLE = "CREATE TABLE " + TABLE_MEDIKART_SEARCH_DATA    + "("
				+ MEDIKART_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ MEDIKART_MEDICINE_ID     + " TEXT ,"
				+ MEDIKART_MEDICINE_NAME     + " TEXT ,"
				+ MEDIKART_PRICE    +"  TEXT ,"
				+ MEDIKART_FORM_NAME    +" TEXT ,"
				+ MEDIKART_IMAGE_URL    +" TEXT ,"
				+ MEDIKART_PACKSIZE    +" TEXT"+
				")";

		try {
			db.execSQL(CREATE_SEARCH_DATA_TABLE);
			Log.d(TAG, "Database tables created");
		}catch(Exception e)
		{
			e.toString();
		}



		///////////////////////////   Buying app MEDICINE SEARCH DATA created by Akhil   ///////////////////////////




///////////////////////////   SETTING TABLE created by Akhil   ///////////////////////////

		String CREATE_SETTING_TABLE = "CREATE TABLE " + TABLE_MEDIKART_SETTING    + "("
				+ SETTING_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ SETTING_MEMBER_ID     + " INTEGER ,"
				+ SETTING_RELATIONSHIP_ID      + " INTEGER ,"
				+ SETTING_NAME   +"  TEXT ,"
				+ SETTING_VALUE    +" TEXT ,"
				+ SETTING_MODULE_ID    +" INTEGER ,"
				+ SETTING_MODULE_DESCRIPTION    +" TEXT ,"
				+ SETTING_CREATED_DATE    +" DATE "+
				")";

		try {
			db.execSQL(CREATE_SETTING_TABLE);
			Log.d(TAG, "Database tables created");
		}catch(Exception e)
		{
			e.toString();
		}

		///////////////////////////   SETTING TABLE created by Akhil   ///////////////////////////


		String CREATE_SEARCH_DATA_TABLE_1 = "CREATE TABLE " + TABLE_Buying_SEARCH_DATA    + "("
				+ Buying_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ Buying_MEDICINE_ID     + " TEXT ,"
				+ Buying_MEDICINE_NAME     + " TEXT ,"
				+ Buying_PRICE    +"  TEXT ,"
				+ Buying_FORM_NAME    +" TEXT ,"
				+ Buying_IMAGE_URL    +" TEXT ,"
				+ Buying_PRODUCT_MFG    +" TEXT ,"
				+ Buying_PRODUCT_TYPE    +" TEXT ,"
				+ Buying_PRODUCT_Is_PHARMA    +" TEXT ,"
				+ Buying_PACKSIZE    +" TEXT"+
				")";

		try {
			db.execSQL(CREATE_SEARCH_DATA_TABLE_1);
			Log.d(TAG, "Database tables created");
		}catch(Exception e)
		{
			e.toString();
		}
















	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if (oldVersion != newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILYMEMBER);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_RELATIONSHIP);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE_MASTER);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_DM);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_DMGOAL);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_BSCATEGORY);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_BPCONDITION);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_BPGOAL );
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNC_DM);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_BP_SYNC);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_BP);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_Medikart_SYNC);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE_MASTER);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MED_REM_NOTIFICATION);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIKART_SETTING);

			onCreate(db);

		}

	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String fname, String lname,String pincode,String phoneno,
						String email,String gender, Integer city,String uid, String created_at, String memberid,String ImageType,String ImageName,String Address) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(F_KEY_FNAME, fname); // Name
		values.put(F_KEY_LNAME, lname); // Name
		values.put(F_KEY_CITY, city); // Name
		values.put(F_KEY_PINCODE, pincode); // Name
		values.put(F_KEY_PHONENO, phoneno); // Name
		values.put(F_KEY_EMAIL, email); // Email
		values.put(F_KEY_GENDER, gender); // Gender
		values.put(F_KEY_CREATED_AT, created_at);
		values.put(F_KEY_MEMBERID, memberid); // memberid);
		values.put(F_MEMBERIMAGETYPE, ImageType); // ImageType);
		values.put(F_MEMBERIMAGE, ImageName); // ImageName);
		values.put(F_MEMBERADDRESS, Address);

		// Inserting Row
		long id = db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("id", cursor.getString(0));
			user.put("name", cursor.getString(1));
			//user.put("lname", cursor.getString(2));
			user.put("city", cursor.getString(3));
			user.put("pincode", cursor.getString(4));
			user.put("phoneno", cursor.getString(5));
			user.put("email", cursor.getString(6));
			user.put("created_at", cursor.getString(7));
			user.put("memberid", cursor.getString(8));
			user.put("gender", cursor.getString(9));
			user.put("MemberImageType", cursor.getString(10));
			user.put("MemberAddress", cursor.getString(11));
//			user.put(""MemberImageType"", cursor.getString(10));
//			user.put(""MemberImage"", cursor.getString(11));

		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}
	public int getdmRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_DM;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}
	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}


	public String getMemberId() {
		HashMap<String, String> user = new HashMap<String, String>();
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		String sMemberid = "";
		cursor.moveToFirst();
		if (cursor.getCount() == 1) {
			sMemberid = cursor.getString(8);
		}
		db.close();
		cursor.close();

		return sMemberid;
	}

	public void addFAMILYMEMBER(Integer member_id, String MemberFamilyNo,String Member_Name,Integer Member_Relationship_ID,
								String Member_DOB, String Member_Gender,String ImageType,String ImageName) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(F_MEMBERID, member_id); // Name
		values.put(F_MEMBERFAMILYNO, MemberFamilyNo); // Name
		values.put(F_MEMBER_NAME, Member_Name); // Name
		values.put(F_MEMBER_RELATIONSHIP_ID, Member_Relationship_ID); // Name
		values.put(F_MEMBER_DOB, Member_DOB); // Name
		values.put(F_MEMBER_GENDER, Member_Gender); // Email
		values.put(F_IMAGETYPE, ImageType); // Email
		values.put(F_MEMBERFAMILYIMAGE, ImageName); // Email


		// Inserting Row
		long id = db.insert(TABLE_FAMILYMEMBER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New Family Member inserted into sqlite: " + id);
	}

	public HashMap<String, String> getFAMILYMEMBERDetails() {
		HashMap<String, String> familymember = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_FAMILYMEMBER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			familymember.put("member_id", cursor.getString(1));
			familymember.put("MemberFamilyNo", cursor.getString(2));
			familymember.put("Member_Name", cursor.getString(3));
			familymember.put("Member_Relationship_ID", cursor.getString(4));
			familymember.put("Member_DOB", cursor.getString(5));
			familymember.put("Member_Gender", cursor.getString(6));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching Family Member Details from Sqlite: " + familymember.toString());

		return familymember;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getFamilyMemberRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_FAMILYMEMBER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteFamilyMember() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_FAMILYMEMBER, null, null);
		db.close();

		Log.d(TAG, "Deleted all Family Member info from sqlite");
	}




	public void addRelationShip(Integer Relationship_ID, String Relationship_Name,Boolean Active) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(F_KEY_RELATIONSHIP_ID, Relationship_ID); // Name
		values.put(F_RELATIONSHIP_NAME, Relationship_Name); // Name
		values.put(F_ACTIVE, Active); // Name

		// Inserting Row
		long id = db.insert(TABLE_RELATIONSHIP, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New Family Member inserted into sqlite: " + id);
	}
	public HashMap<String, String> getRelationshipDetails() {
		HashMap<String, String> relationship = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_RELATIONSHIP;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			relationship.put("Relationship_ID", cursor.getString(1));
			relationship.put("Relationship_Name", cursor.getString(2));
			relationship.put("Active", cursor.getString(3));

		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching Relationship Details from Sqlite: " + relationship.toString());

		return relationship;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getRelationshipRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_RELATIONSHIP;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}
	public Cursor get_searched_medicine_data () {

		String buildSQL = "select * FROM " + TABLE_MEDIKART_SEARCH_DATA+" order by  "+MEDIKART_ID+ " desc limit 5.";


		SQLiteDatabase db = this.getReadableDatabase();
		Log.d(TAG, "getAllData SQL: " + buildSQL);

		return db.rawQuery(buildSQL, null);
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteRelationship() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_RELATIONSHIP, null, null);
		db.close();

		Log.d(TAG, "Deleted all Relationship info from sqlite");
	}

	public Cursor get_search_medicine (String p_search_name) {

		String buildSQL = "select * FROM " + TABLE_MEDICINE_MASTER
				+ " WHERE medicine_name like '" + p_search_name +  "%' ";

		SQLiteDatabase db = this.getReadableDatabase();
		Log.d(TAG, "getAllData SQL: " + buildSQL);

		return db.rawQuery(buildSQL, null);
	}












	public void insert_medicine_master(Integer p_id, String p_name,String p_price,String p_packsize,String p_form_name,String p_img_url,String mfg,String product_type,boolean isPHARMA) {

		int nFlag = Integer.parseInt(check_medicine_exits(p_id));

		if (nFlag == 0) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(F_KEY_MEDICINE_ID, p_id);
			values.put(F_MEDICINE_NAME, p_name);
			values.put(F_MEDICINE_PRICE, p_price);
			values.put(F_MEDICINE_PACKSIZE, p_packsize);
			values.put(F_MEDICINE_FORM, p_form_name);
			values.put(F_MEDICINE_IMAGE_URL, p_img_url);
			values.put(F_MEDICINE_MFG, p_img_url);
			values.put(F_MEDICINE_SET_TYPE, p_img_url);
			values.put(F_IS_PHARMA	, isPHARMA);

			long id = db.insert(TABLE_MEDICINE_MASTER, null, values);
			db.close();

			//Log.d(TAG, "New Medicine inserted into sqlite: " + id);
		}
	}
	public String check_medicine_exits(Integer p_id) {
		String countQuery = "SELECT count(medicine_id) as count_row FROM " + TABLE_MEDICINE_MASTER + " where medicine_id = " + p_id ;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		String rowCount = "0";
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			rowCount = cursor.getString(0);
		}
		db.close();
		cursor.close();
		return rowCount;
	}

	public void logout_delete_all() {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_LOGIN, null, null);
		db.delete(TABLE_FAMILYMEMBER, null, null);
		db.delete(TABLE_RELATIONSHIP, null, null);
		db.delete(TABLE_MEDICINE_MASTER, null, null);
		db.delete(TABLE_BP, null, null);
		db.delete(TABLE_MEDIKART_SEARCH_DATA, null, null);
		db.delete(TABLE_MED_REM_DETAIL, null, null);
		db.delete(TABLE_MED_REM_SET_TIME_QUANTITY, null, null);
		db.delete(TABLE_MED_REM_SCHEDULE, null, null);
		db.delete(TABLE_MED_TIMESLOT_MASTER, null, null);
		db.delete(TABLE_MED_REM_MED_FRIEND_TABLE, null, null);

		db.delete(TABLE_DM, null, null);
		db.delete(TABLE_DMGOAL, null, null);
		db.delete(TABLE_WM_ENTRIES, null, null);
		db.delete(TABLE_WM_SETTINGS, null, null);
		db.delete(TABLE_BPCONDITION,null,null);
		db.delete(TABLE_BSCATEGORY,null,null);
		db.delete(TABLE_BPGOAL,null,null);
		db.delete(TABLE_MEDICINE_MASTER,null,null);
		db.delete(TABLE_MED_REM_NOTIFICATION,null,null);
		db.delete(TABLE_MEDIKART_SETTING,null,null);
		db.delete(TABLE_MED_REM_MEDICINE_MASTER,null,null);




		db.close();

		Log.d(TAG, "Deleted all data from all table from sqlite");
	}
	public int getSearcedMedicineRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_MEDIKART_SEARCH_DATA;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}
	public void Delete_last_searched_medicine_data(Integer id)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_MEDIKART_SEARCH_DATA + " WHERE " + MEDIKART_ID + "=" + id + "");
		db.close();
	}

	public void Insert_search_data_table(String med_id, String medicineName,String price,String  form_name,
										 String packsize,String imageUrl,String mfg, String product_type,String isPharma) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Buying_MEDICINE_ID , med_id);
		values.put(Buying_MEDICINE_NAME , medicineName);
		values.put(Buying_PRICE , price);
		values.put(Buying_FORM_NAME , form_name);
		values.put(Buying_PACKSIZE , packsize);
		values.put(Buying_IMAGE_URL , imageUrl);
		values.put(Buying_PRODUCT_MFG , mfg);
		values.put(Buying_PRODUCT_TYPE, product_type);
		values.put(Buying_PRODUCT_Is_PHARMA, isPharma);
		// Inserting Row
		long id = db.insert(TABLE_Buying_SEARCH_DATA, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New search medicine data inserted into sqlite: " + id);
	}

	public String check_medicine_exist_search_table(Integer p_id) {
		String countQuery = "SELECT count(medicine_id) as count_row FROM " + TABLE_MEDIKART_SEARCH_DATA + " where medicine_id = " + p_id ;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		String rowCount = "0";
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			rowCount = cursor.getString(0);
		}
		db.close();
		cursor.close();
		return rowCount;
	}
	public void update_profile(String p_id,String full_name,String phoneno,String gender,String pincode,String ImageType,String ImageName,String Address) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(F_KEY_FNAME, full_name);
		values.put(F_KEY_PHONENO, phoneno);
		values.put(F_KEY_GENDER, gender);
		values.put(F_KEY_PINCODE, pincode);
		values.put(F_KEY_GENDER, gender);
		values.put(F_MEMBERIMAGETYPE, ImageType);
		values.put(F_MEMBERIMAGE, ImageName);
		values.put(F_MEMBERADDRESS, Address);

		db.update(TABLE_LOGIN, values, "memberid = " + p_id, null);
		db.close();
	}

	//Created by akhil

	public Cursor getAllSyncData () {


		String buildSQL =" SELECT   * FROM " + TABLE_Medikart_SYNC  ;

		SQLiteDatabase db = this.getReadableDatabase();
		Log.d(TAG, "getAllData SQL: " + buildSQL);

		return db.rawQuery(buildSQL, null);
	}

	//createdby akhil
	public void deleteSyncTableById(Integer bpid) {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_Medikart_SYNC, F_KEY_SYNCID +" = "+bpid, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}
	public void InsertSettingData ( String member_id, String relation_ship_id, String setting_name,
									String settingvalue,
									String module_id, String module_description, String created_date ) {

		SQLiteDatabase db = this.getWritableDatabase ( );

		db.beginTransaction();
		try {

			ContentValues values = new ContentValues ( );
			values.put ( SETTING_MEMBER_ID, member_id );
			values.put ( SETTING_RELATIONSHIP_ID, relation_ship_id );
			values.put ( SETTING_NAME, setting_name );
			values.put ( SETTING_VALUE, settingvalue );
			values.put ( SETTING_MODULE_ID, module_id );
			values.put ( SETTING_MODULE_DESCRIPTION, module_description );
			values.put ( SETTING_CREATED_DATE, created_date );

			db.insertOrThrow ( TABLE_MEDIKART_SETTING, null, values );

			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.d(TAG, "Error InsertSettingData");
		} finally {
			db.endTransaction();
		}


	}
	public Cursor getAllSetting_data ( String RelationShipid, String member_id, String module_id ) {


		SQLiteDatabase db = this.getReadableDatabase ( );
		Cursor obj_cursor = null ;

		try {
			String buildSQL = "SELECT   * FROM " + TABLE_MEDIKART_SETTING + "  where relationship_id =  " + RelationShipid + "  and  member_id= " + member_id + " and module_id=" + module_id;/*, new String[]{String.valueOf(toDate), String.valueOf(RelationShipid)}*/

			obj_cursor = db.rawQuery ( buildSQL, null );
		} catch (Exception e) {

			Log.d(TAG, "Error getAllSetting_datat");
		}
		return obj_cursor ;
	}

	public String getSetting_id ( String RelationShipid, String member_id, String module_id, String setting_name ) {

		SQLiteDatabase db = getReadableDatabase();
		String s_id = "0";
		try {
			String buildSQL = "SELECT   id FROM " + TABLE_MEDIKART_SETTING + "  where relationship_id =  " + RelationShipid + "  and  member_id= " + member_id + " and module_id=" + module_id + " and name='" + setting_name + "'";/*, new String[]{String.valueOf(toDate), String.valueOf(RelationShipid)}*/
			Cursor crs = db.rawQuery ( buildSQL, null );

			if ( crs.moveToFirst ( ) ) {
				do {
					s_id = crs.getString ( crs.getColumnIndex ( "id" ) );
				} while ( crs.moveToNext ( ) );
			}
		} catch (Exception e) {

			Log.d(TAG, "Error getSetting_id");
		}
		return s_id ;
	}
	public void InsertSyncTable ( String I_Type, String Controller, String Parameter, String JsonObject,
								  String Created_Date, String UUID,
								  String iUpload_Download, Integer MemberId,
								  String Module_Name, String Mode,
								  String ControllerName, String METHODName ) {

		SQLiteDatabase db = this.getWritableDatabase ( );

		db.beginTransaction();
		try {
			ContentValues values = new ContentValues ( );
			values.put ( F_KEY_I_TYPE, I_Type ); // Name
			values.put ( F_KEY_CONTROLLER, Controller ); // Name
			values.put ( F_KEY_PARAMETER, Parameter ); // Name
			values.put ( F_KEY_JSONOBJECT, JsonObject ); // Name
			values.put ( F_KEY_CREATED_DATE, Created_Date ); // Name
			values.put ( F_KEY_UUID, UUID ); // Email
			values.put ( F_KEY_UPLOAD_DOWNLOAD, iUpload_Download );
			values.put ( F_KEY_SYNCMEMBERID, MemberId );
			values.put ( F_KEY_MODULE_NAME, Module_Name );
			values.put ( F_KEY_MODE, Mode );
			values.put ( F_KEY_CONTROLLERNAME, ControllerName );
			values.put ( F_KEY_METHODNAME, METHODName );



			db.insertOrThrow(TABLE_Medikart_SYNC, null, values);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.d(TAG, "Error InsertSyncTable");
		} finally {
			db.endTransaction();
		}
	}

	public void updateSettingData ( String member_id, String relation_ship_id, String setting_name,
									String settinsystolic,
									String module_id, String created_date ) {

		SQLiteDatabase db = this.getWritableDatabase ( );
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues ( );
			values.put ( SETTING_VALUE, settinsystolic ); // Name
			values.put ( SETTING_CREATED_DATE, created_date );

			long id = db.update ( TABLE_MEDIKART_SETTING, values, "member_id = " + member_id + " and relationship_id=" + relation_ship_id + " and name='" + setting_name + "' and module_id=" + module_id, null );
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.d(TAG, "Error updateSettingData");
		}
		finally {
			db.endTransaction();
		}
	}

	public  void delete_module_wise_setting_module_id(String mod_id)
	{
		SQLiteDatabase db = this.getWritableDatabase ( );
		db.delete(TABLE_MEDIKART_SETTING, SETTING_MODULE_ID +" = "+mod_id, null);
	}

}
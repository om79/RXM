package viroopa.com.medikart.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by prakash on 29/09/15.
 */
public class SqliteMRHandler extends SQLiteOpenHelper {

    private static final String TAG = SqliteMRHandler.class.getSimpleName();

    private static final String PLACEBO_ID="placebo";

    private static final int DATABASE_VERSION = 1;
   // private  SQLiteDatabase db;

    private static final String DATABASE_NAME = "medikart";



    private static final String TABLE_MED_REM_DOCTORMASTER="med_reminder_doctor_aster";
    //columns for  med reminder details
    private static final String mdrem_detail_remDocid = "reminder_doctid";
    private static final String mdrem_Doctid = "ID";
    private static final String medrem_DoctorName = "DoctorName";
    private static final String medrem_DistrictId= "DistrictId";
    private static final String medrem_GUID= "Guid";

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

    // TABLE //
    private static final String TABLE_MED_REM_DETAIL = "med_reminder_details";
    //columns for  med reminder details
    private static final String mdrem_detail_remid = "reminder_id";
    private static final String mdrem_detail_id = "details_id";
    private static final String medrem_detail_timeslot = "timeslot";
    private static final String medrem_detail_timeslot_id = "timeslot_id";
    private static final String medrem_detail_quantity = "medicine_quantity";

    private static final String TABLE_MED_REM_SET_TIME_QUANTITY = "med_reminder_master";
    //columns for medicine reminder set time and quntity
    private static final String mdid = "id";
    private static final String mdrem_id = "reminder_id";
    private static final String med_name = "medicine_name";
    private static final String med_id = "medicine_id";
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
    private static final String med_rem_instructions = "instructions";
    private static final String med_rem_dos_amount = "dosage_type";
    private static final String med_rem_dos_unit = "dosage_value";
    private static final String med_rem_conditions = "condition";
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
	/*private static final String med_rem_json_sweekdays="medfriend_id";
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
    private static final String TABLE_MED_TIMESLOT_MASTER = "med_reminder_timeslot_master";
    //column for timeslot
    private static final String mdrem_timeslotm_id = "timeslot_id";
    private static final String mdrem_timeslot_rem_id = "reminder_id";
    private static final String mdrem_timeslots = "time_slots";
    private static final String mdrem_timeslots_hour = "time_hour";
    private static final String mdrem_timeslots_section = "time_section";
    private static final String medrem_timeslot_quantity = "medicine_quantity";

    //table for med reminder med friend

    private static final String TABLE_MED_REM_MED_FRIEND_TABLE="med_reminder_medfriend";
    //columns for med reminder med friend
    private static final String mdfriend_id= "id";
    private static final String mdrem_medfriend_id= "medfriend_id";
   // private static final String mdrem_medfrined_rem_id= "reminder_id";
    private static final String mdrem_medfrined_friendname= "reminder_friendname";
    private static final String mdrem_medfrined_phone_no= "reminder_phone_no";
    private static final String mdrem_medfrined_email_id= "reminder_email_id";
    private static final String mdrem_medfrined_image_name= "reminder_image_name";
    private static final String mdrem_medfrined_invitation_code= "reminder_invitation_code";

    private static final String mdrem_medfrined_accept_flag= "reminder_accept_flag";
    //private static final String mdrem_medfrined_fkey_rem_id= "reminder_fkey_id";




    private static final String TABLE_Medikart_SYNC = "Medikart_Sync";
    // Login Table Columns names
    private static final String F_KEY_SYNCID = "SyncId";
    private static final String F_KEY_I_TYPE = "I_Type";
    private static final String F_KEY_CONTROLLER = "Controller";
    private static final String F_KEY_PARAMETER = "Parameter";
    private static final String F_KEY_JSONOBJECT = "JsonObject";
    private static final String F_KEY_CREATED_DATE = "Created_Date";
    private static final String F_KEY_UUID = "UUID";
    private static final String F_KEY_UPLOAD_DOWNLOAD = "Upload_Download";
    private static final String F_KEY_SYNCMEMBERID = "MemberId";
    private static final String F_KEY_MODULE_NAME = "Module_Name";
    private static final String F_KEY_MODE = "Mode";
    private static final String F_KEY_CONTROLLERNAME = "ControllerName";
    private static final String F_KEY_METHODNAME = "MethodName";
    private static final String F_KEY_IMEI = "IMEI";


    private static final String TABLE_MED_REM_NOTIFICATION="med_reminder_notification";
    //columns for  med reminder notification
    private static final String mdrem_notify_id= "notify_id";
    private static final String mdrem_reminder_id = "reminder_id";
    private static final String medrem_schedule_id= "schedule_id";
    private static final String medrem_scheduled_time = "scheduled_time";
    private static final String medrem_rescheduled_time= "rescheduled_time";
    private static final String medrem_snooze_duration= "snooze_duration";


    private static  SqliteMRHandler sInstance;


    public static synchronized SqliteMRHandler getInstance(Context context) {


        if (sInstance == null) {
            sInstance = new SqliteMRHandler(context.getApplicationContext());
        }
        return sInstance;
    }


    public SqliteMRHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public void delete_all_data()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MED_REM_DOCTORMASTER, null, null);
        db.delete(TABLE_MED_REM_MEDICINE_MASTER, null, null);
        db.delete(TABLE_MED_REM_DETAIL, null, null);
        db.delete(TABLE_MED_REM_SET_TIME_QUANTITY, null, null);
        db.delete(TABLE_MED_REM_SCHEDULE, null, null);
        db.delete(TABLE_MED_TIMESLOT_MASTER, null, null);
        db.delete(TABLE_MED_REM_MED_FRIEND_TABLE, null, null);
        db.delete(TABLE_MED_REM_NOTIFICATION, null, null);
        db.close();
        }



    public void InsertSyncTable(String I_Type, String Controller, String Parameter, String JsonObject,
                                String Created_Date, String UUID, String iUpload_Download, Integer MemberId,
                                String Module_Name, String Mode, String ControllerName, String METHODName, Long IMEI) {


        SQLiteDatabase db = this.getWritableDatabase ( );
        db.beginTransaction();
        try {
        ContentValues values = new ContentValues();
        values.put(F_KEY_I_TYPE, I_Type); // Name
        values.put(F_KEY_CONTROLLER, Controller); // Name
        values.put(F_KEY_PARAMETER, Parameter); // Name
        values.put(F_KEY_JSONOBJECT, JsonObject); // Name
        values.put(F_KEY_CREATED_DATE, Created_Date); // Name
        values.put(F_KEY_UUID, UUID); // Email
        values.put(F_KEY_UPLOAD_DOWNLOAD, iUpload_Download);
        values.put(F_KEY_SYNCMEMBERID, MemberId);
        values.put(F_KEY_MODULE_NAME, Module_Name);
        values.put(F_KEY_MODE, Mode);
        values.put(F_KEY_CONTROLLERNAME, ControllerName);
        values.put(F_KEY_METHODNAME, METHODName);
        values.put(F_KEY_IMEI, IMEI);

        long SyncId = db.insertOrThrow(TABLE_Medikart_SYNC, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error updateSettingData");
        }
        finally {
            db.endTransaction();
        }
    }
    public int getMeddetial() {
        String countQuery = "SELECT  * FROM " + TABLE_MED_REM_DETAIL;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();

        cursor.close();

        // return row count
        return rowCount;
    }

    public void addMedRemMaster_null_values(String new_rem_id,String Member_id, Integer RelationShip_id) {

        SQLiteDatabase  db = this.getWritableDatabase ( );
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();

            values.put(mdrem_id, new_rem_id);
            values.put(medrem_image_id, "");
            values.put(med_rem_member_id, Member_id);
            values.put(med_rem_relationship_id, RelationShip_id);
            long id = db.insertOrThrow(TABLE_MED_REM_SET_TIME_QUANTITY, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
        }
        finally {
            db.endTransaction();
        }
    }

    public void addMedRemMaster_insert_from_server(String new_rem_id, String MEd_id, String medName, Integer remTypeID, String rem_type,
                                                   Integer schduration_id, Integer SchDurationValue, String schDate,
                                                   Integer daysIntervelId, String daysIntervelValue, Integer useplacebo,
                                                   Integer img_id, Integer f_color_id, Integer s_color_id , String instr, String dos_unit,
                                                   Integer dos_amont, String condition, String Doctor_id, String pill_buddy_id, String Member_id, Integer rel_id,
                                                   String refill_flag, String Refill_date, String packSize ) {


        SQLiteDatabase  db = this.getWritableDatabase ( );
         db.setLockingEnabled(false);
       // db.execSQL("PRAGMA synchronous=OFF");



        try {

            ContentValues values = new ContentValues();
            //values.put(mdrem_id, mid);
            values.put(mdrem_id, new_rem_id);
            values.put(med_name, medName); // Name
            values.put(med_id, MEd_id);
            //values.put(med_rem_condition, condtn);
            values.put(med_rem_type_id, remTypeID); // Name
            values.put(med_rem_type, rem_type); // Name
            values.put(med_rem_sch_duration_id, schduration_id);// Name
            values.put(med_rem_sch_duration_value, SchDurationValue);
            values.put(med_rem_sch_duration_date, schDate); // Email
            values.put(med_rem_days_interval_id, daysIntervelId);
            values.put(med_rem_days_interval_value, daysIntervelValue);
            values.put(med_rem_days_interval_useplacebo, useplacebo);
            values.put(medrem_image_id, img_id);
            values.put(medrem_image_first_color_id, f_color_id); // memberid);
            values.put(medrem_image_second_color_id, s_color_id);
            values.put(med_rem_instructions, instr);
            values.put(med_rem_dos_amount, dos_unit);
            values.put(med_rem_dos_unit, dos_amont);
            values.put(med_rem_conditions, condition);
            values.put(med_rem_med_doctor, Doctor_id);
            values.put(med_rem_pill_buddy, pill_buddy_id);
            values.put(med_rem_member_id, Member_id);
            values.put(med_rem_relationship_id, rel_id);

            values.put(med_rem_refill_flag, refill_flag);
            values.put(med_rem_refill_date, Refill_date);
            values.put(med_rem_packsize, packSize);


            // Inserting Row

            long id = db.insertOrThrow(TABLE_MED_REM_SET_TIME_QUANTITY, null, values);

        } catch (Exception e) {
            Log.d(TAG, "Error Insert");
        } finally {
            //if (db.isOpen()); {db.close(); };
        }

    }


    public long insert_Medicine_master_value(String med_id,String Medname,Integer image_id,Integer first_color_id,Integer second_color_id,String Member_id,Integer Rel_id,String Actual_med_id ) {
        long id =0;
        try {

            if(second_color_id==null)
            {
                second_color_id=-99;
            }
            SQLiteDatabase  db = this.getWritableDatabase ( );
            db.setLockingEnabled(false);
           // db.execSQL("PRAGMA synchronous=OFF");



            try {

            ContentValues values = new ContentValues();
            values.put(mdrem_medicine_id, med_id);
            values.put(mdrem_medicine_name, Medname); // Name
            values.put(medrem_medicine_image_id , image_id); // memberid);
            values.put(medrem_medicine_image_first_color_id , first_color_id);
            values.put(medrem_medicine_image_second_color_id , second_color_id);
            values.put(medrem_medicine_relationship_id , Rel_id);
            values.put(medrem_medicine_member_id , Member_id);
                values.put(mdrem_actual_medicine_id , Actual_med_id);

            // Inserting Row
                id= db.insertOrThrow(TABLE_MED_REM_MEDICINE_MASTER, null, values);
            } catch (Exception e) {
                Log.d(TAG, "Error InsertSyncTable");
            } finally {
                //if (db.isOpen()); {db.close(); };
            }

        } catch (Exception e) {

            e.toString();
        }
        return id;


    }

    public Boolean check_insert_placebo(String member_id) {
        Boolean placebo_inserted=false;


        String countQuery = "SELECT * FROM " + TABLE_MED_REM_MEDICINE_MASTER+" where medicine_id = '"+PLACEBO_ID+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
            placebo_inserted =  true;
        }

        return placebo_inserted;
    }



    public void update_Medicine_master(String Medname,Integer img_id,Integer first_color_id,Integer second_color_id,String medicine_id ) {


        SQLiteDatabase  db = this.getWritableDatabase ( );
        db.beginTransaction();
        try {
        ContentValues values = new ContentValues();
        values.put(mdrem_medicine_name, Medname); // Name
        values.put(medrem_medicine_image_id , img_id); // memberid);
        values.put(medrem_medicine_image_first_color_id , first_color_id);
        values.put(medrem_medicine_image_second_color_id , second_color_id);

        db.update(TABLE_MED_REM_MEDICINE_MASTER, values, " medicine_id = '" + medicine_id+"'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error updateSettingData");
        }
        finally {
            db.endTransaction();
        }
    }


    public Integer getMaxid_med_reminder(String member_id) {
        Integer id;

//		String countsQuery = "SELECT  MAX(reminder_id) FROM " + TABLE_MED_REM_SET_TIME_QUANTITY;
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(countsQuery, null);
//		int rowCount = cursor.getCount();
//		db.close();
//		cursor.close();

        String countQuery = "SELECT " + mdrem_id + " AS _id,MAX(reminder_id) as Maxid FROM " + TABLE_MED_REM_SET_TIME_QUANTITY+" where MemberId = "+member_id;;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        id = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }
        //int rowCount = cursor.getCount();

//		cursor.close();
        //SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "getAllData SQL: " + countQuery);
        //return db.rawQuery(countQuery, null);

        return id;
        //return db.rawQuery(countQuery, null);
        // return row count


    }


    public Integer getMaxid_med_timeslot_id(String member_id) {
        Integer id;


        String countQuery = "SELECT  MAX(reminder_id) as Maxid FROM " + TABLE_MED_TIMESLOT_MASTER;;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        id = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }



        Log.d(TAG, "getAllData SQL: " + countQuery);
        //return db.rawQuery(countQuery, null);

        return id;


    }

    public void addMedFriend(String Friend_id,String FriendName,String PhoneNo,String Email_id,String ImageName,String InvitationCode,Boolean AcceptFlag) {

        SQLiteDatabase db = this.getWritableDatabase ( );
        db.beginTransaction();
        try {
        ContentValues values = new ContentValues();
        //values.put(mdrem_medfriend_id, medfriend_id); // Name
      //  values.put(mdrem_medfrined_rem_id, rem_id); // Name
        values.put(mdrem_medfriend_id, Friend_id);
        values.put(mdrem_medfrined_friendname, FriendName);
        values.put(mdrem_medfrined_phone_no, PhoneNo);
        values.put(mdrem_medfrined_email_id, Email_id);
        values.put(mdrem_medfrined_image_name, ImageName);
        values.put(mdrem_medfrined_invitation_code, InvitationCode);
        values.put(mdrem_medfrined_accept_flag, AcceptFlag);

        // Inserting Row
        long id = db.insertOrThrow(TABLE_MED_REM_MED_FRIEND_TABLE, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error updateSettingData");
        }
        finally {
            db.endTransaction();
        }

    }

    public void delete_med_friend(String p_id) {


        SQLiteDatabase db = this.getWritableDatabase ( );
        db.beginTransaction ( );
        try {
        int i=db.delete(TABLE_MED_REM_MED_FRIEND_TABLE, "id = " + p_id, null);
            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
            Log.d ( TAG, "Deleted deleteGoalBPDetails" );
        } finally {
            db.endTransaction ( );
        }
    }

    public int getRowCountTimeslotMaster() {
        String countQuery = "SELECT  * FROM " + TABLE_MED_TIMESLOT_MASTER;
        SQLiteDatabase  db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();

        cursor.close();

        // return row count
        return rowCount;
    }


    public void Delete_TimeslotMaster() {

        SQLiteDatabase db = this.getWritableDatabase ( );
        db.beginTransaction ( );
        try {

        db.delete(TABLE_MED_TIMESLOT_MASTER, null, null);
            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
        } finally {
            db.endTransaction ( );
        }
    }
    public void addMed_Doctor_master(Integer DocId,String DotorName,Integer DistrictId,String GUID) {
        SQLiteDatabase  db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(mdrem_Doctid, DocId); // Name
        values.put(medrem_DoctorName, DotorName);
        values.put(medrem_DistrictId, DistrictId);
        values.put(medrem_GUID, GUID);


        // Inserting Row
        long id = db.insert(TABLE_MED_REM_DOCTORMASTER, null, values);

    }

    public void Delete_MedRem_DoctorMaster()
    {
        SQLiteDatabase  db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MED_REM_DOCTORMASTER);

    }
    public void addMed_timeslot_master(Integer tm_id, String rem_id, String timeslot, Integer hour, Integer slotid, Integer quantity) {

        SQLiteDatabase  db = getWritableDatabase ( );

        db.beginTransaction ( );
        try {
        ContentValues values = new ContentValues();
        //values.put(mdrem_medfriend_id, medfriend_id); // Name
        values.put(mdrem_timeslotm_id, tm_id);
        values.put(mdrem_timeslot_rem_id, rem_id); // Name
        values.put(mdrem_timeslots, timeslot);
        values.put(mdrem_timeslots_hour, hour);
        values.put(mdrem_timeslots_section, slotid);
        values.put(medrem_timeslot_quantity, quantity);

        // Inserting Row
        long id = db.insertOrThrow(TABLE_MED_TIMESLOT_MASTER, null, values);

            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
            //  Log.d ( TAG, "Error addBPDetails" );
        } finally {
            db.endTransaction ( );
        }
    }

    public Cursor getAllDataMedTimeslot(Integer pos) {
        Cursor crs=null;

        //String buildSQL =" SELECT "+ F_KEY_BPID+ " AS _id,*"+ " FROM " + TABLE_BP;
        String buildSQL;
        buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id =1 ";

        if (pos == 1) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id =1 ";
        }
        if (pos == 2) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in(1,23) ";
        }
        if (pos == 3) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in (1,12,23) ";
        }
        if (pos == 4) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in (1,19,14,20) ";
        }
        if (pos == 5) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in (8,11,14,17,20) ";
        }
        if (pos == 6) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in (1,5,10,13,20,23) ";
        }
        if (pos == 7) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in (1,4,8,12,15,19,23) ";
        }
        if (pos == 8) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in(1,4,8,12,15,19,19,23) ";
        }
        if (pos == 9) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in (1,5,6,11,12,16,17,22,23) ";
        }
        if (pos == 10) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in (1,3,7,8,11,14,15,16,18,22,23) ";
        }

        if (pos == 11) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in (1,2,5,9,10,12,13,16,20,21,23) ";
        }
        if (pos == 12) {
            buildSQL = " SELECT  " + mdrem_timeslotm_id + "  AS  _id," + mdrem_timeslots + "," + " medicine_quantity " + "  FROM  " + TABLE_MED_TIMESLOT_MASTER + " where timeslot_id in (1,2,4,7,9,10,11,14,16,18,21,23) ";
        }
        //buildSQL =" SELECT  "+ mdrem_timeslotm_id+ "  AS  _id,"+ mdrem_timeslots+ "  FROM  " + TABLE_MED_TIMESLOT_MASTER+"where timeslot_id =1 ";

        SQLiteDatabase  db = this.getReadableDatabase();
        Log.d(TAG, "getAllData SQL: " + buildSQL);
       crs=db.rawQuery(buildSQL, null);

        return crs;

    }

    public void addMedDetailsfinal(String rem_id, Integer pos) {
        // db = this.getWritableDatabase();
        String updateQuery;
        String qry = "INSERT INTO " + TABLE_MED_REM_DETAIL + "(timeslot,reminder_id,medicine_quantity)  SELECT time_slots,reminder_id,medicine_quantity  FROM " + TABLE_MED_TIMESLOT_MASTER + " where " + TABLE_MED_TIMESLOT_MASTER + ".timeslot_id";

        updateQuery = qry + " =1";
        if (pos == 1) {
            updateQuery = qry + " =1";
        }
        if (pos == 2) {
            updateQuery = qry + " in(1,23)";
        }
        if (pos == 3) {
            updateQuery = qry + " in(1,12,23)";
        }
        if (pos == 4) {
            updateQuery = qry + " in(1,19,14,20)";
        }
        if (pos == 5) {
            updateQuery = qry + " in(8,11,14,17,20)";
        }
        if (pos == 6) {
            updateQuery = qry + " in(1,5,10,13,20,23)";
        }
        if (pos == 7) {
            updateQuery = qry + " in(1,4,8,12,15,19,23)";
        }
        if (pos == 8) {
            updateQuery = qry + " in(1,4,8,12,15,19,19,23)";
        }
        if (pos == 9) {
            updateQuery = qry + " in(1,5,6,11,12,16,17,22,23)";
        }
        if (pos == 10) {
            updateQuery = qry + " in(1,3,7,8,11,14,15,16,18,22,23)";
        }
        if (pos == 11) {
            updateQuery = qry + " in(1,2,5,9,10,12,13,16,20,21,23)";
        }
        if (pos == 12) {
            updateQuery = qry + " in(1,2,4,7,9,10,11,14,16,18,21,23)";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updateQuery);

		/*ContentValues values = new ContentValues();
		values.put(mdrem_detail_remid, rem_id); // Name
		//values.put(mdrem_detail_id, details); // Name
		values.put(medrem_detail_timeslot, timeslot); // Name
		values.put(medrem_detail_timeslot_id, timslot_id);
		values.put(medrem_detail_timeslot_active, active);*/


        // Inserting Row
//		long id = db.insert(TABLE_MED_REM_DETAIL, null, values);

    }

    public void addMedDetails_update(Integer rem_id, Integer active) {
        SQLiteDatabase  db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(medrem_detail_timeslot_active, 1); // Name
        //values.put(mdrem_detail_id, details); // Name
        //values.put(medrem_detail_timeslot, timeslot); // Name
        //values.put(medrem_detail_timeslot_active, timeslot);


        // Inserting Row
        //long id = db.insert(TABLE_MED_REM_DETAIL, null, values);
        db.update(TABLE_MED_REM_DETAIL, values, "reminder_id = '" + rem_id +"'"+ "  and timeslot_id = " + active, null);

    }

    public void addMedtimeSlotUpdate_update(Integer slot_id, String slots, Integer hour, Integer timeslot_id, Double quantity) {


        SQLiteDatabase db = this.getWritableDatabase ( );
        db.beginTransaction();
        try {
        ContentValues values = new ContentValues();
        values.put(mdrem_timeslots, slots);
        values.put(mdrem_timeslots_hour, hour);
        values.put(mdrem_timeslots_section, timeslot_id);
        values.put(medrem_timeslot_quantity, quantity);
        // Name
        //values.put(mdrem_detail_id, details); // Name
        //values.put(medrem_detail_timeslot, timeslot); // Name
        //values.put(medrem_detail_timeslot_active, timeslot);


        // Inserting Row
        //long id = db.insert(TABLE_MED_REM_DETAIL, null, values);
        db.update(TABLE_MED_TIMESLOT_MASTER, values, " timeslot_id = " + slot_id, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error EditSaveBPDetails");
        }
        finally {
            db.endTransaction();
        }
    }

    public void delete_med_reminder_schedule(String p_id,String Mem_id) {
        SQLiteDatabase  db = getWritableDatabase();
        db.beginTransaction();
        try {

        int id=db.delete(TABLE_MED_REM_SCHEDULE, "reminder_id = '" + p_id +"' and MemberId="+Mem_id, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
            db.endTransaction();
        }
    }


    public void delete_med_reminder_schedule_on_schedule_id(String p_id,String Mem_id) {
        SQLiteDatabase  db = getWritableDatabase();
        db.setLockingEnabled(false);
       // db.execSQL("PRAGMA synchronous=OFF");



        try {
            int id=db.delete(TABLE_MED_REM_SCHEDULE, "schedule_id = '" + p_id +"' and MemberId="+Mem_id, null);

        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
          //  db.close();
        }
    }

    public Cursor get_med_master_data(String p_id,String member_id) {

        Cursor crs=null;
        String buildSQL = " SELECT " + " * FROM " + TABLE_MED_REM_SET_TIME_QUANTITY + " where reminder_id = '" + p_id+"'"+" and MemberId = "+member_id;;

        SQLiteDatabase  db = getWritableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);
        crs=db.rawQuery(buildSQL, null);

        return  crs;
    }

    public Cursor get_med_Empty_data(String member_id) {

        Cursor crs=null;

        String buildSQL = " SELECT " + " * FROM " + TABLE_MED_REM_SET_TIME_QUANTITY + " where  MemberId = "+member_id + " and medicine_id  is null or medicine_id = ''";;

        SQLiteDatabase  db = getWritableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);
        crs=db.rawQuery(buildSQL, null);

        return crs;
    }

    public Cursor get_previous_reminder_data(String member_id,String Medicine_id) {

        Cursor crs=null;

        String buildSQL = " SELECT " + " * FROM " + TABLE_MED_REM_SET_TIME_QUANTITY + " where  MemberId = "+member_id + " and medicine_id ='"+Medicine_id+"'";;

        SQLiteDatabase  db = getWritableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);
        crs=db.rawQuery(buildSQL, null);

        return crs;
    }

    public Cursor get_reminder_id_of_Medicine(String p_id,String member_id) {
        Cursor crs=null;
        String reminder_id="0";
        String buildSQL="";

        String qry_last= "  where medicine_id = '" + p_id+"'";

        if(p_id.equals(""))
        {
             buildSQL = " SELECT " + " reminder_id  FROM " + TABLE_MED_REM_SET_TIME_QUANTITY +" where MemberId = "+member_id; ;
        }else{
            buildSQL = " SELECT " + " reminder_id  FROM " + TABLE_MED_REM_SET_TIME_QUANTITY  +qry_last+" and MemberId = "+member_id;;
        }


        SQLiteDatabase  db = getWritableDatabase();


         crs=db.rawQuery(buildSQL, null);

        return  crs;
    }


    public Cursor get_med_detail_data(String p_id,String member_id) {
        Cursor crs=null;

        String buildSQL = " SELECT  * FROM " + TABLE_MED_REM_DETAIL + " where reminder_id = '" + p_id+"'" + " order by details_id ";

        SQLiteDatabase  db = getWritableDatabase(); db = this.getReadableDatabase();
        Log.d(TAG, "med_detail SQL: " + buildSQL);


        crs=db.rawQuery(buildSQL, null);

        return crs;
    }

    public void insert_med_reminder_schedule(String Schedule_id,String p_id, String p_status, String p_datetime_set, String p_datetime_taken,
                                             Integer p_sequence, String p_dosage_type, Double p_dosage, Double p_quantity,String Member_id,Integer rel_id) {

        SQLiteDatabase  db = getWritableDatabase();
        db.setLockingEnabled(false);
       // db.execSQL("PRAGMA synchronous=OFF");

        try {
        ContentValues values = new ContentValues();
        values.put(MDREM_SCHEDULE_ID, Schedule_id);
        values.put(MDREM_SCHEDULE_REM_ID, p_id);
        values.put(MDREM_SCHEDULE_STATUS, p_status);
        values.put(MDREM_SCHEDULE_DATETIME_SET, p_datetime_set);
        values.put(MDREM_SCHEDULE_DATETIME_TAKEN, p_datetime_taken);
        values.put(MDREM_SCHEDULE_SEQUENCE, p_sequence);
        values.put(MDREM_SCHEDULE_DOSAGE_TYPE, p_dosage_type);
        values.put(MDREM_SCHEDULE_DOSAGE, p_dosage);
        values.put(MDREM_SCHEDULE_QUANTITY, p_quantity);
        values.put(MDREM_SCHEDULE_member_id, Member_id);
        values.put(MDREM_SCHEDULE_relationship_id, rel_id);

            db.insertOrThrow(TABLE_MED_REM_SCHEDULE, null, values);
        } catch (Exception e) {
            Log.d(TAG, "Error InsertSyncTable");
        } finally {
           // if (db.isOpen()); {db.close(); };
        }


    }

    public void addMedRemMaster_update(String mid,String MEd_id, String medName, Integer remTypeID, String rem_type,
                                       Integer schduration_id, Integer SchDurationValue, String schDate,
                                       Integer daysIntervelId, String daysIntervelValue, Integer useplacebo,
                                       Integer img_id, Integer f_color_id,Integer s_color_id ,String instr, Integer dos_amont,
                                       String dos_unit, String condition,String Doctor_id,String pill_buddy_id,String Member_id,Integer rel_id,String ringtone
    ,Integer refill_flag,String Refill_date,Integer Number_of_units,String pack_size,int notified,int no_reminder_flag) {

        SQLiteDatabase db = this.getWritableDatabase ( );

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            //values.put(mdrem_id, mid);
            values.put(med_name, medName); // Name
             values.put(med_id, MEd_id);
            //values.put(med_rem_condition, condtn);
            values.put(med_rem_type_id, remTypeID); // Name
            values.put(med_rem_type, rem_type); // Name
            values.put(med_rem_sch_duration_id, schduration_id);// Name
            values.put(med_rem_sch_duration_value, SchDurationValue);
            values.put(med_rem_sch_duration_date, schDate); // Email
            values.put(med_rem_days_interval_id, daysIntervelId);
            values.put(med_rem_days_interval_value, daysIntervelValue);
            values.put(med_rem_days_interval_useplacebo, useplacebo);
            values.put(medrem_image_id, img_id);
            values.put(medrem_image_first_color_id, f_color_id); // memberid);
            values.put(medrem_image_second_color_id, s_color_id);
            values.put(med_rem_instructions, instr);
            values.put(med_rem_dos_amount, dos_unit);
            values.put(med_rem_dos_unit, dos_amont);
            values.put(med_rem_conditions, condition);
            values.put(med_rem_med_doctor, Doctor_id);
            values.put(med_rem_pill_buddy, pill_buddy_id);
            values.put(med_rem_member_id, Member_id);
            values.put(med_rem_relationship_id, rel_id);
            values.put(med_rem_refill_flag, refill_flag);
            values.put(med_rem_refill_date, Refill_date);
            values.put(med_rem_no_of_units, Number_of_units);
            values.put( med_rem_packsize,pack_size);
            values.put( med_rem_notified,notified);
            values.put( med_rem_no_reminder,no_reminder_flag);





            values.put(med_rem_ringtone, ringtone);



            // Inserting Row
            long id = db.update(TABLE_MED_REM_SET_TIME_QUANTITY, values, "reminder_id='" + mid+"'", null);
            String y= String.valueOf(id);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
            db.endTransaction();
        }
    }


    public void addMedRemMaster_update_refill_reminder(String mid,Integer refill_flag,String Refill_date,Integer Number_of_units,int notified) {

        SQLiteDatabase db = this.getWritableDatabase ( );

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(med_rem_refill_flag, refill_flag);
            values.put(med_rem_refill_date, Refill_date);
            values.put(med_rem_no_of_units, Number_of_units);
            values.put(med_rem_notified, notified);

            // Inserting Row
            long id = db.update(TABLE_MED_REM_SET_TIME_QUANTITY, values, "reminder_id='" + mid+"'", null);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
            db.endTransaction();
        }
    }



    public void addMedRemMaster_update_refill_notified(String mid,int notified) {

        SQLiteDatabase db = this.getWritableDatabase ( );

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(med_rem_notified, notified);

            // Inserting Row
            long id = db.update(TABLE_MED_REM_SET_TIME_QUANTITY, values, "reminder_id='" + mid+"'", null);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
            db.endTransaction();
        }
    }



    public Cursor get_med_rem_sch_daywise_Total(String p_date, String p_date1) {

        String buildSQL =
                " Select sum(total_m) as m,sum(total_a) as a,sum(total_e) as e,sum(total_n) as n from (" +
                        " SELECT count(*) as total_m,0 as total_a,0 as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where datetime(datetime_set) between datetime(\""
                        + p_date + " 06:00:00\")" + "  AND datetime(\"" + p_date + " 11:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,count(*) as total_a,0 as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where datetime(datetime_set) between datetime(\""
                        + p_date + " 12:00:00\")" + "  AND datetime(\"" + p_date + " 17:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,0 as total_a,count(*) as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where datetime(datetime_set) between datetime(\""
                        + p_date + " 18:00:00\")" + "  AND datetime(\"" + p_date + " 23:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,0 as total_a,0 as total_e,count(*) as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where datetime(datetime_set) between datetime(\""
                        + p_date1 + " 00:00:00\")" + "  AND datetime(\"" + p_date1 + " 05:59:59\")"
                        + " ) a ";

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_med_rem_sch_daywise_taken(String p_date, String p_date1) {

        String buildSQL =
                " Select sum(total_m) as m,sum(total_a) as a,sum(total_e) as e,sum(total_n) as n from (" +
                        " SELECT count(*) as total_m,0 as total_a,0 as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"T\" and datetime(datetime_set) between datetime(\""
                        + p_date + " 06:00:00\")" + "  AND datetime(\"" + p_date + " 11:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,count(*) as total_a,0 as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"T\" and datetime(datetime_set) between datetime(\""
                        + p_date + " 12:00:00\")" + "  AND datetime(\"" + p_date + " 17:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,0 as total_a,count(*) as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"T\" and datetime(datetime_set) between datetime(\""
                        + p_date + " 18:00:00\")" + "  AND datetime(\"" + p_date + " 23:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,0 as total_a,0 as total_e,count(*) as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"T\" and datetime(datetime_set) between datetime(\""
                        + p_date1 + " 00:00:00\")" + "  AND datetime(\"" + p_date1 + " 05:59:59\")"
                        + " ) a ";

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_med_rem_sch_daywise_pending(String p_date, String p_date1) {

        String buildSQL =
                " Select sum(total_m) as m,sum(total_a) as a,sum(total_e) as e,sum(total_n) as n from (" +
                        " SELECT count(*) as total_m,0 as total_a,0 as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"SCH\" and datetime(datetime_set) between datetime(\""
                        + p_date + " 06:00:00\")" + "  AND datetime(\"" + p_date + " 11:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,count(*) as total_a,0 as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"SCH\" and datetime(datetime_set) between datetime(\""
                        + p_date + " 12:00:00\")" + "  AND datetime(\"" + p_date + " 17:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,0 as total_a,count(*) as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"SCH\" and datetime(datetime_set) between datetime(\""
                        + p_date + " 18:00:00\")" + "  AND datetime(\"" + p_date + " 23:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,0 as total_a,0 as total_e,count(*) as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"SCH\" and datetime(datetime_set) between datetime(\""
                        + p_date1 + " 00:00:00\")" + "  AND datetime(\"" + p_date1 + " 05:59:59\")"
                        + " ) a ";

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_med_rem_sch_daywise_skip(String p_date, String p_date1) {

        String buildSQL =
                " Select sum(total_m) as m,sum(total_a) as a,sum(total_e) as e,sum(total_n) as n from (" +
                        " SELECT count(*) as total_m,0 as total_a,0 as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"S\" and datetime(datetime_set) between datetime(\""
                        + p_date + " 06:00:00\")" + "  AND datetime(\"" + p_date + " 11:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,count(*) as total_a,0 as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"S\" and datetime(datetime_set) between datetime(\""
                        + p_date + " 12:00:00\")" + "  AND datetime(\"" + p_date + " 17:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,0 as total_a,count(*) as total_e,0 as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"S\" and datetime(datetime_set) between datetime(\""
                        + p_date + " 18:00:00\")" + "  AND datetime(\"" + p_date + " 23:59:59\")" +
                        " UNION " +
                        " SELECT 0 as total_m,0 as total_a,0 as total_e,count(*) as total_n FROM " + TABLE_MED_REM_SCHEDULE
                        + " where status=\"S\" and datetime(datetime_set) between datetime(\""
                        + p_date1 + " 00:00:00\")" + "  AND datetime(\"" + p_date1 + " 05:59:59\")"
                        + " ) a ";

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }

    public String check_schedule(String member_id) {

        String countQuery = "SELECT count(*) as count_row FROM " + TABLE_MED_REM_SCHEDULE+" where MemberId = "+member_id;;
        SQLiteDatabase  db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        String rowCount = "0";
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            rowCount = cursor.getString(0);
        }

        cursor.close();
        return rowCount;
    }

    public Cursor get_med_rem_sch_daywise_session(String p_type, String p_date,String member_id) {
        Cursor crs=null;
        String sDT = "";
        if (p_type.equals("m")) {
            sDT = "and datetime(datetime_set) between datetime(\"" + p_date + " 06:00:00\")" + "  AND datetime(\"" + p_date + " 11:59:59\")";
        } else if (p_type.equals("a")) {
            sDT = "and datetime(datetime_set) between datetime(\"" + p_date + " 12:00:00\")" + "  AND datetime(\"" + p_date + " 17:59:59\")";

        } else if (p_type.equals("e")) {
            sDT = "and datetime(datetime_set) between datetime(\"" + p_date + " 18:00:00\")" + "  AND datetime(\"" + p_date + " 23:59:59\")";

        } else if (p_type.equals("n")) {
            sDT = "and datetime(datetime_set) between datetime(\"" + p_date + " 00:00:00\")" + "  AND datetime(\"" + p_date + " 05:59:59\")";
        } else if (p_type.equals("all")) {
            sDT = "and datetime(datetime_set) between datetime(\"" + p_date + " 00:00:00\")" + "  AND datetime(\"" + p_date + " 23:59:59\")";
        }

        String buildSQL =
                " SELECT m.*,s.schedule_id,s.status,s.datetime_set,s.dosage FROM " + TABLE_MED_REM_SCHEDULE + " s, " + TABLE_MED_REM_SET_TIME_QUANTITY + " m " +
                        " where s.reminder_id = m.reminder_id  " + sDT +" and s.MemberId=m.MemberId and   s.MemberId = "+member_id+ " order by datetime(datetime_set) ";

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);
        crs=db.rawQuery(buildSQL, null);
        return  crs;
    }

    public Cursor get_med_rem_sch_all_medicine(String member_id) {

        String buildSQL = " SELECT * FROM " + TABLE_MED_REM_SET_TIME_QUANTITY +
                " where medicine_name <> \"\" "+" and  MemberId = "+member_id;;

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_med_all_medicine_master(String member_id) {
        Cursor crs=null;

        String buildSQL = " SELECT * FROM " + TABLE_MED_REM_MEDICINE_MASTER  +
                " where medicine_name <> \"\" "+" and MemberId = "+member_id;;

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);
        crs=db.rawQuery(buildSQL, null);

        return crs;
    }


    public Cursor get_all_pill_buddyr(String member_id) {
         Cursor crs=null;
        String buildSQL = " SELECT * FROM " + TABLE_MED_REM_MED_FRIEND_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);
        crs= db.rawQuery(buildSQL, null);
        return crs;
    }

    public void delete_med_friend_all_data() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
        int i=db.delete(TABLE_MED_REM_MED_FRIEND_TABLE, "medfriend_id <>'-99' ", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
            db.endTransaction();
        }
    }


    public void Delete_Medicine_master_details(String p_id,String member_id) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

        db.delete(TABLE_MED_REM_SET_TIME_QUANTITY, "reminder_id = '" + p_id+"'" +" and MemberId= " + member_id, null);
        db.delete(TABLE_MED_REM_DETAIL, "reminder_id = '" + p_id+"'", null);
        db.delete(TABLE_MED_REM_SCHEDULE, "reminder_id = '" + p_id +"' and MemberId= " + member_id, null);
        db.delete(TABLE_MED_TIMESLOT_MASTER, "reminder_id = '" + p_id+"'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
            db.endTransaction();
        }

    }

    public void delete_medreminder_table_on_reminder_id(String p_id,String member_id)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        db.setLockingEnabled(false);
       // db.execSQL("PRAGMA synchronous=OFF");



        try {
       int id= db.delete(TABLE_MED_REM_SET_TIME_QUANTITY, "reminder_id = '" + p_id+"'" +" and MemberId= " + member_id, null);

        } catch ( Exception e ) {
        } finally {
          // db.close();
        }
    }
    public void Delete_empty_fields(String p_id) {


        SQLiteDatabase  db = this.getWritableDatabase();
        db.beginTransaction ( );
        try {
        db.delete(TABLE_MED_REM_SET_TIME_QUANTITY, "reminder_id = '" + p_id+"'" , null);
        db.delete(TABLE_MED_REM_DETAIL, "reminder_id = '" + p_id+"'", null);
        db.delete(TABLE_MED_REM_SCHEDULE, "reminder_id = '" + p_id+"'" , null);
        db.delete(TABLE_MED_TIMESLOT_MASTER, "reminder_id = '" + p_id+"'", null);
            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
        } finally {
            db.endTransaction ( );
        }


    }
    public  void delete_med_rem_details(String p_id )
    {
        SQLiteDatabase  db = this.getWritableDatabase();
        db.beginTransaction ( );
        try {
        db.delete(TABLE_MED_REM_DETAIL, "reminder_id = '" + p_id+"'", null);
            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
        } finally {
            db.endTransaction ( );
        }

    }


    public void Delete_Medicine_master(String p_id,String mem_id) {

        SQLiteDatabase  db = this.getWritableDatabase();
       db.setLockingEnabled(false);
       // db.execSQL("PRAGMA synchronous=OFF");




        try {
        int id=db.delete(TABLE_MED_REM_MEDICINE_MASTER, "medicine_id = '" + p_id+"' and MemberId="+mem_id, null);
                db.delete(TABLE_MED_REM_DETAIL, "reminder_id = '" + p_id+"'", null);
                db.setTransactionSuccessful ( );
            } catch ( Exception e ) {
            } finally {
              //db.close();
            }

    }

    public void Update_Medicine_Schedule(String p_id, String p_status, String p_date) {


        SQLiteDatabase  db = this.getWritableDatabase();
        db.beginTransaction ( );
        try {

        ContentValues values = new ContentValues();
        values.put("status", p_status);

       /* if (p_status.equals("taken") && !p_date.equals("")) {
            values.put("datetime_taken", p_date);
        }*/
        if ( !p_date.equals("")) {
            values.put("datetime_taken", p_date);
        }

        long id = db.update(TABLE_MED_REM_SCHEDULE, values, "schedule_id = '" + p_id+"'", null);
            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
        } finally {
            db.endTransaction ( );
        }

    }

    public void Snooze_Medicine_Schedule(String p_id, String p_date,Integer snooze_count) {


        SQLiteDatabase  db = this.getWritableDatabase();
        db.beginTransaction ( );
        try {


        ContentValues values = new ContentValues();
        values.put("datetime_set", p_date);
        values.put("snooze_count", snooze_count);

        long id = db.update(TABLE_MED_REM_SCHEDULE, values, "schedule_id = '" + p_id+"'", null);
            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
        } finally {
            db.endTransaction ( );
        }

    }




    public void Update_Medicine_Schedule_server(String p_id, String datetime_set,String date_time_taken,String Status) {



        SQLiteDatabase   db = this.getWritableDatabase();
        db.beginTransaction ( );
        try {


        ContentValues values = new ContentValues();
        values.put(MDREM_SCHEDULE_STATUS, Status);
        values.put(MDREM_SCHEDULE_DATETIME_SET, datetime_set);
        values.put(MDREM_SCHEDULE_DATETIME_TAKEN, date_time_taken);

        long id = db.update(TABLE_MED_REM_SCHEDULE, values, "schedule_id = '" + p_id+"'", null);
            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
        } finally {
            db.endTransaction ( );
        }

    }

    public void suspend_Medicine_Schedule(String p_id, String Status,String f_date) {



        SQLiteDatabase   db = this.getWritableDatabase();
        db.beginTransaction ( );
        try {


            ContentValues values = new ContentValues();
            values.put(MDREM_SCHEDULE_STATUS, Status);



            long id = db.update(TABLE_MED_REM_SCHEDULE, values, "reminder_id = '" + p_id+"'   and datetime(datetime_set) >  datetime(\""+f_date+"\")", null);
            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
            e.toString();
        } finally {
            db.endTransaction ( );
        }

    }


    public Cursor get_med_rem_all_schedule_medicine(String first_date, String second_date, String Search_word,String member_id) {
        String MEdSearch = "";
        if (!Search_word.equals("")) {
            MEdSearch = "  AND medicine_name like '" + Search_word + "%'  ";
        }

        String sDT = "datetime(datetime_set) between datetime(\"" + first_date + " 00:00:00\")" + "  AND datetime(\"" + second_date + " 23:59:59\")";


        String buildSQL =
                " SELECT  m.*,s.schedule_id,s.status,s.datetime_set,s.dosage FROM " + TABLE_MED_REM_SCHEDULE + " s, " + TABLE_MED_REM_SET_TIME_QUANTITY + " m " +
                        " where s.reminder_id = m.reminder_id and s.MemberId=m.MemberId  and " + sDT + MEdSearch +" and s.MemberId = "+member_id+"  order by datetime(datetime_set) ";

        SQLiteDatabase  db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }


    public Cursor get_med_rem_all_schedule_medicine_for_pdf(String first_date, String second_date, String Search_word,String member_id) {
        String MEdSearch = "";
        if (!Search_word.equals("")) {
            MEdSearch = "  AND medicine_name like '" + Search_word + "%'  ";
        }

        String sDT = "datetime(datetime_set) between datetime(\"" + first_date + " 00:00:00\")" + "  AND datetime(\"" + second_date + " 23:59:59\")";


        String buildSQL =
                " SELECT  m.*,s.schedule_id,s.status,s.datetime_set,s.dosage FROM " + TABLE_MED_REM_SCHEDULE + " s, " + TABLE_MED_REM_SET_TIME_QUANTITY + " m " +
                        " where s.reminder_id = m.reminder_id and s.MemberId=m.MemberId  and " + sDT + MEdSearch +" and s.MemberId = "+member_id+"  order by m.medicine_name, datetime(datetime_set) ";

        SQLiteDatabase  db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }


    public Cursor get_schedule_status_for_pdf(String sch_date, String rem_id,String member_id) {


        String buildSQL =
                " SELECT * FROM " + TABLE_MED_REM_SCHEDULE  +
                        " where  MemberId = "+member_id+"  AND reminder_id = '" + rem_id+"'"+" and datetime(datetime_set) between datetime(\"" + sch_date + " 00:00:00\")" + "  AND datetime(\"" + sch_date + " 23:59:59\")" ;


        SQLiteDatabase  db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_med_detail_last_taken(String Reminder_id,String member_id) {
        String MEdSearch = "";
        String status = "T";


        String rem_filter = "  AND m.reminder_id =  '" + Reminder_id+"'";
        MEdSearch = " AND s.status= \"" + status + "\"";

        String buildSQL =
                " SELECT s.schedule_id, m.medicine_name,max(datetime(s.datetime_taken)) as lastTaken  FROM " + TABLE_MED_REM_SCHEDULE + " s, " + TABLE_MED_REM_SET_TIME_QUANTITY + " m " +
                        " where s.reminder_id = m.reminder_id and s.MemberId=m.MemberId " + MEdSearch + rem_filter+" and s.MemberId = "+member_id+ "  group by s.schedule_id, m.medicine_name ";

        SQLiteDatabase  db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }


    public Cursor get_med_detail_next_pending(String Reminder_id,String member_id) {
        String MEdSearch = "";
        String status = "SCH";


        String rem_filter = "  AND m.reminder_id =  '" + Reminder_id+"'";
        MEdSearch = " AND s.status= \"" + status + "\"";

        String buildSQL =
                " SELECT s.schedule_id, m.medicine_name,min(datetime(s.datetime_set)) as nextPending  FROM " + TABLE_MED_REM_SCHEDULE + " s, " + TABLE_MED_REM_SET_TIME_QUANTITY + " m " +
                        " where s.reminder_id = m.reminder_id and s.MemberId=m.MemberId " + MEdSearch + rem_filter +" and s.MemberId = "+member_id+ "  group by  m.medicine_name ";

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }
    public Cursor get_med_rem_sch_all_daywise_pending_taken(String p_date, String p_date1,String Reminder_id) {

        String qry_rem_id="";

        if(!Reminder_id.equals(""))
        {
            qry_rem_id=" reminder_id =  '"+Reminder_id+"'";
        }



        String buildSQL =
                " Select datetime_set ,sum(total_pending) as total_pending,sum(total_taken) as total_taken from (" +
                        " SELECT strftime('%Y %m %d',datetime_set) as datetime_set,count(*) as total_pending, 0 as total_taken FROM " + TABLE_MED_REM_SCHEDULE
                        + " where datetime(datetime_set) between datetime(\""
                        + p_date + " 00:00:00\")" + "  AND datetime(\"" + p_date1 + " 23:59:59\")" +" and status =\"SCH\"  "+qry_rem_id+"  GROUP BY strftime('%Y %m %d',datetime_set) "+
                        " UNION " +
                        " SELECT strftime('%Y %m %d',datetime_set) as datetime_set ,0 as total_pending,count(*) as total_taken FROM " + TABLE_MED_REM_SCHEDULE
                        + " where datetime(datetime_set) between datetime(\""
                        + p_date + " 00:00:00\")" + "  AND datetime(\"" + p_date1 + " 23:59:59\")" +"  and status =\"T\""+qry_rem_id+ "  GROUP by strftime('%Y %m %d',datetime_set) "+
                       " ) a  group by datetime_set ";

        SQLiteDatabase  db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_med_rem_sch_all_medicine_wise_pending_taken(String p_date, String p_date1,String med_id,String member_id) {


         String MEdSearch="";
        if (!med_id.equals("0")) {
            MEdSearch = "  AND M.medicine_id ='" + med_id+"'"  ;
        }
       // " AND M.medicine_id = " +  med_id

        String buildSQL =

                " Select datetime_set ,sum(total_pending) as total_pending,sum(total_taken) as total_taken,sum(total_skipped) as total_skipped from (" +
                        " SELECT strftime('%Y %m %d',TS.datetime_set) as datetime_set,count(*) as total_pending, 0 as total_taken,0 as total_skipped FROM " + TABLE_MED_REM_SCHEDULE+ " TS,"+ TABLE_MED_REM_MEDICINE_MASTER + " M,"+TABLE_MED_REM_SET_TIME_QUANTITY +" S "
                + " where TS.reminder_id = S.reminder_id AND S.medicine_id = M.medicine_id and S.MemberId=M.MemberId and ts.MemberId=s.MemberId AND  datetime(TS.datetime_set) between datetime(\""
                + p_date + " 00:00:00\")" + "  AND datetime(\"" + p_date1 + " 23:59:59\")" +" AND TS.status =\"SCH\"  " +MEdSearch +" and s.MemberId = "+member_id+"  GROUP BY strftime('%Y %m %d',TS.datetime_set) "+
                " UNION " +


                " SELECT strftime('%Y %m %d',TS.datetime_set) as datetime_set ,0 as total_pending,count(*) as total_taken,0 as total_skipped FROM " + TABLE_MED_REM_SCHEDULE+ " TS,"+ TABLE_MED_REM_MEDICINE_MASTER + " M,"+TABLE_MED_REM_SET_TIME_QUANTITY +" S "
                + " where TS.reminder_id = S.reminder_id AND S.medicine_id = M.medicine_id and S.MemberId=M.MemberId and ts.MemberId=s.MemberId  AND datetime(TS.datetime_set) between datetime(\""
                + p_date + " 00:00:00\")" + "  AND datetime(\"" + p_date1 + " 23:59:59\")" +"  AND TS.status =\"T\"" +  MEdSearch +" and s.MemberId = "+member_id+ "  GROUP by strftime('%Y %m %d',TS.datetime_set) "+


                " UNION " +

                " SELECT strftime('%Y %m %d',TS.datetime_set) as datetime_set ,0 as total_pending,0 as total_taken,count(*) as  total_skipped FROM " + TABLE_MED_REM_SCHEDULE+ " TS,"+ TABLE_MED_REM_MEDICINE_MASTER + " M,"+TABLE_MED_REM_SET_TIME_QUANTITY +" S "
                + " where TS.reminder_id = S.reminder_id AND S.medicine_id = M.medicine_id and S.MemberId=M.MemberId and ts.MemberId=s.MemberId  AND datetime(TS.datetime_set) between datetime(\""
                + p_date + " 00:00:00\")" + "  AND datetime(\"" + p_date1 + " 23:59:59\")" +"  AND TS.status =\"S\"" +  MEdSearch +" and s.MemberId = "+member_id+ "  GROUP by strftime('%Y %m %d',TS.datetime_set) "+
                " ) a  group by datetime_set ";


        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }


    public Cursor get_top_scheduled_time(String p_date) {


        String MEdSearch = "";
        String status = "SCH";
        MEdSearch = " AND s.status= \"" + status + "\"";

        String buildSQL = " SELECT  datetime_set  FROM " + TABLE_MED_REM_SCHEDULE +" where datetime(datetime_set) between datetime(\""
                + p_date + " 00:00:00\")"+" order by datetime_set desc limit 1"  ;

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }


    public Cursor get_top_scheduled_time_on_current_time(String p_date,String member_id) {

        String MEdSearch = "";
        String status = "SCH";
        MEdSearch = " AND status= \"" + status + "\"";


        String buildSQL = " SELECT  *  FROM " + TABLE_MED_REM_SCHEDULE +" where datetime_set= '"+p_date+"'" +MEdSearch+" and MemberId = "+member_id; ;

        SQLiteDatabase  db = this.getReadableDatabase();


        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_refill_reminder(String p_date,String member_id) {

        String MEdSearch = "";
        String status = "SCH";
        MEdSearch = " AND status= \"" + status + "\"";


        String buildSQL = " SELECT  *  FROM " + TABLE_MED_REM_SET_TIME_QUANTITY +" where RefillDate= '"+p_date+"'" +" and RefillRem=1 and notified=0 and MemberId = "+member_id; ;

        SQLiteDatabase  db = this.getReadableDatabase();


        return db.rawQuery(buildSQL, null);
    }


    public Cursor get_top_scheduled_time_get_doctor_data(String Doc_id,String member_id) {



        String buildSQL = " SELECT  *  FROM " + TABLE_MED_REM_DOCTORMASTER +" where ID=" +Doc_id;

        SQLiteDatabase  db = this.getReadableDatabase();


        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_top_scheduled_time_get_medfriend_data(String med_friend_id) {



        String buildSQL = " SELECT  *  FROM " + TABLE_MED_REM_MED_FRIEND_TABLE +" where medfriend_id= " +med_friend_id ;

        SQLiteDatabase  db = this.getReadableDatabase();


        return db.rawQuery(buildSQL, null);
    }
    public int check_schedule_exist(String Schedule_id) {
        String countQuery = "SELECT  * FROM " + TABLE_MED_REM_NOTIFICATION+" where schedule_id='"+Schedule_id+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();

        cursor.close();

        // return row count
        return rowCount;
    }



    public void Insert_into_notification(String reminder_id,String schedule_id,String scheduled_time,String rescheduled_time,String snooze_duration) {
        SQLiteDatabase  db = getWritableDatabase();
        db.beginTransaction();
        try {
        ContentValues values = new ContentValues();

        values.put(mdrem_reminder_id, reminder_id); // Name
        values.put(medrem_schedule_id, schedule_id);
        values.put(medrem_scheduled_time, scheduled_time);
        values.put(medrem_rescheduled_time, rescheduled_time);
        values.put(medrem_snooze_duration, snooze_duration);


        // Inserting Row
        long id = db.insertOrThrow(TABLE_MED_REM_NOTIFICATION, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
            db.endTransaction();
        }
    }

    public Cursor get_all_notification_data(String member_id) {

        String buildSQL = " SELECT s.*,n.notify_id,m.* FROM " + TABLE_MED_REM_NOTIFICATION+" n , "+TABLE_MED_REM_SET_TIME_QUANTITY+" m,"+TABLE_MED_REM_SCHEDULE+" s"
                +" where n.schedule_id=s.schedule_id and m.reminder_id=s.reminder_id and m.reminder_id=n.reminder_id  and m.MemberId=s.MemberId "+" and m.MemberId = "+member_id;;

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_notification_data_for_schedule_id(String scheduled_id,String member_id) {
        Cursor crs=null;

        String buildSQL = " SELECT s.*,m.* FROM " +TABLE_MED_REM_SET_TIME_QUANTITY+" m,"+TABLE_MED_REM_SCHEDULE+" s"
                +" where  m.reminder_id=s.reminder_id and m.MemberId=s.MemberId and s.schedule_id='"+scheduled_id+"' and m.MemberId = "+member_id;

        SQLiteDatabase db =  this.getReadableDatabase();


        Log.d(TAG, "med_master SQL: " + buildSQL);

        crs= db.rawQuery(buildSQL, null);
        return crs;
    }


    public Cursor get_search_medicine (String p_search_name,String member_id) {
        Cursor crs=null;

        String buildSQL = "select * FROM " + TABLE_MED_REM_MEDICINE_MASTER
                + " WHERE medicine_name like '" + p_search_name +  "%' "+" and MemberId = "+member_id;;

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "getAllData SQL: " + buildSQL);
        crs=db.rawQuery(buildSQL, null);

        return crs;
    }



    public void update_Reschedule_time(String schedule_id,String new_time ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
        ContentValues values = new ContentValues();
        values.put(MDREM_SCHEDULE_DATETIME_SET, new_time);

        long id= db.update(TABLE_MED_REM_SCHEDULE, values, " schedule_id = '" + schedule_id+"'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
            db.endTransaction();
        }
    }
    public void delete_from_notification_table(String p_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
        long id= db.delete(TABLE_MED_REM_NOTIFICATION, "schedule_id = '" + p_id+"'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
            db.endTransaction();
        }
    }

    public Cursor get_last_entry_reminder_medicine (String member_id,String Medicine_id) {

        String buildSQL = "select * FROM " + TABLE_MED_REM_MEDICINE_MASTER+" where MemberId = "+member_id
                + " and medicine_id='"+Medicine_id+"'";

        SQLiteDatabase  db = this.getReadableDatabase();

        return db.rawQuery(buildSQL, null);
    }

    public Cursor get__entry_on_medicine_id_reminder_medicine (String Medicine_id,String member_id) {

        String buildSQL = "select * FROM " + TABLE_MED_REM_MEDICINE_MASTER
                + " where  medicine_id = '"+Medicine_id+"' and MemberId = "+member_id;;

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(buildSQL, null);
    }
    public Cursor get_schedule_on_reminder_id(String rem_id,String member_id) {

        String buildSQL = "select * FROM " + TABLE_MED_REM_SCHEDULE  + " where  reminder_id = '"+rem_id +"' and MemberId = "+member_id;
        SQLiteDatabase  db = this.getReadableDatabase();


        return db.rawQuery(buildSQL, null);
    }

    public Cursor get_schedule_on_schedule_id(String schedule_id,String member_id) {

        String buildSQL = "select * FROM " + TABLE_MED_REM_SCHEDULE  + " where  schedule_id = '"+schedule_id +"' and MemberId = "+member_id;
        SQLiteDatabase  db = this.getReadableDatabase();


        return db.rawQuery(buildSQL, null);
    }
    public String  get_ringtone_reminder(String rem_id)
    {
        String ringtone="";
        String buildSQL = "select ringtone FROM " + TABLE_MED_REM_SET_TIME_QUANTITY  + " where  reminder_id = '"+rem_id +"' ";
        SQLiteDatabase  db = this.getReadableDatabase();

        Cursor crs = db.rawQuery ( buildSQL, null );

        if ( crs.moveToFirst ( ) ) {
            do {
                ringtone = crs.getString ( crs.getColumnIndex ( "ringtone" ) );
            } while ( crs.moveToNext ( ) );
        }
        return ringtone;
    }

    public Cursor  check_reminder_status_on_reminder_id(String Reminder_id) {

        String buildSQL = " SELECT  no_reminder  FROM " + TABLE_MED_REM_SET_TIME_QUANTITY +" where  reminder_id = '"+Reminder_id+"'"; ;

        SQLiteDatabase  db = this.getReadableDatabase();


        return db.rawQuery(buildSQL, null);
    }

    public Integer get_total_quantity(String rem_id,String p_date1 )
    {
        Integer ringtone=1;
        String buildSQL = "select sum(quantity) as total FROM " + TABLE_MED_REM_SCHEDULE  + " where  reminder_id = '"+rem_id +"'  and " +
                " datetime(datetime_set) between datetime(\""
            + p_date1 + " 00:00:00\")" + "  AND datetime(\"" + p_date1 + " 23:59:59\")";
        SQLiteDatabase  db = this.getReadableDatabase();

        Cursor crs = db.rawQuery ( buildSQL, null );

        if ( crs.moveToFirst ( ) ) {
            do {
                ringtone = crs.getInt ( crs.getColumnIndex ( "total" ) );
            } while ( crs.moveToNext ( ) );
        }
        return ringtone;
    }
}

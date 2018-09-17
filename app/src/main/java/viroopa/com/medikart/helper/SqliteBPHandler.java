package viroopa.com.medikart.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by prakash on 29/09/15.
 */
public class SqliteBPHandler extends SQLiteOpenHelper {

    private static final String TAG = SqliteBPHandler.class.getSimpleName ( );

    private static SqliteBPHandler sSqlite_obj;

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "medikart";


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
    private static final String F_SYNCRELATIONSHIP_ID = "sync_Relationship_ID";
    private static final String F_SYNCIMEI_NO = "sync_IMEI_no";
    private static final String F_SYNCUUID = "sync_UUID";
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
    private static final String F_RELATIONSHIP_ID = "Relationship_ID";
    private static final String FIMEI_NO = "IMEI_no";
    private static final String F_UUID = "UUID";
    ///////////////////////////   BLOOD PRESSURE    ///////////////////////////

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


    private static final String TABLE_BPCONDITION = "bp_condition";
    // Login Table Columns names
    private static final String F_KEY_BPCONID = "bpconid";
    private static final String F_CONDITIONID = "condition_id";
    private static final String F_KEY_CONDITIONDESC = "conditiondesc";


    private static final String TABLE_BSCATEGORY = "bs_category";
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
    private static final String F_KEY_BPGOALSYSTOLIC = "bp_goalsystolic";
    private static final String F_KEY_BPGOALDIASTOLIC = "bp_goaldiastolic";
    private static final String F_KEY_BPGOALWEIGHTUNIT = "bp_weightunit";
    private static final String F_KEY_BPGOALKG = "bp_kg";
    private static final String F_KEY_BPGOALLB = "bp_lb";
    // private static final String F_KEY_BPGOALBPUNIT  = "bp_BPunit";


    ///////////////////////////   SETTING TABLE created by Akhil   ///////////////////////////


    private static final String TABLE_MEDIKART_SETTING = "main_setting";

    private static final String SETTING_ID = "id";
    private static final String SETTING_MEMBER_ID = "member_id";
    private static final String SETTING_RELATIONSHIP_ID = "relationship_id";
    private static final String SETTING_NAME = "name";
    private static final String SETTING_VALUE = "value";
    private static final String SETTING_MODULE_ID = "module_id";
    private static final String SETTING_MODULE_DESCRIPTION = "module_description";
    private static final String SETTING_CREATED_DATE = "created_date";


    ///////////////////////////   SETTING TABLE created by Akhil   ///////////////////////////

    public SqliteBPHandler ( Context context ) {
        super ( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    // Prakash K Bhandary
    public static synchronized SqliteBPHandler getInstance ( Context context ) {
        if ( sSqlite_obj == null ) {
            sSqlite_obj = new SqliteBPHandler ( context.getApplicationContext ( ) );
        }
        return sSqlite_obj;
    }

    // Creating Tables
    @Override
    public void onCreate ( SQLiteDatabase db ) {
    }

    // Upgrading database
    @Override
    public void onUpgrade ( SQLiteDatabase db, int oldVersion, int newVersion ) {
    }

    public void InsertBPGOALTable ( String member_id, Integer RelationId, String weight,
                                    String weightunit, String Systolic, String Diastolic,
                                    String KG, String LB, String BPUnit ) {

        SQLiteDatabase db = getWritableDatabase ( );

        db.beginTransaction ( );
        try {
            ContentValues values = new ContentValues ( );
            values.put ( F_BPGOALMEMBERID, member_id ); // Name
            values.put ( F_KEY_BPGOALRELATIONSHIP_ID, RelationId ); // Name
            values.put ( F_KEY_BPGOALWEIGHT, weight ); // Name
            values.put ( F_KEY_BPGOALSYSTOLIC, Systolic ); // Name
            values.put ( F_KEY_BPGOALDIASTOLIC, Diastolic ); // Name
            values.put ( F_KEY_BPGOALWEIGHTUNIT, weightunit ); // Name
            values.put ( F_KEY_BPGOALKG, KG ); // Name
            values.put ( F_KEY_BPGOALLB, LB ); // Name

            db.insertOrThrow ( TABLE_BPGOAL, null, values );


            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
            Log.d ( TAG, "Error InsertBPGOALTable" );
        } finally {
            db.endTransaction ( );
        }

    }

    public Cursor getgoalBPMonitarData ( Integer MemberId, Integer RelationshipId ) {
        // prakashget

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {
            String buildSQL = " SELECT   * FROM " + TABLE_BPGOAL + " Where bp_goalmember_id = " + MemberId + "  and bp_goalrelationship_ID=" + RelationshipId;

            Log.d ( TAG, "getAllData SQL: " + buildSQL );

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error getgoalBPMonitarData");

        }

        return obj_cursor ;

    }

    public void deleteGoalBPDetails ( String MemberId, Integer RelationshipId ) {


        SQLiteDatabase db = this.getWritableDatabase ( );
        db.beginTransaction ( );
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete ( TABLE_BPGOAL, "bp_goalmember_id=" + MemberId + " and bp_goalrelationship_ID=" + RelationshipId, null );

            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
            Log.d ( TAG, "Deleted deleteGoalBPDetails" );
        } finally {
            db.endTransaction ( );
        }

    }
    //Added For Bp Detail

    public void addBPDetails ( String member_id, String body_part,
                               String position, String systolic,
                               String diastolic, String pulse,
                               String weight, String weight_unit,
                               Integer arrthythmia, String comments, String bp_date,
                               String bp_time, Integer bptimehr, String kg, String lb,
                               Integer RelationshipId, Long IMEINo, String UUID ) {

        SQLiteDatabase db = getWritableDatabase ( );

        db.beginTransaction ( );
        try {
            ContentValues values = new ContentValues ( );
            values.put ( F_MEMBER_ID, member_id ); // Name
            values.put ( F_BODY_PART, body_part ); // Name
            values.put ( F_POSITION, position ); // Name
            values.put ( F_SYSTOLIC, systolic ); // Name
            values.put ( F_DIASTOLIC, diastolic ); // Name
            values.put ( F_PULSE, pulse ); // Email
            values.put ( F_WEIGHT, weight );
            values.put ( F_WEIGHT_UNIT, weight_unit );
            values.put ( F_ARRTHYTHMIA, arrthythmia );
            values.put ( F_COMMENTS, comments );
            values.put ( F_BP_DATE, bp_date );
            values.put ( F_BP_TIME, bp_time );
            values.put ( F_BPTIMEHR, bptimehr ); // memberid);
            values.put ( F_KG, kg );
            values.put ( F_LB, lb );
            values.put ( F_RELATIONSHIP_ID, RelationshipId );
            values.put ( FIMEI_NO, IMEINo );
            values.put ( F_UUID, UUID );

            // Inserting Row
            long id=db.insertOrThrow ( TABLE_BP, null, values );


            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
          //  Log.d ( TAG, "Error addBPDetails" );
        } finally {
            db.endTransaction ( );
        }

    }

    public void InsertSyncTable ( String I_Type, String Controller, String Parameter, String JsonObject,
                                  String Created_Date, String UUID, String iUpload_Download, String MemberId,
                                  String Module_Name, String Mode, String ControllerName, String METHODName, Long IMEI ) {



        SQLiteDatabase db = getWritableDatabase ( );

        db.beginTransaction ( );
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
            values.put ( F_KEY_IMEI, IMEI );

            db.insertOrThrow ( TABLE_Medikart_SYNC, null, values );

            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
            Log.d ( TAG, "Error while trying to add post to database" );
        } finally {
            db.endTransaction ( );
        }
    }

    public void deleteBPDetails ( ) {

        // Prakashdelete
        SQLiteDatabase db = this.getWritableDatabase ( );
        db.beginTransaction ( );
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete ( TABLE_BP, null, null );

            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
            Log.d ( TAG, "Deleted all user info from sqlite" );
        } finally {
            db.endTransaction ( );
        }


    }

    public Cursor getAnalysisMaxLineChartData ( Integer MemberId, Integer RelationshipId,
                                                String Filter, String AMPM ) {

        String sAMPM = "";
        String Day = Filter;
        String buildSQL = "";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }

        if ( Filter.equals ( "-8" ) ) {
            Day = "-8 days";

        }
        if ( Filter.equals ( "-11" ) ) {
            Day = "-11 days";
        }

        if ( Filter.equals ( "-30" ) ) {
            Day = "-30 days";
        }
        if ( Filter.equals ( "-365" ) ) {
            Day = "-365 days";
        }

        if ( AMPM.equals ( "AM/PM" ) ) {
            if ( Filter.equals ( "0" ) ) {
                //buildSQL = "select "+ F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdate,strftime('%m', bp_date) as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where   strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

                //buildSQL = "select "+ F_KEY_BPID + " AS _id, strftime('%d', bp_date) as bpdatepart,bp_date,bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where   strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";
                buildSQL = "select " + F_KEY_BPID + " AS _id, strftime('%d', bp_date) as bpdatepart,bp_date,bp_time,systolic as sys_avg, 0 as sys_min,diastolic as dia_avg,0 as dia_min,pulse as pulse_avg,weight as max_wt,0 as min_wt  FROM " + TABLE_BP + " where  member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;
            } else {
                //  buildSQL= "select "+ F_KEY_BPID + " AS _id, strftime('%d', bp_date) as bpdatepart,bp_date,bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where   strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now', '" + Day + "') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                buildSQL = "select " + F_KEY_BPID + " AS _id, strftime('%d', bp_date) as bpdatepart,bp_date,bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(Min(systolic),0) ) as sys_min,(ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(Min(diastolic),0)) as dia_min,(ifnull(Min(diastolic),0)) as dia_min,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt,(ifnull(Min(weight),0)) as min_wt  FROM " + TABLE_BP + " where  bp_date between datetime('now','" + Day + "') and datetime('now','localtime') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            }
        } else {
            if ( Filter.equals ( "0" ) ) {

                //  buildSQL = "select "+ F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdate, strftime('%m', bp_date) as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where  AMPM='" + sAMPM + "' and  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                //buildSQL = "select "+ F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";
                if ( AMPM.equals ( "PM" ) ) {
                    //   buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where substr(bp_time,1,2)>=12 and  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,systolic as sys_avg, 0 as sys_min,diastolic as dia_avg,0 as dia_min,pulse as pulse_avg,weight as max_wt,0 as min_wt  FROM " + TABLE_BP + " where  substr(bp_time,1,2)>=12 and    member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;
                } else {
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,systolic as sys_avg,0 as sys_min, diastolic as dia_avg,0 as dia_min,pulse as pulse_avg,weight as max_wt,0 as min_wt  FROM " + TABLE_BP + " where   substr(bp_time,1,2)<12 and  member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;
                }
            } else {

                if ( AMPM.equals ( "PM" ) ) {


                    //buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where substr(bp_time,1,2)>=12 and  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now', '" + Day + "') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg,(ifnull(Min(systolic),0) ) as sys_min, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(Min(diastolic),0)) as dia_min,(ifnull(Min(diastolic),0)) as dia_min,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt,(ifnull(Min(weight),0)) as min_wt  FROM " + TABLE_BP + " where substr(bp_time,1,2)>=12 and   bp_date between datetime('now','" + Day + "') and datetime('now','localtime') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                } else {
//                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where  substr(bp_time,1,2)<12 and   strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now', '" + Day + "') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg,(ifnull(Min(systolic),0) ) as sys_min, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(Min(diastolic),0)) as dia_min,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt,(ifnull(Min(weight),0)) as min_wt  FROM " + TABLE_BP + " where  substr(bp_time,1,2)<12 and     bp_date between datetime('now','" + Day + "') and datetime('now','localtime') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                }
            }

        }


        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {


            Log.d ( TAG, "getAllData SQL: " + buildSQL );

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error getAnalysisMaxLineChartData");

        }

        return obj_cursor ;

    }

    public Cursor getAnalysisLineChartData ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ) {

        String sAMPM = "";
        String Day = Filter;
        String buildSQL = "";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }

        if ( Filter.equals ( "-8" ) ) {
            Day = "-8 days";

        }
        if ( Filter.equals ( "-11" ) ) {
            Day = "-11 days";
        }
        if ( Filter.equals ( "-31" ) ) {
            Day = "-31 days";
        }
        if ( Filter.equals ( "-30" ) ) {
            Day = "-30 days";
        }
        if ( Filter.equals ( "-28" ) ) {
            Day = "-28 days";
        }
        if ( Filter.equals ( "-29" ) ) {
            Day = "-29 days";
        }
        if ( Filter.equals ( "-365" ) ) {
            Day = "-365 days";
        }

        if ( AMPM.equals ( "AM/PM" ) ) {
            if ( Filter.equals ( "0" ) ) {

                buildSQL = "select " + F_KEY_BPID + " AS _id, strftime('%d', bp_date) as bpdatepart,bp_date,bp_time,systolic as sys_avg, diastolic as dia_avg,pulse as pulse_avg,weight as max_wt  FROM " + TABLE_BP + " where   member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " order by BPID desc  limit 1";

            } else {
                //  buildSQL= "select "+ F_KEY_BPID + " AS _id, strftime('%d', bp_date) as bpdatepart,bp_date,bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where   strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now', '" + Day + "') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                buildSQL = "select " + F_KEY_BPID + " AS _id, strftime('%d', bp_date) as bpdatepart,bp_date,bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where  bp_date between datetime('now','" + Day + "') and datetime('now','localtime') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            }
        } else {
            if ( Filter.equals ( "0" ) ) {

                //  buildSQL = "select "+ F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdate, strftime('%m', bp_date) as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where  AMPM='" + sAMPM + "' and  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                //buildSQL = "select "+ F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";
                if ( AMPM.equals ( "PM" ) ) {
                    //   buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where substr(bp_time,1,2)>=12 and  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,systolic as sys_avg,diastolic as dia_avg,pulse as pulse_avg,weight as max_wt  FROM " + TABLE_BP + " where substr(bp_time,1,2)>=12  and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " order by BPID desc  limit 1";
                } else {
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,systolic as sys_avg, diastolic as dia_avg,pulse as pulse_avg,weight as max_wt  FROM " + TABLE_BP + " where  substr(bp_time,1,2)<12  and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " order by BPID desc  limit 1";
                }
            } else {

                if ( AMPM.equals ( "PM" ) ) {


                    //buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where substr(bp_time,1,2)>=12 and  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now', '" + Day + "') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where substr(bp_time,1,2)>=12 and   bp_date between datetime('now','" + Day + "') and datetime('now','localtime') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                } else {
//                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where  substr(bp_time,1,2)<12 and   strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now', '" + Day + "') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date, bp_time,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " where  substr(bp_time,1,2)<12 and     bp_date between datetime('now','" + Day + "') and datetime('now','localtime') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                }
            }

        }


        // buildSQL = "select \"+ F_KEY_BPID + \" AS _id, bp_date,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " WHERE  bp_date between  '" + Fromdate + "' and  '" + Todate + "'  and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";


        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {
            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error getAnalysisLineChartData");

        }

        return obj_cursor ;


    }



    public Cursor showAllData_Chart ( Integer MemberId, Integer RelationshipId, String Fromdate, String Todate ) {

        String buildSQL = "select " + F_KEY_BPID + " AS _id, Max(ifnull(systolic,0)) as sysMaxVal, Min(ifnull(systolic,0)) as sysMinVal,Max(ifnull(diastolic,0)) as diaMaxVal, Min(ifnull(diastolic,0)) as diaMinVal,strftime('%m', bp_date) as month, * from  " + TABLE_BP + " where  strftime('%Y %m %d', bp_date) Between strftime('%Y %m %d','" + Fromdate + "') and  strftime('%Y %m %d', '" + Todate + "') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error showAllData_Chart");

        }

        return obj_cursor ;


    }

    public Cursor showWeightLastReading ( Integer MemberId, Integer RelationshipId ) {

        String buildSQL = "select " + F_KEY_BPID + " AS _id, systolic as sysMaxVal, systolic as sysMinVal,diastolic as diaMaxVal, diastolic as diaMinVal,strftime('%m', bp_date) as month, * from  " + TABLE_BP + " where  member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error showAllData_Chart");

        }

        return obj_cursor ;


    }

    public Cursor showLastBPReadingChart ( Integer MemberId, Integer RelationshipId ) {

        String buildSQL = "select  * from  " + TABLE_BP + " where  member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + "  order by BPID desc LIMIT 1";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error showAllData_Chart");

        }

        return obj_cursor ;


    }

    public Cursor show_BPAnalysisFilterData_Chart ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String todate) {
        String sAMPM = "";
        String Day = "";
        String buildSQL = "";
        String last_entry_filter=" order by bp_date,BPID ";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }
        if ( Filter.equals ( "LastReading" ) ) {
            Day = "-730 days";
            last_entry_filter= " ORDER BY BPID DESC LIMIT 1";

        }
        if ( Filter.equals ( "Weekly" ) ) {
            last_entry_filter=" order by bp_date,BPID ";
            Day = "-7 days";

        }
        if ( Filter.equals ( "Last10" ) ) {
            Day = "-365 days";
            last_entry_filter=" ORDER BY bp_date, BPID DESC LIMIT 10";
        }

        if ( Filter.equals ( "Monthly" ) ) {
            last_entry_filter=" order by bp_date,BPID ";
            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {
            last_entry_filter=" order by bp_date,BPID ";
            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {
            if ( Filter.equals ( "Day" ) ) {

                buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date,CASE WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) = 12 THEN strftime('%H:%M',bp_time, 'localtime') || ' PM' WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) > 12  THEN strftime('%H:%M', bp_time, '-12 Hours', 'localtime') || ' PM'  ELSE strftime('%H:%M', bp_time, 'localtime') || ' AM'  END " +
                        "as AMPM,strftime('%d', bp_date) as bpdate,case strftime('%m', bp_date) when '01' then 'Jan' when '02' then 'Fe' when '03' then 'Mar' when '04' then 'Apr' when '05' then 'May' when '06' then 'Jun' when '07' then 'Jul' when '08' then 'Aug' when '09' then 'Sept' when '10' then 'Oct' when '11' then 'Nov' when '12' then 'Dec' else '' end    as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where   strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId +last_entry_filter;//+ " group by bp_date";
            }else if(Filter.equals ("Last10"))
            {
                buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date,CASE WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) = 12 THEN strftime('%H:%M',bp_time, 'localtime') || ' PM' WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) > 12  THEN strftime('%H:%M', bp_time, '-12 Hours', 'localtime') || ' PM'  ELSE strftime('%H:%M', bp_time, 'localtime') || ' AM'  END " +
                        "as AMPM,strftime('%d', bp_date) as bpdate,case strftime('%m', bp_date) when '01' then 'Jan' when '02' then 'Fe' when '03' then 'Mar' when '04' then 'Apr' when '05' then 'May' when '06' then 'Jun' when '07' then 'Jul' when '08' then 'Aug' when '09' then 'Sept' when '10' then 'Oct' when '11' then 'Nov' when '12' then 'Dec' else '' end    as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where   member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId +last_entry_filter;
            }else {

                buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date,CASE WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) = 12 THEN strftime('%H:%M',bp_time, 'localtime') || ' PM' WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) > 12  THEN strftime('%H:%M', bp_time, '-12 Hours', 'localtime') || ' PM'  ELSE strftime('%H:%M', bp_time, 'localtime') || ' AM'  END " +
                        "as AMPM,strftime('%d', bp_date) as bpdate,case strftime('%m', bp_date) when '01' then 'Jan' when '02' then 'Feb' when '03' then 'Mar' when '04' then 'Apr' when '05' then 'May' when '06' then 'Jun' when '07' then 'Jul' when '08' then 'Aug' when '09' then 'Sept' when '10' then 'Oct' when '11' then 'Nov' when '12' then 'Dec' else '' end    as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where     bp_date between datetime('"+todate+"','" + Day + "') and datetime('"+todate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId  +last_entry_filter;//+ " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            }
        } else {
            if ( Filter.equals ( "Day" ) ) {

                if ( AMPM.equals ( "PM" ) ) {

                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date,CASE WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) = 12 THEN strftime('%H:%M',bp_time, 'localtime') || ' PM' WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) > 12  THEN strftime('%H:%M', bp_time, '-12 Hours', 'localtime') || ' PM'  ELSE strftime('%H:%M', bp_time, 'localtime') || ' AM'  END " +
                            "as AMPM,strftime('%d', bp_date) as bpdate,case strftime('%m', bp_date) when '01' then 'Jan' when '02' then 'Fe' when '03' then 'Mar' when '04' then 'Apr' when '05' then 'May' when '06' then 'Jun' when '07' then 'Jul' when '08' then 'Aug' when '09' then 'Sept' when '10' then 'Oct' when '11' then 'Nov' when '12' then 'Dec' else '' end    as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where  substr(bp_time,1,2)>=12 and  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId +last_entry_filter;// + " group by bp_date";
                } else {
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date,CASE WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) = 12 THEN strftime('%H:%M',bp_time, 'localtime') || ' PM' WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) > 12  THEN strftime('%H:%M', bp_time, '-12 Hours', 'localtime') || ' PM'  ELSE strftime('%H:%M', bp_time, 'localtime') || ' AM'  END " +
                            "as AMPM,strftime('%d', bp_date) as bpdate,case strftime('%m', bp_date) when '01' then 'Jan' when '02' then 'Fe' when '03' then 'Mar' when '04' then 'Apr' when '05' then 'May' when '06' then 'Jun' when '07' then 'Jul' when '08' then 'Aug' when '09' then 'Sept' when '10' then 'Oct' when '11' then 'Nov' when '12' then 'Dec' else '' end    as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where  substr(bp_time,1,2)<12 and strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId +last_entry_filter;// + " group by bp_date";
                }
            }else if(Filter.equals ("Last10")){
                if ( AMPM.equals ( "PM" ) ) {

                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date,CASE WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) = 12 THEN strftime('%H:%M',bp_time, 'localtime') || ' PM' WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) > 12  THEN strftime('%H:%M', bp_time, '-12 Hours', 'localtime') || ' PM'  ELSE strftime('%H:%M', bp_time, 'localtime') || ' AM'  END " +
                            "as AMPM,strftime('%d', bp_date) as bpdate,case strftime('%m', bp_date) when '01' then 'Jan' when '02' then 'Fe' when '03' then 'Mar' when '04' then 'Apr' when '05' then 'May' when '06' then 'Jun' when '07' then 'Jul' when '08' then 'Aug' when '09' then 'Sept' when '10' then 'Oct' when '11' then 'Nov' when '12' then 'Dec' else '' end    as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where  substr(bp_time,1,2)>=12  and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId +last_entry_filter;
                } else {
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date,CASE WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) = 12 THEN strftime('%H:%M',bp_time, 'localtime') || ' PM' WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) > 12  THEN strftime('%H:%M', bp_time, '-12 Hours', 'localtime') || ' PM'  ELSE strftime('%H:%M', bp_time, 'localtime') || ' AM'  END " +
                            "as AMPM,strftime('%d', bp_date) as bpdate,case strftime('%m', bp_date) when '01' then 'Jan' when '02' then 'Fe' when '03' then 'Mar' when '04' then 'Apr' when '05' then 'May' when '06' then 'Jun' when '07' then 'Jul' when '08' then 'Aug' when '09' then 'Sept' when '10' then 'Oct' when '11' then 'Nov' when '12' then 'Dec' else '' end    as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where  substr(bp_time,1,2)<12  and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId +last_entry_filter;
                }
            } else {


                if ( AMPM.equals ( "PM" ) ) {
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date,CASE WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) = 12 THEN strftime('%H:%M',bp_time, 'localtime') || ' PM' WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) > 12  THEN strftime('%H:%M', bp_time, '-12 Hours', 'localtime') || ' PM'  ELSE strftime('%H:%M', bp_time, 'localtime') || ' AM'  END as AMPM,strftime('%d', bp_date) as bpdate,strftime('%d', bp_date) as bpdate,case strftime('%m', bp_date) when '01' then 'Jan' when '02' then 'Fe' when '03' then 'Mar' when '04' then 'Apr' when '05' then 'May' when '06' then 'Jun' when '07' then 'Jul' when '08' then 'Aug' when '09' then 'Sept' when '10' then 'Oct' when '11' then 'Nov' when '12' then 'Dec' else '' end    as month,datetime('now', 'end of day') as lastdate,* from  " + TABLE_BP +
                            " where  substr(bp_time,1,2)>=12 and    bp_date between datetime('now','" + Day + "') and datetime('"+todate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId  +last_entry_filter;//+ " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                } else {
                    buildSQL = "select " + F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdatepart,bp_date,CASE WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) = 12 THEN strftime('%H:%M',bp_time, 'localtime') || ' PM' WHEN CAST(strftime('%H',bp_time, 'localtime') AS INTEGER) > 12  THEN strftime('%H:%M', bp_time, '-12 Hours', 'localtime') || ' PM'  ELSE strftime('%H:%M', bp_time, 'localtime') || ' AM'  END " +
                            "as AMPM,strftime('%d', bp_date) as bpdate,strftime('%d', bp_date) as bpdate,case strftime('%m', bp_date) when '01' then 'Jan' when '02' then 'Fe' when '03' then 'Mar' when '04' then 'Apr' when '05' then 'May' when '06' then 'Jun' when '07' then 'Jul' when '08' then 'Aug' when '09' then 'Sept' when '10' then 'Oct' when '11' then 'Nov' when '12' then 'Dec' else '' end    as month,datetime('now', 'end of day') as lastdate ,* from  " + TABLE_BP +
                            " where   substr(bp_time,1,2)<12 and  bp_date between datetime('"+todate+"','" + Day + "') and datetime('"+todate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId +last_entry_filter; //+ " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                }
            }

        }


        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error show_BPAnalysisFilterData_Chart");

        }

        return obj_cursor ;


    }
    public Cursor show_BPMinMaxAnalysisFilterData_chart ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {
        String sAMPM = "";
        String Day = "";
        String buildSQL = "";
        String last_entry_filter=" order by bp_date,BPID ";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }
        if ( Filter.equals ( "LastReading" ) ) {
            Day = "-730 days";
            last_entry_filter="ORDER BY BPID DESC LIMIT 1";

        }
        if ( Filter.equals ( "Weekly" ) ) {
            last_entry_filter=" order by bp_date,BPID ";
            Day = "-7 days";

        }
        if ( Filter.equals ( "Last10" ) ) {
            Day = "-365 days";
            last_entry_filter="ORDER BY bp_date, BPID DESC LIMIT 10";
        }

        if ( Filter.equals ( "Monthly" ) ) {
            last_entry_filter=" order by bp_date,BPID ";
            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {
            last_entry_filter=" order by bp_date,BPID ";
            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {

            buildSQL = "select avg(systolic) as systolic,avg(diastolic) as diastolic,avg(weight) as weight,bp_date from  " + TABLE_BP + " where   bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" group by bp_date  "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        } else {

            if ( AMPM.equals ( "PM" ) ) {
                buildSQL = "select avg(systolic) as systolic,avg(diastolic) as diastolic,avg(weight) as weight,bp_date  from  " + TABLE_BP + " where    substr(bp_time,1,2)>=12 and bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" group by bp_date "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            } else {
                buildSQL = "select avg(systolic) as systolic,avg(diastolic) as diastolic,avg(weight) as weight,bp_date  from  " + TABLE_BP + " where    substr(bp_time,1,2)<12 and" +
                        " bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" group by bp_date "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            }

        }

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error show_BPAnalysisFilterData_Chart");

        }

        return obj_cursor ;


    }


    public Cursor show_BPMinMaxAnalysisFilterData_graph ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {
        String sAMPM = "";
        String Day = "";
        String buildSQL = "";
        String last_entry_filter=" order by bp_date,BPID ";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }
        if ( Filter.equals ( "LastReading" ) ) {
            Day = "-730 days";
            last_entry_filter="ORDER BY BPID DESC LIMIT 1";

        }
        if ( Filter.equals ( "Weekly" ) ) {
            last_entry_filter=" order by bp_date,BPID ";
            Day = "-7 days";

        }
        if ( Filter.equals ( "Last10" ) ) {
            Day = "-365 days";
            last_entry_filter="ORDER BY bp_date, BPID DESC LIMIT 10";
        }

        if ( Filter.equals ( "Monthly" ) ) {
            last_entry_filter=" order by bp_date,BPID ";
            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {
            last_entry_filter=" order by bp_date,BPID ";
            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {

            buildSQL = "select * from  " + TABLE_BP + " where   bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        } else {

            if ( AMPM.equals ( "PM" ) ) {
                buildSQL = "select * from  " + TABLE_BP + " where    substr(bp_time,1,2)>=12 and bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            } else {
                buildSQL = "select * from  " + TABLE_BP + " where    substr(bp_time,1,2)<12 and" +
                        " bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            }

        }

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error show_BPAnalysisFilterData_Chart");

        }

        return obj_cursor ;


    }

    public Cursor show_analysisi_for_pdf_graph ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {
        String sAMPM = "";
        String Day = "";
        String buildSQL = "";
        String  last_entry_filter="";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }
        if ( Filter.equals ( "LastReading" ) ) {
            Day = "-365 days";
            last_entry_filter="ORDER BY rowid DESC LIMIT 1 ";


        }
        if ( Filter.equals ( "Weekly" ) ) {

            Day = "-7 days";

        }
        if ( Filter.equals ( "Last10" ) ) {
            Day = "-365 days";

            last_entry_filter="ORDER BY rowid DESC LIMIT 10 ";
        }

        if ( Filter.equals ( "Monthly" ) ) {

            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {

            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {


            buildSQL="select sum(toal_count)as total_count, sum(Low_bp)as Low_bp, sum(Normal_bp)as Normal_bp,sum(Prehypertension)as Prehypertension,sum(Hypertension_stage1) as Hypertension_stage1 ,sum(Hypertension_stage2) as Hypertension_stage2,sum(hypertensive_crisis) as hypertensive_crisis from(select count (*) as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,member_id,Relationship_ID from   "
                    + TABLE_BP +" "+last_entry_filter+ ") where bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                    RelationshipId+")"+

                    "union select 0 as toal_count, count (*) as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,member_id,Relationship_ID from   "
                    + TABLE_BP +" "+last_entry_filter+ ") where systolic < 90 or diastolic < 60  and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                    RelationshipId+")"+

                    "union select 0 as toal_count, 0 as Low_bp, count (*) as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,member_id,Relationship_ID from   "
                    + TABLE_BP +" "+last_entry_filter+ ") where (systolic >= 90 and systolic <= 120) or (diastolic >= 60 and diastolic < 80) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                    RelationshipId+")"+

                    "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, count (*) as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,member_id,Relationship_ID from   "
                    + TABLE_BP +" "+last_entry_filter+ ") where (systolic >= 121 and systolic <= 139) or (diastolic >= 81 and diastolic <= 89) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                    RelationshipId+")"+

                    "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,count (*) as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,member_id,Relationship_ID from   "
                    + TABLE_BP +" "+last_entry_filter+ ") where (systolic >= 140 and systolic <= 159) or (diastolic >= 90 and diastolic <= 99) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                    RelationshipId+")"+

                    "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,count (*) as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,member_id,Relationship_ID from   "
                    + TABLE_BP +" "+last_entry_filter+ ") where (systolic >= 160 and systolic <= 179) or (diastolic >= 100 and diastolic <= 109) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                    RelationshipId+")"+

                    "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,count (*) as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,member_id,Relationship_ID from   "
                    + TABLE_BP +" "+last_entry_filter+ ") where systolic >= 180 or diastolic >= 110 and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                    RelationshipId+")"+
                    " )A";


        } else {

            if ( AMPM.equals ( "PM" ) ) {





                buildSQL="select sum(toal_count)as total_count, sum(Low_bp)as Low_bp, sum(Normal_bp)as Normal_bp,sum(Prehypertension)as Prehypertension,sum(Hypertension_stage1) as Hypertension_stage1 ,sum(Hypertension_stage2) as Hypertension_stage2,sum(hypertensive_crisis) as hypertensive_crisis from(select count (*) as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where substr(bp_time,1,2)>=12 and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, count (*) as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where substr(bp_time,1,2)>=12 and systolic < 90 or diastolic < 60  and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Low_bp, count (*) as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where substr(bp_time,1,2)>=12 and (systolic >= 90 and systolic <= 120) or (diastolic >= 60 and diastolic < 80) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, count (*) as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where substr(bp_time,1,2)>=12 and (systolic >= 120 and systolic <= 139) or (diastolic >= 80 and diastolic <= 89) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,count (*) as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where substr(bp_time,1,2)>=12 and (systolic >= 140 and systolic <= 159) or (diastolic >= 90 and diastolic <= 99) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,count (*) as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where substr(bp_time,1,2)>=12 and (systolic >= 160 and systolic <= 179) or (diastolic >= 100 and diastolic <= 109) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,count (*) as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where substr(bp_time,1,2)>=12 and systolic >= 180 or diastolic >= 110 and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+
                        " )A";







                // buildSQL = "select (ifnull(MAX(weight_number),0)) as max_weight_kg,(ifnull(Min(weight_number),0)) as min_weight_kg,(ifnull(avg(weight_number),0)) as avg_weight_kg,(ifnull(MAX(lbweight_number),0)) as max_weight_lb,(ifnull(Min(lbweight_number),0)) as min_weight_lb,(ifnull(avg(lbweight_number),0)) as avg_weight_lb,(ifnull(MAX(g_value),0)) as max_g_value,(ifnull(Min(g_value),0)) as min_g_value,(ifnull(avg(g_value),0)) as avg_g_value ,(ifnull(MAX(g_mmolval),0)) as max_g_mmolval,(ifnull(Min(g_mmolval),0)) as min_g_mmolval,(ifnull(avg(g_mmolval),0)) as avg_g_mmolval from  " + TABLE_DM + " where    substr(g_time,1,2)>=12 and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            } else {


                buildSQL="select sum(toal_count)as total_count, sum(Low_bp)as Low_bp, sum(Normal_bp)as Normal_bp,sum(Prehypertension)as Prehypertension,sum(Hypertension_stage1) as Hypertension_stage1 ,sum(Hypertension_stage2) as Hypertension_stage2,sum(hypertensive_crisis) as hypertensive_crisis from(select count (*) as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where  substr(bp_time,1,2)<12 and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, count (*) as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where substr(bp_time,1,2)<12 and systolic < 90 or diastolic < 60  and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Low_bp, count (*) as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where  substr(bp_time,1,2)<12 and (systolic >= 90 and systolic <= 120) or (diastolic >= 60 and diastolic < 80) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, count (*) as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where substr(bp_time,1,2)<12 and (systolic >= 120 and systolic <= 139) or (diastolic >= 80 and diastolic <= 89) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,count (*) as Hypertension_stage1,0 as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where  substr(bp_time,1,2)<12 and (systolic >= 140 and systolic <= 159) or (diastolic >= 90 and diastolic <= 99) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,count (*) as Hypertension_stage2,0 as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where  substr(bp_time,1,2)<12 and (systolic >= 160 and systolic <= 179) or (diastolic >= 100 and diastolic <= 109) and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Low_bp, 0 as Normal_bp, 0 as Prehypertension,0 as Hypertension_stage1,0 as Hypertension_stage2,count (*) as hypertensive_crisis from (Select * from(Select systolic,diastolic,bp_date,bp_time,member_id,Relationship_ID from   "
                        + TABLE_BP +" "+last_entry_filter+ ") where  substr(bp_time,1,2)<12 and systolic >= 180 or diastolic >= 110 and bp_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and Relationship_ID = " +
                        RelationshipId+")"+
                        " )A";




               /* buildSQL = "select (ifnull(MAX(weight_number),0)) as max_weight_kg,(ifnull(Min(weight_number),0)) as min_weight_kg,(ifnull(avg(weight_number),0)) as avg_weight_kg,(ifnull(MAX(lbweight_number),0)) as max_weight_lb,(ifnull(Min(lbweight_number),0)) as min_weight_lb,(ifnull(avg(lbweight_number),0)) as avg_weight_lb,(ifnull(MAX(g_value),0)) as max_g_value,(ifnull(Min(g_value),0)) as min_g_value,(ifnull(avg(g_value),0)) as avg_g_value ,(ifnull(MAX(g_mmolval),0)) as max_g_mmolval,(ifnull(Min(g_mmolval),0)) as min_g_mmolval,(ifnull(avg(g_mmolval),0)) as avg_g_mmolval from " + TABLE_DM + " where    substr(g_time,1,2)<12 and" +
                        " g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;*///and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            }

        }

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error show_BPAnalysisFilterData_Chart");

        }

        return obj_cursor ;


    }

    public Cursor show_BPMinMaxAnalysisFilterData_Chartnew ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {
        String sAMPM = "";
        String Day = "";
        String buildSQL = "";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }
        if ( Filter.equals ( "Weekly" ) ) {
            Day = "-7 days";

        }
        if ( Filter.equals("Last10" ) ) {
            Day = "-11 days";
        }

        if ( Filter.equals ( "Monthly" ) ) {
            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {
            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {
            if ( Filter.equals ( "Day" ) ) {
                //buildSQL = "select "+ F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdate,strftime('%m', bp_date) as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where   strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

                buildSQL = "select 0  AS _id,ifnull(MAX(systolic),0) as sys_max, ifnull(MIN(systolic),0) as sys_min,(ifnull(max(systolic),0)+ifnull(MIN(systolic),0))/2  as avg_sys,  ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min , (ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse ,(ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse,  ifnull(MAX(lb),0) as max_wtlb ,   ifnull(MIN(lb),0) as min_wtlb,(ifnull(max(lb),0)+ifnull(MIN(lb),0))/2  as avg_wtlb, ifnull(MAX(weight),0) as max_wt ,ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt,weight_unit from  " + TABLE_BP + " where   strftime('%Y %m %d', bp_date) =strftime('%Y %m %d','now') and member_id =" +     MemberId + "  and Relationship_ID=" + RelationshipId;
            }else if(Filter.equals ("Last10"))
            {
                buildSQL = "select 0  AS _id,ifnull(MAX(systolic),0) as sys_max, ifnull(MIN(systolic),0) as sys_min,(ifnull(max(systolic),0)+ifnull(MIN(systolic),0))/2  as avg_sys,  ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min , (ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse ,(ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse,  ifnull(MAX(lb),0) as max_wtlb ,   ifnull(MIN(lb),0) as min_wtlb,(ifnull(max(lb),0)+ifnull(MIN(lb),0))/2  as avg_wtlb,ifnull(MAX(weight),0) as max_wt ,ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt,weight_unit from  " + TABLE_BP + " where    member_id =" +     MemberId + "  and Relationship_ID=" + RelationshipId+" order by BPID DESC Limit 10";
            }
            else {
                buildSQL = "select 0 AS _id,ifnull(MAX(systolic),0) as sys_max, ifnull(MIN(systolic),0) as sys_min,(ifnull(max(systolic),0)+ifnull(MIN(systolic),0))/2  as avg_sys,  ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min , (ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse ,(ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse,ifnull(MAX(lb),0) as max_wtlb ,   ifnull(MIN(lb),0) as min_wtlb,(ifnull(max(lb),0)+ifnull(MIN(lb),0))/2  as avg_wtlb,  ifnull(MAX(weight),0) as max_wt ,  ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt,weight_unit from  " + TABLE_BP + " where   bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            }
        } else {
            if ( Filter.equals ( "Day" ) ) {

                if ( AMPM.equals ( "PM" ) ) {
                    //  buildSQL = "select "+ F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdate, strftime('%m', bp_date) as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where  AMPM='" + sAMPM + "' and  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                    buildSQL = "select 0 AS _id,ifnull(MAX(systolic),0) as sys_max, ifnull(MIN(systolic),0) as sys_min,(ifnull(max(systolic),0)+ifnull(MIN(systolic),0))/2  as avg_sys,  ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min , (ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse ,(ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse, ifnull(MAX(lb),0) as max_wtlb ,   ifnull(MIN(lb),0) as min_wtlb,(ifnull(max(lb),0)+ifnull(MIN(lb),0))/2  as avg_wtlb, ifnull(MAX(weight),0) as max_wt ,  ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt,weight_unit from  " + TABLE_BP + " where substr(bp_time,1,2)>=12 and strftime('%Y %m %d', bp_date) =strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;
                } else {
                    buildSQL = "select 0 AS _id,ifnull(MAX(systolic),0) as sys_max, ifnull(MIN(systolic),0) as sys_min,(ifnull(max(systolic),0)+ifnull(MIN(systolic),0))/2  as avg_sys,  ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min , (ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse ,(ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse, ifnull(MAX(lb),0) as max_wtlb ,   ifnull(MIN(lb),0) as min_wtlb,(ifnull(max(lb),0)+ifnull(MIN(lb),0))/2  as avg_wtlb, ifnull(MAX(weight),0) as max_wt ,  ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt,weight_unit from  " + TABLE_BP + " where  substr(bp_time,1,2)<12 and strftime('%Y %m %d', bp_date) =strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;
                }
            }else if(Filter.equals ("Last10"))
            {
                if ( AMPM.equals ( "PM" ) ) {
                    //  buildSQL = "select "+ F_KEY_BPID + " AS _id,strftime('%d', bp_date) as bpdate, strftime('%m', bp_date) as month,datetime('now', 'end of day') as lastdate , * from  " + TABLE_BP + " where  AMPM='" + sAMPM + "' and  strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                    buildSQL = "select 0 AS _id,ifnull(MAX(systolic),0) as sys_max, ifnull(MIN(systolic),0) as sys_min,(ifnull(max(systolic),0)+ifnull(MIN(systolic),0))/2  as avg_sys,  ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min , (ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse ,(ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse, ifnull(MAX(lb),0) as max_wtlb ,   ifnull(MIN(lb),0) as min_wtlb,(ifnull(max(lb),0)+ifnull(MIN(lb),0))/2  as avg_wtlb, ifnull(MAX(weight),0) as max_wt ,  ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt,weight_unit from  " + TABLE_BP + " where substr(bp_time,1,2)>=12 and  member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" order by BPID DESC Limit 10";
                } else {
                    buildSQL = "select 0 AS _id,ifnull(MAX(systolic),0) as sys_max, ifnull(MIN(systolic),0) as sys_min,(ifnull(max(systolic),0)+ifnull(MIN(systolic),0))/2  as avg_sys,  ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min , (ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse ,(ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse, ifnull(MAX(lb),0) as max_wtlb ,   ifnull(MIN(lb),0) as min_wtlb,(ifnull(max(lb),0)+ifnull(MIN(lb),0))/2  as avg_wtlb, ifnull(MAX(weight),0) as max_wt ,  ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt,weight_unit from  " + TABLE_BP + " where  substr(bp_time,1,2)<12  and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" order by BPID DESC Limit 10";
                }
            }
            else {


                if ( AMPM.equals ( "PM" ) ) {
                    // buildSQL = "select 0 AS _id,ifnull(MAX(systolic),0) as sys_max, ifnull(MIN(systolic),0) as sys_min,(ifnull(max(systolic),0)+ifnull(MIN(systolic),0))/2  as avg_sys,  ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min , (ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse ,(ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse, ifnull(MAX(lb),0) as max_wtlb ,   ifnull(MIN(lb),0) as min_wtlb,(ifnull(max(lb),0)+ifnull(MIN(lb),0))/2  as avg_wtlb, ifnull(MAX(weight),0) as max_wt ,  ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt, weight_unit from  " + TABLE_BP + " where   strftime('%Y %m %d', bp_date) >=strftime('%Y %m %d','now', '" + Day + "') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId ;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                    buildSQL = "select 0 AS _id,ifnull(MAX(systolic),0) as sys_max, ifnull(MIN(systolic),0) as sys_min,(ifnull(max(systolic),0)+ifnull(MIN(systolic),0))/2  as avg_sys,  ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min , (ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse ,(ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse, ifnull(MAX(lb),0) as max_wtlb ,   ifnull(MIN(lb),0) as min_wtlb,(ifnull(max(lb),0)+ifnull(MIN(lb),0))/2  as avg_wtlb, ifnull(MAX(weight),0) as max_wt ,  ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt, weight_unit from  " + TABLE_BP + " where    substr(bp_time,1,2)>=12 and bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                } else {
                    buildSQL = "select 0 AS _id,ifnull(MAX(systolic),0) as sys_max, ifnull(MIN(systolic),0) as sys_min,(ifnull(max(systolic),0)+ifnull(MIN(systolic),0))/2  as avg_sys,  ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min , (ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse ,(ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse, ifnull(MAX(lb),0) as max_wtlb ,   ifnull(MIN(lb),0) as min_wtlb,(ifnull(max(lb),0)+ifnull(MIN(lb),0))/2  as avg_wtlb, ifnull(MAX(weight),0) as max_wt ,  ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt, weight_unit from  " + TABLE_BP + " where    substr(bp_time,1,2)<12 and" +
                            " bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
                }
            }

        }

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error show_BPAnalysisFilterData_Chart");

        }

        return obj_cursor ;


    }

    public void deleteSyncBPDetails ( String SYNC_BPID, String MemberId, String RelationshipId ) {


        SQLiteDatabase db = this.getWritableDatabase ( );
        db.beginTransaction ( );
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete ( TABLE_BP_SYNC, "SYNC_BPID=" + SYNC_BPID + " AND sync_member_id=" + MemberId, null );

            db.setTransactionSuccessful ( );
        } catch ( Exception e ) {
            Log.d ( TAG, "Deleted all user info from sqlite" );
        } finally {
            db.endTransaction ( );
        }

    }


    public Cursor getLastReadingBPMonitarData ( String MemberId, Integer RelationshipId ) {

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {
            String buildSQL = "select BPID AS _id, strftime('%d', bp_date) as bpdatepart,bp_date,bp_time,systolic  as sys_avg, systolic  as sys_min,diastolic as dia_avg,diastolic as dia_min,pulse as pulse_avg,weight as max_wt,weight as min_wt,pulse,weight_unit,kg,lb,body_part,position,BPID,weight,systolic,diastolic,arrthythmia  FROM " + TABLE_BP + " where     member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId +" order by BPID desc LIMIT 1";

            Log.d ( TAG, "getAllData SQL: " + buildSQL );

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error getLastReadingBPMonitarData");

        }

        return obj_cursor ;

    }

    public Cursor getAllBPMonitorData ( Integer MemberId, Integer RelationshipId ) {

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {
            String buildSQL = " SELECT " + F_KEY_SYNCBPID + " AS _id,SYNC_BPID  , sync_member_id as Member_Id,sync_body_part as Body_Part,sync_position as Position ,sync_systolic as Systolic ,sync_diastolic as Diastolic,sync_pulse as Pulse,sync_weight  as Weight ,sync_weight_unit as Weight_Unit ,sync_arrthythmia as Arrthythmia ,sync_comments as Comments ,sync_bp_date as  Bp_Date ,sync_bp_time as Bp_Time,sync_bptimehr as BpTimehr,sync_kg as kg,sync_lb as lb,sync_Relationship_ID as RelationshipId,sync_IMEI_no as IMEI_No " + " FROM " + TABLE_BP_SYNC + " Where sync_member_id =" + MemberId + " and " + F_SYNCRELATIONSHIP_ID + " = " + RelationshipId;

            Log.d ( TAG, "getAllData SQL: " + buildSQL );

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error getAllBPMonitorData");

        }

        return obj_cursor ;

    }


    public Cursor getAllData_Chart ( Integer MemberId, Integer RelationshipId ) {


        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {
            String buildSQL = "select \"+ F_KEY_BPID + \" AS _id, bp_date,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP; //+" WHERE   bp_date>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', bp_date)<= strftime('%Y %m %d','now') and member_id ="+MemberId +"  and Relationship_ID="+ RelationshipId +" group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

            Log.d ( TAG, "getAllData SQL: " + buildSQL );

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error getAllData_Chart");

        }

        return obj_cursor ;

    }

    public Cursor getChartData ( Integer MemberId, Integer RelationshipId,
                                 String Fromdate, String Todate ) {

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {
            String buildSQL = "select \"+ F_KEY_BPID + \" AS _id, bp_date,(ifnull(MAX(systolic),0) ) as sys_avg, (ifnull(MAX(diastolic),0)) as dia_avg,(ifnull(MAX(pulse),0)) as pulse_avg,(ifnull(MAX(weight),0)) as max_wt  FROM " + TABLE_BP + " WHERE  bp_date between  '" + Fromdate + "' and  '" + Todate + "'  and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId + " group by bp_date";//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

            Log.d ( TAG, "getAllData SQL: " + buildSQL );

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error getChartData");

        }

        return obj_cursor ;


    }





    public Cursor getBPMonitorCount ( Integer MemberId, Integer RelationshipId ) {

        // prakashget

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {
            String buildSQL = "select * FROM " + TABLE_BP + " WHERE member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId;// and  bp_date<=date('now','-30 day') and bp_date>=date('now')";

            Log.d ( TAG, "getAllData SQL: " + buildSQL );

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error getBPMonitorCount");

        }

        return obj_cursor ;


    }


    public void deleteBPDataById ( Integer bpid ) {


        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete ( TABLE_BP, F_KEY_BPID + " = " + bpid, null );

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error deleteBPDataById");
        } finally {
            db.endTransaction();
        }

    }

    //createdby akhil
    public Cursor getAllBPMonitarDataOnBpID ( Integer Bpid ) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor obj_cursor = null ;
        try
        {
            String buildSQL = " SELECT   * FROM " + TABLE_BP + " Where BPID = " + Bpid;

            obj_cursor = db.rawQuery(buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error getAllBPMonitarDataOnBpID");

        }

        return obj_cursor ;
    }

    //Added For DM Detail
    public void EditSaveBPDetails ( String member_id, String body_part, String position,
                                    String systolic,
                                    String diastolic, String pulse, String weight, String weight_unit,
                                    Integer arrthythmia, String comments, String bp_date,
                                    String bp_time, Integer bptimehr, String kg,
                                    String lb, Integer RelationshipId, Long IMEINo, Integer Bpid, String UUID ) {


        SQLiteDatabase db = this.getWritableDatabase ( );
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues ( );
            values.put ( F_MEMBER_ID, member_id );
            values.put ( F_BODY_PART, body_part );
            values.put ( F_POSITION, position );
            values.put ( F_SYSTOLIC, systolic );
            values.put ( F_DIASTOLIC, diastolic );
            values.put ( F_PULSE, pulse );
            values.put ( F_WEIGHT, weight );
            values.put ( F_WEIGHT_UNIT, weight_unit );
            values.put ( F_ARRTHYTHMIA, arrthythmia );
            values.put ( F_COMMENTS, comments );
            values.put ( F_BP_DATE, bp_date );
            values.put ( F_BP_TIME, bp_time );
            values.put ( F_BPTIMEHR, bptimehr );
            values.put ( F_KG, kg );
            values.put ( F_LB, lb );
            values.put ( F_RELATIONSHIP_ID, RelationshipId );
            values.put ( FIMEI_NO, IMEINo );
            values.put ( F_UUID, UUID );

            long id = db.update ( TABLE_BP, values, "BPID = " + Bpid, null );
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error EditSaveBPDetails");
        }
        finally {
            db.endTransaction();
        }

    }

    public void InsertSettingData ( String member_id, String relation_ship_id, String setting_name,
                                    String setting_value,
                                    String module_id, String module_description, String created_date ) {

        SQLiteDatabase db = this.getWritableDatabase ( );

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues ( );
            values.put ( SETTING_MEMBER_ID, member_id ); // Name
            values.put ( SETTING_RELATIONSHIP_ID, relation_ship_id ); // Name
            values.put ( SETTING_NAME, setting_name ); // Name
            values.put ( SETTING_VALUE, setting_value ); // Name
            values.put ( SETTING_MODULE_ID, module_id ); // Email
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

    public void InsertBPCondition ( Integer ConitionId, String Condition_description ) {

        SQLiteDatabase db = this.getWritableDatabase ( );

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues ( );
            values.put ( F_CONDITIONID, ConitionId ); // Name
            values.put ( F_KEY_CONDITIONDESC, Condition_description ); // Name


            db.insertOrThrow ( TABLE_BPCONDITION, null, values );
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error InsertBPCondition");
        } finally {
            db.endTransaction();
        }

    }



    public void deleteCondition ( ) {


        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_BPCONDITION, null, null);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d ( TAG, "Error deleteCondition " );
        } finally {
            db.endTransaction();
        }
    }

    public void updateSettingData ( String member_id, String relation_ship_id, String setting_name,
                                    String setting_value,
                                    String module_id, String created_date ) {

        SQLiteDatabase db = this.getWritableDatabase ( );
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues ( );
            values.put ( SETTING_VALUE, setting_value ); // Name
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

    public Cursor getAllSetting_datat ( String RelationShipid, String member_id, String module_id ) {


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


    public Cursor getAllCondition_datat ( ) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor obj_cursor = null ;

        try {
            String buildSQL = "SELECT   * FROM " + TABLE_BPCONDITION;/*, new String[]{String.valueOf(toDate), String.valueOf(RelationShipid)}*/
            obj_cursor = db.rawQuery ( buildSQL, null );

        } catch (Exception e) {

            Log.d(TAG, "Error getAllCondition_datat");
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

    public void deleteBPData ( ) {


        SQLiteDatabase db = getWritableDatabase();

        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete ( TABLE_BP,null, null );

        } catch (Exception e) {
            Log.d(TAG, "Error deleteBPData");
        } finally {

        }

    }

    public Cursor show_BPMinMaxAnalysisFilterData_Static ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {
        String sAMPM = "";
        String Day = "";
        String buildSQL = "";
        String last_entry_filter="";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }
        if ( Filter.equals ( "LastReading" ) ) {
            Day = "-365 days";
            last_entry_filter="ORDER BY BPID DESC LIMIT 1";

        }
        if ( Filter.equals ( "Weekly" ) ) {
            Day = "-7 days";

        }
        if ( Filter.equals ( "Last10" ) ) {
            Day = "-365 days";
            last_entry_filter="ORDER BY BPID DESC LIMIT 10";
        }

        if ( Filter.equals ( "Monthly" ) ) {
            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {
            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {

            buildSQL = "select (ifnull(MAX(kg),0)) as max_weight_kg,(ifnull(Min(kg),0)) as min_weight_kg,(ifnull(avg(kg),0)) as avg_weight_kg,(ifnull(MAX(lb),0)) as max_weight_lb,(ifnull(Min(lb),0)) as min_weight_lb,(ifnull(avg(lb),0)) as avg_weight_lb, (ifnull(MAX(systolic),0)) as max_systolic,(ifnull(Min(systolic),0)) as min_systolic,(ifnull(avg(systolic),0)) as avg_systolic,(ifnull(MAX(diastolic),0)) as max_diastolic,(ifnull(Min(lb),0)) as min_diastolic,(ifnull(avg(diastolic),0)) as avg_diastolic from  " + TABLE_BP + " where   bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        } else {

            if ( AMPM.equals ( "PM" ) ) {
                buildSQL = "select (ifnull(MAX(kg),0)) as max_weight_kg,(ifnull(Min(kg),0)) as min_weight_kg,(ifnull(avg(kg),0)) as avg_weight_kg,(ifnull(MAX(lb),0)) as max_weight_lb,(ifnull(Min(lb),0)) as min_weight_lb,(ifnull(avg(lb),0)) as avg_weight_lb, (ifnull(MAX(systolic),0)) as max_systolic,(ifnull(Min(systolic),0)) as min_systolic,(ifnull(avg(systolic),0)) as avg_systolic,(ifnull(MAX(diastolic),0)) as max_diastolic,(ifnull(Min(lb),0)) as min_diastolic,(ifnull(avg(diastolic),0)) as avg_diastolic from  " + TABLE_BP + " where    substr(bp_time,1,2)>=12 and bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            } else {
                buildSQL = "select (ifnull(MAX(kg),0)) as max_weight_kg,(ifnull(Min(kg),0)) as min_weight_kg,(ifnull(avg(kg),0)) as avg_weight_kg,(ifnull(MAX(lb),0)) as max_weight_lb,(ifnull(Min(lb),0)) as min_weight_lb,(ifnull(avg(lb),0)) as avg_weight_lb, (ifnull(MAX(systolic),0)) as max_systolic,(ifnull(Min(systolic),0)) as min_systolic,(ifnull(avg(systolic),0)) as avg_systolic,(ifnull(MAX(diastolic),0)) as max_diastolic,(ifnull(Min(lb),0)) as min_diastolic,(ifnull(avg(diastolic),0)) as avg_diastolic from  " + TABLE_BP + " where    substr(bp_time,1,2)<12 and" +
                        " bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            }

        }

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error show_BPAnalysisFilterData_Chart");

        }

        return obj_cursor ;


    }

    public Cursor show_weight_analysis_data ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {
        String sAMPM = "";
        String Day = "";
        String buildSQL = "";
        String last_entry_filter="";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }
        if ( Filter.equals ( "LastReading" ) ) {
            Day = "-365 days";
            last_entry_filter="ORDER BY BPID DESC LIMIT 1";

        }
        if ( Filter.equals ( "Weekly" ) ) {
            Day = "-7 days";

        }
        if ( Filter.equals ( "Last10" ) ) {
            Day = "-365 days";
            last_entry_filter="ORDER BY BPID DESC LIMIT 10";
        }

        if ( Filter.equals ( "Monthly" ) ) {
            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {
            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {

            buildSQL = "select sum(over_weight) as over_weight,sum(normal_weight) as normal_weight from (select count(*) as over_weight, 0 as normal_weight from (select (((dm.kg-gdm.bp_kg)/gdm.bp_kg)*100) as percent  from  " + TABLE_BP +" dm,"+TABLE_BPGOAL+" gdm "+  " where dm.member_id= gdm.bp_goalmember_id and dm.Relationship_ID=gdm.bp_goalrelationship_ID and " +
                    "bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter+") a where percent>15   union "

                    +"select 0 as over_weight, count(*) as normal_weight from (select (((dm.kg-gdm.bp_kg)/gdm.bp_kg)*100)  as percent  from  " + TABLE_BP +" dm,"+TABLE_BPGOAL+" gdm "+  " where dm.member_id= gdm.bp_goalmember_id and dm.Relationship_ID=gdm.bp_goalrelationship_ID and " +
                    "bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter+") a where percent<15) b "


            ;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        } else {

            if ( AMPM.equals ( "PM" ) ) {

                buildSQL = "select sum(over_weight) as over_weight,sum(normal_weight) as normal_weightfrom (select count(*) as over_weight, 0 as normal_weight from (select (((dm.kg-gdm.bp_kg)/gdm.bp_kg)*100) as percent  from  " + TABLE_BP +" dm,"+TABLE_BPGOAL+"  gdm "+  " where dm.member_id= gdm.bp_goalmember_id and dm.Relationship_ID=gdm.bp_goalrelationship_ID and " +
                        "substr(bp_time,1,2)>=12 and bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter+") a where percent>15   union "

                        +"select 0 as over_weight, count(*) as normal_weight from (select (((dm.weight_number-gdm.dm_kg)/gdm.dm_kg)*100) as percent  from  " + TABLE_BP +" dm,"+TABLE_BPGOAL+" gdm "+  " where " +
                        " substr(bp_time,1,2)>=12 and bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter+") a where percent<15) b ";


            /*    buildSQL = "select * from  " + TABLE_DM + " where  " +
                        "  substr(g_time,1,2)>=12 and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"')" +
                        " and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;*///and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            } else {
              /*  buildSQL = "select * from  " + TABLE_DM + " where    substr(g_time,1,2)<12 and" +
                        " g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and" +
                        " member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;*///and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";


                buildSQL = "select sum(over_weight) as over_weight,sum(normal_weight) as normal_weight from (select count(*) as over_weight, 0 as normal_weight from (select (((dm.kg-gdm.bp_kg)/gdm.bp_kg)*100)) as percent  from  " + TABLE_BP +" dm,"+TABLE_BPGOAL+" gdm "+  " where dm.member_id= gdm.bp_goalmember_id and dm.Relationship_ID=gdm.bp_goalrelationship_ID and " +
                        "  bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter+") a where percent>15   union "

                        +"select 0 as over_weight, count(*) as normal_weight from (select (((dm.kg-gdm.bp_kg)/gdm.bp_kg)*100)) as percent  from  " + TABLE_BP +" dm,"+TABLE_BPGOAL+" gdm "+  " where dm.member_id= gdm.bp_goalmember_id and dm.Relationship_ID=gdm.bp_goalrelationship_ID and " +
                        "  bp_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and Relationship_ID=" + RelationshipId+" "+last_entry_filter+") a where percent<15) b ";


            }

        }

        SQLiteDatabase db = this.getReadableDatabase ( );

        Cursor obj_cursor = null ;
        try
        {

            obj_cursor = db.rawQuery ( buildSQL, null);

        } catch (Exception e) {
            Log.d(TAG, "Error show_BPAnalysisFilterData_Chart");

        }

        return obj_cursor ;


    }



}

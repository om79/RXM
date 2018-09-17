package viroopa.com.medikart.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 01/Oct/2015.
 */
public class SqliteDMHandler extends SQLiteOpenHelper {

    private static final String TAG = "SqliteDMHandler";

    private static SqliteDMHandler sSqlite_obj;

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "medikart";

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
    private static final String F_KEY_BSUNIT = "bsunit";
    ///////////////////////////   Diabities Monitor   ///////////////////////////
    private static final String TABLE_DM = "user_dm_log";

    private static final String TABLE_BSCATEGORY = "bs_category";
    // DM Log Table Columns names
    private static final String F_KEY_BSCONID = "bScatid";
    private static final String F_CATEGORYID = "category_id";
    private static final String F_KEY_CATEGORYDESC = "categorydesc";
    private static final String F_KEY_CATEGORYGRPID = "categorygroupid";
    private static final String F_KEY_DMID = "DMID";
    private static final String F_DMmember_id = "member_id";
    private static final String F_RELATION_ID = "relation_id";
    private static final String F_WEIGHT_N0 = "weight_number";
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
    private static final String F_LBWEIGHT_N0 = "lbweight_number";




    private static final String TABLE_SYNC_DM = "user_sync_dm_log";

    private static final String TABLE_DMGOAL = "set_dm_Goal";
    // Login Table Columns names
    private static final String F_KEY_goalID = "goalid";
    private static final String F_GOALMEMBERID = "goalmember_id";
    private static final String F_KEY_goalrelationship_ID = "goalrelationship_ID";
    private static final String F_KEY_GOALWEIGHT = "goalweight";
    private static final String F_KEY_GOALBLOODSUGAR = "goalbloodsugar";
    private static final String F_KEY_GOALWEIGHTUNIT = "dm_weightunit";
    private static final String F_KEY_GOALDMKG = "dm_kg";
    private static final String F_KEY_GOALDMLB = "dm_lb";
    private static final String F_KEY_GOALBSUNIT = "dm_BSunit";
    private static final String F_KEY_MMOLVALUE = "g_mmolval";
    // DM Log Table Columns names

    // private static final String F_KEY_DMID = "DMID";
    private static final String F_KEY_SYNC_DMID = "sync_DMID";
    private static final String F_SYNC_DMMEMBER_ID = "sync_member_id";
    private static final String F_SYNC_RELATION_ID = "sync_relation_id";
    private static final String F_SYNC_WEIGHT_N0 = "sync_weight_number";
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

    public SqliteDMHandler ( Context context ) {
        super ( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    // Prakash K Bhandary
    public static synchronized SqliteDMHandler getInstance ( Context context ) {
        if ( sSqlite_obj == null ) {
            sSqlite_obj = new SqliteDMHandler ( context.getApplicationContext ( ) );
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


    public void InsertDMGOALTable ( Integer member_id, Integer RelationId, String weight, String weightunit, String Bloodsugar, String KG, String LB, String BSUnit ,String MMolValue) {
        SQLiteDatabase db = this.getWritableDatabase ( );

        ContentValues values = new ContentValues ( );
        values.put ( F_GOALMEMBERID, member_id ); // Name
        values.put ( F_KEY_goalrelationship_ID, RelationId ); // Name
        values.put ( F_KEY_GOALWEIGHT, weight ); // Name
        values.put ( F_KEY_GOALBLOODSUGAR, Bloodsugar ); // Name

        values.put ( F_KEY_GOALWEIGHTUNIT, weightunit ); // Name
        values.put ( F_KEY_GOALDMKG, KG ); // Name
        values.put ( F_KEY_GOALDMLB, LB ); // Name
        values.put ( F_KEY_GOALBSUNIT, BSUnit ); // Name
        values.put ( F_KEY_MMOLVALUE, MMolValue );

        long Id = db.insert ( TABLE_DMGOAL, null, values );

        // Inserting Row

        db.close ( ); // Closing database connection

        Log.d ( TAG, "New BP Detail inserted into sqlite: " + Id );
    }

    public void InsertSyncTable ( String I_Type, String Controller, String Parameter, String JsonObject,
                                  String Created_Date, String UUID, String iUpload_Download, Integer MemberId,
                                  String Module_Name, String Mode, String ControllerName, String METHODName, Long IMEI ) {
        SQLiteDatabase db = this.getWritableDatabase ( );

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
        long SyncId = db.insert ( TABLE_Medikart_SYNC, null, values );

        // Inserting Row

        db.close ( ); // Closing database connection

        Log.d ( TAG, "New BP Detail inserted into sqlite: " + SyncId );
    }
//Added For DM Detail

    public void addDMDetails ( Integer member_id, Integer RelationId, String weight, String weight_unit,
                               String g_date, String g_time, String g_value, String g_unit, String add_note, String g_category, String s_reminder, String IMEI, String UUID, String injection_site, Integer injection_position, String AMPM, String BSCategorygrpId, String g_mmolval, String LBWeightNo ) {
        SQLiteDatabase db = this.getWritableDatabase ( );

        ContentValues values = new ContentValues ( );
        values.put ( F_DMmember_id, member_id ); // Name
        values.put ( F_RELATION_ID, RelationId ); // Name
        values.put ( F_WEIGHT_N0, weight ); // Name
        values.put ( F_DM_WEIGHT_UNIT, weight_unit ); // Name
        values.put ( F_DATE, g_date ); // Name
        values.put ( F_TIME, g_time ); // Email
        values.put ( F_VALUE, g_value );
        values.put ( F_UNIT, g_unit );
        values.put ( F_ADD_NOTE, add_note );
        values.put ( F_CATEGORY, g_category );
        values.put ( F_REMINDER, s_reminder );
        values.put ( F_DMIMEI, IMEI );
        values.put ( F_DMUUID, UUID );
        values.put ( F_INJECTION_SITE, injection_site );
        values.put ( F_INJECTION_POSITION, injection_position );
        values.put ( F_AMPM, AMPM );
        values.put ( F_KEY_BSCATEGORYGRPID, BSCategorygrpId );
        values.put ( F_MMOLVALUE, g_mmolval );
        values.put ( F_LBWEIGHT_N0, LBWeightNo );
        // Inserting Row
        long id = db.insert ( TABLE_DM, null, values );

       /* ContentValues Sync_values = new ContentValues ( );

        Sync_values.put ( F_SYNC_DMMEMBER_ID, member_id ); // Name
        Sync_values.put ( F_SYNC_RELATION_ID, RelationId ); // Name
        Sync_values.put ( F_SYNC_WEIGHT_N0, weight ); // Name
        Sync_values.put ( F_SYNC_DM_WEIGHT_UNIT, weight_unit ); // Name
        Sync_values.put ( F_SYNC_DATE, g_date ); // Name
        Sync_values.put ( F_SYNC_TIME, g_time ); // Email
        Sync_values.put ( F_SYNC_VALUE, g_value );
        Sync_values.put ( F_SYNC_UNIT, g_unit );
        Sync_values.put ( F_SYNC_ADD_NOTE, add_note );
        Sync_values.put ( F_SYNC_CATEGORY, g_category );
        Sync_values.put ( F_SYNC_REMINDER, s_reminder );
        Sync_values.put ( F_SYNC_DMIMEI, IMEI );
        Sync_values.put ( F_SYNC_DMUUID, UUID );

        // Inserting Row
        long Syncid = db.insert ( TABLE_SYNC_DM, null, Sync_values );
        db.close ( ); // Closing database connection*/

        Log.d ( TAG, "New DM Detail inserted into sqlite: " + id );
    }

    //Added For DM Detail
    public void EditSaveDMDetails ( Integer member_id, Integer RelationId, String weight, String weight_unit,
                                    String g_date, String g_time, String g_value, String g_unit, String add_note, String g_category, String s_reminder, String IMEI, Integer DMid, String UUID, String injection_site, Integer injection_position, String AMPM, String BSCategorygrpId, String g_mmolval, String LBWeightNo ) {
        SQLiteDatabase db = this.getWritableDatabase ( );

        ContentValues values = new ContentValues ( );
        values.put ( F_DMmember_id, member_id ); // Name
        values.put ( F_RELATION_ID, RelationId ); // Name
        values.put ( F_WEIGHT_N0, weight ); // Name
        values.put ( F_DM_WEIGHT_UNIT, weight_unit ); // Name
        values.put ( F_DATE, g_date ); // Name
        values.put ( F_TIME, g_time ); // Email
        values.put ( F_VALUE, g_value );
        values.put ( F_UNIT, g_unit );
        values.put ( F_ADD_NOTE, add_note );
        values.put ( F_CATEGORY, g_category );
        values.put ( F_REMINDER, s_reminder );
        values.put ( F_DMIMEI, IMEI );
        values.put ( F_DMUUID, UUID );
        values.put ( F_INJECTION_SITE, injection_site );
        values.put ( F_INJECTION_POSITION, injection_position );
        values.put ( F_AMPM, AMPM );

        values.put ( F_KEY_BSCATEGORYGRPID, BSCategorygrpId );
        values.put ( F_MMOLVALUE, g_mmolval );
        values.put ( F_LBWEIGHT_N0, LBWeightNo );
        int id = db.update ( TABLE_DM, values, "DMID = " + DMid, null );

        db.close ( );
    }

    public Cursor getAllCategory_data ( ) {

        String buildSQL = "SELECT   * FROM " + TABLE_BSCATEGORY;/*, new String[]{String.valueOf(toDate), String.valueOf(RelationShipid)}*/

        SQLiteDatabase db = this.getReadableDatabase ( );
        return db.rawQuery ( buildSQL, null );
    }

    public void deleteCategory ( ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        // Delete All Rows
        db.delete ( TABLE_BSCATEGORY, "", null );
        db.close ( );

        Log.d ( TAG, "Deleted all user info from sqlite" );
    }

    public void InsertBSCategory ( Integer CategoryId, String Category_description, String CategoryGrpId ) {
        SQLiteDatabase db = this.getWritableDatabase ( );

        ContentValues values = new ContentValues ( );
        values.put ( F_CATEGORYID, CategoryId ); // Name
        values.put ( F_KEY_CATEGORYDESC, Category_description ); // Name
        values.put ( F_KEY_CATEGORYGRPID, CategoryGrpId ); // Name

        long SyncId = db.insert ( TABLE_BSCATEGORY, null, values );

        // Inserting Row

        db.close ( ); // Closing database connection

    }

    public Cursor getCategoryGrpIdByCategoryName ( String CategoryDesc ) {


        String buildSQL = " SELECT  categorygroupid FROM " + TABLE_BSCATEGORY + " Where categorydesc = '" + CategoryDesc + "'";

        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }

    public Cursor getAllDMMonitarDataOnDmID ( Integer DMid ) {


        String buildSQL = " SELECT   * FROM " + TABLE_DM + " Where DMID = " + DMid;

        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }


    public Cursor getTop10DMMonitarData ( Integer MemberId, Integer RelationshipId ) {


        //String buildSQL =" SELECT  \"+ F_KEY_DMID + \" AS _id, STRFTIME('%d', g_date) as dmdate,g_value as g_value,g_time,weight_number,'Date' as titleDate,'Time' as titleTime ,'Blood Sugar' as titleBloodSugar,'Weight' as titleWeight ,g_value as bs_value,weight_number as wt_value,strftime('%Y %m %d', g_date) as bs_date,ifnull(MAX(g_value),0) as Bs_max, ifnull(MIN(g_value),0) as Bs_min, (ifnull(MAX(g_value),0)+ifnull(MIN(g_value),0))/2  as Bs_avg,ifnull(MAX(weight_number),0) as max_wt ,  ifnull(MIN(weight_number),0) as min_wt,(ifnull(max(weight_number),0)+ifnull(MIN(weight_number),0))/2  as avg_wt,strftime('%Y %m %d', g_date) as fromdate,strftime('%Y %m %d', g_date) as todate FROM " + TABLE_DM + " Where member_id = " +MemberId + "  and relation_id="+ RelationshipId + " and strftime('%Y %m %d', g_date)>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now')  order by DMID desc  ";
        String buildSQL = " SELECT  DMID AS _id, STRFTIME('%d', g_date) as dmdate,g_value as g_value,g_time,weight_number,'Date' as titleDate,'Time' as titleTime ,'Blood Sugar' as titleBloodSugar,'Weight' as titleWeight ,g_value as bs_value,weight_number as wt_value,strftime('%Y %m %d', g_date) as bs_date,ifnull(MAX(g_value),0) as Bs_max, ifnull(MIN(g_value),0) as Bs_min, (ifnull(MAX(g_value),0)+ifnull(MIN(g_value),0))/2  as Bs_avg,ifnull(MAX(weight_number),0) as max_wt ,  ifnull(MIN(weight_number),0) as min_wt,(ifnull(max(weight_number),0)+ifnull(MIN(weight_number),0))/2  as avg_wt,strftime('%Y %m %d', g_date) as fromdate,strftime('%Y %m %d', g_date) as todate FROM " + TABLE_DM + " Where member_id = " + MemberId + "  and relation_id=" + RelationshipId + "  order by DMID desc  limit 10";// and strftime('%Y %m %d', g_date)>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now')
        //String buildSQL ="select "+ F_KEY_DMID + " AS _id, ifnull(MAX(g_value),0) as bs_max, ifnull(MIN(g_value),0) as bs_min, (ifnull(max(g_value),0)+ifnull(MIN(g_value),0))/2  as avg_bs,Case When (( max(g_value) + min(g_value))/2) <= 100 then 0 When ((( max(g_value) + min(g_value))/2) <= 120) and ((g_value) >= 100) then 1 When ((( max(g_value) + min(g_value))/2) >= 120) and ((g_value) <= 139) then 1 When ((( max(g_value) + min(g_value))/2) > 139) then 2 else -1 end as sys_trend, ifnull(MAX(diastolic),0) as dia_max ,  ifnull(MIN(diastolic),0) as dia_min ,(ifnull(max(diastolic),0)+ifnull(MIN(diastolic),0))/2  as avg_dia,Case When ((ifnull(MAX(diastolic),0)+ ifnull(MIN(diastolic),0))/2) <= 60 then 0 When (((ifnull(MAX(diastolic),0)+ ifnull(MIN(diastolic),0))/2) <= 80) and ((ifnull(MAX(diastolic),0)+ ifnull(MIN(diastolic),0))/2) >= 60 then 1  When (((ifnull(MAX(diastolic),0)+ ifnull(MIN(diastolic),0))/2) >= 80) and (((ifnull(MAX(diastolic),0)+ ifnull(MIN(diastolic),0))/2) <= 89) then 1 When (((ifnull(MAX(diastolic),0)+ ifnull(MIN(diastolic),0))/2) >= 100) then 2 else -1 end as dia_trend, ifnull(MAX(pulse),0) as max_pulse,  ifnull(MIN(pulse),0) as min_pulse , (ifnull(max(pulse),0)+ifnull(MIN(pulse),0))/2  as avg_pulse,CASE when ((ifnull(MAX(pulse),0)+ ifnull(MIN(pulse),0))/2>60) then 0 when (((ifnull(MAX(pulse),0)+ ifnull(MIN(pulse),0))/2<=80) and ((ifnull(MAX(pulse),0)+ ifnull(MIN(pulse),0))/2>= 60) ) then 1  when ((ifnull(MAX(pulse),0)+ ifnull(MIN(pulse),0))/2> 89) then 2   else -1 end  as pulse_trend, ifnull(MAX(weight),0) as max_wt ,  ifnull(MIN(weight),0) as min_wt,(ifnull(max(weight),0)+ifnull(MIN(weight),0))/2  as avg_wt,1 as wt_trend,'Afternoon' as title,'g_value' as g_value,'Diastolic' as diastolic,'Pulse' as pulse,'Weight(lbs)' as weight,'Max'as max,'Min' AS min,'Avg' as avg,'Trends' as trend FROM " + TABLE_DM +" WHERE bptimehr >= 12 and bptimehr <= 15 and strftime('%Y %m %d', g_date)>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now')  and  member_id ="+MemberId +"  and relation_id="+ RelationshipId;//";//and  g_date<=date('now','-30 day') and g_date>=date('now')";
        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }

    public Cursor getLastReadingDMMonitarData ( Integer MemberId, Integer RelationshipId ) {

        String buildSQL = " SELECT   * FROM " + TABLE_DM + " Where  member_id = " + MemberId + " and relation_id=" + RelationshipId + " order by DMID desc limit 1;";
        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }


    public Cursor getTop10detailDMMonitarData ( Integer MemberId, Integer RelationshipId ) {


        String buildSQL = " SELECT  DMID AS _id, STRFTIME('%d', g_date) as dmdate,STRFTIME('%Y', g_date) as dmYear,g_value as g_value,g_time,weight_number,'Date' as titleDate,'Time' as titleTime ,'Blood Sugar' as titleBloodSugar,'Weight' as titleWeight ,g_value as bs_value,weight_number as wt_value,strftime('%Y %m %d', g_date) as bs_date,0 as Bs_max, 0 as Bs_min, 0 as Bs_avg,0 as max_wt , 0 as min_wt,0  as avg_wt,strftime('%Y %m %d', g_date) as fromdate,strftime('%Y %m %d', g_date) as todate,AMPM FROM " + TABLE_DM + " Where member_id = " + MemberId + "  and relation_id=" + RelationshipId + "  order by g_date desc  limit 10";// and strftime('%Y %m %d', g_date)>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now')

        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }


    public Cursor getTop1DMMonitarData ( Integer MemberId, Integer RelationshipId ) {


        String buildSQL = " SELECT   * FROM " + TABLE_DM + " Where member_id = " + MemberId + "  and relation_id=" + RelationshipId + " order by DMID desc  limit 1;";
        // String buildSQL =" SELECT DMID AS _id,STRFTIME('%d', g_date) as dmdate,g_value as g_value,g_time,weight_number,'Date' as titleDate,'Time' as titleTime ,'Blood Sugar' as titleBloodSugar,'Weight' as titleWeight ,g_value as bs_value,weight_number as wt_value,strftime('%Y %m %d', g_date) as bs_date, ifnull(MAX(g_value),0) as Bs_max, ifnull(MIN(g_value),0) as Bs_min, (ifnull(MAX(g_value),0)+ifnull(MIN(g_value),0))/2  as Bs_avg,ifnull(MAX(weight_number),0) as max_wt ,  ifnull(MIN(weight_number),0) as min_wt,(ifnull(max(weight_number),0)+ifnull(MIN(weight_number),0))/2  as avg_wt,strftime('%Y %m %d', g_date) as fromdate,strftime('%Y %m %d', g_date) as todate FROM " + TABLE_DM + " Where member_id = " +MemberId + "  and relation_id="+ RelationshipId + " and strftime('%Y %m %d', g_date)>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now')  order by DMID desc  limit 1 ";
        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }


    public Cursor getgoalDMMonitarData ( Integer MemberId, Integer RelationshipId )
    {
        String buildSQL = " SELECT   * FROM " + TABLE_DMGOAL + " Where goalmember_id = " + MemberId + "  and goalrelationship_ID=" + RelationshipId;
        // String buildSQL =" SELECT DMID AS _id,STRFTIME('%d', g_date) as dmdate,g_value as g_value,g_time,weight_number,'Date' as titleDate,'Time' as titleTime ,'Blood Sugar' as titleBloodSugar,'Weight' as titleWeight ,g_value as bs_value,weight_number as wt_value,strftime('%Y %m %d', g_date) as bs_date, ifnull(MAX(g_value),0) as Bs_max, ifnull(MIN(g_value),0) as Bs_min, (ifnull(MAX(g_value),0)+ifnull(MIN(g_value),0))/2  as Bs_avg,ifnull(MAX(weight_number),0) as max_wt ,  ifnull(MIN(weight_number),0) as min_wt,(ifnull(max(weight_number),0)+ifnull(MIN(weight_number),0))/2  as avg_wt,strftime('%Y %m %d', g_date) as fromdate,strftime('%Y %m %d', g_date) as todate FROM " + TABLE_DM + " Where member_id = " +MemberId + "  and relation_id="+ RelationshipId + " and strftime('%Y %m %d', g_date)>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now')  order by DMID desc  limit 1 ";
        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }

    public Cursor getDMMonitardetailData ( Integer MemberId, Integer RelationshipId ) {


        // String buildSQL =" SELECT   * FROM " + TABLE_DM + " Where member_id = " +MemberId + "  and relation_id="+ RelationshipId + " order by DMID desc  limit 1;";
        String buildSQL = " SELECT  DMID AS _id, STRFTIME('%d', g_date) as dmdate,isnull(g_value,0) as g_value,g_time,weight_number,'Date' as titleDate,'Time' as titleTime ,'Blood Sugar' as titleBloodSugar,'Weight' as titleWeight ,strftime('%Y %m %d', g_date) as fromdate,strftime('%Y %m %d', g_date) as todate FROM " + TABLE_DM + " Where member_id = " + MemberId + "  and relation_id=" + RelationshipId + "  order by DMID desc  limit 10";
        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }

    public void deleteDMDataById ( Integer dmid ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        // Delete All Rows
        db.delete ( TABLE_DM, F_KEY_DMID + " = " + dmid, null );
        db.close ( );

        Log.d ( TAG, "Deleted all user info from sqlite" );
    }

    public void deleteDMDataAll () {
        SQLiteDatabase db = this.getWritableDatabase ( );
        // Delete All Rows
        db.delete ( TABLE_DM, "", null );
        db.close ( );

        Log.d ( TAG, "Deleted all user info from sqlite" );
    }

    public void deleteSyncDMDetails ( String DMID, String MemberId, String RelationshipId ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        // Delete All Rows
        db.delete ( TABLE_SYNC_DM, "DMID=" + DMID + " AND member_id=" + MemberId, null );
        db.close ( );

        Log.d ( TAG, "Deleted all user info from sqlite" );
    }

    public void deleteGoalDMDetails ( Integer MemberId, Integer RelationshipId ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        // Delete All Rows
        db.delete ( TABLE_DMGOAL, "goalmember_id=" + MemberId + " and goalrelationship_ID=" + RelationshipId, null );
        db.close ( );

        Log.d ( TAG, "Deleted all user info from sqlite" );
    }





    public Cursor show_DMAnalysisFilterData_Chart(Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {

        String Day = "";
        String buildSQL = "";

        String sQueryCommon = "select * from " + TABLE_DM + " where member_id = " + MemberId + " and relation_id = " + RelationshipId + " " ;
        String sQueryEnd =  "";

        if ( Filter.equals("Weekly")) {
            Day = "-7 days";
        }
        if ( Filter.equals("Last10")) {
            Day = "-11 days";
        }
        if ( Filter.equals("Monthly")) {
            Day = "-30 days";
        }
        if ( Filter.equals("Yearly")) {
            Day = "-365 days";
        }

        if (AMPM.equals("AM/PM"))
        {
            if ( Filter.equals("Day"))
            {
                buildSQL =  sQueryCommon + " and strftime('%Y %m %d', g_date) = strftime('%Y %m %d','now')" + sQueryEnd ;
            } if ( Filter.equals("Last10"))
        {
            buildSQL =  sQueryCommon + " order by DMID DESC Limit 10 " + sQueryEnd ;
        }
            else
            {
                buildSQL = sQueryCommon + " and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"')" + sQueryEnd ;
            }
        }
        else
        {
            if ( Filter.equals("Day"))
            {
                buildSQL = sQueryCommon + " and AMPM='" + AMPM + "' and strftime('%Y %m %d', g_date) =strftime('%Y %m %d','now')" + sQueryEnd ;
            } if ( Filter.equals("Last10"))
        {
            buildSQL = sQueryCommon + " and AMPM='" + AMPM + "' order by DMID DESC Limit 10 " + sQueryEnd ;
        }
            else
            {
                buildSQL = sQueryCommon + " and AMPM='" + AMPM + "' and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"')" + sQueryEnd ;
            }
        }

        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );
        return db.rawQuery ( buildSQL, null );
    }

    public void deleteDMDetails ( ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        // Delete All Rows
        db.delete ( TABLE_DM, null, null );
        db.close ( );

        Log.d ( TAG, "Deleted all user info from sqlite" );
    }

    public Cursor show_DMMinMaxAnalysisFilterData_Chartnew ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM,String toDate ) {

        String Day = "";
        String buildSQL = "";

        String sQueryCommon = "select 0 AS _id,ifnull(MAX(g_value),0) as bs_max,"
                            + "ifnull(MIN(g_value),0) as bs_min,"
                            + "(ifnull(max(g_value),0) + ifnull(MIN(g_value),0))/2  as avg_bs,"
                + " ifnull(MAX(g_value),0) as bsmmol_max,"
                + "ifnull(MIN(g_mmolval),0) as bsmmol_min,"
                + "(ifnull(max(g_mmolval),0) + ifnull(MIN(g_mmolval),0))/2  as avg_bsmmol,"
                            + "ifnull(MAX(weight_number),0) as max_wt,"
                            + "ifnull(MIN(weight_number),0) as min_wt,"
                            + "(ifnull(max(weight_number),0) + ifnull(MIN(weight_number),0))/2 as avg_wt,"
                + "ifnull(MAX(lbweight_number),0) as maxlb_wt,"
                + "ifnull(MIN(lbweight_number),0) as minlb_wt,"
                + "(ifnull(max(lbweight_number),0) + ifnull(MIN(lbweight_number),0))/2 as avg_lbwt,"
                            + "weight_unit, g_unit from " + TABLE_DM
                            + " where member_id = " + MemberId + " and relation_id = " + RelationshipId + " " ;

        if (Filter.equals("Weekly")) {
            Day = "-7 days";
        }
        if (Filter.equals("Last10")) {
            Day = "-11 days";
        }
        if (Filter.equals("Monthly")) {
            Day = "-30 days";
        }
        if (Filter.equals("Yearly")) {
            Day = "-365 days";
        }

        if ( AMPM.equals("AM/PM"))
        {
            if (Filter.equals("Day"))
            {
                buildSQL = sQueryCommon + " and strftime('%Y %m %d', g_date) = strftime('%Y %m %d','now') " ;
            }else  if (Filter.equals("Last10"))
            {
                buildSQL = sQueryCommon + " order by DMID DESC Limit 10" ;
            }
            else
            {
                buildSQL = sQueryCommon + " and g_date  between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') " ;
            }
        }
        else
        {
            if (Filter.equals ( "Day" ) )
            {
                buildSQL = sQueryCommon + " and AMPM = '" + AMPM + "' and strftime('%Y %m %d', g_date) =strftime('%Y %m %d','now') " ;
            }else  if (Filter.equals("Last10"))
            {
                buildSQL = sQueryCommon + " and AMPM = '" + AMPM + "' order by DMID DESC Limit 10 " ;
            }
            else
            {
                buildSQL = sQueryCommon + " and AMPM = '" + AMPM + "' and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') " ;
            }
        }

        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );
        return db.rawQuery ( buildSQL, null );
    }

    public Cursor get_ALLDMDeatil ( Integer MemberId, Integer RelationshipId ) {


        String buildSQL = " select " + F_KEY_DMID + " AS _id,DMID,member_id as memberid,g_date as dm_date,g_time as dm_time,sync_g_value as g_value, g_category as category,relation_id,sync_add_note as Note,s_reminder as reminder,sync_weight_unit as weight_unit,weight_number as weight_number , sync_g_unit as g_unit,DMIMEI AS DMIMEI  FROM " + TABLE_SYNC_DM + " where member_id =" + MemberId + "  and relation_id=" + RelationshipId;//";
        //+" WHERE strftime('%Y %m %d', g_date)>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now')";//date(datetime(g_date / 1000 )) = date('now')";//date('now')";//g_date>=JULIANDAY('now','-30 days') and


        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }

    public Cursor getAllData_historyDM ( Integer MemberId, Integer RelationshipId ) {


        String buildSQL = " select " + F_KEY_DMID + " AS _id,g_date as dm_date,g_time as dm_time,'Glucose' as Glucose_alias, g_value as g_value, g_category as category FROM " + TABLE_DM + " where member_id =" + MemberId + "  and relation_id=" + RelationshipId;//";
        //+" WHERE strftime('%Y %m %d', g_date)>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now')";//date(datetime(g_date / 1000 )) = date('now')";//date('now')";//g_date>=JULIANDAY('now','-30 days') and


        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }

    public Cursor getAllData_Chart ( Integer MemberId, Integer RelationshipId ) {

        //String buildSQL =" SELECT "+ F_KEY_DMID+ " AS _id,*"+ " FROM " + TABLE_DM;
        //String buildSQL ="select \"+ F_KEY_DMID + \" AS _id, g_date,(ifnull(MAX(g_value),0) + ifnull(MIN(g_value),0) )/2 as bs_avg, (ifnull(MAX(diastolic),0) +ifnull(MIN(diastolic),0))/2 as dia_avg,(ifnull(MAX(pulse),0)+ifnull(MIN(pulse),0))/2 as pulse_avg,(ifnull(MAX(weight),0)+ ifnull(MIN(weight),0))/2 as max_wt  FROM " + TABLE_DM +" WHERE   g_date>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now') group by g_date";//and g_date<=date('2015-09-01')" ;// and g_date<=date('now','-30 day') and g_date>=date('now')";
        String buildSQL = "select " + F_KEY_DMID + " AS _id, Max(ifnull(g_value,0)) as MaxVal, Min(ifnull(g_value,0)) as MinVal, * from  " + TABLE_DM + " where  g_date>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now') and member_id =" + MemberId + "  and relation_id=" + RelationshipId + " group by g_date";//and g_date<=date('2015-09-01')" ;// and g_date<=date('now','-30 day') and g_date>=date('now')";

        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }


    public Cursor showAllData_Chart ( Integer MemberId, Integer RelationshipId, String Fromdate, String Todate ) {

        //String buildSQL =" SELECT "+ F_KEY_DMID+ " AS _id,*"+ " FROM " + TABLE_DM;
        //String buildSQL ="select \"+ F_KEY_DMID + \" AS _id, g_date,(ifnull(MAX(g_value),0) + ifnull(MIN(g_value),0) )/2 as bs_avg, (ifnull(MAX(diastolic),0) +ifnull(MIN(diastolic),0))/2 as dia_avg,(ifnull(MAX(pulse),0)+ifnull(MIN(pulse),0))/2 as pulse_avg,(ifnull(MAX(weight),0)+ ifnull(MIN(weight),0))/2 as max_wt  FROM " + TABLE_DM +" WHERE   g_date>= strftime('%Y %m %d','now','-30 days') and strftime('%Y %m %d', g_date)<= strftime('%Y %m %d','now') group by g_date";//and g_date<=date('2015-09-01')" ;// and g_date<=date('now','-30 day') and g_date>=date('now')";
        String buildSQL = "select " + F_KEY_DMID + " AS _id, Max(ifnull(g_value,0)) as MaxVal, Min(ifnull(g_value,0)) as MinVal,strftime('%m', g_date) as month, * from  " + TABLE_DM + " where  strftime('%Y %m %d', g_date) Between strftime('%Y %m %d','" + Fromdate + "') and  strftime('%Y %m %d', '" + Todate + "') and member_id =" + MemberId + "  and relation_id=" + RelationshipId + " group by g_date";//and g_date<=date('2015-09-01')" ;// and g_date<=date('now','-30 day') and g_date>=date('now')";

        SQLiteDatabase db = this.getReadableDatabase ( );
        Log.d ( TAG, "getAllData SQL: " + buildSQL );

        return db.rawQuery ( buildSQL, null );
    }



    public Cursor show_DMMinMaxAnalysisFilterData_Chart ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {
        String sAMPM = "";
        String Day = "";
        String buildSQL = "";
        String last_entry_filter="order by g_date";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }
        if ( Filter.equals ( "LastReading" ) ) {
            Day = "-365 days";
            last_entry_filter="ORDER BY  DMID DESC LIMIT 1";

        }
        if ( Filter.equals ( "Weekly" ) ) {
           last_entry_filter="order by g_date";
            Day = "-7 days";

        }
        if ( Filter.equals ( "Last10" ) ) {
            Day = "-700 days";
            last_entry_filter="ORDER BY g_date, DMID DESC LIMIT 10";
        }

        if ( Filter.equals ( "Monthly" ) ) {
            last_entry_filter="order by g_date";
            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {
            last_entry_filter="order by g_date";
            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {

            buildSQL = "select avg(g_value) as g_value, avg(g_mmolval) as g_mmolval, avg(weight_number) as weight_number , g_date  from  " + TABLE_DM + " where   g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" group by g_date "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        } else {

            if ( AMPM.equals ( "PM" ) ) {
                buildSQL = "select avg(g_value) as g_value, avg(g_mmolval) as g_mmolval, avg(weight_number) as weight_number , g_date from  " + TABLE_DM + " where    substr(g_time,1,2)>=12 and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" group by g_date "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            } else {
                buildSQL = "select avg(g_value) as g_value, avg(g_mmolval) as g_mmolval, avg(weight_number) as weight_number , g_date from  " + TABLE_DM + " where    substr(g_time,1,2)<12 and" +
                        " g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+"  group by g_date "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
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
    public Cursor show_DMMinMaxAnalysisFilterData_pdf ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {
        String sAMPM = "";
        String Day = "";
        String buildSQL = "";
        String last_entry_filter="order by g_date";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }
        if ( Filter.equals ( "LastReading" ) ) {
            Day = "-365 days";
            last_entry_filter="ORDER BY  DMID DESC LIMIT 1";

        }
        if ( Filter.equals ( "Weekly" ) ) {
            last_entry_filter="order by g_date";
            Day = "-7 days";

        }
        if ( Filter.equals ( "Last10" ) ) {
            Day = "-700 days";
            last_entry_filter="ORDER BY g_date, DMID DESC LIMIT 10";
        }

        if ( Filter.equals ( "Monthly" ) ) {
            last_entry_filter="order by g_date";
            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {
            last_entry_filter="order by g_date";
            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {

            buildSQL = "select * from  " + TABLE_DM + " where   g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        } else {

            if ( AMPM.equals ( "PM" ) ) {
                buildSQL = "select * from  " + TABLE_DM + " where    substr(g_time,1,2)>=12 and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            } else {
                buildSQL = "select * from  " + TABLE_DM + " where    substr(g_time,1,2)<12 and" +
                        " g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
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

    public Cursor show_DMMinMaxAnalysisFilterData_Static ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {
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
            last_entry_filter="ORDER BY DMID DESC LIMIT 1";

        }
        if ( Filter.equals ( "Weekly" ) ) {
            Day = "-7 days";

        }
        if ( Filter.equals ( "Last10" ) ) {
            Day = "-700 days";
            last_entry_filter="ORDER BY DMID DESC LIMIT 10";
        }

        if ( Filter.equals ( "Monthly" ) ) {
            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {
            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {

            buildSQL = "select (ifnull(MAX(weight_number),0)) as max_weight_kg,(ifnull(Min(weight_number),0)) as min_weight_kg,(ifnull(avg(weight_number),0)) as avg_weight_kg,(ifnull(MAX(lbweight_number),0)) as max_weight_lb,(ifnull(Min(lbweight_number),0)) as min_weight_lb,(ifnull(avg(lbweight_number),0)) as avg_weight_lb,(ifnull(MAX(g_value),0)) as max_g_value,(ifnull(Min(g_value),0)) as min_g_value,(ifnull(avg(g_value),0)) as avg_g_value ,(ifnull(MAX(g_mmolval),0)) as max_g_mmolval,(ifnull(Min(g_mmolval),0)) as min_g_mmolval,(ifnull(avg(g_mmolval),0)) as avg_g_mmolval from  " + TABLE_DM + " where   g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        } else {

            if ( AMPM.equals ( "PM" ) ) {
                buildSQL = "select (ifnull(MAX(weight_number),0)) as max_weight_kg,(ifnull(Min(weight_number),0)) as min_weight_kg,(ifnull(avg(weight_number),0)) as avg_weight_kg,(ifnull(MAX(lbweight_number),0)) as max_weight_lb,(ifnull(Min(lbweight_number),0)) as min_weight_lb,(ifnull(avg(lbweight_number),0)) as avg_weight_lb,(ifnull(MAX(g_value),0)) as max_g_value,(ifnull(Min(g_value),0)) as min_g_value,(ifnull(avg(g_value),0)) as avg_g_value ,(ifnull(MAX(g_mmolval),0)) as max_g_mmolval,(ifnull(Min(g_mmolval),0)) as min_g_mmolval,(ifnull(avg(g_mmolval),0)) as avg_g_mmolval from  " + TABLE_DM + " where    substr(g_time,1,2)>=12 and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            } else {
                buildSQL = "select (ifnull(MAX(weight_number),0)) as max_weight_kg,(ifnull(Min(weight_number),0)) as min_weight_kg,(ifnull(avg(weight_number),0)) as avg_weight_kg,(ifnull(MAX(lbweight_number),0)) as max_weight_lb,(ifnull(Min(lbweight_number),0)) as min_weight_lb,(ifnull(avg(lbweight_number),0)) as avg_weight_lb,(ifnull(MAX(g_value),0)) as max_g_value,(ifnull(Min(g_value),0)) as min_g_value,(ifnull(avg(g_value),0)) as avg_g_value ,(ifnull(MAX(g_mmolval),0)) as max_g_mmolval,(ifnull(Min(g_mmolval),0)) as min_g_mmolval,(ifnull(avg(g_mmolval),0)) as avg_g_mmolval from " + TABLE_DM + " where    substr(g_time,1,2)<12 and" +
                        " g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
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
            Day = "-700 days";

            last_entry_filter="ORDER BY rowid DESC LIMIT 10 ";
        }

        if ( Filter.equals ( "Monthly" ) ) {

            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {

            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {

         //   buildSQL = "select (ifnull(MAX(weight_number),0)) as max_weight_kg,(ifnull(Min(weight_number),0)) as min_weight_kg,(ifnull(avg(weight_number),0)) as avg_weight_kg,(ifnull(MAX(lbweight_number),0)) as max_weight_lb,(ifnull(Min(lbweight_number),0)) as min_weight_lb,(ifnull(avg(lbweight_number),0)) as avg_weight_lb,(ifnull(MAX(g_value),0)) as max_g_value,(ifnull(Min(g_value),0)) as min_g_value,(ifnull(avg(g_value),0)) as avg_g_value ,(ifnull(MAX(g_mmolval),0)) as max_g_mmolval,(ifnull(Min(g_mmolval),0)) as min_g_mmolval,(ifnull(avg(g_mmolval),0)) as avg_g_mmolval from  " + TABLE_DM + " where   g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";


            buildSQL="select sum(toal_count)as total_count, sum(Hypoglycemia_low_glucose)as Hypoglycemia_low_glucose, sum(normal_glucose)as normal_glucose,sum(Hyperglycemia_high_glucose)as Hyperglycemia_high_glucose from(select count (*) as toal_count, 0 as Hypoglycemia_low_glucose, 0 as normal_glucose, 0 as Hyperglycemia_high_glucose from (Select * from(Select g_value,g_date,member_id,relation_id from   "
                    + TABLE_DM +" "+last_entry_filter+ ") where g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                    RelationshipId+")"+

                    "union select 0 as toal_count, count (*) as Hypoglycemia_low_glucose, 0 as normal_glucose, 0 as Hyperglycemia_high_glucose from (Select * from (Select g_value,g_date,member_id,relation_id from  "
                    + TABLE_DM +" "+last_entry_filter+ ") where g_value<70  and   g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                    RelationshipId+")"+

                    "union select 0 as toal_count, 0 as Hypoglycemia_low_glucose, count (*) as normal_glucose, 0 as Hyperglycemia_high_glucose from(Select * from (Select g_value,g_date,member_id,relation_id from  "
                    + TABLE_DM +" "+last_entry_filter+ ") where g_value between 70 and 250 and    g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                    RelationshipId+")"+

                    "union select 0 as toal_count, 0 as Hypoglycemia_low_glucose, 0 as normal_glucose, count (*) as Hyperglycemia_high_glucose from (Select * from (Select g_value,g_date,member_id,relation_id from  "
                    + TABLE_DM +" "+last_entry_filter+ ") where g_value > 250 and    g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                    RelationshipId+")"+
                    " )A";


        } else {

            if ( AMPM.equals ( "PM" ) ) {

                buildSQL="select sum(toal_count)as total_count, count(Hypoglycemia_low_glucose)as Hypoglycemia_low_glucose, sum(normal_glucose)as normal_glucose,sum(Hyperglycemia_high_glucose)as Hyperglycemia_high_glucose from( select count (*) as toal_count, 0 as Hypoglycemia_low_glucose, 0 as normal_glucose, 0 as Hyperglycemia_high_glucose from (Select * from(Select g_value,g_date,g_time,member_id,relation_id from   "
                        + TABLE_DM +" "+last_entry_filter+ ") where   substr(g_time,1,2)>=12 and  g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, count (*) as Hypoglycemia_low_glucose, 0 as normal_glucose, 0 as Hyperglycemia_high_glucose from(Select * from(Select g_value,g_date,g_time,member_id,relation_id from   "
                        + TABLE_DM +" "+last_entry_filter+ ") where  substr(g_time,1,2)>=12 and g_value<70  and   g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Hypoglycemia_low_glucose, count (*) as normal_glucose, 0 as Hyperglycemia_high_glucose from(Select * from(Select g_value,g_date,g_time,member_id,relation_id from   "
                        + TABLE_DM +" "+last_entry_filter+ ") where  substr(g_time,1,2)>=12 and g_value between 70 and 250 and    g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Hypoglycemia_low_glucose, 0 as normal_glucose, count (*) as Hyperglycemia_high_glucose from(Select * from(Select g_value,g_date,g_time,member_id,relation_id from   "
                        + TABLE_DM +" "+last_entry_filter+ ") where  substr(g_time,1,2)>=12 and g_value > 250 and    g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                        RelationshipId+")"+
                        " )A";



               // buildSQL = "select (ifnull(MAX(weight_number),0)) as max_weight_kg,(ifnull(Min(weight_number),0)) as min_weight_kg,(ifnull(avg(weight_number),0)) as avg_weight_kg,(ifnull(MAX(lbweight_number),0)) as max_weight_lb,(ifnull(Min(lbweight_number),0)) as min_weight_lb,(ifnull(avg(lbweight_number),0)) as avg_weight_lb,(ifnull(MAX(g_value),0)) as max_g_value,(ifnull(Min(g_value),0)) as min_g_value,(ifnull(avg(g_value),0)) as avg_g_value ,(ifnull(MAX(g_mmolval),0)) as max_g_mmolval,(ifnull(Min(g_mmolval),0)) as min_g_mmolval,(ifnull(avg(g_mmolval),0)) as avg_g_mmolval from  " + TABLE_DM + " where    substr(g_time,1,2)>=12 and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            } else {


                buildSQL="select sum(toal_count) as total_count, sum(Hypoglycemia_low_glucose) as Hypoglycemia_low_glucose, sum(normal_glucose) as normal_glucose,sum(Hyperglycemia_high_glucose) as Hyperglycemia_high_glucose from( select count (*)  as toal_count, 0 as Hypoglycemia_low_glucose, 0 as normal_glucose, 0 as Hyperglycemia_high_glucose from(Select * from(Select * from(Select g_value,g_date,g_time,member_id,relation_id from   "
                        + TABLE_DM +" "+last_entry_filter+ ") where    substr(g_time,1,2)<12 and g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, count (*) as Hypoglycemia_low_glucose, 0 as normal_glucose, 0 as Hyperglycemia_high_glucose from(Select * from(Select * from(Select g_value,g_date,g_time,member_id,relation_id from   "
                        + TABLE_DM +" "+last_entry_filter+ ") where    substr(g_time,1,2)<12 and g_value<70  and   g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Hypoglycemia_low_glucose, count (*) as normal_glucose, 0 as Hyperglycemia_high_glucose from(Select * from(Select * from(Select g_value,g_date,g_time,member_id,relation_id from   "
                        + TABLE_DM +" "+last_entry_filter+ ") where   substr(g_time,1,2)<12 and g_value between 70 and 250 and    g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
                        RelationshipId+")"+

                        "union select 0 as toal_count, 0 as Hypoglycemia_low_glucose, 0 as normal_glucose, count (*) as Hyperglycemia_high_glucose from(Select * from(Select * from(Select g_value,g_date,g_time,member_id,relation_id from   "
                        + TABLE_DM +" "+last_entry_filter+ ") where    substr(g_time,1,2)<12 and g_value > 250 and    g_date between datetime('"+toDate+"', '" + Day + "')and datetime('"+toDate+"')and member_id = " + MemberId + " and relation_id = " +
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

    public Cursor show_weight_analysis_data ( Integer MemberId, Integer RelationshipId, String Filter, String AMPM ,String toDate) {
        String sAMPM = "";
        String Day = "";
        String buildSQL = "";
        String last_entry_filter="order by g_date";
        if ( AMPM.equals ( "AM" ) ) {
            sAMPM = "AM";

        }
        if ( AMPM.equals ( "PM" ) ) {
            sAMPM = "PM";
        }
        if ( Filter.equals ( "LastReading" ) ) {
            Day = "-365 days";
            last_entry_filter="ORDER BY  DMID DESC LIMIT 1";

        }
        if ( Filter.equals ( "Weekly" ) ) {
            last_entry_filter="order by g_date";
            Day = "-7 days";

        }
        if ( Filter.equals ( "Last10" ) ) {
            Day = "-700 days";
            last_entry_filter="ORDER BY g_date, DMID DESC LIMIT 10";
        }

        if ( Filter.equals ( "Monthly" ) ) {
            last_entry_filter="order by g_date";
            Day = "-30 days";
        }
        if ( Filter.equals ( "Yearly" ) ) {
            last_entry_filter="order by g_date";
            Day = "-365 days";
        }
        if ( AMPM.equals ( "AM/PM" ) ) {

            buildSQL = "select sum(over_weight) as over_weight,sum(normal_weight) as normal_weight from (select count(*) as over_weight, 0 as normal_weight from (select (((dm.weight_number-gdm.dm_kg)/gdm.dm_kg)*100) as percent  from  " + TABLE_DM +" dm,"+TABLE_DMGOAL+" gdm "+  " where dm.member_id= gdm.goalmember_id and dm.relation_id=gdm.goalrelationship_ID and " +
                    "g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId
                    +" "+last_entry_filter+") a where percent>15   union "

            +"select 0 as over_weight, count(*) as normal_weight from (select (((dm.weight_number-gdm.dm_kg)/gdm.dm_kg)*100) as percent  from  " + TABLE_DM +" dm,"+TABLE_DMGOAL+" gdm "+  " where dm.member_id= gdm.goalmember_id and dm.relation_id=gdm.goalrelationship_ID and " +
                    "g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and member_id =" + MemberId + "  and relation_id=" + RelationshipId
                            +" "+last_entry_filter+") a where percent<15) b "


            ;//and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";

        } else {

            if ( AMPM.equals ( "PM" ) ) {

                buildSQL = "select sum(over_weight) as over_weight,sum(normal_weight) as normal_weightfrom (select count(*) as over_weight, 0 as normal_weight from (select (((dm.weight_number-gdm.dm_kg)/gdm.dm_kg)*100) as percent  from  " + TABLE_DM +" dm,"+TABLE_DMGOAL+"  gdm "+  " where dm.member_id= gdm.goalmember_id and dm.relation_id=gdm.goalrelationship_ID and " +
                        "substr(g_time,1,2)>=12 and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"')" +
                " and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter+") a where percent>15   union "

                        +"select 0 as over_weight, count(*) as normal_weight from (select (((dm.weight_number-gdm.dm_kg)/gdm.dm_kg)*100) as percent  from  " + TABLE_DM +" dm,"+TABLE_DMGOAL+" gdm "+  " where " +
                        " substr(g_time,1,2)>=12 and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"')" +
                        " and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter+") a where percent<15) b ";


            /*    buildSQL = "select * from  " + TABLE_DM + " where  " +
                        "  substr(g_time,1,2)>=12 and g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"')" +
                        " and member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;*///and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";
            } else {
              /*  buildSQL = "select * from  " + TABLE_DM + " where    substr(g_time,1,2)<12 and" +
                        " g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and" +
                        " member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter;*///and bp_date<=date('2015-09-01')" ;// and bp_date<=date('now','-30 day') and bp_date>=date('now')";


                buildSQL = "select sum(over_weight) as over_weight,sum(normal_weight) as normal_weight from (select count(*) as over_weight, 0 as normal_weight from (select (((dm.weight_number-gdm.dm_kg)/gdm.dm_kg)*100) as percent  from  " + TABLE_DM +" dm,"+TABLE_DMGOAL+" gdm "+  " where dm.member_id= gdm.goalmember_id and dm.relation_id=gdm.goalrelationship_ID and " +
                        "  substr(g_time,1,2)<12 and" +
                        " g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and" +
                        " member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter+") a where percent>15   union "

                        +"select 0 as over_weight, count(*) as normal_weight from (select (((dm.weight_number-gdm.dm_kg)/gdm.dm_kg)*100) as percent  from  " + TABLE_DM +" dm,"+TABLE_DMGOAL+" gdm "+  " where dm.member_id= gdm.goalmember_id and dm.relation_id=gdm.goalrelationship_ID and " +
                        "  substr(g_time,1,2)<12 and" +
                        " g_date between datetime('"+toDate+"','" + Day + "') and datetime('"+toDate+"') and" +
                        " member_id =" + MemberId + "  and relation_id=" + RelationshipId+" "+last_entry_filter+") a where percent<15) b ";


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

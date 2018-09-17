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
public class SqliteWMHandler extends SQLiteOpenHelper {

    private static final String TAG = "SqliteWMHandler";

    private static final int DATABASE_VERSION = 1;
    private static SqliteWMHandler sSqlite_obj;
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
    //table for water monitor

    private static final String TABLE_WM_ENTRIES = "wm_entries";
    // water monitor Columns names
    private static final String WM_ID = "id";
    private static final String WM_MEMBER_ID = "member_id";
    private static final String WM_DATE = "created_date";
    private static final String WM_QUANTITY = "quantity";
    private static final String WM_RELATIONSHIP_ID = "Relationship_ID";
    private static final String WM_UUID = "UUID";
    private static final String WM_IMEI = "IMEI";
    private static final String WM_WATER_DRINK_PERCNT = "water_drink_percent";


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


    public SqliteWMHandler(Context context) {
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

    public static synchronized SqliteWMHandler getInstance(Context context) {
        if (sSqlite_obj == null) {
            sSqlite_obj = new SqliteWMHandler(context.getApplicationContext());
        }
        return sSqlite_obj;
    }


    public void delete_all_data()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WM_SETTINGS, null, null);
        db.delete(TABLE_WM_ENTRIES, null, null);
        db.close();
    }




    public Long addWMSetting(Integer member_id, String uu_id, Integer goal, String DateEntries, String Imei, Integer Relashionship_Id, Integer GlassSize
    ) {
        long id=0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(WM_SETTMEMBER_ID, member_id); // Name
            values.put(WM_MAIN_UUID, uu_id); // Name
            values.put(WM_GOAL_SET, goal); // Name
            values.put(WM_MAIN_DATE, DateEntries); // Name
            values.put(WM_IMEI_MAIN, Imei); // Name
            values.put(WM_SETT_RELATIONSHIP_ID, Relashionship_Id);
            values.put(WM_SETT_GLASS_SIZE, GlassSize.toString());
            // Inserting Row
           id = db.insertOrThrow(TABLE_WM_SETTINGS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error addWMSetting");
        } finally {
            db.endTransaction();
        }
        return id;
    }

    public void addWMEntries(Integer member_id, String date, String quantity, Integer Member_Relationship_ID, String uuid, String imei, String DrinkPercnt
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {


            ContentValues values = new ContentValues();
            // Name
            values.put(WM_MEMBER_ID, member_id); // Name
            values.put(WM_DATE, date); // Name
            values.put(WM_QUANTITY, quantity); // Name
            values.put(WM_RELATIONSHIP_ID, Member_Relationship_ID);
            values.put(WM_UUID, uuid);
            values.put(WM_IMEI, imei);
            values.put(WM_WATER_DRINK_PERCNT, DrinkPercnt);

            long id = db.insertOrThrow(TABLE_WM_ENTRIES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error addWMEntries");
        } finally {
            db.endTransaction();
        }
    }


    public void delete_entries(String mid) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.execSQL("DELETE FROM " + TABLE_WM_ENTRIES + " WHERE  id=" + mid); //delete  row in a table with the condition

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Deleted delete_entries");
        } finally {
            db.endTransaction();
        }

    }

    public String getTotalDrink_quantity(String toDate, Integer RelationShipid) {

        //String countQuery = "SELECT sum(quantity) as total  FROM  " + TABLE_WM_ENTRIES ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT   sum(quantity) as total FROM wm_entries where created_date=? AND Relationship_ID=? ", new String[]{String.valueOf(toDate), String.valueOf(RelationShipid)});

        //Cursor cursor = db.rawQuery(countQuery, null);
        String total = "";
        // cursor.moveToFirst();
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            total = cursor.getString(cursor.getColumnIndex("total"));
        }

        cursor.close();

        return total;
    }

    public Cursor    getAllData_Chart(String RelationShipid, String startDate, String endDate) {

        String buildSQL = "SELECT   sum(quantity) as maxqty,created_date FROM " + TABLE_WM_ENTRIES + "  where Relationship_ID =  " + RelationShipid + "  and  created_date between  '" + startDate + "' and  '" + endDate + "'  group by created_date";/*, new String[]{String.valueOf(toDate), String.valueOf(RelationShipid)}*/

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);

    }

    public Cursor getAllDrink_goal_Data_Chart(String RelationShipid, String startDate, String endDate) {

        String buildSQL = "SELECT   sum(Goal_set) as maxqty  FROM " + TABLE_WM_SETTINGS + "  where Relationship_ID =  " + RelationShipid + "  and  created_date between  '" + startDate + "' and  '" + endDate + "'";/*, new String[]{String.valueOf(toDate), String.valueOf(RelationShipid)}*/

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);
    }


    public int getRowCountofWM_Setting_table(Integer ReletionShipId) {
        String countQuery = "SELECT  * FROM " + TABLE_WM_SETTINGS + " Where Relationship_ID = " + ReletionShipId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();

        // return row count
        return rowCount;
    }

    public int getDrinkCountofWM_Entries(String wmDate, Integer RelationShipId) {
        //String countQuery = "SELECT  * FROM " + TABLE_WM_SETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM wm_entries where created_date=? AND Relationship_ID=?", new String[]{String.valueOf(wmDate), String.valueOf(RelationShipId)});

        // Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();

        // return row count
        return rowCount;
    }


    public Cursor getDateUuidWMSetting(String date, Integer relationShipId) {


        //String buildSQL = "SELECT  * FROM " + TABLE_WM_SETTINGS + " Where created_date = " +date ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur3 = db.rawQuery("SELECT  * FROM wm_setting where created_date=? AND Relationship_ID=? ", new String[]{String.valueOf(date), String.valueOf(relationShipId)});


        Log.d(TAG, "getAllData SQL: " + cur3);

        return cur3;
    }

    public String getMAxIdWMSetting(Integer relationShipId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT max(wm_id) as _id  FROM  wm_setting where Relationship_ID=? ", new String[]{String.valueOf(relationShipId)});

        String maxqId = "";
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            maxqId = cursor.getString(cursor.getColumnIndex("_id"));
        }
        cursor.close();

        return maxqId;
    }

    public String getMAxIdWMEntries(Integer relationShipId,String date) {
        String maxqId = "";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT max(id) as _id  FROM  wm_entries where Relationship_ID=?  and created_date='"+date+"' ", new String[]{String.valueOf(relationShipId)});

            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                maxqId = cursor.getString(cursor.getColumnIndex("_id"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Error getMAxIdWMEntries");

        }
        return maxqId;
    }

    public Integer getLastSetGoal(Integer Rel_id,String dateup) {
        Integer goal = 1500;
        try {

            String countQuery = "SELECT Goal_set  FROM   " + TABLE_WM_SETTINGS + " Where Relationship_ID = " + Rel_id+" and created_date = '" + dateup+"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                goal = cursor.getInt(cursor.getColumnIndex("Goal_set"));
            }

            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Error getMAxIdWMEntries");

        }
        return goal;
    }

    public Integer getLastSetGoal(Integer Rel_id) {
        Integer goal = 1500;
        try {

            String countQuery = "SELECT Goal_set  FROM   " + TABLE_WM_SETTINGS + " Where Relationship_ID = " + Rel_id+"  order by created_date desc ";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            if (cursor.getCount()>0) {
                cursor.moveToFirst();
                goal = cursor.getInt(cursor.getColumnIndex("Goal_set"));
            }

            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Error getMAxIdWMEntries");

        }
        return goal;
    }

    public Integer getLastSetGoalonDate(Integer Rel_id, String dateup) {
        Integer goal = 1500;
        try {

            String countQuery = "SELECT Goal_set  FROM   " + TABLE_WM_SETTINGS + " Where Relationship_ID = " + Rel_id+" and created_date = '" + dateup+"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                goal = cursor.getInt(cursor.getColumnIndex("Goal_set"));
            }

            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Error getMAxIdWMEntries");

        }
        return goal;
    }

    public Integer getdrink_count(Integer Rel_id, String dateup) {
        Integer count= 0;
        try {


            String countQuery = "SELECT quantity  FROM   " + TABLE_WM_ENTRIES + " Where Relationship_ID = " + Rel_id+" and created_date = '" + dateup+"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            count=cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Error getMAxIdWMEntries");

        }
     return count;
    }


    public void InsertSyncTable(String I_Type, String Controller, String Parameter, String JsonObject,
                                String Created_Date, String UUID, String iUpload_Download, Integer MemberId,
                                String Module_Name, String Mode, String ControllerName, String METHODName) {
        SQLiteDatabase db = this.getWritableDatabase();

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
            long SyncId = db.insertOrThrow(TABLE_Medikart_SYNC, null, values);


            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error InsertSyncTable");
        } finally {
            db.endTransaction();
        }
    }

    public void update_percentage(String percent, String dateup, Integer RelationShipId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(WM_WATER_DRINK_PERCNT, percent);
            db.update(TABLE_WM_ENTRIES, values, "created_date = " + dateup + " and Relationship_ID= " + RelationShipId, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error EditSaveBPDetails");
        } finally {
            db.endTransaction();
        }

    }

    public Integer getLastSetGlassSize(Integer Rel_id, String date) {
        Integer goal = 250;
        try {
            String countQuery = "SELECT glass_size  FROM   " + TABLE_WM_SETTINGS + " Where Relationship_ID = " + Rel_id+"  and created_date='"+date+"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                goal = Integer.parseInt(cursor.getString(cursor.getColumnIndex("glass_size")));
            }
            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Error getLastSetGlassSize");
        }
        return goal;
    }

    public void update_GlassSize(Integer Glasssize, String dateup, Integer RelationShipId) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(WM_SETT_GLASS_SIZE, Glasssize.toString());
            long id= db.update(TABLE_WM_SETTINGS, values, " created_date = '" + dateup + "' and Relationship_ID= " + RelationShipId, null);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error update_GlassSize");
        } finally {
            db.endTransaction();
        }
    }

    public void update_Goal(Integer Goal, String dateup, Integer RelationShipId) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(WM_GOAL_SET, Goal);
            long id= db.update(TABLE_WM_SETTINGS, values, " created_date = '" + dateup + "' and Relationship_ID= " + RelationShipId, null);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error update_GlassSize");
        } finally {
            db.endTransaction();
        }
    }

    public String check_water_entry(Integer RelationShipId) {
        String rowCount = "0";
        try{
        String countQuery = "SELECT count(*) as count_row FROM " + TABLE_WM_SETTINGS+ " where Relationship_ID= " + RelationShipId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            rowCount = cursor.getString(0);
        }
        cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Error getLastSetGlassSize");
        }
        return rowCount;
    }
    public void delete_wm_settings()
    {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.delete ( TABLE_WM_SETTINGS,null, null );

        } catch (Exception e) {
            Log.d(TAG, "Error TABLE_WM_SETTINGS");
        } finally {

        }

    }

    public void delete_wm_entries()
    {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.delete ( TABLE_WM_ENTRIES,null, null );

        } catch (Exception e) {
            Log.d(TAG, "Error delete_wm_entries");
        } finally {

        }

    }
    public Cursor getAllData_Chart_Static(String RelationShipid, String startDate, String endDate) {

        String buildSQL = "SELECT max(maxqty) as max_qty,min(maxqty) as min_qty,avg(maxqty) as avg_qty FROM ( SELECT   sum(quantity) as maxqty,created_date FROM " + TABLE_WM_ENTRIES + "  where Relationship_ID =  " + RelationShipid + "  and  created_date between  '" + startDate + "' and  '" + endDate + "'  group by created_date ) a";/*, new String[]{String.valueOf(toDate), String.valueOf(RelationShipid)}*/

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);

    }




    public Cursor getAllData_Chart_log(String RelationShipid, String startDate, String endDate) {

        String buildSQL = "select a.created_date,a.member_id, a.totalqty, s.goal_set as goal_set from (SELECT created_date,member_id,sum(quantity) AS totalqty FROM " + TABLE_WM_ENTRIES + "  where Relationship_ID =  " + RelationShipid + "  and  created_date between  '" + startDate + "' and  '" + endDate + "'  GROUP BY created_date, member_id ) a, wm_setting s where a.created_date=s.created_date and a.member_id=s.member_id";/*, new String[]{String.valueOf(toDate), String.valueOf(RelationShipid)}*/

        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);

    }


    public Cursor getAllData_Chart_log_for_pie_chart(String RelationShipid, String startDate, String endDate) {

     /*   String buildSQL = "select a.created_date,a.member_id, (( a.totalqty/s.goal_set)*100) as achieved_percent from " +
                "(SELECT created_date,member_id,sum(quantity) AS totalqty FROM " + TABLE_WM_ENTRIES + "  where " +
                "Relationship_ID =  " + RelationShipid + "  and  created_date between  '" + startDate + "' and  '"
                + endDate + "'  GROUP BY created_date, member_id ) a, wm_setting s where a.created_date=s.created_date " +
                "and a.member_id=s.member_id";*//*, new String[]{String.valueOf(toDate), String.valueOf(RelationShipid)}*//*

*/

        String buildSQL =

                "select  sum(not_achieved) as not_achieved , sum(Normal) as Normal, sum(overdrinking) as overdrinking from"+

                " (select   count (*) as not_achieved, 0 as Normal, 0 as overdrinking  from( select a.created_date,a.member_id, (( a.totalqty/s.goal_set)*100) as achieved_percent from " +
                "(SELECT created_date,member_id,sum(quantity) AS totalqty FROM " + TABLE_WM_ENTRIES + "  where " +
                "Relationship_ID =  " + RelationShipid + "  and  created_date between  '" + startDate + "' and  '"
                + endDate + "'  GROUP BY created_date, member_id ) a, wm_setting s where a.created_date=s.created_date " +
                "and a.member_id=s.member_id) where achieved_percent<100"+

        " union  select   0 as not_achieved, count (*) as Normal,  0 as overdrinking  from(select a.created_date,a.member_id, (( a.totalqty/s.goal_set)*100) as achieved_percent from " +
                "(SELECT created_date,member_id,sum(quantity) AS totalqty FROM " + TABLE_WM_ENTRIES + "  where " +
                "Relationship_ID =  " + RelationShipid + "  and  created_date between  '" + startDate + "' and  '"
                + endDate + "'  GROUP BY created_date, member_id ) a, wm_setting s where a.created_date=s.created_date " +
                "and a.member_id=s.member_id) where achieved_percent>=100 and achieved_percent<=124"+

                " union select   0 as not_achieved , 0 as Normal, count (*) as overdrinking  from(select a.created_date,a.member_id, (( a.totalqty/s.goal_set)*100) as achieved_percent from " +
                "(SELECT created_date,member_id,sum(quantity) AS totalqty FROM " + TABLE_WM_ENTRIES + "  where " +
                "Relationship_ID =  " + RelationShipid + "  and  created_date between  '" + startDate + "' and  '"
                + endDate + "'  GROUP BY created_date, member_id ) a, wm_setting s where a.created_date=s.created_date " +
                "and a.member_id=s.member_id) where achieved_percent>=125)";



        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "med_master SQL: " + buildSQL);

        return db.rawQuery(buildSQL, null);

    }

}

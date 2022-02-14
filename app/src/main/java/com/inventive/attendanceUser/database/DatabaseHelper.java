package com.inventive.attendanceUser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.inventive.attendanceUser.model.dailyreportmodel;

import java.util.ArrayList;

/**
 * Created by Vrushali Chavan on 16-04-2021.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Constants for Database name, table name, and column names
    public static final String DB_NAME = "attendance";
    public static final String TABLE_NAME = "attendencelog";
    public static final String COLUMN_ID = "id";
    public static final String EMPID = "empid";
    public static final String ENTRYDATE = "entrydate";
    public static final String EXITDATE = "exitdate";
    public static final String INTIME = "in_time";
    public static final String INGRACE = "in_grace";
    public static final String LUNCHOUT = "lunchout_time";
    public static final String LUNCHOUTGRACE = "lunchout_grace";
    public static final String LUNCHIN = "lunchin_time";
    public static final String LUNCHINGRACE = "lunchin_grace";
    public static final String OUTTIME = "out_time";
    public static final String OUTGRACE = "out_grace";
    public static final String TOTALHRS = "total_hours";
    public static final String COLUMN_STATUS = "status";
    public static final String TYPENO = "type_no";
    public static final String ATTSHIFTID = "attshiftid";
    public static final String NEWATTSHIFTID = "newattshiftid";

    public static final String ATTENDANCETEMPTABLE_NAME = "attendance_log_temp";
    public static final String TIME = "time";
    public static final String TYPE = "type";
    public static final String TEMPLOGID = "templogid";
    public static final String DATE = "date";

    public static final String EMPTABLE_NAME = "employeefaces";
    public static final String FACEID = "faceid";
    public static final String ETLFID = "tlfid";
    public static final String ENAME = "name";
    public static final String ETITLE = "title";
    public static final String EDISTANCE = "distance";
    public static final String EFLEFT = "fleft";
    public static final String EFRIGHT = "fright";
    public static final String EFTOP = "ftop";
    public static final String EFBOTTOM = "fbottom";
    public static final String EIMAGECROP = "imagecrop";
    public static final String EXTRAS = "extras";

    public static final String EMPINFOTABLE_NAME = "employeeinfo";
    public static final String ADDRESS = "address";
    public static final String COUNTRY = "country";
    public static final String STATE = "state";
    public static final String CITY = "city";
    public static final String EMAIL = "email";
    public static final String MOBNO = "mobno";
    public static final String STATUS = "status";
    public static final String USERID = "userid";
    public static final String EMPENTRYDATE = "entrydate";
    public static final String PANNO = "panno";
    public static final String PHOTO = "photo";
    public static final String DOB = "dob";
    public static final String DESIGNATION = "designation";
    public static final String SHIFTTYPE = "shifttype";
    public static final String JOBTYPE = "jobtype";
    public static final String ORGLOCATION = "orglocation";

    public static final String SHIFTTYPETABLE_NAME = "shifttype";

    public static final String SHIFTTABLE_NAME = "shift";
    public static final String SHIFTID = "shiftid";
    public static final String SHIFTTYPEID = "shifttypeid";
    public static final String SHIFTNAME = "shifttype";
    public static final String SHIFTIN = "intime";
    public static final String SHIFTINGRACE = "ingrace";
    public static final String SHIFTOUT = "outtime";
    public static final String SHIFTOUTGRACE = "outgrace";
    public static final String SHIFTLIN = "lunchin";
    public static final String SHIFTLINGRACE = "lunchingrace";
    public static final String SHIFTLOUT = "lunchout";
    public static final String SHIFTLOUTGRACE = "lunchoutgrace";
    public static final String ADMININGRACE = "adminingrace";
    public static final String ADMINLOGRACE = "adminlunchoutgrace";
    public static final String INDISPLAYBEFORE = "indisplaybefore";
    public static final String INDISPLAYAFTER = "indisplayafter";
    public static final String OUTDISPLAYBEFORE = "outdisplaybefore";
    public static final String OUTDISPLAYAFTER = "outdisplayafter";
    public static final String LINDISPLAYBEFORE = "lindisplaybefore";
    public static final String LINDISPLAYAFTER = "lindisplayafter";
    public static final String LOUTDISPLAYBEFORE = "loutdisplaybefore";
    public static final String LOUTDISPLAYAFTER = "loutdisplayafter";

    public static final String JOBTYPETABLE_NAME = "jobtype";
    public static final String JOBTYPEID = "jobid";
    public static final String JOBTYPENAME = "jobname";

    public static final String DESIGNATIONTABLE_NAME = "designation";
    public static final String DESIGNATIONID = "designationid";
    public static final String DESIGNATIONNAME = "designationname";

    public static final String LocationTABLE_NAME = "location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    //database version
    private static final int DB_VERSION = 1;

    String sql = "CREATE TABLE " + TABLE_NAME
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMPID +
            " VARCHAR, "  + ENTRYDATE +
            " VARCHAR, " + EXITDATE +
            " VARCHAR, " + INTIME +
            " VARCHAR, " + INGRACE +
            " VARCHAR, " + LUNCHOUT +
            " VARCHAR, " + LUNCHOUTGRACE +
            " VARCHAR, " + LUNCHIN +
            " VARCHAR, " + LUNCHINGRACE +
            " VARCHAR, " + OUTTIME +
            " VARCHAR, " + OUTGRACE +
            " VARCHAR, " + TOTALHRS +
            " VARCHAR, " + TYPENO +
            " VARCHAR, " + ATTSHIFTID +
            " VARCHAR, " + NEWATTSHIFTID +
            " VARCHAR, " + COLUMN_STATUS +
            " VARCHAR);";

    String CREATEATTTEMP = "CREATE TABLE " + ATTENDANCETEMPTABLE_NAME
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + TEMPLOGID +
            " VARCHAR, "  + EMPID +
            " VARCHAR, " + DATE +
            " VARCHAR, " + TIME +
            " VARCHAR, " + TYPE +
            " VARCHAR, " + COLUMN_STATUS +
            " VARCHAR);";

    String CREATEATTLOCAION = "CREATE TABLE " + LocationTABLE_NAME
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE +
            " VARCHAR, "  + EMPID +
            " VARCHAR, " + LATITUDE +
            " VARCHAR, " + LONGITUDE +
            " VARCHAR, " + COLUMN_STATUS +
            " VARCHAR);";


    String CREATETABLEEMP = "CREATE TABLE " + EMPTABLE_NAME
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMPID +
            " VARCHAR, "  + FACEID +
            " VARCHAR, "  + ETLFID +
            " VARCHAR, " + ENAME +
            " VARCHAR, " + ETITLE +
            " VARCHAR, " + EDISTANCE +
            " VARCHAR, " + EFLEFT +
            " VARCHAR, " + EFRIGHT +
            " VARCHAR, " + EFTOP +
            " VARCHAR, " + EFBOTTOM +
            " VARCHAR, " + EIMAGECROP +
            " VARCHAR, " + EXTRAS +
            " VARCHAR);";

    String CREATETABLEEMPINFO = "CREATE TABLE " + EMPINFOTABLE_NAME
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMPID +
            " VARCHAR, "  + ENAME +
            " VARCHAR, " + ADDRESS +
            " VARCHAR, " + COUNTRY +
            " VARCHAR, " + STATE +
            " VARCHAR, " + CITY +
            " VARCHAR, " + EMAIL +
            " VARCHAR, " + MOBNO +
            " VARCHAR, " + STATUS +
            " VARCHAR, " + USERID +
            " VARCHAR, " + EMPENTRYDATE +
            " VARCHAR, " + PANNO +
            " VARCHAR, " + PHOTO +
            " VARCHAR, " + DOB +
            " VARCHAR, " + DESIGNATION +
            " VARCHAR, " + SHIFTTYPE +
            " VARCHAR, " + JOBTYPE +
            " VARCHAR, " + ORGLOCATION +
            " VARCHAR);";

    String CREATETABLESHIFTTYPE = "CREATE TABLE " + SHIFTTYPETABLE_NAME
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + SHIFTID +
            " VARCHAR, "  + SHIFTNAME +
            " VARCHAR);";

    String CREATETABLESHIPT = "CREATE TABLE " + SHIFTTABLE_NAME
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + SHIFTID +
            " VARCHAR, "  + SHIFTTYPEID +
            " VARCHAR, "  + SHIFTNAME +
            " VARCHAR, " + SHIFTIN +
            " VARCHAR, " + SHIFTINGRACE +
            " VARCHAR, " + SHIFTOUT +
            " VARCHAR, " + SHIFTOUTGRACE +
            " VARCHAR, " + SHIFTLIN +
            " VARCHAR, " + SHIFTLINGRACE +
            " VARCHAR, " + SHIFTLOUT +
            " VARCHAR, " + SHIFTLOUTGRACE +
            " VARCHAR, " + ADMININGRACE +
            " VARCHAR, " + ADMINLOGRACE +
            " VARCHAR, " + INDISPLAYBEFORE +
            " VARCHAR, " + INDISPLAYAFTER +
            " VARCHAR, " + OUTDISPLAYBEFORE +
            " VARCHAR, " + OUTDISPLAYAFTER +
            " VARCHAR, " + LINDISPLAYBEFORE +
            " VARCHAR, " + LINDISPLAYAFTER +
            " VARCHAR, " + LOUTDISPLAYBEFORE +
            " VARCHAR, " + LOUTDISPLAYAFTER +
            " VARCHAR);";

    String CREATETABLEJOBTYPE = "CREATE TABLE " + JOBTYPETABLE_NAME
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + JOBTYPEID +
            " VARCHAR, "  + JOBTYPENAME +
            " VARCHAR);";

    String CREATETABLEDESIGNATION = "CREATE TABLE " + DESIGNATIONTABLE_NAME
            + "(" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + DESIGNATIONID +
            " VARCHAR, "  + DESIGNATIONNAME +
            " VARCHAR);";

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sql);
        db.execSQL(CREATETABLEEMP);
        db.execSQL(CREATEATTTEMP);
        db.execSQL(CREATETABLEEMPINFO);
        db.execSQL(CREATETABLESHIPT);
        db.execSQL(CREATETABLEJOBTYPE);
        db.execSQL(CREATETABLEDESIGNATION);
        db.execSQL(CREATETABLESHIFTTYPE);
        db.execSQL(CREATEATTLOCAION);
    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS "+TABLE_NAME;
        String sql1 = "DROP TABLE IF EXISTS "+EMPTABLE_NAME;
        String sql2 = "DROP TABLE IF EXISTS "+EMPINFOTABLE_NAME;
        String sql3 = "DROP TABLE IF EXISTS "+SHIFTTABLE_NAME;
        String sql4 = "DROP TABLE IF EXISTS "+JOBTYPETABLE_NAME;
        String sql5 = "DROP TABLE IF EXISTS "+DESIGNATIONTABLE_NAME;
        String sql6 = "DROP TABLE IF EXISTS "+ATTENDANCETEMPTABLE_NAME;
        String sql7 = "DROP TABLE IF EXISTS "+SHIFTTYPETABLE_NAME;
        String sql8 = "DROP TABLE IF EXISTS "+LocationTABLE_NAME;

        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql6);
        db.execSQL(sql7);
        db.execSQL(sql8);

        onCreate(db);
    }


    public boolean addLog(String empid, String entrydate, String exitdate, String time, String grace, String lunchout, String lograce, String lunchin, String ligrace, String out, String outgrace, String totalhrs, String empshiftno, String empshifttype, String updatestatus) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(EMPID, empid);
            contentValues.put(ENTRYDATE, entrydate);
            contentValues.put(EXITDATE, exitdate);
            contentValues.put(INTIME, time);
            contentValues.put(INGRACE, grace);
            contentValues.put(LUNCHOUT, lunchout);
            contentValues.put(LUNCHOUTGRACE, lograce);
            contentValues.put(LUNCHIN, lunchin);
            contentValues.put(LUNCHINGRACE,ligrace);
            contentValues.put(OUTTIME, out);
            contentValues.put(OUTGRACE, outgrace);
            contentValues.put(TOTALHRS, totalhrs);
            contentValues.put(TYPENO, empshiftno);
            contentValues.put(ATTSHIFTID, empshifttype);
            contentValues.put(NEWATTSHIFTID, empshifttype);
            contentValues.put(COLUMN_STATUS, updatestatus);

            db.insert(TABLE_NAME, null, contentValues);
//            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    public boolean addTempLog(String empid, String date, String time, String s) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(EMPID, empid);
            contentValues.put(DATE, date);
            contentValues.put(TIME, time);
            contentValues.put(TYPE, s);
            contentValues.put(COLUMN_STATUS, "0");

            db.insert(ATTENDANCETEMPTABLE_NAME, null, contentValues);
            //db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean addEmployeeFaces(String id,String empid, String tlfid, String name, String title, String distance, String fleft, String fright, String ftop, String fbottom, String imagecrop, String extras) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FACEID, id);
        contentValues.put(EMPID, empid);
        contentValues.put(ETLFID, tlfid);
        contentValues.put(ENAME, name);
        contentValues.put(ETITLE, title);
        contentValues.put(EDISTANCE, distance);
        contentValues.put(EFLEFT, fleft);
        contentValues.put(EFRIGHT, fright);
        contentValues.put(EFTOP, ftop);
        contentValues.put(EFBOTTOM, fbottom);
        contentValues.put(EIMAGECROP, imagecrop);
        contentValues.put(EXTRAS, extras);

        db.insert(EMPTABLE_NAME, null, contentValues);
        //db.close();
        return true;
    }

    public boolean updateEmployeeFaces(String id,String empid, String tlfid, String name, String title, String distance, String fleft, String fright, String ftop, String fbottom, String imagecrop, String extras) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(EMPID, empid);
        contentValues.put(ETLFID, tlfid);
        contentValues.put(ENAME, name);
        contentValues.put(ETITLE, title);
        contentValues.put(EDISTANCE, distance);
        contentValues.put(EFLEFT, fleft);
        contentValues.put(EFRIGHT, fright);
        contentValues.put(EFTOP, ftop);
        contentValues.put(EFBOTTOM, fbottom);
        contentValues.put(EIMAGECROP, imagecrop);
        contentValues.put(EXTRAS, extras);

        db.update(EMPTABLE_NAME, contentValues,FACEID + "=" + id, null);
        //db.close();
        return true;

    }

    /*
     * This method taking two arguments
     * first one is the id of the name for which
     * we have to update the sync status
     * and the second one is the status that will be changed
     * */
    public boolean updateNameStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
        //db.close();
        return true;
    }

    /*
     * this method will give us all the name stored in sqlite
     * */
    public Cursor getNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getEmployeefaces() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + EMPTABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getJobtype() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + JOBTYPETABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getAttendanceLog() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "+COLUMN_STATUS+" = '0' ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getLocationLog() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + LocationTABLE_NAME + " WHERE "+COLUMN_STATUS+" = '0' ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getAttendanceTempLog() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + ATTENDANCETEMPTABLE_NAME + " WHERE "+COLUMN_STATUS+" = '0' ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getAttendanceTempSameLog(String empid, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + ATTENDANCETEMPTABLE_NAME + " WHERE "+EMPID+" = '"+empid+"' AND " + DATE + " = '"+date+"' AND " + TIME + " = '"+time+"';";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getShifttype() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + SHIFTTABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getShifttypen(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + SHIFTTABLE_NAME + " WHERE "+SHIFTTYPEID+" = '"+type+"' ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getdesignation() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DESIGNATIONTABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getEmployeeInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + EMPINFOTABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    /*
     * this method is for getting all the unsynced name
     * so that we can sync it with database
     * */
    public Cursor getUnsyncedNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public boolean addDesignation(String id, String designation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DESIGNATIONID, id);
        contentValues.put(DESIGNATIONNAME, designation);

        db.insert(DESIGNATIONTABLE_NAME, null, contentValues);
        //db.close();
        return true;
    }

    public boolean addLocation(String latitude, String longitude, String empid, String todaydate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DATE, todaydate);
        contentValues.put(EMPID, empid);
        contentValues.put(LATITUDE, latitude);
        contentValues.put(LONGITUDE, longitude);

        db.insert(LocationTABLE_NAME, null, contentValues);
        //db.close();
        return true;
    }

    public boolean addShift(String id, String shift_typeid, String shift_type, String in_time, String in_grace, String out_time, String out_grace, String lunch_in, String lunch_in_grace, String lunch_out, String lunch_out_grace, String adminingrace, String adminlograce, String indisplaybefore, String indisplayafter, String outdisplaybefore, String outdisplayafter, String lindisplaybefore, String lindisplayafter, String loutdisplaybefore, String loutdisplayafter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SHIFTID, id);
        contentValues.put(SHIFTTYPEID, shift_typeid);
        contentValues.put(SHIFTTYPE, shift_type);
        contentValues.put(SHIFTIN, in_time);
        contentValues.put(SHIFTINGRACE, in_grace);
        contentValues.put(SHIFTOUT, out_time);
        contentValues.put(SHIFTOUTGRACE, out_grace);
        contentValues.put(SHIFTLIN, lunch_in);
        contentValues.put(SHIFTLINGRACE, lunch_in_grace);
        contentValues.put(SHIFTLOUT, lunch_out);
        contentValues.put(SHIFTLOUTGRACE, lunch_out_grace);
        contentValues.put(ADMININGRACE, adminingrace);
        contentValues.put(ADMINLOGRACE, adminlograce);
        contentValues.put(INDISPLAYBEFORE, indisplaybefore);
        contentValues.put(INDISPLAYAFTER, indisplayafter);
        contentValues.put(OUTDISPLAYBEFORE, outdisplaybefore);
        contentValues.put(OUTDISPLAYAFTER, outdisplayafter);
        contentValues.put(LINDISPLAYBEFORE, lindisplaybefore);
        contentValues.put(LINDISPLAYAFTER, lindisplayafter);
        contentValues.put(LOUTDISPLAYBEFORE, loutdisplaybefore);
        contentValues.put(LOUTDISPLAYAFTER, loutdisplayafter);

        db.insert(SHIFTTABLE_NAME, null, contentValues);
        //db.close();
        return true;
    }

    public boolean addShiftTYPE(String id, String shift_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SHIFTID, id);
        contentValues.put(SHIFTNAME, shift_type);

        db.insert(SHIFTTYPETABLE_NAME, null, contentValues);
        //db.close();
        return true;
    }

    public boolean addJobtype(String id, String job_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(JOBTYPEID, id);
        contentValues.put(JOBTYPENAME, job_type);

        db.insert(JOBTYPETABLE_NAME, null, contentValues);
        //db.close();
        return true;
    }

    public boolean addEmployeeInfo(String id, String registerdate, String name, String address, String email, String mobno, String status, String panno, String emp_thumb, String dob, String designation, String shift_type, String job_type) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(EMPID, id);
        contentValues.put(ENAME, name);
        contentValues.put(ADDRESS, address);
        contentValues.put(COUNTRY, "");
        contentValues.put(STATE, "");
        contentValues.put(CITY, "");
        contentValues.put(EMAIL, email);
        contentValues.put(MOBNO, mobno);
        contentValues.put(STATUS, status);
        contentValues.put(USERID, "");
        contentValues.put(EMPENTRYDATE, registerdate);
        contentValues.put(PANNO, panno);
        contentValues.put(PHOTO, emp_thumb);
        contentValues.put(DOB, dob);
        contentValues.put(DESIGNATION, designation);
        contentValues.put(SHIFTTYPE, shift_type);
        contentValues.put(JOBTYPE, job_type);

        db.insert(EMPINFOTABLE_NAME, null, contentValues);
        //db.close();
        return true;

    }


    public boolean updateJobtype(String id, String job_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(JOBTYPENAME, job_type);

        db.update(JOBTYPETABLE_NAME,  contentValues,JOBTYPEID + "=" + id, null);
        //db.close();
        return true;
    }

    public boolean updateShift(String id, String shift_typeid, String shift_type, String in_time, String in_grace, String out_time, String out_grace, String lunch_in, String lunch_in_grace, String lunch_out, String lunch_out_grace, String adminingrace, String adminlograce, String indisplaybefore, String indisplayafter, String outdisplaybefore, String outdisplayafter, String lindisplaybefore, String lindisplayafter, String loutdisplaybefore, String loutdisplayafter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SHIFTTYPEID, shift_typeid);
        contentValues.put(SHIFTTYPE, shift_type);
        contentValues.put(SHIFTIN, in_time);
        contentValues.put(SHIFTINGRACE, in_grace);
        contentValues.put(SHIFTOUT, out_time);
        contentValues.put(SHIFTOUTGRACE, out_grace);
        contentValues.put(SHIFTLIN, lunch_in);
        contentValues.put(SHIFTLINGRACE, lunch_in_grace);
        contentValues.put(SHIFTLOUT, lunch_out);
        contentValues.put(SHIFTLOUTGRACE, lunch_out_grace);
        contentValues.put(ADMININGRACE, adminingrace);
        contentValues.put(ADMINLOGRACE, adminlograce);
        contentValues.put(INDISPLAYBEFORE, indisplaybefore);
        contentValues.put(INDISPLAYAFTER, indisplayafter);
        contentValues.put(OUTDISPLAYBEFORE, outdisplaybefore);
        contentValues.put(OUTDISPLAYAFTER, outdisplayafter);
        contentValues.put(LINDISPLAYBEFORE, lindisplaybefore);
        contentValues.put(LINDISPLAYAFTER, lindisplayafter);
        contentValues.put(LOUTDISPLAYBEFORE, loutdisplaybefore);
        contentValues.put(LOUTDISPLAYAFTER, loutdisplayafter);

        db.update(SHIFTTABLE_NAME, contentValues,SHIFTID + "=" + id, null);
        //db.close();
        return true;
    }

    public boolean updateShiftType(String id, String shift_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SHIFTNAME, shift_type);

        db.update(SHIFTTYPETABLE_NAME, contentValues,SHIFTID + "=" + id, null);
        //db.close();
        return true;
    }

    public boolean updateDesignation(String id, String designation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DESIGNATIONNAME, designation);

        db.update(DESIGNATIONTABLE_NAME, contentValues,DESIGNATIONID + "=" + id, null);
        //db.close();
        return true;
    }

    public boolean updateEmployeeInfo(String id, String registerdate, String name, String address, String email, String mobno, String status, String panno, String emp_thumb, String dob, String designation, String shift_type, String job_type) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ENAME, name);
        contentValues.put(ADDRESS, address);
        contentValues.put(COUNTRY, "");
        contentValues.put(STATE, "");
        contentValues.put(CITY, "");
        contentValues.put(EMAIL, email);
        contentValues.put(MOBNO, mobno);
        contentValues.put(STATUS, status);
        contentValues.put(USERID, "");
        contentValues.put(EMPENTRYDATE, registerdate);
        contentValues.put(PANNO, panno);
        contentValues.put(PHOTO, emp_thumb);
        contentValues.put(DOB, dob);
        contentValues.put(DESIGNATION, designation);
        contentValues.put(SHIFTTYPE, shift_type);
        contentValues.put(JOBTYPE, job_type);

        db.update(EMPINFOTABLE_NAME, contentValues,EMPID + "=" + id, null);
        //db.close();
        return true;

    }

    public Cursor getemployeedetails(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + EMPINFOTABLE_NAME + " WHERE " + EMPID + " = '"+id+"';";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getOuttime(String alin_time, String empshifttype1) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + SHIFTTABLE_NAME + " WHERE " + SHIFTIN + " = '"+alin_time+"' AND " + SHIFTID + " = '"+empshifttype1+"';";
        Cursor c = db.rawQuery(sql, null);
        Log.d("pppppppppppppiii32",sql); //db.close();
        return c;
    }

    public Cursor gettodayspresent(String id,String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String outtime="";
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMPID + " = '"+id+"' AND " + ENTRYDATE + " = '"+date+"' AND " + OUTTIME + " = '"+outtime+"' AND " + INTIME + " != 'Absent';";
        Log.d("pppppppppppppiii32",sql);
        Cursor c = db.rawQuery(sql, null);
        int presentemp=c.getCount();

        Log.d("pppppppppppppiii32",String.valueOf(presentemp));

        //db.close();
        return c;
    }

    public Cursor gettodayspresentNight(String id,String date,String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String outtime="";
        if (time.contains("a") || time.contains("A")){
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMPID + " = '"+id+"' AND " + EXITDATE + " = '"+date+"' AND " + OUTTIME + " = '"+outtime+"' AND " + INTIME + " != 'Absent';";

        }else {
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMPID + " = '"+id+"' AND " + ENTRYDATE + " = '"+date+"' AND " + OUTTIME + " = '"+outtime+"' AND " + INTIME + " != 'Absent';";

        }
//        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMPID + " = '"+id+"' AND " + ENTRYDATE + " = '"+date+"' AND " + OUTTIME + " = '"+outtime+"' AND " + INTIME + " != 'Absent';";
        Log.d("pppppppppppppiii32",sql);
        Cursor c = db.rawQuery(sql, null);
        int presentemp=c.getCount();

        Log.d("pppppppppppppiii32",String.valueOf(presentemp));

        //db.close();
        return c;
    }

    public Cursor gettodayspresentshift(String id,String date,String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String outtime="";

        String sql;
        if (time.contains("a") || time.contains("A")){
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMPID + " = '"+id+"' AND " + EXITDATE + " = '"+date+"' AND " + OUTTIME + " = '"+outtime+"' AND " + INTIME + " != 'Absent';";

        }else {
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMPID + " = '"+id+"' AND " + ENTRYDATE + " = '"+date+"' AND " + OUTTIME + " = '"+outtime+"' AND " + INTIME + " != 'Absent';";

        }

        Log.d("pppppppppppppiiiopop",sql);
        Cursor c = db.rawQuery(sql, null);
        int presentemp=c.getCount();

        Log.d("pppppppppppppiii",String.valueOf(presentemp));

        //db.close();
        return c;
    }

    public Cursor gettodayspresentshiftid(String id, String date, String empshifttype) {
        SQLiteDatabase db = this.getReadableDatabase();
        String outtime="";
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMPID + " = '"+id+"' AND " + ENTRYDATE + " = '"+date+"' AND " + OUTTIME + " != '"+outtime+"' AND " + ATTSHIFTID + " = '"+empshifttype+"' AND " + INTIME + " != 'Absent';";
        Log.d("pppppppppppppiii",sql);
        Cursor c = db.rawQuery(sql, null);
        int presentemp=c.getCount();

        Log.d("pppppppppppppiii",String.valueOf(presentemp));

        //db.close();
        return c;
    }

    public Cursor getavailableshiftid(String id, String date, String empshifttype) {
        SQLiteDatabase db = this.getReadableDatabase();
        String outtime="";
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMPID + " = '"+id+"' AND " + ENTRYDATE + " = '"+date+"' AND " + ATTSHIFTID + " = '"+empshifttype+"'";
        Log.d("pppppppppppppiii",sql);
        Cursor c = db.rawQuery(sql, null);
        int presentemp=c.getCount();

        Log.d("pppppppppppppiii",String.valueOf(presentemp));

        //db.close();
        return c;
    }

    public Cursor gettodaysnightpresent(String id,String date) {

        SQLiteDatabase db = this.getReadableDatabase();
        String outtime="";
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMPID + " = '"+id+"' AND " + OUTTIME + " = '"+outtime+"' AND " + INTIME + " != 'Absent';";
        Cursor c = db.rawQuery(sql, null);
        Log.d("pppppppppppppiii12",sql);
        int presentemp=c.getCount();

        Log.d("pppppppppppppiii12",String.valueOf(presentemp));

        //db.close();
        return c;
    }

    public Cursor getemployeeshift(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + SHIFTTABLE_NAME + " WHERE " + SHIFTID + " = '"+id+"';";
        Cursor c = db.rawQuery(sql, null);
        Log.d("pppppppp",String.valueOf(c.getCount()));
//db.close();
        return c;
    }

    public String getInTimeLateCount(String empid,String date) {
        SQLiteDatabase db = this.getReadableDatabase();
//        SELECT * FROM attendencelog WHERE 1  AND `empid`='87' AND `entrydate` >= '2021-09-01' AND `in_grace` LIKE 'L%'
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMPID + " = '"+empid+"' AND  " + ENTRYDATE + " >= '"+date+"'  AND `in_grace` LIKE 'L%';";
        Cursor c = db.rawQuery(sql, null);
        int count=c.getCount()+1;
        Log.d("pppppppp",String.valueOf(c.getCount()));
//db.close();
        return String.valueOf(count);
    }

    public boolean updateEMPLunchOut(String empid, String date, String time, String grace, String valueOf, String empshifttype) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE `attendencelog` SET `lunchout_time`='"+time+"',`lunchout_grace`='"+grace+"',`total_hours`='"+valueOf+"', `status`='0' WHERE `empid`='"+empid+"' AND entrydate='"+date+"' AND attshiftid='"+empshifttype+"'";
        db.execSQL(sql);
        //db.close();
        Log.d("pppppppp",sql);

//        SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues contentValues = new ContentValues();
//
//            contentValues.put(LUNCHOUT, time);
//            contentValues.put(LUNCHOUTGRACE, grace);
//            contentValues.put(TOTALHRS, valueOf);
//
//            db.update(TABLE_NAME, contentValues,EMPID + "=" + empid+" AND "+ENTRYDATE +"="+date, null);
//            //db.close();
            return true;

    }

    public boolean updateEMPLunchIn(String empid, String date, String time, String grace, String empshifttype) {

        Log.d("pppppppp",empid);
        Log.d("pppppppp",date);
        Log.d("pppppppp",time);
        Log.d("pppppppp",grace);

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE `attendencelog` SET `lunchin_time`='"+time+"',`lunchin_grace`='"+grace+"' , `status`='0' WHERE `empid`='"+empid+"' AND entrydate='"+date+"' AND attshiftid='"+empshifttype+"'";
        db.execSQL(sql);
        //db.close();
        Log.d("pppppppp",sql);
        return true;

    }

    public boolean updateEMPOut(String empid, String date, String time, String grace, String valueOf, String exitdate, String typenoi, String empshifttype, String newempshifttype) {

        Log.d("pppppppp2",empid);
        Log.d("pppppppp2",date);
        Log.d("pppppppp2",time);
        Log.d("pppppppp2",grace);
        Log.d("pppppppp2",valueOf);

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE `attendencelog` SET `exitdate`='"+exitdate+"',`out_time`='"+time+"',`out_grace`='"+grace+"',`total_hours`='"+valueOf+"' ,`type_no`='"+typenoi+"' , `status`='0' ,`newattshiftid`='"+newempshifttype+"' WHERE `empid`='"+empid+"' AND entrydate='"+date+"' AND attshiftid='"+empshifttype+"'";
        db.execSQL(sql);
        //db.close();
        Log.d("pppppppp",sql);

        return true;

    }

    public void updateAttLog(String empid, String date) {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE `attendencelog` SET `status`='1' WHERE `empid`='"+empid+"' AND entrydate='"+date+"'";
        db.execSQL(sql);
        //db.close();
        Log.d("pppppppp",sql);

    }

    public void updateAttTempLog(String empid, String date) {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE `attendance_log_temp` SET `status`='1' WHERE `empid`='"+empid+"' AND date='"+date+"'";
        db.execSQL(sql);
        //db.close();
        Log.d("pppppppp",sql);

    }

    public void updateLocationLog(String sqlid) {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE `attendance_log_temp` SET `status`='1' WHERE `id`='"+sqlid+"'";
        db.execSQL(sql);
        //db.close();
        Log.d("pppppppp",sql);

    }

    public Cursor getAbsentEmployee(String date, String abshiftid) {
        SQLiteDatabase db = this.getReadableDatabase();
//        String sql = "SELECT a.empid FROM employeeinfo AS a WHERE a.empid NOT IN (SELECT empid FROM attendencelog WHERE entrydate = '"+date+"')";
        String sql = "SELECT a.empid FROM employeeinfo AS a WHERE a.empid NOT IN (SELECT empid FROM attendencelog WHERE entrydate = '"+date+"') AND (',' || a.shifttype || ',') LIKE '%,"+abshiftid+",%'";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        Log.d("ppppppppoooiii",sql);
        return c;
    }

    public Cursor getAbsentNightEmployee(String date, String abshiftid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT a.empid FROM employeeinfo AS a WHERE a.empid NOT IN (SELECT empid FROM attendencelog WHERE entrydate = '"+date+"')";
//        String sql = "SELECT a.empid FROM employeeinfo AS a WHERE a.empid NOT IN (SELECT empid FROM attendencelog WHERE entrydate = '"+date+"' AND attshiftid='"+abshiftid+"') AND (',' || a.shifttype || ',') LIKE '%,"+abshiftid+",%'";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        Log.d("ppppppppoooiii",sql);
        return c;
    }

    public Cursor getPresentEmployee(String date,String wherejob) {
        String absent="Absent";
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT A.* FROM " + TABLE_NAME + " as A JOIN "+EMPINFOTABLE_NAME+" as B ON A.empid=B.empid WHERE A." + ENTRYDATE + " = '"+date+"' AND A." + INTIME + " != '"+absent+"' AND A." + COLUMN_STATUS + " = '1'"+wherejob+";";
        Log.d("pppppppp123",sql);

//        SELECT a.empid FROM employeeinfo AS a WHERE a.empid NOT IN (SELECT empid FROM attendencelog WHERE entrydate = '2021-7-27' AND attshiftid='1') AND (',' || a.shifttype || ',') LIKE '%,1,%'
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getnuPresentEmployee(String date,String wherejob) {
        String absent="Absent";
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT A.* FROM " + TABLE_NAME + " as A JOIN "+EMPINFOTABLE_NAME+" as B ON A.empid=B.empid WHERE A." + ENTRYDATE + " = '"+date+"' AND A." + INTIME + " != '"+absent+"' AND A." + COLUMN_STATUS + " = '0'"+wherejob+";";
        Log.d("pppppppp123",sql);
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getNotLoginEmployee(String date,String wherejob) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM employeeinfo AS B WHERE B.empid NOT IN (SELECT empid FROM attendencelog WHERE entrydate = '"+date+"' )"+wherejob;
        Cursor c = db.rawQuery(sql, null);
        Log.d("pppppppp1231",sql);
        Log.d("pppppppp1231",String.valueOf(c.getCount()));
        //db.close();
        return c;
    }

    public Cursor getAbEmployee(String date,String wherejob) {
        String absent="Absent";
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT A.* FROM " + TABLE_NAME + " as A JOIN "+EMPINFOTABLE_NAME+" as B ON A.empid=B.empid WHERE A." + ENTRYDATE + " = '"+date+"' AND A." + INTIME + " = '"+absent+"' "+wherejob+";";
        Log.d("pppppppp123",sql);
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getLateEmployee(String date,String wherejob) {
        String absent="Absent";
        SQLiteDatabase db = this.getReadableDatabase();
        //$sql3="SELECT COUNT(*) as count FROM jos_app_attendance_log as A JOIN jos_app_employeeinfo as B ON A.empid=B.employeeid WHERE 1 AND date(A.entrydate)='$startdate' AND A.in_grace LIKE 'L %'".$wherejob;
        String sql = "SELECT A.* FROM " + TABLE_NAME + " as A JOIN "+EMPINFOTABLE_NAME+" as B ON A.empid=B.empid WHERE A." + ENTRYDATE + " = '"+date+"' AND A." + INGRACE + " LIKE 'L %' "+wherejob+";";
        Log.d("pppppppp123",sql);
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public Cursor getAllAttendanceLog1(String present, String notlogin, String absent, String late, String all, String jobtype, String bdate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "+COLUMN_STATUS+" = '0' ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        //db.close();
        return c;
    }

    public ArrayList<dailyreportmodel> getAllAttendanceLog(String present, String notlogin, String absent1, String late, String all, String jobtype, String date,String wherejob) {
        ArrayList<dailyreportmodel> dailyreportmodelArrayList = new ArrayList();
        // Select All Query
        String absent="Absent";
        String selectQuery = "";

        if(present.equals("present")){
            selectQuery = "SELECT A.*,B.name,B.photo,B.shifttype FROM " + TABLE_NAME + " as A JOIN "+EMPINFOTABLE_NAME+" as B ON A.empid=B.empid WHERE A." + ENTRYDATE + " = '"+date+"' AND A." + INTIME + " != '"+absent+"' AND A." + COLUMN_STATUS + " = '1'"+wherejob+";";
            Log.d("aaaaaaa",selectQuery);
        }else if(present.equals("nupresent")){
            selectQuery = "SELECT A.*,B.name,B.photo,B.shifttype FROM " + TABLE_NAME + " as A JOIN "+EMPINFOTABLE_NAME+" as B ON A.empid=B.empid WHERE A." + ENTRYDATE + " = '"+date+"' AND A." + INTIME + " != '"+absent+"' AND A." + COLUMN_STATUS + " = '0'"+wherejob+";";
            Log.d("aaaaaaa",selectQuery);
        }else  if(!absent1.equals("")){
            selectQuery = "SELECT A.*,B.name,B.photo,B.shifttype FROM " + TABLE_NAME + " as A JOIN "+EMPINFOTABLE_NAME+" as B ON A.empid=B.empid WHERE A." + ENTRYDATE + " = '"+date+"' AND A." + INTIME + " = '"+absent+"' "+wherejob+";";
            Log.d("aaaaaaa1",selectQuery);
        }else if(!notlogin.equals("")){
            selectQuery = "SELECT * FROM employeeinfo AS B WHERE B.empid NOT IN (SELECT empid FROM attendencelog WHERE entrydate = '"+date+"' )"+wherejob;
            Log.d("aaaaaaa2",selectQuery);
        }else if(!late.equals("")){
            selectQuery = "SELECT A.*,B.name,B.photo,B.shifttype FROM " + TABLE_NAME + " as A JOIN "+EMPINFOTABLE_NAME+" as B ON A.empid=B.empid WHERE A." + ENTRYDATE + " = '"+date+"' AND A." + INGRACE + " LIKE 'L %'"+wherejob+";";
            Log.d("aaaaaaa3",selectQuery);
        }else{
            selectQuery = "SELECT A.*,B.name,B.photo,B.shifttype FROM " + TABLE_NAME + " as A JOIN "+EMPINFOTABLE_NAME+" as B ON A.empid=B.empid WHERE A." + ENTRYDATE + " = '"+date+"'"+wherejob+";";
            Log.d("aaaaaaa4",selectQuery);
        }

        int tqty=0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try{
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    dailyreportmodel dailyreportmodel = new dailyreportmodel();

                    dailyreportmodel.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                    dailyreportmodel.setEmpname(cursor.getString(cursor.getColumnIndex(ENAME)));
                    dailyreportmodel.setEmpid(cursor.getString(cursor.getColumnIndex(EMPID)));
                    dailyreportmodel.setDate(cursor.getString(cursor.getColumnIndex(ENTRYDATE)));
                    dailyreportmodel.setEntrydate(cursor.getString(cursor.getColumnIndex(ENTRYDATE)));
                    dailyreportmodel.setImage(cursor.getString(cursor.getColumnIndex(PHOTO)));
                    dailyreportmodel.setUpload_status(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));

                    String sql = "SELECT * FROM " + SHIFTTABLE_NAME + " WHERE " + SHIFTID + " = "+cursor.getString(cursor.getColumnIndex(ATTSHIFTID))+";";
                    Cursor c = db.rawQuery(sql, null);
                    String sin="",sout="",shift="";
                    if (c.moveToFirst()) {
                        do {
                            sin=c.getString(c.getColumnIndex(DatabaseHelper.SHIFTIN));
                            sout=c.getString(c.getColumnIndex(DatabaseHelper.SHIFTOUT));
                        } while (c.moveToNext());
                    }

                    shift=sin+"-"+sout;
                    dailyreportmodel.setShift_time(shift);

                    Log.d("aaaaaaapopo",cursor.getString(cursor.getColumnIndex(OUTTIME)));

                    if(!absent.equals("")){
                        dailyreportmodel.setIn_time(cursor.getString(cursor.getColumnIndex(INTIME)));
                        dailyreportmodel.setLunchout_time("");
                        dailyreportmodel.setLunchin_time("");
                        dailyreportmodel.setOut_time("");

                    }else if(!notlogin.equals("")){
                        dailyreportmodel.setIn_time("notlogin");
                        dailyreportmodel.setLunchout_time("");
                        dailyreportmodel.setLunchin_time("");
                        dailyreportmodel.setOut_time("");

                    }else{
                        String intime="",lunchin="";
                        if (cursor.getString(cursor.getColumnIndex(INGRACE)).contains("L")){
                            intime="L"+cursor.getString(cursor.getColumnIndex(INTIME));
                        }else {
                            intime=cursor.getString(cursor.getColumnIndex(INTIME));
                        }

                        if (cursor.getString(cursor.getColumnIndex(LUNCHINGRACE)).contains("L")){
                            lunchin="L"+cursor.getString(cursor.getColumnIndex(LUNCHIN));
                        }else {
                            lunchin=cursor.getString(cursor.getColumnIndex(LUNCHIN));
                        }
                        dailyreportmodel.setIn_time(intime);
                        dailyreportmodel.setLunchout_time(cursor.getString(cursor.getColumnIndex(LUNCHOUT)));
                        dailyreportmodel.setLunchin_time(lunchin);
                        dailyreportmodel.setTotal_hours(cursor.getString(cursor.getColumnIndex(TOTALHRS)));

                    }

                    dailyreportmodel.setOut_time(cursor.getString(cursor.getColumnIndex(OUTTIME)));
                    dailyreportmodelArrayList.add(dailyreportmodel);
                } while (cursor.moveToNext());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        // return country list
        return dailyreportmodelArrayList;
    }

    public void deleteData() {

        // Delete data before
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME +" WHERE "+ COLUMN_STATUS +"='1' AND "+ ENTRYDATE+" <= date('now','-31 day')";
        db.execSQL(sql);

        String sql1 = "DELETE FROM " + ATTENDANCETEMPTABLE_NAME +" WHERE "+ COLUMN_STATUS +"='1' AND "+ DATE+" <= date('now','-31 day')";
        db.execSQL(sql1);
    }

}

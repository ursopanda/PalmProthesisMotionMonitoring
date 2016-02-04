package rehabdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by esyundyukov on 18/01/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // DB version
    private static final int DATABASE_VERSION = 1;
    // DB name
    private static final String DATABASE_NAME = "rehabDB";

    // sessionData table name
    private static final String TABLE_REHAB_SESSION = "sessionData";

    // sessionData table columns
    private static final String KEY_ID = "id";
    private static final String KEY_MOVEMENT_AMOUNT = "movement_amount";
    private static final String KEY_MAX_ANGLE = "max_angle";
    private static final String KEY_AVERAGE_PERIOD = "average_period";
    private static final String KEY_SESSION_START_TIME = "session_starttime";
    private static final String KEY_SESSION_LENGTH = "session_length";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SESSION_DATA_TABLE = "CREATE TABLE " + TABLE_REHAB_SESSION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SESSION_START_TIME + " INTEGER,"
                + KEY_SESSION_LENGTH + " INTEGER," + KEY_MOVEMENT_AMOUNT + " INTEGER,"
                + KEY_MAX_ANGLE + " INTEGER," + KEY_AVERAGE_PERIOD + " INTEGER" + ")";

        db.execSQL(CREATE_SESSION_DATA_TABLE);
    }

    // Upgrading DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older version of table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REHAB_SESSION);

        // Creating new table
        onCreate(db);
    }

    // CRUD operations
    // Adding new Session
    public void addSessionData(SessionData sessionData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SESSION_START_TIME, sessionData.get_sessionStartTime());
        values.put(KEY_SESSION_LENGTH, sessionData.get_sessionLength());
        values.put(KEY_MOVEMENT_AMOUNT, sessionData.get_movementCount());
        values.put(KEY_MAX_ANGLE, sessionData.get_maxAngle());
        values.put(KEY_AVERAGE_PERIOD, sessionData.get_averagePeriod());

        db.insert(TABLE_REHAB_SESSION, null, values);
        db.close();
    }

    // Getting single session data
    public SessionData getSessionData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REHAB_SESSION, new String[] {KEY_ID, KEY_SESSION_START_TIME, KEY_SESSION_LENGTH,
                KEY_MOVEMENT_AMOUNT, KEY_MAX_ANGLE, KEY_AVERAGE_PERIOD }, KEY_ID + "=?",
                new String[] {String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        SessionData sessionData = new SessionData((Integer.valueOf(cursor.getString(0))), Integer.valueOf(cursor.getString(1)),
                Integer.valueOf(cursor.getString(2)), Integer.valueOf(cursor.getString(3)), Integer.valueOf(cursor.getString(4)),
                Integer.valueOf(cursor.getString(5)));

        cursor.close();

        return sessionData;
    }

    public List<SessionData> getAllSessionData() {
        List<SessionData> sessionDataList = new ArrayList<SessionData>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_REHAB_SESSION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all table rows and adding them to list
        if (cursor.moveToFirst()) {
            do {
                SessionData sessionData = new SessionData();
                sessionData.set_id(Integer.valueOf(cursor.getString(0)));
                sessionData.set_sessionStartTime(Integer.valueOf(cursor.getString(1)));
                sessionData.set_sessionLength(Integer.valueOf(cursor.getString(2)));
                sessionData.set_movementCount(Integer.valueOf(cursor.getString(3)));
                sessionData.set_maxAngle(Integer.valueOf(cursor.getString(4)));
                sessionData.set_averagePeriod(Integer.valueOf(cursor.getString(5)));

                sessionDataList.add(sessionData);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return sessionDataList;
    }

    // Getting sessionData amount
    public int getSessionDataAmount() {
        String countQuery = "SELECT * FROM " + TABLE_REHAB_SESSION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single sessionData
//    public int updateSessionData(SessionData sessionData) {}

    // Deleting Tables
    public void deleteSessionData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REHAB_SESSION, null, null);
        db.close();
    }
}

//    // Rehabsession table columns
//    private static final String KEY_ID = "id";
//    private static final String KEY_START_TIME = "start_time";
//    private static final String KEY_END_TIME = "end_time";
//    private static final String KEY_MOVEMENT_AMOUNT = "movement_amount";
//    private static final String KEY_MOVEMENT_AMPLITUDE = "movement_amplitude";
//    private static final String KEY_AVG_FREQUENCY = "avg_frequency";
//    // Foreign Key to relate to PatientData table
//    private static final String KEY_PATIENT_ID = "patient_ID";
//
//    // PatientData table columns
//    private static final String PATIENT_ID = "id";
////    private static final String
//
//
//
//    public DatabaseHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    // Creating Tables
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_REHAB_SESSION_TABLE = "CREATE TABLE " + TABLE_REHAB_SESSION + "("
//                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_START_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
//                + KEY_END_TIME + " DATETIME," + KEY_MOVEMENT_AMOUNT + " INTEGER," + KEY_MOVEMENT_AMPLITUDE + " REAL,"
//                + KEY_AVG_FREQUENCY + " REAL" + ")";
//        db.execSQL(CREATE_REHAB_SESSION_TABLE);
//    }
//
//    // Upgrading DB
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if exists
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REHAB_SESSION);
//
//        // Creating DB
//        onCreate(db);
//    }
//
//    // CRUD operations
//    // Adding new rehabSession
//    public void addRehabSession(RehabSession rehabSession) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_START_TIME, rehabSession.get_startTime().toString());
//        values.put(KEY_END_TIME, rehabSession.get_endTime().toString());
//        values.put(KEY_MOVEMENT_AMOUNT, rehabSession.get_movementAmount());
//        values.put(KEY_MOVEMENT_AMPLITUDE, rehabSession.get_maxMovementAmplitude());
//        values.put(KEY_AVG_FREQUENCY, rehabSession.get_avgFrequency());
//
//        db.insert(TABLE_REHAB_SESSION, null, values);
//        db.close();
//    }
//
//    // Getting single rehabSession
//    public RehabSession getRehabSession(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_REHAB_SESSION, new String[] { KEY_ID, KEY_START_TIME, KEY_END_TIME,
//                KEY_MOVEMENT_AMOUNT, KEY_MOVEMENT_AMPLITUDE, KEY_AVG_FREQUENCY }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        // TODO: Deal with object initialization: cast data types correctly
//        RehabSession rehabSession = new RehabSession(Integer.parseInt(cursor.getString(0)), Timestamp.valueOf(cursor.getString(1)),
//                Timestamp.valueOf(cursor.getString(2)), Integer.valueOf(cursor.getString(3)),
//                Double.parseDouble(cursor.getString(4)), Double.parseDouble(cursor.getString(5)));
//
//        cursor.close();
//
//        return rehabSession;
//    }
//
//    public List<RehabSession> getAllRehabSessions() {
//
//        List<RehabSession> rehabSessionList = new ArrayList<RehabSession>();
//
//        // Select All Query
//        String selectQuery = "SELECT * FROM " + TABLE_REHAB_SESSION;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all table rows and adding them to list
//        if (cursor.moveToFirst()) {
//            do {
//                RehabSession rehabSession = new RehabSession();
//                rehabSession.set_id(Integer.parseInt(cursor.getString(0)));
//                rehabSession.set_startTime(Timestamp.valueOf(cursor.getString(1)));
//                rehabSession.set_endTime(Timestamp.valueOf(cursor.getString(2)));
//                rehabSession.set_movementAmount(Integer.parseInt(cursor.getString(3)));
//                rehabSession.set_maxMovementAmplitude(Double.parseDouble(cursor.getString(4)));
//                rehabSession.set_avgFrequency(Double.parseDouble(cursor.getString(5)));
//
//                // Adding rehabsession instance data to List
//                rehabSessionList.add(rehabSession);
//            } while (cursor.moveToNext());
//        }
//        // return rehab session List
//        cursor.close();
//
//        return rehabSessionList;
//    }
//
//    // Getting rehabSession amount
//    public int getRehabSessionCount() {
//        String countQuery = "SELECT * FROM " + TABLE_REHAB_SESSION;
//        SQLiteDatabase db= this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        return cursor.getCount();
//    }
//
//    // Updating single rehabSession
////    public int updateRehabSession(RehabSession rehabSession) {}
//
//    // Deleting single rehabSession
//    public void deleteRehabSession(RehabSession rehabSession) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        db.delete(TABLE_REHAB_SESSION, KEY_ID + " = ?",
//                new String[] { String.valueOf(rehabSession.get_id()) });
//        db.close();
//    }
//
//    // Deleting DB
//    public void deleteRehabStats() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_REHAB_SESSION, null, null);
//        db.close();
//    }
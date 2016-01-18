package rehabdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by esyundyukov on 18/01/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // DB version
    private static final int DATABASE_VERSION = 1;
    // DB name
    private static final String DATABASE_NAME = "rehabDB";

    // RehabSession table name
    private static final String TABLE_REHAB_SESSION = "rehabSession";

    //Rehabsession table columns
    private static final String KEY_ID = "id";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_MOVEMENT_AMOUNT = "movement_amount";
    private static final String KEY_MOVEMENT_AMPLITUDE = "movement_amplitude";
    private static final String KEY_AVG_FREQUENCY = "avg_frequency";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REHAB_SESSION_TABLE = "CREATE TABLE " + TABLE_REHAB_SESSION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_START_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + KEY_END_TIME + " DATETIME," + KEY_MOVEMENT_AMOUNT + " INTEGER," + KEY_MOVEMENT_AMPLITUDE + " REAL,"
                + KEY_AVG_FREQUENCY + " REAL" + ")";
        db.execSQL(CREATE_REHAB_SESSION_TABLE);
    }

    // Upgrading DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REHAB_SESSION);

        // Creating DB
        onCreate(db);
    }

    // CRUD operations
    // Adding new rehabSession
    public void addRehabSession(RehabSession rehabSession) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_START_TIME, rehabSession.get_startTime().toString());
        values.put(KEY_END_TIME, rehabSession.get_endTime().toString());
        values.put(KEY_MOVEMENT_AMOUNT, rehabSession.get_movementAmount());
        values.put(KEY_MOVEMENT_AMPLITUDE, rehabSession.get_movementAmplitude());
        values.put(KEY_AVG_FREQUENCY, rehabSession.get_avgFrequency());

        db.insert(TABLE_REHAB_SESSION, null, values);
        db.close();
    }

    // Getting single rehabSession
    public RehabSession getRehabSession(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REHAB_SESSION, new String[] { KEY_ID, KEY_START_TIME, KEY_END_TIME,
                KEY_MOVEMENT_AMOUNT, KEY_MOVEMENT_AMPLITUDE, KEY_AVG_FREQUENCY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // TODO: Deal with object initialization: cast data types correctly
        RehabSession rehabSession = new RehabSession(); // = new RehabSession(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

        return rehabSession;
    }

//    public List<RehabSession> getAllRehabSessions() {
//        return rehabSessionList;
//    }

    // Getting rehabSession amount
//    public int getRehabSessionCount() {}

    // Updating single rehabSession
//    public int updateRehabSession(RehabSession rehabSession) {}

    // Deleting single rehabSession
    public void deleteRehabSession(RehabSession rehabSession) {}

}

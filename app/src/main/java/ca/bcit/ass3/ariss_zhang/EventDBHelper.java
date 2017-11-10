package ca.bcit.ass3.ariss_zhang;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Morgan on 2017-11-02.
 */

public class EventDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Event.sqlite";
    private static final int DB_VERSION = 2;
    private Context context;

    public EventDBHelper(Context context) {
        // The 3'rd parameter (null) is an advanced feature relating to cursors
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateMyDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateMyDatabase(sqLiteDatabase, i, i1);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(getCreateEventMasterTableSql());
            db.execSQL(getCreateEventDetailsTableSql());
            for (Event e : eventsMaster) {
                insertEvent(db, e);
            }
            for (Event_Detail e : eventDetails) {
                insertEventDetails(db, e);
            }
        } catch (SQLException sqle) {
            String msg = "[EventDbHelper / updateMyDatabase/insertCountry] DB unavailable";
            msg += "\n\n" + sqle.toString();
            Toast t = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            t.show();
        }
    }

    private String getCreateEventMasterTableSql() {
        String sql = "";
        sql += "CREATE TABLE Event_Master (";
        sql += "_id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += "NAME TEXT, ";
        sql += "DATE TEXT, ";
        sql += "TIME TEXT, ";
        sql += "DESCRIPTION TEXT);";

        return sql;
    }

    private String getCreateEventDetailsTableSql() {
        String sql = "";
        sql += "CREATE TABLE Event_Detail (";
        sql += "_detailID INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += "ItemName TEXT, ";
        sql += "ItemUnit TEXT, ";
        sql += "ItemQuantity INTEGER, ";
        sql += "Event_ID INTEGER, FOREIGN KEY(Event_ID) REFERENCES Event_Master(_id))";

        return sql;
    }

    private void insertEvent(SQLiteDatabase db, Event event) {
        ContentValues values = new ContentValues();
        values.put("NAME", event.getName());
        values.put("DATE", event.getDate());
        values.put("TIME", event.getTime());
        values.put("DESCRIPTION", event.getDescription());

        db.insert("Event_Master", null, values);
    }

    private void insertEventDetails(SQLiteDatabase db, Event_Detail event) {
        ContentValues values = new ContentValues();
        values.put("ItemName", event.getItemName());
        values.put("ItemUnit", event.getItemUnit());
        values.put("ItemQuantity", event.getItemQuantity());
        values.put("Event_ID", event.getEventID());

        db.insert("Event_Detail", null, values);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    private static final Event_Detail[] eventDetails = {
            new Event_Detail("Coca Cola", "6 packs", 2, 1),
            new Event_Detail("Pizza", "Large", 3, 1),
            new Event_Detail("Chips", "Large Bag", 5, 2),
    };

    public static final Event[] eventsMaster = {
            new Event("Halloween Party", "October 30th", "6:30PM", "A description of the Halloween Event"),
            new Event("Christmas Party", "December 20th", "12:30PM", "A description of the Christmas Event"),
            new Event("Boxing Day", "December 26st", "4:30PM", "A description of the Boxing Day Event"),
            new Event("New Year's Eve Party", "December 31st", "8:00PM", "A description of the New Years Eve Event")
    };
}


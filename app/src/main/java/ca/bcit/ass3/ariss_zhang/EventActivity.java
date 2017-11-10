package ca.bcit.ass3.ariss_zhang;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventActivity extends Activity {
    private SQLiteDatabase db;
    private Cursor cursor;
    private Button addEventDetail;
    private Event event = new Event("","","","");
    private Event_Detail event_d = new Event_Detail("","",0,0);
    public static final String EVENT_KEY = "eventID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        addEventDetail = (Button) findViewById(R.id.addEventDetails);

        findViewById(R.id.addEventDetails).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, AddEventDetail.class);
                //Roger hard coded ID
                intent.putExtra(EVENT_KEY, "3");

                startActivity(intent);
            }
        });

        String eString = getIntent().getExtras().get("event").toString();
        event = getEvent(eString);
        event_d = getEventDetail(eString);

        // Populate the event image
        // ImageView photo = (ImageView) findViewById(R.id.photo);
        // photo.setImageResource(country.getImageResourceId());
        // photo.setContentDescription(country.getDescription());

        // Populate the event name
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(event.getName());

        // Populate the date name
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(event.getDate());

        // Populate the time name
        TextView time = (TextView) findViewById(R.id.time);
        time.setText(event.getTime());

        // populate the event description
        TextView desc = (TextView) findViewById(R.id.description);
        desc.setText(event.getDescription());

        // Roger
//        TextView nameD = (TextView) findViewById(R.id.nameD);
//        if (event_d !=null) {
//            nameD.setText(event_d.getItemName());
//        }
//
//        // Populate the date name
//        TextView unit = (TextView) findViewById(R.id.unit);
//        if (event_d !=null) {
//            unit.setText(event_d.getItemUnit());
//        }
//
//        // Populate the time name
//        TextView quantity = (TextView) findViewById(R.id.quantity);
//        if (event_d !=null) {
//            quantity.setText(String.valueOf(event_d.getItemQuantity()));
//        }
    }


    private Event getEvent(String eString) {
        Event event = null;
        SQLiteOpenHelper helper = new EventDBHelper(this);
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query("Event_Master",
                    new String[] {"NAME", "DATE", "TIME", "DESCRIPTION"},
                    "NAME = ?",
                    new String[] {eString},
                    null, null, null);

            // move to the first record
            if (cursor.moveToFirst()) {
                // get the country details from the cursor
                event = new Event(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)  );
            }
        } catch (SQLiteException sqlex) {
            String msg = "[EventActivity/getEvent] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }

        return  event;
    }

    private Event_Detail getEventDetail(String eString) {
        Event_Detail event = null;
        SQLiteOpenHelper helper = new EventDBHelper(this);
        List<String> itemDetails = new ArrayList<>();

        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select ItemName, ItemUnit, ItemQuantity, Event_ID from Event_Detail where Event_ID in (select _id from Event_Master where NAME = ?)",
                    new String[] {eString});

            // move to the first record
            if (cursor.moveToFirst()) {
                // get the country details from the cursor
                do {
                    String temp = "Name: '" + cursor.getString(0) + "'";
                    temp += "\nUnit: " + cursor.getString(1);
                    temp += "\nQuantity: " + cursor.getInt(2);
                    itemDetails.add(temp);
                } while (cursor.moveToNext());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, itemDetails
            );
            ListView itemDetailList = (ListView) findViewById(R.id.eventDetailList);
            itemDetailList.setAdapter(arrayAdapter);
            itemDetailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String name = adapterView.getItemAtPosition(i).toString();
                    Pattern pattern = Pattern.compile("'(.*?)'");
                    Matcher matcher = pattern.matcher(name);
                    if (matcher.find())
                    {
                        //deleteItem(matcher.group(1));
                    }
                    Log.d("Event Detail", "name is: " + matcher.group(1));
                    Log.d("Event Detail", "number is " + i + " and " + l);
                    //deleteItem(l+1);
                }
            });
        } catch (SQLiteException sqlex) {
            String msg = "[EventActivity/getEvent] DB unavailable";
            msg += "\n\n" + sqlex.toString();

            Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            t.show();
        }

        return  event;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
        if (db != null)
            db.close();
    }

    private void deleteEvent(long id) {
        SQLiteOpenHelper helper = new EventDBHelper(this);
        db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM Event_Detail WHERE Event_ID=" + id);
        db.execSQL("DELETE FROM Event_Master WHERE _id=" + id);
    }

    private void deleteItem(long id) {
        SQLiteOpenHelper helper = new EventDBHelper(this);
        db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM Event_Detail WHERE Event_ID=" + id);

    }

}

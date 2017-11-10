package ca.bcit.ass3.ariss_zhang;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEventDetail extends Activity {

    private Button SaveItem;
    private EditText ItemUnit;
    private EditText ItemName;
    private EditText ItemQuantity;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_detail);

        SaveItem = (Button) findViewById(R.id.saveItem);
        ItemName = (EditText) findViewById(R.id.ItemName);
        ItemUnit = (EditText) findViewById(R.id.ItemUnit);
        ItemQuantity = (EditText) findViewById(R.id.ItemQuantity);

        SaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ItemName.getText().toString().equals("")) {
                    int IQuantity = Integer.parseInt(ItemQuantity.getText().toString());
                    Intent i = getIntent();
                    String pkID = getIntent().getStringExtra("eventID");
                    Event_Detail ed = new Event_Detail(ItemName.getText().toString(), ItemUnit.getText().toString(), IQuantity, Integer.parseInt(pkID));
                    insertDetails(ed);
                }
            }
        });
    }

    private void insertDetails (Event_Detail event){
        SQLiteOpenHelper helper = new EventDBHelper(this);
        db = helper.getWritableDatabase();

        // Why the this doesnt work
        //db.insertEventDetails(event);
        ContentValues values = new ContentValues();
        values.put("ItemName", event.getItemName());
        values.put("ItemUnit", event.getItemUnit());
        values.put("ItemQuantity", event.getItemQuantity());
        values.put("Event_ID", event.getEventID());

        db.insert("Event_Detail", null, values);

    }
}

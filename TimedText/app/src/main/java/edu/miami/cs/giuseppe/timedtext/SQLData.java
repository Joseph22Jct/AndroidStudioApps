package edu.miami.cs.giuseppe.timedtext;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Time;
import java.util.ArrayList;

public class SQLData extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    ListView SQLList;
    TimedTextDB database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqldata);
        SQLList = findViewById(R.id.TheList);
        database = new TimedTextDB(this);
        populateListView();
    }

        private  void populateListView() {
            Cursor datastored = database.getData();
            ArrayList<String> listData = new ArrayList<>();
            while (datastored.moveToNext()){
                listData.add(datastored.getString(1) +"\n"+ datastored.getString(2));
            }
            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData);
            SQLList.setAdapter(adapter);

        }
                //(TAG, "PopulateListView, displaying data in the list view");
}

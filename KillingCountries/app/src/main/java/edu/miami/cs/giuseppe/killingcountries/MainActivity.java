package edu.miami.cs.giuseppe.killingcountries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    MySimpleAdapter adapter;
    String[] listofCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.the_grid);
        makeAdapter();


        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setDisplayImage(position);
                gridView.setAdapter(adapter);
            }
        });
        //USE GRIDVIEW FOR THIS
    }

    private void makeAdapter() {
        listofCountries = getResources().getStringArray(R.array.countries_names);
        String[] fieldNames = {
                "name",

        };
        int[] fieldIDs = {
                R.id.the_text_view,

        };

        ArrayList<HashMap<String,String>> listOfHM = new ArrayList<>();

        HashMap<String, String> newHM;
        for (int i = 0; i<listofCountries.length; i++){
            newHM = new HashMap<>();
            newHM.put("name", listofCountries[i]);
            listOfHM.add(newHM);


        }



        adapter = new MySimpleAdapter(this, listOfHM, R.layout.single_item ,fieldNames, fieldIDs);



    }



    //-------------------------------



}

//=============================================================================
//----Have to do this because grid views are recycled
class MySimpleAdapter extends SimpleAdapter {
    //-----------------------------------------------------------------------------
    boolean[] displayImage;

    //-----------------------------------------------------------------------------
    public MySimpleAdapter(Context context, List<? extends Map<String,?>> data,
                           int resource, String[] keyNames, int[] fieldIds) {

        super(context,data,resource,keyNames,fieldIds);

        displayImage = new boolean[getCount()];
        Arrays.fill(displayImage,false);
    }
    //-----------------------------------------------------------------------------
    public void setDisplayImage(int position) {

        displayImage[position] = true;
    }
    //-----------------------------------------------------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        TextView name;
        ImageView smiley;



//----Let the superclass decide whether or not to recycle
        view = super.getView(position, convertView, parent);
        name = (TextView) view.findViewById(R.id.the_text_view);
        smiley = (ImageView) view.findViewById(R.id.the_image_view);
        //name.setText(R.array.countries_names[position]);

        if (displayImage[position]) {
            name.setVisibility(View.INVISIBLE);
            smiley.setVisibility(View.VISIBLE);
        } else {
            name.setVisibility(View.VISIBLE);
            smiley.setVisibility(View.INVISIBLE);
        }
        return (view);
    }
//-----------------------------------------------------------------------------
}
//=============================================================================
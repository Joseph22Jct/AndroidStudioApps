package edu.miami.cs.giuseppe.listofpictures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.text.method.BaseKeyListener;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    ListView theList;
    ImageView theImage;

    String[] listofEmotions;
    String[] listofEmoPics;
    int currentSizeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theList = findViewById(R.id.theList);
        theList.setOnItemClickListener(this);
//        listofEmotions = getResources().getStringArray(R.array.emotionList);
//        listofEmoPics = getResources().getStringArray(R.array.emotionPicList);

        /*
        ArrayList<HashMap<String,String>> listOfHM = new ArrayList<>();
        HashMap<String, String> newHM;
        for (int i = 0; i<theList.getAdapter().getCount(); i++){
            newHM = new HashMap<>();
            newHM.put("name", listofEmotions[i]);
            newHM.put("picture", listofEmoPics[i]);
            listOfHM.add(newHM);
        }
        */





        theImage = findViewById(R.id.theImage);
        registerForContextMenu(theImage);
    }

    //--------------------------------------------------------

    public void onItemClick(AdapterView<?> parent, View view, int position, long rowID){

            if(view.isEnabled()){
                switch (position){
                    case 0:
                        theImage.setImageResource(R.drawable.ironic_meme);
                        break;
                    case 1:
                        theImage.setImageResource(R.drawable.sad_meme);
                        break;
                    case 2:
                        theImage.setImageResource(R.drawable.gottem_meme);
                        break;
                    case 3:
                        theImage.setImageResource(R.drawable.extreme);
                        break;
                    case 4:
                        theImage.setImageResource(R.drawable.pog);
                        break;
                }
            }

    }

    //---------------------------------------------------------------------


    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater;
        inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return true;
    }

    //-----------------------------------------------------------------------

    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.showButton:
                theImage.setVisibility(View.VISIBLE);


                return true;
            case R.id.hideButton:
                theImage.setVisibility((View.INVISIBLE));


                return true;

            case R.id.happyButton:
                theList.getChildAt(0).setEnabled(true);

                if(item.isChecked()) theList.getChildAt(0).setEnabled(false);

                item.setChecked(!item.isChecked());
                return true;

            case R.id.sadButton:
                theList.getChildAt(1).setEnabled(true);

                if(item.isChecked()) theList.getChildAt(1).setEnabled(false);

                item.setChecked(!item.isChecked());
                return true;
            case R.id.gottemButton:
                theList.getChildAt(2).setEnabled(true);

                if(item.isChecked()) theList.getChildAt(2).setEnabled(false);

                item.setChecked(!item.isChecked());
                return true;
            case R.id.extremeButton:
                theList.getChildAt(3).setEnabled(true);

                if(item.isChecked()) theList.getChildAt(3).setEnabled(false);

                item.setChecked(!item.isChecked());
                return true;
            case R.id.pogButton:
                theList.getChildAt(4).setEnabled(true);

                if(item.isChecked()) theList.getChildAt(4).setEnabled(false);

                item.setChecked(!item.isChecked());
                return true;




//            case R.id.propertiesButton:
//
//
//
//
//                return true;

            //TODO figure out the cases for second dropdown menu?
            default:
                return(super.onOptionsItemSelected(item));
        }
    }

    //---------------------------------------------

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu,menu);

    }

    //--------------------------------------------------


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        LinearLayout Background = findViewById(R.id.background);

        switch(item.getItemId()){
            case R.id.red:
                Background.setBackgroundColor(getResources().getColor(R.color.Red));
                return true;
            case R.id.blue:
                Background.setBackgroundColor(getResources().getColor(R.color.Blue));
                return true;
            case R.id.green:
                Background.setBackgroundColor(getResources().getColor(R.color.Green));
                return true;

            default:
                return super.onContextItemSelected(item);

        }

    }




}

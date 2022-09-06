package edu.miami.cs.giuseppe.thephlogging;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {



    PDB database;
    Uri imageUri;
    ListView list;
    private MediaPlayer myPlayer;
    Cursor imagesCursor;
    Cursor audioCursor;
    Cursor databaseCursor;
    private static final int EDIT_ACTIVITY = 2;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    int musicStoppedAt;
    TextToSpeech mTTS;
    Time time;
    public static final int PICTURE_SELECTED = 3;
    Intent gallery;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPermission(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE})) {
            goOnCreating(true);
        }
    }

    //--------------------------------------------------
    void goOnCreating(boolean havePermission) {


        String[] imagequeryFields = {
                MediaStore.Images.Media._ID,

                MediaStore.Images.Media.DATA     //----Path to file on disk
        };


        if (havePermission) {

            setContentView(R.layout.activity_main);

            database = new PDB(this);

            time = new Time();
            time.setToNow();



                //Update database with new and different data, starting by removing images missing from the database.
                databaseCursor = database.getData();
                databaseCursor.moveToFirst();

                for (int i = 0; i<databaseCursor.getCount();i++){ // removes files that were removed from the db

                    String filepath = databaseCursor.getString(2);
                    File myFile = new File(filepath);
                    if(!myFile.exists() || !myFile.isFile()){
                        database.RemoveItem(filepath);
                    }

                    databaseCursor.moveToNext();
                }
                //Now putting new images into the db...

                //database.addData("No description yet.", compare);

            }

            FillList();

            mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener(){


                @Override
                public void onInit(int status) {
                    if(status == TextToSpeech.SUCCESS){
                        mTTS.setLanguage(Locale.ENGLISH);
                    }
                }
            });
        }


    //-----------------------------------------

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {

            case PICTURE_SELECTED:

                if (resultCode == Activity.RESULT_OK) {
                    imageUri = data.getData();
                    String imagePath = ImageFilePath.getPath(MainActivity.this, data.getData());

                    String weekday = "";
                    switch (time.weekDay) {
                        case 1:
                            weekday = "Monday";
                            break;
                        case 2:
                            weekday = "Tuesday";
                            break;
                        case 3:
                            weekday = "Wednesday";
                            break;
                        case 4:
                            weekday = "Thursday";
                            break;
                        case 5:
                            weekday = "Friday";
                            break;
                        case 6:
                            weekday = "Saturday";
                            break;
                        case 7:
                            weekday = "Sunday";
                            break;
                        default:
                            break;
                    }
                    int month = time.month + 1;

                    String dateandtime = weekday + ": " + month + "/" + time.monthDay + "/" + time.year +
                            " at " + time.hour + ":" + time.minute + ":" + time.second;


                    database.addData("", imagePath, "", dateandtime);

                    Intent editActivity = new Intent(this, EditActivity.class);

                    databaseCursor = database.getData();
                    editActivity.putExtra("ID", databaseCursor.getCount());
                    editActivity.putExtra("ImageID", imagePath);
                    editActivity.putExtra("Time", dateandtime);
                    startActivityForResult(editActivity, EDIT_ACTIVITY);
                }

                break;
            case EDIT_ACTIVITY:

                database = new PDB(this);
                databaseCursor = database.getData();
                FillList();

                break;
            default:
                break;
        }


    }

    //-------------------------------------------------------------------------

    void FillList(){

        list = findViewById(R.id.List);

        databaseCursor.moveToFirst();
        final String[] listOfTitles = new String[databaseCursor.getCount()];
        final String[] listOfImageIDs = new String[databaseCursor.getCount()];
        final String[] listOfDesc = new String[databaseCursor.getCount()];
        final String[] listOfTimes = new String[databaseCursor.getCount()];
        final int[] listOfIDs = new int[databaseCursor.getCount()];
        for (int i = 0; i<listOfTitles.length; i++){
            listOfIDs[i] = databaseCursor.getInt(0);
            listOfTitles[i] = databaseCursor.getString(1);
            listOfImageIDs[i] = databaseCursor.getString(2);
            listOfDesc[i] = databaseCursor.getString(3);
            listOfTimes[i] = databaseCursor.getString(4);
            databaseCursor.moveToNext();


        }

        MyAdapter adapter = new MyAdapter(this,listOfIDs, listOfTitles,listOfImageIDs, listOfTimes);
        list.setAdapter(adapter);
        final Intent newIntent = new Intent(this, EditActivity.class);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mTTS.speak(listOfTitles[position],TextToSpeech.QUEUE_ADD,null);
                ShowDialog(listOfTitles[position], listOfImageIDs[position], listOfDesc[position],listOfTimes[position]);

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                newIntent.putExtra("ID",listOfIDs[position]);
                newIntent.putExtra("ImageID", listOfImageIDs[position]);
                newIntent.putExtra("Title",listOfTitles[position]);
                newIntent.putExtra("Desc",listOfDesc[position]);
                newIntent.putExtra("Time",listOfTimes[position]);
                Log.d("Test", newIntent.getStringExtra("ImageID"));
                startActivityForResult(newIntent, EDIT_ACTIVITY);
                return false;
            }
        });



    }

    //------------------------------------------

    void ShowDialog(String Title, String imageID, String desc, String time){
        Bundle bundleToFragment;
        UIDialogFragment uiDialogFragment;
        bundleToFragment = new Bundle();
        uiDialogFragment = new UIDialogFragment();
        uiDialogFragment.setArguments(bundleToFragment);
        uiDialogFragment.show(getSupportFragmentManager(), "my_fragment");
        uiDialogFragment.SetActive(Title, imageID, desc, time);
    }

    //---------------------------------
    public void RestoreMusic(){
        myPlayer.seekTo(musicStoppedAt);
        myPlayer.start();
        mTTS.stop();
    }

    //------------------------------------------

    void choosePicture(){
        gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICTURE_SELECTED);
    }
    //---------------------------------------------
    public void OnClickListener(View view){
        switch(view.getId()){


            case R.id.createEntryButton:
                choosePicture();


                break;

                default:
                    break;

        }

    }


    //-------------------------------------------

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String times[];
        String titles[];
        String image[];
        int IDs[];


        MyAdapter(Context c,int Ids[], String titles[], String images[], String[] times){

            super(c, R.layout.single_item, R.id.listTitle, times);
            this.context = c;
            this.titles = titles;
            this.image = images;
            this.times = times;
            this.IDs = Ids;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate((R.layout.single_item), parent, false);
            ImageView images = row.findViewById(R.id.listImage);
            TextView myDesc = row.findViewById(R.id.listTitle);
            TextView myTime = row.findViewById(R.id.listTime);

            Log.d("ImageID", image[position]);

            Uri fullImageURI = Uri.fromFile(new File(image[position]));

            images.setImageURI(fullImageURI);
            myDesc.setText(titles[position]);
            myTime.setText(times[position]);
            return row;
        }
    }

    //----------------

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //release media player.
        if(mTTS!=null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        myPlayer.release();
    }


    //---------------------------


    private boolean getPermission(String[] whatPermissions) {

        int index;
        boolean haveAllPermissions;

        haveAllPermissions = true;
        for (index =0; index < whatPermissions.length;index++) {
            if (ContextCompat.checkSelfPermission(this,whatPermissions[index]) !=
                    PackageManager.PERMISSION_GRANTED) {
                haveAllPermissions = false;
            }
        }
        if (haveAllPermissions) {
            return(true);
        } else {
            ActivityCompat.requestPermissions(this,whatPermissions,
                    MY_PERMISSIONS_REQUEST);
            return(false);
        }
    }
    //-----------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,int[] grantResults) {

        int index;

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0) {
                    for (index = 0;index < grantResults.length;index++) {
                        if (grantResults[index] !=
                                PackageManager.PERMISSION_GRANTED) {
                            goOnCreating(false);
                            return;
                        }
                    }
                    goOnCreating(true);
                } else {
                    goOnCreating(false);
                }
                return;
            default:
                return;
        }
    }
//-----------------------------------------------------------------------------

}


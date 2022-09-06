package edu.miami.cs.giuseppe.talkingpicturelist;

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
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import static android.os.Build.ID;

public class MainActivity extends AppCompatActivity {



    TPLDB database;

    ListView list;
    private MediaPlayer myPlayer;
    Cursor imagesCursor;
    Cursor audioCursor;
    Cursor databaseCursor;
    private static final int EDIT_ACTIVITY = 2;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    int musicStoppedAt;
    TextToSpeech mTTS;


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
        String[] audioqueryFields = {
                MediaStore.Audio.Media._ID,

                MediaStore.Audio.Media.DATA     //----Path to file on disk
        };

        String[] imagequeryFields = {
                MediaStore.Images.Media._ID,

                MediaStore.Images.Media.DATA     //----Path to file on disk
        };


        if (havePermission) {

            setContentView(R.layout.activity_main);
            myPlayer = new MediaPlayer();
            database = new TPLDB(this);

            audioCursor = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,audioqueryFields,null,null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            imagesCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,imagequeryFields,null,null,
                    MediaStore.Images.Media.DEFAULT_SORT_ORDER);

            if(audioCursor.moveToFirst() && imagesCursor.moveToFirst() && audioCursor.getCount() >=1 && imagesCursor.getCount() >=1){

                //Play random music
                audioCursor.moveToPosition(new Random().nextInt(audioCursor.getCount()));

                int audioDataIndex = audioCursor.getColumnIndex(
                        MediaStore.Audio.Media.DATA);
                String audioFilename = audioCursor.getString(audioDataIndex);

                try {
                    myPlayer.setDataSource(audioFilename);
                    Log.d("Sound", audioFilename);
                    myPlayer.setLooping(true);
                    myPlayer.prepare();
                    myPlayer.start();
                } catch (IOException e) {
                    Log.d("Sound", e.getMessage());
                    //----Should do something here
                }

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

                    imagesCursor.moveToFirst();
                    for(int i = 0; i<imagesCursor.getCount(); i++) { //gets new files into db
                        String compare = imagesCursor.getString(imagesCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        if (!database.compareImgIDs(compare))
                            database.addData("No description yet.", compare);
                        imagesCursor.moveToNext();
                    }
                databaseCursor = database.getData();

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
        }

    //-----------------------------------------

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case EDIT_ACTIVITY:
                RestoreMusic();
                database = new TPLDB(this);
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
        final String[] listOfdescriptions = new String[databaseCursor.getCount()];
        final String[] listOfImageIDs = new String[databaseCursor.getCount()];
        for (int i = 0; i<listOfdescriptions.length; i++){
            listOfdescriptions[i] = databaseCursor.getString(1);
            listOfImageIDs[i] = databaseCursor.getString(2);
            databaseCursor.moveToNext();
            Log.d("DESCRIPTIONS", listOfdescriptions[i]);
            Log.d("DESCRIPTIONS", listOfImageIDs[i]);


        }

        MyAdapter adapter = new MyAdapter(this, listOfdescriptions,listOfImageIDs);
        list.setAdapter(adapter);
         final Intent newIntent = new Intent(this, EditActivity.class);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicStoppedAt=    myPlayer.getCurrentPosition();
                myPlayer.pause();
                mTTS.speak(listOfdescriptions[position],TextToSpeech.QUEUE_ADD,null);
                ShowDialog(listOfImageIDs[position]);

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                musicStoppedAt = myPlayer.getCurrentPosition();
                myPlayer.pause();

                newIntent.putExtra("ImageID", listOfImageIDs[position]);
                newIntent.putExtra("Description",listOfdescriptions[position]);
                Log.d("Test", newIntent.getStringExtra("ImageID"));
                startActivityForResult(newIntent, EDIT_ACTIVITY);
                return false;
            }
        });



    }

    //------------------------------------------

    void ShowDialog(String imageID){
        Bundle bundleToFragment;
        UIDialogFragment uiDialogFragment;
        bundleToFragment = new Bundle();
        uiDialogFragment = new UIDialogFragment();
        uiDialogFragment.setArguments(bundleToFragment);
        uiDialogFragment.show(getSupportFragmentManager(), "my_fragment");
        uiDialogFragment.SetActive(this, imageID);
    }

    //---------------------------------
    public void RestoreMusic(){
        myPlayer.seekTo(musicStoppedAt);
        myPlayer.start();
        mTTS.stop();
    }


    //-------------------------------------------

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String desc[];
        String image[];


        MyAdapter(Context c, String description[], String images[]){

            super(c, R.layout.single_item, R.id.listText, description);
            this.context = c;
            this.desc = description;
            this.image = images;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate((R.layout.single_item), parent, false);
            ImageView images = row.findViewById(R.id.listImage);
            TextView myDesc = row.findViewById(R.id.listText);
            Log.d("ImageID", image[position]);

            Uri fullImageURI = Uri.fromFile(new File(image[position]));

            images.setImageURI(fullImageURI);
            myDesc.setText(desc[position]);
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


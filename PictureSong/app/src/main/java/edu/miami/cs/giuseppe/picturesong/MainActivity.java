package edu.miami.cs.giuseppe.picturesong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private static final int MY_PERMISSIONS_REQUEST = 1;


    private MediaPlayer myPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GetPermission
        //GoOnCreating
        if (getPermission(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE})) {

            goOnCreating(true);
        }
    }

    //--------------------------------------------------
    void goOnCreating(boolean havePermission){
        Cursor imagesCursor;
        Cursor audioCursor;
        ImageView thePic;

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
            thePic = findViewById(R.id.RandomPic);
            myPlayer = new MediaPlayer();


            audioCursor = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,audioqueryFields,null,null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            imagesCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,imagequeryFields,null,null,
                    MediaStore.Images.Media.DEFAULT_SORT_ORDER);

            if(audioCursor.moveToFirst() && imagesCursor.moveToFirst() && audioCursor.getCount() >=1 && imagesCursor.getCount() >=1){
                audioCursor.moveToPosition(new Random().nextInt(audioCursor.getCount()));
                imagesCursor.moveToPosition(new Random().nextInt(imagesCursor.getCount()));

                //thePic.setImageResource(android.R.color.transparent);
                int dataIndex = imagesCursor.getColumnIndex(MediaStore.Images.Media.DATA);

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.d("URI", "Permissionn denied");
                    // Permission is not granted
                }

                Uri fullImageURI = Uri.fromFile(new File(imagesCursor.getString(dataIndex)));

                File file = new File(imagesCursor.getString(dataIndex));
                if (file.exists()) {
                    Log.d("URIC", "Content seen");
                    //Do something
                }
                else{
                    Log.d("URIC", "Not seen content");
                }
                if (fullImageURI != null) {
                    Log.d( "URI",fullImageURI.toString());
                    ((ImageView)findViewById(R.id.RandomPic)).
                            setImageURI(fullImageURI);

                } else {
                    Toast.makeText(this,"Can't get image", Toast.LENGTH_SHORT).show();
                }



                int audioDataIndex = audioCursor.getColumnIndex(
                        MediaStore.Audio.Media.DATA);
                 String audioFilename = audioCursor.getString(audioDataIndex);

                try {

                    myPlayer.setDataSource(audioFilename);
                    Log.d("Sound", audioFilename);
                    myPlayer.setOnCompletionListener(this);
                    myPlayer.prepare();
                    myPlayer.start();
                } catch (IOException e) {
                    Log.d("Sound", e.getMessage());
                    //----Should do something here
                }




            }




        }
        //SetContentView
        //Query images media store
        //Query audio media store
        //if can move to first on both cursors&& there are some images && some songs (Use the getCount() method)
        //move each cursor to a random position
        //display random image
        //play random song
    }

    //----------------------------
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //release media player.
        myPlayer.release();
    }

    //----------------------


    public void onCompletion(MediaPlayer myPlayer){

        finish();
        //finish()
    }


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

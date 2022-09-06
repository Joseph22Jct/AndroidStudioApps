package edu.miami.cs.giuseppe.videowithmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener{
    MediaPlayer myPlayer;
    String videopath;
    Uri uri;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private VideoView videoScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            goOnCreating(true);
        }

        videoScreen = findViewById(R.id.videoPanel);
        videoScreen.setVideoPath(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).
                        toString() + "/" + getString(R.string.external_movie_file));

                //"android.resource://edu.miami.cs.giuseppe.videowithmusic"+R.raw.video;


        myPlayer = MediaPlayer.create(this, R.raw.song);
        myPlayer.setOnCompletionListener(this);
        myPlayer.setOnErrorListener(this);
        myPlayer.start();
    }

    int length;

    //---------------------------

    private void goOnCreating(boolean havePermission) {

        if (havePermission) {
            setContentView(R.layout.activity_main);
            videoScreen = findViewById(R.id.videoPanel);
            videoScreen.setOnCompletionListener(this);
        } else {
            Toast.makeText(this,"Need permission", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    //----------------------------

    private boolean getPermission(String whatPermission) {

        if (ContextCompat.checkSelfPermission(this,whatPermission) ==
                PackageManager.PERMISSION_GRANTED) {
            return(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{whatPermission},MY_PERMISSIONS_REQUEST);
            return(false);
        }
    }

    //---------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
//----If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goOnCreating(true);
                } else {
                    goOnCreating(false);
                }
                return;
            default:
                return;
        }
    }
    //---------------------------------------

    public void MyClickListener(View view) {

        switch (view.getId()) {

            case R.id.PlayButton:

                videoScreen.start();
                myPlayer.pause();
                length = myPlayer.getCurrentPosition();
                break;
            case R.id.PauseButton:
                videoScreen.pause();
                if(!myPlayer.isPlaying()) {
                    myPlayer.seekTo(length);
                    myPlayer.start();
                }


                break;
            case R.id.ResumeButton:
                videoScreen.start();
                myPlayer.pause();
                length = myPlayer.getCurrentPosition();

                break;
            case R.id.StopButton:
                videoScreen.stopPlayback();
                myPlayer.stop();
                finish();
                break;
            default:
                break;

        }
    }

    //-------------------------

    public void onPrepared(MediaPlayer mediaPlayer) {

        mediaPlayer.start();
    }

    //------------------------------
    public void onCompletion(MediaPlayer mediaPlayer) {

        mediaPlayer.release();
        myPlayer = null;
    }
    //-----------------------------------------------------------------------------
    public boolean onError(MediaPlayer mediaPlayer,int whatHappened,int extra) {

        mediaPlayer.stop();
        mediaPlayer.release();
        myPlayer = null;
        return(true);
    }
}

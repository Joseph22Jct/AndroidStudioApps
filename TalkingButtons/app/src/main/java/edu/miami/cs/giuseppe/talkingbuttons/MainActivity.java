package edu.miami.cs.giuseppe.talkingbuttons;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    int sayTime;
    int current;
    int second;


    private final int CHECK_TTS = 1;
    private TextToSpeech mySpeaker;
    boolean alreadySpoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sayTime= getResources().getInteger(R.integer.maxTime);
        second = getResources().getInteger(R.integer.second);
        Intent ttsIntent;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ttsIntent = new Intent();
        ttsIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(ttsIntent,CHECK_TTS);


    }
    //------------------------------


    @Override
    public void onDestroy() {
        super.onDestroy();
        MyHandler.removeCallbacks(myProgresser);
        mySpeaker.shutdown();

    }

    //---------------------------------------


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Intent installTTSIntent;
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CHECK_TTS:
                if(requestCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                    mySpeaker = new TextToSpeech(this, this);
                }
                else{
                    installTTSIntent = new Intent();
                    installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installTTSIntent);
                }
                break;
                default:
                    break;
        }

    }
    //---------------------------------------------------------
    public void onInit(int status){
        if (status == TextToSpeech.SUCCESS){

        }
        else{
            Toast.makeText(this, "TTS could not be initialized", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    //------------------------------------------

    public void myClickHandler(View view){
        switch(view.getId()){
            case R.id.heyButton:
                if(mySpeaker.speak(getResources().getString(R.string.heyButton), TextToSpeech.QUEUE_ADD,null, "WHAT_I_SAID" )== TextToSpeech.SUCCESS){
                    alreadySpoken = false;
                    current = 0;
                    MyHandler.removeCallbacks(myProgresser);
                    myProgresser.run();
                }
                else{
                    Toast.makeText(this, "Failed to speak", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.niceButton:
                if(mySpeaker.speak(getResources().getString(R.string.niceButton), TextToSpeech.QUEUE_ADD,null, "WHAT_I_SAID" )== TextToSpeech.SUCCESS){
                    alreadySpoken = false;
                    current = 0;
                    MyHandler.removeCallbacks(myProgresser);
                    myProgresser.run();
                }
                else{
                    Toast.makeText(this, "Failed to speak", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.memeButton:
                if(mySpeaker.speak(getResources().getString(R.string.memeButton), TextToSpeech.QUEUE_ADD,null, "WHAT_I_SAID" )== TextToSpeech.SUCCESS){
                    alreadySpoken= false;
                    current = 0;
                    MyHandler.removeCallbacks(myProgresser);
                    myProgresser.run();
                }
                else{
                    Toast.makeText(this, "Failed to speak", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.myButton:
                if(mySpeaker.speak(getResources().getString(R.string.myButton), TextToSpeech.QUEUE_ADD,null, "WHAT_I_SAID" )== TextToSpeech.SUCCESS){
                    alreadySpoken = false;
                    current = 0;
                    MyHandler.removeCallbacks(myProgresser);
                    myProgresser.run();
                }
                else{
                    Toast.makeText(this, "Failed to speak", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.friendButton:
                if(mySpeaker.speak(getResources().getString(R.string.friendButton), TextToSpeech.QUEUE_ADD,null, "WHAT_I_SAID" )== TextToSpeech.SUCCESS){
                    alreadySpoken = false;
                    current = 0;
                    MyHandler.removeCallbacks(myProgresser);

                    myProgresser.run();
                }
                else{
                    Toast.makeText(this, "Failed to speak", Toast.LENGTH_SHORT).show();
                }

                break;

                default:
                    break;


        }
    }

    //-------------------------------------

    private UtteranceProgressListener myListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {

        }

        @Override
        public void onDone(String utteranceId) {
            MyHandler.post(myProgresser);
        }

        @Override
        public void onError(String utteranceId) {

        }
    };

    //----------------------------------------
    private Handler MyHandler = new Handler();

    private final Runnable myProgresser = new Runnable() {



        @Override
        public void run() {

            current+=second;

            if(!alreadySpoken && current >= sayTime) {
                alreadySpoken = true;
                mySpeaker.speak(getResources().getString(R.string.waitReact), TextToSpeech.QUEUE_ADD, null, "WHAT_I_SAID");
                doToast();
            }



            if(!MyHandler.postDelayed(myProgresser,second)){
                Log.e("ERROR", "Cannot postDelayed");
            }


        }


    };

    void doToast(){
        Toast.makeText(this, R.string.waitReact, Toast.LENGTH_SHORT).show();
    }

}

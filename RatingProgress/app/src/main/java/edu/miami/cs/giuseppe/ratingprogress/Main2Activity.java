package edu.miami.cs.giuseppe.ratingprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

public class Main2Activity extends AppCompatActivity {

    private ProgressBar myProgressBar;
    private int barClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        myProgressBar = findViewById(R.id.time_left);
        myProgressBar.setProgress(myProgressBar.getMax());

        barClickTime=getResources().getInteger(R.integer.bar_click_time);
        myProgresser.run();
    }

    //----------------------------------------

    private final Runnable myProgresser = new Runnable() {

        private Handler MyHandler = new Handler();

        @Override
        public void run() {
            myProgressBar.setProgress(myProgressBar.getProgress() - barClickTime);
            //TODO APPLY THE REST OF THIS
            if(myProgressBar.getProgress() <=0){
                setResult(RESULT_OK);
                finish();
                return;
            }
//            else{
//                myProgressBar.setProgress(myProgressBar.getMax());
//
//            }

            if(!MyHandler.postDelayed(myProgresser,barClickTime)){
                Log.e("ERROR", "Cannot postDelayed");
            }
        }



    };
}

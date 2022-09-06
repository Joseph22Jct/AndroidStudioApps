package edu.miami.cs.giuseppe.clockpickers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

//    private GregorianCalendar calendar;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Date currentTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
//        datePicker.init(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(), (DatePicker.OnDateChangedListener) this);
//        timePicker.setOnTimeChangedListener((TimePicker.OnTimeChangedListener) this);
        Log.d("hey", " " + R.integer.minute);
        myProgresser.run();
    }

    //-------------------------------------------------------

    private final Runnable myProgresser = new Runnable(){
        private Handler myHandler = new Handler();
        @Override
        public void run(){

            GregorianCalendar calendar = getCurrentDateAndTime();

            calendar.add(Calendar.MINUTE, 1);
            setDateAndTime(calendar);

            if(!myHandler.postDelayed(myProgresser, 60000)){
                Log.d("hey", "postDelayed failed");
            }

            //How does Handlers and Runnables work exactly?




        }
    };


    //-------------------------------------------------------

    GregorianCalendar getCurrentDateAndTime(){

        return new GregorianCalendar(datePicker.getYear(),
                datePicker.getMonth(), datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(), timePicker.getCurrentMinute());

//        currentTime = calendar.getTime();
//        calendar.set(currentTime.getYear(),currentTime.getMonth(),currentTime.getDay(),
//                timePicker.getCurrentHour(), timePicker.getCurrentMinute()+1);
    }


    //------------------------------------------------------

    void setDateAndTime(GregorianCalendar calendar){

        datePicker.updateDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

//        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
//                timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    }
}

package edu.miami.cs.giuseppe.timedtext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 1;

    TimedTextDB database;

    EditText textField;
    Button saveButton;
    Button DataButton;
    Intent DataIntent;
    Time time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPermission(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE})) {
            goOnCreating(true);

        }

    }
    //-------------------------------------------------------------------
    void goOnCreating(boolean permission){




        if (permission) {
            database = new TimedTextDB(this);
            setContentView(R.layout.activity_main);
            saveButton = findViewById(R.id.StoreButton);
            DataButton = findViewById(R.id.DataButton);
            textField = findViewById(R.id.InputField);
            time = new Time();
            time.setToNow();
        }
        else{
            finish();
        }
    }
    //---------------------------------------------------------
    public void addData(String newEntry, String time){
        boolean insertData = database.addData(newEntry, time);
        if(insertData){
            Toast.makeText(this,"Data inserted successfully", Toast.LENGTH_LONG);
        }
        else{
            Toast.makeText(this,"Could not insert data.", Toast.LENGTH_LONG);
        }

    }
    //--------------------------------------------------------------------

    public void OnClickListener(View view){

        switch (view.getId()){
            case R.id.StoreButton:
                String input = textField.getText().toString();
                textField.setText("");


                String weekday = "";
                switch(time.weekDay){
                    case 1: weekday = "Monday"; break;
                    case 2: weekday = "Tuesday"; break;
                    case 3: weekday = "Wednesday"; break;
                    case 4: weekday = "Thursday"; break;
                    case 5: weekday = "Friday"; break;
                    case 6: weekday = "Saturday"; break;
                    case 7: weekday = "Sunday"; break;
                    default: break;
                }

                String dateandtime = weekday+": "+ time.month+"/"+ time.monthDay +"/"+ time.year +
                        " at "+time.hour+":" + time.minute+":" + time.second;

                //input = input + "\n" + dateandtime;
                addData(input,dateandtime);


                break;
            case R.id.DataButton:
                DataIntent = new Intent(this, SQLData.class);


                startActivity(DataIntent);
                break;
                default:
                    break;
        }
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
}

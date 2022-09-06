package edu.miami.cs.Giuseppe.helloworldbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toolbar;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HelloWorldButton", "im in click handler");

        setContentView(R.layout.activity_main);
    }
    //-----------------------------------------------------------------------------
    public void myClickHandler(View view) {
        Vibrator buzzer;
        final long[] menuBuzz =
                {0,250,50,250,500,250,50,250,500,250,50,250,500,250,50,250};

        Intent nextActivity;
        Log.d("HelloWorldButton", "im in click handler");

        switch (view.getId()) {
            case R.id.the_buzz_button:

//                nextActivity = new Intent();
//                nextActivity.setClassName("edu.miami.cs.geoff.helloworld",
//"edu.miami.cs.geoff.helloworld.HelloWorld");
                buzzer = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                buzzer.vibrate(menuBuzz,-1);

                //Hello world act dont forget intent nextactivity
                //nextActivity = new Intent(this,HelloWorld.class);
                //startActivity(nextActivity);
                break;
            case R.id.the_toast_button:
                Log.d("HelloWorldButton", "im about to toast");
                Toast.makeText(this,"Toast is ready!",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
//-----------------------------------------------------------------------------
public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.main_menu,menu);

    return (true);
}
    //-----------------------------------------------------------------------------
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent nextActivity;

        //Alex really helped me guy with glasses and mid level hair

        switch (item.getItemId()) {
            case R.id.action_hello_world:
                //Hello world act dont forget intent nextactivity
                nextActivity = new Intent(this,HelloWorld.class);
                startActivity(nextActivity);
                return true;

            default:
                return(super.onOptionsItemSelected(item));
        }
    }
//-----------------------------------------------------------------------------
}

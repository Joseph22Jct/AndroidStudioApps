package edu.miami.cs.giuseppe.ratingprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.RatingBar;
//Chheck how to make button work.

public class MainActivity extends AppCompatActivity {


    private RatingBar theRatingBar;

    public static final int PROGRESS_START = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theRatingBar = findViewById(R.id.scoresystem);
        theRatingBar.setRating(0); //sets to 0
    }

    //---------------------------------------------------

    Intent nextActivity;
    //IF MY CLICK HANDLER DOES NOT WORK THEN CHECK onClick ON THE XML FILE!
    public void myClickHandler(View view){
        switch(view.getId()){
            case R.id.startbutton:
                nextActivity = new Intent(this,Main2Activity.class);
                startActivityForResult(nextActivity, PROGRESS_START);

                break;
            default:
                break;
        }
    }

    //------------------------------------------------------
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case PROGRESS_START:
                if (resultCode == Activity.RESULT_OK) {

                    theRatingBar.setRating(theRatingBar.getRating()+1);

                    if(theRatingBar.getRating() == theRatingBar.getNumStars()){
                        finish();
                    }
                    break;
                } else if (resultCode == Activity.RESULT_CANCELED) {

                }

                break;
            default:
                break;
        }


    }
}

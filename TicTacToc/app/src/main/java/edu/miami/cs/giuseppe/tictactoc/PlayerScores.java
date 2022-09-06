package edu.miami.cs.giuseppe.tictactoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;

public class PlayerScores extends AppCompatActivity {

    TextView player1Name;
    TextView player2Name;
    RatingBar player1Score;
    RatingBar player2Score;
    Button player1Picture;
    Button player2Picture;
    Uri player1PicLoc;
    Uri player2PicLoc;
    String playerName1;
    String playerName2;
    boolean playerTurn;
    int turnSpeed;

    public static final int ROUND_START = 1;
    public static final int PLAYER_1_WON = 1;
    public static final int PLAYER_2_WON = 2;
    Intent roundStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_scores);
        player1Name = findViewById(R.id.player1SN);
        player2Name = findViewById(R.id.player2SN);
        player1Picture = findViewById(R.id.player1SP);
        player2Picture = findViewById(R.id.player2SP);
        player1Score = findViewById(R.id.player1SRB);
        player2Score = findViewById(R.id.player2SRB);

        playerName1 = getIntent().getStringExtra("name1");
        if (playerName1!=null) {
            player1Name.setText(playerName1);
        }
        playerName2 = getIntent().getStringExtra("name2");
        if (playerName2!=null) {
            player2Name.setText(playerName2);
        }

        turnSpeed = getResources().getInteger(R.integer.mediumBarTime);
        Bitmap selectedPicture = null;
        Drawable playerImage;

        player1PicLoc = getIntent().getParcelableExtra("picture1");
        player2PicLoc = getIntent().getParcelableExtra("picture2");
        if(player1PicLoc != null) {
            try {
                selectedPicture = MediaStore.Images.Media.getBitmap( getContentResolver(), player1PicLoc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            playerImage = new BitmapDrawable(getResources(), selectedPicture);
            player1Picture.setBackground(playerImage);



        }

        if(player2PicLoc != null) {
            try {
                selectedPicture = MediaStore.Images.Media.getBitmap( getContentResolver(), player2PicLoc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            playerImage = new BitmapDrawable(getResources(), selectedPicture);
            player2Picture.setBackground(playerImage);



        }


    }
    //----------------------------------------------------

    public void myClickHandler(View view) {


        switch (view.getId()) {
            case R.id.startRound:
              StartRound();
            default:
                break;
        }
    }
    //---------------------

    void StartRound(){
        roundStart = new Intent(this,GameplaySection.class);

        float player1prob;
        player1prob = player2Score.getRating()-player1Score.getRating();
        player1prob = 5+player1prob/10; //makes the probability between 0 and 1, using 50% as a base and adding the difference between stars.

        float Randomval = new Random().nextFloat();

        if(player1prob<Randomval){
            playerTurn = true;
        }
        else{
            playerTurn = false;
        }

        roundStart.putExtra("name1",playerName1);
        roundStart.putExtra("name2", playerName2);
        roundStart.putExtra("picture1",player1PicLoc);
        roundStart.putExtra("picture2", player2PicLoc);
        roundStart.putExtra("turnSpeed", turnSpeed);

        roundStart.putExtra("turnOrder", playerTurn);
        startActivityForResult(roundStart,ROUND_START);
    }
    //------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater;
        inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //--------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.ResetScore:
                player1Score.setRating(0);
                player2Score.setRating(0);
                break;
            case R.id.shortTimer:
                turnSpeed = getResources().getInteger(R.integer.shortBarTime);
                break;
            case R.id.mediumTimer:
                turnSpeed = getResources().getInteger(R.integer.mediumBarTime);
                break;
            case R.id.longTimer:
                turnSpeed = getResources().getInteger(R.integer.longBarTime);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //----------------------------------------------------------

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case ROUND_START:
                if (resultCode == PLAYER_1_WON) {

                    player1Score.setRating(player1Score.getRating()+1);
                    break;
                }
                else if(resultCode ==PLAYER_2_WON){
                    player2Score.setRating(player2Score.getRating()+1);
                }
                else if (resultCode == Activity.RESULT_CANCELED) {

                }

                break;
            default:
                break;
        }


    }
}

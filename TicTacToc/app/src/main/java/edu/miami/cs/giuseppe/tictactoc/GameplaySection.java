package edu.miami.cs.giuseppe.tictactoc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

public class GameplaySection extends AppCompatActivity {

    private ProgressBar myProgressBar;
    Button player1Portrait;
    Button player2Portrait;
    int[][] buttonGrid = new int[3][3];
    Button[] buttons = new Button[10];

    Uri player1PicLoc;
    Uri player2PicLoc;
    String playerName1;
    String playerName2;

    TextView gameplayText;
    int barTime;
    private int barClickTime;
    boolean playerTurn; //1 is player1, 0 is player2
    public static final int PLAYER_1_WON = 1;
    public static final int PLAYER_2_WON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_section);
        myProgressBar = findViewById(R.id.time_left);
        barTime = getIntent().getIntExtra("turnSpeed", 100);
        myProgressBar.setMax(barTime);
        myProgressBar.setProgress(myProgressBar.getMax());
        player1Portrait = findViewById(R.id.gameplayP1);
        player2Portrait = findViewById(R.id.gameplayP2);

        //if(getIntent().getStringExtra("name1")!="")
        playerName1 = getIntent().getStringExtra("name1");
        //else playerName1 = "Player 1";
        //if(getIntent().getStringExtra("name2")!="")
        playerName2 = getIntent().getStringExtra("name2");
        //else playerName2 = "Player 2";

        playerTurn = !getIntent().getBooleanExtra("turnOrder", false);
        gameplayText = findViewById(R.id.theGameplayText);

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
            player1Portrait.setBackground(playerImage);



        }

        if(player2PicLoc != null) {
            try {
                selectedPicture = MediaStore.Images.Media.getBitmap( getContentResolver(), player2PicLoc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            playerImage = new BitmapDrawable(getResources(), selectedPicture);
            player2Portrait.setBackground(playerImage);



        }

        ChangeTurn(); //Calls this method since the game passed the reverse turn order to not overcomplicate code with if statements



        buttons[1] = findViewById(R.id.button1);
        buttons[2] = findViewById(R.id.button2);
        buttons[3] = findViewById(R.id.button3);
        buttons[4] = findViewById(R.id.button4);
        buttons[5] = findViewById(R.id.button5);
        buttons[6] = findViewById(R.id.button6);
        buttons[7] = findViewById(R.id.button7);
        buttons[8] = findViewById(R.id.button8);
        buttons[9] = findViewById(R.id.button9);

        barClickTime=getResources().getInteger(R.integer.barTime);
        myProgresser.run();
    }

    //----------------------------------------
    private Handler MyHandler = new Handler();
    private final Runnable myProgresser = new Runnable() {



        @Override
        public void run() {
            myProgressBar.setProgress(myProgressBar.getProgress() - barClickTime);


            if(myProgressBar.getProgress() <=0){
                ChangeTurn();



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

    //-------------------------------------
    int buttonChanged;
    public void myClickHandler(View view) {
        Drawable bg;


        switch (view.getId()) {
            case R.id.button1:
                if(playerTurn){
                    buttonGrid[0][0] = 1; //1 for player 1, 2 for player 2
                     bg = player1Portrait.getBackground();
                }
                else{
                    buttonGrid[0][0] = 2; //1 for player 1, 2 for player 2
                     bg = player2Portrait.getBackground();
                }
                buttonChanged = 1;
                buttons[1].setBackground(bg);
                buttons[1].setEnabled(false);
                break;
            case R.id.button2:
                if(playerTurn){
                    buttonGrid[1][0] = 1; //1 for player 1, 2 for player 2
                    bg = player1Portrait.getBackground();
                }
                else{
                    buttonGrid[1][0] = 2; //1 for player 1, 2 for player 2
                    bg = player2Portrait.getBackground();
                }
                buttonChanged = 2;
                buttons[2].setBackground(bg);
                buttons[2].setEnabled(false);
                break;
            case R.id.button3:
                if(playerTurn){
                    buttonGrid[2][0] = 1; //1 for player 1, 2 for player 2
                    bg = player1Portrait.getBackground();
                }
                else{
                    buttonGrid[2][0] = 2; //1 for player 1, 2 for player 2
                    bg = player2Portrait.getBackground();
                }

                buttons[3].setBackground(bg);
                buttons[3].setEnabled(false);
                buttonChanged = 3;
                break;
            case R.id.button4:
                if(playerTurn){
                    buttonGrid[0][1] = 1; //1 for player 1, 2 for player 2
                    bg = player1Portrait.getBackground();
                }
                else{
                    buttonGrid[0][1] = 2; //1 for player 1, 2 for player 2
                    bg = player2Portrait.getBackground();
                }

                buttons[4].setBackground(bg);
                buttons[4].setEnabled(false);
                buttonChanged = 4;
                break;
            case R.id.button5:
                if(playerTurn){
                    buttonGrid[1][1] = 1; //1 for player 1, 2 for player 2
                    bg = player1Portrait.getBackground();
                }
                else{
                    buttonGrid[1][1] = 2; //1 for player 1, 2 for player 2
                    bg = player2Portrait.getBackground();
                }

                buttons[5].setBackground(bg);
                buttons[5].setEnabled(false);
                buttonChanged = 5;
                break;
            case R.id.button6:
                if(playerTurn){
                    buttonGrid[2][1] = 1; //1 for player 1, 2 for player 2
                    bg = player1Portrait.getBackground();
                }
                else{
                    buttonGrid[2][1] = 2; //1 for player 1, 2 for player 2
                    bg = player2Portrait.getBackground();
                }

                buttons[6].setBackground(bg);
                buttons[6].setEnabled(false);
                buttonChanged = 6;
                break;
            case R.id.button7:
                if(playerTurn){
                    buttonGrid[0][2] = 1; //1 for player 1, 2 for player 2
                    bg = player1Portrait.getBackground();
                }
                else{
                    buttonGrid[0][2] = 2; //1 for player 1, 2 for player 2
                    bg = player2Portrait.getBackground();
                }

                buttons[7].setBackground(bg);
                buttons[7].setEnabled(false);
                buttonChanged = 7;
                break;
            case R.id.button8:
                if(playerTurn){
                    buttonGrid[1][2] = 1; //1 for player 1, 2 for player 2
                    bg = player1Portrait.getBackground();
                }
                else{
                    buttonGrid[1][2] = 2; //1 for player 1, 2 for player 2
                    bg = player2Portrait.getBackground();
                }

                buttons[8].setBackground(bg);
                buttons[8].setEnabled(false);
                buttonChanged = 8;
                break;
            case R.id.button9:
                if(playerTurn){
                    buttonGrid[2][2] = 1; //1 for player 1, 2 for player 2
                    bg = player1Portrait.getBackground();
                }
                else{
                    buttonGrid[2][2] = 2; //1 for player 1, 2 for player 2
                    bg = player2Portrait.getBackground();
                }

                buttons[9].setBackground(bg);
                buttons[9].setEnabled(false);
                buttonChanged = 9;
                break;

            default:
                break;
        }
        CheckWin();
        ChangeTurn();

    }

    //------------------------

    void ChangeTurn(){
        playerTurn = !playerTurn;


        MyHandler.removeCallbacks(myProgresser);
        myProgressBar.setProgress(myProgressBar.getMax());

        String placeholder;
        if(playerTurn){
            gameplayText.setText(playerName1+ "'s Turn");
            player1Portrait.setVisibility(View.VISIBLE);
            player2Portrait.setVisibility(View.INVISIBLE);
            //placeholder = playerName1+"'s Turn!";
            //gameplayText.setText(placeholder);
        }
        else{
            player2Portrait.setVisibility(View.VISIBLE);
            player1Portrait.setVisibility(View.INVISIBLE);
            gameplayText.setText(playerName2+ "'s Turn");
            //placeholder = playerName2+"'s Turn!";
            //gameplayText.setText(placeholder);
        }


        myProgresser.run();
    }

    //---------------------------------
    int win = 0;
    void CheckWin(){

        if(buttonGrid[0][0] == buttonGrid[1][0] && buttonGrid[0][0] == buttonGrid[2][0] && buttonGrid[0][0]!=0){
            win = buttonGrid[0][0];
        }
         if(buttonGrid[0][0] == buttonGrid[0][1] && buttonGrid[0][0] == buttonGrid[0][2]&& buttonGrid[0][0]!=0){
            win = buttonGrid[0][0];
        }
         if(buttonGrid[0][0] == buttonGrid[1][1] && buttonGrid[0][0] == buttonGrid[2][2]&& buttonGrid[0][0]!=0){
            win = buttonGrid[0][0];
        }
        //Rays upleft to upright, downleft, and downright.



         if(buttonGrid[1][1] == buttonGrid[0][1] && buttonGrid[1][1] == buttonGrid[2][1]&& buttonGrid[1][1]!=0){
            win = buttonGrid[1][1];
        }
         if(buttonGrid[1][1] == buttonGrid[1][0] && buttonGrid[1][1] == buttonGrid[1][2]&& buttonGrid[1][1]!=0){
            win = buttonGrid[1][1];
        }
         if(buttonGrid[1][1] == buttonGrid[2][0] && buttonGrid[1][1] == buttonGrid[0][2]&& buttonGrid[1][1]!=0){
            win = buttonGrid[1][1];
        }
        //Cardinal cross and upright to downleft.

         if(buttonGrid[2][2] == buttonGrid[2][1] && buttonGrid[2][2] == buttonGrid[2][0]&& buttonGrid[2][2]!=0){

            win = buttonGrid[2][2];
        }
         if(buttonGrid[2][2] == buttonGrid[0][2] && buttonGrid[2][2] == buttonGrid[1][2]&& buttonGrid[2][2]!=0){
            win = buttonGrid[2][2];
        }



        //downright to upright, and downrigh to downleft.

        if(win !=0){
            if (win ==1){
                //Return the player 1 winning
                setResult(PLAYER_1_WON);
            }
            else if (win ==2){
                //Return the player 2 winning
                setResult(PLAYER_2_WON);
            }

            finish();
        }
        boolean noMore = true; // Checks if all boxes are checked.
        for(int i = 0; i<3; i++){
            for(int e = 0; e<3; e++){
                if(buttonGrid[i][e]==0){
                    noMore = false;
                }
            }
        }
        if (noMore){
            setResult(RESULT_OK); //Returns a tie, aka no specific result.
            finish();
        }





        //setResult(RESULT_OK);
        //finish();
    }
}

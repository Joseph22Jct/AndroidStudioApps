package edu.miami.cs.giuseppe.tictactoc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public static final int PICTURE_SELECTED = 3;
    Intent gallery;
    Intent gameStart;

    Button button1;
    Button button2;
    Button startButton;
    boolean playerWho;
    Drawable player1Image;
    Drawable player2Image;
    EditText player1Text;
    EditText player2Text;

    Uri selectedPic1;
    Uri selectedPic2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.chooseP1Pic);
        button2 = findViewById(R.id.chooseP2Pic);
        startButton = findViewById(R.id.theStartButton);
        player1Text = findViewById(R.id.player1Name);
        player2Text = findViewById(R.id.player2Name);
    }

    //-------------------------------------

    public void myClickHandler(View view) {


        switch (view.getId()) {
            case R.id.chooseP1Pic:
                playerWho = true;
                choosePicture();
                break;
            case R.id.chooseP2Pic:
                playerWho = false;
                choosePicture();
                break;

            case R.id.theStartButton:
                startGame();
                break;
            default:
                break;
        }
    }

    //--------------------------------------

    void startGame(){
        gameStart = new Intent(this, PlayerScores.class);

        if(!player1Text.getText().toString().matches(""))
        gameStart.putExtra("name1",player1Text.getText().toString());
        else gameStart.putExtra("name1", "Player 1");
        if(!player2Text.getText().toString().matches(""))
            gameStart.putExtra("name2",player2Text.getText().toString());
        else gameStart.putExtra("name2", "Player 2");
        gameStart.putExtra("picture1",selectedPic1);
        gameStart.putExtra("picture2", selectedPic2);
        startActivity(gameStart);
    }

    //-------------------------------------

    void choosePicture(){
        gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICTURE_SELECTED);
    }


    //---------------------------------------
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedURI;
        Bitmap selectedPicture;


        switch (requestCode) {
            case PICTURE_SELECTED:
                if (resultCode == Activity.RESULT_OK) {
                    selectedURI = data.getData();
                    try {
                        if (playerWho) {
                            selectedPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedURI);
                            player1Image= new BitmapDrawable(getResources(), selectedPicture);
                            button1.setBackground(player1Image);

                            selectedPic1 = selectedURI;

                        }
                        else{
                            selectedPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedURI);
                            player2Image = new BitmapDrawable(getResources(), selectedPicture);
                            button2.setBackground(player2Image);

                            selectedPic2 = selectedURI;
                        }
                    } catch (Exception e) {
                        Log.i("ERROR", "could not get the bitmap from tbe URI");
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

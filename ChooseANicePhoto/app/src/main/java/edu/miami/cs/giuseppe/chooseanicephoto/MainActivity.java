package edu.miami.cs.giuseppe.chooseanicephoto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    //Known issues:
    //I cant get a picture into the emulator, I forgot
    //the application does not stop when a picture is not selected, tried by result codes but no effect.

    public static final int PICTURE_SELECTED = 1;
    private static final int ACTIVITY_SELECT_CONTACT = 2;
    Intent gallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choosePicture();

    }

    //-----------------------------------------------
    public void myClickHandler(View view) {


        switch (view.getId()) {
            case R.id.like_button:
                Toast.makeText(this,"Yes! The Picture was liked!",Toast.LENGTH_LONG).show();
//
                finish();
                break;
            case R.id.dislike_button:
                choosePicture();
                break;
            default:
                break;
        }
    }

    private void choosePicture(){
        gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICTURE_SELECTED);
    }

    //---------------------------------------
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView pictureView = findViewById(R.id.chosenimage);
        Uri selectedURI;
        Bitmap selectedPicture;

        switch (requestCode) {
            case PICTURE_SELECTED:
                if (resultCode == Activity.RESULT_OK) {
                    selectedURI = data.getData();
                    try {
                        selectedPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedURI);
                        pictureView.setImageBitmap(selectedPicture);
                    } catch (Exception e) {
                        Log.i("ERROR", "could not get the bitmap from tbe URI");
                    }
                    break;
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    finish();
                }

                break;
            case ACTIVITY_SELECT_CONTACT:


            break;
            default:
                break;
        }


    }


}

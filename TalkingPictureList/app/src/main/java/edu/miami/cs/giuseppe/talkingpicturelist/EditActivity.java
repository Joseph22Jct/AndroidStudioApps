package edu.miami.cs.giuseppe.talkingpicturelist;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

public class EditActivity extends AppCompatActivity {

    TPLDB database;
    String imageID;
    String textID;
    ImageView theImage;
    EditText theEditableText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        imageID = getIntent().getStringExtra("ImageID");
        textID = getIntent().getStringExtra("Description");
        database = new TPLDB(this);
        theImage = findViewById(R.id.EditImage);
        theEditableText = findViewById(R.id.EditText);
        Uri theUri = Uri.fromFile(new File(imageID));
        theImage.setImageURI(theUri);
        theEditableText.setText(textID);

    }

    //---------------------------------------------------

    public void OnClickListener(View view){
        switch (view.getId()){
            case R.id.SaveButton:
                database.ChangeDescription(imageID, theEditableText.getText().toString(), textID);
                finish();
                break;
            default:
                break;
        }
    }
}


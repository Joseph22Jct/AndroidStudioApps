package edu.miami.cs.giuseppe.thephlogging;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.CaseMap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class EditActivity extends AppCompatActivity {

    PDB database;
    String imageID;
    String textID;
    String descID;
    String timeID;
    ImageView theImage;
    EditText theEditableTitle;
    EditText theEditableText;
    TextView TimeText;
    int CurrentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        imageID = getIntent().getStringExtra("ImageID");
        textID = getIntent().getStringExtra("Title");
        descID = getIntent().getStringExtra("Desc");
        timeID = getIntent().getStringExtra("Time");
        database = new PDB(this);
        theImage = findViewById(R.id.EditImage);
        theEditableTitle = findViewById(R.id.EditTextTitle);
        theEditableText = findViewById(R.id.EditTextDesc);
        TimeText = findViewById(R.id.timeEdit);
        Log.d("Test", imageID);
        Uri theUri = Uri.fromFile(new File(imageID));
        CurrentID = getIntent().getIntExtra("ID",0);
        theImage.setImageURI(theUri);
        theEditableTitle.setText(textID);
        theEditableText.setText(descID);
        TimeText.setText(timeID);

    }

    //---------------------------------------------------

    public void OnClickListener(View view){
        switch (view.getId()){
            case R.id.SaveButton:
                database.ChangeItem(CurrentID, theEditableTitle.getText().toString(), theEditableText.getText().toString(), timeID);
                finish();
                break;
            default:
                break;
        }
    }
}


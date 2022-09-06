package edu.miami.cs.giuseppe.talkingpicturelist;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import java.io.File;

public class UIDialogFragment extends DialogFragment {

    View dialogView;
    MainActivity main;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dialogView = inflater.inflate(R.layout.dialoglayout, container);
        ((Button)dialogView.findViewById(R.id.dismissbutton)).setOnClickListener(myClickHandler);
        ((ImageView)dialogView.findViewById(R.id.memeImage)).setImageURI(uri);
        return dialogView;
    }

    public void SetActive(MainActivity value, String ImageID){ //Used to pass image and the main activity so the player can be stopped.
        uri = Uri.fromFile(new File(ImageID));
        main = value;
    }

    View.OnClickListener myClickHandler = new View.OnClickListener() {
        public void onClick(View view){
            switch (view.getId()){
                case R.id.dismissbutton:
                    main.RestoreMusic();
                    dismiss();
                    break;
                default:
                    break;
            }
        }

    };

}


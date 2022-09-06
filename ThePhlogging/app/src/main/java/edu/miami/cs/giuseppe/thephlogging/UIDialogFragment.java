package edu.miami.cs.giuseppe.thephlogging;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.io.File;

public class UIDialogFragment extends DialogFragment {

    View dialogView;

    Uri uri;
    String title;
    String desc;
    String time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        dialogView = inflater.inflate(R.layout.dialoglayout, container);

        ((Button)dialogView.findViewById(R.id.dismissbutton)).setOnClickListener(myClickHandler);
        ((ImageView)dialogView.findViewById(R.id.memeImage)).setImageURI(uri);

        ((TextView)dialogView.findViewById(R.id.DialogTitle)).setText(title);
        ((TextView)dialogView.findViewById(R.id.DialogDetails)).setText(desc);
        ((TextView)dialogView.findViewById(R.id.DialogTime)).setText(time);
        return dialogView;
    }

    public void SetActive(String Title, String ImageID, String description, String Time){ //Used to pass image and the main activity so the player can be stopped.
        uri = Uri.fromFile(new File(ImageID));
        title = Title;
        desc = description;
        time = Time;

    }

    View.OnClickListener myClickHandler = new View.OnClickListener() {
        public void onClick(View view){
            switch (view.getId()){
                case R.id.dismissbutton:

                    dismiss();
                    break;
                default:
                    break;
            }
        }

    };

}


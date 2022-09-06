package edu.miami.cs.giuseppe.showaphotodialog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class UIDialogsCustomFragment extends DialogFragment {
    View dialogView;
    public int memepic;


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        dialogView = inflater.inflate(R.layout.memecontainer, container);
        ((Button)dialogView.findViewById(R.id.dismissbutton)).setOnClickListener(myClickHandler);
        ((ImageView)dialogView.findViewById(R.id.memeImage)).setImageResource(memepic);

        return dialogView;
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

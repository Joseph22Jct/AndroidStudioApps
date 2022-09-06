package edu.miami.cs.giuseppe.showaphotodialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class UIDialogFragment extends DialogFragment {
    int memepic;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder theDialog = new AlertDialog.Builder(getActivity());


        switch(this.getArguments().getInt("dialog_type")){
            case MainActivity.SHOW_PICKER:
                theDialog.setTitle(getResources().getString(R.string.choosememe));
                theDialog.setItems(getResources().getStringArray(R.array.meme_array),listListener);
                break;


                default:
                    break;

        }

        return theDialog.create();
    }

    private DialogInterface.OnClickListener listListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case 0:
                    memepic = (R.drawable.ironic_meme);
                    break;
                case 1:
                    memepic =(R.drawable.sad_meme);
                    break;
                case 2:
                    memepic = (R.drawable.gottem_meme);
                    break;
                case 3:
                    memepic = (R.drawable.extreme);
                    break;
                case 4:
                    memepic = (R.drawable.pog);
                    break;
                default:
                    break;

            }
            /*
            Bundle bundleToFragment;
            UIDialogFragment uiDialogFragment;

            bundleToFragment = new Bundle();

            bundleToFragment.putInt("dialog_type", SHOW_PICTURE);

            uiDialogFragment = new UIDialogFragment();
            uiDialogFragment.setArguments(bundleToFragment);
            uiDialogFragment.show(getFragmentManager(), "my_fragment");

             */
            UIDialogsCustomFragment    UICustomF = new UIDialogsCustomFragment();
            UICustomF.memepic = memepic;
            UICustomF.show(getFragmentManager(), "my_fragment");
        }
    };


}

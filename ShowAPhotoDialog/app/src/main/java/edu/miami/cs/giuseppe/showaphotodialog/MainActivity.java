package edu.miami.cs.giuseppe.showaphotodialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final int SHOW_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void myClickHandler(View view) {
        Bundle bundleToFragment;
        UIDialogFragment uiDialogFragment;

        bundleToFragment = new Bundle();

        switch(view.getId()){
            case R.id.main_button:
                bundleToFragment.putInt("dialog_type", SHOW_PICKER);
                break;

            default:
                break;


        }
        uiDialogFragment = new UIDialogFragment();
        uiDialogFragment.setArguments(bundleToFragment);
        uiDialogFragment.show(getSupportFragmentManager(), "my_fragment"); //TODO, WHY DOES THIS NOT WORK


    }
}

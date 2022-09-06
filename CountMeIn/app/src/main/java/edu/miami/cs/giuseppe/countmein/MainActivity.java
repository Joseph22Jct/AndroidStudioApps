package edu.miami.cs.giuseppe.countmein;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences PressedPreferences;
    SharedPreferences.Editor PPEditor;
    TextView theText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theText = findViewById(R.id.Counter);
        PressedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        PPEditor = PressedPreferences.edit();
        int current = PressedPreferences.getInt("times", 0);
        current+=1;

        PPEditor.putInt("times", current);
        PPEditor.commit();
        theText.setText(current+"");
    }
}

package com.example.craig.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final SharedPreferences sharedPref = this.getSharedPreferences("PeriodNames", Context.MODE_PRIVATE);
        String p1 = sharedPref.getString("period1", "");
        String p2 = sharedPref.getString("period2", "");
        String p3 = sharedPref.getString("period3", "");
        String p4 = sharedPref.getString("period4", "");
        String p5 = sharedPref.getString("period5", "");
        String p6 = sharedPref.getString("period6", "");

        final EditText inputP1 = (EditText)(findViewById(R.id.editTextPeriod1));
        final EditText inputP2 = (EditText)(findViewById(R.id.editTextPeriod2));
        final EditText inputP3 = (EditText)(findViewById(R.id.editTextPeriod3));
        final EditText inputP4 = (EditText)(findViewById(R.id.editTextPeriod4));
        final EditText inputP5 = (EditText)(findViewById(R.id.editTextPeriod5));
        final EditText inputP6 = (EditText)(findViewById(R.id.editTextPeriod6));
        inputP1.setText(p1);
        inputP2.setText(p2);
        inputP3.setText(p3);
        inputP3.setText(p4);
        inputP3.setText(p5);
        inputP3.setText(p6);


        Button save = (Button)(findViewById(R.id.saveButton));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //read values from UX
                //save them
                //Toast.makeText(SettingsActivity.this, "Successfully Saved!", Toast.LENGTH_SHORT);
                String pr1 = inputP1.getText().toString();
                String pr2 = inputP2.getText().toString();
                String pr3 = inputP3.getText().toString();
                String pr4 = inputP4.getText().toString();
                String pr5 = inputP5.getText().toString();
                String pr6 = inputP6.getText().toString();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("period1", pr1);
                editor.putString("period2", pr2);
                editor.putString("period3", pr3);
                editor.putString("period4", pr4);
                editor.putString("period5", pr5);
                editor.putString("period6", pr6);


                editor.commit();



            }
        });


    }
}

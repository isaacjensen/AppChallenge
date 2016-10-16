package com.example.craig.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        inputP1.setText(p1);
        inputP2.setText(p2);

        Button save = (Button)(findViewById(R.id.saveButton));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //read values from UX
                //save them

                String pr1 = inputP1.getText().toString();
                String pr2 = inputP2.getText().toString();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("period1", pr1);
                editor.putString("period2", pr2);
                editor.commit();


            }
        });


    }
}

package com.example.craig.test;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class fullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Fullscreen View");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String path = (String) bundle.get("path");

            Bitmap image = MainActivity.getPic(path, 800, 800);
            ImageView view = (ImageView)(findViewById(R.id.imageView));
            view.setImageBitmap(image);
        }

    }


}

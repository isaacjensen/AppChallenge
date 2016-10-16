package com.example.craig.test;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class fullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            Bitmap image = (Bitmap) bundle.get("bitmap");
            ImageView view = (ImageView)(findViewById(R.id.imageView));
            view.setImageBitmap(image);
        }

    }


}

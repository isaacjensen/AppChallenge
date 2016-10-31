package com.example.craig.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("NoteLogger");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageAdapter = new ImageAdapter(this);
        GridView grid = (GridView)(findViewById(R.id.imageGrid));
        grid.setAdapter(imageAdapter);
        Button camera = (Button)(findViewById(R.id.cameraButton));
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }

        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, fullscreenActivity.class);
                intent.putExtra("bitmap", imageAdapter.images.get(position));
                startActivity(intent);
            }
        });
        Spinner periodSpinner = (Spinner)(findViewById(R.id.periodSpinner));
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                imageAdapter.setPeriod(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }
    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private Bitmap getPic(String path, int targetW, int targetH) {
        // Get the dimensions of the View

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(path, bmOptions);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmap = getPic(mCurrentPhotoPath, 200, 200);
            Spinner periodSpinner = (Spinner)(findViewById(R.id.periodSpinner));
            int period = periodSpinner.getSelectedItemPosition();

            SharedPreferences sharedPref = getSharedPreferences("Images" + period, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(mCurrentPhotoPath, mCurrentPhotoPath);
            editor.commit();
            imageAdapter.images.add(bitmap);
            GridView grid = (GridView)(findViewById(R.id.imageGrid));
            grid.invalidateViews();
        }
    }
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        // Keep all Images in array
        public ArrayList<Bitmap> images = new ArrayList<Bitmap>();

        public void setPeriod (int period) {
            images.clear();

            SharedPreferences sharedPref = getSharedPreferences("Images" + period, Context.MODE_PRIVATE);
            Map<String,?> keys = sharedPref.getAll();

            for(Map.Entry<String,?> entry : keys.entrySet()){
                //Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
                Bitmap myBitmap = getPic(entry.getKey(), 200, 200);
                images.add(myBitmap);
            }
            GridView grid = (GridView)(findViewById(R.id.imageGrid));
            grid.invalidateViews();


        }

        // Constructor
        public ImageAdapter(Context c){
            mContext = c;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageBitmap(images.get(position));
            //imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
            return imageView;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Spinner periodSpinner = (Spinner)(findViewById(R.id.periodSpinner));

        final SharedPreferences sharedPref = this.getSharedPreferences("PeriodNames", Context.MODE_PRIVATE);
        String p1 = sharedPref.getString("period1", "");
        String p2 = sharedPref.getString("period2", "");
        String p3 = sharedPref.getString("period3", "");
        String p4 = sharedPref.getString("period4", "");
        String p5 = sharedPref.getString("period5", "");
        String p6 = sharedPref.getString("period6", "");

        String[] items = new String[]{p1, p2, p3, p4, p5, p6,};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        periodSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

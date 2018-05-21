package com.example.chintanpatel.basiccamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Environment;

import android.os.StrictMode;


public class MainActivity extends AppCompatActivity {
    private final int PICTURE_ACTIVITY_CODE = 1;
    private final String FILENAME = "photo.jpg";

    private Button mButtonCapture;
    private File mFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.VmPolicy.Builder builder =
                new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 124);
            System.out.println("write permission not enabled");
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 125);
            System.out.println("read permission not enabled");
            return;
        }
        basicCamera();
    }

    private void basicCamera() {
        mButtonCapture = (Button) findViewById(R.id.btnCapture);
        mButtonCapture.setOnClickListener(mCaptureListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 124: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    basicCamera();
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICTURE_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                ImageView imageView = (ImageView) findViewById(R.id.imageView1);
                Uri inputFileUri = Uri.fromFile(mFile);
                imageView.setImageURI(inputFileUri);
            }
        }
    }

    private void launchTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyImages");
        imagesFolder.mkdirs();
        mFile = new File(imagesFolder, FILENAME);
        if (!mFile.exists()) {
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri uriSavedImage = Uri.fromFile(mFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(intent, PICTURE_ACTIVITY_CODE);
    }

    private OnClickListener mCaptureListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            launchTakePhoto();
        }
    };
}

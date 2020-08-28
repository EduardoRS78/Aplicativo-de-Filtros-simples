package com.hefesto.pool;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Camera mCamera;
    HorizontalScrollView horizontalScrollView;
    private int PERMISSION_CONSTANTE = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView ivCapture = (ImageView) findViewById(R.id.ivCapture);
        ImageView ivFilter =  (ImageView) findViewById(R.id.ivFilter);
        horizontalScrollView =(HorizontalScrollView) findViewById(R.id.ivFilterLayout);

        checkPermissionAndGive();
        ivCapture.setOnClickListener(this);
        ivFilter.setOnClickListener(this);

    }

    private void checkPermissionAndGive(){

        initialize();
    }

    private void initialize() {
        mCamera = getCameraInstance();
        CameraPreview mPreview = new CameraPreview(this,mCamera);
        FrameLayout rlCamerapreviewFrame = (FrameLayout) findViewById(R.id.rlCameraPreviem);
        if(rlCamerapreviewFrame != null){
            rlCamerapreviewFrame.addView(mPreview);
        }
    }

    

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }



    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if(pictureFile == null){
                return;
            }
            MediaScannerConnection.scanFile(MainActivity.this, new String[] {pictureFile.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                                    mCamera.startPreview();
                        }
                    });

                    try {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(data);
                        fos.close();
                    }catch (FileNotFoundException e){

                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

        }
    };

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "My Imagens");
        if(!mediaStorageDir.exists()){

            if(!mediaStorageDir.mkdirs()){
                return null;

            }
        }

        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        return new File(mediaStorageDir.getAbsolutePath() + File.separator +"IMG" + num + ".Jpg");
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.ivFilter:
                    if(horizontalScrollView.getVisibility()== View.VISIBLE){
                        horizontalScrollView.setVisibility(View.GONE);
                    }
                    else
                    {
                        horizontalScrollView.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.ivCapture:
                    mCamera.takePicture(null,null,mPicture);
                    break;

            }
    }

    public void colorEffectFilter(View v){


        try {
            Camera.Parameters parameters = mCamera.getParameters();

           switch (v.getId()){
               case R.id.rlNone:
                   parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
                   mCamera.setParameters(parameters);
                   break;

               case R.id.rlAqua:
                   parameters.setColorEffect(Camera.Parameters.EFFECT_AQUA);
                   mCamera.setParameters(parameters);
                   break;


               case R.id.rlMono:
                   parameters.setColorEffect(Camera.Parameters.EFFECT_MONO);
                   mCamera.setParameters(parameters);
                   break;

               case R.id.rlNegative:
                   parameters.setColorEffect(Camera.Parameters.EFFECT_NEGATIVE);
                   mCamera.setParameters(parameters);
                   break;

               case R.id.rlPosterized:
                   parameters.setColorEffect(Camera.Parameters.EFFECT_POSTERIZE);
                   mCamera.setParameters(parameters);
                   break;

               case R.id.rlSepia:
                   parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
                   mCamera.setParameters(parameters);
                   break;

           }

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCamera!= null){
            mCamera.stopPreview();
            mCamera.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCamera!= null){
            mCamera.stopPreview();
            mCamera = null;
        }
    }
}

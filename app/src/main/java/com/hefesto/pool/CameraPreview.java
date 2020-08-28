package com.hefesto.pool;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback
{


        SurfaceHolder mHolder;
        Camera mCamera;


        public CameraPreview(Context context, Camera mCamera){
            super(context);
            this.mCamera = mCamera;
            this.mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            Camera.Parameters parameters = mCamera.getParameters();

            parameters.set("orientation","portrait");
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);

            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if(mHolder.getSurface() == null){
                  return;
        }

        try {
            mCamera.stopPreview();
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

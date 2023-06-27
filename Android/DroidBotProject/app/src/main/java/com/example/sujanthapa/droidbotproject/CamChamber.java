package com.example.sujanthapa.droidbotproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by Sujan Thapa on 19/12/2015.
 */
public class CamChamber extends AppCompatActivity {
    Camera camera;
    RelativeLayout cameraPreview;
    CameraSurface cameraSurface;
    TextView textView;
    MyServer myServer;
    PowerManager.WakeLock wakeLock;
    PowerManager powerManager;
    Camera.PreviewCallback camPreview = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, final Camera camera) {
            try {
                int width =camera.getParameters().getPreviewSize().width;
                int height =camera.getParameters().getPreviewSize().height;
                YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                yuvImage.compressToJpeg(new Rect(0, 0, width, height), 20, os);
                final byte[] jpegByteArray = os.toByteArray();
                CamChamber.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myServer.setRawData(jpegByteArray);
                    }
                });

                if(GlobalClass.serverWakeLock){
                    wakeLock.acquire();
                }
            }catch (Exception ex){
                Log.e("Error", ex.toString());
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camchamber);
        getSupportActionBar().hide();
        try {
            powerManager =(PowerManager)getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyWakeLock");
            cameraPreview = (RelativeLayout) findViewById(R.id.camSurface);
            cameraSurface = new CameraSurface(this, camera);
            cameraPreview.addView(cameraSurface);
            textView = new TextView(this);
            textView.setBackgroundColor(Color.argb(100,255,255,255));
            cameraPreview.addView(textView);
            myServer = new MyServer(textView);
        }
        catch (Exception ex){
            Log.e("Error",ex.toString());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingCam:{
                startActivity(new Intent(this,SettingsChamber.class));
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (camera == null) {
                camera = Camera.open();
                cameraSurface.refreshCamera(camera);
            }
        }catch (Exception ex){
            Toast.makeText(this,ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    class CameraSurface extends SurfaceView implements SurfaceHolder.Callback{
        SurfaceHolder surfaceHolder;
        Camera cam;
        public CameraSurface(Context context,Camera cam){
            super(context);
            this.cam = cam;
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        //-----------CALLBACK---------------------------->
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                cam.setPreviewDisplay(holder);
                cam.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            refreshCamera(camera);
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
        public void refreshCamera(Camera camera) {
            if (surfaceHolder.getSurface() == null) {return;}
                setDisplayOrientation(camera, 90);
                camera.stopPreview();
                setCamera(camera);
                surfaceCreated(surfaceHolder);
                camera.setPreviewCallback(camPreview);
                myServer.setCamera(camera);
        }
        public void setCamera(Camera camera) {
            cam=camera;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
        myServer.Destroy();
        if(wakeLock!=null){
            wakeLock.release();
        }
    }
    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
    protected void setDisplayOrientation(Camera camera, int angle){
        Method downPolymorphic;
        try
        {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, new Object[] { angle });
        }
        catch (Exception e1)
        {
        }
    }
    public void setLog(String logData){
        textView.setText(logData);
    }
}

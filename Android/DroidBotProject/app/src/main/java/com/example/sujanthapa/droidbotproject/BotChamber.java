package com.example.sujanthapa.droidbotproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.OutputStream;

/**
 * Created by Sujan Thapa on 14/12/2015.
 */
public class BotChamber extends AppCompatActivity implements View.OnTouchListener{
    ImageView left,right,forward,backward,render,torch;
    Switch s;
    MyClient myClient;
    int angle =90;
    boolean torchState =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botchamber);
        getSupportActionBar().setTitle("Droid-Bot Chamber");
        left = (ImageView) findViewById(R.id.left);left.setOnTouchListener(BotChamber.this);
        right = (ImageView) findViewById(R.id.right);right.setOnTouchListener(BotChamber.this);
        forward = (ImageView) findViewById(R.id.forward);forward.setOnTouchListener(BotChamber.this);
        backward = (ImageView) findViewById(R.id.backward);backward.setOnTouchListener(BotChamber.this);
        render =(ImageView)findViewById(R.id.render);
        torch = (ImageView)findViewById(R.id.torchBox);
        s = (Switch)findViewById(R.id.switch1);
        myClient = new MyClient(GlobalClass.getServerIp(),render);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.botmenu,menu);
        return true;
    }
    public void toggleTorch(View view){
        if(GlobalClass.isClientTorchDataState){
            GlobalClass.isClientTorchDataState =false;
        }
        else{
            GlobalClass.isClientTorchDataState =true;
        }

        if(torchState){
            torchState=false;
            torch.setImageResource(R.drawable.torch_a);
        }
        else{
            torchState=true;
            torch.setImageResource(R.drawable.torch_b);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fitCenterImg:{
                render.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return true;
            }
            case R.id.centerImg:{
                render.setScaleType(ImageView.ScaleType.CENTER);
                return true;
            }
            case R.id.centerCropImg:{
                render.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return true;
            }
            case R.id.stretchImg:{
                render.setScaleType(ImageView.ScaleType.FIT_XY);
                return true;
            }
            case R.id.rotateClockWise:{
                myClient.angle +=90;
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.forward){
            if(event.getAction() == MotionEvent.ACTION_UP){
                sendCommand("s");
            }
            else if(event.getAction() == MotionEvent.ACTION_DOWN){
                sendCommand("f");
            }
        }
        else if(v.getId() == R.id.backward){
            if(event.getAction() == MotionEvent.ACTION_UP){
                sendCommand("s");
            }
            else if(event.getAction() == MotionEvent.ACTION_DOWN){
                sendCommand("b");
            }
        }
        else if(v.getId() == R.id.right){
            if(event.getAction() == MotionEvent.ACTION_UP){
                sendCommand("s");
            }
            else if(event.getAction() == MotionEvent.ACTION_DOWN){
                sendCommand("r");
            }
        }
        else if(v.getId() == R.id.left){
            if(event.getAction() == MotionEvent.ACTION_UP){
                sendCommand("s");
            }
            else if(event.getAction() == MotionEvent.ACTION_DOWN){
                sendCommand("l");
            }
        }
        return false;
    }
    public void cameMover(View view){

        if(view.getId()== R.id.camleft){

            if(s.isChecked()){
                sendCommand("W");
            }
            else {
                sendCommand("Y");
            }
        }
        else if(view.getId()== R.id.camright){
            if(s.isChecked()){
                sendCommand("V");
            }
            else {
                sendCommand("X");
            }
        }
    }
   void sendCommand(String cmd){
        try {
            if (MainActivity.bluetoothSocket != null) {
                OutputStream outputStream;
                outputStream = MainActivity.bluetoothSocket.getOutputStream();
                //Convert cmd message into byte
                byte[] buffer = cmd.getBytes();
                outputStream.write(buffer);
            }
        }
        catch(Exception ex){
            Log.e("Error", ex.toString());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myClient.Release();
        MainActivity.mainActivity.ReleaseBluetooth();
    }
}

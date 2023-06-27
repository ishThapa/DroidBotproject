package com.example.sujanthapa.droidbotproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sujan Thapa on 20/12/2015.
 */
public class SettingsChamber extends AppCompatActivity {
    EditText ip;
    CheckBox stayAwake;
    Spinner fps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingchamber);
        getSupportActionBar().setTitle("Droid-Bot Setting");
        Init();
    }
    public void Init(){
        try {
            ip = (EditText) findViewById(R.id.ip);
            ip.setText(GlobalClass.getServerIp());
            //------------------------------------>
            stayAwake = (CheckBox) findViewById(R.id.stayWake);
            stayAwake.setChecked(GlobalClass.getServerWakeLock());
            //------------------------------------->
            fps = (Spinner) findViewById(R.id.fps);
            fps.setSelection(GlobalClass.getFrameRate());
        }
        catch (Exception ex){
            Log.e("-->",ex.toString());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settingmenu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:{
                GlobalClass.setServerIp(ip.getText().toString());
                GlobalClass.setServerWakeLock(stayAwake.isChecked());
                GlobalClass.setFrameRate(fps.getSelectedItemPosition());
                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
                return true;
            }
            default:return false;
        }
    }
}

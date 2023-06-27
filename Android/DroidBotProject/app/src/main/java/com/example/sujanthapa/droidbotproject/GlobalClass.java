package com.example.sujanthapa.droidbotproject;

import android.content.SharedPreferences;
import android.hardware.Camera;

/**
 * Created by Sujan Thapa on 20/12/2015.
 */
public class GlobalClass {
    public static SharedPreferences sharedPreferences;
    public static String serverIp;
    public static boolean serverWakeLock = true;
    public static int frameRate = 100;
    public static boolean torch = false;
    public static boolean isTorchAvailable =false;
    public static boolean isClientTorchDataState =false;
    public static void setSharedPreference(SharedPreferences sharedPreferences_){
        sharedPreferences = sharedPreferences_;
    }
    public static void setServerIp(String Ip){
        serverIp = Ip;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("IP",serverIp);
        editor.apply();
    }
    public static String getServerIp(){
        serverIp = sharedPreferences.getString("IP", "127.0.0.1");
        return serverIp;
    }
    public static void setServerWakeLock(boolean val){
        serverWakeLock = val;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("serverWakeLock",serverWakeLock);
        editor.apply();
    }
    public static boolean getServerWakeLock(){
        serverWakeLock = sharedPreferences.getBoolean("serverWakeLock",false);
        return serverWakeLock;
    }
    public static void setFrameRate(int val){
        frameRate = val;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("frameRate",frameRate);
        editor.apply();
    }
    public static int getFrameRate(){
        frameRate = sharedPreferences.getInt("frameRate", 1);
        return frameRate;
    }
    public static void ToggleTorch(Camera camera){
        if(torch){
            torch=false;
            if(isTorchAvailable) {
                Camera.Parameters parameters =camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
            }
        }
        else{
            torch=true;
            if(isTorchAvailable) {
                Camera.Parameters parameters =camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);

            }
        }
    }
}

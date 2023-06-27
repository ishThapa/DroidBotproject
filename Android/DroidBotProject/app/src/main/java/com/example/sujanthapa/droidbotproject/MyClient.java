package com.example.sujanthapa.droidbotproject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * Created by Sujan Thapa on 19/12/2015.
 */
public class MyClient {
    Socket clientSocket;
    Thread clientThread;
    String IPAddress;
    Boolean isRunning =false;
    ImageView imageView;
    public int angle =0;
    int sleepTime;
    public MyClient(final String IPAddress,ImageView imageView){
        this.imageView =imageView;
        isRunning =true;
        this.IPAddress = IPAddress;
        this.sleepTime =getSleepTime(GlobalClass.getFrameRate());
        clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (isRunning) {
                        clientSocket = new Socket(IPAddress, 2001);
                        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                        if(GlobalClass.isClientTorchDataState){
                            oos.writeObject("T");
                            GlobalClass.isClientTorchDataState = false;
                        }
                        else {
                            oos.writeObject("|");
                        }
                        Message serverMessage = Message.obtain();
                        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                        byte[] temp = (byte[]) ois.readObject();
                        serverMessage.obj = temp;
                        mHandler.sendMessage(serverMessage);
                        oos.close();
                        ois.close();
                        //------------------->
                        try {Thread.sleep(sleepTime);} catch (Exception e) {}
                    }
                }
                catch (Exception e){
                    Log.e("Error", e.toString());
                }
            }
        });
        clientThread.start();
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            byte[] bArray =(byte[]) msg.obj;
            if(bArray!=null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
                Matrix matrix = new Matrix();
                matrix.postRotate(angle);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                imageView.setImageBitmap(rotatedBitmap);
            }
        }
    };
    public void Release(){
        isRunning =false;
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    int getSleepTime(int index){
        switch (index){
            case 0:return 50;
            case 1:return 100;
            case 2:return 500;
            default:return 100;
        }
    }
}

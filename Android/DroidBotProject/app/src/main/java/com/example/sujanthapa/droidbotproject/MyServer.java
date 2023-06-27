package com.example.sujanthapa.droidbotproject;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Sujan Thapa on 19/12/2015.
 */
public class MyServer {
    Thread m_objThread;
    ServerSocket m_server;
    boolean isRunning;
    byte[] dataraw;
    TextView textView;
    Camera cam;
    public MyServer(TextView textView){
        isRunning=true;
        this.textView = textView;
        startListening();
    }
    public void setRawData(byte[] dataraw){
        this.dataraw = dataraw;
    }
    public void setCamera(Camera cam){
        this.cam =cam;
    }
    public void startListening(){
        m_objThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String data = getIpAddress();
                    m_server = new ServerSocket(2001);
                    Message serverStatus = Message.obtain();
                    serverStatus.obj ="IP:" + data + " Port:2001";
                    mHandler.sendMessage(serverStatus);
                    ObjectInputStream ois;
                    ObjectOutputStream oos;
                    while(isRunning) {
                        Socket connectedSocket = m_server.accept();
                        ois = new ObjectInputStream(connectedSocket.getInputStream());
                        String strMessage = (String) ois.readObject();
                        if(strMessage.equals("T")){
                            if(cam != null) {
                                GlobalClass.ToggleTorch(cam);
                            }
                        }
                        oos = new ObjectOutputStream(connectedSocket.getOutputStream());
                        if (data != null) {
                            oos.writeObject(dataraw);
                        }
                        ois.close();
                        oos.close();
                    }
                    m_server.close();
                }
                catch (Exception ex){
                    Message msg3 = Message.obtain();
                    msg3.obj = ex.getMessage();
                    mHandler.sendMessage(msg3);
                    Log.e("Error",ex.toString());
                }
            }
        });
        m_objThread.start();
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           textView.setText(msg.obj.toString());
        }
    };
    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
    public void Destroy(){
        try {
            m_server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = false;
    }
}

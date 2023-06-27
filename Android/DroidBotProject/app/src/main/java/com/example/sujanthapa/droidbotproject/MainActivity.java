package com.example.sujanthapa.droidbotproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public static MainActivity mainActivity;
    ListView listView;
    public static BluetoothAdapter bluetoothAdapter;
    public static BluetoothDevice bluetoothDevice;
    public static BluetoothSocket bluetoothSocket;
    ArrayList<BluetoothData> bluetoothDataList = new ArrayList<BluetoothData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalClass.setSharedPreference(getSharedPreferences("droidBot", Context.MODE_PRIVATE));
        mainActivity= this;
        getSupportActionBar().setTitle("Droid-Bot Manager");
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        InitBluetoothPairedList();
        GlobalClass.isTorchAvailable = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:{
                InitBluetoothPairedList();
                return true;
            }
            case R.id.camMode:{
                startActivity(new Intent(this,CamChamber.class));
                return true;
            }
            case R.id.setting:{
                startActivity(new Intent(this,SettingsChamber.class));
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void InitBluetoothPairedList() {
        try {
            bluetoothDataList.clear();
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice tempDevice : devices) {
                    bluetoothDataList.add(new BluetoothData(tempDevice.getAddress(), tempDevice.getName()));
                }
                listView.setAdapter(new BluetoothList());
            }
            else {
                Toast.makeText(this, "Bluetooth Not Enabled", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }
    private class BluetoothData {
        public String MACID;
        public String NAME;

        public BluetoothData(String MACID, String NAME) {
            this.MACID = MACID;
            this.NAME = NAME;
        }
    }
    public void ReleaseBluetooth() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
                bluetoothDevice=null;
                bluetoothSocket=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class BluetoothList extends ArrayAdapter<BluetoothData> {
        public BluetoothList() {
            super(MainActivity.this, R.layout.listdata);
        }
        @Override
        public int getCount() {
            return bluetoothDataList.size();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.listdata, null);
            TextView MACID =(TextView)view.findViewById(R.id.MacID);
            TextView CLIENTID = (TextView)view.findViewById(R.id.ClientID);
            MACID.setText(bluetoothDataList.get(position).MACID);
            CLIENTID.setText(bluetoothDataList.get(position).NAME);
            return view;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothDataList.get(position).MACID);
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
            bluetoothSocket.connect();
            Toast.makeText(this,"Handshake Complete.",Toast.LENGTH_SHORT).show();
            //---------------------------------------------------->
            Intent intent = new Intent(this,BotChamber.class);
            startActivity(intent);
        }
        catch (Exception ex){
            Log.e("Error", ex.toString());
            Toast.makeText(this,ex.toString(),Toast.LENGTH_SHORT).show();
            ReleaseBluetooth();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReleaseBluetooth();
    }

    //------------------------------>
    //Shared Preference for setting

}

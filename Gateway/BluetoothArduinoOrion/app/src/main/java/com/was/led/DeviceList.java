package com.was.led;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DeviceList extends AppCompatActivity {
    Button btnPaired;
    ListView deviceList;

    private BluetoothAdapter myBluetooth=null;
    private Set<BluetoothDevice>pairedDevices;
    public static String EXTRA_ADDRESS="device_adress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);




        //calling widgetes
        btnPaired=(Button)findViewById(R.id.button);
        deviceList=(ListView) findViewById(R.id.listView);

        //if the devices has bluetooth
        myBluetooth=BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth==null){
            //show message that the device has no bluetooth
            Toast.makeText(getApplicationContext(),"Bluetooth device Not Available",Toast.LENGTH_LONG).show();
            //finish apk
            finish();
        }else if(!myBluetooth.isEnabled()){
            //Ask to he user turn the bluetooth on
            Intent turnBTon=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });
    }

    private void pairedDevicesList(){
        pairedDevices=myBluetooth.getBondedDevices();
        ArrayList list=new ArrayList();

        if(pairedDevices.size()>0){
            for(BluetoothDevice bt : pairedDevices){
                list.add(bt.getName()+"\n"+bt.getAddress());//get the decice name and the address
            }
        }else{
            Toast.makeText(getApplicationContext(),"No paired Bluetooth Devices found",Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener);//method called whern the device from the is cliecked
    }

    private AdapterView.OnItemClickListener myListClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            //get the decice MAC address, the last 17 chars in the view
            String info=((TextView)v).getText().toString();
            String address=info.substring(info.length()-17);
            Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();

            //make an intent to start next actiity
            Intent i =new Intent(DeviceList.this,ledControl.class);

            //change the activity
            i.putExtra(EXTRA_ADDRESS,address);//this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_device_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle action bar item clicks here. the action bar eill
        //automatic hadle clicks on the home/up button, so long
        //as you specify a parent activity in androidmanifest
        int id=item.getItemId();
        //noinspection simplifi....
        if (id==R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

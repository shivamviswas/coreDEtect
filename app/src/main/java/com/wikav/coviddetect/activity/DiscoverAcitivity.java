package com.wikav.coviddetect.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.wikav.coviddetect.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DiscoverAcitivity extends AppCompatActivity {
    Button scan;
    ListView listView;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter ;
    private final static int INTERVAL = 100 * 200; //2 minutes
    Handler mHandler = new Handler();
    BluetoothAdapter myBtAdapter = BluetoothAdapter.getDefaultAdapter();;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_acitivity);
        listView=findViewById(R.id.myList);
        scan=findViewById(R.id.scan);

        startRepeatingTask();


        IntentFilter intentFilter=new IntentFilter(BluetoothDevice.ACTION_FOUND);

        registerReceiver(broadcastReceiver,intentFilter);

        arrayAdapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,stringArrayList);
        listView.setAdapter(arrayAdapter);

    }


    BroadcastReceiver broadcastReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)){
           BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
           stringArrayList.add(device.getName()+" "+device.getAddress());
           arrayAdapter.notifyDataSetChanged();
        }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
        unregisterReceiver(broadcastReceiver);
    }


    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            myBtAdapter.startDiscovery();

            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };


    void startRepeatingTask()
    {
        mHandlerTask.run();
    }



    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }
}

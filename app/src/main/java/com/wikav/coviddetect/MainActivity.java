package com.wikav.coviddetect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {
        private  static final int REQUEST_ENABLE_BT=0;
        private  static final int REQUEST_DISCOVER_BT=1;
        private TextView mStatusTv,mList;
        public Button turnOn,turnOf,discover,paired;
        ImageView  bt_image;
        BluetoothAdapter mBluetoothAdapter;
        Intent btEnablingIntent;
        int requestCodeForEnable=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatusTv = findViewById(R.id.statusBluetoothTv);
        turnOn=findViewById(R.id.turnOn);
        turnOf=findViewById(R.id.turnOff);
        bt_image=findViewById(R.id.bt_image);
        discover=findViewById(R.id.discover);
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mBluetoothAdapter.setName("Cor-"+androidID);
        btEnablingIntent= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable=1;

        if (mBluetoothAdapter==null){
            Toast.makeText(MainActivity.this, "Bluetooth not support to this device", Toast.LENGTH_SHORT).show();
        }else {
            if (!mBluetoothAdapter.isEnabled()){
                mStatusTv.setText("Bluetooth Disabled");
                bt_image.setImageResource(R.drawable.bt_off);
            }else {
                mStatusTv.setText("Bluetooth "+mBluetoothAdapter.getName());
                bt_image.setImageResource(R.drawable.bt_on);
            }
        }


        btnClikedMethod();



    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivityForResult(btEnablingIntent,requestCodeForEnable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeForEnable) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(MainActivity.this, "Bluetooth Enabled", Toast.LENGTH_LONG).show();
                mStatusTv.setText("Bluetooth " + mBluetoothAdapter.getName());
                bt_image.setImageResource(R.drawable.bt_on);
            }
        }


    }

    private void btnClikedMethod() {

        turnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter==null){
                    Toast.makeText(MainActivity.this, "Bluetooth not support to this device", Toast.LENGTH_SHORT).show();
                }else {
                    if (!mBluetoothAdapter.isEnabled()){
                        startActivityForResult(btEnablingIntent,requestCodeForEnable);
                    }else {
                        Toast.makeText(MainActivity.this, "Already Enabled", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        turnOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.isEnabled()){
                   mBluetoothAdapter.disable();
                    mStatusTv.setText("Bluetooth Disabled");
                    bt_image.setImageResource(R.drawable.bt_off);
                }else {
                    Toast.makeText(MainActivity.this, "Already disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),DiscoverAcitivity.class);
                startActivity(in);
            }
        });



    }

    public static final String SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address";


    public static String getBluetoothMacAddress(Context mContext) {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "22";
        try {
            Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
            mServiceField.setAccessible(true);

            Object btManagerService = mServiceField.get(bluetoothAdapter);

            if (btManagerService != null) {
                bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
            }
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {

        }
        return bluetoothMacAddress;
    }

}

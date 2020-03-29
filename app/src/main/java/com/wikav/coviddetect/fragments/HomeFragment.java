package com.wikav.coviddetect.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wikav.coviddetect.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

    private SlidingUpPanelLayout slidingLayout;
    private Button btnShow;
    private ImageView statImage;
    private TextView tap_to_ready;
    private GifImageView gifImage;
    private LinearLayout dragView;
    BluetoothAdapter mBluetoothAdapter;
    Intent btEnablingIntent;
    int requestCodeForEnable=0;
    ListView listView;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter ;
    private final static int INTERVAL = 100 * 120; //2 minutes
    Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        String androidID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        mBluetoothAdapter.setName("Cor-"+androidID);
        btEnablingIntent= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable=1;
        if (mBluetoothAdapter==null){
            Toast.makeText(getContext(), "Bluetooth not support to this device", Toast.LENGTH_SHORT).show();
        }else {
            if (!mBluetoothAdapter.isEnabled()){
                mBluetoothAdapter.enable();
            }

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view =inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", Activity.MODE_PRIVATE);

        final SharedPreferences.Editor myEdit = sharedPreferences.edit();


        slidingLayout=view.findViewById(R.id.sliding_layout);
        statImage=view.findViewById(R.id.statImage);
        tap_to_ready=view.findViewById(R.id.tap_ready);
        gifImage=view.findViewById(R.id.gifImage);
        dragView=view.findViewById(R.id.dragView);
        listView=view.findViewById(R.id.myList);
        listView.setVisibility(View.INVISIBLE);

        boolean bTImageState = sharedPreferences.getBoolean("ImageState",false);

        if(bTImageState){
            Toast.makeText(getContext(), " Work", Toast.LENGTH_SHORT).show();
            tap_to_ready.setText("Tap to stop");
            statImage.setVisibility(View.INVISIBLE);
            gifImage.setVisibility(View.VISIBLE);
            startRepeatingTask();
        }else {
            Toast.makeText(getContext(), "Not Work", Toast.LENGTH_SHORT).show();
            tap_to_ready.setText("Tap to ready");
            gifImage.setVisibility(View.INVISIBLE);
            statImage.setVisibility(View.VISIBLE);

            stopRepeatingTask();
        }


        statImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tap_to_ready.setText("Tap to stop");
                statImage.setVisibility(View.INVISIBLE);
                gifImage.setVisibility(View.VISIBLE);
                myEdit.putBoolean("ImageState",true );
                myEdit.commit();
                startRepeatingTask();

            }
        });

        gifImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tap_to_ready.setText("Tap to ready");
                gifImage.setVisibility(View.INVISIBLE);
                statImage.setVisibility(View.VISIBLE);
                myEdit.putBoolean("ImageState",false );
                myEdit.commit();
                stopRepeatingTask();
            }
        });




        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            view.findViewById(R.id.slideUp).setAlpha(1-slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){
                    listView.setVisibility(View.VISIBLE);
                }else if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    listView.setVisibility(View.INVISIBLE);
                }
            }
        });



        IntentFilter intentFilter=new IntentFilter(BluetoothDevice.ACTION_FOUND);

        getActivity().registerReceiver(broadcastReceiver,intentFilter);

        arrayAdapter= new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,stringArrayList);
        listView.setAdapter(arrayAdapter);

        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
        stopRepeatingTask();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private void searchDevices(String devName, String devAddress) {


        if(devName.startsWith("Cor-")){
            Log.i("DEV_NAME",devName);
            Log.i("DEV_ADD",devAddress);
        }

    }


    BroadcastReceiver broadcastReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                searchDevices(device.getName(),device.getAddress());
                stringArrayList.add(device.getName());
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };





    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            mBluetoothAdapter.startDiscovery();

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
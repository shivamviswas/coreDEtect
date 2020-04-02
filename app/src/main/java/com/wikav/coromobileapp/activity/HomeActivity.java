package com.wikav.coromobileapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wikav.coromobileapp.R;
import com.wikav.coromobileapp.fragments.FullScreenDialogForNoInternet;
import com.wikav.coromobileapp.fragments.HomeFragment;
import com.wikav.coromobileapp.fragments.ListFragmant;
import com.wikav.coromobileapp.fragments.ProfileFragment;
import com.wikav.coromobileapp.fragments.UpdateFragment;

public class HomeActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    Intent btEnablingIntent;
    int requestCodeForEnable=0;
    static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    private Toolbar toolbar;
    HomeFragment home;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback mCallback;
    FullScreenDialogForNoInternet full;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        toolbar=findViewById(R.id.toolbar);
        getPermission();
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mBluetoothAdapter.setName("Cor-"+androidID);
        btEnablingIntent= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable=1;
        if (mBluetoothAdapter==null){
            Toast.makeText(getApplicationContext(), "Bluetooth not support to this device", Toast.LENGTH_SHORT).show();
        }else {
            if (!mBluetoothAdapter.isEnabled()){
              mBluetoothAdapter.enable();
            }

        }
        connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        full= new FullScreenDialogForNoInternet();
        NetworkRequest request = new NetworkRequest.Builder().build();

        mCallback= new ConnectivityManager.NetworkCallback(){

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                full.show(getSupportFragmentManager(),"show");
                Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                if(full.isVisible())
                {full.dismiss();}
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                full.show(getSupportFragmentManager(),"show");
                Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
            }
        };

        connectivityManager.registerNetworkCallback(request,mCallback);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Home");
         home = new HomeFragment();
        loadFragment(home);






    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    HomeFragment home = new HomeFragment();
                    loadFragment(home);
                    return true;
                case R.id.navigation_list:
                    ListFragmant listFragmant = new ListFragmant();
                    loadFragment(listFragmant);
                    toolbar.setTitle("You Connect With");
                    return true;
                case R.id.navigation_profile:
                    ProfileFragment profileFragment = new ProfileFragment();
                    loadFragment(profileFragment);
                    toolbar.setTitle("Infected Persons");
                    return true;

                    case R.id.updates:
                    UpdateFragment update = new UpdateFragment();
                    loadFragment(update);
                    toolbar.setTitle("Cases In India");
                    return true;

                  /*  case R.id.notification:
                    NotificationFragment notificationFragment = new NotificationFragment();
                    loadFragment(notificationFragment);
                    toolbar.setTitle("Notifications");
                    return true;*/

            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        home.onDestroy();
        connectivityManager.unregisterNetworkCallback(mCallback);
    }


    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) + ContextCompat
                    .checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) + ContextCompat
                    .checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE)
                    != PackageManager.PERMISSION_GRANTED) {


                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE},PERMISSIONS_MULTIPLE_REQUEST);

            } else {
                // finish();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && readExternalFile) {
                        // write your logic here
                    } else {

                    }
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

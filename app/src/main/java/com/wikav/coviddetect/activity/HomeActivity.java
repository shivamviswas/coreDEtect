package com.wikav.coviddetect.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wikav.coviddetect.R;
import com.wikav.coviddetect.fragments.HomeFragment;
import com.wikav.coviddetect.fragments.ListFragmant;
import com.wikav.coviddetect.fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    Intent btEnablingIntent;
    int requestCodeForEnable=0;


    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        toolbar=findViewById(R.id.toolbar);


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






        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Home");
        HomeFragment home = new HomeFragment();
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

            }
            return false;
            //vvvfvv
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

package com.wikav.coviddetect.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.wikav.coviddetect.R;
import com.wikav.coviddetect.connection.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;
    TextView mName, mMobile, mUniqueId;
    SessionManager sessionManger;
    Button submitBtn;
    String androidID;
    ProgressBar progressBar;
    private static final int REQUEST_DISCOVER_BT = 1;
    Intent btEnablingIntent;
    final String Url = "https://govindsansthan.com/coro_app/api/addUser.php";
    static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sessionManger=new SessionManager(this);
        init();
        enableBlutooth();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();

            }
        });


    }


    private void enableBlutooth() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not support to this device", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                startActivityForResult(btEnablingIntent, REQUEST_DISCOVER_BT);
            }
        }
    }

    private void init() {

        mName = findViewById(R.id.name);
        mUniqueId = findViewById(R.id.uid);
        progressBar = findViewById(R.id.progressBar);
        mMobile = findViewById(R.id.mobile);
        submitBtn = findViewById(R.id.submitBtn);
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.setName("Cor-"+androidID);
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        mUniqueId.setText(androidID);

    }

    private void submitData() {
        getPermission();
        String name=mName.getText().toString();
        String mobile=mMobile.getText().toString();
        String uid=mUniqueId.getText().toString();
        onLogin( name, mobile,uid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DISCOVER_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_LONG).show();
            }
        }


    }


    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) + ContextCompat
                    .checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) + ContextCompat
                    .checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE)
                    != PackageManager.PERMISSION_GRANTED) {


                requestPermissions(
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE},
                        PERMISSIONS_MULTIPLE_REQUEST);

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



    private void onLogin(final String name, final String mobile, final String uid) {

        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response );
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("userData");
                            // Toast.makeText(Login.this, mail+" "+passw+" "+ success, Toast.LENGTH_SHORT).show();
                            if(success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("name").trim();
                                    String mobile = object.getString("mobile").trim();
                                    String uid = object.getString("uid").trim();
                                    String mac = object.getString("mac").trim();
                                    String user_id = object.getString("id").trim();
                                    sessionManger.createSession(name,uid,mobile,mac,user_id);
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else {
                                Log.d("Response", "Error URL" );
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // email_hint.setError("Invalid Mobile no. or Password");
                            Toast.makeText(getApplicationContext(), "Error 1: " + e.toString(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error 2: " + error.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("uid", uid);
                params.put("name", name);
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }


}

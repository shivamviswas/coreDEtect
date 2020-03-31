package com.wikav.coviddetect.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wikav.coviddetect.BuildConfig;
import com.wikav.coviddetect.R;
import com.wikav.coviddetect.connection.SessionManager;
import com.wikav.coviddetect.fragments.FullScreenDialogForNoInternet;
import com.wikav.coviddetect.fragments.FullScreenDialogForUpdateApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    SessionManager sessionManger;
    ProgressBar progressBar;
    int versionCode = BuildConfig.VERSION_CODE;
    boolean isUpdateAvailable =false;
    String Url="https://govindsansthan.com/coro_app/api/getAppUpdate.php";
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback mCallback;
    FullScreenDialogForNoInternet full;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        sessionManger = new SessionManager(this);
        connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        full= new FullScreenDialogForNoInternet();
       NetworkRequest request = new NetworkRequest.Builder().build();

        mCallback= new ConnectivityManager.NetworkCallback(){

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
               full.show(getSupportFragmentManager(),"show");
                Toast.makeText(SplashActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                if(full.isVisible())
                {full.dismiss();}
                getUpdate(versionCode);
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
               full.show(getSupportFragmentManager(),"show");
                Toast.makeText(SplashActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        };

        connectivityManager.registerNetworkCallback(request,mCallback);



    }

    private void getUpdate(final int versionCode) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    progressDialog.dismiss();
                        Log.i("TAG", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")){
                                isUpdateAvailable=true ;
                                intents(isUpdateAvailable);
                            }
                            else
                            {
                                isUpdateAvailable=false;
                                intents(isUpdateAvailable);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            isUpdateAvailable=false ;
                            intents(isUpdateAvailable);
                            Toast.makeText(getApplicationContext(), "Something went wrong"+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isUpdateAvailable=false ;
                        intents(isUpdateAvailable);
                        Toast.makeText(getApplicationContext(), "Something went wrong"+error, Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("version_code", ""+versionCode);
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityManager.unregisterNetworkCallback(mCallback);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public  void  intents(final boolean isUpdateAvailable)
    {             //   Toast.makeText(this, "login check", Toast.LENGTH_SHORT).show();

        if (!sessionManger.isLoging()) {


            if(isUpdateAvailable)
            {
                FullScreenDialogForUpdateApp full=new FullScreenDialogForUpdateApp();
                full.show(getSupportFragmentManager(),"show");
            }
            else {
                // Toast.makeText(this, "ok he", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else
        {
            if(isUpdateAvailable)
            {
                FullScreenDialogForUpdateApp full=new FullScreenDialogForUpdateApp();
                full.show(getSupportFragmentManager(),"show");
            }
            else {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }
}

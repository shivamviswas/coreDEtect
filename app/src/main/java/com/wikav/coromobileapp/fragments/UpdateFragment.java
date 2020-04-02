package com.wikav.coromobileapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wikav.coromobileapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class UpdateFragment extends Fragment {

    TextView totalCases,activeCase,totalDeaths,totalRecovered,newCases,newDeaths,updateTime;
    SwipeRefreshLayout swipe;

    final String Url="https://corona.lmao.ninja/countries/India";
    public UpdateFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_notification, container, false);

        totalCases=v.findViewById(R.id.totalCase);
        activeCase=v.findViewById(R.id.activeCases);
        totalDeaths=v.findViewById(R.id.totalDeaths);
        totalRecovered=v.findViewById(R.id.totalRecovered);
        newCases=v.findViewById(R.id.newCases);
        newDeaths=v.findViewById(R.id.newDeath);
        updateTime=v.findViewById(R.id.updateTime);
        swipe=v.findViewById(R.id.swipeRefresh);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getData();
            }
        });

        getData();

        return v;
    }

    private void getData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("aex", response);
                       try {
                           if(swipe.isRefreshing()){
                               swipe.setRefreshing(false);
                           }
                            JSONObject jsonObject = new JSONObject(response);
                            String cases = jsonObject.getString("cases");
                            String todayCases = jsonObject.getString("todayCases");
                            String deaths = jsonObject.getString("deaths");
                            String todayDeaths = jsonObject.getString("todayDeaths");
                            String recovered = jsonObject.getString("recovered");
                            String active = jsonObject.getString("active");
                            String critical = jsonObject.getString("critical");
                            long updated = jsonObject.getLong("updated");
                          setData(cases,todayCases,deaths,recovered,active,todayDeaths,updated);




                        } catch (JSONException ere) {
                            ere.printStackTrace();
                            Toast.makeText(getContext(),ere.toString(),Toast.LENGTH_LONG).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("0", "0");
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void setData(String cases, String todayCases, String deaths,
                         String recovered, String active, String todayDeaths, long updated) {
        totalCases.setText(cases);
        activeCase.setText(active);
        totalDeaths.setText(deaths);
        totalRecovered.setText(recovered);
        newCases.setText(todayCases);
        newDeaths.setText(todayDeaths);
        java.util.Date time=new java.util.Date(updated);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        updateTime.setText("Last Updated :"+formatter.format(time));

    }

}

package com.wikav.coviddetect.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wikav.coviddetect.HomeActivity;
import com.wikav.coviddetect.R;
import com.wikav.coviddetect.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ListFragmant extends Fragment {

    ListView listView;
    TextView noData;
    SessionManager sessionManger;

    ArrayList<String> stringArrayList = new ArrayList<String>();

    ArrayAdapter<String> arrayAdapter;

    String myName, myId;

    final String Url = "https://govindsansthan.com/coro_app/api/getData.php";


    public ListFragmant() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManger = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManger.getUserDetail();
        myName = user.get(sessionManger.NAME);
        myId = user.get(sessionManger.USERID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view= inflater.inflate(R.layout.fragment_list_fragmant, container, false);
        listView = view.findViewById(R.id.myList);
        noData = view.findViewById(R.id.noData);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringArrayList);
        listView.setAdapter(arrayAdapter);
        getData(myId);
      return view;
    }

    private void getData(final String myId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("userData");
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id").trim();
                                    String ob_id = object.getString("ob_id").trim();
                                    String ob_name = object.getString("ob_name").trim();
                                    String dateTime = object.getString("dateTime").trim();
                                    stringArrayList.add(ob_name);
                                }
                                arrayAdapter.notifyDataSetChanged();

                            } else {
                                listView.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                                Log.d("Response", "NoData");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

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
                params.put("detector_id", myId);
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

}

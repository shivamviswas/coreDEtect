package com.wikav.coromobileapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wikav.coromobileapp.R;
import com.wikav.coromobileapp.adaptors.InfectedPrsnAdaptor;
import com.wikav.coromobileapp.connection.SessionManager;
import com.wikav.coromobileapp.models.InfectedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileFragment extends Fragment {

    ListView listView;
    TextView noData;
    SessionManager sessionManger;

    ArrayList<String> stringArrayList = new ArrayList<String>();

    List<InfectedModel> list;

    ArrayAdapter<String> arrayAdapter;

    RecyclerView recyclerView;
    String myName, myId;
    ProgressBar progressBar;

    final String Url = "https://govindsansthan.com/coro_app/api/getInfacted.php";





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
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        noData = view.findViewById(R.id.noData);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView= view.findViewById(R.id.infectedRecycleView);
        list = new ArrayList<>();
        getData(myId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);




        return view;
    }

    private void getData(final String myId) {
        progressBar.setVisibility(View.VISIBLE);
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
                                progressBar.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id").trim();
                                    String ob_id = object.getString("ob_id").trim();
                                    String ob_name = object.getString("ob_name").trim();
                                    String dateTime = object.getString("dateTime").trim();
                                    list.add(new InfectedModel(ob_name,dateTime));
                                }
                             //   arrayAdapter.notifyDataSetChanged();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                                Log.d("Response", "NoData");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        InfectedPrsnAdaptor adaptors = new InfectedPrsnAdaptor(getActivity(),list);
                        adaptors.notifyDataSetChanged();
                        recyclerView.setAdapter(adaptors);

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

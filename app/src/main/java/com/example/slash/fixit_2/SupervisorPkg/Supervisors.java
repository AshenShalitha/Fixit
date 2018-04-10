package com.example.slash.fixit_2.SupervisorPkg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Supervisors extends Fragment {

    View rootView;
    ListView supervisorsLV;
    List<String> supervisorList;
    ArrayAdapter<String> arrayAdapter;
    String companyName;
    String url= "http://fixit.projects.mrt.ac.lk/Fixit/get_supervisor_profiles";

    public Supervisors() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_supervisors, container, false);

        SessionManager sessionManager = new SessionManager(getActivity());
        companyName = sessionManager.getUserDetails().get("company");

        supervisorsLV = (ListView)rootView.findViewById(R.id.supervisorsLV);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                supervisorList = new ArrayList<String>();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int l = jsonArray.length();
                    for (int i=0;i<l;i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        supervisorList.add(jsonObject.getString("first_name")+" "+jsonObject.getString("second_name"));
                    }
                    arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,supervisorList);
                    supervisorsLV.setAdapter(arrayAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("companyName",companyName);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


        return rootView;
    }

}

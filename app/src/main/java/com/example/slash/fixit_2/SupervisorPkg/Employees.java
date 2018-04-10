package com.example.slash.fixit_2.SupervisorPkg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.Others.TwoColumnListViewAdapter;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.slash.fixit_2.Others.Constraints.FIRST_COLUMN;
import static com.example.slash.fixit_2.Others.Constraints.SECOND_COLUMN;


public class Employees extends Fragment {

    View rootView;
    ListView employeesLV;
    ArrayList<HashMap<String, String>> employeeList;
    TwoColumnListViewAdapter adapter;
    String url= "http://fixit.projects.mrt.ac.lk/Fixit/get_employee_profiles";
    String companyName;

    public Employees() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_employees, container, false);

        SessionManager sessionManager = new SessionManager(getActivity());
        companyName = sessionManager.getUserDetails().get("company");
        employeesLV = (ListView) rootView.findViewById(R.id.employeeLV);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                employeeList = new ArrayList<HashMap<String, String>>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int l = jsonArray.length();
                    for (int i=0;i<l;i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String,String> temp = new HashMap<String, String>();
                        temp.put(FIRST_COLUMN,jsonObject.getString("first_name")+" "+jsonObject.getString("second_name"));
                        temp.put(SECOND_COLUMN,"Ratings: "+jsonObject.getDouble("rating"));
                        employeeList.add(temp);
                    }

                    adapter = new TwoColumnListViewAdapter(getActivity(),employeeList);
                    employeesLV.setAdapter(adapter);
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

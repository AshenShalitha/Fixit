package com.example.slash.fixit_2.SupervisorPkg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CompanyDetails extends Fragment {

    public View rootView;
    TextView displayNameTV,displayAddressTV,displayContactNumberTV,displayEmailTV,displayProjects,displayEmployees,displaySupervisors;
    String url = "http://fixit.projects.mrt.ac.lk/Fixit/get_company_details";
    String companyName;

    public CompanyDetails() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_company_details, container, false);

        displayNameTV = (TextView)rootView.findViewById(R.id.DisplayNameTV);
        displayAddressTV = (TextView)rootView.findViewById(R.id.DisplayAddressTV);
        displayContactNumberTV = (TextView)rootView.findViewById(R.id.DisplayContactNumberTV);
        displayEmailTV = (TextView)rootView.findViewById(R.id.DIsplayEmailTV);
//        displayProjects =(TextView)rootView.findViewById(R.id.displayProjects);
//        displayEmployees = (TextView)rootView.findViewById(R.id.displayEmployees);
//        displaySupervisors = (TextView)rootView.findViewById(R.id.Displaysupervisors);

        SessionManager sessionManager = new SessionManager(getActivity());
        companyName = sessionManager.getUserDetails().get("company");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("company");
                    displayNameTV.setText(jsonObject1.getString("name"));
                    displayAddressTV.setText(jsonObject1.getString("address"));
                    displayContactNumberTV.setText(jsonObject1.getString("contact_no"));
                    displayEmailTV.setText(jsonObject1.getString("email"));
//                    displaySupervisors.setText(jsonObject.getJSONObject("supervisorCount").toString());
//                    displayEmployees.setText(jsonObject.getJSONObject("employeeCount").toString());
//                    displayProjects.setText(jsonObject.getJSONObject("projectCount").toString());
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

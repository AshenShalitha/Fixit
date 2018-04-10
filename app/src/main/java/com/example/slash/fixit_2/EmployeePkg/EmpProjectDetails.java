package com.example.slash.fixit_2.EmployeePkg;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Models.ProjectEntity;
import com.example.slash.fixit_2.Models.UserEntity;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmpProjectDetails extends Fragment {

    View rootView;
    TextView projectNameTV, descriptionTV, locationTV, deadlineTV, supervisorTV;
    Button markBTN;
    String projectname,projectId;
    UserEntity supervisor;
    String userDetailsUrl = "http://fixit.projects.mrt.ac.lk/Fixit/get_user_details";
    String markUrl = "http://fixit.projects.mrt.ac.lk/Fixit/submit_completed_employee";
    String notificationUrl = "http://fixit.projects.mrt.ac.lk/Fixit/notify_employee";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_emp_project_details, container, false);

        projectNameTV = (TextView) rootView.findViewById(R.id.DisplayProjectName);
        descriptionTV = (TextView)rootView.findViewById(R.id.DisplayDescription);
        locationTV = (TextView)rootView.findViewById(R.id.DisplayLocation);
        deadlineTV = (TextView)rootView.findViewById(R.id.DisplayDeadline);
        supervisorTV = (TextView)rootView.findViewById(R.id.DisplaySupervisorname);
        markBTN = (Button)rootView.findViewById(R.id.mark);


        SessionManager sessionManager = new SessionManager(getActivity());
        final String name = sessionManager.getUserDetails().get("firstName")+" "+sessionManager.getUserDetails().get("lastName");
        Bundle bundle = getActivity().getIntent().getExtras();
        final String type = bundle.getString("type");
        final ProjectEntity selectedProject = bundle.getParcelable("selectedProject");
        compareType(type);
        displayData(selectedProject);

        markBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, markUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(),"Project is submitted for grading!",Toast.LENGTH_LONG).show();
                        String  not = name+" marked "+selectedProject.getName() +" as completed!";
                        sendNotification(not);
                        Intent intent = new Intent(getActivity(),EmpViewProjects.class);
                        startActivity(intent);
                        getActivity().finish();
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
                        params.put("id",""+selectedProject.getId());
                        return params;
                    }
                };
                MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
            }
        });

        return rootView;
    }

    public void compareType(String t) {
        if (t.equals("late")||t.equals("ongoing")) {
            markBTN.setVisibility(View.VISIBLE);
        }
    }

    public void displayData(final ProjectEntity p)
    {
        Integer iid = p.getSupervisor_id();
        final String id = iid.toString();
        projectname = p.getName();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, userDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                supervisor = gson.fromJson(response,UserEntity.class);
                Integer pId = p.getId();
                projectId = pId.toString();
                projectNameTV.setText(p.getName());
                descriptionTV.setText(p.getDescription());
                locationTV.setText(p.getLocation());
                deadlineTV.setText(p.getDeadline_date());
                supervisorTV.setText(supervisor.getFirst_name()+" "+supervisor.getSecond_name());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void sendNotification(final String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, notificationUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                params.put("message",message);
                params.put("receiver",supervisor.getUser_name());
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}

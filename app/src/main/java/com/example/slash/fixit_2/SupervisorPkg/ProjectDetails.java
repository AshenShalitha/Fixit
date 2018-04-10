package com.example.slash.fixit_2.SupervisorPkg;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ProjectDetails extends Fragment {

    View rootView;
    TextView projectNameTV, descriptionTV, locationTV, deadlineTV, assignedEmployeeTV, deadlineET;
    EditText projectNameET, descriptionET, locationET, assignedEmployeeET;
    Button edit, delete, save, grade, revise;
    String userDetailsUrl="http://fixit.projects.mrt.ac.lk/Fixit/get_user_details";
    String deleteUrl = "http://fixit.projects.mrt.ac.lk/Fixit/delete_project";
    String editUrl = "http://fixit.projects.mrt.ac.lk/Fixit/update_project";
    String notificationUrl = "http://fixit.projects.mrt.ac.lk/Fixit/notify_employee";
    DatePickerDialog.OnDateSetListener dateSetListener;
    String date, projectId;
    String currentDate;
    String supervisorName, selectedEmployeeUserName;
    String projectname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_project_details, container, false);

        SessionManager sessionManager = new SessionManager(getActivity());
        final String id = sessionManager.getUserDetails().get("id");
        currentDate = new SimpleDateFormat("YYYY-MM-DD", Locale.getDefault()).format(new Date());

        final String fname = sessionManager.getUserDetails().get("firstName");
        final String lname = sessionManager.getUserDetails().get("lastName");
        supervisorName = fname+" "+lname;

        projectNameTV = (TextView) rootView.findViewById(R.id.DisplayProjectName);
        descriptionTV = (TextView) rootView.findViewById(R.id.DisplayDescription);
        locationTV = (TextView)rootView.findViewById(R.id.DisplayLocation);
        deadlineTV = (TextView) rootView.findViewById(R.id.DisplayDeadline);
        assignedEmployeeTV = (TextView) rootView.findViewById(R.id.employee);

        edit = (Button) rootView.findViewById(R.id.editBTN);
        save = (Button) rootView.findViewById(R.id.saveBTN);
        delete = (Button) rootView.findViewById(R.id.deleteBTN);
        grade = (Button) rootView.findViewById(R.id.gradeBTN);
        revise = (Button) rootView.findViewById(R.id.reviseBTN);

        projectNameET = (EditText) rootView.findViewById(R.id.DisplayProjectNameET);
        descriptionET = (EditText) rootView.findViewById(R.id.DisplayDescriptionET);
        locationET = (EditText)rootView.findViewById(R.id.DisplayLocationET);
        deadlineET = (TextView) rootView.findViewById(R.id.DisplayDeadlineET);
        assignedEmployeeET = (EditText) rootView.findViewById(R.id.employeeET);

        Bundle bundle = getActivity().getIntent().getExtras();
        final String type = bundle.getString("type");
        final ProjectEntity selectedProject = bundle.getParcelable("selectedProject");
        compareType(type);
        displayData(selectedProject);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEdit(descriptionTV, descriptionET);
                onEdit(locationTV,locationET);
                deadlineTV.setVisibility(View.INVISIBLE);
                deadlineET.setVisibility(View.VISIBLE);
                deadlineET.setText(deadlineTV.getText().toString());
                edit.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditReverse(descriptionTV, descriptionET);
                onEditReverse(locationTV, locationET);
                deadlineTV.setText(deadlineET.getText().toString());
                deadlineET.setVisibility(View.INVISIBLE);
                deadlineTV.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.VISIBLE);
                sendDataOnSaveClicked();
                final String message = supervisorName+" updated "+projectname+" project";
                sendNotification(message);


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = supervisorName+" deleted "+projectname+" project";
                sendNotification(message);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Intent intent = new Intent(getActivity(),ClientHome.class);
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
                        String name=selectedProject.getName();
                        params.put("id",id);
                        params.put("name",name);
                        return params;
                    }
                };
                MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
            }
        });

        grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id=selectedProject.getId();
                String pid=id.toString();
                Bundle bundle = new Bundle();
                bundle.putString("type", "grade");
                bundle.putString("projectId",pid);
                bundle.putString("username",selectedEmployeeUserName);
                bundle.putString("projectname",projectname);
                Intent intent = new Intent(getActivity(), GradePopUp.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id=selectedProject.getId();
                String pid=id.toString();
                Bundle bundle = new Bundle();
                bundle.putString("type", "revise");
                bundle.putString("projectId",pid);
                bundle.putString("username",selectedEmployeeUserName);
                bundle.putString("projectname",projectname);
                Intent intent = new Intent(getActivity(), GradePopUp.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        deadlineET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDeadline();
            }
        });


        return rootView;
    }

    //convert text view to edit text
    public void onEdit(TextView tv, EditText et) {
        et.setText(tv.getText().toString());
        tv.setVisibility(View.INVISIBLE);
        et.setVisibility(View.VISIBLE);
    }

    //convert edit text to text view
    public void onEditReverse(TextView tv, EditText et) {
        tv.setText(et.getText().toString());
        et.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.VISIBLE);
    }

    public void compareType(String t) {
        if (t.equals("completed")) {
            edit.setVisibility(View.INVISIBLE);
        } else if (t.equals("sfg")) {
            edit.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
            grade.setVisibility(View.VISIBLE);
            revise.setVisibility(View.VISIBLE);
        }
    }

    public void displayData(final ProjectEntity p)
    {
        Integer iid = p.getEmployee_id();
        final String id = iid.toString();
        projectname = p.getName();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, userDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                UserEntity employee = gson.fromJson(response,UserEntity.class);
                selectedEmployeeUserName = employee.getUser_name();
                Integer pId = p.getId();
                projectId = pId.toString();
                projectNameTV.setText(p.getName());
                descriptionTV.setText(p.getDescription());
                locationTV.setText(p.getLocation());
                deadlineTV.setText(p.getDeadline_date());
                assignedEmployeeTV.setText(employee.getFirst_name()+" "+employee.getSecond_name());
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

    public void setDeadline() {
        deadlineET.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date = year + "-" + month + "-" + day;
                deadlineET.setText(date);

                if (currentDate.compareTo(date) > 0) {
                    deadlineET.requestFocus();
                    deadlineET.setError("Deadline date is outdated!");
                } else {
                    deadlineET.setError(null);
                }
            }
        };
    }

    public void sendDataOnSaveClicked()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, editUrl, new Response.Listener<String>() {
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
                params.put("projectId",projectId);
                params.put("description",descriptionTV.getText().toString());
                params.put("location",locationTV.getText().toString());
                params.put("deadline",deadlineTV.getText().toString());
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
                params.put("receiver",selectedEmployeeUserName);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}

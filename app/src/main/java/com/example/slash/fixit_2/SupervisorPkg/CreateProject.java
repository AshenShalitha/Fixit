package com.example.slash.fixit_2.SupervisorPkg;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateProject extends AppCompatActivity {
    private EditText projectNameET,descriptionET,locationET;
    private TextView deadlineDate;
    private Spinner employeeNameSP;
    private Button createProjectBTN;
    String projectname,description,deadline,selectedEmployee,location;
    String selectedEmployeeUserName;
    String supervisorId,supervisorName;
    String userListUrl="http://fixit.projects.mrt.ac.lk/Fixit/get_employees";
    String projectUrl = "http://fixit.projects.mrt.ac.lk/Fixit/save_project";
    String projectNameValidateUrl="http://fixit.projects.mrt.ac.lk/Fixit/supervisor_projectlist_get";
    String notificationUrl = "http://fixit.projects.mrt.ac.lk/Fixit/notify_employee";
    List<String> employeeList;
    List<String> usernameList;
    List<String> projectNamesList;
    UserEntity[] userEntities;
    ProjectEntity[] projectEntity;
    DatePickerDialog.OnDateSetListener dateSetListener;
    String date;
    String currentDate;
    int l;
    private String companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        projectNameET = (EditText)findViewById(R.id.projectNameET);
        descriptionET = (EditText)findViewById(R.id.discriptionET);
        locationET = (EditText)findViewById(R.id.locationET);
        deadlineDate = (TextView) findViewById(R.id.deadlineET);
        employeeNameSP = (Spinner)findViewById(R.id.employeeSP);
        createProjectBTN = (Button)findViewById(R.id.createbtn);

        SessionManager sessionManager = new SessionManager(CreateProject.this);
        supervisorId=sessionManager.getUserDetails().get("id");
        companyName = sessionManager.getUserDetails().get("company");
        final String fname = sessionManager.getUserDetails().get("firstName");
        final String lname = sessionManager.getUserDetails().get("lastName");
        supervisorName = fname+" "+lname;

        Date today = new Date();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = DATE_FORMAT.format(today);


        getEmployeename();
        setDeadline();

        createProjectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
                sendNotification();
            }
        });

    }


    public void getEmployeename()
    {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, userListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                employeeList = new ArrayList<String>();
                usernameList = new ArrayList<String>();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                userEntities = gson.fromJson(response, UserEntity[].class);
                int l=userEntities.length;
                for (int i=0;i<l;i++)
                {
                    if(userEntities[i].getRole().equals("Employee"))
                    {
                        usernameList.add(userEntities[i].getUser_name());
                        employeeList.add(userEntities[i].getFirst_name()+" "+userEntities[i].getSecond_name());
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateProject.this,android.R.layout.simple_list_item_1,employeeList);
                employeeNameSP.setAdapter(arrayAdapter);
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

        MySingleton.getInstance(CreateProject.this).addToRequestQueue(stringRequest1);

        employeeNameSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                selectedEmployee = tv.getText().toString();
                selectedEmployeeUserName = usernameList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void sendData()
    {
        projectname = projectNameET.getText().toString();
        description = descriptionET.getText().toString();
        location = locationET.getText().toString();
        validate();

        //Validating project name------------------------------------------------------------------------------------------------------
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, projectNameValidateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                projectNamesList = new ArrayList<String>();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                projectEntity =  gson.fromJson(response, ProjectEntity[].class);
                l = projectEntity.length;
                for(int i=0;i<l;i++)
                {
                    projectNamesList.add(projectEntity[i].getName());
                }
                for (int i=0;i<l;i++)
                {
                    if(projectname.equals(projectNamesList.get(i)))
                    {
                        projectNameET.setError("Project name is already exists!");
                        projectNameET.setText("");
                        break;
                    }
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
                params.put("id",supervisorId);
                return params;
            }
        };
        MySingleton.getInstance(CreateProject.this).addToRequestQueue(stringRequest1);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, projectUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success"))
                {
                    Intent intent  = new Intent(CreateProject.this,Projects.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(CreateProject.this,"Operation Failed!",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("projectName",projectname);
                params.put("description",description);
                params.put("deadlineDate",date);
                params.put("createdDate",currentDate);
                params.put("location",location);
                params.put("empUsername",selectedEmployeeUserName);
                params.put("supervisorId",supervisorId);
                return params;
            }
        };
        MySingleton.getInstance(CreateProject.this).addToRequestQueue(stringRequest);
    }


    public void validate()
    {
        //Validating editText inputs---------------------------------------------------------------------------------------------------
        if(projectname.equals(""))
        {
            projectNameET.requestFocus();
            projectNameET.setError("Project name is required!");
        }
        if(description.equals(""))
        {
            descriptionET.requestFocus();
            descriptionET.setError("Description is requirred!");
        }
        if(location.equals(""))
        {
            locationET.requestFocus();
            locationET.setError("Location is requirred!");
        }

        //Validating deadline date-----------------------------------------------------------------------------------------------------
        if(deadlineDate.getText().toString().equals("deadline"))
        {
            deadlineDate.requestFocus();
            deadlineDate.setError("Please set deadline!");
        }

    }

    public void setDeadline()
    {
        deadlineDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateProject.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                date = year+"-"+month+"-"+day;
                deadlineDate.setText(date);

                if (currentDate.compareTo(date)>0)
                {
                    deadlineDate.requestFocus();
                    deadlineDate.setError("Deadline date is outdated!");
                }
                else
                {
                    deadlineDate.setError(null);
                }
            }
        };

    }

    public void sendNotification()
    {
        projectname = projectNameET.getText().toString();
        final String message = supervisorName+" assigned you new project "+projectname;
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
        MySingleton.getInstance(CreateProject.this).addToRequestQueue(stringRequest);
    }
}


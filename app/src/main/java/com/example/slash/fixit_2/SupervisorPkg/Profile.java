package com.example.slash.fixit_2.SupervisorPkg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Models.UserEntity;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Profile extends AppCompatActivity {

    TextView DisplayFirstNameTV,DisplaySecondNameTV,DisplayContactNumberTV,DisplayEmailTV,DisplayCompanyTV,DisplayRoleTV,totalTV,activeTV,completedTV;
    EditText editFirstNameET,editSecondNameET,editContactNumberET,editEmailET;
    Button editBTN,saveBTN;
    String FirstName,SecondName,ContactNumber,Email;
    String id,username;
    UserEntity userEntity;
    String getPersonalDataUrl = "http://fixit.projects.mrt.ac.lk/Fixit/get_user_details";
    String getProjectDataUrl = "http://fixit.projects.mrt.ac.lk/Fixit/profile_view_sup";
    String sendDataUrl = "http://fixit.projects.mrt.ac.lk/Fixit/save_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SessionManager sessionManager = new SessionManager(Profile.this);
        username = sessionManager.getUserDetails().get("username");
        id = sessionManager.getUserDetails().get("id");

        DisplayFirstNameTV = (TextView) findViewById(R.id.DisplayFirstNameTV);
        DisplaySecondNameTV = (TextView)findViewById(R.id.DisplaySecondNameTV);
        DisplayContactNumberTV = (TextView)findViewById(R.id.DisplayContactNumberTV);
        DisplayEmailTV = (TextView)findViewById(R.id.DIsplayEmailTV);
        DisplayCompanyTV = (TextView)findViewById(R.id.DisplayCompanyTV);
        DisplayRoleTV = (TextView)findViewById(R.id.DisplayRoleTV);
        totalTV = (TextView)findViewById(R.id.DisplayTotalProjects);
        activeTV = (TextView)findViewById(R.id.DisplayActiveProjects);
        completedTV = (TextView)findViewById(R.id.DisplayCompletedProjects);

        editFirstNameET = (EditText)findViewById(R.id.editFirstNameET);
        editSecondNameET = (EditText)findViewById(R.id.editSecondnameET);
        editContactNumberET = (EditText)findViewById(R.id.editcontactnumberET);
        editEmailET = (EditText)findViewById(R.id.editEmailET);

        editBTN = (Button)findViewById(R.id.editBTN);
        saveBTN = (Button) findViewById(R.id.saveBTN);

        displayPersonalData();
        displayProjectData();

        editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onEdit(DisplayFirstNameTV,editFirstNameET);
                onEdit(DisplaySecondNameTV,editSecondNameET);
                onEdit(DisplayContactNumberTV,editContactNumberET);
                onEdit(DisplayEmailTV,editEmailET);
                editBTN.setVisibility(View.INVISIBLE);
                saveBTN.setVisibility(View.VISIBLE);

            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Email = editEmailET.getText().toString();
                if(!isValidEmail(Email))
                {
                    editEmailET.setError("Invalid Email!");
                }
                else
                {
                    onEditReverse(DisplayFirstNameTV,editFirstNameET);
                    onEditReverse(DisplaySecondNameTV,editSecondNameET);
                    onEditReverse(DisplayContactNumberTV,editContactNumberET);
                    onEditReverse(DisplayEmailTV,editEmailET);
                    saveBTN.setVisibility(View.INVISIBLE);
                    editBTN.setVisibility(View.VISIBLE);
                    sendData();
                }

            }
        });



    }

    // validating email
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //convert textView to editText
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

    public void sendData()
    {
        FirstName = editFirstNameET.getText().toString();
        SecondName = editSecondNameET.getText().toString();
        ContactNumber = editContactNumberET.getText().toString();
        Email = editEmailET.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, sendDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success"))
                {
                    Toast.makeText(Profile.this,"Operation Sussessfull!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Profile.this,"Operation Failed!",Toast.LENGTH_LONG).show();
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
                params.put("first_name",FirstName);
                params.put("second_name",SecondName);
                params.put("contact_number",ContactNumber);
                params.put("email",Email);
                params.put("user_name",username);
                return params;
            }
        };
        MySingleton.getInstance(Profile.this).addToRequestQueue(stringRequest);

    }

    public void displayPersonalData()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getPersonalDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                userEntity = gson.fromJson(response,UserEntity.class);
                DisplayFirstNameTV.setText(userEntity.getFirst_name().toString());
                DisplaySecondNameTV.setText(userEntity.getSecond_name().toString());
                DisplayContactNumberTV.setText(userEntity.getContact_number().toString());
                DisplayEmailTV.setText(userEntity.getEmail().toString());
                DisplayCompanyTV.setText(userEntity.getCompany_name().toString());
                DisplayRoleTV.setText(userEntity.getRole().toString());

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
                params.put("id",id);
                return params;
            }
        };
        MySingleton.getInstance(Profile.this).addToRequestQueue(stringRequest);
    }

    public void displayProjectData()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getProjectDataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    totalTV.setText(jsonObject.getString("totalProjectsCount"));
                    activeTV.setText(jsonObject.getString("ongoingProjectsCount"));
                    completedTV.setText(jsonObject.getString("completedProjectsCount"));

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
                params.put("id",id);
                return params;
            }
        };
        MySingleton.getInstance(Profile.this).addToRequestQueue(stringRequest);
    }
}

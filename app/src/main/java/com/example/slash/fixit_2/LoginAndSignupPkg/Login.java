package com.example.slash.fixit_2.LoginAndSignupPkg;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.SupervisorPkg.ClientHome;
import com.example.slash.fixit_2.EmployeePkg.EmployeeHome;
import com.example.slash.fixit_2.Models.UserEntity;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static Button login;
    EditText usernameET,passwordET;
    AlertDialog.Builder builder;
    String loginUrl = "http://fixit.projects.mrt.ac.lk/Fixit/checklogin";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameET = (EditText)findViewById(R.id.loginUsername);
        passwordET = (EditText)findViewById(R.id.loginPassword);
        login = (Button)findViewById(R.id.login);
        builder = new AlertDialog.Builder(Login.this);

        final SessionManager session = new SessionManager(Login.this);
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = usernameET.getText().toString();
                final String password = passwordET.getText().toString();

                if (username.equals("") || password.equals("")) {
                    builder.setTitle("Login Failed!");
                    builder.setMessage("Please fill all fields...");
                    displayAlert();

                }
                else
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    Gson gson = gsonBuilder.create();
                                    UserEntity userEntity = gson.fromJson(response,UserEntity.class);
                                    String receivedUsername = userEntity.getUser_name();
                                    String company = userEntity.getCompany_name();
                                    String role = userEntity.getRole();
                                    Integer iid = userEntity.getId();
                                    String id = iid.toString();
                                    String firstName = userEntity.getFirst_name();
                                    String lastName = userEntity.getSecond_name();
                                    if(username.equals(receivedUsername))
                                    {
                                        session.createLoginSession(username,company,role,id,firstName,lastName);
                                        if(role.equals("Supervisor"))
                                        {
                                            Intent intent = new Intent(Login.this,ClientHome.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else if(role.equals("Employee"))
                                        {
                                            Intent intent = new Intent(Login.this,EmployeeHome.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                    else
                                    {
                                        builder.setTitle("Login Failed!");
                                        builder.setMessage("Username or password is incorrect!");
                                        displayAlert();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String,String> params = new HashMap<String, String>();
                            params.put("username",username);
                            params.put("password",password);
                            params.put("token",refreshedToken);
                            return params;
                        }
                    };
                    MySingleton.getInstance(Login.this).addToRequestQueue(stringRequest);
                }
            }
        });
    }

    public void displayAlert()
    {
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                usernameET.setText("");
                passwordET.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}

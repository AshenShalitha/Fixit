package com.example.slash.fixit_2.SupervisorPkg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.NotificationPkg.Notifications;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientHome extends AppCompatActivity {

    String url = "http://fixit.projects.mrt.ac.lk/Fixit/delete_token";
    String username;

    Button createProjectBtn,projectBtn,messageBtn,companyBtn,profileBtn,notificationBtn,logoutBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        createProjectBtn = (Button)findViewById(R.id.createProject);
        projectBtn = (Button)findViewById(R.id.projects);
        companyBtn = (Button)findViewById(R.id.companies);
        messageBtn = (Button)findViewById(R.id.message);
        profileBtn = (Button)findViewById(R.id.profile);
        notificationBtn = (Button)findViewById(R.id.aboutUs);
        logoutBTN = (Button)findViewById(R.id.logoutBTN);

        final SessionManager session = new SessionManager(ClientHome.this);
        session.checkLogin();
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        HashMap<String,String> userdata = session.getUserDetails();
        username = userdata.get(SessionManager.KEY_NAME);


        createProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientHome.this,CreateProject.class);
                startActivity(intent);
            }
        });

        projectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientHome.this,Projects.class);
                startActivity(intent);
            }
        });

        companyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientHome.this,Company.class);
                startActivity(intent);
            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientHome.this,Messages.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientHome.this,Profile.class);
                startActivity(intent);
            }
        });

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientHome.this,Notifications.class);
                startActivity(intent);
            }
        });

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                session.logoutUser();
                notifyBackend();
                finish();
            }
        });

    }

    public void notifyBackend()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                params.put("userName",username);

                return params;
            }
        };
        MySingleton.getInstance(ClientHome.this).addToRequestQueue(stringRequest);
    }
}

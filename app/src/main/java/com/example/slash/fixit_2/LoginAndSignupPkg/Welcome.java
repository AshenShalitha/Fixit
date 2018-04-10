package com.example.slash.fixit_2.LoginAndSignupPkg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;

import com.example.slash.fixit_2.SupervisorPkg.ClientHome;
import com.example.slash.fixit_2.EmployeePkg.EmployeeHome;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;

import java.util.HashMap;


public class Welcome extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            SessionManager sessionManager = new SessionManager(Welcome.this);
            @Override
            public void run() {

                if(sessionManager.isLoggedIn())
                {
                    HashMap<String,String> userdata = sessionManager.getUserDetails();
                    String role = userdata.get(SessionManager.KEY_ROLE);
                    if(role.equals("Supervisor"))
                    {
                        Intent homeIntent = new Intent(Welcome.this, ClientHome.class);
                        startActivity(homeIntent);
                        finish();
                    }
                    else if(role.equals("Employee"))
                    {
                        Intent homeIntent = new Intent(Welcome.this, EmployeeHome.class);
                        startActivity(homeIntent);
                        finish();
                    }

                }
                else
                {
                    Intent homeIntent = new Intent(Welcome.this, LoginAndSignup.class);
                    startActivity(homeIntent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);

    }
}

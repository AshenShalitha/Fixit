package com.example.slash.fixit_2.LoginAndSignupPkg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.slash.fixit_2.R;

public class LoginAndSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_signup);
    }

    public void openLogin(View v){
        Intent intent = new Intent(LoginAndSignup.this, Login.class);
        startActivity(intent);
    }

    public void openSignup(View v){
        Intent intent = new Intent(LoginAndSignup.this, CompanySelect.class);
        startActivity(intent);
    }
}

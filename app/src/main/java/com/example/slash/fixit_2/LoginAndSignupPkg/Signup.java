package com.example.slash.fixit_2.LoginAndSignupPkg;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity
{

    EditText firstNameET, lastNameET, emailET,contactNumberET, usernameET, passwordET,confirmPasswordET;
    String firstname,lastname,email,contactnumber,username,password,confirmpassword,userType,companyName;
    Button signup;
    AlertDialog.Builder builder;
    String signupUrl="http://fixit.projects.mrt.ac.lk/Fixit/registerUser";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstNameET = (EditText)findViewById(R.id.c_fname);
        lastNameET = (EditText)findViewById(R.id.c_lname);
        emailET = (EditText)findViewById(R.id.c_email);
        contactNumberET = (EditText)findViewById(R.id.c_contactnumber);
        usernameET = (EditText)findViewById(R.id.c_username);
        passwordET = (EditText)findViewById(R.id.c_password);
        confirmPasswordET = (EditText)findViewById(R.id.c_confirmpassword);
        signup = (Button)findViewById(R.id.signupbtn);
        builder =new AlertDialog.Builder(Signup.this);

        Bundle bundle = getIntent().getExtras();
        companyName = bundle.getString("companyName");
        userType = bundle.getString("userType");

    }

    public void signupClient(View v)
    {
        firstname = firstNameET.getText().toString();
        lastname = lastNameET.getText().toString();
        email = emailET.getText().toString();
        contactnumber = confirmPasswordET.getText().toString();
        username = usernameET.getText().toString();
        password = passwordET.getText().toString();
        confirmpassword = confirmPasswordET.getText().toString();


        if (firstname.equals("") || lastname.equals("") || email.equals("") || contactnumber.equals("") || username.equals("") || password.equals("") || confirmpassword.equals("")) {
            builder.setTitle("Registration Failed!");
            builder.setMessage("Please fill all fields...");
            displayAlert("input error");
        }
        else if(!password.equals(confirmpassword))
        {
            builder.setTitle("Registration Failed!");
            builder.setMessage("Passwords are not matching!");
            displayAlert("input error");
        }
        else if(!isValidEmail(email))
        {
            emailET.setError("Invalid Email!");
        }
        else if(!validate(password))
        {
            passwordET.setError("Password is not valid!");
        }
        else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, signupUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            builder.setTitle("Server Response...");
                            if(response.equals("success"))
                            {
                                builder.setMessage("Registration successful!");
                            }
                            else if(response.equals("failed"))
                            {
                                builder.setMessage("Username is already taken!");
                            }
                            displayAlert(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Signup.this,error.getMessage(),Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("firstname", firstname);
                    params.put("lastname", lastname);
                    params.put("email", email);
                    params.put("contactnumber", contactnumber);
                    params.put("username", username);
                    params.put("password", password);
                    params.put("usertype",userType);
                    params.put("companyname",companyName);
                    return params;
                }
            };
            MySingleton.getInstance(Signup.this).addToRequestQueue(stringRequest);

        }
    }


    public void displayAlert(final String code )
    {

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(code.equals("input error"))
                {
                    passwordET.setText("");
                    confirmPasswordET.setText("");
                }
                else if(code.equals("success"))
                {
                    Intent intent =new Intent(Signup.this,Login.class);
                    startActivity(intent);
                    finish();
                }
                else if(code.equals("failed"))
                {
                    usernameET.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //validating password
    public boolean validate(final String password){

        String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // validating email
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}

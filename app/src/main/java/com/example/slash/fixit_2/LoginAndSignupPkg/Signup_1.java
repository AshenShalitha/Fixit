package com.example.slash.fixit_2.LoginAndSignupPkg;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Signup_1 extends AppCompatActivity{

    private Spinner userTypeSP;
    EditText authKeyET;
    Button proceed;
    ArrayAdapter<CharSequence> adapter_1;
    String companyName;
    String userType;
    AlertDialog.Builder builder;
    String url = "http://fixit.projects.mrt.ac.lk/Fixit/checkKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_1);

        userTypeSP = (Spinner) findViewById(R.id.userTypeSP);
        authKeyET = (EditText) findViewById(R.id.authKeyET);
        proceed = (Button) findViewById(R.id.proceedBT);
        companyName = getIntent().getExtras().getString("companyName");
        builder  = new AlertDialog.Builder(Signup_1.this);

        adapter_1 = ArrayAdapter.createFromResource(this, R.array.userType, android.R.layout.simple_spinner_item);
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSP.setAdapter(adapter_1);

        userTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String authKey  = authKeyET.getText().toString();
                if(authKey.equals(""))
                {
                    Toast.makeText(Signup_1.this,"Please Enter Authentication Key",Toast.LENGTH_LONG).show();
                    authKeyET.setText("");
                }
                else if(userType.equals("Select user type..."))
                {
                    Toast.makeText(Signup_1.this,"Please Select User type!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success"))
                                    {
                                        Intent intent = new Intent(Signup_1.this,Signup.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("userType",userType);
                                        bundle.putString("companyName",companyName);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        builder.setTitle("Registration Error");
                                        builder.setMessage("Authentication Key is incorrect!");
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                authKeyET.setText("");
                                            }
                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Signup_1.this,error.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("companyname",companyName);
                            params.put("authkey",authKey);
                            return params;
                        }
                    };
                    MySingleton.getInstance(Signup_1.this).addToRequestQueue(stringRequest);
                }
            }
        });

    }


}

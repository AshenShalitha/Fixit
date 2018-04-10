package com.example.slash.fixit_2.EmployeePkg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

public class MarkProgressPopUp extends AppCompatActivity {

    EditText progressET;
    Button submitBTN;
    String pId,issueName;
    String url = "http://fixit.projects.mrt.ac.lk/Fixit/progress_save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_progress_pop_up);

        progressET = (EditText)findViewById(R.id.progressET);
        submitBTN = (Button)findViewById(R.id.submitBTN);

        DisplayMetrics dm  = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width,height);
        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        pId = bundle.getString("pId");
        issueName = bundle.getString("issueName");

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String progress = progressET.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success"))
                        {
                            Toast.makeText(MarkProgressPopUp.this,"Operation Successfull!",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(MarkProgressPopUp.this,"Operation Failed!",Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("projectId",pId);
                        params.put("issueName",issueName);
                        params.put("progress",progress);
                        return params;
                    }
                };
                MySingleton.getInstance(MarkProgressPopUp.this).addToRequestQueue(stringRequest);
            }
        });
    }
}

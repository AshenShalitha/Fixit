package com.example.slash.fixit_2.SupervisorPkg;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;

import java.util.HashMap;
import java.util.Map;

import static com.example.slash.fixit_2.R.id.ratingBar;

/**
 * Created by slash on 1/20/2018.
 */

public class GradePopUp extends Activity{

    Button submitBTN,reasonSubmitBTN;
    EditText reasonET;
    TextView txt;
    RatingBar ratingbar;
    String reason;
    String projectId;
    String ratingUrl = "http://fixit.projects.mrt.ac.lk/Fixit/rate";
    String reviseUrl = "http://fixit.projects.mrt.ac.lk/Fixit/revise";
    String notificationUrl = "http://fixit.projects.mrt.ac.lk/Fixit/notify_employee";
    String supervisorName, selectedEmployeeUserName, projectname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_pop_up);

        reasonET = (EditText)findViewById(R.id.reason);
        reasonSubmitBTN = (Button)findViewById(R.id.reasonsubmitBTN);
        submitBTN = (Button)findViewById(R.id.submitBTN);
        ratingbar = (RatingBar)findViewById(ratingBar);
        txt = (TextView)findViewById(R.id.textView4);


        DisplayMetrics dm  = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width,height);

        getWindow().setLayout((int)(width*.8),(int)(height*.6));


        SessionManager sessionManager = new SessionManager(GradePopUp.this);
        final String fname = sessionManager.getUserDetails().get("firstName");
        final String lname = sessionManager.getUserDetails().get("lastName");
        supervisorName = fname+" "+lname;


        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        String type = bundle.getString("type");
        projectId = bundle.getString("projectId");
        selectedEmployeeUserName = bundle.getString("username");
        projectname = bundle.getString("projectname");
        if(type.equals("revise"))
        {
            reasonET.setVisibility(View.VISIBLE);
            reasonSubmitBTN.setVisibility(View.VISIBLE);
            ratingbar.setVisibility(View.INVISIBLE);
            submitBTN.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.INVISIBLE);

        }

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float value = ratingbar.getRating();
                Integer iValue = value.intValue();
                String rating = String.valueOf(iValue);
                sendData(ratingUrl,rating);
                Toast.makeText(GradePopUp.this,"Employee is rated Successfully!",Toast.LENGTH_LONG).show();
                sendData(reviseUrl,reason);
                final String message = supervisorName+" marked "+projectname+" as completed and rated you "+rating+" stars!";
                sendNotification(message);
                finish();

            }
        });

        reasonSubmitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason = reasonET.getText().toString();
                Toast.makeText(GradePopUp.this,"Project is to emloyee for revision!",Toast.LENGTH_LONG).show();
                sendData(reviseUrl,reason);
                final String message = supervisorName+" requested "+projectname+" project for modification";
                sendNotification(message);
                finish();
            }
        });



    }

    public void sendData(String url, final String data)
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
                params.put("data",data);
                params.put("projectId",projectId);

                return params;
            }
        };
        MySingleton.getInstance(GradePopUp.this).addToRequestQueue(stringRequest);
    }

    public void sendNotification(final String message)
    {
        Toast.makeText(GradePopUp.this,message,Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(GradePopUp.this).addToRequestQueue(stringRequest);
    }
}

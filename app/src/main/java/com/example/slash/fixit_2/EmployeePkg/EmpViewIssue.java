package com.example.slash.fixit_2.EmployeePkg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.SupervisorPkg.ViewImagePopUp;
import com.example.slash.fixit_2.Models.Image;
import com.example.slash.fixit_2.Models.ProgressEntity;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmpViewIssue extends AppCompatActivity {

    TextView displayNameTV,displayDescriptionTV;
    Button viewImageBTN,markProgressBTN;
    ListView progressLV;
    String imageNameUrl = "http://fixit.projects.mrt.ac.lk/Fixit/getIssue";
    String issueDetailsUrl = "";
    String getProgressUrl = "http://fixit.projects.mrt.ac.lk/Fixit/progress_view";
    String pId,issueName,description,imageName;
    Image imageEntity;
    String imgURL;
    String jsonURL;
    ArrayAdapter<String> adapter;
    List<String> progressList;
    ProgressEntity[] progressEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_view_issue);
        displayNameTV = (TextView)findViewById(R.id.displayIssueNameTV);
        displayDescriptionTV = (TextView)findViewById(R.id.displayDescriptionTV);
        viewImageBTN = (Button)findViewById(R.id.viewImageBTN);
        markProgressBTN = (Button)findViewById(R.id.markProgressBTN);
        progressLV = (ListView)findViewById(R.id.progressLV);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        pId = bundle.getString("pId");
        issueName = bundle.getString("issueName");
        description = bundle.getString("description");

        displayNameTV.setText(issueName);
        displayDescriptionTV.setText(description);


        //requesting image name
        StringRequest stringRequest = new StringRequest(Request.Method.POST, imageNameUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Image imageEntity = gson.fromJson(response,Image.class);
                imageName = imageEntity.getName();
                imgURL = "http://fixit.projects.mrt.ac.lk/Fixit/files/images/"+imageName;
                jsonURL = "http://fixit.projects.mrt.ac.lk/Fixit/files/json/"+imageName+".json";

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("projectId",pId);
                params.put("issueName",issueName);
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(EmpViewIssue.this).addToRequestQueue(stringRequest);


        //requesting issue details
//        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, issueDetailsUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("pId",pId);
//                params.put("issueName",issueName);
//                return params;
//            }
//        };
//        MySingleton.getInstance(ViewIssue.this).addToRequestQueue(stringRequest1);

        getProgress();

        viewImageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmpViewIssue.this,ViewImagePopUp.class);
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl",imgURL);
                bundle.putString("jsonUrl",jsonURL);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        markProgressBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmpViewIssue.this,MarkProgressPopUp.class);
                Bundle bundle = new Bundle();
                bundle.putString("pId",pId);
                bundle.putString("issueName",issueName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    public void getProgress()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getProgressUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressList = new ArrayList<String>();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                progressEntities = gson.fromJson(response,ProgressEntity[].class);
                int l = progressEntities.length;
                Toast.makeText(EmpViewIssue.this,progressEntities[0].getDate()+" : "+progressEntities[0].getProgress(),Toast.LENGTH_LONG).show();
                for(int i=0;i<l;i++)
                {
                    progressList.add(progressEntities[i].getDate()+"  :   "+progressEntities[i].getProgress());
                }
                adapter = new ArrayAdapter<String>(EmpViewIssue.this,android.R.layout.simple_list_item_1,progressList);
                progressLV.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(EmpViewIssue.this).addToRequestQueue(stringRequest);
    }


}



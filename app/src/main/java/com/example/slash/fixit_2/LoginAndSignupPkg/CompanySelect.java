package com.example.slash.fixit_2.LoginAndSignupPkg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Models.CompanyEntity;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class CompanySelect extends Activity {

    ArrayList<String> companyList;
    ListView companyListLV;
    String companyName;
    String url = "http://fixit.projects.mrt.ac.lk/Fixit/company_list_get";
    CompanyEntity[] companyEntities;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_select);

         companyListLV = (ListView)findViewById(R.id.companyList);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        companyList = new ArrayList<String>();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        companyEntities = gson.fromJson(response,CompanyEntity[].class);
                        int l = companyEntities.length;
                        for(int i=0;i<l;i++)
                        {
                            companyList.add(companyEntities[i].getName());
                        }
                        adapter = new ArrayAdapter<String>(CompanySelect.this,android.R.layout.simple_list_item_1,companyList);
                        companyListLV.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(CompanySelect.this).addToRequestQueue(stringRequest);



        companyListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                companyName = tv.getText().toString();
                Intent intent = new Intent(getApplicationContext(),Signup_1.class);
                intent.putExtra("companyName",companyName);
                startActivity(intent);

            }
        });

    }

}

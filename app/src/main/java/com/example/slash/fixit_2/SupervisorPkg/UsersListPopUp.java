package com.example.slash.fixit_2.SupervisorPkg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.ChatPkg.ChatView;
import com.example.slash.fixit_2.Models.UserEntity;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsersListPopUp extends AppCompatActivity {

    ListView usersLV;
    List<String> usersList;
    ArrayAdapter<String> adapter;
    UserEntity[] users;
    String url = "http://fixit.projects.mrt.ac.lk/Fixit/user_list_get_company";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list_pop_up);

        DisplayMetrics dm  = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width,height);
        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        usersLV = (ListView)findViewById(R.id.usersLV);
        SessionManager sessionManager = new SessionManager(UsersListPopUp.this);
        final String companyName = sessionManager.getUserDetails().get("company");
        final String fname = sessionManager.getUserDetails().get("firstName");
        final String lname = sessionManager.getUserDetails().get("lastName");
        final String currentUser = fname + " " + lname;

        StringRequest stringRequest = new StringRequest( Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                usersList = new ArrayList<String>();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                users = gson.fromJson(response,UserEntity[].class);
                int l = users.length;
                for(int i=0;i<l;i++)
                {
                    String name = users[i].getFirst_name()+" "+users[i].getSecond_name();
                    if(name.equals(currentUser)||name.equals("Admin Admin"))
                    {
                        continue;
                    };
                    usersList.add(name);
                }
                adapter = new ArrayAdapter<String>(UsersListPopUp.this,android.R.layout.simple_list_item_1,usersList);
                usersLV.setAdapter(adapter);

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
                params.put("company",companyName);
                return params;
            }
        };
        MySingleton.getInstance(UsersListPopUp.this).addToRequestQueue(stringRequest);

        usersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                String name = tv.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("receiver",name);
                Intent intent = new Intent(UsersListPopUp.this, ChatView.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}

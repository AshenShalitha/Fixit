package com.example.slash.fixit_2.NotificationPkg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Models.NotificationEntity;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notifications extends AppCompatActivity {

    ListView notificationLV;
    List<String> notificationList;
    ArrayAdapter<String> adapter;
    NotificationEntity[] notificationEntity;

    ListView unreadNotificationLV;
    List<String> unreadNotificationList;
    ArrayAdapter<String> adapter1;
    NotificationEntity[] notificationEntity1;
    String username;

    String readUrl = "http://fixit.projects.mrt.ac.lk/Fixit/get_old_notifications";
    String unreadUrl = "http://fixit.projects.mrt.ac.lk/Fixit/get_new_notifications";
    String url = "http://fixit.projects.mrt.ac.lk/Fixit/read_notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        final TextView tv1 = (TextView)findViewById(R.id.tv1);
        final TextView tv2 = (TextView)findViewById(R.id.tv2);
        notificationLV = (ListView)findViewById(R.id.notificationLV);
        unreadNotificationLV = (ListView)findViewById(R.id.unreadnotificationLV);
        SessionManager sessionManager = new SessionManager(Notifications.this);
        username = sessionManager.getUserDetails().get("username");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, readUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                notificationList = new ArrayList<String>();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                notificationEntity = gson.fromJson(response,NotificationEntity[].class);
                int l = notificationEntity.length;
                for(int i=0;i<l;i++)
                {
                    notificationList.add(notificationEntity[i].getMessage()+" : "+notificationEntity[i].getTimeStamp());
                }
                adapter = new ArrayAdapter<String>(Notifications.this,android.R.layout.simple_list_item_1,notificationList);
                notificationLV.setAdapter(adapter);

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
        MySingleton.getInstance(Notifications.this).addToRequestQueue(stringRequest);




        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, unreadUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                unreadNotificationList = new ArrayList<String>();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                notificationEntity1 = gson.fromJson(response,NotificationEntity[].class);
                int l = notificationEntity1.length;
                if(l!=0)
                {
                    tv1.setVisibility(View.VISIBLE);
                    unreadNotificationLV.setVisibility(View.VISIBLE);
                }
                for(int i=0;i<l;i++)
                {
                    unreadNotificationList.add(notificationEntity1[i].getMessage()+" : "+notificationEntity1[i].getTimeStamp());
                }
                adapter1 = new ArrayAdapter<String>(Notifications.this,android.R.layout.simple_list_item_1,unreadNotificationList);
                unreadNotificationLV.setAdapter(adapter1);

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
        MySingleton.getInstance(Notifications.this).addToRequestQueue(stringRequest1);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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
        MySingleton.getInstance(Notifications.this).addToRequestQueue(stringRequest);

    }
}

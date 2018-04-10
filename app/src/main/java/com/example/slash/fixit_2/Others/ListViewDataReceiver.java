package com.example.slash.fixit_2.Others;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Models.ProjectEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by slash on 1/17/2018.
 */

public class ListViewDataReceiver {

    ListView LV;
    JSONArray jsonArray;
    Context context;
    ArrayList<String> list;
    ArrayAdapter<String> arrayAdapter;
    ProjectEntity[] projectEntity;

    public ListViewDataReceiver(Context context,ListView LV)
    {
        this.context = context;
        this.LV = LV;
    }

    public void in(String url, final String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list = new ArrayList<String>();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                projectEntity = gson.fromJson(response,ProjectEntity[].class);
                int l=projectEntity.length;
                for(int i=0;i<l;i++)
                {
                    list.add(projectEntity[i].getName());
                }
                arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
                LV.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id",id);

                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    public List<ProjectEntity> passArray()
    {
        List<ProjectEntity> p = new ArrayList<>(Arrays.asList(projectEntity));
        return p;
    }

}

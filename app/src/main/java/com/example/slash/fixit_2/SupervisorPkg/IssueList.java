package com.example.slash.fixit_2.SupervisorPkg;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Models.IssueEntity;
import com.example.slash.fixit_2.Models.ProjectEntity;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IssueList extends Fragment {

    View rootView;
    Button addIssueBTN;
    ListView issueListLV;
    String url = "http://fixit.projects.mrt.ac.lk/Fixit/get_issue_list";
    String supervisorUsername;
    Integer selectedProjectId;
    List<String> issueList;
    List<String> issueDescripton;
    IssueEntity[] issueEntity;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_issue_list, container, false);

        issueListLV = (ListView)rootView.findViewById(R.id.reportedIssues);
        addIssueBTN = (Button)rootView.findViewById(R.id.addIssueBTN);

        SessionManager sessionManager = new SessionManager(getActivity());
        supervisorUsername = sessionManager.getUserDetails().get("username");

        Bundle bundle = getActivity().getIntent().getExtras();
        final ProjectEntity selectedProject = bundle.getParcelable("selectedProject");
        String type = bundle.getString("type");
        selectedProjectId = selectedProject.getId();

        if(type.equals("completed")||type.equals("sfg"))
            addIssueBTN.setVisibility(View.INVISIBLE);

        getIssueList();

        issueListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView v = (TextView)view;
                Intent intent = new Intent(getActivity(),ViewIssue.class);
                Bundle bundle = new Bundle();
                bundle.putString("pId",""+selectedProjectId);
                bundle.putString("issueName",v.getText().toString());
                bundle.putString("description",issueDescripton.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        addIssueBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CreateIssue.class);
                intent.putExtra("pId",""+selectedProjectId);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void getIssueList()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                issueList = new ArrayList<String>();
                issueDescripton = new ArrayList<String>();
                GsonBuilder gsonBuilder  = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                issueEntity = gson.fromJson(response,IssueEntity[].class);
                int l=issueEntity.length;
                for(int i=0;i<l;i++)
                {
                    issueList.add(issueEntity[i].getName());
                    issueDescripton.add(issueEntity[i].getDescription());
                }
                arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,issueList);
                issueListLV.setAdapter(arrayAdapter);
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
                params.put("id",selectedProjectId.toString());
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}

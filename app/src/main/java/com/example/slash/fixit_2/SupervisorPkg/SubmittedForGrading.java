package com.example.slash.fixit_2.SupervisorPkg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.slash.fixit_2.Others.ListViewDataReceiver;
import com.example.slash.fixit_2.Models.ProjectEntity;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;

import java.util.List;


public class SubmittedForGrading extends Fragment {

    View rootView;
    ListView submittedForGradingLV;
    String url = "http://fixit.projects.mrt.ac.lk/Fixit/supervisor_projectlist_get_sfg";
    String type = "sfg";
    List<ProjectEntity> projectList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_submitted_for_grading, container, false);

        SessionManager sessionManager = new SessionManager(getActivity());
        final String id = sessionManager.getUserDetails().get("id");

        submittedForGradingLV = (ListView)rootView.findViewById(R.id.submittedforgradingLV);
        final ListViewDataReceiver listViewDataReceiver = new ListViewDataReceiver(getActivity(),submittedForGradingLV);
        listViewDataReceiver.in(url,id);

        submittedForGradingLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                projectList = listViewDataReceiver.passArray();
                ProjectEntity selectedProject = projectList.get(i);
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedProject",selectedProject);
                bundle.putString("type",type);
                Intent intent = new Intent(getActivity(),ViewProject.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return rootView;
    }

}

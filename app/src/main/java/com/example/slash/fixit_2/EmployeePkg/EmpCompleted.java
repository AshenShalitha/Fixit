package com.example.slash.fixit_2.EmployeePkg;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.slash.fixit_2.Others.ListViewDataReceiver;
import com.example.slash.fixit_2.Models.ProjectEntity;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmpCompleted extends Fragment {

    View rootView;
    ListView completedLV;
    String url = "http://fixit.projects.mrt.ac.lk/Fixit/employee_projectlist_get_completed";
    String type = "completed";
    List<ProjectEntity> projectList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_emp_completed, container, false);

        SessionManager sessionManager = new SessionManager(getActivity());
        String id = sessionManager.getUserDetails().get("id");


        completedLV = (ListView)rootView.findViewById(R.id.empcompletedLV);
        final ListViewDataReceiver listViewDataReceiver = new ListViewDataReceiver(getActivity(),completedLV);
        listViewDataReceiver.in(url,id);

        completedLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                projectList = listViewDataReceiver.passArray();
                ProjectEntity selectedProject = projectList.get(i);
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedProject",selectedProject);
                bundle.putString("type",type);
                Intent intent = new Intent(getActivity(),EmpProjectDetailsTab.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return rootView;
    }

}

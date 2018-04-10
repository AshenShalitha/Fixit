package com.example.slash.fixit_2.EmployeePkg;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;

public class EmpViewProjects extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_view_projects);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String name = sessionManager.getUserDetails().get("firstName")+" "+sessionManager.getUserDetails().get("lastName");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name+"'s Projects");
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    EmpOngoing empOngoing = new EmpOngoing();
                    return empOngoing;
                case 1:
                    EmpLateSubmissions empLateSubmissions = new EmpLateSubmissions();
                    return empLateSubmissions;
                case 2:
                    EmpSendForRevision empSendForRevivision = new EmpSendForRevision();
                    return empSendForRevivision;
                case 3:
                    EmpSubmitted empSubmitted = new EmpSubmitted();
                    return empSubmitted;
                case 4:
                    EmpCompleted empCompleted = new EmpCompleted();
                    return empCompleted;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";
                case 1:
                    return "";
                case 2:
                    return "";
                case 3:
                    return "";
                case 4:
                    return "";
            }
            return null;
        }
    }
}

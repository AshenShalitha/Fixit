package com.example.slash.fixit_2.SupervisorPkg;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;

public class Projects extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

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
                    Ongoing ongoing = new Ongoing();
                    return ongoing;
                case 1:
                    Completed completed = new Completed();
                    return completed;
                case 2:
                    Revised revised = new Revised();
                    return revised;
                case 3:
                    LateSubmissions lateSubmissions = new LateSubmissions();
                    return lateSubmissions;
                case 4:
                    SubmittedForGrading submittedForGrading = new SubmittedForGrading();
                    return submittedForGrading;
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

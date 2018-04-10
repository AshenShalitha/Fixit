package com.example.slash.fixit_2.Others;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.slash.fixit_2.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.slash.fixit_2.Others.Constraints.FIRST_COLUMN;
import static com.example.slash.fixit_2.Others.Constraints.SECOND_COLUMN;

/**
 * Created by slash on 2/10/2018.
 */

public class TwoColumnListViewAdapter extends BaseAdapter{

    Activity activity;
    ArrayList<HashMap<String,String>> list;
    TextView txtFirst;
    TextView txtSecond;

    public TwoColumnListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = activity.getLayoutInflater();

        if(view==null)
        {
            view = inflater.inflate(R.layout.two_column_layout,null);
            txtFirst = (TextView)view.findViewById(R.id.nameTV);
            txtSecond = (TextView)view.findViewById(R.id.ratingsTV);
        }
        HashMap<String,String> map = list.get(i);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));

        return view;
    }
}

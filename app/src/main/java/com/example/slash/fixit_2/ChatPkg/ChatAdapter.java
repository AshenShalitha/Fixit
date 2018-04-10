package com.example.slash.fixit_2.ChatPkg;

import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by slash on 1/27/2018.
 */

public class ChatAdapter extends ArrayAdapter<DataProvider> {

    private List<DataProvider> chat_list = new ArrayList<DataProvider>();
    private TextView CHAT_TXT;
    private TextView TIMESTAMP;
    private Layout singleMessage;
    Context CTX;
    String currentUser;


    public ChatAdapter(Context context, int resource) {
        super(context, resource);
        CTX = context;
        // TODO Auto-generated constructor stub
    }
    @Override
    public void add(DataProvider object) {
        // TODO Auto-generated method stub
        chat_list.add(object);
        super.add(object);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return chat_list.size();
    }
    @Override
    public DataProvider getItem(int position) {
        // TODO Auto-generated method stub
        return chat_list.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SessionManager sessionManager = new SessionManager(CTX);
        final String fname = sessionManager.getUserDetails().get("firstName");
        final String lname = sessionManager.getUserDetails().get("lastName");
        currentUser = fname+" "+lname;

        if(convertView == null)
        {
            LayoutInflater inflator = (LayoutInflater) CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.single_message,parent,false);
        }
        CHAT_TXT = (TextView) convertView.findViewById(R.id.messageText);
        TIMESTAMP = (TextView)convertView.findViewById(R.id.time);

        String Message,timeStamp;
        boolean POSITION;
        DataProvider provider = getItem(position);
        Message = provider.message;
        timeStamp = provider.timestamp;
        POSITION = provider.position;
        CHAT_TXT.setText(Message);
        TIMESTAMP.setText(timeStamp);



        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        if(!POSITION)
        {
            params.gravity = Gravity.RIGHT;
        }
        else
        {
            params.gravity = Gravity.LEFT;
        }
        CHAT_TXT.setLayoutParams(params);
        TIMESTAMP.setLayoutParams(params);
        return convertView;
    }

}

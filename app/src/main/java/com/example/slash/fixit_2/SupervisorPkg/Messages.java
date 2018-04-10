package com.example.slash.fixit_2.SupervisorPkg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.slash.fixit_2.Models.MessageEntity;
import com.example.slash.fixit_2.R;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity {

    ListView messageLV;
    Button startNewBTN;
    List<String> messageList;
    ArrayAdapter<String> adapter;
    MessageEntity messages;
    String url = "http://fixit.projects.mrt.ac.lk/Fixit/user_list_get_company";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        messageLV = (ListView)findViewById(R.id.messagesLV);
        startNewBTN = (Button)findViewById(R.id.startNewBTN);
        messageList = new ArrayList<String>();
        startNewBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Messages.this,UsersListPopUp.class);
                startActivity(intent);
            }
        });

    }
}

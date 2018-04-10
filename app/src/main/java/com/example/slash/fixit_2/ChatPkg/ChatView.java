package com.example.slash.fixit_2.ChatPkg;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.slash.fixit_2.Models.MessageEntity;
import com.example.slash.fixit_2.Others.MySingleton;
import com.example.slash.fixit_2.R;
import com.example.slash.fixit_2.Others.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChatView extends AppCompatActivity {

    ListView listview;
    EditText chatTextET;
    TextView recipient;
    String chatText;
    Button SEND;
    boolean position = false;
    ChatAdapter adapter;
    Context ctx = this;
    String loadMessages = "http://fixit.projects.mrt.ac.lk/Fixit/loadMessages";
    String sendMessage = "http://fixit.projects.mrt.ac.lk/Fixit/sendMessage";
    MessageEntity[] messageEntity;
    String currentUser,receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        SessionManager sessionManager = new SessionManager(ChatView.this);
        final String fname = sessionManager.getUserDetails().get("firstName");
        final String lname = sessionManager.getUserDetails().get("lastName");
        currentUser = fname+" "+lname;

        listview = (ListView) findViewById(R.id.chat_list_view);
        chatTextET = (EditText) findViewById(R.id.chatTxt);
        SEND = (Button) findViewById(R.id.send_button);
        recipient = (TextView)findViewById(R.id.recipient);
        Bundle bundle = getIntent().getExtras();
        receiver = bundle.getString("receiver");

        recipient.setText(receiver);

        adapter = new ChatAdapter(ctx,R.layout.single_message);

        listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                scrollMyListViewToBottom();
            }
        });

        SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatText = chatTextET.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, sendMessage, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        chatTextET.setText("");
                        LoadMessages();
                        listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                        adapter.registerDataSetObserver(new DataSetObserver() {
                            @Override
                            public void onChanged() {
                                super.onChanged();
                                scrollMyListViewToBottom();
                            }
                        });
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
                        params.put("receiver",receiver);
                        params.put("message",chatText);
                        params.put("sender",currentUser);
                        return params;
                    }
                };
                MySingleton.getInstance(ctx).addToRequestQueue(stringRequest);
            }
        });


        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, loadMessages, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                messageEntity = gson.fromJson(response,MessageEntity[].class);
                int l = messageEntity.length;
                String sendr;
                String message;
                String timeStamp;
                DataProvider dataProvider;
                for(int i=0;i<l;i++)
                {
                    sendr = messageEntity[i].getSender();
                    message = messageEntity[i].getMessageBody();
                    timeStamp = messageEntity[i].getTimeStamp();
                    if(sendr.equals(currentUser)) {
                        position=false;
                    }
                    else{
                        position=true;
                    }
                    dataProvider=new DataProvider(position,message,sendr,timeStamp);
                    adapter.add(dataProvider);
                    listview.setAdapter(adapter);
                }
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
                params.put("receiver",receiver);
                params.put("sender",currentUser);
                return params;
            }
        };
        stringRequest1.setShouldCache(false);
        MySingleton.getInstance(ctx).addToRequestQueue(stringRequest1);

    }

    public void LoadMessages()
    {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, loadMessages, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                messageEntity = gson.fromJson(response,MessageEntity[].class);
                int l = messageEntity.length;
                String sendr;
                String message;
                String timeStamp;
                DataProvider dataProvider;
                for(int i=0;i<l;i++)
                {
                    sendr = messageEntity[i].getSender();
                    message = messageEntity[i].getMessageBody();
                    timeStamp = messageEntity[i].getTimeStamp();
                    if(sendr.equals(currentUser)) {
                        position=false;
                    }
                    else{
                        position=true;
                    }
                    dataProvider=new DataProvider(position,message,sendr,timeStamp);
                    adapter.add(dataProvider);
                    listview.setAdapter(adapter);
                }
                Toast.makeText(ChatView.this,currentUser+" "+receiver,Toast.LENGTH_LONG).show();
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
                params.put("receiver",receiver);
                params.put("sender",currentUser);
                return params;
            }
        };
        stringRequest1.setShouldCache(false);
        MySingleton.getInstance(ctx).addToRequestQueue(stringRequest1);
    }

    private void scrollMyListViewToBottom() {
        listview.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listview.setSelection(adapter.getCount() - 1);
            }
        });
    }


}

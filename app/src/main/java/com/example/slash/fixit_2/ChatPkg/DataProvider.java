package com.example.slash.fixit_2.ChatPkg;

/**
 * Created by slash on 1/27/2018.
 */

public class DataProvider {

    public boolean position;
    public String message;
    public String sender;
    public String receiver;
    public String timestamp;
    public DataProvider(boolean position, String message, String sender, String timestamp) {
        super();
        this.position = position;
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
    }

}

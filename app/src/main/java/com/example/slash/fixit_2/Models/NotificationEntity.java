package com.example.slash.fixit_2.Models;

import java.sql.Timestamp;



public class NotificationEntity {

    private int id;
  private String message;
  private String timeStamp;
  boolean isDelivered;
  String receiver;
  boolean isRead;
  
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getTimeStamp() {
	return timeStamp;
}
public void setTimeStamp(String timeStamp) {
	this.timeStamp = timeStamp;
}
public boolean isDelivered() {
	return isDelivered;
}
public void setDelivered(boolean isDelivered) {
	this.isDelivered = isDelivered;
}
public String getReceiver() {
	return receiver;
}
public void setReceiver(String receiver) {
	this.receiver = receiver;
}
public boolean isRead() {
	return isRead;
}
public void setRead(boolean isRead) {
	this.isRead = isRead;
}

  

}

package com.example.slash.fixit_2.Models;


public class ProgressEntity {

	private int Id;
	private int issue_Id;
	private String progress;
	private String date;
	
	
	
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	
	
	public int getIssue_Id() {
		return issue_Id;
	}
	public void setIssue_Id(int issue_Id) {
		this.issue_Id = issue_Id;
	}
	
	
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}

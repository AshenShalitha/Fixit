package com.example.slash.fixit_2.Models;

/**
 * Created by slash on 1/26/2018.
 */

public class IssueEntity {

    private int id;
    private String name;
    private String description;
    private int project_Id;
    private String status;



    public int getId() {
        return id;
    }
    public void setId(int id) {
        id = id;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getProject_Id() {
        return project_Id;
    }
    public void setProject_Id(int project_Id) {
        this.project_Id = project_Id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}


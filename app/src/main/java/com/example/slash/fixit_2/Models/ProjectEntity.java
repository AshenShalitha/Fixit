package com.example.slash.fixit_2.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by slash on 1/21/2018.
 */

public class ProjectEntity implements Parcelable {

    private int id;
    private String name;
    private int supervisor_id;
    private int employee_id;
    private String description;
    private String created_date;
    private String deadline_date;
    private String status;
    private String location;
    private int blue_print_id;
    private String reject_reason;

    protected ProjectEntity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        supervisor_id = in.readInt();
        employee_id = in.readInt();
        description = in.readString();
        created_date = in.readString();
        deadline_date = in.readString();
        status = in.readString();
        location = in.readString();
        blue_print_id = in.readInt();
        reject_reason = in.readString();
    }

    public static final Creator<ProjectEntity> CREATOR = new Creator<ProjectEntity>() {
        @Override
        public ProjectEntity createFromParcel(Parcel in) {
            return new ProjectEntity(in);
        }

        @Override
        public ProjectEntity[] newArray(int size) {
            return new ProjectEntity[size];
        }
    };

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
    public int getSupervisor_id() {
        return supervisor_id;
    }
    public void setSupervisor_id(int supervisor_id) {
        this.supervisor_id = supervisor_id;
    }
    public int getEmployee_id() {
        return employee_id;
    }
    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCreated_date() {
        return created_date;
    }
    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getDeadline_date() {
        return deadline_date;
    }
    public void setDeadline_date(String deadline_date) {
        this.deadline_date = deadline_date;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public int getBlue_print_id() {
        return blue_print_id;
    }
    public void setBlue_print_id(int blue_print_id) {
        this.blue_print_id = blue_print_id;
    }
    public String getReject_reason() {
        return reject_reason;
    }
    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(supervisor_id);
        parcel.writeInt(employee_id);
        parcel.writeString(description);
        parcel.writeString(created_date);
        parcel.writeString(deadline_date);
        parcel.writeString(status);
        parcel.writeString(location);
        parcel.writeInt(blue_print_id);
        parcel.writeString(reject_reason);
    }
}

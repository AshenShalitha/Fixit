package com.example.slash.fixit_2.Models;

/**
 * Created by slash on 1/21/2018.
 */

public class CompanyEntity {

    private int Id;
    private String name;
    private String security_key;
    private String status;
    private String email;
    private String confirm_email;
    private String contact_no;
    private String address;
    private String admin_password;

    private void checkEmail() {
        if(this.email == null || this.confirm_email == null){
            return;
        }else if(!this.email.equals(confirm_email)){
            this.confirm_email = null;
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getContact_no() {
        return contact_no;
    }
    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSecurity_key() {
        return security_key;
    }
    public void setSecurity_key(String security_key) {
        this.security_key = security_key;
    }
    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        checkEmail();
    }

    public String getConfirm_email() {
        return confirm_email;
    }

    public void setConfirm_email(String confirm_email) {
        this.confirm_email = confirm_email;
        checkEmail();
    }

    public String getAdminpassword() {
        return admin_password;
    }

    public void setAdminpassword(String adminpassword) {
        this.admin_password = adminpassword;
    }

}

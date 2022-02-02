package com.bob.vendorapp.datamodel;

public class UsersModel {
    String firstname,uid;

    public UsersModel() {
    }

    public UsersModel(String firstname, String uid) {
        this.firstname = firstname;
        this.uid = uid;
    }

    public String getfirstname() {
        return firstname;
    }

    public void setfirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

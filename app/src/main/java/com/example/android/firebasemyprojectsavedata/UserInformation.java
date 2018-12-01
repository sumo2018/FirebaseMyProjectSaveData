package com.example.android.firebasemyprojectsavedata;

public class UserInformation {
    public String name;
    public String phone;
    public String request;
    public String table;
    public String date;
    public String time;

    public UserInformation() {

    }

    public UserInformation(String name, String phone, String request, String table, String date, String time) {
        this.name = name;
        this.phone = phone;
        this.request = request;
        this.table = table;
        this.date = date;
        this.time = time;
    }
}


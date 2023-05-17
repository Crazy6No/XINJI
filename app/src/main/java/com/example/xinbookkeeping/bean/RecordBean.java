package com.example.xinbookkeeping.bean;

import android.text.TextUtils;

public class RecordBean {

    private int id;
    private String userId;
    private String type;
    private String money;
    private String state;
    private String recordDesc;
    private int year;
    private int month;
    private int day;
    private long time;

    public RecordBean(int id, String userId, String type, String money, String state, String recordDesc, int year, int month, int day, long time) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.money = money;
        this.state = state;
        this.recordDesc = recordDesc;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRecordDesc() {
        if (TextUtils.isEmpty(recordDesc)) {
            recordDesc = "无";
        }
        return recordDesc;
    }

    public void setRecordDesc(String recordDesc) {
        this.recordDesc = recordDesc;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

package com.example.xinbookkeeping.bean;

public class CompanyBean {

    private int id;

    private String unitname;
    private String uid;
    private String teleNum;
    private String upwd;

    public CompanyBean(int id, String unitname, String uid, String teleNum, String upwd) {
        this.id = id;
        this.unitname = unitname;
        this.uid = uid;
        this.teleNum = teleNum;
        this.upwd = upwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTeleNum() {
        return teleNum;
    }

    public void setTeleNum(String teleNum) {
        this.teleNum = teleNum;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }
}

package com.example.xinbookkeeping.bean;

public class UserBean {

    private int id;

    private String nickname;
    private String uid;
    private String teleNum;
    private String upwd;
    private String unitName;
    private String companyId;

    public UserBean(int id, String nickname, String uid, String teleNum, String upwd) {
        this.id = id;
        this.nickname = nickname;
        this.uid = uid;
        this.teleNum = teleNum;
        this.upwd = upwd;
    }

    public UserBean(int id, String nickname, String uid, String teleNum, String upwd, String unitName, String companyId) {
        this.id = id;
        this.nickname = nickname;
        this.uid = uid;
        this.teleNum = teleNum;
        this.upwd = upwd;
        this.unitName = unitName;
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}

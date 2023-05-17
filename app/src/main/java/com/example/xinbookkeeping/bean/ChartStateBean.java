package com.example.xinbookkeeping.bean;

public class ChartStateBean {
    private String state;
    private Integer num;
    private Double money;

    private Double ratio;

    public ChartStateBean(String state, Integer num, Double money, Double ratio) {
        this.state = state;
        this.num = num;
        this.money = money;
        this.ratio = ratio;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

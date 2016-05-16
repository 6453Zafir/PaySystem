package com.example.admin.payment;

/**
 * Created by 马二爷 on 2016/5/16.
 */
public class PhoneBill {

    private String phonenumber;
    private int notPaidNumThisYear;
    private float owingMoneyBeforeThisYear;
    private int thisMonthMinutes;
    private float moneyToPay;

    public PhoneBill(String phonenumber, int notPaidNumThisYear, float owingMoneyBeforeThisYear, int thisMonthMinutes,float moneyToPay) {
        this.phonenumber = phonenumber;
        this.notPaidNumThisYear = notPaidNumThisYear;
        this.owingMoneyBeforeThisYear = owingMoneyBeforeThisYear;
        this.thisMonthMinutes = thisMonthMinutes;
        this.moneyToPay=moneyToPay;
    }

    public float getMoneyToPay() {
        return moneyToPay;
    }

    public void setMoneyToPay(float moneyToPay) {
        this.moneyToPay = moneyToPay;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getNotPaidNumThisYear() {
        return notPaidNumThisYear;
    }

    public void setNotPaidNumThisYear(int notPaidNumThisYear) {
        this.notPaidNumThisYear = notPaidNumThisYear;
    }

    public float getOwingMoneyBeforeThisYear() {
        return owingMoneyBeforeThisYear;
    }

    public void setOwingMoneyBeforeThisYear(float owingMoneyBeforeThisYear) {
        this.owingMoneyBeforeThisYear = owingMoneyBeforeThisYear;
    }

    public int getThisMonthMinutes() {
        return thisMonthMinutes;
    }

    public void setThisMonthMinutes(int thisMonthMinutes) {
        this.thisMonthMinutes = thisMonthMinutes;
    }

}
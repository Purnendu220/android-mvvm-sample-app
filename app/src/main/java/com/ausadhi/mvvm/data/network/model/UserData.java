package com.ausadhi.mvvm.data.network.model;

public class UserData {
   private int UCode;
   private String Uname;
   private String UEmail;
   private String UPhone;
   private String UPass;

    public int getUCode() {
        return UCode;
    }

    public void setUCode(int UCode) {
        this.UCode = UCode;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    public String getUEmail() {
        return UEmail;
    }

    public void setUEmail(String UEmail) {
        this.UEmail = UEmail;
    }

    public String getUPhone() {
        return UPhone;
    }

    public void setUPhone(String UPhone) {
        this.UPhone = UPhone;
    }

    public String getUPass() {
        return UPass;
    }

    public void setUPass(String UPass) {
        this.UPass = UPass;
    }
}

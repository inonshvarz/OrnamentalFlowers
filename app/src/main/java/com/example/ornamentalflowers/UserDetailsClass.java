package com.example.ornamentalflowers;

import android.provider.ContactsContract;
import android.widget.EditText;

public class UserDetailsClass {

    String mailAddress;
    String userName;
    String phoneNumber;
    String storeSite;
    String idUser;
    String uId;
    Boolean manager;

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStoreSite() {
        return storeSite;
    }

    public void setStoreSite(String storeSite) {
        this.storeSite = storeSite;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setuId(String uId) { this.uId = uId; }

    public String getuId() { return uId; }

    public Boolean getManager() { return manager;}

    public void setManager(Boolean manager) { this.manager = manager; }
}

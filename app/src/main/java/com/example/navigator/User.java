/**
 *
 *  File Name: Signup.js (path: app/src/main/java/com.example.navigator/utils/Signup.js)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  09/07/2019  Mpho Mashaba    Original
 *
 *  Functional Description: This program file Creates USer Class
 *  Error Messages: none
 *  Constraints: none
 *  Assumptions: none
 *
 */
package com.example.navigator;



public class User {
    private String userid;
    private String username;
    private String email;
    private String addressL1;
    private String addressL2;
    private String addressL3;
    private String phoneNumber;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userid, String username, String email) {
        this.userid=userid;
        this.username = username;
        this.email = email;
        this.addressL1 = "";
        this.addressL2 = "";
        this.addressL3 = "";
        this.phoneNumber = "";
    }

    public User(String userid, String username, String email, String addressL1, String addressL2, String addressL3, String phoneNumber) {
        this.userid=userid;
        this.username = username;
        this.email = email;
        this.addressL1 = addressL1;
        this.addressL2 = addressL2;
        this.addressL3 = addressL3;
        this.phoneNumber = phoneNumber;
    }

    public String getuserid() {
       return userid;
    }
    public String getusername() {
        return username;
    }
    public String getemail() {
       return email;
    }
    public String getaddressL1() {
        return addressL1;
    }
    public String getAddressL2() {
        return addressL1;
    }
    public String getAddressL3() {
        return addressL2;
    }
    public String getphoneNumber() {
        return phoneNumber;
    }


}

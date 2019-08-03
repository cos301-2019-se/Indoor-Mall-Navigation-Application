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
    public String userid;
    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userid, String username, String email) {
        this.userid=userid;
        this.username = username;
        this.email = email;
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

}

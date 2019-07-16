/**
 *
 *  File Name: Shop.java (path: app/src/main/java/com.example.navigator/utils/Shop.java)
 *  Version: 1.0
 *  Author: Brute Force - Insert Shop Names into Database
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  11/07/2019  Thabo Ntsoane    Original
 *
 *  Functional Description: This program file is a class for Creating an object of a shop.
 *  Error Messages: None
 *  Constraints: Can only be used by instances of the class.
 *  Assumptions: It is assumed that all instances are existing Shops.
 *
 */
package com.example.navigator;

public class Shop {

    private String name;
    private String category;
    private int shopId;

    public Shop(String Cat, String  nm, int si){
        category = Cat;
        name = nm;
        shopId = si;
    }
    public int getShopId() {
        return shopId;
    }

    public String getName(){
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }
}

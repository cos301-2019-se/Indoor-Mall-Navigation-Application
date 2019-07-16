/**
 *
 *  File Name: Navigation.js (path: app/src/main/java/com.example.navigator/utils/Search.js)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  11/07/2019  Thabo Ntsoane    Original
 *
 *  Functional Description: This program file creates instances in the DB
 *  Error Messages: none
 *  Constraints: Can only be used to navigate
 *  Assumptions: It is assumed that the user will be navigated to destination appropriately
 *
 */
package com.example.navigator;

public class Shop {

    private String name;
    private String category;
    private int shopId;

    /*
    *Purpose: Constructor that set's passed parameters to their appropriate variables.
    *
     */
    public Shop(String Cat, String  nm, int si){
        category = Cat;
        name = nm;
        shopId = si;
    }

    /*
    *  Purpose: Get and Set Shop_id, Name and Category
    *
    *
    * */
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

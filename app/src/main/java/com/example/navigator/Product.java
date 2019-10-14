/**
 *
 *  File Name: Product.js (path: app/src/main/java/com.example.navigator/utils/Product.js)
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
 *
 *  Functional Description: This program file add's creates a Product class.
 *  Error Messages: None
 *  Constraints: field types
 *  Assumptions: It is assumed that the user Knows how to ad a product.
 *
 */
package com.example.navigator;



public class Product {

    public String id;
    public String name;
    public String shopResult;
    public double price;
    public int quantity;
    public String imageUrl;

    public Product() {

    }

    public Product(String id, String name, double price, int qty) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = qty;

    }

    public Product(String id, String name, double price, int qty, String imageUrl,String shopName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = qty;
        this.imageUrl = imageUrl;
        this.shopResult = shopName;

    }
    public Product(String id, String name, String price, int qty, String imageUrl,String shopName) {
        this.id = id;
        this.name = name;
        this.price = Double.parseDouble(price.replace("R",""));
        this.quantity = qty;
        this.imageUrl = imageUrl;
        this.shopResult = shopName;

    }
    public Product(String id, String name, double price, int qty, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = qty;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }
    public String getName() {return name;}
    public double getPrice() {
        return price;
    }
    public int getQuantity() {return quantity; }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
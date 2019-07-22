package com.example.navigator;



public class Product {

    public String id;
    public String name;
    public double price;

    public Product() {

    }

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }
    public String getName() {return name;}
    public double getPrice() {
        return price;
    }
}
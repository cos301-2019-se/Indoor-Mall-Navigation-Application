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

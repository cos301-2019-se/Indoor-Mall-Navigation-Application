package entities;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.DecimalFormat;




public class CartProduct implements Serializable{
    private Context context;


    //Each CardView Product will have the following
    private String id;
    private String name;
    private String price;
    private String quantity;
    private String totalPrice;
    private String imageUrl;
    private String shopResult;



    private static DecimalFormat roundToTwo = new DecimalFormat("#.##");


    public CartProduct() {
    }

    public CartProduct(String id, String name, String price, String quantity, int photo) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl= "";

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
        this.totalPrice = roundToTwo.format(tPrice);

    }



    public CartProduct(String id, String name, String price, String quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
        this.totalPrice = roundToTwo.format(tPrice);


    }

    public CartProduct(String id, String name, String price, String quantity, String imageUrl, String shopResult) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        //this.photo = photo;
        this.imageUrl = imageUrl;
        this.shopResult = shopResult;

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);

        this.totalPrice = roundToTwo.format(tPrice);


    }

    public void setCartProduct(String id, String name, String price, String quantity, int photo) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = "";

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
        this.totalPrice = roundToTwo.format(tPrice);

    }

    public void setCartProduct(String id, String name, String price, String quantity, int photo, Bitmap b) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = "";

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
        this.totalPrice = roundToTwo.format(tPrice);

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price.replace(',','.');
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public String increaseQuantity()
    {
        int temp = Integer.parseInt(quantity);
        temp++;
        quantity = Integer.toString(temp);
        setTotalPrice(quantity,price);
        return quantity;
    }

    public double roundToTwoPoint(double round)
    {
        return Math.round(round*100)/100;
    }

    public String decreaseQuantity()
    {
        int temp = Integer.parseInt(quantity);
        if(temp>1)
            temp--;
        quantity = Integer.toString(temp);
        setTotalPrice(quantity,price);
        return quantity;
    }

    public void setQuantity(String description) {
        this.quantity = quantity;
    }

    public String getTotalPrice(){return this.totalPrice.replace(',','.');}

    public String getStoreResult(){return this.shopResult;}

    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public void setTotalPrice(String quantity, String price)
    {
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
        this.totalPrice = roundToTwo.format(tPrice);

    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Drawable getDrawable()
    {
        return LoadImageFromUrl(this.getImageUrl());
    }

    private static Drawable LoadImageFromUrl(String url)
    {
        try{
            InputStream inStream = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(inStream,"product name");
            return drawable;

        } catch (Exception E)
        {
            return null;
        }
    }



}

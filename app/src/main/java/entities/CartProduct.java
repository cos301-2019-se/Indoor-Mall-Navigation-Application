package entities;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.DecimalFormat;

public class CartProduct implements Serializable{

    //Each CardView Product will have the following
    private String id;
    private String name;
    private String price;
    private String quantity;
    private int photo;
    private String totalPrice;
    private String imageUrl;
    private Bitmap bmap;


    public CartProduct() {
    }

    public CartProduct(String id, String name, String price, String quantity, int photo) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.photo = photo;
        this.imageUrl= "";

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
//        DecimalFormat decimal = new DecimalFormat("0.00");
        tPrice = roundToTwoPoint(tPrice);
        this.totalPrice = Double.toString(tPrice);

    }

   /* public CartProduct(String id, String name, String price, String quantity, int photo, Bitmap b) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.photo = photo;
        this.bmap = b;

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
//        DecimalFormat decimal = new DecimalFormat("0.00");
        tPrice = roundToTwoPoint(tPrice);
        this.totalPrice = Double.toString(tPrice);

    }*/

   /* public CartProduct(String id, String name, String price, String quantity, Bitmap b) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.photo = photo;
        this.bmap = b;

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
//        DecimalFormat decimal = new DecimalFormat("0.00");
        tPrice = roundToTwoPoint(tPrice);
        this.totalPrice = Double.toString(tPrice);

    }*/

    public CartProduct(String id, String name, String price, String quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        //this.photo = photo;
        this.imageUrl = imageUrl;

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
//        DecimalFormat decimal = new DecimalFormat("0.00");
        tPrice = roundToTwoPoint(tPrice);
        this.totalPrice = Double.toString(tPrice);

    }

    public void setCartProduct(String id, String name, String price, String quantity, int photo) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.photo = photo;
        this.imageUrl = "";

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
//        DecimalFormat decimal = new DecimalFormat("0.00");
        tPrice = Math.round(tPrice *100.00)/100.00;
        this.totalPrice = Double.toString(tPrice);

    }

    public void setCartProduct(String id, String name, String price, String quantity, int photo, Bitmap b) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.photo = photo;
        this.imageUrl = "";
        this.bmap = b;

        price.replace(',','.');
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
//        DecimalFormat decimal = new DecimalFormat("0.00");
        tPrice = roundToTwoPoint(tPrice);
        this.totalPrice = Double.toString(tPrice);

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
        return this.price;
    }

    public void setBmap(Bitmap b){this.bmap = b;};

    public Bitmap getBmap(){return this.bmap;};

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
        if(temp>0)
            temp--;
        quantity = Integer.toString(temp);
        setTotalPrice(quantity,price);
        return quantity;
    }

    public void setQuantity(String description) {
        this.quantity = quantity;
    }

    public int getPhoto() {
        return this.photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getTotalPrice(){return this.totalPrice;}

    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public void setTotalPrice(String quantity, String price)
    {
        double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
//        DecimalFormat decimal = new DecimalFormat("0.00");
        tPrice = roundToTwoPoint(tPrice);
        this.totalPrice = Double.toString(tPrice);
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}

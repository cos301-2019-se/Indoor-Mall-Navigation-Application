package entities;

import java.io.Serializable;
import java.text.DecimalFormat;

public class WishListProduct implements Serializable{

    //Each CardView Product will have the following
    private String id;
    private String name;
    private String price;
    private String quantity;
    private int photo;
    private String totalPrice;

    public WishListProduct() {
    }

    public WishListProduct(String id, String name, String price, String quantity, int photo) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.photo = photo;

        Double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
        DecimalFormat decimal = new DecimalFormat("0.00");
        tPrice = Double.parseDouble(decimal.format(tPrice));
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
        Double tPrice = Double.parseDouble(quantity) * Double.parseDouble(price);
        DecimalFormat decimal = new DecimalFormat("0.00");
        tPrice = Double.parseDouble(decimal.format(tPrice));
        this.totalPrice = Double.toString(tPrice);
    }
}

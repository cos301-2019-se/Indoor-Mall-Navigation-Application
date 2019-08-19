package entities;
import java.io.Serializable;

public class CartProduct implements Serializable{

    //Each CardView Product will have the following
    private String id;
    private String name;
    private String price;
    private String quantity;
    private int photo;

    public CartProduct() {
    }

    public CartProduct(String id, String name, String price, String quantity, int photo) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.photo = photo;
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

    public void setQuantity(String description) {
        this.quantity = quantity;
    }

    public int getPhoto() {
        return this.photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }


}

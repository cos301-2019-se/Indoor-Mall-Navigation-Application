package adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.navigator.R;

import java.util.ArrayList;

public class ComparePriceDialog extends Dialog implements
        android.view.View.OnClickListener{

    private Context c;
    private Dialog d;
    private String productName;
    private String price;
    private String shop;
    private ArrayList<String> otherShops;

    public ComparePriceDialog(Context a, String productName, String price, String shop, ArrayList<String> otherShops){
        super(a);
        this.c = a;
        this.productName = productName;
        this.price = price;
        this.shop = shop;
        this.otherShops = otherShops;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_price_dialog);

        TextView prodName = findViewById(R.id.textViewName);
        prodName.setText(productName);
        TextView prodPrice = findViewById(R.id.textViewPrice);
        prodPrice.setText("R" + price);
        TextView prodShop = findViewById(R.id.textViewShopName);
        prodShop.setText(shop);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.ok_btn:
                dismiss();
                break;
            default:
                break;*/
        }
        dismiss();
    }

}

package adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.navigator.R;

import java.util.ArrayList;

public class ComparePriceDialog extends Dialog{

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
        ListView otherShopsListView = findViewById(R.id.othershops_lv);
        Button close = findViewById(R.id.close_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, otherShops);
        otherShopsListView.setAdapter(arrayAdapter);

    }

}

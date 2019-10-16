package adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.navigator.R;
import com.example.navigator.ScanCodeActivity;

import java.util.ArrayList;

public class ComparePriceDialog extends Dialog{

    private Context c;
    private Dialog d;
    private String productName;
    private String price;
    private String shop;
    private ArrayList<String> otherShops;
    private Bitmap scanImageBitmap;

    public ComparePriceDialog(Context a, Bitmap scanImageBitmap, String productName, String price, String shop, ArrayList<String> otherShops){
        super(a);
        this.c = a;
        this.productName = productName;
        this.price = price;
        this.shop = shop;
        this.otherShops = ScanCodeActivity.otherShops;
        this.scanImageBitmap = scanImageBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_price_dialog);

        ImageView prodImage = findViewById(R.id.imageViewPhoto);
        prodImage.setImageBitmap(scanImageBitmap);
        TextView prodName = findViewById(R.id.textViewName);
        prodName.setText(productName);
        TextView prodPrice = findViewById(R.id.textViewPrice);
        prodPrice.setText(price);
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

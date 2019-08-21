package adapters;

import entities.CartProduct;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import android.content.Context;

import com.example.navigator.Product;
import com.example.navigator.R;

import static com.example.navigator.R.layout.cart_product_list_layout;

public class CartProductListAdapter extends ArrayAdapter<CartProduct> {
    private Context context;
    private List<CartProduct> products;

    public CartProductListAdapter(Context context, List<CartProduct> products) {
        super(context, R.layout.cart_product_list_layout, products);
        this.context = context;
        this.products = products;
    }

    @NonNull
    //@Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(cart_product_list_layout, parent, false);
            viewHolder.textViewName = view.findViewById(R.id.textViewName);
            viewHolder.textViewPrice = view.findViewById(R.id.textViewPrice);
            viewHolder.textViewQuantity = view.findViewById(R.id.textQuantity);
            viewHolder.imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
            viewHolder.totalPrice = view.findViewById(R.id.totalPrice);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CartProduct product = products.get(position);
        viewHolder.textViewName.setText(product.getName());
        viewHolder.textViewQuantity.setText(product.getQuantity());
        viewHolder.textViewPrice.setText(String.valueOf(product.getPrice()));
        viewHolder.imageViewPhoto.setImageResource(product.getPhoto());
        viewHolder.totalPrice.setText(product.getTotalPrice());
        return view;
    }

    private static class ViewHolder {
        public static TextView textViewName;
        public static TextView textViewQuantity;
        public static TextView textViewPrice;
        public static ImageView imageViewPhoto;
        public static TextView totalPrice;
    }
}

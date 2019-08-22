package adapters;

import entities.CartProduct;
import entities.WishListProduct;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.widget.Toast;

import com.example.navigator.Product;
import com.example.navigator.R;
import com.example.navigator.utils.Installation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.navigator.R.layout.wish_list_product_layout;

public class WishListAdapter extends ArrayAdapter<CartProduct>{

    private Context context;
    private List<CartProduct> products;

    //Get device ID
    final String deviceId = Installation.id(getContext());

    DatabaseReference tempDBRef = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(deviceId);
    //Query myQuery = tempDBRef.orderByChild("id").equalTo(false);


    public WishListAdapter(Context context, List<CartProduct> products) {
        super(context, R.layout.wish_list_product_layout, products);
        this.context = context;
        this.products = products;
    }

    @NonNull
    //@Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(wish_list_product_layout, parent, false);
            viewHolder.textViewName = view.findViewById(R.id.textViewName);
            viewHolder.textViewPrice = view.findViewById(R.id.textViewPrice);
            viewHolder.imageViewPhoto = view.findViewById(R.id.imageViewPhoto);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final CartProduct product = products.get(position);
        viewHolder.textViewName.setText(product.getName());

        viewHolder.textViewPrice.setText("R " + product.getPrice());
        viewHolder.imageViewPhoto.setImageResource(product.getPhoto());

        return view;
    }

    private static class ViewHolder {
        public static TextView textViewName;
        public static TextView textViewPrice;
        public static ImageView imageViewPhoto;

    }
}

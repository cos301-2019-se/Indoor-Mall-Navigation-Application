package adapters;

import entities.CartProduct;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.widget.Toast;

import com.example.navigator.Product;
import com.example.navigator.R;
import com.example.navigator.utils.DatabaseConn;
import com.example.navigator.utils.Installation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.navigator.R.layout.cart_product_list_layout;



public class CartProductListAdapter extends ArrayAdapter<CartProduct> {
    private Context context;
    private List<CartProduct> products;
    private ListView cartListView;

    //Get device ID
    final String deviceId = Installation.id(getContext());

    DatabaseReference cartDBRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);
    DatabaseReference wishDBRef = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(deviceId);



    public CartProductListAdapter(Context context, List<CartProduct> products) {
        super(context, R.layout.cart_product_list_layout, products);
        this.context = context;
        this.products = products;
    }

    @NonNull
    //@Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(cart_product_list_layout, parent, false);
            viewHolder.textViewName = view.findViewById(R.id.textViewName);
            viewHolder.textViewPrice = view.findViewById(R.id.textViewPrice);
            viewHolder.textViewQuantity = view.findViewById(R.id.textQuantity);
            viewHolder.imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
            viewHolder.totalPrice = view.findViewById(R.id.totalPrice);
            viewHolder.incrementQuantity = view.findViewById(R.id.incrementQuantity);
            viewHolder.decrementQuantity = view.findViewById(R.id.decrementQuantity);
            viewHolder.deleteCartProduct = view.findViewById(R.id.deleteCartItem);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final CartProduct product = products.get(position);
        viewHolder.textViewName.setText(product.getName());
        viewHolder.textViewQuantity.setText(product.getQuantity());
        viewHolder.textViewPrice.setText("R " + product.getPrice());
        viewHolder.imageViewPhoto.setImageResource(product.getPhoto());
        viewHolder.totalPrice.setText(product.getTotalPrice());

        //Increasing Quantity through button
        viewHolder.incrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Updated quantity on display
                viewHolder.textViewQuantity.setText(product.increaseQuantity());
                viewHolder.totalPrice.setText(product.getTotalPrice());
                //Query to find the ID
                Query myQuery = cartDBRef.orderByChild("id").equalTo(product.getId());


                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            dataSnap.child("quantity").getRef().setValue(product.getQuantity());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                myQuery.addListenerForSingleValueEvent(valueEventListener);

                //Toast.makeText(getContext(),"Item deleted from Wish list", Toast.LENGTH_LONG).show();
            }
        });

        //Decrementing the quality
        viewHolder.decrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Updated quantity on display
                viewHolder.textViewQuantity.setText(product.decreaseQuantity());
                viewHolder.totalPrice.setText(product.getTotalPrice());
                //Query to find the ID
                Query myQuery = cartDBRef.orderByChild("id").equalTo(product.getId());


                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            dataSnap.child("quantity").getRef().setValue(product.getQuantity());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                myQuery.addListenerForSingleValueEvent(valueEventListener);

                //HashMap<String, Object> update = new HashMap<>();
                //update.put("/" + product.getId(), product.getQuantity());
                //tempDBRef.updateChildren(update);
            }
        });

        final View finalView = view;
        int currPosition = position;
        final CartProduct currProduct = products.get(position);

        viewHolder.deleteCartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //products.remove(products.get(position));

                Query myQuery = cartDBRef.orderByChild("id").equalTo(product.getId());

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            String toDelete = dataSnap.getKey();
                            DatabaseConn data = DatabaseConn.open();
                            //Toast.makeText(getContext(),deviceId + ", " + toDelete, Toast.LENGTH_LONG).show();
                            data.delete("Cart",deviceId+"/"+toDelete);
                        }

                        removeFromList(currProduct);

                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                /*ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                myQuery.addListenerForSingleValueEvent(valueEventListener);*/
            }
        });


        return view;
    }

    private void removeFromList(CartProduct cp) {
        int position = products.indexOf(cp);
        products.remove(position);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public static TextView textViewName;
        public static TextView textViewQuantity;
        public static TextView textViewPrice;
        public static ImageView imageViewPhoto;
        public static TextView totalPrice;
        public static Button incrementQuantity;
        public static Button decrementQuantity;
        public static Button deleteCartProduct;
    }
}

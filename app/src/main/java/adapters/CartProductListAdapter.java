package adapters;

import entities.CartProduct;


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.List;
import android.content.Context;
import android.widget.Toast;

import com.example.navigator.Cart;
import com.example.navigator.R;
import com.example.navigator.utils.DatabaseConn;
import com.example.navigator.utils.Installation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.navigator.R.layout.cart_product_list_layout;


public class CartProductListAdapter extends ArrayAdapter<CartProduct> {
    private Context context;
    private List<CartProduct> products;
    private TextView localOverall;

    private static DecimalFormat roundToTwo = new DecimalFormat("#.##");
    //Get device ID
    final String deviceId = Installation.id(getContext());

    DatabaseReference cartDBRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);
    DatabaseReference wishDBRef = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(deviceId);



    public CartProductListAdapter(Context context, List<CartProduct> products, TextView oTotal) {
        super(context, R.layout.cart_product_list_layout, products);
        this.context = context;
        this.products = products;
        this.localOverall = oTotal;
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
            viewHolder.imageViewPhoto =  view.findViewById(R.id.imageViewPhoto);
            viewHolder.totalPrice = view.findViewById(R.id.totalPrice);
            viewHolder.incrementQuantity = view.findViewById(R.id.incrementQuantity);
            viewHolder.decrementQuantity = view.findViewById(R.id.decrementQuantity);
            viewHolder.deleteCartProduct = view.findViewById(R.id.deleteCartItem);
            viewHolder.addToWishList = view.findViewById(R.id.addToWishlist);
            viewHolder.storeResult = view.findViewById(R.id.ShopResult);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final CartProduct product = products.get(position);

        viewHolder.textViewName.setText(product.getName());
        viewHolder.textViewQuantity.setText(product.getQuantity());
        viewHolder.textViewPrice.setText("R " + product.getPrice());
        viewHolder.totalPrice.setText(product.getTotalPrice());

        //List<String
        int shopImage = R.drawable.exact;
        final String tester = product.getStoreResult();

        if(tester.equals("Woolworths"))
        {
            shopImage = R.drawable.woolworths;
            //Toast.makeText(getContext()," Woolworths ", Toast.LENGTH_LONG).show();
        }
        else if(tester.equals("Pick n Pay"))
        {
            shopImage = R.drawable.pnp;
        }
        else if(tester.equals("Exclusive Books"))
        {
            shopImage = R.drawable.exclusive_books;
        }
        else if(tester.equals("CNA"))
        {
            shopImage = R.drawable.cna;
        }
        else if(tester.equals("Exact"))
        {
            //Toast.makeText(getContext()," Came here ", Toast.LENGTH_LONG).show();
            shopImage = R.drawable.exact;
        }

        viewHolder.storeResult.setImageResource(shopImage);


        //Trying Drawable Method
        Picasso.with(context).load(product.getImageUrl()).into(viewHolder.imageViewPhoto);
        //new DownloadImageTask(viewHolder.imageViewPhoto).execute(product.getImageUrl());

        final CartProduct currProduct = products.get(position);

        //Increasing Quantity through button
        viewHolder.incrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Updated quantity on display

                product.increaseQuantity();
                viewHolder.textViewQuantity.setText(product.getQuantity());
                viewHolder.totalPrice.setText("R " + product.getTotalPrice());

                //Get the double from cart
                String sOverallTotal = localOverall.getText().toString().substring(2);
                double temp = Double.parseDouble(sOverallTotal.replace(",", "."));

                temp += Double.parseDouble(product.getPrice());
                Cart.oTotal = temp;
                //temp = (double) Math.round(temp*100)/100;
                localOverall.setText("R " +roundToTwo.format(temp));

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
                notifyDataSetChanged();
                myQuery.addListenerForSingleValueEvent(valueEventListener);

            }
        });

        //Decrementing the quality
        viewHolder.decrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sOverallTotal = localOverall.getText().toString().substring(2);

                double temp = Double.parseDouble(sOverallTotal.replace(",", "."));
                double currPrice = Double.parseDouble(product.getPrice());
                double currTotalPrice = Double.parseDouble(product.getTotalPrice().replace(",", "."));

                if(currTotalPrice>currPrice) {

                    temp -= Double.parseDouble(product.getPrice());
                    temp = (double) Math.round(temp*100)/100;
                    Cart.oTotal = temp;
                    localOverall.setText("R " +roundToTwo.format(temp));

                }

                product.decreaseQuantity();

                viewHolder.textViewQuantity.setText(product.getQuantity());
                viewHolder.totalPrice.setText("R " + product.getTotalPrice());

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
                notifyDataSetChanged();
                myQuery.addListenerForSingleValueEvent(valueEventListener);


            }
        });


        //final CartProduct currProduct = products.get(position);

        viewHolder.deleteCartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sOverallTotal = localOverall.getText().toString().substring(2);

                double temp = Double.parseDouble(sOverallTotal.replace(',','.'));


                temp -= Double.parseDouble(product.getTotalPrice().replace(',','.'));
                temp = (double) Math.round(temp*100)/100;
                localOverall.setText("R " +roundToTwo.format(temp));


                Query myQuery = cartDBRef.orderByChild("id").equalTo(product.getId());

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            String toDelete = dataSnap.getKey();
                            DatabaseConn data = DatabaseConn.open();
                            Toast.makeText(getContext(),product.getName()+ " removed from Cart ", Toast.LENGTH_LONG).show();
                            data.delete("Cart",deviceId+"/"+toDelete);
                        }

                        removeFromList(currProduct);

                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        viewHolder.addToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sOverallTotal = localOverall.getText().toString().substring(2);


                double temp = Double.parseDouble(sOverallTotal.replace(',','.'));

                temp -= Double.parseDouble(product.getTotalPrice().replace(',','.'));
                temp = (double) Math.round(temp*100)/100;
                localOverall.setText("R " +roundToTwo.format(temp));

                wishDBRef.push().setValue(product);
                Toast.makeText(getContext(),product.getName()+ " added to WishList ", Toast.LENGTH_LONG).show();

                Query myQuery = cartDBRef.orderByChild("id").equalTo(product.getId());

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                        {
                            String toDelete = dataSnap.getKey();
                            DatabaseConn data = DatabaseConn.open();

                            data.delete("Cart",deviceId+"/"+toDelete);
                        }
                        removeFromList(currProduct);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
        public TextView textViewName;
        public TextView textViewQuantity;
        public TextView textViewPrice;
        public ImageView imageViewPhoto;
        public TextView totalPrice;
        public Button incrementQuantity;
        Button decrementQuantity;
        Button deleteCartProduct;
        Button addToWishList;
        ImageView storeResult;
    }
}

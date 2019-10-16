/**
 *
 *  File Name: CartProductListAdapter.java (path: app/src/main/java/com.example.navigator/utils/CartProduct.java)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author              Changes
 *  --------------------------------------------
 *  14/10/2019  Khodani Tshisimba   Original 1.0, Populate Cart with CardView Items (Products)
 *
 *  Functional Description: This program file Scan's a product to a Cart or Wishlist.
 *  Error Messages: None
 *  Constraints: Can only be used if Items are available
 *  Assumptions: It is assumed that the user will be able to add items correctly to the cart.
 *
 */

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
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static com.example.navigator.R.layout.cart_product_list_layout;


public class CartProductListAdapter extends ArrayAdapter<CartProduct> {
    private Context context;
    private List<CartProduct> products;
    private TextView localOverall;
    private static DecimalFormat decimal = new DecimalFormat("#.##");

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
        double totalPrice = Double.parseDouble(product.getTotalPrice());
        viewHolder.totalPrice.setText(String.format("%.2f",totalPrice).replace(",","."));


        //List<String
        int shopImage = R.drawable.ic_store_black_24dp;
        final String tester = product.getStoreResult();

        if(tester.equals("Woolworths"))
        {
            shopImage = R.drawable.woolworths;
        }
        else if(tester.equals("Pick 'n Pay"))
        {
            shopImage = R.drawable.pnp;
        }
        else if(tester.equals("CNA"))
        {
            shopImage = R.drawable.cna;
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
                double totalPrice = Double.parseDouble(product.getTotalPrice());
                viewHolder.totalPrice.setText("R " + String.format("%.2f",totalPrice).replace(",","."));

                //Get the double from cart
                String sOverallTotal = localOverall.getText().toString().substring(2);
                double temp = Double.parseDouble(sOverallTotal.replace(",", "."));

                temp += Double.parseDouble(product.getPrice());
                Cart.oTotal = temp;

                localOverall.setText("R " + String.format("%.2f",temp).replace(",","."));

                //Query to find the ID
                cartDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            DataSnapshot deviceSnapshot = dataSnapshot;
                            //Unique Key in database
                            Iterable<DataSnapshot> deviceChildren = deviceSnapshot.getChildren();
                            String sessionId = product.getId();
                            for (DataSnapshot productItem : deviceChildren) {
                                if(productItem.child("shopResult").exists())
                                {

                                    String store = productItem.child("shopResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(product.getId()))
                                    {
                                        productItem.child("quantity").getRef().setValue(product.getQuantity());
                                    }
                                }
                                else if(productItem.child("storeResult").exists())
                                {
                                    //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("storeResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(sessionId))
                                    {
                                        productItem.child("quantity").getRef().setValue(product.getQuantity());

                                    }
                                }
                            }



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                notifyDataSetChanged();

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
                    localOverall.setText("R " + String.format("%.2f",temp).replace(",","."));

                }

                product.decreaseQuantity();

                viewHolder.textViewQuantity.setText(product.getQuantity());
                double totalPrice = Double.parseDouble(product.getTotalPrice());
                viewHolder.totalPrice.setText("R " + String.format("%.2f",totalPrice).replace(",","."));

                cartDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            //

                            DataSnapshot deviceSnapshot = dataSnapshot;
                            //Unique Key in database
                            Iterable<DataSnapshot> deviceChildren = deviceSnapshot.getChildren();
                            String sessionId = product.getId();
                            for (DataSnapshot productItem : deviceChildren) {
                                if(productItem.child("shopResult").exists())
                                {
                                    //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("shopResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(product.getId()))
                                    {
                                        productItem.child("quantity").getRef().setValue(product.getQuantity());
                                    }
                                }
                                else if(productItem.child("storeResult").exists())
                                {
                                    //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("storeResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(sessionId))
                                    {
                                        productItem.child("quantity").getRef().setValue(product.getQuantity());

                                    }
                                }
                            }



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                notifyDataSetChanged();



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
                Cart.oTotal = temp;
                if(Cart.oTotal == 0.00 || Cart.oTotal == 0){
                    localOverall.setVisibility(View.INVISIBLE);
                }else {
                    localOverall.setVisibility(View.VISIBLE);
                    localOverall.setText("R " + String.format("%.2f",temp).replace(",","."));
                }


                cartDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            //

                            DataSnapshot deviceSnapshot = dataSnapshot;
                            //Unique Key in database
                            Iterable<DataSnapshot> deviceChildren = deviceSnapshot.getChildren();
                            String sessionId = product.getId();
                            for (DataSnapshot productItem : deviceChildren) {
                                if(productItem.child("shopResult").exists())
                                {
                                    //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("shopResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(sessionId))
                                    {
                                        String toDelete = productItem.getKey();
                                        DatabaseConn data = DatabaseConn.open();
                                        Toast.makeText(getContext(),product.getName()+ " removed from Cart ", Toast.LENGTH_LONG).show();
                                        data.delete("Cart",deviceId+"/"+toDelete);
                                        removeFromList(currProduct);


                                    }
                                }
                                else if(productItem.child("storeResult").exists())
                                {
                                    //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("storeResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(sessionId))
                                    {
                                        String toDelete = productItem.getKey();
                                        DatabaseConn data = DatabaseConn.open();
                                        Toast.makeText(getContext(),product.getName()+ " removed from Cart ", Toast.LENGTH_LONG).show();
                                        data.delete("Cart",deviceId+"/"+toDelete);
                                        removeFromList(currProduct);
                                    }
                                }

                                //Contact c = contact.getValue(Contact.class);
                            }



                        }
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
                Cart.oTotal = temp;
                if(Cart.oTotal == 0.00 || Cart.oTotal == 0){
                    localOverall.setVisibility(View.INVISIBLE);
                }else {
                    localOverall.setVisibility(View.VISIBLE);
                    localOverall.setText("R " + String.format("%.2f",temp).replace(",","."));
                }

                wishDBRef.push().setValue(product);
                Toast.makeText(getContext(),product.getName()+ " added to WishList ", Toast.LENGTH_LONG).show();

                cartDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            //

                            DataSnapshot deviceSnapshot = dataSnapshot;
                            //Unique Key in database
                            Iterable<DataSnapshot> deviceChildren = deviceSnapshot.getChildren();
                            String sessionId = product.getId();
                            for (DataSnapshot productItem : deviceChildren) {
                                if(productItem.child("shopResult").exists())
                                {
                                    //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("shopResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(product.getId()))
                                    {
                                        String toDelete = productItem.getKey();
                                        DatabaseConn data = DatabaseConn.open();

                                        data.delete("Cart",deviceId+"/"+toDelete);
                                        removeFromList(currProduct);
                                    }
                                }
                                else if(productItem.child("storeResult").exists())
                                {
                                    //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                                    String store = productItem.child("storeResult").getValue().toString();
                                    String productId = productItem.child("id").getValue().toString();
                                    if(store.equals(product.getStoreResult()) && productId.equals(sessionId))
                                    {
                                        String toDelete = productItem.getKey();
                                        DatabaseConn data = DatabaseConn.open();

                                        data.delete("Cart",deviceId+"/"+toDelete);
                                        removeFromList(currProduct);

                                    }
                                }
                            }



                        }
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

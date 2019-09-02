/**
 *
 *  File Name: WishList.js (path: app/src/main/java/com.example.navigator/utils/wishList.js)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  09/07/2019  Thabo Ntsoane    Original
 *
 *  Functional Description: This program file add's a product to the wishlist.
 *  Error Messages: None
 *  Constraints: Can only be used if Items are available
 *  Assumptions: It is assumed that the user will be able to add items correctly to the cart.
 *
*/
package com.example.navigator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigator.utils.DatabaseConn;
import com.example.navigator.utils.Installation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapters.CartProductListAdapter;
import adapters.WishListAdapter;
import entities.CartProduct;


/**
 * A simple {@link Fragment} subclass.
 */
public class Wishlist extends Fragment {
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayList<String> listProductNames;
    ArrayAdapter<String > adapter;
    Button sub;
    DatabaseReference ref;
    Product objProduct;
    private Context context = null;
    private ListView listWLViewProduct;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private FirebaseAuth firebaseAuth;
    TextView demoValue;
    ListView cartList;

    DatabaseReference rootRef,demoRef,wishToCart;
    public Wishlist() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        final String deviceId = Installation.id(getContext());
        demoValue = (TextView) view.findViewById(R.id.tvValue);
        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to Product node
        final List<CartProduct> products = new ArrayList<CartProduct>();
        demoRef = rootRef.child("Wishlist").child(deviceId);

        listWLViewProduct = view.findViewById(R.id.listWLViewProduct);
        wishToCart = rootRef.child("Cart");
        //final TableLayout myTable = (TableLayout)view.findViewById(R.id.);
        final ArrayList<String>  list = new ArrayList<>();
        final ArrayList<String>  IDList = new ArrayList<>();
        final ArrayList<String>  listProductNames = new ArrayList<>();
        //final TableLayout myTable = (TableLayout) view.findViewById(R.id.myTableLayout);
        demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productName = snapshot.child("name").getValue().toString();
                    String barCode  = snapshot.child("id").getValue().toString();
                    IDList.add(snapshot.getKey());
                    list.add(barCode);
                    listProductNames.add(productName);
                    String price = snapshot.child("price").getValue().toString();
                    String priceProduct = productName + " R"+ price;
                    String id = snapshot.child("id").getValue().toString();
                    String quantity = snapshot.child("quantity").getValue().toString();
                    final String url = snapshot.child("imageUrl").getValue().toString();
                    //price = "R price;
                    //String ShopName = snapshot.child("name").toString(); returns {key: name,value : ABSA
                    //list.add(priceProduct);
                    //final int curr = count;
                    //final String currProductName = productName;
                    //for (int i = 0; i <2; i++) {

                    products.add(new CartProduct(id, productName, price, quantity, url));
                    /*
                    TableRow tableRow = new TableRow(getContext());

                    // Set new table row layout parameters.
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    tableRow.setLayoutParams(layoutParams);

                    // Add a TextView in the first column.
                    TextView name = new TextView(getContext());
                    name.setText(productName);
                    tableRow.addView(name);
                    // Add a TextView in the first column.
                    TextView aPrice = new TextView(getContext());
                    aPrice.setText(price);
                    tableRow.addView(aPrice);

                    // Add a button in the second column
                    ImageButton button1 = new ImageButton(getContext());
                    button1.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myTable.removeViewAt(curr);
                            //CODE THAT ADDS TO CART FROM WISHLIST GOES HERE
                            String y = listProductNames.get(curr-1);
                            Query applesQuery = demoRef.orderByChild("name").equalTo(listProductNames.get(curr-1));
                            DatabaseConn data = DatabaseConn.open();

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        Product addToCart = new Product();
                                        addToCart.id = appleSnapshot.child("id").getValue().toString();
                                        addToCart.name = appleSnapshot.child("name").getValue().toString();
                                        addToCart.price = Double.parseDouble(appleSnapshot.child("price").getValue().toString());
                                        wishToCart.push().setValue(addToCart);
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(getContext(),"Item added to Cart", Toast.LENGTH_LONG).show();
                        }
                    });
                    tableRow.addView(button1);
                    ImageButton button = new ImageButton(getContext());
                    button.setImageResource(R.drawable.ic_delete_black_24dp);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myTable.removeViewAt(curr);
                            String y = listProductNames.get(curr-1);
                            String yID = list.get(curr-1);
//                            Toast.makeText(getContext(),y + " has ID: " + yID + " and is element " + IDList.get(curr-1), Toast.LENGTH_LONG).show();
                            Query applesQuery = demoRef.orderByChild("name").equalTo(listProductNames.get(curr-1));

                            DatabaseConn data = DatabaseConn.open();
                            data.delete("Wishlist", IDList.get(curr-1));

                            Toast.makeText(getContext(),"Item deleted from Wish list", Toast.LENGTH_LONG).show();
                        }
                    });
                    tableRow.addView(button);
                    myTable.addView(tableRow,count);
                    //increment counter
                    count++;*/
                }

                WishListAdapter wlProductListAdapter = new WishListAdapter(getContext(), products);

                listWLViewProduct.setAdapter(wlProductListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}



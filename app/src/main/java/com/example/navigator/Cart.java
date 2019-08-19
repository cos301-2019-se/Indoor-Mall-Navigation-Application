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
 *  09/07/2019  Mpho Mashaba    Original
 *  01/08/2019  Thabo Ntsoane   version 1.0.1
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapters.CartProductListAdapter;
import entities.CartProduct;

import static android.app.Activity.DEFAULT_KEYS_DIALER;
import static com.example.navigator.MainActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */

public class Cart extends Fragment {
    private Context context = null;
    private ListView listViewProduct;

    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private FirebaseAuth firebaseAuth;
    TextView demoValue;
    ListView cartList;

    DatabaseReference rootRef,demoRef;
    public Cart() {
        // Required empty public constructor
    }

    private void listViewProduct_onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product product = (Product) adapterView.getItemAtPosition(i);
        Toast.makeText(getContext(), product.getName(), Toast.LENGTH_LONG).show();
    }

    /*private void initView() {
        listViewProduct = findViewById(R.id.listViewProduct);
        listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listViewProduct_onItemClick(adapterView, view, i, l);
            }
        });
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        demoValue = (TextView) view.findViewById(R.id.tvValue);
        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to Product node
        demoRef = rootRef.child("Wishlist");

        listViewProduct = view.findViewById(R.id.listViewProduct);

        final List<CartProduct> products = new ArrayList<CartProduct>();


        //final TableLayout myTable = (TableLayout) view.findViewById(R.id.myTableLayout);
        demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 1;
                final ArrayList<Integer> quantities = new ArrayList<>();
                int quantitiesCount = 0;
                int decreaseButtonID = 0;
                int increaseButtonID = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productName = snapshot.child("name").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    String id = snapshot.child("id").getValue().toString();
                    String quantity = "1";
                    String priceProduct = productName + " R "+ price;
                    price = "R " + price;

                    //Load Elements from DB to product list
                    products.add(new CartProduct(id,productName, price, quantity, R.drawable.thumb1));


                    /*
                    final int curr = count;
                    final String currProductName = productName;


                        TableRow tableRow = new TableRow(getContext());

                        // Set new table row layout parameters.
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        tableRow.setLayoutParams(layoutParams);

                        // Add a TextView in the first column.
                        TextView name = new TextView(getContext());
                        name.setText(productName);
                        tableRow.addView(name);

                        //Add a an image in the second column which only has a general image  for now
                        ImageButton button = new ImageButton(getContext());
                        button.setImageResource(R.drawable.ic_image_black_24dp);
                        tableRow.addView(button);

                        // Add a TextView in the third column for Price.
                        TextView aPrice = new TextView(getContext());
                        aPrice.setText(price);
                        tableRow.addView(aPrice);

                        //Add a an image in the second column which only has a general image  for now
                        final ImageButton decreaseButton = new ImageButton(getContext());
                        decreaseButton.setImageResource(R.drawable.ic_indeterminate_check_box_black_24dp);
                        decreaseButton.setId(decreaseButtonID);
                        decreaseButtonID++;
                        decreaseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantities.set(decreaseButton.getId(),quantities.get(decreaseButton.getId())-1);
                            }
                        });
                        tableRow.addView(decreaseButton);

                        // Add a TextView in the fifth column for Quantity.
                        TextView aQuantity = new TextView(getContext());
                        quantities.add(1);
                        String sQuantity = quantities.get(quantitiesCount++).toString();
                        aQuantity.setText(sQuantity);
                        tableRow.addView(aQuantity);

                        //Add a an image in the second column which only has a general image  for now
                        final ImageButton increaseButton = new ImageButton(getContext());
                        increaseButton.setImageResource(R.drawable.ic_add_box_black_24dp);
                        increaseButton.setId(decreaseButtonID);
                        increaseButtonID++;
                        increaseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantities.set(increaseButton.getId(),quantities.get(increaseButton.getId())+1);
                            }
                        });
                        tableRow.addView(increaseButton);

                        // Add a button in the second column
                        ImageButton deleteButton = new ImageButton(getContext());
                        deleteButton.setImageResource(R.drawable.ic_delete_black_24dp);
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myTable.removeViewAt(curr);

                                //CODE THAT REMOVES PRODUCT FROM DB GOES HERE
                            }
                        });
                        tableRow.addView(deleteButton);



                        myTable.addView(tableRow,count);
                        //increment counter
                        count++;
                        */
                }

                CartProductListAdapter productListAdapter = new CartProductListAdapter(getContext(), products);

                listViewProduct.setAdapter(productListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}

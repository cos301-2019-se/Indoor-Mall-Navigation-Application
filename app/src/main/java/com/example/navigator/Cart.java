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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.navigator.utils.Installation;
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

import static android.app.Activity.DEFAULT_KEYS_DIALER;
import static com.example.navigator.MainActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */

public class Cart extends Fragment {
    private Context context = null;
//
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private FirebaseAuth firebaseAuth;
    TextView demoValue;
    ListView cartList;

    DatabaseReference rootRef,demoRef;
    public Cart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        final String deviceId = Installation.id(getContext());
        demoValue = (TextView) view.findViewById(R.id.tvValue);
        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to Product node
        demoRef = rootRef.child("Cart").child(deviceId);
        //final TableLayout myTable = (TableLayout)view.findViewById(R.id.);

        final TableLayout myTable = (TableLayout) view.findViewById(R.id.myTableLayout);
        demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productName = snapshot.child("name").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    String priceProduct = productName + " R"+ price;
                    price = "R " + price;
                    //String ShopName = snapshot.child("name").toString(); returns {key: name,value : ABSA
                    //list.add(priceProduct);
                    final int curr = count;
                    final String currProductName = productName;
                    //for (int i = 0; i <2; i++) {

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
                        ImageButton button = new ImageButton(getContext());
                        button.setImageResource(R.drawable.ic_delete_black_24dp);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myTable.removeViewAt(curr);

                                //CODE THAT REMOVES PRODUCT FROM DB GOES HERE
                            }
                        });
                        tableRow.addView(button);

                        // Add a checkbox in the third column.
                        //CheckBox checkBox = new CheckBox(context);
                        //checkBox.setText("Check it");
                        //myTable.addView(checkBox, 2);

                        myTable.addView(tableRow,count);
                        //increment counter
                        count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}

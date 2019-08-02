/**
 *
 *  File Name: Navigation.js (path: component/app/navigation.js)
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
 *  Functional Description: This program file searches and navigates user to a specific shop
 *  Error Messages: Shop does not exist
 *  Constraints: Can only be used to navigate
 *  Assumptions: It is assumed that the user will be navigated to destination appropriately
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;


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
        demoValue = (TextView) view.findViewById(R.id.tvValue);
        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to Product node
        demoRef = rootRef.child("Wishlist");
        wishToCart = rootRef.child("Cart");
        //final TableLayout myTable = (TableLayout)view.findViewById(R.id.);
        final ArrayList<String>  list = new ArrayList<>();
        final ArrayList<String>  listProductNames = new ArrayList<>();
        final TableLayout myTable = (TableLayout) view.findViewById(R.id.myTableLayout);
        demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productName = snapshot.child("name").getValue().toString();
                    String barCode  = snapshot.child("id").getValue().toString();
                    list.add(barCode);
                    listProductNames.add(productName);
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
                    ImageButton button1 = new ImageButton(getContext());
                    button1.setImageResource(R.drawable.ic_add_black_24dp);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myTable.removeViewAt(curr);
                            //CODE THAT ADDS TO CART FROM WISHLIST GOES HERE
                            String y = listProductNames.get(curr-1);
                            Query applesQuery = demoRef.orderByChild("name").equalTo(listProductNames.get(curr-1));
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
                            Query applesQuery = demoRef.orderByChild("name").equalTo(listProductNames.get(curr-1));

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    tableRow.addView(button);
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



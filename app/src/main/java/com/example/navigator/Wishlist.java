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
import android.content.Intent;
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

    private ListView listWLViewProduct;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private FirebaseAuth firebaseAuth;
    TextView demoValue;
    ListView cartList;
    DatabaseReference rootRef,demoRef,wishToCart;
    DatabaseReference dbRef,wlRef;


    public Wishlist() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        //Get Device ID
        final String deviceId = Installation.id(getContext());

        //Retrieve Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference();

        //List of Products to be placed in Wishlist
        final List<CartProduct> products = new ArrayList<CartProduct>();

        //Point to Wishlist in DB
        wlRef = dbRef.child("Wishlist").child(deviceId);

        //List of Wishlist products
        listWLViewProduct = view.findViewById(R.id.listWLViewProduct);

        //Get Items From Database
        wlRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    //Assign Item attributes from DB to each product attribute
                    String productName = snapshot.child("name").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    String id = snapshot.child("id").getValue().toString();
                    String quantity = snapshot.child("quantity").getValue().toString();
                    final String url = snapshot.child("imageUrl").getValue().toString();
                    final String storeResult = snapshot.child("shopResult").getValue().toString();

                    //Add a product to list of Wishlist products
                    products.add(new CartProduct(id, productName, price, quantity, url, storeResult));

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



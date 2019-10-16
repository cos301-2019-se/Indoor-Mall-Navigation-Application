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
 *  09/07/2019  Mpho Mashaba        Origina l
 *  01/08/2019  Thabo Ntsoane       version 1.1
 *  05/08/2019  Khodani Tshisimba   Version 1.2, Cart Functionality
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
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TableLayout;
        import android.widget.TableRow;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.navigator.utils.Installation;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FileDownloadTask;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;

        import java.io.File;
        import java.io.IOException;
        import java.security.cert.PolicyNode;
        import java.text.DecimalFormat;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import adapters.CartProductListAdapter;
        import entities.CartProduct;
        import static android.app.Activity.DEFAULT_KEYS_DIALER;

        import java.math.RoundingMode;
        import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */

public class Cart extends Fragment {

    //Object Declarations
    private Context context = null;
    private ListView listViewProduct;
    public static double oTotal = 0.00;
    public static List<CartProduct> products;
    TextView overallTotal;
    ListView cartList;
    Button checkout;
    DatabaseReference dbRef,cartRef;
    public static String deviceId;
    private static DecimalFormat decimal = new DecimalFormat("#.##");

    public Cart() {
    }

    private void listViewProduct_onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product product = (Product) adapterView.getItemAtPosition(i);
        Toast.makeText(getContext(), product.getName(), Toast.LENGTH_LONG).show();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        //Get Device ID
        deviceId = Installation.id(getContext());

        //Retrieve Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference();

        //List of Products to be placed in Cart
        products = new ArrayList<CartProduct>();

        //Point to Cart in DB
        cartRef = dbRef.child("Cart").child(deviceId);

        //List of Cart products
        listViewProduct = view.findViewById(R.id.listViewProduct);

        //Retrieve Checkout Button
        checkout = view.findViewById(R.id.fetch);

        //Retrieve Overall Total
        overallTotal = view.findViewById(R.id.overallTotal);

        //Retrieve Items From Database
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if(snapshot.child("name").getValue()!=null) {

                        //Assign Item attributes from DB to each product attribute
                        final String productName = snapshot.child("name").getValue().toString();
                        final String price = snapshot.child("price").getValue().toString().replace(',','.');
                        final String id = snapshot.child("id").getValue().toString();
                        final String quantity = snapshot.child("quantity").getValue().toString();
                        final String url = snapshot.child("imageUrl").getValue().toString();

                        //Store Result Returning one of the two following values
                        final String storeResult;
                        if(snapshot.child("shopResult").getValue()!=null)
                            storeResult = snapshot.child("shopResult").getValue().toString();
                        else
                            storeResult = snapshot.child("storeResult").getValue().toString();

                        //Add a product to list of Cart products
                        products.add(new CartProduct(id, productName, price, quantity, url, storeResult));

                    }


                }

                //Re-assign Overall Total
                oTotal = 0.00;

                //Loop Through Cart once opened to get total value
                for(int i = 0; i< products.size();i++)
                {
                    double temp = Double.parseDouble(products.get(i).getTotalPrice().replace(',','.'));
                    temp = (double) Math.round(temp*100)/100;
                    oTotal += temp;
                }

                //Rounding Value to set to Cart Display
                oTotal = (double) Math.round(oTotal*100)/100;
                if(oTotal == 0.00 || oTotal == 0){
                    overallTotal.setVisibility(View.INVISIBLE);
                }else {
                    overallTotal.setVisibility(View.VISIBLE);
                    overallTotal.setText("R " + String.format("%.2f",oTotal).replace(",","."));
                }

                //Send created products to Cart Product Adapter which will generate items
                if(!products.isEmpty()) {
                    CartProductListAdapter productListAdapter = new CartProductListAdapter(getContext(), products, overallTotal);
                    listViewProduct.setAdapter(productListAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Actions to happen when Checkout button is clicked
        checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(oTotal>0) {
                        startActivity(new Intent(getContext(), Login.class));
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Your Cart is empty. Please add products", Toast.LENGTH_LONG).show();
                    }
                }

            });


        return view;
    }
}


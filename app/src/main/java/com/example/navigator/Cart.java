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
import java.util.List;
import java.util.Map;

import adapters.CartProductListAdapter;
import entities.CartProduct;

import static android.app.Activity.DEFAULT_KEYS_DIALER;
import static com.example.navigator.MainActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */

public class
Cart extends Fragment {
    private Context context = null;
    private ListView listViewProduct;

//
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private FirebaseAuth firebaseAuth;
    TextView demoValue;
    TextView overallTotal;
    ListView cartList;
    Button buttonCheckout;

    //Retrieve Images from FirebaseStorage



    DatabaseReference rootRef,demoRef;
    FirebaseStorage storage;
    public Cart() {
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
        final String deviceId = Installation.id(getContext());
        demoValue = (TextView) view.findViewById(R.id.tvValue);
        overallTotal = view.findViewById(R.id.overallTotal);
        rootRef = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        //database reference pointing to Product node
        demoRef = rootRef.child("Wishlist");

        listViewProduct = view.findViewById(R.id.listViewProduct);

        final List<CartProduct> products = new ArrayList<CartProduct>();
        demoRef = rootRef.child("Cart").child(deviceId);
        //final TableLayout myTable = (TableLayout)view.findViewById(R.id.);


        buttonCheckout = (Button) view.findViewById(R.id.fetch);

        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Login.class));
            }
        });





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
                    final String productName = snapshot.child("name").getValue().toString();
                    final String price = snapshot.child("price").getValue().toString();
                    final String id = snapshot.child("id").getValue().toString();
                    final String quantity = snapshot.child("quantity").getValue().toString();
                    String imageName = snapshot.child("imageName").getValue().toString();

                    final CartProduct currCartProduct = new CartProduct();
                    /*final Bitmap[] aBitMap = new Bitmap[1];

                    try{
                        final File localFile = File.createTempFile("images","jpg");

                        StorageReference imageRef = storage.getReferenceFromUrl("gs://bruteforce-d8058.appspot.com").child(id+ ".jpg");



                        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                aBitMap[0] = bitmap;
                                //products.add(new CartProduct(id, productName, price, quantity,bitmap));
                                //Toast.makeText(getContext(),"Local File name: " + localFile.getName() + " image name "+ product.getImageName() , Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onClick(View v) {
                                myTable.removeViewAt(curr);
                                //CODE THAT REMOVES PRODUCT FROM DB GOES HERE
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    */

                    currCartProduct.setCartProduct(id, productName, price, quantity, R.drawable.thumb1);
                    products.add(new CartProduct(id, productName, price, quantity, R.drawable.thumb1));
                    //Load Elements from DB to product list


                }

                CartProductListAdapter productListAdapter = new CartProductListAdapter(getContext(), products);

                listViewProduct.setAdapter(productListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Double oTotal = 0.00;

        for(int i = 0; i< products.size();i++)
        {
            oTotal =+ Double.parseDouble(products.get(i).getTotalPrice());
        }

        //String setTotal = "R " + oTotal;

        overallTotal.setText("R " + oTotal);

        return view;
    }
}

/**
 *
 *  File Name: Scan.js (path: app/src/main/java/com.example.navigator/utils/Scan.js)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  08/07/2019  Mpho  Mashaba    Original
 *  01/08/2019  Thabo Ntsoane    Version 1.0.1
 *
 *  Functional Description: This program file Scan's a product to a Cart or Wishlist.
 *  Error Messages: None
 *  Constraints: Can only be used if Items are available
 *  Assumptions: It is assumed that the user will be able to add items correctly to the cart.
 *
 */
package com.example.navigator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
/**
 * A simple {@link Fragment} subclass.
 */


public class Scan extends Fragment {
  private ZXingScannerView mScannerView;
  private DatabaseReference databaseReference1,databaseReference3,unameref;
  public static TextView resultTextView;
  Button buttonScan;
  Button buttonAddToCart;
  Button addToWishList;
  private FirebaseAuth firebaseAuth;
  private ProgressDialog progressDialog;
  private DatabaseReference rootRef,demoRef;
  private Product objProduct;
  private DatabaseReference ref;

    public Scan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_scan, container, false);
      resultTextView = (TextView) view.findViewById(R.id.result_text);
      buttonScan = (Button) view.findViewById(R.id.btn_scan);
      buttonAddToCart = (Button) view.findViewById(R.id.btn_addToCart);
      addToWishList = (Button)  view.findViewById(R.id.btn_addToWishlist);

      buttonScan.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          startActivity(new Intent(getContext(),ScanCodeActivity.class));
        }
      });


      rootRef = FirebaseDatabase.getInstance().getReference();
      //database reference pointing to demo node
      demoRef = rootRef.child("Product");


      buttonAddToCart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          ref = FirebaseDatabase.getInstance().getReference().child("Cart");
          ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              String sessionId = resultTextView.getText().toString();
              AddProduct(sessionId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
          });
          Toast.makeText(getContext(),"Item added to Cart", Toast.LENGTH_LONG).show();
        }
      });

      addToWishList.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          ref = FirebaseDatabase.getInstance().getReference().child("Wishlist");
          ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              String sessionId = resultTextView.getText().toString();
              AddProduct(sessionId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
          });
          Toast.makeText(getContext(),"Item added to Wish list", Toast.LENGTH_LONG).show();
        }
      });

      return view;
    }
    public void AddProduct(String sessionId){

      if(sessionId.equals("5060466519077")){
        objProduct = new Product("5060466519077","Power Play",19.99);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("8718114642871")){
        objProduct = new Product("8718114642871","Vaseline Lip T",23.99);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6009635830536")){
        objProduct = new Product("6009635830536","Manuscript Book",10.99);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6009695584912")){
        objProduct = new Product("6009695584912","Bioplus",4.99);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6003326009584")){
        objProduct = new Product("6003326009584","Flying Fish Pressed Lemmon",15.99);
        ref.push().setValue(objProduct);
      }
    }
}

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
/**
 * A simple {@link Fragment} subclass.
 */


public class Scan extends Fragment {
  private StorageReference mStorageRef; //Retrieving images from DB.
  private ZXingScannerView mScannerView;
  private DatabaseReference databaseReference1,databaseReference3,unameref;
  public static TextView resultTextView;
  public EditText quantityValue;
  Button buttonScan;
  Button buttonAddToCart;
  Button addToWishList;
  Button incrementQuantity;
  Button decrementQuantity;
  public ImageView scanImage;
  private FirebaseAuth firebaseAuth;
  private ProgressDialog progressDialog;
  private DatabaseReference rootRef,demoRef;
  private Product objProduct;
  private DatabaseReference ref;
  int itemQuantity;
  //Retrieve images from DB
  FirebaseStorage storage = FirebaseStorage.getInstance();
  StorageReference storageRef = storage.getReferenceFromUrl("gs://bruteforce-d8058.appspot.com").child("vaseline_blue_seal.jpg");//remember to remove .child when working
    public Scan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     // mStorageRef = FirebaseStorage.getInstance();//Retrieving From DB
      //StorageReference storageRef = mStorageRef.getReferenceFromUrl("gs://bruteforce-d8058.appspot.com").child("android.jpg");

      // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_scan, container, false);
      resultTextView = (TextView) view.findViewById(R.id.result_text);
      buttonScan = (Button) view.findViewById(R.id.btn_scan);
      buttonAddToCart = (Button) view.findViewById(R.id.btn_addToCart);
      addToWishList = (Button)  view.findViewById(R.id.btn_addToWishlist);
      //Quantity to Cart.
      quantityValue = (EditText) view.findViewById(R.id.edt_Quantity);
      incrementQuantity = (Button) view.findViewById(R.id.btn_Increment_Quantity);
      decrementQuantity = (Button)  view.findViewById(R.id.btn_Decrement_Quantity);
      scanImage = (ImageView) view.findViewById(R.id.img_scanned_product);

      /*
      *   PHONE ID
      * */
      final String deviceId = Installation.id(getContext());
      //Toast.makeText(getContext(),"Your Device ID is: " + deviceId, Toast.LENGTH_LONG).show();

      try {
        final File localFile = File.createTempFile("images", "jpg");
        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
          @Override
          public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
            scanImage.setImageBitmap(bitmap);

          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception exception) {
          }
        });
      } catch (IOException e ) {}

      decrementQuantity.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int  count = Integer.parseInt(quantityValue.getText().toString());
          count--;
          quantityValue.setText(String.valueOf(count));
          itemQuantity = count;
        }
      });

       //final int count = 0;
      incrementQuantity.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        int  count = Integer.parseInt(quantityValue.getText().toString());
          count++;
          quantityValue.setText(String.valueOf(count));
          itemQuantity = count;
        }
      });

      buttonScan.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          startActivity(new Intent(getContext(),ScanCodeActivity.class));

          //CHECK!
         /* String barCode = resultTextView.getText().toString();
          storageRef = storage.getReferenceFromUrl("gs://bruteforce-d8058.appspot.com").child(barCode);
          try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                scanImage.setImageBitmap(bitmap);

              }
            }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception exception) {
              }
            });
          } catch (IOException e ) {}*/
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

              if(dataSnapshot.child(deviceId).exists()){
                ref = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);
                String sessionId = resultTextView.getText().toString();

                //CODE TO RETRIEVE IMAGE THROUGH ITS BARCODE WHICH IS : resultTextView.getText().toString()



                AddProduct(sessionId,itemQuantity);
              }
              else {
                ref.push().setValue(deviceId);
                ref = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);
                String sessionId = resultTextView.getText().toString();
                AddProduct(sessionId,itemQuantity);
              }

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

              if(dataSnapshot.child(deviceId).exists()){
                ref = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(deviceId);
                String sessionId = resultTextView.getText().toString();
                AddProduct(sessionId,itemQuantity);
              }
              else {
                ref.push().setValue(deviceId);
                ref = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(deviceId);
                String sessionId = resultTextView.getText().toString();
                AddProduct(sessionId,itemQuantity);
              }

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
    public void AddProduct(String sessionId, int itemQty){

      if(sessionId.equals("5060466519077")){
        objProduct = new Product("5060466519077","Power Play",19.99,itemQty);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("8718114642871")){
        objProduct = new Product("8718114642871","Vaseline Lip T",23.99,itemQty);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6009635830536")){
        objProduct = new Product("6009635830536","Manuscript Book",10.99,itemQty);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6009695584912")){
        objProduct = new Product("6009695584912","Bioplus",4.99,itemQty);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6003326009584")){
        objProduct = new Product("6003326009584","Flying Fish Pressed Lemmon",15.99,itemQty);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6009690380038")){
        objProduct = new Product("6009690380038","Oasis Still 500ml",9.99,itemQty);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("60018939")){
        objProduct = new Product("60018939","Vaseline Blueseal",23.99,itemQty);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6001120602871")){
        objProduct = new Product("6001120602871","Jungle Bar",10.99,itemQty);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6001120624972")){
        objProduct = new Product("6001120624972","Sour Jelly Beans",22.99,itemQty);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6001007091521")){
        objProduct = new Product("6001007091521","Cream Soda",15.99,itemQty);
        ref.push().setValue(objProduct);
      }
      else if(sessionId.equals("6007652000574")){
        objProduct = new Product("6007652000574","Hand book",12.99,itemQty);
        ref.push().setValue(objProduct);
      }
    }
}

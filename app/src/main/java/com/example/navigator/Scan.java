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
 *  Date        Author              Changes
 *  --------------------------------------------
 *  08/07/2019  Mpho  Mashaba       Original
 *  01/08/2019  Thabo Ntsoane       Version 1.1
 *  14/10/2019  Khodani Tshisimba   Version 1.2, Validate Scan
 *
 *  Functional Description: This program file Scan's a product to a Cart or Wishlist.
 *  Error Messages: None
 *  Constraints: Can only be used if Items are available
 *  Assumptions: It is assumed that the user will be able to add items correctly to the cart.
 *
 */


package com.example.navigator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.navigator.utils.Installation;
import com.example.navigator.utils.SearchDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import adapters.ComparePriceDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;



/**
 * A simple {@link Fragment} subclass.
 */


public class Scan extends Fragment {
  private Context context;
  public static TextView resultTextView;
  public static TextView productName;
  public static TextView productPrice;
  public static ImageView scanImage;
  public static Bitmap scanImageBitmap;
  public static EditText quantityValue;
  /*search*/
  SearchView searchView;
  ListView listView;
  ArrayList<String> list;
  private Button shopResult;
  public static int activeShopIndex = 0;
  public static boolean WishlistBoolean = false;
  public static boolean CartBoolean = false;
  Button buttonScan;
  Button comparePrice;
  Button buttonAddToCart;
  Button addToWishList;
  Button incrementQuantity;
  Button decrementQuantity;
  private LinearLayout comparePriceContainer;
  private LinearLayout imageContainer;
  private LinearLayout qtyContainer;
  private LinearLayout addToCartContainer;
  private LinearLayout addToWishlistContainer;
  public ArrayList<String> otherShops = null;
  public static View view;


  private FirebaseAuth firebaseAuth;
  private ProgressDialog progressDialog;
  private DatabaseReference rootRef,demoRef;
  private Product objProduct;
  private DatabaseReference ref;
  Button buttonCheckout;

  int itemQuantity = 1;
  public static boolean itemFound = false;

  public static String imageUrl;
  //Retrieve images from DB
  //FirebaseStorage storage = FirebaseStorage.getInstance();
  //StorageReference storageRef = storage.getReferenceFromUrl("gs://bruteforce-d8058.appspot.com").child("036002914585.jpg");//remember to remove .child when working

    public Scan() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     // mStorageRef = FirebaseStorage.getInstance();//Retrieving From DB
      //StorageReference storageRef = mStorageRef.getReferenceFromUrl("gs://bruteforce-d8058.appspot.com").child("android.jpg");

      // Inflate the layout for this fragment
      view = inflater.inflate(R.layout.fragment_scan, container, false);
      resultTextView = (TextView) view.findViewById(R.id.result_text);
      productName = (TextView) view.findViewById((R.id.result_name));
      productPrice = (TextView) view.findViewById((R.id.result_price));
      buttonScan = (Button) view.findViewById(R.id.btn_scan);
      buttonAddToCart = (Button) view.findViewById(R.id.btn_addToCart);
      addToWishList = (Button)  view.findViewById(R.id.btn_addToWishlist);
      comparePrice = (Button) view.findViewById(R.id.compare_price);
      //Quantity to Cart.
      quantityValue = (EditText) view.findViewById(R.id.edt_Quantity);
      incrementQuantity = (Button) view.findViewById(R.id.btn_Increment_Quantity);
      decrementQuantity = (Button)  view.findViewById(R.id.btn_Decrement_Quantity);
      scanImage = (ImageView) view.findViewById(R.id.img_scanned_product);
      comparePriceContainer = (LinearLayout) view.findViewById(R.id.compare_price_container);
      imageContainer = (LinearLayout) view.findViewById(R.id.imageContainer);
      qtyContainer = (LinearLayout) view.findViewById(R.id.qtyContainer);
      addToCartContainer = (LinearLayout) view.findViewById(R.id.addToCartContainer);
      addToWishlistContainer = (LinearLayout) view.findViewById(R.id.addToWishlistContainer);
      shopResult = (Button) view.findViewById(R.id.shop_name);

        //Notify = (Button) view.findViewById(R.id.btn_notify);
        rootRef = FirebaseDatabase.getInstance().getReference();
        
        /*
      *   PHONE ID
      * */
        final String deviceId = Installation.id(getContext());

        // Search Bar Implementation-------------------------------------------------------------
        searchView = (SearchView) view.findViewById(R.id.searchView);
        listView = (ListView) view.findViewById(R.id.lv1);
        list = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("Shop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String ShopName = snapshot.child("name").getValue().toString();
                    //String ShopName = snapshot.child("name").toString(); returns {key: name,value : ABSA
                    list.add(ShopName);
                    Log.d("Shop DB Connection","");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(!list.isEmpty()) {
            shopResult.setText(list.get(activeShopIndex));
            shopResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchDialog searchDialog = new SearchDialog(getContext(), list, shopResult);
                    searchDialog.show();
                }
            });
        }

      decrementQuantity.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int  count = Integer.parseInt(quantityValue.getText().toString());

          if(count > 1) {
              count--;
              quantityValue.setText(String.valueOf(count));
              itemQuantity = count;
          }
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

      comparePrice.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!list.isEmpty()) {
                otherShops = new ArrayList<>();
                otherShops.add("Shoprite - R18.00");
                otherShops.add("Pick 'n Pay - R20.00");
                otherShops.add("Spar - R22.00");
                ComparePriceDialog comparePriceDialog = new ComparePriceDialog(getContext(), scanImageBitmap, productName.getText().toString(), productPrice.getText().toString(),
                        list.get(activeShopIndex), otherShops);
                comparePriceDialog.show();
            }
        }
      });


      buttonScan.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Toast.makeText(getContext(), "/"+displayedShop+"/", Toast.LENGTH_LONG).show();
            if(shopResult.getText().toString().equals("Shop Name")|| shopResult.getText().toString().equals("SHOP NAME"))
            {    Toast.makeText(getContext(), "Please Select Current Store", Toast.LENGTH_LONG).show();}
            else
            {startActivity(new Intent(getContext(),ScanCodeActivity.class));}



        }


      });

      rootRef = FirebaseDatabase.getInstance().getReference();
      //database reference pointing to demo node
      demoRef = rootRef.child("Product");

      String sessionId = resultTextView.getText().toString();

        //CODE TO RETRIEVE IMAGE THROUGH ITS BARCODE WHICH IS : resultTextView.getText().toString()

        AddProduct(sessionId,productName.getText().toString(),productPrice.getText().toString(),itemQuantity,imageUrl,list.get(activeShopIndex));



      buttonAddToCart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          CartBoolean = true;

          ref = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);
          final DatabaseReference cartRef = ref;

          //Establish DB Connection to check if same item exists in the database
            Query myQuery = cartRef.orderByChild("idShopResult").equalTo(objProduct.getIdShopResult());

            myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        itemFound = true;
                        int tempQuantity = Integer.parseInt(dataSnapshot.child("quantity").getValue().toString());
                        tempQuantity += objProduct.getQuantity();
                        dataSnapshot.child("quantity").getRef().setValue(tempQuantity);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if(!itemFound) {

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(deviceId).exists()) {
                            ref = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);
                            ref.push().setValue(objProduct);
                        } else {
                            ref.push().setValue(deviceId);//shopResult
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
          Toast.makeText(getContext(),"Item added to Cart", Toast.LENGTH_LONG).show();
        }
      });

      addToWishList.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            itemFound = false;
            WishlistBoolean = true;
          ref = FirebaseDatabase.getInstance().getReference().child("Wishlist");
            final DatabaseReference WLRef = ref;

            //Establish DB Connection to check if same item exists in the database
            Query myQuery = WLRef.orderByChild("idShopResult").equalTo(objProduct.getIdShopResult());

            myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        itemFound = true;
                        int tempQuantity = Integer.parseInt(dataSnapshot.child("quantity").getValue().toString());
                        tempQuantity += objProduct.getQuantity();
                        dataSnapshot.child("quantity").getRef().setValue(tempQuantity);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if(!itemFound) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(deviceId).exists()) {
                            ref = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(deviceId);
                            ref.push().setValue(objProduct);
                        } else {
                            ref.push().setValue(deviceId);//shopResult
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
          Toast.makeText(getContext(),"Item added to Wish list", Toast.LENGTH_LONG).show();
        }
      });




      return view;
    }

    public void AddProduct(String sessionId,String PName,String pPrice, final int itemQty, String imageUrl,String shopResult){
      objProduct = new Product(sessionId,PName,pPrice,itemQty,imageUrl,shopResult);
      //ref.push().setValue(objProduct);
    }
}



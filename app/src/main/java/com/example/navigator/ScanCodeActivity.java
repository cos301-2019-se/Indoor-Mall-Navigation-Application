package com.example.navigator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigator.utils.Installation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.Result;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static java.security.AccessController.getContext;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private Context context;
    private DatabaseReference rootRef,demoRef;
    ZXingScannerView ScannerView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    public static ArrayList<String> otherShops = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        rootRef = FirebaseDatabase.getInstance().getReference();
        setContentView(ScannerView);
        demoRef = rootRef.child("Product");
    }

    @Override
    public void handleResult(final Result result) {
        Scan.view.findViewById(R.id.compare_price_container).setVisibility(View.VISIBLE);
        Scan.view.findViewById(R.id.imageContainer).setVisibility(View.VISIBLE);
        Scan.view.findViewById(R.id.qtyContainer).setVisibility(View.VISIBLE);
        Scan.view.findViewById(R.id.addToCartContainer).setVisibility(View.VISIBLE);
        Scan.view.findViewById(R.id.addToWishlistContainer).setVisibility(View.VISIBLE);
        Scan.quantityValue.setText("1");

        Scan.resultTextView.setText(result.getText());
        String imageURL = result.getText()+".jpg";
        try {
            final File localFile = File.createTempFile("images", "jpg");
            StorageReference storageLocation = storage.getReferenceFromUrl("gs://bruteforce-d8058.appspot.com").child(imageURL);//imageURL
            storageLocation.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Scan.imageUrl = uri.toString();
                }
            });

            storageLocation.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    //Toast.makeText(getContext(), localFile.getName(),Toast.LENGTH_LONG).show();
                    Scan.scanImage.setImageBitmap(bitmap);
                    Scan.scanImageBitmap = bitmap;

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}

        //Establish DB Connection
        /*final String deviceId = Installation.id(this);
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);

        if(Scan.WishlistBoolean == true)
            DBRef = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(deviceId);
        else if(Scan.CartBoolean == true)
            DBRef = FirebaseDatabase.getInstance().getReference().child("Cart").child(deviceId);*/

        /*Query myQuery = DBRef.orderByChild("id").equalTo(product.getId());


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnap : dataSnapshot.getChildren())
                {
                    dataSnap.child("id").getRef().setValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myQuery.addListenerForSingleValueEvent(valueEventListener);*/



        /**/
        demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               boolean foundFlag = false;
               otherShops.clear();
               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   String snapResult =  snapshot.child("id").getValue().toString();
                   if(result.toString().equals(snapResult)){
                       String productNam = snapshot.child("name").getValue().toString();

                       //Get specific price for specific shop.
                       DataSnapshot storeSnapshot = snapshot.child("store");
                       Iterable<DataSnapshot> storeChildren = storeSnapshot.getChildren();
                       String productPrices = "0.00";
                       for (DataSnapshot store : storeChildren) {
                           if(store.child("shop").getValue().toString().equals(Scan.passIndex)){
                               //Toast.makeText(getApplicationContext(),"It's set. " , Toast.LENGTH_LONG).show();
                               foundFlag = true;
                               productPrices = store.child("price").getValue().toString();
                           }
                           else{
                                String output = "It's R"+store.child("price").getValue().toString()+ " at "+store.child("shop").getValue().toString();
                                otherShops.add(output);
                           }
                           //Contact c = contact.getValue(Contact.class);
                       }
                       //String productPrices = snapshot.child("price").getValue().toString();
                        if(otherShops.isEmpty()){
                            Scan.view.findViewById(R.id.compare_price_container).setVisibility(View.GONE);
                        }
                        else{
                            Scan.view.findViewById(R.id.compare_price_container).setVisibility(View.VISIBLE);
                        }
                       Scan.productName.setText(productNam);
                       Scan.productPrice.setText("R"+productPrices);

                       break;
                   }
               }
               if (foundFlag == false){
                   Scan.view.findViewById(R.id.compare_price_container).setVisibility(View.INVISIBLE);
                   Scan.view.findViewById(R.id.imageContainer).setVisibility(View.INVISIBLE);
                   Scan.view.findViewById(R.id.qtyContainer).setVisibility(View.INVISIBLE);
                   Scan.view.findViewById(R.id.addToCartContainer).setVisibility(View.INVISIBLE);
                   Scan.view.findViewById(R.id.addToWishlistContainer).setVisibility(View.INVISIBLE);
                   Scan.view.findViewById(R.id.result_name).setVisibility(View.INVISIBLE);
                   Scan.view.findViewById(R.id.result_price).setVisibility(View.INVISIBLE);
                   Toast.makeText(getApplicationContext(),"Product not on System", Toast.LENGTH_LONG).show();
               }
               else {
                   //Scan.view.findViewById(R.id.compare_price_container).setVisibility(View.VISIBLE);
                   Scan.view.findViewById(R.id.imageContainer).setVisibility(View.VISIBLE);
                   Scan.view.findViewById(R.id.qtyContainer).setVisibility(View.VISIBLE);
                   Scan.view.findViewById(R.id.addToCartContainer).setVisibility(View.VISIBLE);
                   Scan.view.findViewById(R.id.addToWishlistContainer).setVisibility(View.VISIBLE);
                   Scan.view.findViewById(R.id.result_name).setVisibility(View.VISIBLE);
                   Scan.view.findViewById(R.id.result_price).setVisibility(View.VISIBLE);
               }

           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
        /**/
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}

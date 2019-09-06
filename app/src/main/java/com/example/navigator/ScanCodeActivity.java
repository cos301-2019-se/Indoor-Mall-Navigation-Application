package com.example.navigator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.Result;

import java.io.File;
import java.io.IOException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static java.security.AccessController.getContext;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private Context context;
    private DatabaseReference rootRef,demoRef;
    ZXingScannerView ScannerView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
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

        Scan.resultTextView.setText(result.getText());
        String imageURL = result.getText()+".jpg";
        try {
            final File localFile = File.createTempFile("images", "jpg");
            StorageReference storageLocation = storage.getReferenceFromUrl("gs://bruteforce-d8058.appspot.com").child(imageURL);//imageURL
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

        /**/
        demoRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               //int count = 1;
               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   String snapResult =  snapshot.child("id").getValue().toString();
                   if(result.toString().equals(snapResult)){
                       String productNam = snapshot.child("name").getValue().toString();
                       String productPrices = snapshot.child("price").getValue().toString();

                       Scan.productName.setText(productNam);
                       Scan.productPrice.setText("R"+productPrices);
                   }
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

package com.example.navigator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
  private FirebaseAuth firebaseAuth;


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
      buttonAddToCart = (Button) view.findViewById(R.id.btn_addToCart) ;



      buttonScan.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          startActivity(new Intent(getContext(),ScanCodeActivity.class));


        }
      });

      buttonAddToCart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          startActivity(new Intent(getContext(),Login.class));
          Intent newIntent = new Intent(getContext(), Login.class);
          newIntent.putExtra("EXTRA_SESSION_ID", resultTextView.getText());
          startActivity(newIntent);

        }
      });

      return view;

    }

}

package com.example.navigator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
/**
 * A simple {@link Fragment} subclass.
 */
public class Scan extends Fragment {

  public static TextView resultTextView;
  Button buttonScan;
  Button buttonAddToCart;
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

          startActivity(new Intent(getContext(),Signup.class));

        }
      });

      return view;

    }

}

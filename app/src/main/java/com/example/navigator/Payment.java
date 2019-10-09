package com.example.navigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import adapters.CartProductListAdapter;


public class Payment extends AppCompatActivity implements View.OnClickListener {

    private ImageView debitImage;
    private ImageView eftImage;
    public TextView totalCheckOutPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        debitImage = (ImageView) findViewById(R.id.imageCredit);
        eftImage = (ImageView) findViewById(R.id.imageEFT);
        totalCheckOutPrice = (TextView) findViewById(R.id.totalPrice);

        /*Calling the overall value
         *
         * Retrieve it from the display
         *
         * String sOverallTotal = localOverall.getText().toString().substring(2);
         * double valueAsDouble = Double.parseDouble(sOverallTotal);
         *
         * */

        String sOverallTotal = totalCheckOutPrice.getText().toString().substring(2);
        totalCheckOutPrice.setText(sOverallTotal);


        debitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CardInfo.class);
                view.getContext().startActivity(intent);}
        });

    }

    @Override
    public void onClick(View view) {

    }
}

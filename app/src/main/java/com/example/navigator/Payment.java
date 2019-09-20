package com.example.navigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


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

package com.example.navigator;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Navigate navigateFragment = new Navigate();
        final Cart cartFragment = new Cart();
        final Scan scanFragment = new Scan();
        final Search searchFragment = new Search();
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);



        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        setFragment(navigateFragment);
                        return true;
                    case R.id.navigation_search:
                        setFragment(searchFragment);
                        return true;
                    case R.id.navigation_scan:
                        setFragment(scanFragment);
                        //mTextMessage.setText(R.string.title_scan);
                        return true;
                    case R.id.navigation_cart:
                        setFragment(cartFragment);
                        //mTextMessage.setText(R.string.title_cart);
                        return true;
                }
                return false;
            }
        };


        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

}

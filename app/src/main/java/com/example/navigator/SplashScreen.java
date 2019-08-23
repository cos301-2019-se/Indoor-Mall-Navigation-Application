/**
 *
 *  File Name: SplashScreen.js (path: app/src/main/java/com.example.navigator/utils/SplashScreen.js)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  11/07/2019  Mpho Mashaba    Original
 *
 *  Functional Description: This program file is a splashScreen file
 *  Error Messages: none
 *  Constraints: none
 *  Assumptions: none
 *
 */
package com.example.navigator;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar2;
    private GifImageView gifImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

       // progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        gifImageView = (GifImageView) findViewById(R.id.gifImageView);

        try {
            InputStream inputstream = getAssets().open("giphy.gif");
            byte[] bytes = IOUtils.toByteArray(inputstream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        }
        catch (IOException e){

        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //SplashScreen.this.startActivity(SplashScreen.this,MainActivity.class);
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                SplashScreen.this.finish();

            }
        },5000);

    }
}

//Previous One

      /*  ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        final boolean connected = networkinfo != null && networkinfo.isAvailable() && networkinfo.isConnected();
        Log.v("Network state : ", connected + "");

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 5000) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Looper.prepare();
                    if (connected == false) {
                        SplashScreen.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(SplashScreen.this, "You must connect to the Internet to continue", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();

                            }
                        });
                        //finish();
                        Intent intent=new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);

                    } else {
                        finish();
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    }
                    Looper.loop();
                }
            }
        };
        splashThread.start();
*/




/*
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class); startActivity(i);
                finish(); } }, 3000);
    }





}
*/


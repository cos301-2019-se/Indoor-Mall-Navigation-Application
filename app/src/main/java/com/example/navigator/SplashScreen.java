package com.example.navigator;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    }

}






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


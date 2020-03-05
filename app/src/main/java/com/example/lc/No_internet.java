package com.example.lc;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class No_internet extends AppCompatActivity {
Button try_again;
ProgressBar connection_internet_progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        getSupportActionBar().hide();
        connection_internet_progressBar=findViewById(R.id.internet_connection_progressBar);

        try_again=findViewById(R.id.try_again);
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection_internet_progressBar.setVisibility(View.VISIBLE);
                if(isConnected()){
                    startActivity(new Intent(No_internet.this,home.class));
                    finish();
                }else{
                    connection_internet_progressBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            connection_internet_progressBar.setVisibility(View.INVISIBLE);
                        }
                    },2000);
                    connection_internet_progressBar.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    public boolean isConnected(){
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}

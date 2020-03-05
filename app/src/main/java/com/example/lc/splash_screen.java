package com.example.lc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class  splash_screen extends AppCompatActivity {
    SharedPreference mPref;
    TextView lc_logo_text;

    private final int VERIFY_PERMISSIONS_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        lc_logo_text=findViewById(R.id.text);
        //YoYo.with(Techniques.RollIn).duration(500).repeat(2).playOn(lc_logo_text);
        mPref = new SharedPreference(this);

        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(checkPermissionsArray(Permissions.PERMISSIONS)){
                        if (isConnected()) {
                            boolean loginstatus = mPref.getLoginStatus();
                            if (loginstatus == false) {
                                Intent intent = new Intent(splash_screen.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(splash_screen.this, home.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                    }else{
                        verifyPermissions(Permissions.PERMISSIONS);
                    }

                    } else {
                        startActivity(new Intent(splash_screen.this, No_internet.class));
                        finish();
                    }
                }
            }
        };t.start();


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

    //PERMISSIONS METHODS

    public boolean checkPermissionsArray(String[] permissions){
        for(int i=0;i<permissions.length;i++){
            String check=permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(String permissions){
        int permissionRequest= ActivityCompat.checkSelfPermission(splash_screen.this,permissions);
        if(permissionRequest!= PackageManager.PERMISSION_GRANTED){
            return false;
        }else{
            return true;
        }
    }

    public void verifyPermissions(String[] permission){
        ActivityCompat.requestPermissions(splash_screen.this,permission,VERIFY_PERMISSIONS_REQUEST);
    }
}

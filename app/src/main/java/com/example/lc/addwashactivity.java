package com.example.lc;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class addwashactivity extends AppCompatActivity {
    private static final String TAG="addwashactivity";
    private static final int ACTIVITY_NUM=2;
    private static final int VERIFY_PERMISSIONS_REQUEST=1;
    private Context mContext=addwashactivity.this;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addwashone);
        getSupportActionBar().setTitle("Wash List");
        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            setupviewpager();
        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }
    }

    private void setupviewpager(){
        SectionsPagerAdapter adapter=new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new addwash());
        adapter.addFragment(new washStatus());
        mViewPager=findViewById(R.id.container);
        mViewPager.setAdapter(adapter);
        TabLayout tabLayout=findViewById(R.id.tabstop);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText("Add Wash");
        tabLayout.getTabAt(1).setText("Wash Status");
    }

    public void verifyPermissions(String[] permission){
        ActivityCompat.requestPermissions(addwashactivity.this,permission,VERIFY_PERMISSIONS_REQUEST);
    }

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
        int permissionRequest= ActivityCompat.checkSelfPermission(addwashactivity.this,permissions);
        if(permissionRequest!= PackageManager.PERMISSION_GRANTED){
            return false;
        }else{
            return true;
        }
    }
}

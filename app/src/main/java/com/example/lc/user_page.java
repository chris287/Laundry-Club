package com.example.lc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class user_page extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        setTitle(R.string.user_title);


    }


    public void adduserfragment(View view){
        Fragment fraguser;
        fraguser=new adduser_frag();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frag,fraguser);
        ft.commit();
    }

    public void viewuserfragment(View view){
        Intent in=new Intent(user_page.this,viewuser_frag.class);
        startActivity(in);
        finish();
    }




}

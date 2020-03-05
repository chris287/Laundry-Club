package com.example.lc;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {

    DataBaseHandler dbh;
    SharedPreference ref;
    FirebaseAuth fAuth;

ImageButton user_click,wash_click,accounts_click,store_click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(R.string.home_title);


        fAuth=FirebaseAuth.getInstance();
        fAuth= FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser()==null){
            startActivity(new Intent(home.this,MainActivity.class));
            finish();
        }
       // dbh=new DataBaseHandler(this);
        ref=new SharedPreference(this);

        user_click=findViewById(R.id.user_imgbttn);
        wash_click=findViewById(R.id.wash_imgbtn);
        accounts_click=findViewById(R.id.accounts_imgbtn);
        store_click=findViewById(R.id.store_imgbtn);
        user_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user_intent=new Intent(home.this,user_page.class);
                startActivity(user_intent);
            }
        });

        wash_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wash_intent=new Intent(home.this,addwashactivity.class);
                startActivity(wash_intent);
            }
        });


        accounts_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account_intent=new Intent(home.this,accounts_page.class);
                startActivity(account_intent);
            }
        });

        store_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent store_intent=new Intent(home.this,store_page.class);
                startActivity(store_intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.logoutitem) {
            new AlertDialog.Builder(this).setTitle("Confirmation").setMessage("Are you sure you want to Logout?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fAuth.signOut();
                    finish();
                    startActivity(new Intent(home.this,MainActivity.class));

                }
            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();

        }

        return super.onOptionsItemSelected(item);
    }
}

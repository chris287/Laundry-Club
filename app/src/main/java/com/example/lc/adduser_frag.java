package com.example.lc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class adduser_frag extends Fragment {
    AppCompatActivity a=new AppCompatActivity();

    Context context=this.getContext();
    EditText nameet,regnoet,rmnoet,rmob;
    Button btnclk;
    DatabaseReference dbref;
    TextView txt;
    long id=0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_adduser_frag, container,false);

    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {

        nameet=getView().findViewById(R.id.edittextuname);
        regnoet=getView().findViewById(R.id.edittextregno);
        rmnoet=getView().findViewById(R.id.edittextrmno);
        dbref= FirebaseDatabase.getInstance().getReference().child("Users");
        btnclk=getView().findViewById(R.id.adduserbtn);
        txt=getView().findViewById(R.id.adduserRequire);
        rmob=getView().findViewById(R.id.edittextmobno);


        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    id=dataSnapshot.getChildrenCount();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=nameet.getText().toString().trim();
                final String regno=regnoet.getText().toString().trim();
                final String rmno=rmnoet.getText().toString().trim();
                final String mobno=rmob.getText().toString().trim();
                final String mob="+91"+mobno;

                if(name.equals("") || regno.equals("") || rmno.equals("")){
                    txt.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt.setVisibility(View.INVISIBLE);
                        }
                    },3000);
                    txt.setVisibility(View.VISIBLE);
                }else {
                    Query query=dbref.getParent().child("Users").orderByChild("mRegNo").equalTo(regno);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                UserModule u=new UserModule();
                                u.setmUsername(name);
                                u.setmRoomNo(rmno);
                                u.setmRegNo(regno);
                               u.setmMobNo(mob);
                                dbref.child(String.valueOf(id+1)).setValue(u);
                                Toast.makeText(getContext(),"User "+name+" added successfully",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(),"RegNo already exists",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
//QR CODE SCANNER
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        final IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        int a=0;
        if(a==0){
            if(a!=0){
                Toast.makeText(getContext().getApplicationContext(),"Result Not Found",Toast.LENGTH_SHORT).show();
            }else{
                AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(getContext().getApplicationContext());
                alertdialogbuilder.setMessage(result.getContents()+"\n\nScan Again?");
                alertdialogbuilder.setTitle("Result Scanned");
                alertdialogbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str=result.getContents();
                        regnoet.setText(str);
                    }
                });
                 alertdialogbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog=alertdialogbuilder.create();
                alertDialog.show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }*/





    /*public void scan(){
        IntentIntegrator integrator=new IntentIntegrator(a.getParent());
        integrator.setCaptureActivity(portrait.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan your ID");
        integrator.initiateScan();
    }*/

}


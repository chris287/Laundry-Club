package com.example.lc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class addwash extends Fragment implements com.example.lc.firebaseListner {
EditText dateEdt,uNameedt,uRmnoedt;
Button addWash;
public StorageReference filePath;
private String name,rmno;
private int mYear, mMonth, mDay, mHour, mMinute;
SearchableSpinner searchableSpinner;
DatabaseReference dbref;
firebaseListner firebaseListner;
List<UserModule> list;
ArrayAdapter<String> adapter;
private static final int CAMERA_REQUEST_CODE=101;
ImageView pic;



long id=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.activity_addwash,container,false);
        pic=view.findViewById(R.id.takepic);
        uNameedt=view.findViewById(R.id.user_name);
        uRmnoedt=view.findViewById(R.id.washRoomNo);
        dateEdt=view.findViewById(R.id.washDate);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((addwashactivity)getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])){
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CAMERA_REQUEST_CODE);

                }else{
                    Intent intent=new Intent(getActivity(),addwashactivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
        return view;



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST_CODE){
           Bitmap img= (Bitmap)data.getExtras().get("data");
           pic.setImageBitmap(img);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        searchableSpinner=view.findViewById(R.id.searchable_spinner);
        dbref= FirebaseDatabase.getInstance().getReference("Users");

        firebaseListner=this;
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserModule> userModuleList=new ArrayList<>();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    userModuleList.add(data.getValue(UserModule.class));
                }
                firebaseListner.onFireBaseLoadSuccess(userModuleList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseListner.onFireBaseLoadFailed(databaseError.getMessage());
            }
        });

        dateEdt=view.findViewById(R.id.washDate);
        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateEdt.setText(dayOfMonth +"-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String spinner_Value=searchableSpinner.getSelectedItem().toString();
                Query q=dbref.orderByChild("mRegNo").equalTo(spinner_Value);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            UserModule um=ds.getValue(UserModule.class);
                            name=um.getmUsername();
                            rmno=um.getmRoomNo();
                        }
                        uNameedt.setText(name);
                        uRmnoedt.setText(rmno);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addWash=view.findViewById(R.id.addwashbutton);

        addWash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String spinner_value=searchableSpinner.getSelectedItem().toString();
                final String date=dateEdt.getText().toString();
                final String name=uNameedt.getText().toString().trim();
                final String rmno=uRmnoedt.getText().toString();


                dbref=FirebaseDatabase.getInstance().getReference().child("Wash");

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
                Query query=dbref.getParent().child("Wash").orderByChild("mRegNo").equalTo(spinner_value);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserModule u=new UserModule();
                        u.setmRegNo(spinner_value);
                        u.setWashDate(date);
                        u.setmUsername(name);
                        u.setmRoomNo(rmno);
                        dbref.child(String.valueOf(id+1)).setValue(u);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


    }



    @Override
    public void onFireBaseLoadSuccess(List<UserModule> userModuleList) {
        list=userModuleList;
        List<String> regno_list=new ArrayList<>();
        for(UserModule userModule:userModuleList)
            regno_list.add(userModule.getmRegNo());
        adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,regno_list);
        searchableSpinner.setAdapter(adapter);

    }

    @Override
    public void onFireBaseLoadFailed(String message) {
        Toast.makeText(getContext(),"Firebase Load Failed.Reload the page or pease try after some time.\nThank You",Toast.LENGTH_SHORT);
    }



}

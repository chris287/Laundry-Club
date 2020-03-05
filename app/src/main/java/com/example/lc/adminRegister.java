package com.example.lc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adminRegister extends AppCompatActivity {

//VARIABLE DECLARATION
    EditText uNameet,pswet,regNoet,rmNoet,emailedt,phnoet;
    Button submitBtn;
    SharedPreference mpref;
    String a_Uname,a_Password,a_regNo,a_rmNo,a_Email,a_Phno;
    long id=0;
    FirebaseAuth fAuth;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

//RETRIEVING THE IDS
        uNameet=findViewById(R.id.reg_etUname);
        pswet=findViewById(R.id.reg_etpsw);
        regNoet=findViewById(R.id.reg_etregNo);
        rmNoet=findViewById(R.id.reg_etrmNo);
        mpref=new SharedPreference(this);
        final DatabaseReference fref= FirebaseDatabase.getInstance().getReference().child("Admin");
        fAuth=FirebaseAuth.getInstance();
        submitBtn=findViewById(R.id.reg_btn);
        emailedt=findViewById(R.id.reg_etUemail);
        pb=findViewById(R.id.progress);
        phnoet=findViewById(R.id.reg_etphno);


//INITIALIZING THE ID ATTRIBUTE TO THE NUMBER OF CHILD NODE VALUES AVAILABLE IN DATABASE
        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    id=(dataSnapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//SETTING ONCLICKLISTNER TO SUBMIT BUTTON
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//GETTING THE USER INPUT VALUES
                a_Uname=uNameet.getText().toString().trim();
                a_Password=pswet.getText().toString().trim();
                a_regNo=regNoet.getText().toString().trim();
                a_rmNo=rmNoet.getText().toString().trim();
                a_Email=emailedt.getText().toString().trim();
                a_Phno=phnoet.getText().toString().trim();
//CREATING ADMIN ACCOUNT WITH EMAIL AND PASSWORD AUTHENTICATION
                registerUser();
//QUERY TO CHECK IF THE REGNO EXISTS
                Query check=fref.getParent().child("Admin").orderByChild("aRegNo").equalTo(a_regNo);
               if(a_Uname.isEmpty()){
                   uNameet.setError("Name Required");
                   uNameet.requestFocus();
                   return;
               }
               if(a_Password.isEmpty()){
                   pswet.setError("Password Required");
                   pswet.requestFocus();
                   return;
               }
               if(a_Password.length()<6){
                   pswet.setError("Minimum characters-6");
                   pswet.requestFocus();
                   return;
               }
               if(a_Email.isEmpty()){
                   emailedt.setError("E-Mail Required");
                   emailedt.requestFocus();
                   return;
               }
               if(!Patterns.EMAIL_ADDRESS.matcher(a_Email).matches()){
                   emailedt.setError("Enter a valid E-mail");
               }
               if(a_regNo.isEmpty()){
                   regNoet.setError("Register number Required");
                   regNoet.requestFocus();
                   return;
               }
               if(a_rmNo.isEmpty()){
                   rmNoet.setError("Room number Required");
                   rmNoet.requestFocus();
                   return;
               }
               if(a_Phno.isEmpty()){
                   phnoet.setError("Mobile Number Required");
                   phnoet.requestFocus();
                   return;
               }
//ADDING VALUEEVENTLISTNER FOR CHECK QUERY
                   check.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//CHECKING IF THE DATASNAPSHOT OF QUERY CHECK EXISTS(if exists donot add admin i.e REGNO already exists.Else add new admin account)
                           if(!dataSnapshot.exists()){
                               UserModule u=new UserModule();
                               u.setaUsername(a_Uname);
                               u.setaPassword(a_Password);
                               u.setaRegNo(a_regNo);
                               u.setaRoomNo(a_rmNo);
                               u.setaEmail(a_Email);
                               u.setaPhno(a_Phno);
                               fref.child(String.valueOf(id+1)).setValue(u);
                               Toast.makeText(getApplicationContext(),"Admin Account created successfully",Toast.LENGTH_SHORT).show();
                               mpref.setLoginStatus(true);
                               mpref.setusername(a_Uname);
                               Intent intent = new Intent(adminRegister.this, home.class);
                               startActivity(intent);
                               finish();
                           }else{
                               Toast.makeText(getApplicationContext(),"RegNo already exists",Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

            }

            private void registerUser() {
                a_Email=emailedt.getText().toString().trim();
                a_Password=pswet.getText().toString().trim();

                if(a_Email.isEmpty()){
                    emailedt.setError("Email is required");
                    emailedt.requestFocus();
                    return;

                }
                if(a_Password.isEmpty()){
                    pswet.setError("Password is required");
                    pswet.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(a_Email).matches()){
                    emailedt.setError("Please enter a valid email");
                    emailedt.requestFocus();
                    return;
                }
                if(a_Password.length()<6){
                    pswet.setError("Minimum length is 6");
                    pswet.requestFocus();
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(a_Email,a_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pb.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });

    }
}

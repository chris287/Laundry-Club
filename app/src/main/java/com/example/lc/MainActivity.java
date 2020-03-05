package com.example.lc;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    DatabaseReference dbref;
    SharedPreference mpref;
    FirebaseAuth fAuth;


    ProgressBar progressBar;
    EditText entered_username,entered_password,entered_mobno,entered_otp;
    Button click_login,click_login_mobileno;
    ImageButton generate;
    TextView ertext,regtxt;

    String pno,otp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        entered_username=findViewById(R.id.username_edittext);
        entered_password=findViewById(R.id.password_edittext);
        click_login=findViewById(R.id.login_button);
        ertext=findViewById(R.id.error_textview);
        regtxt=findViewById(R.id.registerText);
        progressBar=findViewById(R.id.pb);
        mpref=new SharedPreference(this);
        entered_mobno=findViewById(R.id.mno_edittext);
        entered_otp=findViewById(R.id.otp_edittext);
        generate=findViewById(R.id.mobnobtn);
        click_login_mobileno=findViewById(R.id.loginPno_button);



        fAuth=FirebaseAuth.getInstance();
        dbref= FirebaseDatabase.getInstance().getReference().child("Admin");
          if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,home.class));
            finish();
        }
        regtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(MainActivity.this,adminRegister.class);
                startActivity(in);
            }
        });

        //setting link underline for Register text
        regtxt.setPaintFlags(regtxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        click_login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    adminLogin();
            }
        });

        //MOBILE OTP AUTHENTICATION

        StartFirebaseLogin();
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pno = entered_mobno.getText().toString();
                if (pno.isEmpty() || pno.length()<10) {
                    entered_mobno.setError("Enter a valid m obile number");
                    entered_mobno.requestFocus();
                    return;
                } else {


                    PhoneAuthProvider.getInstance().verifyPhoneNumber(

                            pno,                     // Phone number to verify

                            60,                           // Timeout duration

                            TimeUnit.SECONDS,                // Unit of timeout

                            MainActivity.this,        // Activity (for callback binding)

                            mCallback);
                }
            }
        });
        click_login_mobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp=entered_otp.getText().toString();
                if(otp.isEmpty()){
                    entered_otp.setError("Enter OTP");
                    entered_otp.requestFocus();
                    return;
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                    SigninWithPhone(credential);
                }
            }
        });
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity.this,home.class));
                    finish();

                } else {

                    Toast.makeText(MainActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }

    private void StartFirebaseLogin() {
        fAuth=FirebaseAuth.getInstance();
        mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this,"verification Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode=s;
                Toast.makeText(MainActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void adminLogin() {
        String check_user=entered_username.getText().toString().trim();
        String check_pass=entered_password.getText().toString().trim();
        if(check_user.isEmpty()){
            entered_username.setError("Please enter Email");
            entered_username.requestFocus();
            return;
        }
        if(check_pass.isEmpty()) {
            entered_password.setError("Please enter password");
            entered_password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        fAuth.signInWithEmailAndPassword(check_user,check_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){

                    startActivity(new Intent(MainActivity.this,home.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Invalid Email or Password",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

package com.example.administrator.whyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public
class phone extends AppCompatActivity {
    private
    Button sendverificationcodebtn,verificationcodebtn;
    private
    EditText inputphonrnumber,inputverificationcode;
    private
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog Loadingbar;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        mAuth=FirebaseAuth.getInstance();
        sendverificationcodebtn=(Button)findViewById(R.id.send_verify_code_button);
        verificationcodebtn=(Button)findViewById(R.id.verify_button);
        inputphonrnumber=(EditText)findViewById(R.id.phone_number_input);
        inputverificationcode=(EditText)findViewById(R.id.verification_code_input);
        Loadingbar=new ProgressDialog(this);
        sendverificationcodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {

                String phoneNumber=inputphonrnumber.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(phone.this,"Please enter your phone number...",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Loadingbar.setTitle("Phone verification");
                    Loadingbar.setMessage("please wait while we are authenticating your phone...");
                    Loadingbar.setCanceledOnTouchOutside(false);
                    Loadingbar.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            phone.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });
        verificationcodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                sendverificationcodebtn.setVisibility(View.INVISIBLE);
                inputphonrnumber.setVisibility(View.INVISIBLE);
                String verificationcode=inputverificationcode.getText().toString();
                if (TextUtils.isEmpty(verificationcode))
                {
                    Toast.makeText(phone.this,"please enter the verification code..",Toast.LENGTH_LONG).show();
                }
                else{
                    Loadingbar.setTitle("Code verification");
                    Loadingbar.setMessage("please wait while we are verifiying verification code...");
                    Loadingbar.setCanceledOnTouchOutside(false);
                    Loadingbar.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationcode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public
            void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public
            void onVerificationFailed(FirebaseException e) {
                Loadingbar.dismiss();
                Toast.makeText(phone.this,"Invlaid phone number! Please enter valid phone number",Toast.LENGTH_LONG).show();
                sendverificationcodebtn.setVisibility(View.VISIBLE);
                inputphonrnumber.setVisibility(View.VISIBLE);
                verificationcodebtn.setVisibility(View.INVISIBLE);
                inputverificationcode.setVisibility(View.INVISIBLE);
            }
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                Loadingbar.dismiss();
                Toast.makeText(phone.this,"Code has been sent Please verify",Toast.LENGTH_LONG).show();
                sendverificationcodebtn.setVisibility(View.INVISIBLE);
                inputphonrnumber.setVisibility(View.INVISIBLE);
                verificationcodebtn.setVisibility(View.VISIBLE);
                inputverificationcode.setVisibility(View.VISIBLE);

                // ...
            }
        };
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public
            void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Loadingbar.dismiss();
                    Toast.makeText(phone.this,"Congratulation you have login successfully..",Toast.LENGTH_LONG).show();
                    SenduserToMainactivity();
                }
                else {
                    String message=task.getException().toString();
                    Toast.makeText(phone.this,"Error:"+message,Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private
    void SenduserToMainactivity() {
        Intent intent=new Intent(phone.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}


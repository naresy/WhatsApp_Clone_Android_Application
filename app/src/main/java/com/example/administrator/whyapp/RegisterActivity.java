package com.example.administrator.whyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public
class RegisterActivity extends AppCompatActivity {
    private
    Button registerbutton;
    private
    EditText registeremail,registerpassword;
    private
    TextView registeralredyhaveaccount;
    //for firebase
    private FirebaseAuth mAuth;
    private
    DatabaseReference mRef;
    //for progress bar
    private
    ProgressDialog loadingbar;
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mRef=FirebaseDatabase.getInstance().getReference();
        clickbutton();
        registeralredyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                SendusertoLogin();

            }
        });
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {

                createaccount();

            }
        });


    }

    private
    void createaccount() {
        String Email=registeremail.getText().toString();
        String Password=registerpassword.getText().toString();
        if(TextUtils.isEmpty(Email)){
            Toast.makeText(RegisterActivity.this,"please enter email..",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(Password)){
            Toast.makeText(RegisterActivity.this,"please enter password..",Toast.LENGTH_LONG).show();
        }
        else {
            loadingbar.setTitle("Creating new account");
            loadingbar.setMessage("please wait while creating an account...");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();
            mAuth.createUserWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public
                        void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                String currentUserid=mAuth.getCurrentUser().getUid();
                                mRef.child("Users").child(currentUserid).setValue("");
                               sendusertomainactivity();
                                Toast.makeText(RegisterActivity.this,"Account create successfull",Toast.LENGTH_LONG).show();
                                loadingbar.dismiss();

                            }
                            else {
                                String message=task.getException().toString();
                                Toast.makeText(RegisterActivity.this,"Error"+message,Toast.LENGTH_LONG).show();
                                loadingbar.dismiss();
                            }
                        }
                    });
        }
    }


    private
    void clickbutton() {
        registerbutton=(Button)findViewById(R.id.Register_button);
        registeremail=(EditText)findViewById(R.id.register_email);
        registerpassword=(EditText)findViewById(R.id.register_password);
        registeralredyhaveaccount=(TextView)findViewById(R.id.register_already_have_account);
        loadingbar=new ProgressDialog(this);
    }
    private void SendusertoLogin() {
        Intent register=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(register);
    }
    private
    void sendusertomainactivity() {
        Intent uerintent=new Intent(RegisterActivity.this,MainActivity.class);
        uerintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(uerintent);
        finish();
    }

}

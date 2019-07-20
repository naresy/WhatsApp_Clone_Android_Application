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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public
class LoginActivity extends AppCompatActivity {
private
    FirebaseUser currentuser;
private
    Button loginbutton,phonrloginbutton;
private
    EditText useremil,userpassword;
private
    TextView needacoount,forgetpassword;
//pregress ber
    private
    ProgressDialog loadingloginbar;
//fire
private FirebaseAuth mAuth;
private
DatabaseReference mref;
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mref=FirebaseDatabase.getInstance().getReference();

        clickbutton();
        needacoount.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                SendusertoRegister();

            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                allowuserlogin();

            }
        });
        phonrloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this,phone.class);
                startActivity(intent);
            }
        });
    }



    private
    void allowuserlogin() {
        String Email=useremil.getText().toString();
        String Password=userpassword.getText().toString();
        if(TextUtils.isEmpty(Email))
        {
            Toast.makeText(LoginActivity.this,"please enter the email...",Toast.LENGTH_LONG).show();

        }
        if(TextUtils.isEmpty(Password))
        {
            Toast.makeText(LoginActivity.this,"please enter the password...",Toast.LENGTH_LONG).show();

        }
        else {
            loadingloginbar.setTitle("login user");
            loadingloginbar.setMessage("please wait while login your account");
            loadingloginbar.setCanceledOnTouchOutside(true);
            loadingloginbar.show();
            mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public
                void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {

                        sendusertomainactivity();
                        finish();
                        Toast.makeText(LoginActivity.this,"Login successfull",Toast.LENGTH_LONG).show();
                        loadingloginbar.dismiss();
                    }
                    else {
                        String message =task.getException().toString();
                        Toast.makeText(LoginActivity.this,"Error:"+message,Toast.LENGTH_LONG).show();
                        loadingloginbar.dismiss();

                    }

                }
            });

        }

    }

    private
    void clickbutton() {
        loginbutton=(Button)findViewById(R.id.Login_button);
        phonrloginbutton=(Button)findViewById(R.id.phone_Login_button);
        useremil=(EditText) findViewById(R.id.login_email);
        userpassword=(EditText)findViewById(R.id.login_password);
        needacoount=(TextView)findViewById(R.id.already_have_account);
        forgetpassword=(TextView)findViewById(R.id.login_forget);
loadingloginbar=new ProgressDialog(this);

    }
    private
    void sendusertomainactivity() {
        Intent uerintent=new Intent(LoginActivity.this,MainActivity.class);
        uerintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(uerintent);
        finish();
    }

    private void SendusertoRegister() {
            Intent register=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(register);
    }

}

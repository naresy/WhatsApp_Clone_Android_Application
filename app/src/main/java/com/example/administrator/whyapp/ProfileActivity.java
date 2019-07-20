package com.example.administrator.whyapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public
class ProfileActivity extends AppCompatActivity {
    private String receiverId;
    private FirebaseAuth mauth;
    private CircleImageView userImgeview;
    private TextView username, userstatus;
    private Button sendmessage;
    private DatabaseReference mRef;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        receiverId = getIntent().getExtras().get("Visit_id").toString();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mauth = FirebaseAuth.getInstance();
        Toast.makeText(ProfileActivity.this, "UserId" + receiverId, Toast.LENGTH_LONG).show();
        userImgeview = (CircleImageView) findViewById(R.id.user_profile_image);
        username = (TextView) findViewById(R.id.userprofile_name);
        userstatus = (TextView) findViewById(R.id.usrprofile_status);
        sendmessage = (Button) findViewById(R.id.usersend_message);
        Retriveinformation();
    }

    private
    void Retriveinformation() {
        mRef.child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot dataSnapshot) {
if ((dataSnapshot.exists())&&(dataSnapshot.hasChild("image")))
{
    String Userimage = dataSnapshot.child("image").getValue().toString();
    String Username = dataSnapshot.child("name").getValue().toString();
    String Userstatus = dataSnapshot.child("status").getValue().toString();
   Picasso.get().load(Userimage).into(userImgeview);
    username.setText(Username);
    userstatus.setText(Userstatus);
}else {      String Username = dataSnapshot.child("name").getValue().toString();
             String Userstatus = dataSnapshot.child("status").getValue().toString();

    username.setText(Username);
    userstatus.setText(Userstatus);

}
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}


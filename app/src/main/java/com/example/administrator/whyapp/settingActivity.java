  package com.example.administrator.whyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

 public
class settingActivity extends AppCompatActivity {
     private
     Button usersettingbutton;
     private
     EditText userstatus, username;

     private
     CircleImageView userimageview;
     private String CurrentUserID;
     private
     FirebaseAuth mAuth;private
     DatabaseReference mref;
     private ProgressDialog loadingbar;
     private static int Gallerypick=1;
     private
     StorageReference profileref;



     @Override
     protected
     void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_setting);
         mAuth=FirebaseAuth.getInstance();
         CurrentUserID=mAuth.getCurrentUser().getUid();
         mref=FirebaseDatabase.getInstance().getReference();
         profileref=FirebaseStorage.getInstance().getReference().child("Profile_Image");

         declear();
         username.setVisibility(View.INVISIBLE);
         usersettingbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public
             void onClick(View v) {
                 UpdateSetting();

             }
         });
         Retrievedata();
         userimageview.setOnClickListener(new View.OnClickListener() {
             @Override
             public
             void onClick(View v) {
                Intent Galleryintent=new Intent();
                 Galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                 Galleryintent.setType("image/*");
                 startActivityForResult(Galleryintent,Gallerypick);


             }
         });


     }




     private
     void declear() {
         usersettingbutton = (Button) findViewById(R.id.update_setting_button);
         userstatus = (EditText) findViewById(R.id.set_profile);
         username = (EditText) findViewById(R.id.set_text);
         userimageview = (CircleImageView) findViewById(R.id.profile_image);
         loadingbar=new ProgressDialog(this);

     }

     @Override
     protected
     void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode==Gallerypick&&resultCode==RESULT_OK) {
             Uri getUri=data.getData();
             CropImage.activity()
                     .setGuidelines(CropImageView.Guidelines.ON)
                     .setAspectRatio(2,2)
                     .start(this);
         }

             if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
             CropImage.ActivityResult result = CropImage.getActivityResult(data);
             if (resultCode==RESULT_OK) {
                 loadingbar.setTitle("Set Profile Image");
                 loadingbar.setMessage("Please wait ,your profile image is Updating......... ");
                 loadingbar.setCanceledOnTouchOutside(true);
                 loadingbar.show();
                 Uri Resulturi = result.getUri();
                 StorageReference filepath = profileref.child(CurrentUserID + ".jpg");
                 filepath.putFile(Resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public
                     void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                         if (task.isSuccessful()) {
                             Toast.makeText(settingActivity.this, "Profile image upload successful.....", Toast.LENGTH_LONG).show();
                             String Downloaduri=task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                             mref.child("Users").child(CurrentUserID).child("image").setValue(Downloaduri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public
                                 void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful())
                                     {
                                         Toast.makeText(settingActivity.this,"Image stored successful...",Toast.LENGTH_LONG).show();
                                         loadingbar.dismiss();
                                     }
                                     else {
                                         Toast.makeText(settingActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                         loadingbar.dismiss();
                                     }
                                 }
                             });



                         } else {
                             Toast.makeText(settingActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                             loadingbar.dismiss();
                         }
                     }
                 });
             }

         }
     }

     private
     void UpdateSetting() {

         String Setusername=username.getText().toString();
         String setstatus=userstatus.getText().toString();
         if (TextUtils.isEmpty(Setusername))
         {
             Toast.makeText(settingActivity.this,"Please write your user name....",Toast.LENGTH_LONG).show();

         }
         if (TextUtils.isEmpty(setstatus))
         {
             Toast.makeText(settingActivity.this,"Please write your status....",Toast.LENGTH_LONG).show();

         }
         else {
loadingbar.setTitle("Updating....");
loadingbar.setMessage("Please wait while Updating your information..");
loadingbar.setCanceledOnTouchOutside(true);
loadingbar.show();
             HashMap<String,Object> profilemap=new HashMap<>();
             profilemap.put("uid",CurrentUserID);
             profilemap.put("name",Setusername);
             profilemap.put("status",setstatus);
            mref.child("Users").child(CurrentUserID).updateChildren(profilemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public
                 void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful())
                     {
                         sendusertomaninactivity();
                         Toast.makeText(settingActivity.this,"Profile updated Successful....",Toast.LENGTH_LONG).show();
                         loadingbar.dismiss();

                     }
                     else {
                         String message=task.getException().toString();
                         Toast.makeText(settingActivity.this,"Error"+message,Toast.LENGTH_LONG).show();
                         loadingbar.dismiss();

                     }
                 }
             });


         } }
     private
     void Retrievedata() {
         mref.child("Users").child(CurrentUserID).addValueEventListener(new ValueEventListener() {
             @Override
             public
             void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if ((dataSnapshot.exists())&&(dataSnapshot.hasChild("name")&&(dataSnapshot.hasChild("status")&&(dataSnapshot.hasChild("image")))))
                 {
                     String receiveimage=dataSnapshot.child("image").getValue().toString();
                     String receivename=dataSnapshot.child("name").getValue().toString();
                     String receivestatus=dataSnapshot.child("status").getValue().toString();
                    //String receiveimage=dataSnapshot.child("image").getValue().toString();
                     username.setText(receivename);
                     userstatus.setText(receivestatus);
                     Picasso.get().load(receiveimage).placeholder(R.drawable.profile_image).into(userimageview);

                     Picasso.get().load(new File(receiveimage)).into(userimageview);




                 } else if((dataSnapshot.exists())&&(dataSnapshot.hasChild("name")&&(dataSnapshot.hasChild("status"))))
                 {
                     String receivename=dataSnapshot.child("name").getValue().toString();
                     String receivestatus=dataSnapshot.child("status").getValue().toString();
                     username.setText(receivename);
                     userstatus.setText(receivestatus);
                 }else{
                     Toast.makeText(settingActivity.this,"please set and update your  information....",Toast.LENGTH_LONG).show();
                     username.setVisibility(View.VISIBLE);


                 }


             }

             @Override
             public
             void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
     }



     private
     void sendusertomaninactivity() {
         Intent settingintent = new Intent(settingActivity.this, MainActivity.class);
         settingintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(settingintent);
         finish();
     }

 }


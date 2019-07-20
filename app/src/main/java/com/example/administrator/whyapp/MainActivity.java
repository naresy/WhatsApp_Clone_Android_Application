package com.example.administrator.whyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public
class MainActivity extends AppCompatActivity {
    // declear section
    private
    Toolbar mtoolbar;
    private
    ViewPager myviewager;
    private
    TabLayout mylatlayout;
    private tabacessoradaptor Mytabaccoradaptor;
    //for firebase
    private
    FirebaseUser currentuser;
    private FirebaseAuth mAuth;
    private
    DatabaseReference rootref;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        rootref=FirebaseDatabase.getInstance().getReference();
        mtoolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Whyapps");
        myviewager = (ViewPager) findViewById(R.id.main_view_pager);
        Mytabaccoradaptor = new tabacessoradaptor(getSupportFragmentManager());
       //myviewager.setAdapter(Mytabaccoradaptor);
//       mylatlayout.setupWithViewPager(myviewager);

    }

    @Override
    protected
    void onStart() {
        super.onStart();
        if (currentuser == null) {
            SendusertologinActivity();

        }else {
             verifyexistinguser();

        }
    }

    private
    void verifyexistinguser() {
        String CurrentUserID= mAuth.getCurrentUser().getUid();
        rootref.child("Users").child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_LONG).show();
                } else {
                    SendUsertosettingactivity();

                }
            }


            @Override
            public
            void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private
    void SendusertologinActivity() {
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
    }

    void SendusertoRegisterActivity() {
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
    }

    @Override
    public
    boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public
    boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.main_find_logout) {
            mAuth.signOut();
            SendusertologinActivity();
        }
        if (item.getItemId() == R.id.main_find_setting) {
Intent intentin=new Intent(MainActivity.this,settingActivity.class);
startActivity(intentin);

        }
        if (item.getItemId() == R.id.main_find_create) {

        Requestgroup();

    }
        if (item.getItemId() == R.id.main_find_friends) {
SendUsertoFindactivity();

        }
        return true;
    }

    private
    void Requestgroup() {
        AlertDialog.Builder Builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        Builder.setTitle("Enter group Name");
        final EditText groupid = new EditText(MainActivity.this);
        groupid.setHint("e.g.hami kamina sathi.....");
        Builder.setView(groupid);
        Builder.setPositiveButton("create", new DialogInterface.OnClickListener() {
            @Override
            public
            void onClick(DialogInterface dialog, int i) {
                String groupname = groupid.getText().toString();
                if (TextUtils.isEmpty(groupname)) {
                    Toast.makeText(MainActivity.this, "please enter the group name", Toast.LENGTH_LONG).show();
                } else {
                    Createnewgroup(groupname);
                }
            }
        });
        Builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
            @Override
            public
            void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        Builder.show();
    }

    private
    void Createnewgroup(final String groupname) {
        rootref.child("Groups").child(groupname).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public
            void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, groupname + "group is created successfull", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private
    void SendUsertoLoginactivity() {
        Intent logininten = new Intent(MainActivity.this, LoginActivity.class);
        logininten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logininten);

    }

    private
    void SendUsertosettingactivity() {
        Intent settingintent = new Intent(MainActivity.this, settingActivity.class);
        settingintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingintent);
        finish();

}
    private
    void SendUsertoFindactivity() {
        Intent logininten = new Intent(MainActivity.this, FindfriendActivity.class);
        logininten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logininten);

    }}
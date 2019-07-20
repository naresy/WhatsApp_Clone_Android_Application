package com.example.administrator.whyapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public
class GroupchatActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ImageButton sendimagebutton;
    private EditText usermessageView;
    private ScrollView mScrollview;
    private FirebaseAuth mAuth;
    private TextView displaytextmessage;
    private String currentgroup, currentuserid, currentusernmae, currentdate, currenttime;
    private DatabaseReference userRef, GroupRef, GroupMessagekeyRef;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        currentgroup = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(GroupchatActivity.this, currentgroup, Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        currentuserid = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentgroup);

        Initialize();
        getuserInformation();
        sendimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                savemessageintodatabase();
                usermessageView.setText(null);
                mScrollview.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }
    protected
    void onStart() {
        super.onStart();
        GroupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public
            void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    Displaymessage(dataSnapshot);
                }

            }

            @Override
            public
            void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    Displaymessage(dataSnapshot);
                }

            }

            @Override
            public
            void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public
            void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private
    void Initialize() {
        mtoolbar = (Toolbar) findViewById(R.id.group_chat_layout);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(currentgroup);
        sendimagebutton = (ImageButton) findViewById(R.id.send_message_button);
        usermessageView = (EditText) findViewById(R.id.input_group_message);
        mScrollview = (ScrollView) findViewById(R.id.my_scroll_view);
        displaytextmessage = (TextView) findViewById(R.id.group_chat_text);

    }
    private
    void getuserInformation() {
        userRef.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    currentusernmae=dataSnapshot.child("name").getValue().toString();
                }

            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private
    void savemessageintodatabase() {
        String message=usermessageView.getText().toString();
        String messagekey=GroupRef.push().getKey();
        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(GroupchatActivity.this,"Please write message first",Toast.LENGTH_LONG).show();
        }
        else {
            //for time and date
            Calendar calfordate =Calendar.getInstance();
            SimpleDateFormat currentformat=new SimpleDateFormat("MMM dd,yyyy");
            currentdate=currentformat.format(calfordate.getTime());

            Calendar calfortime =Calendar.getInstance();
            SimpleDateFormat currenttimeformat=new SimpleDateFormat("hh:mm a");
            currenttime=currenttimeformat.format(calfortime.getTime());
            HashMap<String,Object> groupmesagekey=new HashMap<>();
            GroupRef.updateChildren(groupmesagekey);
            GroupMessagekeyRef=GroupRef.child(messagekey);
            HashMap<String,Object>messageinfomap=new HashMap<>();

            messageinfomap.put("name",currentusernmae);

            messageinfomap.put("message",message);

            messageinfomap.put("Date",currentdate);

            messageinfomap.put("Time",currenttime);
            GroupMessagekeyRef.updateChildren(messageinfomap);


        }

    }
    private
    void Displaymessage(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while (iterator.hasNext())
        {
            String chatDate=(String)((DataSnapshot)iterator.next()).getValue();
            String chatTime=(String)((DataSnapshot)iterator.next()).getValue();
            String chatMesage=(String)((DataSnapshot)iterator.next()).getValue();
            String chatName=(String)((DataSnapshot)iterator.next()).getValue();

            displaytextmessage.append(chatName+":\n"+chatMesage+"\n"+chatTime+"    "+chatDate+"\n\n\n");
            mScrollview.fullScroll(ScrollView.FOCUS_UP);


        }
    }
}

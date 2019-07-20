package com.example.administrator.whyapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public
class FindfriendActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView findfriendview;
    DatabaseReference userRef;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findfriend);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        findfriendview = (RecyclerView) findViewById(R.id.findfriendlist);
        findfriendview.setLayoutManager(new LinearLayoutManager(this));
        mToolbar = (Toolbar) findViewById(R.id.find_friend_tool);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friend....");
    }

    @Override
    protected
    void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts>options=new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(userRef,Contacts.class).build();
        FirebaseRecyclerAdapter<Contacts,Findfriendviewholder>adapter=new FirebaseRecyclerAdapter<Contacts, Findfriendviewholder>(options) {
            @Override
            protected
            void onBindViewHolder(@NonNull Findfriendviewholder holder, final int position, @NonNull Contacts model) {
                holder.username.setText(model.getName());
                holder.userstatus.setText(model.getStatus());
                Picasso.get().load(model.getImage()).into(holder.userimageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View v) {
                        String Visit_id=getRef(position).getKey();
                        Intent intent=new Intent(FindfriendActivity.this,ProfileActivity.class);
                        intent.putExtra("Visit_id",Visit_id);
                        startActivity(intent);

                    }
                });


            }

            @NonNull
            @Override
            public
            Findfriendviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_user_leyout,viewGroup,false);
Findfriendviewholder viewholder=new Findfriendviewholder(view);
return viewholder;
            }
        };
        findfriendview.setAdapter(adapter);
        adapter.startListening();
    }
    public static class Findfriendviewholder extends RecyclerView.ViewHolder{
        TextView username,userstatus;
        CircleImageView userimageView;

        public
        Findfriendviewholder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.user_profilename);
            userstatus=itemView.findViewById(R.id.user_status);
            userimageView=itemView.findViewById(R.id.user_profile_image);
        }
    }
}
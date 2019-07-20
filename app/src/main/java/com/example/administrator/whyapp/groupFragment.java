package com.example.administrator.whyapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public
class groupFragment extends Fragment {
    private
    View groupfragmentview;
    private ListView listview;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listt_of_group=new ArrayList<>();
    private DatabaseReference Groupref;



    public
    groupFragment() {
        // Required empty public constructor
    }


    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        groupfragmentview= inflater.inflate(R.layout.fragment_group, container, false);
        Groupref=FirebaseDatabase.getInstance().getReference().child("Groups");
        iniliatize();
        RetriveandDisplay();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public
            void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Currentgroup=parent.getItemAtPosition(position).toString();
                Intent groupchatintent=new Intent(getContext(),GroupchatActivity.class);
                groupchatintent.putExtra("groupName",Currentgroup);
                startActivity(groupchatintent);
            }
        });
        return groupfragmentview;
    }
    private
    void iniliatize() {
        listview= (ListView)groupfragmentview.findViewById(R.id.list_view);
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,listt_of_group);
        listview.setAdapter(arrayAdapter);
    }
    private
    void RetriveandDisplay() {
        Groupref.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    set.add(((DataSnapshot) iterator.next()).getKey());
                    listt_of_group.clear();
                    listt_of_group.addAll(set);
                    arrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }}

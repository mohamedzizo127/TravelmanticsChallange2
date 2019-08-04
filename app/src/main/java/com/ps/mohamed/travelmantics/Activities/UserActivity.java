package com.ps.mohamed.travelmantics.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ps.mohamed.travelmantics.Adapter.TravelAdapter;
import com.ps.mohamed.travelmantics.Model.TravelDeal;
import com.ps.mohamed.travelmantics.R;

import java.util.ArrayList;
import java.util.Collections;

public class UserActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private TravelAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<TravelDeal> mDeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        firebaseInit();
        otherInit();




    }
    private void firebaseInit() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();//get instance of all database or the link
        //get refranse to child and if not exist create new one
        mDatabaseReference = mDatabase.getReference().child("travel");
        //macke sure every thing is Synced up
        mDatabaseReference.keepSynced(true);
    }

    private void otherInit() {
        mDeals = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.UserActRecyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    //the activity start firset befor it become visible
    @Override
    protected void onStart() {
        super.onStart();
        firebaseInit();
        otherInit();
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //this whate we needed
                TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);
                mDeals.add(deal);
                //to reverse bloglist to show recent post first
                Collections.reverse(mDeals);

                mAdapter = new TravelAdapter(UserActivity.this, mDeals);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.insert_menu:
                //macke sure exist user and everything Authriazation
                if (mUser !=null && mAuth != null){
                    startActivity(new Intent(UserActivity.this,AdminActivity.class));
                    finish();
                }

                break;
            case R.id.logout_menu:
                if (mUser !=null && mAuth != null) {
                    mAuth.signOut();
                    startActivity(new Intent(UserActivity.this,AuthenticationActivity .class));
                    finish();
                }
                break;




        }
        return super.onOptionsItemSelected(item);
    }
}

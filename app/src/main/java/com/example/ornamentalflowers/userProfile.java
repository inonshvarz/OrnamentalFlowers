package com.example.ornamentalflowers;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class userProfile extends Fragment {

    DatabaseReference userDetailsReferance;
    UserDetailsClass userDetailsClass;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    private TextView userName;
    private TextView storeSite;


    public userProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // user_profile = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        userDetailsReferance = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userDetailsClass = new UserDetailsClass();


        userDetailsReferance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot, view);
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });

    }

    private void showData(DataSnapshot dataSnapshot, View view) {

        userName = view.findViewById(R.id.userName);
        storeSite = view.findViewById(R.id.storeSite);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference uIdRef = usersRef.child(mAuth.getUid());
        DatabaseReference nameRef = uIdRef.child("userName");
        DatabaseReference siteRef = uIdRef.child("storeSite");

        nameRef.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.getValue(String.class)); }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        siteRef.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                storeSite.setText(dataSnapshot.getValue(String.class)); }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
}

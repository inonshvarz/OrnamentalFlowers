package com.example.ornamentalflowers;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class listInvoice extends Fragment {

    DatabaseReference invoiceReferance;
    FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    View listInvoice;
    ListView listView;
    private Boolean managerFlag;

    public listInvoice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listInvoice = inflater.inflate(R.layout.fragment_list_invoice, container, false);
        mAuth = FirebaseAuth.getInstance();

        return listInvoice;

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference uIdRef = usersRef.child(mAuth.getUid());
        DatabaseReference managerRef = uIdRef.child("manager");

        managerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                managerFlag = (Boolean) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        invoiceReferance = FirebaseDatabase.getInstance().getReference("Invoice");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //invoiceClass = new ArrayList<InvoiceClass>();


        invoiceReferance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot, view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void showData(DataSnapshot dataSnapshot, View view) {

        String uId = mAuth.getUid();
        String uIdKey;
        ArrayList<InvoiceClass> invoiceClassList = new ArrayList<InvoiceClass>();
        invoiceAdapterClass invoiceAdapter = new invoiceAdapterClass(getActivity(), invoiceClassList);

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            InvoiceClass invoiceClass = postSnapshot.getValue(InvoiceClass.class);
            uIdKey = invoiceClass.invoiceUid;
            if (uIdKey.equals(uId) || managerFlag == true) {





/*

                FirebaseStorage storage = FirebaseStorage.getInstance();

                // [START download_create_reference]
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();

                // Create a reference with an initial file path and name
                StorageReference pathReference = storageRef.child("images/stars.jpg");

                // Create a reference to a file from a Google Cloud Storage URI
                StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");

                // Create a reference from an HTTPS URL
                // Note that in the URL, characters are URL escaped!
                StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");
                // [END download_create_reference]

                // [START download_to_memory]
                StorageReference islandRef = storageRef.child("images/island.jpg");

                final long ONE_MEGABYTE = 1024 * 1024;
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
*/








                invoiceClassList.add(invoiceClass);
            }

        }

        listView = (ListView) listInvoice.findViewById(R.id.list_item);
        listView.setAdapter(invoiceAdapter);

    }
}

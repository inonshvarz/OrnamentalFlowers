package com.example.ornamentalflowers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class listInvoice extends Fragment {

    DatabaseReference invoiceReferance;
    //InvoiceClass invoiceClass;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    List<InvoiceClass> invoiceClass;

    public listInvoice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View listInvoice = inflater.inflate(R.layout.fragment_list_invoice, container, false);

        return listInvoice;

/*        // Construct the data source
        ArrayList<InvoiceClass> arrayOfInvoice = new ArrayList<InvoiceClass>();
        // Create the adapter to convert the array to views
        invoiceAdapterClass adapter = new invoiceAdapterClass(getActivity(), arrayOfInvoice);
        // Attach the adapter to a ListView
        ListView listView = (ListView) listInvoice.findViewById(R.id.list_item);
        listView.setAdapter(adapter);

        // Add item to adapter
        InvoiceClass newInvoice = new InvoiceClass();
        newInvoice.setInvoiceId("444444");
        newInvoice.setInvoiceSuper("אלון מורה");
        newInvoice.setInvoiceSum("350");
        newInvoice.setInvoiceDate("25/03/2019");
        adapter.add(newInvoice);*/

        //ListView listFBInvoice = (ListView) listInvoice.findViewById(R.id.listFBInvoice);

        //listFBInvoice.setAdapter(invoiceClasse);

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        invoiceReferance = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        invoiceClass = new ArrayList<InvoiceClass>();


        invoiceReferance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot, view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    private void showData(DataSnapshot dataSnapshot, View view) {

        /*userName = view.findViewById(R.id.userName);
        storeSite = view.findViewById(R.id.storeSite);*/

        DatabaseReference invoiceRef = FirebaseDatabase.getInstance().getReference("Invoice");
/*        DatabaseReference uIdRef = invoiceRef.child(mAuth.getUid());
        DatabaseReference nameRef = uIdRef.child("userName");
        DatabaseReference siteRef = uIdRef.child("storeSite");*/

        invoiceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<Map<String, List<InvoiceClass>>> genericTypeIndicator = new GenericTypeIndicator<Map<String, List<InvoiceClass>>>() {};
                Map<String, List<InvoiceClass>> hashMap = dataSnapshot.getValue(genericTypeIndicator);

                for (Map.Entry<String,List<InvoiceClass>> entry : hashMap.entrySet()) {
                    List<InvoiceClass> educations = entry.getValue();
                    for (InvoiceClass education: educations){
                        //Log.i(TAG, education.Degree);
                    }
                }



/*                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    <invoiceClass> post = postSnapshot.getValue(<invoiceClass>.class);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

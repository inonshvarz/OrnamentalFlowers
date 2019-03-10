package com.example.ornamentalflowers;


import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class anInvoice extends Fragment {

    InvoiceClass InvoiceClass;
    DatabaseReference anInvoiceReferance;
    private FirebaseAuth fbAuth;

    public anInvoice() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        InvoiceClass = new InvoiceClass();
        fbAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View anInvoiceView = inflater.inflate(R.layout.fragment_an_invoice, container, false);

        final EditText invoiceId = anInvoiceView.findViewById(R.id.inputNumInvoice);
        final EditText invoiceSum = anInvoiceView.findViewById(R.id.inputSumInvoice);
        final EditText invoiceDate = anInvoiceView.findViewById(R.id.inputDateInvoice);

        final Button SaveInvoice = (Button) anInvoiceView.findViewById(R.id.SaveInvoice);
        SaveInvoice.setOnClickListener(new View.OnClickListener()
        {
            //@Override
            public void onClick (View anInvoiceView){
                //do what you want to do when button is clicked
                SaveInvoice(invoiceId.getText().toString(),
                            invoiceSum.getText().toString(),
                            invoiceDate.getText().toString());
            }
            });
        return anInvoiceView;

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



/*        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    animateRevealColorFromCoordinates((ViewGroup) view, right / 2, bottom);
                }
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Animator animateRevealColorFromCoordinates(ViewGroup viewRoot, int x, int y) {
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
        anim.setDuration(1000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
        return anim;
    }



    public void SaveInvoice(String invoiceId, String invoiceSum, String invoiceDate){


        //final EditText invoiceId = view.findViewById(R.id.inputNumInvoice);
        //final EditText invoiceSum = view.findViewById(R.id.inputSumInvoice);
        //final EditText invoiceDate = view.findViewById(R.id.inputDateInvoice);

        InvoiceClass.setInvoiceId(invoiceId);
        InvoiceClass.setInvoiceSum(invoiceSum);
        InvoiceClass.setInvoiceDate(invoiceDate);
        InvoiceClass.setInvoiceUid(fbAuth.getUid());
        //InvoiceClass.setInvoiceSuper(idUser);


        anInvoiceReferance = FirebaseDatabase.getInstance().getReference();
        anInvoiceReferance.child("Invoice").child(invoiceId).setValue(InvoiceClass);

        notifyUser("Successfully saved invoice");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new userProfile()).commit();
    }

    private void notifyUser(String message){
        Toast.makeText(getActivity(), message,
                Toast.LENGTH_SHORT).show();
    }

}

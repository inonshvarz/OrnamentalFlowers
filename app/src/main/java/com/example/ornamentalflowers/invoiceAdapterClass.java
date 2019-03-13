package com.example.ornamentalflowers;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;

class invoiceAdapterClass extends ArrayAdapter<InvoiceClass> {

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    String imageURL;

    //to reference the Activity
    private final Activity context;
    //to store the list of invoice class
    private final ArrayList<InvoiceClass> invoiceClassList;

    public invoiceAdapterClass(Activity context, ArrayList<InvoiceClass> invoiceClassList) {
        super(context, R.layout.invoice_row, invoiceClassList);
        this.context=context;
        this.invoiceClassList = invoiceClassList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater invoiceflater = context.getLayoutInflater();
        View invoiceView = invoiceflater.inflate(R.layout.invoice_row, parent, false);

        InvoiceClass invoiceClass = getItem(position);

        TextView inputNumInvoice  = (TextView) invoiceView.findViewById(R.id.inputNumInvoice);
        TextView inputSumInvoice  = (TextView) invoiceView.findViewById(R.id.inputSumInvoice);
        TextView inputDateInvoice = (TextView) invoiceView.findViewById(R.id.inputDateInvoice);
        final ImageView invoiceImage    = (ImageView) invoiceView.findViewById(R.id.invoiceImage);

        inputNumInvoice.setText(invoiceClass.invoiceId);
        inputSumInvoice.setText(invoiceClass.invoiceSum);
        inputDateInvoice.setText(invoiceClass.invoiceDate);
        //invoiceImage.setImageURI(Uri.parse("images/" + invoiceClass.invoiceUid + ".jpg"));
        imageURL = "images/" + invoiceClass.invoiceId + ".jpg";
        storageRef.child(imageURL).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Picasso.get().load(task.getResult()).into(invoiceImage);
                }
            }
        });

        return invoiceView;

    }
}

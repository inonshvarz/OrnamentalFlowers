package com.example.ornamentalflowers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class invoiceAdapterClass extends ArrayAdapter<InvoiceClass> {

    //public invoiceAdapterClass(@androidx.annotation.NonNull Context context, int resource) {
    invoiceAdapterClass(Context context, InvoiceClass[] invoices) {
        super(context, R.layout.recycler_view_item, invoices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater invoiceflater = LayoutInflater.from(getContext());
        View invoiceView = invoiceflater.inflate(R.layout.invoice_row, parent, false);

        InvoiceClass invoiceClass = getItem(position);

        TextView inputNumInvoice  = (TextView) invoiceView.findViewById(R.id.inputNumInvoice);
        TextView inputSumInvoice  = (TextView) invoiceView.findViewById(R.id.inputSumInvoice);
        TextView inputDateInvoice = (TextView) invoiceView.findViewById(R.id.inputDateInvoice);
        ImageView invoiceImage    = (ImageView)invoiceView.findViewById(R.id.invoiceImage);

        inputNumInvoice.setText(invoiceClass.invoiceId);
        inputSumInvoice.setText(invoiceClass.invoiceSum);
        inputDateInvoice.setText(invoiceClass.invoiceDate);
        invoiceImage.setImageResource(R.drawable.flower2);

        return invoiceView;

/*        InvoiceClass invoiceClass = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_invoice, parent, false);
        }

        TextView inputNumInvoice = (TextView) convertView.findViewById(R.id.inputNumInvoice);
        TextView inputSumInvoice = (TextView) convertView.findViewById(R.id.inputSumInvoice);
        TextView inputDateInvoice = (TextView) convertView.findViewById(R.id.inputDateInvoice);

        inputNumInvoice.setText(invoiceClass.invoiceId);
        inputSumInvoice.setText(invoiceClass.invoiceSum);
        inputDateInvoice.setText(invoiceClass.invoiceDate);
        return convertView;*/
    }
}

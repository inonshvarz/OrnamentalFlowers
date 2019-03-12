package com.example.ornamentalflowers;


import android.Manifest;
import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class anInvoice extends Fragment {

    InvoiceClass InvoiceClass;
    DatabaseReference anInvoiceReferance;
    private FirebaseAuth fbAuth;
    private ImageView mImageView;
    private Bitmap photo;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

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

        final Button btnImage = (Button) anInvoiceView.findViewById(R.id.BtnImage);
        mImageView = (ImageView) anInvoiceView.findViewById(R.id.mImageView);

        btnImage.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(cameraIntent,111);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        return anInvoiceView;

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        InvoiceClass.setInvoiceId(invoiceId);
        InvoiceClass.setInvoiceSum(invoiceSum);
        InvoiceClass.setInvoiceDate(invoiceDate);
        InvoiceClass.setInvoiceUid(fbAuth.getUid());
        //InvoiceClass.setInvoiceSuper(idUser);


        anInvoiceReferance = FirebaseDatabase.getInstance().getReference();
        anInvoiceReferance.child("Invoice").child(invoiceId).setValue(InvoiceClass);








        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START upload_create_reference]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to "mountains.jpg"
        StorageReference imgInvoiceRef = storageRef.child(InvoiceClass.invoiceId);

        // Create a reference to 'images/mountains.jpg'
        StorageReference foldInvoiceRef = storageRef.child("images/mountains.jpg");

        // While the file names are the same, the references point to different files
        imgInvoiceRef.getName().equals(foldInvoiceRef.getName());    // true
        imgInvoiceRef.getPath().equals(foldInvoiceRef.getPath());    // false


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgInvoiceRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });










        notifyUser("Successfully saved invoice");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new userProfile()).commit();
    }

    private void notifyUser(String message){
        Toast.makeText(getActivity(), message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(photo);
        }
    }
}

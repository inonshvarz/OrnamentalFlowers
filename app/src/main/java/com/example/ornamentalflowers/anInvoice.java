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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    //********************************************
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    Uri imageUri;
    int REQUEST_CODE_LOAD_IMAGE = 10;
    //********************************************

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

        //********************************************************************
        final Button btnSave = anInvoiceView.findViewById(R.id.SaveInvoice);
        //********************************************************************

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
            @Override
            public void onClick(View v) {
                //registerBool = true;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_LOAD_IMAGE);
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


        anInvoiceReferance = FirebaseDatabase.getInstance().getReference();
        anInvoiceReferance.child("Invoice").child(invoiceId).setValue(InvoiceClass);

        //**********************************
        final StorageReference riversRef = storageRef.child("images/" + invoiceId + ".jpg");
        riversRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                notifyUser(getString(R.string.secces_save));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new userProfile()).commit();
            }
        });


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

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                Bitmap photo = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    //photo = getResizedBitmap(photo, 400);
                    //imageButton.setImageBitmap(photo);
                    mImageView.setImageBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


/*        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(photo);
        }*/
    }
}

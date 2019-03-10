package com.example.ornamentalflowers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.app.PendingIntent.getActivity;
//import com.google.firebase.quickstart.auth.R;



public class AutoActivity extends AppCompatActivity{

    private TextView userText;
    private TextView statusText;
    private EditText emailText;
    private EditText passwordText;

    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener authListener;

    DatabaseReference userDetailsReferance;
    UserDetailsClass userDetailsClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auto);

        userDetailsClass = new UserDetailsClass();

        userText     = (TextView) findViewById(R.id.userText);
        statusText   = (TextView) findViewById(R.id.statusText);
        emailText    = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        userText.setText("");
        statusText.setText("Signed Out");

        fbAuth = FirebaseAuth.getInstance();

        if (FirebaseAuth.getInstance() == null)
        {
            goToHomePage();
        }

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {
                    //goToHomePage();
                    /*userText.setText(user.getEmail());
                    statusText.setText("Signed In");*/

                }
                else
                {
                    userText.setText("");
                    statusText.setText("Signed Out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            fbAuth.removeAuthStateListener(authListener);
        }
    }

    public void createAccount(View view) {
        final String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.length() == 0) {
            emailText.setError("Enter an email address");
            return;
        }

        if (password.length() < 6) {
            passwordText.setError("Password must be at least 6 characters");
            return;
        }

        fbAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this,
        new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {

                if (!task.isSuccessful())
                {
                    notifyUser("Account creation failed");
                }
                else
                {
                    View view = (LayoutInflater.from(AutoActivity.this)).inflate(R.layout.activity_user_details, null);

                    AlertDialog.Builder alertBulider = new AlertDialog.Builder(AutoActivity.this);
                    alertBulider.setView(view);

                    final EditText userName   = view.findViewById(R.id.userName);
                    final EditText phoneNumber = view.findViewById(R.id.phoneNumber);
                    final EditText storeSite  = view.findViewById(R.id.storeSite);
                    final EditText idUser     = view.findViewById(R.id.idUser);

                    alertBulider.setCancelable(true).
                            setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    insertUserDetailsFB(userName.getText().toString(),
                                            phoneNumber.getText().toString(),
                                            storeSite.getText().toString(),
                                            emailText.getText().toString(),
                                            idUser.getText().toString(),
                                            fbAuth.getUid().toString());

                                    dialog.cancel();

                                }
                            });

                    Dialog dialog = alertBulider.show();
                    dialog.show();


                    //startActivity(new Intent(AutoActivity.this, UserDetails.class));
                }
            }
        });

    }

    public void signIn(View view) {

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.length() == 0) {
            emailText.setError("Enter an email address");
            return;
        }

        if (password.length() < 6) {
            passwordText.setError("Password must be at least 6 characters");
            return;
        }

        fbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull
                                                           Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    notifyUser("Authentication failed");
                                }
                                else
                                {
                                    goToHomePage();
                                }
                            }
                        });

    }

    public void resetPassword(View view) {

        String email = emailText.getText().toString();

        if (email.length() == 0) {
            emailText.setError("Enter an email address");
            return;
        }

        fbAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            notifyUser("Reset email sent");
                        }
                    }
                });
    }

    public void signOut(View view) {
        fbAuth.signOut();
    }

    public void insertUserDetailsFB(String userName,
                                    String phoneNumber,
                                    String storeSite,
                                    String email,
                                    String idUser,
                                    String uId){
        userDetailsClass.setUserName(userName);
        userDetailsClass.setPhoneNumber(phoneNumber);
        userDetailsClass.setStoreSite(storeSite);
        userDetailsClass.setMailAddress(email);
        userDetailsClass.setIdUser(idUser);

        userDetailsReferance = FirebaseDatabase.getInstance().getReference();
        userDetailsReferance.child("Users").child(uId).setValue(userDetailsClass);

        goToHomePage();

    }

    private void notifyUser(String message){
        Toast.makeText(AutoActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }

    private void goToHomePage()
    {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}

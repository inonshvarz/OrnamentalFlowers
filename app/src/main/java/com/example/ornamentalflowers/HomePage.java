package com.example.ornamentalflowers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import client.yalantis.com.foldingtabbar.FoldingTabBar;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            goToAutoPage();
        }
        else {
            FoldingTabBar tabBar = (FoldingTabBar) findViewById(R.id.folding_tab_bar);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new userProfile()).commit();

            tabBar.setOnFoldingItemClickListener(new FoldingTabBar.OnFoldingItemSelectedListener() {
                @Override
                public boolean onFoldingItemSelected(@NotNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.signOut:
                            FirebaseAuth.getInstance().signOut();
                            goToAutoPage();
                            break;

                        case R.id.ftb_anInvoice:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new anInvoice()).commit();
                            //goToAnInvoice();
                            break;

                        case R.id.ftb_listInvoice:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new listInvoice()).commit();
                            break;

                        //goToListInvoice();

                        case R.id.ftb_ShowSumInvoices:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new showSumInvoice()).commit();
                            break;

                        //goToListInvoice();
                    }
                    return false;
                }
            });


            tabBar.setOnMainButtonClickListener(new FoldingTabBar.OnMainButtonClickedListener() {
                @Override
                public void onMainButtonClicked() {

                }
            });
        }
    }

/*    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_an_invoice, container, false);
    }*/

    private void goToAutoPage() {
        Intent intent = new Intent(this, AutoActivity.class);
        startActivity(intent);
    }

}
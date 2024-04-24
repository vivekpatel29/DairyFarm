package com.example.dairyfarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnHome,btnProduct,btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnHome=findViewById(R.id.btnHome);
        btnProduct=findViewById(R.id.btnProduct);
        btnAbout= findViewById(R.id.btnAbout);
        btnHome.setOnClickListener(this);
        btnProduct.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnHome:
                HomeFragment homeFragment= new HomeFragment();
                FragmentManager fragmentManager= getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.homelayout,homeFragment).commit();
                break;

            case R.id.btnProduct:
                CategoryFragment categoryFragment= new CategoryFragment();
                FragmentManager fragmentManager1= getSupportFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.homelayout,categoryFragment).commit();
                break;
            case R.id.btnAbout:
                AboutFragment aboutFragment= new AboutFragment();
                FragmentManager fragmentManager2= getSupportFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.homelayout,aboutFragment).commit();
                break;
        }
    }
}
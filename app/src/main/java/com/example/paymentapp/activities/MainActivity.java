package com.example.paymentapp.activities;


import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.paymentapp.R;
import com.example.paymentapp.account.Account;
import com.example.paymentapp.account.InternalStorage;
import com.example.paymentapp.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import static android.provider.Contacts.SettingsColumns.KEY;

public class MainActivity extends AppCompatActivity {
    private Account account;
    private String data;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            account = (Account) InternalStorage.readObject(this, KEY);
            data = (String) InternalStorage.readObject(this, "data");

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        loadFragment(new HomeFragment(account));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_2);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.page_1:
                    fragment = new NotificationsFragment(data);
                    break;

                case R.id.page_2:
                    fragment = new HomeFragment(account);
                    break;

                case R.id.page_3:
                    fragment = new QRScannerFragment(account);
                    break;
                case R.id.page_4:
                    fragment = new SettingsFragment(account);
                    break;
            }

            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
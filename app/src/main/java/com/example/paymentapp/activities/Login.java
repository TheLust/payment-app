package com.example.paymentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.paymentapp.R;
import com.example.paymentapp.account.Account;
import com.example.paymentapp.account.InternalStorage;
import com.google.android.material.textfield.TextInputEditText;

import java.io.*;
import java.util.Objects;

import static android.provider.Contacts.SettingsColumns.KEY;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextInputEditText uuidInput = findViewById(R.id.uuidInput);
        TextInputEditText passwordInput = findViewById(R.id.etPassword);
        Button loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(view -> {

            if (!Objects.requireNonNull(uuidInput.getText()).toString().equals("") &&
                !Objects.requireNonNull(passwordInput.getText()).toString().equals("") ) {

                Account account = new Account(uuidInput.getText().toString(), passwordInput.getText().toString(), 1500F);

                try {
                    InternalStorage.writeObject(this, KEY, account);
                    InternalStorage.writeObject(this, "QRToken", "null");
                    InternalStorage.writeObject(this, "data", "null");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Enter Credentials!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

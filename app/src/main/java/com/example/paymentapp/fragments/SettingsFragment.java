package com.example.paymentapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.paymentapp.R;
import com.example.paymentapp.account.Account;
import com.example.paymentapp.account.InternalStorage;
import com.example.paymentapp.activities.Login;

import java.io.IOException;
import java.util.Objects;

import static android.provider.Contacts.SettingsColumns.KEY;

public class SettingsFragment extends Fragment {

    private final Account account;

    public SettingsFragment(Account account) {
        this.account = account;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_layout, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView uuid = view.findViewById(R.id.uuid_settings);
        Button logoutButton = view.findViewById(R.id.logoutButton);
        uuid.setText("UUID: " + account.getUuid());

        logoutButton.setOnClickListener(v -> {
            try {
                InternalStorage.writeObject(Objects.requireNonNull(getContext()), KEY, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            startActivity(new Intent(getContext(), Login.class));
        });
    }
}

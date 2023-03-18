package com.example.paymentapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.paymentapp.account.InternalStorage;
import com.example.paymentapp.activities.ConfirmQRDialog;
import com.example.paymentapp.R;
import com.example.paymentapp.account.Account;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Objects;

import static android.provider.Contacts.SettingsColumns.KEY;

public class HomeFragment extends Fragment {
    private Account account;

    public HomeFragment(Account account) {
        this.account = account;
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container = (ViewGroup) inflater.inflate(R.layout.main_app, null);

        try {
            account = (Account) InternalStorage.readObject(Objects.requireNonNull(getContext()), KEY);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        TextView uuid = container.findViewById(R.id.uuid);
        TextView amountAcc = container.findViewById(R.id.amountAcc);

        uuid.setText("Account: " + account.getUuid());
        amountAcc.setText("Amount: " + account.getAmount().toString() + " MDL");

        TextInputEditText data = container.findViewById(R.id.amountInput);
        Button button = container.findViewById(R.id.materialButton);


        button.setOnClickListener(v -> {

            if (!Objects.requireNonNull(data.getText()).toString().isEmpty()) {

                if (data.toString().endsWith(".")){
                    Toast.makeText(getContext(), "Wrong Input", Toast.LENGTH_SHORT).show();
                    data.setText(data.getText() + "0");
                }

                if (Float.parseFloat(data.getText().toString()) <= account.getAmount()) {

                    ConfirmQRDialog confirmQRDialog = new ConfirmQRDialog(data.getText().toString(), account);
                    confirmQRDialog.setOnDismissListener(dialogInterface -> {
                        try {
                            account = (Account) InternalStorage.readObject(Objects.requireNonNull(getContext()), KEY);
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        amountAcc.setText("Amount: " + account.getAmount().toString() + " MDL");
                    });
                    confirmQRDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Confirm");

                } else {
                    Toast.makeText(getContext(), "You have only: " + account.getAmount() + " MDL", Toast.LENGTH_SHORT).show();
                }
                data.setText("");
            } else {
                Toast.makeText(getContext(), "Enter some amount to generate QR Code", Toast.LENGTH_SHORT).show();
            }


        });

        data.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().startsWith(".")){
                    Toast.makeText(getContext(), "Wrong Input", Toast.LENGTH_SHORT).show();
                    data.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        return container;
    }
}

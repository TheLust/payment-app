package com.example.paymentapp.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.paymentapp.R;
import com.example.paymentapp.account.Account;
import com.example.paymentapp.account.InternalStorage;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Objects;

import static android.provider.Contacts.SettingsColumns.KEY;

public class ConfirmQRDialog extends DialogFragment {


    private TextInputEditText password;
    private final String data;
    private Account account;

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public ConfirmQRDialog(String data, Account account) {
        this.data = data;
        this.account = account;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirm_display_qr, container);
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        password = view.findViewById(R.id.confirmPassword);
        Button confirmButton = view.findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(v -> {

            if (Objects.requireNonNull(password.getText()).toString().equals(account.getPassword())) {
                PopupWindowQR popupWindowQR = new PopupWindowQR(data);
                popupWindowQR.setOnDismissListener(this);
                popupWindowQR.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Fragment");

                try {
                    account = (Account) InternalStorage.readObject(Objects.requireNonNull(getContext()), KEY);
                    account.setAmount(account.getAmount() - Float.parseFloat(data));

                    InternalStorage.writeObject(Objects.requireNonNull(getContext()), KEY, account);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null)
            onDismissListener.onDismiss(dialog);
    }
}

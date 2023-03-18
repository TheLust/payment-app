package com.example.paymentapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.paymentapp.R;
public class NotificationsFragment extends Fragment {
    private final String data;

    public NotificationsFragment(String data) {
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notification_layout, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView idTransaction = view.findViewById(R.id.id_transaction);
        TextView title = view.findViewById(R.id.title);
        TextView receiver = view.findViewById(R.id.receiver);

        String[] qrData = data.split("/");

        if (qrData.length == 3) {
            idTransaction.setText("Transaction ID: " + qrData[0]);
            title.setText("Received: " + qrData[1] + " MDL");
            receiver.setText("From: " + qrData[2]);
        } else {
            idTransaction.setText("Transaction ID");
            title.setText("Received");
            receiver.setText("From");
        }

    }
}

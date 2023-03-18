package com.example.paymentapp.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.ImageView;
import android.widget.Toast;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.paymentapp.R;
import com.example.paymentapp.account.Account;
import com.example.paymentapp.account.InternalStorage;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static android.provider.Contacts.SettingsColumns.KEY;

public class PopupWindowQR extends DialogFragment {

    private final String data;

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public PopupWindowQR(String data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.qr_generator_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView qr = view.findViewById(R.id.generatedQRCode);

        try {
           Account account = (Account) InternalStorage.readObject(Objects.requireNonNull(getContext()), KEY);
           qr.setImageBitmap(generateQRCode(createDataQR(account, Float.parseFloat(data))));

        } catch (IOException | ClassNotFoundException | WriterException e) {
            throw new RuntimeException(e);
        }
    }

    private Bitmap generateQRCode(String data) throws WriterException {

        if (TextUtils.isEmpty(data)) {
            Toast.makeText(getContext(), "Enter some text to generate QR Code", Toast.LENGTH_SHORT).show();
        } else {
            int dimen = 300;
            dimen = dimen * 3 / 4;
            QRGEncoder encoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, dimen);
            return encoder.encodeAsBitmap();
        }
        return null;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null)
            onDismissListener.onDismiss(dialog);
    }

    private String createDataQR(Account account, Float amount) {
        long token = new Date().getDate();
        return new StringBuilder()
                .append(token).append("/")
                .append(amount).append("/")
                .append(account.getUuid())
                .toString();
    }
}

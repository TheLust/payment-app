package com.example.paymentapp.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.paymentapp.account.Account;
import com.example.paymentapp.account.InternalStorage;
import com.example.paymentapp.R;
import com.example.paymentapp.activities.MainActivity;
import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

import java.io.IOException;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;
import static android.provider.Contacts.SettingsColumns.KEY;

public class QRScannerFragment extends Fragment {

    private ScannerLiveView camera;
    private Account account;

    public QRScannerFragment(Account account) {
        this.account = account;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.qr_scanner, null);

        if (checkPermission()) {
            Toast.makeText(getContext(), "Permission Granted..", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        camera = container.findViewById(R.id.camview);
        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                Toast.makeText(getContext(), "Scanner Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                Toast.makeText(getContext(), "Scanner Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                Toast.makeText(getContext(), "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {

                String[] qrData = data.split("/");

                if (qrData.length == 3) {
                    String token = qrData[0];
                    try {
                        if (!token.equals(InternalStorage.readObject(Objects.requireNonNull(getContext()), "QRToken"))) {
                            InternalStorage.writeObject(getContext(), "QRToken", token);
                            String amount = qrData[1];

                            account = (Account) InternalStorage.readObject(Objects.requireNonNull(getContext()), KEY);
                            account.setAmount(account.getAmount() + Float.parseFloat(amount));
                            InternalStorage.writeObject(Objects.requireNonNull(getContext()), KEY, account);
                            InternalStorage.writeObject(getContext(), "data", data);

                            startActivity(new Intent(getContext(), MainActivity.class));

                        } else {
                            Toast.makeText(getContext(), "QR Code has expired!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(getContext(), "Invalid QR Code!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return container;
    }

    @Override
    public void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        decoder.setScanAreaPercent(0.8);
        camera.setDecoder(decoder);
        camera.startScanner();
    }

    @Override
    public void onPause() {
        camera.stopScanner();
        super.onPause();
    }
    private boolean checkPermission() {
        int camera_permission = ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(getContext(), VIBRATE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{CAMERA, VIBRATE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0) {
            boolean cameraaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (cameraaccepted && vibrateaccepted) {
                Toast.makeText(getContext(), "Permission granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission Denied \n You cannot use app without providing permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

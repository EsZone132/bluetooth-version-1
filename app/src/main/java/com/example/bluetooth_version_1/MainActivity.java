package com.example.bluetooth_version_1;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Bluetooth";
    private static final int MY_PERMISSIONS_REQUEST_BLUETOOTH = 123;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, MY_PERMISSIONS_REQUEST_BLUETOOTH);
            return;
        }

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice hc05 = btAdapter.getRemoteDevice("00:18:91:D7:25:B8");

        try {
            BluetoothSocket btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
            btSocket.connect();
            Log.d(TAG, "Connected to Bluetooth device.");

            BluetoothThread btThread = new BluetoothThread(btSocket, textView);
            btThread.start();
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Bluetooth device: " + e.getMessage());
        }
    }
}





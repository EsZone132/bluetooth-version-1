package com.example.bluetooth_version_1;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;

class BluetoothThread extends Thread{
    private final InputStream inputStream;
    private static final String TAG = "Bluetooth";
    private final TextView textView;
    public BluetoothThread(BluetoothSocket socket, TextView textView) {

        this.textView = textView;

        InputStream tempInput = null;
        try {
            tempInput = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error obtaining InputStream: " + e.getMessage());
        }
        inputStream = tempInput;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;
        try {
            while (true) {
                bytes = inputStream.read(buffer);
                if (bytes > 0) {
                    final String receivedData = new String(buffer, 0, bytes);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("Received Data: " + receivedData);
                        }
                    });
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading data from InputStream: " + e.getMessage());
        }
    }
}

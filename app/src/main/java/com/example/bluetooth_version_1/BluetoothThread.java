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
        int bytes = 0;
        int numberOfReadings = 0;

        while(true){ // Change value according to the number of sample points desired when clicking SCAN button
            try{
                buffer[bytes] = (byte) inputStream.read();
                String receivedSentence;
                if(buffer[bytes] == '\n'){
                    receivedSentence = new String(buffer, 0, bytes);
                    Log.d(TAG, receivedSentence);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(receivedSentence);
                        }
                    });
                    bytes = 0;
                    numberOfReadings++;
                }
                else{
                    bytes++;
                }
            } catch(IOException e){
                Log.d(TAG, "Input stream disconnected", e);
                break;
            }
        }
    }
}

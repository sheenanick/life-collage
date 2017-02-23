package com.doandstevensen.lifecollage.ui.pass;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by Sheena on 2/22/17.
 */

public class BluetoothService {
    private static final UUID MY_UUID = UUID.fromString("fb2e4b79-731e-4cf8-98e9-8123a21324d1");
    private static final String TAG = "BLUETOOTH_SERVICE";

    private Context mContext;
    private static BluetoothAdapter mBluetoothAdapter;
    private static ConnectedThread mConnectedThread;
    private static BluetoothServiceListener mListener;

    public BluetoothService(Context context, BluetoothServiceListener listener) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mListener = listener;
    }

    public static class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Life Collage", MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
                mListener.makeToast("Error occurred");
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;

            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    mListener.makeToast("Error occurred");
                    break;
                }

                if (socket != null) {
                    manageMyConnectedSocket(socket);

                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Socket's close() method failed", e);
                        break;
                    }

                    break;
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    public static class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
                mListener.makeToast("Error occurred");
            }
            mmSocket = tmp;
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }
            manageMyConnectedSocket(mmSocket);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    private static void manageMyConnectedSocket(BluetoothSocket socket) {
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.run();
    }

    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer;

        public ConnectedThread(BluetoothSocket socket) {
            mListener.makeToast("Connected");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
                mListener.makeToast("Error occurred");
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
                mListener.makeToast("Error occurred");
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes;

            while (true) {
                try {
                    numBytes = mmInStream.read(mmBuffer);
                    String incomingMessage = new String(mmBuffer, 0, numBytes);

                    mListener.incomingMessage(incomingMessage);
                    Log.d(TAG, "Incoming message: " + incomingMessage);

                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                String text = new String(bytes, Charset.defaultCharset());
                Log.d(TAG, "write: Writing to outputstream: " + text);

            } catch (IOException e) {
                mListener.makeToast("Error occurred when sending data");
                Log.e(TAG, "Error occurred when sending data", e);
            }

        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    public void write(byte[] out) {
        mConnectedThread.write(out);
    }

    interface BluetoothServiceListener {
        void incomingMessage(String message);
        void makeToast(String message);
        void showProgressDialog();
        void hideProgressDialog();
    }

}

package com.doandstevensen.lifecollage.util;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.doandstevensen.lifecollage.data.model.CollageListResponse;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by CGrahamS on 2/21/17.
 */

public class BluetoothConnectionService {
    private static final String TAG = BluetoothConnectionService.class.getSimpleName();

    private static final String appName = "Life Collage";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    DataManager mDataManager;
    LifeCollageApiService mApiService;
    Subscription mSubscription;

    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mDataManager = new DataManager(context);
        String token = mDataManager.getUserToken().getAccessToken();
        mApiService = LifeCollageApiService.ServiceCreator.newPrivateService(token);
        mDataManager.setApiService(mApiService);
        start();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Setting up server using: " + MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.e(TAG, "AcceptThread: IOException" + e.getMessage());
            }

            mServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "run: AcceptThread Running");

            BluetoothSocket socket = null;

            try {
                Log.d(TAG, "Accept Thread run: RFCOM server socket start.....");
                socket = mServerSocket.accept();
                Log.d(TAG, "Accept Thread run: RFCOM server socket accepted connection");
            } catch (IOException e) {
                Log.e(TAG, "Accept Thread run: IOException" + e.getMessage());
            } catch (NullPointerException e) {
                Log.e(TAG, "run: Null pointer exception: " + e.getMessage());
            }

            if(socket != null) {
                connected(socket, mDevice);
            }

            Log.i(TAG, "END mAcceptThread");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread");
            try {
                mServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed" + e.getMessage());
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started");

            mDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "run: mConnectThread Running");

            try {
                tmp = mDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread run: Could not create InsecureRFCommSocket" + e.getMessage());
            }

            mSocket = tmp;

            mBluetoothAdapter.cancelDiscovery();

            try {
                mSocket.connect();
                Log.d(TAG, "run: ConnectThread connected");
            } catch (IOException e) {
                try {
                    mSocket.close();
                    Log.d(TAG, "run: Closed Socket");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectedThread run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID" + MY_UUID_INSECURE);
            }
            connected(mSocket, mDevice);
        }

         public void cancel() {
             try {
                 Log.d(TAG, "cancel: Closing Client Socket" );
                 mSocket.close();
             } catch (IOException e) {
                 Log.e(TAG, "cancel: close() of mSocket in ConnectThread failed" + e.getMessage());
             }
         }
    }

    public synchronized void start() {
        Log.d(TAG, "start: ");

        if(mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startClient: Started");

        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth", "Please Wait...", true);
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mSocket;
        private final InputStream mInputStream;
        private final OutputStream mOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting");

            mSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                mProgressDialog.dismiss();
            } catch (NullPointerException e) {
                Log.i(TAG, "ConnectedThread: no progress dialog to dismiss: " + e.getMessage());
            }

            try {
                tmpIn = mSocket.getInputStream();
                tmpOut = mSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "ConnectedThread: Unable to to establish input and/or output streams: " + e.getMessage());
            }

            mInputStream = tmpIn;
            mOutStream = tmpOut;
        }


        //FIXME This is where we should change what we read
        public void run() {
            int id;

            while (true) {
                try {
                    id = mInputStream.read();

                    mSubscription = mDataManager.updateCollageOwner(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnUnsubscribe(new Action0() {
                                @Override
                                public void call() {
                                    mSubscription = null;
                                }
                            })
                            .subscribe(new Subscriber<CollageResponse>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(CollageResponse collage) {
                                    //notify data set change
                                }
                            });
                    Log.d(TAG, "InputStream: " + id);
                } catch (IOException e) {
                    Log.e(TAG, "run: Error reading input stream: " + e.getMessage());
                    break;
                }
            }
        }

        //FIXME This is where we change what we write
        public void write(int id) {
            Log.d(TAG, "write: Writing to outputstream: " + id);
            try {
                mOutStream.write(id);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing bytes to output stream" + e.getMessage());
            }
        }

        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: unable to close connection" + e.getMessage());
            }
        }
    }

    public void connected(BluetoothSocket mSocket, BluetoothDevice device) {
        Log.d(TAG, "connected: Starting");

        mConnectedThread = new ConnectedThread(mSocket);
        mConnectedThread.start();
    }

     public void write(int id) {
         ConnectedThread r;

         Log.d(TAG, "write: Write Called");
         mConnectedThread.write(id);
     }
}































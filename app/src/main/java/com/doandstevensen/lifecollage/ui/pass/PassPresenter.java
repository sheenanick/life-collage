package com.doandstevensen.lifecollage.ui.pass;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.ui.base.BasePresenterClass;

import java.nio.charset.Charset;
import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/22/17.
 */

public class PassPresenter extends BasePresenterClass implements PassContract.Presenter, BluetoothService.BluetoothServiceListener {
    private Context mContext;
    private PassContract.MvpView mView;
    private DataManager mDataManager;
    private BluetoothService mBluetoothService;
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;
    private Subscription mSubscription;
    private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();

    public PassPresenter(PassContract.MvpView view, Context context) {
        super(view, context);
        mContext = context;
        mView = view;
        mDataManager = new DataManager(context);

        mBluetoothService = new BluetoothService(context, this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();

                    if (deviceName != null) {
                        mDevices.add(device);
                        mView.setRecyclerViewDevices(mDevices);
                    }
                }
            }
        };
    }

    @Override
    public void isBluetoothCapable() {
        if (mBluetoothAdapter == null) {
            mView.disableView();
        }
    }

    @Override
    public void bluetoothEnabled() {
        mDevices.clear();

        if (!mBluetoothAdapter.isEnabled()) {
            mView.enableBluetooth();
        } else {
            registerForBroadcasts();
        }
    }

    @Override
    public void registerForBroadcasts() {
        mView.checkBTPermissions();

        mBluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mReceiver, filter);
    }

    public void connectThread(BluetoothDevice device) {
        BluetoothService.ConnectThread connectThread = new BluetoothService.ConnectThread(device);
        connectThread.run();
    }

    @Override
    public void acceptThread() {
        BluetoothService.AcceptThread acceptThread = new BluetoothService.AcceptThread();
        acceptThread.run();
    }

    @Override
    public void write(String collageId) {
        byte[] bytes = collageId.getBytes(Charset.defaultCharset());
        mBluetoothService.write(bytes);
    }

    @Override
    public void incomingMessage(String message) {
        Integer collageId = Integer.parseInt(message);
        mDataManager.setApiService(privateService());
        mSubscription = mDataManager.updateCollageOwner(collageId)
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
                        mView.collageReceived(collage);
                    }
                });
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {
        mView.displayLoadingAnimation();
    }

    @Override
    public void hideProgressDialog() {
        mView.hideLoadingAnimation();
    }

    @Override
    public void detach() {
        detachBase();
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
        mContext = null;
        mView = null;
        mDataManager = null;
        mBluetoothService = null;
        mBluetoothAdapter = null;
        mSubscription = null;
        mDevices = null;
    }
}

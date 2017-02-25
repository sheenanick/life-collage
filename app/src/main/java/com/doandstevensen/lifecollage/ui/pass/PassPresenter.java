package com.doandstevensen.lifecollage.ui.pass;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.doandstevensen.lifecollage.data.model.CollageListResponse;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.ui.base.BasePresenterClass;

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
    private static final String TAG = "PASS_PRESENTER";
    private Context mContext;
    private PassContract.MvpView mView;
    private DataManager mDataManager;
    private BluetoothService mBluetoothService;
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mScanReceiver;
    private final BroadcastReceiver mDiscoverabilityReceiver;
    private Subscription mSubscription;
    private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();

    public PassPresenter(PassContract.MvpView view, Context context) {
        super(view, context);
        mContext = context;
        mView = view;
        mDataManager = new DataManager(context);

        mBluetoothService = new BluetoothService(context, this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mScanReceiver = new BroadcastReceiver() {
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

        mDiscoverabilityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                    int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                    switch (mode) {
                        case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                            mView.discoverable(false);
                            Log.d(TAG, "mDiscoverabilityReceiver: Discoverability Enabled.");
                            break;
                        case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                            mView.discoverable(true);
                            Log.d(TAG, "mDiscoverabilityReceiver: Discoverability Disabled. Able to receive connections.");
                            break;
                        case BluetoothAdapter.SCAN_MODE_NONE:
                            mView.discoverable(true);
                            Log.d(TAG, "mDiscoverabilityReceiver: Discoverability Disabled. Not able to receive connections.");
                            break;
                        case BluetoothAdapter.STATE_CONNECTING:
                            mView.displayConnectingAnimation();
                            Log.d(TAG, "mDiscoverabilityReceiver: Connecting....");
                            break;
                        case BluetoothAdapter.STATE_CONNECTED:
                            mView.hideConnectingAnimation();
                            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "mDiscoverabilityReceiver: Connected.");
                            break;
                    }

                }
            }
        };
    }

    @Override
    public void loadDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            registerForBroadcasts();
        }
    }

    @Override
    public void loadCollageList() {
        mView.displayLoadingAnimation();

        int userId = mDataManager.getUserData().getUid();
        mDataManager.setApiService(privateService());

        mSubscription = mDataManager.getCollages(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<ArrayList<CollageListResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoadingAnimation();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<CollageListResponse> collages) {
                        mView.hideLoadingAnimation();
                        mView.updateSpinner(collages);
                    }
                });
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
        mContext.registerReceiver(mScanReceiver, filter);
    }

    @Override
    public void registerForDiscoverable() {
        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        mContext.registerReceiver(mDiscoverabilityReceiver,intentFilter);
    }

    public void connectThread(BluetoothDevice device) {
        mBluetoothService.connectThread(device);
    }

    public void acceptThread() {
        mBluetoothService.acceptThread();
    }

    @Override
    public void write(int collageId) {
        mBluetoothService.write(collageId);
    }

    @Override
    public void incomingMessage(int collageId) {
        if (collageId == 254) {
            Log.d("PassPresenter", "collage sent");
        } else {
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
                            mBluetoothService.write(-2);
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(CollageResponse collage) {
                            mView.collageReceived(collage);
                            mBluetoothService.write(-1);
                        }
                    });
        }
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
        try {
            mContext.unregisterReceiver(mScanReceiver);
        } catch (RuntimeException e) {
            Log.d(TAG, "detach: mScanReceiver was never registered");
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

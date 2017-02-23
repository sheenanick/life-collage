package com.doandstevensen.lifecollage.ui.pass;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.CollageListResponse;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.ui.base.BaseDrawerActivity;
import com.doandstevensen.lifecollage.util.BluetoothConnectionService;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class PassActivity extends BaseDrawerActivity implements PassContract.MvpView, AdapterView.OnItemClickListener {
    public static final String TAG = PassActivity.class.getSimpleName();

    Button toggleBluetoothButton;
    Button toggleDiscoverabilityButton;
    Button discoverDevicesButton;
    Button sendButton;
    Button startConnectionButton;
    int collageId;

    ListView mDeviceListView;

    Spinner mCollageSpinner;

    private PassPresenter mPresenter;
    private CollageSpinnerAdapter mAdapter;


    BluetoothAdapter mBluetoothAdapter;
    BluetoothConnectionService mBluetoothConnection;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mBTDevice;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_pass, mFrameLayout);

        toggleBluetoothButton = (Button) findViewById(R.id.toggleBluetoothButton);
        toggleDiscoverabilityButton = (Button) findViewById(R.id.toggleDiscoverable);
        discoverDevicesButton = (Button) findViewById(R.id.discoverDevicesButton);
        sendButton = (Button) findViewById(R.id.sendButton);
        mCollageSpinner = (Spinner) findViewById(R.id.collageSpinner);
        startConnectionButton = (Button) findViewById(R.id.connectionButton);

        mDeviceListView = (ListView) findViewById(R.id.lvNewDevices);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mDeviceListView.setOnItemClickListener(PassActivity.this);

        toggleBluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: toggle bluetooth");
                toggleBluetooth();
            }
        });

        toggleDiscoverabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: toggle discoverability");
                toggleDiscoverability();
            }
        });

        discoverDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: begin discovering devices");
                discoverDevices();
            }
        });

        startConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: start connection");
                startConnection();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: send byte array clicked");
                send();
            }
        });

        initSpinnerAdapter();
        initDrawer();
        setActionBarTitle("Pass Collages");
        mPresenter = new PassPresenter(this, this);
        Log.d(TAG, "Yay onCreate: " + mPresenter);
        mPresenter.loadCollageList();
    }

    public void initSpinnerAdapter() {
        mAdapter = new CollageSpinnerAdapter(this, android.R.layout.simple_spinner_item);
        mCollageSpinner.setAdapter(mAdapter);
        mCollageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                collageId = mAdapter.getCollageId(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(PassActivity.this, "Nothing Selected", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void updateSpinner(ArrayList<CollageListResponse> collages) {
        mAdapter.setCollages(collages);
        mAdapter.notifyDataSetChanged();
    }

    public void startConnection() {
        Log.d(TAG, "onClick: start connection");
        startBTConnection(mBTDevice, MY_UUID_INSECURE);
    }


    public void startBTConnection(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection");
        if (mBluetoothConnection == null) {
            Log.d(TAG, "startBTConnection: mBluetoothConnection is null");
            Toast.makeText(PassActivity.this, "You don't have a connection to start!", Toast.LENGTH_LONG).show();
        } else {
            mBluetoothConnection.startClient(device, uuid);
        }
    }

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch(state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, mBluetoothAdapter.ERROR);

                switch(mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverablity Enabled");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Unable to receive connections");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "BR 3 onReceive: ACTION FOUND");
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "BR 3 onReceive: " + device.getName() + " " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                mDeviceListView.setAdapter(mDeviceListAdapter);
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "BR 4 onReceive: " + action);
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "BroadcastReceiver4: BOND_BONDED");
                    Toast.makeText(PassActivity.this, "Paired with " + mDevice.getName() + "!", Toast.LENGTH_LONG).show();
                    mBTDevice = mDevice;
                }

                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver4: BOND_BONDING");
                }

                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver4: BOND_NONE");
                }
            }
        }
    };

    public void toggleBluetooth() {
        Log.d(TAG, "onClick: toggle bluetooth");
        if(mBluetoothAdapter == null) {
            Log.d(TAG, "toggleBluetooth: Does not have Bluetooth");
        } else {
            if(!mBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "toggleBluetooth: enablingBT");
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBTIntent);

                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mBroadcastReceiver1, BTIntent);
            } else if(mBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "toggleBluetooth: disablingBT");
                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                mBluetoothAdapter.disable();
            }
        }
    }

    public void toggleDiscoverability() {
        Log.d(TAG, "onClick: toggle discoverability");
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, intentFilter);
    }

    public void discoverDevices() {
        Log.d(TAG, "discoverDevices: Looking for unpaired devices");

        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "discoverDevices: Cancelling discovery");

            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, intentFilter);
        }
        if(!mBluetoothAdapter.isDiscovering()) {
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, intentFilter);
        }
    }

    public void send() {
        //TODO This is what we change to retrieve the collage id
        mBluetoothConnection.write(collageId);
    }

    private void checkBTPermissions() {
//FIXME breaks app but may need to implement for devices with higher api levels
        if(Build.VERSION.SDK_INT > 22){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < 22.");
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        try {
            unregisterReceiver(mBroadcastReceiver1);
        } catch (RuntimeException e) {
            Log.i(TAG, "onDestroy: not unregistering Receiver 1 since it was never registerer");
        }
        try {
            unregisterReceiver(mBroadcastReceiver2);
        } catch (RuntimeException e) {
            Log.i(TAG, "onDestroy: not unregistering Receiver 2 since it was never registerer");
        }
        try {
            unregisterReceiver(mBroadcastReceiver3);
        } catch (RuntimeException e) {
            Log.i(TAG, "onDestroy: not unregistering Receiver 3 since it was never registerer");
        }
        try {
            unregisterReceiver(mBroadcastReceiver4);
        } catch (RuntimeException e) {
            Log.i(TAG, "onDestroy: not unregistering Receiver 4 since it was never registerer");
        }
        if (mPresenter != null) {
            mPresenter.detach();
        }
        if (mAdapter != null) {
            mAdapter.detach();
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mBluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You clicked on a mBTDevice");
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.d(TAG, "onItemClick: Trying to pair with " + deviceName);
            Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
            mBTDevice = mBTDevices.get(i);
            if(!bondedDevices.contains(mBTDevice)) {
                mBTDevice.createBond();
            }
            mBluetoothConnection = new BluetoothConnectionService(PassActivity.this);
        } else {
            Log.d(TAG, "onItemClick: Not proceeding with pairing");
        }
    }
}

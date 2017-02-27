package com.doandstevenson.lifecollage.ui.pass;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doandstevenson.lifecollage.R;
import com.doandstevenson.lifecollage.data.model.CollageListResponse;
import com.doandstevenson.lifecollage.data.model.CollageResponse;
import com.doandstevenson.lifecollage.ui.base.BaseDrawerActivity;
import com.doandstevenson.lifecollage.util.DialogBuilder;

import java.util.ArrayList;

import static com.doandstevenson.lifecollage.Constants.DISCOVERABLE_DURATION;
import static com.doandstevenson.lifecollage.Constants.REQUEST_DISCOVERABLE;
import static com.doandstevenson.lifecollage.Constants.REQUEST_ENABLE_BT;

public class PassActivity extends BaseDrawerActivity implements PassContract.MvpView, View.OnClickListener, DeviceListAdapter.ClickListener{
    private Button mDiscoverableButton;
    private Button mScanButton;
    private Button mPassButton;
    private RecyclerView mRecyclerView;
    private Spinner mCollageSpinner;
    private TextView mNoDevicesText;

    private PassPresenter mPresenter;
    private DeviceListAdapter mAdapter;
    private CollageSpinnerAdapter mSpinnerAdapter;
    private int mCollageId = 0;

    private static final String TAG = PassActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_pass, mFrameLayout);

        initDrawer();
        setNavViewCheckedItem(R.id.nav_pass, true);

        mDiscoverableButton = (Button) findViewById(R.id.discoverable);
        mScanButton = (Button) findViewById(R.id.scan);
        mPassButton = (Button) findViewById(R.id.pass_collage);
        mRecyclerView = (RecyclerView) findViewById(R.id.devices_recycler_view);
        mCollageSpinner = (Spinner) findViewById(R.id.collage_spinner);
        mNoDevicesText = (TextView) findViewById(R.id.no_devices_text);

        mDiscoverableButton.setOnClickListener(this);
        mScanButton.setOnClickListener(this);
        mPassButton.setOnClickListener(this);

        initRecyclerView();
        initSpinnerAdapter();

        mPresenter = new PassPresenter(this, this);
        mPresenter.isBluetoothCapable();
        mPresenter.loadCollageList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavViewCheckedItem(R.id.nav_pass, true);
    }

    @Override
    public void disableView() {
        mDiscoverableButton.setEnabled(false);
        mScanButton.setEnabled(false);
        mPassButton.setEnabled(false);
    }

    private void initRecyclerView() {
        mAdapter = new DeviceListAdapter(this);
        mAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void setRecyclerViewDevices(ArrayList<BluetoothDevice> devices) {
        mNoDevicesText.setVisibility(View.GONE);
        mAdapter.setDevices(devices);
        mAdapter.notifyDataSetChanged();
    }

    public void initSpinnerAdapter() {
        mSpinnerAdapter = new CollageSpinnerAdapter(this, android.R.layout.simple_spinner_item);
        mCollageSpinner.setAdapter(mSpinnerAdapter);
        mCollageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCollageId = mSpinnerAdapter.getCollageId(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(PassActivity.this, "Nothing Selected", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void updateSpinner(ArrayList<CollageListResponse> collages) {
        mSpinnerAdapter.setCollages(collages);
        mSpinnerAdapter.notifyDataSetChanged();
    }

    @TargetApi(23)
    @Override
    public void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mDiscoverableButton) {
            setDiscoverability();
        }
        if (view == mScanButton) {
            scanForDevices();
        }
        if (view == mPassButton) {
            passCollage();
        }
    }

    public void setDiscoverability() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION); //discoverable for 300 seconds
        startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
        mPresenter.registerForDiscoverable();
    }

    public void scanForDevices() {
        mPresenter.bluetoothEnabled();
    }

    public void passCollage() {
        mPresenter.write(mCollageId);
    }

    @Override
    public void onUserClick(BluetoothDevice device) {
        mPresenter.connectThread(device);
    }

    @Override
    public void enableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
                mPresenter.registerForBroadcasts();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth enabling failed", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_DISCOVERABLE) {
            if (resultCode == DISCOVERABLE_DURATION) {
                Toast.makeText(this, "Bluetooth discoverablitiy enabled for " + DISCOVERABLE_DURATION + " seconds", Toast.LENGTH_SHORT).show();
                mPresenter.acceptThread();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Setting bluetooth discoverability failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void discoverable(boolean discoverable) {
        mDiscoverableButton.setEnabled(discoverable);
    }

    @Override
    public void collageReceived(CollageResponse collage) {
        DialogBuilder.ReceivedCollageDialogFragment(this).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }
}

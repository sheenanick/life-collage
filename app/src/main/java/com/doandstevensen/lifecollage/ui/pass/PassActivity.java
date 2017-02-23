package com.doandstevensen.lifecollage.ui.pass;

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
import android.widget.Button;
import android.widget.Toast;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.ui.base.BaseDrawerActivity;
import com.doandstevensen.lifecollage.util.DialogBuilder;

import java.util.ArrayList;

import static com.doandstevensen.lifecollage.Constants.DISCOVERABLE_DURATION;
import static com.doandstevensen.lifecollage.Constants.REQUEST_DISCOVERABLE;
import static com.doandstevensen.lifecollage.Constants.REQUEST_ENABLE_BT;

public class PassActivity extends BaseDrawerActivity implements PassContract.MvpView, View.OnClickListener, RecyclerViewAdapter.ClickListener{
    private Button mDiscoverableButton;
    private Button mScanButton;
    private Button mPassButton;
    private RecyclerView mRecyclerView;

    private PassPresenter mPresenter;
    private RecyclerViewAdapter mAdapter;

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

        mDiscoverableButton.setOnClickListener(this);
        mScanButton.setOnClickListener(this);
        mPassButton.setOnClickListener(this);

        initRecyclerView();
        mPresenter = new PassPresenter(this, this);
        mPresenter.isBluetoothCapable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavViewCheckedItem(R.id.nav_pass, true);
    }

    @Override
    public void onClick(View view) {
        if (view == mDiscoverableButton) {
            setDiscoverability();
        }
        if (view == mScanButton) {
            initiatePass();
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

    public void initiatePass() {
        mPresenter.bluetoothEnabled();
    }

    public void passCollage() {
        mPresenter.write(104);
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerViewAdapter(this);
        mAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void disableView() {
        mDiscoverableButton.setEnabled(false);
        mScanButton.setEnabled(false);
        mPassButton.setEnabled(false);
    }

    @Override
    public void discoverable(boolean discoverable) {
        mDiscoverableButton.setEnabled(discoverable);
    }

    @Override
    public void setRecyclerViewDevices(ArrayList<BluetoothDevice> devices) {
        mAdapter.setDevices(devices);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void enableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    public void onUserClick(BluetoothDevice device) {
        Log.d("PassActivity", "device clicked");
        mPresenter.connectThread(device);
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

    @TargetApi(23)
    @Override
    public void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
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

package com.doandstevensen.lifecollage.ui.pass;

import android.bluetooth.BluetoothDevice;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/22/17.
 */

public interface PassContract {
    interface Presenter extends BasePresenter {
        void isBluetoothCapable();
        void bluetoothEnabled();
        void registerForBroadcasts();
        void connectThread(BluetoothDevice device);
        void acceptThread();
        void write(String collageId);
    }

    interface MvpView extends BaseMvpView {
        void disableView();
        void setRecyclerViewDevices(ArrayList<BluetoothDevice> devices);
        void enableBluetooth();
        void checkBTPermissions();
        void collageReceived(CollageResponse collage);
    }
}

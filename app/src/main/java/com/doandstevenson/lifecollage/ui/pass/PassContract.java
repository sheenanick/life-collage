package com.doandstevenson.lifecollage.ui.pass;

import android.bluetooth.BluetoothDevice;

import com.doandstevenson.lifecollage.data.model.CollageListResponse;
import com.doandstevenson.lifecollage.data.model.CollageResponse;
import com.doandstevenson.lifecollage.ui.base.BaseMvpView;
import com.doandstevenson.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/22/17.
 */

public interface PassContract {
    interface Presenter extends BasePresenter {
        void isBluetoothCapable();
        void loadCollageList();
        void loadDevices();
        void bluetoothEnabled();
        void registerForBroadcasts();
        void registerForDiscoverable();
        void connectThread(BluetoothDevice device);
        void acceptThread();
        void write(int collageId);
    }

    interface MvpView extends BaseMvpView {
        void disableView();
        void discoverable(boolean discoverable);
        void setRecyclerViewDevices(ArrayList<BluetoothDevice> devices);
        void updateSpinner(ArrayList<CollageListResponse> collages);
        void enableBluetooth();
        void checkBTPermissions();
        void collageReceived(CollageResponse collage);
    }
}

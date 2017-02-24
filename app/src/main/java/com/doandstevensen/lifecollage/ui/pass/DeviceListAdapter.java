package com.doandstevensen.lifecollage.ui.pass;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/22/17.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<BluetoothDevice> mDevices;
    private DeviceListAdapter.ClickListener mClickListener;
    private int selectedPosition = -1;

    public DeviceListAdapter(Context context) {
        mContext = context;
    }

    public void setDevices(ArrayList<BluetoothDevice> data) {
        mDevices = data;
    }

    @Override
    public DeviceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_list, parent, false);
        return new DeviceListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeviceListAdapter.MyViewHolder holder, final int position) {
//        holder.itemView.setBackgroundColor(Color.WHITE);
        final BluetoothDevice device = mDevices.get(position);

        final String deviceName = device.getName();
        holder.textView.setText(deviceName);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (selectedPosition > -1) {
//                    notifyItemChanged(selectedPosition);
//                }
//                view.setBackgroundColor(Color.GRAY);
//                selectedPosition = position;

                mClickListener.onUserClick(device);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDevices == null) {
            return 0;
        } else {
            return mDevices.size();
        }
    }

    public void setClickListener(DeviceListAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void detach() {
        mContext = null;
        mDevices = null;
        mClickListener = null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.deviceName);
        }
    }

    interface ClickListener {
        void onUserClick(BluetoothDevice device);
    }
}


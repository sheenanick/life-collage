package com.doandstevenson.lifecollage.ui.pass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.doandstevenson.lifecollage.data.model.CollageListResponse;
import com.doandstevenson.lifecollage.data.model.CollageResponse;

import java.util.ArrayList;

/**
 * Created by CGrahamS on 2/22/17.
 */

public class CollageSpinnerAdapter extends ArrayAdapter<CollageResponse> {
    public static final String TAG = CollageSpinnerAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<CollageListResponse> mCollages;

    public CollageSpinnerAdapter(Context mContext, int textViewResourceId) {
        super(mContext, textViewResourceId);
        this.mContext = mContext;
    }

    public void setCollages(ArrayList<CollageListResponse> collages) {mCollages = collages;}

    public int getCount() {
        if (mCollages == null) {
            return 0;
        } else {
            return mCollages.size();
        }
    }

    public int getCollageId(int position) {
        return mCollages.get(position).getCollage().getCollageId();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(mContext);
        label.setText(mCollages.get(position).getCollage().getTitle());
        label.setTextSize(20);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(mContext);
        label.setText(mCollages.get(position).getCollage().getTitle());
        label.setPadding(16, 4, 4, 16);
        label.setTextSize(20);
        label.setGravity(Gravity.CENTER);

        return label;
    }

    public void detach() {
        mContext = null;
        mCollages = null;
    }
}
package com.doandstevensen.lifecollage.ui.search_collage_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.CollageListResponse;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/16/17.
 */

public class SearchListRecyclerViewAdapter extends RecyclerView.Adapter<SearchListRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<CollageListResponse> mCollages;
    private SearchListRecyclerViewAdapter.ClickListener mClickListener;

    public SearchListRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<CollageListResponse> collages) {
        mCollages = collages;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_collage_list_item, parent, false);
        return new SearchListRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CollageListResponse collageListResponse = mCollages.get(position);
        CollageResponse collage = collageListResponse.getCollage();
        PictureResponse picture = collageListResponse.getCollagePic();
        final String collageName = collage.getTitle();
        final int collageId = collage.getCollageId();

        holder.titleTextView.setText(collageName);
        String location = picture.getLocation();
        if (location != null) {
            Picasso.with(mContext).load(location).into(holder.imageView);
        } else {
            holder.emptyTextView.setVisibility(View.VISIBLE);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onCollageClick(collageId, collageName);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCollages == null) {
            return 0;
        } else {
            return mCollages.size();
        }
    }

    public void setClickListener(SearchListRecyclerViewAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void detach() {
        mContext = null;
        mCollages = null;
        mClickListener = null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView emptyTextView;

        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            titleTextView = (TextView) view.findViewById(R.id.collageTitleTextView);
            emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);
        }
    }

    interface ClickListener {
        void onCollageClick(int collageId, String collageTitle);
    }
}

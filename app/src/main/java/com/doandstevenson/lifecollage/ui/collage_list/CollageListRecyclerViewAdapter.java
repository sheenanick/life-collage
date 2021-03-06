package com.doandstevenson.lifecollage.ui.collage_list;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doandstevenson.lifecollage.R;
import com.doandstevenson.lifecollage.data.model.CollageListResponse;
import com.doandstevenson.lifecollage.data.model.CollageResponse;
import com.doandstevenson.lifecollage.data.model.PictureResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheena on 2/7/17.
 */

public class CollageListRecyclerViewAdapter extends RecyclerView.Adapter<CollageListRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<CollageListResponse> mCollages;
    private CollageListRecyclerViewAdapter.ClickListener mClickListener;
    private int mWidth;
    private static final int HEIGHT = 220;

    public CollageListRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<CollageListResponse> collages) {
        mCollages = collages;
    }

    public void setCollages(ArrayList<CollageListResponse> collages) {
        mCollages = collages;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.collage_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CollageListResponse collageListResponse = mCollages.get(position);
        final CollageResponse collage = collageListResponse.getCollage();
        final PictureResponse picture = collageListResponse.getCollagePic();
        final int collageId = collage.getCollageId();
        final String collageName = collage.getTitle();

        holder.textView.setText(collageName);
        final String location = picture.getLocation();
        if (location != null) {
            Picasso.with(mContext)
                    .load(location)
                    .resize(mWidth, HEIGHT)
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            holder.emptyTextView.setVisibility(View.VISIBLE);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onCollageClick(collageId, collageName, location != null);
            }
        });

        holder.moreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(mContext, holder.moreIcon);
                popup.getMenuInflater().inflate(R.menu.activity_collage_list_popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        mClickListener.onMenuClick(item, collageId, collageName);
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Object payload = payloads.get(0);
            if (payload instanceof String) {
                holder.textView.setText((String) payload);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mCollages == null) {
            return 0;
        } else {
            return mCollages.size();
        }
    }

    public void setClickListener(CollageListRecyclerViewAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void detach() {
        mContext = null;
        mCollages = null;
        mClickListener = null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ImageView moreIcon;
        TextView emptyTextView;

        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.collageTitleTextView);
            moreIcon = (ImageView) view.findViewById(R.id.moreIcon);
            emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);
        }
    }

    interface ClickListener {
        void onCollageClick(int collageId, String collageTitle, boolean load);
        void onMenuClick(MenuItem item, int collageId, String collageName);
    }
}

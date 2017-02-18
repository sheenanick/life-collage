package com.doandstevensen.lifecollage.ui.collage_list;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/7/17.
 */

public class CollageListRecyclerViewAdapter extends RecyclerView.Adapter<CollageListRecyclerViewAdapter.MyViewHolder> {
    private final Context mContext;
    private CollageListPresenter mPresenter;
    private ArrayList<CollageResponse> mCollages;
    private CollageListRecyclerViewAdapter.ClickListener mClickListener;

    public CollageListRecyclerViewAdapter(Context context, CollageListPresenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    public void setCollages(ArrayList<CollageResponse> data) {
        mCollages = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.collage_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CollageResponse collage = mCollages.get(position);
        final String collageName = collage.getTitle();
        holder.textView.setText(collageName);

        String url = "https://source.unsplash.com/random";
        Picasso.with(mContext).load(url).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onCollageClick(collage.getCollageId(), collageName);
            }
        });

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onCollageClick(collage.getCollageId(), collageName);
            }
        });

        holder.moreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(mContext, holder.moreIcon);
                popup.getMenuInflater().inflate(R.menu.activity_collage_list_popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        mClickListener.onMenuClick(item, collage.getCollageId(), collageName);
                        return true;
                    }
                });
                popup.show();
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

    public void setClickListener(CollageListRecyclerViewAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ImageView moreIcon;

        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.collageTitleTextView);
            moreIcon = (ImageView) view.findViewById(R.id.moreIcon);
        }
    }

    interface ClickListener {
        void onCollageClick(int collageId, String collageTitle);
        void onMenuClick(MenuItem item, int collageId, String collageName);
    }
}

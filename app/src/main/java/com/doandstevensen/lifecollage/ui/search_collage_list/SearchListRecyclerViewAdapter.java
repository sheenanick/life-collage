package com.doandstevensen.lifecollage.ui.search_collage_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.CollageResponse;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/16/17.
 */

public class SearchListRecyclerViewAdapter extends RecyclerView.Adapter<SearchListRecyclerViewAdapter.MyViewHolder> {
    private final Context mContext;
    private ArrayList<CollageResponse> mCollages;
    private SearchListRecyclerViewAdapter.ClickListener mClickListener;

    public SearchListRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void setCollages(ArrayList<CollageResponse> data) {
        mCollages = data;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_collage_list_item, parent, false);
        return new SearchListRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CollageResponse collage = mCollages.get(position);
        final String collageName = collage.getTitle();
        holder.textView.setText(collageName);

        //TODO Load Pictures

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

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.collageTitleTextView);
        }
    }

    interface ClickListener {
        void onCollageClick(int collageId, String collageTitle);
    }
}

package com.doandstevensen.lifecollage.ui.collage_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.CollageResponse;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/7/17.
 */

public class CollageListRecyclerViewAdapter extends RecyclerView.Adapter<CollageListRecyclerViewAdapter.MyViewHolder> {
    private final Context mContext;
    private ArrayList<CollageResponse> mCollages;
    private CollageListRecyclerViewAdapter.ClickListener mClickListener;

    public CollageListRecyclerViewAdapter(Context context) {
        mContext = context;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CollageResponse collage = mCollages.get(position);

        final String collageName = collage.getTitle();
        holder.textView.setText(collageName);

        //TODO Load picture
//        if (collage.getPictures().size() > 0) {
//            Picture firstPicture = collage.getPictures().get(0);
//            String url = firstPicture.getPath();
//            Picasso.with(context).load(url).into(holder.imageView);
//        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
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

    public void setClickListener(CollageListRecyclerViewAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;

        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.collageTitleTextView);
            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        }
    }

    public interface ClickListener {
        void onCollageClick(int collageId, String collageTitle);
    }
}

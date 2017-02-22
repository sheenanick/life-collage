package com.doandstevensen.lifecollage.ui.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.UserResponse;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/15/17.
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<UserResponse> mUsers;
    private SearchRecyclerViewAdapter.ClickListener mClickListener;

    public SearchRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void setUsers(ArrayList<UserResponse> data) {
        mUsers = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new SearchRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final UserResponse user = mUsers.get(position);

        final String username = user.getUsername();
        holder.textView.setText(username);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onUserClick(user.getId(), user.getUsername());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mUsers == null) {
            return 0;
        } else {
            return mUsers.size();
        }
    }

    public void setClickListener(SearchRecyclerViewAdapter.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void detach() {
        mContext = null;
        mUsers = null;
        mClickListener = null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.username);
        }
    }

    interface ClickListener {
        void onUserClick(int userId, String username);
    }
}

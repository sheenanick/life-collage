package com.doandstevensen.lifecollage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.SearchCollageActivity;
import com.doandstevensen.lifecollage.model.User;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Sheena on 1/31/17.
 */

public class SearchViewAdapter extends ArrayAdapter<User> {
    private final RealmResults<User> mUsers;
    private List<User> mResults;
    private Context mContext;

    public SearchViewAdapter(Context context, RealmResults<User> users) {
        super(context, 0);
        mUsers = users;
        mContext = context;
    }

    public int getCount() {
        return mResults == null ? 0 : mResults.size();
    }

    public User getItem(int position) {
        return mResults == null ? null : mResults.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_item, parent, false);
        }
        TextView username = (TextView) convertView.findViewById(R.id.username);
        if (user != null) {
            username.setText(user.getUsername());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchCollageActivity.class);
                intent.putExtra("uid", user.getUid());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (constraint != null) {
                    mResults = performRealmFiltering(constraint, mUsers);
                    notifyDataSetChanged();
                }
            }
        };
    }

    private ArrayList<User> performRealmFiltering(@NonNull CharSequence constraint, RealmResults<User> results){
        ArrayList<User> filteredResults = new ArrayList<>();
        for (User result : results) {
            if (result.getUsername().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                filteredResults.add(result);
            }
        }
        return filteredResults;
    }

}

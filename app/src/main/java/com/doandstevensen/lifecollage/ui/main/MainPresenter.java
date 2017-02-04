package com.doandstevensen.lifecollage.ui.main;

import android.content.Context;
import android.util.Log;

import com.doandstevensen.lifecollage.data.model.User;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Sheena on 2/3/17.
 */

public class MainPresenter implements MainContract.Presenter {
    private Realm mRealm;
    private MainContract.MvpView mView;
    private Context mContext;

    public MainPresenter(MainContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void getGridViewUsers() {
        RealmResults<User> users = mRealm.where(User.class).findAll();
        ArrayList<User> gridUsers = new ArrayList<>();
        int size = users.size();
        if (size > 6) {
            size = 6;
        }
        for (int i = 0 ; i < size ; i++) {
            Log.d("ADD USER", users.get(i).getUsername());
            gridUsers.add(users.get(i));
        }
        mView.setupGridViewAdapter(gridUsers);
    }

    @Override
    public void searchUsers() {

    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
        if (mRealm != null) {
            mRealm.close();
        }
    }
}

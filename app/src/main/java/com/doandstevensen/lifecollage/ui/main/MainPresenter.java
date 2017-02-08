package com.doandstevensen.lifecollage.ui.main;

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

    public MainPresenter(MainContract.MvpView view) {
        mView = view;
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
        RealmResults<User> users = mRealm.where(User.class).findAll();
        mView.setupSearchAdapter(users);
    }

    @Override
    public void detach() {
        mView = null;
        if (mRealm != null) {
            mRealm.close();
        }
    }
}

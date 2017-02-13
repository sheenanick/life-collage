package com.doandstevensen.lifecollage.ui.main;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

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
        mRealm = Realm.getDefaultInstance();
        mContext = context;
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
    public void load() {

    }

    @Override
    public void detach() {
        mView = null;
        if (mRealm != null) {
            mRealm.close();
        }
    }
}

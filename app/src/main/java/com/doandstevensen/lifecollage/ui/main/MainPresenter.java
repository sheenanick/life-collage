package com.doandstevensen.lifecollage.ui.main;

import android.content.Context;

/**
 * Created by Sheena on 2/3/17.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.MvpView mView;
    private Context mContext;

    public MainPresenter(MainContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getGridViewUsers() {

    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
    }
}

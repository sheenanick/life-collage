package com.doandstevensen.lifecollage.ui.account;

import android.content.Context;

/**
 * Created by Sheena on 2/6/17.
 */

public class AccountPresenter implements AccountContract.Presenter {
    private AccountContract.MvpView mView;
    private Context mContext;

    public AccountPresenter(AccountContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void deleteUser() {
        mView.userDeleted();
    }

    @Override
    public void updateEmail(String email) {
        mView.emailUpdated();
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
    }
}

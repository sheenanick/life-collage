package com.doandstevensen.lifecollage.ui.login;

import android.content.Context;
import android.widget.Toast;

import com.doandstevensen.lifecollage.ThisApplication;
import com.doandstevensen.lifecollage.ui.login.LogInContract.Presenter;

import io.realm.ObjectServerError;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by Sheena on 2/2/17.
 */

public class LogInPresenter implements Presenter {
    private LogInActivity mLogInMvpView;
    private Context mContext;

    public LogInPresenter(LogInActivity view, Context context) {
        mLogInMvpView = view;
        mContext = context;
    }

    @Override
    public void logIn(String username, String password) {
        mLogInMvpView.displayLoadingAnimation();
        SyncUser.loginAsync(SyncCredentials.usernamePassword(username, password, false), ThisApplication.AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                mLogInMvpView.navigateToMain();
                mLogInMvpView.hideLoadingAnimation();
            }
            @Override
            public void onError(ObjectServerError error) {
                String errorMsg = error.toString();
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
                mLogInMvpView.hideLoadingAnimation();
            }
        });
    }

    @Override
    public void detach() {
        mLogInMvpView = null;
        mContext = null;
    }
}

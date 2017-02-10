package com.doandstevensen.lifecollage.ui.login;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.LogInResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.ui.login.LogInContract.Presenter;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/2/17.
 */

public class LogInPresenter implements Presenter {
    private LogInContract.MvpView mView;
    private Context mContext;
    private DataManager mDataManager;
    private LifeCollageApiService mService;
    private Subscription mSubscription;

    public LogInPresenter(LogInContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mService = LifeCollageApiService.ServiceCreator.newService();
        mDataManager = new DataManager(mService, mContext);
    }

    @Override
    public void logIn(String email, String password) {
        mView.displayLoadingAnimation();
        mSubscription = mDataManager.logIn(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<LogInResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(LogInResponse logInResponse) {
                        storeData(logInResponse.getToken(), logInResponse.getId());
                        mView.hideLoadingAnimation();
                        mView.navigateToMain();
                    }
                });
    }

    private void storeData(ApplicationToken token, int userId) {
        UserDataSharedPrefsHelper helper = new UserDataSharedPrefsHelper();
        helper.storeUserToken(mContext, token);
        helper.storeUserData(mContext, userId);
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
        mService = null;
        mDataManager = null;
        mSubscription = null;
    }
}

package com.doandstevensen.lifecollage.ui.login;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.LogInResponse;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.ui.base.BasePresenterClass;
import com.doandstevensen.lifecollage.ui.login.LogInContract.Presenter;

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
        mDataManager = new DataManager(mContext);
        mDataManager.setApiService(mService);
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
                        mView.showError();
                    }

                    @Override
                    public void onNext(LogInResponse logInResponse) {
                        storeData(logInResponse.getToken(), logInResponse.getId(), logInResponse.getUsername());
                        mView.hideLoadingAnimation();
                        mView.navigateToCollageList();
                    }
                });
    }

    private void storeData(ApplicationToken token, int userId, String username) {
        User user = new User();
        user.setUid(userId);
        user.setUsername(username);
        mDataManager.storeUserToken(token);
        mDataManager.storeUserData(user);
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

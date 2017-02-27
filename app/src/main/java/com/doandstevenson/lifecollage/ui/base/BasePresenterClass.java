package com.doandstevenson.lifecollage.ui.base;

import android.content.Context;

import com.doandstevenson.lifecollage.data.model.ApplicationToken;
import com.doandstevenson.lifecollage.data.model.LogInResponse;
import com.doandstevenson.lifecollage.data.model.User;
import com.doandstevenson.lifecollage.data.remote.DataManager;
import com.doandstevenson.lifecollage.data.remote.LifeCollageApiService;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/17/17.
 */

public class BasePresenterClass {
    private boolean mRefreshComplete;
    private BaseMvpView mView;
    private Context mContext;
    private DataManager mDataManager;
    private Subscription mSubscription;
    private LifeCollageApiService mService;
    private ApplicationToken mToken;

    public BasePresenterClass(BaseMvpView view, Context context) {
        mView = view;
        mContext = context;
        mDataManager = new DataManager(context);
    }

    public LifeCollageApiService privateService() {
        mToken = mDataManager.getUserToken();
        String accessToken = mToken.getAccessToken();
        return LifeCollageApiService.ServiceCreator.newPrivateService(accessToken);
    }

    public void refresh(Throwable e, Runnable callback) {
        if (e.getMessage().contains("401")) {
            if (getAccessTokenFromRefreshToken()) {
                callback.run();
            } else {
                mView.logout();
            }
        }
    }

    private boolean getAccessTokenFromRefreshToken() {
        mService = LifeCollageApiService.ServiceCreator.newService();
        mDataManager.setApiService(mService);

        String refreshToken = mToken.getRefreshToken();
        mSubscription = mDataManager.refresh(refreshToken)
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
                        mRefreshComplete = false;
                    }

                    @Override
                    public void onNext(LogInResponse response) {
                        mDataManager.storeUserToken(response.getToken());
                        mRefreshComplete = true;
                    }
                });
        return mRefreshComplete;
    }

    public void storeData(LogInResponse response) {
        ApplicationToken token = response.getToken();
        User user = new User(response.getId(), response.getUsername(), response.getEmail());
        mDataManager.storeUserToken(token);
        mDataManager.storeUserData(user);
    }

    public void logout() {
        mDataManager.clearData();
    }

    public void detachBase() {
        mView = null;
        mContext = null;
        mDataManager = null;
        mService = null;
        mToken = null;
        mSubscription = null;
    }
}

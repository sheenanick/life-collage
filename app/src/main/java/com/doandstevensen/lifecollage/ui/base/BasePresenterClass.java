package com.doandstevensen.lifecollage.ui.base;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.LogInResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/17/17.
 */

public class BasePresenterClass {
    private boolean mRefreshComplete;
    private BaseDrawerMvpView mView;
    private Context mContext;
    private ApplicationToken mToken;
    private DataManager mDataManager;

    public BasePresenterClass(BaseDrawerMvpView view, Context context) {
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
            if (getAccessTokenFromRefreshToken(mContext, mToken)) {
                callback.run();
            } else {
                mView.logout();
            }
        }
    }

    public boolean getAccessTokenFromRefreshToken(final Context context, ApplicationToken token) {
        LifeCollageApiService publicService = LifeCollageApiService.ServiceCreator.newService();
        final DataManager dataManager = new DataManager(context);
        dataManager.setApiService(publicService);

        String refreshToken = token.getRefreshToken();
        dataManager.refresh(refreshToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                        dataManager.storeUserToken(response.getToken());
                        mRefreshComplete = true;
                    }
                });
        return mRefreshComplete;
    }
}

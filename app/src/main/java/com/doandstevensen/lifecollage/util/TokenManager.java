package com.doandstevensen.lifecollage.util;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.LogInResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/14/17.
 */

public class TokenManager {
    private boolean mRefreshComplete;

    public void getAccessTokenFromRefreshToken(final Context context, ApplicationToken token) {
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
    }

    public boolean getRefreshComplete() {
        return mRefreshComplete;
    }
}

package com.doandstevensen.lifecollage.ui.main;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.CollageListResponse;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/3/17.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.MvpView mView;
    private Context mContext;
    private Subscription mSubscription;
    private DataManager mDataManager;
    private LifeCollageApiService mService;

    public MainPresenter(MainContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mDataManager = new DataManager(context);
        mService = LifeCollageApiService.ServiceCreator.newService();
        mDataManager.setApiService(mService);
    }

    @Override
    public void checkIfLoggedIn() {
        ApplicationToken token = mDataManager.getUserToken();
        if (token.getAccessToken() != null) {
            mView.navigateToCollageList();
        } else {
            getGridViewCollages();
        }
    }

    private void getGridViewCollages() {
        mView.displayLoadingAnimation();
        mSubscription = mDataManager.getCollages(94)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<ArrayList<CollageListResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoadingAnimation();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<CollageListResponse> collages) {
                        mView.hideLoadingAnimation();
                        mView.updateGridView(collages);
                    }
                });
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
        mDataManager = null;
        mService = null;
        mSubscription = null;
    }
}

package com.doandstevensen.lifecollage.ui.collage_list;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/7/17.
 */

public class CollageListPresenter implements CollageListContract.Presenter {
    private CollageListContract.MvpView mView;
    private Context mContext;
    private LifeCollageApiService mService;
    private DataManager mDataManager;
    private Subscription mSubscription;
    private UserDataSharedPrefsHelper mSharedPrefsHelper;

    public CollageListPresenter(CollageListContract.MvpView view, Context context) {
        mView = view;
        mContext = context;

        mSharedPrefsHelper = new UserDataSharedPrefsHelper();
        String accessToken = mSharedPrefsHelper.getUserToken(context).getAccessToken();
        mService = LifeCollageApiService.ServiceCreator.newPrivateService(accessToken);
        mDataManager = new DataManager(mService, mContext);
    }

    @Override
    public void searchUsers() {

    }

    @Override
    public void loadCollageList() {
        mView.displayLoadingAnimation();
        mSubscription = mDataManager.getCollages(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<ArrayList<CollageResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(ArrayList<CollageResponse> collages) {
                        mView.hideLoadingAnimation();
                        mView.setupRecyclerViewAdapter(collages);
                    }
                });
    }

    @Override
    public void createNewCollage(final String name) {

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

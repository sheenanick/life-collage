package com.doandstevensen.lifecollage.ui.pass;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.CollageListResponse;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.ui.base.BasePresenterClass;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by CGrahamS on 2/22/17.
 */

public class PassPresenter extends BasePresenterClass implements PassContract.Presenter {
    private PassContract.MvpView mView;
    private Context mContext;
    private DataManager mDataManager;
    private LifeCollageApiService mPrivateService;
    private Subscription mSubscription;
    private ArrayList<CollageListResponse> mCollages = new ArrayList<>();

    public PassPresenter(PassContract.MvpView view, Context context) {
        super(view, context);
        mView = view;
        mContext = context;
        mDataManager = new DataManager(context);
        String token = mDataManager.getUserToken().getAccessToken();
        mPrivateService = LifeCollageApiService.ServiceCreator.newPrivateService(token);
    }

    @Override
    public void loadCollageList() {
        mView.displayLoadingAnimation();

        int userId = mDataManager.getUserData().getUid();
        mDataManager.setApiService(mPrivateService);

        mSubscription = mDataManager.getCollages(userId)
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
                        e.printStackTrace();
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(ArrayList<CollageListResponse> collages) {
                        mView.hideLoadingAnimation();
                        mCollages = collages;
                        mView.updateSpinner(collages);
                    }
                });
    }



    @Override
    public void detach() {
        detachBase();
        mView = null;
        mContext = null;
        mDataManager = null;
        mPrivateService = null;
        mSubscription = null;
    }
}

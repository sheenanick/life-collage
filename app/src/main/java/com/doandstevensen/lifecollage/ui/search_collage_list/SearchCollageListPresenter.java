package com.doandstevensen.lifecollage.ui.search_collage_list;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/16/17.
 */

public class SearchCollageListPresenter implements SearchCollageListContract.Presenter {
    private SearchCollageListContract.MvpView mView;
    private Context mContext;
    private LifeCollageApiService mPublicService;
    private DataManager mPublicDataManager;
    private Subscription mSubscription;

    public SearchCollageListPresenter(SearchCollageListContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mPublicService = LifeCollageApiService.ServiceCreator.newService();
        mPublicDataManager = new DataManager(mPublicService, mContext);
    }

    @Override
    public void loadCollageList(int userId) {
        mView.displayLoadingAnimation();
        mSubscription = mPublicDataManager.getCollages(userId)
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
                        mView.updateRecyclerView(collages);
                    }
                });
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
        mPublicService = null;
        mPublicDataManager = null;
        mSubscription = null;
    }
}

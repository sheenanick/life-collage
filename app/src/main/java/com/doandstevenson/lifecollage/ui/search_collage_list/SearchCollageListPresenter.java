package com.doandstevenson.lifecollage.ui.search_collage_list;

import android.content.Context;

import com.doandstevenson.lifecollage.data.model.CollageListResponse;
import com.doandstevenson.lifecollage.data.remote.DataManager;
import com.doandstevenson.lifecollage.data.remote.LifeCollageApiService;

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
    private LifeCollageApiService mService;
    private DataManager mDataManager;
    private Subscription mSubscription;
    private ArrayList<CollageListResponse> mCollages = new ArrayList<>();

    public SearchCollageListPresenter(SearchCollageListContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mService = LifeCollageApiService.ServiceCreator.newService();
        mDataManager = new DataManager(mContext);
        mDataManager.setApiService(mService);
    }

    @Override
    public void loadCollageList(int userId) {
        mView.displayLoadingAnimation();
        final int width = mDataManager.getScreenWidth();
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
                        mView.updateRecyclerView(collages, width);
                    }
                });
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

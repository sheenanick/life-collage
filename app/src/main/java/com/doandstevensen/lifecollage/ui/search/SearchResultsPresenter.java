package com.doandstevensen.lifecollage.ui.search;

import android.content.Context;
import android.view.View;

import com.doandstevensen.lifecollage.data.model.UserResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/15/17.
 */

public class SearchResultsPresenter implements SearchResultsContract.Presenter {
    private Context mContext;
    private SearchResultsContract.MvpView mView;
    private LifeCollageApiService mPublicService;
    private DataManager mPublicDataManager;
    private Subscription mSubscription;

    public SearchResultsPresenter(Context context, SearchResultsContract.MvpView view) {
        mContext = context;
        mView = view;
    }

    public void search(String username) {
        mPublicService = LifeCollageApiService.ServiceCreator.newService();
        mPublicDataManager = new DataManager(mPublicService, mContext);
        mSubscription = mPublicDataManager.getUsers(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<ArrayList<UserResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(ArrayList<UserResponse> users) {
                        mView.hideLoadingAnimation();
                        if (users.size() == 0) {
                            mView.setEmptyViewVisibility(View.VISIBLE);
                        } else {
                            mView.updateRecyclerView(users);
                        }
                    }
                });
    }

    @Override
    public void detach() {
        mContext = null;
        mView = null;
        mPublicService = null;
        mPublicDataManager = null;
        mSubscription = null;
    }
}

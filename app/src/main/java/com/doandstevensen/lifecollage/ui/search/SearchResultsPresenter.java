package com.doandstevensen.lifecollage.ui.search;

import android.content.Context;
import android.view.View;

import com.doandstevensen.lifecollage.data.model.UserResponse;
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
 * Created by Sheena on 2/15/17.
 */

public class SearchResultsPresenter extends BasePresenterClass implements SearchResultsContract.Presenter {
    private Context mContext;
    private SearchResultsContract.MvpView mView;
    private LifeCollageApiService mPublicService;
    private DataManager mPublicDataManager;
    private Subscription mSubscription;

    public SearchResultsPresenter(SearchResultsContract.MvpView view, Context context) {
        super(view, context);
        mContext = context;
        mView = view;
        mPublicDataManager = new DataManager(mContext);
        mPublicService = LifeCollageApiService.ServiceCreator.newService();
        mPublicDataManager.setApiService(mPublicService);
    }

    public void search(String username) {
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

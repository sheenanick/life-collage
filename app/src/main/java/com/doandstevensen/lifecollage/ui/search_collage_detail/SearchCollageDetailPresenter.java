package com.doandstevensen.lifecollage.ui.search_collage_detail;

import android.content.Context;
import android.view.View;

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
 * Created by Sheena on 2/16/17.
 */

public class SearchCollageDetailPresenter implements SearchCollageDetailContract.Presenter {
    private SearchCollageDetailContract.MvpView mView;
    private Context mContext;
    private DataManager mDataManager;
    private LifeCollageApiService mService;
    private Subscription mSubscription;

    public SearchCollageDetailPresenter(SearchCollageDetailContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mDataManager = new DataManager(context);
        mService = LifeCollageApiService.ServiceCreator.newService();
    }

    @Override
    public void loadCollage(int collageId) {
        mView.displayLoadingAnimation();
        mDataManager.setApiService(mService);

        mSubscription = mDataManager.getAllPictures(collageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<ArrayList<PictureResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(ArrayList<PictureResponse> response) {
                        mView.hideLoadingAnimation();
                        if (response.size() > 0) {
                            mView.setRecyclerViewPictures(response);
                        } else {
                            mView.setEmptyViewVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
        mDataManager = null;
        mService = null;
        mService = null;
    }
}

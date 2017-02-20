package com.doandstevensen.lifecollage.ui.featured_collage;

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
 * Created by Sheena on 2/19/17.
 */

public class FeaturedCollagePresenter implements FeaturedCollageContract.Presenter {
    private FeaturedCollageContract.MvpView mView;
    private DataManager mDataManager;
    private LifeCollageApiService mService;
    private Subscription mSubscription;

    public FeaturedCollagePresenter(FeaturedCollageContract.MvpView view, Context context) {
        mView = view;
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
                        mView.setRecyclerViewPictures(response);
                        mView.setEmptyViewVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void detach() {
        mView = null;
        mDataManager = null;
        mService = null;
        mService = null;
    }
}

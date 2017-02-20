package com.doandstevensen.lifecollage.ui.main;

import android.content.Context;
import android.util.Log;

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
    private Subscription mSubscription;
    private DataManager mDataManager;
    private LifeCollageApiService mService;
    private ArrayList<PictureResponse> mPictures = new ArrayList<>();

    public MainPresenter(MainContract.MvpView view, Context context) {
        mView = view;
        mDataManager = new DataManager(context);
        mService = LifeCollageApiService.ServiceCreator.newService();
        mDataManager.setApiService(mService);
    }

    @Override
    public void getGridViewUsers() {
       for (int i = 0; i < 3; i++) {
           getPicture(i);
       }
    }

    public void getPicture(final int position) {
        mView.displayLoadingAnimation();
        int collageId = position + 91;
        mSubscription = mDataManager.getLastPicture(collageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<PictureResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoadingAnimation();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PictureResponse pictureResponse) {
                        mView.hideLoadingAnimation();
                        mPictures.add(pictureResponse);
                        Log.d("POSITION", position + "");
                        if (position == 2) {
                            mView.updateGridView(mPictures);
                        }
                    }
                });
    }

    @Override
    public void detach() {
        mView = null;
        mDataManager = null;
        mService = null;
        mSubscription = null;
    }
}

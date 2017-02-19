package com.doandstevensen.lifecollage.ui.search_collage_list;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
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

public class SearchCollageListPresenter implements SearchCollageListContract.Presenter {
    private SearchCollageListContract.MvpView mView;
    private Context mContext;
    private LifeCollageApiService mService;
    private DataManager mDataManager;
    private Subscription mSubscription;
    private ArrayList<CollageResponse> mCollages = new ArrayList<>();
    private ArrayList<PictureResponse> mPictures = new ArrayList<>();

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
        mSubscription = mDataManager.getCollages(userId)
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
                        mCollages = collages;
                        for (int i = 0; i < mCollages.size(); i++) {
                            getPicture(i);
                        }
                    }
                });
    }

    public void getPicture(final int position) {
        if (position == 0) {
            mView.displayLoadingAnimation();
        }
        int collageId = mCollages.get(position).getCollageId();

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
                        if (position == 0) {
                            mView.updateRecyclerView(mCollages, mPictures);
                        } else {
                            mView.insertPicture(mPictures, position);
                        }
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

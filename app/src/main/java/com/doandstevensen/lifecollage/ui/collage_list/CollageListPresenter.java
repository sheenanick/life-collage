package com.doandstevensen.lifecollage.ui.collage_list;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.NewCollageRequest;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.data.model.UpdateCollageRequest;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.ui.base.BasePresenterClass;

import java.util.ArrayList;
import java.util.Iterator;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/7/17.
 */

public class CollageListPresenter extends BasePresenterClass implements CollageListContract.Presenter {
    private CollageListContract.MvpView mView;
    private DataManager mDataManager;
    private LifeCollageApiService mPublicService;
    private Subscription mSubscription;
    private ArrayList<CollageResponse> mCollages = new ArrayList<>();
    private ArrayList<PictureResponse> mPictures = new ArrayList<>();


    public CollageListPresenter(CollageListContract.MvpView view, Context context, DataManager dataManager) {
        super(view, context, dataManager);
        mView = view;
        mDataManager = dataManager;
        mPublicService = LifeCollageApiService.ServiceCreator.newService();
    }

    @Override
    public void loadCollageList(int userId) {
        mView.displayLoadingAnimation();
        mDataManager.setApiService(mPublicService);

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
                        for (CollageResponse collage : collages) {
                            getPicture(collage.getCollageId());
                        }
                    }
                });
    }

    public void getPicture(int collageId) {
        mView.displayLoadingAnimation();

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
                        mView.updateRecyclerView(mCollages, mPictures);                    }
                });
    }

    @Override
    public void createNewCollage(final String title) {
        mView.displayLoadingAnimation();
        mDataManager.setApiService(privateService());

        NewCollageRequest request = new NewCollageRequest(title);
        mSubscription = mDataManager.newCollage(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<CollageResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        refresh(e, new Runnable() {
                            @Override
                            public void run() {
                                createNewCollage(title);
                            }
                        });
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(CollageResponse collage) {
                        mView.hideLoadingAnimation();
                        mCollages.add(collage);
                        mView.updateRecyclerView(mCollages, mPictures);
                        mView.navigateToCollage(collage.getCollageId(), collage.getTitle());
                    }
                });
    }

    @Override
    public void deleteCollage(final int collageId) {
        mView.displayLoadingAnimation();
        mDataManager.setApiService(privateService());

        mSubscription = mDataManager.deleteCollageById(collageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<CollageResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        refresh(e, new Runnable() {
                            @Override
                            public void run() {
                                deleteCollage(collageId);
                            }
                        });
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(CollageResponse response) {
                        Iterator<CollageResponse> iterator = mCollages.iterator();
                        while (iterator.hasNext()) {
                            CollageResponse collage = iterator.next();
                            if (collage.getCollageId() == response.getCollageId()) {
                                iterator.remove();
                            }
                        }
                        Iterator<PictureResponse> pictureIterator = mPictures.iterator();
                        while (iterator.hasNext()) {
                            PictureResponse picture = pictureIterator.next();
                            if (picture.getCollageId() == response.getCollageId()) {
                                iterator.remove();
                            }
                        }
                        mView.hideLoadingAnimation();
                        mView.onDeleteSuccess();
                        mView.updateRecyclerView(mCollages, mPictures);
                    }
                });
    }

    @Override
    public void updateCollage(final int collageId, final String title) {
        mView.displayLoadingAnimation();
        mDataManager.setApiService(privateService());

        UpdateCollageRequest request = new UpdateCollageRequest(collageId, title);
        mSubscription = mDataManager.updateCollage(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<CollageResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        refresh(e, new Runnable() {
                            @Override
                            public void run() {
                                updateCollage(collageId, title);
                            }
                        });
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(CollageResponse response) {
                        for (int i = 0; i < mCollages.size(); i++) {
                            if (mCollages.get(i).getCollageId() == response.getCollageId()){
                                mCollages.set(i, response);
                            }
                        }
                        mView.updateRecyclerView(mCollages, mPictures);
                        mView.hideLoadingAnimation();
                    }
                });
    }

    @Override
    public void detach() {
        mView = null;
        mDataManager = null;
        mPublicService = null;
        mSubscription = null;
    }

}

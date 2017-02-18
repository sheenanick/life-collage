package com.doandstevensen.lifecollage.ui.collage_list;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.NewCollageRequest;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.data.model.UpdateCollageRequest;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.util.TokenManager;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

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

public class CollageListPresenter implements CollageListContract.Presenter {
    private CollageListContract.MvpView mView;
    private Context mContext;
    private LifeCollageApiService mPrivateService;
    private DataManager mDataManager;
    private Subscription mSubscription;
    private ApplicationToken mToken;
    private ArrayList<CollageResponse> mCollages = new ArrayList<>();
    private ArrayList<PictureResponse> mPictures = new ArrayList<>();
    private UserDataSharedPrefsHelper mSharedPrefHelper;

    public CollageListPresenter(CollageListContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mSharedPrefHelper = new UserDataSharedPrefsHelper();
    }

    public void setPrivateService() {
        mToken = mSharedPrefHelper.getUserToken(mContext);
        String accessToken = mToken.getAccessToken();
        mPrivateService = LifeCollageApiService.ServiceCreator.newPrivateService(accessToken);
        mDataManager = new DataManager(mPrivateService, mContext);
    }

    @Override
    public void loadCollageList(int userId) {
        mView.displayLoadingAnimation();

        LifeCollageApiService publicService = LifeCollageApiService.ServiceCreator.newService();
        DataManager publicDataManager = new DataManager(publicService, mContext);

        mSubscription = publicDataManager.getCollages(userId)
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

        LifeCollageApiService publicService = LifeCollageApiService.ServiceCreator.newService();
        DataManager publicDataManager = new DataManager(publicService, mContext);

        mSubscription = publicDataManager.getLastPicture(collageId)
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
                        updateRecyclerView();
                    }
                });
    }

    private void updateRecyclerView() {
        mView.updateRecyclerView(mCollages, mPictures);
    }

    @Override
    public void createNewCollage(final String title) {
        mView.displayLoadingAnimation();
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
                        if (e.getMessage().contains("401")) {
                            if (handleRefreshToken()) {
                                setPrivateService();
                                createNewCollage(title);
                            } else {
                                mView.logout();
                            }
                        }
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(CollageResponse collage) {
                        mCollages.add(collage);
                        mPictures.add(null);
                        mView.updateRecyclerView(mCollages, mPictures);
                        mView.navigateToCollage(collage.getCollageId(), collage.getTitle());
                        mView.hideLoadingAnimation();
                    }
                });
    }

    @Override
    public void deleteCollage(final int collageId) {
        mView.displayLoadingAnimation();
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
                        if (e.getMessage().contains("401")) {
                            if (handleRefreshToken()) {
                                setPrivateService();
                                deleteCollage(collageId);
                            } else {
                                mView.logout();
                            }
                        }
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
                        mView.onDeleteSuccess();
                        mView.updateRecyclerView(mCollages, mPictures);
                        mView.hideLoadingAnimation();
                    }
                });
    }

    @Override
    public void updateCollage(final int collageId, final String title) {
        mView.displayLoadingAnimation();
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
                        if (e.getMessage().contains("401")) {
                            if (handleRefreshToken()) {
                                setPrivateService();
                                updateCollage(collageId, title);
                            } else {
                                mView.logout();
                            }
                        }
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

    private boolean handleRefreshToken() {
        TokenManager tm = new TokenManager();
        tm.getAccessTokenFromRefreshToken(mContext, mToken);
        return tm.getRefreshComplete();
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
        mPrivateService = null;
        mDataManager = null;
        mSubscription = null;
    }

}

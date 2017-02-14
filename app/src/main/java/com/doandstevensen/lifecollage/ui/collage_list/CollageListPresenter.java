package com.doandstevensen.lifecollage.ui.collage_list;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.NewCollageRequest;
import com.doandstevensen.lifecollage.data.model.ServerResponse;
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


    public CollageListPresenter(CollageListContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
    }

    public void setPrivateService() {
        UserDataSharedPrefsHelper sharedPrefsHelper = new UserDataSharedPrefsHelper();
        mToken = sharedPrefsHelper.getUserToken(mContext);
        String accessToken = mToken.getAccessToken();
        mPrivateService = LifeCollageApiService.ServiceCreator.newPrivateService(accessToken);
        mDataManager = new DataManager(mPrivateService, mContext);
    }

    @Override
    public void searchUsers() {

    }

    @Override
    public void loadCollageList() {
        mView.displayLoadingAnimation();
        mSubscription = mDataManager.getCollages(false)
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
                        if (e.getMessage().contains("401")) {
                            if (handleRefreshToken()) {
                                setPrivateService();
                                loadCollageList();
                            } else {
                                mView.logout();
                            }
                        }
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(ArrayList<CollageResponse> collages) {
                        mView.hideLoadingAnimation();
                        mView.updateRecyclerView(collages);
                        mCollages = collages;
                    }
                });
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
                        mView.updateRecyclerView(mCollages);
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
                .subscribe(new Subscriber<ServerResponse>() {
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
                    public void onNext(ServerResponse response) {
                        Iterator<CollageResponse> iterator = mCollages.iterator();
                        while (iterator.hasNext()) {
                            CollageResponse collage = iterator.next();
                            if (collage.getCollageId() == collageId) {
                                iterator.remove();
                            }
                        }
                        mView.onDeleteSuccess();
                        mView.updateRecyclerView(mCollages);
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

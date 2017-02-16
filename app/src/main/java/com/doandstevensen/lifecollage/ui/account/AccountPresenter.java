package com.doandstevensen.lifecollage.ui.account;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.ServerResponse;
import com.doandstevensen.lifecollage.data.model.UpdateUserRequest;
import com.doandstevensen.lifecollage.data.model.UserResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.util.TokenManager;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/6/17.
 */

public class AccountPresenter implements AccountContract.Presenter {
    private AccountContract.MvpView mView;
    private Context mContext;
    private LifeCollageApiService mPrivateService;
    private DataManager mDataManager;
    private Subscription mSubscription;
    private ApplicationToken mToken;
    private UserDataSharedPrefsHelper mHelper;

    public AccountPresenter(AccountContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mHelper = new UserDataSharedPrefsHelper();
    }

    public void setPrivateService() {
        mToken = mHelper.getUserToken(mContext);
        String accessToken = mToken.getAccessToken();
        mPrivateService = LifeCollageApiService.ServiceCreator.newPrivateService(accessToken);
        mDataManager = new DataManager(mPrivateService, mContext);
    }

    @Override
    public void getUser() {
        mView.displayLoadingAnimation();
        mSubscription = mDataManager.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<UserResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e.getMessage().contains("401")) {
                            if (handleRefreshToken()) {
                                setPrivateService();
                                deleteUser();
                            } else {
                                mView.navigateToMain();
                            }
                        }
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(UserResponse response) {
                        mView.hideLoadingAnimation();
                        mView.setEmail(response.getEmail());
                    }
                });
    }

    @Override
    public void deleteUser() {
        mView.displayLoadingAnimation();
        mSubscription = mDataManager.deleteUser()
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
                                deleteUser();
                            } else {
                                mView.navigateToMain();
                            }
                        }
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(ServerResponse response) {
                        mView.hideLoadingAnimation();
                        mView.userDeleted();
                    }
                });
    }

    @Override
    public void updateEmail(String email) {
        mView.displayLoadingAnimation();
        int uid = mHelper.getUserData(mContext).getUid();
        UpdateUserRequest request = new UpdateUserRequest(email, uid);
        mSubscription = mDataManager.updateUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<UserResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e.getMessage().contains("401")) {
                            if (handleRefreshToken()) {
                                setPrivateService();
                                deleteUser();
                            } else {
                                mView.navigateToMain();
                            }
                        }
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(UserResponse response) {
                        mView.hideLoadingAnimation();
                        mView.emailUpdated();
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

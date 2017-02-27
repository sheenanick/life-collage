package com.doandstevenson.lifecollage.ui.account;

import android.content.Context;

import com.doandstevenson.lifecollage.data.model.ServerResponse;
import com.doandstevenson.lifecollage.data.model.UpdateUserRequest;
import com.doandstevenson.lifecollage.data.model.UserResponse;
import com.doandstevenson.lifecollage.data.remote.DataManager;
import com.doandstevenson.lifecollage.ui.base.BasePresenterClass;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/6/17.
 */

public class AccountPresenter extends BasePresenterClass implements AccountContract.Presenter {
    private AccountContract.MvpView mView;
    private Context mContext;
    private DataManager mDataManager;
    private Subscription mSubscription;

    public AccountPresenter(AccountContract.MvpView view, Context context) {
        super(view, context);
        mView = view;
        mContext = context;
        mDataManager = new DataManager(context);
    }

    @Override
    public void getEmail() {
        String email = mDataManager.getUserData().getEmail();
        mView.setEmail(email);
    }

    @Override
    public void deleteUser() {
        mView.displayLoadingAnimation();
        mDataManager.setApiService(privateService());

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
                        refresh(e, new Runnable() {
                            @Override
                            public void run() {
                                deleteUser();
                            }
                        });
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
    public void updateEmail(final String email) {
        mView.displayLoadingAnimation();
        mDataManager.setApiService(privateService());

        int uid = mDataManager.getUserData().getUid();
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
                        refresh(e, new Runnable() {
                            @Override
                            public void run() {
                                updateEmail(email);
                            }
                        });
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(UserResponse response) {
                        mView.hideLoadingAnimation();
                        mView.emailUpdated();
                    }
                });
    }

    @Override
    public void detach() {
        detachBase();
        mView = null;
        mDataManager = null;
        mSubscription = null;
        mContext = null;
    }
}

package com.doandstevenson.lifecollage.ui.signin;

import android.content.Context;

import com.doandstevenson.lifecollage.data.model.LogInResponse;
import com.doandstevenson.lifecollage.data.remote.DataManager;
import com.doandstevenson.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevenson.lifecollage.ui.base.BasePresenterClass;
import com.doandstevenson.lifecollage.ui.signin.LogInContract.Presenter;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/2/17.
 */

public class LogInPresenter extends BasePresenterClass implements Presenter {
    private LogInContract.MvpView mView;
    private Context mContext;
    private DataManager mDataManager;
    private LifeCollageApiService mService;
    private Subscription mSubscription;

    public LogInPresenter(LogInContract.MvpView view, Context context) {
        super(view, context);
        mView = view;
        mContext = context;
        mService = LifeCollageApiService.ServiceCreator.newService();
        mDataManager = new DataManager(context);
        mDataManager.setApiService(mService);
    }

    @Override
    public void logIn(String email, String password) {
        mView.displayLoadingAnimation();
        mSubscription = mDataManager.logIn(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<LogInResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideLoadingAnimation();
                        mView.showError();
                    }

                    @Override
                    public void onNext(LogInResponse logInResponse) {
                        storeData(logInResponse);
                        mView.hideLoadingAnimation();
                        mView.navigateToCollageList();
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

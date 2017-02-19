package com.doandstevensen.lifecollage.ui.signup;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.LogInResponse;
import com.doandstevensen.lifecollage.data.model.ServerResponse;
import com.doandstevensen.lifecollage.data.model.SignUpRequest;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.ui.base.BasePresenterClass;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/2/17.
 */

public class SignUpPresenter extends BasePresenterClass implements SignUpContract.Presenter {
    private SignUpContract.MvpView mView;
    private DataManager mDataManager;
    private LifeCollageApiService mService;
    private Subscription mSubscription;

    public SignUpPresenter(SignUpContract.MvpView view, Context context, DataManager dataManager) {
        super(view, context, dataManager);
        mView = view;
        mService = LifeCollageApiService.ServiceCreator.newService();
        mDataManager = dataManager;
        mDataManager.setApiService(mService);
    }

    @Override
    public void signUp(String firstName, String lastName, final String email, String username, final String password) {
        mView.displayLoadingAnimation();
        SignUpRequest request = new SignUpRequest(firstName, lastName, email, username, password);
        mSubscription = mDataManager.signUp(request)
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

                    if (e instanceof HttpException) {
                        ResponseBody body = ((HttpException) e).response().errorBody();
                        String error = null;
                        try {
                            error = body.string();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }

                        if (error != null) {
                            Gson gson = new Gson();
                            ServerResponse response = gson.fromJson(error, ServerResponse.class);
                            String devMessage = response.getDevMessage();
                            mView.showSignUpError(devMessage);
                        }
                    }
                }

                @Override
                public void onNext(LogInResponse logInResponse) {
                    logIn(email, password);
                }
            });
    }

    private void logIn(String email, String password) {
        mSubscription = mDataManager.logIn(email, password)
            .subscribeOn(Schedulers.newThread())
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
        mService = null;
        mDataManager = null;
        mSubscription = null;
    }
}

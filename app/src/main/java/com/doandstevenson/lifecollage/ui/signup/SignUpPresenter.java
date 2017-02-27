package com.doandstevenson.lifecollage.ui.signup;

import android.content.Context;

import com.doandstevenson.lifecollage.data.model.LogInResponse;
import com.doandstevenson.lifecollage.data.model.ServerResponse;
import com.doandstevenson.lifecollage.data.model.SignUpRequest;
import com.doandstevenson.lifecollage.data.remote.DataManager;
import com.doandstevenson.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevenson.lifecollage.ui.base.BasePresenterClass;
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
    private Context mContext;
    private DataManager mDataManager;
    private LifeCollageApiService mService;
    private Subscription mSubscription;

    public SignUpPresenter(SignUpContract.MvpView view, Context context) {
        super(view, context);
        mView = view;
        mContext = context;
        mService = LifeCollageApiService.ServiceCreator.newService();
        mDataManager = new DataManager(context);
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
                    mView.hideLoadingAnimation();
                    storeData(logInResponse);
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

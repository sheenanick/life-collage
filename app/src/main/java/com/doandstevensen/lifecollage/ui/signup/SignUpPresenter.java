package com.doandstevensen.lifecollage.ui.signup;

import android.content.Context;
import android.widget.Toast;

import com.doandstevensen.lifecollage.data.model.ApplicationUser;
import com.doandstevensen.lifecollage.data.model.SignUpRequest;
import com.doandstevensen.lifecollage.data.model.SignUpResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/2/17.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.MvpView mView;
    private Context mContext;
    private DataManager mDataManager;
    private Subscription mSubscription;
    private LifeCollageApiService mService;

    public SignUpPresenter(SignUpContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mService = LifeCollageApiService.ServiceCreator.newService();
        mDataManager = new DataManager(mService, mContext);
    }

    @Override
    public void signUp(String firstName, String lastName, String email, String username, String password) {
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
                .subscribe(new Subscriber<SignUpResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(SignUpResponse signUpResponse) {
                        Toast.makeText(mContext, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        mView.hideLoadingAnimation();
                        storeData(signUpResponse.getUser());
                    }
                });
    }

    public void storeData(ApplicationUser user) {
        UserDataSharedPrefsHelper helper = new UserDataSharedPrefsHelper();
        helper.storeUserData(mContext, user);
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

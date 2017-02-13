package com.doandstevensen.lifecollage.ui.signup;

import android.content.Context;
import android.widget.Toast;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.LogInResponse;
import com.doandstevensen.lifecollage.data.model.SignUpRequest;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/2/17.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.MvpView mView;
    private Context mContext;
    private DataManager mDataManager;
    private LifeCollageApiService mService;

    public SignUpPresenter(SignUpContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mService = LifeCollageApiService.ServiceCreator.newService();
        mDataManager = new DataManager(mService, mContext);
    }

    @Override
    public void signUp(String firstName, String lastName, final String email, String username, final String password) {
        mView.displayLoadingAnimation();
        SignUpRequest request = new SignUpRequest(firstName, lastName, email, username, password);
        mDataManager.signUp(request)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<LogInResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    mView.hideLoadingAnimation();
                }

                @Override
                public void onNext(LogInResponse logInResponse) {
                    logIn(email, password);
                }
            });
    }

    private void logIn(String email, String password) {
        mDataManager.logIn(email, password)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
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
                    storeData(logInResponse.getToken(), logInResponse.getId());
                    mView.hideLoadingAnimation();
                    mView.navigateToCollageList();
                }
            });
    }

    private void storeData(ApplicationToken token, int userId) {
        UserDataSharedPrefsHelper helper = new UserDataSharedPrefsHelper();
        helper.storeUserToken(mContext, token);
        helper.storeUserData(mContext, userId);
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
        mService = null;
        mDataManager = null;
    }
}

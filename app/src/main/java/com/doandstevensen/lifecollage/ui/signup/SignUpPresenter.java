package com.doandstevensen.lifecollage.ui.signup;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.LogInResponse;
import com.doandstevensen.lifecollage.data.model.ServerResponse;
import com.doandstevensen.lifecollage.data.model.SignUpRequest;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
                    storeData(logInResponse.getToken(), logInResponse.getId(), logInResponse.getUsername());
                    mView.hideLoadingAnimation();
                    mView.navigateToCollageList();
                }
            });
    }

    private void storeData(ApplicationToken token, int userId, String username) {
        UserDataSharedPrefsHelper helper = new UserDataSharedPrefsHelper();
        User user = new User();
        user.setUid(userId);
        user.setUsername(username);
        helper.storeUserToken(mContext, token);
        helper.storeUserData(mContext, user);
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
        mService = null;
        mDataManager = null;
    }
}

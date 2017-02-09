package com.doandstevensen.lifecollage.data.remote;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.SignUpRequest;
import com.doandstevensen.lifecollage.data.model.LogInResponse;

import rx.Observable;

/**
 * Created by Sheena on 2/8/17.
 */

public class DataManager {

    private LifeCollageApiService mApiService;
    private Context mContext;

    public DataManager(LifeCollageApiService service, Context context) {
        mApiService = service;
        mContext = context;
    }

    public Observable<LogInResponse> signUp(SignUpRequest request) {
        return mApiService.signUp(request);
    }

    public Observable<LogInResponse> logIn(String email, String password) {
        return mApiService.logIn(email, password);
    }
}

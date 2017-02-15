package com.doandstevensen.lifecollage.data.remote;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.LogInResponse;
import com.doandstevensen.lifecollage.data.model.NewCollageRequest;
import com.doandstevensen.lifecollage.data.model.ServerResponse;
import com.doandstevensen.lifecollage.data.model.SignUpRequest;

import java.util.ArrayList;

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

    public Observable<LogInResponse> refresh(String refreshToken) {
        return mApiService.refresh(refreshToken);
    }

    public Observable<ArrayList<CollageResponse>> getCollages(boolean getAllUsers) {
        return mApiService.getCollages(getAllUsers);
    }

    public Observable<CollageResponse> getCollageById(int collageId) {
        return mApiService.getCollageById(collageId);
    }

    public Observable<ServerResponse> deleteCollageById(int collageId) {
        return mApiService.deleteCollageById(collageId);
    }

    public Observable<CollageResponse> newCollage(NewCollageRequest collageRequest) {
        return mApiService.newCollage(collageRequest);
    }

    public Observable<ServerResponse> deleteUser() {
        return mApiService.deleteUser();
    }
}

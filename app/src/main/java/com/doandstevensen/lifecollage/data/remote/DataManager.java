package com.doandstevensen.lifecollage.data.remote;

import android.content.Context;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.LogInResponse;
import com.doandstevensen.lifecollage.data.model.NewCollageRequest;
import com.doandstevensen.lifecollage.data.model.NewPictureRequest;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.data.model.ServerResponse;
import com.doandstevensen.lifecollage.data.model.SignUpRequest;
import com.doandstevensen.lifecollage.data.model.UpdateCollageRequest;
import com.doandstevensen.lifecollage.data.model.UpdateUserRequest;
import com.doandstevensen.lifecollage.data.model.UserResponse;

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

    public Observable<UserResponse> getUser() {
        return mApiService.getUser();
    }

    public Observable<ArrayList<UserResponse>> getUsers(String username) {
        return mApiService.getUsers(username);
    }

    public Observable<ServerResponse> deleteUser() {
        return mApiService.deleteUser();
    }

    public Observable<UserResponse> updateUser(UpdateUserRequest request) {
        return mApiService.updateUser(request);
    }

    public Observable<ArrayList<CollageResponse>> getAllCollages() {
        return mApiService.getAllCollages();
    }

    public Observable<ArrayList<CollageResponse>> getCollages(int userId) {
        return mApiService.getCollages(userId);
    }

    public Observable<CollageResponse> getCollageById(int collageId) {
        return mApiService.getCollageById(collageId);
    }

    public Observable<CollageResponse> newCollage(NewCollageRequest collageRequest) {
        return mApiService.newCollage(collageRequest);
    }

    public Observable<CollageResponse> updateCollage(UpdateCollageRequest request) {
        return mApiService.updateCollage(request);
    }

    public Observable<CollageResponse> deleteCollageById(int collageId) {
        return mApiService.deleteCollageById(collageId);
    }

    public Observable<ArrayList<PictureResponse>> getAllPictures(int collageId) {
        return mApiService.getAllPictures(collageId);
    }

    public Observable<PictureResponse> getLastPicture(int collageId) {
        return mApiService.getLastPicture(collageId);
    }

    public Observable<PictureResponse> postPicture(int collageId, NewPictureRequest request) {
        return mApiService.postPicture(collageId, request);
    }

}

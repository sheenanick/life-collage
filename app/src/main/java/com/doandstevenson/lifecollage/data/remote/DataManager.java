package com.doandstevenson.lifecollage.data.remote;

import android.content.Context;

import com.doandstevenson.lifecollage.data.model.ApplicationToken;
import com.doandstevenson.lifecollage.data.model.CollageListResponse;
import com.doandstevenson.lifecollage.data.model.CollageResponse;
import com.doandstevenson.lifecollage.data.model.LogInResponse;
import com.doandstevenson.lifecollage.data.model.NewCollageRequest;
import com.doandstevenson.lifecollage.data.model.NewPictureRequest;
import com.doandstevenson.lifecollage.data.model.PictureResponse;
import com.doandstevenson.lifecollage.data.model.ServerResponse;
import com.doandstevenson.lifecollage.data.model.SignUpRequest;
import com.doandstevenson.lifecollage.data.model.UpdateCollageRequest;
import com.doandstevenson.lifecollage.data.model.UpdateUserRequest;
import com.doandstevenson.lifecollage.data.model.User;
import com.doandstevenson.lifecollage.data.model.UserResponse;
import com.doandstevenson.lifecollage.util.UserDataSharedPrefsHelper;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by Sheena on 2/8/17.
 */

public class DataManager {
    private LifeCollageApiService mApiService;
    private UserDataSharedPrefsHelper mHelper;

    public DataManager(Context context) {
        mHelper = new UserDataSharedPrefsHelper(context);
    }

    public void setApiService(LifeCollageApiService service) {
        mApiService = service;
    }


    //ACCESS API SERVICE
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

    public Observable<ArrayList<CollageListResponse>> getCollages(int userId) {
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

    public Observable<CollageResponse> updateCollageOwner(int collageId) {
        return mApiService.updateCollageOwner(collageId);
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


    //ACCESS SHARED PREFS HELPER
    public void storeUserData(User user) {
        mHelper.storeUserData(user);
    }

    public User getUserData() {
        return mHelper.getUserData();
    }

    public void storeUserToken(ApplicationToken token) {
        mHelper.storeUserToken(token);
    }

    public ApplicationToken getUserToken() {
        return mHelper.getUserToken();
    }

    public void storeScreenSize() {
        mHelper.storeScreenSize();
    }

    public Integer getScreenWidth() {
        return mHelper.getScreenWidth();
    }

    public Integer getScreenHeight() {
        return mHelper.getScreenHeight();
    }

    public void clearData() {
        mHelper.clearData();
    }
}

package com.doandstevensen.lifecollage.ui.collage_detail;

import android.content.Context;
import android.view.View;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.NewPictureRequest;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.util.S3Util;
import com.doandstevensen.lifecollage.util.TokenManager;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

import java.io.File;
import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Sheena on 2/2/17.
 */

public class CollagePresenter implements CollageContract.Presenter {
    private CollageContract.MvpView mView;
    private Context mContext;
    private LifeCollageApiService mPrivateService;
    private DataManager mDataManager;
    private Subscription mSubscription;
    private ApplicationToken mToken;
    private UserDataSharedPrefsHelper mHelper;
    private int mCollageId;
    private ArrayList<PictureResponse> mPictures = new ArrayList<>();

    public CollagePresenter(CollageContract.MvpView view, Context context, int collageId) {
        mView = view;
        mContext = context;
        mHelper = new UserDataSharedPrefsHelper();
        mCollageId = collageId;
    }

    public void setPrivateService() {
        mToken = mHelper.getUserToken(mContext);
        String accessToken = mToken.getAccessToken();
        mPrivateService = LifeCollageApiService.ServiceCreator.newPrivateService(accessToken);
        mDataManager = new DataManager(mPrivateService, mContext);
    }

    @Override
    public void loadCollage(final String title) {
        mView.displayLoadingAnimation();

        LifeCollageApiService service = LifeCollageApiService.ServiceCreator.newService();
        DataManager dataManager = new DataManager(service, mContext);
        mSubscription = dataManager.getAllPictures(mCollageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<ArrayList<PictureResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e.getMessage().contains("401")) {
                            if (handleRefreshToken()) {
                                setPrivateService();
                                loadCollage(title);
                            } else {
                                mView.logout();
                            }
                        }
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(ArrayList<PictureResponse> response) {
                        mView.hideLoadingAnimation();
                        if (response.size() > 0) {
                            mView.setRecyclerViewPictures(response);
                            mPictures = response;
                        } else {
                            mView.setEmptyViewVisibility(View.VISIBLE);
                        }
                    }
                });
        mView.setToolbarTitle(title);
    }

    public void uploadFile(final File file) {
        AmazonS3Client amazonS3Client = S3Util.getsS3Client(mContext);
        TransferUtility transferUtility = S3Util.getsTransferUtility(mContext);
        TransferObserver observer = transferUtility.upload(
                Constants.BUCKET_NAME,
                file.getName(),
                file);

        String picPath = Constants.ROOT_URL + Constants.BUCKET_NAME + "/" + file.getName();
        addCollagePic(picPath);

    }

    private void addCollagePic(final String location) {
        mView.displayLoadingAnimation();
        setPrivateService();
        NewPictureRequest request = new NewPictureRequest(location);
        mSubscription = mDataManager.postPicture(mCollageId, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSubscription = null;
                    }
                })
                .subscribe(new Subscriber<PictureResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e.getMessage().contains("401")) {
                            if (handleRefreshToken()) {
                                setPrivateService();
                                addCollagePic(location);
                            } else {
                                mView.logout();
                            }
                        }
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(PictureResponse response) {
                        mView.hideLoadingAnimation();
                        mPictures.add(response);
                        mView.setRecyclerViewPictures(mPictures);
                        mView.setEmptyViewVisibility(View.GONE);
                    }
                });
    }

    private boolean handleRefreshToken() {
        TokenManager tm = new TokenManager();
        tm.getAccessTokenFromRefreshToken(mContext, mToken);
        return tm.getRefreshComplete();
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
    }
}

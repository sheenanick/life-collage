package com.doandstevensen.lifecollage.ui.collage_detail;

import android.content.Context;
import android.view.View;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.data.model.NewPictureRequest;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.data.remote.LifeCollageApiService;
import com.doandstevensen.lifecollage.ui.base.BasePresenterClass;
import com.doandstevensen.lifecollage.util.S3Util;

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

public class CollagePresenter extends BasePresenterClass implements CollageContract.Presenter {
    private CollageContract.MvpView mView;
    private Context mContext;
    private LifeCollageApiService mPublicService;
    private DataManager mDataManager;
    private Subscription mSubscription;
    private int mCollageId;
    private ArrayList<PictureResponse> mPictures = new ArrayList<>();

    public CollagePresenter(CollageContract.MvpView view, Context context, int collageId) {
        super(view, context);
        mView = view;
        mContext = context;
        mCollageId = collageId;
        mDataManager = new DataManager(context);
        mPublicService = LifeCollageApiService.ServiceCreator.newService();
    }

    @Override
    public void loadCollage() {
        mView.displayLoadingAnimation();
        mDataManager.setApiService(mPublicService);

        mSubscription = mDataManager.getAllPictures(mCollageId)
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
        mDataManager.setApiService(privateService());

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
                        refresh(e, new Runnable() {
                            @Override
                            public void run() {
                                addCollagePic(location);
                            }
                        });
                        mView.hideLoadingAnimation();
                    }

                    @Override
                    public void onNext(PictureResponse response) {
                        mView.hideLoadingAnimation();
                        mPictures.add(response);
                        mView.updateRecyclerViewPictures(mPictures, mPictures.size() - 1);
                        mView.setEmptyViewVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void detach() {
        detachBase();
        mView = null;
        mContext = null;
        mDataManager = null;
        mPublicService = null;
        mSubscription = null;
        mPictures = null;
    }
}

package com.doandstevensen.lifecollage.ui.collage_detail;

import android.content.Context;
import android.view.View;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.util.S3Util;

import java.io.File;

/**
 * Created by Sheena on 2/2/17.
 */

public class CollagePresenter implements CollageContract.Presenter {
    private CollageContract.MvpView mView;
    private Context mContext;

    public CollagePresenter(CollageContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void loadCollage(String collageId, String title) {
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
        Picture pic = new Picture(picPath);

        mView.setEmptyViewVisibility(View.GONE);
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
    }
}

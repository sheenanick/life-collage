package com.doandstevensen.lifecollage.ui.my_collage;

import android.content.Context;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.util.RealmUserManager;
import com.doandstevensen.lifecollage.util.S3Util;

import java.io.File;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Sheena on 2/2/17.
 */

public class MyCollagePresenter implements MyCollageContract.Presenter {
    private MyCollageActivity mView;
    private Context mContext;
    private Realm mRealm;

    public MyCollagePresenter(MyCollageActivity view, Context context) {
        mView = view;
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void loadCollage() {
        User user = mRealm.where(User.class).equalTo("uid", RealmUserManager.getCurrentUserId()).findFirst();
        if (user != null && user.getCollages().size() != 0) {
            RealmList<Picture> pictures = user.getCollages().get(0).getPictures();
            mView.initRecyclerViewAdapter(pictures);
        }
    }

    @Override
    public void searchUsers() {
        RealmResults<User> users = mRealm.where(User.class).findAll();
        mView.initSearchAdapter(users);
    }

    public void uploadFile(File file) throws IOException {
        AmazonS3Client amazonS3Client = S3Util.getsS3Client(mContext);
        TransferUtility transferUtility = S3Util.getsTransferUtility(mContext);
        TransferObserver observer = transferUtility.upload(
                Constants.BUCKET_NAME,
                file.getName(),
                file);
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
        if (mRealm != null) {
            mRealm.close();
        }
    }
}

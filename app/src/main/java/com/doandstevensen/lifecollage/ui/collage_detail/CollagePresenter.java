package com.doandstevensen.lifecollage.ui.collage_detail;

import android.content.Context;
import android.view.View;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.data.model.Collage;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.util.RealmUserManager;
import com.doandstevensen.lifecollage.util.S3Util;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Sheena on 2/2/17.
 */

public class CollagePresenter implements CollageContract.Presenter {
    private CollageContract.MvpView mView;
    private Context mContext;
    private Realm mRealm;
    private Collage mCollage;
    private String mUid;
    private String mName;

    public CollagePresenter(CollageContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void loadCollage(String uid, String name) {
        mUid = uid;
        mName = name;

        User user = mRealm.where(User.class).equalTo("uid", uid).findFirst();
        mCollage =  mRealm.where(Collage.class).equalTo("userId", uid).equalTo("name", name).findFirst();

        RealmList<Picture> pictures = mCollage.getPictures();
        mView.setupRecyclerViewAdapter(pictures);

        int emptyView;
        if (pictures.size() == 0) {
            emptyView = View.VISIBLE;
        } else {
            emptyView = View.GONE;
        }

        mView.setEmptyViewVisibility(emptyView);

        String title = mCollage.getName();
        int visibility;
        boolean sameUser = uid.equals(RealmUserManager.getCurrentUserId());

        if (sameUser) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.GONE;
            title = title + " (" + user.getUsername() + ")";
        }

        mView.setToolbarTitle(title);
        mView.setFabVisibility(visibility);
    }

    public void uploadFile(final File file) {
        AmazonS3Client amazonS3Client = S3Util.getsS3Client(mContext);
        TransferUtility transferUtility = S3Util.getsTransferUtility(mContext);
        TransferObserver observer = transferUtility.upload(
                Constants.BUCKET_NAME,
                file.getName(),
                file);

        mRealm.executeTransaction( new Realm.Transaction() {
            String picPath = Constants.ROOT_URL + Constants.BUCKET_NAME + "/" + file.getName();
            Picture pic = new Picture(picPath);
            @Override
            public void execute(Realm realm) {
                mCollage.addPicture(pic);
            }
        });
        mView.setEmptyViewVisibility(View.GONE);
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

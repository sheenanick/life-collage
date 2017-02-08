package com.doandstevensen.lifecollage.ui.collage;

import android.content.Context;
import android.view.View;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.util.RealmUserManager;
import com.doandstevensen.lifecollage.util.S3Util;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Sheena on 2/2/17.
 */

public class CollagePresenter implements CollageContract.Presenter {
    private CollageContract.MvpView mView;
    private Context mContext;
    private Realm mRealm;
    private User mUser;

    public CollagePresenter(CollageContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void loadCollage(String uid) {
        mUser = mRealm.where(User.class).equalTo("uid", uid).findFirst();
        if (mUser != null && mUser.getCollages().size() != 0) {
            RealmList<Picture> pictures = mUser.getCollages().get(0).getPictures();
            mView.setupRecyclerViewAdapter(pictures);

            String title;
            int visibility;
            boolean sameUser = uid.equals(RealmUserManager.getCurrentUserId());

            if (sameUser) {
                title = "My Collage";
                visibility = View.VISIBLE;
            } else {
                title = mUser.getUsername() + "'s Collage";
                visibility = View.GONE;
            }

            mView.setToolbarTitle(title);
            mView.setNavViewCheckedItem(sameUser);
            mView.setFabVisibility(visibility);
        }
    }

    @Override
    public void searchUsers() {
        RealmResults<User> users = mRealm.where(User.class).findAll();
        mView.setupSearchAdapter(users);
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
                mUser.getCollages().get(0).addPicture(pic);
            }
        });
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
        mUser = null;
        if (mRealm != null) {
            mRealm.close();
        }
    }
}

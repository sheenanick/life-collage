package com.doandstevensen.lifecollage.ui.collage_list;

import android.content.Context;
import android.view.View;

import com.doandstevensen.lifecollage.data.model.Collage;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.util.RealmUserManager;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Sheena on 2/7/17.
 */

public class CollageListPresenter implements CollageListContract.Presenter {
    private CollageListContract.MvpView mView;
    private Context mContext;
    private Realm mRealm;
    private User mUser;

    public CollageListPresenter(CollageListContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void searchUsers() {
        RealmResults<User> users = mRealm.where(User.class).findAll();
        mView.setupSearchAdapter(users);
    }

    @Override
    public void loadCollageList(String uid) {
        mUser = mRealm.where(User.class).equalTo("uid", uid).findFirst();
        if (mUser != null && mUser.getCollages().size() != 0) {
            RealmList<Collage> collages = mUser.getCollages();
            mView.setupRecyclerViewAdapter(collages);

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
            mView.setFabVisibility(visibility);
            mView.setNavViewCheckedItem(sameUser);
        }
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

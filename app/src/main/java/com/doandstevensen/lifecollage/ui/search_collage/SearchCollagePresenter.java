package com.doandstevensen.lifecollage.ui.search_collage;

import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Sheena on 2/2/17.
 */

public class SearchCollagePresenter implements SearchCollageContract.Presenter {
    private SearchCollageActivity mView;
    private Realm mRealm;

    public SearchCollagePresenter(SearchCollageActivity view) {
        mView = view;
        mRealm = Realm.getDefaultInstance();
    }

    public void loadCollage(String uid) {
        if (uid != null) {
            User user = mRealm.where(User.class).equalTo("uid", uid).findFirst();
            RealmList<Picture> pictures = user.getCollages().get(0).getPictures();
            mView.setupRecyclerView(pictures);
        }
    }

    public void searchUsers() {
        RealmResults<User> userResults = mRealm.where(User.class).findAll();
        mView.setupSearchAdapter(userResults);
    }

    @Override
    public void detach() {
        if (mRealm != null) {
            mRealm.close();
        }
    }
}

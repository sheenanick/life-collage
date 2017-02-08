package com.doandstevensen.lifecollage.ui.collage;

import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.io.File;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Sheena on 2/2/17.
 */

public interface CollageContract {
    interface Presenter extends BasePresenter {
        void loadCollage(String uid);
        void searchUsers();
        void uploadFile(File file);
    }

    interface MvpView extends BaseMvpView {
        void setupRecyclerViewAdapter(RealmList<Picture> pictures);
        void setToolbarTitle(String title);
        void setNavViewCheckedItem(boolean sameUser);
        void setFabVisibility(int visibility);
        void setupSearchAdapter(RealmResults<User> users);
    }
}

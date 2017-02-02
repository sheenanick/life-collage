package com.doandstevensen.lifecollage.ui.search_collage;

import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Sheena on 2/2/17.
 */

public interface SearchCollageContract {
    interface Presenter extends BasePresenter {
        void loadCollage(String uid);
        void searchUsers();
    }

    interface MvpView extends BaseMvpView {
        void setupRecyclerView(RealmList<Picture> pictures);
        void setupSearchAdapter(RealmResults<User> userResults);
    }
}

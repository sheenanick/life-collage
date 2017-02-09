package com.doandstevensen.lifecollage.ui.collage_list;

import com.doandstevensen.lifecollage.data.model.Collage;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Sheena on 2/7/17.
 */

public interface CollageListContract {
    interface Presenter extends BasePresenter {
        void searchUsers();
        void loadCollageList(String uid);
        void createNewCollage(String name);
    }

    interface MvpView extends BaseMvpView {
        void setNavViewCheckedItem(boolean sameUser);
        void setupRecyclerViewAdapter(RealmList<Collage> collages);
        void setToolbarTitle(String title);
        void setupSearchAdapter(RealmResults<User> users);
        void setFabVisibility(int visibility);
        void navigateToCollage(String collageName);
    }
}

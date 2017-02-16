package com.doandstevensen.lifecollage.ui.collage_list;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by Sheena on 2/7/17.
 */

public interface CollageListContract {
    interface Presenter extends BasePresenter {
        void searchUsers();
        void loadCollageList();
        void createNewCollage(String name);
        void deleteCollage(int collageId);
    }

    interface MvpView extends BaseMvpView {
        void updateRecyclerView(ArrayList<CollageResponse> collages);
        void setToolbarTitle(String title);
        void setupSearchAdapter(RealmResults<User> users);
        void setFabVisibility(int visibility);
        void navigateToCollage(int collageId, String collageTitle);
        void onDeleteSuccess();
        void logout();
    }
}

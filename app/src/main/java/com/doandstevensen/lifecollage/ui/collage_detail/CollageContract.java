package com.doandstevensen.lifecollage.ui.collage_detail;

import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.io.File;

import io.realm.RealmList;

/**
 * Created by Sheena on 2/2/17.
 */

public interface CollageContract {
    interface Presenter extends BasePresenter {
        void loadCollage(String uid, String title);
        void uploadFile(File file);
    }

    interface MvpView extends BaseMvpView {
        void setupRecyclerViewAdapter(RealmList<Picture> pictures);
        void setToolbarTitle(String title);
        void setEmptyViewVisibility(int visibility);
    }
}

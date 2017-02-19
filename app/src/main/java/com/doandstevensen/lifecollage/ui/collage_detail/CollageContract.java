package com.doandstevensen.lifecollage.ui.collage_detail;

import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sheena on 2/2/17.
 */

public interface CollageContract {
    interface Presenter extends BasePresenter {
        void loadCollage();
        void uploadFile(File file);
    }

    interface MvpView extends BaseMvpView {
        void setRecyclerViewPictures(ArrayList<PictureResponse> pictures);
        void setEmptyViewVisibility(int visibility);
        void logout();
    }
}

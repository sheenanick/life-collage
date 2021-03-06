package com.doandstevenson.lifecollage.ui.collage_detail;

import com.doandstevenson.lifecollage.data.model.PictureResponse;
import com.doandstevenson.lifecollage.ui.base.BaseMvpView;
import com.doandstevenson.lifecollage.ui.base.BasePresenter;

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
        void updateRecyclerViewPictures(ArrayList<PictureResponse> pictures, int position);
        void setEmptyViewVisibility(int visibility);
        void logout();
    }
}

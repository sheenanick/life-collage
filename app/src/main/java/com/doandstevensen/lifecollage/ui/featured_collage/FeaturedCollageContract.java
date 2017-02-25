package com.doandstevensen.lifecollage.ui.featured_collage;

import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/19/17.
 */

public interface FeaturedCollageContract {
    interface Presenter extends BasePresenter {
        void loadCollage(int collageId);
    }

    interface MvpView extends BaseMvpView {
        void setRecyclerViewPictures(ArrayList<PictureResponse> pictures);
        void setEmptyViewVisibility(int visibility);
    }
}

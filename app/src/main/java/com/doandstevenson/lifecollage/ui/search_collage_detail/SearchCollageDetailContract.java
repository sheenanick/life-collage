package com.doandstevenson.lifecollage.ui.search_collage_detail;

import com.doandstevenson.lifecollage.data.model.PictureResponse;
import com.doandstevenson.lifecollage.ui.base.BaseMvpView;
import com.doandstevenson.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/16/17.
 */

public interface SearchCollageDetailContract {
    interface Presenter extends BasePresenter {
        void loadCollage(int collageId);
    }

    interface MvpView extends BaseMvpView {
        void setRecyclerViewPictures(ArrayList<PictureResponse> pictures);
        void setEmptyViewVisibility(int visibility);
    }
}

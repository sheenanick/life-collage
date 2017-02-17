package com.doandstevensen.lifecollage.ui.search_collage_list;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/16/17.
 */

public interface SearchCollageListContract {
    interface Presenter extends BasePresenter {
        void loadCollageList(int userId);
    }

    interface MvpView extends BaseMvpView {
        void updateRecyclerView(ArrayList<CollageResponse> collages);
        void navigateToSearchCollage(int collageId, String collageTitle);
    }
}
package com.doandstevensen.lifecollage.ui.collage_list;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/7/17.
 */

public interface CollageListContract {
    interface Presenter extends BasePresenter {
        void loadCollageList();
        void createNewCollage(String name);
        void deleteCollage(int collageId);
    }

    interface MvpView extends BaseMvpView {
        void updateRecyclerView(ArrayList<CollageResponse> collages);
        void setToolbarTitle(String title);
        void setFabVisibility(int visibility);
        void navigateToCollage(int collageId, String collageTitle);
        void onDeleteSuccess();
        void logout();
    }
}

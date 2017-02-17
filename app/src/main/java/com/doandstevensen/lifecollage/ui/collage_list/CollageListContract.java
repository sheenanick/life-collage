package com.doandstevensen.lifecollage.ui.collage_list;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.ui.base.BaseDrawerMvpView;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/7/17.
 */

public interface CollageListContract {
    interface Presenter extends BasePresenter {
        void loadCollageList(int userId);
        void createNewCollage(String name);
        void deleteCollage(int collageId);
        void updateCollage(int collageId, String title);
    }

    interface MvpView extends BaseDrawerMvpView {
        void updateRecyclerView(ArrayList<CollageResponse> collages);
        void navigateToCollage(int collageId, String collageTitle);
        void onDeleteSuccess();
    }
}

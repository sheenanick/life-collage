package com.doandstevensen.lifecollage.ui.collage_list;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
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

    interface MvpView extends BaseMvpView {
        void updateRecyclerView(ArrayList<CollageResponse> collages, ArrayList<PictureResponse> pictures);
        void navigateToCollage(int collageId, String collageTitle);
        void onDeleteSuccess();
        void logout();
    }
}

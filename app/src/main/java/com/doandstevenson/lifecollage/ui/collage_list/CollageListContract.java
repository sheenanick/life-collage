package com.doandstevenson.lifecollage.ui.collage_list;

import com.doandstevenson.lifecollage.data.model.CollageListResponse;
import com.doandstevenson.lifecollage.ui.base.BaseDrawerMvpView;
import com.doandstevenson.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/7/17.
 */

public interface CollageListContract {
    interface Presenter extends BasePresenter {
        void loadCollageList();
        void createNewCollage(String name);
        void deleteCollage(int collageId);
        void updateCollage(int collageId, String title);
    }

    interface MvpView extends BaseDrawerMvpView {
        void updateRecyclerView(ArrayList<CollageListResponse> collages, int width);
        void insertCollage(ArrayList<CollageListResponse> collages, int position);
        void deleteCollage(ArrayList<CollageListResponse> collages, int position);
        void updateCollageTitle(int position, Object payload);
        void navigateToCollage(int collageId, String collageTitle, boolean load);
    }
}

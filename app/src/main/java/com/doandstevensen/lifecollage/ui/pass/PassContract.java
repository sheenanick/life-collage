package com.doandstevensen.lifecollage.ui.pass;

import com.doandstevensen.lifecollage.data.model.CollageListResponse;
import com.doandstevensen.lifecollage.ui.base.BaseDrawerMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by CGrahamS on 2/22/17.
 */

public interface PassContract {
    interface Presenter extends BasePresenter {
        void loadCollageList();
    }

    interface MvpView extends BaseDrawerMvpView {
        void updateSpinner(ArrayList<CollageListResponse> collages);
    }
}

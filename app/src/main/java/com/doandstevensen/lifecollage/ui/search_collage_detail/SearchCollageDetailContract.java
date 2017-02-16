package com.doandstevensen.lifecollage.ui.search_collage_detail;

import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

/**
 * Created by Sheena on 2/16/17.
 */

public interface SearchCollageDetailContract {
    interface Presenter extends BasePresenter {
        void loadCollage(String uid, String title);
    }

    interface MvpView extends BaseMvpView {
        void setToolbarTitle(String title);
    }
}

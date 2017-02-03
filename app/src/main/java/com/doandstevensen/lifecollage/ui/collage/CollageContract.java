package com.doandstevensen.lifecollage.ui.collage;

import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.io.File;

/**
 * Created by Sheena on 2/2/17.
 */

public interface CollageContract {
    interface Presenter extends BasePresenter {
        void loadCollage(String uid);
        void searchUsers();
        void uploadFile(File file);
    }

    interface MvpView extends BaseMvpView {
        void logout();
    }
}

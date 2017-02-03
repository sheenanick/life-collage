package com.doandstevensen.lifecollage.ui.main;

import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

/**
 * Created by Sheena on 2/3/17.
 */

public interface MainContract {
    interface Presenter extends BasePresenter {
        void populateGridView();
        void searchUsers();

    }

    interface MvpView extends BaseMvpView {
        void navigateToCollage();
        void navigateToSignUp();
        void navigateToLogIn();
    }
}

package com.doandstevensen.lifecollage.ui.main;

import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

/**
 * Created by Sheena on 2/3/17.
 */

public interface MainContract {
    interface Presenter extends BasePresenter {
        void getGridViewUsers();
    }

    interface MvpView extends BaseMvpView {
        void setupGridViewAdapter();
        void navigateToCollageList();
        void navigateToSignUp();
        void navigateToLogIn();
    }
}

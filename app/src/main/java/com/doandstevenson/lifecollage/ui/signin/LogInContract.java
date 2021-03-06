package com.doandstevenson.lifecollage.ui.signin;

import com.doandstevenson.lifecollage.ui.base.BaseMvpView;
import com.doandstevenson.lifecollage.ui.base.BasePresenter;

/**
 * Created by Sheena on 2/2/17.
 */

public interface LogInContract {
    interface Presenter extends BasePresenter {
        void logIn(String username, String password);
    }

    interface MvpView extends BaseMvpView {
        void navigateToCollageList();
        void showError();
    }
}

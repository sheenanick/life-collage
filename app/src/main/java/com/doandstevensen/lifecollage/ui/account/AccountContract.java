package com.doandstevensen.lifecollage.ui.account;

import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

/**
 * Created by Sheena on 2/6/17.
 */

public interface AccountContract {
    interface Presenter extends BasePresenter {
        void deleteUser();
        void updateEmail(String email);
    }

    interface MvpView extends BaseMvpView {
        void emailUpdated();
        void userDeleted();
        void navigateToMain();
    }
}

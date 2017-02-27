package com.doandstevenson.lifecollage.ui.account;

import com.doandstevenson.lifecollage.ui.base.BaseDrawerMvpView;
import com.doandstevenson.lifecollage.ui.base.BasePresenter;

/**
 * Created by Sheena on 2/6/17.
 */

public interface AccountContract {
    interface Presenter extends BasePresenter {
        void deleteUser();
        void getEmail();
        void updateEmail(String email);
    }

    interface MvpView extends BaseDrawerMvpView {
        void setEmail(String email);
        void emailUpdated();
        void userDeleted();
    }
}

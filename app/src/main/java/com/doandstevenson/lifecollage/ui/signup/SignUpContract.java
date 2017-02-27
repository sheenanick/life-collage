package com.doandstevenson.lifecollage.ui.signup;

import com.doandstevenson.lifecollage.ui.base.BaseMvpView;
import com.doandstevenson.lifecollage.ui.base.BasePresenter;

/**
 * Created by Sheena on 2/2/17.
 */

public interface SignUpContract {
    interface Presenter extends BasePresenter {
        void signUp(String lastName, String firstName, String username, String email, String password);
    }

    interface MvpView extends BaseMvpView {
        void navigateToCollageList();
        void showSignUpError(String error);
    }
}

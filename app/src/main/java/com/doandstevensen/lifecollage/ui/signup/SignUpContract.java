package com.doandstevensen.lifecollage.ui.signup;

import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

/**
 * Created by Sheena on 2/2/17.
 */

public interface SignUpContract {
    interface Presenter extends BasePresenter {
        void signUp(String username, String password);
    }

    interface MvpView extends BaseMvpView {
        void navigateToMain();
    }
}

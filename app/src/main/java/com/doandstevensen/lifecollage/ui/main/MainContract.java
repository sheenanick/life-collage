package com.doandstevensen.lifecollage.ui.main;

import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/3/17.
 */

public interface MainContract {
    interface Presenter extends BasePresenter {
        void checkIfLoggedIn();
    }

    interface MvpView extends BaseMvpView {
        void setupGridViewAdapter();
        void updateGridView(ArrayList<PictureResponse> pictures);
        void navigateToCollageList();
        void navigateToSignUp();
        void navigateToLogIn();
    }
}

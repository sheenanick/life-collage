package com.doandstevenson.lifecollage.ui.main;

import com.doandstevenson.lifecollage.data.model.CollageListResponse;
import com.doandstevenson.lifecollage.ui.base.BaseMvpView;
import com.doandstevenson.lifecollage.ui.base.BasePresenter;

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
        void updateGridView(ArrayList<CollageListResponse> collages);
        void navigateToCollageList();
        void navigateToSignUp();
        void navigateToLogIn();
    }
}

package com.doandstevensen.lifecollage.ui.main;

import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.base.BaseMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by Sheena on 2/3/17.
 */

public interface MainContract {
    interface Presenter extends BasePresenter {
        void getGridViewUsers();
        void searchUsers();

    }

    interface MvpView extends BaseMvpView {
        void setupGridViewAdapter(ArrayList<User> featuredUsers);
        void setupSearchAdapter(RealmResults<User> users);
        void navigateToCollageList(String uid);
        void navigateToSignUp();
        void navigateToLogIn();
    }
}

package com.doandstevensen.lifecollage.ui.search;

import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.data.model.UserResponse;
import com.doandstevensen.lifecollage.ui.base.BaseDrawerMvpView;
import com.doandstevensen.lifecollage.ui.base.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Sheena on 2/15/17.
 */

public interface SearchResultsContract {
    interface Presenter extends BasePresenter {
        void getCurrentUser();
        void search(String username);
    }

    interface MvpView extends BaseDrawerMvpView {
        void setupNav(User currentUser);
        void updateRecyclerView(ArrayList<UserResponse> users);
        void setEmptyViewVisibility(int visibility);
    }
}

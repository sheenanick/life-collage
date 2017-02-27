package com.doandstevenson.lifecollage.ui.search;

import com.doandstevenson.lifecollage.data.model.User;
import com.doandstevenson.lifecollage.data.model.UserResponse;
import com.doandstevenson.lifecollage.ui.base.BaseDrawerMvpView;
import com.doandstevenson.lifecollage.ui.base.BasePresenter;

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

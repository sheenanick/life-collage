package com.doandstevensen.lifecollage.ui.search_collage_detail;

import android.content.Context;

/**
 * Created by Sheena on 2/16/17.
 */

public class SearchCollageDetailPresenter implements SearchCollageDetailContract.Presenter {
    private SearchCollageDetailContract.MvpView mView;
    private Context mContext;

    public SearchCollageDetailPresenter(SearchCollageDetailContract.MvpView view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void loadCollage(String uid, String title) {
        mView.setToolbarTitle(title);
    }

    @Override
    public void detach() {
        mView = null;
        mContext = null;
    }
}

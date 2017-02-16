package com.doandstevensen.lifecollage.ui.search_collage_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchCollageDetailActivity extends BaseActivity implements SearchCollageDetailContract.MvpView {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyView)
    TextView mEmptyView;
    private SearchCollageDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_collage_detail);
        ButterKnife.bind(this);

        mPresenter = new SearchCollageDetailPresenter(this, this);

        Intent intent = getIntent();
        String collageId = intent.getStringExtra("collageId");
        String collageTitle = intent.getStringExtra("collageTitle");

        mPresenter.loadCollage(collageId, collageTitle);
    }

    @Override
    public void setToolbarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}

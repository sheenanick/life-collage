package com.doandstevensen.lifecollage.ui.search_collage_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage_detail.PicturesRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchCollageDetailActivity extends BaseActivity implements SearchCollageDetailContract.MvpView {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyView)
    TextView mEmptyView;
    private SearchCollageDetailPresenter mPresenter;
    private PicturesRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_collage_detail);
        ButterKnife.bind(this);

        enableUpButton();
        initRecyclerViewAdapter();
        mPresenter = new SearchCollageDetailPresenter(this, this);

        Intent intent = getIntent();
        int collageId = intent.getIntExtra("collageId", -1);
        String collageTitle = intent.getStringExtra("collageTitle");
        setActionBarTitle(collageTitle);

        mPresenter.loadCollage(collageId);
    }

    public void initRecyclerViewAdapter() {
        mAdapter = new PicturesRecyclerViewAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void setRecyclerViewPictures(ArrayList<PictureResponse> pictures) {
        mAdapter.setPictures(pictures);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setEmptyViewVisibility(int visibility) {
        mEmptyView.setVisibility(visibility);
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
        if (mAdapter != null) {
            mAdapter.detach();
        }
        super.onDestroy();
    }
}

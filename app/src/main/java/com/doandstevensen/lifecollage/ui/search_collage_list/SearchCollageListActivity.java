package com.doandstevensen.lifecollage.ui.search_collage_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.CollageListResponse;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.search_collage_detail.SearchCollageDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchCollageListActivity extends BaseActivity implements SearchCollageListContract.MvpView, SearchListRecyclerViewAdapter.ClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private SearchCollageListPresenter mPresenter;
    private SearchListRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_collage_list);
        ButterKnife.bind(this);

        enableUpButton();
        initRecyclerViewAdapter();
        mPresenter = new SearchCollageListPresenter(this, this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        int userId = intent.getIntExtra("userId", -1);

        setToolbarTitle(username + "'s Collages");
        mPresenter.loadCollageList(userId);
    }

    private void setToolbarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    private void initRecyclerViewAdapter() {
        mAdapter = new SearchListRecyclerViewAdapter(this);
        mAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onCollageClick(int collageId, String collageTitle) {
        navigateToSearchCollage(collageId, collageTitle);
    }

    @Override
    public void updateRecyclerView(ArrayList<CollageListResponse> collages) {
        mAdapter.setData(collages);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void navigateToSearchCollage(int collageId, String collageTitle) {
        Intent intent = new Intent(SearchCollageListActivity.this, SearchCollageDetailActivity.class);
        intent.putExtra("collageTitle", collageTitle);
        intent.putExtra("collageId", collageId);
        startActivity(intent);
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

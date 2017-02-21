package com.doandstevensen.lifecollage.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.data.model.UserResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.ui.base.BaseDrawerActivity;
import com.doandstevensen.lifecollage.ui.search_collage_list.SearchCollageListActivity;

import java.util.ArrayList;

public class SearchResultsActivity extends BaseDrawerActivity implements SearchResultsContract.MvpView, SearchRecyclerViewAdapter.ClickListener {
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private TextView mNoSearchView;
    private SearchResultsContract.Presenter mPresenter;
    private SearchRecyclerViewAdapter mAdapter;
    private User mCurrentUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_search_results, mFrameLayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEmptyView = (TextView) findViewById(R.id.emptyView);
        mNoSearchView = (TextView) findViewById(R.id.noSearchView);

        mPresenter = new SearchResultsPresenter(this, this);
        initRecyclerView();
        setActionBarTitle("Search");

        DataManager dataManager = new DataManager(this);
        mCurrentUser = dataManager.getUserData();

        if (mCurrentUser.getUsername() != null) {
            initDrawer();
            setNavViewCheckedItem(R.id.nav_search, true);
        } else {
            enableUpButton();
        }

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mPresenter.search(query);
        } else {
            mNoSearchView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNavigationView != null) {
            setNavViewCheckedItem(R.id.nav_search, true);
        }
    }

    private void enableUpButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private void initRecyclerView() {
        mAdapter = new SearchRecyclerViewAdapter(this);
        mAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void updateRecyclerView(ArrayList<UserResponse> users) {
        mAdapter.setUsers(users);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setEmptyViewVisibility(int visibility) {
        mEmptyView.setVisibility(visibility);
    }

    @Override
    public void onUserClick(int userId, String username) {
        if (mCurrentUser.getUid() == userId) {
            navigateToCollageList();
        } else {
            navigateToSearchCollageList(userId, username);
        }
    }

    private void navigateToSearchCollageList(int userId, String username) {
        Intent intent = new Intent(getBaseContext(), SearchCollageListActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("username", username);
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
        super.onDestroy();
    }
}

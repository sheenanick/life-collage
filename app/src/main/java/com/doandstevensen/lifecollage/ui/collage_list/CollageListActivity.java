package com.doandstevensen.lifecollage.ui.collage_list;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.ui.base.BaseDrawerActivity;
import com.doandstevensen.lifecollage.ui.collage_detail.CollageActivity;
import com.doandstevensen.lifecollage.ui.search.SearchResultsActivity;
import com.doandstevensen.lifecollage.util.DialogBuilder;

import java.util.ArrayList;

public class CollageListActivity extends BaseDrawerActivity
        implements CollageListContract.MvpView, CollageListRecyclerViewAdapter.ClickListener, View.OnClickListener, DialogBuilder.DialogClickListener {
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;

    private CollageListPresenter mPresenter;
    private CollageListRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_collage_list, mFrameLayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        DataManager dataManager = new DataManager(this);
        int currentUserId  = dataManager.getUserData().getUid();

        mPresenter = new CollageListPresenter(this, this, dataManager);

        initRecyclerViewAdapter();
        initDrawer();

        mPresenter.loadCollageList(currentUserId);
        setActionBarTitle("My Life Collages");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavViewCheckedItem(R.id.nav_collage, true);
    }

    private void initRecyclerViewAdapter() {
        mAdapter = new CollageListRecyclerViewAdapter(this);
        mAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void updateRecyclerView(ArrayList<CollageResponse> collages, ArrayList<PictureResponse> pictures) {
        mAdapter.setData(collages, pictures);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void insertCollage(ArrayList<CollageResponse> collages, int position) {
        mAdapter.setCollages(collages);
        mAdapter.notifyItemInserted(position);
    }

    @Override
    public void insertPicture(ArrayList<PictureResponse> pictures, int position) {
        mAdapter.setPictures(pictures);
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void deleteCollage(ArrayList<CollageResponse> collages, ArrayList<PictureResponse> pictures, int position) {
        mAdapter.setData(collages, pictures);
        mAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Collage Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateCollageTitle(int position, Object payload) {
        mAdapter.notifyItemChanged(position, payload);
    }


    @Override
    public void onCollageClick(int collageId, String collageTitle, boolean load) {
        navigateToCollage(collageId, collageTitle, load);
    }

    @Override
    public void navigateToCollage(int collageId, String collageTitle, boolean load) {
        Intent intent = new Intent(getBaseContext(), CollageActivity.class);
        intent.putExtra("collageTitle", collageTitle);
        intent.putExtra("collageId", collageId);
        intent.putExtra("load", load);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == mFab) {
            DialogBuilder.NewCollageDialogFragment(this, this).show();
        }
    }

    @Override
    public void onMenuClick(MenuItem item, int collageId, String collageTitle) {
        int id = item.getItemId();

        if (id == R.id.edit) {
            DialogBuilder.UpdateCollageDialogFragment(this, this, collageTitle, collageId).show();
        }
        if (id == R.id.delete) {
            DialogBuilder.DeleteCollageDialogFragment(this, this, collageId).show();
        }
    }

    @Override
    public void onDeleteCollagePositiveClick(int collageId) {
        mPresenter.deleteCollage(collageId);
    }

    @Override
    public void onNewCollagePositiveClick(String title) {
        mPresenter.createNewCollage(title);
    }

    @Override
    public void onUpdateCollagePositiveClick(String title, int collageId) {
        mPresenter.updateCollage(collageId, title);
    }

    @Override
    public void onDialogNegativeClick() { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        ComponentName cn = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

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

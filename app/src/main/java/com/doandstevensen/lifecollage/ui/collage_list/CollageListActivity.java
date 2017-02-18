package com.doandstevensen.lifecollage.ui.collage_list;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
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
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.base.BaseDrawerActivity;
import com.doandstevensen.lifecollage.ui.collage_detail.CollageActivity;
import com.doandstevensen.lifecollage.ui.search.SearchResultsActivity;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

import java.util.ArrayList;

public class CollageListActivity extends BaseDrawerActivity
        implements CollageListContract.MvpView, CollageListRecyclerViewAdapter.ClickListener, View.OnClickListener, NewCollageDialogFragment.NewCollageDialogListener, DeleteCollageDialogFragment.DeleteCollageDialogListener, UpdateCollageDialogFragment.UpdateCollageDialogListener {
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

        mPresenter = new CollageListPresenter(this, this);
        mPresenter.setPrivateService();

        initRecyclerViewAdapter();
        initDrawer();

        UserDataSharedPrefsHelper sharedPrefs = new UserDataSharedPrefsHelper();
        User currentUser = sharedPrefs.getUserData(this);
        int currentUserId = currentUser.getUid();

        mPresenter.loadCollageList(currentUserId);
        setActionBarTitle("My Life Collages");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavViewCheckedItem(R.id.nav_collage, true);
    }

    private void initRecyclerViewAdapter() {
        mAdapter = new CollageListRecyclerViewAdapter(this, mPresenter);
        mAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void updateRecyclerView(ArrayList<CollageResponse> collages) {
        mAdapter.setCollages(collages);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCollageClick(int collageId, String collageTitle) {
        navigateToCollage(collageId, collageTitle);
    }

    @Override
    public void navigateToCollage(int collageId, String collageTitle) {
        Intent intent = new Intent(getBaseContext(), CollageActivity.class);
        intent.putExtra("collageTitle", collageTitle);
        intent.putExtra("collageId", collageId);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == mFab) {
            launchNewCollageAlertDialog();
        }
    }

    private void launchNewCollageAlertDialog() {
        NewCollageDialogFragment dialogFragment = new NewCollageDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "newCollage");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String title) {
        mPresenter.createNewCollage(title);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) { }

    @Override
    public void onMenuClick(MenuItem item, int collageId, String collageName) {
        int id = item.getItemId();

        if (id == R.id.edit) {
            launchUpdateAlertDialog(collageId, collageName);
        }
        if (id == R.id.delete) {
            launchDeleteAlertDialog(collageId);
        }
    }

    private void launchUpdateAlertDialog(int collageId, String collageName) {
        UpdateCollageDialogFragment dialogFragment = new UpdateCollageDialogFragment();
        dialogFragment.setCollage(collageId, collageName);
        dialogFragment.show(getSupportFragmentManager(), "updateCollage");
    }

    @Override
    public void onUpdateDialogPositiveClick(DialogFragment dialog, String collageName, int collageId) {
        mPresenter.updateCollage(collageId, collageName);
    }

    private void launchDeleteAlertDialog(int collageId) {
        DeleteCollageDialogFragment dialogFragment = new DeleteCollageDialogFragment();
        dialogFragment.setCollageId(collageId);
        dialogFragment.show(getSupportFragmentManager(), "deleteCollage");
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog, int collageId) {
        mPresenter.deleteCollage(collageId);
    }

    @Override
    public void onDeleteSuccess() {
        Toast.makeText(this, "Collage Deleted", Toast.LENGTH_SHORT).show();
    }

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

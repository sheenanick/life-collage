package com.doandstevensen.lifecollage.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage_detail.CollageActivity;
import com.doandstevensen.lifecollage.ui.collage_list.CollageListActivity;
import com.doandstevensen.lifecollage.ui.login.LogInActivity;
import com.doandstevensen.lifecollage.ui.signup.SignUpActivity;
import com.doandstevensen.lifecollage.util.RealmUserManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements MainContract.MvpView, MainSearchAdapter.ClickListener {
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mPresenter = new MainPresenter(this);
        mPresenter.getGridViewUsers();
        mPresenter.searchUsers();

        String currentUserId = RealmUserManager.getCurrentUserId();
        if (currentUserId != null) {
            navigateToCollage(currentUserId);
        }

    }

    public void setupGridViewAdapter(final ArrayList<User> featuredUsers) {
        gridView.setAdapter(new MainImageAdapter(this, featuredUsers));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String uid = featuredUsers.get(position).getUid();
                navigateToCollage(uid);
            }
        });
    }

    public void setupSearchAdapter(RealmResults<User> users) {
        MainSearchAdapter adapter = new MainSearchAdapter(this, users);
        autoCompleteTextView.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    public void clearSearchView() {
        autoCompleteTextView.setText("");
    }


    @Override
    public void navigateToCollage(String uid) {
        Intent intent = new Intent(getBaseContext(), CollageListActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    @OnClick(R.id.signUpButton)
    @Override
    public void navigateToSignUp() {
        Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logInButton)
    @Override
    public void navigateToLogIn() {
        Intent intent = new Intent(getBaseContext(), LogInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }

    @Override
    public void onUserClick(String uuid) {
        clearSearchView();
        Intent intent = new Intent(this, CollageListActivity.class);
        intent.putExtra("uid", uuid);
        startActivity(intent);
    }
}

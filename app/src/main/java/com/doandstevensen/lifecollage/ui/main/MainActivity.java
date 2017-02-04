package com.doandstevensen.lifecollage.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage.CollageActivity;
import com.doandstevensen.lifecollage.ui.login.LogInActivity;
import com.doandstevensen.lifecollage.ui.main.MainContract.MvpView;
import com.doandstevensen.lifecollage.ui.signup.SignUpActivity;
import com.doandstevensen.lifecollage.util.RealmUserManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MvpView {
    @BindView(R.id.gridView)
    GridView gridView;

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mPresenter = new MainPresenter(this, this);
        mPresenter.getGridViewUsers();

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

    @Override
    public void navigateToCollage(String uid) {
        Intent intent = new Intent(getBaseContext(), CollageActivity.class);
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
}

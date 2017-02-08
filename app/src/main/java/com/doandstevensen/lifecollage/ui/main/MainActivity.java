package com.doandstevensen.lifecollage.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import butterknife.OnItemSelected;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements MainContract.MvpView, MainSearchAdapter.ClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    InputMethodManager imm;


    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        ButterKnife.bind(this);

        mPresenter = new MainPresenter(this);
        mPresenter.getGridViewUsers();
        mPresenter.searchUsers();

        setupAutoCompleteTextViewListeners();

        String currentUserId = RealmUserManager.getCurrentUserId();
        if (currentUserId != null) {
            navigateToCollageList(currentUserId);
        }
    }

    public void setupGridViewAdapter(final ArrayList<User> featuredUsers) {
        gridView.setAdapter(new MainImageAdapter(this, featuredUsers));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                User user = featuredUsers.get(position);
                String uid = user.getUid();
                String collageName = user.getCollages().get(0).getName();
                navigateToCollage(uid, collageName);
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
        autoCompleteTextView.clearFocus();
    }

    //TODO set gridView to visible when edit text clears focus

    public void setupAutoCompleteTextViewListeners() {
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (imm.isAcceptingText()) {
                    Log.d(TAG, "Software keyboard visible");
                    gridView.setVisibility(View.GONE);
                    linearLayout.setGravity(Gravity.NO_GRAVITY);
                } else {
                    Log.d(TAG, "Software keyboard not visible");
                }
            }
        });

        autoCompleteTextView.setOnEditorActionListener( new AutoCompleteTextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, "SEARCHING FOR USERS");
                    autoCompleteTextView.clearFocus();
                }
                return true;
            }
        });
    }

    private void navigateToCollage(String uid, String name) {
        Intent intent = new Intent(getBaseContext(), CollageActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    @Override
    public void navigateToCollageList(String uid) {
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

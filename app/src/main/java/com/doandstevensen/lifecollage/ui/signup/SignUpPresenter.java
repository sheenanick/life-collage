package com.doandstevensen.lifecollage.ui.signup;

import com.doandstevensen.lifecollage.ThisApplication;
import com.doandstevensen.lifecollage.data.model.Collage;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by Sheena on 2/2/17.
 */

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpActivity mSignUpMvpView;
    private Realm mRealm;

    public SignUpPresenter(SignUpActivity view) {
        mSignUpMvpView = view;
    }

    @Override
    public void signUp(final String username, String password) {
        mSignUpMvpView.displayLoadingAnimation();
        SyncUser.loginAsync(SyncCredentials.usernamePassword(username, password, true), ThisApplication.AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                mSignUpMvpView.hideLoadingAnimation();
                createUserObject(user, username);
                mSignUpMvpView.navigateToMain();
            }
            @Override
            public void onError(ObjectServerError error) {
                mSignUpMvpView.hideLoadingAnimation();
                mSignUpMvpView.showSignUpError("Username already exists");
            }
        });
    }

    private void createUserObject(final SyncUser currentUser, final String username) {
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm){
                User user = realm.createObject(User.class, currentUser.getIdentity());
                user.setUsername(username);

                Collage collage = new Collage();
                collage.setName("Test Collage");

                Picture picture = new Picture("https://source.unsplash.com/random");
                collage.addPicture(picture);
                collage.addPicture(picture);

                user.addCollage(collage);
            }
        });
    }

    @Override
    public void detach() {
        mSignUpMvpView = null;
        if (mRealm != null) {
            mRealm.close();
        }
    }
}

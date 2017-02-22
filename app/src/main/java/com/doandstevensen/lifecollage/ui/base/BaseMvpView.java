package com.doandstevensen.lifecollage.ui.base;

/**
 * Created by Sheena on 2/2/17.
 */

public interface BaseMvpView {
    boolean displayLoadingAnimation();
    void hideLoadingAnimation();
    void enableUpButton();
    void setActionBarTitle(String title);
    void logout();
}

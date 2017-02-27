package com.doandstevenson.lifecollage.ui.base;

/**
 * Created by Sheena on 2/17/17.
 */

public interface BaseDrawerMvpView extends BaseMvpView {
    void initDrawer();
    void setNavViewCheckedItem(int item, boolean isChecked);
    void navigateToCollageList();
    void navigateToSearch();
    void navigateToAccount();
    void navigateToAbout();
    void logout();
}

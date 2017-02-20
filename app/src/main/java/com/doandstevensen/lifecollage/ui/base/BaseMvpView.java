package com.doandstevensen.lifecollage.ui.base;

import android.widget.TextView;

/**
 * Created by Sheena on 2/2/17.
 */

public interface BaseMvpView {
    boolean displayLoadingAnimation();
    void hideLoadingAnimation();
    void setActionBarTitle(String title);
    void setFont(TextView view);
    void logout();
}

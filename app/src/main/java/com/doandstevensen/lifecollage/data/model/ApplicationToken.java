package com.doandstevensen.lifecollage.data.model;

/**
 * Created by Sheena on 2/9/17.
 */

public class ApplicationToken {
    private String accessToken;
    private String refreshToken;
    private long accessExpires;
    private long refreshExpires;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getAccessExpires() {
        return accessExpires;
    }

    public long getRefreshExpires() {
        return refreshExpires;
    }

}

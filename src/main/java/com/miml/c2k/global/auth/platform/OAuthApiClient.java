package com.miml.c2k.global.auth.platform;

import com.miml.c2k.domain.member.OAuthProvider;

public interface OAuthApiClient {

    OAuthProvider getOAuthProvider();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);

}

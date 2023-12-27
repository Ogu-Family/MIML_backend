package com.miml.c2k.global.auth.platform;

import com.miml.c2k.domain.member.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {

    void setAuthorization(String authorizationCode);
    OAuthProvider getOAuthProvider();
    MultiValueMap<String, String> makeBody();

}

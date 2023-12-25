package com.miml.c2k.global.auth.platform;

import com.miml.c2k.domain.member.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {

    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();

}

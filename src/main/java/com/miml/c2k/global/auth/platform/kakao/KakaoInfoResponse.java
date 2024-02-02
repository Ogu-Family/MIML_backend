package com.miml.c2k.global.auth.platform.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.miml.c2k.domain.member.OAuthProvider;
import com.miml.c2k.global.auth.platform.OAuthInfoResponse;
import lombok.Getter;

/*
https://kapi.kakao.com/v2/user/me 요청 결과값
원래 더 많은 응답값이 오지만 필요한 데이터만 추려내기 위해 @JsonIgnoreProperties(ignoreUnknown = true)를 사용
 */

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;
        private String email;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}

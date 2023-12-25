package com.miml.c2k.global.auth;

import com.miml.c2k.global.auth.jwt.AuthTokens;
import com.miml.c2k.global.auth.platform.kakao.KakaoLoginParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/api/auth/kakao")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        log.info("{}", params.getAuthorizationCode());
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

}

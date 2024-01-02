package com.miml.c2k.global.auth;

import com.miml.c2k.global.auth.jwt.AuthTokens;
import com.miml.c2k.global.auth.platform.OAuthLoginParams;
import com.miml.c2k.global.auth.platform.kakao.KakaoLoginParams;
import com.miml.c2k.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthLoginService oAuthLoginService;

    @GetMapping("/login/oauth2/code/kakao")
    public ApiResponse<AuthTokens> loginKakao(@RequestParam("code") String code) {
        OAuthLoginParams params = new KakaoLoginParams();
        params.setAuthorization(code);
        return ApiResponse.create(HttpStatus.OK.value(), "정상 처리", oAuthLoginService.login(params));
    }

}

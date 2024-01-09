package com.miml.c2k.global.auth.controller;

import com.miml.c2k.domain.member.Role;
import com.miml.c2k.domain.member.controller.MemberController;
import com.miml.c2k.domain.member.dto.MemberResponseDto;
import com.miml.c2k.global.auth.service.OAuthLoginService;
import com.miml.c2k.global.auth.jwt.AuthTokens;
import com.miml.c2k.global.auth.platform.OAuthLoginParams;
import com.miml.c2k.global.auth.platform.kakao.KakaoLoginParams;
import com.miml.c2k.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthLoginService oAuthLoginService;
    private final MemberController memberController;

    @GetMapping("/login")
    public String login() {
        return "/login/login";
    }

    @GetMapping("/login/oauth2/code/kakao")
    public String loginKakao(@RequestParam("code") String code, Model model) {
        OAuthLoginParams params = new KakaoLoginParams();
        params.setAuthorization(code);
        AuthTokens authTokens = oAuthLoginService.login(params);
        if (checkAdmin(authTokens)) {
            model.addAttribute("role", Role.ADMIN);
        } else {
            model.addAttribute("role", Role.USER);
        }
        model.addAttribute("authTokens", authTokens);
        return "/home/home";
    }

    private boolean checkAdmin(AuthTokens authTokens) {
        ApiResponse<MemberResponseDto> member = memberController.findMember(
                authTokens.getAccessToken());
        return member.getData().getRole() == Role.ADMIN;
    }

}

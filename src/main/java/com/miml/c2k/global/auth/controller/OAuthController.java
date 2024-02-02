package com.miml.c2k.global.auth.controller;

import com.miml.c2k.domain.member.Role;
import com.miml.c2k.domain.member.controller.MemberController;
import com.miml.c2k.domain.member.dto.MemberResponseDto;
import com.miml.c2k.global.auth.jwt.AuthTokens;
import com.miml.c2k.global.auth.platform.OAuthLoginParams;
import com.miml.c2k.global.auth.platform.kakao.KakaoLoginParams;
import com.miml.c2k.global.auth.service.OAuthLoginService;
import com.miml.c2k.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthLoginService oAuthLoginService;
    private final MemberController memberController;

    @GetMapping("/login")
    public String login() {
        return "/login/login";
    }

    @GetMapping("/login/oauth2/code/kakao")
    public String loginKakao(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        OAuthLoginParams params = new KakaoLoginParams();
        params.setAuthorization(code);
        AuthTokens authTokens = oAuthLoginService.login(params);

        redirectAttributes.addAttribute("accessToken", authTokens.getAccessToken());

        if (checkAdmin(authTokens)) {
            return "redirect:/admin/movies/insert";
        }

        return "redirect:/myPage";
    }

    private boolean checkAdmin(AuthTokens authTokens) {
        ApiResponse<MemberResponseDto> member = memberController.findMember(
                authTokens.getAccessToken());
        return member.getData().getRole() == Role.ADMIN;
    }

}

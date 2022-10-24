package com.beside.special.controller;

import com.auth0.jwk.JwkException;
import com.beside.special.domain.dto.TokenResponse;
import com.beside.special.infra.KaKaoAuthClient;
import com.beside.special.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Social Login", description = "소셜 로그인")
@RequestMapping("login/oauth2")
@RestController
public class LoginController {
    private final LoginService loginService;
    private final KaKaoAuthClient kaKaoAuthClient;

    public LoginController(LoginService loginService, KaKaoAuthClient kaKaoAuthClient) {
        this.loginService = loginService;
        this.kaKaoAuthClient = kaKaoAuthClient;
    }

    @GetMapping("code")
    public TokenResponse loginByCode(String code) throws JwkException {
        String idToken = kaKaoAuthClient.getIdTokenByCode(code);
        return loginService.login(idToken);
    }

    @Operation(summary = "idToken기반 oauth", responses = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping("id-token")
    public TokenResponse loginByIdToken(String idToken) throws JwkException {
        return loginService.login(idToken);
    }
}

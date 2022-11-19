package com.beside.special.controller;

import com.auth0.jwk.JwkException;
import com.beside.special.domain.RefreshToken;
import com.beside.special.domain.dto.RefreshTokenRequest;
import com.beside.special.domain.dto.TokenResponse;
import com.beside.special.exception.AuthorizationException;
import com.beside.special.infra.KaKaoAuthClient;
import com.beside.special.service.AccessTokenService;
import com.beside.special.service.LoginService;
import com.beside.special.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Social Login", description = "소셜 로그인")
@RequestMapping("login/oauth2")
@RestController
public class LoginController {
    private final LoginService loginService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final KaKaoAuthClient kaKaoAuthClient;

    public LoginController(LoginService loginService,
                           AccessTokenService accessTokenService,
                           RefreshTokenService refreshTokenService,
                           KaKaoAuthClient kaKaoAuthClient) {
        this.loginService = loginService;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
        this.kaKaoAuthClient = kaKaoAuthClient;
    }

    @Operation(summary = "authorization_code기반 로그인 oauth(앱 사용 X)", responses = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
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

    @Operation(summary = "JWT refresh", responses = {
        @ApiResponse(responseCode = "200", description = "refresh 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping("refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        if (!refreshTokenService.isVerify(request.getAccessToken(), request.getRefreshToken())) {
            throw new AuthorizationException(String.format("유효하지 않은 request %s", request));
        }

        String accessToken = accessTokenService.generate(request.getAccessToken());
        RefreshToken refreshToken = refreshTokenService.generate(accessToken);

        return new TokenResponse(accessToken, refreshToken.getId());
    }
}

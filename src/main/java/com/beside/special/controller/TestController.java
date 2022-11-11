package com.beside.special.controller;

import com.beside.special.domain.AuthProvider;
import com.beside.special.domain.AuthUser;
import com.beside.special.domain.RefreshToken;
import com.beside.special.domain.User;
import com.beside.special.domain.dto.TokenResponse;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.service.AccessTokenService;
import com.beside.special.service.RefreshTokenService;
import com.beside.special.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile({"!prod"})
@RestController
public class TestController {
    private final UserService userService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public TestController(UserService userService,
                          AccessTokenService accessTokenService,
                          RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("test")
    public UserDto hello(@AuthUser UserDto userDto) {
        return userDto;
    }

    @GetMapping("test/token")
    public TokenResponse token() {
        User user = userService.findOrCreateByProvider(AuthProvider.FACEBOOK, "TEST", "TEST@test.com");
        String accessToken = accessTokenService.generate(user);
        RefreshToken refreshToken = refreshTokenService.generate(accessToken);
        return new TokenResponse(accessToken, refreshToken.getId());
    }
}

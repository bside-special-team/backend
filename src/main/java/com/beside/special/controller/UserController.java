package com.beside.special.controller;

import com.beside.special.controller.dto.UserLevelResponse;
import com.beside.special.domain.AuthUser;
import com.beside.special.domain.RefreshToken;
import com.beside.special.domain.User;
import com.beside.special.domain.UserLevel;
import com.beside.special.domain.dto.TokenResponse;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.domain.dto.UserUpdateRequest;
import com.beside.special.service.AccessTokenService;
import com.beside.special.service.RefreshTokenService;
import com.beside.special.service.UserService;
import com.beside.special.service.dto.GainPointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "User", description = "유저 관리")
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService userService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService,
                          AccessTokenService accessTokenService,
                          RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("one")
    public GainPointResponse<User> getUser(@Parameter(hidden = true) @AuthUser UserDto userDto) {
        return userService.findByIdWithAttendance(userDto.getUserId());
    }

    @GetMapping("levels")
    public List<UserLevelResponse> userLevels(@Parameter(hidden = true) @AuthUser UserDto userDto) {
        return Arrays.stream(UserLevel.values())
            .sorted(Comparator.comparing(UserLevel::getLevel))
            .map(UserLevelResponse::from)
            .collect(Collectors.toList());
    }

    @Operation(summary = "닉네임 업데이트", responses = {
        @ApiResponse(responseCode = "200", description = "업데이트 성공"),
        @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PutMapping("update")
    public TokenResponse update(@Parameter(hidden = true) @AuthUser UserDto userDto,
                                @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userService.update(userDto.getUserId(), userUpdateRequest.getNickName());
        String accessToken = accessTokenService.generate(user);
        RefreshToken refreshToken = refreshTokenService.generate(accessToken);
        return new TokenResponse(accessToken, refreshToken.getId());
    }


}

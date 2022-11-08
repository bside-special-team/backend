package com.beside.special.controller;

import com.beside.special.domain.AuthUser;
import com.beside.special.domain.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("test")
    public UserDto hello(@AuthUser UserDto userDto) {
        return userDto;
    }
}

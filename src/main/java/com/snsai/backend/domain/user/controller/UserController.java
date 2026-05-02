package com.snsai.backend.domain.user.controller;

import com.snsai.backend.domain.user.dto.AuthResponse;
import com.snsai.backend.domain.user.dto.LoginRequest;
import com.snsai.backend.domain.user.dto.SignupRequest;
import com.snsai.backend.domain.user.service.UserService;
import com.snsai.backend.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<Long> signup(@Valid @RequestBody SignupRequest request) {
        Long userId = userService.signup(request);
        return ApiResponse.success(userId);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = userService.login(request);
        return ApiResponse.success(authResponse);
    }
}

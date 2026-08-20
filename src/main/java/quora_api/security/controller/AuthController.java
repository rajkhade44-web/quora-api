package quora_api.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import quora_api.common.annotation.RateLimited;
import quora_api.common.dto.ApiResponse;
import quora_api.security.dto.AuthResponse;
import quora_api.security.dto.LoginRequest;
import quora_api.security.dto.RefreshTokenRequest;
import quora_api.security.dto.RegisterRequest;
import quora_api.security.jwt.CookieUtils;
import quora_api.security.service.AuthService;
import quora_api.security.service.UserPrincipal;
import quora_api.user.dto.UserResponseDto;
import quora_api.user.mapper.UserMapper;
import quora_api.user.repository.UserRepository;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CookieUtils cookieUtils;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final UserRepository userRepository;



    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request,
            HttpServletResponse response) {
        var result = authService.register(request);
        response.addHeader("Set-Cookie", cookieUtils.creatResponseTokenCookie(result.getRefreshToken()).toString());
        return ApiResponse.success(result.getAuthResponse(), "Registration Successful");
    }

    @PostMapping("/login")
    @RateLimited(limit = 5, windowSeconds = 60, key = "login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var result = authService.login(request);
        response.addHeader("Set-Cookie", cookieUtils.creatResponseTokenCookie(result.getRefreshToken()).toString());
        return ApiResponse.success(result.getAuthResponse(), "Login successful");
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(
        @CookieValue(name="refresh_token",required = false) String refreshToken, HttpServletResponse response
    ) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token not present in cookie");
        }
        var result = authService.refresh(refreshToken);

        response.addHeader("Set-Cookie", cookieUtils.creatResponseTokenCookie(result.getRefreshToken()).toString());
        return ApiResponse.success(result.getAuthResponse(), "Token refreshed");
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
        @CookieValue(name = "refresh_token",required = false) String refreshToken, HttpServletResponse response
    ) {
        if (refreshToken == null) {
            authService.logout(refreshToken);
        }
        response.addHeader("Set-Cookie", cookieUtils.removeResponseTokenCookie().toString());
    }

    @GetMapping("/me")
    public ApiResponse<UserResponseDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        var user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ApiResponse.success(userMapper.toResponseDto(user), "Current user profile");
    }
}

package quora_api.user.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import quora_api.common.dto.ApiResponse;
import quora_api.user.dto.UserRequestDto;
import quora_api.user.dto.UserResponseDto;
import quora_api.user.dto.UserUpdateDto;
import quora_api.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ApiResponse<UserResponseDto> registerUser(@RequestBody UserRequestDto requestDto) {
        return ApiResponse.success(userService.register(requestDto), "User registered successfully.");
    }
    
    @GetMapping("/{id}")
    public ApiResponse<UserResponseDto> getById(@PathVariable UUID id) {
        return ApiResponse.success(userService.getById(id), "User found.");
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponseDto> updateUser(@PathVariable UUID id, @RequestBody UserUpdateDto dto) {
        return ApiResponse.success(userService.update(id, dto), "User updated successfully.");
    }
}

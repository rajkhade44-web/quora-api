package quora_api.user.service;

import java.util.UUID;

import quora_api.user.dto.UserRequestDto;
import quora_api.user.dto.UserResponseDto;
import quora_api.user.dto.UserUpdateDto;

public interface UserService {
    UserResponseDto register(UserRequestDto dto);

    UserResponseDto getById(UUID id);

    UserResponseDto update(UUID id, UserUpdateDto dto);
}

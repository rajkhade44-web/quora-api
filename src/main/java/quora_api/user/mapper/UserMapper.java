package quora_api.user.mapper;

import org.springframework.stereotype.Component;

import quora_api.user.dto.UserRequestDto;
import quora_api.user.dto.UserResponseDto;
import quora_api.user.dto.UserUpdateDto;
import quora_api.user.entity.User;

@Component
public class UserMapper {

    public User toUser(UserRequestDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .bio(dto.getBio())
                .build();
    }
    
    public User updateEntity(User existing, UserUpdateDto dto) {
        if (dto.getUsername() != null)
            existing.setUsername(dto.getUsername());
        if (dto.getEmail() != null)
            existing.setEmail(dto.getEmail());
        if (dto.getBio() != null)
            existing.setBio(dto.getBio());
        return existing;
    }
    
    public UserResponseDto toResponseDto(User entity) {
        return UserResponseDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .bio(entity.getBio())
                .createdAt(entity.getCreatedAt())
                .build();
    }


}

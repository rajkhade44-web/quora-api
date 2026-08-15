package quora_api.user.service.impl;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import quora_api.common.exception.DuplicateResourceException;
import quora_api.common.exception.ResourceNotFoundException;
import quora_api.user.dto.UserRequestDto;
import quora_api.user.dto.UserResponseDto;
import quora_api.user.dto.UserUpdateDto;
import quora_api.user.entity.User;
import quora_api.user.mapper.UserMapper;
import quora_api.user.repository.UserRepository;
import quora_api.user.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto register(UserRequestDto dto) {
        User savedUser = userRepository.save(userMapper.toUser(dto));
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#id")
    public UserResponseDto getById(UUID id) {
        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponseDto(dbUser);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key="#id")
    public UserResponseDto update(UUID id, UserUpdateDto dto) {

        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exists with id: " + id));
        
        if (dto.getUsername() != null && !dbUser.getUsername().equals(dto.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username alreadytaken");
        }

        if (dto.getEmail() != null && !dbUser.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        userMapper.updateEntity(dbUser, dto);
        return userMapper.toResponseDto(userRepository.save(dbUser));
    }

}

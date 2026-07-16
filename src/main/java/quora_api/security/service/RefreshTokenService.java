package quora_api.security.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import quora_api.common.exception.ResourceNotFoundException;
import quora_api.security.entity.RefreshToken;
import quora_api.security.repoository.RefreshTokenRepository;
import quora_api.user.entity.User;
import quora_api.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public RefreshToken createRefreshToken(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UUID familyId = UUID.randomUUID();
        String tokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .revoked(false)
                .familyId(familyId)
                .token(tokenValue)
                .used(false)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
    
    @Transactional
    public RefreshToken rotateRefreshToken(String oldTokenValue) {
        RefreshToken oldToken = refreshTokenRepository.findByToken(oldTokenValue)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

        if (oldToken.isRevoked() || oldToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        if (oldToken.isUsed()) {
            refreshTokenRepository.revokeAllByFamilyId(oldToken.getFamilyId());
            throw new IllegalArgumentException("Refresh token reuse detected - family revoked");
        }

        oldToken.setUsed(true);
        refreshTokenRepository.save(oldToken);

        User user = oldToken.getUser();
        RefreshToken newToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .familyId(oldToken.getFamilyId())
                .createdAt(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .used(false)
                .build();

        return refreshTokenRepository.save(newToken);
    }
    
    public void revokeRefreshToken(String tokenValue) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
        
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }
}

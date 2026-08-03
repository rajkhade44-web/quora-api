package quora_api.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import quora_api.common.exception.DuplicateResourceException;
import quora_api.common.exception.ResourceNotFoundException;
import quora_api.security.dto.AuthResponse;
import quora_api.security.dto.AuthResult;
import quora_api.security.dto.LoginRequest;
import quora_api.security.dto.RefreshTokenRequest;
import quora_api.security.dto.RegisterRequest;
import quora_api.security.entity.RefreshToken;
import quora_api.security.jwt.JwtUtils;
import quora_api.security.repoository.RefreshTokenRepository;
import quora_api.user.entity.User;
import quora_api.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResult register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new DuplicateResourceException("Username already taken");
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Email already registered");
        }

        User user = User.builder()
                .email(request.getEmail())
                .bio(request.getBio())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .failedLoginAttemps(0)
                .build();
        
        user = userRepository.save(user);

        //Generate access token
        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return buildAuthResponse(user, accessToken,refreshToken.getToken());
    }

    @Override
    public AuthResult login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isAccountLocked()) {
            throw new IllegalArgumentException("Account is locked. Try again later.");
        }

        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        user.resetFailedAttempts();
        userRepository.save(user);

        return buildAuthResponse(user, accessToken,refreshToken.getToken());
    }

    @Override
    public AuthResult refresh(String refreshToken) {
        RefreshToken newToken = refreshTokenService.rotateRefreshToken(refreshToken);
        User user = newToken.getUser();
        String newAccessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());
        return buildAuthResponse(user, newAccessToken,newToken.getToken());
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.revokeRefreshToken(refreshToken);
    }

    private AuthResult buildAuthResponse(User user, String accessToken, String refreshToken) {
        AuthResponse response = AuthResponse.builder()
                .accessToken(accessToken)
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
        return AuthResult.builder().authResponse(response).refreshToken(refreshToken).build();
    }

}

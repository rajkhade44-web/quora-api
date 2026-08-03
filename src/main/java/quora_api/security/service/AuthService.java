package quora_api.security.service;

import quora_api.security.dto.AuthResponse;
import quora_api.security.dto.AuthResult;
import quora_api.security.dto.LoginRequest;
import quora_api.security.dto.RefreshTokenRequest;
import quora_api.security.dto.RegisterRequest;

public interface AuthService {
    AuthResult register(RegisterRequest request);

    AuthResult login(LoginRequest request);

    AuthResult refresh(String refreshToken);

    void logout(String refreshToken);
}

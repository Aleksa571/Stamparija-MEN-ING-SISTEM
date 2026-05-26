package rs.meningsistem.stamparija.service;

import rs.meningsistem.stamparija.dto.auth.AuthResponse;
import rs.meningsistem.stamparija.dto.auth.LoginRequest;
import rs.meningsistem.stamparija.dto.auth.RefreshTokenRequest;
import rs.meningsistem.stamparija.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
    void logout(String username);
}

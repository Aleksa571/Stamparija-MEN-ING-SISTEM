package rs.meningsistem.stamparija.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.meningsistem.stamparija.dto.auth.AuthResponse;
import rs.meningsistem.stamparija.dto.auth.LoginRequest;
import rs.meningsistem.stamparija.dto.auth.RefreshTokenRequest;
import rs.meningsistem.stamparija.dto.auth.RegisterRequest;
import rs.meningsistem.stamparija.dto.response.UserResponseDto;
import rs.meningsistem.stamparija.security.UserPrincipal;
import rs.meningsistem.stamparija.service.AuthService;
import rs.meningsistem.stamparija.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserPrincipal principal) {
        authService.logout(principal.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.findByUsername(principal.getUsername()));
    }
}

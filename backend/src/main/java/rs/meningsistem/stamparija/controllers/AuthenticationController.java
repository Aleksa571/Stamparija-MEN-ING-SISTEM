package rs.meningsistem.stamparija.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.meningsistem.stamparija.models.LoginResponseModel;
import rs.meningsistem.stamparija.models.LoginUserModel;
import rs.meningsistem.stamparija.models.RefreshTokenModel;
import rs.meningsistem.stamparija.models.RegisterUserModel;
import rs.meningsistem.stamparija.models.UserModel;
import rs.meningsistem.stamparija.security.UserPrincipal;
import rs.meningsistem.stamparija.services.IAuthenticationService;
import rs.meningsistem.stamparija.services.IUserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationService authenticationService;
    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponseModel> register(@Valid @RequestBody RegisterUserModel model) {
        return ResponseEntity.ok(authenticationService.signup(model));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseModel> login(@Valid @RequestBody LoginUserModel model) {
        return ResponseEntity.ok(authenticationService.authenticate(model));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseModel> refresh(@Valid @RequestBody RefreshTokenModel model) {
        return ResponseEntity.ok(authenticationService.refresh(model));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserPrincipal principal) {
        authenticationService.logout(principal.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserModel> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.findByUsername(principal.getUsername()));
    }
}

package rs.meningsistem.stamparija.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.dto.auth.AuthResponse;
import rs.meningsistem.stamparija.dto.auth.LoginRequest;
import rs.meningsistem.stamparija.dto.auth.RefreshTokenRequest;
import rs.meningsistem.stamparija.dto.auth.RegisterRequest;
import rs.meningsistem.stamparija.exception.ConflictException;
import rs.meningsistem.stamparija.exception.ResourceNotFoundException;
import rs.meningsistem.stamparija.model.RefreshToken;
import rs.meningsistem.stamparija.model.Role;
import rs.meningsistem.stamparija.model.User;
import rs.meningsistem.stamparija.model.enums.RoleName;
import rs.meningsistem.stamparija.repository.RoleRepository;
import rs.meningsistem.stamparija.repository.UserRepository;
import rs.meningsistem.stamparija.security.UserPrincipal;
import rs.meningsistem.stamparija.security.jwt.JwtTokenProvider;
import rs.meningsistem.stamparija.service.AuthService;
import rs.meningsistem.stamparija.service.RefreshTokenService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Korisnicko ime '" + request.getUsername() + "' je vec zauzeto");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email '" + request.getEmail() + "' je vec zauzet");
        }
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_USER ne postoji"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .roles(roles)
                .build();
        userRepository.save(user);
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "id", principal.getId()));
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken token = refreshTokenService.findByToken(request.getRefreshToken());
        refreshTokenService.verifyExpiration(token);
        return buildAuthResponse(token.getUser());
    }

    @Override
    public void logout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "username", username));
        refreshTokenService.deleteByUser(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        List<String> roleNames = user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toList());
        String accessToken = tokenProvider.generateAccessToken(user.getUsername(), roleNames);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(new HashSet<>(roleNames))
                .build();
    }
}

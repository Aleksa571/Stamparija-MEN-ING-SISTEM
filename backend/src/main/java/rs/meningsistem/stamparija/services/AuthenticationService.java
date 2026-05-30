package rs.meningsistem.stamparija.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.entities.RefreshToken;
import rs.meningsistem.stamparija.entities.Role;
import rs.meningsistem.stamparija.entities.User;
import rs.meningsistem.stamparija.entities.enums.RoleName;
import rs.meningsistem.stamparija.exceptions.ResourceNotFoundException;
import rs.meningsistem.stamparija.exceptions.user.UserAlreadyExistException;
import rs.meningsistem.stamparija.mappers.UserMapper;
import rs.meningsistem.stamparija.models.LoginResponseModel;
import rs.meningsistem.stamparija.models.LoginUserModel;
import rs.meningsistem.stamparija.models.RefreshTokenModel;
import rs.meningsistem.stamparija.models.RegisterUserModel;
import rs.meningsistem.stamparija.repositories.IRoleRepository;
import rs.meningsistem.stamparija.repositories.IUserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService implements IAuthenticationService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final IRefreshTokenService refreshTokenService;

    @Override
    public LoginResponseModel signup(RegisterUserModel model) {
        if (userRepository.existsByUsername(model.getUsername())) {
            throw new UserAlreadyExistException("Korisnicko ime '" + model.getUsername() + "' je vec zauzeto");
        }
        if (userRepository.existsByEmail(model.getEmail())) {
            throw new UserAlreadyExistException("Email '" + model.getEmail() + "' je vec zauzet");
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_USER ne postoji"));

        var user = UserMapper.toEntity(model, passwordEncoder);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        var saved = userRepository.save(user);
        return buildLoginResponse(saved);
    }

    @Override
    public LoginResponseModel authenticate(LoginUserModel model) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(model.getUsername(), model.getPassword())
        );
        var user = userRepository.findByUsername(model.getUsername())
                .or(() -> userRepository.findByEmail(model.getUsername()))
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "username", model.getUsername()));
        return buildLoginResponse(user);
    }

    @Override
    public LoginResponseModel refresh(RefreshTokenModel model) {
        RefreshToken token = refreshTokenService.findByToken(model.getRefreshToken());
        refreshTokenService.verifyExpiration(token);
        return buildLoginResponse(token.getUser());
    }

    @Override
    public void logout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "username", username));
        refreshTokenService.deleteByUser(user);
    }

    private LoginResponseModel buildLoginResponse(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toList());
        String accessToken = jwtService.generateToken(user.getUsername(), roleNames);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return LoginResponseModel.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(new HashSet<>(roleNames))
                .build();
    }
}

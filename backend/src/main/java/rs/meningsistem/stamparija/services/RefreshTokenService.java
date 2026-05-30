package rs.meningsistem.stamparija.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.entities.RefreshToken;
import rs.meningsistem.stamparija.entities.User;
import rs.meningsistem.stamparija.exceptions.BadRequestException;
import rs.meningsistem.stamparija.exceptions.ResourceNotFoundException;
import rs.meningsistem.stamparija.repositories.IRefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService implements IRefreshTokenService {

    private final IRefreshTokenRepository refreshTokenRepository;

    @Value("${security.jwt.refresh-expiration-time}")
    private long refreshExpirationMs;

    @Override
    public RefreshToken createRefreshToken(User user) {
        Optional<RefreshToken> existing = refreshTokenRepository.findByUser(user);
        existing.ifPresent(refreshTokenRepository::delete);
        refreshTokenRepository.flush();
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
                .build();
        return refreshTokenRepository.save(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new BadRequestException("Refresh token je istekao. Molimo prijavite se ponovo.");
        }
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token", "vrednost", "(skriveno)"));
    }

    @Override
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}

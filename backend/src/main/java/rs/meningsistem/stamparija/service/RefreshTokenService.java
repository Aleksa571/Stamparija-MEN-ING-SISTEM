package rs.meningsistem.stamparija.service;

import rs.meningsistem.stamparija.model.RefreshToken;
import rs.meningsistem.stamparija.model.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyExpiration(RefreshToken token);
    RefreshToken findByToken(String token);
    void deleteByUser(User user);
}

package rs.meningsistem.stamparija.services;

import rs.meningsistem.stamparija.entities.RefreshToken;
import rs.meningsistem.stamparija.entities.User;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyExpiration(RefreshToken token);
    RefreshToken findByToken(String token);
    void deleteByUser(User user);
}

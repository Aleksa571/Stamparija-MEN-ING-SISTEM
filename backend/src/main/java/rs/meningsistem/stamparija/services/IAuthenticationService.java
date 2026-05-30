package rs.meningsistem.stamparija.services;

import rs.meningsistem.stamparija.models.LoginResponseModel;
import rs.meningsistem.stamparija.models.LoginUserModel;
import rs.meningsistem.stamparija.models.RefreshTokenModel;
import rs.meningsistem.stamparija.models.RegisterUserModel;

public interface IAuthenticationService {
    LoginResponseModel signup(RegisterUserModel model);
    LoginResponseModel authenticate(LoginUserModel model);
    LoginResponseModel refresh(RefreshTokenModel model);
    void logout(String username);
}

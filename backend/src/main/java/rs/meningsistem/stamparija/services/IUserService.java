package rs.meningsistem.stamparija.services;

import rs.meningsistem.stamparija.models.UserModel;

import java.util.List;

public interface IUserService {
    List<UserModel> findAll();
    UserModel findById(Long id);
    UserModel findByUsername(String username);
    UserModel setEnabled(Long id, boolean enabled);
    void delete(Long id);
}

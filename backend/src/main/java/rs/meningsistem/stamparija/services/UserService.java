package rs.meningsistem.stamparija.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.entities.User;
import rs.meningsistem.stamparija.exceptions.ResourceNotFoundException;
import rs.meningsistem.stamparija.mappers.UserMapper;
import rs.meningsistem.stamparija.models.UserModel;
import rs.meningsistem.stamparija.repositories.IUserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserModel> findAll() {
        return UserMapper.toModelList(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UserModel findById(Long id) {
        return UserMapper.toModel(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public UserModel findByUsername(String username) {
        return UserMapper.toModel(userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "username", username)));
    }

    @Override
    public UserModel setEnabled(Long id, boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "id", id));
        user.setEnabled(enabled);
        return UserMapper.toModel(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "id", id));
        userRepository.delete(user);
    }
}

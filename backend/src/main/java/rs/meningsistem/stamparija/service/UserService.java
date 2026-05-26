package rs.meningsistem.stamparija.service;

import rs.meningsistem.stamparija.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();
    UserResponseDto findById(Long id);
    UserResponseDto findByUsername(String username);
    UserResponseDto setEnabled(Long id, boolean enabled);
    void delete(Long id);
}

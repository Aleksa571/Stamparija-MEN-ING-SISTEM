package rs.meningsistem.stamparija.mappers;

import org.springframework.security.crypto.password.PasswordEncoder;
import rs.meningsistem.stamparija.entities.User;
import rs.meningsistem.stamparija.models.RegisterUserModel;
import rs.meningsistem.stamparija.models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {}

    public static User toEntity(RegisterUserModel model, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(model.getUsername())
                .email(model.getEmail())
                .password(passwordEncoder.encode(model.getPassword()))
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .phone(model.getPhone())
                .address(model.getAddress())
                .build();
    }

    public static UserModel toModel(User entity) {
        return UserModel.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .createdAt(entity.getCreatedAt())
                .enabled(entity.getEnabled())
                .roles(entity.getRoles().stream()
                        .map(r -> r.getName().name())
                        .collect(Collectors.toSet()))
                .build();
    }

    public static List<UserModel> toModelList(List<User> entities) {
        var list = new ArrayList<UserModel>();
        for (var entity : entities) {
            list.add(toModel(entity));
        }
        return list;
    }
}

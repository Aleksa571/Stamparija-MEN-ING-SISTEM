package rs.meningsistem.stamparija.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserModel {

    @NotBlank(message = "Korisnicko ime je obavezno")
    private String username;

    @NotBlank(message = "Lozinka je obavezna")
    private String password;
}

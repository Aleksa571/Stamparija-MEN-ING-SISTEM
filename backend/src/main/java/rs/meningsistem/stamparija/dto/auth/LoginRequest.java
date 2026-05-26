package rs.meningsistem.stamparija.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Korisnicko ime je obavezno")
    private String username;

    @NotBlank(message = "Lozinka je obavezna")
    private String password;
}

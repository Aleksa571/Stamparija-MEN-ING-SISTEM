package rs.meningsistem.stamparija.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import rs.meningsistem.stamparija.validators.ContactNumberConstraint;

@Data
public class RegisterUserModel {

    @NotBlank(message = "Korisnicko ime je obavezno")
    @Size(min = 3, max = 50, message = "Korisnicko ime mora imati 3-50 karaktera")
    private String username;

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Email nije validan")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Lozinka je obavezna")
    @Size(min = 6, max = 100, message = "Lozinka mora imati najmanje 6 karaktera")
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @ContactNumberConstraint
    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String address;
}

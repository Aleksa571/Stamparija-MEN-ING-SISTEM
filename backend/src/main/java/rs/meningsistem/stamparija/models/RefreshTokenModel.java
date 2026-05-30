package rs.meningsistem.stamparija.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenModel {

    @NotBlank(message = "Refresh token je obavezan")
    private String refreshToken;
}

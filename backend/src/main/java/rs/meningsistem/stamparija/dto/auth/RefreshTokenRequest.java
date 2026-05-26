package rs.meningsistem.stamparija.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token je obavezan")
    private String refreshToken;
}

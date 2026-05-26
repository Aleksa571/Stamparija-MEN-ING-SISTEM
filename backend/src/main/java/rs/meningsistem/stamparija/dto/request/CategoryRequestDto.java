package rs.meningsistem.stamparija.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {

    @NotBlank(message = "Naziv kategorije je obavezan")
    @Size(max = 100, message = "Naziv ne sme biti duzi od 100 karaktera")
    private String name;

    @Size(max = 500, message = "Opis ne sme biti duzi od 500 karaktera")
    private String description;

    @Size(max = 500)
    private String imageUrl;
}

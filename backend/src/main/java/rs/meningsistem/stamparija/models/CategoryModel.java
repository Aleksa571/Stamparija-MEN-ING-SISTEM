package rs.meningsistem.stamparija.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {
    private Long id;

    @NotBlank(message = "Naziv kategorije je obavezan")
    @Size(max = 100, message = "Naziv ne sme biti duzi od 100 karaktera")
    private String name;

    @Size(max = 500, message = "Opis ne sme biti duzi od 500 karaktera")
    private String description;

    @Size(max = 500)
    private String imageUrl;

    private Integer productCount;
}

package rs.meningsistem.stamparija.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {

    @NotBlank(message = "Naziv proizvoda je obavezan")
    @Size(max = 150)
    private String name;

    @Size(max = 2000)
    private String description;

    @NotNull(message = "Cena je obavezna")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cena mora biti veca od 0")
    private BigDecimal price;

    @Size(max = 100)
    private String dimensions;

    @Size(max = 500)
    private String imageUrl;

    @NotNull(message = "Stanje na zalihama je obavezno")
    @Min(value = 0, message = "Stanje ne moze biti negativno")
    private Integer stock;

    private Boolean available = true;

    @NotNull(message = "Kategorija je obavezna")
    private Long categoryId;
}

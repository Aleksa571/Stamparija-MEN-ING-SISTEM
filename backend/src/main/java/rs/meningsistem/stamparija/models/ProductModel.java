package rs.meningsistem.stamparija.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    private Long id;

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

    private Boolean available;

    private LocalDateTime createdAt;

    @NotNull(message = "Kategorija je obavezna")
    private Long categoryId;

    private String categoryName;
}

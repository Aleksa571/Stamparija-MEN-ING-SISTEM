package rs.meningsistem.stamparija.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequestDto {

    @NotNull(message = "Proizvod je obavezan")
    private Long productId;

    @NotNull(message = "Kolicina je obavezna")
    @Min(value = 1, message = "Kolicina mora biti najmanje 1")
    private Integer quantity;
}

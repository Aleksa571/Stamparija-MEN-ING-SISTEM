package rs.meningsistem.stamparija.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemModel {
    private Long id;

    @NotNull(message = "Proizvod je obavezan")
    private Long productId;

    private String productName;
    private String productImageUrl;

    @NotNull(message = "Kolicina je obavezna")
    @Min(value = 1, message = "Kolicina mora biti najmanje 1")
    private Integer quantity;

    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}

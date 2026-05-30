package rs.meningsistem.stamparija.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.meningsistem.stamparija.entities.enums.DeliveryType;
import rs.meningsistem.stamparija.entities.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus status;

    @NotNull(message = "Nacin dostave je obavezan")
    private DeliveryType deliveryType;

    @Size(max = 500)
    private String deliveryAddress;

    @Size(max = 20)
    private String contactPhone;

    @Size(max = 1000)
    private String note;

    private BigDecimal totalPrice;
    private Long userId;
    private String username;
    private String userFullName;

    @NotEmpty(message = "Porudzbina mora imati barem jedan proizvod")
    @Valid
    private List<OrderItemModel> items;
}

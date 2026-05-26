package rs.meningsistem.stamparija.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.meningsistem.stamparija.model.enums.DeliveryType;
import rs.meningsistem.stamparija.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private DeliveryType deliveryType;
    private String deliveryAddress;
    private String contactPhone;
    private String note;
    private BigDecimal totalPrice;
    private Long userId;
    private String username;
    private String userFullName;
    private List<OrderItemResponseDto> items;
}

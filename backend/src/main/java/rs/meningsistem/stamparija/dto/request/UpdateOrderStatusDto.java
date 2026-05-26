package rs.meningsistem.stamparija.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import rs.meningsistem.stamparija.model.enums.OrderStatus;

@Data
public class UpdateOrderStatusDto {

    @NotNull(message = "Status je obavezan")
    private OrderStatus status;
}

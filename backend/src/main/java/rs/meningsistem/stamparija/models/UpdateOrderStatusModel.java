package rs.meningsistem.stamparija.models;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import rs.meningsistem.stamparija.entities.enums.OrderStatus;

@Data
public class UpdateOrderStatusModel {

    @NotNull(message = "Status je obavezan")
    private OrderStatus status;
}

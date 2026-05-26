package rs.meningsistem.stamparija.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import rs.meningsistem.stamparija.model.enums.DeliveryType;

import java.util.List;

@Data
public class OrderRequestDto {

    @NotNull(message = "Nacin dostave je obavezan")
    private DeliveryType deliveryType;

    @Size(max = 500)
    private String deliveryAddress;

    @Size(max = 20)
    private String contactPhone;

    @Size(max = 1000)
    private String note;

    @NotEmpty(message = "Porudzbina mora imati barem jedan proizvod")
    @Valid
    private List<OrderItemRequestDto> items;
}

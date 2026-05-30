package rs.meningsistem.stamparija.mappers;

import rs.meningsistem.stamparija.entities.OrderItem;
import rs.meningsistem.stamparija.models.OrderItemModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderItemMapper {

    private OrderItemMapper() {}

    public static OrderItemModel toModel(OrderItem entity) {
        var product = entity.getProduct();
        BigDecimal total = entity.getUnitPrice().multiply(BigDecimal.valueOf(entity.getQuantity()));
        return OrderItemModel.builder()
                .id(entity.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productImageUrl(product.getImageUrl())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .totalPrice(total)
                .build();
    }

    public static List<OrderItemModel> toModelList(List<OrderItem> entities) {
        var list = new ArrayList<OrderItemModel>();
        for (var entity : entities) {
            list.add(toModel(entity));
        }
        return list;
    }
}

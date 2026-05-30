package rs.meningsistem.stamparija.mappers;

import rs.meningsistem.stamparija.entities.Order;
import rs.meningsistem.stamparija.models.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    private OrderMapper() {}

    public static OrderModel toModel(Order entity) {
        var user = entity.getUser();
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        String fullName = (firstName + " " + lastName).trim();

        return OrderModel.builder()
                .id(entity.getId())
                .orderDate(entity.getOrderDate())
                .status(entity.getStatus())
                .deliveryType(entity.getDeliveryType())
                .deliveryAddress(entity.getDeliveryAddress())
                .contactPhone(entity.getContactPhone())
                .note(entity.getNote())
                .totalPrice(entity.getTotalPrice())
                .userId(user.getId())
                .username(user.getUsername())
                .userFullName(fullName)
                .items(OrderItemMapper.toModelList(entity.getItems()))
                .build();
    }

    public static List<OrderModel> toModelList(List<Order> entities) {
        var list = new ArrayList<OrderModel>();
        for (var entity : entities) {
            list.add(toModel(entity));
        }
        return list;
    }
}

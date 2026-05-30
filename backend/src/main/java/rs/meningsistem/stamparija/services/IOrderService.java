package rs.meningsistem.stamparija.services;

import rs.meningsistem.stamparija.models.OrderModel;
import rs.meningsistem.stamparija.models.UpdateOrderStatusModel;

import java.util.List;

public interface IOrderService {
    OrderModel create(String username, OrderModel model);
    List<OrderModel> findMyOrders(String username);
    List<OrderModel> findAll();
    OrderModel findById(Long id);
    OrderModel updateStatus(Long id, UpdateOrderStatusModel model);
    void delete(Long id);
}

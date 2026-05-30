package rs.meningsistem.stamparija.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.entities.Order;
import rs.meningsistem.stamparija.entities.OrderItem;
import rs.meningsistem.stamparija.entities.Product;
import rs.meningsistem.stamparija.entities.User;
import rs.meningsistem.stamparija.exceptions.BadRequestException;
import rs.meningsistem.stamparija.exceptions.ResourceNotFoundException;
import rs.meningsistem.stamparija.mappers.OrderMapper;
import rs.meningsistem.stamparija.models.OrderItemModel;
import rs.meningsistem.stamparija.models.OrderModel;
import rs.meningsistem.stamparija.models.UpdateOrderStatusModel;
import rs.meningsistem.stamparija.repositories.IOrderRepository;
import rs.meningsistem.stamparija.repositories.IProductRepository;
import rs.meningsistem.stamparija.repositories.IUserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;

    @Override
    public OrderModel create(String username, OrderModel model) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "username", username));

        Order order = Order.builder()
                .user(user)
                .deliveryType(model.getDeliveryType())
                .deliveryAddress(model.getDeliveryAddress())
                .contactPhone(model.getContactPhone())
                .note(model.getNote())
                .totalPrice(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemModel itemModel : model.getItems()) {
            Product product = productRepository.findById(itemModel.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proizvod", "id", itemModel.getProductId()));
            if (Boolean.FALSE.equals(product.getAvailable())) {
                throw new BadRequestException("Proizvod '" + product.getName() + "' nije dostupan");
            }
            if (product.getStock() < itemModel.getQuantity()) {
                throw new BadRequestException("Nedovoljna kolicina na zalihama za proizvod '" + product.getName() +
                        "'. Trenutno: " + product.getStock());
            }
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemModel.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();
            order.getItems().add(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemModel.getQuantity())));
            product.setStock(product.getStock() - itemModel.getQuantity());
        }
        order.setTotalPrice(total);
        return OrderMapper.toModel(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderModel> findMyOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "username", username));
        return OrderMapper.toModelList(orderRepository.findByUserIdOrderByOrderDateDesc(user.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderModel> findAll() {
        return OrderMapper.toModelList(orderRepository.findAllByOrderByOrderDateDesc());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderModel findById(Long id) {
        return OrderMapper.toModel(orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Porudzbina", "id", id)));
    }

    @Override
    public OrderModel updateStatus(Long id, UpdateOrderStatusModel model) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Porudzbina", "id", id));
        order.setStatus(model.getStatus());
        return OrderMapper.toModel(orderRepository.save(order));
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Porudzbina", "id", id));
        orderRepository.delete(order);
    }
}

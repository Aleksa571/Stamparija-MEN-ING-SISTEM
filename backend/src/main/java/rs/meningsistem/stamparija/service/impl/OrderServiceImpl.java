package rs.meningsistem.stamparija.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.meningsistem.stamparija.dto.request.OrderItemRequestDto;
import rs.meningsistem.stamparija.dto.request.OrderRequestDto;
import rs.meningsistem.stamparija.dto.request.UpdateOrderStatusDto;
import rs.meningsistem.stamparija.dto.response.OrderItemResponseDto;
import rs.meningsistem.stamparija.dto.response.OrderResponseDto;
import rs.meningsistem.stamparija.exception.BadRequestException;
import rs.meningsistem.stamparija.exception.ResourceNotFoundException;
import rs.meningsistem.stamparija.model.*;
import rs.meningsistem.stamparija.repository.OrderRepository;
import rs.meningsistem.stamparija.repository.ProductRepository;
import rs.meningsistem.stamparija.repository.UserRepository;
import rs.meningsistem.stamparija.service.OrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderResponseDto create(String username, OrderRequestDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "username", username));

        Order order = Order.builder()
                .user(user)
                .deliveryType(dto.getDeliveryType())
                .deliveryAddress(dto.getDeliveryAddress())
                .contactPhone(dto.getContactPhone())
                .note(dto.getNote())
                .totalPrice(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequestDto itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proizvod", "id", itemDto.getProductId()));
            if (Boolean.FALSE.equals(product.getAvailable())) {
                throw new BadRequestException("Proizvod '" + product.getName() + "' nije dostupan");
            }
            if (product.getStock() < itemDto.getQuantity()) {
                throw new BadRequestException("Nedovoljna kolicina na zalihama za proizvod '" + product.getName() + "'. Trenutno: " + product.getStock());
            }
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();
            order.getItems().add(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
            product.setStock(product.getStock() - itemDto.getQuantity());
        }
        order.setTotalPrice(total);
        return toDto(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> findMyOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik", "username", username));
        return orderRepository.findByUserIdOrderByOrderDateDesc(user.getId()).stream()
                .map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> findAll() {
        return orderRepository.findAllByOrderByOrderDateDesc().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto findById(Long id) {
        return toDto(orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Porudzbina", "id", id)));
    }

    @Override
    public OrderResponseDto updateStatus(Long id, UpdateOrderStatusDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Porudzbina", "id", id));
        order.setStatus(dto.getStatus());
        return toDto(orderRepository.save(order));
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Porudzbina", "id", id));
        orderRepository.delete(order);
    }

    private OrderResponseDto toDto(Order o) {
        List<OrderItemResponseDto> items = o.getItems().stream().map(i ->
                OrderItemResponseDto.builder()
                        .id(i.getId())
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .productImageUrl(i.getProduct().getImageUrl())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .totalPrice(i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                        .build()
        ).collect(Collectors.toList());

        String fullName = (o.getUser().getFirstName() != null ? o.getUser().getFirstName() : "") + " " +
                (o.getUser().getLastName() != null ? o.getUser().getLastName() : "");
        return OrderResponseDto.builder()
                .id(o.getId())
                .orderDate(o.getOrderDate())
                .status(o.getStatus())
                .deliveryType(o.getDeliveryType())
                .deliveryAddress(o.getDeliveryAddress())
                .contactPhone(o.getContactPhone())
                .note(o.getNote())
                .totalPrice(o.getTotalPrice())
                .userId(o.getUser().getId())
                .username(o.getUser().getUsername())
                .userFullName(fullName.trim())
                .items(items)
                .build();
    }
}

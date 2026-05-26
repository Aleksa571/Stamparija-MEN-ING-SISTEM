package rs.meningsistem.stamparija.service;

import rs.meningsistem.stamparija.dto.request.OrderRequestDto;
import rs.meningsistem.stamparija.dto.request.UpdateOrderStatusDto;
import rs.meningsistem.stamparija.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto create(String username, OrderRequestDto dto);
    List<OrderResponseDto> findMyOrders(String username);
    List<OrderResponseDto> findAll();
    OrderResponseDto findById(Long id);
    OrderResponseDto updateStatus(Long id, UpdateOrderStatusDto dto);
    void delete(Long id);
}

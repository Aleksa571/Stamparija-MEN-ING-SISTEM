package rs.meningsistem.stamparija.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.meningsistem.stamparija.dto.request.OrderRequestDto;
import rs.meningsistem.stamparija.dto.request.UpdateOrderStatusDto;
import rs.meningsistem.stamparija.dto.response.OrderResponseDto;
import rs.meningsistem.stamparija.security.UserPrincipal;
import rs.meningsistem.stamparija.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDto> create(@AuthenticationPrincipal UserPrincipal principal,
                                                   @Valid @RequestBody OrderRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(principal.getUsername(), dto));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderResponseDto>> myOrders(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(orderService.findMyOrders(principal.getUsername()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDto> findById(@AuthenticationPrincipal UserPrincipal principal,
                                                     @PathVariable Long id) {
        OrderResponseDto order = orderService.findById(id);
        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !order.getUserId().equals(principal.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateStatus(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateOrderStatusDto dto) {
        return ResponseEntity.ok(orderService.updateStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package rs.meningsistem.stamparija.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.meningsistem.stamparija.constants.RoleConstants;
import rs.meningsistem.stamparija.models.OrderModel;
import rs.meningsistem.stamparija.models.UpdateOrderStatusModel;
import rs.meningsistem.stamparija.security.UserPrincipal;
import rs.meningsistem.stamparija.services.IOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderModel> create(@AuthenticationPrincipal UserPrincipal principal,
                                             @Valid @RequestBody OrderModel model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(principal.getUsername(), model));
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderModel>> myOrders(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(orderService.findMyOrders(principal.getUsername()));
    }

    @GetMapping
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<List<OrderModel>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderModel> findById(@AuthenticationPrincipal UserPrincipal principal,
                                               @PathVariable Long id) {
        OrderModel order = orderService.findById(id);
        boolean isAdmin = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleConstants.ROLE_ADMIN));
        if (!isAdmin && !order.getUserId().equals(principal.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<OrderModel> updateStatus(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateOrderStatusModel model) {
        return ResponseEntity.ok(orderService.updateStatus(id, model));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('" + RoleConstants.ADMIN + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

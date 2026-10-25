package com.example.ss_authorization_exercise.web;

import com.example.ss_authorization_exercise.model.Order;
import com.example.ss_authorization_exercise.service.OrderService;
import com.example.ss_authorization_exercise.web.dto.CreateOrderRequest;
import com.example.ss_authorization_exercise.web.dto.UpdateStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final String ANONYMOUS_FALLBACK_OWNER = "guest";

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request, Principal principal) {
        String owner = principal != null ? principal.getName() : ANONYMOUS_FALLBACK_OWNER;
        Order created = orderService.createOrder(owner, request.productId(), request.quantity());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelAnyOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<Void> refundOrder(@PathVariable Long id) {
        orderService.refundOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
        orderService.updateOrderStatus(id, request.status());
        return ResponseEntity.noContent().build();
    }
}

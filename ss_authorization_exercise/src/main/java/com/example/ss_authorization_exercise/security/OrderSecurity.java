package com.example.ss_authorization_exercise.security;

import com.example.ss_authorization_exercise.model.Order;
import com.example.ss_authorization_exercise.model.OrderReport;
import com.example.ss_authorization_exercise.repository.OrderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderSecurity {

    private final OrderRepository orderRepository;

    public OrderSecurity(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean isOwner(Long orderId) {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        Optional<Order> o = orderRepository.findById(orderId);
        // returns true if the authenticated user owns the order
        return o.map(c ->
                        c.ownerUsername()
                        .equals(a.getName()))
                .orElse(false);
    }

    public boolean canUpdateStatus(String status) {
        List<String> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream()
                        .map(a -> a.getAuthority())
                        .toList();

        if (authorities.contains("ADMIN")) {
            return true;
        } else {
            if (authorities.contains("MANAGER")) {
                return status.equals("PROCESSING") || status.equals("SHIPPED");
            }
        }

        return false;
    }

}
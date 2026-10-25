package com.example.ss_authorization_exercise.repository;

import com.example.ss_authorization_exercise.model.Order;
import com.example.ss_authorization_exercise.model.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.UnaryOperator;


@Repository
public class OrderRepository {

    private static final long FIRST_GENERATED_ID = 1001L;

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(FIRST_GENERATED_ID);

    public OrderRepository() {
        seed(new Order(101L, "alice", 1L, 1, OrderStatus.CREATED, false));
        seed(new Order(102L, "bob", 2L, 2, OrderStatus.CREATED, false));
        seed(new Order(103L, "alice", 3L, 3, OrderStatus.PROCESSING, false));
    }

    public List<Order> findAll() {
        return orders.values().stream()
                .sorted(Comparator.comparing(Order::id))
                .toList();
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(id).map(orders::get);
    }

    public Order create(String ownerUsername, Long productId, int quantity) {
        long id = nextId.getAndIncrement();
        Order order = new Order(id, ownerUsername, productId, quantity, OrderStatus.CREATED, false);
        orders.put(id, order);
        return order;
    }

    public Optional<Order> update(Long id, UnaryOperator<Order> change) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(orders.computeIfPresent(id, (key, existing) -> change.apply(existing)));
    }

    private void seed(Order order) {
        orders.put(order.id(), order);
    }
}

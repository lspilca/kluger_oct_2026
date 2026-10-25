package com.example.ss_authorization_exercise.repository;

import com.example.ss_authorization_exercise.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepository {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();

    public ProductRepository() {
        add(new Product(1L, "Laptop", new BigDecimal("1299.00")));
        add(new Product(2L, "Headphones", new BigDecimal("199.99")));
        add(new Product(3L, "USB-C Cable", new BigDecimal("12.50")));
    }

    public List<Product> findAll() {
        return products.values().stream()
                .sorted(Comparator.comparing(Product::id))
                .toList();
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(id).map(products::get);
    }

    private void add(Product product) {
        products.put(product.id(), product);
    }
}

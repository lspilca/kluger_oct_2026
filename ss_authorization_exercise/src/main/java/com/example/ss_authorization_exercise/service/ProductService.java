package com.example.ss_authorization_exercise.service;

import com.example.ss_authorization_exercise.model.Product;
import com.example.ss_authorization_exercise.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }
}

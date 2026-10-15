package com.shop.service;

import com.shop.model.Product;
import com.shop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getPaginatedProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public void addProduct(String name, String description, double price, int quantity) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(BigDecimal.valueOf(price));
        p.setQuantity(quantity);
        productRepository.save(p);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
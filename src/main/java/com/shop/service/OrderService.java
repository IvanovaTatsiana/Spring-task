package com.shop.service;

import com.shop.model.Order;
import com.shop.model.Product;
import com.shop.model.User;
import com.shop.repository.OrderRepository;
import com.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void createOrder(User user, Long productId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (qty > product.getQuantity()) throw new IllegalArgumentException("Insufficient stock");

        BigDecimal totalCost = product.getPrice().multiply(BigDecimal.valueOf(qty));
        product.setQuantity(product.getQuantity() - qty);

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setQuantity(qty);
        order.setTotalPrice(totalCost);
        order.setStatus("CREATED");

        orderRepository.save(order);
    }

    public List<Order> findUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getUser().getId().equals(userId) || !"CREATED".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot cancel this order");
        }

        order.setStatus("CANCELLED");
        Product product = order.getProduct();
        product.setQuantity(product.getQuantity() + order.getQuantity());
    }
}
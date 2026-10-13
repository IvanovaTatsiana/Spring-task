package com.shop.controller;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderRoutingController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderRoutingController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String viewOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("orders", orderService.findUserOrders(user.getId()));
        return "orders";
    }

    @PostMapping("/create")
    public String create(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam("productId") Long productId,
                         @RequestParam("quantity") int quantity) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        try {
            orderService.createOrder(user, productId, quantity);
        } catch (Exception e) {
            return "redirect:/orders?error=" + e.getMessage();
        }
        return "redirect:/orders";
    }

    @PostMapping("/cancel")
    public String cancel(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam("id") Long id) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        orderService.cancelOrder(id, user.getId());
        return "redirect:/orders";
    }
}
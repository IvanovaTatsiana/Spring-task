package com.shop.controller;

import com.shop.model.Product;
import com.shop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@Controller
@RequestMapping("/products")
public class CatalogController {
    private final ProductRepository productRepository;
    private static final int LIMIT = 5;

    public CatalogController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String catalog(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        if (page < 1) page = 1;
        Page<Product> productPage = productRepository.findAll(PageRequest.of(page - 1, LIMIT));

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "products";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("price") double price,
                             @RequestParam("quantity") int quantity) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(BigDecimal.valueOf(price));
        p.setQuantity(quantity);
        productRepository.save(p);
        return "redirect:/products";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("id") Long id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }
}
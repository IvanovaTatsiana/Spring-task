package com.shop.controller;

import com.shop.model.Product;
import com.shop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class CatalogController {
    private final ProductService productService;
    private static final int LIMIT = 5;

    public CatalogController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String catalog(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        if (page < 1) page = 1;
        Page<Product> productPage = productService.getPaginatedProducts(page - 1, LIMIT);

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
        productService.addProduct(name, description, price, quantity);
        return "redirect:/products";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
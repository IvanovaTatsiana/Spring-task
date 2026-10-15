package com.shop.controller;

import com.shop.model.User;
import com.shop.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileRoutingController {
    private final UserService userService;

    public ProfileRoutingController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("email") String email) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        userService.updateUserEmail(user.getId(), email);
        return "redirect:/profile?success";
    }
}
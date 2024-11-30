package org.example.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/test")
@EnableMethodSecurity
public class TestController {
    @GetMapping("/seller")
    public String testSeller() {
        return "Test Seller passed";
    }

    @GetMapping("/seller/auth")
    @PreAuthorize("hasRole('SELLER')")
    public String testSellerAuth() {
        return "Test Seller auth passed";
    }

    @GetMapping("/buyer")
    public String testBuyer() {
        return "Test Buyer passed";
    }

    @GetMapping("/buyer/auth")
    @PreAuthorize("hasRole('BUYER')")
    public String testBuyerAuth() {
        return "Test Buyer auth passed";
    }
}

package com.Shopify.e_commerce.controller;

import com.Shopify.e_commerce.entity.*;
import com.Shopify.e_commerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItem item, @RequestParam String email) {
        Cart cart = cartRepository.findByUserEmail(email);
        if (cart == null) {
            cart = new Cart();
            cart.setUserEmail(email);
        }

        cart.getItems().add(item);
        cartRepository.save(cart);

        return ResponseEntity.ok("Item added to cart!");
    }

    @GetMapping
    public ResponseEntity<?> getCart(@RequestParam String email) {
        Cart cart = cartRepository.findByUserEmail(email);
        if (cart == null) return ResponseEntity.ok("Cart is empty!");
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long itemId, @RequestParam String email) {
        Cart cart = cartRepository.findByUserEmail(email);
        if (cart == null) return ResponseEntity.badRequest().body("Cart not found!");

        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        cartRepository.save(cart);
        return ResponseEntity.ok("Item removed!");
    }
}

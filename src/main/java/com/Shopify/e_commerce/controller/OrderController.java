package com.Shopify.e_commerce.controller;

import com.Shopify.e_commerce.entity.*;
import com.Shopify.e_commerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    // Place Order from Cart
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestParam String email) {
        Cart cart = cartRepository.findByUserEmail(email);
        if (cart == null || cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("Cart is empty!");
        }

        Order order = new Order();
        order.setUserEmail(email);
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setTotalAmount(cart.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum());
        order.setStatus("CONFIRMED");

        orderRepository.save(order);
        cart.getItems().clear();
        cartRepository.save(cart);

        return ResponseEntity.ok("Order placed successfully!");
    }

    // Get All Orders for a User
    @GetMapping
    public ResponseEntity<?> getOrders(@RequestParam String email) {
        List<Order> orders = orderRepository.findByUserEmail(email);
        return ResponseEntity.ok(orders);
    }

    // Track Order Status
    @GetMapping("/track/{orderId}")
    public ResponseEntity<?> trackOrder(@PathVariable Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(o -> ResponseEntity.ok((Object) o))
                .orElseGet(() -> ResponseEntity.badRequest().body("Order not found!"));
    }

    // Update Order Status (Admin)
    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) return ResponseEntity.badRequest().body("Order not found!");

        Order order = orderOpt.get();
        order.setStatus(status.toUpperCase());
        orderRepository.save(order);

        return ResponseEntity.ok("Order status updated to " + status);
    }
}

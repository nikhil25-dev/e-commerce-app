package com.Shopify.e_commerce.repository;

import com.Shopify.e_commerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserEmail(String userEmail);
}

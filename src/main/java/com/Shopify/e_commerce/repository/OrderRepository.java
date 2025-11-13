package com.Shopify.e_commerce.repository;

import com.Shopify.e_commerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserEmail(String userEmail);
}

package com.gateway.repository;

import com.gateway.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
    boolean existsById(String id); // for unique order ID check
}


package com.gateway.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gateway.models.Order;
// import com.gateway.models.Merchant;
import java.util.UUID;


public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByIdAndMerchantId(String id, UUID merchantId);

}

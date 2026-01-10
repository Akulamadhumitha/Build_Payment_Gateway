package com.gateway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gateway.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    long countByStatus(String status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'success'")
    Long sumSuccessfulAmount();
}

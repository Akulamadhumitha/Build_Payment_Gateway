package com.gateway.controllers;

import com.gateway.repositories.PaymentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final PaymentRepository paymentRepository;

    public DashboardController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        long totalPayments = paymentRepository.count();
        long successPayments = paymentRepository.countByStatus("success");
        Long totalAmount = paymentRepository.sumSuccessfulAmount();

        if (totalAmount == null) totalAmount = 0L;

        int successRate = totalPayments == 0
                ? 0
                : (int) ((successPayments * 100) / totalPayments);

        Map<String, Object> response = new HashMap<>();
        response.put("totalTransactions", totalPayments);
        response.put("totalAmount", totalAmount);
        response.put("successRate", successRate);

        return response;
    }
}

package com.gateway.services;

import org.springframework.stereotype.Service;

import com.gateway.models.Order;
import com.gateway.models.Merchant;
import com.gateway.repositories.OrderRepository;
import com.gateway.repositories.MerchantRepository;
import com.gateway.exception.ResourceNotFoundException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MerchantRepository merchantRepository;

    public OrderService(OrderRepository orderRepository,
                        MerchantRepository merchantRepository) {
        this.orderRepository = orderRepository;
        this.merchantRepository = merchantRepository;
    }

    public Order getOrderByIdAndApiKey(String orderId, String apiKey) {

    Merchant merchant = merchantRepository.findByApiKey(apiKey)
            .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));

    return orderRepository
            .findByIdAndMerchantId(orderId, merchant.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
}

}

package com.gateway.controller;

import com.gateway.model.Merchant;
import com.gateway.model.Order;
import com.gateway.repository.MerchantRepository;
import com.gateway.repository.OrderRepository;
import com.gateway.dto.CreateOrderRequest;
import com.gateway.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final MerchantRepository merchantRepository;
    private final OrderRepository orderRepository;
    private final Random random = new Random();

    public OrderController(MerchantRepository merchantRepository, OrderRepository orderRepository){
        this.merchantRepository = merchantRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestHeader("X-Api-Key") String apiKey,
            @RequestHeader("X-Api-Secret") String apiSecret,
            @RequestBody CreateOrderRequest request
    ){
        // 1️⃣ Authenticate merchant
        Merchant merchant = merchantRepository.findByApiKey(apiKey)
                .orElse(null);

        if(merchant == null || !merchant.getApiSecret().equals(apiSecret)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("AUTHENTICATION_ERROR", "Invalid API credentials"));
        }

        // 2️⃣ Validate request
        if(request.getAmount() == null || request.getAmount() < 100){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("BAD_REQUEST_ERROR", "amount must be at least 100"));
        }

        if(request.getCurrency() == null) request.setCurrency("INR");

        // 3️⃣ Generate unique order ID
        String orderId;
        do {
            orderId = "order_" + generateRandomString(16);
        } while(orderRepository.existsById(orderId));

        // 4️⃣ Create order entity
        Order order = new Order();
        order.setId(orderId);
        order.setMerchantId(merchant.getId());
        order.setAmount(request.getAmount());
        order.setCurrency(request.getCurrency());
        order.setReceipt(request.getReceipt());
        order.setNotes(request.getNotes());
        order.setStatus("created");
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());

        orderRepository.save(order);

        // 5️⃣ Return response
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    private String generateRandomString(int length){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<length;i++){
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}

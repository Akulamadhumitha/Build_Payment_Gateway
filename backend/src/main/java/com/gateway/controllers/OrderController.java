package com.gateway.controllers;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.dto.CreateOrderRequest;
import com.gateway.dto.ErrorResponse;
import com.gateway.models.Merchant;
import com.gateway.models.Order;
import com.gateway.repositories.MerchantRepository;
import com.gateway.repositories.OrderRepository;
import com.gateway.services.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final MerchantRepository merchantRepository;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService,
                           MerchantRepository merchantRepository,
                           OrderRepository orderRepository) {
        this.orderService = orderService;
        this.merchantRepository = merchantRepository;
        this.orderRepository = orderRepository;
    }

    // =========================
    // POST /api/v1/orders
    // =========================
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestHeader("X-Api-Key") String apiKey,
            @RequestHeader("X-Api-Secret") String apiSecret,
            @RequestBody CreateOrderRequest request
    ) {
        // 1️⃣ Authenticate merchant
        Merchant merchant = merchantRepository.findByApiKey(apiKey).orElse(null);
        if (merchant == null || !merchant.getApiSecret().equals(apiSecret)) {
            return ResponseEntity.status(401)
                    .body(new ErrorResponse("Invalid API credentials"));
        }

        // 2️⃣ Validate request
        if (request.getAmount() == null || request.getAmount() < 100) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Amount must be at least 100"));
        }
        if (request.getCurrency() == null) request.setCurrency("INR");

        // 3️⃣ Generate unique order ID
        String orderId = "order_" + UUID.randomUUID().toString();

        // 4️⃣ Create order
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

        // 5️⃣ Return created order
        return ResponseEntity.status(201).body(order);
    }

    // =========================
    // GET /api/v1/orders/{order_id}
    // =========================
    @GetMapping("/{orderId}")
public ResponseEntity<?> getOrder(
        @RequestHeader("X-Api-Key") String apiKey,
        @RequestHeader("X-Api-Secret") String apiSecret,
        @PathVariable String orderId
) {
    // 1️⃣ Authenticate merchant
    Merchant merchant = merchantRepository.findByApiKey(apiKey).orElse(null);
    if (merchant == null || !merchant.getApiSecret().equals(apiSecret)) {
        return ResponseEntity.status(401)
                .body(new ErrorResponse("Invalid API credentials"));
    }

    // 2️⃣ Find order
    Order order = orderRepository.findById(orderId).orElse(null);
    if (order == null) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse("Order not found"));
    }

    // 3️⃣ Success
    return ResponseEntity.ok(order);
}
@CrossOrigin(origins = "http://localhost:3001") // allow frontend
    @GetMapping("/{orderId}/public")
    public ResponseEntity<?> getOrderPublic(@PathVariable String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", Map.of("message", "Order not found")));
        }

        Map<String, Object> response = Map.of(
            "id", order.getId(),
            "amount", order.getAmount(),
            "currency", order.getCurrency(),
            "status", order.getStatus()
        );

        return ResponseEntity.ok(response);
    }
}

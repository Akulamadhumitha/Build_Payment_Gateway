package com.gateway.controllers;

import com.gateway.dto.CreatePaymentRequest;
import com.gateway.dto.ErrorResponse;
import com.gateway.models.Merchant;
import com.gateway.models.Order;
import com.gateway.models.Payment;
import com.gateway.repositories.MerchantRepository;
import com.gateway.repositories.OrderRepository;
import com.gateway.repositories.PaymentRepository;
import com.gateway.utils.PaymentValidationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final MerchantRepository merchantRepo;
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;

    public PaymentController(MerchantRepository merchantRepo,
                             OrderRepository orderRepo,
                             PaymentRepository paymentRepo) {
        this.merchantRepo = merchantRepo;
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
    }

    @PostMapping
    public ResponseEntity<?> createPayment(
            @RequestHeader("X-Api-Key") String apiKey,
            @RequestHeader("X-Api-Secret") String apiSecret,
            @RequestBody CreatePaymentRequest request
    ) throws InterruptedException {

        // 🔐 Authenticate
        Merchant merchant = merchantRepo.findByApiKey(apiKey).orElse(null);
        if (merchant == null || !merchant.getApiSecret().equals(apiSecret)) {
            return ResponseEntity.status(401)
                    .body(new ErrorResponse("Invalid API credentials"));
        }

        // 📦 Validate order
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        if (order == null || !order.getMerchantId().equals(merchant.getId())) {
            return ResponseEntity.status(404)
                    .body(new ErrorResponse("Order not found"));
        }

        // 🆔 Create payment
        Payment payment = new Payment();
        payment.setId("pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        payment.setOrderId(order.getId());
        payment.setMerchantId(merchant.getId());
        payment.setAmount(order.getAmount());
        payment.setCurrency(order.getCurrency());
        payment.setMethod(request.getMethod());
        payment.setStatus("processing");
        payment.setCreatedAt(Instant.now());
        payment.setUpdatedAt(Instant.now());

        // 💳 Method validation
        if ("upi".equals(request.getMethod())) {
            if (!PaymentValidationUtil.isValidVpa(request.getVpa())) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Invalid VPA"));
            }
            payment.setVpa(request.getVpa());
        }
        else if ("card".equals(request.getMethod())) {
            var card = request.getCard();
            if (card == null ||
                !PaymentValidationUtil.isValidCardNumber(card.getNumber()) ||
                !PaymentValidationUtil.isValidExpiry(
                        card.getExpiryMonth(),
                        card.getExpiryYear()
                )) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Invalid card details"));
            }

            payment.setCardNetwork(
                    PaymentValidationUtil.detectNetwork(card.getNumber())
            );
            payment.setCardLast4(
                    card.getNumber().substring(card.getNumber().length() - 4)
            );
        }
        else {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Unsupported payment method"));
        }

        paymentRepo.save(payment);

        // ⏳ Processing delay
        boolean testMode = Boolean.parseBoolean(System.getenv("TEST_MODE"));
        int delay = testMode
                ? Integer.parseInt(System.getenv().getOrDefault("TEST_PROCESSING_DELAY", "1000"))
                : 5000 + (int)(Math.random() * 5000);

        Thread.sleep(delay);

        boolean success;
        if (testMode) {
            success = Boolean.parseBoolean(
                    System.getenv().getOrDefault("TEST_PAYMENT_SUCCESS", "true")
            );
        } else {
            success = "upi".equals(payment.getMethod())
                    ? PaymentValidationUtil.randomOutcome(0.9)
                    : PaymentValidationUtil.randomOutcome(0.95);
        }

        // ✅ Update status
        if (success) {
            payment.setStatus("success");
        } else {
            payment.setStatus("failed");
            payment.setErrorCode("PAYMENT_FAILED");
            payment.setErrorDescription("Bank declined the transaction");
        }

        payment.setUpdatedAt(Instant.now());
        paymentRepo.save(payment);

        return ResponseEntity.status(201).body(payment);
    }
    @GetMapping("/{paymentId}")
public ResponseEntity<?> getPayment(
        @RequestHeader("X-Api-Key") String apiKey,
        @RequestHeader("X-Api-Secret") String apiSecret,
        @PathVariable String paymentId
) {

    // 🔐 Authenticate merchant
    Merchant merchant = merchantRepo.findByApiKey(apiKey).orElse(null);
    if (merchant == null || !merchant.getApiSecret().equals(apiSecret)) {
        return ResponseEntity.status(401)
                .body(new ErrorResponse("Invalid API credentials"));
    }

    // 💳 Fetch payment
    Payment payment = paymentRepo.findById(paymentId).orElse(null);
    if (payment == null) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse("Payment not found"));
    }

    // 🔒 Authorization check
    if (!payment.getMerchantId().equals(merchant.getId())) {
        return ResponseEntity.status(403)
                .body(new ErrorResponse("Access denied"));
    }

    // ✅ Success
    return ResponseEntity.ok(payment);
}

}

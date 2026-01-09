package com.gateway.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.models.Merchant;
import com.gateway.repositories.MerchantRepository;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final MerchantRepository merchantRepo;

    // 🔒 Fixed test merchant ID (as per spec)
    private static final UUID TEST_MERCHANT_ID =
            UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    public TestController(MerchantRepository merchantRepo) {
        this.merchantRepo = merchantRepo;
    }

    @GetMapping("/merchant")
    public ResponseEntity<?> getTestMerchant() {

        Optional<Merchant> merchantOpt = merchantRepo.findById(TEST_MERCHANT_ID);

        if (merchantOpt.isEmpty()) {
            return ResponseEntity.status(404).body(
                    Map.of("error", Map.of("message", "Test merchant not found"))
            );
        }

        Merchant merchant = merchantOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("id", merchant.getId().toString());
        response.put("email", merchant.getEmail());
        response.put("api_key", merchant.getApiKey());
        response.put("seeded", true);

        return ResponseEntity.ok(response);
    }
}

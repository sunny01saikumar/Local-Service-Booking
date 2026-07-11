package com.sai.geoLocation.controller;

import com.sai.geoLocation.common.ApiResponse;
import com.sai.geoLocation.entity.Wallet;
import com.sai.geoLocation.entity.WalletTransaction;
import com.sai.geoLocation.entity.User;
import com.sai.geoLocation.repository.UserRepository;
import com.sai.geoLocation.payment.WalletService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {
    private final WalletService walletService;
    private final UserRepository userRepository;

    public WalletController(WalletService walletService, UserRepository userRepository) {
        this.walletService = walletService;
        this.userRepository = userRepository;
    }

    public record TopUpRequest(BigDecimal amount, String paymentId) {}

    @GetMapping("/me")
    public ApiResponse<Wallet> getMyWallet(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        return ApiResponse.ok("Fetched wallet", walletService.getWallet(user.getId()));
    }

    @GetMapping("/me/transactions")
    public ApiResponse<List<WalletTransaction>> getMyTransactions(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Wallet wallet = walletService.getWallet(user.getId());
        return ApiResponse.ok("Fetched transactions", walletService.getTransactions(wallet.getId()));
    }

    @PostMapping("/me/topup")
    public ApiResponse<Wallet> topUpWallet(Principal principal, @RequestBody TopUpRequest request) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Wallet wallet = walletService.depositFunds(
            user.getId(), 
            request.amount(), 
            "CARD", 
            "order_mock_" + UUID.randomUUID().toString().substring(0, 8), 
            request.paymentId()
        );
        return ApiResponse.ok("Wallet top up successful", wallet);
    }
}

package com.catchprice.apiservice.controller;

import com.catchprice.apiservice.dto.SavingsResponse;
import com.catchprice.apiservice.model.User;
import com.catchprice.apiservice.repositories.SavingsRepository;
import com.catchprice.apiservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/savings") @RequiredArgsConstructor
public class SavingsController {
    private final SavingsRepository savingsRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<SavingsResponse>> getMySavings(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        return ResponseEntity.ok(savingsRepository.findLast12MonthsByUserId(user.getId()));
    }
}
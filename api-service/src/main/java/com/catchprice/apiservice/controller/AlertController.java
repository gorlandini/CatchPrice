package com.catchprice.apiservice.controller;

import com.catchprice.apiservice.dto.AlertResponse;
import com.catchprice.apiservice.repositories.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertRepository alertRepository;

    @GetMapping
    public Page<AlertResponse> getAlerts(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        return alertRepository
                .findByProductUserEmailOrderByCreatedAtDesc(userDetails.getUsername(), pageable)
                .map(a -> new AlertResponse(
                        a.getId(),
                        a.getProduct().getId(),
                        a.getProduct().getName(),
                        a.getPriceFound(),
                        a.getShipping(),
                        a.getTotalPrice(),
                        a.getSourceUrl(),
                        a.getMarketplace(),
                        a.isSeen(),
                        a.getCreatedAt()
                ));
    }


    @GetMapping("/unseen-count")
    public Map<String, Long> getUnseenCount(@AuthenticationPrincipal UserDetails userDetails) {
        long count = alertRepository.countByProductUserEmailAndSeenFalse(userDetails.getUsername());
        return Map.of("count", count);
    }

}
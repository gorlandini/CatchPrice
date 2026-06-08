package com.catchprice.apiservice.controller;



import com.catchprice.apiservice.dto.AlertResponse;
import com.catchprice.apiservice.model.Alert;

import com.catchprice.apiservice.repositories.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertRepository alertRepository;

    @GetMapping
    public List<AlertResponse> getAlerts(@AuthenticationPrincipal UserDetails userDetails) {
        return alertRepository.findByProductUserEmailOrderByCreatedAtDesc(userDetails.getUsername())
                .stream()
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
                ))
                .toList();
    }
}
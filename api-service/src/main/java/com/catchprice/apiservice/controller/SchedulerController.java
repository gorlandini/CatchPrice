package com.catchprice.apiservice.controller;

import com.catchprice.apiservice.scheduler.PriceCheckScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class SchedulerController {
    private final PriceCheckScheduler scheduler;

    @PostMapping("/trigger-check")
    public String triggerCheck() {
        scheduler.triggerDailyCheck();
        return "Price check triggered";
    }
}
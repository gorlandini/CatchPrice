package com.catchprice.apiservice.dto;

public record AuthResponse(
        String token,
        String name,
        String email,
        boolean firstLogin
) {}
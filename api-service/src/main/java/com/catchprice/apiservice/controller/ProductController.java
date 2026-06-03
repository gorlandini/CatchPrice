package com.catchprice.apiservice.controller;

import com.catchprice.apiservice.dto.CreateProductRequest;
import com.catchprice.apiservice.dto.CreateProductResponse;
import com.catchprice.apiservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<CreateProductResponse>> list(Pageable pageable,
                                                            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(productService.findByUser(user.getUsername(), pageable));
    }

    @PostMapping
    public ResponseEntity<CreateProductResponse> create(@RequestBody @Valid CreateProductRequest req,
                                                        @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(req, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @AuthenticationPrincipal UserDetails user) {
        productService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
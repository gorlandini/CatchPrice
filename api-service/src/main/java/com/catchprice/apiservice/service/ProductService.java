package com.catchprice.apiservice.service;

import com.catchprice.apiservice.dto.CreateProductRequest;
import com.catchprice.apiservice.dto.CreateProductResponse;
import com.catchprice.apiservice.model.Product;
import com.catchprice.apiservice.model.User;
import com.catchprice.apiservice.repositories.ProductRepository;
import com.catchprice.apiservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Page<CreateProductResponse> findByUser(String email, Pageable pageable) {
        return productRepository.findByUserEmail(email, pageable)
                .map(CreateProductResponse::fromEntity);
    }

    public CreateProductResponse create(CreateProductRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        Product product = Product.builder()
                .user(user)
                .name(request.name())
                .mlQuery(request.mlQuery())
                .referencePrice(request.referencePrice())
                .active(true)
                .build();
        return CreateProductResponse.fromEntity(productRepository.save(product));
    }

    public void delete(UUID id, String email) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));
        if (!product.getUser().getEmail().equals(email)) {
            throw new IllegalStateException("Product not found"); // 404, não 403
        }
        productRepository.delete(product);
    }
}
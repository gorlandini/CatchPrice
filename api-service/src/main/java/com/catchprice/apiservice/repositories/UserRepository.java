package com.catchprice.apiservice.repositories;

// api-service/src/main/java/com/pricetracker/api/repository/UserRepository.java

import com.catchprice.apiservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
}
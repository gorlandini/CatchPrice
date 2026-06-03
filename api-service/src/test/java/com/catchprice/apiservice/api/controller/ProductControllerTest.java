package com.catchprice.apiservice.api.controller;

import com.catchprice.apiservice.dto.CreateProductRequest;
import com.catchprice.apiservice.model.Product;
import com.catchprice.apiservice.model.User;
import com.catchprice.apiservice.repositories.ProductRepository;
import com.catchprice.apiservice.repositories.UserRepository;
import com.catchprice.apiservice.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Testcontainers

class ProductControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16").withDatabaseName("catchprice");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url",      postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private JwtService jwtService;
    @Autowired private ObjectMapper objectMapper;

    private String token;
    private User loggedUser;

    @BeforeEach
    void setup() {
        loggedUser = userRepository.save(User.builder()
                .name("Test").email("test@test.com").password("hash").cep("01310-100").build());
        token = jwtService.generateToken(loggedUser);
    }

    @Test
    void shouldReturn401WhenNoToken() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn200WithValidToken() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        var req = new CreateProductRequest("Smart TV 55", "smart tv 55", new BigDecimal("2000.00"));
        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Smart TV 55"));
    }

    @Test
    void shouldReturn404WhenDeletingOtherUsersProduct() throws Exception {
        User other = userRepository.save(User.builder()
                .name("Other").email("other@test.com").password("hash").build());
        Product otherProduct = productRepository.save(Product.builder()
                .user(other).name("iPhone").mlQuery("iphone")
                .referencePrice(new BigDecimal("5000.00")).active(true).build());

        mockMvc.perform(delete("/api/v1/products/" + otherProduct.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    void cleanup() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

}
package com.catchprice.apiservice.api.repository;

import com.catchprice.apiservice.model.Product;
import com.catchprice.apiservice.model.User;
import com.catchprice.apiservice.repositories.AlertRepository;
import com.catchprice.apiservice.repositories.ProductRepository;
import com.catchprice.apiservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
class RepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16").withDatabaseName("pricetracker_test");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AlertRepository alertRepository;

    private User savedUser;
    private Product savedProduct;

    @BeforeEach
    void setup() {
        savedUser = userRepository.save(User.builder()
                .name("João").email("joao@email.com").password("hash").cep("01310-100").build());
        savedProduct = productRepository.save(Product.builder()
                .user(savedUser).name("Smart TV 55").mlQuery("smart tv 55")
                .referencePrice(new BigDecimal("2000.00")).active(true).build());
    }


    @Test
    void shouldFindProductsByUserEmail() {
        var page = productRepository.findByUserEmail("joao@email.com", Pageable.ofSize(10));
        assertThat(page.getTotalElements() == 1);
        assertThat(page.getContent().get(0).getName()).isEqualTo("Smart TV 55");
    }

    @Test
    void shouldNotReturnProductsFromOtherUsers() {
        var other = userRepository.save(User.builder()
                .name("Maria").email("maria@email.com").password("hash").build());
        productRepository.save(Product.builder().user(other).name("iPhone")
                .mlQuery("iphone").referencePrice(new BigDecimal("5000.00")).active(true).build());
        var page = productRepository.findByUserEmail("joao@email.com", Pageable.ofSize(10));
        assertThat(page.getContent()).asList().hasSize(1);
    }




}
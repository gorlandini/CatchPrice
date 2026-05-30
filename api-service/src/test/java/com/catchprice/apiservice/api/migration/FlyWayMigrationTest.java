package com.catchprice.apiservice.api.migration;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import java.util.List;

@SpringBootTest
@org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class FlywayMigrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<> ("postgres:16").withDatabaseName("catchprice");

    @Autowired
    private DataSource dataSource;

    @Test
    void shouldApplyAllMigrationsSuccessfully() throws Exception{
        try ( var conn = dataSource.getConnection() ) {
            assertThat(conn.isValid(1));
        }
    }

    @Test
    void shouldHaveAllExpectedTables() throws Exception {
        try (var conn = dataSource.getConnection()) {
            for (String table : List.of("users", "products", "price_history", "alerts")) {
                var rs = conn.getMetaData().getTables(null, null, table, null);
                assertThat(rs.next()).as("Tabela '%s' deveria existir", table).isTrue();
            }
        }
    }

    @Test
    void shouldHaveCepColumnInUsers() throws Exception {
        try (var conn = dataSource.getConnection()) {
            var rs = conn.getMetaData().getColumns(null, null, "users", "cep");
            assertThat(rs.next()).as("Coluna 'cep' deveria existir em users").isTrue();
        }
    }

    @Test
    void shouldHaveShippingAndTotalPriceInAlerts() throws Exception {
        try (var conn = dataSource.getConnection()) {
            for (String col : List.of("shipping", "total_price", "marketplace")) {
                var rs = conn.getMetaData().getColumns(null, null, "alerts", col);
                assertThat(rs.next()).as("Coluna '%s' deveria existir em alerts", col).isTrue();
            }
        }
    }


}
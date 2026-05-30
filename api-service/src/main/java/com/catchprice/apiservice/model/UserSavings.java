package com.catchprice.apiservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// UserSavings.java
// Mapeia a VIEW user_savings — não é uma tabela, por isso @Immutable e sem @GeneratedValue
@Entity
@Table(name = "user_savings")
@Immutable  // view é somente leitura — nunca tenta fazer INSERT/UPDATE
@Data
@NoArgsConstructor
public class UserSavings {

    @Id
    @Column(name = "user_id")
    private UUID userId;  // não é PK real, mas JPA exige um @Id

    @Column(name = "month")
    private LocalDateTime month;

    @Column(name = "alerts_count")
    private Long alertsCount;

    @Column(name = "total_saved")
    private BigDecimal totalSaved;

    @Column(name = "best_deal")
    private BigDecimal bestDeal;

    @Column(name = "lowest_total_found")
    private BigDecimal lowestTotalFound;

    @Column(name = "lowest_shipping_found")
    private BigDecimal lowestShippingFound;
}
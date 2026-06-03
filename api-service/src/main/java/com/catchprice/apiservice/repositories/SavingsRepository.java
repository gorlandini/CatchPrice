package com.catchprice.apiservice.repositories;


import com.catchprice.apiservice.dto.SavingsResponse;
import com.catchprice.apiservice.model.UserSavings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SavingsRepository extends Repository<UserSavings, UUID> {
        @Query(value = "SELECT month, alerts_count, total_saved, best_deal," +
                " lowest_total_found, lowest_shipping_found" +
                " FROM user_savings WHERE user_id = :userId ORDER BY month DESC LIMIT 12",
                nativeQuery = true)
        List<SavingsResponse> findLast12MonthsByUserId(@Param("userId") UUID userId);


}

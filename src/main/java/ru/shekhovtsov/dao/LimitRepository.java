package ru.shekhovtsov.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shekhovtsov.model.Limit;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    Optional<Limit> findByClientId(Long clientId);

    @Modifying
    @Query("UPDATE Limit l SET l.dailyLimit = :newLimit")
    void resetAllDailyLimits(@Param("newLimit") BigDecimal newLimit);
    void deleteByClientId(Long clientId);
}
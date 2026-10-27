package com.example.merchantqr.repository;

import com.example.merchantqr.entity.MerchantRegistration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MerchantRegistrationRepository
        extends JpaRepository<MerchantRegistration, Long> {

    @Query("""
            SELECT m
            FROM MerchantRegistration m
            WHERE
                (
                    :search IS NULL
                    OR :search = ''
                    OR LOWER(m.merchantName)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(m.accountNumber)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(m.merchantId)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(m.city)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(m.countryCode)
                        LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND (
                    :merchantType IS NULL
                    OR :merchantType = ''
                    OR m.merchantType = :merchantType
                )
                AND (
                    :fromDate IS NULL
                    OR m.createdAt >= :fromDate
                )
                AND (
                    :toDate IS NULL
                    OR m.createdAt < :toDate
                )
            ORDER BY m.id DESC
            """)
    Page<MerchantRegistration> searchRegistrations(
            @Param("search")
            String search,

            @Param("merchantType")
            String merchantType,

            @Param("fromDate")
            LocalDateTime fromDate,

            @Param("toDate")
            LocalDateTime toDate,

            Pageable pageable
    );


    @Query("""
            SELECT DISTINCT m.merchantType
            FROM MerchantRegistration m
            WHERE m.merchantType IS NOT NULL
              AND m.merchantType <> ''
            ORDER BY m.merchantType
            """)
    List<String> findDistinctMerchantTypes();
}
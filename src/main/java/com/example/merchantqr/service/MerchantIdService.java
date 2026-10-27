package com.example.merchantqr.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantIdService {

    private final JdbcTemplate jdbcTemplate;

    public MerchantIdService(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public String nextMerchantId() {

        /*
         * Increase the current Merchant ID.
         *
         * Initial database value:
         *
         * 54000000
         *
         * First generated value:
         *
         * 54000001
         */
        jdbcTemplate.update("""
                UPDATE merchant_id_sequence
                SET current_value = current_value + 1
                WHERE id = 1
                """);

        /*
         * Read the newly generated value.
         */
        Long value = jdbcTemplate.queryForObject(
                """
                SELECT current_value
                FROM merchant_id_sequence
                WHERE id = 1
                """,
                Long.class
        );

        if (value == null) {

            throw new IllegalStateException("Merchant ID sequence was not initialized.");
        }

        String merchantId = String.valueOf(value);

        /*
         * Merchant ID must always contain
         * exactly 8 digits.
         */
        if (merchantId.length() != 8) {

            throw new IllegalStateException("Generated Merchant ID is not 8 digits: " + merchantId);
        }

        return merchantId;
    }
}
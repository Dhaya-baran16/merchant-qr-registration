package com.example.merchantqr.service;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AccountValidationService {

    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate;

    public AccountValidationService(
            JdbcTemplate jdbcTemplate,
            RestTemplate restTemplate) {

        this.jdbcTemplate = jdbcTemplate;
        this.restTemplate = restTemplate;
    }

    // =========================================================
    // ACCOUNT VALIDATION RESULT
    // =========================================================

    public record AccountValidationResult(
            boolean verified,
            String accountNumber,
            int count,
            String status,
            String message,
            String merchantName,
            String countryCode,
            String emailId,
            String mobileNumber) {
    }

    // =========================================================
    // VALIDATE ACCOUNT
    // =========================================================

    public AccountValidationResult validate(String accountNumber) {

        String cleanAccountNumber =
                accountNumber == null
                        ? ""
                        : accountNumber.trim();

        if (!cleanAccountNumber.matches("\\d{1,15}")) {

            return new AccountValidationResult(
                    false,
                    cleanAccountNumber,
                    0,
                    "error",
                    "Account number must contain numbers only and must not exceed 15 digits.",
                    "",
                    "",
                    "",
                    ""
            );
        }

        try {

            // =====================================================
            // GET URI FROM webservice001
            // =====================================================

            String uri = jdbcTemplate.query(
                    """
                    SELECT uri
                    FROM webservice001
                    WHERE chcode = ?
                      AND servicecd = ?
                    LIMIT 1
                    """,
                    ps -> {
                        ps.setString(1, "ACCOUNT");
                        ps.setString(2, "GETBAL");
                    },
                    rs -> rs.next() ? rs.getString("uri") : null
            );

            if (uri == null || uri.isBlank()) {

                return new AccountValidationResult(
                        false,
                        cleanAccountNumber,
                        0,
                        "error",
                        "Account validation URI was not found in webservice001.",
                        "",
                        "",
                        "",
                        ""
                );
            }

            // =====================================================
            // REPLACE ACCOUNT NUMBER IN URI
            // =====================================================

            String finalUri = uri.replace(
                            "{accountNumber}",
                            cleanAccountNumber
                    );

            // =====================================================
            // IMPORTANT:
            // The SIMV1 endpoint is called using GET.
            // We intentionally do not use the method value from
            // webservice001 because the configured endpoint expects GET.
            // =====================================================

            ResponseEntity<Map> response =
                    restTemplate.getForEntity(
                            finalUri,
                            Map.class
                    );

            Map body = response.getBody();

            if (body == null) {

                return new AccountValidationResult(
                        false,
                        cleanAccountNumber,
                        0,
                        "error",
                        "Empty response received from account validation service.",
                        "",
                        "",
                        "",
                        ""
                );
            }

            // =====================================================
            // ACTUAL SIMV1 RESPONSE
            //
            // {
            //   "success": true,
            //   "count": 1,
            //   "data": [
            //      {
            //        "ACCOUNTNAME": "...",
            //        "CURRENCY": "TZS",
            //        "EMAIL": "...",
            //        "PHONENUMBER": "..."
            //      }
            //   ]
            // }
            // =====================================================

            boolean success = Boolean.TRUE.equals(body.get("success"));

            int count = toInt(body.get("count"));

            String message = body.get("message") == null
                            ? ""
                            : String.valueOf(body.get("message"));

            if (!success || count != 1) {

                if (message.isBlank()) {
                    message =
                            "Account number could not be verified.";
                }

                return new AccountValidationResult(
                        false,
                        cleanAccountNumber,
                        count,
                        "error",
                        message,
                        "",
                        "",
                        "",
                        ""
                );
            }

            // =====================================================
            // READ DATA ARRAY
            // =====================================================

            List<Map<String, Object>> dataList =
                    extractDataList(body.get("data"));

            Map<String, Object> customer =
                    dataList.isEmpty()
                            ? Collections.emptyMap()
                            : dataList.get(0);

            // =====================================================
            // API FIELD MAPPING
            //
            // ACCOUNTNAME -> Merchant Name
            // CURRENCY    -> Country Code
            // EMAIL       -> Email ID
            // PHONENUMBER -> Mobile Number
            // =====================================================

            String merchantName = stringValue(customer.get("ACCOUNTNAME"));

            String countryCode = stringValue(customer.get("CURRENCY"));

            String emailId = stringValue(customer.get("EMAIL"));

            String mobileNumber = stringValue(customer.get("PHONENUMBER"));

            return new AccountValidationResult(
                    true,
                    cleanAccountNumber,
                    count,
                    "success",
                    "Account number verified successfully.",
                    merchantName,
                    countryCode,
                    emailId,
                    mobileNumber
            );

        } catch (RestClientException e) {

            return new AccountValidationResult(
                    false,
                    cleanAccountNumber,
                    0,
                    "error",
                    "Unable to connect to the account validation service.",
                    "",
                    "",
                    "",
                    ""
            );

        } catch (Exception e) {

            return new AccountValidationResult(
                    false,
                    cleanAccountNumber,
                    0,
                    "error",
                    "Account validation failed: " + e.getMessage(),
                    "",
                    "",
                    "",
                    ""
            );
        }
    }

    // =========================================================
    // USED BY MerchantRegistrationService
    // =========================================================

    public boolean exists(String accountNumber) {

        AccountValidationResult result = validate(accountNumber);

        return result.verified() && result.count() == 1;
    }

    // =========================================================
    // CONVERT count SAFELY
    // =========================================================

    private int toInt(Object value) {

        if (value == null) {
            return 0;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        try {
            return Integer.parseInt(
                    String.valueOf(value)
            );
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // =========================================================
    // EXTRACT DATA ARRAY
    // =========================================================

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> extractDataList(
            Object data) {

        if (!(data instanceof List<?> list)) {
            return Collections.emptyList();
        }

        return (List<Map<String, Object>>) (List<?>) list;
    }

    // =========================================================
    // SAFE STRING VALUE
    // =========================================================

    private String stringValue(Object value) {

        if (value == null) {
            return "";
        }

        return String.valueOf(value).trim();
    }
}

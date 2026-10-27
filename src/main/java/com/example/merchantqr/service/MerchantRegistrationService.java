package com.example.merchantqr.service;

import com.example.merchantqr.dto.MerchantRegistrationRequest;
import com.example.merchantqr.entity.MerchantRegistration;
import com.example.merchantqr.repository.MerchantRegistrationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MerchantRegistrationService {

    private final AccountValidationService accountValidationService;
    private final MerchantIdService merchantIdService;
    private final QrCodeService qrCodeService;
    private final MerchantRegistrationRepository repository;

    public MerchantRegistrationService(
            AccountValidationService accountValidationService,
            MerchantIdService merchantIdService,
            QrCodeService qrCodeService,
            MerchantRegistrationRepository repository) {

        this.accountValidationService = accountValidationService;
        this.merchantIdService = merchantIdService;
        this.qrCodeService = qrCodeService;
        this.repository = repository;
    }

    // =========================================================
    // CREATE MERCHANT REGISTRATION
    // =========================================================

    @Transactional
    public MerchantRegistration create(
            MerchantRegistrationRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Registration request cannot be empty."
            );
        }

        // =====================================================
        // 1. ACCOUNT NUMBER
        // =====================================================

        String accountNumber =
                cleanInput(request.getAccountNumber());

        if (accountNumber.isEmpty()) {
            throw new IllegalArgumentException(
                    "Account number is required."
            );
        }

        if (!accountNumber.matches("\\d{1,15}")) {
            throw new IllegalArgumentException(
                    "Account number must contain numbers only and must not exceed 15 digits."
            );
        }

        // =====================================================
        // 2. VALIDATE ACCOUNT AGAINST ACCOUNT API
        // =====================================================

        if (!accountValidationService.exists(accountNumber)) {
            throw new IllegalArgumentException(
                    "Account number was not found or could not be verified."
            );
        }

        // =====================================================
        // 3. MERCHANT NAME
        // =====================================================

        String merchantName =
                request.getMerchantName();

        if (merchantName == null || merchantName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Merchant Name is required."
            );
        }

        if (merchantName.length() > 25) {
            throw new IllegalArgumentException(
                    "Merchant Name cannot exceed 25 characters."
            );
        }

        if (!merchantName.matches(
                "^[A-Za-z]+(?: [A-Za-z]+)*$")) {

            throw new IllegalArgumentException(
                    "Merchant Name must contain only letters and single spaces."
            );
        }

        // =====================================================
        // 4. CITY
        // =====================================================

        String city =
                cleanInput(request.getCity());

        if (city.isEmpty()) {
            throw new IllegalArgumentException(
                    "City is required."
            );
        }

        // =====================================================
        // 5. COUNTRY CODE
        // =====================================================

        String countryCode =
                cleanInput(request.getCountryCode());

        if (countryCode.isEmpty()) {
            throw new IllegalArgumentException(
                    "Country Code is required."
            );
        }

        // =====================================================
        // 6. POSTAL CODE
        // =====================================================

        String postalCode =
                cleanInput(request.getPostalCode());

        if (!postalCode.matches("\\d{6}")) {
            throw new IllegalArgumentException(
                    "Postal Code must contain exactly 6 digits."
            );
        }

        // =====================================================
        // 7. MERCHANT TYPE
        // =====================================================

        String merchantType =
                cleanInput(request.getMerchantType());

        if (merchantType.isEmpty()) {
            throw new IllegalArgumentException(
                    "Merchant Type is required."
            );
        }

        // =====================================================
        // 8. MOBILE NUMBER
        //
        // Frontend submits:
        //
        // +91 + 1234567890
        // = +911234567890
        //
        // This is '+' followed by exactly 12 digits.
        // =====================================================

        String mobileNumber =
                cleanInput(request.getMobileNumber());

        if (mobileNumber.isEmpty()) {
            throw new IllegalArgumentException(
                    "Mobile Number is required."
            );
        }

        if (!mobileNumber.matches("\\+\\d{12}")) {
            throw new IllegalArgumentException(
                    "Mobile Number must contain a '+' followed by exactly 12 digits."
            );
        }

        // =====================================================
        // 9. EMAIL
        // =====================================================

        String emailId =
                cleanInput(request.getEmailId());

        if (emailId.isEmpty()) {
            throw new IllegalArgumentException(
                    "Email ID is required."
            );
        }

        if (!emailId.matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

            throw new IllegalArgumentException(
                    "Please enter a valid Email ID."
            );
        }

        // =====================================================
        // 10. CHARGES
        //
        // Numbers only.
        // Minimum 0.
        // Maximum 100.
        // =====================================================

        String charges =
                cleanInput(request.getCharges());

        if (charges.isEmpty()) {
            throw new IllegalArgumentException(
                    "Charges is required."
            );
        }

        if (!charges.matches("^\\d+$")) {
            throw new IllegalArgumentException(
                    "Charges must contain numbers only."
            );
        }

        int chargesValue;

        try {
            chargesValue =
                    Integer.parseInt(charges);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Charges must be a valid number."
            );
        }

        if (chargesValue < 0 || chargesValue > 100) {
            throw new IllegalArgumentException(
                    "Charges must be between 0 and 100."
            );
        }

        // =====================================================
        // 11. GENERATE MERCHANT ID
        // =====================================================

        String merchantId =
                merchantIdService.nextMerchantId();

        // =====================================================
        // 12. BUILD QR STRING
        // =====================================================

        String qrString =
                buildQrString(
                        accountNumber,
                        merchantName,
                        city,
                        countryCode,
                        postalCode,
                        merchantType,
                        mobileNumber,
                        emailId,
                        charges,
                        merchantId
                );

        // =====================================================
        // 13. GENERATE QR IMAGE
        // =====================================================

        byte[] qrImage =
                qrCodeService.generatePng(
                        qrString,
                        650,
                        650
                );

        // =====================================================
        // 14. CREATE ENTITY
        // =====================================================

        MerchantRegistration registration =
                new MerchantRegistration();

        registration.setAccountNumber(accountNumber);
        registration.setMerchantId(merchantId);

        // Do not trim/normalize merchant name.
        // Save the exact validated value.
        registration.setMerchantName(merchantName);

        registration.setCity(city);
        registration.setCountryCode(countryCode);
        registration.setPostalCode(postalCode);
        registration.setMerchantType(merchantType);
        registration.setMobileNumber(mobileNumber);
        registration.setEmailId(emailId);
        registration.setCharges(charges);
        registration.setQrString(qrString);
        registration.setQrImage(qrImage);
        registration.setCreatedAt(LocalDateTime.now());

        // =====================================================
        // 15. SAVE TO merchant_id_created
        // =====================================================

        return repository.save(registration);
    }

    // =========================================================
    // BUILD QR STRING
    // =========================================================

    private String buildQrString(
            String accountNumber,
            String merchantName,
            String city,
            String countryCode,
            String postalCode,
            String merchantType,
            String mobileNumber,
            String emailId,
            String charges,
            String merchantId) {

        String value =
                cleanQrValue(accountNumber)
                + cleanQrValue(merchantName)
                + cleanQrValue(city)
                + cleanQrValue(countryCode)
                + cleanQrValue(postalCode)
                + cleanQrValue(merchantType)
                + cleanQrValue(mobileNumber)
                + cleanQrValue(emailId)
                + cleanQrValue(charges)
                + cleanQrValue(merchantId);

        return "\"" + value + "\"";
    }

    // =========================================================
    // NORMAL INPUT CLEANING
    // =========================================================

    private String cleanInput(String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    // =========================================================
    // QR VALUE CLEANING
    // =========================================================

    private String cleanQrValue(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replaceAll("\\s+", "")
                .replace("|", "")
                .replace("\"", "");
    }
}

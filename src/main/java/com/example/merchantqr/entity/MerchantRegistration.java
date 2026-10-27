package com.example.merchantqr.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_id_created")
public class MerchantRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "account_number",
            nullable = false,
            length = 50
    )
    private String accountNumber;

    @Column(
            name = "merchant_id",
            nullable = false,
            unique = true,
            length = 8
    )
    private String merchantId;

    @Column(
            name = "merchant_name",
            nullable = false,
            length = 150
    )
    private String merchantName;

    @Column(
            name = "city",
            nullable = false,
            length = 100
    )
    private String city;

    @Column(
            name = "country_code",
            nullable = false,
            length = 20
    )
    private String countryCode;

    @Column(
            name = "postal_code",
            nullable = false,
            length = 30
    )
    private String postalCode;

    @Column(
            name = "merchant_type",
            nullable = false,
            length = 50
    )
    private String merchantType;

    @Column(
            name = "mobile_number",
            nullable = false,
            length = 20
    )
    private String mobileNumber;

    @Column(
            name = "email_id",
            nullable = false,
            length = 150
    )
    private String emailId;

    @Column(
    	    name = "charges",
    	    nullable = false,
    	    length = 100
    	)
    	private String charges;

    @Lob
    @Column(
            name = "qr_string",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String qrString;

    @Lob
    @Column(
            name = "qr_image",
            nullable = false,
            columnDefinition = "MEDIUMBLOB"
    )
    private byte[] qrImage;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getQrString() {
        return qrString;
    }

    public void setQrString(String qrString) {
        this.qrString = qrString;
    }

    public byte[] getQrImage() {
        return qrImage;
    }

    public void setQrImage(byte[] qrImage) {
        this.qrImage = qrImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
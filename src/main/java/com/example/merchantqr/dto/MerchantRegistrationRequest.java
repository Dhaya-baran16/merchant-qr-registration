package com.example.merchantqr.dto;

public class MerchantRegistrationRequest {

    private String accountNumber;
    private String merchantName;
    private String city;
    private String countryCode;
    private String postalCode;
    private String merchantType;
    private String mobileNumber;
    private String emailId;
    private String charges;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(
            String accountNumber) {

        this.accountNumber = accountNumber;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(
            String merchantName) {

        this.merchantName = merchantName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(
            String city) {

        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(
            String countryCode) {

        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(
            String postalCode) {

        this.postalCode = postalCode;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(
            String merchantType) {

        this.merchantType = merchantType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(
            String mobileNumber) {

        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(
            String emailId) {

        this.emailId = emailId;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(
            String charges) {

        this.charges = charges;
    }
}
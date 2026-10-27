package com.example.merchantqr.controller;

import com.example.merchantqr.dto.MerchantRegistrationRequest;
import com.example.merchantqr.entity.MerchantRegistration;
import com.example.merchantqr.repository.MerchantRegistrationRepository;
import com.example.merchantqr.service.AccountValidationService;
import com.example.merchantqr.service.MerchantExcelExportService;
import com.example.merchantqr.service.MerchantExportPdfService;
import com.example.merchantqr.service.MerchantRegistrationService;
import com.example.merchantqr.service.PdfService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class MerchantRegistrationController {

    private static final int PAGE_SIZE = 10;
    private final MerchantRegistrationService registrationService;
    private final MerchantRegistrationRepository repository;
    private final PdfService pdfService;
    private final AccountValidationService accountValidationService;
    private final MerchantExcelExportService excelExportService;
    private final MerchantExportPdfService exportPdfService;


    public MerchantRegistrationController(
            MerchantRegistrationService registrationService,
            MerchantRegistrationRepository repository,
            PdfService pdfService,
            AccountValidationService accountValidationService,
            MerchantExcelExportService excelExportService,
            MerchantExportPdfService exportPdfService) {

        this.registrationService = registrationService;
        this.repository = repository;
        this.pdfService = pdfService;
        this.accountValidationService = accountValidationService;
        this.excelExportService = excelExportService;
        this.exportPdfService = exportPdfService;
    }


    @GetMapping("/")
    public String home() {

        return "redirect:/merchant-registration";
    }


    @GetMapping("/merchant-registration")
    public String registrationPage() {

        return "merchant-registration";
    }
    
    @GetMapping("/api/accounts/check")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkAccount(
            @RequestParam String accountNumber) {

        AccountValidationService.AccountValidationResult result =
                accountValidationService.validate(accountNumber);

        Map<String, Object> response =
                new HashMap<>();


        // =========================================================
        // ACCOUNT VALIDATION RESULT
        // =========================================================

        response.put("success", result.verified());

        response.put("verified", result.verified());

        response.put("accountNumber", result.accountNumber());

        response.put("count", result.count());

        response.put("message", result.message());


        // =========================================================
        // API VALUES FOR REGISTRATION FORM
        // =========================================================

        response.put("merchantName", result.merchantName());

        response.put("countryCode", result.countryCode());

        response.put("emailId", result.emailId());

        response.put("mobileNumber", result.mobileNumber());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/merchant-registration")
    public String register(
            @ModelAttribute MerchantRegistrationRequest request,
            Model model) {

        try {

            MerchantRegistration registration = registrationService.create(request);

            model.addAttribute("registration", registration);

            model.addAttribute("success", true);

        } catch (IllegalArgumentException e) {

            /*
             * Validation errors are returned to the same registration
             * page. The frontend will display the message instead of
             * treating the request as a successful processing flow.
             */
            model.addAttribute("success", false);
            model.addAttribute(
                    "error",
                    e.getMessage() == null
                            ? "Please check the entered registration details."
                            : e.getMessage()
            );

        } catch (Exception e) {

            model.addAttribute("success", false);
            model.addAttribute(
                    "error",
                    "Unable to process the registration. Please check the entered details and try again."
            );
        }

        return "merchant-registration";
    }

    // =========================================================
    // MERCHANT REGISTRATIONS PAGE
    // =========================================================

    @GetMapping("/merchant-registrations")
    public String registrationsPage(
            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            String merchantType,

            @RequestParam(required = false)
            String fromDate,

            @RequestParam(required = false)
            String toDate,

            @RequestParam(defaultValue = "0")
            int page,

            Model model) {

        if (page < 0) {
            page = 0;
        }


        // =========================================================
        // CLEAN SEARCH
        // =========================================================

        String cleanSearch = search;

        if (cleanSearch != null) {

            cleanSearch = cleanSearch.trim();

            if (cleanSearch.isEmpty()) {
                cleanSearch = null;
            }
        }


        // =========================================================
        // CLEAN MERCHANT TYPE
        // =========================================================

        String cleanMerchantType = merchantType;

        if (cleanMerchantType != null) {

            cleanMerchantType = cleanMerchantType.trim();

            if (cleanMerchantType.isEmpty() || cleanMerchantType.equalsIgnoreCase("ALL")) {

                cleanMerchantType = null;
            }
        }


        // =========================================================
        // DATE VARIABLES
        // =========================================================

        LocalDateTime fromDateTime = null;

        LocalDateTime toDateTime = null;

        // =========================================================
        // DEFAULT VIEW CHECK
        //
        // No search
        // No merchant type
        // No from date
        // No to date
        //
        // => TODAY'S RECORDS
        // =========================================================

        boolean defaultView = cleanSearch == null
                        && cleanMerchantType == null
                        && (fromDate == null || fromDate.isBlank())
                        && (toDate == null || toDate.isBlank());

        try {

            // =====================================================
            // DEFAULT VIEW
            // =====================================================

            if (defaultView) {

                LocalDate today = LocalDate.now();

                fromDateTime = today.atStartOfDay();
                toDateTime = today.plusDays(1).atStartOfDay();

            } else {

                // =================================================
                // FROM DATE
                // =================================================

                if (
                        fromDate != null && !fromDate.isBlank()
                ) {

                    fromDateTime = LocalDate.parse(fromDate).atStartOfDay();
                }


                // =================================================
                // TO DATE
                // =================================================

                if (
                        toDate != null && !toDate.isBlank()
                ) {

                    toDateTime = LocalDate.parse(toDate).plusDays(1).atStartOfDay();
                }
            }


            // =====================================================
            // DATE VALIDATION
            // =====================================================

            if (
                    fromDateTime != null
                            && toDateTime != null
                            && !fromDateTime.isBefore(toDateTime)
            ) {

                model.addAttribute("error", "From Date must be before or equal to To Date.");

                model.addAttribute("pageData", Page.empty());

            } else {

                // ================================================
                // PAGE REQUEST
                // ================================================

                PageRequest pageRequest = PageRequest.of(
                                page,
                                PAGE_SIZE,
                                Sort.by(Sort.Direction.DESC, "id")
                        );


                // ================================================
                // SEARCH DATABASE
                // ================================================

                Page<MerchantRegistration> pageData = repository.searchRegistrations(
                                cleanSearch,
                                cleanMerchantType,
                                fromDateTime,
                                toDateTime,
                                pageRequest
                        );


                model.addAttribute("pageData", pageData);
            }


        } catch (Exception e) {

            model.addAttribute("error", "Invalid date format. Please use the date fields provided.");

            model.addAttribute("pageData", Page.empty());
        }


        // =========================================================
        // MERCHANT TYPE OPTIONS
        // =========================================================

        model.addAttribute("merchantTypes", repository.findDistinctMerchantTypes());

        // =========================================================
        // RETURN VALUES TO HTML
        // =========================================================

        model.addAttribute("search", search == null ? "" : search);

        model.addAttribute("merchantType", merchantType == null ? "" : merchantType);

        model.addAttribute("fromDate", fromDate == null ? "" : fromDate);

        model.addAttribute("toDate", toDate == null ? "" : toDate);

        model.addAttribute("defaultView", defaultView);

        return "merchant-registrations";
    }

    // =========================================================
    // QR IMAGE
    // =========================================================

    @GetMapping("/merchant-registrations/{id}/qr")
    public ResponseEntity<byte[]> qrImage(
            @PathVariable Long id) {

        MerchantRegistration registration = repository.findById(id)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException("Merchant registration not found")
                        );

        return ResponseEntity
                .ok()
                .contentType(
                        MediaType.IMAGE_PNG
                )
                .body(
                        registration.getQrImage()
                );
    }

    // =========================================================
    // INDIVIDUAL PDF
    // =========================================================

    @GetMapping("/merchant-registrations/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable Long id) {

        MerchantRegistration registration =
                repository.findById(id)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Merchant registration not found"
                                        )
                        );

        byte[] pdf = pdfService.createPdf(
                        registration.getQrImage(),
                        registration.getMerchantId(),
                        registration.getMerchantName()
                );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename(
                                "merchant-"
                                        + registration.getMerchantId()
                                        + ".pdf"
                        )
                        .build()
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }

    // =========================================================
    // FULL DATABASE EXCEL EXPORT
    // =========================================================

    @GetMapping("/merchant-registrations/export/excel")
    public ResponseEntity<byte[]> exportExcel() {

        byte[] excel = excelExportService.createExcel();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        headers.setContentDisposition(ContentDisposition.attachment().filename("merchant-registrations.xlsx").build());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(excel);
    }

    // =========================================================
    // FULL DATABASE PDF EXPORT
    // =========================================================

    @GetMapping("/merchant-registrations/export/pdf")
    public ResponseEntity<byte[]> exportFullPdf() {

        byte[] pdf = exportPdfService.createPdf();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.setContentDisposition(ContentDisposition
                        .attachment()
                        .filename(
                                "merchant-registrations.pdf"
                        )
                        .build()
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }
}
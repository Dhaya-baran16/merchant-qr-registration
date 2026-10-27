package com.example.merchantqr.service;

import com.example.merchantqr.entity.MerchantRegistration;
import com.example.merchantqr.repository.MerchantRegistrationRepository;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MerchantExcelExportService {

    private final MerchantRegistrationRepository repository;

    public MerchantExcelExportService(MerchantRegistrationRepository repository) {

        this.repository = repository;
    }

    public byte[] createExcel() {

        List<MerchantRegistration> registrations = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        try (
                XSSFWorkbook workbook = new XSSFWorkbook();

                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {

            var sheet = workbook.createSheet("Merchant Registrations");


            // =====================================================
            // TITLE STYLE
            // =====================================================

            CellStyle titleStyle = workbook.createCellStyle();

            Font titleFont = workbook.createFont();

            titleFont.setBold(true);
            titleFont.setFontHeightInPoints(
                    (short) 16
            );

            titleStyle.setFont(titleFont);

            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);


            // =====================================================
            // HEADER STYLE
            // =====================================================

            CellStyle headerStyle = workbook.createCellStyle();

            Font headerFont = workbook.createFont();

            headerFont.setBold(true);
            headerFont.setColor(org.apache.poi.ss.usermodel.IndexedColors.WHITE
                            .getIndex()
            );

            headerStyle.setFont(headerFont);

            headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.DARK_BLUE
                            .getIndex()
            );

            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            headerStyle.setBorderBottom(BorderStyle.THIN);


            // =====================================================
            // DATA STYLE
            // =====================================================

            CellStyle dataStyle = workbook.createCellStyle();

            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            dataStyle.setBorderBottom(BorderStyle.THIN);


            // =====================================================
            // WRAP STYLE
            // =====================================================

            CellStyle wrapStyle = workbook.createCellStyle();

            wrapStyle.cloneStyleFrom(dataStyle);

            wrapStyle.setWrapText(true);

            // =====================================================
            // DATE STYLE
            // =====================================================

            CellStyle dateStyle = workbook.createCellStyle();

            dateStyle.cloneStyleFrom(dataStyle);

            dateStyle.setDataFormat(workbook.getCreationHelper()
                            .createDataFormat()
                            .getFormat("dd-mm-yyyy hh:mm")
            );

            // =====================================================
            // TITLE
            // =====================================================

            var titleRow = sheet.createRow(0);

            titleRow.setHeightInPoints(28);

            Cell titleCell = titleRow.createCell(0);

            titleCell.setCellValue("MERCHANT QR REGISTRATION DATABASE");

            titleCell.setCellStyle(titleStyle);

            sheet.addMergedRegion(
                    new org.apache.poi.ss.util.CellRangeAddress(
                            0,
                            0,
                            0,
                            13
                    )
            );

            // =====================================================
            // SUBTITLE
            // =====================================================

            var subtitleRow = sheet.createRow(1);

            Cell subtitleCell = subtitleRow.createCell(0);

            subtitleCell.setCellValue("Complete merchant registration database - All Records");

            subtitleCell.setCellStyle(titleStyle);

            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                            1,
                            1,
                            0,
                            13
                    )
            );

            // =====================================================
            // HEADER
            // =====================================================

            String[] headers = {

                    "SI.No.",
                    "Database ID",
                    "Merchant ID",
                    "Account Number",
                    "Merchant Name",
                    "City",
                    "Country Code",
                    "Postal Code",
                    "Merchant Type",
                    "Mobile Number",
                    "Email ID",
                    "Charges",
                    "QR String",
                    "Created At"
            };

            var headerRow = sheet.createRow(3);

            headerRow.setHeightInPoints(28);

            for (int i = 0;
                 i < headers.length;
                 i++) {

                Cell cell = headerRow.createCell(i);

                cell.setCellValue(headers[i]);

                cell.setCellStyle(headerStyle);
            }

            // =====================================================
            // DATA
            // =====================================================

            int rowNumber = 4;

            int serialNumber = 1;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            for (MerchantRegistration registration
                        : registrations
            ) {

                var row = sheet.createRow(rowNumber++);

                row.setHeightInPoints(35);

                createCell(
                        row,
                        0,
                        String.valueOf(serialNumber++),
                        dataStyle
                );

                createCell(
                        row,
                        1,
                        String.valueOf(
                                registration.getId()
                        ),
                        dataStyle
                );

                createCell(
                        row,
                        2,
                        registration.getMerchantId(),
                        dataStyle
                );

                createCell(
                        row,
                        3,
                        registration.getAccountNumber(),
                        dataStyle
                );

                createCell(
                        row,
                        4,
                        registration.getMerchantName(),
                        dataStyle
                );

                createCell(
                        row,
                        5,
                        registration.getCity(),
                        dataStyle
                );

                createCell(
                        row,
                        6,
                        registration.getCountryCode(),
                        dataStyle
                );

                createCell(
                        row,
                        7,
                        registration.getPostalCode(),
                        dataStyle
                );

                createCell(
                        row,
                        8,
                        registration.getMerchantType(),
                        dataStyle
                );

                createCell(
                        row,
                        9,
                        registration.getMobileNumber(),
                        dataStyle
                );

                createCell(
                        row,
                        10,
                        registration.getEmailId(),
                        dataStyle
                );

                Cell chargesCell = row.createCell(11);

                String charges = registration.getCharges();

                if (charges != null) {
                	
                    chargesCell.setCellValue(charges);
                } 
                
                else {
                    chargesCell.setCellValue("");
                }

                chargesCell.setCellStyle(dataStyle);

                createCell(
                        row,
                        12,
                        registration.getQrString(),
                        wrapStyle
                );

                Cell dateCell = row.createCell(13);

                dateCell.setCellValue(registration.getCreatedAt().format(formatter));

                dateCell.setCellStyle(dateStyle);
            }

            // =====================================================
            // COLUMN WIDTHS
            // =====================================================

            int[] widths = {

                    10,
                    12,
                    15,
                    20,
                    25,
                    18,
                    15,
                    15,
                    20,
                    20,
                    30,
                    14,
                    55,
                    22
            };


            for (int i = 0;
                 i < widths.length;
                 i++) {

                sheet.setColumnWidth(i, widths[i] * 256);
            }


            // =====================================================
            // FREEZE HEADER
            // =====================================================

            sheet.createFreezePane(
                    0,
                    4
            );


            // =====================================================
            // FILTER
            // =====================================================

            if (!registrations.isEmpty()) {

                sheet.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(
                                3,
                                3 + registrations.size(),
                                0,
                                13
                        )
                );
            }

            workbook.write(output);

            return output.toByteArray();

        } catch (Exception e) {

            throw new IllegalStateException("Could not create Excel export", e);
        }
    }


    private void createCell(
            org.apache.poi.ss.usermodel.Row row,
            int column,
            String value,
            CellStyle style) {

        Cell cell = row.createCell(column);

        cell.setCellValue(value == null
                        ? ""
                        : value
        );

        cell.setCellStyle(style);
    }
}
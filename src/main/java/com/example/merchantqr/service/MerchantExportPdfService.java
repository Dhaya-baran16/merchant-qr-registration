package com.example.merchantqr.service;

import com.example.merchantqr.entity.MerchantRegistration;
import com.example.merchantqr.repository.MerchantRegistrationRepository;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MerchantExportPdfService {

    private static final float PAGE_MARGIN = 24f;

    private static final float HEADER_HEIGHT = 42f;

    private static final float TABLE_HEADER_HEIGHT = 30f;

    private static final float ROW_HEIGHT = 38f;

    private final MerchantRegistrationRepository repository;

    public MerchantExportPdfService(
            MerchantRegistrationRepository repository) {

        this.repository = repository;
    }

    public byte[] createPdf() {

        List<MerchantRegistration> registrations = repository.findAll(Sort.by(Sort.Direction.DESC, "id")     
                        );

        try (
                PDDocument document = new PDDocument();

                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {

            PDType1Font bold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            PDType1Font normal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            PDRectangle landscape = new PDRectangle(PDRectangle.A3.getHeight(), PDRectangle.A3.getWidth());

            PDPage page = new PDPage(landscape);

            document.addPage(page);

            float pageWidth = page.getMediaBox().getWidth();

            float pageHeight = page.getMediaBox().getHeight();

            int rowIndex = 0;

            int pageNumber = 1;

            PDPageContentStream content = new PDPageContentStream(document, page);

            while (true) {

                drawPageHeader(
                        content,
                        pageWidth,
                        pageHeight,
                        bold,
                        normal,
                        registrations.size()
                );

                float y = pageHeight
                                - PAGE_MARGIN
                                - HEADER_HEIGHT
                                - TABLE_HEADER_HEIGHT;

                drawTableHeader(
                        content,
                        y,
                        bold
                );

                y -= TABLE_HEADER_HEIGHT;

                while (rowIndex < registrations.size() && y > 45)
                {

                    MerchantRegistration registration =
                            registrations.get(
                                    rowIndex
                            );


                    drawRow(
                            content,
                            registration,
                            rowIndex + 1,
                            y,
                            normal,
                            bold
                    );


                    y -= ROW_HEIGHT;

                    rowIndex++;
                }


                drawFooter(
                        content,
                        pageWidth,
                        pageNumber,
                        normal
                );


                content.close();


                if (
                        rowIndex
                                >= registrations.size()
                ) {

                    break;
                }


                pageNumber++;

                page =
                        new PDPage(landscape);

                document.addPage(page);

                content =
                        new PDPageContentStream(
                                document,
                                page
                        );
            }


            document.save(output);

            return output.toByteArray();

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Could not create merchant export PDF",
                    e
            );
        }
    }


    private void drawPageHeader(
            PDPageContentStream content,
            float pageWidth,
            float pageHeight,
            PDType1Font bold,
            PDType1Font normal,
            int totalRecords)
            throws IOException {


        content.setNonStrokingColor(
                new Color(11, 45, 92)
        );

        content.addRect(
                PAGE_MARGIN,
                pageHeight
                        - PAGE_MARGIN
                        - HEADER_HEIGHT,
                pageWidth
                        - PAGE_MARGIN * 2,
                HEADER_HEIGHT
        );

        content.fill();


        writeText(
                content,
                "MERCHANT QR REGISTRATION SYSTEM",
                PAGE_MARGIN + 15,
                pageHeight - PAGE_MARGIN - 17,
                bold,
                14f,
                Color.WHITE
        );


        writeText(
                content,
                "COMPLETE MERCHANT DATABASE REPORT",
                PAGE_MARGIN + 15,
                pageHeight - PAGE_MARGIN - 33,
                normal,
                7f,
                new Color(220, 230, 245)
        );


        String generated =
                "Generated: "
                        + LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd-MM-yyyy HH:mm"
                                )
                        )
                        + "    Total Records: "
                        + totalRecords;


        float textWidth =
                normal.getStringWidth(generated)
                        / 1000f
                        * 7f;


        writeText(
                content,
                generated,
                pageWidth
                        - PAGE_MARGIN
                        - textWidth
                        - 15,
                pageHeight - PAGE_MARGIN - 25,
                normal,
                7f,
                Color.WHITE
        );
    }


    private void drawTableHeader(
            PDPageContentStream content,
            float y,
            PDType1Font bold)
            throws IOException {


        float x =
                PAGE_MARGIN;


        float[] widths = columnWidths();


        content.setNonStrokingColor(
                new Color(37, 99, 235)
        );


        float totalWidth = 0;

        for (float width : widths) {
            totalWidth += width;
        }


        content.addRect(
                x,
                y,
                totalWidth,
                TABLE_HEADER_HEIGHT
        );

        content.fill();


        String[] headers = {

                "SI",
                "DB ID",
                "MERCHANT ID",
                "ACCOUNT",
                "MERCHANT NAME",
                "CITY",
                "COUNTRY",
                "POSTAL",
                "TYPE",
                "MOBILE",
                "EMAIL",
                "CHARGES",
                "CREATED AT",
                "QR STRING"
        };


        float currentX = x;


        for (int i = 0;
             i < headers.length;
             i++) {

            writeText(
                    content,
                    headers[i],
                    currentX + 3,
                    y + 9,
                    bold,
                    5.3f,
                    Color.WHITE
            );

            currentX += widths[i];
        }
    }


    private void drawRow(
            PDPageContentStream content,
            MerchantRegistration registration,
            int serial,
            float y,
            PDType1Font normal,
            PDType1Font bold)
            throws IOException {


        float x =
                PAGE_MARGIN;

        float[] widths =
                columnWidths();


        if (serial % 2 == 0) {

            content.setNonStrokingColor(
                    new Color(247, 249, 252)
            );

        } else {

            content.setNonStrokingColor(
                    Color.WHITE
            );
        }


        float totalWidth = 0;

        for (float width : widths) {
            totalWidth += width;
        }


        content.addRect(
                x,
                y,
                totalWidth,
                ROW_HEIGHT
        );

        content.fill();


        content.setLineWidth(
                0.3f
        );

        content.setStrokingColor(
                new Color(220, 226, 235)
        );

        content.addRect(
                x,
                y,
                totalWidth,
                ROW_HEIGHT
        );

        content.stroke();


        String createdAt =
                registration
                        .getCreatedAt()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd-MM-yyyy HH:mm"
                                )
                        );


        String[] values = {

                String.valueOf(serial),

                String.valueOf(
                        registration.getId()
                ),

                registration.getMerchantId(),

                registration.getAccountNumber(),

                registration.getMerchantName(),

                registration.getCity(),

                registration.getCountryCode(),

                registration.getPostalCode(),

                registration.getMerchantType(),

                registration.getMobileNumber(),

                registration.getEmailId(),

                registration.getCharges() == null
                        ? ""
                        : registration
                                .getCharges(),

                createdAt,
                
                registration.getQrString()
        };


        float currentX = x;


        for (int i = 0;
             i < values.length;
             i++) {

            String value =
                    safeText(values[i]);


            writeWrappedText(
                    content,
                    value,
                    currentX + 3,
                    y + 17,
                    widths[i] - 6,
                    normal,
                    6.0f
            );


            currentX += widths[i];
        }
    }


    private void drawFooter(
            PDPageContentStream content,
            float pageWidth,
            int pageNumber,
            PDType1Font normal)
            throws IOException {


        String footer =
                "Merchant QR Registration System    |    Page "
                        + pageNumber;


        float fontSize = 7f;


        float width =
                normal.getStringWidth(footer)
                        / 1000f
                        * fontSize;


        writeText(
                content,
                footer,
                (pageWidth - width) / 2f,
                25,
                normal,
                fontSize,
                new Color(100, 116, 139)
        );
    }


    private void writeText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            PDType1Font font,
            float size,
            Color color)
            throws IOException {


        content.beginText();

        content.setFont(
                font,
                size
        );

        content.setNonStrokingColor(
                color
        );

        content.newLineAtOffset(
                x,
                y
        );

        content.showText(
                safeText(text)
        );

        content.endText();
    }


    private void writeWrappedText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float maxWidth,
            PDType1Font font,
            float fontSize)
            throws IOException {

        if (text == null) {
            text = "";
        }

        text = safeText(text);

        if (text.isEmpty()) {
            return;
        }

        StringBuilder line = new StringBuilder();

        int lineNumber = 0;

        for (int i = 0; i < text.length(); i++) {

            char character = text.charAt(i);

            String candidate =
                    line.toString() + character;

            float width =
                    font.getStringWidth(candidate)
                            / 1000f
                            * fontSize;

            if (width > maxWidth && line.length() > 0) {

                writeText(
                        content,
                        line.toString(),
                        x,
                        y - (lineNumber * 7),
                        font,
                        fontSize,
                        new Color(51, 65, 85)
                );

                line = new StringBuilder();

                lineNumber++;

                if (lineNumber >= 4) {
                    break;
                }

                line.append(character);

            } else {

                line.append(character);
            }
        }

        if (line.length() > 0 && lineNumber < 4) {

            writeText(
                    content,
                    line.toString(),
                    x,
                    y - (lineNumber * 7),
                    font,
                    fontSize,
                    new Color(51, 65, 85)
            );
        }
    }


    private float[] columnWidths() {

        return new float[] {

        		28f,    // SI
                42f,    // DB ID
                67f,    // MERCHANT ID
                77f,    // ACCOUNT
                115f,   // MERCHANT NAME
                68f,    // CITY
                52f,    // COUNTRY
                62f,    // POSTAL
                78f,    // TYPE
                95f,    // MOBILE
                125f,   // EMAIL
                55f,    // CHARGES
                75f,   // CREATED AT
                204f    // QR STRING
        };
    }


    private String safeText(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replaceAll(
                        "[^\\x20-\\x7E]",
                        ""
                );
    }
}
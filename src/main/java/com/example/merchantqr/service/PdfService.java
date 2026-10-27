package com.example.merchantqr.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    public byte[] createPdf(
            byte[] qrImage,
            String merchantId,
            String merchantName) {

        try (
                PDDocument document = new PDDocument();
                ByteArrayOutputStream output =
                        new ByteArrayOutputStream()
        ) {

            // ---------------------------------------------------------
            // PAGE
            // ---------------------------------------------------------

            PDPage page =
                    new PDPage(PDRectangle.A4);

            document.addPage(page);

            PDType1Font boldFont =
                    new PDType1Font(
                            Standard14Fonts.FontName.HELVETICA_BOLD
                    );

            PDType1Font normalFont =
                    new PDType1Font(
                            Standard14Fonts.FontName.HELVETICA
                    );

            float pageWidth =
                    page.getMediaBox().getWidth();

            float pageHeight =
                    page.getMediaBox().getHeight();

            try (
                    PDPageContentStream content =
                            new PDPageContentStream(
                                    document,
                                    page
                            )
            ) {

                // -----------------------------------------------------
                // OUTER BORDER
                // -----------------------------------------------------

                content.setLineWidth(2f);

                content.setStrokingColor(
                        new Color(37, 99, 235)
                );

                content.addRect(
                        30,
                        30,
                        pageWidth - 60,
                        pageHeight - 60
                );

                content.stroke();


                // -----------------------------------------------------
                // HEADER BACKGROUND
                // -----------------------------------------------------

                content.setNonStrokingColor(
                        new Color(11, 45, 92)
                );

                content.addRect(
                        30,
                        pageHeight - 120,
                        pageWidth - 60,
                        60
                );

                content.fill();


                // -----------------------------------------------------
                // HEADER TITLE
                // -----------------------------------------------------

                String title =
                        "MERCHANT QR REGISTRATION";

                float titleFontSize = 20f;

                float titleWidth =
                        boldFont.getStringWidth(title)
                                / 1000f
                                * titleFontSize;

                content.beginText();

                content.setNonStrokingColor(
                        Color.WHITE
                );

                content.setFont(
                        boldFont,
                        titleFontSize
                );

                content.newLineAtOffset(
                        (pageWidth - titleWidth) / 2f,
                        pageHeight - 90
                );

                content.showText(title);

                content.endText();


                // -----------------------------------------------------
                // HEADER SUBTITLE
                // -----------------------------------------------------

                String subtitle =
                        "Merchant Payment QR";

                float subtitleFontSize = 10f;

                float subtitleWidth =
                        normalFont.getStringWidth(subtitle)
                                / 1000f
                                * subtitleFontSize;

                content.beginText();

                content.setFont(
                        normalFont,
                        subtitleFontSize
                );

                content.newLineAtOffset(
                        (pageWidth - subtitleWidth) / 2f,
                        pageHeight - 106
                );

                content.showText(subtitle);

                content.endText();


                // -----------------------------------------------------
                // QR IMAGE
                // -----------------------------------------------------

                PDImageXObject image =
                        PDImageXObject.createFromByteArray(
                                document,
                                qrImage,
                                "merchant-qr"
                        );

                float qrSize = 285f;

                float qrX =
                        (pageWidth - qrSize) / 2f;

                float qrY = 410f;


                // QR BACKGROUND

                content.setNonStrokingColor(
                        new Color(248, 250, 252)
                );

                content.addRect(
                        qrX - 15,
                        qrY - 15,
                        qrSize + 30,
                        qrSize + 30
                );

                content.fill();


                // QR BORDER

                content.setLineWidth(1.5f);

                content.setStrokingColor(
                        new Color(203, 213, 225)
                );

                content.addRect(
                        qrX - 15,
                        qrY - 15,
                        qrSize + 30,
                        qrSize + 30
                );

                content.stroke();


                // QR IMAGE

                content.drawImage(
                        image,
                        qrX,
                        qrY,
                        qrSize,
                        qrSize
                );


                // -----------------------------------------------------
                // INFORMATION PANEL
                // -----------------------------------------------------

                float panelX = 75f;

                float panelWidth =
                        pageWidth - 150f;

                float panelY = 250f;

                float panelHeight = 105f;


                // PANEL BACKGROUND

                content.setNonStrokingColor(
                        new Color(245, 248, 252)
                );

                content.addRect(
                        panelX,
                        panelY,
                        panelWidth,
                        panelHeight
                );

                content.fill();


                // PANEL BORDER

                content.setLineWidth(1f);

                content.setStrokingColor(
                        new Color(221, 228, 238)
                );

                content.addRect(
                        panelX,
                        panelY,
                        panelWidth,
                        panelHeight
                );

                content.stroke();


                // -----------------------------------------------------
                // MERCHANT ID LABEL
                // -----------------------------------------------------

                content.beginText();

                content.setNonStrokingColor(
                        new Color(100, 116, 139)
                );

                content.setFont(
                        boldFont,
                        9f
                );

                content.newLineAtOffset(
                        panelX + 20,
                        panelY + 78
                );

                content.showText(
                        "MERCHANT ID"
                );

                content.endText();


                // -----------------------------------------------------
                // MERCHANT ID VALUE
                // -----------------------------------------------------

                content.beginText();

                content.setNonStrokingColor(
                        new Color(15, 23, 42)
                );

                content.setFont(
                        boldFont,
                        19f
                );

                content.newLineAtOffset(
                        panelX + 20,
                        panelY + 55
                );

                content.showText(
                        safePdfText(merchantId)
                );

                content.endText();


                // -----------------------------------------------------
                // MERCHANT NAME LABEL
                // -----------------------------------------------------

                content.beginText();

                content.setNonStrokingColor(
                        new Color(100, 116, 139)
                );

                content.setFont(
                        boldFont,
                        9f
                );

                content.newLineAtOffset(
                        panelX + 210,
                        panelY + 78
                );

                content.showText(
                        "MERCHANT NAME"
                );

                content.endText();


                // -----------------------------------------------------
                // MERCHANT NAME VALUE
                // -----------------------------------------------------

                content.beginText();

                content.setNonStrokingColor(
                        new Color(15, 23, 42)
                );

                content.setFont(
                        normalFont,
                        14f
                );

                content.newLineAtOffset(
                        panelX + 210,
                        panelY + 55
                );

                content.showText(
                        safePdfText(merchantName)
                );

                content.endText();

                // -----------------------------------------------------
                // INSTRUCTION
                // -----------------------------------------------------

                String instruction =
                        "Scan the QR code to make a merchant payment.";

                float instructionFontSize = 11f;

                float instructionWidth =
                        normalFont.getStringWidth(instruction)
                                / 1000f
                                * instructionFontSize;

                content.beginText();

                content.setNonStrokingColor(
                        new Color(71, 85, 105)
                );

                content.setFont(
                        normalFont,
                        instructionFontSize
                );

                content.newLineAtOffset(
                        (pageWidth - instructionWidth) / 2f,
                        210f
                );

                content.showText(instruction);

                content.endText();


                // -----------------------------------------------------
                // FOOTER
                // -----------------------------------------------------

                String footer =
                        "Merchant QR Registration System";

                float footerFontSize = 9f;

                float footerWidth =
                        normalFont.getStringWidth(footer)
                                / 1000f
                                * footerFontSize;

                content.beginText();

                content.setNonStrokingColor(
                        new Color(148, 163, 184)
                );

                content.setFont(
                        normalFont,
                        footerFontSize
                );

                content.newLineAtOffset(
                        (pageWidth - footerWidth) / 2f,
                        65f
                );

                content.showText(footer);

                content.endText();


                // -----------------------------------------------------
                // FOOTER LINE
                // -----------------------------------------------------

                content.setLineWidth(0.8f);

                content.setStrokingColor(
                        new Color(226, 232, 240)
                );

                content.moveTo(
                        80,
                        85
                );

                content.lineTo(
                        pageWidth - 80,
                        85
                );

                content.stroke();
            }


            // ---------------------------------------------------------
            // SAVE PDF
            // ---------------------------------------------------------

            document.save(output);

            return output.toByteArray();

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Could not create PDF",
                    e
            );
        }
    }


    // -------------------------------------------------------------
    // PDF TEXT SAFETY
    // -------------------------------------------------------------

    private String safePdfText(String value) {

        if (value == null) {
            return "";
        }

        return value.replaceAll(
                "[^\\x20-\\x7E]",
                ""
        );
    }
}      
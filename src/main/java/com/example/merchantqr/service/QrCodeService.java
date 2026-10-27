package com.example.merchantqr.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;

import com.google.zxing.client.j2se.MatrixToImageWriter;

import com.google.zxing.common.BitMatrix;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

@Service
public class QrCodeService {

    public byte[] generatePng(
            String value,
            int width,
            int height) {

        try {

            Map<EncodeHintType, Object> hints = new HashMap<>();

            /*
             * QR code margin.
             */
            hints.put(EncodeHintType.MARGIN, 2);

            /*
             * Convert the QR string into
             * a QR BitMatrix.
             */
            BitMatrix matrix = new MultiFormatWriter().encode(
                            value,
                            BarcodeFormat.QR_CODE,
                            width,
                            height,
                            hints
                    );

            /*
             * Convert the BitMatrix into
             * PNG bytes.
             */
            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

                MatrixToImageWriter.writeToStream(
                        matrix,
                        "PNG",
                        output
                );

                return output.toByteArray();
            }

        } catch (
                WriterException |
                IOException e
        ) {

            throw new IllegalStateException("Could not generate QR code", e);
        }
    }
}
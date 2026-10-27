package com.example.merchantqr.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class BrowserLauncher {

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {

        try {

            String url =
                    "http://localhost:8080/merchant-registration";

            if (Desktop.isDesktopSupported()
                    && Desktop.getDesktop()
                    .isSupported(Desktop.Action.BROWSE)) {

                Desktop.getDesktop().browse(
                        new URI(url)
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Could not open browser automatically: "
                            + e.getMessage()
            );
        }
    }
}
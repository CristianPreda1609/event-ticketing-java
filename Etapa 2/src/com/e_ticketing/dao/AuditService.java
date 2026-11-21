package com.e_ticketing.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditService {
    private static final String AUDIT_FILE = "src/com/e_ticketing/resources/audit.csv";

    public static void log(String actiune) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(AUDIT_FILE, true))) {
            String timestamp = LocalDateTime.now().toString();
            bw.write(actiune + "," + timestamp);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Eroare la scrierea în audit: " + e.getMessage());
        }
    }
}
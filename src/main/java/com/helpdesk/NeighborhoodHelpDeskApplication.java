package com.helpdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NeighborhoodHelpDeskApplication {

    public static void main(String[] args) {
        try {
            // Validate MySQL configuration
            validateDatabaseConfiguration();

            SpringApplication.run(NeighborhoodHelpDeskApplication.class, args);
            System.out.println("🚀 Neighborhood Help Desk started successfully!");
            System.out.println("📱 Open your browser and go to: http://localhost:8080");
            System.out.println("🗄️ Using MySQL database for data persistence");
        } catch (Exception e) {
            System.err.println("❌ Failed to start application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void validateDatabaseConfiguration() {
        String datasourceUrl = System.getProperty("spring.datasource.url",
            System.getenv("SPRING_DATASOURCE_URL"));

        if (datasourceUrl != null && !datasourceUrl.contains("mysql")) {
            System.err.println("⚠️ Warning: This application is designed for MySQL database only!");
        }
    }
}

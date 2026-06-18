package com.hospitalmanagementsystem;

import com.hospitalmanagementsystem.repository.AdminRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class HospitalManagementSystemApplication {

    public static void main(String[] args) {

        ApplicationContext context =
                SpringApplication.run(HospitalManagementSystemApplication.class, args);

        // Run staff data migration on application startup
        try {
            AdminRepository adminRepository =
                    context.getBean(AdminRepository.class);

            adminRepository.migrateStaffToRoleFiles();

        } catch (Exception e) {
            System.err.println("WARNING: Could not run staff data migration");
            System.err.println("Startup Error: " + e.getMessage());
        }
    }
}
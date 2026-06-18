package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.models.Patient;
import com.hospitalmanagementsystem.models.Staff;
import com.hospitalmanagementsystem.services.AdminManagementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private AdminManagementService adminManagementService;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    // ================= LOGIN PAGE =================

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ================= PROCESS LOGIN =================

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(defaultValue = "admin") String role,
                               HttpSession session) {

        username = username.trim();
        password = password.trim();
        role = role.trim();

        System.out.println("LOGIN ATTEMPT");
        System.out.println("USERNAME = " + username);
        System.out.println("ROLE = " + role);

        // ================= ADMIN LOGIN =================

        if (role.equalsIgnoreCase("admin")) {

            if (username.equals(adminUsername) &&
                    password.equals(adminPassword)) {

                session.setAttribute("loggedUser", username);
                session.setAttribute("role", "ADMIN");
                session.setAttribute("staffId", null);
                session.setAttribute("staffName", "Admin");

                System.out.println("ADMIN LOGIN SUCCESS");

                return "redirect:/admin/dashboard";
            }

            System.out.println("ADMIN LOGIN FAILED");

            return "redirect:/login?error=true&role=admin";
        }

        // ================= PATIENT LOGIN =================

        if (role.equalsIgnoreCase("patient")) {

            Patient patient = authenticatePatient(username, password);

            if (patient != null) {

                session.setAttribute("loggedUser", patient.getUsername());
                session.setAttribute("role", "PATIENT");
                session.setAttribute("patientId", patient.getPatientId());
                session.setAttribute("patientName", patient.getFullName());
                session.setAttribute("patientEmail", patient.getEmail());

                System.out.println("PATIENT LOGIN SUCCESS");
                System.out.println("PATIENT ID = " + patient.getPatientId());
                System.out.println("PATIENT NAME = " + patient.getFullName());

                return "redirect:/patient/dashboard";
            }

            System.out.println("PATIENT LOGIN FAILED");

            return "redirect:/login?error=true&role=patient";
        }

        // ================= STAFF LOGIN =================
        // Doctor, Nurse, Receptionist, Pharmacist

        Staff staff = authenticateStaff(username, password, role);

        if (staff != null) {

            String staffRole = staff.getRole().trim().toUpperCase();

            session.setAttribute("loggedUser", staff.getUsername());
            session.setAttribute("role", staffRole);
            session.setAttribute("staffId", staff.getId());
            session.setAttribute("staffName", staff.getFullName());

            System.out.println("STAFF LOGIN SUCCESS");
            System.out.println("STAFF ID = " + staff.getId());
            System.out.println("STAFF NAME = " + staff.getFullName());
            System.out.println("STAFF ROLE = " + staffRole);

            return redirectToRoleDashboard(staffRole);
        }

        System.out.println("STAFF LOGIN FAILED");

        return "redirect:/login?error=true&role=" + role;
    }

    // ================= AUTHENTICATE PATIENT =================

    private Patient authenticatePatient(String username, String password) {

        try {

            List<Patient> patients = adminManagementService.getAllPatients();

            System.out.println("TOTAL PATIENTS LOADED = " + patients.size());

            for (Patient patient : patients) {

                if (patient.getUsername() != null &&
                        patient.getPassword() != null &&
                        patient.getUsername().trim().equalsIgnoreCase(username) &&
                        patient.getPassword().trim().equals(password)) {

                    return patient;
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR DURING PATIENT AUTHENTICATION");
            e.printStackTrace();
        }

        return null;
    }

    // ================= AUTHENTICATE STAFF =================

    private Staff authenticateStaff(String username, String password, String role) {

        try {

            List<Staff> staffList = adminManagementService.getAllStaff();

            System.out.println("TOTAL STAFF LOADED = " + staffList.size());

            for (Staff staff : staffList) {

                System.out.println(
                        "CHECKING STAFF: "
                                + staff.getUsername()
                                + " | ROLE: "
                                + staff.getRole()
                );

                if (staff.getUsername() != null &&
                        staff.getPassword() != null &&
                        staff.getRole() != null &&
                        staff.getUsername().trim().equalsIgnoreCase(username) &&
                        staff.getPassword().trim().equals(password) &&
                        staff.getRole().trim().equalsIgnoreCase(role)) {

                    return staff;
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR DURING STAFF AUTHENTICATION");
            e.printStackTrace();
        }

        return null;
    }

    // ================= ROLE DASHBOARD REDIRECT =================

    private String redirectToRoleDashboard(String role) {

        switch (role.toUpperCase()) {

            case "DOCTOR":
                return "redirect:/doctor/dashboard";

            case "NURSE":
                return "redirect:/nurse/dashboard";

            case "RECEPTIONIST":
                return "redirect:/receptionist/dashboard";

            case "PHARMACIST":
                return "redirect:/pharmacist/dashboard";

            default:
                return "redirect:/login?error=true";
        }
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}
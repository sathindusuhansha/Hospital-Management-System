package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.models.Patient;
import com.hospitalmanagementsystem.services.AdminManagementService;
import com.hospitalmanagementsystem.services.AppointmentService;
import com.hospitalmanagementsystem.services.MedicalRecordService;
import com.hospitalmanagementsystem.services.MedicineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private AdminManagementService adminService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private MedicineService medicineService;

    // ================= PATIENT REGISTRATION PAGE =================

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("patient", new Patient());
        return "register";
    }

    // ================= SAVE PATIENT REGISTRATION =================

    @PostMapping("/register")
    public String registerPatient(@ModelAttribute Patient patient, Model model) {

        System.out.println("PATIENT REGISTRATION HIT");
        System.out.println("Registering patient: " + patient.getFullName());

        String errorMessage = adminService.addPatient(patient);

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("patient", patient);
            return "register";
        }

        System.out.println("PATIENT REGISTERED SUCCESSFULLY");

        return "redirect:/login?registered=true&role=patient";
    }

    // ================= DASHBOARD =================

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        System.out.println("PATIENT DASHBOARD CONTROLLER HIT");

        String role = (String) session.getAttribute("role");
        String patientId = (String) session.getAttribute("patientId");

        System.out.println("ROLE = " + role);
        System.out.println("PATIENT ID = " + patientId);

        if (role == null || !role.equalsIgnoreCase("PATIENT")) {
            System.out.println("INVALID SESSION");
            return "redirect:/login";
        }

        Patient patient = adminService.getPatientById(patientId);

        if (patient == null) {
            System.out.println("PATIENT NOT FOUND");
            return "redirect:/login";
        }

        int appointmentCount =
                appointmentService.getAppointmentsByPatientId(patientId).size();

        int medicalRecordCount =
                medicalRecordService.getRecordsByPatientId(patientId).size();

        int medicineCount =
                medicineService.getOrdersByPatientId(patientId).size();

        model.addAttribute("patient", patient);
        model.addAttribute("patientName", patient.getFullName());
        model.addAttribute("patientEmail", patient.getEmail());
        model.addAttribute("patientPhone", patient.getPhone());
        model.addAttribute("patientDOB", patient.getDateOfBirth());
        model.addAttribute("patientGender", patient.getGender());
        model.addAttribute("patientAddress", patient.getAddress());
        model.addAttribute("patientCity", patient.getCity());
        model.addAttribute("medicalHistory", patient.getMedicalHistory());

        model.addAttribute("appointmentCount", appointmentCount);
        model.addAttribute("medicalRecordCount", medicalRecordCount);
        model.addAttribute("medicineCount", medicineCount);

        System.out.println("APPOINTMENT COUNT = " + appointmentCount);
        System.out.println("MEDICAL RECORD COUNT = " + medicalRecordCount);
        System.out.println("MEDICINE COUNT = " + medicineCount);

        return "patient/dashboard";
    }

    // Appointments are handled by AppointmentController.
    // Medical records are handled by MedicalRecordController.
    // Medicine is handled by MedicineController.

    // ================= BILLING =================

}
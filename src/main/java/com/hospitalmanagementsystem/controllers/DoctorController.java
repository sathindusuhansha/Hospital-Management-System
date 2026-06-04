package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.models.Appointment;
import com.hospitalmanagementsystem.services.AdminManagementService;
import com.hospitalmanagementsystem.services.AppointmentService;
import com.hospitalmanagementsystem.services.MedicalRecordService;
import com.hospitalmanagementsystem.services.MedicineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class DoctorController {

    @Autowired
    private AdminManagementService adminService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String doctorId = (String) session.getAttribute("staffId");
        String doctorName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("DOCTOR")) {
            return "redirect:/login";
        }

        List<Appointment> doctorAppointments =
                appointmentService.getAppointmentsByDoctorId(doctorId);

        Set<String> uniquePatientIds = new HashSet<>();

        for (Appointment appointment : doctorAppointments) {
            if (appointment.getPatientId() != null &&
                    !appointment.getPatientId().isEmpty()) {
                uniquePatientIds.add(appointment.getPatientId());
            }
        }

        int appointmentCount = doctorAppointments.size();
        int medicalRecordCount =
                medicalRecordService.getRecordsByDoctorId(doctorId).size();
        int medicineCount =
                medicineService.getOrdersByDoctorId(doctorId).size();
        int patientCount =
                uniquePatientIds.size();

        model.addAttribute("doctorId", doctorId);
        model.addAttribute("doctorName", doctorName);
        model.addAttribute("appointmentCount", appointmentCount);
        model.addAttribute("medicalRecordCount", medicalRecordCount);
        model.addAttribute("medicineCount", medicineCount);
        model.addAttribute("patientCount", patientCount);

        return "doctor/dashboard";
    }
}
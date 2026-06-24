package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.models.Patient;
import com.hospitalmanagementsystem.models.Staff;
import com.hospitalmanagementsystem.services.AdminManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @Autowired
    private AdminManagementService adminService;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalPatients", adminService.getAllPatients().size());
        model.addAttribute("totalStaff", adminService.getAllStaff().size());
        return "admin/dashboard";
    }

    // ================= PATIENT MANAGEMENT =================

    @GetMapping("/admin/patients")
    public String patientsPage(Model model) {
        model.addAttribute("patients", adminService.getAllPatients());
        model.addAttribute("patient", new Patient());
        return "admin/patients";
    }

    @PostMapping("/admin/patients/add")
    public String addPatient(@ModelAttribute Patient patient, Model model) {

        String errorMessage = adminService.addPatient(patient);

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("patient", patient);
            model.addAttribute("patients", adminService.getAllPatients());
            return "admin/patients";
        }

        return "redirect:/admin/patients";
    }

    @GetMapping("/admin/patients/edit/{patientId}")
    public String editPatientPage(@PathVariable String patientId, Model model) {

        Patient patient = adminService.getPatientById(patientId);

        if (patient == null) {
            return "redirect:/admin/patients";
        }

        model.addAttribute("patient", patient);
        return "admin/edit-patient";
    }

    @PostMapping("/admin/patients/update")
    public String updatePatient(@ModelAttribute Patient patient, Model model) {

        String errorMessage = adminService.updatePatient(patient);

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("patient", patient);
            model.addAttribute("patients", adminService.getAllPatients());
            return "admin/patients";
        }

        return "redirect:/admin/patients";
    }

    @GetMapping("/admin/patients/delete/{patientId}")
    public String deletePatient(@PathVariable String patientId) {
        adminService.deletePatient(patientId);
        return "redirect:/admin/patients";
    }

    // ================= STAFF MANAGEMENT =================

    @GetMapping("/admin/staff")
    public String staffPage(Model model) {
        model.addAttribute("staffList", adminService.getAllStaff());
        model.addAttribute("staff", new Staff());
        return "admin/staff";
    }

    @PostMapping("/admin/staff/add")
    public String addStaff(@ModelAttribute Staff staff) {
        adminService.addStaff(staff);
        return "redirect:/admin/staff";
    }

    @GetMapping("/admin/staff/edit/{id}")
    public String editStaffPage(@PathVariable String id, Model model) {

        Staff staff = adminService.getStaffById(id);

        if (staff == null) {
            return "redirect:/admin/staff";
        }

        model.addAttribute("staff", staff);
        return "admin/edit-staff";
    }

    @PostMapping("/admin/staff/update")
    public String updateStaff(@ModelAttribute Staff staff) {
        adminService.updateStaff(staff);
        return "redirect:/admin/staff";
    }

    @GetMapping("/admin/staff/delete/{id}")
    public String deleteStaff(@PathVariable String id) {
        adminService.deleteStaff(id);
        return "redirect:/admin/staff";
    }
}
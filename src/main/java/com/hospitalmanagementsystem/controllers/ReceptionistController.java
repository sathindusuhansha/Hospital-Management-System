package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.services.AdminManagementService;
import com.hospitalmanagementsystem.services.AppointmentService;
import com.hospitalmanagementsystem.services.BillingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/receptionist")

public class ReceptionistController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private AdminManagementService adminService;

    // ================= DASHBOARD ONLY =================

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String staffId = (String) session.getAttribute("staffId");
        String staffName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("RECEPTIONIST")) {
            return "redirect:/login";
        }

        model.addAttribute("staffId", staffId);
        model.addAttribute("receptionistName", staffName);
        model.addAttribute("staffName", staffName);

        model.addAttribute("appointmentCount",
                appointmentService.getAllAppointments().size());

        model.addAttribute("patientCount",
                adminService.getAllPatients().size());

        model.addAttribute("billCount",
                billingService.getAllBills().size());

        model.addAttribute("pendingBillCount",
                billingService.getAllBills()
                        .stream()
                        .filter(bill -> bill.getPaymentStatus() == null ||
                                bill.getPaymentStatus().equalsIgnoreCase("Pending"))
                        .count());

        return "receptionist/dashboard";
    }
}
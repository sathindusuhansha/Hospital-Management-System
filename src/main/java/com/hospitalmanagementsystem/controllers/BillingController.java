package com.hospitalmanagementsystem.controllers;
// Billing management functions for receptionist and patient billing workflows

import com.hospitalmanagementsystem.models.Bill;
import com.hospitalmanagementsystem.models.Patient;
import com.hospitalmanagementsystem.services.AdminManagementService;
import com.hospitalmanagementsystem.services.BillingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BillingController {

    @Autowired
    private BillingService billingService;

    @Autowired
    private AdminManagementService adminService;

    // ================= RECEPTIONIST BILLING PAGE =================

    @GetMapping("/receptionist/billing")
    public String receptionistBilling(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String staffName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("RECEPTIONIST")) {
            return "redirect:/login";
        }

        model.addAttribute("bill", new Bill());
        model.addAttribute("bills", billingService.getAllBills());
        model.addAttribute("patients", adminService.getAllPatients());
        model.addAttribute("receptionistName", staffName);
        model.addAttribute("staffName", staffName);

        return "receptionist/billing";
    }

    // ================= ADD BILL =================

    @PostMapping("/receptionist/billing/add")
    public String addBill(@ModelAttribute Bill bill,
                          HttpSession session) {

        String role = (String) session.getAttribute("role");
        String staffName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("RECEPTIONIST")) {
            return "redirect:/login";
        }

        Patient patient = adminService.getPatientById(bill.getPatientId());

        if (patient != null) {
            bill.setPatientName(patient.getFullName());
        }

        bill.setCreatedBy(staffName);

        billingService.addBill(bill);

        return "redirect:/receptionist/billing";
    }

    // ================= UPDATE BILL =================

    @PostMapping("/receptionist/billing/update")
    public String updateBill(@ModelAttribute Bill bill,
                             HttpSession session) {

        String role = (String) session.getAttribute("role");
        String staffName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("RECEPTIONIST")) {
            return "redirect:/login";
        }

        Patient patient = adminService.getPatientById(bill.getPatientId());

        if (patient != null) {
            bill.setPatientName(patient.getFullName());
        }

        bill.setCreatedBy(staffName);

        billingService.updateBill(bill);

        return "redirect:/receptionist/billing";
    }

    // ================= DELETE BILL =================

    @GetMapping("/receptionist/billing/delete/{billId}")
    public String deleteBill(@PathVariable String billId,
                             HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equalsIgnoreCase("RECEPTIONIST")) {
            return "redirect:/login";
        }

        billingService.deleteBill(billId);

        return "redirect:/receptionist/billing";
    }

    // ================= PATIENT BILLING PAGE =================

    @GetMapping("/patient/billing")
    public String patientBilling(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String patientId = (String) session.getAttribute("patientId");
        String patientName = (String) session.getAttribute("patientName");
        String patientEmail = (String) session.getAttribute("patientEmail");

        if (role == null || !role.equalsIgnoreCase("PATIENT")) {
            return "redirect:/login";
        }

        model.addAttribute("bills", billingService.getBillsByPatientId(patientId));
        model.addAttribute("patientId", patientId);
        model.addAttribute("patientName", patientName);
        model.addAttribute("patientEmail", patientEmail);

        return "patient/billing";
    }

    // ================= PATIENT PAY BILL =================

    @PostMapping("/patient/billing/pay/{billId}")
    public String payBill(@PathVariable String billId,
                          @RequestParam(defaultValue = "Online") String paymentMethod,
                          HttpSession session) {

        String role = (String) session.getAttribute("role");
        String patientId = (String) session.getAttribute("patientId");

        if (role == null || !role.equalsIgnoreCase("PATIENT")) {
            return "redirect:/login";
        }

        Bill bill = billingService.getBillById(billId);

        if (bill != null &&
                bill.getPatientId() != null &&
                bill.getPatientId().equalsIgnoreCase(patientId)) {

            billingService.markBillAsPaid(billId, paymentMethod);
        }

        return "redirect:/patient/billing";
    }
}
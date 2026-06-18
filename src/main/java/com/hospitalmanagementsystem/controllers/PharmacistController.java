package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.services.MedicineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pharmacist")
public class PharmacistController {

    @Autowired
    private MedicineService medicineService;

    // ================= PHARMACIST DASHBOARD =================

    @GetMapping("/dashboard")
    public String pharmacistDashboard(HttpSession session, Model model) {
        int totalOrders = medicineService.getAllOrders().size();
        model.addAttribute("totalOrders", totalOrders);

        String role = (String) session.getAttribute("role");
        String staffId = (String) session.getAttribute("staffId");
        String staffName = (String) session.getAttribute("staffName");
        String username = (String) session.getAttribute("loggedUser");

        System.out.println("PHARMACIST DASHBOARD CONTROLLER HIT");
        System.out.println("ROLE = " + role);
        System.out.println("STAFF ID = " + staffId);
        System.out.println("STAFF NAME = " + staffName);

        if (role == null || !role.equalsIgnoreCase("PHARMACIST")) {

            System.out.println("UNAUTHORIZED ACCESS TO PHARMACIST DASHBOARD");

            return "redirect:/login";
        }

        model.addAttribute("staffId", staffId);
        model.addAttribute("staffName", staffName);
        model.addAttribute("username", username);
        model.addAttribute("role", role);

        System.out.println(
                "PHARMACIST DASHBOARD LOADED SUCCESSFULLY - "
                        + staffId + " | "
                        + staffName
        );

        return "pharmacist/dashboard";
    }
}
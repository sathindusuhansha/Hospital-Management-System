package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.services.WardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nurse")
public class NurseController {

    @Autowired
    private WardService wardService;

    // ================= NURSE DASHBOARD =================

    @GetMapping("/dashboard")
    public String nurseDashboard(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String nurseId = (String) session.getAttribute("staffId");
        String nurseName = (String) session.getAttribute("staffName");
        String username = (String) session.getAttribute("loggedUser");

        if (role == null || !role.equalsIgnoreCase("NURSE")) {
            return "redirect:/login";
        }

        model.addAttribute("nurseId", nurseId);
        model.addAttribute("nurseName", nurseName);
        model.addAttribute("username", username);

        model.addAttribute("totalWardRecords", wardService.getTotalWardRecordsCount());
        model.addAttribute("activeAdmissions", wardService.getActiveAdmissionsCount());
        model.addAttribute("dischargedCount", wardService.getDischargedCount());
        model.addAttribute("availableBeds", wardService.getAvailableBedsCount());

        model.addAttribute("recentWardRooms", wardService.getAllWardRooms());

        return "nurse/dashboard";
    }
}
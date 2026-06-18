package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.models.Patient;
import com.hospitalmanagementsystem.models.WardRoom;
import com.hospitalmanagementsystem.services.AdminManagementService;
import com.hospitalmanagementsystem.services.WardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nurse")
public class NurseController {

    @Autowired
    private WardService wardService;

    @Autowired
    private AdminManagementService adminService;

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

    // ================= WARD ROOM PAGE =================

    @GetMapping("/wardroom")
    public String wardRoomPage(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String nurseId = (String) session.getAttribute("staffId");
        String nurseName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("NURSE")) {
            return "redirect:/login";
        }

        model.addAttribute("wardRoom", new WardRoom());
        model.addAttribute("wardRooms", wardService.getAllWardRooms());
        model.addAttribute("patients", adminService.getAllPatients());

        model.addAttribute("nurseId", nurseId);
        model.addAttribute("nurseName", nurseName);

        model.addAttribute("totalWardRecords", wardService.getTotalWardRecordsCount());
        model.addAttribute("activeAdmissions", wardService.getActiveAdmissionsCount());
        model.addAttribute("dischargedCount", wardService.getDischargedCount());
        model.addAttribute("availableBeds", wardService.getAvailableBedsCount());

        return "nurse/wardroom";
    }

    // ================= ADD / ADMIT PATIENT =================

    @PostMapping("/wardroom/add")
    public String addWardRoom(@ModelAttribute WardRoom wardRoom,
                              HttpSession session) {

        String role = (String) session.getAttribute("role");
        String nurseId = (String) session.getAttribute("staffId");
        String nurseName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("NURSE")) {
            return "redirect:/login";
        }

        Patient patient = adminService.getPatientById(wardRoom.getPatientId());

        if (patient != null) {
            wardRoom.setPatientName(patient.getFullName());
        }

        wardRoom.setNurseId(nurseId);
        wardRoom.setNurseName(nurseName);

        wardService.addWardRoom(wardRoom);

        return "redirect:/nurse/wardroom";
    }

    // ================= UPDATE WARD ROOM =================

    @PostMapping("/wardroom/update")
    public String updateWardRoom(@ModelAttribute WardRoom wardRoom,
                                 HttpSession session) {

        String role = (String) session.getAttribute("role");
        String nurseId = (String) session.getAttribute("staffId");
        String nurseName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("NURSE")) {
            return "redirect:/login";
        }

        WardRoom existingWardRoom =
                wardService.getWardRoomById(wardRoom.getWardId());

        if (existingWardRoom == null) {
            return "redirect:/nurse/wardroom";
        }

        Patient patient = adminService.getPatientById(wardRoom.getPatientId());

        if (patient != null) {
            wardRoom.setPatientName(patient.getFullName());
        }

        wardRoom.setNurseId(nurseId);
        wardRoom.setNurseName(nurseName);

        if (wardRoom.getAdmissionDate() == null ||
                wardRoom.getAdmissionDate().isEmpty()) {
            wardRoom.setAdmissionDate(existingWardRoom.getAdmissionDate());
        }

        if (wardRoom.getDischargeDate() == null ||
                wardRoom.getDischargeDate().isEmpty()) {
            wardRoom.setDischargeDate(existingWardRoom.getDischargeDate());
        }

        wardService.updateWardRoom(wardRoom);

        return "redirect:/nurse/wardroom";
    }

    // ================= DELETE WARD ROOM RECORD =================

    @GetMapping("/wardroom/delete/{wardId}")
    public String deleteWardRoom(@PathVariable String wardId,
                                 HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equalsIgnoreCase("NURSE")) {
            return "redirect:/login";
        }

        wardService.deleteWardRoom(wardId);

        return "redirect:/nurse/wardroom";
    }

    // ================= DISCHARGE PATIENT =================

    @GetMapping("/wardroom/discharge/{wardId}")
    public String dischargePatient(@PathVariable String wardId,
                                   HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equalsIgnoreCase("NURSE")) {
            return "redirect:/login";
        }

        wardService.dischargePatient(wardId);

        return "redirect:/nurse/wardroom";
    }
}
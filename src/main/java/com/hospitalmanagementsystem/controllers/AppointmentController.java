package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.models.Appointment;
import com.hospitalmanagementsystem.models.Staff;
import com.hospitalmanagementsystem.services.AdminManagementService;
import com.hospitalmanagementsystem.services.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AdminManagementService adminService;

    // ================= PATIENT APPOINTMENTS =================

    @GetMapping("/patient/appointments")
    public String patientAppointments(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String patientId = (String) session.getAttribute("patientId");
        String patientName = (String) session.getAttribute("patientName");

        if (role == null || !role.equals("PATIENT")) {
            return "redirect:/login";
        }

        List<Staff> doctors = adminService.getAllStaff()
                .stream()
                .filter(staff -> staff.getRole() != null &&
                        staff.getRole().equalsIgnoreCase("DOCTOR"))
                .collect(Collectors.toList());

        List<String> categories = doctors.stream()
                .map(Staff::getDepartment)
                .filter(dept -> dept != null && !dept.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("appointment", new Appointment());

        // IMPORTANT: only logged-in patient's appointments
        model.addAttribute("appointments",
                appointmentService.getAppointmentsByPatientId(patientId));

        model.addAttribute("doctors", doctors);
        model.addAttribute("categories", categories);
        model.addAttribute("patientId", patientId);
        model.addAttribute("patientName", patientName);

        return "patient/appointments";
    }

    @PostMapping("/patient/appointments/add")
    public String addPatientAppointment(@ModelAttribute Appointment appointment,
                                        HttpSession session) {

        String role = (String) session.getAttribute("role");
        String patientId = (String) session.getAttribute("patientId");
        String patientName = (String) session.getAttribute("patientName");

        if (role == null || !role.equals("PATIENT")) {
            return "redirect:/login";
        }

        appointment.setPatientId(patientId);
        appointment.setPatientName(patientName);
        appointment.setCreatedBy("PATIENT");
        appointment.setStatus("Pending");

        appointmentService.addAppointment(appointment);

        return "redirect:/patient/appointments";
    }

    @PostMapping("/patient/appointments/update")
    public String updatePatientAppointment(@ModelAttribute Appointment appointment,
                                           HttpSession session) {

        String role = (String) session.getAttribute("role");
        String patientId = (String) session.getAttribute("patientId");
        String patientName = (String) session.getAttribute("patientName");

        if (role == null || !role.equals("PATIENT")) {
            return "redirect:/login";
        }

        Appointment existing =
                appointmentService.getAppointmentById(appointment.getAppointmentId());

        if (existing == null || !patientId.equals(existing.getPatientId())) {
            return "redirect:/patient/appointments";
        }

        appointment.setPatientId(patientId);
        appointment.setPatientName(patientName);
        appointment.setCreatedBy("PATIENT");
        appointment.setStatus(existing.getStatus());

        appointmentService.updateAppointment(appointment);

        return "redirect:/patient/appointments";
    }

    @GetMapping("/patient/appointments/delete/{appointmentId}")
    public String deletePatientAppointment(@PathVariable String appointmentId,
                                           HttpSession session) {

        String role = (String) session.getAttribute("role");
        String patientId = (String) session.getAttribute("patientId");

        if (role == null || !role.equals("PATIENT")) {
            return "redirect:/login";
        }

        Appointment existing =
                appointmentService.getAppointmentById(appointmentId);

        if (existing != null && patientId.equals(existing.getPatientId())) {
            appointmentService.deleteAppointment(appointmentId);
        }

        return "redirect:/patient/appointments";
    }

    @GetMapping("/patient/appointments/cancel/{appointmentId}")
    public String cancelPatientAppointment(@PathVariable String appointmentId,
                                           HttpSession session) {

        String role = (String) session.getAttribute("role");
        String patientId = (String) session.getAttribute("patientId");

        if (role == null || !role.equals("PATIENT")) {
            return "redirect:/login";
        }

        Appointment existing =
                appointmentService.getAppointmentById(appointmentId);

        if (existing != null && patientId.equals(existing.getPatientId())) {
            appointmentService.cancelAppointment(appointmentId);
        }

        return "redirect:/patient/appointments";
    }

    // ================= RECEPTIONIST APPOINTMENTS =================

    @GetMapping("/receptionist/appointments")
    public String receptionistAppointments(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("RECEPTIONIST")) {
            return "redirect:/login";
        }

        List<Staff> doctors = adminService.getAllStaff()
                .stream()
                .filter(staff -> staff.getRole() != null &&
                        staff.getRole().equalsIgnoreCase("DOCTOR"))
                .collect(Collectors.toList());

        List<String> categories = doctors.stream()
                .map(Staff::getDepartment)
                .filter(dept -> dept != null && !dept.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("appointment", new Appointment());
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        model.addAttribute("doctors", doctors);
        model.addAttribute("categories", categories);
        model.addAttribute("patients", adminService.getAllPatients());

        return "receptionist/appointments";
    }

    @PostMapping("/receptionist/appointments/add")
    public String addReceptionistAppointment(@ModelAttribute Appointment appointment) {

        appointment.setCreatedBy("RECEPTIONIST");

        if (appointment.getStatus() == null || appointment.getStatus().isEmpty()) {
            appointment.setStatus("Confirmed");
        }

        appointmentService.addAppointment(appointment);

        return "redirect:/receptionist/appointments";
    }

    @PostMapping("/receptionist/appointments/update")
    public String updateReceptionistAppointment(@ModelAttribute Appointment appointment) {
        appointment.setCreatedBy("RECEPTIONIST");
        appointmentService.updateAppointment(appointment);
        return "redirect:/receptionist/appointments";
    }

    @GetMapping("/receptionist/appointments/delete/{appointmentId}")
    public String deleteReceptionistAppointment(@PathVariable String appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return "redirect:/receptionist/appointments";
    }

    @GetMapping("/receptionist/appointments/cancel/{appointmentId}")
    public String cancelReceptionistAppointment(@PathVariable String appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return "redirect:/receptionist/appointments";
    }

    // ================= DOCTOR APPOINTMENTS =================

    @GetMapping("/doctor/appointments")
    public String doctorAppointments(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String doctorId = (String) session.getAttribute("staffId");
        String doctorName = (String) session.getAttribute("staffName");

        if (role == null || !role.equals("DOCTOR")) {
            return "redirect:/login";
        }

        model.addAttribute("appointments",
                appointmentService.getAppointmentsByDoctorId(doctorId));

        model.addAttribute("doctorId", doctorId);
        model.addAttribute("doctorName", doctorName);

        return "doctor/appointments";
    }
}
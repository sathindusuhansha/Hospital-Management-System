package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.models.Appointment;
import com.hospitalmanagementsystem.models.MedicineOrder;
import com.hospitalmanagementsystem.models.Patient;
import com.hospitalmanagementsystem.services.AdminManagementService;
import com.hospitalmanagementsystem.services.AppointmentService;
import com.hospitalmanagementsystem.services.MedicineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private AdminManagementService adminService;

    @Autowired
    private AppointmentService appointmentService;

    // ================= PHARMACIST MEDICINE ORDERS =================

    @GetMapping("/pharmacist/medicine-orders")
    public String pharmacistOrders(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equalsIgnoreCase("PHARMACIST")) {
            return "redirect:/login";
        }

        model.addAttribute("orders", medicineService.getAllOrders());

        return "pharmacist/medicine-orders";
    }

    // ================= DOCTOR MEDICINE PAGE =================

    @GetMapping("/doctor/medicine")
    public String doctorMedicine(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String doctorId = (String) session.getAttribute("staffId");
        String doctorName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("DOCTOR")) {
            return "redirect:/login";
        }

        List<Patient> doctorPatients = getPatientsForDoctor(doctorId);

        List<MedicineOrder> allOrders = medicineService.getAllOrders();
        List<MedicineOrder> doctorOrders = medicineService.getOrdersByDoctorId(doctorId);

        System.out.println("========== DOCTOR MEDICINE PAGE ==========");
        System.out.println("LOGGED DOCTOR ID = " + doctorId);
        System.out.println("LOGGED DOCTOR NAME = " + doctorName);
        System.out.println("ALL MEDICINE ORDERS COUNT = " + allOrders.size());
        System.out.println("DOCTOR MEDICINE ORDERS COUNT = " + doctorOrders.size());

        for (MedicineOrder order : allOrders) {
            System.out.println(
                    "ORDER ID = " + order.getOrderId()
                            + " | JSON DOCTOR ID = " + order.getDoctorId()
                            + " | PATIENT = " + order.getPatientName()
                            + " | MEDICINE = " + order.getMedicineName()
            );
        }

        model.addAttribute("orders", doctorOrders);
        model.addAttribute("medicineOrder", new MedicineOrder());
        model.addAttribute("patients", doctorPatients);
        model.addAttribute("doctorId", doctorId);
        model.addAttribute("doctorName", doctorName);

        return "doctor/medicine";
    }

    // ================= ADD MEDICINE =================

    @PostMapping("/doctor/medicine/add")
    public String addMedicine(@ModelAttribute MedicineOrder medicineOrder,
                              HttpSession session) {

        String role = (String) session.getAttribute("role");
        String doctorId = (String) session.getAttribute("staffId");
        String doctorName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("DOCTOR")) {
            return "redirect:/login";
        }

        System.out.println("========== ADD MEDICINE ==========");
        System.out.println("SESSION DOCTOR ID = " + doctorId);
        System.out.println("FORM PATIENT ID = " + medicineOrder.getPatientId());
        System.out.println("FORM MEDICINE NAME = " + medicineOrder.getMedicineName());

        if (!isPatientAssignedToDoctor(medicineOrder.getPatientId(), doctorId)) {
            System.out.println("DOCTOR TRIED TO ADD MEDICINE FOR UNASSIGNED PATIENT");
            return "redirect:/doctor/medicine";
        }

        Patient patient = adminService.getPatientById(medicineOrder.getPatientId());

        if (patient != null) {
            medicineOrder.setPatientName(patient.getFullName());
        }

        medicineOrder.setDoctorId(doctorId);
        medicineOrder.setDoctorName(doctorName);

        if (medicineOrder.getStatus() == null || medicineOrder.getStatus().isEmpty()) {
            medicineOrder.setStatus("Pending");
        }

        if (medicineOrder.getOrderDate() == null || medicineOrder.getOrderDate().isEmpty()) {
            medicineOrder.setOrderDate(java.time.LocalDate.now().toString());
        }

        medicineService.addOrder(medicineOrder);

        System.out.println("MEDICINE ORDER SAVED FOR DOCTOR ID = " + medicineOrder.getDoctorId());

        return "redirect:/doctor/medicine";
    }

    // ================= UPDATE MEDICINE =================

    @PostMapping("/doctor/medicine/update")
    public String updateMedicine(@ModelAttribute MedicineOrder medicineOrder,
                                 HttpSession session) {

        String role = (String) session.getAttribute("role");
        String doctorId = (String) session.getAttribute("staffId");
        String doctorName = (String) session.getAttribute("staffName");

        if (role == null || !role.equalsIgnoreCase("DOCTOR")) {
            return "redirect:/login";
        }

        MedicineOrder existing = medicineService.getOrderById(medicineOrder.getOrderId());

        if (existing == null || !doctorId.equals(existing.getDoctorId())) {
            return "redirect:/doctor/medicine";
        }

        if (!isPatientAssignedToDoctor(medicineOrder.getPatientId(), doctorId)) {
            System.out.println("DOCTOR TRIED TO UPDATE MEDICINE FOR UNASSIGNED PATIENT");
            return "redirect:/doctor/medicine";
        }

        Patient patient = adminService.getPatientById(medicineOrder.getPatientId());

        if (patient != null) {
            medicineOrder.setPatientName(patient.getFullName());
        }

        medicineOrder.setDoctorId(doctorId);
        medicineOrder.setDoctorName(doctorName);
        medicineOrder.setStatus(existing.getStatus());
        medicineOrder.setOrderDate(existing.getOrderDate());

        medicineService.updateOrder(medicineOrder);

        return "redirect:/doctor/medicine";
    }

    // ================= DELETE MEDICINE =================

    @GetMapping("/doctor/medicine/delete/{id}")
    public String deleteMedicine(@PathVariable String id,
                                 HttpSession session) {

        String role = (String) session.getAttribute("role");
        String doctorId = (String) session.getAttribute("staffId");

        if (role == null || !role.equalsIgnoreCase("DOCTOR")) {
            return "redirect:/login";
        }

        MedicineOrder existing = medicineService.getOrderById(id);

        if (existing != null && doctorId.equals(existing.getDoctorId())) {
            medicineService.deleteOrder(id);
        }

        return "redirect:/doctor/medicine";
    }

    // ================= PATIENT MEDICINE PAGE =================

    @GetMapping("/patient/medicine")
    public String patientMedicine(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String patientId = (String) session.getAttribute("patientId");
        String patientName = (String) session.getAttribute("patientName");

        if (role == null || !role.equalsIgnoreCase("PATIENT")) {
            return "redirect:/login";
        }

        model.addAttribute("orders", medicineService.getOrdersByPatientId(patientId));
        model.addAttribute("patientName", patientName);
        model.addAttribute("patientId", patientId);

        return "patient/medicine";
    }

    // ================= HELPER METHODS =================

    private List<Patient> getPatientsForDoctor(String doctorId) {

        List<Appointment> doctorAppointments =
                appointmentService.getAppointmentsByDoctorId(doctorId);

        List<Patient> doctorPatients = new ArrayList<>();
        List<String> addedPatientIds = new ArrayList<>();

        for (Appointment appointment : doctorAppointments) {

            String patientId = appointment.getPatientId();

            if (patientId != null &&
                    !patientId.isEmpty() &&
                    !addedPatientIds.contains(patientId)) {

                Patient patient = adminService.getPatientById(patientId);

                if (patient != null) {
                    doctorPatients.add(patient);
                    addedPatientIds.add(patientId);
                }
            }
        }

        return doctorPatients;
    }

    private boolean isPatientAssignedToDoctor(String patientId, String doctorId) {

        if (patientId == null || doctorId == null) {
            return false;
        }

        List<Appointment> doctorAppointments =
                appointmentService.getAppointmentsByDoctorId(doctorId);

        for (Appointment appointment : doctorAppointments) {

            if (appointment.getPatientId() != null &&
                    appointment.getPatientId().equalsIgnoreCase(patientId)) {
                return true;
            }
        }

        return false;
    }
}
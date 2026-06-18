package com.hospitalmanagementsystem.controllers;

import com.hospitalmanagementsystem.models.Appointment;
import com.hospitalmanagementsystem.models.GeneralMedicalRecord;
import com.hospitalmanagementsystem.models.Patient;
import com.hospitalmanagementsystem.services.AdminManagementService;
import com.hospitalmanagementsystem.services.AppointmentService;
import com.hospitalmanagementsystem.services.MedicalRecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private AdminManagementService adminService;

    @Autowired
    private AppointmentService appointmentService;

    // ================= DOCTOR MEDICAL RECORDS =================

    @GetMapping("/doctor/medical-records")
    public String doctorMedicalRecords(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String doctorId = (String) session.getAttribute("staffId");
        String doctorName = (String) session.getAttribute("staffName");

        if (role == null || !role.equals("DOCTOR")) {
            return "redirect:/login";
        }

        List<Appointment> doctorAppointments =
                appointmentService.getAppointmentsByDoctorId(doctorId);

        List<Patient> doctorPatients = new ArrayList<>();

        for (Appointment appointment : doctorAppointments) {

            String patientId = appointment.getPatientId();

            if (patientId != null && !patientId.isEmpty()) {

                Patient patient = adminService.getPatientById(patientId);

                if (patient != null && !doctorPatients.contains(patient)) {
                    doctorPatients.add(patient);
                }
            }
        }

        model.addAttribute("record", new GeneralMedicalRecord());
        model.addAttribute("records", medicalRecordService.getRecordsByDoctorId(doctorId));
        model.addAttribute("patients", doctorPatients);
        model.addAttribute("doctorId", doctorId);
        model.addAttribute("doctorName", doctorName);

        return "doctor/medical-records";
    }

    @PostMapping("/doctor/medical-records/add")
    public String addMedicalRecord(@ModelAttribute GeneralMedicalRecord record,
                                   HttpSession session) {

        String role = (String) session.getAttribute("role");
        String doctorId = (String) session.getAttribute("staffId");
        String doctorName = (String) session.getAttribute("staffName");

        if (role == null || !role.equals("DOCTOR")) {
            return "redirect:/login";
        }

        boolean allowedPatient = false;

        List<Appointment> doctorAppointments =
                appointmentService.getAppointmentsByDoctorId(doctorId);

        for (Appointment appointment : doctorAppointments) {
            if (appointment.getPatientId() != null &&
                    appointment.getPatientId().equals(record.getPatientId())) {
                allowedPatient = true;
                break;
            }
        }

        if (!allowedPatient) {
            System.out.println("DOCTOR TRIED TO ADD RECORD FOR UNASSIGNED PATIENT");
            return "redirect:/doctor/medical-records";
        }

        Patient patient = adminService.getPatientById(record.getPatientId());

        if (patient != null) {
            record.setPatientName(patient.getFullName());
        }

        record.setDoctorId(doctorId);
        record.setDoctorName(doctorName);
        record.setRecordType("General");
        record.setStatus("ACTIVE");

        medicalRecordService.addRecord(record);

        return "redirect:/doctor/medical-records";
    }

    @PostMapping("/doctor/medical-records/update")
    public String updateMedicalRecord(@ModelAttribute GeneralMedicalRecord record,
                                      HttpSession session) {

        String role = (String) session.getAttribute("role");
        String doctorId = (String) session.getAttribute("staffId");
        String doctorName = (String) session.getAttribute("staffName");

        if (role == null || !role.equals("DOCTOR")) {
            return "redirect:/login";
        }

        GeneralMedicalRecord existing =
                medicalRecordService.getRecordById(record.getRecordId());

        if (existing == null || !doctorId.equals(existing.getDoctorId())) {
            return "redirect:/doctor/medical-records";
        }

        boolean allowedPatient = false;

        List<Appointment> doctorAppointments =
                appointmentService.getAppointmentsByDoctorId(doctorId);

        for (Appointment appointment : doctorAppointments) {
            if (appointment.getPatientId() != null &&
                    appointment.getPatientId().equals(record.getPatientId())) {
                allowedPatient = true;
                break;
            }
        }

        if (!allowedPatient) {
            System.out.println("DOCTOR TRIED TO UPDATE RECORD FOR UNASSIGNED PATIENT");
            return "redirect:/doctor/medical-records";
        }

        Patient patient = adminService.getPatientById(record.getPatientId());

        if (patient != null) {
            record.setPatientName(patient.getFullName());
        }

        record.setDoctorId(doctorId);
        record.setDoctorName(doctorName);
        record.setRecordType(existing.getRecordType());
        record.setStatus(existing.getStatus());

        medicalRecordService.updateRecord(record);

        return "redirect:/doctor/medical-records";
    }

    @GetMapping("/doctor/medical-records/delete/{recordId}")
    public String deleteMedicalRecord(@PathVariable String recordId,
                                      HttpSession session) {

        String role = (String) session.getAttribute("role");
        String doctorId = (String) session.getAttribute("staffId");

        if (role == null || !role.equals("DOCTOR")) {
            return "redirect:/login";
        }

        GeneralMedicalRecord existing =
                medicalRecordService.getRecordById(recordId);

        if (existing != null && doctorId.equals(existing.getDoctorId())) {
            medicalRecordService.deleteRecord(recordId);
        }

        return "redirect:/doctor/medical-records";
    }

    // ================= PATIENT MEDICAL RECORDS =================

    @GetMapping("/patient/medical-records")
    public String patientMedicalRecords(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        String patientId = (String) session.getAttribute("patientId");
        String patientName = (String) session.getAttribute("patientName");

        if (role == null || !role.equalsIgnoreCase("PATIENT")) {
            return "redirect:/login";
        }

        model.addAttribute("records", medicalRecordService.getRecordsByPatientId(patientId));
        model.addAttribute("patientId", patientId);
        model.addAttribute("patientName", patientName);

        return "patient/medical-records";
    }
}
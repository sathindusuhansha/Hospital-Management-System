package com.hospitalmanagementsystem.services;

import com.hospitalmanagementsystem.models.Appointment;
import com.hospitalmanagementsystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // ================= GET ALL =================

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.getAllAppointments();
    }

    // ================= GET BY ID =================

    public Appointment getAppointmentById(String appointmentId) {

        if (appointmentId == null || appointmentId.isEmpty()) {
            return null;
        }

        return appointmentRepository.getAppointmentById(appointmentId);
    }

    // ================= PATIENT APPOINTMENTS =================

    public List<Appointment> getAppointmentsByPatientId(String patientId) {

        if (patientId == null || patientId.isEmpty()) {
            return new ArrayList<>();
        }

        return appointmentRepository.getAllAppointments()
                .stream()
                .filter(a ->
                        a.getPatientId() != null &&
                                a.getPatientId().trim()
                                        .equalsIgnoreCase(patientId.trim()))
                .collect(Collectors.toList());
    }

    // ================= DOCTOR APPOINTMENTS =================

    public List<Appointment> getAppointmentsByDoctorId(String doctorId) {

        if (doctorId == null || doctorId.isEmpty()) {
            return new ArrayList<>();
        }

        return appointmentRepository.getAllAppointments()
                .stream()
                .filter(a ->
                        a.getDoctorId() != null &&
                                a.getDoctorId().trim()
                                        .equalsIgnoreCase(doctorId.trim()))
                .collect(Collectors.toList());
    }

    // ================= ADD =================

    public void addAppointment(Appointment appointment) {

        if (appointment == null) {
            return;
        }

        if (appointment.getAppointmentId() == null ||
                appointment.getAppointmentId().isEmpty()) {

            appointment.setAppointmentId(
                    "APT-" +
                            UUID.randomUUID()
                                    .toString()
                                    .substring(0, 6)
                                    .toUpperCase()
            );
        }

        if (appointment.getStatus() == null ||
                appointment.getStatus().isEmpty()) {

            appointment.setStatus("Pending");
        }

        appointmentRepository.addAppointment(appointment);

        System.out.println(
                "APPOINTMENT ADDED -> "
                        + appointment.getAppointmentId()
                        + " | Patient: "
                        + appointment.getPatientName()
                        + " | Doctor: "
                        + appointment.getDoctorName()
        );
    }

    // ================= UPDATE =================

    public void updateAppointment(Appointment updatedAppointment) {

        if (updatedAppointment == null) {
            return;
        }

        appointmentRepository.updateAppointment(updatedAppointment);

        System.out.println(
                "APPOINTMENT UPDATED -> "
                        + updatedAppointment.getAppointmentId()
        );
    }

    // ================= DELETE =================

    public void deleteAppointment(String appointmentId) {

        appointmentRepository.deleteAppointment(appointmentId);

        System.out.println(
                "APPOINTMENT DELETED -> "
                        + appointmentId
        );
    }

    // ================= CANCEL =================

    public void cancelAppointment(String appointmentId) {

        Appointment appointment =
                appointmentRepository.getAppointmentById(appointmentId);

        if (appointment != null) {

            appointment.setStatus("Cancelled");

            appointmentRepository.updateAppointment(appointment);

            System.out.println(
                    "APPOINTMENT CANCELLED -> "
                            + appointmentId
            );
        }
    }

    // ================= STATUS HELPERS =================

    public void confirmAppointment(String appointmentId) {

        Appointment appointment =
                appointmentRepository.getAppointmentById(appointmentId);

        if (appointment != null) {

            appointment.setStatus("Confirmed");

            appointmentRepository.updateAppointment(appointment);
        }
    }

    public void completeAppointment(String appointmentId) {

        Appointment appointment =
                appointmentRepository.getAppointmentById(appointmentId);

        if (appointment != null) {

            appointment.setStatus("Completed");

            appointmentRepository.updateAppointment(appointment);
        }
    }
    public List<String> getPatientIdsForDoctor(String doctorId) {

        List<String> patientIds = new ArrayList<>();

        for (Appointment appointment : getAllAppointments()) {

            if (appointment.getDoctorId() != null
                    && appointment.getDoctorId().equals(doctorId)) {

                if (!patientIds.contains(appointment.getPatientId())) {
                    patientIds.add(appointment.getPatientId());
                }
            }
        }

        return patientIds;
    }
}
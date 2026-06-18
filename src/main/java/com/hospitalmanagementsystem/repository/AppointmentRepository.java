package com.hospitalmanagementsystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagementsystem.models.Appointment;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
@Repository
public class AppointmentRepository {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    private static final String APPOINTMENT_FILE =
            "src/main/resources/data/appointments.json";

    // ================= READ ALL =================

    public List<Appointment> getAllAppointments() {

        try {
            File file = new File(APPOINTMENT_FILE);

            System.out.println("READING APPOINTMENTS FROM:");
            System.out.println(file.getAbsolutePath());

            if (!file.exists()) {
                File parent = file.getParentFile();
if (parent != null && !parent.exists()) {
    parent.mkdirs();
}

                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<Appointment>());
            }

            if (file.length() == 0) {
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<Appointment>());
            }

            List<Appointment> appointments =
                    objectMapper.readValue(
                            file,
                            new TypeReference<List<Appointment>>() {}
                    );

            System.out.println("TOTAL APPOINTMENTS LOADED = " + appointments.size());

            return appointments;

        } catch (Exception e) {
            System.out.println("ERROR READING APPOINTMENTS JSON");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ================= SAVE ALL =================

    public void saveAppointments(List<Appointment> appointments) {

        try {
            File file = new File(APPOINTMENT_FILE);
            File parent = file.getParentFile();
if (parent != null && !parent.exists()) {
    parent.mkdirs();
}

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, appointments);

            System.out.println("APPOINTMENTS SAVED TO:");
            System.out.println(file.getAbsolutePath());
            System.out.println("APPOINTMENTS SAVED = " + appointments.size());

        } catch (Exception e) {
            System.out.println("ERROR SAVING APPOINTMENTS JSON");
            e.printStackTrace();
        }
    }

    // ================= ADD =================

    public void addAppointment(Appointment appointment) {

        List<Appointment> appointments = getAllAppointments();

        appointments.add(appointment);

        saveAppointments(appointments);

        System.out.println("APPOINTMENT ADDED = " + appointment.getAppointmentId());
        System.out.println("PATIENT ID SAVED = " + appointment.getPatientId());
    }

    // ================= GET BY ID =================

    public Appointment getAppointmentById(String appointmentId) {

        if (appointmentId == null || appointmentId.isEmpty()) {
            return null;
        }

        List<Appointment> appointments = getAllAppointments();

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId() != null &&
                    appointment.getAppointmentId().trim()
                            .equalsIgnoreCase(appointmentId.trim())) {

                return appointment;
            }
        }

        return null;
    }

    // ================= UPDATE =================

    public void updateAppointment(Appointment updatedAppointment) {

        if (updatedAppointment == null ||
                updatedAppointment.getAppointmentId() == null) {
            System.out.println("INVALID APPOINTMENT UPDATE REQUEST");
            return;
        }

        List<Appointment> appointments = getAllAppointments();

        for (int i = 0; i < appointments.size(); i++) {

            Appointment existingAppointment = appointments.get(i);

            if (existingAppointment.getAppointmentId() != null &&
                    existingAppointment.getAppointmentId().trim()
                            .equalsIgnoreCase(updatedAppointment.getAppointmentId().trim())) {

                appointments.set(i, updatedAppointment);

                saveAppointments(appointments);

                System.out.println("APPOINTMENT UPDATED = " + updatedAppointment.getAppointmentId());
                System.out.println("PATIENT ID AFTER UPDATE = " + updatedAppointment.getPatientId());

                return;
            }
        }

        System.out.println("APPOINTMENT NOT FOUND FOR UPDATE = "
                + updatedAppointment.getAppointmentId());
    }

    // ================= DELETE =================

    public void deleteAppointment(String appointmentId) {

        if (appointmentId == null || appointmentId.isEmpty()) {
            System.out.println("INVALID APPOINTMENT DELETE REQUEST");
            return;
        }

        List<Appointment> appointments = getAllAppointments();

        boolean removed =
                appointments.removeIf(appointment ->
                        appointment.getAppointmentId() != null &&
                                appointment.getAppointmentId().trim()
                                        .equalsIgnoreCase(appointmentId.trim())
                );

        if (removed) {
            saveAppointments(appointments);
            System.out.println("APPOINTMENT DELETED = " + appointmentId);
        } else {
            System.out.println("APPOINTMENT NOT FOUND = " + appointmentId);
        }
    }
}
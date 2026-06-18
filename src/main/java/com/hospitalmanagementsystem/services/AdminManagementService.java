package com.hospitalmanagementsystem.services;

import com.hospitalmanagementsystem.models.Patient;
import com.hospitalmanagementsystem.models.Staff;
import com.hospitalmanagementsystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminManagementService {

    @Autowired
    private AdminRepository adminRepository;

    // ================= PATIENT MANAGEMENT =================

    public List<Patient> getAllPatients() {
        return adminRepository.getAllPatients();
    }

    private boolean isSame(String value1, String value2) {
        return value1 != null
                && value2 != null
                && value1.trim().equalsIgnoreCase(value2.trim());
    }

    public String addPatient(Patient patient) {

        List<Patient> patients = adminRepository.getAllPatients();

        for (Patient existingPatient : patients) {

            if (isSame(existingPatient.getUsername(), patient.getUsername())) {
                return "Username already exists. Please use a different username.";
            }

            if (isSame(existingPatient.getPhone(), patient.getPhone())) {
                return "Phone number already exists. Please use a different phone number.";
            }

            if (isSame(existingPatient.getEmail(), patient.getEmail())) {
                return "Email address already exists. Please use a different email address.";
            }
        }

        if (patient.getPatientId() == null || patient.getPatientId().isEmpty()) {
            patient.setPatientId("PAT-" + UUID.randomUUID().toString().substring(0, 6));
        }

        if (patient.getRegistrationDate() == null || patient.getRegistrationDate().isEmpty()) {
            patient.setRegistrationDate(java.time.LocalDate.now().toString());
        }

        if (patient.getIsActive() == null) {
            patient.setIsActive(true);
        }

        adminRepository.addPatient(patient);

        System.out.println("PATIENT ADDED FROM SERVICE: "
                + patient.getFullName()
                + " | ID: "
                + patient.getPatientId());

        return null;
    }

    public Patient getPatientById(String patientId) {
        return adminRepository.getPatientById(patientId);
    }

    public String updatePatient(Patient updatedPatient) {

        Patient existingPatient =
                adminRepository.getPatientById(updatedPatient.getPatientId());

        if (existingPatient == null) {
            return "Patient not found.";
        }

        List<Patient> patients = adminRepository.getAllPatients();

        for (Patient patient : patients) {

            if (!patient.getPatientId().equals(updatedPatient.getPatientId())) {

                if (isSame(patient.getUsername(), updatedPatient.getUsername())) {
                    return "Username already exists. Please use a different username.";
                }

                if (isSame(patient.getPhone(), updatedPatient.getPhone())) {
                    return "Phone number already exists. Please use a different phone number.";
                }

                if (isSame(patient.getEmail(), updatedPatient.getEmail())) {
                    return "Email address already exists. Please use a different email address.";
                }
            }
        }

        if (updatedPatient.getIsActive() == null) {
            updatedPatient.setIsActive(true);
        }

        if (updatedPatient.getRegistrationDate() == null ||
                updatedPatient.getRegistrationDate().isEmpty()) {
            updatedPatient.setRegistrationDate(existingPatient.getRegistrationDate());
        }

        adminRepository.updatePatient(updatedPatient);

        System.out.println("PATIENT UPDATED FROM SERVICE: "
                + updatedPatient.getFullName()
                + " | ID: "
                + updatedPatient.getPatientId());

        return null;
    }

    public void deletePatient(String patientId) {
        adminRepository.deletePatient(patientId);

        System.out.println("PATIENT DELETED FROM SERVICE: " + patientId);
    }

    // ================= STAFF MANAGEMENT =================

    public List<Staff> getAllStaff() {
        return adminRepository.getAllStaff();
    }

    public List<Staff> getAllStaffByRole(String role) {
        return adminRepository.getAllStaffByRole(role);
    }

    public void addStaff(Staff staff) {

        System.out.println("ADD STAFF SERVICE HIT");

        if (staff.getId() == null || staff.getId().isEmpty()) {
            staff.setId("STF-" + UUID.randomUUID().toString().substring(0, 6));
        }

        List<Staff> staffList = adminRepository.getAllStaff();
        staffList.add(staff);

        adminRepository.saveStaff(staffList);
        adminRepository.saveStaffByRole(staff);

        System.out.println("STAFF ADDED: "
                + staff.getFullName()
                + " | Role: "
                + staff.getRole()
                + " | ID: "
                + staff.getId());
    }

    public void deleteStaff(String id) {
        System.out.println("DELETE STAFF SERVICE HIT: " + id);

        Staff staff = getStaffById(id);

        adminRepository.deleteStaff(id);

        if (staff != null) {
            adminRepository.deleteStaffByRole(id, staff.getRole());

            System.out.println("STAFF DELETED: "
                    + staff.getFullName()
                    + " | Role: "
                    + staff.getRole());
        } else {
            System.out.println("WARNING: Could not find staff to delete by role");
        }
    }

    public void updateStaff(Staff staff) {
        System.out.println("UPDATE STAFF SERVICE HIT: " + staff.getId());

        Staff originalStaff = getStaffById(staff.getId());
        String oldRole = originalStaff != null ? originalStaff.getRole() : staff.getRole();

        adminRepository.updateStaff(staff);
        adminRepository.updateStaffByRole(staff, oldRole);

        System.out.println("STAFF UPDATED: "
                + staff.getFullName()
                + " | Role: "
                + staff.getRole());
    }

    public Staff getStaffById(String id) {
        return adminRepository.getAllStaff()
                .stream()
                .filter(staff -> staff.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
package com.hospitalmanagementsystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagementsystem.models.Patient;
import com.hospitalmanagementsystem.models.Staff;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASE_PATH =
            "src/main/resources/data/";
    private static final String PATIENT_FILE = BASE_PATH + "patients.json";
    private static final String STAFF_FILE = BASE_PATH + "staff.json";
    private static final String DOCTOR_FILE = BASE_PATH + "doctor.json";
    private static final String NURSE_FILE = BASE_PATH + "nurse.json";
    private static final String RECEPTIONIST_FILE = BASE_PATH + "receptionist.json";
    private static final String PHARMACIST_FILE = BASE_PATH + "pharmacist.json";

    // ================= PATIENT FILE OPERATIONS =================

    public List<Patient> getAllPatients() {

        try {

            File file = new File(PATIENT_FILE);

            System.out.println("READING FROM:");
            System.out.println(file.getAbsolutePath());

            List<Patient> patients =
                    objectMapper.readValue(
                            file,
                            new TypeReference<List<Patient>>() {}
                    );

            System.out.println("PATIENTS FOUND = "
                    + patients.size());

            return patients;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public void savePatients(List<Patient> patients) {
        try {
            File file = new File(PATIENT_FILE);
            File parent = file.getParentFile();

            if (parent != null) {
                parent.mkdirs();
            }

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, patients);

            System.out.println("PATIENTS SAVED TO: " + file.getAbsolutePath());
            System.out.println("TOTAL PATIENTS SAVED: " + patients.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPatient(Patient patient) {
        List<Patient> patients = getAllPatients();

        boolean exists = false;

        for (Patient existingPatient : patients) {
            if (existingPatient.getPatientId().equals(patient.getPatientId())) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            patients.add(patient);
            savePatients(patients);
            System.out.println("NEW PATIENT ADDED: " + patient.getFullName());
        } else {
            System.out.println("PATIENT ID ALREADY EXISTS: " + patient.getPatientId());
        }
    }

    public Patient getPatientById(String patientId) {
        List<Patient> patients = getAllPatients();

        for (Patient patient : patients) {
            if (patient.getPatientId().equals(patientId)) {
                return patient;
            }
        }

        return null;
    }

    public void updatePatient(Patient updatedPatient) {
        List<Patient> patients = getAllPatients();

        for (int i = 0; i < patients.size(); i++) {
            Patient existingPatient = patients.get(i);

            if (existingPatient.getPatientId().equals(updatedPatient.getPatientId())) {
                patients.set(i, updatedPatient);
                savePatients(patients);
                System.out.println("PATIENT UPDATED: " + updatedPatient.getFullName());
                return;
            }
        }

        System.out.println("PATIENT NOT FOUND FOR UPDATE: " + updatedPatient.getPatientId());
    }

    public void deletePatient(String patientId) {
        List<Patient> patients = getAllPatients();

        boolean removed = patients.removeIf(patient ->
                patient.getPatientId().equals(patientId)
        );

        if (removed) {
            savePatients(patients);
            System.out.println("PATIENT DELETED: " + patientId);
        } else {
            System.out.println("PATIENT NOT FOUND FOR DELETE: " + patientId);
        }
    }


    public List<Staff> getAllStaff() {
        try {
            File file = new File(STAFF_FILE);

            System.out.println("READING STAFF FROM: " + file.getAbsolutePath());
            if (!file.exists()) {

                File parent = file.getParentFile();

                if (parent != null) {
                    parent.mkdirs();
                }

                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<Staff>());
            }



            if (file.length() == 0) {
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<Staff>());
            }

            return objectMapper.readValue(file, new TypeReference<List<Staff>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveStaff(List<Staff> staffList) {
        try {
            File file = new File(STAFF_FILE);
            File parent = file.getParentFile();

            if (parent != null) {
                parent.mkdirs();
            }

            System.out.println("SAVE STAFF REPOSITORY HIT");
            System.out.println("SAVING STAFF TO: " + file.getAbsolutePath());
            System.out.println("STAFF COUNT: " + staffList.size());

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, staffList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteStaff(String id) {
        List<Staff> staffList = getAllStaff();
        staffList.removeIf(staff -> staff.getId().equals(id));
        saveStaff(staffList);
    }

    public void updateStaff(Staff updatedStaff) {
        List<Staff> staffList = getAllStaff();

        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).getId().equals(updatedStaff.getId())) {
                staffList.set(i, updatedStaff);
                break;
            }
        }

        saveStaff(staffList);
    }

    // ============== ROLE-BASED STAFF FILE OPERATIONS ==============

    private String getRoleSpecificFile(String role) {
        switch (role.toUpperCase()) {
            case "DOCTOR":
                return DOCTOR_FILE;
            case "NURSE":
                return NURSE_FILE;
            case "RECEPTIONIST":
                return RECEPTIONIST_FILE;
            case "PHARMACIST":
                return PHARMACIST_FILE;
            default:
                return null;
        }
    }

    public void saveStaffByRole(Staff staff) {
        // Only save if the staff's role is valid
        if (staff == null || staff.getRole() == null) {
            System.out.println("ERROR: Cannot save staff with null role");
            return;
        }

        String roleFile = getRoleSpecificFile(staff.getRole());
        if (roleFile == null) {
            System.out.println("ERROR: No file defined for role: " + staff.getRole());
            return;
        }

        try {
            File file = new File(roleFile);
            File parent = file.getParentFile();
if (parent != null && !parent.exists()) {
    parent.mkdirs();
}

            List<Staff> roleStaffList = getAllStaffByRole(staff.getRole());
            
            // Check if staff already exists and update, otherwise add
            boolean exists = false;
            for (int i = 0; i < roleStaffList.size(); i++) {
                if (roleStaffList.get(i).getId().equals(staff.getId())) {
                    roleStaffList.set(i, staff);
                    exists = true;
                    break;
                }
            }
            
            if (!exists) {
                roleStaffList.add(staff);
            }

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, roleStaffList);

            System.out.println("SAVED TO ROLE FILE: " + roleFile + " | Staff: " + staff.getFullName() + " | Role: " + staff.getRole());

        } catch (Exception e) {
            System.err.println("ERROR saving staff to role file: " + roleFile);
            e.printStackTrace();
        }
    }

    public List<Staff> getAllStaffByRole(String role) {
        try {
            String roleFile = getRoleSpecificFile(role);
            if (roleFile == null) return new ArrayList<>();

            File file = new File(roleFile);

            if (!file.exists()) {
                File parent = file.getParentFile();
if (parent != null && !parent.exists()) {
    parent.mkdirs();
}
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<Staff>());
            }

            if (file.length() == 0) {
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file, new ArrayList<Staff>());
            }

            return objectMapper.readValue(file, new TypeReference<List<Staff>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void deleteStaffByRole(String id, String role) {
        if (id == null || role == null) {
            System.out.println("ERROR: Cannot delete staff with null id or role");
            return;
        }

        String roleFile = getRoleSpecificFile(role);
        if (roleFile == null) {
            System.out.println("ERROR: No file defined for role: " + role);
            return;
        }

        try {
            List<Staff> roleStaffList = getAllStaffByRole(role);
            roleStaffList.removeIf(staff -> staff.getId().equals(id));
            
            File file = new File(roleFile);
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, roleStaffList);

            System.out.println("DELETED FROM ROLE FILE: " + roleFile + " | ID: " + id);

        } catch (Exception e) {
            System.err.println("ERROR deleting staff from role file: " + roleFile);
            e.printStackTrace();
        }
    }

    public void updateStaffByRole(Staff updatedStaff, String oldRole) {
        if (updatedStaff == null || updatedStaff.getRole() == null || oldRole == null) {
            System.out.println("ERROR: Cannot update staff with null values");
            return;
        }

        // If role changed, remove from old role file and add to new role file
        if (!oldRole.equalsIgnoreCase(updatedStaff.getRole())) {
            System.out.println("ROLE CHANGED: " + oldRole + " -> " + updatedStaff.getRole());
            deleteStaffByRole(updatedStaff.getId(), oldRole);
            saveStaffByRole(updatedStaff);
        } else {
            // Same role, just update
            System.out.println("ROLE UNCHANGED: " + oldRole + " | Updating staff in role file");
            saveStaffByRole(updatedStaff);
        }
    }

    // ============== DATA MIGRATION ==============
    
    /**
     * Migrates all staff from staff.json to their respective role-specific files.
     * This should be called once during application startup to ensure data consistency.
     */
    public void migrateStaffToRoleFiles() {
        try {
            System.out.println("STARTING STAFF DATA MIGRATION...");
            
            List<Staff> allStaff = getAllStaff();
            
            // Clear role-specific files first
            clearRoleFiles();
            
            // Populate each role file with staff matching that role
            for (Staff staff : allStaff) {
                if (staff.getRole() != null && !staff.getRole().isEmpty()) {
                    saveStaffByRole(staff);
                }
            }
            
            System.out.println("STAFF DATA MIGRATION COMPLETED. Total staff migrated: " + allStaff.size());
            
        } catch (Exception e) {
            System.err.println("ERROR during staff data migration");
            e.printStackTrace();
        }
    }

    /**
     * Clears all role-specific JSON files.
     */
    private void clearRoleFiles() {
        try {
            String[] roleFiles = {DOCTOR_FILE, NURSE_FILE, RECEPTIONIST_FILE, PHARMACIST_FILE};
            
            for (String roleFile : roleFiles) {
                File file = new File(roleFile);
                if (file.exists()) {
                    objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValue(file, new ArrayList<Staff>());
                    System.out.println("CLEARED ROLE FILE: " + roleFile);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR clearing role files");
            e.printStackTrace();
        }
    }
}